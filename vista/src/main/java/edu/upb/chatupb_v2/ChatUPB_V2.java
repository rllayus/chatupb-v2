/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.upb.chatupb_v2;

import edu.upb.chatupb_v2.controller.ContactController;
import edu.upb.chatupb_v2.controller.Mediador;
import edu.upb.chatupb_v2.controller.MessageController;
import edu.upb.chatupb_v2.view.ChatUI;
import edu.upb.chatupb_v2.model.network.ChatServer;

/**
 * @author rlaredo
 */
public class ChatUPB_V2 {

    public static void main(String[] args) throws Exception {
               /* Create and display the form */

        final ChatUI chatUI = new ChatUI();
        java.awt.EventQueue.invokeLater(() -> chatUI.setVisible(true));

        try {
            ChatServer chatServer = new ChatServer(Mediador.getInstance());
            chatServer.start();
        }catch (Exception e){
            e.printStackTrace();
        }

        Mediador.getInstance().setChatView(chatUI);

        MessageController messageController = new MessageController(chatUI);
        chatUI.setMessageController(messageController);

        ContactController contactController = new ContactController(chatUI);
        chatUI.setContactController(contactController);
        contactController.loadContacts();

    }
}
