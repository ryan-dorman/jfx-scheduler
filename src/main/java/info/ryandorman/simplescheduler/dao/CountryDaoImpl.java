package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDaoImpl implements CountryDao {

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
}
