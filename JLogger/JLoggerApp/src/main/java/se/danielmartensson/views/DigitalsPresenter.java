package se.danielmartensson.views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

import javax.inject.Inject;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.util.EntityUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import se.danielmartensson.tools.Dialogs;
import se.danielmartensson.tools.FileHandler;
import se.danielmartensson.views.globals.Configurations;
import se.danielmartensson.views.globals.Connections;
import se.danielmartensson.views.globals.DigitalsConfiguration;
import se.danielmartensson.views.globals.MeasurementLogs;
import se.danielmartensson.views.messages.DeviceMessage;

public class DigitalsPresenter {
	
	private static final String DIGITAL_OUTPUT_STATES = "/JLoggerStorage/DIGITAL_OUTPUT_STATES_";

	@FXML
    private View view;
	
	@FXML
    private TextField measurement0;

    @FXML
    private TextField analogNote0;

    @FXML
    private TextField measurement1;

    @FXML
    private TextField analogNote1;

    @FXML
    private TextField measurement2;

    @FXML
    private TextField analogNote2;

    @FXML
    private TextField measurement3;

    @FXML
    private TextField analogNote3;

    @FXML
    private TextField measurement4;

    @FXML
    private TextField analogNote4;

    @FXML
    private TextField measurement5;

    @FXML
    private TextField analogNote5;

	@FXML
    private Button digitalButton0;

    @FXML
    private TextField digitalNote0;

    @FXML
    private Button digitalButton1;

    @FXML
    private TextField digitalNote1;

    @FXML
    private Button digitalButton2;

    @FXML
    private TextField digitalNote2;

    @FXML
    private Button digitalButton3;

    @FXML
    private TextField digitalNote3;

    @FXML
    private Button digitalButton4;

    @FXML
    private TextField digitalNote4;

    @FXML
    private Button digitalButton5;

    @FXML
    private TextField digitalNote5;

    @FXML
    private Button digitalButton6;

    @FXML
    private TextField digitalNote6;

    @FXML
    private Button digitalButton7;

    @FXML
    private TextField digitalNote7;

    @FXML
    private Button digitalButton8;

    @FXML
    private TextField digitalNote8;

    @FXML
    private Button digitalButton9;

    @FXML
    private TextField digitalNote9;

    @FXML
    private Button digitalButton10;

    @FXML
    private TextField digitalNote10;

    @FXML
    private Button digitalButton11;

    @FXML
    private TextField digitalNote11;

    @FXML
    private Button digitalButton12;

    @FXML
    private TextField digitalNote12;

    @FXML
    private Button digitalButton13;

    @FXML
    private TextField digitalNote13;

    @FXML
    private Button digitalButton14;

    @FXML
    private TextField digitalNote14;

    @FXML
    private Button digitalButton15;

    @FXML
    private TextField digitalNote15;

    @FXML
    private Button digitalButton16;

    @FXML
    private TextField digitalNote16;

    @FXML
    private Button digitalButton17;

    @FXML
    private TextField digitalNote17;

    @FXML
    private Button digitalButton18;

    @FXML
    private TextField digitalNote18;

    @FXML
    private Button digitalButton19;

    @FXML
    private TextField digitalNote19;

    @FXML
    private Button digitalButton20;

    @FXML
    private TextField digitalNote20;

    @FXML
    private Button digitalButton21;

    @FXML
    private TextField digitalNote21;

    @FXML
    private Button digitalButton22;

    @FXML
    private TextField digitalNote22;

    @FXML
    private Button digitalButton23;

    @FXML
    private TextField digitalNote23;

    @FXML
    private Button digitalButton24;

    @FXML
    private TextField digitalNote24;

    @FXML
    private Button digitalButton25;

    @FXML
    private TextField digitalNote25;

    @FXML
    private Button digitalButton26;

    @FXML
    private TextField digitalNote26;
    
    @Inject
    private FileHandler fileHandler;
    
    @Inject
    private Dialogs dialogs;
    
    @Inject
    private DigitalsConfiguration digitalsConfiguration;
    
    @Inject 
	private DeviceMessage deviceMessage;

	@Inject
	private Connections connections;
	
	@Inject
	private Configurations configurations;
	
