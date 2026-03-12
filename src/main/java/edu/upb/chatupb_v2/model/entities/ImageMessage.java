package edu.upb.chatupb_v2.model.entities;

import edu.upb.chatupb_v2.model.network.SocketClient;

import java.io.IOException;
import java.util.Base64;

public class ImageMessage extends AbstractMessage implements Model {
    private String userId;
    private byte[] image;
    public ImageMessage() {
        super("021");
    }
    public ImageMessage(String userId, byte[] image) {
        super("021");
        this.userId=userId;
        this.image = image;
    }

    @Override
    public String generarTrama() {
        return getCodigo() + "|" + userId+"|" + Base64.getEncoder().encodeToString(image) + System.lineSeparator();
    }

    @Override
    public void execute(SocketClient client) throws IOException {
        client.send(this);
        //Guardar en Base Datos
    }

    @Override
    public void setId(String id) {
        this.userId = id;
    }

    @Override
    public String getId() {
        return this.userId;
    }
}
