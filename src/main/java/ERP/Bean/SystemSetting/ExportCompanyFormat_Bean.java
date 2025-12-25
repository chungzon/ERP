package ERP.Bean.SystemSetting;

import ERP.Enum.SystemSetting.SystemSetting_Enum.ExportCompanyFormat;
import javafx.beans.property.SimpleStringProperty;

public class ExportCompanyFormat_Bean {
    private SimpleStringProperty exportCompanyTitleFormat, exportCompanyInfoFormat;
    private ExportCompanyFormat ExportCompanyFormat;
    public ExportCompanyFormat_Bean(ExportCompanyFormat ExportCompanyFormat, String exportCompanyInfoFormat){
        this.ExportCompanyFormat = ExportCompanyFormat;
        this.exportCompanyTitleFormat = new SimpleStringProperty();
        this.exportCompanyTitleFormat.set(ExportCompanyFormat.getColumnName());
        this.exportCompanyInfoFormat = new SimpleStringProperty();
        this.exportCompanyInfoFormat.set(exportCompanyInfoFormat);
    }
    public String getExportCompanyTitleFormat() {   return exportCompanyTitleFormat.get();  }
    public SimpleStringProperty exportCompanyTitleFormatProperty() {    return exportCompanyTitleFormat;    }
    public ExportCompanyFormat getExportCompanyFormat(){    return ExportCompanyFormat; }

    public String getExportCompanyInfoFormat() {    return exportCompanyInfoFormat.get();   }
    public SimpleStringProperty exportCompanyInfoFormatProperty() { return exportCompanyInfoFormat; }
    public void setExportCompanyInfoFormat(String exportCompanyInfoFormat) {    this.exportCompanyInfoFormat.set(exportCompanyInfoFormat);  }
}
