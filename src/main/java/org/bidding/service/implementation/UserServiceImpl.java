package org.bidding.service.implementation;

import org.bidding.dto.UserDTO;
import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.model.User;
import org.bidding.repository.UserRepository;
import org.bidding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Conversion from User to UserDTO
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }

    // Conversion from UserDTO to User
    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        return user;
    }

    @Override
    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CannotCreateDuplicateEntryException("User with email " + userDTO.getEmail() + " already exists.");
        }
        return convertToDTO(userRepository.save(convertToEntity(userDTO)));
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        if (userRepository.existsById(id)) {
            User user = convertToEntity(userDTO);
            user.setId(id);
            return convertToDTO(userRepository.save(user));
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}