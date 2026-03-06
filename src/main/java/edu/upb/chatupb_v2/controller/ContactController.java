package edu.upb.chatupb_v2.controller;

import edu.upb.chatupb_v2.model.repository.ContactDao;
import edu.upb.chatupb_v2.view.interfaces.IChatView;

public class ContactController {
    private final ContactDao contactDao;
    private final IChatView chatView;
    public ContactController(IChatView chatView) {
        this.contactDao = new ContactDao();
        this.chatView = chatView;
    }

    public void loadContacts() throws Exception{
        chatView.onLoadData(this.contactDao.findAll());
    }

}
