package ERP.Controller.Order.SearchOrder;

import ERP.ERPApplication;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.ToolKit.CallConfig;
import com.alibaba.fastjson.JSONObject;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DefaultSearchText_ToggleSwitch extends Parent {
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    private CallConfig callConfig;
    private SearchOrder_Controller searchOrder_Controller;

    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }
    public DefaultSearchText_ToggleSwitch(boolean defaultSwitched, SearchOrder_Controller searchOrder_Controller) {
        this.callConfig = ERPApplication.ToolKit.CallConfig;
        this.searchOrder_Controller = searchOrder_Controller;

        Rectangle background = new Rectangle(60, 20);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(javafx.scene.paint.Color.WHITE);
        background.setStroke(javafx.scene.paint.Color.LIGHTGRAY);

        Circle trigger = new Circle(10);
        trigger.setCenterX(10);
        trigger.setCenterY(10);
        trigger.setFill(javafx.scene.paint.Color.WHITE);
        trigger.setStroke(javafx.scene.paint.Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState;
            setSwitchedOnStatus(isOn);
        });
        setOnMouseClicked(MouseEvent -> switchedOn.set(!switchedOn.get()));
        switchedOn.set(defaultSwitched);
        if(!defaultSwitched) {
            setSwitchedOnStatus(false);
        }
    }
    public void setSwitchedOnStatus(boolean isOn){
        translateAnimation.setToX(isOn ? 100 - 60 : 0);
        fillAnimation.setFromValue(isOn ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.LIGHTGREEN);
        fillAnimation.setToValue(isOn ? javafx.scene.paint.Color.LIGHTGREEN : Color.RED);
        animation.play();

        JSONObject systemDefaultConfig = searchOrder_Controller.systemDefaultConfigJson;
        systemDefaultConfig.put(SystemDefaultConfig_Enum.SearchOrder_DefaultSearchTextFunction.class.getSimpleName(),
                isOn ? SystemDefaultConfig_Enum.SearchOrder_DefaultSearchTextFunction.And.ordinal() : SystemDefaultConfig_Enum.SearchOrder_DefaultSearchTextFunction.Or.ordinal());
        callConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.SearchOrder_DefaultSearchTextFunction, systemDefaultConfig);
    }
}
