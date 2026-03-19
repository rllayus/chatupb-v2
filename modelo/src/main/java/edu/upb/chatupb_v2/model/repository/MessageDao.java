package edu.upb.chatupb_v2.model.repository;

import edu.upb.chatupb_v2.model.entities.TextMessage;

import java.net.ConnectException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageDao extends DaoHelper<TextMessage> {
    public static final class Column{
        public static final String ID= "id";
        public static final String NAME ="name";

    }
    public MessageDao() {
        super();
    }

    ResultReader<TextMessage> resultReader = result -> {
        TextMessage message = new TextMessage();
        if (existColumn(result, Column.ID)) {
            message.setId(result.getString(Column.ID));
        }

        return message;
    };

    public static boolean existColumn(ResultSet result, String columnName) {
        try {
            result.findColumn(columnName);
            return true;
        } catch (SQLException sqlex) {
            //log.error("No se encontro la columna: {}", columnName); // log innecesario
        }
        return false;
    }

    public List<TextMessage> findAll() throws Exception {
        try {
            String query = "SELECT * FROM text_message";
            return executeQuery(query, resultReader);
        }catch (ConnectException| SQLException e){
            throw new Exception("adasda");
        }
    }
}
