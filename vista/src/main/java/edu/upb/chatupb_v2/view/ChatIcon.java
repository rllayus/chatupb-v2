/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.view;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author rlaredo
 */
public class ChatIcon extends JLabel {

    public ChatIcon() {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/people.png"));
        Image imgScaled = imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(imgScaled);
        setIcon(scaledIcon);
    }

}
