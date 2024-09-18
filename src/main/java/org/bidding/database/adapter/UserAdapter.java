package org.bidding.database.adapter;

import lombok.AllArgsConstructor;
import org.bidding.database.entity.UserEntity;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.database.repository.UserRepository;
import org.bidding.domain.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserAdapter {
    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    public UserDTO save(UserDTO UserDTO) {
        UserEntity userEntity = entityMapper.toUserEntity(UserDTO);
        return entityMapper.toUserDTO(userRepository.save(userEntity));
    }

    public UserDTO findById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return entityMapper.toUserDTO(userEntity.orElse(null));
    }

    public List<UserDTO> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(entityMapper::toUserDTO).collect(Collectors.toList());
    }

    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}