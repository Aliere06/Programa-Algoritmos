package com.aliere;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

    public enum Screen{
        MENU("Menu Principal.fxml", true),
        ALGORITHM("Pantalla Algoritmo.fxml", true);

        private URL resource;
        private Parent root;

        Screen(String filename, boolean isPreCached){
            resource = getClass().getClassLoader().getResource(filename);
            if (isPreCached) {
                setRoot();
            }
        }

        private void setRoot() {
            try {
                root = FXMLLoader.load(resource);
            } catch (Exception e) {
                System.out.println("Failed to load fxml for enum: " + this.name());
                e.printStackTrace();
            }
        }

        public Parent getRoot() {
            if (root == null) {
                setRoot();
            }
            return root;
        }

    }

    private static Scene scene;

    @Override
    public void start(Stage stage) throws Exception {

        scene = new Scene(Screen.MENU.getRoot());
        ScreenSwitcher.setScene(scene);

        stage.setTitle("Generadores Aleatorios");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}