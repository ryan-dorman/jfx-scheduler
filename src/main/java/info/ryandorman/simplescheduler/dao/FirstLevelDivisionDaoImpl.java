package info.ryandorman.simplescheduler.dao;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.common.ColumnIterator;
import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Allows access of persistent FirstLevelDivision data.
 */
public class FirstLevelDivisionDaoImpl implements FirstLevelDivisionDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_BY_COUNTRY_ID = "SELECT co.*, fld.* FROM first_level_divisions fld " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "WHERE fld.country_id = ?;";

    /**
     * Creates a new class instance for this Data Access Object.
     */
    public FirstLevelDivisionDaoImpl() {
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a FirstLevelDivision entity.
     *
     * @param rs <code>java.sql.ResultSet</code> to map
     * @return FirstLevelDivision entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary FirstLevelDivision
     *                      data
     */
    public static FirstLevelDivision mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a FirstLevelDivision entity. Allows specification of
     * <code>java.sql.ResultSet</code> column FirstLevelDivision data starts at.
     *
     * @param rs           <code>java.sql.ResultSet</code> to map
     * @param resultColumn Column where FirstLevelDivision data starts
     * @return FirstLevelDivision entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary FirstLevelDivision
     *                      data
     */
    public static FirstLevelDivision mapResult(ResultSet rs, ColumnIterator resultColumn) throws SQLException {
        Country country = CountryDaoImpl.mapResult(rs, resultColumn);
        FirstLevelDivision division = new FirstLevelDivision(
                rs.getInt(resultColumn.next()),
                rs.getString(resultColumn.next()),
                country,
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next())
        );

        // Skip country_id column, instead we store the Country class directly in FirstLevelDivision
        resultColumn.next();

        return division;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FirstLevelDivision> getByCountryId(int countryId) {
        Connection conn;
        PreparedStatement stmt = null;
        List<FirstLevelDivision> divisions = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_COUNTRY_ID);

            stmt.setInt(1, countryId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FirstLevelDivision division = mapResult(rs);
                divisions.add(division);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(divisions.size() +
                " Divisions returned from database by FirstLevelDivisionDao.getByCountryId=" + countryId);
        return divisions;
    }
}
