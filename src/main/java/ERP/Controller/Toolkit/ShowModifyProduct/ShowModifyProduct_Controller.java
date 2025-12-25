package ERP.Controller.Toolkit.ShowModifyProduct;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.CustomerSaleModel;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Product.Product_Enum.ManageProductInfoTableColumn;
import ERP.Model.Order.Order_Model;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashMap;

/** [Controller] Show modify product */
public class ShowModifyProduct_Controller {
    @FXML Label keyInDate_label;
    @FXML CheckBox productName_checkBox, batchPrice_checkBox, singlePrice_checkBox, pricing_checkBox;
    @FXML Button modifyProductTag_button;
    @FXML ComboBox<String> productTag_comboBox;
    @FXML TextField ProductNameText_Old, ProductNameText_New, ProductCodeText_Old, InternationalCodeText_Old, InternationalCodeText_New,FirmCodeText_Old,FirmCodeText_New,
            BatchPriceText_Old, BatchPriceText_New, SinglePriceText_Old, SinglePriceText_New, PricingText_Old, PricingText_New, VipPrice1Text_Old, VipPrice1Text_New,
            VipPrice2Text_Old, VipPrice2Text_New, VipPrice3Text_Old, VipPrice3Text_New;
    @FXML TextArea RemarkText_Old, RemarkText_New;
    @FXML Label batchPrice_label, singlePrice_label, pricing_label, vipPrice1_label, vipPrice2_label, vipPrice3_label, tooltip_Label;
    @FXML Tooltip tooltip;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private OrderObject orderObject;
    private ObjectInfo_Bean ObjectInfo_Bean;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private ERP.ToolKit.Tooltip Tooltip;
    private Order_Model Order_Model;
    private Stage MainStage,Stage;
    private OrderProduct_Bean selectOrderProduct_Bean, storeOrderProduct_Bean;

