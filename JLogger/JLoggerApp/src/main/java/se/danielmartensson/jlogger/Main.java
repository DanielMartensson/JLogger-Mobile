package se.danielmartensson.jlogger;

import se.danielmartensson.views.ConnectServerView;
import se.danielmartensson.views.LogsView;
import se.danielmartensson.views.MeasurementsView;
import se.danielmartensson.views.PinmapView;
import se.danielmartensson.views.PlotView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.SplashView;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.Swatch;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends MobileApplication {

	public static final String LOGS_VIEW = HOME_VIEW;
	public static final String PLOT_VIEW = "Plot View";
	public static final String MEASUREMENTS_VIEW = "Measurements View";
	public static final String CONNECTSERVER_VIEW = "Connect Server View";
	public static final String PINMAP_VIEW = "Pinmap View";

	@Override
	public void init() {
		addViewFactory(MobileApplication.SPLASH_VIEW, () -> {
    		Image image = new Image("icon.png");
    		ImageView imageView  = new ImageView(image);
    	     SplashView splashView = new SplashView(imageView);
    	     splashView.setOnShown(e -> {
    	         PauseTransition pause = new PauseTransition(Duration.seconds(3));
    	         pause.setOnFinished(e1 -> splashView.hideSplashView());
    	         pause.play();
    	     }); 
    	     return splashView;
    	 });
		addViewFactory(LOGS_VIEW, () -> (View) new LogsView().getView());
		addViewFactory(PLOT_VIEW, () -> (View) new PlotView().getView());
		addViewFactory(MEASUREMENTS_VIEW, () -> (View) new MeasurementsView().getView());
		addViewFactory(CONNECTSERVER_VIEW, () -> (View) new ConnectServerView().getView());
		addViewFactory(PINMAP_VIEW, () -> (View) new PinmapView().getView());

		DrawerManager.buildDrawer(this);
	}

	@Override
	public void postInit(Scene scene) {
		Swatch.BLUE.assignTo(scene);

		scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
		((Stage) scene.getWindow()).getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
	}
}