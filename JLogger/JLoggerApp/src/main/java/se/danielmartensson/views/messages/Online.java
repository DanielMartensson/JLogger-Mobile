package se.danielmartensson.views.messages;

import lombok.Getter;
import lombok.Setter;

public class Online {
	private @Getter @Setter int online_id;

	private @Getter @Setter String username;

	private @Getter @Setter String role;

	private @Getter @Setter boolean online;

	private @Getter @Setter String device;
}
