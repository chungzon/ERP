package ERP.Controller.Toolkit.ShowExportQuotationRecord;

import ERP.Bean.ToolKit.ShowExportQuotationRecord.ExportQuotationRecord_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum.ReviewObject;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ShowExportQuotationRecord_Controller {
    @FXML TableColumn<ExportQuotationRecord_Bean, Integer> itemNumberColumn;
    @FXML TableColumn<ExportQuotationRecord_Bean, String> exportContentColumn,exportFormatColumn,exportManufacturer_VendorNameColumn,exportObjectColumn,exportTotalPriceIncludeTaxColumn,exportStatusColumn, insertDateTimeColumn;
    @FXML TableView<ExportQuotationRecord_Bean> TableView;

    private ToolKit toolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private Stage Stage;

    public ShowExportQuotationRecord_Controller(){
        this.toolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = toolKit.ComponentToolKit;
        this.KeyPressed = toolKit.KeyPressed;
    }

    public void setExportQuotationRecordList(ObservableList<ExportQuotationRecord_Bean> exportQuotationRecordList){
        TableView.getItems().addAll(exportQuotationRecordList);
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        initialTableView();
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(itemNumberColumn,"itemNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(exportContentColumn,"exportContent", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(exportFormatColumn,"exportFormat", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(exportManufacturer_VendorNameColumn,"vendorName", "CENTER-LEFT", "16",null);
        setExportObjectColumnTextFill(exportObjectColumn,"exportObject", "CENTER-LEFT", "16");
        setPriceColumnMicrometerFormat(exportTotalPriceIncludeTaxColumn,"totalPriceIncludeTax", "CENTER-LEFT", "16");
        setStatusColumnTextFill(exportStatusColumn,"exportStatus", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(insertDateTimeColumn,"insertDateTime", "CENTER-LEFT", "16",null);
    }
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.closeThisStage(Stage);
        }
    }

    private void setPriceColumnMicrometerFormat(TableColumn<ExportQuotationRecord_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setPriceColumnMicrometerFormat extends TableCell<ExportQuotationRecord_Bean, String> {
        String Alignment, FontSize;
        setPriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(toolKit.fmtMicrometer(item));
            }
        }
    }
    private void setExportObjectColumnTextFill(TableColumn<ExportQuotationRecord_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setExportObjectColumnTextFill(Alignment, FontSize));
    }
    private class setExportObjectColumnTextFill extends TableCell<ExportQuotationRecord_Bean, String> {
        String Alignment, FontSize;
        setExportObjectColumnTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item);
                ExportQuotationRecord_Bean exportQuotationRecord_Bean = getTableView().getItems().get(getIndex());
                ReviewObject reviewObject = exportQuotationRecord_Bean.getExportObject();
                String textColor = "black";
                if(reviewObject == ReviewObject.貨單商品){
                    textColor = "#143BBA";
                }else if(reviewObject == ReviewObject.商品群組){
                    textColor = "#009900";
                }
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-text-fill:" + textColor);
            }
        }
    }
    private void setStatusColumnTextFill(TableColumn<ExportQuotationRecord_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setStatusColumnTextFill(Alignment, FontSize));
    }
    private class setStatusColumnTextFill extends TableCell<ExportQuotationRecord_Bean, String> {
        String Alignment, FontSize;
        setStatusColumnTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item);
                ExportQuotationRecord_Bean exportQuotationRecord_Bean = getTableView().getItems().get(getIndex());
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + (exportQuotationRecord_Bean.getExportStatus() ? ";" : ";-fx-text-fill:red"));
            }
        }
    }
}
