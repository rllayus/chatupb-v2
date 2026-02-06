/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.upb.chatupb_v2;

import edu.upb.chatupb_v2.repository.Contact;
import edu.upb.chatupb_v2.repository.ContactDao;

import java.util.List;

/**
 *
 * @author rlaredo
 */
public class ChatUPB_V2 {

    public static void main(String[] args) {
        ContactDao contactDao = new ContactDao();
        try {
            List<Contact> contacts = contactDao.findAll();
            for (Contact contact : contacts) {
                System.out.println(contact.getName());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
