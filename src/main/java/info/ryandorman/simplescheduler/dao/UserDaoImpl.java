package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.model.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public List<User> getAll() {
        String selectStatement = "SELECT * FROM users;";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getAllUsers = conn.prepareStatement(selectStatement)) {

            ResultSet rs = getAllUsers.executeQuery();
            // TODO: use rs
        } catch (SQLException | IOException throwables) {
            logger.severe(throwables.getMessage());
            throwables.printStackTrace();
        }
        return null;
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
