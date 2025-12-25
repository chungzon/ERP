package ERP.Bean.ManagePayableReceivable;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.SystemSetting.IAECrawlerBelong_Bean;
import com.alibaba.fastjson.JSONObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

import java.util.ArrayList;

public class IAECrawlerAccount_Bean {
    private int id,exportQuotation_id;
    private String Belong;
    private boolean isExportQuotation,exportQuotation_defaultSelect;

    private ObjectInfo_Bean ObjectInfo_Bean;

    private ArrayList<IAECrawlerBelong_Bean> IAECrawlerBelongList;
    private SimpleStringProperty ObjectID, ObjectName, Account, Password, exportQuotation_ManufacturerNickName;
    private String exportQuotation_ManufacturerAllName;

    private CheckBox theSameANoneInvoice_CheckBox;
    private JSONObject templateFormatJsonObject;

    public IAECrawlerAccount_Bean(){
        theSameANoneInvoice_CheckBox = new CheckBox();
        IAECrawlerBelongList = new ArrayList<>();
        ObjectID = new SimpleStringProperty();
        ObjectName = new SimpleStringProperty();
        Account = new SimpleStringProperty();
        Password = new SimpleStringProperty();
        exportQuotation_ManufacturerNickName = new SimpleStringProperty();
    }

    public CheckBox getTheSameANoneInvoice_CheckBox() { return theSameANoneInvoice_CheckBox;    }
    public void setTheSameANoneInvoice_CheckBox(CheckBox theSameANoneInvoice_CheckBox) {    this.theSameANoneInvoice_CheckBox = theSameANoneInvoice_CheckBox;   }
    public boolean isTheSameAsNoneInvoice() {   return theSameANoneInvoice_CheckBox.isSelected();  }
    public void setTheSameAsNoneInvoice(boolean theSameAsNoneInvoice) { theSameANoneInvoice_CheckBox.setSelected(theSameAsNoneInvoice);    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectID() {   return ObjectID.get();  }
    public SimpleStringProperty ObjectIDProperty() {
        return ObjectID;
    }
    private void setObjectID(String objectID) { this.ObjectID.set(objectID);    }

    public String getObjectName() { return ObjectName.get();    }
    public SimpleStringProperty ObjectNameProperty() {  return ObjectName;  }
    private void setObjectName(String objectName) { this.ObjectName.set(objectName);    }

    public ObjectInfo_Bean getObjectInfo_Bean(){
        return this.ObjectInfo_Bean;
    }
    public void setObjectInfo_Bean(ObjectInfo_Bean objectInfo_Bean) {
        ObjectInfo_Bean = objectInfo_Bean;
        setObjectID(ObjectInfo_Bean.getObjectID());
        setObjectName(ObjectInfo_Bean.getObjectName());
    }

    public String getAccount() {
        return Account.get();
    }

    public SimpleStringProperty AccountProperty() {
        return Account;
    }

    public void setAccount(String account) {
        this.Account.set(account);
    }

    public String getPassword() {
        return Password.get();
    }

    public SimpleStringProperty PasswordProperty() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password.set(password);
    }

    public String getBelong() { return Belong;  }
    public void setBelong(String Belong) {
        this.Belong = Belong;
    }

    public ArrayList<IAECrawlerBelong_Bean> getIAECrawlerBelongList() { return IAECrawlerBelongList;    }
    public void setIAECrawlerBelongList(ArrayList<IAECrawlerBelong_Bean> IAECrawlerBelongList) {    this.IAECrawlerBelongList = IAECrawlerBelongList;   }

    public int getExportQuotation_id() {
        return exportQuotation_id;
    }

    public void setExportQuotation_id(int exportQuotation_id) {
        this.exportQuotation_id = exportQuotation_id;
    }

    public String getExportQuotation_ManufacturerNickName() {   return exportQuotation_ManufacturerNickName.get();  }
    public SimpleStringProperty ExportQuotation_ManufacturerNickNameProperty() {  return exportQuotation_ManufacturerNickName;  }
    public void setExportQuotation_ManufacturerNickName(String exportQuotation_ManufacturerNickName) {  this.exportQuotation_ManufacturerNickName.set(exportQuotation_ManufacturerNickName);    }

    public String getExportQuotation_ManufacturerAllName() {    return exportQuotation_ManufacturerAllName; }
    public void setExportQuotation_ManufacturerAllName(String exportQuotation_ManufacturerAllName) {    this.exportQuotation_ManufacturerAllName = exportQuotation_ManufacturerAllName; }

    public boolean isExportQuotation() {
        return isExportQuotation;
    }

    public void setExportQuotation(boolean exportQuotation) {
        isExportQuotation = exportQuotation;
    }

    public boolean isExportQuotation_defaultSelect() {
        return exportQuotation_defaultSelect;
    }

    public void setExportQuotation_defaultSelect(boolean exportQuotation_defaultSelect) {
        this.exportQuotation_defaultSelect = exportQuotation_defaultSelect;
    }

    public JSONObject getTemplateFormatJsonObject() {   return templateFormatJsonObject;    }
    public void setTemplateFormatJsonObject(JSONObject templateFormatJsonObject) {  this.templateFormatJsonObject = templateFormatJsonObject;   }
}
