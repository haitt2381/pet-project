package com.pp.auth.service;

import com.pp.auth.dto.data.UserData;
import com.pp.auth.dto.request.LoginRequest;
import org.keycloak.representations.AccessTokenResponse;

public interface IAuthenticationService {

    UserData getCurrentUser();

    AccessTokenResponse login(LoginRequest request);

    void logout();

}