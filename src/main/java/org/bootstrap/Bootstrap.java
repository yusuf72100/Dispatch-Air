package org.bootstrap;

import com.google.cloud.storage.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.Affichage.MainMenu;
import org.launcher.Launcher;
import org.launcher.Version;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class Bootstrap extends Application {
    /* Google Cloud Storage */
    private static String projectId = "dispatchair";
    private static String bucketName = "dispatchair";
    private static String objectName = "sample.txt";
    private static Version version;

    /* JavaFX */
    private double xOffset = 0;
    private double yOffset = 0;

    private static Scene Main;
    private static Stage primaryStage;

    private static final double WIDTH = 800;
    private static final double HEIGHT = 450;

    private static StackPane root;
    private static HBox header;

    public static void uploadFile() throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        String filePath = Launcher.normaliserChemin(Launcher.dossierAssets + "/sample.txt");

        storage.createFrom(blobInfo, Paths.get(filePath));
    }

    public static void downloadFiles(String objName, String file) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        BlobId blobId = BlobId.of(bucketName, objName);
        Blob blob = storage.get(blobId);

        String filePath = Launcher.normaliserChemin(Launcher.dossierAssets + file);

        blob.downloadTo(Paths.get(filePath));
    }

    /**
     * Met à jour le logiciel si nécessaire
     */
    private static void updateLauncher() throws IOException {
        downloadFiles("version.vs", "/new_version.vs");

        version = Version.deserialize(Launcher.dossierAssets + "/version.vs");
        Version newVersion = Version.deserialize(Launcher.dossierAssets + "/new_version.vs");

        System.out.println(version.getVersion() + " && " + newVersion.getVersion());

        if(!Objects.equals(version.getVersion(), newVersion.getVersion())) {
            boolean deleted = new File(Launcher.dossierAssets + "/version.vs").delete();
            boolean renamed = new File(Launcher.dossierAssets + "/new_version.vs").renameTo(new File(Launcher.dossierAssets + "/version.vs"));
        }
    }

    @Override
    public void start(Stage primary) throws Exception {
        try {
            primaryStage = primary;

            root = new StackPane();
            root.setPrefSize(WIDTH, HEIGHT);

            // Charger le GIF
            String cheminGifBackground = Launcher.normaliserChemin(Launcher.dossierAssets + "/img/bootstrap.gif");
            Image gifImage = new Image(Launcher.chargerFichierEnUrl(cheminGifBackground));

            // Créer un ImageView pour afficher le GIF
            ImageView gifView = new ImageView(gifImage);

            // Ajuster le ImageView pour qu'il remplisse la fenêtre
            gifView.setFitWidth(WIDTH); // Ajustez la largeur selon vos besoins
            gifView.setFitHeight(HEIGHT); // Ajustez la hauteur selon vos besoins
            gifView.setPreserveRatio(true); // Conserver les proportions de l'image

            // Ajouter l'ImageView à la racine (StackPane)
            root.getChildren().add(gifView);

            // Gestion de la scène
            Main = new Scene(root, WIDTH, HEIGHT, Color.TRANSPARENT);

            // Animation de fondu
            primary.setOpacity(0.0);
            primary.initStyle(StageStyle.UNDECORATED);
            primary.setScene(Main);
            primary.setTitle("Dispatch'Air");
            primary.setResizable(true);
            primary.setMaximized(false);
            primary.show();

            // Timeline pour l'animation de fondu
            Timeline fadeInTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), new KeyValue(primary.opacityProperty(), 1.0))
            );
            fadeInTimeline.setOnFinished(event -> {
                // Lancer l'animation du GIF après le fondu
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(pauseEvent -> {
                    // Remplacer le GIF par une image statique après 3 secondes
                    String cheminImageBackground = Launcher.normaliserChemin(Launcher.dossierAssets + "/img/bootstrap.png");
                    Image staticImage = new Image(Launcher.chargerFichierEnUrl(cheminImageBackground));

                    // Créer un nouvel ImageView pour l'image statique
                    ImageView staticImageView = new ImageView(staticImage);

                    // Ajuster le ImageView pour qu'il remplisse la fenêtre
                    staticImageView.setFitWidth(WIDTH);
                    staticImageView.setFitHeight(HEIGHT);
                    staticImageView.setPreserveRatio(true);

                    // Remplacer l'ImageView du GIF par l'ImageView de l'image statique
                    root.getChildren().clear();
                    root.getChildren().add(staticImageView);
                });
                pause.play();
            });
            fadeInTimeline.play(); // Démarrer l'animation de fondu

            // Mouvement fenêtre
            root.setOnMousePressed(event -> {
                xOffset = event.getScreenX() - primaryStage.getX();
                yOffset = event.getScreenY() - primaryStage.getY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

            primary.setOnCloseRequest(event -> {
                Platform.exit();
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
    public static void stageFadeIn() {
        // Assurez-vous que la modification de l'opacité se fait sur le JavaFX Application Thread
        Platform.runLater(() -> {
            double opacity = 0.0;

            // Animation de fondu
            for (int i = 0; i <= 100; i++) {
                final double currentOpacity = i / 100.0;  // Calculer l'opacité (entre 0 et 1)

                // Mettre à jour l'opacité dans le JavaFX thread
                primaryStage.setOpacity(currentOpacity);

                try {
                    // Pause entre chaque étape de l'animation
                    Thread.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
