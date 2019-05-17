/**
 * @authors : David NASR - Joris OEUVRAY
 * @date : 15 avr. 2018
 * @file : Grid.java
 * @package : fr.upem.captcha.grid
 */

package fr.upem.captcha.grid;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import fr.upem.captcha.images.Images;
import fr.upem.captcha.images.boisson.Boisson;
import fr.upem.captcha.images.poulet.Poulet;

/**
 * Grid est une classe qui va contenir les informations � afficher dans le Captcha :
 * 	- correctCategory : La bonne categorie
 * 	- correctImages : Les images de la cat�gorie (de 1 � 4 images)
 * 	- allImages : correctImages + des images qui ne font pas partie de la cat�gorie
 */
public class Grid {
	private Images correctCategory;
	private List<URL> correctImages = new ArrayList<URL>();
	private ArrayList<URL> allImages = new ArrayList<URL>();
	private ArrayList<String> categorieNames = new ArrayList<String>();
	
	/**
	 * Constructeur par d�faut de Grid.
	 * Fais appel � ses m�thodes pour initialiser ses variables (cat�gorie et photos al�atoires)
	 */
	public Grid() {
		// On fixe la premiere categorie � images, le point de d�part
		this.categorieNames.add("images");
		
		this.buildGrid();
	}
	
	public void buildGrid() {
		ArrayList<Images> categories = null;
		try {
			categories = getCategories(categorieNames);	// On r�cup�re les diff�rentes classes
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		try {
			correctCategory = getRandomCategory(categories, categorieNames);	// On r�cup�re une classe au hasard
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(4)+1;	// Renvoie un nombre entre 1 et 4 (le nombre de bonnes images possibles)
		
		correctImages = correctCategory.getRandomPhotosURL(randomNumber);	// Renvoie un nombre al�atoire d'images de la cat�gorie
		List<URL> wrongImages = getOtherCategoryPhotos(categories, correctCategory, randomNumber);

		allImages.addAll(correctImages);
		allImages.addAll(wrongImages);
		Collections.shuffle(allImages);
	}
	
	public Images getCategory() {
		return correctCategory;
	}
	public List<URL> getCorrectImages() {
		return correctImages;
	}
	public ArrayList<URL> getImages() {
		return allImages;
	}
	public String getCategoryName() {
		StringBuilder categoryName = new StringBuilder();
		for(int i = 1; i < categorieNames.size(); i++) {
			categoryName.append(" ").append(categorieNames.get(i));
		}
		return categoryName.toString();
	}
	
	/**
	 * Renvoie un String contenant le chemin vers la cat�gorie actuelle
	 * 
	 * @param ArrayList<String> - La liste des categories visit�es
	 * @return String - Le chemin vers la cat�gorie actuelle
	 */
	private static String getCurrentPath(ArrayList<String> categorieNames) {
		//StringBuilder fullPath = new StringBuilder("src/fr/upem/captcha");	// Chemin pour ex�cuter � partir d'Eclipse
		StringBuilder fullPath = new StringBuilder("src/fr/upem/captcha");	// Chemin pour ex�cuter en ligne de commandes depuis le dossier bin
		for(String categorie: categorieNames) {
			fullPath.append("/").append(categorie);
		}
		return fullPath.toString();
	}
	
	/**
	 * Renvoie un String contenant le chemin vers la cat�gorie actuelle
	 * 
	 * @param ArrayList<String> - La liste des categories visit�es
	 * @return String - Le chemin vers la cat�gorie actuelle
	 */
	private static ArrayList<String> getClassPath(ArrayList<String> categorieNames, List<String> categories) {
		StringBuilder fullPath = new StringBuilder("fr.upem.captcha");
		for(String categorie: categorieNames) {
			fullPath.append(".").append(categorie);
		}
		ArrayList<String> classPath = new ArrayList<String>();
		for(String categorie: categories) {
			String className = categorie.substring(0, 1).toUpperCase() + categorie.substring(1);
			String fullName = fullPath+"."+categorie+"."+className;
			classPath.add(fullName);
		}
		return classPath;
	}

	/**
	 * Retourne la liste des cat�gories existantes
	 * 
	 * @return ArrayList<Images> - La liste des classes existantes impl�mentant l'interface Images
	 */
	public static ArrayList<Images> getCategories(ArrayList<String> categorieNames) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>(); 		// une liste de toutes nos classes
		
		// On r�cup�re le dossier dans lequel on se trouve actuellement
		String currentPath = getCurrentPath(categorieNames);
		Path currentRelativePath = Paths.get(currentPath);
		
		// On r�cup�re les sous dossiers (c'est � dire les categories)
		List<String> directories = null;
		try {
			directories = Files.walk(currentRelativePath, 1)
			        .map(Path::getFileName)
			        .map(Path::toString)
			        .filter(n -> !n.contains("."))
			        .collect(Collectors.toList());
			directories.remove(0);	// On enl�ve le 0 car c'est le nom du dossier courant
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// On r�cup�re le nom des classes trouv�es et on les rajoute � la liste
		ArrayList<String> classPath = getClassPath(categorieNames, directories);
		for (String s : classPath) {
			classes.add(Class.forName(s));
		}
		
		// On instancie chaque classe en objet de type Images qu'on rajoute dans notre liste
		ArrayList<Images> categories = new ArrayList<Images>();
		for (Class clazz : classes) {
			categories.add(instantiateImages(clazz));
		}
		
		return categories;
	}

	/**
	 * Retourne une cat�gorie al�atoire
	 * 
	 * @param categories - La liste des categories existantes, � r�cup�rer avec getCategories()
	 * @return Images - Un objet de la cat�gorie qui impl�mente l'interface Images
	 */
	public static Images getRandomCategory(ArrayList<Images> categories, ArrayList<String> categorieNames) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Random randomGenerator = new Random();
		Images category = categories.get(randomGenerator.nextInt(categories.size()));	// on choisit al�atoirement une classe dans la liste

		// On met la 1ere lettre en minuscule et on rajoute la categorie dans la liste des categories
		String categoryName = category.getClass().getSimpleName();
		String name = categoryName.substring(0,1).toLowerCase() + categoryName.substring(1);
		categorieNames.add(name);	// on rajoute la nouvelle categorie dans la liste
		return category;	// on renvoit la categorie
	}
	
	/**
	 * Retourne une cat�gorie al�atoire
	 * 
	 * @param category - Instancie un objet de type Images � partir de la class de category
	 * @return Images - L'objet de type Images instanci�
	 */
	public static Images instantiateImages(Class<?> category) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> cls = Class.forName(category.getTypeName());	// On r�cup�re le type de la classe
		Object clsInstance = cls.newInstance();	// On instancie un objet du type de la classe
		return (Images)clsInstance;	// On le cast en Images pour pouvoir utiliser les m�thodes de l'interface
	}
	
