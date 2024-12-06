package org.Affichage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class CustomObjectInputStream extends ObjectInputStream {
    public CustomObjectInputStream(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        // Si le nom de la classe sérialisée est "org.bootstrap.version.Version",
        // on redirige vers la nouvelle classe "org.launcher.Version"
        if ("org.launcher.Version".equals(desc.getName())) {
            return Class.forName("org.bootstrap.Version");
        }
        return super.resolveClass(desc);
    }
}
