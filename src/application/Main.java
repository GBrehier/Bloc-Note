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

	TextArea textArea;
	FileChooser file;
	String contenu="";
	String chemin=null;

	MenuBar menu;

	Menu fichier;
	Menu edition;

	MenuItem ouvrir;
	MenuItem sauvegarder;
	MenuItem coller;
	MenuItem copier;

	@Override
	public void start(Stage primaryStage) {
		
			VBox root = new VBox();
			Scene scene = new Scene(root,600,600);

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
		fichier = new Menu("Fichier");
		creationMenuFichier(primaryStage);

		edition = new Menu("Edition");	
		creationMenuEdition();

		menu.getMenus().addAll(fichier,edition);
		menu.setMaxWidth(Double.MAX_VALUE);
	}

	private void creationMenuEdition() {
		copier = new MenuItem("Copier");
		coller = new MenuItem("Coller");
		evenementMenuItemColler();
		evenementMenuItemCopier();
		edition.getItems().addAll(copier,coller);
	}

	private void evenementMenuItemCopier() {
		copier.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
		copier.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				textArea.copy();
			}

		});
	}

	private void evenementMenuItemColler() {
		coller.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
		coller.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				textArea.paste();
			}

		});
	}

	private void creationMenuFichier(Stage primaryStage) {
		ouvrir  = new MenuItem("Ouvrir");
		sauvegarder = new MenuItem("Sauvegarder");
		evenementMenuItemOuvrir(primaryStage);
		evenementMenuItemSauvegarder(primaryStage);
		fichier.getItems().addAll(ouvrir,sauvegarder);
	}

	private void creationTextArea(Scene scene) {
		textArea.prefHeight(Double.MAX_VALUE);
		textArea.prefWidth(Double.MAX_VALUE);
		textArea.setWrapText(true);
		textArea.setPrefRowCount((int)((scene.getHeight()/16)-1));
		textArea.setFocusTraversable(false);
		textArea.setStyle("-fx-faint-focus-color: transparent; -fx-text-box-border: transparent;");
	}

	private void evenementMenuItemSauvegarder(Stage primaryStage) {
		sauvegarder.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		sauvegarder.setOnAction(new EventHandler <ActionEvent>(){
			@Override
			public void handle (ActionEvent event) {


				if(null==chemin) {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Save file");

					FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text", "*.txt");
					FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("HTML", "*.html");
					fileChooser.getExtensionFilters().addAll(filter,filter2);
					File savedFile = fileChooser.showSaveDialog(primaryStage);

					String extension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
					System.out.println(extension);

					chemin= savedFile.getAbsolutePath();
					primaryStage.setTitle(savedFile.getName()+ " - Bloc-note");

				}
				try {

					if(null!=chemin) {

						BufferedWriter scribe = new BufferedWriter(new FileWriter(chemin));

						String[] texteLigne = textArea.getText().split("\n");
						for(int i=0;i<texteLigne.length;i++) {
							scribe.write(texteLigne[i]);
							scribe.newLine();
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
		ouvrir.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		ouvrir.setOnAction(new EventHandler <ActionEvent>(){
			@Override
			public void handle (ActionEvent event) {
				file= new FileChooser();
				file.setTitle("Choix du fichier");
				File fichier = file.showOpenDialog(primaryStage);
				chemin= fichier.getAbsolutePath();

				try {

					String ligne ;
					BufferedReader lecteur = new BufferedReader(new FileReader(chemin));

					while ((ligne = lecteur.readLine()) != null) {
						contenu=contenu+ligne+"\n";
					}

					lecteur.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
				primaryStage.setTitle(fichier.getName()+ " - Bloc-note");
				textArea.setText(contenu);
				contenu="";
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
