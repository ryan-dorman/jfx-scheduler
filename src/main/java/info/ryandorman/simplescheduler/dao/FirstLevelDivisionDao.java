package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.FirstLevelDivision;

import java.util.List;

public interface FirstLevelDivisionDao {
    List<FirstLevelDivision> getByCountryId(int countryId);
}
