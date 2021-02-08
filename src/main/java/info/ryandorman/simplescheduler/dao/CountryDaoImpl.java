package info.ryandorman.simplescheduler.dao;

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

public class CountryDaoImpl implements CountryDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_ALL = "SELECT * FROM countries;";

    public static Country mapResult(ResultSet rs) throws SQLException {
        int[] counter = new int[]{1};
        return mapResult(rs, counter);
    }

    public static Country mapResult(ResultSet rs, int[] counter) throws SQLException {
        return new Country(
                rs.getInt(counter[0]++),
                rs.getString(counter[0]++),
                L10nUtil.utcToLocal(rs.getTimestamp(counter[0]++)),
                rs.getString(counter[0]++),
                L10nUtil.utcToLocal(rs.getTimestamp(counter[0]++)),
                rs.getString(counter[0]++)
        );
    }

    @Override
    public List<Country> getAll() {
        Connection conn = null;
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
