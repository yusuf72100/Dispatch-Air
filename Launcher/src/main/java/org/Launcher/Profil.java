package org.Launcher;

import org.Vol.Vol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Profil implements Serializable {
    private static final long serialVersionUID = 1L;

    private String Nom;
    private List<Vol> listVols;

    public Profil(String Nom) {
        this.Nom = Nom;
        this.listVols = this.getListVols();
    }

    public List<Vol> getListVols() {
        File folder = new File(Launcher.chargerFichierEnUrl(Launcher.normaliserChemin(Launcher.dossierProfils + "/" + this.Nom + Launcher.dossierVolsAvailable)));
        List<Vol> deserializedObjects = new ArrayList<>();

        if (!folder.isDirectory()) {
            System.out.println("Le chemin fourni n'est pas un dossier !");
            return deserializedObjects;
        }

        // Filtrer les fichiers pour ne prendre que les .flight
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".flight"));
        if (files == null || files.length == 0) {
            System.out.println("Aucun fichier sérialisé trouvé dans le dossier.");
            return deserializedObjects;
        }

        // Désérialiser chaque fichier
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
