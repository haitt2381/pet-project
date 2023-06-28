package com.pp.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ResponseInfo {
    Integer page;
    Integer size;
    Long total;
    @Builder.Default
    List<MessageInfo> errors = new ArrayList<>();
}
