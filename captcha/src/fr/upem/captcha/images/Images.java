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
	
	public List<URL> getPhotos();

	public List<URL> getRandomPhotosURL(int value);

	public List<URL> getRandomPhotoURL();
	
	public boolean isPhotoCorrect(URL url);
	
}
