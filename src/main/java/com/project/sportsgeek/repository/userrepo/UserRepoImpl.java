package com.project.sportsgeek.repository.userrepo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- SELECT QUERY ------------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------


	@Override
	public List<User> findAllUsers() {
		String sql = "SELECT * FROM User";
		return jdbcTemplate.query(sql, new UserRowMapper());
	}

	@Override
	public User findUserByUserId(int userId) throws Exception {
		String sql = "SELECT User.UserId as UserId, FirstName, LastName, GenderId, RoleId, Username, AvailablePoints, ProfilePicture, Status, EmailContact.EmailId as Email, MobileContact.MobileNumber as MobileNumber FROM User inner join EmailContact on User.UserId=EmailContact.UserId inner join MobileContact on User.UserId=MobileContact.UserId WHERE User.UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
		List<User> userList = jdbcTemplate.query(sql, params, new UserRowMapper());
		if(userList.size() > 0){
			return userList.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<User> findAllUsersByRole(int roleId) throws Exception {
		String sql = "SELECT * FROM User WHERE RoleId= :roleId";
		MapSqlParameterSource params = new MapSqlParameterSource("roleId", roleId);
		return jdbcTemplate.query(sql, params, new UserRowMapper());
	}

	@Override
	public List<User> findUserByEmailIdAndMobileNumber(User user) throws Exception {
		String sql = "SELECT User.UserId as UserId, FirstName, LastName, GenderId, RoleId, Username, AvailablePoints, ProfilePicture, Status, EmailContact.EmailId as Email, MobileContact.MobileNumber as MobileNumber "
				+ "FROM User inner join EmailContact on User.UserId=EmailContact.UserId inner join MobileContact on User.UserId=MobileContact.UserId WHERE EmailContact.EmailId = :emailId AND MobileContact.MobileNumber = :mobileNumber";
		MapSqlParameterSource params = new MapSqlParameterSource("emailId", user.getEmail());
		params.addValue("mobileNumber", user.getMobileNumber());
		return jdbcTemplate.query(sql, params, new UserRowMapper());
	}

	@Override
	public List<User> findUsersByStatus(boolean status) throws Exception {
		String sql = "SELECT User.UserId as UserId, FirstName, LastName, GenderId, RoleId, Username, AvailablePoints, ProfilePicture, Status, EmailContact.EmailId as Email, MobileContact.MobileNumber as MobileNumber "
				+ "FROM User inner join EmailContact on User.UserId=EmailContact.UserId inner join MobileContact on User.UserId=MobileContact.UserId WHERE User.Status = :status";
		MapSqlParameterSource params = new MapSqlParameterSource("status", status);
		return jdbcTemplate.query(sql, params, new UserRowMapper());
	}

	@Override
	public List<UserWinningAndLossingPoints> findLoosingPointsByUserId(int userId) throws Exception {
		String sql = "SELECT SUM(ContestPoints) as LoosingPoints,c.UserId \n"
				+ "FROM Contest as c INNER JOIN Matches as m ON c.MatchId = m.MatchId\n"
				+ "WHERE WinningPoints=0 AND m.ResultStatus=1 AND UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(sql, params, new userWithLoosingPointsRowMapper());
	}

	@Override
	public List<UserWinningAndLossingPoints> findWinningPointsByUserId(int userId) throws Exception {
		String sql = "select sum(WinningPoints) as WinningPoints,UserId from Contest where UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(sql, params, new UserWithWinningPointsRowMapper());
	}

	@Override
	public List<UserWithPassword> findUserByUserName(String username) throws Exception {
		String sql = "SELECT u.UserName as UserName,u.Password as Password,r.Name as Name FROM User as u INNER JOIN Role as r on u.RoleId=r.RoleId WHERE UserName = :username";
		MapSqlParameterSource params = new MapSqlParameterSource("username", username);
		return jdbcTemplate.query(sql, params, new UserWithPasswordRowMapper());
	}

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- AUTHENTICATION QUERY ----------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------


	@Override
	public UserForLoginState authenticate(UserAtLogin userAtLogin) throws Exception {
		System.out.println("Authenticate from repo called...");
//		String sql = "select u.UserId, u.UserName, r.Name, u.Status from User as u inner join Role as r on u.RoleId = r.RoleId where u.UserName=:username";
		String sql = "SELECT u.UserId AS UserId, Username, r.Name AS Role, Status from User as u inner join Role as r on u.RoleId = r.RoleId where Username=:username";
//		String sql = "SELECT u.UserId AS UserId, Username, r.Name AS Role, Status from User as u inner join Role as r on u.RoleId = r.RoleId where u.UserName=:username and u.Password=:password";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new BeanPropertySqlParameterSource(userAtLogin));
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
		int n = jdbcTemplate.update(insert_sql, new BeanPropertySqlParameterSource(userWithPassword), holder);
		if(n > 0){
			return holder.getKey().intValue();
		}else{
			return 0;
		}
	}

//	--------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- UPDATE QUERY -----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean updateUser(int userId, User user) throws Exception {
		String update_query = "UPDATE User SET FirstName =:firstName, LastName =:lastName, GenderId =:genderId WHERE UserId=:userId";
		user.setUserId(userId);
		return jdbcTemplate.update(update_query, new BeanPropertySqlParameterSource(user)) > 0;
	}

	@Override
	public boolean updateStatus(int userId, boolean status) throws Exception {
		String update_status = "UPDATE User set Status = :status WHERE UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("status", status);
		params.addValue("userId", userId);
		return jdbcTemplate.update(update_status, params) > 0;
	}

	@Override
	public int updateUserPassword(UserWithNewPassword userWithNewPassword) throws Exception {
		String sql = "UPDATE User SET Password = :newPassword WHERE UserId = :userId";
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(userWithNewPassword));
	}

	@Override
	public int updateForgetPassword(UserWithOtp userWithOtp) throws Exception {
		System.out.println("Password : " + userWithOtp.getPassword());
		String encodedPassword = bCryptPasswordEncoder.encode(userWithOtp.getPassword());
		String sql = "UPDATE User SET Password = :encodedPassword WHERE UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("encodedPassword", encodedPassword);
		params.addValue("userId", userWithOtp.getUserId());
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public int updateUserRole(int userId, int roleId) throws Exception {
		String sql = "UPDATE User SET RoleId = :roleId WHERE UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("roleId", roleId);
		params.addValue("userId", userId);
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public boolean updateUserProfilePicture(int userId, String profilePicture) throws Exception {
		String sql = "UPDATE User SET ProfilePicture = :profilePicture WHERE UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("profilePicture", profilePicture);
		params.addValue("userId", userId);
		return jdbcTemplate.update(sql, params) > 0;
	}

	@Override
	public boolean updateUserAvailablePoints(int userId, int availablePoints) throws Exception {
		String sql = "UPDATE User SET AvailablePoints = :availablePoints WHERE UserId =:userId";
		MapSqlParameterSource params = new MapSqlParameterSource("availablePoints", availablePoints);
		return jdbcTemplate.update(sql, params) > 0;
	}

	@Override
	public int addAvailablePoints(int userId, int points) throws Exception {
		String sql = "UPDATE User SET AvailablePoints = AvailablePoints + :points WHERE UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("points", points);
		params.addValue("userId", userId);
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public int deductAvailablePoints(int userId, int points) throws Exception {
		String sql = "UPDATE User SET AvailablePoints = AvailablePoints-:points WHERE UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("points", points);
		params.addValue("userId", userId);
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public int getUsersCountByUsername(String username) throws Exception {
		RowCountCallbackHandler countCallback = new RowCountCallbackHandler();
		MapSqlParameterSource params = new MapSqlParameterSource("username", username);
		jdbcTemplate.query("SELECT * FROM User WHERE Username= :username", params, countCallback);
		return countCallback.getRowCount();
	}

//	--------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- DELETE QUERY -----------------------------------------------------------------------------
//	--------------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean deleteUser(int userId) throws Exception {
		String sql = "DELETE FROM User WHERE UserId = :userId";
		MapSqlParameterSource params = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(userId)) > 0;
	}

}
