package com.project.sportsgeek.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.project.sportsgeek.model.Email;
import com.project.sportsgeek.model.EmailContact;
import com.project.sportsgeek.model.MobileContact;
import com.project.sportsgeek.repository.contestrepo.ContestRepository;
import com.project.sportsgeek.repository.emailcontactrepo.EmailContactRepository;
import com.project.sportsgeek.repository.mobilecontactrepo.MobileContactRepository;
import com.project.sportsgeek.repository.rechargerepo.RechargeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.sportsgeek.exception.ResultException;
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
	@Qualifier(value = "userRepo")
	UserRepository userRepository;

	@Autowired
	@Qualifier(value = "emailContactRepo")
	EmailContactRepository emailContactRepository;

	@Autowired
	@Qualifier(value = "mobileContactRepo")
	MobileContactRepository mobileContactRepository;

	@Autowired
	@Qualifier(value = "rechargeRepo")
	RechargeRepository rechargeRepository;

	@Autowired
	@Qualifier(value = "contestRepo")
	ContestRepository contestRepository;

	@Autowired
	EmailService emailService;

	private int otp;
	private int sendOtp;

//	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- SELECT SERVICE ----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	public Result<List<User>> findAllUsers() {
		List<User> userList = userRepository.findAllUsers();
		return new Result<>(200, userList);
	}

	public Result<User> findUserByUserId(int userId) throws Exception {
		User user = userRepository.findUserByUserId(userId);
		if (user != null) {
			return new Result<>(200, user);
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((userId + "").hashCode(),
							"user with given id('" + userId + "') does not exists")))));
		}
	}

	public Result<UserWinningAndLossingPoints> findUserLoosingPoints(int userId) throws Exception {
		List<UserWinningAndLossingPoints> userList = userRepository.findLoosingPointsByUserId(userId);
		if (userList.size() > 0) {
			return new Result<>(200, userList.get(0));
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((userId + "").hashCode(),
							"user with given id('" + userId + "') does not exists")))));
		}
	}

	public Result<UserWinningAndLossingPoints> findUserWinningPoints(int userId) throws Exception {
		List<UserWinningAndLossingPoints> userList = userRepository.findWinningPointsByUserId(userId);
		if (userList.size() > 0) {
			return new Result<>(200, userList.get(0));
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((userId + "").hashCode(),
							"user with given id('" + userId + "') does not exists")))));
		}
	}

	public Result<UserWithPassword> findUserByUserName(String username) throws Exception {
		List<UserWithPassword> userList = userRepository.findUserByUserName(username);
		if (userList.size() > 0) {
			return new Result<>(200, userList.get(0));
		} else {
			throw new ResultException(new Result<>(404, "no user's found, please try again!",
					new ArrayList<>(Arrays.asList(new Result.SportsGeekSystemError((username + "").hashCode(),
							"user with given Username('" + username + "') does not exists")))));
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
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserWithPassword> userList = userRepository.findUserByUserName(username);
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + userList.get(0).getRole()));
		if (userList == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
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
		int usernameCount = userRepository.getUsersCountByUsername(userWithPassword.getUsername());
		int emailCount = emailContactRepository.getCountByEmailId(userWithPassword.getEmail());
		if (usernameCount > 0) {
			throw new ResultException(new Result<>(404, "Username already exists."));
		}
		else if(emailCount > 0){
			throw new ResultException(new Result<>(400, "Email already exists."));
		}
		else {
			int userId = userRepository.addUser(userWithPassword);
			userWithPassword.setUserId(userId);
			if (userId > 0) {
				// Add Email
				EmailContact emailContact = new EmailContact();
				emailContact.setUserId(userId);
				emailContact.setEmailId(userWithPassword.getEmail());
				emailContactRepository.addEmailContact(emailContact);
				// Add Mobile Number
				MobileContact mobileContact = new MobileContact();
				mobileContact.setUserId(userId);
				mobileContact.setMobileNumber(userWithPassword.getMobileNumber());
				mobileContactRepository.addMobileContact(mobileContact);

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
				throw new ResultException(new Result<>(500, "Unable to add User"));
			}
		}
	}

