package com.aliere;

import com.aliere.Main.Screen;

import javafx.scene.Scene;

public class ScreenSwitcher {

    private static Scene scene;

    public static void setScene(Scene scene) {
        ScreenSwitcher.scene = scene;
    }

    public static void switchScreen(Screen newScreen) {
        scene.setRoot(newScreen.getRoot());
    }
}
