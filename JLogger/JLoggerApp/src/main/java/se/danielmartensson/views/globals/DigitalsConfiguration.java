package se.danielmartensson.views.globals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DigitalsConfiguration {
	// They all need to have the same length
	private boolean[] buttonStates = new boolean[27];
	private String[] noteText = new String[27+6]; // +6 because we have 27 text fields for digitals + 6 fields for analogs

}
