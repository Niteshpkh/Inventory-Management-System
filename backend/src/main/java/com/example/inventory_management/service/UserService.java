package com.example.inventory_management.service;

import com.example.inventory_management.Mapper.UserMapper;
import com.example.inventory_management.dto.UserRequestDto;
import com.example.inventory_management.dto.UserResponseDto;
import com.example.inventory_management.entity.User;
import com.example.inventory_management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    public  UserService(UserRepository userRepo, UserMapper userMapper){
        this.userRepo = userRepo;
        this.userMapper = userMapper;
    }


public UserResponseDto saveUser(UserRequestDto dto){
        User user = userMapper.toEntity(dto);
        User savedUser = userRepo.save(user);
        return userMapper.toResponseDto(savedUser);
}

    public List<UserResponseDto> getAllUsers() {

        List<User> users = userRepo.findAll();

        return users.stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto getUserById(Long id) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toResponseDto(user);
    }

    public UserResponseDto updateUser(
            Long id,
            UserRequestDto dto) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateEntity(dto, user);

        User updatedUser = userRepo.save(user);

        return userMapper.toResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {

        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepo.deleteById(id);
    }
}
