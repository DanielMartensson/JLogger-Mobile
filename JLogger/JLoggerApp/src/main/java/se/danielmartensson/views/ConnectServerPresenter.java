package se.danielmartensson.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.auth.UsernamePasswordCredentials;
import cz.msebera.android.httpclient.client.config.RequestConfig;
import cz.msebera.android.httpclient.client.methods.CloseableHttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.BasicCredentialsProvider;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClients;
import cz.msebera.android.httpclient.util.EntityUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import se.danielmartensson.tools.Dialogs;
import se.danielmartensson.tools.FileHandler;
import se.danielmartensson.views.containers.OnlineTableContainer;
import se.danielmartensson.views.globals.Connections;
import se.danielmartensson.views.messages.DeviceMessage;
import se.danielmartensson.views.messages.Online;
import se.danielmartensson.views.messages.Role;
import se.danielmartensson.views.messages.SimpleMessageStatus;
import se.danielmartensson.views.messages.User;
import se.danielmartensson.views.savings.LastLogin;

public class ConnectServerPresenter {

	private static final String LOGIN_PARAMETERS = "/JLoggerStorage/loginparameters.json";

	public static String[] ports = { "80", "8080", "8081", "8082", "8085", "8090"};

	@FXML
	private View connectserver;

	@FXML
	private TextField serverAddress;

	@FXML
	private DropdownButton serverPort;

	@FXML
	private TextField userName;

	@FXML
	private PasswordField password;

	@FXML
	private Button connectServerButton;

	@FXML
	private TableView<OnlineTableContainer> tableView;

	@FXML
	private TableColumn<OnlineTableContainer, String> nameColumn;

	@FXML
	private TableColumn<OnlineTableContainer, Boolean> onlineColumn;

	@FXML
	private TableColumn<OnlineTableContainer, String> roleColumn;

	@FXML
	private TableColumn<OnlineTableContainer, String> deviceColumn;

	private ObservableList<OnlineTableContainer> tableViewListener;

	@Inject
	private Dialogs dialogs;

	@Inject
	private Connections connections;
	
	@Inject
	private DeviceMessage deviceMessage;
	
	@Inject
	private FileHandler fileHandler;

