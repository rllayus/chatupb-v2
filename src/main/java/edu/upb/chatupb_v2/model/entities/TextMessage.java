package edu.upb.chatupb_v2.model.entities;

import edu.upb.chatupb_v2.model.network.SocketClient;

import java.io.IOException;

public class TextMessage extends AbstractMessage implements Model {
    private String id;
    private String userId;
    private String text;
    private String message;

    public TextMessage() {
        super("007");
    }

    public TextMessage(String userId, String id, String message) {
        super("007");
        this.userId = userId;
        this.id = id;
        this.message = message;
    }


    @Override
    public String generarTrama() {
        return getCodigo() + "|" + userId + "|" + id + "|" + message + System.lineSeparator();
    }

    @Override
    public void execute(SocketClient client) throws IOException {
        client.send(this);
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
