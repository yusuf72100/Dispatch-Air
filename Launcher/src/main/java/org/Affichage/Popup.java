package org.Affichage;


import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.Launcher.Launcher;
import org.Launcher.Profile;

public class Popup implements Menu {
    protected static StackPane popupMainPane;
    protected static Button confirm;
    protected static Button cancel;
    protected static TextField textField;
    protected static HBox buttonBox;
    protected static VBox vbox;

    public static StackPane getMenu(Double WIDTH, Double HEIGHT) {
        textField = new TextField("Entrez le nom du profil...");
        textField.getStyleClass().add("nameTextField");

        // SCALE UP on focus
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), textField);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);

        // SCALE DOWN on focus lost
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(300), textField);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        // Shadow externe (comme ton CSS web)
        DropShadow focusShadow1 = new DropShadow(
                10,      // radius (moins de flou)
                2, 2,    // offset X/Y (moins éloigné)
                Color.web("#b3b3b3") // gris plus doux
        );

        DropShadow focusShadow2 = new DropShadow(
                10,
                -2, -2,
                Color.web("#ffffff")
        );
        focusShadow1.setInput(focusShadow2);

        // Listener pour focus
        textField.focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (isFocused) {
                scaleUp.play();
            } else {
                scaleDown.play();
            }
        });

        textField.textProperty().addListener((obs, oldText, newText) -> {
            // Champ vide
            if (newText.isEmpty()) {
                System.out.println("Champ vide");
                confirm.setDisable(true);
                return;
            }

            // Nom trop court
            if (newText.length() < 3) {
                System.out.println("Nom trop court");
                confirm.setDisable(true);
                return;
            }

            // Caractères interdits
            if (!newText.matches("[a-zA-Z0-9_]+")) {
                System.out.println("Caractères interdits");
                confirm.setDisable(true);
                return;
            }

            if (Launcher.profilsExist(newText)) {
                System.out.println("Ce nom de profil existe déjà !");
                confirm.setDisable(true);
                return;
            }

            // Si tout est OK
            System.out.println("Nom valide : " + newText);
            confirm.setDisable(false);
        });


        // BUTTONS DESIGN
        confirm = new Button("Confirmer");
        cancel = new Button("Annuler");
        confirm.getStyleClass().add("popupButtons");
        cancel.getStyleClass().add("popupButtons");
        confirm.setDisable(true);

        TranslateTransition hoverTransitionConfirm = new TranslateTransition(Duration.seconds(0.1), confirm);
        confirm.setOnMouseEntered(e -> {
            hoverTransitionConfirm.stop();
            hoverTransitionConfirm.setToY(-4); // translation vers le haut
            hoverTransitionConfirm.play();
        });

        confirm.setOnMouseExited(e -> {
            hoverTransitionConfirm.stop();
            hoverTransitionConfirm.setToY(0); // retour à la position normale
            hoverTransitionConfirm.play();
        });

        TranslateTransition hoverTransitionCancel = new TranslateTransition(Duration.seconds(0.1), cancel);
        cancel.setOnMouseEntered(e -> {
            hoverTransitionCancel.stop();
            hoverTransitionCancel.setToY(-4); // translation vers le haut
            hoverTransitionCancel.play();
        });

        cancel.setOnMouseExited(e -> {
            hoverTransitionCancel.stop();
            hoverTransitionCancel.setToY(0); // retour à la position normale
            hoverTransitionCancel.play();
        });

        // HBox pour les boutons en bas
        buttonBox = new HBox(10.0);
        buttonBox.getChildren().addAll(confirm, cancel);
        buttonBox.setAlignment(Pos.CENTER_RIGHT); // boutons alignés à droite
        HBox.setHgrow(confirm, Priority.ALWAYS);   // Annuler à gauche
        cancel.setMaxWidth(Double.MAX_VALUE);

        // VBox pour empiler TextField et boutons
        vbox = new VBox(20.0);
        vbox.getChildren().addAll(textField, buttonBox);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPrefSize(WIDTH, HEIGHT);
        vbox.setStyle("-fx-padding: 20;"); // un peu de marge

        popupMainPane = new StackPane(vbox);
        popupMainPane.getStyleClass().add("popupMainPane");
        popupMainPane.setPrefSize(WIDTH, HEIGHT);
        popupMainPane.setFocusTraversable(true);

        return popupMainPane;
    }
}
