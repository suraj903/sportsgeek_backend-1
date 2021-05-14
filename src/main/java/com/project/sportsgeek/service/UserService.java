package com.project.sportsgeek.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.sportsgeek.exception.ResultException;
import com.project.sportsgeek.mapper.UserWithPasswordRowMapper;
import com.project.sportsgeek.model.Email;
import com.project.sportsgeek.model.profile.User;
import com.project.sportsgeek.model.profile.UserAtLogin;
import com.project.sportsgeek.model.profile.UserForLoginState;
import com.project.sportsgeek.model.profile.UserWinningAndLossingPoints;
import com.project.sportsgeek.model.profile.UserWithNewPassword;
import com.project.sportsgeek.model.profile.UserWithOtp;
import com.project.sportsgeek.model.profile.UserWithPassword;
import com.project.sportsgeek.repository.userrepo.UserRepository;
import com.project.sportsgeek.response.Result;

import lombok.SneakyThrows;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	EmailService emailService;
	private int otp;
	private int sendOtp;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- SELECT SERVICE ----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	public Result<List<User>> findAllUsers() {
		List<User> userList = userRepository.findAllUsers();
		return new Result<>(200, userList);
	}

	public Result<User> findUserByUserId(int id) throws Exception {
		List<User> userList = userRepository.findUserByUserId(id);
		if (userList.size() > 0) {
			return new Result<>(200, userList.get(0));
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((id + "").hashCode(),
							"user with given id('" + id + "') does not exists")))));
		}
	}

	public Result<UserWinningAndLossingPoints> findUserLoosingPoints(int id) throws Exception {
		List<UserWinningAndLossingPoints> userList = userRepository.findLoosingPointsByUserId(id);
		if (userList.size() > 0) {
			return new Result<>(200, userList.get(0));
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((id + "").hashCode(),
							"user with given id('" + id + "') does not exists")))));
		}
	}

	public Result<UserWinningAndLossingPoints> findUserWinningPoints(int id) throws Exception {
		List<UserWinningAndLossingPoints> userList = userRepository.findWinningPointsByUserId(id);
		if (userList.size() > 0) {
			return new Result<>(200, userList.get(0));
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((id + "").hashCode(),
							"user with given id('" + id + "') does not exists")))));
		}
	}

	public Result<UserWithPassword> findUserByUserName(String userName) throws Exception {
		List<UserWithPassword> userList = userRepository.findUserByUserName(userName);
		if (userList.size() > 0) {
			return new Result<>(200, userList.get(0));
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((userName + "").hashCode(),
							"user with given Username('" + userName + "') does not exists")))));
		}
	}

	public Result<List<User>> findUsersByStatus(boolean status) throws Exception {
		List<User> userList = userRepository.findUsersByStatus(status);
		if (userList.size() > 0) {
			return new Result<>(200, userList);
		} else {

			return new Result<>(400, "No User's Found, Please try again!");
		}
	}

	public Result<List<User>> findUsersByRole(int roleId) throws Exception {
		List<User> userList = userRepository.findAllUsersByRole(roleId);
		if (userList.size() > 0) {
			return new Result<>(200, userList);
		} else {

			return new Result<>(400, "No User's Found, Please try again!");
		}
	}

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		List<UserWithPassword> userList = userRepository.findUserByUserName(s);
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + userList.get(0).getRole()));
		if (userList == null) {
			throw new UsernameNotFoundException("User not found with username: " + s);
		} else {
			return new org.springframework.security.core.userdetails.User(userList.get(0).getUsername(),
					userList.get(0).getPassword(), authorities);
		}
	}

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- AUTHENTICATION SERVICE --------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	public Result<UserForLoginState> authenticate(UserAtLogin userAtLogin) throws Exception {
		UserForLoginState userForLoginState = userRepository.authenticate(userAtLogin);
		System.out.println("UserForLoginState:" + userForLoginState);
		if (userForLoginState != null) {
			if (userForLoginState.isStatus() == false) {
//				return new Result<>(401, "Sorry! you have been blocked by the admin");
//				throw new DisabledException("Sorry! you have been blocked by the admin");
//				throw new ResultException(new Result<>(401, "Sorry! you have been blocked by the admin",
//						new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError(999,
//								"Sorry! you have been blocked by the admin")))));
				throw new ResultException(new Result<>(401, "Sorry! you have been blocked by the admin"));
			} else {
				return new Result<>(200, userForLoginState);
			}
		}
//		return new Result<>(400, "Invalid username or password!");
//		throw new BadCredentialsException("Invalid username or password!");
		throw new ResultException(new Result<>(400, "Invalid username or password!"));
	}

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- INSERT SERVICE ----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	public Result<User> addUser(UserWithPassword userWithPassword) throws Exception {
		RowCountCallbackHandler countCallback = new RowCountCallbackHandler();
		namedParameterJdbcTemplate.query("select * from User WHERE UserName= :username",
				new BeanPropertySqlParameterSource(userWithPassword), countCallback);
		int username = countCallback.getRowCount();
		System.out.println("UserName Count :- " + username);
		if (username > 0) {
			throw new ResultException(new Result<>(500, "User Already Exists"));
		} else {
			int id = userRepository.addUser(userWithPassword);
			userWithPassword.setUserId(id);
			System.out.println(id);
			if (id > 0) {
				userRepository.addEmail(userWithPassword);
				userRepository.addMobile(userWithPassword);
				// Send Email to User
				String subject = "New User Registration!!";
				String msg = "Hello " + userWithPassword.getFirstName() + " " + userWithPassword.getLastName()
						+ "\n Welcome to SportsGeek.\n"
						+ "Your account is pending for approval by Admin. Wait For the Response of the approval of account.";
				Email email = Email.builder().setSubject(subject).setTo(userWithPassword.getEmail()).message(msg)
						.build();
				emailService.sendEmail(email);

				// Send Email to Admin
				String admin_subject = "New User Registration Approval!!";
				String adm_msg = "Hello Admin \n New user With Name:" + userWithPassword.getFirstName() + " "
						+ userWithPassword.getLastName() + " and username: " + userWithPassword.getUsername() + " "
						+ " has Registered for SportsGeek, Please Approve if he/she is a valid user.\n"
						+ "Thanking you\n" + "SportsGeek Team";
				String admin_email = "admn.sportsgeek@gmail.com";
				Email adm_sendemail = Email.builder().setSubject(admin_subject).setTo(admin_email).message(adm_msg)
						.build();
				emailService.sendEmail(adm_sendemail);
				return new Result<>(200, userWithPassword);
			} else {
				throw new ResultException(new Result<>(500, "User Already Exists"));
			}
		}
	}

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- UPDATE SERVICE ----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	public Result<User> updateUser(int id, User user) throws Exception {
		if (userRepository.updateUser(id, user)) {
			userRepository.updateEmail(id, user);
			userRepository.updateMobile(id, user);
			return new Result<>(201, (User) user);
		}
		return new Result<>(400, "Given User Id does not exists");
	}

	public Result<User> updateStatus(int id, boolean status) throws Exception {
		if (userRepository.updateStatus(id, status)) {
			List<User> userList = userRepository.findUserByUserId(id);
			String sub = "Account Approved!!";
			String updateStatus_msg = "Congratulations " + userList.get(0).getFirstName() + " "
					+ userList.get(0).getLastName() + ",\n\n" + "Your account is approved by the Admin. \n"
					+ "Your username is \"" + userList.get(0).getUsername() + "\".\n"
					+ "Now, You can login to the app and enjoy SportsGeek.\n" + "\n" + "Thanking you\n"
					+ "SportsGeek Team";
			String user_email = userList.get(0).getEmail();
			Email email = Email.builder().setSubject(sub).setTo(user_email).message(updateStatus_msg).build();
			emailService.sendEmail(email);
			return new Result<>(201, "status of given id(" + id + ") has been succefully updated");
		}
		return new Result<>(400, "No User's Found, Please try again!");
	}

	public Result<String> updateUserRole(int userId, int role) throws Exception {
		int result = userRepository.updateUserRole(userId, role);
		if (result > 0) {
			return new Result<>(201, "Successfully Assigned Role to User");
		} else {
			return new Result<>(500, "Internal Server error!, Unable to update the Role");
		}
	}

