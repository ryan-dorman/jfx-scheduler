package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDaoImpl implements FirstLevelDivisionDao{

    public static FirstLevelDivision mapResult(ResultSet rs) throws SQLException {
        int[] counter = new int[]{1};
        return mapResult(rs, counter);
    }

    public static FirstLevelDivision mapResult(ResultSet rs, int[] counter) throws SQLException {
        Country country = CountryDaoImpl.mapResult(rs, counter);
        FirstLevelDivision division = new FirstLevelDivision(
                rs.getInt(counter[0]++),
                rs.getString(counter[0]++),
                country,
                L10nUtil.utcToLocal(rs.getTimestamp(counter[0]++)),
                rs.getString(counter[0]++),
                L10nUtil.utcToLocal(rs.getTimestamp(counter[0]++)),
                rs.getString(counter[0]++)
        );

        // Skip country_id column, instead we store the Country class directly in FirstLevelDivision
        counter[0]++;

        return division;
    }
}
