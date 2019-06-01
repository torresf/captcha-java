/**
 * @authors Florian Torres - Stella Poulain
 * @date : 7 mai 2019
 * @file : Images.java
 * @package : fr.upem.captcha.images
 */

package fr.upem.captcha.images;

import java.net.URL;
import java.util.List;


/**	
 * Images interface is used to manage photos of the captcha
 */
public interface Images {

	/**
	 * @param
	 * @return List<URL>
	 * This method is getting all pictures found in the sub directories and filling the photos List<URL>
	 */
	public List<URL> getPhotos();
	
	/**
	 * @param int
	 * @return List<URL>
	 */
	public List<URL> getRandomPhotosURL(int value);
	
	/**
	 * @param
	 * @return URL
	 * Returns a random photo URL from the current category
	 */
	public List<URL> getRandomPhotoURL();
	
	/**
	 * @param URL
	 * @return boolean that permits to check if the url belongs to the current category
	 */
	public boolean isPhotoCorrect(URL url);
	
}
