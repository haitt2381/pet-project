package com.pp.user.service.impl;

import com.pp.core.dto.IdResponse;
import com.pp.core.exception.AppErrorInfo;
import com.pp.core.exception.AppRuntimeException;
import com.pp.core.util.CommonUtils;
import com.pp.core.util.RequestUtils;
import com.pp.user.dto.data.UserData;
import com.pp.user.dto.request.CreateUserRequest;
import com.pp.user.dto.request.GetUsersRequest;
import com.pp.user.dto.request.UpdateUserRequest;
import com.pp.user.dto.response.GetUserResponse;
import com.pp.user.dto.response.GetUsersResponse;
import com.pp.user.entity.User;
import com.pp.user.mapper.UserMapper;
import com.pp.user.repository.UserRepository;
import com.pp.user.service.IKeycloakAdminClientService;
import com.pp.user.service.IUserService;
import com.pp.user.specification.UserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Transactional
public class UserService implements IUserService {

    final UserRepository userRepository;

    final IKeycloakAdminClientService kcService;

    final UserMapper userMapper;

    @Override
    public GetUsersResponse getUsers(GetUsersRequest request) {
        Pageable pageRequest = RequestUtils.getPageRequest(request.getRequestInfo());
        Specification<User> specRequest = UserSpecification.buildSpecification(request);
        Page<User> users = userRepository.findAll(specRequest, pageRequest);

        return GetUsersResponse.builder()
                .responseInfo(CommonUtils.toResponseInfo(users))
                .data(userMapper.toListUserData(users.getContent()))
                .build();
    }

    @Override
    public GetUserResponse getUser(String id) {
        UUID uuid = CommonUtils.isValidUUID(id);
        User user = this.getUserById(uuid);
        UserData userDto = userMapper.toUserData(user);
        return GetUserResponse.builder().data(userDto).build();
    }

    public UserData getUserByEmailOrUsername(String emailOrUsername) {
        Optional<User> user = userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);

        if (user.isEmpty() || Boolean.TRUE.equals(user.get().getIsDeleted())) {
            log.info("[UserService] Not found user: {}", emailOrUsername);
            throw new AppRuntimeException(AppErrorInfo.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return userMapper.toUserData(user.get());
    }

    private User getUserById(UUID userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty() || Boolean.TRUE.equals(user.get().getIsDeleted())) {
            log.info("[UserService] Not found user: {}", userId);
            throw new AppRuntimeException(AppErrorInfo.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return user.get();
    }

    private User getUserByIdDeleted(UUID userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty() || Boolean.FALSE.equals(user.get().getIsDeleted())) {
            log.info("[UserService] Not found user: {}", userId);
            throw new AppRuntimeException(AppErrorInfo.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        return user.get();
    }

    @Override
    public IdResponse createUser(CreateUserRequest request) {
        checkUserExisted(request.getEmail(), request.getUsername(), request.getPhoneNumber());

        String userKeycloakId = kcService.createKeycloakUser(request);
        if (Objects.isNull(userKeycloakId)) {
            throw new AppRuntimeException(AppErrorInfo.CREATE_USER_KEYCLOAK_FAILED);
        }

        User userSaved;
        try {
            User user = userMapper.toUser(request);
            user.setUserKeycloakId(userKeycloakId);
            userSaved = userRepository.save(user);
        } catch (Exception ex) {
            log.info("Fail to create user, trying delete user keycloak", ex);
            kcService.deleteKeycloakUserById(userKeycloakId);
            throw new AppRuntimeException(AppErrorInfo.CREATE_USER_FAILED);
        }

        //TODO: send mail to verify email & active user

        return CommonUtils.buildIdResponse(userSaved.getId());
    }

    @Override
    public void checkUserExisted(String email, String username, String phoneNumber) {
        List<AppErrorInfo> errors = new ArrayList<>();
        if (userRepository.existsByEmail(email)) {
            errors.add(AppErrorInfo.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByUsername(username)) {
            errors.add(AppErrorInfo.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            errors.add(AppErrorInfo.PHONE_NUMBER_ALREADY_EXISTS);
        }

        if (CollectionUtils.isNotEmpty(errors)) {
            throw new AppRuntimeException(errors, HttpStatus.CONFLICT);
        }
    }

    @Override
    public void isActiveUser(String id) {
        UUID uuid = CommonUtils.isValidUUID(id);
        User user = this.getUserById(uuid);

        if (Boolean.FALSE.equals(user.getIsActive())) {
            throw new AppRuntimeException(AppErrorInfo.USER_NOT_ACTIVE, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public IdResponse activateAndDeactivateUser(String id, boolean isActive) {
        UUID uuid = CommonUtils.isValidUUID(id);
        User user = getUserById(uuid);
        user.setIsActive(isActive);

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().isActive(isActive).build();
        kcService.updateKeycloakUser(user.getUserKeycloakId(), updateUserRequest);

        User userSaved = userRepository.save(user);
        return CommonUtils.buildIdResponse(userSaved.getId());
    }

    @Override
    public IdResponse updateUser(UpdateUserRequest request) {
        User user = getUserById(request.getId());
        userMapper.toUser(user, request);
        kcService.updateKeycloakUser(user.getUserKeycloakId(), request);
        User userSaved = userRepository.save(user);

        //TODO: send email notify user if email changed

        return CommonUtils.buildIdResponse(userSaved.getId());
    }

    @Override
    public IdResponse deleteUser(String id) {
        UUID uuid = CommonUtils.isValidUUID(id);
        User user = getUserById(uuid);
        userRepository.delete(user);
        return CommonUtils.buildIdResponse(uuid);
    }

    @Override
    public IdResponse hardDeleteUser(String id) {
        UUID uuid = CommonUtils.isValidUUID(id);
        User user = getUserByIdDeleted(uuid);
        userRepository.hardDelete(id);
        kcService.deleteKeycloakUserById(user.getUserKeycloakId());
        return CommonUtils.buildIdResponse(uuid);
    }

    @Override
    public IdResponse restoreUser(String id) {
        UUID uuid = CommonUtils.isValidUUID(id);
        User user = getUserByIdDeleted(uuid);
        user.setIsDeleted(Boolean.FALSE);
        User userSaved = userRepository.save(user);
        return CommonUtils.buildIdResponse(userSaved.getId());
    }
}
