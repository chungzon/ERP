package ERP.Bean.ToolKit.ShowReportGenerator;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReportGenerator_Item_Bean {
    private int id, report_generator_id;
    private SimpleIntegerProperty itemNumber;
    private SimpleStringProperty itemName;
    public ReportGenerator_Item_Bean(){
        this.itemNumber = new SimpleIntegerProperty();
        this.itemName = new SimpleStringProperty();
    }

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public int getReport_generator_id() {   return report_generator_id; }
    public void setReport_generator_id(int report_generator_id) {   this.report_generator_id = report_generator_id; }

    public int getItemNumber() {    return itemNumber.get();    }
    public SimpleIntegerProperty itemNumberProperty() { return itemNumber;  }
    public void setItemNumber(int itemNumber) { this.itemNumber.set(itemNumber);    }

    public String getItemName() {   return itemName.get();  }
    public SimpleStringProperty itemNameProperty() {    return itemName;    }
    public void setItemName(String itemName) {  this.itemName.set(itemName);    }
}
