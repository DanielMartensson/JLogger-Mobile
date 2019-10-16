package se.danielmartensson.views.globals;



import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import lombok.Getter;
import lombok.Setter;

public class Connections {
	private @Getter @Setter boolean connected;
	private @Getter @Setter CloseableHttpClient httpclient;
	private @Getter @Setter String serverPort;
	private @Getter @Setter String serverAddress;
	private @Getter @Setter String userName;
}
