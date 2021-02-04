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

    private static final String GET_ALL = "SELECT * FROM users;";
    private static final String GET_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
    private static final String GET_BY_NAME = "SELECT * FROM users WHERE user_name = ?;";

    @Override
    public List<User> getAll() {
        ArrayList<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = mapResult(rs);
                users.add(user);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            sysLogger.severe(e.getStackTrace().toString());
        }

        sysLogger.info(users.size() + " Users returned from database by UserDao.getAll");
        return users;
    }

    @Override
    public User getById(int id) {
        User user = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BY_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user = mapResult(rs);
                sysLogger.info(user.getName() + " returned from database by UserDao.getById");
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            sysLogger.severe(e.getStackTrace().toString());
        }

        return user;
    }

    @Override
    public User getByName(String name) {
        User user = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BY_NAME)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user = mapResult(rs);
                sysLogger.info(user.getName() + " returned from database by UserDao.getByName");
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            sysLogger.severe(e.getStackTrace().toString());
        }

        return user;
    }

    private User mapResult(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("user_name"),
                rs.getString("password"),
                L10nUtil.utcToLocal(rs.getTimestamp("create_date")),
                rs.getString("created_by"),
                L10nUtil.utcToLocal(rs.getTimestamp("last_update")),
                rs.getString("last_updated_by")
        );
    }
}
