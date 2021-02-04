package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAll();

    User getById(int id);

    User getByName(String name);
}