//	------------------------------------------------- UPDATE PASSWORD SERVICE --------------------------------------------------------------------

	public Result<String> updatePassword(UserWithNewPassword userWithNewPassword) throws Exception {
		String select_password = "SELECT u.UserName as UserName,u.Password as Password,r.Name as Name FROM User as u INNER JOIN Role as r on u.RoleId=r.RoleId WHERE UserId= :userId";
		System.out.println(select_password);
		List<UserWithPassword> userWithOldPassword = namedParameterJdbcTemplate.query(select_password,
				new BeanPropertySqlParameterSource(userWithNewPassword), new UserWithPasswordRowMapper());
		System.out.println(userWithOldPassword);
		if (userWithOldPassword.size() > 0 && bCryptPasswordEncoder.matches(userWithNewPassword.getOldPassword(),
				userWithOldPassword.get(0).getPassword())) {
			userWithNewPassword.setNewPassword(bCryptPasswordEncoder.encode(userWithNewPassword.getNewPassword()));
			int result = userRepository.updateUserPassword(userWithNewPassword);
			System.out.println(result);
			if (result > 0) {
				return new Result<>(200, "password has been succefully updated");
			} else {

				return new Result<>(500, "Internal Server error!, Unable to update the password");
			}
		}
		return new Result<>(400,"Old Password does not match!");

	}

