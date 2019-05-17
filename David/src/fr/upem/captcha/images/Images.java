/**
 * @authors : David NASR - Joris OEUVRAY
 * @date : 13 avr. 2018
 * @file : Images.java
 * @package : fr.upem.captcha.images
 */

package fr.upem.captcha.images;

import java.net.URL;
import java.util.List;

/**
 * Images est une interface qui permet de récupérer les photos, d'en piocher aléatoirement et de vérifier si une photo est correcte
 */
public interface Images {
	
	/**
	 * Retourne une liste d'URL contenant l'URL de toutes les photos de la classe
	 * </br>S'il n'y en a pas, retourne une liste vide
	 * 
	 * @return List<URL> - Retourne une liste d'URL
	 */
	public List<URL> getPhotos();
	
	/**
	 * Retourne une liste d'URL aléatoire de photos de la classe
	 * 
	 * @param value - Indique le nombre d'URL que l'on souhaite obtenir
	 * @return List<URL> - Retourne une liste d'URL
	 * @throws IllegalArgumentException - Si jamais on demande plus de photos qu'il y en a
	 */
	public List<URL> getRandomPhotosURL(int value) throws IllegalArgumentException;
	
	/**
	 * Retourne une URL aléatoire d'une photo de la classe
	 * </br>Fais appel à la fonction getRandomPhotos avec un paramétre de 1
	 * 
	 * 
	 * @return URL - Retourne une URL
	 * @throws IllegalArgumentException - Si jamais il n'y a aucun fichier
	 */
	public URL getRandomPhotoURL();
	
	/**
	 * Indique si une photo appartient ou non à la classe
	 * 
	 * @param url - Indique l'URL de la photo que l'on souhaite vérifiée
	 * @return boolean - Retourne un boolean
	 */
	public boolean isPhotoCorrect(URL url);
	
	/**
	 * Renvoie une sous-catégorie
	 * 
	 * @return Images - La nouvelle classe
	 */
	//public Images getSubCategory();
}
