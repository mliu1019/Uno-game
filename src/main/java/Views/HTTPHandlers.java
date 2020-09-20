package Views;


import CardPackage.Card;
import Models.PlayCommand;
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
        PlayCommand cmd = new PlayCommand();
        cmd.setCommand(PlayCommand.CMD.PLAY);
        cmd.setPlayerID(playerID);
        cmd.setIndex(index);

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(cmd);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String route = "/game/makeplay";

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

    public static void setWildAndPlay(String playerID, int index, Card.Color color) {
        PlayCommand cmd = new PlayCommand();
        cmd.setCommand(PlayCommand.CMD.WILD);
        cmd.setPlayerID(playerID);
        cmd.setIndex(index);
        cmd.setWildColor(color);

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(cmd);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String route = "/game/makeplay";

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

    public static void drawCard(String playerID) {
        PlayCommand cmd = new PlayCommand();
        cmd.setCommand(PlayCommand.CMD.DRAW);
        cmd.setPlayerID(playerID);

        var objectMapper = new ObjectMapper();
        String requestBody = null;
        try {
            requestBody = objectMapper.writeValueAsString(cmd);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String route = String.format("/game/makeplay");
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

    public static void endPlay(String playerID) {
        String route = String.format("/game/endplay?playerID=%s", playerID);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .uri(URI.create(HOST + route))
                .PUT(HttpRequest.BodyPublishers.ofString(""))
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
