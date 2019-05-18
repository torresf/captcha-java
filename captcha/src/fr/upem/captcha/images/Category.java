/**
 * @authors Florian Torres - Stella Poulain
 * @date : 7 mai 2019
 * @file : Category.java
 * @package : fr.upem.captcha.images
 */

package fr.upem.captcha.images;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

abstract public class Category implements Images {
	List<URL> photos = new ArrayList<URL>();
	
	public Category() {
		super();
		this.getPhotos();
	}

	@Override
	public List<URL> getPhotos() {
		System.out.println("getPhotos");
		List<URL> photos = new ArrayList<URL>();
		
		String packageName = "src/"+this.getClass().getPackage().getName();
		String currentPath = packageName.replace('.', '/');
		Path currentRelativePath = Paths.get(currentPath);
		
		// On récupère les dossiers de catégories
		List<String> directories = null;

		try {
			directories = Files.walk(currentRelativePath, 1)
			        .map(Path::getFileName)
			        .map(Path::toString)
			        .filter(n -> !n.contains("."))
			        .collect(Collectors.toList());
			directories.remove(0); // On enlève le 0 car c'est le nom du dossier courant
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		// Pour chaque catégorie, on récupère les sous direcotry
		for (String directory : directories) {
			Path childPath = Paths.get(currentPath + "/" + directory);
			// On récupère les images
			List<String> images = null;
			try {
				images = Files.walk(childPath, 2)
				        .map(Path::getFileName)
				        .map(Path::toString)
				        .filter(n -> n.contains(".jpg") || n.contains(".png"))
				        .collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) {
				photos.add(this.getClass().getResource(directory + "/" + image));
			}
		}

		// S'il n'y a pas de sous dossier, on rÃ©cupÃ¨re les images directement dans le dossier actuel
		if (directories.isEmpty()) {
			List<String> images = null;
			try {
				images = Files.walk(currentRelativePath, 1)
				        .map(Path::getFileName)
				        .map(Path::toString)
				        .filter(n -> n.contains(".jpg") || n.contains(".png"))
				        .collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) {
				photos.add(this.getClass().getResource(image));
			}
		}

		this.photos = photos;
		System.out.println(photos);
		return photos;
	}

	@Override
	public List<URL> getRandomPhotosURL(int value) {
		if (this.photos.isEmpty()) getPhotos();
		List<URL> photos = this.photos;
		List<URL> randomPhotos = new ArrayList<URL>();
		Random randomGenerator = new Random();
		List<Integer> randomNumbers = new ArrayList<Integer>();
		
		if (photos.size() == 0) {
			throw new IllegalArgumentException("Il n'y a aucune photo pour cette classe");
		}
		else if (value > photos.size()) {
			throw new IllegalArgumentException("La valeur doit Ãªtre infÃ©rieure Ã  " + photos.size());
		}
		
		int randomNumber;
		for (int i = 0; i < value; i++) {
			do {
				randomNumber = randomGenerator.nextInt(photos.size());
			} while(randomNumbers.contains(randomNumber));
			
			randomNumbers.add(randomNumber);
			randomPhotos.add(photos.get(randomNumber));
		}
		
		return randomPhotos;
	}

	@Override
	public List<URL> getRandomPhotoURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPhotoCorrect(URL url) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
}
