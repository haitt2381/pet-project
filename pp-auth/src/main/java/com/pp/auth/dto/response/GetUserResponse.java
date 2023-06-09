package com.pp.auth.dto.response;

import com.pp.auth.dto.data.UserData;
import com.pp.common.dto.BaseResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class GetUserResponse extends BaseResponse {
    UserData data;
}
