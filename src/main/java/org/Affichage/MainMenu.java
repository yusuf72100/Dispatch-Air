package org.Affichage;

import javafx.scene.layout.StackPane;

public class MainMenu implements Menu {
    protected static StackPane mainPane;

    /**
     * Cette méthode renvoi la forme du menu (en l'occurence, un StackPane)
     * @param WIDTH
     * @param HEIGHT
     * @return
     */
    public static StackPane getMenu(Double WIDTH, Double HEIGHT) {
        mainPane = new StackPane();
        mainPane.setPrefSize(WIDTH, HEIGHT);
        return mainPane;
    }
}
