package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoImpl implements UserDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public List<User> getAll() {
        ArrayList<User> users = new ArrayList<>();
        String selectStatement = "SELECT * FROM users;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getAllUsers = conn.prepareStatement(selectStatement)) {

            ResultSet rs = getAllUsers.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getLong("User_Id"),
                        rs.getString("User_Name"),
                        rs.getString("Password"),
                        L10nUtil.utcToLocal(rs.getTimestamp("Create_Date")),
                        rs.getString("Created_By"),
                        L10nUtil.utcToLocal(rs.getTimestamp("Last_Update")),
                        rs.getString("Last_Updated_By")
                );
                users.add(user);
            }
        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        }

        sysLogger.info(users.size() + " Users returned by database.");
        return users;
    }

    @Override
    public User getById(long id) {
        return null;
    }

    @Override
    public User getByNameAndPassword(String username, String password) {
        return null;
    }
}
