package com.project.sportsgeek.repository.userrepo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.project.sportsgeek.mapper.UserRowMapper;
import com.project.sportsgeek.mapper.UserWithPasswordRowMapper;
import com.project.sportsgeek.mapper.UserWithWinningPointsRowMapper;
import com.project.sportsgeek.mapper.userWithLoosingPointsRowMapper;
import com.project.sportsgeek.model.profile.User;
import com.project.sportsgeek.model.profile.UserAtLogin;
import com.project.sportsgeek.model.profile.UserForLoginState;
import com.project.sportsgeek.model.profile.UserWinningAndLossingPoints;
import com.project.sportsgeek.model.profile.UserWithNewPassword;
import com.project.sportsgeek.model.profile.UserWithOtp;
import com.project.sportsgeek.model.profile.UserWithPassword;

@Repository(value = "userRepo")
public class UserRepoImpl implements UserRepository {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- SELECT QUERY ------------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------


	@Override
	public List<User> findAllUsers() {
		String sql = "SELECT * FROM User";
		return namedParameterJdbcTemplate.query(sql, new UserRowMapper());
	}

	@Override
	public List<User> findUserByUserId(int userId) throws Exception {
		String sql = "SELECT User.UserId as UserId, FirstName, LastName, GenderId, RoleId, Username, AvailablePoints, ProfilePicture, Status, EmailContact.EmailId as Email, MobileContact.MobileNumber as MobileNumber FROM User inner join EmailContact on User.UserId=EmailContact.UserId inner join MobileContact on User.UserId=MobileContact.UserId WHERE User.UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(user), new UserRowMapper());
	}

	@Override
	public List<User> findAllUsersByRole(int roleId) throws Exception {
		String sql = "SELECT * FROM User WHERE RoleId= :roleId";
		User user = new User();
		user.setRoleId(roleId);
		return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(user), new UserRowMapper());
	}

	@Override
	public List<User> findUserByEmailId(User user) throws Exception {
		String sql = "SELECT User.UserId as UserId, FirstName, LastName, GenderId, RoleId, Username, AvailablePoints, ProfilePicture, Status, EmailContact.EmailId as Email, MobileContact.MobileNumber as MobileNumber "
				+ "FROM User inner join EmailContact on User.UserId=EmailContact.UserId inner join MobileContact on User.UserId=MobileContact.UserId WHERE EmailContact.EmailId=:email AND MobileContact.MobileNumber=:mobileNumber";
		return namedParameterJdbcTemplate.query(sql, new UserRowMapper());
	}

	@Override
	public List<User> findUsersByStatus(boolean status) throws Exception {
		String sql = "SELECT User.UserId as UserId, FirstName, LastName, GenderId, RoleId, Username, AvailablePoints, ProfilePicture, Status, EmailContact.EmailId as Email, MobileContact.MobileNumber as MobileNumber "
				+ "FROM User inner join EmailContact on User.UserId=EmailContact.UserId inner join MobileContact on User.UserId=MobileContact.UserId WHERE User.Status=:status";
		User user = new User();
		user.setStatus(status);
		return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(user), new UserRowMapper());
	}

	@Override
	public List<UserWinningAndLossingPoints> findLoosingPointsByUserId(int userId) throws Exception {
		String sql = "SELECT SUM(BetPoints) as LoosingPoints,bot.UserId \n"
				+ "FROM Contest as bot INNER JOIN Matches as m ON bot.MatchId = m.MatchId\n"
				+ "WHERE WinningPoints=0 AND m.ResultStatus=1 AND UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(user),
				new userWithLoosingPointsRowMapper());
	}

	@Override
	public List<UserWinningAndLossingPoints> findWinningPointsByUserId(int userId) throws Exception {
		String sql = "select sum(WinningPoints) as WinningPoints,UserId from Contest where UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(user),
				new UserWithWinningPointsRowMapper());
	}

	@Override
	public List<UserWithPassword> findUserByUserName(String Username) throws Exception {
		String sql = "SELECT u.UserName as UserName,u.Password as Password,r.Name as Name FROM User as u INNER JOIN Role as r on u.RoleId=r.RoleId WHERE UserName=:Username";
		User user = new User();
		user.setUsername(Username);
		return namedParameterJdbcTemplate.query(sql, new BeanPropertySqlParameterSource(user),
				new UserWithPasswordRowMapper());
	}

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- AUTHENTICATION QUERY ----------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------


	@Override
	public UserForLoginState authenticate(UserAtLogin userAtLogin) throws Exception {
		System.out.println("Authenticate from repo called...");
//		String sql = "select u.UserId, u.UserName, r.Name, u.Status from User as u inner join Role as r on u.RoleId = r.RoleId where u.UserName=:username";
		String sql = "SELECT u.UserId AS UserId, Username, r.Name AS Role, Status from User as u inner join Role as r on u.RoleId = r.RoleId where u.UserName=:username";

		List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sql, new BeanPropertySqlParameterSource(userAtLogin));
		System.out.println("List size : " + list.size());
		if (list.size() > 0) {
			return new UserForLoginState(Integer.parseInt(list.get(0).get("UserId") + ""),
					list.get(0).get("UserName") + "", list.get(0).get("Role") + "",
					Boolean.parseBoolean(list.get(0).get("Status").toString()), "");
		}
		return null;
	}

