package se.danielmartensson.JLoggerServer.controller.device;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceMessage {

	private String devicename;
	private short[] sliderValues = new short[6]; // Our slider values to JLoggerDevice
	private short[] adcValues = new short[6]; // From JLoggerDevice
	private short[] digitalValues = new short[27]; // Our button states to JLoggerDevice
	private byte sendState; // 0 = Send ONLY sliderValues. 1 = Send ONLY digitalValues
}