	/**
	 * Retourne une liste d'url d'images qui ne font pas partie de la bonne cat�gorie
	 * 
	 * @param 
	 * 		categories - La liste de toutes les cat�gories
	 * 		category - La bonne cat�gorie
	 * 		randomNumber - Le nombre d'images correctes (on fera 9 - ce nombre pour connaitre combien d'images il nous manque)
	 * @return List<URL> - La liste d'url des mauvaises images
	 */
	private static List<URL> getOtherCategoryPhotos(ArrayList<Images> categories, Images category, int randomNumber) {
		categories.remove(category);	// on enl�ve la bonne cat�gorie de la liste
		List<URL> wrongImages = new ArrayList<URL>();	// on va stocker toutes les images dans une liste
		
		Random randomGenerator = new Random();
		for (int i = randomNumber; i < 9; i++) {
			URL url;
			do {
				int rand = randomGenerator.nextInt(categories.size());	// On choisit une categorie al�atoire
				url = categories.get(rand).getRandomPhotoURL();
			} while (wrongImages.contains(url));	// on v�rifie qu'on n'a pas d�j� s�lectionn� cette image, sinon on en choisit une autre

			wrongImages.add(url);	// On rajoute une photo al�atoire de cette cat�gorie
		}
		
		return wrongImages;
	}
	
	/**
	 * Restart, clear the image Array and build a new Grid from the previous category
	 */
	public void restart() {
		allImages.clear();
		this.buildGrid();
	}
	
	/**
	 * Tell if the max difficulty is reached or not
	 * 
	 * @return boolean - true if difficulty max is reached (2)
	 */
	public boolean maxDifficultyReached() {
		if(categorieNames.size() > 2) return true;
		else return false;
	}
}
