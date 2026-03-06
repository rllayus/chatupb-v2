package edu.upb.chatupb_v2.model.network;

import edu.upb.chatupb_v2.model.entities.AbstractMessage;

public interface SocketListener {
    void onMessage(SocketClient socketClient, AbstractMessage invitacion);
    void onCloseConnection(SocketClient socketClient);
}
