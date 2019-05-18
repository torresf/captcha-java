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

	public static void main(String[] args) throws IOException {
		System.out.println("- Main");
		
		LogicEngine logicEngine = new LogicEngine();
		String selectedCategory = logicEngine.getSelectedCategory();

		JFrame frame = new JFrame("Capcha"); // Création de la fenêtre principale

		GridLayout layout = createLayout();  // Création d'un layout de type Grille avec 4 lignes et 3 colonnes

		frame.setLayout(layout);  // affection du layout dans la fenêtre.
		frame.setSize(1024, 768); // définition de la taille
		frame.setResizable(false);  // On définit la fenêtre comme non redimentionnable

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Lorsque l'on ferme la fenêtre on quitte le programme.

		JButton okButton = createOkButton();
		
		/* frame.add(createLabelImage("centre ville.jpg")); //ajouter des composants à la fenêtre
		frame.add(createLabelImage("le havre.jpg"));
		frame.add(createLabelImage("panneau 70.jpg"));
		frame.add(createLabelImage("panneaubleu-carre.jpeg"));
		frame.add(createLabelImage("parking.jpg"));
		frame.add(createLabelImage("route panneau.jpg"));
		frame.add(createLabelImage("tour eiffel.jpg"));
		frame.add(createLabelImage("ville espace verts.jpg"));
		frame.add(createLabelImage("voie pieton.jpg")); */
		
		// Fill the grid with random images
		for (URL image : logicEngine.getGridImages()) {
			frame.add(createLabelImage(image));
		}
		
		frame.add(new JTextArea("Click on " + selectedCategory + " images."));

		frame.add(okButton);

		frame.setVisible(true);
	}

	private static GridLayout createLayout(){
		return new GridLayout(4,3);
	}

	private static JButton createOkButton(){
		return new JButton(new AbstractAction("Check") { // ajouter l'action du bouton

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() { // faire des choses dans l'interface donc appeler cela dans la queue des évènements

					@Override
					public void run() { // c'est un runnable
						LogicEngine logicEngine = new LogicEngine();
						System.out.println("J'ai cliqué sur Ok");
						if (logicEngine.isCaptchaCorrect(selectedImages)) {
							System.out.println("C'est juste !");
						} else {
							System.out.println("C'est faux !");
							// TODO relancer le captcha avec un niveau de difficult� sup�rieur.
						}
					}
				});
			}
		});
	}

	private static JLabel createLabelImage(URL imageLocation) throws IOException{

//		final URL url = MainUi.class.getResource(imageLocation); // Aller chercher les images !! IMPORTANT
		final URL url = imageLocation;

		System.out.println(url);

		BufferedImage img = ImageIO.read(url); // Lire l'image
		Image sImage = img.getScaledInstance(1024/3,768/4, Image.SCALE_SMOOTH); // Redimentionner l'image

		final JLabel label = new JLabel(new ImageIcon(sImage)); // créer le composant pour ajouter l'image dans la fenêtre

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
