package edu.upb.chatupb_v2.model.repository;

import edu.upb.chatupb_v2.model.entities.Contact;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class ContactDao extends DaoHelper<Contact> {
    public static final class Column{
        public static final String ID= "id";
        public static final String CODE ="code";
        public static final String NAME ="name";
        public static final String IP ="ip";

    }

    public ContactDao() {
        super();
    }

    DaoHelper.ResultReader<Contact> resultReader = result -> {
        Contact contact = new Contact();
        if (existColumn(result, Column.ID)) {
            contact.setId(result.getString(Column.ID));
        }
        if (existColumn(result, Column.CODE)) {
            contact.setCode(result.getString(Column.CODE));
        }
        if (existColumn(result, Column.NAME)) {
            contact.setName(result.getString(Column.NAME));
        }
        if (existColumn(result, Column.IP)) {
            contact.setIp(result.getString(Column.IP));
        }
        return contact;
    };

    public boolean existColumn(ResultSet result, String columnName) {
        try {
            result.findColumn(columnName);
            return true;
        } catch (SQLException sqlex) {
            //log.error("No se encontro la columna: {}", columnName); // log innecesario
        }
        return false;
    }

    public List<Contact> findAll() throws ConnectException, SQLException {
        String query = "SELECT * FROM contact";
        return executeQuery(query, resultReader);
    }

    public boolean exist(String argument) throws ConnectException, SQLException {
        String query = "SELECT count(*) FROM contact WHERE " + argument;
        return executeQueryCount(query, null) == 1;
    }

    public boolean existByCode(String code) throws ConnectException, SQLException {
        String query = "SELECT count(*) FROM contact WHERE code='" + code + "'";
        return executeQueryCount(query, null) == 1;
    }

    public Contact findByCode(String code) throws ConnectException, SQLException {
        String query = "SELECT * FROM contact WHERE code ='" + code + "'";
        System.out.println(query);
        List<Contact> list = executeQuery(query, resultReader);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public void update(String query) throws Exception {
        super.update(query, null);
    }

    public void save(Contact contact) throws Exception {
        String query = "INSERT INTO contact(id, name, ip) values (?,?,?)";
        DaoHelper.QueryParameters params = new DaoHelper.QueryParameters() {
            @Override
            public void setParameters(PreparedStatement pst) throws SQLException {
                pst.setString(1, contact.getId());
                pst.setString(2, contact.getName());
                pst.setString(3, contact.getIp());
            }
        };
        insert(query, params, contact);
    }

    public void update(Contact contact) throws Exception {
        String query = "UPDATE contact SET IP=? WHERE code =?";
        DaoHelper.QueryParameters params = new DaoHelper.QueryParameters() {
            @Override
            public void setParameters(PreparedStatement pst) throws SQLException {
                pst.setString(1, contact.getIp());
                pst.setString(2, contact.getCode());
            }
        };
        super.update(query, params);
    }

    public void update(String query, String conditionWhere) throws SQLException, ConnectException {
        if (query.trim().endsWith("%s")) {
            query = String.format(query, conditionWhere);
        } else {
            query = String.format("%s %s", query, conditionWhere);
        }
        super.update(query, null);
    }
}
