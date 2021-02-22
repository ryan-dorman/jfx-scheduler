package info.ryandorman.simplescheduler.dao;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import info.ryandorman.simplescheduler.model.Country;

import java.util.List;

/**
 * Allows easy access to persistent Country data.
 */
public interface CountryDao {
    /**
     * Gets a <code>java.util.List</code> of all Countries.
     * @return All Countries
     */
    List<Country> getAll();
}
