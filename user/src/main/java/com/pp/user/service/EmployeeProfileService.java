package com.pp.user.service;

import com.pp.user.entity.EmployeeProfile;
import java.util.List;

public interface EmployeeProfileService {
    void addEmployeeProfile(EmployeeProfile profile);
    List < EmployeeProfile > getEmployeeProfiles();
}