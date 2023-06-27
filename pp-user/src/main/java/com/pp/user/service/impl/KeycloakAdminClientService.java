package com.pp.user.service.impl;

import com.pp.core.config.KeycloakProvider;
import com.pp.core.exception.AppErrorInfo;
import com.pp.core.exception.AppRuntimeException;
import com.pp.user.constant.Role;
import com.pp.user.dto.request.CreateUserRequest;
import com.pp.user.dto.request.UpdateUserRequest;
import com.pp.user.mapper.UserMapper;
import com.pp.user.service.IKeycloakAdminClientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class KeycloakAdminClientService implements IKeycloakAdminClientService {
    final KeycloakProvider kcProvider;
    final UserMapper userMapper;

    public String createKeycloakUser(CreateUserRequest user) {
        UsersResource usersResource = kcProvider.getUsersResource();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(user.getFirstName());
        kcUser.setLastName(user.getLastName());
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(false);
        kcUser.setEmailVerified(true);

        Response response = usersResource.create(kcUser);
        if (response.getStatus() == 201) {
            String userId = CreatedResponseUtil.getCreatedId(response);
            String roleName = Arrays.stream(Role.values())
                    .filter(role -> role.name().equals(user.getRole()))
                    .findAny().orElseThrow()
                    .getValue();

            addRealmRoleToUser(userId, roleName);
            return userId;
        } else if (response.getStatus() == 409) {
            throw new AppRuntimeException(AppErrorInfo.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }


        return null;
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    public void addRealmRoleToUser(String userId, String roleName) {
        // Get realm
        RealmResource realmResource = kcProvider.getRealmResource();
        UserResource userResource = this.getUserResourceById(userId);

        String clientId = kcProvider.getClientRepresentation().getId();

        RoleRepresentation realmRole = realmResource
                .clients()
                .get(clientId)
                .roles()
                .get(roleName)
                .toRepresentation();

        // Assign realm role to user
        userResource.roles().clientLevel(clientId) //
                .add(Collections.singletonList(realmRole));
    }

    public UserResource getUserResourceById(String userId) {
        return kcProvider.getUsersResource().get(userId);
    }

    @Override
    public void updateKeycloakUser(String keycloakId, UpdateUserRequest updateUserRequest) {

        try {
            UserResource userResource = getUserResourceById(keycloakId);
            UserRepresentation userRepresentation = userResource.toRepresentation();
            userMapper.updateUserRepresentation(userRepresentation, updateUserRequest);

            if (Objects.nonNull(updateUserRequest.getRole())) {
                String roleName = Arrays.stream(Role.values())
                        .filter(role -> role.name().equals(updateUserRequest.getRole()))
                        .findAny().orElseThrow()
                        .getValue();

                addRealmRoleToUser(userRepresentation.getId(), roleName);
            }

            userResource.update(userRepresentation);
        } catch (Exception ex) {
            log.error("[kcService] Update user failed: ", ex);
            throw new AppRuntimeException(AppErrorInfo.UPDATE_USER_KEYCLOAK_FAILED);
        }
    }

    @Override
    public void deleteKeycloakUserById(String keycloakId) {
        RealmResource realmResource = kcProvider.getRealmResource();

        try {
            realmResource.users().delete(keycloakId);
            log.info("[kcService] Delete user successfully with id: " + keycloakId);
        } catch (Exception ex) {
            throw new AppRuntimeException(AppErrorInfo.DELETE_USER_KEYCLOAK_FAILED);
        }

    }

}
