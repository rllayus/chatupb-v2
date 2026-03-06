package edu.upb.chatupb_v2.model.entities;

public class TextMessage extends AbstractMessage implements Model {
    private String id;
    public TextMessage() {
        super("070");
    }

    @Override
    public String generarTrama() {
        return "";
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
