package org.Affichage;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Objects;


public class MainMenu implements Menu {
    protected static StackPane mainPane;
    protected static HBox header;
    protected static Text title;
    protected static Button minimizeButton;
    protected static Button exitButton;
    protected static Rectangle reduceRectangle;
    protected static ImagePattern reduceImagePattern;
    protected static Image reduceImage;
    protected static double WIDTH;
    protected static double HEIGHT;

    /**
     * Cette méthode renvoi la forme du menu (en l'occurence, un StackPane)
     * @param WIDTH
     * @param HEIGHT
     * @return
     */
    public static StackPane getMenu(Double WIDTH, Double HEIGHT) {
        MainMenu.WIDTH = WIDTH;
        MainMenu.HEIGHT = HEIGHT;
        minimizeButton = new Button();
        exitButton = new Button();
        reduceRectangle = new Rectangle(WIDTH, HEIGHT);
        reduceImage = new Image(Objects.requireNonNull(MainMenu.class.getResourceAsStream("/BOOT-INF/classes/ressources/assets/img/window_reduce.png")));
        reduceImagePattern = new ImagePattern(reduceImage);
        reduceRectangle.setFill(reduceImagePattern);

        minimizeButton.setPrefSize(30, 50);
        exitButton.setPrefSize(30, 50);
        minimizeButton.getStyleClass().add("button-withoutbg-reduce");
        exitButton.getStyleClass().add("button-withoutbg-close");
        minimizeButton.setAlignment(Pos.CENTER_RIGHT);
        exitButton.setAlignment(Pos.CENTER_RIGHT);

        minimizeButton.setOnAction(e -> {
            // Animation du rectangle avec une durée de 1 seconde pour une animation rapide
            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), reduceRectangle);
            transition.setFromX(-WIDTH); // Départ à gauche
            transition.setToX(0); // Traverse pour occuper toute la vue

            // on utilise un interpolateur inverse pour un démarrage rapide et un ralentissement à la fin
            transition.setInterpolator(new Interpolator() {
                @Override
                protected double curve(double t) {
                    // fonction inverse exponentielle pour démarrer rapidement puis ralentir à la fin
                    return 1 - Math.pow(1 - t, 3); // cette fonction rend l'animation rapide au début et lente à la fin
                }
            });

            transition.setCycleCount(1); // Une seule exécution

            // on ajoute un listener pour attendre la fin de l'animation avant de réduire la fenêtre
            transition.setOnFinished(event -> {
                Main.stageFadeOut();
            });

            // on démarre l'animation
            transition.play();
        });

        exitButton.setOnAction(e -> {
            Platform.exit();
        });

        title = new Text("D i s p a t c h ' A i r");
        title.setFont(Font.font("BrownRosemary", 20));
        title.setFill(Color.WHITE);
        title.setTranslateX(20);

        header = new HBox();
        header.setMaxSize(WIDTH, 30);   
        header.getStyleClass().add("header");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, minimizeButton, exitButton);
        header.setAlignment(Pos.CENTER_LEFT);

        // Rectangle noir pour l'animation
        reduceRectangle.setTranslateX(-WIDTH); // Initialement en dehors de la vue, à gauche
        reduceRectangle.getStyleClass().add("reduce-rectangle");

        mainPane = new StackPane();
        mainPane.setPrefSize(WIDTH, HEIGHT);
        mainPane.getChildren().addAll(header, reduceRectangle);
        mainPane.getStyleClass().add("mainPane");

        StackPane.setAlignment(header, Pos.TOP_CENTER);

        return mainPane;
    }

    public static void resetRectangle() {
        reduceRectangle.setTranslateX(-MainMenu.WIDTH);
    }

    public static HBox getHeader() {
        return header;
    }
}
