package se.danielmartensson.views.savings;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LastLogin {
	private String serverPort;
	private String serverAddress;
	private String userName;
	// Password not stored for a reason ;)
}
