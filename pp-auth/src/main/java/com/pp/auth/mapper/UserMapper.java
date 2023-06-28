package com.pp.auth.mapper;

import com.pp.auth.dto.request.UpdateUserRequest;
import com.pp.common.mapper.CommonMapper;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommonMapper.class, UserMapper.class}
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", source = "isActive")
    void updateUserRepresentation(@MappingTarget UserRepresentation userRepresentation, UpdateUserRequest updateUserRequest);
}
