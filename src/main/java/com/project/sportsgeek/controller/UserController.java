package com.project.sportsgeek.controller;

import com.project.sportsgeek.jwtconfig.JwtTokenUtil;
import com.project.sportsgeek.model.profile.*;
import com.project.sportsgeek.response.Result;
import com.project.sportsgeek.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- SELECT CONTROLLER -------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PreAuthorize("hasRole('Admin')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        Result<List<User>> userList = userService.findAllUsers();
        return new ResponseEntity<>(userList.getData(), HttpStatus.valueOf(userList.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable int userId) throws Exception {
        Result<User> userResult = userService.findUserByUserId(userId);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping("/user-name/{username}")
    public ResponseEntity<UserWithPassword> getUserByUserName(@PathVariable String username) throws Exception {
        Result<UserWithPassword> userResult = userService.findUserByUserName(username);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping("/{userId}/loosing-points")
    public ResponseEntity<UserWinningAndLossingPoints> getUserLoosingPoints(@PathVariable int userId)
            throws Exception {
        Result<UserWinningAndLossingPoints> userResult = userService.findUserLoosingPoints(userId);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping("/{userId}/winning-points")
    public ResponseEntity<UserWinningAndLossingPoints> getUserWinningPoints(@PathVariable int userId)
            throws Exception {
        Result<UserWinningAndLossingPoints> userResult = userService.findUserWinningPoints(userId);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @GetMapping("/user-with-status/{status}")
    public ResponseEntity<List<User>> getUsersByStatus(@PathVariable boolean status) throws Exception {
        Result<List<User>> userResult = userService.findUsersByStatus(status);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping("/role-id/{roleId}")
    public ResponseEntity<List<User>> getUserByRoleId(@PathVariable int roleId) throws Exception {
        Result<List<User>> userResult = userService.findUsersByRole(roleId);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- INSERT CONTROLLER --------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Bad Request")})
    @PostMapping("/add-user")
    public ResponseEntity<User> addUser(@RequestBody(required = true) UserWithPassword userWithPassword)
            throws Exception {
        userWithPassword.setPassword(bCryptPasswordEncoder.encode(userWithPassword.getPassword()));
        Result<User> userResult = userService.addUser(userWithPassword);
        System.out.println(userWithPassword);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- UPDATE CONTROLLER --------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated schema"),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody(required = true) UserWithNewPassword userWithNewPassword) throws Exception {
        System.out.println(userWithNewPassword);
        Result<String> userResult = userService.updatePassword(userWithNewPassword);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated schema"),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")})
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{userId}/update-status/{status}")
    public ResponseEntity<User> updateStatus(@PathVariable int userId, @PathVariable boolean status) throws Exception {
        Result<User> userResult = userService.updateStatus(userId, status);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated schema"),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable int userId, @RequestBody(required = true) User user)
            throws Exception {
        Result<User> userResult = userService.updateUser(userId, user);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated schema"),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")})
    @PreAuthorize("hasRole('Admin')")
    @PutMapping("/{userId}/update-user-role/{roleId}")
    public ResponseEntity<String> updateUserRole(@PathVariable int userId, @PathVariable int roleId)
            throws Exception {
        Result<String> userResult = userService.updateUserRole(userId, roleId);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

//	------------------------------------------------- FORGET PASSWORD CONTROLLER -----------------------------------------------------------------

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated schema"),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PostMapping("/forget-password")
    public ResponseEntity<User> getUserByEmailIdAndMobileNumber(@RequestBody(required = true) User user) throws Exception {
        Result<User> userResult = userService.findUserByEmailIdAndMobileNumber(user);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully updated schema"),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 400, message = "Missing or invalid request body"),
            @ApiResponse(code = 500, message = "Internal error")})
    @PreAuthorize("hasAnyRole('Admin','User')")
    @PutMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody(required = true) UserWithOtp userWithOtp)
            throws Exception {
        Result<String> userResult = userService.updateForgetPassword(userWithOtp);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- DELETE CONTROLLER -------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully deleted schema"),
            @ApiResponse(code = 404, message = "Schema not found"),
            @ApiResponse(code = 409, message = "Schema is in use"),
            @ApiResponse(code = 500, message = "Error deleting schema")})
    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Result<User>> deleteUser(@PathVariable int userId) throws Exception {
        Result<User> userResult = userService.deleteUser(userId);
        return new ResponseEntity<>(userResult, HttpStatus.valueOf(userResult.getCode()));
    }

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- AUTHENTICATION CONTROLLER -----------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/authenticate")
    public ResponseEntity<UserForLoginState> authenticateStatus(@RequestBody(required = true) UserAtLogin userAtLogin) throws Exception {
        Result<UserForLoginState> userResult = userService.authenticate(userAtLogin);
        // Generate token
        authenticate(userAtLogin.getUsername(), userAtLogin.getPassword());
        final UserDetails userDetails = userService.loadUserByUsername(userAtLogin.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        userResult.getData().setToken(token);
        return new ResponseEntity<>(userResult.getData(), HttpStatus.valueOf(userResult.getCode()));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
