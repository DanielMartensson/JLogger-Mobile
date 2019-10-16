package se.danielmartensson.JLoggerServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;

import se.danielmartensson.JLoggerServer.model.Online;
import se.danielmartensson.JLoggerServer.model.Role;
import se.danielmartensson.JLoggerServer.model.User;
import se.danielmartensson.JLoggerServer.repository.OnlineRepository;
import se.danielmartensson.JLoggerServer.repository.UserRepository;

@Component
public class InitialStartUp implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OnlineRepository onlineRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// Set the default standard admin and password as admin, if not exist
		User user = userRepository.findByUsername("admin");
		if (user == null) {
			user = new User();
			user.setUser_id(0);
			user.setUsername("admin");
			user.setPassword(passwordEncoder().encode("admin"));
			Role role = new Role();
			role.setRole("ADMIN");
			role.setRole_id(0);
			Set<Role> roles = new HashSet<Role>();
			roles.add(role);
			user.setRoles(roles);
			userRepository.save(user);
			Online online = new Online();
			online.setOnline_id(user.getUser_id());
			online.setOnline(false);
			online.setUsername(user.getUsername());
			online.setRole(role.getRole());
			online.setDevice("None");
			onlineRepository.save(online);
			logger.info("No user named 'admin' in entity 'user'. Creating user named 'admin' with password 'admin' with role 'ADMIN'. Please change the password later.");
		}

		// This need to be called so we create our bean for the devices
		storeAllDevices();
	}

	/**
	 * This is a way for me to use the SerialPort in the Netty package
	 * 
	 * @return
	 */
	@Bean
	private Map<String, SerialPort> storeAllDevices() {
		return new HashMap<String, SerialPort>();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}