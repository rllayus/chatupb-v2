package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.controller.exception.OperationException;
import edu.upb.chatupb_v2.model.entities.AbstractMessage;
import edu.upb.chatupb_v2.model.entities.Invitacion;
import edu.upb.chatupb_v2.model.entities.Aceptar;
import edu.upb.chatupb_v2.model.network.SocketClient;
import edu.upb.chatupb_v2.model.network.SocketListener;
import edu.upb.chatupb_v2.model.repository.ContactDao;
import edu.upb.chatupb_v2.model.repository.MessageDao;
import edu.upb.chatupb_v2.view.interfaces.IChatView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Mediador implements SocketListener {
    private static final Mediador INSTANCE = new Mediador();
    private IChatView chatView;
    private ContactDao contactDao;
    private MessageDao messageDao;

    private final HashMap<String, SocketClient>  clients = new HashMap<>();
    private Mediador() {
        this.contactDao = new ContactDao();
        this.messageDao = new MessageDao();
    }

    public static Mediador getInstance() {
        return INSTANCE;
    }

    public void addClient(String userId, SocketClient client) {
        this.clients.put(userId, client);
    }

    public void removeClient(String userId) {
        this.clients.remove(userId);
    }

    public IChatView getChatView() {
        return chatView;
    }

    public void setChatView(IChatView chatView) {
        this.chatView = chatView;
    }

    public void sendMessage(String userId, AbstractMessage abstractMessage) {
        SocketClient client = this.clients.get(userId);
        if (client == null) {
            System.out.println("Client not found");
            return;
        }

        try {
            abstractMessage.execute(client);
        }catch (Exception e) {
            System.out.println();
        }
        System.out.println("Enviado mensaje: " + abstractMessage.generarTrama());
        //Guardar mensaje en base de datos
    }

    public void invitacion(String ip){
        SocketClient client;
        try {
            client = new SocketClient(ip);
            client.addListener(this);
            client.start();
        }catch (Exception e) {
            throw new OperationException("No se logró establecer la conexión");
        }

        Invitacion invitacion = new Invitacion();
        invitacion.setIdUsuario("MI_ID");
        invitacion.setNombre("Mi nombre");
        try {
            client.send(invitacion);
        } catch (IOException e) {
            throw new OperationException("No se logró enviar el mensaje");
        }
    }

    @Override
    public void onMessage(SocketClient socketClient, AbstractMessage message) {
        if(message instanceof Invitacion invitacion) {
            clients.put(invitacion.getIdUsuario(), socketClient);
        }
        chatView.onMessage(message);
    }


    @Override
    public void onCloseConnection(SocketClient socketClient) {
        System.out.println("onCloseConnection: "+socketClient.getIp());
        Set<String> keys = this.clients.keySet();
        for (String key : keys) {
            SocketClient client = this.clients.get(key);
            if(client.getIp().equals(socketClient.getIp())) {
                clients.remove(key);
                System.out.println("Eliminado cliente: " + key);
            }
        }
    }
}
