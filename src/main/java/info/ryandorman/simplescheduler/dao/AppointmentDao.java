package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Appointment;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentDao {
    List<Appointment> getAll();

    List<Appointment> getByStartDateTimeWindow(ZonedDateTime start, ZonedDateTime end);

    List<Appointment> getByCustomerIdAndDateTimeWindow(int customerId, ZonedDateTime start, ZonedDateTime end);

    Appointment getById(int id);

    int create(Appointment appointment);

    int update(Appointment appointment);

    int delete(int id);

    int deleteByCustomerId(int customerId);
}
