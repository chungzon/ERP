package ERP.Controller.Toolkit.ShowBigGoPriceSetting;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.ObjectSearchColumn;
import ERP.Enum.Product.Product_Enum.BigGoPriceSettingColumn;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.Order.Order_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ShowBigGoPriceSetting_Controller {
    @FXML
    TextField manufacturerCode_textField, manufacturerName_textField;
    @FXML
    Spinner<Double> singlePrice_percentagePrice_spinner, pricing_percentagePrice_spinner, vipPrice1_percentagePrice_spinner, vipPrice2_percentagePrice_spinner, vipPrice3_percentagePrice_spinner;
    @FXML
    RadioButton singlePrice_maxPrice_radioButton, singlePrice_minPrice_radioButton, singlePrice_averagePrice_radioButton, singlePrice_percentagePrice_radioButton,
            pricing_maxPrice_radioButton, pricing_minPrice_radioButton, pricing_averagePrice_radioButton, pricing_percentagePrice_radioButton,
            vipPrice1_maxPrice_radioButton, vipPrice1_minPrice_radioButton, vipPrice1_averagePrice_radioButton, vipPrice1_percentagePrice_radioButton,
            vipPrice2_maxPrice_radioButton, vipPrice2_minPrice_radioButton, vipPrice2_averagePrice_radioButton, vipPrice2_percentagePrice_radioButton,
            vipPrice3_maxPrice_radioButton, vipPrice3_minPrice_radioButton, vipPrice3_averagePrice_radioButton, vipPrice3_percentagePrice_radioButton;
    @FXML
    HBox taxStatus_HBox;

    private ToolKit toolKit;
    private ComponentToolKit componentToolKit;
    private CallFXML callFXML;
    private KeyPressed keyPressed;

    private Order_Model order_Model;
    private SystemSetting_Model systemSetting_Model;

    private Stage stage;
    private JSONObject storePriceParameterJson;
    private ObjectInfo_Bean objectInfo_Bean;

    private TaxStatus_ToggleSwitch taxStatus_toggleSwitch = null;

    public ShowBigGoPriceSetting_Controller() {
        this.toolKit = ERPApplication.ToolKit;
        this.componentToolKit = toolKit.ComponentToolKit;
        this.callFXML = toolKit.CallFXML;
        this.keyPressed = toolKit.KeyPressed;

        this.order_Model = toolKit.ModelToolKit.getOrderModel();
        this.systemSetting_Model = toolKit.ModelToolKit.getSystemSettingModel();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setStorePriceParameterJson(JSONObject storePriceParameterJson) {
        this.storePriceParameterJson = storePriceParameterJson;
    }

    public void setComponent() {
        taxStatus_toggleSwitch = createTaxStatusSwitchButton();

        initialSpinner();
        initialPriceParameter();
    }

    private void initialPriceParameter() {
        if (storePriceParameterJson == null) {
            storePriceParameterJson = new JSONObject();
            return;
        }

        String manufacturerCode = storePriceParameterJson.getString(BigGoPriceSettingColumn.manufacturerCode.name());
        if (manufacturerCode != null) {
            searchManufacturer(ObjectSearchColumn.廠商編號, manufacturerCode);
        }
        setPriceKey(storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.singlePrice.name()),
                singlePrice_maxPrice_radioButton, singlePrice_minPrice_radioButton, singlePrice_averagePrice_radioButton, singlePrice_percentagePrice_radioButton, singlePrice_percentagePrice_spinner);
        setPriceKey(storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.pricing.name()),
                pricing_maxPrice_radioButton, pricing_minPrice_radioButton, pricing_averagePrice_radioButton, pricing_percentagePrice_radioButton, pricing_percentagePrice_spinner);
        setPriceKey(storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.vipPrice1.name()),
                vipPrice1_maxPrice_radioButton, vipPrice1_minPrice_radioButton, vipPrice1_averagePrice_radioButton, vipPrice1_percentagePrice_radioButton, vipPrice1_percentagePrice_spinner);
        setPriceKey(storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.vipPrice2.name()),
                vipPrice2_maxPrice_radioButton, vipPrice2_minPrice_radioButton, vipPrice2_averagePrice_radioButton, vipPrice2_percentagePrice_radioButton, vipPrice2_percentagePrice_spinner);
        setPriceKey(storePriceParameterJson.getJSONObject(BigGoPriceSettingColumn.vipPrice3.name()),
                vipPrice3_maxPrice_radioButton, vipPrice3_minPrice_radioButton, vipPrice3_averagePrice_radioButton, vipPrice3_percentagePrice_radioButton, vipPrice3_percentagePrice_spinner);

        taxStatus_toggleSwitch.switchedOnProperty().set(storePriceParameterJson.getBoolean(BigGoPriceSettingColumn.taxStatus.name()));
    }
    private void setPriceKey(JSONObject jsonObject, RadioButton maxPrice, RadioButton minPrice, RadioButton averagePrice, RadioButton percentagePrice, Spinner<Double> spinner){
        if(jsonObject.containsKey(BigGoPriceSettingColumn.priceKey.max.name())){
            maxPrice.setSelected(true);
        }else if(jsonObject.containsKey(BigGoPriceSettingColumn.priceKey.min.name())){
            minPrice.setSelected(true);
        }else if(jsonObject.containsKey(BigGoPriceSettingColumn.priceKey.average.name())){
            averagePrice.setSelected(true);
        }else if(jsonObject.containsKey(BigGoPriceSettingColumn.priceKey.percentage.name())){
            clearSpinner(spinner,false);
            percentagePrice.setSelected(true);
            componentToolKit.setSpinnerDoubleValue(spinner, jsonObject.getDouble(BigGoPriceSettingColumn.priceKey.percentage.name()));
        }
    }

    private void initialSpinner() {
        componentToolKit.addSpinnerLimitDouble(singlePrice_percentagePrice_spinner, 2);
        componentToolKit.setDoubleSpinnerValueFactory(singlePrice_percentagePrice_spinner, 0.01, 100, 1.00, 0.01);
        componentToolKit.addSpinnerLimitDouble(pricing_percentagePrice_spinner, 2);
        componentToolKit.setDoubleSpinnerValueFactory(pricing_percentagePrice_spinner, 0.01, 100, 1.00, 0.01);
        componentToolKit.addSpinnerLimitDouble(vipPrice1_percentagePrice_spinner, 2);
        componentToolKit.setDoubleSpinnerValueFactory(vipPrice1_percentagePrice_spinner, 0.01, 100, 1.00, 0.01);
        componentToolKit.addSpinnerLimitDouble(vipPrice2_percentagePrice_spinner, 2);
        componentToolKit.setDoubleSpinnerValueFactory(vipPrice2_percentagePrice_spinner, 0.01, 100, 1.00, 0.01);
        componentToolKit.addSpinnerLimitDouble(vipPrice3_percentagePrice_spinner, 2);
        componentToolKit.setDoubleSpinnerValueFactory(vipPrice3_percentagePrice_spinner, 0.01, 100, 1.00, 0.01);
    }

    @FXML
    protected void manufacturerCodeKeyPressed(KeyEvent keyEvent) {
        if (keyPressed.isEnterKeyPressed(keyEvent)) {
            String manufacturerCode = manufacturerCode_textField.getText();
            if (manufacturerCode.equals("")) {
                DialogUI.MessageDialog("請輸入廠商編號");
                return;
            }
            searchManufacturer(ObjectSearchColumn.廠商編號, manufacturerCode);
        }
    }

    @FXML
    protected void manufacturerNameKeyPressed(KeyEvent keyEvent) {
        if (keyPressed.isEnterKeyPressed(keyEvent)) {
            String manufacturerName = manufacturerName_textField.getText();
            if (manufacturerName.equals("")) {
                DialogUI.MessageDialog("請輸入廠商編號");
                return;
            }
            searchManufacturer(ObjectSearchColumn.廠商名稱, manufacturerName);
        }
    }

    private void searchManufacturer(ObjectSearchColumn objectSearchColumn, String manufacturerInfo) {
        OrderObject orderObject = OrderObject.廠商;
        ObservableList<ObjectInfo_Bean> ObjectList = order_Model.getObjectFromSearchColumn(orderObject, objectSearchColumn, manufacturerInfo);
        if (ObjectList.size() == 1) {
            setManufacturerInfo(ObjectList.get(0));
        } else if (ObjectList.size() > 1)
            callFXML.ShowObject(stage, orderObject, ObjectList, true, Order_Enum.ShowObjectSource.待確認_Store參數, this);
        else {
            manufacturerCode_textField.setText(this.objectInfo_Bean == null ? "" : this.objectInfo_Bean.getObjectID());
            manufacturerName_textField.setText(this.objectInfo_Bean == null ? "" : this.objectInfo_Bean.getObjectName());
            DialogUI.MessageDialog("※ 查無相關" + orderObject.name());
        }
    }

    public void setManufacturerInfo(ObjectInfo_Bean objectInfo_Bean) {
        this.objectInfo_Bean = objectInfo_Bean;
        manufacturerCode_textField.setText(objectInfo_Bean == null ? "" : objectInfo_Bean.getObjectID());
        manufacturerName_textField.setText(objectInfo_Bean == null ? "" : objectInfo_Bean.getObjectName());
    }


    @FXML
    protected void singleMaxPriceOnAction() {
        if (singlePrice_maxPrice_radioButton.isSelected()) {
            clearSpinner(singlePrice_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void singleMinPriceOnAction() {
        if (singlePrice_minPrice_radioButton.isSelected()) {
            clearSpinner(singlePrice_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void singleAveragePriceOnAction() {
        if (singlePrice_averagePrice_radioButton.isSelected()) {
            clearSpinner(singlePrice_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void singlePercentagePriceOnAction() {
        clearSpinner(singlePrice_percentagePrice_spinner,false);
    }

    @FXML
    protected void pricingMaxPriceOnAction() {
        if (pricing_maxPrice_radioButton.isSelected()) {
            clearSpinner(pricing_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void pricingMinPriceOnAction() {
        if (pricing_minPrice_radioButton.isSelected()) {
            clearSpinner(pricing_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void pricingAveragePriceOnAction() {
        if (pricing_averagePrice_radioButton.isSelected()) {
            clearSpinner(pricing_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void pricingPercentagePriceOnAction() {
        clearSpinner(pricing_percentagePrice_spinner,false);
    }

    @FXML
    protected void vipPrice1MaxPriceOnAction() {
        if (vipPrice1_maxPrice_radioButton.isSelected()) {
            clearSpinner(vipPrice1_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice1MinPriceOnAction() {
        if (vipPrice1_minPrice_radioButton.isSelected()) {
            clearSpinner(vipPrice1_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice1AveragePriceOnAction() {
        if (vipPrice1_averagePrice_radioButton.isSelected()) {
            clearSpinner(vipPrice1_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice1PercentagePriceOnAction() {
        clearSpinner(vipPrice1_percentagePrice_spinner,false);
    }

    @FXML
    protected void vipPrice2MaxPriceOnAction() {
        if (vipPrice2_maxPrice_radioButton.isSelected()) {
            clearSpinner(vipPrice2_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice2MinPriceOnAction() {
        if (vipPrice2_minPrice_radioButton.isSelected()) {
            clearSpinner(vipPrice2_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice2AveragePriceOnAction() {
        if (vipPrice2_averagePrice_radioButton.isSelected()) {
            clearSpinner(vipPrice2_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice2PercentagePriceOnAction() {
        clearSpinner(vipPrice2_percentagePrice_spinner,false);
    }

    @FXML
    protected void vipPrice3MaxPriceOnAction() {
        if (vipPrice3_maxPrice_radioButton.isSelected()) {
            clearSpinner(vipPrice3_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice3MinPriceOnAction() {
        if (vipPrice3_minPrice_radioButton.isSelected()) {
            clearSpinner(vipPrice3_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice3AveragePriceOnAction() {
        if (vipPrice3_averagePrice_radioButton.isSelected()) {
            clearSpinner(vipPrice3_percentagePrice_spinner,true);
        }
    }

    @FXML
    protected void vipPrice3PercentagePriceOnAction() {
        clearSpinner(vipPrice3_percentagePrice_spinner,false);
    }

    @FXML protected void clearManufacturerMouseClicked(MouseEvent MouseEvent) {
        if(keyPressed.isMouseLeftClicked(MouseEvent)) {
            setManufacturerInfo(null);
        }
    }
    @FXML protected void saveMouseClicked(MouseEvent MouseEvent) {
        if(keyPressed.isMouseLeftClicked(MouseEvent)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(BigGoPriceSettingColumn.manufacturerCode.name(), objectInfo_Bean == null ? null : objectInfo_Bean.getObjectID());

            BigGoPriceSettingColumn.priceKey singlePriceKey =
                    getPriceKey(singlePrice_maxPrice_radioButton, singlePrice_minPrice_radioButton, singlePrice_averagePrice_radioButton, singlePrice_percentagePrice_radioButton);
            jsonObject.put(BigGoPriceSettingColumn.singlePrice.name(),
                    new JSONObject(){{  put(singlePriceKey.name(), singlePriceKey != BigGoPriceSettingColumn.priceKey.percentage ? -1 : singlePrice_percentagePrice_spinner.getValue());  }});

            BigGoPriceSettingColumn.priceKey pricingKey =
                    getPriceKey(pricing_maxPrice_radioButton, pricing_minPrice_radioButton, pricing_averagePrice_radioButton, pricing_percentagePrice_radioButton);
            jsonObject.put(BigGoPriceSettingColumn.pricing.name(),
                    new JSONObject(){{  put(pricingKey.name(), pricingKey != BigGoPriceSettingColumn.priceKey.percentage ? -1 : pricing_percentagePrice_spinner.getValue());  }});

            BigGoPriceSettingColumn.priceKey vipPrice1Key =
                    getPriceKey(vipPrice1_maxPrice_radioButton, vipPrice1_minPrice_radioButton, vipPrice1_averagePrice_radioButton, vipPrice1_percentagePrice_radioButton);
            jsonObject.put(BigGoPriceSettingColumn.vipPrice1.name(),
                    new JSONObject(){{  put(vipPrice1Key.name(), vipPrice1Key != BigGoPriceSettingColumn.priceKey.percentage ? -1 : vipPrice1_percentagePrice_spinner.getValue());  }});

            BigGoPriceSettingColumn.priceKey vipPrice2Key =
                    getPriceKey(vipPrice2_maxPrice_radioButton, vipPrice2_minPrice_radioButton, vipPrice2_averagePrice_radioButton, vipPrice2_percentagePrice_radioButton);
            jsonObject.put(BigGoPriceSettingColumn.vipPrice2.name(),
                    new JSONObject(){{  put(vipPrice2Key.name(), vipPrice2Key != BigGoPriceSettingColumn.priceKey.percentage ? -1 : vipPrice2_percentagePrice_spinner.getValue());  }});

            BigGoPriceSettingColumn.priceKey vipPrice3Key =
                    getPriceKey(vipPrice3_maxPrice_radioButton, vipPrice3_minPrice_radioButton, vipPrice3_averagePrice_radioButton, vipPrice3_percentagePrice_radioButton);
            jsonObject.put(BigGoPriceSettingColumn.vipPrice3.name(),
                    new JSONObject(){{  put(vipPrice3Key.name(), vipPrice3Key != BigGoPriceSettingColumn.priceKey.percentage ? -1 : vipPrice3_percentagePrice_spinner.getValue());  }});

            jsonObject.put(BigGoPriceSettingColumn.taxStatus.name(), taxStatus_toggleSwitch.switchedOnProperty().get());
            if(systemSetting_Model.updateSystemSettingConfigValue(SystemSetting_Enum.SystemSettingConfig.待確認_Store參數, jsonObject.toString())){
                storePriceParameterJson.put(BigGoPriceSettingColumn.manufacturerCode.name(), jsonObject.getString(BigGoPriceSettingColumn.manufacturerCode.name()));
                storePriceParameterJson.put(BigGoPriceSettingColumn.singlePrice.name(), jsonObject.getJSONObject(BigGoPriceSettingColumn.singlePrice.name()));
                storePriceParameterJson.put(BigGoPriceSettingColumn.pricing.name(), jsonObject.getJSONObject(BigGoPriceSettingColumn.pricing.name()));
                storePriceParameterJson.put(BigGoPriceSettingColumn.vipPrice1.name(), jsonObject.getJSONObject(BigGoPriceSettingColumn.vipPrice1.name()));
                storePriceParameterJson.put(BigGoPriceSettingColumn.vipPrice2.name(), jsonObject.getJSONObject(BigGoPriceSettingColumn.vipPrice2.name()));
                storePriceParameterJson.put(BigGoPriceSettingColumn.vipPrice3.name(), jsonObject.getJSONObject(BigGoPriceSettingColumn.vipPrice3.name()));
                storePriceParameterJson.put(BigGoPriceSettingColumn.taxStatus.name(), jsonObject.getBoolean(BigGoPriceSettingColumn.taxStatus.name()));
                DialogUI.MessageDialog("※ 設定成功");
                closeStage();
            }else
                DialogUI.MessageDialog("※ 設定失敗");
        }
    }
    private BigGoPriceSettingColumn.priceKey getPriceKey(RadioButton maxPrice, RadioButton minPrice, RadioButton averagePrice, RadioButton percentagePrice){
        if(maxPrice.isSelected()){
            return BigGoPriceSettingColumn.priceKey.max;
        }else if(minPrice.isSelected()){
            return BigGoPriceSettingColumn.priceKey.min;
        }else if(averagePrice.isSelected()){
            return BigGoPriceSettingColumn.priceKey.average;
        }else {
            return BigGoPriceSettingColumn.priceKey.percentage;
        }
    }

    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent))
            closeStage();
    }
    private void closeStage(){
        componentToolKit.closeThisStage(stage);
    }

    private void clearSpinner(Spinner<Double> spinner, boolean disable){
        componentToolKit.setSpinnerDoubleValue(spinner,1.00);
        componentToolKit.setDoubleSpinnerDisable(spinner, disable);
    }
    private TaxStatus_ToggleSwitch createTaxStatusSwitchButton(){
        TaxStatus_ToggleSwitch toggle = new TaxStatus_ToggleSwitch(false, this);
        toggle.setTranslateX(0);
        toggle.setTranslateY(0);
        taxStatus_HBox.getChildren().add(1, toggle);
        return toggle;
    }
}
