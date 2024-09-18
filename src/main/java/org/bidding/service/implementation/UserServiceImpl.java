package org.bidding.service.implementation;

import org.bidding.database.adapter.UserAdapter;
import org.bidding.domain.dto.UserDTO;
import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserAdapter userAdapter;

    @Override
    public List<UserDTO> findAllRegisteredUsers() {
        return userAdapter.findAll();
    }

    @Override
    public UserDTO findUserByUserId(Long id) {
        return userAdapter.findById(id);
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (userAdapter.existsByEmail(userDTO.getEmail())) {
            throw new CannotCreateDuplicateEntryException("User with email " + userDTO.getEmail() + " already exists.");
        }

        return userAdapter.save(userDTO);
    }
    @Override
    public UserDTO updateUserDetail(Long id, UserDTO userDTO) {
        if (userAdapter.existsById(id)) {
            userDTO.setId(id);
            return userAdapter.save(userDTO);
        }
        return null;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userAdapter.existsById(id)) {
            userAdapter.deleteById(id);
            return true;
        }
        return false;
    }
}