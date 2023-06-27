package com.pp.user.dto.request;

import com.pp.core.dto.BaseRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class GetUsersRequest extends BaseRequest {
//    @ApiModelProperty(notes = "Any keyword for search name, username, email, phoneNumber")
    String keyword;
    List<String> role;
    Boolean isActive;
    Boolean isDeleted;
    Boolean isExcludeCurrentUserLogged;
    LocalDateTime fromDate;
    LocalDateTime toDate;
}
