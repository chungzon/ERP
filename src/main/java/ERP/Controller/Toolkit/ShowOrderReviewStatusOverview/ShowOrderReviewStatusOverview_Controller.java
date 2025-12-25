package ERP.Controller.Toolkit.ShowOrderReviewStatusOverview;

import ERP.Bean.Order.OrderReviewStatusPicture_Bean;
import ERP.Bean.Order.OrderReviewStatusRecord_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ShowOrderReviewStatusOverview_Controller {
    @FXML private Accordion accordion;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;

    private Order_Model Order_Model;

    private Stage Stage;
    private boolean isTransferWaitingOrder;
    private ObservableList<OrderReviewStatusRecord_Bean> orderReviewStatusRecordList;
    private HashMap<OrderReviewStatusPicture_Bean, HBox> orderReviewStatusPictureList;
    public ShowOrderReviewStatusOverview_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;

        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();

        orderReviewStatusPictureList = new LinkedHashMap<>();
    }
    public void setTransferWaitingOrder(Boolean isTransferWaitingOrder){
        this.isTransferWaitingOrder = isTransferWaitingOrder;
    }
    public void setOrderReviewStatusRecordList(ObservableList<OrderReviewStatusRecord_Bean> orderReviewStatusRecordList){
        this.orderReviewStatusRecordList = orderReviewStatusRecordList;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        loadOrderReviewStatusRecord();
    }
    private void loadOrderReviewStatusRecord(){
        ComponentToolKit.getOrderReferenceAccordionList(accordion).clear();
        for(int index = 0 ; index < orderReviewStatusRecordList.size() ; index++){
            OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean = orderReviewStatusRecordList.get(index);
            TitledPane TitlePane = createTitlePane(index,OrderReviewStatusRecord_Bean);
            BorderPane BorderPane = createBorderPane(OrderReviewStatusRecord_Bean);
            TitlePane.setContent(BorderPane);
            ComponentToolKit.getOrderReferenceAccordionList(accordion).add(TitlePane);
        }
    }
    private TitledPane createTitlePane(int index, OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean){
        TitledPane TitledPane = new TitledPane();
        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER,20,0,0,0,0);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        HBoxChild.add(ComponentToolKit.setLabel((index+1) + " ",-1, -1,18,null));

        Button removeButton = ComponentToolKit.setButton("移除",60,-1,16);
        removeButton.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());
        ComponentToolKit.setButtonDisable(removeButton,isTransferWaitingOrder);
        HBoxChild.add(removeButton);

        Button modifySubjectButton = ComponentToolKit.setButton("變更主旨",100,-1,16);
        ComponentToolKit.setButtonDisable(modifySubjectButton,isTransferWaitingOrder);
        modifySubjectButton.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());

        HBoxChild.add(modifySubjectButton);
        if(OrderReviewStatusRecord_Bean.getReviewStatus() == Order_Enum.ReviewStatus.待修改) {
            if(OrderReviewStatusRecord_Bean.getReviewObject() == Order_Enum.ReviewObject.商品群組)
                HBoxChild.add(ComponentToolKit.setLabel("【" + OrderReviewStatusRecord_Bean.getReviewObject().name() + "】 " + OrderReviewStatusRecord_Bean.getReviewStatus().name(), -1, -1, 18, "-fx-text-fill:red"));
            else
                HBoxChild.add(ComponentToolKit.setLabel(OrderReviewStatusRecord_Bean.getReviewStatus().name(), -1, -1, 18, "-fx-text-fill:red"));
        }else if(OrderReviewStatusRecord_Bean.getReviewStatus() == Order_Enum.ReviewStatus.審查通過) {
            if(OrderReviewStatusRecord_Bean.getReviewObject() == Order_Enum.ReviewObject.商品群組)
                HBoxChild.add(ComponentToolKit.setLabel("【" + OrderReviewStatusRecord_Bean.getReviewObject().name() + "】 " + OrderReviewStatusRecord_Bean.getReviewStatus().name(), -1, -1, 18, "-fx-text-fill:blue"));
            else
                HBoxChild.add(ComponentToolKit.setLabel(OrderReviewStatusRecord_Bean.getReviewStatus().name(), -1, -1, 18, "-fx-text-fill:blue"));
        }
        HBoxChild.add(ComponentToolKit.setLabel(OrderReviewStatusRecord_Bean.getRecord_time(),-1, -1,18,null));
        Label subjectLabel = ComponentToolKit.setLabel(OrderReviewStatusRecord_Bean.getSubject(),-1, -1,18,null);
        HBoxChild.add(subjectLabel);
        removeButton.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                if(DialogUI.ConfirmDialog("確定刪除此紀錄 ?",true,false,0,0) &&
                    DialogUI.requestAuthorization(ToolKit_Enum.Authority.管理者, ToolKit_Enum.AuthorizedLocation.修改貨單審查紀錄)){
                    if(Order_Model.deleteOrderReviewStatusRecord(OrderReviewStatusRecord_Bean.getId(),OrderReviewStatusRecord_Bean.getReview_status_id())) {
                        orderReviewStatusRecordList.remove(OrderReviewStatusRecord_Bean);
                        loadOrderReviewStatusRecord();
                        DialogUI.MessageDialog("※ 刪除成功");
                    }else
                        DialogUI.MessageDialog("※ 刪除失敗");
                }
            }
        });

        modifySubjectButton.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                String subjectText = DialogUI.TextInputDialog("編輯主旨","請輸入欲變更的主旨",null);
                if(subjectText != null){
                    if(subjectText.equals(""))
                        DialogUI.MessageDialog("※ 請輸入主旨");
                    else if(DialogUI.requestAuthorization(ToolKit_Enum.Authority.管理者, ToolKit_Enum.AuthorizedLocation.修改貨單審查紀錄)){
                        if(Order_Model.modifyOrderReviewStatusRecord(OrderReviewStatusRecord_Bean.getId(),OrderReviewStatusRecord_Bean.getReview_status_id(),subjectText,OrderReviewStatusRecord_Bean.getRecord())){
                            OrderReviewStatusRecord_Bean.setSubject(subjectText);
                            subjectLabel.setText(subjectText);
                        }else
                            DialogUI.MessageDialog("※ 修改失敗");
                    }
                }
            }
        });

        TitledPane.setGraphic(HBox);
        TitledPane.setExpanded(false);

        return TitledPane;
    }
    private BorderPane createBorderPane(OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean){
        BorderPane BorderPane = ComponentToolKit.setBorderPane(5,5,5,5);
        TextArea TextArea = ComponentToolKit.setTextArea(OrderReviewStatusRecord_Bean.getRecord(),850,300,18);
        BorderPane.setCenter(TextArea);

        HBox HBox = ComponentToolKit.setHBox(Pos.CENTER_LEFT,20,10,0,10,0);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        Button modifyRecordButton = ComponentToolKit.setButton("修改當前原因",120,-1,16);
        ComponentToolKit.setButtonDisable(modifyRecordButton,isTransferWaitingOrder);
        modifyRecordButton.setStyle("-fx-background-color:" + ToolKit.getSubBillBackgroundColor());
        modifyRecordButton.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
                if(DialogUI.ConfirmDialog("確定儲存當前原因 ?",true,false,0,0) &&
                        DialogUI.requestAuthorization(ToolKit_Enum.Authority.管理者, ToolKit_Enum.AuthorizedLocation.修改貨單審查紀錄)){
                    String record = TextArea.getText();
                    if(Order_Model.modifyOrderReviewStatusRecord(OrderReviewStatusRecord_Bean.getId(),OrderReviewStatusRecord_Bean.getReview_status_id(),OrderReviewStatusRecord_Bean.getSubject(),record)){
                        OrderReviewStatusRecord_Bean.setRecord(record);
                        DialogUI.MessageDialog("※ 修改成功");
                    }else
                        DialogUI.MessageDialog("※ 修改失敗");
                }
            }
        });

        HBoxChild.add(modifyRecordButton);
        BorderPane.setTop(HBox);

        HBox = ComponentToolKit.setHBox(Pos.CENTER_LEFT,20,10,0,10,0);
        if(OrderReviewStatusRecord_Bean.getPictureList() != null){
            HBoxChild = HBox.getChildren();
            ArrayList<OrderReviewStatusPicture_Bean> pictureList = OrderReviewStatusRecord_Bean.getPictureList();
            for (OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean : pictureList) {
                StackPane stackPane = generatePictureStackPane(orderReviewStatusPicture_Bean);
                orderReviewStatusPicture_Bean.setStackPane(stackPane);
                orderReviewStatusPictureList.put(orderReviewStatusPicture_Bean, HBox);
                HBoxChild.add(stackPane);
            }
        }
        BorderPane.setBottom(HBox);
        return BorderPane;
    }
    public void insertPictureStackPane(OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean, OrderReviewStatusPicture_Bean newOrderReviewStatusPicture_Bean){
        HBox thisUI = orderReviewStatusPictureList.get(orderReviewStatusPicture_Bean);
        newOrderReviewStatusPicture_Bean.setStackPane(generatePictureStackPane(newOrderReviewStatusPicture_Bean));
        thisUI.getChildren().add(newOrderReviewStatusPicture_Bean.getStackPane());
        orderReviewStatusPictureList.put(newOrderReviewStatusPicture_Bean, thisUI);
    }
    public void refreshPictureStackPane(OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean){
        ImageView image = (ImageView)orderReviewStatusPicture_Bean.getStackPane().getChildren().get(0);
        image.setImage(ToolKit.decodeBase64ToImage(orderReviewStatusPicture_Bean.getBase64()));
    }
    public void deletePictureStackPane(OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean){
        HBox thisUI = orderReviewStatusPictureList.get(orderReviewStatusPicture_Bean);
        thisUI.getChildren().remove(orderReviewStatusPicture_Bean.getStackPane());
        orderReviewStatusPictureList.remove(orderReviewStatusPicture_Bean);
    }
    private StackPane generatePictureStackPane(OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean){
        StackPane pictureStackPane = ComponentToolKit.setPictureStackPane(ToolKit.decodeBase64ToImage(orderReviewStatusPicture_Bean.getBase64()), 300, 350);
        pictureStackPane.setOnMouseClicked(MouseEvent -> {
            if (KeyPressed.isMouseLeftClicked(MouseEvent)) {
                CallFXML.ShowPictureEditor(Stage, orderReviewStatusPicture_Bean, this);
            }
        });
        return pictureStackPane;
    }
}
