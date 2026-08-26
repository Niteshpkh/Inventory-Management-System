package com.example.inventory_management.Mapper;

import com.example.inventory_management.dto.UserRequestDto;
import com.example.inventory_management.dto.UserResponseDto;
import com.example.inventory_management.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDto dto);

    UserResponseDto toResponseDto(User user);

      void updateEntity(
            UserRequestDto dto,
            @MappingTarget User user
    );
}