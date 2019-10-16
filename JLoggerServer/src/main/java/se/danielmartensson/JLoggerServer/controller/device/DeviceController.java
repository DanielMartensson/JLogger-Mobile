package se.danielmartensson.JLoggerServer.controller.device;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fazecast.jSerialComm.SerialPort;

@RestController
@RequestMapping("/user")
public class DeviceController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Map<String, SerialPort> allDevices;
	
	@PostMapping("/calldevice")
	public DeviceMessage callDevice(@RequestBody DeviceMessage deviceMessage) {
		logger.info("Read from client");
		
		// Get message
		//System.out.println("Device name is: " + deviceMessage.getDevicename());
		SerialPort serialPort = allDevices.get(deviceMessage.getDevicename());
		if (serialPort == null)
			return deviceMessage;

		// Create TXData memory with twice of length as values array and insert its values
		byte[] TXData = new byte[deviceMessage.getSliderValues().length * 2];
		for (int i = 0; i < deviceMessage.getSliderValues().length; i++) {
			TXData[i + i] = (byte) (deviceMessage.getSliderValues()[i] >> 8);
			TXData[i + i + 1] = (byte) (deviceMessage.getSliderValues()[i]);			
			
			//System.out.println("TXData[" + i + " + " + i + "] = " + TXData[i+i]);
			//System.out.println("TXData[" + i + " + " + i + " + 1] = " + TXData[i+i+1]);
			//System.out.println("Slider" + i + " value: " + deviceMessage.getSliderValues()[i]);
		}

		// Write to JLoggerDevice
		serialPort.writeBytes(TXData, TXData.length);

		// Create RXData memory with twice of length as values array and read the ADC values from JLoggerDevice
		byte[] RXData = new byte[deviceMessage.getAdcValues().length * 2];
		int receivedBytes = serialPort.bytesAvailable();
		while (receivedBytes < RXData.length)
			receivedBytes = serialPort.bytesAvailable(); // We need to have values.length * 2 bytes to pass
		serialPort.readBytes(RXData, RXData.length);

		// Create them to short
		for (int i = 0; i < RXData.length / 2; i++) {
			deviceMessage.getAdcValues()[i] = (short)(((RXData[i + i] & 0xFF) << 8) | (RXData[i + i + 1]  & 0xFF));
			//System.out.println("Adc" + i + ": " + deviceMessage.getAdcValues()[i]);
		}

		// Write back message to the client
		return deviceMessage;
	}
	
}
