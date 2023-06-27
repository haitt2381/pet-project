package com.pp.core.mapper;

import com.pp.core.dto.RequestInfo;
import com.pp.core.dto.ResponseInfo;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommonMapper {

    ResponseInfo toResponseInfo(RequestInfo requestInfo);
}