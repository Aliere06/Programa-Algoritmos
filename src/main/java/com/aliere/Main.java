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
        private Parent screen;
        private FXMLLoader loader;

        Screen(String filename, boolean isPreCached){
            resource = getClass().getClassLoader().getResource(filename);
            loader = new FXMLLoader(getClass().getClassLoader().getResource(filename));
            if (isPreCached) {
                loadScreen();
            }
        }

        private void loadScreen() {
            try {
                System.out.println("\nLoading " + resource);
                screen = loader.load();
            } catch (Exception e) {
                System.out.println("Failed to load fxml for enum: " + this.name());
                e.printStackTrace();
            }
        }

        protected Parent getScreen() {
            if (screen == null) {
                loadScreen();
            }
            return screen;
        }
        public Object getController() {
            return loader.getController();
        }

    }

    private static Scene scene;

    public static void setScreen(Screen newScreen) {
        scene.setRoot(newScreen.getScreen());
    }

    /*protected static Stage getStage() {
        return 
    }*/
    public static AlgorithmController getAlgorithmController() {
        return (AlgorithmController)Screen.ALGORITHM.getController();
    }
    public static MenuController getMenuController() {
        return (MenuController)Screen.MENU.getController();
    }

    @Override
    @SuppressWarnings("exports")
    public void start(Stage stage) throws Exception {
        scene = new Scene(Screen.MENU.getScreen(),600,400);
        stage.setTitle("Generadores Aleatorios");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}