package se.danielmartensson.jlogger;

import static se.danielmartensson.jlogger.Main.CONNECTSERVER_VIEW;
import static se.danielmartensson.jlogger.Main.LOGS_VIEW;
import static se.danielmartensson.jlogger.Main.MEASUREMENTS_VIEW;
import static se.danielmartensson.jlogger.Main.PLOT_VIEW;
import static se.danielmartensson.jlogger.Main.PINMAP_VIEW;

import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;

import javafx.scene.image.Image;
import se.danielmartensson.tools.Dialogs;

public class DrawerManager {
	
	private static Dialogs dialogs = new Dialogs();

	public static void buildDrawer(MobileApplication app) {
		NavigationDrawer drawer = app.getDrawer();
		NavigationDrawer.Header header = new NavigationDrawer.Header("JLogger", "IoT measurement & control tool",new Avatar(30, new Image(DrawerManager.class.getResourceAsStream("/icon.png"))));
		drawer.setHeader(header);

		final Item logsItem = new ViewItem("Logs", MaterialDesignIcon.DATE_RANGE.graphic(), LOGS_VIEW,ViewStackPolicy.SKIP);
		final Item plotItem = new ViewItem("Plot", MaterialDesignIcon.SHOW_CHART.graphic(), PLOT_VIEW);
		final Item measurementsItem = new ViewItem("Measurements", MaterialDesignIcon.BUILD.graphic(),MEASUREMENTS_VIEW);
		final Item connectserverItem = new ViewItem("Connect server",MaterialDesignIcon.TRANSFER_WITHIN_A_STATION.graphic(), CONNECTSERVER_VIEW);
		final Item pinmapItem = new ViewItem("STM32 Pinmap",MaterialDesignIcon.COMPUTER.graphic(), PINMAP_VIEW);

		drawer.getItems().addAll(logsItem, plotItem, measurementsItem, connectserverItem, pinmapItem);

		if (true) { // Used to be Platform.isDesktop()
            final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, oldValue, newValue) -> {
            	if (newValue)
                	if(dialogs.question("Quit", "Do you want to exit?") == true)
                		Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                
            });
            drawer.getItems().add(quitItem);
        }
	}
}