	@Inject
	private MeasurementLogs measurementLogs;

	private boolean ENABLE_DISABLE;
	
	private Button[] buttonList = new Button[27];
	private TextField[] noteList = new TextField[27 + 6]; // +6 because we have analog measurements too
	private TextField[] analogList = new TextField[6];
	private File file;


	@FXML
	public void initialize() {
		
		// Add them all first
		buttonList[0] = digitalButton0;
		buttonList[1] = digitalButton1;
		buttonList[2] = digitalButton2;
		buttonList[3] = digitalButton3;
		buttonList[4] = digitalButton4;
		buttonList[5] = digitalButton5;
		buttonList[6] = digitalButton6;
		buttonList[7] = digitalButton7;
		buttonList[8] = digitalButton8;
		buttonList[9] = digitalButton9;
		buttonList[10] = digitalButton10;
		buttonList[11] = digitalButton11;
		buttonList[12] = digitalButton12;
		buttonList[13] = digitalButton13;
		buttonList[14] = digitalButton14;
		buttonList[15] = digitalButton15;
		buttonList[16] = digitalButton16;
		buttonList[17] = digitalButton17;
		buttonList[18] = digitalButton18;
		buttonList[19] = digitalButton19;
		buttonList[20] = digitalButton20;
		buttonList[21] = digitalButton21;
		buttonList[22] = digitalButton22;
		buttonList[23] = digitalButton23;
		buttonList[24] = digitalButton24;
		buttonList[25] = digitalButton25;
		buttonList[26] = digitalButton26;
		
		// AnalogList
		analogList[0] = measurement0;
		analogList[1] = measurement1;
		analogList[2] = measurement2;
		analogList[3] = measurement3;
		analogList[4] = measurement4;
		analogList[5] = measurement5;
		
		
		noteList[0] = digitalNote0;
		noteList[1] = digitalNote1;
		noteList[2] = digitalNote2;
		noteList[3] = digitalNote3;
		noteList[4] = digitalNote4;
		noteList[5] = digitalNote5;
		noteList[6] = digitalNote6;
		noteList[7] = digitalNote7;
		noteList[8] = digitalNote8;
		noteList[9] = digitalNote9;
		noteList[10] = digitalNote10;
		noteList[11] = digitalNote11;
		noteList[12] = digitalNote12;
		noteList[13] = digitalNote13;
		noteList[14] = digitalNote14;
		noteList[15] = digitalNote15;
		noteList[16] = digitalNote16;
		noteList[17] = digitalNote17;
		noteList[18] = digitalNote18;
		noteList[19] = digitalNote19;
		noteList[20] = digitalNote20;
		noteList[21] = digitalNote21;
		noteList[22] = digitalNote22;
		noteList[23] = digitalNote23;
		noteList[24] = digitalNote24;
		noteList[25] = digitalNote25;
		noteList[26] = digitalNote26;
		noteList[26+1] = analogNote0;
		noteList[26+2] = analogNote1;
		noteList[26+3] = analogNote2;
		noteList[26+4] = analogNote3;
		noteList[26+5] = analogNote4;
		noteList[26+6] = analogNote5;
		
		// Initial values for the digitals
		for(int i = 0; i < buttonList.length; i++) {
			digitalsConfiguration.getButtonStates()[i] = false; 
			digitalsConfiguration.getNoteText()[i] = "";
		}
		for(int i = buttonList.length; i < noteList.length; i++) {
			digitalsConfiguration.getNoteText()[i] = ""; // This is for the analog text fields notes
		}

		// view smooth in
		view.setShowTransitionFactory(BounceInRightTransition::new);

		// Listener when sliding in
		view.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().getDrawer().open()));
				appBar.getActionItems().add(MaterialDesignIcon.CREATE.button(e -> enableEdit()));
				appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> getMeasurements()));
				appBar.getActionItems().add(MaterialDesignIcon.SAVE.button(e -> saveState()));
				appBar.setTitleText("Digital outputs");
				
				// If we have not select any device in our configuration
				if(deviceMessage.getDevicename() == null) {
					dialogs.alertDialog(AlertType.ERROR, "No device", "You have to select a device");
				}else {
					// Import the digital states in from of a JSON file
					if(fileHandler.exist(DIGITAL_OUTPUT_STATES + deviceMessage.getDevicename().replace("/", "_") + ".json") == true) {
						file = fileHandler.loadNewFile(DIGITAL_OUTPUT_STATES + deviceMessage.getDevicename().replace("/", "_") + ".json");
						try {
							// Read json
							BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
							String json = bufferedreader.readLine();						
							DigitalsConfiguration digitalsConfiguration_JSON = new Gson().fromJson(json, DigitalsConfiguration.class);
							
							// Copy over
							if(digitalsConfiguration_JSON != null) {
								for(int i = 0; i < buttonList.length; i++) {
									digitalsConfiguration.getButtonStates()[i] = digitalsConfiguration_JSON.getButtonStates()[i];
									digitalsConfiguration.getNoteText()[i] = digitalsConfiguration_JSON.getNoteText()[i];
								}
								for(int i = buttonList.length; i < noteList.length; i++) {
									digitalsConfiguration.getNoteText()[i] = digitalsConfiguration_JSON.getNoteText()[i]; // For the analog notes
								}
							}
							bufferedreader.close();
						} catch (Exception e) {
							dialogs.exception("Cannot load digital configurations", e);
						}
					}else {
						// Create a new file if not exist. Turn / to _. Only happen in unix-systems- Windows use COMx ports
						fileHandler.createNewFile(DIGITAL_OUTPUT_STATES + deviceMessage.getDevicename().replace("/", "_") + ".json", true);
					}
				}
				
				// Apply the button states - No json slected = Then they all are false
				reloadDigitalStates();
				
				// This will disable the notice text fields
				ENABLE_DISABLE = false;
				enableEdit();
			} else {
				// When we leave, save the state if we forgot to press the save button
				if(deviceMessage.getDevicename() != null) {
					saveState();
				}
				
			}
		});
		
	}

	/**
	 * To get all measurements, just call send();
	 */
	private void getMeasurements() {
		send(); // We re-send the same digitals but we receive the analogs
	}

	/**
	 * This will save the state
	 */
	private void saveState() {
		if(deviceMessage.getDevicename() == null) {
			dialogs.alertDialog(AlertType.WARNING, "Not saved", "You need to select a device first");
			return;
		}
		// Save also the text fields notes
		for(int i = 0; i < noteList.length; i++) 
			digitalsConfiguration.getNoteText()[i] = noteList[i].getText();
		try {	
			// Check if file exist
			if(fileHandler.exist(DIGITAL_OUTPUT_STATES + deviceMessage.getDevicename().replace("/", "_") + ".json") == false) {
				fileHandler.createNewFile(DIGITAL_OUTPUT_STATES + deviceMessage.getDevicename().replace("/", "_") + ".json", true);
				file = fileHandler.loadNewFile(DIGITAL_OUTPUT_STATES + deviceMessage.getDevicename().replace("/", "_") + ".json");
			}
			
			// What if file exist, but file is null
			if(file == null)
				file = fileHandler.loadNewFile(DIGITAL_OUTPUT_STATES + deviceMessage.getDevicename().replace("/", "_") + ".json");
			
			// Save now
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			Type type = new TypeToken<DigitalsConfiguration>() {}.getType();
			String json = new Gson().toJson(digitalsConfiguration,type);
			bufferedWriter.write(json);
			bufferedWriter.close();
		} catch (Exception e) {
			dialogs.exception("Cannot save the digital configurations", e);
		}
		
	}

	/**
	 * This reloads the states and set the color
	 */
	private void reloadDigitalStates() {
		for(int i = 0; i < buttonList.length; i++) {
			if(digitalsConfiguration.getButtonStates()[i] == false) {
				buttonList[i].setStyle("-fx-background-color: #ff0000"); // Red
				deviceMessage.getDigitalValues()[i] = 0;
			}else {
				buttonList[i].setStyle("-fx-background-color: #53ff1a");  // Green
				deviceMessage.getDigitalValues()[i] = 1;
			}
			noteList[i].setText(digitalsConfiguration.getNoteText()[i]); // For buttons
		}
		// We end at buttonList.length-1 and we start at buttonList.length
		for(int i = buttonList.length; i < noteList.length; i++)
			noteList[i].setText(digitalsConfiguration.getNoteText()[i]); // For analogs
	}

	/**
	 * This method enable or disable the text fields
	 */
	private void enableEdit() {
		if(ENABLE_DISABLE == true)
			ENABLE_DISABLE = false;
		else
			ENABLE_DISABLE = true;
		
		digitalNote0.setDisable(ENABLE_DISABLE);
		digitalNote1.setDisable(ENABLE_DISABLE);
		digitalNote2.setDisable(ENABLE_DISABLE);
		digitalNote3.setDisable(ENABLE_DISABLE);
		digitalNote4.setDisable(ENABLE_DISABLE);
		digitalNote5.setDisable(ENABLE_DISABLE);
		digitalNote6.setDisable(ENABLE_DISABLE);
		digitalNote7.setDisable(ENABLE_DISABLE);
		digitalNote8.setDisable(ENABLE_DISABLE);
		digitalNote9.setDisable(ENABLE_DISABLE);
		digitalNote10.setDisable(ENABLE_DISABLE);
		digitalNote11.setDisable(ENABLE_DISABLE);
		digitalNote12.setDisable(ENABLE_DISABLE);
		digitalNote13.setDisable(ENABLE_DISABLE);
		digitalNote14.setDisable(ENABLE_DISABLE);
		digitalNote15.setDisable(ENABLE_DISABLE);
		digitalNote16.setDisable(ENABLE_DISABLE);
		digitalNote17.setDisable(ENABLE_DISABLE);
		digitalNote18.setDisable(ENABLE_DISABLE);
		digitalNote19.setDisable(ENABLE_DISABLE);
		digitalNote20.setDisable(ENABLE_DISABLE);
		digitalNote21.setDisable(ENABLE_DISABLE);
		digitalNote22.setDisable(ENABLE_DISABLE);
		digitalNote23.setDisable(ENABLE_DISABLE);
		digitalNote24.setDisable(ENABLE_DISABLE);
		digitalNote25.setDisable(ENABLE_DISABLE);
		digitalNote26.setDisable(ENABLE_DISABLE);
		analogNote0.setDisable(ENABLE_DISABLE);
		analogNote1.setDisable(ENABLE_DISABLE);
		analogNote2.setDisable(ENABLE_DISABLE);
		analogNote3.setDisable(ENABLE_DISABLE);
		analogNote4.setDisable(ENABLE_DISABLE);
		analogNote5.setDisable(ENABLE_DISABLE);

	}
	
	/**
	 * This will set action onto the button. 
	 * Let the user know if we have press the button or not
	 * @param index
	 */
	private void setAction(int index) {
		if(measurementLogs.getLogName() == null) {
			dialogs.alertDialog(AlertType.WARNING, "No log", "You need to select a log file");
			return;
		}
		
		if(deviceMessage.getDevicename() == null) {
			dialogs.alertDialog(AlertType.WARNING, "No device", "You need to select device");
			return;
		}
		
		// Change the button state
		changeButtonState(index);
			
		// Send the updated digital + all the other non-updated
		boolean success = send(); 
		if(success == false) {
			changeButtonState(index);// Call it again to reverse
		}
	}

	/**
	 * This method will change the state and also change its color
	 * @param index index of the button
	 */
	private void changeButtonState(int index) {
		if(digitalsConfiguration.getButtonStates()[index] == true) {
			digitalsConfiguration.getButtonStates()[index] = false;
			deviceMessage.getDigitalValues()[index] = 0;
			buttonList[index].setStyle("-fx-background-color: #ff0000"); // Red
		}else {
			digitalsConfiguration.getButtonStates()[index] = true;
			deviceMessage.getDigitalValues()[index] = 1;
			buttonList[index].setStyle("-fx-background-color: #53ff1a");  // Green
		}
	}

	/**
	 * This method will send digitals and also get the ADC measurements
	 * @return If we send or not
	 */
	private boolean send() {
		if(measurementLogs.getLogName() == null) {
			dialogs.alertDialog(AlertType.WARNING, "No log", "You need to select a log file");
			return false; // fail
		}
		
		if(deviceMessage.getDevicename() == null) {
			dialogs.alertDialog(AlertType.WARNING, "No device", "You need to select device");
			return false; // Fail
		}
		
		// Apply ONLY digitalValues to JLoggerDevice - That's because you won't want to change the PWM if you turn on a e.g relay
		deviceMessage.setSendState((byte) 1); 
		
		// We send to the device and the device is going to send back ADC values
		HttpPost httppost = new HttpPost("http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/user/calldevice");
		try {
			httppost.setEntity(new StringEntity(new Gson().toJson(deviceMessage)));
			httppost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = connections.getHttpclient().execute(httppost); // Blocking
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				dialogs.alertDialog(AlertType.WARNING, "Connection", "Action did not send to server. Try to re-login: " + response.getStatusLine().getStatusCode());
				return false; // Fail
			}
			// Get the ADC values and set them to the measurement fields
			String json = EntityUtils.toString(response.getEntity());
			Type type = new TypeToken<DeviceMessage>() {}.getType();
			DeviceMessage deviceMessageJLoggerDevice = new Gson().fromJson(json, type);
			short[] adcValues = deviceMessageJLoggerDevice.getAdcValues();
			for(int i = 0; i < adcValues.length; i++) {
				try {
					double y = Double.parseDouble(configurations.getScalar()[i]) * adcValues[i] + Double.parseDouble(configurations.getBias()[i]);	
					analogList[i].setText(String.valueOf(y));
				}catch(NumberFormatException e) {
					analogList[i].setText("Calibrated?");
				}
			}
			// Done!
			return true; // Success!
			
		} catch (Exception e) {
			dialogs.exception("Could not get the response", e);
		}
		return false; // Fail
		
	}

	@FXML
    void digitalAction0(ActionEvent event) {
		setAction(0);
    }

    @FXML
    void digitalAction1(ActionEvent event) {
    	setAction(1);
    }
    
    @FXML
    void digitalAction2(ActionEvent event) {
    	setAction(2);
    }
    
    @FXML
    void digitalAction3(ActionEvent event) {
    	setAction(3);
    }

    @FXML
    void digitalAction4(ActionEvent event) {
    	setAction(4);
    }

    @FXML
    void digitalAction5(ActionEvent event) {
    	setAction(5);
    }

    @FXML
    void digitalAction6(ActionEvent event) {
    	setAction(6);
    }

    @FXML
    void digitalAction7(ActionEvent event) {
    	setAction(7);
    }

    @FXML
    void digitalAction8(ActionEvent event) {
    	setAction(8);
    }

    @FXML
    void digitalAction9(ActionEvent event) {
    	setAction(9);
    }

    @FXML
    void digitalAction10(ActionEvent event) {
    	setAction(10);
    }

    @FXML
    void digitalAction11(ActionEvent event) {
    	setAction(11);
    }

    @FXML
    void digitalAction12(ActionEvent event) {
    	setAction(12);
    }

    @FXML
    void digitalAction13(ActionEvent event) {
    	setAction(13);
    }

    @FXML
    void digitalAction14(ActionEvent event) {
    	setAction(14);
    }

    @FXML
    void digitalAction15(ActionEvent event) {
    	setAction(15);
    }

    @FXML
    void digitalAction16(ActionEvent event) {
    	setAction(16);
    }

    @FXML
    void digitalAction17(ActionEvent event) {
    	setAction(17);
    }

    @FXML
    void digitalAction18(ActionEvent event) {
    	setAction(18);
    }

    @FXML
    void digitalAction19(ActionEvent event) {
    	setAction(19);
    }

    @FXML
    void digitalAction20(ActionEvent event) {
    	setAction(20);
    }

    @FXML
    void digitalAction21(ActionEvent event) {
    	setAction(21);
    }

    @FXML
    void digitalAction22(ActionEvent event) {
    	setAction(22);
    }

    @FXML
    void digitalAction23(ActionEvent event) {
    	setAction(23);
    }

    @FXML
    void digitalAction24(ActionEvent event) {
    	setAction(24);
    }

    @FXML
    void digitalAction25(ActionEvent event) {
    	setAction(25);
    }

    @FXML
    void digitalAction26(ActionEvent event) {
    	setAction(26);
    }
}
