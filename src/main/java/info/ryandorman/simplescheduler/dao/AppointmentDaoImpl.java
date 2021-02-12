package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.ResultColumnIterator;
import info.ryandorman.simplescheduler.model.Appointment;

import java.sql.ResultSet;
import java.time.ZonedDateTime;
import java.util.List;

public class AppointmentDaoImpl implements AppointmentDao {

    public static Appointment mapResult(ResultSet rs) {
        return null;
    }

    public static Appointment mapResult(ResultSet rs, ResultColumnIterator resultColumn) {
        return null;
    }

    @Override
    public List<Appointment> getAll() {
        return null;
    }

    @Override
    public List<Appointment> getByDateTimeWindow(ZonedDateTime start, ZonedDateTime end) {
        return null;
    }

    @Override
    public Appointment getById(int id) {
        return null;
    }

    @Override
    public int create() {
        return 0;
    }

    @Override
    public int update() {
        return 0;
    }

    @Override
    public int delete() {
        return 0;
    }
}
