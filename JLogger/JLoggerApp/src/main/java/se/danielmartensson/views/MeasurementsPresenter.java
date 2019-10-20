package se.danielmartensson.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import javax.inject.Inject;


import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.DropdownButton;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import se.danielmartensson.tools.Dialogs;
import se.danielmartensson.tools.FileHandler;
import se.danielmartensson.views.globals.Configurations;
import se.danielmartensson.views.globals.Connections;
import se.danielmartensson.views.globals.MeasurementLogs;
import se.danielmartensson.views.messages.DeviceMessage;
import se.danielmartensson.views.messages.SimpleMessageStatus;

public class MeasurementsPresenter {

	private static final String[] MAX_DATA = { "10", "20", "30", "50", "60", "70", "80", "90", "100", "120", "140", "160", "180", "200" };

	private static final String[] SAMPLE_TIME = { "10", "50", "100", "200", "250", "300", "350", "400", "450", "500", "600", "700", "800", "900", "1000" };

	@FXML
	private View measurements;

	@FXML
	private TextField legendText0;

	@FXML
	private TextField legendText1;

	@FXML
	private TextField legendText2;

	@FXML
	private TextField legendText3;

	@FXML
	private TextField legendText4;

	@FXML
	private TextField legendText5;

	@FXML
	private TextField scalar0;

	@FXML
	private TextField bias0;

	@FXML
	private TextField scalar1;

	@FXML
	private TextField bias1;

	@FXML
	private TextField scalar2;

	@FXML
	private TextField bias2;

	@FXML
	private TextField scalar3;

	@FXML
	private TextField bias3;

	@FXML
	private TextField scalar4;

	@FXML
	private TextField bias4;

	@FXML
	private TextField scalar5;

	@FXML
	private TextField bias5;

	@FXML
	private DropdownButton sampleTime;

	@FXML
	private DropdownButton showMaxData;

	@FXML
	private DropdownButton selectedDevice; // Shall not be saved in the configuration fields

	@FXML
    private Button connectionButton;
	
	@Inject
	private Configurations configurations;

	@Inject
	private FileHandler fileHandler;

	@Inject
	private Dialogs dialogs;

	@Inject
	private MeasurementLogs measurementLogs;
	
	@Inject
	private Connections connections;
	
	@Inject
	private DeviceMessage deviceMessage;

