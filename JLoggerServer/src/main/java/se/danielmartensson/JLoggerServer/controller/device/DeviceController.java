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
		
		// Get message
		//System.out.println("Device name is: " + deviceMessage.getDevicename());
		SerialPort serialPort = allDevices.get(deviceMessage.getDevicename());
		if (serialPort == null) {
			logger.info("ERROR: Device: " + deviceMessage.getDevicename() + " is null.");
			return deviceMessage;
		}
		
		// Create TXData that store sliderValues(short) + digitalValues(byte) + sendState(byte)
		byte[] TXData = new byte[deviceMessage.getSliderValues().length * 2 + deviceMessage.getDigitalValues().length + 1]; // Last +1 is for the sendState
		
		// Include the slider values now. They need to be converted to byte from short 
		for (int i = 0; i < deviceMessage.getSliderValues().length; i++) {
			TXData[i + i] = (byte) (deviceMessage.getSliderValues()[i] >> 8);
			TXData[i + i + 1] = (byte) (deviceMessage.getSliderValues()[i]);			
		}
		
		// Include the digital values now. We start at index 12, because we sliderValues are between 0 to 11
		for(int i = 0; i < deviceMessage.getDigitalValues().length; i++) {
			TXData[i + deviceMessage.getSliderValues().length * 2] = (byte) deviceMessage.getDigitalValues()[i];
		}
		
		// Include the send state. It should always be the last element
		TXData[TXData.length - 1] = deviceMessage.getSendState(); // 0/1 = Apply ONLY sliderValues/digitalValues inside JLoggerDevice

		// Write to JLoggerDevice
		serialPort.writeBytes(TXData, TXData.length);

		// Create RXData that can hold 12-bits ADC data
		byte[] RXData = new byte[deviceMessage.getAdcValues().length * 2];
		int receivedBytes = serialPort.bytesAvailable();
		while (receivedBytes < RXData.length)
			receivedBytes = serialPort.bytesAvailable(); // We need to have values.length * 2 bytes to pass
		serialPort.readBytes(RXData, RXData.length);

		// Create them to short
		for (int i = 0; i < RXData.length / 2; i++) {
			deviceMessage.getAdcValues()[i] = (short)(((RXData[i + i] & 0xFF) << 8) | (RXData[i + i + 1]  & 0xFF));
			//System.out.println("Adc:" + i + " " + deviceMessage.getAdcValues()[i]);
		}

		// Write back message to the client
		return deviceMessage;
	}
	
}
