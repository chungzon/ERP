package ERP.Controller.Toolkit.ShowSeparatePurchaseManufacturer;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.Controller.Order.SearchOrder.SearchOrder_Controller;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OffsetOrderStatus;
import ERP.Enum.Order.Order_Enum.OrderBorrowed;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.Order.Order_Enum.EstablishSource;
import ERP.ERPApplication;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** [Controller] Show separate purchase manufacturer */
public class ShowSeparatePurchaseManufacturer_Controller {

    @FXML TextField OrderIndexText, ManufacturerIDText, ManufacturerNameText;
    @FXML Button importProduct_Button, deleteProduct_Button;
    @FXML TableView<OrderProduct_Bean> MainTableView;
    @FXML TableColumn<OrderProduct_Bean, CheckBox> SelectColumn;
    @FXML TableColumn<OrderProduct_Bean, Integer> QuantityColumn;
    @FXML TableColumn<OrderProduct_Bean, String> ItemNumberColumn, ISBNColumn, ProductNameColumn, UnitColumn, BatchPricePriceColumn, PricingColumn, PriceAmountColumn, InStockColumn, SafetyStockColumn;
    @FXML TabPane TabPane;

    private final String[] TableColumns = { "項號", "主碼", "品名", "數量", "單位", "單價", "定價", "金額", "存量", "安全存量"};
    private final String[] TableColumnCellValue = new String[]{"SeriesNumber","ISBN","ProductName","Quantity","Unit","SinglePrice","Pricing","PriceAmount","InStock","SafetyStock"};
    private final int[] ColumnsLength = { 45, 130, 450, 85, 50, 80, 80, 80, 50, 80};

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;

    private String projectCode, projectName;
    private ArrayList<HashMap<ObjectInfo_Bean, TableView<OrderProduct_Bean>>> PurchaseQuotationTableViewList = null;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private Order_Model Order_Model;
    private SearchOrder_Controller SearchOrder_Controller;
    private Stage Stage;

    private HashMap<Integer,OrderProduct_Bean> originProductMap;
    private HashMap<Integer,Integer> nowAllSelectQuantityMap;
    private ObjectInfo_Bean tempObjectInfo_Bean;
    public ShowSeparatePurchaseManufacturer_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallFXML = ToolKit.CallFXML;

        Order_Model = ToolKit.ModelToolKit.getOrderModel();

