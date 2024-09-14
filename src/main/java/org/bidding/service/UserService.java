package org.bidding.service;

import org.bidding.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User save(User user);
    User update(Long id, User user);
    boolean delete(Long id);
}