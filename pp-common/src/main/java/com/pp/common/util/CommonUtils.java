package com.pp.common.util;

import com.pp.common.dto.IdResponse;
import com.pp.common.dto.ResponseInfo;
import com.pp.common.exception.AppErrorInfo;
import com.pp.common.exception.AppRuntimeException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@Slf4j
@UtilityClass
public class CommonUtils {
    public static IdResponse buildIdResponse(UUID id) {
        return IdResponse.builder().id(id).build();
    }

    public static <T> ResponseInfo toResponseInfo(Page<T> page) {
        return ResponseInfo.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .build();
    }

    public static UUID isValidUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (Exception ex) {
            throw new AppRuntimeException(AppErrorInfo.UUID_INVALID);
        }
    }

    public static String getCurrentUsernameLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return "";
        }
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
        return accessToken.getPreferredUsername();
    }
}
