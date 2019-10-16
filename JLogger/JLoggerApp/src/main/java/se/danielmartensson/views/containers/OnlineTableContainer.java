package se.danielmartensson.views.containers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Setter;

/**
 * This is for the table view inside the ConnectServerPresenter class
 * 
 * @author Daniel Mårtensson
 *
 */
public class OnlineTableContainer {
	private @Setter SimpleStringProperty name;
	private @Setter SimpleBooleanProperty online;
	private @Setter SimpleStringProperty role;
	private @Setter SimpleStringProperty device;

	public OnlineTableContainer(String name, Boolean online, String role, String device) {
		this.name = new SimpleStringProperty(name);
		this.online = new SimpleBooleanProperty(online);
		this.role = new SimpleStringProperty(role);
		this.device = new SimpleStringProperty(device);
	}

	public String getName() {
		return name.get();
	}

	public boolean getOnline() {
		return online.get();
	}

	public String getRole() {
		return role.get();
	}

	public String getDevice() {
		return device.get();
	}

}
