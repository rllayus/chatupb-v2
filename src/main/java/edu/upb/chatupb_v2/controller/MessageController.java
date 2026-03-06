package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.entities.TextMessage;
import edu.upb.chatupb_v2.model.repository.MessageDao;
import edu.upb.chatupb_v2.view.interfaces.IChatView;

import java.util.List;

public class MessageController {
    private MessageDao messageDao;
    private IChatView chatView;

    public MessageController(IChatView chatView) {
        messageDao = new MessageDao();
        this.chatView = chatView;
    }

    public List<TextMessage>  getMessages() throws Exception {
        return messageDao.findAll();
    }


}
