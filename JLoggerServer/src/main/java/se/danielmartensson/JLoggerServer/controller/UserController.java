package se.danielmartensson.JLoggerServer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fazecast.jSerialComm.SerialPort;
import se.danielmartensson.JLoggerServer.controller.messages.SimpleMessageStatus;
import se.danielmartensson.JLoggerServer.model.Online;
import se.danielmartensson.JLoggerServer.model.User;
import se.danielmartensson.JLoggerServer.repository.OnlineRepository;
import se.danielmartensson.JLoggerServer.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final int BAUDRATE = 115200;

	@Autowired
	private OnlineRepository onlineRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Map<String, SerialPort> allDevices; // This will be filled when some one calls /getdevices

	/**
	 * Get all the devices
	 * 
	 * @return
	 */
	@GetMapping("/getdevices")
	public SimpleMessageStatus getDevices() {
		// Send all the connected devices that are not used
		SerialPort[] ports = SerialPort.getCommPorts();
		SimpleMessageStatus simpleMessageStatus = new SimpleMessageStatus();
		if (ports.length > 0) {
			String[] portNames = new String[ports.length];
			for (int i = 0; i < ports.length; i++) {
				String portName = ports[i].getSystemPortName();
				portNames[i] = portName;
				allDevices.put(portName, ports[i]);
			}
			simpleMessageStatus.setJsonMessage(portNames);
			simpleMessageStatus.setStatuscode(200);
		} else {
			simpleMessageStatus.setJsonMessage("No devices available");
			simpleMessageStatus.setStatuscode(404);
		}
		return simpleMessageStatus;
	}

	/**
	 * Ask for a connection to the USB device - Call it again and you will be
	 * disconnected from it
	 * 
	 * @param devicename The name of the device
	 * @param username   The username that going to use the device
	 * @return
	 */
	@PostMapping("/askforconnection")
	public SimpleMessageStatus askForConnection(@RequestParam("devicename") String devicename,
			@RequestParam("username") String username) {
		// Check if the device is available
		Online online = onlineRepository.findByDevice(devicename);
		if (online == null && devicename.equals("None") == false) {
			// Nobody using that device in the online table - username can use it
			online = onlineRepository.findByUsername(username);
			online.setOnline(true);
			online.setDevice(devicename);
			onlineRepository.save(online);
			// Use the device now
			SerialPort serialPort = allDevices.get(devicename);
			if(serialPort == null) {
				return new SimpleMessageStatus("Found no device. Try to scan again." + devicename, 404);
			}
			serialPort.setBaudRate(BAUDRATE);
			serialPort.openPort();
			return new SimpleMessageStatus("Now user: " + username + " using the device: " + devicename, 200);
		} else if (online != null && devicename.equals("None") == false) {
			// Who is using the device?
			if (online.getUsername().equals(username) == true) {
				// You are! Disconnect from the device
				SimpleMessageStatus simpleMessageStatus = new SimpleMessageStatus(
						"User: " + username + " has stopped to use the device: " + online.getDevice(), 204);
				online.setOnline(false);
				online.setDevice("None");
				onlineRepository.save(online);
				// Disconnect from the device now
				SerialPort serialPort = allDevices.get(devicename);
				serialPort.closePort();
				return simpleMessageStatus;
			} else {
				// The device is selected by another user
				return new SimpleMessageStatus("Device: " + devicename + " is used by the user: " + online.getUsername(), 403);
			}
		} else {
			// This happens if devicename variable is equal to "None" - Disconnect and select None in database
			online = onlineRepository.findByUsername(username);
			if (online != null) {
				SerialPort serialPort = allDevices.get(online.getDevice());
				System.out.println("Device is = " + online.getDevice() + " and serialPort = " + (serialPort == null));
				if(serialPort != null) { // Might be "None" as serial port = null
					serialPort.closePort(); // Close the current port if we selected None
					online.setDevice("None");
					onlineRepository.save(online);
					return new SimpleMessageStatus("User: " + username + " has stopped to use the device: " + online.getDevice(), 204);
				}
			}
			return new SimpleMessageStatus("Device: " + devicename + " is not not a device", 404);
		}
	}

	/**
	 * Search for users
	 * 
	 * @return
	 */
	@GetMapping("/searchusers")
	public SimpleMessageStatus searchUsers() {
		// Send the Online entity in form of a JSON file
		SimpleMessageStatus simpleMessageStatus = new SimpleMessageStatus();
		if (onlineRepository.count() > 0) {
			simpleMessageStatus.setStatuscode(200);
			simpleMessageStatus.setJsonMessage(onlineRepository.findAll());
			return simpleMessageStatus;
		} else {
			simpleMessageStatus.setStatuscode(404); // No data
			simpleMessageStatus.setJsonMessage(onlineRepository.findAll());
			return simpleMessageStatus;
		}
	}

	/**
	 * This will connect the user
	 * 
	 * @param username
	 * @return
	 */
	@GetMapping("/connect")
	public SimpleMessageStatus connect(@RequestParam("username") String username) {
		// Check if user exist
		User user = userRepository.findByUsername(username);
		if (user == null) {
			return new SimpleMessageStatus("User: " + username + " does not exist", 404);
		} else {
			// Set some values
			Online online = onlineRepository.findByUsername(username);
			online.setOnline_id(user.getUser_id());
			online.setOnline(false);
			// Do not set device to "none" if we just quit rapidly and then login again
			onlineRepository.save(online);
			return new SimpleMessageStatus("User: " + username + " connected", 200);
		}
	}

	/**
	 * This will disconnect the user
	 * 
	 * @param username Name of the user
	 * @return
	 */
	@PostMapping("/disconnect")
	public SimpleMessageStatus disconnect(@RequestParam("username") String username) {
		// Check if user exist
		User user = userRepository.findByUsername(username);
		if (user == null) {
			return new SimpleMessageStatus("User: " + username + " does not exist", 404);
		} else {
			Online online = onlineRepository.findByUsername(username);
			online.setOnline(false);
			online.setDevice("None");
			onlineRepository.save(online);
			return new SimpleMessageStatus("User: " + username + " disconnected", 200);
		}
	}

}
