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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import fr.upem.captcha.images.Category;
import fr.upem.captcha.logicengine.LogicEngine;

public class MainUi {

	private static ArrayList<URL> selectedImages = new ArrayList<URL>();
	private static JFrame frame;

	public static void main(String[] args) throws IOException {

		frame = new JFrame("Captcha"); // Cr�ation de la fen�tre principale

		GridLayout layout = createLayout();  // Cr�ation d'un layout de type Grille avec 4 lignes et 3 colonnes

		frame.setLayout(layout);  // affection du layout dans la fen�tre.
		frame.setSize(1024, 768); // d�finition de la taille
		frame.setResizable(false);  // On d�finit la fen�tre comme non redimentionnable

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fen�tre on quitte le programme.

		// Fill the grid
		fillGrid();

		frame.setVisible(true);
	}

	private static JTextArea createTextArea(int instructionID, String selectedCategory) {
		switch (instructionID) {
			case 1:
				return new JTextArea("Click on " + selectedCategory + " images.");
			case 2:
				return new JTextArea("You're right, you're not a robot");
			case 3:
				return new JTextArea("You're wrong, you're a robot or a dumb guy");
			default:
				return new JTextArea("Click on " + selectedCategory + " images.");
		}
	}

	private static GridLayout createLayout() {
		return new GridLayout(4,3);
	}

	private static void fillGrid() throws IOException {
		// Clear Grid
		frame.getContentPane().removeAll();
		
		// Fill with images 
		LogicEngine logicEngine = LogicEngine.getInstance();
		for (URL image : logicEngine.getGridImages()) {
			frame.add(createLabelImage(image));
		}
		
		// Add Text Area
		String selectedCategory = logicEngine.getSelectedCategory();
		JTextArea instruction1 = createTextArea(1, selectedCategory);
		frame.add(instruction1);
		
		// Add ok button
		JButton okButton = createOkButton();
		frame.add(okButton);
		
		// Refresh the frame
		frame.validate();
		frame.repaint();
	}

	private static JButton createOkButton() {
		return new JButton(new AbstractAction("Check") { // Ajouter l'action du bouton

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // Faire des choses dans l'interface donc appeler cela dans la queue des �v�nements

					@Override
					public void run() { // c'est un runnable
						LogicEngine logicEngine = LogicEngine.getInstance();
						if (logicEngine.isCaptchaCorrect(selectedImages)) {
							System.out.println("Right, you're not a robot !");
						} else {
							System.out.println("Are you dumb or a robot ?");
							logicEngine.clearGrid();
							logicEngine.increaseDifficultyLevel();
							logicEngine.selectRandomCategory();
							logicEngine.setGridImages();
							try {
								fillGrid();
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

		final JLabel label = new JLabel(new ImageIcon(sImage)); // cr�er le composant pour ajouter l'image dans la fen�tre

		label.addMouseListener(new MouseListener() { // Ajouter le listener d'�venement de souris
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
			public void mouseClicked(MouseEvent arg0) { // Ce qui nous int�resse c'est lorsqu'on clique sur une image, il y a donc des choses � faire ici
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
