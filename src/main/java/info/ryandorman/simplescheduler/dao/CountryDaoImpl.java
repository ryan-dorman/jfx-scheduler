package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.ColumnIterator;
import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.Country;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Allows access of persistent Country data.
 */
public class CountryDaoImpl implements CountryDao {
    /**
     * System Logger
     */
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * MySQL statement to get all Countries
     */
    private static final String GET_ALL = "SELECT * FROM countries;";

    /**
     * Creates a new class instance for this Data Access Object.
     */
    public CountryDaoImpl() {
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Country entity.
     *
     * @param rs <code>java.sql.ResultSet</code> to map
     * @return Country entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Country data
     */
    public static Country mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Country entity. Allows specification of
     * <code>java.sql.ResultSet</code> column Country data starts at.
     *
     * @param rs           <code>java.sql.ResultSet</code> to map
     * @param resultColumn Column where Country data starts
     * @return Country entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Country data
     */
    public static Country mapResult(ResultSet rs, ColumnIterator resultColumn) throws SQLException {
        return new Country(
                rs.getInt(resultColumn.next()),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Country> getAll() {
        Connection conn;
        PreparedStatement stmt = null;
        List<Country> countries = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_ALL);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Country country = mapResult(rs);
                countries.add(country);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(countries.size() + " Countries returned from database by CountryDao.getAll");
        return countries;
    }
}
