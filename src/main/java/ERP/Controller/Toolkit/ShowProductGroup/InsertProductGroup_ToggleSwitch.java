package ERP.Controller.Toolkit.ShowProductGroup;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.ERPApplication;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class InsertProductGroup_ToggleSwitch extends Parent {
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private Accordion accordion;

    private Label switchLabel;
    private ShowProductGroup_Controller showProductGroup_Controller;
    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }
    public InsertProductGroup_ToggleSwitch(boolean defaultSwitched, ShowProductGroup_Controller showProductGroup_Controller, Label switchLabel){
        this.ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        this.showProductGroup_Controller = showProductGroup_Controller;
        this.switchLabel = switchLabel;
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
        setOnMouseClicked(MouseEvent -> {
            if(ComponentToolKit.getSelectProductList(showProductGroup_Controller.MainTableView).size() != 0){
                switchedOn.set(!switchedOn.get());
            }
        });
        switchedOn.set(defaultSwitched);
        if(!defaultSwitched) {
            setSwitchedOnStatus(false);
        }
    }
    private void setSwitchedOnStatus(boolean isOn){
        translateAnimation.setToX(isOn ? 100 - 60 : 0);
        fillAnimation.setFromValue(isOn ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.LIGHTGREEN);
        fillAnimation.setToValue(isOn ? javafx.scene.paint.Color.LIGHTGREEN : Color.RED);
        animation.play();

        switchLabel.setText(isOn ? "組合" : "非組合");
        ComponentToolKit.setLabelStyle(switchLabel,isOn ? "-fx-text-fill:red" : "-fx-text-fill:black");
        showProductGroup_Controller.setInsertProductGroupQuantityEditable(false, isOn);
        showProductGroup_Controller.calculateInsertProductGroupSinglePrice(ComponentToolKit.getSelectProductList(showProductGroup_Controller.MainTableView), isOn);
        showProductGroup_Controller.calculateInsertGroupPriceAmount();
    }
}
