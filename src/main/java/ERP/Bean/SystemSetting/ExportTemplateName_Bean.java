package ERP.Bean.SystemSetting;

import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ExportTemplateName;
import javafx.beans.property.SimpleStringProperty;

public class ExportTemplateName_Bean {
    private SimpleStringProperty exportTemplateTitleName, exportTemplateInfoName;
    private ExportTemplateName exportTemplateName;
    public ExportTemplateName_Bean(SystemSetting_Enum.ExportTemplateName exportTemplateName, String exportTemplateInfoName){
        this.exportTemplateName = exportTemplateName;
        this.exportTemplateTitleName = new SimpleStringProperty();
        this.exportTemplateTitleName.set(exportTemplateName.getDescribe());
        this.exportTemplateInfoName = new SimpleStringProperty();
        this.exportTemplateInfoName.set(exportTemplateInfoName);
    }
    public String getExportTemplateTitleName() {   return exportTemplateTitleName.get();  }
    public SimpleStringProperty exportTemplateTitleNameProperty() {    return exportTemplateTitleName;    }
    public ExportTemplateName getExportTemplateName(){    return exportTemplateName; }

    public String getExportTemplateInfoName() {    return exportTemplateInfoName.get();   }
    public SimpleStringProperty exportTemplateInfoNameProperty() { return exportTemplateInfoName; }
    public void setExportTemplateInfoName(String exportTemplateInfoName) {    this.exportTemplateInfoName.set(exportTemplateInfoName);  }
}
