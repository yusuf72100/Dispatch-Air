package org.bootstrap;

import com.google.cloud.storage.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private static Stage primaryStage;

    private static final double WIDTH = 800;
    private static final double HEIGHT = 450;

    private static ProgressBar progressBar;

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
            int waiting = 0;

            // Construire la commande avec ProcessBuilder
            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-jar");
            command.add(jarFilePath);
            command.addAll(Arrays.asList(args)); // Ajouter les arguments supplémentaires

            ProcessBuilder processBuilder = new ProcessBuilder(command);

            // Rediriger la sortie et les erreurs pour les afficher dans la console actuelle
            processBuilder.inheritIO();

            // Démarrer le processus
            Process process = processBuilder.start();

            // Vérifier si le processus a démarré correctement
            while(!process.isAlive() && waiting < 10000) {
                Thread.sleep(100); // Petite pause pour laisser le processus se stabiliser
                waiting += 100;
            }

            // On vérifie la sortie ( échec ou success )
            if(waiting < 10000) {
                System.out.println("Le fichier JAR a été démarré avec succès !");
            } else {
                // Problème de lancement ( a pris trop de temps à se lancer )
            }

        } catch (IOException | InterruptedException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour le logiciel si nécessaire
     */
    private static void updateLauncher() throws IOException, InterruptedException {
        File versionFile = new File("DispatchAir/version.vs");
        File launcherFile = new File("Launcher.jar");

        if (InternetChecker.isInternetAvailable()) {
            progressBar.setProgress(0.1); // Étape 1

            // Update launcher
            if (versionFile.exists() && launcherFile.exists()) {
                System.out.println("Téléchargement du fichier version...");
                downloadFiles("version.vs", "DispatchAir/new_version.vs");
                progressBar.setProgress(0.3); // Étape 2

                version = Version.deserialize("DispatchAir/version.vs");
                Version newVersion = Version.deserialize("DispatchAir/new_version.vs");

                if (!Objects.equals(version.getVersion(), newVersion.getVersion())) {
                    System.out.println("Update en cours...");

                    new File("DispatchAir/version.vs").delete();
                    new File("DispatchAir/new_version.vs").delete();

                    System.out.println("Téléchargement de la mise à jour en cours...");
                    downloadFiles("Launcher.jar", "Launcher.jar");
                    progressBar.setProgress(0.8); // Étape 3

                    System.out.println("Téléchargement terminé!");
                } else {
                    new File("DispatchAir/new_version.vs").delete();
                    System.out.println("Launcher déjà à jour!");
                }
            } else {
                System.out.println("Téléchargement de la mise à jour en cours...");
                downloadFiles("Launcher.jar", "Launcher.jar");
                progressBar.setProgress(0.8); // Étape 3
                System.out.println("Téléchargement terminé!");
            }

            // On vérifie si tout s'est bien passé
            if (launcherFile.exists()) {
                progressBar.setProgress(1.0); // Étape finale
                System.out.println("Lancement du launcher...");
                executeJar("Launcher.jar", "--tools-launch");
            }
        } else {
            // Une erreur est survenue (pas d'internet)
        }

        // Fermer le bootstrap une fois Launcher.jar lancé
        System.out.println("Fermeture du bootstrap...");
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void start(Stage primary) throws Exception {
        try {
            primaryStage = primary;

            // Racine principale avec StackPane pour empiler les éléments
            StackPane root = new StackPane();
            root.setPrefSize(WIDTH, HEIGHT);
            root.setStyle("-fx-background-color: transparent;");
            root.setAlignment(Pos.CENTER); // Centrer les éléments

            // Charger le GIF
            Image gifImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BOOT-INF/classes/ressources/assets/img/bootstrap.gif")));

            // Créer un ImageView pour afficher le GIF
            ImageView gifView = new ImageView(gifImage);

            // Ajuster le ImageView pour qu'il remplisse la fenêtre
            gifView.setFitWidth(WIDTH);
            gifView.setFitHeight(HEIGHT); // Laisser de la place pour la ProgressBar
            gifView.setPreserveRatio(true); // Conserver les proportions de l'image

            // Barre de progression en bas de la fenêtre
            progressBar = new ProgressBar(0);
            progressBar.setPrefWidth(WIDTH); // La largeur de la ProgressBar correspond à la fenêtre
            progressBar.setMaxHeight(10); // Ajuster la hauteur si nécessaire
            StackPane.setAlignment(progressBar, Pos.BOTTOM_CENTER);

            // Ajouter le GIF et la ProgressBar dans le StackPane
            root.getChildren().addAll(gifView, progressBar);

            // Gestion de la scène
            Scene mainScene = new Scene(root, WIDTH, HEIGHT, Color.TRANSPARENT);

            mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/BOOT-INF/classes/ressources/assets/style.css")).toExternalForm());

            // Animation de fondu
            primary.setOpacity(0.0);
            primary.initStyle(StageStyle.UNDECORATED);
            primary.setScene(mainScene);
            primary.setTitle("Dispatch'Air");
            primary.setResizable(false);
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

                    // Remplacer le contenu de la VBox
                    root.getChildren().set(0, staticImageView); // Remplacer l'ImageView du GIF par l'image statique
                    root.setPrefSize(WIDTH, HEIGHT);
                    root.setAlignment(Pos.CENTER); // Centrage des éléments

                    // Appeler updateLauncher après que tout soit terminé
                    new Thread(() -> {
                        try {
                            updateLauncher();
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
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
        {
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
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}