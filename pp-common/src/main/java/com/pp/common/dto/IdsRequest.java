package com.pp.common.dto;

import com.pp.common.dto.BaseRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class IdsRequest extends BaseRequest {
    List<String> ids;
    String keyword;
}
