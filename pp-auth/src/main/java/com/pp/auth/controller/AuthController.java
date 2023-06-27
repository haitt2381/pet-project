package com.pp.auth.controller;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.pp.auth.config.KeycloakProvider;
import com.pp.auth.dto.request.LoginRequest;
import com.pp.auth.service.IAuthenticationService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@Api(tags = "Auth")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final IAuthenticationService authService;
    final KeycloakProvider kcProvider;

    @PostMapping("/login")
    public AccessTokenResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @GetMapping(path = "/logout")
    public void logout(HttpServletRequest request) throws ServletException {
        authService.logout();
        request.logout();
    }

    @GetMapping("/refreshToken/{refreshToken}")
    public String getRefreshToken(@PathVariable String refreshToken) throws UnirestException {
        JsonNode jsonNode = kcProvider.refreshToken(refreshToken);
        return jsonNode.getObject().toString();
    }
}
