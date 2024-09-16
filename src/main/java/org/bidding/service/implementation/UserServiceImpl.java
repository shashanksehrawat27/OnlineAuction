package org.bidding.service.implementation;

import org.bidding.database.mapper.EntityMapper;
import org.bidding.dto.UserDTO;
import org.bidding.exception.CannotCreateDuplicateEntryException;
import org.bidding.database.entity.UserEntity;
import org.bidding.database.repository.UserRepository;
import org.bidding.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<UserDTO> findAllRegisteredUsers() {
        return userRepository.findAll()
                .stream()
                .map(entityMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findUserByUserId(Long id) {
        return userRepository.findById(id)
                .map(entityMapper::toUserDTO)
                .orElse(null);
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("UserDTO cannot be null");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new CannotCreateDuplicateEntryException("User with email " + userDTO.getEmail() + " already exists.");
        }

        UserEntity userEntity = entityMapper.toUser(userDTO);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        return entityMapper.toUserDTO(savedUserEntity);
    }
    @Override
    public UserDTO updateUserDetail(Long id, UserDTO userDTO) {
        if (userRepository.existsById(id)) {
            UserEntity user = entityMapper.toUser(userDTO);
            user.setId(id);
            return entityMapper.toUserDTO(userRepository.save(user));
        }
        return null;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}