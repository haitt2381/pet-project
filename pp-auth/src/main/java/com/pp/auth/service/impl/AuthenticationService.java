package com.pp.auth.service.impl;

import com.pp.auth.config.KeycloakProvider;
import com.pp.auth.dto.data.UserData;
import com.pp.auth.dto.request.LoginRequest;
import com.pp.auth.service.IAuthenticationService;
import com.pp.core.exception.AppErrorInfo;
import com.pp.core.exception.AppRuntimeException;
import com.pp.core.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService implements IAuthenticationService {

    final HttpServletRequest httpRequest;
    final KeycloakProvider kcProvider;

//    final IUserService userService;

    @Override
    public UserData getCurrentUser() {
        String currentUsernameLogged = CommonUtils.getCurrentUsernameLogged();
        //TODO: userService.getUserByEmailOrUsername(currentUsernameLogged);
        return null;
    }

    @Override
    public AccessTokenResponse login(LoginRequest request) {

        try (Keycloak keycloak =
                     kcProvider.newKeycloakBuilderWithPasswordCredentials(request.getEmailOrUsername(), request.getPassword())) {
            return keycloak.tokenManager().getAccessToken();
        } catch (NotAuthorizedException ex) {
            log.info("[AuthService] Invalid account", ex);
            throw new AppRuntimeException(AppErrorInfo.INVALID_CREDENTIALS, HttpStatus.FORBIDDEN);
        } catch (BadRequestException ex) {
            log.info("[AuthService] User probably hasn't active", ex);
            throw new AppRuntimeException(AppErrorInfo.USER_NOT_ACTIVE, HttpStatus.FORBIDDEN);
        }

    }

    public void logout() {
        UserData currentUser = getCurrentUser();
        UserResource userResource = kcProvider.getUsersResource().get(currentUser.getUserKeycloakId());
        userResource.logout();
        kcProvider.getRealmResource().logoutAll();
    }


}
