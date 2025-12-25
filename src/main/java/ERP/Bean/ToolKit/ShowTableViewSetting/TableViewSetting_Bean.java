package ERP.Bean.ToolKit.ShowTableViewSetting;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class TableViewSetting_Bean {
    private CheckBox CheckBox;
    private SimpleStringProperty ColumnName;

    public TableViewSetting_Bean(boolean isCheckBoxSelect, String ColumnName){
        CheckBox = new CheckBox();
        CheckBox.setSelected(isCheckBoxSelect);
        this.ColumnName = new SimpleStringProperty();
        this.ColumnName.set(ColumnName);
    }

    public boolean isCheckBoxSelect() {    return CheckBox.isSelected();    }
    public void setCheckBoxSelect(boolean CheckBoxSelect) {   this.CheckBox.setSelected(CheckBoxSelect);    }
    public CheckBox getCheckBox(){  return CheckBox;    }

    public String getColumnName() { return ColumnName.get();    }
    public SimpleStringProperty columnNameProperty() {  return ColumnName;  }
//    public void setColumnName(String columnName) {  this.ColumnName.set(columnName);    }
}
