package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.Country;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomerDaoImpl implements CustomerDao{
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_ALL = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id;";

    @Override
    public List<Customer> getAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        List<Customer> customers = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_ALL);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Customer customer = mapResult(rs);
                customers.add(customer);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(customers.size() + " Customers returned from database by CustomerDao.getAll");
        return customers;
    }

    @Override
    public Customer getById(int id) {
        return null;
    }

    @Override
    public Customer create(Customer customer) {
        return null;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public Customer delete(Customer customer) {
        return null;
    }

    public static Customer mapResult(ResultSet rs) throws SQLException {
        int[] counter = new int[]{1 };
        return mapResult(rs, counter);
    }

    public static Customer mapResult(ResultSet rs, int[] counter) throws SQLException {
        FirstLevelDivision division = FirstLevelDivisionDaoImpl.mapResult(rs, counter);

        Customer customer = new Customer(
                rs.getInt(counter[0]++),
                rs.getString(counter[0]++),
                rs.getString(counter[0]++),
                rs.getString(counter[0]++),
                rs.getString(counter[0]++),
                division,
                L10nUtil.utcToLocal(rs.getTimestamp(counter[0]++)),
                rs.getString(counter[0]++),
                L10nUtil.utcToLocal(rs.getTimestamp(counter[0]++)),
                rs.getString(counter[0]++)
        );

        // Skip division_id column, instead we store the FirstLevelDivision class directly in Customer
        counter[0]++;

        return customer;
    }
}
