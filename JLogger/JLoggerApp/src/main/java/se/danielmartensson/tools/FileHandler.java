package se.danielmartensson.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.StorageService;

import javafx.scene.control.Alert.AlertType;

public class FileHandler {

	private static File localRoot;

	@Inject
	private Dialogs dialogs;

	/**
	 * Constructor - Open connection to root catalog Local root e.g /root/Documents
	 * folder
	 */
	public FileHandler() {
		localRoot = Services.get(StorageService.class).flatMap(s -> s.getPublicStorage("Documents"))
				.orElseThrow(() -> new RuntimeException("Error retrieving private storage"));
	}

	/**
	 * This method will create a file and then delete the file.
	 * 
	 * @param filePath Our file path
	 */
	public void runCreateDeleteTest(String filePath) {
		File file = new File(localRoot + filePath);
		file.mkdirs();
		createTheFile(file);
		file.delete();
	}

	/**
	 * Delete the file if it's exist
	 * 
	 * @param filePath Our file path
	 */
	public void deleteFile(String filePath) {
		File file = loadNewFile(filePath);
		if (exist(filePath) == false) {
			dialogs.alertDialog(AlertType.WARNING, "Delete",
					"The file: \n" + filePath + "\nDid not exist. Just continue as normal.");
			return;
		}

		if (file.delete() == false)
			dialogs.alertDialog(AlertType.ERROR, "Delete", "Cannot delete file: \n" + filePath);
	}

	/**
	 * This will delete our folder
	 * 
	 * @param pathToFolder Our folder path
	 */
	public void deleteFolder(String pathToFolder) {
		File folder = new File(localRoot + pathToFolder);
		File[] files = folder.listFiles();
		try {
			for (File file : files)
				file.delete(); // Delete all files first!
			folder.delete(); // Now delete the folder
		} catch (NullPointerException e) {
			dialogs.exception("Could not delete folder " + folder.getName() + ". It did not exist, just continue.", e);
		}
	}

	/**
	 * This will create a new file
	 * 
	 * @param filePath         Our file path
	 * @param allwaysOverwrite Set this to true if we don't want any question about
	 *                         overwriting
	 */
	public void createNewFile(String filePath, boolean allwaysOverwrite) {
		File file = new File(localRoot + filePath);
		file.getParentFile().mkdirs(); // Create folders, if it's not created
		if (file.exists() == true && allwaysOverwrite == false) {
			if (dialogs.question("File already exist", "Should we overwrite?") == true)
				createTheFile(file);
			else
				return; // Cancle
		} else {
			createTheFile(file);
		}
	}

	/**
	 * This will create the file
	 * 
	 * @param file Our file
	 */
	private void createTheFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			dialogs.exception("Cannot create the file at:\n" + file.getAbsolutePath(), e);
		}
	}

	/**
	 * Load the file
	 * 
	 * @param filePath Our file path
	 * @return File
	 */
	public File loadNewFile(String filePath) {
		File file = new File(localRoot + filePath);
		if (file.exists() == false)
			return null;
		else
			return file;
	}

	/**
	 * Check if a file exist
	 * 
	 * @param filePath Our file path
	 * @return boolean
	 */
	public boolean exist(String filePath) {
		File file = new File(localRoot + filePath);
		if (file.exists() == false)
			return false;
		else
			return true;
	}

	/**
	 * This list all folder names only
	 * 
	 * @param pathToFolder Our folder path
	 * @return File[]
	 */
	public File[] scanFolderNames(String pathToFolder) {
		File folder = new File(localRoot + pathToFolder);
		File[] directories = folder.listFiles(File::isDirectory);
		sortByDateModified(directories);
		return directories;
	}

	/**
	 * This method sorting the file objects by date modified
	 * 
	 * @param files Our File path
	 * @return File[]
	 */
	private File[] sortByDateModified(File[] files) {
		if (files != null) {
			/*
			 * Sort on date modified
			 */
			Arrays.sort(files, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return Long.compare(f1.lastModified(), f2.lastModified());
				}
			});

			return files;
		} else {
			return null;
		}
	}

	/**
	 * Scan a folder and list all files
	 * 
	 * @param fileExtension File extension such as .csv, .txt or .png etc.
	 * @param pathToFolder  Our string path to the folder
	 * @return File[]
	 */
	public File[] scanFolder(String fileExtension, String pathToFolder) {
		File folder = new File(localRoot + pathToFolder);
		if (folder.exists() == false)
			folder.mkdirs(); // Create one

		File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(fileExtension));
		sortByDateModified(files);
		return files;
	}

	/**
	 * Count the files inside a folder
	 * 
	 * @param fileExtension File extension such as .csv, .txt or .png etc.
	 * @param pathToFolder  Our string path to the folder
	 * @return int
	 */
	public int countFiles(String fileExtension, String pathToFolder) {
		File[] files = scanFolder(fileExtension, pathToFolder);
		if (files == null)
			return 0; // No files
		else
			return files.length;
	}

	/**
	 * Use this if you want to write text to a file. This method create the file
	 * automatically write to it and then close it. This WILL overwrite existing
	 * text. Not append!
	 * 
	 * @param absolutPath Our path such as
	 *                    "/Deeplearning2CStorage/folder/anotherfolder/fileName.txt"
	 * @param text        This can be a long string
	 * @return boolean
	 */
	public boolean writeTextTo(String absolutPath, String text) {
		try {
			createNewFile(absolutPath, true);
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(loadNewFile(absolutPath)));
			bufferWriter.write(text);
			bufferWriter.close();
			return true;
		} catch (IOException e) {
			dialogs.exception("Cannot write file:\n" + absolutPath, e);
			return false;
		}
	}
}
