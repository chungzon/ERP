package ERP.Controller.Toolkit.ShowExportQuotation;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.Order.ExportQuotation_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Model.Order.ExportShipmentQuotationDocument.ExportShipmentQuotationDocument;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;

public class ShowExportQuotation_Controller {
    @FXML HBox showBatchPrice_HBox;
    @FXML RadioButton yes_radioButton, no_radioButton;

    @FXML CheckBox selectAll_checkBox;
    @FXML TableColumn<OrderProduct_Bean, Boolean> select_tableColumn;
    @FXML TableColumn<OrderProduct_Bean, String> productName_tableColumn;
    @FXML TableColumn<OrderProduct_Bean, Integer> quantity_tableColumn, priceAmount_tableColumn;
    @FXML TableView<OrderProduct_Bean> tableView;

    private ToolKit toolKit;
    private KeyPressed keyPressed;
    private ComponentToolKit componentToolKit;
    private Order_Model order_model;

    private ExportQuotation_Bean exportQuotation_bean;
    private Order_Bean order_bean;
    private IAECrawlerAccount_Bean iAECrawlerAccount_bean;
    private Stage stage;

    private ArrayList<Integer> originQuantityList = null;
    public ShowExportQuotation_Controller(){
        this.toolKit = ERPApplication.ToolKit;
        this.componentToolKit = toolKit.ComponentToolKit;
        this.keyPressed = toolKit.KeyPressed;

        this.order_model = toolKit.ModelToolKit.getOrderModel();
    }

