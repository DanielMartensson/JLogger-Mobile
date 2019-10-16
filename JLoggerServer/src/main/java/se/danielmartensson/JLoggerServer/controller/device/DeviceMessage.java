package se.danielmartensson.JLoggerServer.controller.device;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String devicename;
	private short[] sliderValues = new short[6];
	private short[] adcValues = new short[6];
}
