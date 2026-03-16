package edu.upb.chatupb_v2.model.repository;

import edu.upb.chatupb_v2.model.entities.Contact;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EncryContactDao implements IContactDao {
    private final IContactDao daoOriginal;
    public EncryContactDao(IContactDao daoOriginal) {
        super();
        this.daoOriginal = daoOriginal;
    }

    public List<Contact> findAll() throws ConnectException, SQLException {
        List<Contact> contacts = daoOriginal.findAll();
        for (Contact contact : contacts) {
            contact.setName( contact.getName() );
        }
        return contacts;
    }

    public boolean existById(String id) throws ConnectException, SQLException {
        return daoOriginal.existById(id);
    }

    public Contact findById(String id) throws ConnectException, SQLException {
        Contact contact =  daoOriginal.findById(id);
        contact.setName( contact.getName() );
        return contact;
    }

    public boolean exist(String id) throws ConnectException, SQLException {
        return daoOriginal.exist(id);
    }


    public void save(Contact contact) throws Exception {
        contact.setName( contact.getName() );// estoy encritando
        daoOriginal.save(contact);
    }

    public void update(Contact contact) throws Exception {
        daoOriginal.update(contact);
    }

}
