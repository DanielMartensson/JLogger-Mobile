package se.danielmartensson.views.globals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Configurations {
	private String[] legendText = new String[6];
	private String[] scalar = new String[6];
	private String[] bias = new String[6];
	private String sampleTime = "";
	private String showMaxData = "";
}
