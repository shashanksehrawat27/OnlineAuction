package org.bidding.service;

import org.bidding.domain.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAllRegisteredUsers();
    UserDTO findUserByUserId(Long id);
    UserDTO addUser(UserDTO user);
    UserDTO updateUserDetail(Long id, UserDTO user);
    boolean deleteUser(Long id);
}