    private CustomerSaleModel customerSaleModel;
    public ShowModifyProduct_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.Tooltip = ToolKit.Tooltip;
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){  this.EstablishOrder_Controller = EstablishOrder_Controller; }
    public void setOrderObject(OrderObject orderObject){    this.orderObject = orderObject; }
    public void setObjectInfo_Bean(ObjectInfo_Bean ObjectInfo_Bean){    this.ObjectInfo_Bean = ObjectInfo_Bean; }
    public void setStoreOrderProduct_Bean(OrderProduct_Bean storeOrderProduct_Bean){    this.storeOrderProduct_Bean = storeOrderProduct_Bean;   }
    public void setSelectOrderProduct_Bean(OrderProduct_Bean selectOrderProduct_Bean){  this.selectOrderProduct_Bean = selectOrderProduct_Bean; }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){ this.Stage = Stage; }
    public void setMainStage(Stage MainStage){ this.MainStage = MainStage; }

    /** Set component of show modify product */
    public void setComponent(){
        setTextFieldLimitDigital();
        setCustomerSaleModel();
        setTextFill();
        setProductInfo();
        lockCheckBox(!((orderObject == Order_Enum.OrderObject.廠商 || orderObject == Order_Enum.OrderObject.客戶) && !EstablishOrder_Controller.isTransferWaitingOrder()));
        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.modifyProduct());
        ProductNameText_New.requestFocus();
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(InternationalCodeText_Old,false);
        ComponentToolKit.addTextFieldLimitDigital(InternationalCodeText_New,false);
        ComponentToolKit.addTextFieldLimitDouble(BatchPriceText_Old);
        ComponentToolKit.addTextFieldLimitDouble(BatchPriceText_New);
        ComponentToolKit.addTextFieldLimitDouble(SinglePriceText_Old);
        ComponentToolKit.addTextFieldLimitDouble(SinglePriceText_New);
        ComponentToolKit.addTextFieldLimitDouble(PricingText_Old);
        ComponentToolKit.addTextFieldLimitDouble(PricingText_New);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice1Text_Old);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice1Text_New);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice2Text_Old);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice2Text_New);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice3Text_Old);
        ComponentToolKit.addTextFieldLimitDouble(VipPrice3Text_New);
    }
    private void setCustomerSaleModel(){
        this.customerSaleModel = ObjectInfo_Bean.getSaleModel();
        if(orderObject == Order_Enum.OrderObject.客戶 && customerSaleModel != null){
            if(customerSaleModel == CustomerSaleModel.成本價){
                ComponentToolKit.setLabelStyle(batchPrice_label,"-fx-background-color:yellow;-fx-text-fill:red");
                ComponentToolKit.setCheckBoxSelect(batchPrice_checkBox,true);
            }else if(customerSaleModel == CustomerSaleModel.單價){
                ComponentToolKit.setLabelStyle(singlePrice_label,"-fx-background-color:yellow;-fx-text-fill:red");
                ComponentToolKit.setCheckBoxSelect(singlePrice_checkBox,true);
            }else if(customerSaleModel == CustomerSaleModel.定價){
                ComponentToolKit.setLabelStyle(pricing_label,"-fx-background-color:yellow;-fx-text-fill:red");
                ComponentToolKit.setCheckBoxSelect(pricing_checkBox,true);
            }else if(customerSaleModel == CustomerSaleModel.VipPrice1){
                ComponentToolKit.setLabelStyle(vipPrice1_label,"-fx-background-color:yellow;-fx-text-fill:red");
            }else if(customerSaleModel == CustomerSaleModel.VipPrice2){
                ComponentToolKit.setLabelStyle(vipPrice2_label,"-fx-background-color:yellow;-fx-text-fill:red");
            }else if(customerSaleModel == CustomerSaleModel.VipPrice3){
                ComponentToolKit.setLabelStyle(vipPrice3_label,"-fx-background-color:yellow;-fx-text-fill:red");
            }
        }
    }
    private void setTextFill(){
        ProductNameText_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String productNameText_Old = ProductNameText_Old.getText();
            if(!productNameText_Old.equals("")){
                if(selectOrderProduct_Bean.getProductName().equals(productNameText_Old))  ProductNameText_Old.setStyle("-fx-background-color:#bebebe; -fx-text-fill: black");
                else    ProductNameText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        ProductNameText_New.textProperty().addListener((ov, oldValue, newValue) -> {
            String productNameText_new = ProductNameText_New.getText();
            if(storeOrderProduct_Bean.getProductName().equals(productNameText_new)){
                ProductNameText_New.setStyle("-fx-text-fill: black");
                ComponentToolKit.setCheckBoxSelect(productName_checkBox,false);
                ComponentToolKit.setCheckBoxStyle(productName_checkBox,null);
            }else{
                ProductNameText_New.setStyle("-fx-text-fill: red");
                ComponentToolKit.setCheckBoxSelect(productName_checkBox,true);
                ComponentToolKit.setCheckBoxStyle(productName_checkBox,"-fx-background-color: red");
            }
        });
        InternationalCodeText_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String internationalCodeText_Old = InternationalCodeText_Old.getText();
            if(!internationalCodeText_Old.equals("")){
                if(selectOrderProduct_Bean.getInternationalCode().equals(internationalCodeText_Old))  InternationalCodeText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    InternationalCodeText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        FirmCodeText_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String firmCodeText_Old = FirmCodeText_Old.getText();
            if(!firmCodeText_Old.equals("")){
                if(selectOrderProduct_Bean.getFirmCode().equals(firmCodeText_Old))  FirmCodeText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    FirmCodeText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        BatchPriceText_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String batchPriceText_Old = BatchPriceText_Old.getText();
            if(!batchPriceText_Old.equals("")){
                if(selectOrderProduct_Bean.getBatchPrice() == ToolKit.RoundingDouble(batchPriceText_Old))  BatchPriceText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    BatchPriceText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        BatchPriceText_New.textProperty().addListener((ov, oldValue, newValue) -> {
            boolean isCustomerModel = (customerSaleModel == CustomerSaleModel.成本價);
            if(!newValue.equals("") && storeOrderProduct_Bean.getBatchPrice() == ToolKit.RoundingDouble(newValue)){
                BatchPriceText_New.setStyle("-fx-text-fill: black");
                ComponentToolKit.setCheckBoxStyle(batchPrice_checkBox,null);
                if(!isCustomerModel) {
                    ComponentToolKit.setCheckBoxSelect(batchPrice_checkBox,false);
                }
            }else{
                BatchPriceText_New.setStyle("-fx-text-fill: red");
                ComponentToolKit.setCheckBoxSelect(batchPrice_checkBox,true);
                ComponentToolKit.setCheckBoxStyle(batchPrice_checkBox,"-fx-background-color: red");
            }
        });
        SinglePriceText_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String singlePriceText_Old = SinglePriceText_Old.getText();
            if(!singlePriceText_Old.equals("")){
                if(selectOrderProduct_Bean.getSinglePrice() == ToolKit.RoundingDouble(singlePriceText_Old))  SinglePriceText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    SinglePriceText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        SinglePriceText_New.textProperty().addListener((ov, oldValue, newValue) -> {
            boolean isCustomerModel = (customerSaleModel == CustomerSaleModel.單價);
            if(!newValue.equals("") && storeOrderProduct_Bean.getSinglePrice() == ToolKit.RoundingDouble(newValue)){
                SinglePriceText_New.setStyle("-fx-text-fill: black");
                ComponentToolKit.setCheckBoxStyle(singlePrice_checkBox,null);
                if(!isCustomerModel) {
                    ComponentToolKit.setCheckBoxSelect(singlePrice_checkBox,false);
                }
            }else{
                SinglePriceText_New.setStyle("-fx-text-fill: red");
                ComponentToolKit.setCheckBoxSelect(singlePrice_checkBox,true);
                ComponentToolKit.setCheckBoxStyle(singlePrice_checkBox,"-fx-background-color: red");
            }
        });
        PricingText_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String pricingText_Old = PricingText_Old.getText();
            if(!pricingText_Old.equals("")){
                if(selectOrderProduct_Bean.getPricing() == ToolKit.RoundingDouble(pricingText_Old))  PricingText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    PricingText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        PricingText_New.textProperty().addListener((ov, oldValue, newValue) -> {
            boolean isCustomerModel = (customerSaleModel == CustomerSaleModel.定價);
            if(!newValue.equals("") && storeOrderProduct_Bean.getPricing() == ToolKit.RoundingDouble(newValue)){
                PricingText_New.setStyle("-fx-text-fill: black");
                ComponentToolKit.setCheckBoxStyle(pricing_checkBox,null);
                if(!isCustomerModel) {
                    ComponentToolKit.setCheckBoxSelect(pricing_checkBox,false);
                }
            }else{
                PricingText_New.setStyle("-fx-text-fill: red");
                ComponentToolKit.setCheckBoxSelect(pricing_checkBox,true);
                ComponentToolKit.setCheckBoxStyle(pricing_checkBox,"-fx-background-color: red");
            }
        });
        VipPrice1Text_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String vipPrice1Text_Old = VipPrice1Text_Old.getText();
            if(!vipPrice1Text_Old.equals("")){
                if(selectOrderProduct_Bean.getVipPrice1() == ToolKit.RoundingDouble(vipPrice1Text_Old))  VipPrice1Text_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    VipPrice1Text_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        VipPrice2Text_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String vipPrice2Text_Old = VipPrice2Text_Old.getText();
            if(!vipPrice2Text_Old.equals("")){
                if(selectOrderProduct_Bean.getVipPrice2() == ToolKit.RoundingDouble(vipPrice2Text_Old))  VipPrice2Text_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    VipPrice2Text_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        VipPrice3Text_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String vipPrice3Text_Old = VipPrice3Text_Old.getText();
            if(!vipPrice3Text_Old.equals("")){
                if(selectOrderProduct_Bean.getVipPrice3() == ToolKit.RoundingDouble(vipPrice3Text_Old))  VipPrice3Text_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: black");
                else    VipPrice3Text_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
        RemarkText_Old.textProperty().addListener((ov, oldValue, newValue) -> {
            String remarkText_Old = RemarkText_Old.getText();
            if(!remarkText_Old.equals("")){
                if(selectOrderProduct_Bean.getRemark().equals(remarkText_Old))  RemarkText_Old.setStyle("-fx-background-color:#bebebe; -fx-text-fill: black");
                else    RemarkText_Old.setStyle("-fx-background-color:#bebebe;-fx-text-fill: red");
            }
        });
    }
    private void setProductInfo(){
        ProductNameText_Old.setText(storeOrderProduct_Bean.getProductName());
        ProductCodeText_Old.setText(storeOrderProduct_Bean.getProductCode());
        InternationalCodeText_Old.setText(storeOrderProduct_Bean.getInternationalCode());
        FirmCodeText_Old.setText(storeOrderProduct_Bean.getFirmCode());
        BatchPriceText_Old.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getBatchPrice()));
        SinglePriceText_Old.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getSinglePrice()));
        PricingText_Old.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getPricing()));
        VipPrice1Text_Old.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getVipPrice1()));
        VipPrice2Text_Old.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getVipPrice2()));
        VipPrice3Text_Old.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getVipPrice3()));
        RemarkText_Old.setText(storeOrderProduct_Bean.getRemark());
        keyInDate_label.setText(storeOrderProduct_Bean.getKeyInDate());
        productTag_comboBox.getItems().addAll(storeOrderProduct_Bean.getProductTag());
        productTag_comboBox.getSelectionModel().selectFirst();

        ProductNameText_New.setText(storeOrderProduct_Bean.getProductName());
        InternationalCodeText_New.setText(storeOrderProduct_Bean.getInternationalCode());
        FirmCodeText_New.setText(storeOrderProduct_Bean.getFirmCode());
        BatchPriceText_New.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getBatchPrice()));
        SinglePriceText_New.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getSinglePrice()));
        PricingText_New.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getPricing()));
        VipPrice1Text_New.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getVipPrice1()));
        VipPrice2Text_New.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getVipPrice2()));
        VipPrice3Text_New.setText(ToolKit.RoundingString(true, storeOrderProduct_Bean.getVipPrice3()));
        RemarkText_New.setText(storeOrderProduct_Bean.getRemark());

        if(storeOrderProduct_Bean.getFirmCode() == null || storeOrderProduct_Bean.getFirmCode().equals(""))
            ComponentToolKit.setButtonDisable(modifyProductTag_button,true);
        else
            ComponentToolKit.setButtonDisable(modifyProductTag_button,false);
    }
    private void lockCheckBox(boolean lock){
        ComponentToolKit.setCheckBoxSelect(productName_checkBox,!lock);
        ComponentToolKit.setCheckBoxDisable(productName_checkBox,lock);
        ComponentToolKit.setCheckBoxDisable(batchPrice_checkBox,lock);
        ComponentToolKit.setCheckBoxDisable(singlePrice_checkBox,lock);
        ComponentToolKit.setCheckBoxDisable(pricing_checkBox,lock);
    }
    @FXML protected void FirmCodeText_New_KeyReleased(){
        String firmCode_New = FirmCodeText_New.getText();
        if(firmCode_New == null || firmCode_New.equals("")){
            ComponentToolKit.setButtonDisable(modifyProductTag_button,true);
        }else
            ComponentToolKit.setButtonDisable(modifyProductTag_button,false);
    }
    /** Button Mouse Clicked - 修改商品碼(標籤)設定 */
    @FXML protected void ModifyProductTagMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(!FirmCodeText_New.getText().equals(""))
                CallFXML.ShowProductTagSetting(Stage, selectOrderProduct_Bean.getISBN(), storeOrderProduct_Bean.getFirmCode(), ComponentToolKit.getComboBoxItemsStringFormat(productTag_comboBox));
            else
                DialogUI.MessageDialog("※ 請輸入商品碼");
        }
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void ModifyProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
            OrderProduct_Bean.setISBN(this.storeOrderProduct_Bean.getISBN());
            OrderProduct_Bean.setQuantity(this.storeOrderProduct_Bean.getQuantity());
            if(!ProductNameText_New.getText().equals(""))   OrderProduct_Bean.setProductName(ProductNameText_New.getText());
            else{
                DialogUI.MessageDialog("※ 請輸入品名");
                return;
            }
            OrderProduct_Bean.setInternationalCode(InternationalCodeText_New.getText());

            OrderProduct_Bean.setFirmCode(FirmCodeText_New.getText());

            if((!BatchPriceText_New.getText().equals("")))   OrderProduct_Bean.setBatchPrice(Double.parseDouble(BatchPriceText_New.getText()));
            else{
                DialogUI.MessageDialog("※ 請輸入成本");
                return;
            }
            if((!SinglePriceText_New.getText().equals("")))   OrderProduct_Bean.setSinglePrice(Double.parseDouble(SinglePriceText_New.getText()));
            else{
                DialogUI.MessageDialog("※ 請輸入單價");
                return;
            }
            if((!PricingText_New.getText().equals("")))   OrderProduct_Bean.setPricing(Double.parseDouble(PricingText_New.getText()));
            else{
                DialogUI.MessageDialog("※ 請輸入定價");
                return;
            }
            if((!VipPrice1Text_New.getText().equals("")))   OrderProduct_Bean.setVipPrice1(Double.parseDouble(VipPrice1Text_New.getText()));
            else{
                DialogUI.MessageDialog("※ 請輸入VipPrice1");
                return;
            }
            if((!VipPrice2Text_New.getText().equals("")))   OrderProduct_Bean.setVipPrice2(Double.parseDouble(VipPrice2Text_New.getText()));
            else{
                DialogUI.MessageDialog("※ 請輸入VipPrice2");
                return;
            }
            if((!VipPrice3Text_New.getText().equals("")))   OrderProduct_Bean.setVipPrice3(Double.parseDouble(VipPrice3Text_New.getText()));
            else{
                DialogUI.MessageDialog("※ 請輸入VipPrice3");
                return;
            }
            OrderProduct_Bean.setRemark(RemarkText_New.getText());

            boolean isQuotation = (orderObject == Order_Enum.OrderObject.廠商 || orderObject == Order_Enum.OrderObject.客戶) && !EstablishOrder_Controller.isTransferWaitingOrder();
            boolean isProductNameSelect = productName_checkBox.isSelected();
            boolean isBatchPriceSelect = batchPrice_checkBox.isSelected();
            boolean isSinglePriceSelect = singlePrice_checkBox.isSelected();
            boolean isPricingSelect = pricing_checkBox.isSelected();

            boolean isCustomerModelCheckBoxSelected = true;
            if(orderObject ==  OrderObject.客戶){
                isCustomerModelCheckBoxSelected = isCustomerModelCheckBoxNoneSelected();
            }
            if(!isCustomerModelCheckBoxSelected){
                DialogUI.AlarmDialog("未選擇出售模式【" + customerSaleModel.name() + "】");
            }

            String dialogString = customerSaleModel == null ? "" : "出售模式【" + customerSaleModel.name() + "】\n------------------------------------------------\n",
                    redTextFillString = null;
            if(isQuotation && (isProductNameSelect || isBatchPriceSelect || isSinglePriceSelect || isPricingSelect)){
                dialogString = dialogString + "是否確定修改(包含貨單上該商品資訊";
                redTextFillString = (isProductNameSelect ? "【品名】" : "") + (isBatchPriceSelect ? "【成本】" : "") + (isSinglePriceSelect ? "【單價】" : "") + (isPricingSelect ? "【定價】" : "");
            }else
                dialogString = dialogString + "是否確定修改 ?";

            Boolean stillModify = null;
            if(isQuotation && redTextFillString != null){
                int startIndex = dialogString.length();
                int redIndex = redTextFillString.length();
                dialogString = dialogString +  redTextFillString + ") ?";
                stillModify = DialogUI.ConfirmDialog(dialogString,true,true,startIndex,startIndex+redIndex);
            }
            if(stillModify == null)
                stillModify = DialogUI.ConfirmDialog(dialogString,true,false,0,0);
            if(stillModify){
                if(Order_Model.modifyProduct(OrderProduct_Bean)) {
                    if(isQuotation){
                        HashMap<ManageProductInfoTableColumn,Boolean> modifyInfoMap = new HashMap<>();
                        modifyInfoMap.put(ManageProductInfoTableColumn.品名,isProductNameSelect);
                        modifyInfoMap.put(ManageProductInfoTableColumn.成本價,isBatchPriceSelect);
                        modifyInfoMap.put(ManageProductInfoTableColumn.單價,isSinglePriceSelect);
                        modifyInfoMap.put(ManageProductInfoTableColumn.市價,isPricingSelect);
                        modifyInfoMap.put(ManageProductInfoTableColumn.Vip金額1,true);
                        modifyInfoMap.put(ManageProductInfoTableColumn.Vip金額2,true);
                        modifyInfoMap.put(ManageProductInfoTableColumn.Vip金額3,true);
                        EstablishOrder_Controller.refreshModifyProductInfoInTableView(OrderProduct_Bean, customerSaleModel, modifyInfoMap);
                    }
                    CloseMouseClicked();
                    DialogUI.MessageDialog("※ 修改成功");
                }else    DialogUI.MessageDialog("※ 修改失敗");
            }
        }
    }
    private boolean isCustomerModelCheckBoxNoneSelected(){
        if(customerSaleModel != null){
            if(customerSaleModel == CustomerSaleModel.成本價){
                return batchPrice_checkBox.isSelected();
            }else if(customerSaleModel == CustomerSaleModel.單價){
                return singlePrice_checkBox.isSelected();
            }else if(customerSaleModel == CustomerSaleModel.定價){
                return pricing_checkBox.isSelected();
            }else
                return true;
        }
        return false;
    }
    @FXML protected void ModifySpecificationMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowProductSpecification(MainStage, storeOrderProduct_Bean.getISBN());
            CloseMouseClicked();
        }
    }
    /** Button Mouse Clicked - 離開 */
    @FXML protected void CloseMouseClicked(){   ComponentToolKit.closeThisStage(Stage); }
}
