/**
 * @authors Florian Torres - Stella Poulain
 * @date : 17 mai 2019
 * @file : LogicEngine.java
 * @package : fr.upem.captcha.images.logicengine
 */

package fr.upem.captcha.logicengine;

import java.util.ArrayList;
import java.util.Collections;

import fr.upem.captcha.images.Category;
import fr.upem.captcha.images.animals.Animal;
import fr.upem.captcha.images.animals.cats.Cat;
import fr.upem.captcha.images.animals.dogs.Dog;
import fr.upem.captcha.images.instruments.Instrument;
import fr.upem.captcha.images.instruments.flutes.Flute;

public class LogicEngine {
	ArrayList<Category> categories;
	Category selectedCategory;
	private int difficultyLevel;
	
	public LogicEngine() {
		difficultyLevel = 2;
		categories = new ArrayList<Category>();
		categories = getCategories();
	}
	
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
	
	public void increaseDifficultyLevel() {
		difficultyLevel++;
	}
	
	public String selectRandomCategory() {
		Collections.shuffle(categories);
		selectedCategory = categories.get(0);
		return selectedCategory.toString();
	}
}
