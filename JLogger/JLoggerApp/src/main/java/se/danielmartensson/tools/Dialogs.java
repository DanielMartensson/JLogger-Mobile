package se.danielmartensson.tools;

import java.util.Optional;

import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.control.DropdownButton;
import com.gluonhq.charm.glisten.control.ExceptionDialog;
import com.gluonhq.charm.glisten.control.TextField;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;

public class Dialogs {
	/**
	 * Create an exception dialog with intro text and an exception
	 * 
	 * @param introText Text to show
	 * @param e         Our error exception
	 */
	public void exception(String introText, Exception e) {
		ExceptionDialog exceptionDialog = new ExceptionDialog();
		exceptionDialog.setIntroText(introText);
		exceptionDialog.setException(e);
		exceptionDialog.showAndWait();
	}

	/**
	 * Ask a question. Cancel return is ""
	 * 
	 * @param titleText String title
	 * @param content   String question
	 * @return String
	 */
	public String input(String titleText, String content) {
		Dialog<String> dialog = new Dialog<String>();
		dialog.setAutoHide(false);
		dialog.setTitleText(titleText);
		dialog.setContent(new TextField(content));
		Button button_OK = new Button("OK");
		Button button_CANCLE = new Button("Cancle");
		button_OK.setOnAction(e -> {
			TextField text = (TextField) dialog.getContent();
			dialog.setResult(text.getText());
			dialog.hide();
		});
		button_CANCLE.setOnAction(e -> {
			dialog.setResult("");
			dialog.hide();
		});
		dialog.getButtons().addAll(button_OK, button_CANCLE);
		return dialog.showAndWait().get();
	}

	/**
	 * Ask for a selection
	 * 
	 * @return String
	 */
	public String choice() {
		Dialog<String> dialog = new Dialog<String>();
		dialog.setAutoHide(false);
		dialog.setTitleText("Select what you want to change");
		DropdownButton dropdownButton = new DropdownButton();
		dropdownButton.getItems().addAll(new MenuItem("Username"), new MenuItem("Password"), new MenuItem("Role"));
		dialog.setContent(dropdownButton);
		Button button_OK = new Button("OK");
		Button button_CANCLE = new Button("Cancle");
		button_OK.setOnAction(e -> {
			DropdownButton selectedDropdown = (DropdownButton) dialog.getContent();
			dialog.setResult(selectedDropdown.getSelectedItem().getText());
			dialog.hide();
		});
		button_CANCLE.setOnAction(e -> {
			dialog.setResult("");
			dialog.hide();
		});
		dialog.getButtons().addAll(button_OK, button_CANCLE);
		return dialog.showAndWait().get();
	}

	/**
	 * Ask for password
	 * 
	 * @return String
	 */
	public String password() {
		Dialog<String> dialog = new Dialog<String>();
		dialog.setAutoHide(false);
		dialog.setTitleText("Enter password");
		dialog.setContent(new PasswordField());
		Button button_OK = new Button("OK");
		Button button_CANCLE = new Button("Cancle");
		button_OK.setOnAction(e -> {
			PasswordField text = (PasswordField) dialog.getContent();
			dialog.setResult(text.getText());
			dialog.hide();
		});
		button_CANCLE.setOnAction(e -> {
			dialog.setResult("");
			dialog.hide();
		});
		dialog.getButtons().addAll(button_OK, button_CANCLE);
		return dialog.showAndWait().get();
	}

	/**
	 * Display a alert dialog
	 * 
	 * @param alertType AlertType.CONFIRMATION, AlertType.WARNING,
	 *                  AlertType.INFORMATION, AlertType.ERROR
	 * @param title     String title
	 * @param content   String information
	 */
	public void alertDialog(javafx.scene.control.Alert.AlertType alertType, String title, String content) {
		Alert info = new Alert(alertType);
		info.setAutoHide(false);
		info.setTitleText(title);
		info.setContentText(content);
		info.showAndWait();
	}

	/**
	 * Asking for selection
	 * 
	 * @param title   String title
	 * @param content String question
	 * @return boolean
	 */
	public boolean question(String title, String content) {
		Alert question = new Alert(AlertType.CONFIRMATION);
		question.setAutoHide(false);
		question.setTitleText(title);
		question.setContentText(content);
		question.setContentText(content);
		Optional<ButtonType> result = question.showAndWait();
		if (result.get() == ButtonType.OK)
			return true; // Yes!
		else
			return false; // No!
	}
}
