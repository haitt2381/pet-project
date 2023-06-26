package com.pp.user.repository;

import com.pp.user.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository < EmployeeProfile, Integer > {

}