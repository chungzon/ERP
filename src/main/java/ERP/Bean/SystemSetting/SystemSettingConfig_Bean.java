package ERP.Bean.SystemSetting;

import com.alibaba.fastjson.JSONObject;

/** [ERP.Bean] The config in system setting */
public class SystemSettingConfig_Bean {
    private String DefaultPrinter,ConsignmentPrinter,LabelPrinter;
    private String ManufacturerIDLength;
    private String CustomerIDCharacter,CustomerIDLength, CustomerIDAreaLength, OrderNumberLength;
    private String ProjectCodeDefaultUrl;
    private String InitialCheckNumber, NowCheckNumber;
    private JSONObject ExportCompanyFormat, exportTemplateName;
    private JSONObject testDatabase;

    public void setDefaultPrinter(String DefaultPrinter){   this.DefaultPrinter = DefaultPrinter;   }
    public String getDefaultPrinter(){  return DefaultPrinter;  }
    public void setConsignmentPrinter(String ConsignmentPrinter){   this.ConsignmentPrinter = ConsignmentPrinter;   }
    public String getConsignmentPrinter(){  return ConsignmentPrinter;  }
    public void setLabelPrinter(String LabelPrinter){   this.LabelPrinter = LabelPrinter;   }
    public String getLabelPrinter(){    return LabelPrinter;    }
    public String getManufacturerIDLength() {   return ManufacturerIDLength;    }
    public void setManufacturerIDLength(String ManufacturerIDLength) {  this.ManufacturerIDLength = ManufacturerIDLength;    }
    public void setCustomerIDCharacter(String CustomerIDCharacter){  this.CustomerIDCharacter = CustomerIDCharacter; }
    public String getCustomerIDCharacter(){   return CustomerIDCharacter;   }
    public String getCustomerIDLength() {   return CustomerIDLength;    }
    public void setCustomerIDLength(String CustomerIDLength) {  this.CustomerIDLength = CustomerIDLength;    }
    public String getCustomerIDAreaLength() {   return CustomerIDAreaLength;    }
    public void setCustomerIDAreaLength(String CustomerIDAreaLength) {  this.CustomerIDAreaLength = CustomerIDAreaLength;    }
    public String getOrderNumberLength() {  return OrderNumberLength;   }
    public void setOrderNumberLength(String orderNumberLength) {    OrderNumberLength = orderNumberLength;  }
    public String getProjectCodeDefaultUrl() {  return ProjectCodeDefaultUrl;   }
    public void setProjectCodeDefaultUrl(String projectCodeDefaultUrl) {    ProjectCodeDefaultUrl = projectCodeDefaultUrl;  }
    public String getInitialCheckNumber() { return InitialCheckNumber;  }
    public void setInitialCheckNumber(String InitialCheckNumber) {  this.InitialCheckNumber = InitialCheckNumber;   }
    public String getNowCheckNumber() { return NowCheckNumber;  }
    public void setNowCheckNumber(String NowCheckNumber) {  this.NowCheckNumber = NowCheckNumber;    }
    public JSONObject getExportCompanyFormat() {    return ExportCompanyFormat; }
    public void setExportCompanyFormat(JSONObject exportCompanyFormat) {    ExportCompanyFormat = exportCompanyFormat;  }
    public JSONObject getExportTemplateName() { return exportTemplateName;  }
    public void setExportTemplateName(JSONObject exportTemplateName) {  this.exportTemplateName = exportTemplateName;   }
    public JSONObject getTestDatabase() {   return testDatabase;    }
    public void setTestDatabase(JSONObject testDatabase) {  this.testDatabase = testDatabase;   }
}
