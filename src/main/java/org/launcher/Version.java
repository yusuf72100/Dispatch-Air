package org.launcher;

import java.io.*;

public class Version implements Serializable {
    private static final long serialVersionUID = 1L;
    private String version;

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
            e.printStackTrace();
        }
        return version;
    }

    public static void main(String[] args) {
        Version version = new Version();
        version.setVersion("alpha-0.0.1");

        // Serialize the object
        version.serialize("version.vs");
    }
}