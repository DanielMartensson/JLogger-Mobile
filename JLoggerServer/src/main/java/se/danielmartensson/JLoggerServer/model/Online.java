package se.danielmartensson.JLoggerServer.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Online {
	@Id
	private long online_id;

	private String username;

	private String role;

	private boolean online;

	private String device;

}
