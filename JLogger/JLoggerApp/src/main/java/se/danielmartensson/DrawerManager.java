package se.danielmartensson;

import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import static se.danielmartensson.Main.LOGS_VIEW;
import static se.danielmartensson.Main.PLOT_VIEW;
import static se.danielmartensson.Main.MEASUREMENTS_VIEW;
import static se.danielmartensson.Main.CONNECTSERVER_VIEW;
import javafx.scene.image.Image;

public class DrawerManager {

	public static void buildDrawer(MobileApplication app) {
		NavigationDrawer drawer = app.getDrawer();

		NavigationDrawer.Header header = new NavigationDrawer.Header("JLogger", "IoT measurement & control tool",
				new Avatar(30, new Image(DrawerManager.class.getResourceAsStream("/icon.png"))));
		drawer.setHeader(header);

		final Item logsItem = new ViewItem("Logs", MaterialDesignIcon.DATE_RANGE.graphic(), LOGS_VIEW,
				ViewStackPolicy.SKIP);
		final Item plotItem = new ViewItem("Plot", MaterialDesignIcon.SHOW_CHART.graphic(), PLOT_VIEW);
		final Item measurementsItem = new ViewItem("Measurements", MaterialDesignIcon.BUILD.graphic(),
				MEASUREMENTS_VIEW);
		final Item connectserverItem = new ViewItem("Connect server",
				MaterialDesignIcon.TRANSFER_WITHIN_A_STATION.graphic(), CONNECTSERVER_VIEW);

		drawer.getItems().addAll(logsItem, plotItem, measurementsItem, connectserverItem);

		if (Platform.isDesktop()) {
			final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
			quitItem.selectedProperty().addListener((obs, ov, nv) -> {
				if (nv) {
					Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
				}
			});
			drawer.getItems().add(quitItem);
		}
	}
}