/**
 * @authors Florian Torres - Stella Poulain
 * @date : 17 mai 2019
 * @file : LogicEngine.java
 * @package : fr.upem.captcha.images.logicengine
 */

package fr.upem.captcha.logicengine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fr.upem.captcha.images.Category;
import fr.upem.captcha.images.animals.Animal;
import fr.upem.captcha.images.animals.cats.Cat;
import fr.upem.captcha.images.animals.dogs.Dog;
import fr.upem.captcha.images.instruments.Instrument;
import fr.upem.captcha.images.instruments.flutes.Flute;


public class LogicEngine {
	private ArrayList<Category> categories; // All the categories
	private Category selectedCategory; // Selected category to click on
	private int difficultyLevel; // Difficulty level of the captcha
	private List<URL> gridImages; // 9 images of the grid
	private List<URL> correctImages; // Correct image of the grid
	private int numberOfCorrectImages; // Number of correct images to choose
	
	/**
	 * @param
	 * LogicEgineConstructor
	 * Set difficultyLevel at 2
	 * Create the ArrayList Category
	 */
	public LogicEngine() {
		difficultyLevel = 2;
		categories = new ArrayList<Category>();
		categories = getCategories();
		gridImages = new ArrayList<URL>();
		selectRandomCategory();
		setGridImages();
	}
	
	/**
	 * @param
	 * Initializes and returns the ArrayList of Categories
	 * @return ArrayList<Category>
	 * The arraysList depends on the value of difficultyLevel
	 *
	 */
	public ArrayList<Category> getCategories() {
		switch (difficultyLevel) {
			case 1:
				categories.add(new Animal());
				categories.add(new Instrument());
				break;
			case 2:
				categories.add(new Dog());
				categories.add(new Cat());
				categories.add(new Flute());
				break;
	
			default:
				categories.add(new Animal());
				categories.add(new Instrument());
				break;
		}
		return categories;
	}
	
	/**
	 * @param
	 * It increases the difficultyLevel when the user doesn't perfom the previous level
	 * 
	 */
	public void increaseDifficultyLevel() {
		difficultyLevel++;
	}
	
	/**
	 * @param
	 * @return a String which is the selected category from the first member of the categories ArrayList
	 * set the selectedCategory as the first member of the ArrayList
	 */
	public void selectRandomCategory() {
		Collections.shuffle(categories);
		selectedCategory = categories.get(0);
	}
	
	/**
	 * @param
	 * @return String which is the selectedCategory
	 *
	 */
	public String getSelectedCategory() {
		return selectedCategory.toString();
	}
	
	/**
	 * @param
	 * @return void
	 *	Fills the grid with the correct and incorrect categories
	 */
	public void setGridImages() {
		gridImages.clear();
		
		// Set numberOfCorrectImages randomly between 1 and 4
		this.numberOfCorrectImages = (int)((Math.random() * 4) + 1);
		
		// Get n images form the selected category (with n = numberOfCorrectImages)
		List<URL> allCorrectImages = selectedCategory.getPhotos();
		Collections.shuffle(allCorrectImages);
		correctImages = allCorrectImages
				.stream()
				.limit(numberOfCorrectImages)
				.collect(Collectors.toList());
		
		// Add the correct images to the grid
		for (URL image : correctImages) {
			gridImages.add(image);
		}
		
		// Fill the grid with images from other categories
		List<URL> allIncorrectImages = new ArrayList<URL>();
		List<URL> incorrectImages = new ArrayList<URL>();
		ArrayList<Category> incorrectCategories = new ArrayList<Category>(categories);
		incorrectCategories.remove(selectedCategory);
		for (Category cat : incorrectCategories) {
			for (URL photo : cat.getPhotos()) {
				allIncorrectImages.add(photo);
			}
		}
		
		// We only need 9 - n false images (with n = numberOfCorrectImages)
		incorrectImages = allIncorrectImages
				.stream()
				.limit(9 - numberOfCorrectImages)
				.collect(Collectors.toList());
		
		// Add the incorrect images to the grid
		for (URL image : incorrectImages) {
			gridImages.add(image);
		}
		Collections.shuffle(gridImages);
	}
	

	/**
	 * @param
	 * @return List<URL> which is the entire grid to display
	 *
	 */
	public List<URL> getGridImages() {
		return gridImages;
	}
	
	
	public boolean isCaptchaCorrect(List<URL> images) {
		// TODO : Check nombre d'images selectionnées + si ils sont dans la bonne catégorie
		if (images.size() != numberOfCorrectImages) {
			return false;
		}
		for (URL image : images) {
			if (!selectedCategory.isPhotoCorrect(image)) {
				return false;
			}
		}
		return true;
	}
}
