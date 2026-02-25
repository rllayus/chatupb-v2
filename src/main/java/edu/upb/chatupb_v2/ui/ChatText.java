/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.chatupb_v2.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JTextPane;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rlaredo
 */
@Getter
@Setter
public class ChatText extends JTextPane{
    Color borderColor = Color.BLUE;
    Color bgColor = Color.GREEN;

    public ChatText() {
        setBackground(new Color(0,0,0,0));
    }
    
    

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 =(Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() -1, getHeight() -1, 5, 5);
        super.paintComponent(g);
        
    }
    
    
    

}
