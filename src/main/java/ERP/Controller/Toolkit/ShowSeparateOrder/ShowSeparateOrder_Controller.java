package ERP.Controller.Toolkit.ShowSeparateOrder;

import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Controller.Order.SearchOrder.SearchOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.Product_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.ToolKit.Tooltip;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.ArrayList;

/** [Controller] Show separate order */
public class ShowSeparateOrder_Controller {
    @FXML Label tooltip_Label;
    @FXML javafx.scene.control.Tooltip tooltip;
    @FXML Button importProduct_Button, deleteProduct_Button;
    @FXML Label subBillStatusLabel;
    @FXML TableView<OrderProduct_Bean> MainTableView;
    @FXML TableColumn<OrderProduct_Bean, CheckBox> SelectColumn;
    @FXML TableColumn<OrderProduct_Bean, Integer> QuantityColumn;
    @FXML TableColumn<OrderProduct_Bean, String> ItemNumberColumn, ISBNColumn, ProductNameColumn, UnitColumn, SinglePriceColumn, PricingColumn, PriceAmountColumn, InStockColumn;
    @FXML TabPane TabPane;

    private final String[] TableColumns = { "項號", "主碼", "品名", "數量", "單位", "單價", "定價", "金額", "存量"};
    private final String[] TableColumnCellValue = new String[]{"SeriesNumber","ISBN","ProductName","Quantity","Unit","SinglePrice","Pricing","PriceAmount","InStock"};
    private final int[] ColumnsLength = { 45, 130, 450, 85, 50, 80, 80, 80 , 50};

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;
    private Tooltip Tooltip;