//	--------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- INSERT QUERY -----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------


	@Override
	public int addUser(UserWithPassword userWithPassword) throws Exception {

		KeyHolder holder = new GeneratedKeyHolder();
		String insert_sql = "INSERT INTO User (FirstName,LastName,GenderId,Username,Password,ProfilePicture,RoleId,AvailablePoints,Status)"
				+ "values(:firstName,:lastName,:genderId,:Username,:password,:profilePicture,:roleId,:availablePoints,:status)";
		namedParameterJdbcTemplate.update(insert_sql, new BeanPropertySqlParameterSource(userWithPassword), holder);
		return holder.getKey().intValue();
	}

	@Override
	public int addEmail(UserWithPassword userWithPassword) throws Exception {

		String email_sql = "INSERT INTO EmailContact (UserId,EmailId) values(:userId, :email)";
		System.out.println(userWithPassword.getUserId());
		System.out.println(userWithPassword.getEmail());
		return namedParameterJdbcTemplate.update(email_sql, new BeanPropertySqlParameterSource(userWithPassword));
	}

	@Override
	public int addMobile(UserWithPassword userWithPassword) throws Exception {

		String mobile_sql = "INSERT INTO MobileContact (UserId,MobileNumber) values(:userId, :mobileNumber)";
		return namedParameterJdbcTemplate.update(mobile_sql, new BeanPropertySqlParameterSource(userWithPassword));
	}

