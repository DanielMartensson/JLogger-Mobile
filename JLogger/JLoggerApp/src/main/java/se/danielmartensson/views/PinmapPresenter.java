package se.danielmartensson.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class PinmapPresenter {
	
	@FXML
    private View view;

    @FXML
    private ImageView imageView;


	@FXML
	public void initialize() {

		// view smooth in
		view.setShowTransitionFactory(BounceInRightTransition::new);

		// Listener when sliding in
		view.showingProperty().addListener((obs, oldValue, newValue) -> {
			if (newValue) {
				AppBar appBar = MobileApplication.getInstance().getAppBar();
				appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().getDrawer().open()));
				appBar.setTitleText("STM32 Pinmap");
			} else {
				// When we leave
			}
		});



	}

}
