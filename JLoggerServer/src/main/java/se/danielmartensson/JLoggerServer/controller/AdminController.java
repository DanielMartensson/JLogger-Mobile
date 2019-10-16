package se.danielmartensson.JLoggerServer.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se.danielmartensson.JLoggerServer.controller.messages.SimpleMessageStatus;
import se.danielmartensson.JLoggerServer.model.Online;
import se.danielmartensson.JLoggerServer.model.Role;
import se.danielmartensson.JLoggerServer.model.User;
import se.danielmartensson.JLoggerServer.repository.OnlineRepository;
import se.danielmartensson.JLoggerServer.repository.RoleRepository;
import se.danielmartensson.JLoggerServer.repository.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OnlineRepository onlineRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Add a new user by sending the object user
	 * 
	 * @param user Object of the user
	 * @return String that say if the user exist or do not exist
	 */
	@PostMapping("/adduser")
	public SimpleMessageStatus addUser(@RequestBody User user) {
		// Search first for the user if it's null = not exist
		if (userRepository.findByUsername(user.getUsername()) == null) {
			// For user
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setUser_id(userRepository.count());

			// For online
			Online online = new Online();
			online.setOnline_id(user.getUser_id());
			online.setOnline(false);
			online.setUsername(user.getUsername());
			online.setDevice("None");

			// For role
			for (Role role : user.getRoles()) {
				role.setRole_id(user.getUser_id()); // We need to set role ID too
				online.setRole(role.getRole());
			}

			// Save
			userRepository.save(user);
			onlineRepository.save(online);
			return new SimpleMessageStatus("User: " + user.getUsername() + " added", 200);
		} else {
			return new SimpleMessageStatus("User: " + user.getUsername() + " already exist", 403);
		}

	}

	/**
	 * Delete user by sending the user name of the user
	 * 
	 * @param username Name if the user
	 * @return String that says user is deleted or did not exist
	 */
	@PostMapping("/deleteuser")
	public SimpleMessageStatus deleteUser(@RequestParam("username") String username) {
		// Check if user exist
		User user = userRepository.findByUsername(username);
		if (user != null) {
			// Get roles and delete them
			Set<Role> roles = user.getRoles();
			for (Role role : roles) {
				roleRepository.delete(role);
			}
			// Delete the user and the online
			onlineRepository.deleteById(user.getUser_id());
			userRepository.deleteById(user.getUser_id());
			return new SimpleMessageStatus("User: " + username + " updated", 200);
		} else {
			return new SimpleMessageStatus("User: " + username + " does not exist", 404);
		}

	}

	/**
	 * Update user by sending the user object and a boolean variable if password
	 * should be updated or not
	 * 
	 * @param user           Object user
	 * @param updatepassword Boolean if we want to update the user or not
	 * @return String that says user is updated
	 */
	@PostMapping("/updateuser")
	public SimpleMessageStatus uppdateUser(@RequestBody User user,
			@RequestParam("updatepassword") boolean updatepassword) {
		// Check if user exist
		if (userRepository.findByUsername(user.getUsername()) != null) {
			// Update the password if we say that it should be updated
			if (updatepassword == true)
				user.setPassword(passwordEncoder.encode(user.getPassword()));

			// This will update the role table too
			userRepository.save(user);

			// Update the online table too
			Online online = onlineRepository.findById(user.getUser_id()).get();
			for (Role role : user.getRoles())
				online.setRole(role.getRole());
			online.setUsername(user.getUsername());
			onlineRepository.save(online);
			return new SimpleMessageStatus("User: " + user.getUsername() + " is updated", 200);

		} else {
			return new SimpleMessageStatus("User: " + user.getUsername() + " does not exist", 404);
		}
	}

	/**
	 * Sends back the JSON string of user object.
	 * 
	 * @param username Name if the user
	 * @return JSON String object
	 */
	@GetMapping("/getuser")
	public SimpleMessageStatus getUser(@RequestParam("username") String username) {
		// Send the Online entity in form of a JSON file
		User user = userRepository.findByUsername(username);
		SimpleMessageStatus simpleMessageStatus = new SimpleMessageStatus();
		if (user != null) {
			simpleMessageStatus.setJsonMessage(user);
			simpleMessageStatus.setStatuscode(200);
		} else {
			simpleMessageStatus.setMessage("User: " + username + " not found");
			simpleMessageStatus.setStatuscode(404);
		}
		return simpleMessageStatus;
	}
}
