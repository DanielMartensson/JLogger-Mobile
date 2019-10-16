package se.danielmartensson.views.messages;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private int user_id;

	private String username;

	private String password;

	private String email;

	private Set<Role> roles;

}