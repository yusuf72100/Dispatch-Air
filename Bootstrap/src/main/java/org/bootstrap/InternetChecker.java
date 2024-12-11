package org.bootstrap;

import java.net.InetAddress;

/**
 * Vérifie si l'utilisateur a accès à internet
 */
public class InternetChecker {
    public static boolean isInternetAvailable() {
        try {
            // Vérifie si la machine peut résoudre un nom d'hôte (par exemple, google.com)
            InetAddress address = InetAddress.getByName("www.google.com");
            return address != null && address.isReachable(5000);  // Attente de 2 secondes
        } catch (Exception e) {
            return false;
        }
    }
}
