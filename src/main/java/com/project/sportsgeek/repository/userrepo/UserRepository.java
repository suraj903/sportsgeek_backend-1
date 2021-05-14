package com.project.sportsgeek.repository.userrepo;

import com.project.sportsgeek.model.profile.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userRepo")
public interface UserRepository {

    List<User> findAllUsers() ;

    List<User> findUserByUserId(int id) throws Exception;

    List<UserWithPassword> findUserByUserName(String userName) throws Exception;

    List<User> findAllUsersByRole(int roleId) throws Exception;

    List<User> findUserByEmailId(User user) throws Exception;

    List<UserWinningAndLossingPoints> findLoosingPointsByUserId(int userId) throws Exception;

    List<UserWinningAndLossingPoints> findWinningPointsByUserId(int userId) throws Exception;

    List<User> findUsersByStatus(boolean status) throws Exception;

    int addUser(UserWithPassword userWithPassword) throws Exception;

    int addEmail(UserWithPassword userWithPassword) throws Exception;

    int addMobile(UserWithPassword userWithPassword) throws Exception;

    boolean updateUser(int id, User user) throws Exception;

    boolean updateStatus(int id, boolean status) throws Exception;

    int updateUserPassword(UserWithNewPassword userWithNewPassword) throws Exception;

    int updateForgetPassword(UserWithOtp userWithOtp) throws Exception;

    int updateUserRole(int userId, int role) throws Exception;

    boolean updateUserProfilePicture(int userId, String profilePicture) throws Exception;

    boolean updateUserAvailablePoints(int userId, int availablePoints) throws Exception;

    UserForLoginState authenticate(UserAtLogin userAtLogin) throws Exception;

    int deleteUser(int id) throws Exception;

    int deleteEmail(int id) throws Exception;

    int deleteMobile(int id) throws Exception;

    int deleteBOT(int id) throws Exception;

    int deleteRecharge(int id) throws Exception;

    boolean updateEmail(int id, User user) throws Exception;

    boolean updateMobile(int id, User user) throws Exception;

    int addAvailablePoints(int userId, int points) throws Exception;

    int deductAvailablePoints(int userId, int points) throws Exception;
}
