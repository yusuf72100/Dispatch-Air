package org.bootstrap;

import com.google.cloud.storage.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class Main extends Application {
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

        String filePath = "/sample.txt";

        storage.createFrom(blobInfo, Paths.get(filePath));
    }

    public static void downloadFiles(String objName, String file) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();

        BlobId blobId = BlobId.of(bucketName, objName);
        Blob blob = storage.get(blobId);

        blob.downloadTo(Paths.get(file));
    }

    // Méthode pour exécuter un fichier .jar spécifique
    private static void executeJar(String jarFilePath, String... args) {
        try {
            // Construire la commande pour exécuter le JAR avec les arguments
            StringBuilder command = new StringBuilder("java -jar " + jarFilePath);

            // Ajouter les arguments si présents
            for (String arg : args) {
                command.append(" ").append(arg);
            }

            // Démarrer le processus
            Process process = Runtime.getRuntime().exec(command.toString());

            // Attendre que le processus se termine avant de continuer (si nécessaire)
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Met à jour le logiciel si nécessaire
     */
    private static void updateLauncher() throws IOException {
        File versionFile = new File("DispatchAir/version.vs");
        File launcherFile = new File("Launcher.jar");

        if(versionFile.exists() && launcherFile.exists()) {
            downloadFiles("version.vs", "DispatchAir/new_version.vs");

            version = Version.deserialize("DispatchAir/version.vs");
            Version newVersion = Version.deserialize("DispatchAir/new_version.vs");

            if(!Objects.equals(version.getVersion(), newVersion.getVersion())) {
                System.out.println("Update en cours...");
                boolean deleted = new File("DispatchAir/version.vs").delete();
                deleted = new File("DispatchAir/new_version.vs").delete();

                downloadFiles("Launcher.jar", "Launcher.jar");

            } else {
                /* Déjà à jour */
                boolean deleted = new File("DispatchAir/new_version.vs").delete();
            }
        } else {
            downloadFiles("Launcher.jar", "Launcher.jar");
        }

        // Lancer Launcher.jar dans un nouveau thread pour éviter de bloquer le processus du bootstrap
        new Thread(() -> {
            try {
                executeJar("Launcher.jar", "--tools-launch");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Fermer le bootstrap une fois Launcher.jar lancé
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void start(Stage primary) throws Exception {
        try {
            primaryStage = primary;

            root = new StackPane();
            root.setPrefSize(WIDTH, HEIGHT);

            // Charger le GIF
            Image gifImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BOOT-INF/classes/ressources/assets/img/bootstrap.gif")));

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
                    Image staticImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BOOT-INF/classes/ressources/assets/img/bootstrap.png")));

                    // Créer un nouvel ImageView pour l'image statique
                    ImageView staticImageView = new ImageView(staticImage);

                    // Ajuster le ImageView pour qu'il remplisse la fenêtre
                    staticImageView.setFitWidth(WIDTH);
                    staticImageView.setFitHeight(HEIGHT);
                    staticImageView.setPreserveRatio(true);

                    // Remplacer l'ImageView du GIF par l'ImageView de l'image statique
                    root.getChildren().clear();
                    root.getChildren().add(staticImageView);

                    // Appeler updateLauncher après que tout soit terminé
                    try {
                        updateLauncher();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
            primary.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/ressources/assets/img/logo.png")).toExternalForm()));
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

    public static void main(String[] args) {
        launch(args);
    }
}
