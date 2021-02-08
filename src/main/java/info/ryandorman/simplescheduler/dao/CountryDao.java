package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.Country;

import java.util.List;

public interface CountryDao {
    List<Country> getAll();
}
