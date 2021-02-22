package info.ryandorman.simplescheduler.dao;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.common.ColumnIterator;
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

/**
 * Allows access of persistent User data.
 */
public class UserDaoImpl implements UserDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_ALL = "SELECT * FROM users;";
    private static final String GET_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
    private static final String GET_BY_NAME = "SELECT * FROM users WHERE user_name = ?;";

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a User entity. Allows specification of <code>java.sql.ResultSet</code>
     * column User data starts at.
     *
     * @param rs           <code>java.sql.ResultSet</code> to map
     * @param resultColumn Column where User data starts
     * @return User entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary User data
     */
    public static User mapResult(ResultSet rs, ColumnIterator resultColumn) throws SQLException {
        return new User(
                rs.getInt(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next())
        );
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a User entity.
     *
     * @param rs <code>java.sql.ResultSet</code> to map
     * @return User entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary User data
     */
    private User mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        Connection conn;
        PreparedStatement stmt = null;
        ArrayList<User> users = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_ALL);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = mapResult(rs);
                users.add(user);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(users.size() + " Users returned from database by UserDao.getAll");
        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getById(int id) {
        Connection conn;
        PreparedStatement stmt = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_ID);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapResult(rs);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        if (user != null) {
            sysLogger.info(user.getId() + ":" + user.getName() + " returned from database by UserDao.getById=" + id);
        } else {
            sysLogger.warning("No User returned from database by UserDao.getById=" + id);
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getByName(String name) {
        Connection conn;
        PreparedStatement stmt = null;
        User user = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_NAME);

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user = mapResult(rs);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        if (user != null) {
            sysLogger.info(user.getId() + ":" + user.getName() + " returned from database by UserDao.getByName=" + name);
        } else {
            sysLogger.warning("No User returned from database by UserDao.getByName=" + name);
        }
        return user;
    }
}
