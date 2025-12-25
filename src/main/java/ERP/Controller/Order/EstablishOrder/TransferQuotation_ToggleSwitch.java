package ERP.Controller.Order.EstablishOrder;

import ERP.Enum.Order.Order_Enum;
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

public class TransferQuotation_ToggleSwitch extends Parent {
    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));

    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    private Order_Enum.OrderObject OrderObject;
    private EstablishOrder_Controller EstablishOrder_Controller;
    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    public TransferQuotation_ToggleSwitch(boolean defaultSwitched, Order_Enum.OrderObject OrderObject, EstablishOrder_Controller EstablishOrder_Controller){
        this.OrderObject = OrderObject;
        this.EstablishOrder_Controller = EstablishOrder_Controller;
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
    private void setSwitchedOnStatus(boolean isOn){
        translateAnimation.setToX(isOn ? 100 - 60 : 0);
        fillAnimation.setFromValue(isOn ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.LIGHTGREEN);
        fillAnimation.setToValue(isOn ? javafx.scene.paint.Color.LIGHTGREEN : Color.RED);
        animation.play();
        if(OrderObject == Order_Enum.OrderObject.廠商){
            if(isOn){
                EstablishOrder_Controller.transferQuotationButton_PurchaseUI.setText("轉報價");
            }else{
                EstablishOrder_Controller.transferQuotationButton_PurchaseUI.setText("轉採購");
            }
            EstablishOrder_Controller.transferQuotation_ObjectCodeText_PurchaseUI.setText("");
            EstablishOrder_Controller.transferQuotation_ObjectNameText_PurchaseUI.setText("");
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            if(isOn){
                EstablishOrder_Controller.transferQuotationButton_ShipmentUI.setText("轉報價");
            }else{
                EstablishOrder_Controller.transferQuotationButton_ShipmentUI.setText("轉採購");
            }
            EstablishOrder_Controller.transferQuotation_ObjectCodeText_ShipmentUI.setText("");
            EstablishOrder_Controller.transferQuotation_ObjectNameText_ShipmentUI.setText("");
        }
        EstablishOrder_Controller.transferQuotation_ObjectInfoBean = null;
    }
}
