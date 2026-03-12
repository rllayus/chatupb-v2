package edu.upb.chatupb_v2.model.factory;

import edu.upb.chatupb_v2.model.entities.AbstractMessage;
import edu.upb.chatupb_v2.model.entities.Aceptar;
import edu.upb.chatupb_v2.model.entities.Invitacion;

import java.util.regex.Pattern;

public class MessageFactory {
    public static AbstractMessage createMessage(String message) {
        String[] split = message.split(Pattern.quote("|"));
        if (split.length == 0) {
            throw  new IllegalArgumentException("Invalid message");
        }

        switch (split[0]) {
            case "001": {
                return Invitacion.parse(message);
            }
            case "002": {
                return Aceptar.parse(message);

            }
            default:
                throw new IllegalArgumentException("Tipo de mensaje desconocido");
        }
    }
}
