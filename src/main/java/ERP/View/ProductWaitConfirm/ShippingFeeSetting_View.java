package ERP.View.ProductWaitConfirm;

import ERP.View.DialogUI;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.HashMap;
import java.util.Optional;

class ShippingFeeSetting_View {
    private CheckBox MailDeliver__CheckBox, ExpressDeliver_CheckBox;
    private TextField MailDeliver_TextFiled, ExpressDeliver_TextFiled;

    private HashMap<String,Integer> DeliverMap;
    ShippingFeeSetting_View(){   initComponents();   }
    private void initComponents() {
        Alert DeliverDialog = new Alert(Alert.AlertType.NONE);
        DeliverDialog.setTitle("運費設定");
        DeliverDialog.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭

        GridPane GridPane = setGridPane();
        Label DeliverMethod = setLabel("運送方式:");
        DeliverMethod.setStyle("-fx-font-weight: bold");
        GridPane.add(DeliverMethod, 0, 0);

        MailDeliver__CheckBox = setCheckBox("郵寄寄送，運費：");
        GridPane.add(MailDeliver__CheckBox, 0, 1);
        MailDeliver_TextFiled = setTextFiled();
        GridPane.add(MailDeliver_TextFiled, 1, 1);
        GridPane.add(setLabel("元 / 件"), 2, 1);

        ExpressDeliver_CheckBox = setCheckBox("宅配/快遞，運費：");
        GridPane.add(ExpressDeliver_CheckBox, 0, 2);
        ExpressDeliver_TextFiled = setTextFiled();
        GridPane.add(ExpressDeliver_TextFiled, 1, 2);
        GridPane.add(setLabel("元 / 件"), 2, 2);

        Label Remark = setLabel("※若無設定，則以預設值運費計算。");
        Remark.setTextFill(Color.web("#FF0000"));
        GridPane.add(Remark, 0, 3, 2, 1);

        DeliverDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        DeliverDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        DeliverDialog.getDialogPane().setContent(GridPane);

        Optional<ButtonType> OptionalResult = DeliverDialog.showAndWait();
        if(!OptionalResult.isPresent()) {
            System.out.println("no button has been pressed");
        }else if(OptionalResult.get() == ButtonType.OK) {
            ShippingSetting();
        }else if(OptionalResult.get() == ButtonType.CANCEL){
            System.out.println("Cancel");
        }
    }
    private void ShippingSetting(){
        if(DeliverMap == null)
            DeliverMap = new HashMap<>();
        if(MailDeliver__CheckBox.isSelected()){
            DeliverMap.put("MailDeliver",Integer.parseInt(MailDeliver_TextFiled.getText()));
        }
        if(ExpressDeliver_CheckBox.isSelected()){
            DeliverMap.put("ExpressDeliver",Integer.parseInt(ExpressDeliver_TextFiled.getText()));
        }
        DialogUI.MessageDialog("設定成功");
    }
    HashMap<String,Integer> getDeliverMap(){    return DeliverMap;  }
    private GridPane setGridPane(){
        GridPane GridPane = new GridPane();
        GridPane.setPrefSize(420,170);
        GridPane.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setAlignment(Pos.TOP_CENTER);
        GridPane.setHgap(10); //水平距離
        GridPane.setVgap(10); //垂直距離
        return GridPane;
    }
    private Label setLabel(String Title){
        Label Label = new Label(Title);

        Label.setFont(new Font("微軟正黑體", 16));
        return Label;
    }
    private CheckBox setCheckBox(String Title){
        CheckBox CheckBox = new CheckBox(Title);
        CheckBox.setFont(new Font("微軟正黑體", 16));
        return CheckBox;
    }
    private TextField setTextFiled(){
        TextField TextField = new TextField();
        TextField.setPrefWidth(50);
        TextField.setFont(new Font("微軟正黑體", 16));
        return TextField;
    }
}
