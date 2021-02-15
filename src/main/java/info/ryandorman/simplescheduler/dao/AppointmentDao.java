package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Appointment;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentDao {
    List<Appointment> getAll();

    List<Appointment> getByDateTimeWindow(ZonedDateTime start, ZonedDateTime end);

    Appointment getById(int id);

    Appointment getByCustomerIdAndDateTime(int customerId, ZonedDateTime appointmentTime);

    int create(Appointment appointment);

    int update(Appointment appointment);

    int delete(int id);

    int deleteByCustomerId(int customerId);
}
