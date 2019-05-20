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
import fr.upem.captcha.images.animals.otters.Otter;
import fr.upem.captcha.images.instruments.Instrument;
import fr.upem.captcha.images.instruments.flutes.Flute;
import fr.upem.captcha.images.instruments.guitars.Guitar;
import fr.upem.captcha.images.instruments.pianos.Piano;


public class LogicEngine {
	private static final LogicEngine instance = new LogicEngine(); // Instance of the singleton
	private static final int MAX_DIFFICULTY_LEVEL = 2; // Maximum difficulty level
	
	private ArrayList<Category> categories; // All the categories
	private Category selectedCategory; // Selected category to click on
	private int difficultyLevel; // Difficulty level of the captcha
	private List<URL> gridImages; // Images of the grid
	private List<URL> correctImages; // Correct image of the grid
	private int numberOfCorrectImages; // Number of correct images to choose
	private int gridSize = 9; // Maximum number of images in the grid
	
	/** Logic Engine Constructor
	 * Sets difficultyLevel at 1
	 */
	private LogicEngine() {
		difficultyLevel = 1;
		categories = new ArrayList<Category>();
		categories = getCategories();
		gridImages = new ArrayList<URL>();
		selectRandomCategory();
		setGridImages();
	}
	
	/**
	 * @return instance of Logic Engine Singleton
	 */
	public static final LogicEngine getInstance() 
    {
        return instance;
    }

	
	/**
	 * @param
	 * Initializes and returns the ArrayList of Categories
	 * @return ArrayList<Category>
	 * The arraysList depends on the value of difficultyLevel
	 */
	public ArrayList<Category> getCategories() {
		categories.clear();
		switch (difficultyLevel) {
			case 1:
				categories.add(new Animal());
				categories.add(new Instrument());
				break;
			case 2:
				categories.add(new Dog());
				categories.add(new Cat());
				categories.add(new Otter());
				categories.add(new Guitar());
				categories.add(new Flute());
				categories.add(new Piano());
				break;
	
			default:
				categories.add(new Animal());
				categories.add(new Instrument());
				break;
		}
		return categories;
	}
	
	/** It increases the difficultyLevel when the user doesn't perform the previous level,
	 * 	and get the new categories
	 */
	public void increaseDifficultyLevel() {
		if (difficultyLevel < MAX_DIFFICULTY_LEVEL) 
			difficultyLevel++;
		getCategories();
	}
	
	/** Shuffles the categories, takes the first one and assign it to selectedCategory
	 */
	public void selectRandomCategory() {
		Collections.shuffle(categories);
		selectedCategory = categories.get(0);
	}
	
	/**
	 * @return String representing the random selected category
	 */
	public String getSelectedCategory() {
		return selectedCategory.toString();
	}
	
	/** Fills the grid with 9 images (1 to 4 correct images, and fill with incorrect images)
	 * @param
	 */
	public void setGridImages() {
		// Clear the grid
		gridImages.clear();
		
		// Set numberOfCorrectImages randomly between 1 and 4
		this.numberOfCorrectImages = (int)((Math.random() * 4) + 1);
		
		// Get some correct images from the selected category
		correctImages = selectedCategory.getRandomPhotosURL(numberOfCorrectImages);
		
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
		
		// We only need (gridSize - numberOfCorrectImages) false images
		incorrectImages = allIncorrectImages
				.stream()
				.limit(gridSize - numberOfCorrectImages)
				.collect(Collectors.toList());
		
		// Add the incorrect images to the grid
		for (URL image : incorrectImages) {
			gridImages.add(image);
		}

		// Shuffle the grid
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
	
	/**
	 * @param List<URL> of selected images
	 * @return boolean if it's correct return true, else false
	 *
	 */
	public boolean isCaptchaCorrect(List<URL> images) {
		// TODO : Check nombre d'images selectionnées + si ils sont dans la bonne catégorie
		System.out.println("numberOfCorrectImages2 : " + numberOfCorrectImages);
		if (images.size() != numberOfCorrectImages) {
			System.out.println("You didn't select the correct number of images.");
			return false;
		}
		for (URL image : images) {
			if (!selectedCategory.isPhotoCorrect(image)) {
				System.out.println("Some pictures are not from the right category");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Clear the grid
	 */
	public void clearGrid() {
		gridImages.clear();
	}
	
	
}