        originProductMap = new HashMap<>();
        nowAllSelectQuantityMap = new HashMap<>();
    }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set object - [Controller] EstablishOrder_Controller */
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){  this.EstablishOrder_Controller = EstablishOrder_Controller; }
    /** Set object - [Controller] SearchOrder_Controller */
    public void setSearchOrder_Controller(SearchOrder_Controller SearchOrder_Controller){   this.SearchOrder_Controller = SearchOrder_Controller;   }
    public void setProjectCodeAndProjectName(String productCode, String projectName){
        this.projectCode = productCode;
        this.projectName = projectName;
    }
    /** Set order of products in TableView */
    public void setTableViewItems(ObservableList<OrderProduct_Bean> ProductList) throws Exception {
        for(OrderProduct_Bean OrderProduct_Bean : ProductList) {
            OrderProduct_Bean.setCheckBoxSelect(false);
            if(OrderProduct_Bean.getQuantity() == 0)    OrderProduct_Bean.setCheckBoxDisable(true);
            else    OrderProduct_Bean.setCheckBoxDisable(false);
            MainTableView.getItems().add(ToolKit.CopyProductBean(OrderProduct_Bean));
            originProductMap.put(OrderProduct_Bean.getItemNumber(),OrderProduct_Bean);
        }
    }
    /** Set component of show separate purchase manufacturer */
    public void setComponent(){
        initialTableView();
        setTextFieldEditable(false,false);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(SelectColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ItemNumberColumn,"ItemNumber","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ISBNColumn,"ISBN", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProductNameColumn,"ProductName", "CENTER-LEFT", "16",null);
        setColumnCellIntegerValue(QuantityColumn,"Quantity", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(UnitColumn,"Unit", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(BatchPricePriceColumn,"BatchPrice", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PricingColumn,"Pricing", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(PriceAmountColumn,"PriceAmount", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(InStockColumn,"InStock", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(SafetyStockColumn,"SafetyStock", "CENTER-LEFT", "16",null);
    }
    /** TableView Key Release - 表格事件 */
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent) throws Exception{
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) {
            ObservableList<OrderProduct_Bean> SelectProductList = ComponentToolKit.getSelectProductList(MainTableView);
            if(SelectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
            else    createPurchaseQuotationTab(SelectProductList);
        }else if(KeyPressed.isSpaceKeyPressed(KeyEvent)){
            OrderProduct_Bean thisOrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView);
            if(thisOrderProduct_Bean.isCheckBoxSelect()) {
                thisOrderProduct_Bean.setCheckBoxSelect(false);
                ObservableList<OrderProduct_Bean> OrderProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                setTextFieldEditable(false,false);
                for(OrderProduct_Bean OrderProduct_Bean : OrderProductList){
                    if(OrderProduct_Bean.isCheckBoxSelect()){
                        setTextFieldEditable(true,false);
                        break;
                    }
                }
            }else if(!thisOrderProduct_Bean.isCheckBoxDisable() && !thisOrderProduct_Bean.isCheckBoxSelect()) {
                setTextFieldEditable(true,false);
                thisOrderProduct_Bean.setCheckBoxSelect(true);
            }
        }
    }
    /** Button Mouse Clicked - 匯入 */
    @FXML protected void ImportProductMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && PurchaseQuotationTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView());
            for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                if(MainOrderProduct_Bean.isCheckBoxSelect()){
                    if(MainOrderProduct_Bean.getItemNumber() < TabProductList.get(0).getItemNumber()) {
                        TabProductList.add(0, ToolKit.CopyProductBean(MainOrderProduct_Bean));
                        nowAllSelectQuantityMap.put(MainOrderProduct_Bean.getItemNumber(), nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber()) + MainOrderProduct_Bean.getQuantity());
                    }else if(MainOrderProduct_Bean.getItemNumber() > TabProductList.get(TabProductList.size()-1).getItemNumber()) {
                        TabProductList.add(TabProductList.size(), ToolKit.CopyProductBean(MainOrderProduct_Bean));
                        nowAllSelectQuantityMap.put(MainOrderProduct_Bean.getItemNumber(), nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber()) + MainOrderProduct_Bean.getQuantity());
                    }else {
                        if(!isTabTableViewExistProduct(MainOrderProduct_Bean, TabProductList)){
                            for (int index = 0; index < TabProductList.size(); index++) {
                                OrderProduct_Bean TabProductBean = TabProductList.get(index);
                                if (MainOrderProduct_Bean.getItemNumber() > TabProductBean.getItemNumber()) {
                                    TabProductList.add(index+1, ToolKit.CopyProductBean(MainOrderProduct_Bean));
                                    nowAllSelectQuantityMap.put(MainOrderProduct_Bean.getItemNumber(), nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber()) + MainOrderProduct_Bean.getQuantity());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            refreshMainTableView();
            refreshPurchaseQuotationTableView();
            refreshProductSeriesNumber(getSelectTabProductTableView());
        }
    }
    private boolean isTabTableViewExistProduct(OrderProduct_Bean MainOrderProduct_Bean, ObservableList<OrderProduct_Bean> TabProductList){
        for (OrderProduct_Bean TabProductBean : TabProductList) {
            if (MainOrderProduct_Bean.getItemNumber() == TabProductBean.getItemNumber()) {
                TabProductBean.setQuantity(TabProductBean.getQuantity() + MainOrderProduct_Bean.getQuantity());
                nowAllSelectQuantityMap.put(MainOrderProduct_Bean.getItemNumber(), nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber()) + MainOrderProduct_Bean.getQuantity());
                return true;
            }
        }
        return false;
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeleteProductMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && PurchaseQuotationTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView());
            OrderProduct_Bean OrderProduct_Bean = ComponentToolKit.getOrderProductTableViewSelectItem(getSelectTabProductTableView());
            if(OrderProduct_Bean == null)   DialogUI.MessageDialog("※ 請選擇商品");
            else{
                for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                    if(MainOrderProduct_Bean.getItemNumber() == OrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(OrderProduct_Bean.getISBN())){
                        int nowSelectQuantity = nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber());
                        int originQuantity = originProductMap.get(MainOrderProduct_Bean.getItemNumber()).getQuantity();
                        int noneSeparateQuantity = (originQuantity - (nowSelectQuantity - OrderProduct_Bean.getQuantity()));
                        if(noneSeparateQuantity < 0)    noneSeparateQuantity = 0;
                        MainOrderProduct_Bean.setQuantity(noneSeparateQuantity);
                        nowAllSelectQuantityMap.put(MainOrderProduct_Bean.getItemNumber(), nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber()) - OrderProduct_Bean.getQuantity());
                        TabProductList.remove(OrderProduct_Bean);
                        MainOrderProduct_Bean.setCheckBoxDisable(false);
                        refreshProductSeriesNumber(getSelectTabProductTableView());
                        break;
                    }
                }
                if(TabProductList.size() == 0)  deleteTab();
            }
        }
    }
    /** TextField Key Pressed - 廠商編號 */
    @FXML protected void ManufacturerIDKeyPressed(KeyEvent KeyEvent){
        String ObjectID = ManufacturerIDText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && !ObjectID.equals("")){
            ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject.廠商, Order_Enum.ObjectSearchColumn.廠商編號, ObjectID);
            if(ObjectList.size() == 1)  setManufacturerInfo(ObjectList.get(0));
            else if(ObjectList.size() > 1)   CallFXML.ShowObject(Stage, OrderObject.廠商, ObjectList,true, Order_Enum.ShowObjectSource.待出貨庫存不足轉採購,this);
            else{
                if(tempObjectInfo_Bean == null){
                    OrderIndexText.setText("");
                    ManufacturerIDText.setText("");
                    ManufacturerNameText.setText("");
                }else{
                    ManufacturerIDText.setText(tempObjectInfo_Bean.getObjectID());
                    ManufacturerNameText.setText(tempObjectInfo_Bean.getObjectName());
                }
                DialogUI.MessageDialog("※ 查無相關廠商");
            }
        }
    }
    /** TextField Key Pressed - 廠商名稱 */
    @FXML protected void ManufacturerNameKeyPressed(KeyEvent KeyEvent){
        String ObjectName = ManufacturerNameText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && !ObjectName.equals("")){
            ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject.廠商, Order_Enum.ObjectSearchColumn.廠商名稱, ObjectName);
            if(ObjectList.size() == 1)  setManufacturerInfo(ObjectList.get(0));
            else if(ObjectList.size() > 1)   CallFXML.ShowObject(Stage, OrderObject.廠商, ObjectList,true, Order_Enum.ShowObjectSource.待出貨庫存不足轉採購,this);
            else{
                if(tempObjectInfo_Bean == null){
                    OrderIndexText.setText("");
                    ManufacturerIDText.setText("");
                    ManufacturerNameText.setText("");
                }else{
                    ManufacturerIDText.setText(tempObjectInfo_Bean.getObjectID());
                    ManufacturerNameText.setText(tempObjectInfo_Bean.getObjectName());
                }
                DialogUI.MessageDialog("※ 查無相關廠商");
            }
        }
    }
    /** Set manufacturer info of order */
    public void setManufacturerInfo(ObjectInfo_Bean ObjectInfo_Bean){
        if(tempObjectInfo_Bean != null){
            if(DialogUI.ConfirmDialog("※ 是否變更廠商 ?",true,false,0,0)) {
                tempObjectInfo_Bean = ObjectInfo_Bean;
                if (ComponentToolKit.getTabPaneSelectTab(TabPane) != null)
                    updateSelectTabObjectInfoBean(tempObjectInfo_Bean);
            }
        }else   tempObjectInfo_Bean = ObjectInfo_Bean;
        ManufacturerIDText.setText(tempObjectInfo_Bean.getObjectID());
        ManufacturerNameText.setText(tempObjectInfo_Bean.getObjectName());

    }
    private void updateSelectTabObjectInfoBean(ObjectInfo_Bean ObjectInfo_Bean){
        Tab SelectTab = ComponentToolKit.getTabPaneSelectTab(TabPane);
        ObjectInfo_Bean OldObject = getSelectTabProductObjectInfoBean();
        String TabName = SelectTab.getText();
        SelectTab.setText(ObjectInfo_Bean.getObjectNickName() + TabName.substring(TabName.indexOf("(")));
        PurchaseQuotationTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)).put(ObjectInfo_Bean,getSelectTabProductTableView());
        PurchaseQuotationTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)).remove(OldObject);
    }
    /** Button Mouse Clicked - 新增採購單 */
    @FXML protected void InsertPurchaseQuotationMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<OrderProduct_Bean> SelectProductList = ComponentToolKit.getSelectProductList(MainTableView);
            if(SelectProductList.size() == 0) DialogUI.MessageDialog("※ 請選擇商品");
            else if(tempObjectInfo_Bean == null)    DialogUI.MessageDialog("※ 請輸入進貨廠商");
            else {
                createPurchaseQuotationTab(SelectProductList);
                setButtonDisable(false);
                if(PurchaseQuotationTableViewList.size() != 0)  setTextFieldEditable(false,false);
                else    setTextFieldEditable(true,true);
            }
        }
    }
    private void createPurchaseQuotationTab(ObservableList<OrderProduct_Bean> SelectProductList) throws Exception{
        TableView<OrderProduct_Bean> TableView = createTableView();
        for(OrderProduct_Bean OrderProduct_Bean : SelectProductList){
            OrderProduct_Bean CopyBean = ToolKit.CopyProductBean(OrderProduct_Bean);
            ComponentToolKit.getOrderProductTableViewItemList(TableView).add(CopyBean);
            if(!nowAllSelectQuantityMap.containsKey(CopyBean.getItemNumber()))
                nowAllSelectQuantityMap.put(CopyBean.getItemNumber(),CopyBean.getQuantity());
            else
                nowAllSelectQuantityMap.put(CopyBean.getItemNumber(),nowAllSelectQuantityMap.get(CopyBean.getItemNumber())+CopyBean.getQuantity());
        }
        if(ComponentToolKit.getTableViewItemsSize(TableView) > 0){
            if(PurchaseQuotationTableViewList == null)  PurchaseQuotationTableViewList = new ArrayList<>();
            PurchaseQuotationTableViewList.add(new HashMap<ObjectInfo_Bean, TableView<OrderProduct_Bean>>(){{put(tempObjectInfo_Bean, TableView);}});

            Tab Tab = ComponentToolKit.setTab(tempObjectInfo_Bean.getObjectNickName() + "(" + PurchaseQuotationTableViewList.size() + ")");
            Tab.setOnSelectionChanged(event -> {
                if(Tab.isSelected()){
                    if(tempObjectInfo_Bean == null){
                        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                        for (OrderProduct_Bean OrderProduct_Bean : ProductList) OrderProduct_Bean.setCheckBoxSelect(false);
                    }
                    tempObjectInfo_Bean = getSelectTabProductObjectInfoBean();
                    OrderIndexText.setText(String.valueOf(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)+1));
                    ManufacturerIDText.setText(tempObjectInfo_Bean.getObjectID());
                    ManufacturerNameText.setText(tempObjectInfo_Bean.getObjectName());
                }
            });
            BorderPane BorderPane = ComponentToolKit.setBorderPane(10,10,0,10);
            BorderPane.setCenter(TableView);
            Tab.setContent(BorderPane);
            TabPane.getTabs().add(Tab);
            TabPane.getSelectionModel().select(Tab);
            refreshProductSeriesNumber(TableView);
        }
        refreshMainTableView();
    }
    private TableView<OrderProduct_Bean> createTableView(){
        TableView<OrderProduct_Bean> TableView = ComponentToolKit.createOrderProductTableView();
        for(int index = 0 ; index < TableColumns.length ; index++) {
            TableColumn<OrderProduct_Bean,Integer> TableColumn = ComponentToolKit.createOrderProductTableIntegerColumn(TableColumns[index],ColumnsLength[index],TableColumnCellValue[index]);
            if(index == 3)  TableColumn.setCellFactory(p -> new createSpinner());
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
        createSpinner() {
            QuantitySpinner = new Spinner<>();
            QuantitySpinner.setEditable(true);
            ComponentToolKit.setIntegerSpinnerValueFactory(QuantitySpinner,1, 999, 1, 1);
            AtomicInteger oldQuantity = new AtomicInteger();
            QuantitySpinner.setOnKeyReleased(KeyEvent -> {
                if(!KeyPressed.isEnterKeyPressed(KeyEvent)){
                    oldQuantity.set(QuantitySpinner.getValueFactory().getValue());
                }else if(KeyPressed.isEnterKeyPressed(KeyEvent)){
                    if(oldQuantity.get() == QuantitySpinner.getValueFactory().getValue())
                        return;
                    ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                    OrderProduct_Bean SelectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView()).get(getIndex());
                    for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                        if(SelectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(SelectProduct_Bean.getISBN())){
                            Integer newQuantity = QuantitySpinner.getValueFactory().getValue();
                            if(newQuantity == null) newQuantity = 0;
                            int nowAllSelectQuantity = nowAllSelectQuantityMap.get(SelectProduct_Bean.getItemNumber()) - oldQuantity.get() + newQuantity;
                            nowAllSelectQuantityMap.put(SelectProduct_Bean.getItemNumber(), nowAllSelectQuantity);
                            SelectProduct_Bean.setQuantity(newQuantity);
                            SelectProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(SelectProduct_Bean.getQuantity()*SelectProduct_Bean.getBatchPrice()));

                            int originQuantity = originProductMap.get(SelectProduct_Bean.getItemNumber()).getQuantity();
                            if(nowAllSelectQuantity < originQuantity){
                                MainOrderProduct_Bean.setQuantity(originQuantity - nowAllSelectQuantity);
                            }else{
                                MainOrderProduct_Bean.setQuantity(0);
                            }

                            oldQuantity.set(newQuantity);
                            if(MainOrderProduct_Bean.getQuantity() == 0)    MainOrderProduct_Bean.setCheckBoxDisable(true);
                            else    MainOrderProduct_Bean.setCheckBoxDisable(false);
                            break;
                        }
                    }
                }
            });

            QuantitySpinner.setOnMouseClicked(event -> {
                ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                OrderProduct_Bean SelectProduct_Bean = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView()).get(getIndex());
                int Discount = QuantitySpinner.getValueFactory().getValue() - SelectProduct_Bean.getQuantity();
                for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                    if(SelectProduct_Bean.getItemNumber() == MainOrderProduct_Bean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(SelectProduct_Bean.getISBN())){
                        if(Discount == 1) {
                            SelectProduct_Bean.setQuantity(SelectProduct_Bean.getQuantity() + 1);
                            SelectProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(SelectProduct_Bean.getQuantity()*SelectProduct_Bean.getBatchPrice()));
                            nowAllSelectQuantityMap.put(SelectProduct_Bean.getItemNumber(), nowAllSelectQuantityMap.get(SelectProduct_Bean.getItemNumber()) + 1);

                            int MainQuantity = MainOrderProduct_Bean.getQuantity()-Discount;
                            if(MainQuantity <= 0)
                                MainOrderProduct_Bean.setQuantity(0);
                            else
                                MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()-Discount);

                        }else if(Discount == -1) {
                            SelectProduct_Bean.setQuantity(SelectProduct_Bean.getQuantity() - 1);
                            SelectProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(SelectProduct_Bean.getQuantity()*SelectProduct_Bean.getBatchPrice()));
                            int nowAllSelectQuantity = nowAllSelectQuantityMap.get(SelectProduct_Bean.getItemNumber()) - 1;
                            nowAllSelectQuantityMap.put(SelectProduct_Bean.getItemNumber(), nowAllSelectQuantity);

                            int originQuantity = originProductMap.get(SelectProduct_Bean.getItemNumber()).getQuantity();
                            if(nowAllSelectQuantity < originQuantity){
                                MainOrderProduct_Bean.setQuantity(MainOrderProduct_Bean.getQuantity()-Discount);
                            }
                        }
                        if(MainOrderProduct_Bean.getQuantity() == 0)    MainOrderProduct_Bean.setCheckBoxDisable(true);
                        else    MainOrderProduct_Bean.setCheckBoxDisable(false);
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

    /** Button Mouse Clicked - 刪除採購單 */
    @FXML protected void DeletePurchaseQuotationMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && PurchaseQuotationTableViewList.size() != 0){
            ObservableList<OrderProduct_Bean> MainProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
            ObservableList<OrderProduct_Bean> TabProductList = ComponentToolKit.getOrderProductTableViewItemList(getSelectTabProductTableView());
            for(OrderProduct_Bean MainOrderProduct_Bean : MainProductList){
                for (OrderProduct_Bean TabProductBean : TabProductList){
                    if(MainOrderProduct_Bean.getItemNumber() == TabProductBean.getItemNumber() && MainOrderProduct_Bean.getISBN().equals(TabProductBean.getISBN())){
                        int nowSelectQuantity = nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber());
                        int originQuantity = originProductMap.get(MainOrderProduct_Bean.getItemNumber()).getQuantity();
                        int noneSeparateQuantity = (originQuantity - (nowSelectQuantity - TabProductBean.getQuantity()));
                        if(noneSeparateQuantity < 0)    noneSeparateQuantity = 0;
                        MainOrderProduct_Bean.setQuantity(noneSeparateQuantity);
                        MainOrderProduct_Bean.setCheckBoxDisable(false);
                        nowAllSelectQuantityMap.put(MainOrderProduct_Bean.getItemNumber(), nowAllSelectQuantityMap.get(MainOrderProduct_Bean.getItemNumber()) - TabProductBean.getQuantity());
                    }
                }
            }
            deleteTab();
            if(PurchaseQuotationTableViewList.size() == 0){
                tempObjectInfo_Bean = null;
                setButtonDisable(true);
                setTextFieldEditable(false,false);
            }
        }
    }
    private void deleteTab(){
        PurchaseQuotationTableViewList.remove(ComponentToolKit.getTabPaneSelectTabIndex(TabPane));
        ComponentToolKit.deleteSelectTab(TabPane);
        TabRename();
    }
    private void TabRename(){
        ObservableList<Tab> TabList = ComponentToolKit.getTabPaneList(TabPane);
        for(int index = 0 ; index < TabList.size() ; index++){
            Tab Tab = TabList.get(index);
            ObjectInfo_Bean ObjectInfo_Bean = PurchaseQuotationTableViewList.get(index).entrySet().iterator().next().getKey();
            Tab.setText(ObjectInfo_Bean.getObjectNickName() + "(" + (index+1) + ")");
        }
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
    private void refreshPurchaseQuotationTableView(){
        getSelectTabProductTableView().refresh();
    }
    /** Button Mouse Clicked - 建立 */
    @FXML protected void EstablishPurchaseQuotationMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(PurchaseQuotationTableViewList == null || PurchaseQuotationTableViewList.size() == 0)    DialogUI.MessageDialog("※ 未分配商品");
            else if(isRemainProduct()) DialogUI.MessageDialog("※ 尚有剩餘商品");
            else{
                boolean whetherBringProjectCode = false, whetherBringProjectName = false;
                if(projectCode != null && !projectCode.equals(""))
                    whetherBringProjectCode = DialogUI.ConfirmDialog("是否帶入專案編號 ?",true,false,0,0);
                if(projectName != null && !projectName.equals(""))
                    whetherBringProjectName = DialogUI.ConfirmDialog("是否帶入專案名稱 ?",true,false,0,0);

                for (HashMap<ObjectInfo_Bean, TableView<OrderProduct_Bean>> PurchaseQuotationMap : PurchaseQuotationTableViewList) {
                    Order_Bean Order_Bean = RecordOrderBean(whetherBringProjectCode, whetherBringProjectName, PurchaseQuotationMap);
                    Order_Model.refreshProductSaleStatus(Order_Bean.getProductList());

                    ObservableList<OrderProduct_Bean> originProductList = FXCollections.observableArrayList();
                    for(int ItemNumber : originProductMap.keySet()){
                        originProductList.add(originProductMap.get(ItemNumber));
                    }

                    if (Order_Model.insertOrder(OrderSource.採購單, Order_Bean,originProductList,true,false)) {
                        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
                        establishOrder_NewStage_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
                        establishOrder_NewStage_Bean.setOrderSource(OrderSource.採購單);
                        establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.已存在);
                        establishOrder_NewStage_Bean.setOrder_Bean(Order_Bean);
                        establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
                        establishOrder_NewStage_Bean.setSearchOrder_Controller(SearchOrder_Controller);
                        establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
                        CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
                        EstablishOrder_Controller.refreshWaitingOrderNumber();
                        EstablishOrder_Controller.TransferWaitingShipment();
                        CloseMouseClicked(null);
                        ERPApplication.Logger.info("※ [成功] 「待出貨單」庫存不足，建立「" + OrderSource.採購單 + " : " + Order_Bean.getNowOrderNumber() + "」");
                        DialogUI.MessageDialog("※ 採購單建立成功");
                    }else{
                        ERPApplication.Logger.warn("※ [失敗] 「待出貨單」庫存不足，建立「" + OrderSource.採購單 + " : " + Order_Bean.getNowOrderNumber() + "」");
                        DialogUI.MessageDialog("※ 採購單建立失敗");
                    }
                    ERPApplication.Logger.info("---------------------------------------------------");
                }
            }
        }
    }
    private boolean isRemainProduct(){
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(!OrderProduct_Bean.isCheckBoxDisable())  return true;
        }
        return false;
    }
    private Order_Bean RecordOrderBean(boolean whetherBringProjectCode, boolean whetherBringProjectName, HashMap<ObjectInfo_Bean, TableView<OrderProduct_Bean>> PurchaseQuotationMap){
        ObjectInfo_Bean ObjectInfo_Bean = PurchaseQuotationMap.entrySet().iterator().next().getKey();
        ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(PurchaseQuotationMap.entrySet().iterator().next().getValue());
        for(OrderProduct_Bean OrderProduct_Bean : ProductList)  OrderProduct_Bean.setItemNumber(OrderProduct_Bean.getSeriesNumber());

        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setOrderDate(ToolKit.getToday("yyyy-MM-dd"));
        Order_Bean.setNowOrderNumber(Order_Model.generateNewestOrderNumberOfEstablishOrder(Order_Enum.OrderSource.採購單, Order_Enum.generateOrderNumberMethod.日期, Order_Bean.getOrderDate(),false));
        Order_Bean.setObjectID(ObjectInfo_Bean.getObjectID());
        Order_Bean.setObjectName(ObjectInfo_Bean.getObjectName());
        Order_Bean.setIsBorrowed(OrderBorrowed.未借測.value());
        Order_Bean.setIsOffset(OffsetOrderStatus.未沖帳);
        Order_Bean.setNumberOfItems(String.valueOf(ProductList.size()));

        Order_Bean.setOrderTaxStatus(Order_Enum.OrderTaxStatus.values()[ObjectInfo_Bean.getOrderTax().ordinal()]);
        Order_Bean.setTotalPriceNoneTax(Order_Model.calculateOrderTotalPrice_NoneTax(false, ProductList));
        Order_Bean.setDiscount("0");
        if(Order_Bean.getOrderTaxStatus() == Order_Enum.OrderTaxStatus.含稅){
            int TotalPriceIncludeTax = Order_Model.calculateOrderTotalPrice_IncludeTax(Order_Bean.getOrderTaxStatus(), Integer.parseInt(Order_Bean.getTotalPriceNoneTax()));
            int TotalPriceNoneTax = Integer.parseInt(Order_Bean.getTotalPriceNoneTax());
            Order_Bean.setTotalPriceIncludeTax("" + TotalPriceIncludeTax);
            Order_Bean.setTax("" + (TotalPriceIncludeTax - TotalPriceNoneTax));
        }else{
            Order_Bean.setTax("0");
            Order_Bean.setTotalPriceIncludeTax(Order_Bean.getTotalPriceNoneTax());
        }
        Order_Bean.setOrderRemark("");
        Order_Bean.setCashierRemark("");

        Order_Bean.setProductList(ProductList);
        Order_Bean.setOrderSource(Order_Enum.OrderSource.採購單);
        Order_Bean.setPurchaserName("");
        Order_Bean.setPurchaserTelephone("");
        Order_Bean.setPurchaserCellphone("");
        Order_Bean.setPurchaserAddress("");
        Order_Bean.setRecipientName("");
        Order_Bean.setRecipientTelephone("");
        Order_Bean.setRecipientCellphone("");
        Order_Bean.setRecipientAddress("");
        Order_Bean.setProjectCode(whetherBringProjectCode ? projectCode : "");
        Order_Bean.setProjectName(whetherBringProjectName ? projectName : "");
        Order_Bean.setProjectQuantity("");
        Order_Bean.setProjectUnit("");
        Order_Bean.setProjectPriceAmount("");
        Order_Bean.setProjectTotalPriceNoneTax("");
        Order_Bean.setProjectTax("");
        Order_Bean.setProjectTotalPriceIncludeTax("");
        Order_Bean.setProjectDifferentPrice("");
        Order_Bean.setIsCheckout(CheckoutStatus.未結帳.value());
        Order_Bean.setWaitingOrderDate(null);
        Order_Bean.setWaitingOrderNumber(null);
        Order_Bean.setAlreadyOrderDate(null);
        Order_Bean.setAlreadyOrderNumber(null);
        Order_Bean.setEstablishSource(EstablishSource.系統建立);

        Order_Bean.setReviewStatusMap(Order_Enum.ReviewObject.貨單商品,Order_Enum.ReviewStatus.無);
        Order_Bean.setReviewStatusMap(Order_Enum.ReviewObject.商品群組,Order_Enum.ReviewStatus.無);

        HashMap<Order_Enum.OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = new HashMap<>();
        orderReferenceMap.put(Order_Enum.OrderSource.報價單,new HashMap<>());
        orderReferenceMap.put(Order_Enum.OrderSource.出貨子貨單, new HashMap<>());
        Order_Bean.setOrderReferenceMap(orderReferenceMap);
        return Order_Bean;
    }
    private void refreshProductSeriesNumber(TableView<OrderProduct_Bean> TableView){
        ObservableList<OrderProduct_Bean> OrderProductList = ComponentToolKit.getOrderProductTableViewItemList(TableView);
        for(int index = 0 ; index < OrderProductList.size() ; index++){
            OrderProduct_Bean OrderProduct_Bean = OrderProductList.get(index);
            OrderProduct_Bean.setSeriesNumber(index+1);
        }
    }
    private void setTextFieldEditable(boolean Editable, boolean requestFocus){
        if(!Editable){
            if(PurchaseQuotationTableViewList != null && PurchaseQuotationTableViewList.size() != 0){
                ObjectInfo_Bean ObjectInfo_Bean = getSelectTabProductObjectInfoBean();
                OrderIndexText.setText(String.valueOf(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)+1));
                ManufacturerIDText.setText(ObjectInfo_Bean.getObjectID());
                ManufacturerNameText.setText(ObjectInfo_Bean.getObjectName());
                ComponentToolKit.setTextFieldEditable(ManufacturerIDText,true);
                ComponentToolKit.setTextFieldEditable(ManufacturerNameText,true);
            }else{
                ComponentToolKit.setTextFieldStyle(ManufacturerIDText,"-fx-background-color:#bebebe");
                ComponentToolKit.setTextFieldStyle(ManufacturerNameText,"-fx-background-color:#bebebe");
                OrderIndexText.setText("");
                ManufacturerIDText.setText("");
                ManufacturerNameText.setText("");
                ComponentToolKit.setTextFieldEditable(ManufacturerIDText,false);
                ComponentToolKit.setTextFieldEditable(ManufacturerNameText,false);
            }
        }else{
            ComponentToolKit.setTextFieldEditable(ManufacturerIDText,true);
            ComponentToolKit.setTextFieldEditable(ManufacturerNameText,true);
            ComponentToolKit.setTextFieldStyle(ManufacturerIDText,"-fx-light-text-color:white");
            ComponentToolKit.setTextFieldStyle(ManufacturerNameText,"-fx-light-text-color:white");
            tempObjectInfo_Bean = null;
            OrderIndexText.setText("");
            ManufacturerIDText.setText("");
            ManufacturerNameText.setText("");
            if(requestFocus)
                ManufacturerIDText.requestFocus();
        }
    }
    private void setButtonDisable(boolean Disable){
        ComponentToolKit.setButtonDisable(importProduct_Button,Disable);
        ComponentToolKit.setButtonDisable(deleteProduct_Button, Disable);
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){   if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent))  ComponentToolKit.closeThisStage(Stage);  }

    private ObjectInfo_Bean getSelectTabProductObjectInfoBean(){
        return PurchaseQuotationTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)).entrySet().iterator().next().getKey();
    }
    private TableView<OrderProduct_Bean> getSelectTabProductTableView(){
        return PurchaseQuotationTableViewList.get(ComponentToolKit.getTabPaneSelectTabIndex(TabPane)).entrySet().iterator().next().getValue();
    }
    private void setColumnCellValueAndCheckBox(TableColumn<OrderProduct_Bean,CheckBox> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<OrderProduct_Bean, CheckBox> {
        private String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(CheckBox CheckBox, boolean empty) {
            super.updateItem(CheckBox, empty);
            if(!empty){

                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                CheckBox SelectCheckBox = ComponentToolKit.getOrderProductTableViewSelectItem(MainTableView,getIndex()).getCheckBox();
                setGraphic(SelectCheckBox);
                SelectCheckBox.setOnAction(ActionEvent -> {
                    boolean isProductSelect = false;
                    ObservableList<OrderProduct_Bean> ProductList = ComponentToolKit.getOrderProductTableViewItemList(MainTableView);
                    for(OrderProduct_Bean OrderProduct_Bean : ProductList){
                        if(OrderProduct_Bean.isCheckBoxSelect()) {
                            isProductSelect = true;
                            break;
                        }
                    }
                    setTextFieldEditable(isProductSelect,true);
                });
            }else   setGraphic(null);
        }
    }

    private void setColumnCellIntegerValue(TableColumn<OrderProduct_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
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
