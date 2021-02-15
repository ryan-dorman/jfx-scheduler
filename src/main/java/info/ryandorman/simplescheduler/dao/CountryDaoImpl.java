package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.common.ColumnIterator;
import info.ryandorman.simplescheduler.model.Country;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CountryDaoImpl implements CountryDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_ALL = "SELECT * FROM countries;";

    public static Country mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

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

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(countries.size() + " Countries returned from database by CountryDao.getAll");
        return countries;
    }
}
