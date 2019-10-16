package se.danielmartensson.views.containers;

import javafx.beans.property.SimpleStringProperty;
import lombok.Setter;

/**
 * This is for the table view inside the LogPresenter class
 * 
 * @author Daniel Mårtensson
 *
 */
public class LogTableContainer {

	private @Setter SimpleStringProperty log;
	private @Setter SimpleStringProperty created;

	public LogTableContainer(String log, String created) {
		this.log = new SimpleStringProperty(log);
		this.created = new SimpleStringProperty(created);
	}

	public String getLog() {
		return log.get();
	}

	public String getCreated() {
		return created.get();
	}

}
