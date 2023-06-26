package com.pp.user;

import com.pp.core.dto.BaseResponse;
import com.pp.core.dto.ResponseInfo;
import com.pp.user.entity.EmployeeProfile;
import com.pp.user.service.EmployeeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class EmployeeController {
    @Autowired
    EmployeeProfileService employeeProfileService;

    @PostMapping
    public void saveEmployeeProfile(@RequestBody EmployeeProfile employeeProfile) {
        employeeProfileService.addEmployeeProfile(employeeProfile);
    }

    @GetMapping
    public BaseResponse getAllEmployee() {
        BaseResponse baseResponse = BaseResponse.builder().responseInfo(ResponseInfo.builder().page(1).build()).build();
        return baseResponse;
    }

}