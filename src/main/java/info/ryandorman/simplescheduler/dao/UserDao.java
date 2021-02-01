package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.model.User;

import java.util.List;

public interface UserDao {
    public List<User> getAll();
    public User getById(long id);
    public User getByNameAndPassword(String username, String password);
}
