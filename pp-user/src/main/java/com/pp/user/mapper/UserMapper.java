package com.pp.user.mapper;

import com.pp.common.mapper.CommonMapper;
import com.pp.user.dto.data.UserData;
import com.pp.user.dto.request.CreateUserRequest;
import com.pp.user.dto.request.UpdateUserRequest;
import com.pp.user.entity.User;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, UserMapper.class}
)
public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);

    UserData toUserData(User user);

    List<UserData> toListUserData(List<User> users);

    void toUser(@MappingTarget User user, UpdateUserRequest updateUserRequest);
}
