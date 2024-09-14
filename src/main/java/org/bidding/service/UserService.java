package org.bidding.service;

import org.bidding.dto.UserDTO;
import org.bidding.model.User;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO save(UserDTO user);
    UserDTO update(Long id, UserDTO user);
    boolean delete(Long id);
}