	@FXML
	public void initialize() {
		// Slide smooth in
		connectserver.setShowTransitionFactory(BounceInRightTransition::new);

		// Listener when sliding in
		connectserver.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().getDrawer().open()));
				appBar.setTitleText("Connect server");
				appBar.getActionItems().add(MaterialDesignIcon.PEOPLE.button(e -> addUser()));
				appBar.getActionItems().add(MaterialDesignIcon.SEARCH.button(e -> searchUsers()));
				appBar.getActionItems().add(MaterialDesignIcon.CREATE.button(e -> editUser()));
				appBar.getActionItems().add(MaterialDesignIcon.DELETE.button(e -> deleteUser()));
				
				// Insert the last login parameters
				loadLoginParameters();
			}else {
				// When you leave
				
			}
		});

		// Declare the table view and create a listener for the table
		nameColumn.setCellValueFactory(new PropertyValueFactory<OnlineTableContainer, String>("name"));
		onlineColumn.setCellValueFactory(new PropertyValueFactory<OnlineTableContainer, Boolean>("online"));
		roleColumn.setCellValueFactory(new PropertyValueFactory<OnlineTableContainer, String>("role"));
		deviceColumn.setCellValueFactory(new PropertyValueFactory<OnlineTableContainer, String>("device"));
		tableViewListener = FXCollections.observableArrayList();
		tableView.setItems(tableViewListener);

		// Set some default values
		for (String port : ports)
			serverPort.getItems().add(new MenuItem(port));

		// Check if connected
		if (connections.isConnected() == false)
			connectServerButton.setText("Connect to the server");
		
		// Clear the online table
		tableViewListener.clear();
		tableView.setDisable(true);
	}

	/**
	 * This function will set the parameters of last login
	 */
	private void loadLoginParameters() {
		if(fileHandler.exist(LOGIN_PARAMETERS) == true) {
			try {
				File file = fileHandler.loadNewFile(LOGIN_PARAMETERS);
				LastLogin lastLogin = new Gson().fromJson(new JsonReader(new FileReader(file)), LastLogin.class);
				serverAddress.setText(lastLogin.getServerAddress());
				userName.setText(lastLogin.getUserName());
				for(int i = 0; i < ports.length; i++) {
					if(ports[i].equals(lastLogin.getServerPort()) == true) {
						serverPort.setSelectedItem(serverPort.getItems().get(i)); // Select
					}
				}	
			} catch (Exception e) {
				dialogs.exception("Cannot load: " + LOGIN_PARAMETERS, e);
			}
			
		}else {
			// No file exist, create
			fileHandler.createNewFile(LOGIN_PARAMETERS, true);
		}
	}

	/**
	 * Ask what you want to change
	 */
	private void editUser() {
		try {

			CloseableHttpClient httpclient = connections.getHttpclient();
			if (httpclient == null)
				return;

			// First get the user
			if(tableView.getSelectionModel().getSelectedItem() == null) {
				dialogs.alertDialog(AlertType.WARNING, "No user", "You need to select a user inside the table view");
				return; 
			}
			CloseableHttpResponse response = getResponse(httpclient,"http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/admin/getuser?username=" + tableView.getSelectionModel().getSelectedItem().getName(), "GET");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				dialogs.alertDialog(AlertType.WARNING, "Forbidden", "You don't have the authority");
				return; // Forbidden
			}
			// In this case, there is a JSON message of a User object included inside this simpleMessageStatus object
			SimpleMessageStatus simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
			if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_NOT_FOUND) {
				dialogs.alertDialog(AlertType.WARNING, "Exist", simpleMessageStatus.getMessage()); // Except this 404 case does not include json message
				return;
			}
			Type type = new TypeToken<User>() {}.getType();
			User user = new Gson().fromJson(simpleMessageStatus.getMessage(), type);
			response.close();
			
			// Ask for a choice
			String choice = dialogs.choice();
			if (choice.equals("") == true)
				return;

			// Change role
			if (choice.equals("Role") == true) {
				String newrole = dialogs.input("Enter new role", "ADMIN or USER").toUpperCase();
				if (newrole.equals("") == true || (newrole.equals("ADMIN") == false && newrole.equals("USER") == false)) {
					dialogs.alertDialog(AlertType.WARNING, "Invalid", "You did not enter correct role");
					return;
				}
				for (Role role : user.getRoles()) 
					role.setRole(newrole);
			}

			// Change username
			if (choice.equals("Username") == true) {
				String newUsername = dialogs.input("Enter new username", "usernamne...").toLowerCase();
				if (newUsername.equals("") == true)
					return;
				user.setUsername(newUsername);
			}

			// Change password
			boolean updatePassword = false;
			if (choice.equals("Password") == true) {
				String newPassword = dialogs.password();
				if (newPassword.equals("") == true)
					return;
				user.setPassword(newPassword);
				updatePassword = true;
			}

			// Update now
			HttpPost httppost = new HttpPost("http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/admin/updateuser?updatepassword=" + updatePassword);
			httppost.setEntity(new StringEntity(new Gson().toJson(user)));
			httppost.setHeader("Content-type", "application/json");
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
				if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_OK) {
					dialogs.alertDialog(AlertType.INFORMATION, "Updated", simpleMessageStatus.getMessage());
				}else {
					// In this case it's 404
					dialogs.alertDialog(AlertType.WARNING, "Exist", simpleMessageStatus.getMessage());
				}
			}else {
				dialogs.alertDialog(AlertType.ERROR, "Response", "Could not get the response from server");
			}
			response.close();
		} catch (Exception e) {
			dialogs.exception("Cannot search for users - not connected", e);
		}
	}

	/**
	 * Get all users that are online or not online
	 */
	private void searchUsers() {
		try {
			CloseableHttpClient httpclient = connections.getHttpclient();
			if (httpclient == null)
				return;
			CloseableHttpResponse response = getResponse(httpclient, "http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/user/searchusers", "GET");
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// In this case, there is a JSON message of a List<Online> object included inside this simpleMessageStatus object
				SimpleMessageStatus simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
				if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_OK) {
					// Get our list
					Type type = new TypeToken<List<Online>>() {}.getType();
					List<Online> usersOnline = new Gson().fromJson(simpleMessageStatus.getMessage(), type);
					tableViewListener.clear();
					for (Online userOnline : usersOnline)
						tableViewListener.add(new OnlineTableContainer(userOnline.getUsername(), userOnline.isOnline(), userOnline.getRole(), userOnline.getDevice()));
					dialogs.alertDialog(AlertType.INFORMATION, "Updated", "Table is updated");

				}else {
					// In this case it's 404
					dialogs.alertDialog(AlertType.WARNING, "No users", "You don't have any users in database");
				}
			}else {
				dialogs.alertDialog(AlertType.ERROR, "Response", "Could not get the response from server");
			}
			response.close();
		} catch (Exception e) {
			dialogs.exception("Cannot search for users - not connected", e);
		}
	}

	/**
	 * Delete selected user
	 */
	private void deleteUser() {
		try {
			CloseableHttpClient httpclient = connections.getHttpclient();
			if (httpclient == null)
				return;
			if(tableView.getSelectionModel().getSelectedItem() == null) {
				dialogs.alertDialog(AlertType.WARNING, "No user", "You need to select a user inside the table view");
				return; 
			}
			CloseableHttpResponse response = getResponse(httpclient, "http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/admin/deleteuser?username=" + tableView.getSelectionModel().getSelectedItem().getName(), "POST");
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				SimpleMessageStatus simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
				if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_OK) {
					dialogs.alertDialog(AlertType.INFORMATION, "Deleted", simpleMessageStatus.getMessage());
				}else {
					// In this case it's 404
					dialogs.alertDialog(AlertType.WARNING, "Exist", simpleMessageStatus.getMessage());
				}
			}else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				dialogs.alertDialog(AlertType.WARNING, "Forbidden", "You don't have the authority");
			}else {
				dialogs.alertDialog(AlertType.ERROR, "Response", "Could not get the response from server");
			}
			response.close();
			
		} catch (Exception e) {
			dialogs.exception("Cannot add a new user to the server", e);
		}
	}

	/**
	 * Add a new user
	 */
	private void addUser() {
		try {
			// Get input
			String username = dialogs.input("Enter username", "username...").toLowerCase();
			if (username.equals("") == true)
				return;
			String password = dialogs.password();
			if (password.equals("") == true)
				return;
			String userRole = dialogs.input("Enter role", "ADMIN or USER").toUpperCase();
			if (userRole.equals("") == true || (userRole.equals("ADMIN") == false && userRole.equals("USER") == false))
				return;

			// Create user
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			Role role = new Role();
			role.setRole(userRole.toUpperCase());
			Set<Role> roles = new HashSet<Role>();
			roles.add(role);
			user.setRoles(roles);

			// Send user object to the server
			CloseableHttpClient httpclient = connections.getHttpclient();
			if (httpclient == null)
				return;
			HttpPost httppost = new HttpPost("http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/admin/adduser");
			httppost.setEntity(new StringEntity(new Gson().toJson(user)));
			httppost.setHeader("Content-type", "application/json");
			CloseableHttpResponse response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				SimpleMessageStatus simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
				if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_OK) {
					dialogs.alertDialog(AlertType.INFORMATION, "Added", simpleMessageStatus.getMessage());
				}else {
					// In this case it's 403
					dialogs.alertDialog(AlertType.WARNING, "Exist", simpleMessageStatus.getMessage());
				}
			}else if(response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
				dialogs.alertDialog(AlertType.WARNING, "Forbidden", "You don't have the authority");
			}else {
				dialogs.alertDialog(AlertType.ERROR, "Response", "Could not get the response from server");
			}
			response.close();
		} catch (Exception e) {
			dialogs.exception("Cannot add a new user to the server", e);
		}
	}

	/**
	 * Connect the server or disconnect
	 * 
	 * @param event
	 */
	@FXML
	void connectServer(ActionEvent event) {
		if (connections.isConnected() == false) {
			try {
				// Set the timeout
				int timeout = 10; // seconds
				RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(timeout * 1000)
				  .setConnectionRequestTimeout(timeout * 1000)
				  .setSocketTimeout(timeout * 1000).build();
				
				// First create the initial Http client
				BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(serverAddress.getText(), Integer.parseInt(serverPort.getSelectedItem().getText())), new UsernamePasswordCredentials(userName.getText(), password.getText()));
				CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).setDefaultRequestConfig(config).build();		
				
				// Then get our response
				CloseableHttpResponse response = getResponse(httpclient, "http://" + serverAddress.getText() + ":" + serverPort.getSelectedItem().getText() + "/user/connect?username=" + userName.getText(), "GET");
				SimpleMessageStatus simpleMessageStatus =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(response.getEntity()));				
				
				// Now do some connections
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					if(simpleMessageStatus.getStatuscode() == HttpStatus.SC_OK) {
						// Display the news
						dialogs.alertDialog(AlertType.INFORMATION, "Connected", simpleMessageStatus.getMessage());
						connectServerButton.setText("Disconnect from the server");
						
						// Save our connections
						connections.setConnected(true);
						connections.setServerAddress(serverAddress.getText());
						connections.setServerPort(serverPort.getSelectedItem().getText());
						connections.setUserName(userName.getText());
						connections.setHttpclient(httpclient);
						
						// Set device to None
						deviceMessage.setDevicename(null);
						
						// Ask for a disconnection - We say the device name is None, not null here
						response = ConnectServerPresenter.getResponse(httpclient, "http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/user/askforconnection?devicename=" + "None" + "&username=" + connections.getUserName(), "POST");
						
						// Disable UI components
						serverAddress.setDisable(true);
						serverAddress.setOpacity(0.5);
						serverPort.setDisable(true);
						userName.setDisable(true);
						userName.setOpacity(0.5);
						password.setDisable(true);
						password.setOpacity(0.5);
						
						// Clear the online table
						tableViewListener.clear();
						tableView.setDisable(false);
						
						// Save login parameters
						saveLoginParameters();
					}else {
						// In this case it's 404
						dialogs.alertDialog(AlertType.WARNING, "Not exist", simpleMessageStatus.getMessage());
					}
				} else {
					dialogs.alertDialog(AlertType.ERROR, "Not connected", "Failed to login");
				}
				response.close();
			} catch (Exception e) {
				dialogs.exception("Cannot connect server", e);
			}
		}else {
			try {
				// Disconnect from server
				CloseableHttpClient httpclient = connections.getHttpclient();
				CloseableHttpResponse responseDisconnect = getResponse(httpclient, "http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/user/disconnect?username=" + connections.getUserName(), "POST");
				SimpleMessageStatus simpleMessageStatusDisconnect =  new SimpleMessageStatus().getJsonMessage(EntityUtils.toString(responseDisconnect.getEntity()));				
				
				// Check if we got connection
				if (responseDisconnect.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					// Logout the user from Spring Security
					CloseableHttpResponse responseLogout = getResponse(httpclient, "http://" + connections.getServerAddress() + ":" + connections.getServerPort() + "/logout", "POST");

					// Check if we could logout from Spring
					if(responseLogout.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
						// Display the news
						dialogs.alertDialog(AlertType.INFORMATION, "Logged out", simpleMessageStatusDisconnect.getMessage());
						connectServerButton.setText("Connect to the server");
						
						// Change our connections
						connections.getHttpclient().close();
						connections.setHttpclient(null);
						connections.setConnected(false);
						
						// Set device to null
						deviceMessage.setDevicename(null);
						
						// Enable UI components
						serverAddress.setDisable(false);
						serverAddress.setOpacity(1);
						serverPort.setDisable(false);
						userName.setDisable(false);
						userName.setOpacity(1);
						password.setDisable(false);
						password.setOpacity(1);
						
						// Clear the online table
						tableViewListener.clear();
						tableView.setDisable(true);
					}else {
						dialogs.alertDialog(AlertType.WARNING, "Disconneted only. Try login -> Logout", responseLogout.getStatusLine().getReasonPhrase());
					}
					responseLogout.close();
				} else {
					dialogs.alertDialog(AlertType.ERROR, "Not disconnected", responseDisconnect.getStatusLine().getReasonPhrase());
				}
				responseDisconnect.close();
			} catch (Exception e) {
				dialogs.exception("Cannot disconnect server", e);
			}
		}
	}
	
	/**
	 * This will save the login parameters
	 */
	private void saveLoginParameters() {
		if(fileHandler.exist(LOGIN_PARAMETERS) == true) {
			try {
				File file = fileHandler.loadNewFile(LOGIN_PARAMETERS);
				LastLogin lastLogin = new LastLogin();
				lastLogin.setServerAddress(serverAddress.getText());
				lastLogin.setServerPort(serverPort.getSelectedItem().getText());
				lastLogin.setUserName(userName.getText());
				String json = new Gson().toJson(lastLogin);
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
				bufferedWriter.write(json);
				bufferedWriter.close();
			} catch (Exception e) {
				dialogs.exception("Cannot load: " + LOGIN_PARAMETERS, e);
			}
			
		}else {
			// No file exist, create
			fileHandler.createNewFile(LOGIN_PARAMETERS, true);
		}
	}

	/**
	 * Get the response
	 * @param httpclient The client handler
	 * @param url Url of the connection
	 * @param GetPost "GET", default is post
	 * @return
	 */
	static public CloseableHttpResponse getResponse(CloseableHttpClient httpclient, String url, String GetPost) {
		try {
			if(GetPost.equals("GET") == true) {
				HttpGet http = new HttpGet(url);
				return httpclient.execute(http);
			}else {
				HttpPost http = new HttpPost(url);
				return httpclient.execute(http);
			}
		} catch (Exception e) {
			return null;
		}
	}
}