//	------------------------------------------------- UPDATE FORGOT PASSWORD SERVICE -------------------------------------------------------------

	public int generateOTP() {

		Random random = new Random();
		otp = 100000 + random.nextInt(900000);
		return otp;
	}

	public Result<User> findUserByEmailId(User user) throws Exception {
		List<User> userList = userRepository.findUserByEmailId(user);
		System.out.println(userList);
		if (userList.size() > 0) {
			sendOtp = generateOTP();
			String subject = "Forgot Password OTP Verification!!";
			String msg = "Hello " + userList.get(0).getFirstName() + " " + userList.get(0).getLastName()
					+ "\n Your OTP For Password Change.\n" + "OTP:" + sendOtp + "";
			System.out.println(msg);
			Email email = Email.builder().setSubject(subject).setTo(userList.get(0).getEmail()).message(msg).build();
			emailService.sendEmail(email);
			return new Result<>(200, userList.get(0));
		} else {
			return new Result<>(404, "Email Id And Mobile Number Not Found");

		}
	}

	public Result<String> updateForgetPassword(UserWithOtp userWithOtp) throws Exception {
		System.out.println("Send OTP in Update Password Service:" + sendOtp);
		System.out.println("Otp Received by service" + userWithOtp.getOtp());
		if (userWithOtp.getOtp() == sendOtp) {
			int result = userRepository.updateForgetPassword(userWithOtp);
			if (result > 0) {
				return new Result<>(200, "password has been successfully Changed");
			} else {

				return new Result<>(500, "Internal Server error!, Unable to update the password");
			}
		} else {
			return new Result<>(404, "OTP Not Match");
		}
	}

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- DELETE SERVICE ----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	public Result<User> deleteUser(int id) throws Exception {
		int result = userRepository.deleteEmail(id);
		if (result > 0) {
			userRepository.deleteMobile(id);
			userRepository.deleteRecharge(id);
			userRepository.deleteBOT(id);
			userRepository.deleteUser(id);
			return new Result<>(201, "User Deleted Successfully!!");
		}

		return new Result<>(400, "Given User Id does not exists");
	}

}
