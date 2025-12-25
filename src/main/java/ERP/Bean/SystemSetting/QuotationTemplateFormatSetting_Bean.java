package ERP.Bean.SystemSetting;

import ERP.Enum.SystemSetting.SystemSetting_Enum.ExportQuotationTemplateTitle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class QuotationTemplateFormatSetting_Bean {
    private SimpleStringProperty sheetTitle, sheetCell;

    private SimpleObjectProperty<Integer> sheetRow, startIndex, endIndex;

    private ExportQuotationTemplateTitle exportQuotationTemplateTitle;
    public QuotationTemplateFormatSetting_Bean(){
        sheetTitle = new SimpleStringProperty();
        sheetRow = new SimpleObjectProperty();
        sheetCell = new SimpleStringProperty();
        startIndex = new SimpleObjectProperty<>();
        endIndex = new SimpleObjectProperty<>();
    }

    public ExportQuotationTemplateTitle getExportQuotationTemplateTitle() {
        return exportQuotationTemplateTitle;
    }

    public void setExportQuotationTemplateTitle(ExportQuotationTemplateTitle exportQuotationTemplateTitle) {
        this.exportQuotationTemplateTitle = exportQuotationTemplateTitle;
        this.sheetTitle.set(exportQuotationTemplateTitle.getDescribe());
    }

    public SimpleStringProperty sheetTitleProperty() {
        return sheetTitle;
    }
    public Integer getSheetRow() {
        return sheetRow.get();
    }

    public SimpleObjectProperty sheetRowProperty() {
        return sheetRow;
    }

    public void setSheetRow(Integer sheetRow) {  this.sheetRow.set(sheetRow);    }

    public String getSheetCell() {
        return sheetCell.get();
    }

    public SimpleStringProperty sheetCellProperty() {
        return sheetCell;
    }

    public void setSheetCell(String sheetCell) {
        this.sheetCell.set(sheetCell);
    }

    public Integer getStartIndex() {    return startIndex.get();    }

    public SimpleObjectProperty<Integer> startIndexProperty() { return startIndex;  }

    public void setStartIndex(Integer startIndex) { this.startIndex.set(startIndex);    }

    public Integer getEndIndex() {  return endIndex.get();  }

    public SimpleObjectProperty<Integer> endIndexProperty() {   return endIndex;    }

    public void setEndIndex(Integer endIndex) { this.endIndex.set(endIndex);    }
}