    private ArrayList<TableView<OrderProduct_Bean>> SubBillTableViewList = null;
    private OrderObject OrderObject;
    private OrderSource OrderSource;
    private Product_Model Product_Model;
    private Order_Model Order_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private SystemSetting_Model SystemSetting_Model;
    private SearchOrder_Controller SearchOrder_Controller;
    private Order_Bean Order_Bean;
    private Stage Stage;
    public ShowSeparateOrder_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallFXML = ToolKit.CallFXML;
        Tooltip = ToolKit.Tooltip;
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
        Product_Model = ToolKit.ModelToolKit.getProductModel();
        ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setOrderObject(OrderObject OrderObject){   this.OrderObject = OrderObject; }
    public void setOrderSource(OrderSource OrderSource){    this.OrderSource = OrderSource; }
    public void setSearchOrder_Controller(SearchOrder_Controller SearchOrder_Controller){   this.SearchOrder_Controller = SearchOrder_Controller;   }
    public void setOrder_Bean(Order_Bean Order_Bean){   this.Order_Bean = Order_Bean;   }
    /** Set order of products in TableView */
    public void setTableViewItems(ObservableList<OrderProduct_Bean> ProductList) throws Exception {
        for(OrderProduct_Bean OrderProduct_Bean : ProductList) {
            OrderProduct_Bean.setCheckBoxSelect(false);
            if(OrderProduct_Bean.getQuantity() == 0)    OrderProduct_Bean.setCheckBoxDisable(true);
            else    OrderProduct_Bean.setCheckBoxDisable(false);
            MainTableView.getItems().add(ToolKit.CopyProductBean(OrderProduct_Bean));
        }
    }
    /** Set component of show separate order */
    public void setComponent(){
        initialTableView();
        subBillStatusLabel.setText(Order_Bean.isOffset() ? "「" + Order_Bean.getOffsetOrderStatus().name()+ "」子貨單" : "「正常」子貨單");
        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.showSeparateOrder());
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(MainTableView,SelectColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(SelectColumn,"SelectCheckBox","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ItemNumberColumn,"ItemNumber","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ISBNColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName", "CENTER-LEFT", "16",null);
        setColumnCellIntegerValue(QuantityColumn,"Quantity", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(UnitColumn,"Unit", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SinglePriceColumn,"SinglePrice", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PricingColumn,"Pricing", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PriceAmountColumn,"PriceAmount", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(InStockColumn,"InStock", "CENTER-LEFT", "16",null);
    }
    /** TableView Key Released - 表格事件 */
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent) throws Exception{
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
            ObservableList<OrderProduct_Bean> SelectProductList = ComponentToolKit.getSelectProductList(MainTableView);
            if(SelectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
            else    createSubBillTab(SelectProductList);
        }else if(KeyPressed.isSpaceKeyPressed(KeyEvent)){
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if(OrderProduct_Bean.isCheckBoxSelect())  OrderProduct_Bean.setCheckBoxSelect(false);
            else if(!OrderProduct_Bean.isCheckBoxDisable() && !OrderProduct_Bean.isCheckBoxSelect())  OrderProduct_Bean.setCheckBoxSelect(true);
        }
    }
    /** Button Mouse Clicked - 匯入 */
    @FXML protected void ImportProductMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && SubBillTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)));
            for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                if(MainOrderProduct_Bean.isCheckBoxSelect()){
                    if(MainOrderProduct_Bean.getItemNumber() < TabProductList.get(0).getItemNumber())
                        TabProductList.add(0, ToolKit.CopyProductBean(MainOrderProduct_Bean));
                    else if(MainOrderProduct_Bean.getItemNumber() > TabProductList.get(TabProductList.size()-1).getItemNumber())
                        TabProductList.add(TabProductList.size(), ToolKit.CopyProductBean(MainOrderProduct_Bean));
                    else {
                        if(!isTabTableViewExistProduct(MainOrderProduct_Bean, TabProductList)){
                            for (int index = 0; index < TabProductList.size(); index++) {
                                OrderProduct_Bean TabProductBean = TabProductList.get(index);
                                if (MainOrderProduct_Bean.getItemNumber() > TabProductBean.getItemNumber()) {
                                    TabProductList.add(index+1, ToolKit.CopyProductBean(MainOrderProduct_Bean));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            refreshMainTableView();
            refreshSubBillTableView();
            Order_Model.refreshProductSeriesNumber(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)));
        }
    }
    private boolean isTabTableViewExistProduct(OrderProduct_Bean OrderProduct_Bean, ObservableList<OrderProduct_Bean> TabProductList){
        for (OrderProduct_Bean TabProductBean : TabProductList) {
            if (OrderProduct_Bean.getItemNumber() == TabProductBean.getItemNumber()) {
                TabProductBean.setQuantity(TabProductBean.getQuantity() + OrderProduct_Bean.getQuantity());
                return true;
            }
        }
        return false;
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeleteProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && SubBillTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)));
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)));
            if(OrderProduct_Bean == null)   DialogUI.MessageDialog("※ 請選擇商品");
            else{
                for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                    if(MainOrderProduct_Bean.getItemNumber() == OrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(OrderProduct_Bean.getISBN())){
                        MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()+OrderProduct_Bean.getQuantity());
                        TabProductList.remove(OrderProduct_Bean);
                        MainOrderProduct_Bean.setCheckBoxDisable(false);
                        Order_Model.refreshProductSeriesNumber(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)));
                        break;
                    }
                }
                if(TabProductList.size() == 0)  deleteTab();
            }
        }
    }
    /** Button Mouse Clicked - 新增子貨單 */
    @FXML protected void InsertOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<OrderProduct_Bean> SelectProductList = ComponentToolKit.getSelectProductList(MainTableView);
            if(!Order_Bean.isOffset() && SelectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
            else {
                createSubBillTab(SelectProductList);
                setButtonDisable(false);
            }
        }
    }
    private void createSubBillTab(ObservableList<OrderProduct_Bean> SelectProductList) throws Exception{
        TableView<OrderProduct_Bean> TableView = createTableView();
        for(OrderProduct_Bean OrderProduct_Bean : SelectProductList){
            OrderProduct_Bean CopyBean = ToolKit.CopyProductBean(OrderProduct_Bean);
            ComponentToolKit.getOrderProductTableViewItemList(TableView).add(CopyBean);
        }
        if(Order_Bean.isOffset() || ComponentToolKit.getTableViewItemsSize(TableView) > 0){
            if(SubBillTableViewList == null)  SubBillTableViewList = new ArrayList<>();
            SubBillTableViewList.add(TableView);

            Tab Tab = ComponentToolKit.setTab("子貨單 " + SubBillTableViewList.size());
            BorderPane BorderPane = ComponentToolKit.setBorderPane(10,10,0,10);
            BorderPane.setCenter(TableView);
            Tab.setContent(BorderPane);
            TabPane.getTabs().add(Tab);
            TabPane.getSelectionModel().select(Tab);
            Order_Model.refreshProductSeriesNumber(TableView);
        }
        refreshMainTableView();
    }
    private void refreshMainTableView(){
        ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for (OrderProduct_Bean MainOrderProduct_Bean : MainProductList) {
            if (MainOrderProduct_Bean.isCheckBoxSelect()) {
                MainOrderProduct_Bean.setQuantity(0);
                MainOrderProduct_Bean.setCheckBoxSelect(false);
                MainOrderProduct_Bean.setCheckBoxDisable(true);
            }
        }
    }
    private void refreshSubBillTableView(){
        SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)).refresh();
    }
    private TableView<OrderProduct_Bean> createTableView(){
        TableView<OrderProduct_Bean> TableView = ComponentToolKit.createOrderProductTableView();
        for(int index = 0 ; index < TableColumns.length ; index++) {
            TableColumn<OrderProduct_Bean,Integer> TableColumn = ComponentToolKit.createOrderProductTableIntegerColumn(TableColumns[index],ColumnsLength[index],TableColumnCellValue[index]);
            if(index == 3)  TableColumn.setCellFactory(p -> new createSpinner(SubBillTableViewList, TabPane));
            TableView.getColumns().add(TableColumn);
        }
        TableView.setOnMouseClicked(MouseEvent -> {
            if(KeyPressed.isMouseLeftClicked(MouseEvent)){
                OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(TableView);
                if(OrderProduct_Bean != null){
                    ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                    for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                        if(MainOrderProduct_Bean.getItemNumber() == OrderProduct_Bean.getItemNumber()){
                            MainTableView.getSelectionModel().select(MainOrderProduct_Bean);
                            break;
                        }
                    }
                }
            }
        });
        return TableView;
    }
    private class createSpinner extends TableCell<OrderProduct_Bean, Integer> {
        Spinner<Integer> QuantitySpinner;
        createSpinner(ArrayList<TableView<OrderProduct_Bean>> SubBillTableViewList, TabPane TabPane) {
            QuantitySpinner = new Spinner<>();
            QuantitySpinner.setEditable(true);
            ComponentToolKit.setIntegerSpinnerValueFactory(QuantitySpinner,1, 999, 1, 1);
            QuantitySpinner.setOnKeyReleased(KeyEvent -> {
                if(KeyPressed.isEnterKeyPressed(KeyEvent)){
                    ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                    OrderProduct_Bean SelectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane))).get(getIndex());
                    for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                        if(SelectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(SelectProduct_Bean.getISBN())){
                            int TotalQuantity = MainOrderProduct_Bean.getQuantity() + SelectProduct_Bean.getQuantity();
                            if(QuantitySpinner.getValueFactory().getValue() > TotalQuantity)    //  超出範圍
                                QuantitySpinner.getValueFactory().setValue(SelectProduct_Bean.getQuantity());
                            else{
                                TotalQuantity = TotalQuantity - QuantitySpinner.getValueFactory().getValue();
                                MainOrderProduct_Bean.setQuantity(TotalQuantity);
                                SelectProduct_Bean.setQuantity(QuantitySpinner.getValueFactory().getValue());
                                if(TotalQuantity == 0)    MainOrderProduct_Bean.setCheckBoxDisable(true);
                                else    MainOrderProduct_Bean.setCheckBoxDisable(false);
                            }
                            break;
                        }
                    }
                }
            });
            QuantitySpinner.setOnMouseClicked(e -> {
                ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                OrderProduct_Bean SelectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane))).get(getIndex());
                int Discount = QuantitySpinner.getValueFactory().getValue() - SelectProduct_Bean.getQuantity();
                for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                    if(SelectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(SelectProduct_Bean.getISBN())){
                        if(MainOrderProduct_Bean.getQuantity() == 0 && Discount == 1)   QuantitySpinner.getValueFactory().setValue(SelectProduct_Bean.getQuantity());
                        else{
                            if(Discount == 1)   SelectProduct_Bean.setQuantity(SelectProduct_Bean.getQuantity()+1);
                            else if(Discount == -1) SelectProduct_Bean.setQuantity(SelectProduct_Bean.getQuantity() - 1);
                            MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()-Discount);
                            if(MainOrderProduct_Bean.getQuantity() == 0)    MainOrderProduct_Bean.setCheckBoxDisable(true);
                            else    MainOrderProduct_Bean.setCheckBoxDisable(false);
                        }
                        break;
                    }
                }
            });
        }
        @Override
        protected void updateItem(Integer Quantity, boolean empty) {
            super.updateItem(Quantity, empty);
            if (empty)  setGraphic(null);
            else{
                ComponentToolKit.setSpinnerIntegerValue(QuantitySpinner, Quantity);
                setGraphic(QuantitySpinner);
            }
        }
    }
    /** Button Mouse Clicked - 刪除子貨單 */
    @FXML protected void DeleteOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && SubBillTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(SubBillTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)));
            for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                for (OrderProduct_Bean TabProductBean : TabProductList){
                    if(MainOrderProduct_Bean.getItemNumber() == TabProductBean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(TabProductBean.getISBN())){
                        MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()+TabProductBean.getQuantity());
                        MainOrderProduct_Bean.setCheckBoxDisable(false);
                    }
                }
            }
            deleteTab();
            if(SubBillTableViewList.size() == 0)    setButtonDisable(true);
        }
    }
    private void deleteTab(){
        SubBillTableViewList.remove(ComponentToolKit.getTabPaneSelectTabIndex(TabPane));
        ComponentToolKit.deleteSelectTab(TabPane);
        TabRename();
    }
    private void TabRename(){
        ObservableList<Tab> TabList = TabPane.getTabs();
        for(int index = 0 ; index < TabList.size() ; index++){
            Tab Tab = TabList.get(index);
            Tab.setText("子貨單 " + (index+1));
        }
    }
    /** Button Mouse Clicked - 建立 */
    @FXML protected void establishSubBillOrder(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(SubBillTableViewList == null || SubBillTableViewList.size() == 0){
                DialogUI.MessageDialog("※ 未新增子貨單");
                return;
            }
            String MaxOrderNumber = Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource, Order_Enum.generateOrderNumberMethod.單號, this.Order_Bean.getWaitingOrderNumber(),false);
            int OrderNumberIndex = 1 ;
            if(MaxOrderNumber != null)  OrderNumberIndex = Integer.parseInt(MaxOrderNumber.substring(getOrderNumberLength()));

            CloseMouseClicked(null);
            for(int index = 0 ; index < SubBillTableViewList.size() ; index++){
                Order_Bean Order_Bean = ToolKit.CopyOrderBean(this.Order_Bean);
                Order_Bean.setNowOrderNumber(this.Order_Bean.getWaitingOrderNumber() + ToolKit.fillZero(String.valueOf(OrderNumberIndex+index),2));
                Order_Bean.setOrderDate(ToolKit.getToday("yyyy-MM-dd"));
                Order_Bean.setObjectID(this.Order_Bean.getObjectID());
                Order_Bean.setObjectName(this.Order_Bean.getObjectName());
                Order_Bean.setIsBorrowed(this.Order_Bean.isBorrowed().value());
                Order_Bean.setIsOffset(this.Order_Bean.getOffsetOrderStatus());
                Order_Bean.setProductList(setProductPriceAmount(SubBillTableViewList.get(index)));

                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
                establishOrder_NewStage_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
                establishOrder_NewStage_Bean.setOrderSource(OrderSource);
                establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.未存在);
                establishOrder_NewStage_Bean.setOrder_Bean(Order_Bean);
                establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
                establishOrder_NewStage_Bean.setSearchOrder_Controller(SearchOrder_Controller);
                establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }
    }
    private int getOrderNumberLength(){  return Integer.parseInt(SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.貨單號長度));   }
    private ObservableList<OrderProduct_Bean> setProductPriceAmount(TableView<OrderProduct_Bean> TableView){
        ObservableList<OrderProduct_Bean> OrderProductList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for (OrderProduct_Bean OrderProduct_Bean : OrderProductList) {
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單)
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity() * OrderProduct_Bean.getBatchPrice()));
            else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity() * OrderProduct_Bean.getSinglePrice()));
        }
        return OrderProductList;
    }
    private void setButtonDisable(boolean Disable){
        ComponentToolKit.setButtonDisable(importProduct_Button,Disable);
        ComponentToolKit.setButtonDisable(deleteProduct_Button, Disable);
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){   if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent))    ComponentToolKit.closeThisStage(Stage);  }

    private void setColumnCellValueAndCheckBox(TableView<OrderProduct_Bean> TableView,TableColumn<OrderProduct_Bean,CheckBox> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(TableView, Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, CheckBox> {
        private TableView<OrderProduct_Bean> TableView;
        private String Alignment, FontSize;
        setColumnCellValueAndCheckBox(TableView<OrderProduct_Bean> TableView, String Alignment, String FontSize){
            this.TableView = TableView;
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(CheckBox CheckBox, boolean empty) {
            super.updateItem(CheckBox, empty);
            if(!empty){
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                setGraphic(ComponentToolKit.getOrderProductTableViewSelectItem(TableView,getIndex()).getCheckBox());
            }else   setGraphic(null);
        }
    }
    private void setColumnCellIntegerValue(TableColumn<OrderProduct_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setColumnCellIntegerValue(Alignment, FontSize));
    }
    private class setColumnCellIntegerValue extends TableCell<OrderProduct_Bean, Integer> {
        String Alignment, FontSize;
        setColumnCellIntegerValue(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(String.valueOf(item));
                OrderProduct_Bean OrderProduct_Bean = getTableView().getItems().get(getIndex());
                if(OrderProduct_Bean.getQuantity() <= 0)
                    getTableRow().setStyle("-fx-background-color: #B9B9B9;");
                else
                    getTableRow().setStyle("");
            }
        }
    }
}
