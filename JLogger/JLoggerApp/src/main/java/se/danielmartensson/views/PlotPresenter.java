package se.danielmartensson.views;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import se.danielmartensson.tools.Dialogs;
import se.danielmartensson.tools.FileHandler;
import se.danielmartensson.views.globals.Configurations;
import se.danielmartensson.views.globals.Connections;
import se.danielmartensson.views.globals.MeasurementLogs;
import se.danielmartensson.views.messages.DeviceMessage;
import se.danielmartensson.views.plotting.PlotMeasurements;

public class PlotPresenter {

	@FXML
	private View plot;

	@FXML
	private LineChart<String, Double> lineChart;

	@FXML
	private Slider slider0;

	@FXML
	private Slider slider1;

	@FXML
	private Slider slider2;

	@FXML
	private Slider slider3;

	@FXML
	private Slider slider4;

	@FXML
	private Slider slider5;
	
	@Inject
	private Configurations configurations;
	
	@Inject
	private MeasurementLogs measurementLogs;
	
	@Inject
	private Dialogs dialogs;
	
	@Inject 
	private DeviceMessage deviceMessage;
	
	@Inject
	private FileHandler fileHandler;

	@Inject
	private Connections connections;
	
	private AppBar appBar;
	private Button playButton = MaterialDesignIcon.PLAY_ARROW.button(e -> play());
	private Button stopButton = MaterialDesignIcon.STOP.button(e -> stop());
	private AtomicBoolean running = new AtomicBoolean();

	@FXML
	public void initialize() {
		/*
		 * Slide smooth in
		 */
		plot.setShowTransitionFactory(BounceInRightTransition::new);

		/*
		 * Listener when sliding in
		 */
		plot.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().getDrawer().open()));
				appBar.setTitleText("Plot");
				appBar.getActionItems().add(playButton);
				
				// Set disable or enable on the sliders
				enableSlider(slider0, configurations.getLegendText()[0]);
				enableSlider(slider1, configurations.getLegendText()[1]);
				enableSlider(slider2, configurations.getLegendText()[2]);
				enableSlider(slider3, configurations.getLegendText()[3]);
				enableSlider(slider4, configurations.getLegendText()[4]);
				enableSlider(slider5, configurations.getLegendText()[5]);
			}
		});
	}

	private void stop() {
		appBar.getActionItems().clear();
		appBar.getActionItems().add(playButton);
		appBar.getNavIcon().setDisable(false);
		running.set(false);
	}

	/**
	 * Enable or disable the slider
	 * @param slider Slider object
	 * @param legendText Legend text
	 */
	private void enableSlider(Slider slider, String legendText) {
		// If we have not write any configuration before
		if(legendText == null) {
			slider.setDisable(true);
			slider.setOpacity(0);
			slider.setValue(0);
			return;
		}
		
		// If we have configuration
		if(legendText.equals("") == true) {
			slider.setDisable(true);
			slider.setOpacity(0);
			slider.setValue(0);
		}else {
			slider.setDisable(false);
			slider.setOpacity(1.0);
		}
	}

	/**
	 * This will start logging
	 */
	private void play() {
		// Set 
		appBar.getActionItems().clear();
		appBar.getActionItems().add(stopButton);
		appBar.getNavIcon().setDisable(true); // We cannot go out to the menu slider page
		running.set(true);
		
		// Check if we have created a new log or selected a new locg
		if(measurementLogs.getLogName() == null) {
			dialogs.alertDialog(AlertType.ERROR, "No log file", "You have to select a log file");
			stop();
			return;
		}
		// If we have at least been in configuration page and return back = creating configurations 
		if(configurations.getSampleTime() == null) {
			dialogs.alertDialog(AlertType.ERROR, "No sample time", "You have to create configurations");
			stop();
			return;
		}
		// If we have not select any device in our configuration
		if(deviceMessage.getDevicename() == null) {
			dialogs.alertDialog(AlertType.ERROR, "No device", "You have to select a device");
			stop();
			return;
		}
		
		// Create data series
		XYChart.Series<String, Double> dataSeries0 = new XYChart.Series<String, Double>();
		XYChart.Series<String, Double> dataSeries1 = new XYChart.Series<String, Double>();
		XYChart.Series<String, Double> dataSeries2 = new XYChart.Series<String, Double>();
		XYChart.Series<String, Double> dataSeries3 = new XYChart.Series<String, Double>();
		XYChart.Series<String, Double> dataSeries4 = new XYChart.Series<String, Double>();
		XYChart.Series<String, Double> dataSeries5 = new XYChart.Series<String, Double>();
		
		// Insert them into the chart - Perhaps not all of them, depending on if legend is set or not
		lineChart.getData().clear();
		ArrayList<Integer> legendIndex = new ArrayList<Integer>(); // Has same length as lineChart.getData()
		insertSeriesToChart(lineChart, dataSeries0, configurations.getLegendText(), legendIndex, 0);
		insertSeriesToChart(lineChart, dataSeries1, configurations.getLegendText(), legendIndex, 1);
		insertSeriesToChart(lineChart, dataSeries2, configurations.getLegendText(), legendIndex, 2);
		insertSeriesToChart(lineChart, dataSeries3, configurations.getLegendText(), legendIndex, 3);
		insertSeriesToChart(lineChart, dataSeries4, configurations.getLegendText(), legendIndex, 4);
		insertSeriesToChart(lineChart, dataSeries5, configurations.getLegendText(), legendIndex, 5);
		
		// Start thread now
		PlotMeasurements plotMeasurement = new PlotMeasurements(running, slider0, slider1, slider2, slider3, slider4, slider5, lineChart, appBar, playButton, legendIndex, fileHandler, connections, configurations, measurementLogs, deviceMessage);
		plotMeasurement.start();
	}

	/**
	 * Insert the data series into the chart if the legend have a length longer than 0
	 * @param chart
	 * @param seriesIndex 
	 * @param serie
	 * @param legend
	 */
	private void insertSeriesToChart(LineChart<String, Double> chart, XYChart.Series<String, Double> series, String[] legends, ArrayList<Integer> legendIndex, int index) {
		if(legends[index].length() > 0) {
			series.setName(legends[index]);
			chart.getData().add(series);
			legendIndex.add(index);
		}
	}
}
