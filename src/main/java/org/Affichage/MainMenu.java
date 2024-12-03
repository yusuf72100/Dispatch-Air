package org.Affichage;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.launcher.Launcher;

import static org.launcher.Launcher.normaliserChemin;


public class MainMenu implements Menu {
    protected static StackPane mainPane;
    protected static HBox header;
    protected static Label title;
    protected static Font customFont;
    protected static Button minimizeButton;
    protected static Button exitButton;

    /**
     * Cette méthode renvoi la forme du menu (en l'occurence, un StackPane)
     * @param WIDTH
     * @param HEIGHT
     * @return
     */
    public static StackPane getMenu(Double WIDTH, Double HEIGHT) {
        minimizeButton = new Button();
        exitButton = new Button();

        minimizeButton.setPrefSize(30, 50);
        exitButton.setPrefSize(30, 50);
        minimizeButton.getStyleClass().add("button-withoutbg-reduce");
        exitButton.getStyleClass().add("button-withoutbg-close");
        minimizeButton.setAlignment(Pos.CENTER_RIGHT);
        exitButton.setAlignment(Pos.CENTER_RIGHT);

        minimizeButton.setOnAction(e -> {
            Main.reduceWindow();
        });

        exitButton.setOnAction(e -> {
            Platform.exit();
        });

        customFont = Font.loadFont(Launcher.normaliserChemin(Launcher.chargerFichierEnUrl(Launcher.dossierAssets + "/font/BrownRosemary.ttf")), 30);
        title = new Label("D i s p a t c h ' A i r");
        title.setTextFill(Color.WHITE);
        title.setFont(customFont);
        title.setPadding(new Insets(0, 0, 0, 50));

        header = new HBox();
        header.setMaxSize(WIDTH, 30);
        header.getStyleClass().add("header");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, minimizeButton, exitButton);
        header.setAlignment(Pos.CENTER_LEFT);

        mainPane = new StackPane();
        mainPane.setPrefSize(WIDTH, HEIGHT);
        mainPane.getChildren().addAll(header);
        mainPane.getStyleClass().add("mainPane");
        StackPane.setAlignment(header, Pos.TOP_CENTER);

        return mainPane;
    }

    public static HBox getHeader() {
        return header;
    }
}
