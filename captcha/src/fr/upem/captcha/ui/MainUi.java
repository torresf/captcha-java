/**
 * @authors Florian Torres - Stella Poulain
 * @date : 7 mai 2019
 * @file : MainUi.java
 * @package : fr.upem.captcha.ui
 */

package fr.upem.captcha.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.upem.captcha.logicengine.LogicEngine;

public class MainUi {

	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	private static JFrame frame;
	private static final int INIT = 1;
	private static final int SUCCESS = 2;
	private static final int FAILED = 3;
	private static final int END = 4;

	public static void main(String[] args) throws IOException {

		frame = new JFrame("Captcha"); // Création de la fenêtre principale

		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes

		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.

		// Fill the grid
		fillGrid(0);

		frame.setVisible(true);
	}

	private static JTextArea createTextArea(int instructionID, String selectedCategory) {
		LogicEngine logicEngine = LogicEngine.getInstance();
		switch (instructionID) {
			case INIT:
				return new JTextArea(" Difficulty level: " + logicEngine.getDifficultyLevel() + "\n" + " Click on " + selectedCategory + " images.");
			case SUCCESS:
				return new JTextArea("You're right");
			case FAILED:
				return new JTextArea("You're wrong");
			case END:
				return new JTextArea("Test's failed. Quit the captcha.");
			default:
				return new JTextArea("Click on " + selectedCategory + " images.");
		}
	}

	private static GridLayout createLayout() {
		return new GridLayout(4,3);
	}

	private static void fillGrid(int instruction) throws IOException {
		// Clear Grid
		frame.getContentPane().removeAll();
		
		// Fill with images 
		LogicEngine logicEngine = LogicEngine.getInstance();
		for (URL image : logicEngine.getGridImages()) {
			frame.add(createLabelImage(image));
		}
		
		if(instruction == SUCCESS) {
			JButton quitButton = createQuitButton();
			frame.add(quitButton);
		}
		else if(instruction == END) {
			JButton quitButton = createQuitButton();
			frame.add(quitButton);
		}
		else {
			// Add Text Area
			String selectedCategory = logicEngine.getSelectedCategory();
			JTextArea instruction1 = createTextArea(INIT, selectedCategory);
			frame.add(instruction1);
		}
		
		// Add ok button
		JButton okButton = createOkButton();
		frame.add(okButton);
		if(instruction > 1) {
			JTextArea instruction3 = createTextArea(instruction, "");
			frame.add(instruction3);
		}
		
		// Refresh the frame
		frame.validate();
		frame.repaint();
	}

	private static JButton createQuitButton() {
		return new JButton(new AbstractAction("Quit") { // Ajouter l'action du bouton

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // Faire des choses dans l'interface donc appeler cela dans la queue des évènements

					@Override
					public void run() { // c'est un runnable
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					}
				});
			}
		});
	}

	private static JButton createOkButton() {
		return new JButton(new AbstractAction("Check") { // Ajouter l'action du bouton

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // Faire des choses dans l'interface donc appeler cela dans la queue des évènements

					@Override
					public void run() { // c'est un runnable
						LogicEngine logicEngine = LogicEngine.getInstance();
						if (logicEngine.isCaptchaCorrect(selectedImages)) {
							try {
								fillGrid(SUCCESS);
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							logicEngine.clearGrid();
							logicEngine.increaseDifficultyLevel();
							logicEngine.selectRandomCategory();
							logicEngine.setGridImages();
							try {
								if(logicEngine.isFAILED()==1) {
									fillGrid(END);
								}
								else
									fillGrid(FAILED);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						selectedImages.clear();
					}
				});
			}
		});
	}

	private static JLabel createLabelImage(URL imageLocation) throws IOException{

		final URL url = imageLocation;

		BufferedImage img = ImageIO.read(url); // Lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); // Redimentionner l'image

		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenêtre

		label.addMouseListener(new MouseListener() { // Ajouter le listener d'évènement de souris
			private boolean isSelected = false;

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) { // Ce qui nous intéresse c'est lorsqu'on clique sur une image, il y a donc des choses à faire ici
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						if(!isSelected){
							label.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
							isSelected = true;
							selectedImages.add(url);
						}
						else {
							label.setBorder(BorderFactory.createEmptyBorder());
							isSelected = false;
							selectedImages.remove(url);
						}

					}
				});

			}
		});

		return label;
	}
}
