package edu.upb.chatupb_v2.model.entities;

import edu.upb.chatupb_v2.model.network.SocketClient;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

public class TextMessage extends AbstractMessage implements Model {
    private String id;
    private String userId;
    private String message;

    public TextMessage() {
        super("007");
    }

    public TextMessage(String userId, String message) {
        super("007");
        this.userId = userId;
        this.id = UUID.randomUUID().toString();
        this.message = message;
    }

    public TextMessage(String userId, String id, String message) {
        super("007");
        this.userId = userId;
        this.id = id;
        this.message = message;
    }

    @Override
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String horaActual = LocalTime.now().format(timeFormatter);
        return String.format("[%s] Yo: %s", horaActual, message);
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + userId + "|" + id + "|" + message + System.lineSeparator();
    }

    @Override
    public void execute(SocketClient client) throws IOException {
        client.send(this);
    }

    public static TextMessage parse(String trama) {
        String[] split = trama.split(Pattern.quote("|"));
        if(split.length != 4) {
            System.out.println("ERROR: Formato de trama no valido");
            return null;
        }
        return new TextMessage(split[1], split[2],split[3]);
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
