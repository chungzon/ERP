package ERP.Bean.ToolKit.ShowExportQuotationRecord;

import ERP.Enum.Order.Order_Enum.ReviewObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExportQuotationRecord_Bean {
    private int id,order_id;
    private SimpleIntegerProperty itemNumber;
    private SimpleStringProperty vendorName;
    private SimpleStringProperty exportContent;
    private SimpleStringProperty exportFormat;
    private SimpleStringProperty exportStatus;
    private SimpleStringProperty insertDateTime;
    private SimpleStringProperty exportObject;
    private SimpleStringProperty totalPriceIncludeTax;
    public ExportQuotationRecord_Bean(){
        this.itemNumber = new SimpleIntegerProperty();
        this.vendorName = new SimpleStringProperty();
        this.exportContent = new SimpleStringProperty();
        this.exportFormat = new SimpleStringProperty();
        this.exportStatus = new SimpleStringProperty();
        this.insertDateTime = new SimpleStringProperty();
        this.exportObject = new SimpleStringProperty();
        this.totalPriceIncludeTax = new SimpleStringProperty();
    }

    public int getItemNumber() {    return itemNumber.get();    }
    public SimpleIntegerProperty itemNumberProperty() { return itemNumber;  }
    public void setItemNumber(int itemNumber) { this.itemNumber.set(itemNumber);    }

    public String getVendorName() { return vendorName.get();    }
    public SimpleStringProperty vendorNameProperty() {  return vendorName;  }
    public void setVendorName(String vendorName) {  this.vendorName.set(vendorName);    }

    public String getExportContent() {  return exportContent.get(); }
    public SimpleStringProperty exportContentProperty() {   return exportContent;   }
    public void setExportContent(String exportContent) {    this.exportContent.set(exportContent);  }

    public String getExportFormat() {   return exportFormat.get();  }
    public SimpleStringProperty exportFormatProperty() {    return exportFormat;    }
    public void setExportFormat(String exportFormat) {  this.exportFormat.set(exportFormat);    }

    public boolean getExportStatus() {   return exportStatus.get().equals("成功");  }
    public SimpleStringProperty exportStatusProperty() {    return exportStatus;    }
    public void setExportStatus(boolean exportStatus) {  this.exportStatus.set(exportStatus ? "成功":"失敗");    }

    public String getInsertDateTime() {   return insertDateTime.get();  }
    public SimpleStringProperty insertDateTimeProperty() {    return insertDateTime;    }
    public void setInsertDateTime(String insertDateTime) {    this.insertDateTime.set(insertDateTime);   }

    public ReviewObject getExportObject() {   return ReviewObject.valueOf(exportObject.get());  }
    public SimpleStringProperty exportObjectProperty() {    return exportObject;    }
    public void setExportObject(ReviewObject reviewObject) {  this.exportObject.set(reviewObject.name());    }

    public String getTotalPriceIncludeTax() {   return totalPriceIncludeTax.get();  }
    public SimpleStringProperty totalPriceIncludeTaxProperty() {    return totalPriceIncludeTax;    }
    public void setTotalPriceIncludeTax(String totalPriceIncludeTax) {  this.totalPriceIncludeTax.set(totalPriceIncludeTax);    }
}
