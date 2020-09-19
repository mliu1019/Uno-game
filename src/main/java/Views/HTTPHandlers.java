package Views;


import CardPackage.Card;
import Models.WildCardPlay;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPHandlers {
    private static final String HOST = "http://127.0.0.1:8080";

    public static void playCard(String playerID, int index) {
        String query = String.format("playerID=%s", playerID);
        String route = String.format("/game/play/%d", index);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(HOST + route + "?" + query))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(response.body());

    }

    public static void setWildAndPlay(String playerID, int index, Card.Color color) {
        WildCardPlay cp = new WildCardPlay(playerID, index, color);
        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper
                    .writeValueAsString(cp);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println(requestBody);

        String route = String.format("/game/color");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .uri(URI.create(HOST + route))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(response.body());
    }
}
