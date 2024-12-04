package org.Affichage;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.launcher.Launcher;

import java.io.IOException;


public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    private static Scene Main;
    private static Stage primaryStage;

    private static final double WIDTH = 800;
    private static final double HEIGHT = 450;

    private static StackPane root;
    private static HBox header;

    @Override
    public void start(Stage primary) throws IOException {
        try {
            primaryStage = primary;
            root = MainMenu.getMenu(WIDTH, HEIGHT);
            header = MainMenu.getHeader();

            // Gestion de la scène
            Main = new Scene(root, WIDTH, HEIGHT, Color.TRANSPARENT);

            // Gestion du style
            String cheminStyleCss = Launcher.normaliserChemin(Launcher.dossierAssets + "/style.css");
            Main.getStylesheets().add(Launcher.chargerFichierEnUrl(cheminStyleCss));

            // Mouvement fenêtre
            header.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Enregistrer la position de la souris lorsque l'utilisateur clique
                    xOffset = event.getScreenX() - primaryStage.getX();
                    yOffset = event.getScreenY() - primaryStage.getY();
                }
            });

            header.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Déplacer la fenêtre en fonction du mouvement de la souris
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            // Gestion de la fenêtre
            primary.initStyle(StageStyle.UNDECORATED);
            primary.setScene(Main);
            primary.setTitle("Dispatch'Air");
            primary.setResizable(true);
            primary.setMaximized(false);
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
            String cheminImgIcon = Launcher.normaliserChemin(Launcher.dossierAssets + "/img/logo.png");
            primary.getIcons().add(Launcher.chargerImage(cheminImgIcon));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reduceWindow() {
        primaryStage.setIconified(true);
    }

    public static void stageFadeOut() throws InterruptedException {
        double opacity = 1.0;

        while (opacity > 0.0) {
            Thread.sleep(1);
            opacity -= 0.01;

            // Assure que l'opacité ne flambe pas
            if (opacity < 0.0) {
                opacity = 0.0;
            }

            primaryStage.setOpacity(opacity);
        }

        reduceWindow();
        MainMenu.resetRectangle();
        primaryStage.setOpacity(1.0);
    }
}