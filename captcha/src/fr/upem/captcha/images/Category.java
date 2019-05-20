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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


abstract public class Category implements Images {
	private List<URL> photos = new ArrayList<URL>();
	
	public Category() {
		super();
		this.photos = new ArrayList<URL>();
		this.getPhotos();
	}
	
	@Override
	public List<URL> getPhotos() {
		photos.clear();
		
		String packageName = "src/"+this.getClass().getPackage().getName();
		String currentPath = packageName.replace('.', '/');
		Path currentRelativePath = Paths.get(currentPath);
		
		// Getting the directories for the main categories
		List<String> directories = null;

		try {
			directories = Files.walk(currentRelativePath, 1)
			        .map(Path::getFileName)
			        .map(Path::toString)
			        .filter(n -> !n.contains("."))
			        .collect(Collectors.toList());
			directories.remove(0); // Remove the current directory
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// For each categogy, we get the sub-directory
		for (String directory : directories) {
			Path childPath = Paths.get(currentPath + "/" + directory);
			// Getting images
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

		// S'il n'y a pas de sous dossier, on récupère les images directement dans le dossier actuel
		if (directories.isEmpty()) {
			List<String> images = null;
			try {
				images = Files.walk(currentRelativePath, 1)
				        .map(Path::getFileName)
				        .map(Path::toString)
				        .filter(n -> n.contains(".jpg") || n.contains(".jpeg") || n.contains(".png"))
				        .collect(Collectors.toList());
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String image : images) {
				photos.add(this.getClass().getResource(image));
			}
		}
		
		return photos;
	}

	@Override
	public List<URL> getRandomPhotosURL(int value) {
		if (this.photos.isEmpty()) {
			getPhotos();
		}
		
		List<URL> allImages = new ArrayList<URL>(this.photos);
		if (allImages.size() == 0) {
			throw new IllegalArgumentException("The category " + this.toString() + " has no pictures.");
		} else if (value > allImages.size()) {
			throw new IllegalArgumentException("The value should be less than or equal to " + allImages.size());
		}
		Collections.shuffle(allImages);
		return allImages
				.stream()
				.limit(value)
				.collect(Collectors.toList());
	}

	@Override
	public List<URL> getRandomPhotoURL() {
		return getRandomPhotosURL(1);
	}

	@Override
	public boolean isPhotoCorrect(URL url) {
		String currentCategory = this.getClass().getPackage().getName().replace(".", "/");
		return url.toString().contains(currentCategory);
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
