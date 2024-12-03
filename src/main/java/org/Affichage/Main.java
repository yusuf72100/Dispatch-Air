package org.Affichage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.launcher.Launcher;

import java.io.IOException;


public class Main extends Application {
    private static Scene Main;
    private static Stage primaryStage;

    @Override
    public void start(Stage primary) throws IOException {
        try {
            primaryStage = primary;

            // Gestion de la scène
            Main = new Scene(
                MainMenu.getMenu(Screen.getPrimary().getVisualBounds().getWidth(),
                        Screen.getPrimary().getVisualBounds().getHeight()),
                Screen.getPrimary().getVisualBounds().getWidth(),
                Screen.getPrimary().getVisualBounds().getHeight()
            );

            // Gestion du style
            String cheminStyleCss = Launcher.normaliserChemin(Launcher.dossierAssets + "/style.css");
            Main.getStylesheets().add(Launcher.chargerFichierEnUrl(cheminStyleCss));

            // Gestion de la fenêtre
            primary.initStyle(StageStyle.DECORATED);
            primary.setScene(Main);
            primary.setTitle("Dispatch'Air");
            primary.setResizable(true);
            primary.setMaximized(true);
            primary.show();

            primary.setOnCloseRequest(event -> {
                Platform.exit();
                // Attend la fin des sauvegardes avant de fermer l'application
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        System.exit(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
                System.exit(0);
            });

            // Gestion de l'icône
            String cheminImgIcon = Launcher.normaliserChemin(Launcher.dossierAssets + "/icon/icon.png");
            primary.getIcons().add(Launcher.chargerImage(cheminImgIcon));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}