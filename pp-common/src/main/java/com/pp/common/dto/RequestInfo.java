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
public class RequestInfo {
//    String userId = .getUserId();
    Integer page;
    Integer size;
    @Builder.Default
    List<SortInfo> sortInfo = new ArrayList<>();
}
