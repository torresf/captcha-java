/**
 * @authors Florian Torres - Stella Poulain
 * @date : 7 mai 2019
 * @file : Category.java
 * @package : fr.upem.captcha.images
 */

package fr.upem.captcha.images;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**	
 * Abstract class that is the parent 
 * category of all others categories
 */
abstract public class Category implements Images {
	private List<URL> photos = new ArrayList<URL>();

	public Category() {
		super();
		this.photos = new ArrayList<URL>();
	}

	public Path getPath() {
		StringBuilder packageName = new StringBuilder();
		packageName.append(System.getProperty("user.dir"));
		packageName.append("/");

		// If launched using eclipse (not added in .jar)
		if (new File (packageName.toString() + "/bin").exists()) {
			packageName.append("bin/");
		}

		packageName.append(this.getClass().getPackage().getName().replace(".", "/"));

		return Paths.get(packageName.toString());
	}
	
	@Override
	public List<URL> getPhotos() {
		photos.clear();

		ArrayList<Category> subCategories = getCategories();
		
		if (subCategories.isEmpty()) {
			List<String> images = null;
			Path path = this.getPath();
			try {
				images = Files.walk(path, 1)
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
			return photos;
		} else {
			for (Category category : subCategories) {
				for (URL photoURL : category.getPhotos()) {
					photos.add(photoURL);
				}
			}
		}
		
//		Path currentRelativePath = this.getPath();
//
//		// Getting the directories for the main categories
//		List<String> directories = null;
//
//		try {
//			directories = Files.walk(currentRelativePath, 1)
//			        .map(Path::getFileName)
//			        .map(Path::toString)
//			        .filter(n -> !n.contains("."))
//			        .collect(Collectors.toList());
//			directories.remove(0); // Remove the current directory
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		// For each categogy, we get the sub-directory
//		for (String directory : directories) {
//			Path childPath = Paths.get(currentRelativePath + "/" + directory);
//			// Getting images
//			List<String> images = null;
//			try {
//				images = Files.walk(childPath, 2)
//				        .map(Path::getFileName)
//				        .map(Path::toString)
//				        .filter(n -> n.contains(".jpg") || n.contains(".png"))
//				        .collect(Collectors.toList());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			for (String image : images) {
//				photos.add(this.getClass().getResource(directory + "/" + image));
//			}
//		}
//
//		// S'il n'y a pas de sous dossier, on récupère les images directement dans le dossier actuel
//		if (directories.isEmpty()) {
//			List<String> images = null;
//			try {
//				images = Files.walk(currentRelativePath, 1)
//				        .map(Path::getFileName)
//				        .map(Path::toString)
//				        .filter(n -> n.contains(".jpg") || n.contains(".jpeg") || n.contains(".png"))
//				        .collect(Collectors.toList());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			for (String image : images) {
//				photos.add(this.getClass().getResource(image));
//			}
//		}

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
	
	public ArrayList<Category> getCategories() {
		ArrayList<Category> categories = new ArrayList<Category>();
		Path path = this.getPath();
		List<String> subDirectories = null; // List of subCategories names
		try {
			subDirectories = Files.walk(path, 1)
				.skip(1) // Skipping current directory
		        .map(Path::getFileName) // Get filename
		        .map(Path::toString) // Set filename to string
		        .filter(n -> !n.contains(".")) // Only if doesn't have extension
		        .collect(Collectors.toList()); // Add to list
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (subDirectories.isEmpty()) {
			return categories;
		}
		
		List<String> classNames = null; // List of class names
		for (String dirName : subDirectories) {
			Path subDirectoryPath = Paths.get(path + "/" + dirName);
			String subPackageName = this.getClass().getPackage().getName() + "." + subDirectoryPath.getFileName();
			try {
				classNames = Files.walk(Paths.get(path + "/" + dirName), 1)
				        .map(Path::getFileName) // Get filename
				        .map(Path::toString) // Set filename to string
				        .filter(n -> n.contains(".class")) // Only if doesn't have extension
				        .map(n -> subPackageName + '.' + n.replace(".class", ""))
				        .collect(Collectors.toList()); // Add to list
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			for (String className : classNames) {
				Object object = null;
				try {
					object = Class.forName(className).getDeclaredConstructor().newInstance();
				}
				catch (ClassNotFoundException e) // Class doesn't exist
			    {
					e.printStackTrace();
			    }
			    catch (InstantiationException e) // Class is abstract or interface or has no specified constructor
			    {
			    	e.printStackTrace();
			    }
			    catch (IllegalAccessException e) // Class not accessible
			    {
			    	e.printStackTrace();
			    }
				catch (InvocationTargetException e) // Failure with called constructor
			    {
			    	e.printStackTrace();
			    }
				catch (NoSuchMethodException e) // Class has no declared constructor
			    {
			    	e.printStackTrace();
			    }
				categories.add((Category)object); // Adding object to subCategories
			}
		}
		return categories;
	}
}
