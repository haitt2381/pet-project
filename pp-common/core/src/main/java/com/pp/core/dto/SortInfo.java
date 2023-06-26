package com.pp.core.dto;

//import com.example.petproject.common.constant.DirectionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SortInfo {
    String field;
//    DirectionType direction;
}