//	---------------------------------------------------------------------------------------------------------------------------------------------
//	------------------------------------------------- UPDATE SERVICE ----------------------------------------------------------------------------
//	---------------------------------------------------------------------------------------------------------------------------------------------

	public Result<User> updateUser(int userId, User user) throws Exception {
		// Update User
		if (userRepository.updateUser(userId, user)) {
			// Update Email
			EmailContact emailContact = new EmailContact();
			emailContact.setEmailId(user.getEmail());
			emailContactRepository.updateEmailContactByUserId(userId, emailContact);
			// Add Mobile Number
			MobileContact mobileContact = new MobileContact();
			mobileContact.setMobileNumber(user.getMobileNumber());
			mobileContactRepository.updateMobileContactByUserId(userId, mobileContact);
			return new Result<>(201, user);
		}
//		return new Result<>(400, "Given User Id does not exists");
		throw new ResultException((new Result<>(404, "No User's found,please try again", "User with id=('" + userId + "') not found")));
	}

	public Result<User> updateStatus(int userId, boolean status) throws Exception {
		if (userRepository.updateStatus(userId, status)) {
			User user = userRepository.findUserByUserId(userId);
			String sub = "Account Approved!!";
			String updateStatus_msg = "Congratulations " + user.getFirstName() + " "
					+ user.getLastName() + ",\n\n" + "Your account is approved by the Admin. \n"
					+ "Your username is \"" + user.getUsername() + "\".\n"
					+ "Now, You can login to the app and enjoy SportsGeek.\n" + "\n" + "Thanking you\n"
					+ "SportsGeek Team";
			String user_email = user.getEmail();
			Email email = Email.builder().setSubject(sub).setTo(user_email).message(updateStatus_msg).build();
			emailService.sendEmail(email);
			return new Result<>(201, "status of given id(" + userId + ") has been successfully updated");
		}
		return new Result<>(400, "No User's Found, Please try again!");
	}

	public Result<String> updateUserRole(int userId, int roleId) throws Exception {
		int result = userRepository.updateUserRole(userId, roleId);
		if (result > 0) {
			return new Result<>(201, "Successfully Assigned Role to User");
		} else {
			return new Result<>(500, "Internal Server error!, Unable to update the Role");
		}
	}

//	------------------------------------------------- UPDATE PASSWORD SERVICE --------------------------------------------------------------------

	public Result<String> updatePassword(UserWithNewPassword userWithNewPassword) throws Exception {
//		String select_password = "SELECT u.UserName as UserName,u.Password as Password,r.Name as Name FROM User as u INNER JOIN Role as r on u.RoleId=r.RoleId WHERE UserId= :userId";
//		System.out.println(select_password);
//		List<UserWithPassword> userWithOldPassword = namedParameterJdbcTemplate.query(select_password,
//				new BeanPropertySqlParameterSource(userWithNewPassword), new UserWithPasswordRowMapper());
//		System.out.println(userWithOldPassword);
//		if (userWithOldPassword.size() > 0 && bCryptPasswordEncoder.matches(userWithNewPassword.getOldPassword(),
//				userWithOldPassword.get(0).getPassword())) {
//			userWithNewPassword.setNewPassword(bCryptPasswordEncoder.encode(userWithNewPassword.getNewPassword()));
//			int result = userRepository.updateUserPassword(userWithNewPassword);
//			System.out.println(result);
//			if (result > 0) {
//				return new Result<>(200, "password has been successfully updated");
//			} else {
//
//				return new Result<>(500, "Internal Server error!, Unable to update the password");
//			}
//		}
//		return new Result<>(400,"Old Password does not match!");
		return null;
	}

//	------------------------------------------------- UPDATE FORGOT PASSWORD SERVICE -------------------------------------------------------------

	public int generateOTP() {

		Random random = new Random();
		otp = 100000 + random.nextInt(900000);
		return otp;
	}

	public Result<User> findUserByEmailIdAndMobileNumber(User user) throws Exception {
		List<User> userList = userRepository.findUserByEmailIdAndMobileNumber(user);
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

	public Result<User> deleteUser(int userId) throws Exception {
		boolean result = emailContactRepository.deleteEmailContactByUserId(userId);
		if (result) {
			mobileContactRepository.deleteMobileContactByUserId(userId);
			rechargeRepository.deleteRechargeByUserId(userId);
			contestRepository.deleteContestByUserId(userId);
			userRepository.deleteUser(userId);
			return new Result<>(201, "User Deleted Successfully!!");
		}

		return new Result<>(404, "Given User Id does not exists");
	}

}
