package se.danielmartensson.views.plotting;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.gluonhq.charm.glisten.control.AppBar;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import se.danielmartensson.tools.FileHandler;
import se.danielmartensson.views.LogsPresenter;
import se.danielmartensson.views.globals.Configurations;
import se.danielmartensson.views.globals.Connections;
import se.danielmartensson.views.globals.MeasurementLogs;
import se.danielmartensson.views.messages.DeviceMessage;


public class PlotMeasurements extends Thread {

	private BufferedWriter bufferedWriter;

	private AtomicBoolean running;

	private Slider slider0;

	private Slider slider1;

	private Slider slider2;

	private Slider slider3;

	private Slider slider4;

	private Slider slider5;

	private LineChart<String, Double> lineChart;

	private AppBar appBar;

	private Button playButton;

	private ArrayList<Integer> legendIndex;

	private FileHandler fileHandler;

	private Connections connections;

	private Configurations configurations;

	private MeasurementLogs measurementLogs;

	private DeviceMessage deviceMessage;

	private CloseableHttpResponse response;

	public PlotMeasurements(AtomicBoolean running, Slider slider0, Slider slider1, Slider slider2, Slider slider3, Slider slider4, Slider slider5, LineChart<String, Double> lineChart, AppBar appBar, Button playButton, ArrayList<Integer> legendIndex, FileHandler fileHandler, Connections connections, Configurations configurations, MeasurementLogs measurementLogs, DeviceMessage deviceMessage) {
		this.running = running;
		this.slider0 = slider0;
		this.slider1 = slider1;
		this.slider2 = slider2;
		this.slider3 = slider3;
		this.slider4 = slider4;
		this.slider5 = slider5;
		this.lineChart = lineChart;
		this.appBar = appBar;
		this.playButton = playButton;
		this.legendIndex = legendIndex;
		this.fileHandler = fileHandler;
		this.connections = connections;
		this.configurations = configurations;
		this.measurementLogs = measurementLogs;
		this.deviceMessage = deviceMessage;
	}

	@Override
	public void run() {

		// Get series - It has the same length as legendIndex
		ObservableList<XYChart.Series<String, Double>> series = lineChart.getData();

		// Open up for file writer
		try {
			File file = fileHandler.loadNewFile(LogsPresenter.LOG_FOLDER_PATH + measurementLogs.getLogName() + "/" + measurementLogs.getLogName() + ".measure");
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(""); // Clear
		} catch (IOException e1) {
			restore(); // Something bad happen -> Quit
		}

		// Create the time stamp formatter
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		
		// Create the header in the measurement file with the legends as topic
		appendWith("Time" + ",");
		for(int i = 0; i <  configurations.getLegendText().length; i++) {
			if(totalSumLeft(configurations.getLegendText(), i) > 0) {
				if(configurations.getLegendText()[i].length() > 0) { // If we select legend0 and legend 2, then we wont write out legend1 if lenght of legend1 == 0
					appendWith(configurations.getLegendText()[i] + "," + "Slider" + i + ",");
				}
			}else {
				appendWith(configurations.getLegendText()[i] + "," + "Slider" + i + "\n");
				break; // Done!
			}
		}	
		
		// Check if deviceName is null - Not selected in other words
		if(deviceMessage.getDevicename() == null)
			restore(); // Quit

		// Make a simple connection
		
		// Loop
		System.out.println("Start loop");
		while (running.get() == true) {

			// Set slider values
			deviceMessage.getSliderValues()[0] = (short) slider0.getValue();
			deviceMessage.getSliderValues()[1] = (short) slider1.getValue();
			deviceMessage.getSliderValues()[2] = (short) slider2.getValue();
			deviceMessage.getSliderValues()[3] = (short) slider3.getValue();
			deviceMessage.getSliderValues()[4] = (short) slider4.getValue();
			deviceMessage.getSliderValues()[5] = (short) slider5.getValue();
				
			// Send the slider values to the server
			HttpPost httppost = new HttpPost("http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/user/calldevice");
			try {
				httppost.setEntity(new StringEntity(new Gson().toJson(deviceMessage)));
				httppost.setHeader("Content-type", "application/json");
				response = connections.getHttpclient().execute(httppost);
			} catch (Exception e) {
				System.out.println("Could not get the response");
				restore(); // Something bad happen -> Quit
				break;
			}


			// Get time stamp directly after we have send the slider values to JLoggerServer
			// Then JLoggerServer fast implement the slider values to the JLoggerDevice and JLoggerDevice take a ADC measurement and send it quick back to JLoggerServer
			String timeStamp = simpleDateFormat.format(new Date());

			// Sleep - We waiting for the ADC values
			try {
				Thread.sleep(Integer.parseInt(configurations.getSampleTime()));
			} catch (Exception e) {
				System.out.println("Could not sleep the thread");
				restore(); // Something bad happen -> Quit
				break;
			}
			// Now get the ADC values
			try {
				String json = EntityUtils.toString(response.getEntity());
				Type type = new TypeToken<DeviceMessage>() {}.getType();
				deviceMessage = new Gson().fromJson(json, type);
			} catch (Exception e) {
				System.out.println("Cannot get the ADC values");
				e.printStackTrace();
				restore(); // Something bad happen -> Quit
				break;
			}
			

			Platform.runLater(() -> {
			// Begin to write the time stamp
			appendWith(timeStamp + ",");
			for (int i = 0; i < series.size(); i++) {
				// Get legendIndex
				int index = legendIndex.get(i); // This can be e.g 0, 1, 5, 6 depending how we select our legends

				// Get the ADC data
				short adcValue = deviceMessage.getAdcValues()[index]; 

				// Create Data object and insert its values
				Data<String, Double> sample = new Data<String, Double>();

				// Set the measurement: Y = scalar*adc + bias
				double Y = Double.parseDouble(configurations.getScalar()[index]) * adcValue + Double.parseDouble(configurations.getBias()[index]);
				sample.setYValue(Y);
				
				// Set time stamp
				sample.setXValue(timeStamp);

				// Check how many data we want to display
				series.get(i).getData().add(sample);
				if (series.get(i).getData().size() > Integer.parseInt(configurations.getShowMaxData()))
					series.get(i).getData().remove(0); // Remove the first one

				// Write to file
				if(i < series.size()-1)
					appendWith(String.valueOf(Y) + "," + deviceMessage.getSliderValues()[index] + ",");
				else
					appendWith(String.valueOf(Y) + "," + deviceMessage.getSliderValues()[index] + "\n");
			}
		});

	}
	System.out.println("Done loop");

	// Save file
	try {
		bufferedWriter.close();
	} catch (Exception e) {
		// TODO: Dialog here
	}
}
	
	/**
	 * This function will sum how many characters we have left in our legends array
	 * @param legendText Our legend array string object
	 * @param i Current index
	 * @return total sum
	 */
	private int totalSumLeft(String[] legendText, int i) {
		int sumChars = 0;
		for(int k = i+1; k < legendText.length; k++) {
			sumChars += legendText[k].length();
		}
		return sumChars;
	}

	/**
	 * This will append text to our file
	 * @param text
	 */
	private void appendWith(String text) {
		try {
			bufferedWriter.append(text);
		} catch (IOException e) {
			restore(); // Something bad happen -> Quit
		}
	}

	/**
	 * If something bad happen
	 */
	private void restore() {
		Platform.runLater(() -> {
		appBar.getActionItems().clear();
		appBar.getActionItems().add(playButton);
		appBar.getNavIcon().setDisable(false); // So we can go back the the menu again
		});
		running.set(false); // Quit while loop above
	}

}
