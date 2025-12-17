package org.Affichage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import org.Launcher.Launcher;
import org.Launcher.Profile;

import java.util.Objects;

import java.awt.*;
import java.io.IOException;

import static com.sun.javafx.application.PlatformImpl.exit;


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
                    // on enregistre la position de la souris lorsque l'utilisateur clique
                    xOffset = event.getScreenX() - primaryStage.getX();
                    yOffset = event.getScreenY() - primaryStage.getY();
                }
            });

            header.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // déplacement de la fenetre
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
                // attend la fin des sauvegardes avant de fermer l'application
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

            primary.getIcons().add(new Image(Objects.requireNonNull(MainMenu.class.getResource("/ressources/assets/img/logo.png")).toExternalForm()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Réduit la fenetre avec animation de fondu
     * @throws InterruptedException
     */
    public static void stageFadeOut() {
        // on utilise Platform.runLater pour exécuter les mises à jour de l'UI sur le thread JavaFX
        Platform.runLater(() -> {
            double opacity = 1.0;

            // on réduit de l'opacité progressivement jusqu'à 0
            while (opacity > 0.0) {
                try {
                    Thread.sleep(1);  // Petite pause pour créer l'effet de fondu
                    opacity -= 0.01;

                    // on s'assure que l'opacité ne devienne pas négative
                    if (opacity < 0.0) {
                        opacity = 0.0;
                    }

                    // on met à jour l'opacité de la fenêtre
                    primaryStage.setOpacity(opacity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // quand le fade-out est terminé, réduire la fenêtre (minimiser)
            primaryStage.setIconified(true);

            // on appel une méthode pour réinitialiser la position du rectangle d'animation, si nécessaire
            MainMenu.resetRectangle();

            // on remet l'opacité à 1 pour préparer la fenêtre à être réutilisée
            primaryStage.setOpacity(1.0);
        });
    }

    public static void createProfile() {
        // Nouveau stage pour le popup
        Stage popupStage = new Stage();
        popupStage.initOwner(primaryStage);          // le parent est la fenêtre principale
        popupStage.initStyle(StageStyle.TRANSPARENT); // style sans bordure

        // StackPane principal
        StackPane popupRoot = Popup.getMenu(400.0, 200.0);

        // Scene du popup
        Scene popupScene = new Scene(popupRoot, 400, 200, Color.TRANSPARENT);
        popupScene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/ressources/assets/style.css")).toExternalForm());
        popupStage.setScene(popupScene);

        // Positionner le popup au centre de la fenêtre principale
        popupStage.setX(primaryStage.getX() + (primaryStage.getWidth() - 400) / 2);
        popupStage.setY(primaryStage.getY() + (primaryStage.getHeight() - 200) / 2);

        System.out.println("popup créé");
        popupStage.show();

        Popup.cancel.setOnAction(e -> {
                popupStage.close();
                MainMenu.profilsCombo.setMouseTransparent(false);
                MainMenu.mainPane.setMouseTransparent(false);
        });

        Popup.confirm.setOnAction(e -> {
            // Création du profile
            Profile profile = new Profile(Popup.textField.getText());

            if (profile.serialiseProfile() != 0) {
                // TODO : NOTIF ALERTE
            }

            Launcher.chargerProfils();
            MainMenu.refreshProfilsList();
            popupStage.close();
            MainMenu.profilsCombo.setMouseTransparent(false);
            MainMenu.mainPane.setMouseTransparent(false);

        });
    }
}