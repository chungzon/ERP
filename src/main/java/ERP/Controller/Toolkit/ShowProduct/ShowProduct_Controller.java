package ERP.Controller.Toolkit.ShowProduct;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.ERPApplication;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/** [Controller] Show product */
public class ShowProduct_Controller{
    @FXML private TableColumn<OrderProduct_Bean, CheckBox> SelectColumn;
    @FXML private TableColumn<OrderProduct_Bean, String> ISBNColumn, InternationalCodeColumn, FirmCodeColumn, ProductNameColumn, UnitColumn, MinimumPriceColumn, InStockColumn, SaleQuantityColumn, PurchaseDateColumn, SaleDateColumn;
    @FXML private TableColumn<OrderProduct_Bean, Double> BatchPriceColumn, SinglePriceColumn, PricingColumn, VipPrice1Column, VipPrice2Column, VipPrice3Column;
    @FXML TableView<OrderProduct_Bean> TableView;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private Stage Stage;
    private OrderObject OrderObject;
    private EstablishOrder_Controller EstablishOrder_Controller;
    public ShowProduct_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
    }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set object - Product list */
    public void setTableViewItems(ObservableList<OrderProduct_Bean> ProductList){   TableView.getItems().addAll(ProductList);   }
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    /** Set object - [Controller] EstablishOrder_Controller */
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){   this.EstablishOrder_Controller = EstablishOrder_Controller;  }
    /** Set component of show product */
    public void setComponent(){
        MinimumPriceColumn.setText(OrderObject.name() + "特別價");
        InitialTableView();
    }
    private void InitialTableView(){
        setColumnCellValueAndCheckBox(SelectColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ISBNColumn,"ISBN","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(InternationalCodeColumn,"InternationalCode","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(FirmCodeColumn,"FirmCode","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(UnitColumn,"Unit","CENTER-LEFT","14",null);
        setDoublePriceColumnMicrometerFormat(BatchPriceColumn,"BatchPrice","CENTER-LEFT","14");
        setDoublePriceColumnMicrometerFormat(SinglePriceColumn,"SinglePrice","CENTER-LEFT","14");
        setDoublePriceColumnMicrometerFormat(PricingColumn,"Pricing","CENTER-LEFT","14");
        setStringPriceColumnMicrometerFormat(MinimumPriceColumn,"MinimumPrice","CENTER-LEFT","14");
        setDoublePriceColumnMicrometerFormat(VipPrice1Column,"VipPrice1","CENTER-LEFT","14");
        setDoublePriceColumnMicrometerFormat(VipPrice2Column,"VipPrice2","CENTER-LEFT","14");
        setDoublePriceColumnMicrometerFormat(VipPrice3Column,"VipPrice3","CENTER-LEFT","14");
        ComponentToolKit.setColumnCellValue(InStockColumn,"InStock","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(SaleQuantityColumn,"SaleQuantity","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(PurchaseDateColumn,"PurchaseDate","CENTER-LEFT","14",null);
        ComponentToolKit.setColumnCellValue(SaleDateColumn,"SaleDate","CENTER-LEFT","14",null);
    }
    /** TableView Key Released - 表格事件 */
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent){
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
            ObservableList<OrderProduct_Bean> SelectProductList = ComponentToolKit.getSelectProductList(TableView);
            if(SelectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
            else{
                EstablishOrder_Controller.setSelectProductBean(true,SelectProductList);
                ComponentToolKit.closeThisStage(Stage);
            }
        }else if(KeyPressed.isSpaceKeyPressed(KeyEvent)){
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
            if(OrderProduct_Bean.isCheckBoxSelect())    OrderProduct_Bean.setCheckBoxSelect(false);
            else if(!OrderProduct_Bean.isCheckBoxSelect())  OrderProduct_Bean.setCheckBoxSelect(true);
        }
    }
    /** Button Mouse Clicked - 取得 */
    @FXML protected void GetProductMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ObservableList<OrderProduct_Bean> SelectProductList = ComponentToolKit.getSelectProductList(TableView);
            if (SelectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
            else {
                EstablishOrder_Controller.setSelectProductBean(true,SelectProductList);
                ComponentToolKit.closeThisStage(Stage);
            }
        }
    }
    /** Button Mouse Clicked - 離開 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent) { if(KeyPressed.isMouseLeftClicked(MouseEvent))  ComponentToolKit.closeThisStage(Stage); }

    private void setColumnCellValueAndCheckBox(TableColumn<OrderProduct_Bean, CheckBox> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, CheckBox> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(CheckBox CheckBox, boolean empty) {
            super.updateItem(CheckBox, empty);
            if(!empty){
                setGraphic(ComponentToolKit.getOrderProductTableViewSelectItem(TableView,getIndex()).getCheckBox());
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
//                CheckBox.setSelected(false);
//                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectIndex(TableView,getIndex());
//                OrderProduct_Bean.setCheckBox(CheckBox);

            }else   setGraphic(null);
        }
    }
    private void setStringPriceColumnMicrometerFormat(TableColumn<OrderProduct_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setStringPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setStringPriceColumnMicrometerFormat extends TableCell<OrderProduct_Bean, String> {
        String Alignment, FontSize;
        setStringPriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
            }
        }
    }
    private void setDoublePriceColumnMicrometerFormat(TableColumn<OrderProduct_Bean,Double> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setDoublePriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setDoublePriceColumnMicrometerFormat extends TableCell<OrderProduct_Bean, Double> {
        String Alignment, FontSize;
        setDoublePriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
            }
        }
    }
}
