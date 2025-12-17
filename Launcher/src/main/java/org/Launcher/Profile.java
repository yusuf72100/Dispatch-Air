package org.Launcher;

import org.Vol.Vol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;

    private String Nom;
    private List<Vol> listVols;

    // Constructeur
    public Profile(String Nom) {
        this.Nom = Nom;
        this.listVols = this.getListVols();
    }

    public String getNom(){
        return this.Nom;
    }

    public int serialiseProfile() {
        String chemin = Launcher.dossierProfils + "/" + this.Nom + "/profil.prf";

        File fichier = new File(chemin);

        // Création des dossiers si nécessaires
        fichier.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(this);
            System.out.println("Profil sérialisé avec succès : " + fichier.getAbsolutePath());
            return 0;

        } catch (IOException e) {
            System.out.println("Erreur lors de la sérialisation du profil");
            e.printStackTrace();
            return 1;
        }
    }

    public List<Vol> getListVols() {
        File folder = new File(Launcher.chargerFichierEnUrl(Launcher.normaliserChemin(Launcher.dossierProfils + "/" + this.Nom + Launcher.dossierVolsAvailable)));
        List<Vol> deserializedObjects = new ArrayList<>();

        if (!folder.isDirectory()) {
            System.out.println("Le chemin fourni n'est pas un dossier !");
            return null;
        }

        // On filtre les fichiers pour ne prendre que les .flight
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".flight"));
        if (files == null || files.length == 0) {
            System.out.println("Aucun fichier sérialisé trouvé dans le dossier.");
            return null;
        }

        // Déssérialisation de chaque fichier
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                deserializedObjects.add((Vol) obj);
                System.out.println("Désérialisé : " + obj);

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la désérialisation de " + file.getName());
                e.printStackTrace();
            }
        }

        return deserializedObjects;
    }
}
