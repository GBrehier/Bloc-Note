package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	// initialisation des composants graphiques
	
	
	TextArea textArea;
	FileChooser file;
	String contenu="";
	String chemin=null;

	
	//composants du menu
	MenuBar menu;

	Menu fichier; // composant de la barre du menu
	Menu edition;

	MenuItem ouvrir; // composant des sous-options
	MenuItem sauvegarder;
	MenuItem coller;
	MenuItem copier;

	@Override
	public void start(Stage primaryStage) {
		
			VBox root = new VBox();
			Scene scene = new Scene(root,600,600); // mise en place de la scene

			textArea= new TextArea();
			creationTextArea(scene);

			menu = new MenuBar();
			creationMenu(primaryStage);


			root.getChildren().addAll(menu,textArea);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Sans titre - Bloc-notes");
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image("/image/iconeBN2.png"));
			primaryStage.show();

		
	}

	private void creationMenu(Stage primaryStage) {
		fichier = new Menu("Fichier");   // menu fichier
		creationMenuFichier(primaryStage);

		edition = new Menu("Edition");	// menu edition
		creationMenuEdition();

		menu.getMenus().addAll(fichier,edition); // ajout des sous menus au menu principal
		menu.setMaxWidth(Double.MAX_VALUE);
	}

	private void creationMenuEdition() {
		copier = new MenuItem("Copier"); // options du menu edition
		coller = new MenuItem("Coller");
		evenementMenuItemColler();	// mise en place des évènements
		evenementMenuItemCopier(); 
		edition.getItems().addAll(copier,coller);  // ajout des options
	}

	private void evenementMenuItemCopier() {
		copier.setAccelerator(KeyCombination.keyCombination("Ctrl+C")); // si l'utilisateur fait ctrl cela aura le même effet que l'option
		copier.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				textArea.copy();
			}

		});
	}

	private void evenementMenuItemColler() {
		coller.setAccelerator(KeyCombination.keyCombination("Ctrl+V")); // même chose que pour copier
		coller.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				textArea.paste();
			}

		});
	}

	private void creationMenuFichier(Stage primaryStage) {
		ouvrir  = new MenuItem("Ouvrir");
		sauvegarder = new MenuItem("Sauvegarder"); // option du menu Fichier
		evenementMenuItemOuvrir(primaryStage);
		evenementMenuItemSauvegarder(primaryStage);
		fichier.getItems().addAll(ouvrir,sauvegarder); // ajout des options au menu
	}

	private void creationTextArea(Scene scene) {
		textArea.prefHeight(Double.MAX_VALUE); // Double.MAX_VALUE pour que la textArea prenne tout l'espace
		textArea.prefWidth(Double.MAX_VALUE);
		textArea.setWrapText(true);
		textArea.setPrefRowCount((int)((scene.getHeight()/16)-1));
		textArea.setFocusTraversable(false);
		textArea.setStyle("-fx-faint-focus-color: transparent; -fx-text-box-border: transparent;");
	}

	private void evenementMenuItemSauvegarder(Stage primaryStage) {
		sauvegarder.setAccelerator(KeyCombination.keyCombination("Ctrl+S")); // ctrl + s = bouton sauvegarder
		sauvegarder.setOnAction(new EventHandler <ActionEvent>(){
			@Override
			public void handle (ActionEvent event) {


				if(null==chemin) {
					FileChooser fileChooser = new FileChooser(); // fenetre explorateur de fichier 
					fileChooser.setTitle("Sauvegarder un fichier");

					FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text", "*.txt"); // filtres de fichier
					FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("HTML", "*.html");
					fileChooser.getExtensionFilters().addAll(filter,filter2); // ajout des filtres
									
					File savedFile = fileChooser.showSaveDialog(primaryStage); 
					
					chemin= savedFile.getAbsolutePath(); // on récupère le chemin pour le prochaine sauvegarde
					primaryStage.setTitle(savedFile.getName()+ " - Bloc-note");

				}
				try {

					if(null!=chemin) { // dans le cas où le chemin est connue

						BufferedWriter scribe = new BufferedWriter(new FileWriter(chemin)); // on initialise un scribe

						String[] texteLigne = textArea.getText().split("\n"); // on récupère chaque ligne dans un tableau de chaine de caractères
						for(int i=0;i<texteLigne.length;i++) { // pour tout le tableau
							scribe.write(texteLigne[i]); // on recopie la ligne
							scribe.newLine(); // on retourne à la ligne
						}

						scribe.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				} 
			}

		});
	}

	private void evenementMenuItemOuvrir(Stage primaryStage) {
		ouvrir.setAccelerator(KeyCombination.keyCombination("Ctrl+O")); // meme chose que pour sauvegarder
		ouvrir.setOnAction(new EventHandler <ActionEvent>(){
			@Override
			public void handle (ActionEvent event) {
				
				file= new FileChooser();  // fenetre explorateur de fichier
				file.setTitle("Choix du fichier");
				File fichier = file.showOpenDialog(primaryStage);
				chemin= fichier.getAbsolutePath(); //on recupere le chemin absolu du fichier

				try {

					String ligne ;
					BufferedReader lecteur = new BufferedReader(new FileReader(chemin)); 
					// on utilise le chemin pour créer un FileReader
					while ((ligne = lecteur.readLine()) != null) { // tant que le lecteur n'est pas à la fin du fichier
						contenu=contenu+ligne+"\n"; //on remplit la variable contenu
					}

					lecteur.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
				primaryStage.setTitle(fichier.getName()+ " - Bloc-note"); // on change le titre de la fenêtre pour savoir quel fichier on modifie
				textArea.setText(contenu); // on met le contenu comme texte
				contenu="";
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
