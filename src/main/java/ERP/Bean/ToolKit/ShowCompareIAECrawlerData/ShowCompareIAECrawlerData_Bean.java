package ERP.Bean.ToolKit.ShowCompareIAECrawlerData;

import javafx.beans.property.SimpleStringProperty;

public class ShowCompareIAECrawlerData_Bean {

    public SimpleStringProperty columnName, oldInfo, newInfo;

    public ShowCompareIAECrawlerData_Bean(){
        columnName = new SimpleStringProperty();
        oldInfo = new SimpleStringProperty();
        newInfo = new SimpleStringProperty();
    }

    public String getColumnName() {
        return columnName.get();
    }

    public SimpleStringProperty columnNameProperty() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName.set(columnName);
    }

    public String getOldInfo() {
        return oldInfo.get();
    }

    public SimpleStringProperty oldInfoProperty() {
        return oldInfo;
    }

    public void setOldInfo(String oldInfo) {
        this.oldInfo.set(oldInfo);
    }

    public String getNewInfo() {
        return newInfo.get();
    }

    public SimpleStringProperty newInfoProperty() {
        return newInfo;
    }

    public void setNewInfo(String newInfo) {
        this.newInfo.set(newInfo);
    }
}
