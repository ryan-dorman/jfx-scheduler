package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Appointment;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Allows predictable access to persistent Appointment data.
 */
public interface AppointmentDao {
    /**
     * Gets a <code>java.util.List</code> of all Appointments.
     *
     * @return All Appointments
     */
    List<Appointment> getAll();

    /**
     * Gets a <code>java.util.List</code> of all Appointments that start within the given date and time window.
     *
     * @param start Start date and time of window to filter by
     * @param end   End date and time of window to filter by
     * @return All Appointments that start within the given date and time window
     */
    List<Appointment> getByStartDateTimeWindow(ZonedDateTime start, ZonedDateTime end);

    /**
     * Gets a <code>java.util.List</code> of all Appointments for a specific Customer that start, stop, or are ongoing
     * within the given date and time window.
     *
     * @param customerId Unique identifier for Customer to filter by
     * @param start      Start date and time of window to filter by
     * @param end        End date and time of window to filter by
     * @return All Appointments for the Customer that fall within the given date and time window
     */
    List<Appointment> getByCustomerIdAndDateTimeWindow(int customerId, ZonedDateTime start, ZonedDateTime end);

    /**
     * Gets a specific Appointment based on their unique identifier.
     *
     * @param id Unique identifier for Appointment
     * @return Appointment associated with identifier if any
     */
    Appointment getById(int id);

    /**
     * Creates a new Appointment record.
     *
     * @param appointment New Appointment data to store
     * @return 1 or 0 to indicate number of records created
     */
    int create(Appointment appointment);

    /**
     * Updates an existing Appointment record.
     *
     * @param appointment Updated Appointment data to store
     * @return 1 or 0 to indicate number of records updated
     */
    int update(Appointment appointment);

    /**
     * Deletes an existing Appointment record.
     *
     * @param id Unique identifier of Appointment to delete
     * @return 1 or 0 to indicate number of records deleted
     */
    int delete(int id);

    /**
     * Deletes any existing Appointment records for a specific Customer.
     *
     * @param customerId Unique identifier of Customer to delete Appointments for
     * @return 0+ to indicate number of records deleted
     */
    int deleteByCustomerId(int customerId);
}
