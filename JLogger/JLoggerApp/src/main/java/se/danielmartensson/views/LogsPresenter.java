package se.danielmartensson.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import se.danielmartensson.tools.Dialogs;
import se.danielmartensson.tools.FileHandler;
import se.danielmartensson.views.containers.LogTableContainer;
import se.danielmartensson.views.globals.MeasurementLogs;

public class LogsPresenter {

	public static final String TEST_FILE_PATH = "/JLoggerStorage/test.log";
	public static final String LOG_FOLDER_PATH = "/JLoggerStorage/logs/";

	@FXML
	private View logs;

	@FXML
	private TableView<LogTableContainer> tableView;

	@FXML
	private TableColumn<LogTableContainer, String> logColumn;

	@FXML
	private TableColumn<LogTableContainer, String> createdColumn;

	@Inject
	private FileHandler fileHandler;

	@Inject
	private Dialogs dialogs;

	@Inject
	private MeasurementLogs measurementLogs;

	private ObservableList<LogTableContainer> tableViewListener;

	@FXML
	public void initialize() {
		// Check test - Need to be done
		fileHandler.runCreateDeleteTest(TEST_FILE_PATH);

		// Slide smooth in
		logs.setShowTransitionFactory(BounceInRightTransition::new);

		// Listener when sliding in
		logs.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().getDrawer().open()));
				appBar.setTitleText("Logs");
				appBar.getActionItems().add(MaterialDesignIcon.CREATE.button(e -> createNewLog()));
				appBar.getActionItems().add(MaterialDesignIcon.DELETE.button(e -> deleteLog()));

			} else {
				// When we leave
				if (tableView.getSelectionModel().getSelectedItem() != null)
					measurementLogs.setLogName(tableView.getSelectionModel().getSelectedItem().getLog());
				else
					measurementLogs.setLogName(null);
			}
		});

		// Declare the table view and create a listener for the table
		logColumn.setCellValueFactory(new PropertyValueFactory<LogTableContainer, String>("log"));
		createdColumn.setCellValueFactory(new PropertyValueFactory<LogTableContainer, String>("created"));
		tableViewListener = FXCollections.observableArrayList();
		tableView.setItems(tableViewListener);

		// Update table view
		Platform.runLater(() -> updateTable());
		tableView.getSelectionModel().selectFirst();

	}

	/**
	 * Delete a log
	 */
	private void deleteLog() {
		// Check if we have selected a row
		if (tableView.getSelectionModel().getSelectedItem() == null)
			return;

		// Ask a question if we should delete or not
		if (dialogs.question("Delete", "Do you want to delete?") == false)
			return;

		// Delete
		LogTableContainer logTableContainer = tableView.getSelectionModel().getSelectedItem();
		String logName = logTableContainer.getLog();
		int selectedRow = tableView.getSelectionModel().getSelectedIndex();
		fileHandler.deleteFolder(LOG_FOLDER_PATH + logName + "/");
		tableViewListener.remove(logTableContainer);
		tableView.getSelectionModel().select(selectedRow);
	}

	/**
	 * Create a new log
	 */
	private void createNewLog() {
		// Check if we are connected to the server
		String logName = dialogs.input("Enter new name", "name...");

		// Exceptions
		if (logName.equals("") == true)
			return;
		if (logName.equals("test") == true) {
			dialogs.alertDialog(AlertType.ERROR, "Wrong", "Cannot be named 'test'");
			return;
		}
		if (logName.matches("[a-zA-Z0-9]*") == false) {
			dialogs.alertDialog(AlertType.INFORMATION, "Only numbers and letters", "Select another file name.");
			return; // Contains more than letters and numbers? Return then.
		}
		if (fileHandler.exist(LOG_FOLDER_PATH + logName + "/" + logName + ".log") == true) {
			dialogs.alertDialog(AlertType.ERROR, "Exist", "Log already exist.");
			return;
		}

		// Get time
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String creationDate = formatter.format(date);

		// Add to table and also to the json file
		LogTableContainer logTableContainer = new LogTableContainer(logName, creationDate);
		Gson gson = new Gson();
		Type type = new TypeToken<LogTableContainer>() {}.getType();
		fileHandler.writeTextTo(LOG_FOLDER_PATH + logName + "/" + logName + ".log", gson.toJson(logTableContainer, type));
		tableViewListener.add(logTableContainer);
		tableView.getSelectionModel().selectLast();

		// Create also the measurement
		fileHandler.createNewFile(LOG_FOLDER_PATH + logName + "/" + logName + ".measure", true);

		// And the configs
		fileHandler.createNewFile(LOG_FOLDER_PATH + logName + "/" + logName + ".config", true);

	}

	/**
	 * Update the table view
	 */
	private void updateTable() {
		tableViewListener.clear();
		Gson gson = new Gson();
		Type type = new TypeToken<LogTableContainer>() {
		}.getType();
		File[] files = fileHandler.scanFolderNames(LOG_FOLDER_PATH);
		if (files != null)
			for (File file : files) {
				try {
					if(fileHandler.exist(LOG_FOLDER_PATH + file.getName() + "/" + file.getName() + ".log") == true) {
						File jsonFile = fileHandler.loadNewFile(LOG_FOLDER_PATH + file.getName() + "/" + file.getName() + ".log");
						FileReader fileReader = new FileReader(jsonFile);
						JsonReader jsonReader = new JsonReader(fileReader);
						LogTableContainer logTableContainer = gson.fromJson(jsonReader, type);
						tableViewListener.add(logTableContainer);
					}
				} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
					dialogs.exception("Cannot read file", e);
				}
			}
	}
}
