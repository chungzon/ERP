package ERP.Controller.Toolkit.ShowProductGroup;

import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.ERPApplication;
import ERP.ToolKit.ComponentToolKit;
import ERP.View.DialogUI;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ModifyProductGroup_ToggleSwitch extends Parent {
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    private ComponentToolKit ComponentToolKit;
    private ShowProductGroup_Controller showProductGroup_Controller;
    private Label switchLabel;
    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public ModifyProductGroup_ToggleSwitch(boolean defaultSwitched, ShowProductGroup_Controller showProductGroup_Controller, Label switchLabel){
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
            if(showProductGroup_Controller.getSelectTabProductGroupBean() != null) {
                boolean isOn = !switchedOn.get();
                if(DialogUI.ConfirmDialog("確定切換成【" + (isOn ? "組合" : "非組合")+ "】?",true,false,0,0)) {
                    switchedOn.set(isOn);
                    ProductGroup_Bean selectProductGroup_Bean = showProductGroup_Controller.getSelectTabProductGroupBean();
                    if(selectProductGroup_Bean != null){
                        setGroupInfo(isOn);
                        selectProductGroup_Bean.setQuantity(1);
                        selectProductGroup_Bean.setSmallQuantity(isOn ? null : 0);
                    }
                }
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
        showProductGroup_Controller.setModifyProductGroupQuantityEditable(false, isOn);
    }
    private void setGroupInfo(boolean isOn){
        ProductGroup_Bean selectProductGroup_Bean = showProductGroup_Controller.getSelectTabProductGroupBean();
        showProductGroup_Controller.calculateModifyProductGroupSinglePrice(isOn);
        selectProductGroup_Bean.setCombineGroup(isOn);
        showProductGroup_Controller.refreshMainProductQuantityAndCalculatePrice(selectProductGroup_Bean,true,true,true);
    }
}
