package info.ryandorman.simplescheduler.dao;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.model.User;

import java.util.List;

/**
 * Allows predictable access to persistent User data.
 */
public interface UserDao {
    /**
     * Gets a <code>java.util.List</code> of all Users.
     * @return All Users
     */
    List<User> getAll();

    /**
     * Gets a specific User based on their unique identifier.
     * @param id Unique identifier for User
     * @return User associated with identifier if any
     */
    User getById(int id);

    /**
     * Gets a specific User based on their unique name.
     * @param name Name of User
     * @return User associated with name if any
     */
    User getByName(String name);
}
