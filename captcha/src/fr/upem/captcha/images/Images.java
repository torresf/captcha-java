/**
 * @authors Florian Torres - Stella Poulain
 * @date : 7 mai 2019
 * @file : Images.java
 * @package : fr.upem.captcha.images
 */

package fr.upem.captcha.images;

import java.net.URL;
import java.util.List;


public interface Images {

	/** 
	 * Méthode qui retourne une liste d'URL (du chemin) des photos de toutes les classes
	 * @return List<URL>
	 */
	public List<URL> getPhotos();
	
	/**
	 * 
	 *
	 */
	public List<URL> getRandomPhotosURL(int value);

	public List<URL> getRandomPhotoURL();
	
	public boolean isPhotoCorrect(URL url);
	
}
