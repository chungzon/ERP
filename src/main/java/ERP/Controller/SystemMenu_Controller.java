package ERP.Controller;

import ERP.Bean.DataBaseServer_Bean;
import ERP.Bean.SystemSetting.Version_Bean;
import ERP.ERPApplication;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.Product_Model;
import ERP.Model.ProductWaitingConfirm.ProductWaitingConfirm_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.CallJAR;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import ERP.View.ManagePayableReceivableSystem.ManagePayableReceivableSystem_View;
import ERP.View.ManageProduct.ManageProduct_View;
import ERP.View.ManagePurchaseSystem.ManagePurchaseSystem_View;
import ERP.View.ManageShipmentSystem.ManageShipmentSystem_View;
import ERP.View.ManageStoreSale.ManageStoreSale_View;
import ERP.View.ProductWaitConfirm.ProductWaitConfirm_View;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Observer;
import java.util.Set;

public class SystemMenu_Controller {
    @FXML Label versionLabel;

    private Observer Observer;
    private Stage Stage;

    private ToolKit ToolKit;
    private CallFXML CallFXML;
    private CallJAR CallJAR;
    private KeyPressed KeyPressed;
    private Order_Model Order_Model;
    private Product_Model Product_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    private SystemSetting_Model SystemSetting_Model;
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setComponent(){
        Observer = (o, arg) -> Platform.runLater(() -> Stage.setOpacity(1));
        this.ToolKit = ERPApplication.ToolKit;
        this.CallFXML = ToolKit.CallFXML;
        this.CallJAR = ToolKit.CallJAR;
        this.KeyPressed = ToolKit.KeyPressed;
        SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
        Product_Model = ToolKit.ModelToolKit.getProductModel();
        ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        ProductWaitingConfirm_Model = ToolKit.ModelToolKit.getProductWaitingConfirmModel();
        Stage.setOnCloseRequest((WindowEvent event1) -> CallFXML.SelectDatabase(true));
    }
    public void versionSynchronize(){
        String database = SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.測試資料庫);
        if(database == null) {
            DialogUI.MessageDialog("未設定【測試資料庫】");
        }else{
            JSONObject databaseJson = JSONObject.parseObject(database);
            DataBaseServer_Bean database_bean = new DataBaseServer_Bean();
            database_bean.setDriver(databaseJson.getString("driver"));
            database_bean.setUrl(databaseJson.getString("url"));
            database_bean.setUser(databaseJson.getString("user"));
            database_bean.setPassword(databaseJson.getString("password"));
            LinkedHashMap<String, Version_Bean> testDBMap = SystemSetting_Model.getTestDBVersion(database_bean);
            if(testDBMap != null){
                String compareText = compareVersion(testDBMap);
                String synchronizeText = synchronizeVersion(testDBMap);
                if(!synchronizeText.equals("")){
                    DialogUI.AlarmDialog(synchronizeText + "\n" + compareText);
                }else if(!compareText.equals("")){
                    DialogUI.AlarmDialog(compareText);
                }
            }
        }
    }
    private String compareVersion(LinkedHashMap<String, Version_Bean> testDBMap){
        StringBuilder compareText = new StringBuilder();
        int size = 0;
        for(String version : testDBMap.keySet()){
            size++;
            Version_Bean version_bean = testDBMap.get(version);
            if(size == testDBMap.size()){
                if(!version_bean.getVersion().equals(versionLabel.getText())){
                    compareText.append("當前版本：").append(versionLabel.getText()).append("\n");
                    compareText.append("請更新 ↓ - ↓ - ↓").append("\n\n");
                    compareText.append(version).append("\n---------------------------------------------\n").append(version_bean.getVersionContent());
                }
            }
        }
        return compareText.toString();
    }
    private String synchronizeVersion(LinkedHashMap<String, Version_Bean> testDBMap){
        ObservableList<Version_Bean> allVersionList = SystemSetting_Model.getAllVersion();
        StringBuilder synchronizeText = new StringBuilder();
        for(int i = 1; i < allVersionList.size(); i++){
            Version_Bean version_bean = allVersionList.get(i);
            if(testDBMap.containsKey(version_bean.getVersion())){
                if(!version_bean.getVersionContent().equals(testDBMap.get(version_bean.getVersion()).getVersionContent())){
                    boolean modifyStatus = SystemSetting_Model.modifyVersion(version_bean, testDBMap.get(version_bean.getVersion()).getVersionContent());
                    synchronizeText.append("更新").append("【").append(modifyStatus ? "O" : "X").append(" 】");
                    synchronizeText.append(version_bean.getVersion()).append("\n");
                }
                testDBMap.remove(version_bean.getVersion());
            }else{
                boolean deleteStatus = SystemSetting_Model.deleteVersion(version_bean);
                synchronizeText.append("刪除").append("【").append(deleteStatus ? "O" : "X").append(" 】");
                synchronizeText.append(version_bean.getVersion()).append("\n");
            }
        }
        if(testDBMap.size() != 0){
            for(String version : testDBMap.keySet()){
                Version_Bean version_bean = testDBMap.get(version);
                boolean insertStatus = SystemSetting_Model.insertVersion(version_bean);
                synchronizeText.append("新增").append("【").append(insertStatus ? "O" : "X").append(" 】");
                synchronizeText.append(version).append("\n");
            }
        }
        return synchronizeText.toString();
    }
    /** Set system menu title reflection */
    public void setTitleReflection(Parent Parent){
        Node Node = Parent.lookup("#title");
        Reflection Reflection = new Reflection();
        Reflection.setFraction(0.7f);
        Reflection.setTopOpacity(0.1);
        Node.setEffect(Reflection);
    }
    public void setMenuButton(Parent Parent){
        Set<Node> ButtonSet = Parent.lookupAll(".function");
        for (Node Button : ButtonSet) {
            Button.setOnMouseClicked(e -> buttonClick(e, Stage));
        }
    }
    private void buttonClick(MouseEvent Event , Stage Stage){
        Button Button = (Button) Event.getSource();
        if ("商品資料管理".equals(Button.getText())) {
            ManageProduct_View ManageProduct_View = new ManageProduct_View();
            ManageProduct_View.addObserver(Observer);
            Stage.setOpacity(0);
        } else if ("門市銷售管理".equals(Button.getText())) {
            ManageStoreSale_View ManageStoreSale_View = new ManageStoreSale_View(ToolKit);
            ManageStoreSale_View.addObserver(Observer);
            Stage.setOpacity(0);
        } else if ("進貨管理系統".equals(Button.getText())) {
            ManagePurchaseSystem_View ManagePurchaseSystem_View = new ManagePurchaseSystem_View(ToolKit, Product_Model, Order_Model, ManagePayableReceivable_Model, ProductWaitingConfirm_Model, SystemSetting_Model);
            ManagePurchaseSystem_View.addObserver(Observer);
            Stage.setOpacity(0);
        } else if ("出貨管理系統".equals(Button.getText())) {
            ManageShipmentSystem_View ManageShipmentSystem_View =  new ManageShipmentSystem_View(ToolKit, Product_Model, Order_Model, ManagePayableReceivable_Model, ProductWaitingConfirm_Model, SystemSetting_Model);
            ManageShipmentSystem_View.addObserver(Observer);
            Stage.setOpacity(0);
        } else if ("應收應付管理".equals(Button.getText())) {
            ManagePayableReceivableSystem_View ManagePayableReceivableSystem_View = new ManagePayableReceivableSystem_View(ToolKit, Product_Model, Order_Model, ManagePayableReceivable_Model, SystemSetting_Model);
            ManagePayableReceivableSystem_View.addObserver(Observer);
            Stage.setOpacity(0);
        } else if ("商品待確認".equals(Button.getText())) {
            ProductWaitConfirm_View ProductWaitConfirm_View = new ProductWaitConfirm_View();
            ProductWaitConfirm_View.addObserver(Observer);
            Stage.setOpacity(0);
        } else if ("維修管理系統".equals(Button.getText())) {
            CallJAR.Repair_Generate();
//            Stage.setOpacity(0);
        }else if ("系統設定".equals(Button.getText())) {
            CallFXML.ShowSystemSetting(Observer);
            Stage.setOpacity(0);
        }
    }
    @FXML
    protected void versionMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowVersion();
        }
    }
}
