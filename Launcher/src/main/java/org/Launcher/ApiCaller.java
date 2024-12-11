package org.Launcher;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class ApiCaller {

    public static String makeApiCall(String apiUrl, String apiKey, String jsonBody) {
        String result = "";
        try {
            // Encodage de la clé API pour l'authentification basique
            String encodedApiKey = Base64.getEncoder().encodeToString((apiKey + ":").getBytes());

            // Créer un client HTTP
            HttpClient client = HttpClient.newHttpClient();

            // Construire la requête avec authentification et corps JSON
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .header("Authorization", "Basic " + encodedApiKey) // Authentification basique
                    .header("Content-Type", "application/json") // Type de contenu JSON
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody)) // Corps de la requête
                    .build();

            // Envoyer la requête et attendre la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Vérifier le code de réponse HTTP
            if (response.statusCode() == 200 || response.statusCode() == 201) { // Succès
                result = response.body();
            } else {
                System.out.println("API call failed with status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*public static void main(String[] args) {
        String apiUrl = "https://api.flightplandatabase.com/plan"; // URL de l'API
        String apiKey = "N5OeQ9GpKVVVvHFwn5pVjV4stSWvKSDep5ZV25Z3"; // Remplacez par votre clé API
        String jsonBody = "{"
            + "\"fromICAO\": \"EHAM\","
            + "\"toICAO\": \"KJFK\","
            + "\"fromName\": \"Schiphol\","
            + "\"toName\": \"John F Kennedy Intl\","
            + "\"tags\": [\"atlantic\"],"
            + "\"route\": {"
            + "  \"nodes\": ["
            + "    {"
            + "      \"ident\": \"EHAM\","
            + "      \"type\": \"APT\","
            + "      \"lat\": 52.31485,"
            + "      \"lon\": 4.75812,"
            + "      \"alt\": 0,"
            + "      \"name\": \"Schiphol\","
            + "      \"via\": null"
            + "    },"
            + "    {"
            + "      \"ident\": \"KJFK\","
            + "      \"type\": \"APT\","
            + "      \"lat\": 40.63990,"
            + "      \"lon\": -73.77666,"
            + "      \"alt\": 0,"
            + "      \"name\": \"John F Kennedy Intl\","
            + "      \"via\": null"
            + "    }"
            + "  ]"
            + "}"
            + "}";

        String response = makeApiCall(apiUrl, apiKey, jsonBody);
        System.out.println("Response from API: " + response);
    }*/
}