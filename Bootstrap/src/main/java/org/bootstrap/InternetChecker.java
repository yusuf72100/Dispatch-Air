package org.bootstrap;

import java.net.InetAddress;

/**
 * Vérifie si l'utilisateur a accès à internet
 */
public class InternetChecker {
    public static boolean isInternetAvailable() {
        try {
            // vérifie si la machine peut résoudre un nom d'hôte (par exemple, google.com)
            InetAddress address = InetAddress.getByName("www.google.com");
            return address != null && address.isReachable(5000);  // attente de 2 secondes

        } catch (Exception e) {
            return false;
        }
    }
}
