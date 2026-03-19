package edu.upb.chatupb_v2.view;

import javax.swing.*;
import java.awt.*;

public class ButtonUtil {
    public static void configurar(JButton  button, String nombre) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setName(nombre);
    }
}
