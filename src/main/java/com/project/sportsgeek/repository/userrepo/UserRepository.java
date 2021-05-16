package com.project.sportsgeek.repository.userrepo;

import com.project.sportsgeek.model.profile.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userRepo")
public interface UserRepository {

    List<User> findAllUsers() ;

    User findUserByUserId(int id) throws Exception;

    List<UserWithPassword> findUserByUserName(String userName) throws Exception;

    List<User> findAllUsersByRole(int roleId) throws Exception;

    List<User> findUserByEmailIdAndMobileNumber(User user) throws Exception;

    List<UserWinningAndLossingPoints> findLoosingPointsByUserId(int userId) throws Exception;

    List<UserWinningAndLossingPoints> findWinningPointsByUserId(int userId) throws Exception;

    List<User> findUsersByStatus(boolean status) throws Exception;

    int addUser(UserWithPassword userWithPassword) throws Exception;


    boolean updateUser(int id, User user) throws Exception;

    boolean updateStatus(int id, boolean status) throws Exception;

    int updateUserPassword(UserWithNewPassword userWithNewPassword) throws Exception;

    int updateForgetPassword(UserWithOtp userWithOtp) throws Exception;

    int updateUserRole(int userId, int roleId) throws Exception;

    boolean updateUserProfilePicture(int userId, String profilePicture) throws Exception;

    boolean updateUserAvailablePoints(int userId, int availablePoints) throws Exception;

    UserForLoginState authenticate(UserAtLogin userAtLogin) throws Exception;

    boolean deleteUser(int id) throws Exception;

    int addAvailablePoints(int userId, int points) throws Exception;

    int deductAvailablePoints(int userId, int points) throws Exception;

    int getUsersCountByUsername(String username) throws Exception;
}
