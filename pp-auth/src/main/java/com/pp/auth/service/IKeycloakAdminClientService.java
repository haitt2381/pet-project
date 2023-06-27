package com.pp.auth.service;

import com.pp.auth.dto.request.CreateUserRequest;
import com.pp.auth.dto.request.UpdateUserRequest;
import org.keycloak.admin.client.resource.UserResource;

public interface IKeycloakAdminClientService {

    String createKeycloakUser(CreateUserRequest user);

    UserResource getUserResourceById(String userId);

    void updateKeycloakUser(String keycloakId, UpdateUserRequest updateUserRequest);

    void deleteKeycloakUserById(String keycloakId);
}
