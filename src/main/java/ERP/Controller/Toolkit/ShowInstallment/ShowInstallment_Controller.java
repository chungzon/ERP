package ERP.Controller.Toolkit.ShowInstallment;

import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.Installment.Installment_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowInstallment_Controller {
    @FXML private TextField installment_textField;
    @FXML private CheckBox balancePrice_checkBox;
    @FXML private VBox vBox;
    @FXML private Button save_button, delete_button;

    private ToolKit toolKit;
    private ComponentToolKit componentToolKit;
    private KeyPressed keyPressed;
    private Order_Model order_model;

    private Stage stage;

    private EstablishOrder_Controller establishOrder_controller;
    private Order_Bean order_bean;
    private String orderPrice;
    private ArrayList<Installment_Bean> installmentList;

    private int minInstallment = 2, maxInstallment = 6;
    public ShowInstallment_Controller(){
        this.toolKit = ERPApplication.ToolKit;
        this.componentToolKit = toolKit.ComponentToolKit;
        this.keyPressed = toolKit.KeyPressed;

        this.order_model = toolKit.ModelToolKit.getOrderModel();
    }
    public void setEstablishOrder_controller(EstablishOrder_Controller establishOrder_controller){
        this.establishOrder_controller = establishOrder_controller;
    }
    public void setOrder_bean(Order_Bean order_bean){
        this.order_bean = order_bean;
    }
    public void setOrderPrice(String orderPrice){
        this.orderPrice = orderPrice;
    }
    public void setInstallmentList(ArrayList<Installment_Bean> installmentList){
        this.installmentList = installmentList;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void setComponent(){
        setTextFieldLimitDigital();
        initialInstallment();
        componentToolKit.setButtonDisable(save_button, order_bean.isCheckout().value());
        componentToolKit.setButtonDisable(delete_button, order_bean.isCheckout().value());
    }
    private void initialInstallment(){
        componentToolKit.setButtonDisable(delete_button, (installmentList == null || installmentList.size() == 0));
        if(installmentList == null){    //  不存在分期付款
            installmentList = new ArrayList<>();
            Installment_Bean first = new Installment_Bean(0);
            first.setTotalPrice(toolKit.RoundingInteger(Double.parseDouble(orderPrice)/minInstallment));
            installmentList.add(first);

            Installment_Bean second = new Installment_Bean(1);
            second.setTotalPrice(toolKit.RoundingInteger(orderPrice) - first.getTotalPrice());
            installmentList.add(second);
        }
        installment_textField.setText("" + installmentList.size());
        generateInstallment();
    }
    private void setTextFieldLimitDigital(){
        componentToolKit.addTextFieldLimitDigital(installment_textField,false);
    }
    @FXML protected void deleteInstallmentMouseClicked(MouseEvent mouseEvent){
        if(keyPressed.isMouseLeftClicked(mouseEvent)){
            if(componentToolKit.getVBoxSize(vBox) > minInstallment) {
                deleteInstallment();
                if(balancePrice_checkBox.isSelected()){
                    calculateInstallment(null);
                }
                renameInstallment();
            }else{
                DialogUI.MessageDialog("期數最小值:" + minInstallment);
            }
        }
    }
    private void deleteInstallment(){
        Installment_Bean lastInstallment_bean = installmentList.get(installmentList.size()-1);
        if(lastInstallment_bean.isCheckout()){
            DialogUI.MessageDialog("刪除失敗，期數【" + (lastInstallment_bean.getInstallment()+1) + "】已結帳");
        }else{
            vBox.getChildren().remove(vBox.getChildren().size()-1);
            installmentList.remove(installmentList.size()-1);
            installment_textField.setText("" + installmentList.size());
        }
    }

    @FXML protected void addInstallmentMouseClicked(MouseEvent mouseEvent){
        if(keyPressed.isMouseLeftClicked(mouseEvent)){
            if(componentToolKit.getVBoxSize(vBox) < maxInstallment) {
                addInstallment(false, new Installment_Bean(vBox.getChildren().size()));
                if(balancePrice_checkBox.isSelected()){
                    calculateInstallment(null);
                }
                renameInstallment();
            }else{
                DialogUI.MessageDialog("期數最大值:" + maxInstallment);
            }
        }
    }
    private void addInstallment(boolean isExist, Installment_Bean installment_bean){
        createInstallment(installment_bean);
        if(!isExist){
            installmentList.add(installment_bean);
            installment_textField.setText("" + installmentList.size());
        }
    }
    @FXML protected void installmentKeyReleased(KeyEvent keyEvent){
        if(keyPressed.isEnterKeyPressed(keyEvent)){
            String installment = installment_textField.getText();
            if(installment == null || installment.equals("")){
                generateInstallment();
                return;
            }
            if(Integer.parseInt(installment) < minInstallment){
                DialogUI.MessageDialog("期數最小值:" + minInstallment);
                installment_textField.setText(installmentList.size() != 0 ? "" + installmentList.size() : "" + minInstallment);
            }else if(Integer.parseInt(installment) > maxInstallment){
                DialogUI.MessageDialog("期數最大值:" + maxInstallment);
                installment_textField.setText(installmentList.size() != 0 ? "" + installmentList.size() : "" + maxInstallment);
            }else{
                generateInstallment();
            }
        }
    }
    private void createInstallment(Installment_Bean installment_bean){
        HBox hBox = componentToolKit.setHBox(Pos.CENTER,20,0,0,0,0);

        Label installmentLabel = componentToolKit.setLabel("期數【" + (installment_bean.getInstallment()+1) + "】",90, -1,18,null);

        double percentage = toolKit.RoundingDouble(installment_bean.getTotalPrice()/Double.parseDouble(orderPrice)*100);
        Spinner<Double> percentageSpinner = componentToolKit.setDoubleSpinner(0.0,99.9, percentage,0.1,true,16,100);
        componentToolKit.addTextFieldLimitDouble(1, percentageSpinner.getEditor());
        componentToolKit.setDoubleSpinnerDisable(percentageSpinner, installment_bean.isCheckout());

        TextField priceText = componentToolKit.setTextFiled(16,100, installment_bean.getTotalPrice());
        componentToolKit.addTextFieldLimitDigital(priceText,false);
        componentToolKit.setTextFieldDisable(priceText, installment_bean.isCheckout());

        listeningComponent(percentageSpinner, priceText, installment_bean);

        Label isCheckoutLabel = componentToolKit.setLabel(installment_bean.isCheckout() ? "已結" : "",40, -1,18, installment_bean.isCheckout() ? "-fx-text-fill: red;" : null);

        hBox.getChildren().add(installmentLabel);
        hBox.getChildren().add(percentageSpinner);
        hBox.getChildren().add(componentToolKit.setLabel("%",-1, -1,18,null));
        hBox.getChildren().add(priceText);
        hBox.getChildren().add(isCheckoutLabel);
        vBox.getChildren().add(hBox);
    }
    private void listeningComponent(Spinner<Double> percentageSpinner, TextField priceText, Installment_Bean installment_bean){
        percentageSpinner.setOnKeyReleased(keyEvent -> {
            if(keyPressed.isEnterKeyPressed(keyEvent)){
                Double percentage = percentageSpinner.getValue();
                if(percentage != null){
                    calculatePrice(priceText, percentageSpinner, installment_bean);
                    calculateInstallment(installment_bean);
                }
            }
        });
        percentageSpinner.setOnMouseClicked(mouseEvent -> {
            if(keyPressed.isMouseLeftClicked(mouseEvent) || keyPressed.isMouseRightClicked(mouseEvent)){
                calculatePrice(priceText, percentageSpinner, installment_bean);
                calculateInstallment(installment_bean);
            }
        });
        priceText.setOnKeyReleased(keyEvent -> {
            if(keyPressed.isEnterKeyPressed(keyEvent)){
                String price = priceText.getText();
                if(price == null || price.equals("")){
                    priceText.setText(toolKit.RoundingString(installment_bean.getTotalPrice()));
                    DialogUI.MessageDialog("請輸入金額");
                }else{
                    if(isInstallmentPriceMoreThanOrderPrice(installment_bean.getInstallment(), price)){
                        priceText.setText(toolKit.RoundingString(installment_bean.getTotalPrice()));
                        DialogUI.MessageDialog("超出貨單金額");
                    }else{
                        calculatePercentage(percentageSpinner, price);
                        installment_bean.setTotalPrice(toolKit.RoundingInteger(price));
                        calculateInstallment(installment_bean);
                    }
                }
            }
        });
    }
    private boolean isInstallmentPriceMoreThanOrderPrice(int installment, String price){
        double orderPrice = toolKit.RoundingInteger(this.orderPrice) - toolKit.RoundingInteger(price);
        for(Installment_Bean installment_bean : installmentList){
            if(installment_bean.getInstallment() < installment){
                orderPrice = orderPrice - installment_bean.getTotalPrice();
                if(orderPrice < 0){
                    return true;
                }
            }
        }
        return false;
    }

    private void generateInstallment(){
        if(installment_textField.getText() == null || installment_textField.getText().equals("")){
            installment_textField.setText("" + installmentList.size());
        }
        int installment = Integer.parseInt(installment_textField.getText());
        vBox.getChildren().clear();
        int needRemoteInstallmentCount = 0;
        if(installment <= installmentList.size()){
            for(int index = 0; index < installmentList.size(); index++){
                Installment_Bean installment_bean = installmentList.get(index);
                if((index+1) <= installment || installment_bean.isCheckout()){
                    //  一定要新增，修改期數為 index+1
                    addInstallment(true, installment_bean);
                    installment_textField.setText("" + (index+1));
                }else{
                    needRemoteInstallmentCount++;
                }
            }
            for(int j = 0; j < needRemoteInstallmentCount; j++){
                installmentList.remove(installmentList.size()-1);
            }
        }else{
            for(int index = 0; index < installment; index++){
                if((index+1) <= installmentList.size()){
                    Installment_Bean installment_bean = installmentList.get(index);
                    addInstallment(true, installment_bean);
                }else{
                    addInstallment(false, new Installment_Bean(index));
                }
            }
        }
        renameInstallment();
    }
    private void renameInstallment(){
        ObservableList<Node> vBoxChildren = componentToolKit.getVBoxChildren(vBox);
        for(int index = 0; index < installmentList.size(); index++){
            Installment_Bean installment_bean = installmentList.get(index);
            Label installmentLabel = (Label)componentToolKit.getHBoxChildren(((HBox)vBoxChildren.get(index))).get(0);
            if(index == (installmentList.size()-1)){
                installmentLabel.setText("尾款");
            }else{
                installmentLabel.setText("期數【" + (installment_bean.getInstallment()+1) + "】");
            }
        }
    }
    private void calculateInstallment(Installment_Bean modify_installment_bean){
        double orderPrice = toolKit.RoundingDouble(this.orderPrice);

        HashMap<CheckoutStatus, ArrayList<Installment_Bean>> checkoutMap = new HashMap<CheckoutStatus, ArrayList<Installment_Bean>>()
                {{    put(CheckoutStatus.未結帳, null);   put(CheckoutStatus.已結帳, null); }};
        for (Installment_Bean installment_bean : installmentList) {
            CheckoutStatus checkoutStatus = installment_bean.getCheckoutStatus();
            boolean isSkip = modify_installment_bean != null && installment_bean.getInstallment() < modify_installment_bean.getInstallment();
            if(!isSkip){
                if (checkoutMap.get(checkoutStatus) == null) {
                    checkoutMap.put(checkoutStatus, new ArrayList<Installment_Bean>(){{add(installment_bean);}});
                }else{
                    checkoutMap.get(checkoutStatus).add(installment_bean);
                }
            }
            if(checkoutStatus == CheckoutStatus.已結帳 || isSkip){
                orderPrice = orderPrice - installment_bean.getTotalPrice();
            }
        }
        ArrayList<Installment_Bean> noneCheckoutList = checkoutMap.get(CheckoutStatus.未結帳);

        if(noneCheckoutList != null){
            if(modify_installment_bean != null){
                noneCheckoutList.remove(modify_installment_bean);
                orderPrice = orderPrice - modify_installment_bean.getTotalPrice();
            }
            for(int index = 0; index < noneCheckoutList.size(); index++){
                Installment_Bean installment_bean = noneCheckoutList.get(index);
                if(index == noneCheckoutList.size()-1){ //  最後一筆，錢都給它
                    int price = toolKit.RoundingInteger(orderPrice);
                    installment_bean.setTotalPrice(Math.max(price, 0));
                }else{  //  錢繼續計算，總金額 / 剩餘筆數(四捨五入)
                    int tempPrice = toolKit.RoundingInteger(orderPrice/(noneCheckoutList.size()-index));
                    installment_bean.setTotalPrice(Math.max(tempPrice, 0));
                    orderPrice = orderPrice - tempPrice;
                }
                setPriceText(installment_bean);
            }
        }
        checkoutMap.clear();
    }
    private void setPriceText(Installment_Bean installment_bean){
        ObservableList<Node> vBoxChildren = componentToolKit.getVBoxChildren(vBox);
        TextField priceText = (TextField)componentToolKit.getHBoxChildren(((HBox)vBoxChildren.get(installment_bean.getInstallment()))).get(3);
        String price = toolKit.RoundingString(installment_bean.getTotalPrice());
        priceText.setText(price);

        Spinner<Double> percentageSpinner = (Spinner<Double>) componentToolKit.getHBoxChildren(((HBox)vBoxChildren.get(installment_bean.getInstallment()))).get(1);
        calculatePercentage(percentageSpinner, price);
    }
    private void calculatePrice(TextField priceText, Spinner<Double> percentageSpinner, Installment_Bean installment_bean){
        String price = toolKit.RoundingString(Double.parseDouble(orderPrice)*percentageSpinner.getValue()/100);
        if(isInstallmentPriceMoreThanOrderPrice(installment_bean.getInstallment(), price)){
            calculatePercentage(percentageSpinner, toolKit.RoundingString(installment_bean.getTotalPrice()));
            DialogUI.MessageDialog("超出貨單金額");
        }else{
            installment_bean.setTotalPrice(toolKit.RoundingInteger(price));
            priceText.setText(price);
        }
    }
    private void calculatePercentage(Spinner<Double> percentageSpinner, String price){
        double percentage = toolKit.RoundingDouble(toolKit.RoundingInteger(price)/Double.parseDouble(orderPrice)*100);
        percentageSpinner.getValueFactory().setValue(percentage);
    }
    @FXML protected void saveMouseClicked(MouseEvent mouseEvent){
        if(keyPressed.isMouseLeftClicked(mouseEvent)){
            if(checkData()){
                if(isOrderPriceMatch()){
                    if(order_model.updateInstallment(false, order_bean.getOrderID(), installmentList)){
                        componentToolKit.closeThisStage(stage);
                        order_bean.setExistInstallment(true);
                        establishOrder_controller.setEstablishPayReceiveMenuItemDisable();
                        DialogUI.MessageDialog("儲存成功");
                    }else{
                        DialogUI.MessageDialog("儲存失敗");
                    }
                }
            }else{
                //  非連續的已結帳資料
                DialogUI.AlarmDialog("發生錯誤，請聯絡工程師。");
            }
        }
    }
    private boolean checkData(){
        boolean isCheckout = false;
        for(int index = installmentList.size()-1 ; index >= 0; index--){
            Installment_Bean installment_bean = installmentList.get(index);
            if(isCheckout && !installment_bean.isCheckout()){
                return false;
            }
            if(installment_bean.isCheckout()){
                isCheckout = true;
            }
        }
        return true;
    }
    private boolean isOrderPriceMatch(){
        int totalPrice = 0;
        for(Installment_Bean installment_bean : installmentList){
            totalPrice += installment_bean.getTotalPrice();
        }
        boolean isMatch = toolKit.RoundingInteger(orderPrice) == totalPrice;
        if(!isMatch){
            int differentPrice = totalPrice-toolKit.RoundingInteger(orderPrice);
            String alertText = "貨單總金額： $" + orderPrice + "\n分期總金額： $" + totalPrice + " (" + (differentPrice > 0 ? "+" : "-") + Math.abs(differentPrice)+ ")";
            DialogUI.AlarmDialog(alertText);
        }
        return isMatch;
    }
    @FXML protected void deleteMouseClicked(MouseEvent mouseEvent){
        if(keyPressed.isMouseLeftClicked(mouseEvent)){
            if(isExistCheckoutData()){
                DialogUI.MessageDialog("刪除失敗，存在【已結帳】款項");
            }else if(DialogUI.ConfirmDialog("確定要刪除【整份款項】?",true,false,0,0)){
                if(order_model.updateInstallment(true, order_bean.getOrderID(), null)){
                    componentToolKit.closeThisStage(stage);
                    order_bean.setExistInstallment(false);
                    establishOrder_controller.setEstablishPayReceiveMenuItemDisable();
                    DialogUI.MessageDialog("刪除成功");
                }else{
                    DialogUI.MessageDialog("刪除失敗");
                }
            }
        }
    }
    private boolean isExistCheckoutData(){
        for (Installment_Bean installment_bean : installmentList) {
            if (installment_bean.isCheckout()) {
                return true;
            }
        }
        return false;
    }
    @FXML protected void closeMouseClicked(MouseEvent mouseEvent){
        if(keyPressed.isMouseLeftClicked(mouseEvent)){
            componentToolKit.closeThisStage(stage);
        }
    }
}