    public void setExportQuotation_Bean(ExportQuotation_Bean exportQuotation_bean){
        this.exportQuotation_bean = exportQuotation_bean;
    }
    public void setOrder_Bean(Order_Bean order_bean){
        this.order_bean = order_bean;
    }
    public void setIAECrawlerAccount_Bean(IAECrawlerAccount_Bean iAECrawlerAccount_bean){
        this.iAECrawlerAccount_bean = iAECrawlerAccount_bean;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    public void setComponent() throws Exception {
        componentToolKit.setRadioButtonSelect(yes_radioButton, true);
        componentToolKit.setRadioButtonSelect(no_radioButton, false);
        componentToolKit.setCheckBoxSelect(selectAll_checkBox, true);
        initialTableView();
        componentToolKit.setHBoxDisable(showBatchPrice_HBox, exportQuotation_bean.getExportContent() == Order_Enum.ExportQuotationContent.專案);

        setOrderProductList(exportQuotation_bean.getProductList());

        if(exportQuotation_bean.getOrderSource() != Order_Enum.OrderSource.報價單 && exportQuotation_bean.getOrderSource() != Order_Enum.OrderSource.待出貨單){
            componentToolKit.setCheckBoxDisable(selectAll_checkBox,true);
        }else{
            componentToolKit.setCheckBoxDisable(selectAll_checkBox,false);
        }
    }
    public void setOrderProductList(ObservableList<OrderProduct_Bean> productList) throws Exception {
        if(originQuantityList == null)
            originQuantityList = new ArrayList<>();
        for(OrderProduct_Bean orderProduct_bean : productList){
            OrderProduct_Bean newOrderProduct_bean = toolKit.CopyProductBean(orderProduct_bean);

            originQuantityList.add(newOrderProduct_bean.getQuantity());
            componentToolKit.setCheckBoxSelect(newOrderProduct_bean.getCheckBox(), true);
            tableView.getItems().addAll(newOrderProduct_bean);
        }
    }

    private void initialTableView(){
        setTableColumnCellValueAndCheckBox(select_tableColumn,"select", "CENTER", "16");
        componentToolKit.setColumnCellValue(productName_tableColumn,"ProductName", "CENTER-LEFT", "16",null);
        componentToolKit.setColumnCellValue(quantity_tableColumn,"Quantity", "CENTER-LEFT", "16",null);
        quantity_tableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        componentToolKit.setColumnCellValue(priceAmount_tableColumn,"PriceAmount", "CENTER-LEFT", "16",null);
        setTableColumnMicrometerFormat(priceAmount_tableColumn,"PriceAmount", "CENTER-LEFT", "16");
    }
    @FXML protected void selectAllOnAction(){
        boolean isSelectAll = selectAll_checkBox.isSelected();
        ObservableList<OrderProduct_Bean> productList = componentToolKit.getOrderProductTableViewItemList(tableView);
        for(OrderProduct_Bean orderProduct_bean : productList){
            componentToolKit.setCheckBoxSelect(orderProduct_bean.getCheckBox(), isSelectAll);
        }
    }
    @FXML protected void quantityOnEditCommit(TableColumn.CellEditEvent<OrderProduct_Bean, Integer> quantityOnEditCommit){
        OrderProduct_Bean orderProduct_bean = componentToolKit.getOrderProductTableViewSelectItem(tableView);
        int originQuantity = originQuantityList.get(orderProduct_bean.getItemNumber()-1);
        int newQuantity = quantityOnEditCommit.getNewValue();
        if(newQuantity <= originQuantity && newQuantity != 0){
            orderProduct_bean.setQuantity(quantityOnEditCommit.getNewValue());
            orderProduct_bean.setPriceAmount(toolKit.RoundingInteger(orderProduct_bean.getSinglePrice()*orderProduct_bean.getQuantity()));
        }else{
            if(newQuantity > originQuantity){
                DialogUI.MessageDialog("【數量】超出上限");
            }else {
                DialogUI.MessageDialog("【數量】不允許輸入0");
            }
            orderProduct_bean.setQuantity(quantityOnEditCommit.getOldValue());
            tableView.refresh();
        }
    }
    @FXML protected void exportQuotationMouseClicked(MouseEvent mouseEvent){
        if(keyPressed.isMouseLeftClicked(mouseEvent)){
            exportQuotation_bean.setShowBatchPrice(yes_radioButton.isSelected());

            ObservableList<OrderProduct_Bean> exportProductList = FXCollections.observableArrayList();
            ObservableList<OrderProduct_Bean> productList = componentToolKit.getOrderProductTableViewItemList(tableView);
            for(OrderProduct_Bean orderProduct_bean : productList){
                if(orderProduct_bean.getCheckBox().isSelected()){
                    exportProductList.add(orderProduct_bean);
                }
            }
            exportQuotation_bean.setProductList(exportProductList);
            boolean exportStatus = false;
            try{
                ExportShipmentQuotationDocument exportShipmentQuotationDocument = new ExportShipmentQuotationDocument();
                if(exportShipmentQuotationDocument.setExportDataAndExportQuotation(OrderObject.客戶, exportQuotation_bean)){
                    exportStatus = true;
                    ERPApplication.Logger.info("報價單：【" + exportQuotation_bean.getVendorNickName() + "】匯出成功!");
                    DialogUI.MessageDialog("※ 匯出成功!");
                    componentToolKit.closeThisStage(stage);
                }else{
                    ERPApplication.Logger.warn("報價單：【" + exportQuotation_bean.getVendorNickName() + "】匯出失敗!");
                    DialogUI.MessageDialog("※ 匯出失敗!");
                }
            }catch (Exception ex){
                ERPApplication.Logger.catching(ex);
                DialogUI.ExceptionDialog(ex);
            }finally {
                if(!order_model.insertExportQuotationRecord(order_bean.getOrderID(),iAECrawlerAccount_bean.getExportQuotation_id(), exportQuotation_bean, exportStatus))
                    DialogUI.AlarmDialog("※ 匯出報價單 - 記錄失敗");
            }
        }
    }
    @FXML protected void closeMouseClicked(MouseEvent mouseEvent){
        if(keyPressed.isMouseLeftClicked(mouseEvent)){
            componentToolKit.closeThisStage(stage);
        }
    }

    private void setTableColumnCellValueAndCheckBox(TableColumn<OrderProduct_Bean, Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        componentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setTableColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setTableColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, Boolean> {
        String Alignment, FontSize;
        setTableColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                OrderProduct_Bean orderProduct_bean = componentToolKit.getOrderProductTableViewSelectItem(tableView, getIndex());
                CheckBox checkBox = orderProduct_bean.getCheckBox();
                if(exportQuotation_bean.getOrderSource() != Order_Enum.OrderSource.報價單 && exportQuotation_bean.getOrderSource() != Order_Enum.OrderSource.待出貨單){
                    componentToolKit.setCheckBoxDisable(checkBox,true);
                }else{
                    componentToolKit.setCheckBoxDisable(checkBox,false);
                }
                setGraphic(orderProduct_bean.getCheckBox());
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);

                checkBox.setOnAction(ActionEvent -> {
                    orderProduct_bean.setCheckBoxSelect(checkBox.isSelected());
                    boolean isSelectAll = true;
                    ObservableList<OrderProduct_Bean> productList = componentToolKit.getOrderProductTableViewItemList(tableView);
                    for(OrderProduct_Bean orderProduct_Bean : productList){
                        if(!orderProduct_Bean.isCheckBoxSelect()) {
                            isSelectAll = false;
                            break;
                        }
                    }
                    componentToolKit.setCheckBoxSelect(selectAll_checkBox, isSelectAll);
                });
            }else   setGraphic(null);
        }
    }

    private void setTableColumnMicrometerFormat(TableColumn<OrderProduct_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setTableColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setTableColumnMicrometerFormat extends TableCell<OrderProduct_Bean, Integer> {
        String Alignment, FontSize;
        setTableColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(toolKit.fmtMicrometer(item));
            }
        }
    }
}
