package org.bootstrap;

import java.io.*;

public class Version implements Serializable, Comparable<Version> {
    private static final long serialVersionUID = 1L;
    private String version;

    public Version(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void serialize(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Version deserialize(String fileName) {
        Version version = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            version = (Version) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error deserializing file: " + fileName);
            e.printStackTrace();
            // Retourner une valeur par défaut si la désérialisation échoue
            version = new Version("alpha-0.0.0"); // Exemple de valeur par défaut
        }
        return version;
    }

    @Override
    public int compareTo(Version o) {
        return o.getVersion().compareTo(this.version);
    }

    /*public static void main(String[] args) {
        Version version = new Version("alpha-0.0.1");

        // Serialize the object
        version.serialize("version.vs");
    }*/
}