//	--------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- UPDATE QUERY -----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------


	@Override
	public boolean updateUser(int userId, User user) throws Exception {
		String update_query = "UPDATE User SET FirstName =:firstName, LastName =:lastName, GenderId =:genderId WHERE UserId=:userId";
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(update_query, new BeanPropertySqlParameterSource(user)) > 0;
	}

	@Override
	public boolean updateStatus(int userId, boolean status) throws Exception {
		String update_status = "UPDATE User set Status =:status WHERE UserId =:userId";
		User user = new User();
		user.setUserId(userId);
		user.setStatus(status);
		return namedParameterJdbcTemplate.update(update_status, new BeanPropertySqlParameterSource(user)) > 0;
	}

	@Override
	public int updateUserPassword(UserWithNewPassword userWithNewPassword) throws Exception {

		String sql = "UPDATE User SET Password =:newPassword WHERE UserId =:userId";
		System.out.println(sql);
		return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(userWithNewPassword));
	}

	@Override
	public int updateForgetPassword(UserWithOtp userWithOtp) throws Exception {
		System.out.println("Password : " + userWithOtp.getPassword());
		String encodedPassword = bCryptPasswordEncoder.encode(userWithOtp.getPassword());
		String sql = "UPDATE User SET Password ='" + encodedPassword + "' WHERE UserId=:userId";
		return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(userWithOtp));
	}

	@Override
	public int updateUserRole(int userId, int role) throws Exception {
		String sql = "UPDATE User SET RoleId=:roleId WHERE UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		user.setRoleId(role);
		return namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(user));
	}

	@Override
	public boolean updateUserProfilePicture(int userId, String profilePicture) throws Exception {
		String update_pp = "UPDATE User SET ProfilePicture =:profilePicture WHERE UserId =:userId";
		User user = new User();
		user.setUserId(userId);
		user.setProfilePicture(profilePicture);
		return namedParameterJdbcTemplate.update(update_pp, new BeanPropertySqlParameterSource(user)) > 0;
	}

	@Override
	public boolean updateUserAvailablePoints(int userId, int availablePoints) throws Exception {
		String update_AvailablePoints = "UPDATE User SET AvailablePoints =:availablePoints WHERE UserId =:userId";
		User user = new User();
		user.setUserId(userId);
		user.setAvailablePoints(availablePoints);
		return namedParameterJdbcTemplate.update(update_AvailablePoints, new BeanPropertySqlParameterSource(user)) > 0;
	}

	@Override
	public boolean updateEmail(int userId, User user) throws Exception {
		String update_email = "UPDATE EmailContact SET EmailId =:email WHERE UserId =:userId";
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(update_email, new BeanPropertySqlParameterSource(user)) > 0;
	}

	@Override
	public boolean updateMobile(int userId, User user) throws Exception {
		String update_mobile = "UPDATE MobileContact SET MobileNumber=:mobileNumber WHERE UserId=:userId";
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(update_mobile, new BeanPropertySqlParameterSource(user)) > 0;
	}

	@Override
	public int addAvailablePoints(int userId, int points) throws Exception {
		String insert_points = "UPDATE User set AvailablePoints = AvailablePoints + :points WHERE UserId=:userId";
		User user = new User();
		user.setAvailablePoints(points);
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(insert_points, new BeanPropertySqlParameterSource(user));
	}

	@Override
	public int deductAvailablePoints(int userId, int points) throws Exception {
		String deduct_points = "UPDATE User SET AvailablePoints = AvailablePoints - :points WHERE UserId = :userId";
		User user = new User();
		user.setAvailablePoints(points);
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(deduct_points, new BeanPropertySqlParameterSource(points));
	}

//	--------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- DELETE QUERY -----------------------------------------------------------------------------
//	--------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public int deleteUser(int userId) throws Exception {
		String delete_user = "DELETE FROM User WHERE UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(delete_user, new BeanPropertySqlParameterSource(user));
	}

	@Override
	public int deleteEmail(int userId) throws Exception {
		String delete_emailContact = "DELETE FROM EmailContact WHERE UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(delete_emailContact, new BeanPropertySqlParameterSource(user));
	}

	@Override
	public int deleteMobile(int userId) throws Exception {
		String delete_mobileContact = "DELETE FROM MobileContact WHERE UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(delete_mobileContact, new BeanPropertySqlParameterSource(user));
	}

	@Override
	public int deleteBOT(int userId) throws Exception {
		String delete_betOnTeam = "DELETE FROM Contest Where UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		namedParameterJdbcTemplate.update(delete_betOnTeam, new BeanPropertySqlParameterSource(user));
		return 0;
	}

	@Override
	public int deleteRecharge(int userId) throws Exception {
		String delete_Recharge = "DELETE FROM Recharge WHERE UserId=:userId";
		User user = new User();
		user.setUserId(userId);
		return namedParameterJdbcTemplate.update(delete_Recharge, new BeanPropertySqlParameterSource(user));
	}

}
