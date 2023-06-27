package com.pp.user.service;


import com.pp.core.dto.IdResponse;
import com.pp.user.dto.data.UserData;
import com.pp.user.dto.request.CreateUserRequest;
import com.pp.user.dto.request.GetUsersRequest;
import com.pp.user.dto.request.UpdateUserRequest;
import com.pp.user.dto.response.GetUserResponse;
import com.pp.user.dto.response.GetUsersResponse;

public interface IUserService {

    GetUsersResponse getUsers(GetUsersRequest request);

    GetUserResponse getUser(String id);

    UserData getUserByEmailOrUsername(String emailOrUsername);

    IdResponse createUser(CreateUserRequest request);

    void checkUserExisted(String email, String username, String phoneNumber);

    void isActiveUser(String emailOrUsername);

    IdResponse activateAndDeactivateUser(String emailOrUsername, boolean isActive);

    IdResponse updateUser(UpdateUserRequest request);

    IdResponse deleteUser(String id);

    IdResponse hardDeleteUser(String id);

    IdResponse restoreUser(String id);
}