	@FXML
	public void initialize() {
		/*
		 * Slide smooth in
		 */
		measurements.setShowTransitionFactory(BounceInRightTransition::new);

		/*
		 * Listener when sliding in
		 */
		measurements.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().getDrawer().open()));
				appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> searchDevices()));
				appBar.setTitleText("Measurements");

				// Update fields
				clearFields();
				updateFields();
				
			} else {
				// Save fields
				saveFields();
				clearFields();
			}

		});

		// Set some default values
		for (int i = 0; i < MAX_DATA.length; i++)
			showMaxData.getItems().add(new MenuItem(MAX_DATA[i]));

		for (int i = 0; i < SAMPLE_TIME.length; i++)
			sampleTime.getItems().add(new MenuItem(SAMPLE_TIME[i]));
		
		selectedDevice.getItems().add(new MenuItem("None"));

		// Set listener
		setListenerTextField(scalar0);
		setListenerTextField(scalar1);
		setListenerTextField(scalar2);
		setListenerTextField(scalar3);
		setListenerTextField(scalar4);
		setListenerTextField(scalar5);
		setListenerTextField(bias0);
		setListenerTextField(bias1);
		setListenerTextField(bias2);
		setListenerTextField(bias3);
		setListenerTextField(bias4);
		setListenerTextField(bias5);
	}

	/**
	 * Search for connected devices
	 */
	private void searchDevices() {		
		try {
			CloseableHttpClient httpclient = connections.getHttpclient();
			if (httpclient == null)
				return;
			CloseableHttpResponse response = ConnectServerPresenter.getResponse(httpclient, "http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/user/getdevices", "GET");
			if(response.getStatusLine().getStatusCode() == 200) {
				// In this case, there is a JSON message of a String[] object included inside this simpleMessageStatus object
				SimpleMessageStatus simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
				if(simpleMessageStatus.getStatuscode() == 200) {
					Type type = new TypeToken<String[]>() {}.getType();
					String[] portNames = new Gson().fromJson(simpleMessageStatus.getMessage(), type);
					selectedDevice.getItems().clear();
					selectedDevice.getItems().add(new MenuItem("None"));
					for(String portName : portNames) 
						selectedDevice.getItems().add(new MenuItem(portName));
					dialogs.alertDialog(AlertType.INFORMATION, "Devices", "All devices are listed now");
				}else {
					// In this case it's 404
					dialogs.alertDialog(AlertType.WARNING, "Devices", simpleMessageStatus.getMessage()); // No JSON in this message due to code 404
				}
			}else {
				dialogs.alertDialog(AlertType.ERROR, "Response", "Could not get the response from server");
			}
			
		} catch (IOException | NullPointerException e) {
			dialogs.alertDialog(AlertType.ERROR, "Not connected", "You have not connected the server");
		}
	}

	/**
	 * This function add a listener to a textfield so we can write integers,
	 * decimals or integers with a dot at the end
	 * 
	 * @param textField
	 */
	private void setListenerTextField(TextField textField) {
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(() -> {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Nothing
				}
				if (newValue.matches("^\\d*\\.\\d+|\\d+\\.\\d*$") == true) {
					if(newValue.endsWith(".") == true)
						textField.setText(newValue + "0");
					else
						textField.setText(newValue);
				} else {
					if (newValue.matches("^\\d+$") == true) {
						textField.setText(newValue);
					} else {
						if (newValue.equals("") == true) {
							textField.setText(newValue);
						} else {
							textField.setText(oldValue);
						}
					}
				}
			});
		});
	}

	/*
	 * Serialization
	 */
	private void saveFields() {
		// Check
		if(measurementLogs.getLogName() == null)
			return;
		
		// Insert
		configurations.getBias()[0] = bias0.getText();
		configurations.getBias()[1] = bias1.getText();
		configurations.getBias()[2] = bias2.getText();
		configurations.getBias()[3] = bias3.getText();
		configurations.getBias()[4] = bias4.getText();
		configurations.getBias()[5] = bias5.getText();

		configurations.getScalar()[0] = scalar0.getText();
		configurations.getScalar()[1] = scalar1.getText();
		configurations.getScalar()[2] = scalar2.getText();
		configurations.getScalar()[3] = scalar3.getText();
		configurations.getScalar()[4] = scalar4.getText();
		configurations.getScalar()[5] = scalar5.getText();

		configurations.getLegendText()[0] = legendText0.getText();
		configurations.getLegendText()[1] = legendText1.getText();
		configurations.getLegendText()[2] = legendText2.getText();
		configurations.getLegendText()[3] = legendText3.getText();
		configurations.getLegendText()[4] = legendText4.getText();
		configurations.getLegendText()[5] = legendText5.getText();
		
		configurations.setSampleTime(sampleTime.getSelectedItem().getText());
		configurations.setShowMaxData(showMaxData.getSelectedItem().getText());

		// Serialization
		Type type = new TypeToken<Configurations>() {}.getType();
		String json = new Gson().toJson(configurations, type);
		fileHandler.writeTextTo(LogsPresenter.LOG_FOLDER_PATH + measurementLogs.getLogName() + "/" + measurementLogs.getLogName() + ".config", json);
	}

	/**
	 * Deserialization
	 */
	private void updateFields() {
		try {
			// Deserialization
			File jsonFile = fileHandler.loadNewFile(LogsPresenter.LOG_FOLDER_PATH + measurementLogs.getLogName() + "/" + measurementLogs.getLogName() + ".config");
			FileReader fileReader = new FileReader(jsonFile);
			JsonReader jsonReader = new JsonReader(fileReader);
			Type type = new TypeToken<Configurations>() {}.getType();
			Configurations configurationsSER = new Gson().fromJson(jsonReader, type);

			// Insert
			configurations.setBias(configurationsSER.getBias());
			configurations.setLegendText(configurationsSER.getLegendText());
			configurations.setScalar(configurationsSER.getScalar());
			configurations.setSampleTime(configurationsSER.getSampleTime());
			configurations.setShowMaxData(configurationsSER.getShowMaxData());

			// Select
			legendText0.setText(configurations.getLegendText()[0]);
			legendText1.setText(configurations.getLegendText()[1]);
			legendText2.setText(configurations.getLegendText()[2]);
			legendText3.setText(configurations.getLegendText()[3]);
			legendText4.setText(configurations.getLegendText()[4]);
			legendText5.setText(configurations.getLegendText()[5]);

			bias0.setText(configurations.getBias()[0]);
			bias1.setText(configurations.getBias()[1]);
			bias2.setText(configurations.getBias()[2]);
			bias3.setText(configurations.getBias()[3]);
			bias4.setText(configurations.getBias()[4]);
			bias5.setText(configurations.getBias()[5]);

			scalar0.setText(configurations.getScalar()[0]);
			scalar1.setText(configurations.getScalar()[1]);
			scalar2.setText(configurations.getScalar()[2]);
			scalar3.setText(configurations.getScalar()[3]);
			scalar4.setText(configurations.getScalar()[4]);
			scalar5.setText(configurations.getScalar()[5]);

			for (MenuItem menuItem : sampleTime.getItems())
				if (menuItem.getText().equals(configurations.getSampleTime()) == true)
					sampleTime.setSelectedItem(menuItem);
			for (MenuItem menuItem : showMaxData.getItems())
				if (menuItem.getText().equals(configurations.getShowMaxData()) == true)
					showMaxData.setSelectedItem(menuItem);

		} catch (NullPointerException e) {
			dialogs.alertDialog(AlertType.WARNING, "No configurations", "There where no configurations loaded");
		} catch (FileNotFoundException e) {
			dialogs.exception("Configurations file not found", e);
		}
	}

	/**
	 * This will reset all fields
	 */
	private void clearFields() {
		legendText0.setText("");
		legendText1.setText("");
		legendText2.setText("");
		legendText3.setText("");
		legendText4.setText("");
		legendText5.setText("");

		bias0.setText("");
		bias1.setText("");
		bias2.setText("");
		bias3.setText("");
		bias4.setText("");
		bias5.setText("");

		scalar0.setText("");
		scalar1.setText("");
		scalar2.setText("");
		scalar3.setText("");
		scalar4.setText("");
		scalar5.setText("");

		sampleTime.setSelectedItem(sampleTime.getItems().get(0));
		showMaxData.setSelectedItem(showMaxData.getItems().get(0));

	}
	
	@FXML
    void askForConnection(ActionEvent event) {
		try {
			CloseableHttpClient httpclient = connections.getHttpclient();
			if (httpclient == null)
				return;
			if(selectedDevice.getSelectedItem() == null)
				return;
			
			String devicename = selectedDevice.getSelectedItem().getText();
			CloseableHttpResponse response = ConnectServerPresenter.getResponse(httpclient, "http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/user/askforconnection?devicename=" + devicename + "&username=" + connections.getUserName(), "POST");
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				SimpleMessageStatus simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
				if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_OK) {
					// You using the device now
					dialogs.alertDialog(AlertType.INFORMATION, "Device", simpleMessageStatus.getMessage());
					connectionButton.setText("Ask for a disconnection");
					deviceMessage.setDevicename(devicename);
				}else if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_FORBIDDEN) {
					// Somebody else are using the device
					dialogs.alertDialog(AlertType.WARNING, "Device", simpleMessageStatus.getMessage());
				}else if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_NO_CONTENT) {
					// You are disconnecting the device now
					dialogs.alertDialog(AlertType.INFORMATION, "Device", simpleMessageStatus.getMessage());
					connectionButton.setText("Ask for a connection");
					deviceMessage.setDevicename(null); // Empty - Not selected
				}else {
					// In this case it's 404 - Not found any devices
					dialogs.alertDialog(AlertType.WARNING, "Device", simpleMessageStatus.getMessage());
				}
			}else {
				dialogs.alertDialog(AlertType.ERROR, "Response", "Could not get the response from server");
			}
			
		} catch (IOException | NullPointerException e) {
			dialogs.alertDialog(AlertType.ERROR, "Not connected", "You have not connected the server");
		}
    }

}
