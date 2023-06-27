package com.pp.user.controller;

import com.pp.core.dto.IdResponse;
import com.pp.user.dto.request.CreateUserRequest;
import com.pp.user.dto.request.GetUsersRequest;
import com.pp.user.dto.request.UpdateUserRequest;
import com.pp.user.dto.response.GetUserResponse;
import com.pp.user.dto.response.GetUsersResponse;
import com.pp.user.service.IUserService;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

//@Api(tags = "Users")
@RestController
@RequestMapping("/api/user")
public class UserController {

    final IUserService userService;

//    @ApiOperation(value = "Get list user")
//    @RolesAllowed("ROLE_moderator")
    @PostMapping("/list")
    public GetUsersResponse getUsers(@RequestBody GetUsersRequest request) {
        return userService.getUsers(request);
    }

//    @ApiOperation(value = "Get user detail")
//    @RolesAllowed("ROLE_admin")
    @GetMapping("/{id}")
    public GetUserResponse getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

//    @ApiOperation(value = "Create user")
//    @RolesAllowed("ROLE_admin")
    @PostMapping(value = "/create")
    public IdResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

//    @ApiOperation(value = "Activate user")
//    @RolesAllowed("ROLE_admin")
    @PatchMapping(value = "/activate/{id}")
    public IdResponse activateUser(@PathVariable String id) {
        return userService.activateAndDeactivateUser(id, true);
    }

//    @ApiOperation(value = "Deactivate user")
//    @RolesAllowed("ROLE_admin")
    @PatchMapping(value = "/deactivate/{id}")
    public IdResponse deactivateUser(@PathVariable String id) {
        return userService.activateAndDeactivateUser(id, false);
    }

//    @ApiOperation(value = "Update user")
//    @RolesAllowed("ROLE_admin")
    @PutMapping(value = "/update")
    public IdResponse updateUser(@Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(request);
    }

//    @ApiOperation(value = "Soft delete user")
//    @RolesAllowed("ROLE_admin")
    @DeleteMapping("/{id}")
    public IdResponse deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

//    @ApiOperation(value = "Hard delete user")
    @DeleteMapping("/hard-delete/{id}")
    public IdResponse hardDeleteUser(@PathVariable String id) {
        return userService.hardDeleteUser(id);
    }

//    @ApiOperation(value = "Restore user from recycle bin")
    @PatchMapping("/restore/{id}")
    public IdResponse restoreUser(@PathVariable String id) {
        return userService.restoreUser(id);
    }

}
