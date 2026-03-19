package edu.upb.chatupb_v2.controller.interfaces;

import edu.upb.chatupb_v2.model.entities.AbstractMessage;
import edu.upb.chatupb_v2.model.entities.Contact;
import edu.upb.chatupb_v2.model.network.SocketClient;

import java.util.List;

public interface IChatView {
    void onLoadData(List<Contact> contacts);
    void onMessage(AbstractMessage invitacion);
}
