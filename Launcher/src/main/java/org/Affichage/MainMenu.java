package org.Affichage;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.Launcher.Launcher;
import org.Launcher.Profil;
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
    protected static ComboBox<String> profilsCombo;
    protected static Text welcomeText;

    /**
     * Cette méthode renvoi la forme du menu (en l'occurence, un StackPane)
     * @param WIDTH
     * @param HEIGHT
     * @return
     */
    public static StackPane getMenu(Double WIDTH, Double HEIGHT) {
        MainMenu.WIDTH = WIDTH;
        MainMenu.HEIGHT = HEIGHT;
        profilsCombo = new ComboBox<>();
        minimizeButton = new Button();
        exitButton = new Button();
        reduceRectangle = new Rectangle(WIDTH, HEIGHT);
        reduceImage = new Image(Objects.requireNonNull(MainMenu.class.getResourceAsStream("/BOOT-INF/classes/ressources/assets/img/window_reduce.png")));
        reduceImagePattern = new ImagePattern(reduceImage);
        reduceRectangle.setFill(reduceImagePattern);

        for (Profil profil : Launcher.profilsList) {
            profilsCombo.getItems().add(profil.getNom());
        }

        if(Launcher.profilsList.isEmpty()) {
            profilsCombo.setValue("➕ Ajouter un profil...");
        } else {
            profilsCombo.getItems().add("➕ Ajouter un profil...");
        }

        // Style que l'on va retirer après le premier hover
        profilsCombo.setStyle(
                "-fx-min-width: 500px;" +
                        "-fx-min-height: 50px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-border-color: #212121;" +
                        "-fx-border-width: 0 0 3 0;"
        );

        // Si on clique sur la valeur actuelle
        profilsCombo.showingProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (profilsCombo.getValue().compareTo("➕ Ajouter un profil...") == 0) {
                    mainPane.setMouseTransparent(true);     // élément non clickable
                    profilsCombo.setMouseTransparent(true); // élément non clickable
                    Main.createProfile();
                }
            }
        });

        profilsCombo.setOnAction(e ->
                Main.createProfile()
        );

        // Attendre que le skin soit chargé
        Platform.runLater(() -> {
            Region base = (Region) profilsCombo.lookup(".combo-box-base");

            // Animation pour hover
            profilsCombo.setOnMouseEntered(e -> {
                // On enlève la barre du bas statique pour éviter le bugg de stutter
                profilsCombo.getStyleClass().clear();
                profilsCombo.getStyleClass().add("combo-box");

                Timeline timeline = new Timeline();
                int start = 3;
                int end = 10;

                // On crée plusieurs KeyFrame pour interpoler la valeur
                for (float i = start; i <= end; i+=0.05) {
                    KeyFrame kf = getKeyFrame(i, i - start, base);
                    timeline.getKeyFrames().add(kf);
                }
                timeline.play();
            });

            // Animation pour exit
            profilsCombo.setOnMouseExited(e -> {
                Timeline timeline = new Timeline();
                int start = 10;
                int end = 3;

                for (float i = start; i >= end; i-=0.05) {
                    KeyFrame kf = getKeyFrame(i, start - i, base);
                    timeline.getKeyFrames().add(kf);
                }
                timeline.play();
            });
        });

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

        // Bouton de fermeture de l'application
        exitButton.setOnAction(e -> {
            Platform.exit();
        });

        // Titre
        title = new Text("D i s p a t c h ' A i r");
        title.setFont(Font.font("BrownRosemary", 20));
        title.setFill(Color.WHITE);
        title.setTranslateX(20);

        welcomeText = new Text("Bienvenue");
        welcomeText.setFont(Font.font("BrownRosemary", 20));
        welcomeText.setFill(Color.BLACK);
        welcomeText.setTranslateY(-120);
        welcomeText.getStyleClass().add("welcomeText");

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
        mainPane.getChildren().addAll(header, welcomeText, profilsCombo, reduceRectangle);
        mainPane.getStyleClass().add("mainPane");

        StackPane.setAlignment(header, Pos.TOP_CENTER);
        reduceRectangle.setMouseTransparent(true);

        return mainPane;
    }

    private static KeyFrame getKeyFrame(float i, float i1, Region base) {
        float borderSize = i;
        KeyFrame kf = new KeyFrame(Duration.millis(i1 * 10), // 40ms entre chaque étape
                ev -> base.setBorder(new Border(new BorderStroke(
                        Color.web("#212121"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, borderSize, 0)
                )))
        );
        return kf;
    }

    /**
     * On réinitialise la position du rectangle de transition
     */
    public static void resetRectangle() {
        reduceRectangle.setTranslateX(-MainMenu.WIDTH);
    }

    /**
     * Getter du node d'affichage principal
     * @return
     */
    public static HBox getHeader() {
        return header;
    }
}
