package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Appointment;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentDao {
    List<Appointment> getAll();

    List<Appointment> getByDateTimeWindow(ZonedDateTime start, ZonedDateTime end);

    Appointment getById(int id);

    int create();

    int update();

    int delete();
}
