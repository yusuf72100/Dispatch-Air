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
import java.util.Objects;


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
            Main.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/ressources/assets/style.css")).toExternalForm());

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

    /**
     * Réduit la fenetre avec animation de fondu
     * @throws InterruptedException
     */
    public static void stageFadeOut() {
        // Utilisation de Platform.runLater pour exécuter les mises à jour de l'UI sur le thread JavaFX
        Platform.runLater(() -> {
            double opacity = 1.0;

            // Réduction de l'opacité progressivement jusqu'à 0
            while (opacity > 0.0) {
                try {
                    Thread.sleep(1);  // Petite pause pour créer l'effet de fondu
                    opacity -= 0.01;

                    // S'assurer que l'opacité ne devienne pas négative
                    if (opacity < 0.0) {
                        opacity = 0.0;
                    }

                    // Mettre à jour l'opacité de la fenêtre
                    primaryStage.setOpacity(opacity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Quand le fade-out est terminé, réduire la fenêtre (minimiser)
            primaryStage.setIconified(true);

            // Appeler une méthode pour réinitialiser la position du rectangle d'animation, si nécessaire
            MainMenu.resetRectangle();

            // Remettre l'opacité à 1 pour préparer la fenêtre à être réutilisée
            primaryStage.setOpacity(1.0);
        });
    }

}