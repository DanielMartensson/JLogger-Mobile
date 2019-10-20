package se.danielmartensson.views.globals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Configurations {
	// Size 6 because we have 6 ADC's to read and total 6 sliders to move
	private String[] legendText = new String[6];
	private String[] scalar = new String[6];
	private String[] bias = new String[6];
	private String sampleTime = "";
	private String showMaxData = "";
}
