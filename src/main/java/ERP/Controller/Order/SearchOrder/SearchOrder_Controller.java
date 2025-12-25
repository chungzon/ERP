package ERP.Controller.Order.SearchOrder;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.*;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import ERP.Enum.Order.Order_Enum.OrderExist;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Enum.Order.Order_Enum.SearchOrderTableColumn;
import ERP.Enum.Order.Order_Enum.OrderSearchColumn;
import ERP.Enum.Order.Order_Enum.OrderSearchMethod;
import ERP.Enum.Order.Order_Enum.EstablishSource;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum.Authority;
import ERP.Enum.ToolKit.ToolKit_Enum.AuthorizedLocation;
import ERP.ERPApplication;
import ERP.Model.Order.Order_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.*;
import ERP.ToolKit.Tooltip;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/** [Controller] Search order */
public class SearchOrder_Controller {
    @FXML BorderPane SearchOrder_BorderPane;
    @FXML HBox SearchMethodHBox, Quotation_WaitingPurchase_HBox, Purchase_Return_HBox, Quotation_WaitingShipment_HBox, Shipment_Return_HBox;
    @FXML CheckBox OrderBorrowed_CheckBox, combinedOrder_CheckBox;
    @FXML RadioButton SearchObjectOrProjectCode_RadioButton, SearchAll_RadioButton;
    @FXML TextField SearchColumnText;
    @FXML CheckBox WaitingOrderReviewStatus_CheckBox_SearchByColumn, OrderReviewFinished_CheckBox_SearchByColumn, WaitingModifyOrderReview_CheckBox_SearchByColumn, WaitingOrderReviewStatus_CheckBox_SearchByDate, OrderReviewFinished_CheckBox_SearchByDate, WaitingModifyOrderReview_CheckBox_SearchByDate, SelectAll_CheckBox;
    @FXML Button combinedOrder_Button, DeleteOrder_Button;
    @FXML DatePicker StartDate_DatePicker, EndDate_DatePicker;
    @FXML TableColumn<SearchOrder_Bean,Boolean> Select_TableColumn;
    @FXML TableColumn<SearchOrder_Bean,String> OrderSourceColumn, OrderNumberColumn, OrderDateColumn, EstablishSourceColumn, ObjectIDColumn, ObjectNameColumn, NumberOfItemsColumn, TotalPriceIncludeTaxColumn, InvoiceNumberColumn, ProjectCodeColumn, ProjectNameColumn, ExportQuotationManufacturerNickNameColumn, ReviewStatusColumn, CashierRemarkColumn, ProjectTotalPriceIncludeTaxColumn, ProjectDifferentPriceColumn, BalanceColumn, isCheckoutColumn, isOffsetColumn, pictureCountColumn, QuotationDateColumn, QuotationNumberColumn, WaitingOrderDateColumn, WaitingOrderNumberColumn, AlreadyOrderDateColumn, AlreadyOrderNumberColumn;
    @FXML TableView<SearchOrder_Bean> TableView;
    @FXML Label TotalOrderCount_Label, TotalOrderPrice_Label, TotalOrderProfit_Label;
    @FXML Label tooltip_Label;
    @FXML javafx.scene.control.Tooltip tooltip;

    private ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.CallConfig CallConfig;
    private Tooltip Tooltip;
    private javafx.scene.control.Tooltip mouseTooltip = new javafx.scene.control.Tooltip();
    private Stage MainStage;
    private JSONObject TableViewSettingMap;
    private OrderObject OrderObject;
    private SearchOrderSource SearchOrderSource;
    private OrderSearchMethod OrderSearchMethod;
    private Order_Model Order_Model;
    private SystemSetting_Model SystemSetting_Model;
    private int orderNumberLength;

    public JSONObject systemDefaultConfigJson;
    private DefaultSearchText_ToggleSwitch defaultSearchText_ToggleSwitch = null;
    public SearchOrder_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.CallConfig = ToolKit.CallConfig;
        this.Tooltip = ToolKit.Tooltip;
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    /** Set object - [Enum] Order_Enum.OrderObject */
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    /** Set object - [Enum] Order_Enum.SearchOrderSource */
    public void setSearchOrderSource(SearchOrderSource SearchOrderSource){    this.SearchOrderSource = SearchOrderSource; }
    public void setMainStage(Stage MainStage){  this.MainStage = MainStage; }
    /** Set component of search order */
    public void setComponent() throws Exception{
        systemDefaultConfigJson = CallConfig.getSystemDefaultConfigJson();
        orderNumberLength = Integer.parseInt(SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.貨單號長度));
        SearchObjectOrProjectCode_RadioButton.setText(OrderObject.name() + "編號(名稱) / 專案編號");

        createDefaultSearchTextFunction();

        ObjectIDColumn.setText(OrderObject.name() + "編號");
        ObjectNameColumn.setText(OrderObject.name() + "名稱");
        if(OrderObject == Order_Enum.OrderObject.廠商){
            QuotationDateColumn.setText("採購日期");
            QuotationNumberColumn.setText("採購單號");
            WaitingOrderDateColumn.setText("待入倉日期");
            WaitingOrderNumberColumn.setText("待入倉單號");
            AlreadyOrderDateColumn.setText("進貨日期");
            AlreadyOrderNumberColumn.setText("進貨單號");
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            QuotationDateColumn.setText("報價日期");
            QuotationNumberColumn.setText("報價單號");
            WaitingOrderDateColumn.setText("待出貨日期");
            WaitingOrderNumberColumn.setText("待出貨單號");
            AlreadyOrderDateColumn.setText("出貨日期");
            AlreadyOrderNumberColumn.setText("出貨單號");
        }
        if(SearchOrderSource == Order_Enum.SearchOrderSource.採購單) setPurchaseQuotationUI();
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單)  setPurchaseReturnUI();
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.報價單)    setShipmentQuotationUI();
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單)  setShipmentReturnUI();
        initialTableView();
        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.searchOrder());
        ComponentToolKit.setTooltipStyle(mouseTooltip);
    }
    private void createDefaultSearchTextFunction(){
        Integer defaultConfig = systemDefaultConfigJson.getInteger(SystemDefaultConfig_Enum.SearchOrder_DefaultSearchTextFunction.class.getSimpleName());
        if(defaultConfig == null)
            defaultConfig = SystemDefaultConfig_Enum.SearchOrder_DefaultSearchTextFunction.Or.ordinal();
        defaultSearchText_ToggleSwitch = new DefaultSearchText_ToggleSwitch(defaultConfig != 0,this);
        SearchMethodHBox.getChildren().add(defaultSearchText_ToggleSwitch);

        ComponentToolKit.setToolTips(defaultSearchText_ToggleSwitch, new javafx.scene.control.Tooltip(), Tooltip.searchOrder_ConditionalSearch());
    }
    private void setPurchaseQuotationUI() throws Exception{
        Quotation_WaitingPurchase_HBox.setVisible(true);
        Purchase_Return_HBox.setVisible(false);
        Quotation_WaitingShipment_HBox.setVisible(false);
        Shipment_Return_HBox.setVisible(false);
        PurchaseQuotationMouseClicked(null);
    }
    private void setPurchaseReturnUI() throws Exception{
        Quotation_WaitingPurchase_HBox.setVisible(false);
        Purchase_Return_HBox.setVisible(true);
        Quotation_WaitingShipment_HBox.setVisible(false);
        Shipment_Return_HBox.setVisible(false);
        PurchaseAndReturnOrderMouseClicked(null);
    }
    private void setShipmentQuotationUI() throws Exception{
        Quotation_WaitingPurchase_HBox.setVisible(false);
        Purchase_Return_HBox.setVisible(false);
        Quotation_WaitingShipment_HBox.setVisible(true);
        Shipment_Return_HBox.setVisible(false);
        ShipmentQuotationMouseClicked(null);
    }
    private void setShipmentReturnUI() throws Exception{
        Quotation_WaitingPurchase_HBox.setVisible(false);
        Purchase_Return_HBox.setVisible(false);
        Quotation_WaitingShipment_HBox.setVisible(false);
        Shipment_Return_HBox.setVisible(true);
        ShipmentAndReturnOrderMouseClicked(null);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(Select_TableColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(Select_TableColumn,"Select", "CENTER-LEFT", "16",null);
        setColumnCellValueAndTextFill_OrderSource(OrderSourceColumn,"OrderSource", "CENTER-LEFT", "16");
        setColumnCellValueAndTextFill_OrderSource(OrderNumberColumn,"OrderNumber", "CENTER-LEFT", "16");
        setColumnCellValueAndTextFill_OrderSource(OrderDateColumn,"OrderDate", "CENTER-LEFT", "16");
        setColumnCellValueAndTextFill_EstablishSource(EstablishSourceColumn,"EstablishSource", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ObjectIDColumn,"ObjectID", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ObjectNameColumn,"ObjectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(NumberOfItemsColumn,"NumberOfItems", "CENTER-LEFT", "16",null);
        setTotalPriceIncludeTaxTableCell(TotalPriceIncludeTaxColumn,"TotalPriceIncludeTax", "CENTER-LEFT", "16");
        ComponentToolKit.setTableColumnDoubleSort(TotalPriceIncludeTaxColumn);

        setColumnCellValueAndTextFill_InvoiceNumber(InvoiceNumberColumn,"InvoiceNumber", "CENTER-LEFT", "16");
        setColumnCellValueAndTextFill_ProjectCode(ProjectCodeColumn,"ProjectCode", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ProjectNameColumn,"ProjectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ExportQuotationManufacturerNickNameColumn,"ExportQuotationManufacturerNickName", "CENTER-LEFT", "16",null);
        setReviewStatusTableCell(ReviewStatusColumn,"ReviewStatus", "CENTER-LEFT", "16");

        setCashierRemarkColumnMouseEvent(CashierRemarkColumn,"CashierRemark", "CENTER-LEFT", "16");

        setStringPriceColumnMicrometerFormat(ProjectTotalPriceIncludeTaxColumn,"ProjectTotalPriceIncludeTax", "CENTER-LEFT", "16");
        setColumnCellValueAndTextFill_DifferentPrice(ProjectDifferentPriceColumn,"ProjectDifferentPrice", "CENTER-LEFT", "16");
        setStringPriceColumnMicrometerFormat(BalanceColumn,"Balance", "CENTER-LEFT", "16");
        setColumnCellValueAndTextFill_isCheckout(isCheckoutColumn,"isCheckout", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(isOffsetColumn,"isOffset", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(pictureCountColumn,"pictureCount", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuotationDateColumn,"QuotationDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuotationNumberColumn,"QuotationNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(WaitingOrderDateColumn,"WaitingOrderDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(WaitingOrderNumberColumn,"WaitingOrderNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(AlreadyOrderDateColumn,"AlreadyOrderDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(AlreadyOrderNumberColumn,"AlreadyOrderNumber", "CENTER-LEFT", "16",null);
    }
    private void setTableColumnSort() throws Exception{
        TableViewSettingMap = CallConfig.getSearchOrderTableViewSettingJson(SearchOrderSource);
        refreshTableView(TableViewSettingMap);
    }
    /** RadioButton Mouse Clicked - [單別] 採購單 */
    @FXML protected void PurchaseQuotationMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.採購單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
            lockSearchReviewStatusCheckBox(false);
            showCombinedOrderComponent(true);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, true);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 待入倉與子貨單 */
    @FXML protected void WaitingPurchaseAndSubBillMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.待入倉與子貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getWaitingPurchaseBackgroundColor());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, false);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 進貨與退貨單 */
    @FXML protected void PurchaseAndReturnOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.進貨與進退貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color:" + ToolKit.getAlreadyOrderAndReturnOrderBackgroundColorMix());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, false);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 進貨單 */
    @FXML protected void PurchaseOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.進貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getAlreadyOrderBackgroundColor());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, false);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 進退貨單 */
    @FXML protected void PurchaseReturnOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.進貨退貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getReturnOrderBackgroundColor());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, true);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 報價單 */
    @FXML protected void ShipmentQuotationMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.報價單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getQuotationBackgroundColor());
            lockSearchReviewStatusCheckBox(false);
            showCombinedOrderComponent(true);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, true);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 待出貨與子貨單 */
    @FXML protected void WaitingShipmentAndShipmentSubBillMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.待出貨與子貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getWaitingShipmentBackgroundColor());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, false);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 出貨與出退貨單 */
    @FXML protected void ShipmentAndReturnOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.出貨與出退貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color:" + ToolKit.getAlreadyOrderAndReturnOrderBackgroundColorMix());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, false);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 出貨單 */
    @FXML protected void ShipmentOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.出貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getAlreadyOrderBackgroundColor());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, false);
            setTableColumnSort();
            initialComponent();
        }
    }
    /** RadioButton Mouse Clicked - [單別] 出退貨單 */
    @FXML protected void ShipmentReturnOrderMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(MouseEvent == null || KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchOrderSource = Order_Enum.SearchOrderSource.出貨退貨單;
            SearchOrder_BorderPane.setStyle("-fx-background-color: " + ToolKit.getReturnOrderBackgroundColor());
            lockSearchReviewStatusCheckBox(true);
            showCombinedOrderComponent(false);
            ComponentToolKit.setButtonDisable(DeleteOrder_Button, true);
            setTableColumnSort();
            initialComponent();
        }
    }
    private void lockSearchReviewStatusCheckBox(boolean lock){
        ComponentToolKit.setCheckBoxDisable(WaitingOrderReviewStatus_CheckBox_SearchByColumn,lock);
        ComponentToolKit.setCheckBoxDisable(OrderReviewFinished_CheckBox_SearchByColumn,lock);
        ComponentToolKit.setCheckBoxDisable(WaitingModifyOrderReview_CheckBox_SearchByColumn,lock);
        ComponentToolKit.setCheckBoxDisable(WaitingOrderReviewStatus_CheckBox_SearchByDate,lock);
        ComponentToolKit.setCheckBoxDisable(OrderReviewFinished_CheckBox_SearchByDate,lock);
        ComponentToolKit.setCheckBoxDisable(WaitingModifyOrderReview_CheckBox_SearchByDate,lock);
    }
    private void initialComponent(){
        OrderBorrowed_CheckBox.setSelected(false);
        SearchColumnText.setText("");
        ComponentToolKit.setCheckBoxSelect(WaitingOrderReviewStatus_CheckBox_SearchByColumn,false);
        ComponentToolKit.setCheckBoxSelect(OrderReviewFinished_CheckBox_SearchByColumn,false);
        ComponentToolKit.setCheckBoxSelect(WaitingModifyOrderReview_CheckBox_SearchByColumn,false);
        ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ToolKit.getLastMonthSpecificDay(1));
        ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.getSearchOrderTableViewItemList(TableView).clear();
        TotalOrderCount_Label.setText("--");
        TotalOrderPrice_Label.setText("--");
        TotalOrderProfit_Label.setText("--");
    }
    @FXML protected void WaitingOrderReviewStatus_CheckBox_SearchByColumn_OnAction(){
        ComponentToolKit.setCheckBoxSelect(WaitingOrderReviewStatus_CheckBox_SearchByDate,WaitingOrderReviewStatus_CheckBox_SearchByColumn.isSelected());
    }
    @FXML protected void WaitingModifyOrderReview_CheckBox_SearchByColumn_OnAction(){
        ComponentToolKit.setCheckBoxSelect(WaitingModifyOrderReview_CheckBox_SearchByDate,WaitingModifyOrderReview_CheckBox_SearchByColumn.isSelected());
    }
    @FXML protected void OrderReviewFinished_CheckBox_SearchByColumn_OnAction(){
        ComponentToolKit.setCheckBoxSelect(OrderReviewFinished_CheckBox_SearchByDate,OrderReviewFinished_CheckBox_SearchByColumn.isSelected());
    }
    @FXML protected void WaitingOrderReviewStatus_CheckBox_SearchByDate_OnAction(){
        ComponentToolKit.setCheckBoxSelect(WaitingOrderReviewStatus_CheckBox_SearchByColumn,WaitingOrderReviewStatus_CheckBox_SearchByDate.isSelected());
    }
    @FXML protected void WaitingModifyOrderReview_CheckBox_SearchByDate_OnAction(){
        ComponentToolKit.setCheckBoxSelect(WaitingModifyOrderReview_CheckBox_SearchByColumn,WaitingModifyOrderReview_CheckBox_SearchByDate.isSelected());
    }
    @FXML protected void OrderReviewFinished_CheckBox_SearchByDate_OnAction(){
        ComponentToolKit.setCheckBoxSelect(OrderReviewFinished_CheckBox_SearchByColumn,OrderReviewFinished_CheckBox_SearchByDate.isSelected());
    }
    @FXML protected void SelectAllOnAction(){
        ObservableList<SearchOrder_Bean> searchOrderList = ComponentToolKit.getSearchOrderTableViewItemList(TableView);
        if(searchOrderList.size() != 0){
            for(SearchOrder_Bean searchOrder_Bean : searchOrderList){
                if(searchOrder_Bean.isCheckBoxSelect() != null)
                    searchOrder_Bean.setCheckBoxSelect(SelectAll_CheckBox.isSelected());
            }
            TableView.refresh();
        }
    }

    /** TextField Key Released - 送出 */
    @FXML protected void SearchOrderByColumnMouseClicked(){
        OrderSearchMethod = Order_Enum.OrderSearchMethod.關鍵字搜尋;
        if(isSearchTextEmpty()) DialogUI.MessageDialog("※ 請輸入搜尋資料!");
        else    OrderSearch();
    }
    /** TextField Key Released - 送出 */
    @FXML protected void SearchColumnKeyReleased(KeyEvent KeyEvent){
        OrderSearchMethod = Order_Enum.OrderSearchMethod.關鍵字搜尋;
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(isSearchTextEmpty()) DialogUI.MessageDialog("※ 請輸入搜尋資料!");
            else    OrderSearch();
        }
    }
    private boolean isSearchTextEmpty(){
        String searchText = SearchColumnText.getText();
        return searchText.equals("") || searchText.trim().isEmpty();
    }
    /** Button Mouse Clicked - 日期搜尋 */
    @FXML protected void SearchOrderByDateMouseClicked() throws Exception{
        OrderSearchMethod = Order_Enum.OrderSearchMethod.日期搜尋;
        if(ToolKit.isDateRangeError(StartDate_DatePicker, EndDate_DatePicker)) {
            ComponentToolKit.getSearchOrderTableViewItemList(TableView).clear();
            DialogUI.MessageDialog("※ 日期設定錯誤");
        }else    OrderSearch();
    }
    @FXML protected void searchPurchaseOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.SearchOrderProgress_NewStage(Order_Enum.OrderObject.廠商);
        }
    }
    @FXML protected void searchShipmentOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.SearchOrderProgress_NewStage(Order_Enum.OrderObject.客戶);
        }
    }
    /** Call function of search order
     * TODO 搜尋進貨單的發票暫時沒有功能  */
    public void callOrderSearch(){  OrderSearch();  }
    private void OrderSearch(){
        ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox, false);
        ObservableList<SearchOrder_Bean> SearchOrderList = getSearchOrderList();
        if(SearchOrderList.size() == 0){
            ComponentToolKit.getSearchOrderTableViewItemList(TableView).clear();
            if(SearchOrderSource == Order_Enum.SearchOrderSource.採購單 || SearchOrderSource == Order_Enum.SearchOrderSource.報價單) DialogUI.MessageDialog("※ 查無相關報價單");
            else if(SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單) DialogUI.MessageDialog("※ 查無相關待入倉與子貨單");
            else if(SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單) DialogUI.MessageDialog("※ 查無相關待出貨與子貨單");
            else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單) DialogUI.MessageDialog("※ 查無相關進貨與進退貨單");
            else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單) DialogUI.MessageDialog("※ 查無相關出貨與出退貨單");
            else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨單) DialogUI.MessageDialog("※ 查無相關進貨單");
            else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨單) DialogUI.MessageDialog("※ 查無相關出貨單");
            else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.出貨退貨單) DialogUI.MessageDialog("※ 查無相關退貨單");
        }else   TableView.setItems(SearchOrderList);
        setSearchOrderInfo(SearchOrderList);
    }
    private ObservableList<SearchOrder_Bean> getSearchOrderList(){
        String SearchKeyWord = SearchColumnText.getText().trim();
        String StartDate = ComponentToolKit.getDatePickerValue(StartDate_DatePicker, "yyyy-MM-dd");
        String EndDate = ComponentToolKit.getDatePickerValue(EndDate_DatePicker, "yyyy-MM-dd");
        HashMap<Order_Enum.ReviewStatus,Boolean> searchOrderReviewStatus = new HashMap<>();
        searchOrderReviewStatus.put(Order_Enum.ReviewStatus.待審查, WaitingOrderReviewStatus_CheckBox_SearchByColumn.isSelected());
        searchOrderReviewStatus.put(Order_Enum.ReviewStatus.待修改, WaitingModifyOrderReview_CheckBox_SearchByColumn.isSelected());
        searchOrderReviewStatus.put(Order_Enum.ReviewStatus.審查通過, OrderReviewFinished_CheckBox_SearchByColumn.isSelected());

        ConditionalOrderSearch_Bean conditionalOrderSearch_Bean = new ConditionalOrderSearch_Bean();
        conditionalOrderSearch_Bean.setOrderObject(OrderObject);
        conditionalOrderSearch_Bean.setSearchOrderSource(SearchOrderSource);
        conditionalOrderSearch_Bean.setOrderSearchMethod(OrderSearchMethod);
        conditionalOrderSearch_Bean.setBorrowed(OrderBorrowed_CheckBox.isSelected());
        conditionalOrderSearch_Bean.setOrderStatus(SearchOrderSource == Order_Enum.SearchOrderSource.採購單 && combinedOrder_CheckBox.isSelected() ? OrderStatus.併單 : OrderStatus.有效貨單);
        conditionalOrderSearch_Bean.setStartDate(StartDate);
        conditionalOrderSearch_Bean.setEndDate(EndDate);
        conditionalOrderSearch_Bean.setSearchKeyWord(SearchKeyWord);
        conditionalOrderSearch_Bean.setSearchOrderReviewStatus(searchOrderReviewStatus);
        conditionalOrderSearch_Bean.setSearchTextByOr(!defaultSearchText_ToggleSwitch.switchedOnProperty().get());
        ObservableList<SearchOrder_Bean> SearchOrderList = null;
        if(SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單)   SearchOrderList = getWaitingPurchaseAndSubBillOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單)  SearchOrderList = getAlreadyPurchaseAndReturnOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨單)  SearchOrderList = getAlreadyPurchaseOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨退貨單)    SearchOrderList = getPurchaseReturnOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.採購單)    SearchOrderList = getPurchaseQuotation(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單)   SearchOrderList = getWaitingShipmentAndSubBillOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單)  SearchOrderList = getAlreadyShipmentAndReturnOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨單)  SearchOrderList = getAlreadyShipmentOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨退貨單)    SearchOrderList = getShipmentReturnOrder(conditionalOrderSearch_Bean);
        else if(SearchOrderSource == Order_Enum.SearchOrderSource.報價單)    SearchOrderList = getShipmentQuotation(conditionalOrderSearch_Bean);
        return SearchOrderList;
    }
    private ObservableList<SearchOrder_Bean> getWaitingPurchaseAndSubBillOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        OrderSearchColumn waitingOrder_orderSearchColumn = getOrderSearchColumn(OrderSource.待入倉單);
        OrderSearchColumn subBill_orderSearchColumn = getOrderSearchColumn(OrderSource.進貨子貨單);
        ObservableList<SearchOrder_Bean> purchaseOrderList = Order_Model.searchOrder(OrderSource.待入倉單, waitingOrder_orderSearchColumn, conditionalOrderSearch_Bean);
        ObservableList<SearchOrder_Bean> subBillList = Order_Model.searchOrder(Order_Enum.OrderSource.進貨子貨單, subBill_orderSearchColumn, conditionalOrderSearch_Bean);

        boolean isSearchProductCode = (waitingOrder_orderSearchColumn == OrderSearchColumn.專案編號 && subBill_orderSearchColumn == OrderSearchColumn.專案編號);
        if(isSearchProductCode){
            searchProjectCodeOrder(false, OrderObject, purchaseOrderList, subBillList, conditionalOrderSearch_Bean);
        }else{
            ToolKit.sortSearchOrderNumber(purchaseOrderList);
            if(purchaseOrderList.size() != 0)
                ToolKit.sortSubBillFromWaitingShipment(purchaseOrderList, subBillList, Order_Model.getOrderNumberLength());
            else
                purchaseOrderList.addAll(subBillList);
        }
        return purchaseOrderList;
    }
    private ObservableList<SearchOrder_Bean> getAlreadyPurchaseAndReturnOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        ObservableList<SearchOrder_Bean> PurchaseOrderList = Order_Model.searchOrder(OrderSource.進貨單, getOrderSearchColumn(OrderSource.進貨單), conditionalOrderSearch_Bean);
        if(getOrderSearchColumn(OrderSource.進貨退貨單) != OrderSearchColumn.發票號碼 || (getOrderSearchColumn(OrderSource.進貨退貨單) == OrderSearchColumn.發票號碼 && conditionalOrderSearch_Bean.getSearchKeyWord().equals("")))
            PurchaseOrderList.addAll(Order_Model.searchOrder(OrderSource.進貨退貨單, getOrderSearchColumn(OrderSource.進貨退貨單), conditionalOrderSearch_Bean));
        ToolKit.sortSearchOrderNumber(PurchaseOrderList);
        ToolKit.sortSubBillFromAlreadyOrder(PurchaseOrderList,Order_Model.searchOrder(OrderSource.進貨子貨單, getOrderSearchColumn(OrderSource.進貨子貨單), conditionalOrderSearch_Bean), Order_Model.getOrderNumberLength());
        return PurchaseOrderList;
    }
    private ObservableList<SearchOrder_Bean> getAlreadyPurchaseOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        OrderSearchColumn alreadyOrder_orderSearchColumn = getOrderSearchColumn(OrderSource.進貨單);
        OrderSearchColumn subBill_orderSearchColumn = getOrderSearchColumn(OrderSource.進貨子貨單);
        ObservableList<SearchOrder_Bean> purchaseOrderList = Order_Model.searchOrder(OrderSource.進貨單, alreadyOrder_orderSearchColumn, conditionalOrderSearch_Bean);
        ObservableList<SearchOrder_Bean> subBillList = Order_Model.searchOrder(OrderSource.進貨子貨單, subBill_orderSearchColumn, conditionalOrderSearch_Bean);

        boolean isSearchProductCode = alreadyOrder_orderSearchColumn == OrderSearchColumn.專案編號 && subBill_orderSearchColumn == OrderSearchColumn.專案編號;
        if(isSearchProductCode){
            searchProjectCodeOrder(true, OrderObject, purchaseOrderList, subBillList, conditionalOrderSearch_Bean);
        }else{
            purchaseOrderList.addAll(subBillList);
            ToolKit.sortSearchOrderNumber(purchaseOrderList);
        }
        return purchaseOrderList;
    }
    private ObservableList<SearchOrder_Bean> getPurchaseReturnOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        conditionalOrderSearch_Bean.setSearchOrderReviewStatus(null);
        ObservableList<SearchOrder_Bean> PurchaseOrderList = Order_Model.searchOrder(OrderSource.進貨退貨單, getOrderSearchColumn(OrderSource.進貨退貨單), conditionalOrderSearch_Bean);
        ToolKit.sortSearchOrderNumber(PurchaseOrderList);
        return PurchaseOrderList;
    }
    private ObservableList<SearchOrder_Bean> getPurchaseQuotation(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        ObservableList<SearchOrder_Bean> PurchaseOrderList = Order_Model.searchOrder(OrderSource.採購單, getOrderSearchColumn(OrderSource.採購單), conditionalOrderSearch_Bean);
        ToolKit.sortSearchOrderNumber(PurchaseOrderList);
        return PurchaseOrderList;
    }
    private ObservableList<SearchOrder_Bean> getWaitingShipmentAndSubBillOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        OrderSearchColumn waitingOrder_orderSearchColumn = getOrderSearchColumn(OrderSource.待出貨單);
        OrderSearchColumn subBill_orderSearchColumn = getOrderSearchColumn(OrderSource.出貨子貨單);
        ObservableList<SearchOrder_Bean> shipmentOrderList = Order_Model.searchOrder(OrderSource.待出貨單, waitingOrder_orderSearchColumn, conditionalOrderSearch_Bean);
        ObservableList<SearchOrder_Bean> subBillList = Order_Model.searchOrder(Order_Enum.OrderSource.出貨子貨單, subBill_orderSearchColumn, conditionalOrderSearch_Bean);

        boolean isSearchProductCode = waitingOrder_orderSearchColumn == OrderSearchColumn.專案編號 && subBill_orderSearchColumn == OrderSearchColumn.專案編號;
        if(isSearchProductCode){
            searchProjectCodeOrder(false, OrderObject, shipmentOrderList, subBillList, conditionalOrderSearch_Bean);
        }else{
            ToolKit.sortSearchOrderNumber(shipmentOrderList);
            if(shipmentOrderList.size() != 0)
                ToolKit.sortSubBillFromWaitingShipment(shipmentOrderList, subBillList ,Order_Model.getOrderNumberLength());
            else
                shipmentOrderList.addAll(subBillList);
        }
        return shipmentOrderList;
    }
    private ObservableList<SearchOrder_Bean> getAlreadyShipmentAndReturnOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        OrderSearchColumn alreadyOrder_orderSearchColumn = getOrderSearchColumn(OrderSource.出貨單);
        OrderSearchColumn subBill_orderSearchColumn = getOrderSearchColumn(OrderSource.出貨子貨單);
        ObservableList<SearchOrder_Bean> shipmentOrderList = Order_Model.searchOrder(OrderSource.出貨單, alreadyOrder_orderSearchColumn, conditionalOrderSearch_Bean);
        ObservableList<SearchOrder_Bean> subBillList = Order_Model.searchOrder(OrderSource.出貨子貨單, subBill_orderSearchColumn, conditionalOrderSearch_Bean);
        if(getOrderSearchColumn(OrderSource.出貨退貨單) != OrderSearchColumn.發票號碼 || (getOrderSearchColumn(OrderSource.出貨退貨單) == OrderSearchColumn.發票號碼 && conditionalOrderSearch_Bean.getSearchKeyWord().equals("")))
            shipmentOrderList.addAll(Order_Model.searchOrder(OrderSource.出貨退貨單, getOrderSearchColumn(OrderSource.出貨退貨單), conditionalOrderSearch_Bean));

        boolean isSearchProductCode = alreadyOrder_orderSearchColumn == OrderSearchColumn.專案編號 && subBill_orderSearchColumn == OrderSearchColumn.專案編號;
        if(isSearchProductCode){
            searchProjectCodeOrder(true, OrderObject, shipmentOrderList, subBillList, conditionalOrderSearch_Bean);
        }else{
            ToolKit.sortSearchOrderNumber(shipmentOrderList);
            ToolKit.sortSubBillFromAlreadyOrder(shipmentOrderList, subBillList, Order_Model.getOrderNumberLength());
        }
        return shipmentOrderList;
    }
    private ObservableList<SearchOrder_Bean> getShipmentQuotation(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        ObservableList<SearchOrder_Bean> ShipmentOrderList = Order_Model.searchOrder(OrderSource.報價單, getOrderSearchColumn(OrderSource.報價單), conditionalOrderSearch_Bean);
        ToolKit.sortSearchOrderNumber(ShipmentOrderList);
        return ShipmentOrderList;
    }
    private ObservableList<SearchOrder_Bean> getAlreadyShipmentOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        OrderSearchColumn alreadyOrder_orderSearchColumn = getOrderSearchColumn(OrderSource.出貨單);
        OrderSearchColumn subBill_orderSearchColumn = getOrderSearchColumn(OrderSource.出貨子貨單);
        ObservableList<SearchOrder_Bean> shipmentOrderList = Order_Model.searchOrder(OrderSource.出貨單, alreadyOrder_orderSearchColumn, conditionalOrderSearch_Bean);
        ObservableList<SearchOrder_Bean> subBillList = Order_Model.searchOrder(OrderSource.出貨子貨單, subBill_orderSearchColumn, conditionalOrderSearch_Bean);

        boolean isSearchProductCode = alreadyOrder_orderSearchColumn == OrderSearchColumn.專案編號 && subBill_orderSearchColumn == OrderSearchColumn.專案編號;
        if(isSearchProductCode){
            searchProjectCodeOrder(true, OrderObject, shipmentOrderList, subBillList, conditionalOrderSearch_Bean);
        }else{
            shipmentOrderList.addAll(subBillList);
            ToolKit.sortSearchOrderNumber(shipmentOrderList);
        }
        return shipmentOrderList;
    }
    private ObservableList<SearchOrder_Bean> getShipmentReturnOrder(ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        conditionalOrderSearch_Bean.setSearchOrderReviewStatus(null);
        ObservableList<SearchOrder_Bean> ShipmentOrderList = Order_Model.searchOrder(OrderSource.出貨退貨單, getOrderSearchColumn(OrderSource.出貨退貨單), conditionalOrderSearch_Bean);
        ToolKit.sortSearchOrderNumber(ShipmentOrderList);
        return ShipmentOrderList;
    }
    private void searchProjectCodeOrder(boolean isSearchAlreadyOrder, OrderObject orderObject, ObservableList<SearchOrder_Bean> mainOrderList, ObservableList<SearchOrder_Bean> subBillList, ConditionalOrderSearch_Bean conditionalOrderSearch_Bean){
        LinkedHashMap<String, SearchOrder_Bean> subBillMap = new LinkedHashMap<>();
        if(mainOrderList.size() != 0){   //  找出同一個待出貨單號的子貨單
            for (SearchOrder_Bean searchOrderBean : mainOrderList) {
                if(searchOrderBean.getOrderSource() != OrderSource.進貨退貨單 && searchOrderBean.getOrderSource() != OrderSource.出貨退貨單){
                    conditionalOrderSearch_Bean.setSearchKeyWord(searchOrderBean.getWaitingOrderNumber());
                    ObservableList<SearchOrder_Bean> newSubBillList = Order_Model.searchOrder(
                            (orderObject == Order_Enum.OrderObject.廠商 ? OrderSource.進貨子貨單 : OrderSource.出貨子貨單),
                            (orderObject == Order_Enum.OrderObject.廠商 ? OrderSearchColumn.待入倉單號 : OrderSearchColumn.待出貨單號),
                            conditionalOrderSearch_Bean);
                    for (SearchOrder_Bean searchOrder_bean : newSubBillList) {
                        subBillMap.put(searchOrder_bean.getOrderNumber(), searchOrder_bean);
                    }
                }
            }
        }
        if(subBillList.size() != 0){    //  找出同一個待出貨單號的子貨單與母貨單，之後要跟母貨單的子貨單比對
            for (SearchOrder_Bean searchOrderBean : subBillList) {
                if (subBillMap.containsKey(searchOrderBean.getOrderNumber())) {
                    // 該子貨單的母貨單與其他子貨單都已經在 subBillMap 內了。
                } else {    // 須找出該子貨單的母貨單與其他子貨單
                    if(isSearchAlreadyOrder){
                        String alreadyOrderNumber = Order_Model.isSubBillOfMainOrderTransferAlready(searchOrderBean.getOrderSource(), searchOrderBean.getWaitingOrderNumber());
                        if(alreadyOrderNumber != null){
                            conditionalOrderSearch_Bean.setSearchKeyWord(alreadyOrderNumber);
                            mainOrderList.addAll(Order_Model.searchOrder(
                                    (orderObject == Order_Enum.OrderObject.廠商 ? OrderSource.進貨單 : OrderSource.出貨單),
                                    (orderObject == Order_Enum.OrderObject.廠商 ? OrderSearchColumn.進貨單號 : OrderSearchColumn.出貨單號),
                                    conditionalOrderSearch_Bean));
                        }
                    }else {
                        conditionalOrderSearch_Bean.setSearchKeyWord(searchOrderBean.getWaitingOrderNumber());
                        mainOrderList.addAll(Order_Model.searchOrder(
                                (orderObject == Order_Enum.OrderObject.廠商 ? OrderSource.待入倉單 : OrderSource.待出貨單),
                                (orderObject == Order_Enum.OrderObject.廠商 ? OrderSearchColumn.待入倉單號 : OrderSearchColumn.待出貨單號),
                                conditionalOrderSearch_Bean));
                    }
                    conditionalOrderSearch_Bean.setSearchKeyWord(searchOrderBean.getWaitingOrderNumber());
                    ObservableList<SearchOrder_Bean> newSubBillList = Order_Model.searchOrder(
                            (orderObject == Order_Enum.OrderObject.廠商 ? OrderSource.進貨子貨單 : OrderSource.出貨子貨單),
                            (orderObject == Order_Enum.OrderObject.廠商 ? OrderSearchColumn.待入倉單號 : OrderSearchColumn.待出貨單號),
                            conditionalOrderSearch_Bean);
                    for (SearchOrder_Bean searchOrder_bean : newSubBillList) {
                        subBillMap.put(searchOrder_bean.getOrderNumber(), searchOrder_bean);
                    }
                }
            }
        }
        subBillList.clear();
        for(String orderNumber : subBillMap.keySet()){
            subBillList.add(subBillMap.get(orderNumber));
        }
        ToolKit.sortSearchOrderNumber(mainOrderList);
        if(isSearchAlreadyOrder){
            ToolKit.sortSubBillFromAlreadyOrder(mainOrderList, subBillList, Order_Model.getOrderNumberLength());
        }else{
            ToolKit.sortSubBillFromWaitingShipment(mainOrderList, subBillList ,Order_Model.getOrderNumberLength());
        }
    }
    private void setSearchOrderInfo(ObservableList<SearchOrder_Bean> searchOrderList){
        TotalOrderProfit_Label.setText("0");
        TotalOrderCount_Label.setText(String.valueOf(searchOrderList.size()));
        double totalPrice = 0;
        HashMap<String, Integer> subBillMap = new HashMap<>();
        for(SearchOrder_Bean searchOrder_bean : searchOrderList) {
            if(searchOrder_bean.getOrderSource() == OrderSource.進貨子貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨子貨單){
                if(subBillMap.containsKey(searchOrder_bean.getWaitingOrderNumber())){
                    subBillMap.put(searchOrder_bean.getWaitingOrderNumber(), subBillMap.get(searchOrder_bean.getWaitingOrderNumber())+searchOrder_bean.getTotalPriceIncludeTax());
                }else{
                    subBillMap.put(searchOrder_bean.getWaitingOrderNumber(), searchOrder_bean.getTotalPriceIncludeTax());
                }
            }
            if(searchOrder_bean.getOrderSource() == OrderSource.進貨退貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨退貨單)
                totalPrice -= searchOrder_bean.getTotalPriceIncludeTax();
            else
                totalPrice += searchOrder_bean.getTotalPriceIncludeTax();
        }
        for(String subBillNumber : subBillMap.keySet()){
            totalPrice = totalPrice - subBillMap.get(subBillNumber);
        }
        subBillMap.clear();
        TotalOrderPrice_Label.setText(ToolKit.fmtMicrometer(totalPrice));
    }
    /** Button Mouse Clicked - 合併貨單 */
    @FXML protected void combinedOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<SearchOrder_Bean> selectOrderList = ComponentToolKit.getSearchOrderSelectList(TableView);
            if(selectOrderList.size() <= 1) {
                DialogUI.MessageDialog("※ 請選擇至少兩筆" + (OrderObject == Order_Enum.OrderObject.廠商 ? "採購單" : "報價單"));
                return;
            }
            SearchOrder_Bean firstSelectOrder_Bean = selectOrderList.get(0);
            if(DialogUI.ConfirmDialog(
                "是否合併所選" + (OrderObject == Order_Enum.OrderObject.廠商 ? "採購單" : "報價單") + " ?\n" +
                        OrderObject.name()+ "：(" + firstSelectOrder_Bean.getObjectID() + ") " + firstSelectOrder_Bean.getObjectName(),
                true,false,0,0)){
                OrderSource combine_OrderSource = (OrderObject == Order_Enum.OrderObject.廠商 ? Order_Enum.OrderSource.採購單 : Order_Enum.OrderSource.報價單);
                if(!isSearchOrderListExistWaitingOrder(selectOrderList)){
                    try{
                        Order_Bean mainOrder_Bean = combinedOrderProduct(combine_OrderSource, selectOrderList);
                        if(mainOrder_Bean == null){
                            DialogUI.AlarmDialog("合併貨單異常!!");
                            return;
                        }
                        if(Order_Model.insertOrder(combine_OrderSource, mainOrder_Bean,null,false,false)) {
                            if(mainOrder_Bean.getOrderPictureList() != null){
                                for(String base64Format : mainOrder_Bean.getOrderPictureList()){
                                    Order_Model.insertOrderPicture(mainOrder_Bean.getOrderID(), base64Format, combine_OrderSource);
                                }
                            }
                            boolean isCombinedProductGroup = OrderObject == Order_Enum.OrderObject.客戶 &&
                                    DialogUI.ConfirmDialog("已合併貨單，是否合併商品群組 ?",true,true,8,14);
                            if(isCombinedProductGroup){
                                HashMap<Integer,HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> mainProductGroupMap = generateCombinedOrderProductGroup(mainOrder_Bean, selectOrderList);
                                if(mainProductGroupMap != null && !Order_Model.updateItemsProductGroup(null, mainProductGroupMap)){
                                    DialogUI.AlarmDialog("商品群組合併異常!!");
                                }
                            }

                            StringBuilder logText = new StringBuilder();
                            for (SearchOrder_Bean searchOrder_Bean : selectOrderList) {
                                if (searchOrder_Bean != firstSelectOrder_Bean)
                                    logText.append("【").append(searchOrder_Bean.getOrderNumber()).append("】");
                            }
                            ERPApplication.Logger.info((OrderObject == Order_Enum.OrderObject.廠商 ? "採購單" : "報價單") + "合併資訊:" +
                                    logText + " → → → " + firstSelectOrder_Bean.getOrderNumber());
                            boolean hidingCombinedOrder = DialogUI.ConfirmDialog("合併完成，是否封存被合併貨單 ?", true, true, 7, 9);
                            if (hidingCombinedOrder) {
                                for (SearchOrder_Bean searchOrder_Bean : selectOrderList) {
                                    OrderStatus orderStatus = Order_Enum.OrderStatus.併單;
                                    if (Order_Model.modifyOrderStatus(combine_OrderSource, searchOrder_Bean.getId(), orderStatus))
                                        ERPApplication.Logger.info("【成功】修改貨單狀態 【" + combine_OrderSource.name() + "】" + searchOrder_Bean.getOrderNumber() + " - " + orderStatus.name());
                                    else
                                        ERPApplication.Logger.info("【失敗】修改貨單狀態 【" + combine_OrderSource.name() + "】" + searchOrder_Bean.getOrderNumber() + " - " + orderStatus.name());
                                }
                            }
                            callOrderSearch();
                            if (DialogUI.ConfirmDialog("是否開啟合併貨單 ?", true, false, 0, 0)) {
                                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(mainOrder_Bean.getOrderSource(), mainOrder_Bean);
                                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
                            }
                        }else{
                            DialogUI.AlarmDialog("※ 合併失敗!");
                        }
                    }catch (Exception Ex){
                        ERPApplication.Logger.catching(Ex);
                        DialogUI.ExceptionDialog(Ex);
                    }
                }
            }
        }
    }
    private boolean isSearchOrderListExistWaitingOrder(ObservableList<SearchOrder_Bean> selectOrderList){
        StringBuilder rejectCombinedAlert = new StringBuilder("合併被拒，存在" + (OrderObject == Order_Enum.OrderObject.廠商 ? "待入倉單" : "待出貨單"));
        boolean isExistWaitingOrder = false;
        for(SearchOrder_Bean searchOrder_Bean : selectOrderList){
            if(searchOrder_Bean.getWaitingOrderNumber() != null || searchOrder_Bean.getWaitingOrderDate() != null){
                rejectCombinedAlert.append("\n").append(searchOrder_Bean.getOrderNumber());
                isExistWaitingOrder = true;
            }
        }
        if(isExistWaitingOrder)
            DialogUI.AlarmDialog(rejectCombinedAlert.toString());
        return isExistWaitingOrder;
    }
    private Order_Bean combinedOrderProduct(OrderSource combine_OrderSource, ObservableList<SearchOrder_Bean> selectOrderList) throws Exception {
        HashMap<String, String> productRemarkMap = new HashMap<>();
        Order_Bean mainOrder_Bean = null;
        ObservableList<OrderProduct_Bean> mainProductList = FXCollections.observableArrayList();
        ArrayList<String> mainPictureList = null;
        for(int index = 0; index < selectOrderList.size(); index++){
            SearchOrder_Bean searchOrder_bean = selectOrderList.get(index);
            ObservableList<OrderProduct_Bean> productList = Order_Model.getOrderItems(combine_OrderSource, searchOrder_bean.getId());
            ArrayList<HashMap<Boolean,String>> orderPictureList = Order_Model.getOrderPicture(searchOrder_bean.getId(), combine_OrderSource);
            if(index == 0){
                mainOrder_Bean = new Order_Bean();
                mainOrder_Bean.setNowOrderNumber(searchOrder_bean.getOrderNumber());
                mainOrder_Bean.setOldOrderNumber(searchOrder_bean.getOrderNumber());
                mainOrder_Bean = Order_Model.getOrderInfo(combine_OrderSource, mainOrder_Bean);
                mainProductList.addAll(productList);
            }
            if(orderPictureList != null){
                if(mainPictureList == null){
                    mainPictureList = new ArrayList<>();
                }
                for(HashMap<Boolean,String> pictureMap : orderPictureList){
                    mainPictureList.add(pictureMap.entrySet().iterator().next().getValue());
                }
            }
            if(index != 0){
                for(OrderProduct_Bean orderProduct_Bean : productList){
                    boolean isExist = false;
                    String remark;
                    for(OrderProduct_Bean mainOrderProduct_Bean : mainProductList){
                        if(orderProduct_Bean.getISBN().equals(mainOrderProduct_Bean.getISBN())){
                            isExist = true;
                            remark = searchOrder_bean.getOrderNumber();
                            mainOrderProduct_Bean.setQuantity(mainOrderProduct_Bean.getQuantity() + orderProduct_Bean.getQuantity());
                            if(OrderObject == Order_Enum.OrderObject.廠商) {
                                mainOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(mainOrderProduct_Bean.getQuantity() * mainOrderProduct_Bean.getBatchPrice()));
                                if(orderProduct_Bean.getBatchPrice() != mainOrderProduct_Bean.getBatchPrice())
                                    remark = remark + " $" + orderProduct_Bean.getBatchPrice() + " *" + orderProduct_Bean.getQuantity();
                                else
                                    remark = remark + "*" + orderProduct_Bean.getQuantity();
                            }else if(OrderObject == Order_Enum.OrderObject.客戶) {
                                mainOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(mainOrderProduct_Bean.getQuantity() * mainOrderProduct_Bean.getSinglePrice()));
                                if(orderProduct_Bean.getSinglePrice() != mainOrderProduct_Bean.getSinglePrice())
                                    remark = remark + " $" + orderProduct_Bean.getSinglePrice() + " *" + orderProduct_Bean.getQuantity();
                                else
                                    remark = remark + "*" + orderProduct_Bean.getQuantity();
                            }
                            if(!productRemarkMap.containsKey(orderProduct_Bean.getISBN()))
                                productRemarkMap.put(orderProduct_Bean.getISBN(), "併單備註：" + remark);
                            else
                                productRemarkMap.put(orderProduct_Bean.getISBN(), productRemarkMap.get(orderProduct_Bean.getISBN()) + "、" + remark);
                            break;
                        }
                    }
                    if(!isExist){   //  新增
                        ObservableList<OrderProduct_Bean> fineProductList = Order_Model.getProduct(
                                OrderObject,
                                Order_Enum.ProductSearchColumn.ISBN,
                                mainOrder_Bean.getObjectID(),
                                orderProduct_Bean.getISBN());
                        if(fineProductList.size() == 0){
                            DialogUI.AlarmDialog("不存在商品：" + orderProduct_Bean.getISBN() + "  " + orderProduct_Bean.getProductName());
                            ERPApplication.Logger.warn("不存在商品：" + orderProduct_Bean.getISBN() + "  " + orderProduct_Bean.getProductName());
                            return null;
                        }else{
                            OrderProduct_Bean copyProductBean = ToolKit.CopyProductBean(orderProduct_Bean);
                            copyProductBean.setItemNumber(mainProductList.size()+1);
                            mainProductList.add(copyProductBean);
                            remark = searchOrder_bean.getOrderNumber() +
                                    " $" + ((OrderObject == Order_Enum.OrderObject.廠商) ? orderProduct_Bean.getBatchPrice() : orderProduct_Bean.getSinglePrice()) +
                                    " *" + orderProduct_Bean.getQuantity();
                            if(!productRemarkMap.containsKey(orderProduct_Bean.getISBN()))
                                productRemarkMap.put(orderProduct_Bean.getISBN(), "併單備註：" + remark);
                            else
                                productRemarkMap.put(orderProduct_Bean.getISBN(), productRemarkMap.get(orderProduct_Bean.getISBN()) + "、" + remark);
                            /*
                            OrderProduct_Bean newOrderProduct_Bean = fineProductList.get(0);
                            newOrderProduct_Bean.setItemNumber(mainProductList.size()+1);
                            newOrderProduct_Bean.setQuantity(orderProduct_Bean.getQuantity());
                            newOrderProduct_Bean.setPriceAmount(
                                    ToolKit.RoundingInteger(newOrderProduct_Bean.getQuantity() *
                                            (OrderObject == Order_Enum.OrderObject.廠商 ? newOrderProduct_Bean.getBatchPrice() : newOrderProduct_Bean.getSinglePrice())
                                    ));
                            newOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(newOrderProduct_Bean.getQuantity() * newOrderProduct_Bean.getSinglePrice()));
                            mainProductList.add(newOrderProduct_Bean);
                            remark = searchOrder_bean.getOrderNumber() +
                                    " $" + ((OrderObject == Order_Enum.OrderObject.廠商) ? orderProduct_Bean.getBatchPrice() : orderProduct_Bean.getSinglePrice()) +
                                    " *" + orderProduct_Bean.getQuantity();
                            if(!productRemarkMap.containsKey(orderProduct_Bean.getISBN()))
                                productRemarkMap.put(orderProduct_Bean.getISBN(), "併單備註：" + remark);
                            else
                                productRemarkMap.put(orderProduct_Bean.getISBN(), productRemarkMap.get(orderProduct_Bean.getISBN()) + "、" + remark);*/
                        }
                    }
                }
            }
        }
        for(String ISBN : productRemarkMap.keySet()){
            for(OrderProduct_Bean mainOrderProduct_Bean: mainProductList){
                if(mainOrderProduct_Bean.getISBN().equals(ISBN)){
                    mainOrderProduct_Bean.setRemark("【" + productRemarkMap.get(ISBN) + "】" + mainOrderProduct_Bean.getRemark());
                    break;
                }
            }
        }
        productRemarkMap.clear();
        if(mainOrder_Bean != null){
            mainOrder_Bean.setProductList(mainProductList);
            mainOrder_Bean.setOrderPictureList(mainPictureList);
            return generateCombinedOrderBean(mainOrder_Bean);
        }else
            return null;
    }
    private Order_Bean generateCombinedOrderBean(Order_Bean mainOrder_Bean){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setEstablishSource(EstablishSource.人工建立);
        Order_Bean.setOrderSource(mainOrder_Bean.getOrderSource());
        Order_Bean.setOrderDate(ToolKit.getToday("yyyy-MM-dd"));
        Order_Bean.setNowOrderNumber(Order_Model.generateNewestOrderNumberOfEstablishOrder(mainOrder_Bean.getOrderSource(), Order_Enum.generateOrderNumberMethod.日期, Order_Bean.getOrderDate(),false));
        Order_Bean.setObjectID(mainOrder_Bean.getObjectID());
        Order_Bean.setObjectName(mainOrder_Bean.getObjectName());
        Order_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.未沖帳);
        Order_Bean.setIsBorrowed(Order_Enum.OrderBorrowed.未借測.value());
        Order_Bean.setIsCheckout(Order_Enum.CheckoutStatus.未結帳.value());
        Order_Bean.setExistPicture(mainOrder_Bean.isExistPicture());
        Order_Bean.setProductList(mainOrder_Bean.getProductList());
        Order_Bean.setNumberOfItems(String.valueOf(Order_Bean.getProductList().size()));

        ObservableList<ObjectInfo_Bean> objectList = Order_Model.getObjectFromSearchColumn(OrderObject, Order_Enum.ObjectSearchColumn.客戶編號, Order_Bean.getObjectID());
        ObjectInfo_Bean objectInfo_Bean = objectList.get(0);
        mainOrder_Bean.setOrderTaxStatus(Order_Enum.OrderTaxStatus.values()[objectInfo_Bean.getOrderTax().ordinal()]);

        int tax = 0;
        int discount = ToolKit.RoundingInteger(mainOrder_Bean.getDiscount());
        int totalPriceNoneTax = ToolKit.RoundingInteger(Order_Model.calculateOrderTotalPrice_NoneTax(false, Order_Bean.getProductList()));
        int totalPriceIncludeTax = Order_Model.calculateOrderTotalPrice_IncludeTax(mainOrder_Bean.getOrderTaxStatus(), totalPriceNoneTax);
        if (totalPriceNoneTax > 0) {
            tax = totalPriceIncludeTax - totalPriceNoneTax;
            totalPriceIncludeTax = totalPriceIncludeTax - discount;
        } else
            totalPriceIncludeTax = 0;
        Order_Bean.setTotalPriceNoneTax(ToolKit.RoundingString(totalPriceNoneTax));
        Order_Bean.setTax(ToolKit.RoundingString(tax));
        Order_Bean.setDiscount(ToolKit.RoundingString(discount));
        Order_Bean.setTotalPriceIncludeTax(ToolKit.RoundingString(totalPriceIncludeTax));

        Order_Bean.setOrderRemark("");
        Order_Bean.setCashierRemark("");
        Order_Bean.setPurchaserName("");
        Order_Bean.setPurchaserTelephone("");
        Order_Bean.setPurchaserCellphone("");
        Order_Bean.setPurchaserAddress("");
        Order_Bean.setRecipientName("");
        Order_Bean.setRecipientTelephone("");
        Order_Bean.setRecipientCellphone("");
        Order_Bean.setRecipientAddress("");
        Order_Bean.setProjectCode("");
        Order_Bean.setProjectName("");
        Order_Bean.setProjectQuantity("");
        Order_Bean.setProjectUnit("");
        Order_Bean.setProjectPriceAmount("");
        Order_Bean.setProjectTotalPriceNoneTax("");
        Order_Bean.setProjectTax("");
        Order_Bean.setProjectTotalPriceIncludeTax("");
        Order_Bean.setProjectDifferentPrice("");
        Order_Bean.setIsCheckout(Order_Enum.CheckoutStatus.未結帳.value());
        Order_Bean.setWaitingOrderDate(null);
        Order_Bean.setWaitingOrderNumber(null);
        Order_Bean.setAlreadyOrderDate(null);
        Order_Bean.setAlreadyOrderNumber(null);
        Order_Bean.setEstablishSource(EstablishSource.系統建立);

        Order_Bean.setOrderPictureList(mainOrder_Bean.getOrderPictureList());

        Order_Bean.setReviewStatusMap(Order_Enum.ReviewObject.貨單商品,Order_Enum.ReviewStatus.無);
        Order_Bean.setReviewStatusMap(Order_Enum.ReviewObject.商品群組,Order_Enum.ReviewStatus.無);

        HashMap<Order_Enum.OrderSource, HashMap<Integer, Boolean>> orderReferenceMap = new HashMap<>();
        orderReferenceMap.put(Order_Enum.OrderSource.報價單,new HashMap<>());
        orderReferenceMap.put(Order_Enum.OrderSource.出貨子貨單, new HashMap<>());
        Order_Bean.setOrderReferenceMap(orderReferenceMap);
        return Order_Bean;
    }
    private HashMap<Integer,HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> generateCombinedOrderProductGroup(Order_Bean mainOrder_Bean, ObservableList<SearchOrder_Bean> selectOrderList){
        ObservableList<OrderProduct_Bean> mainProductList = mainOrder_Bean.getProductList();
        for(OrderProduct_Bean orderProduct_bean : mainProductList){
            Integer item_id = Order_Model.refresh_Order_Item_Id(mainOrder_Bean.getOrderID(), orderProduct_bean);
            if(item_id != null)
                orderProduct_bean.setItem_id(item_id);
        }

        HashMap<Integer,HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> mainProductGroupMap = null;
        int mainGroup_itemNumber = 0;
        for (SearchOrder_Bean searchOrder_bean : selectOrderList) {
            HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = Order_Model.getProductGroupByOrderId(searchOrder_bean.getId());
            if (productGroupMap != null) {
                if(mainProductGroupMap == null){
                    mainProductGroupMap = new HashMap<>();
                }
                for(Integer group_itemNumber : productGroupMap.keySet()){
                    mainGroup_itemNumber++;
                    ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                    ProductGroup_Bean.setItemNumber(mainGroup_itemNumber);
                    LinkedHashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                    for(Integer item_id : itemMap.keySet()){
                        ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                        for(OrderProduct_Bean orderProduct_bean : mainProductList){
                            if(ItemGroup_Bean.getIsbn().equals(orderProduct_bean.getISBN())){
                                ItemGroup_Bean.setItem_id(orderProduct_bean.getItem_id());
                                break;
                            }
                        }
                    }
                    mainProductGroupMap.put(mainGroup_itemNumber, productGroupMap.get(group_itemNumber));
                }
            }
        }
        return mainProductGroupMap;
    }
    /** Button Mouse Clicked - 刪除貨單 */
    @FXML protected void deleteOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            boolean isHandleProductStatus = false;
            SearchOrder_Bean SearchOrder_Bean = ComponentToolKit.getSearchOrderTableViewSelectItem(TableView);
            if (SearchOrder_Bean == null) DialogUI.MessageDialog("※ 請選擇貨單 !");
            else{
                if(!SearchOrder_Bean.isOffset())    isHandleProductStatus = true;
                if(SearchOrder_Bean.isCheckout()){  DialogUI.AlarmDialog("※ 刪除失敗，此貨單已結帳 !");
                }else if (SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單) deleteWaitingShipmentAndSubBill(SearchOrder_Bean, isHandleProductStatus);
                else if(SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單)  deleteWaitingPurchaseAndSubBill(SearchOrder_Bean, isHandleProductStatus);
                else deletePurchase_Shipment_ReturnOrder(SearchOrder_Bean, isHandleProductStatus);
            }
        }
    }
    private void deleteWaitingPurchaseAndSubBill(SearchOrder_Bean SearchOrder_Bean, boolean isHandleProductStatus){
        boolean deleteStatus = false, isExistSubBill = false;
        if(SearchOrder_Bean.getOrderSource() == OrderSource.待入倉單) {
            ArrayList<Order_Bean> SubBillList = getSubBillOfOrder(OrderObject, SearchOrder_Bean);
            if(SubBillList != null){
                isExistSubBill = true;
                if(DialogUI.ConfirmDialog("※ 刪除失敗，存在子貨單 ! 是否開啟 ?",true,false,0,0)) {
                    for (Order_Bean Order_Bean : SubBillList) {
                        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(OrderSource.進貨子貨單, Order_Bean);
                        CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
                    }
                }
            }else   isExistSubBill = false;
        }
        if(!isExistSubBill && DialogUI.ConfirmDialog("※ 是否刪除此 「" + SearchOrder_Bean.getOrderSource().name() + "」",true,false,0,0)){
            if(SearchOrder_Bean.getOrderSource() == OrderSource.待入倉單)   deleteStatus = deleteWaitingPurchaseOrder(SearchOrder_Bean, isHandleProductStatus);
            else if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨子貨單) deleteStatus = deletePurchaseSubBill(SearchOrder_Bean);
            if(deleteStatus){
                ERPApplication.Logger.info("※ [成功] 刪除「" + SearchOrder_Bean.getOrderSource() + " : " + SearchOrder_Bean.getOrderNumber() + "(" + SearchOrder_Bean.getQuotationNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 刪除成功 !");
            }else{
                ERPApplication.Logger.warn("※ [失敗] 刪除「" + SearchOrder_Bean.getOrderSource() + " : " + SearchOrder_Bean.getOrderNumber() + "(" + SearchOrder_Bean.getQuotationNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 刪除失敗 !");
            }
            OrderSearch();
        }
    }
    private void deleteWaitingShipmentAndSubBill(SearchOrder_Bean SearchOrder_Bean, boolean isHandleProductStatus){
        boolean deleteStatus = false, isExistSubBill = false;
        if(SearchOrder_Bean.getOrderSource() == OrderSource.待出貨單) {
            ArrayList<Order_Bean> SubBillList = getSubBillOfOrder(OrderObject, SearchOrder_Bean);
            if(SubBillList != null){
                isExistSubBill = true;
                if(DialogUI.ConfirmDialog("※ 刪除失敗，存在子貨單 ! 是否開啟 ?",true,false,0,0)) {
                    for (Order_Bean Order_Bean : SubBillList) {
                        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(OrderSource.出貨子貨單, Order_Bean);
                        CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
                    }
                }
            }else   isExistSubBill = false;
        }
        if(!isExistSubBill && DialogUI.ConfirmDialog("※ 是否刪除此 「" + SearchOrder_Bean.getOrderSource().name() + "」",true,false,0,0)){
            if(SearchOrder_Bean.getOrderSource() == OrderSource.待出貨單)   deleteStatus = deleteWaitingShipmentOrder(SearchOrder_Bean, isHandleProductStatus);
            else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單){
                if(SearchOrder_Bean.getInvoiceNumber() != null && !SearchOrder_Bean.getInvoiceNumber().equals("")){
                    if (!SearchOrder_Bean.getInvoiceNumber().contains("廢") || !DialogUI.AlarmDialog("※ 刪除被拒，此貨單存在「作廢發票」!")) {
                        DialogUI.AlarmDialog("※ 刪除被拒，此貨單「已開立發票」 !");
                    }
                    return;
                }
                deleteStatus = deleteShipmentSubBill(SearchOrder_Bean);
            }
            if(deleteStatus){
                ERPApplication.Logger.info("※ [成功] 刪除「" + SearchOrder_Bean.getOrderSource() + " : " + SearchOrder_Bean.getOrderNumber() + "(" + SearchOrder_Bean.getQuotationNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 刪除成功 !");
            }else{
                ERPApplication.Logger.warn("※ [失敗] 刪除「" + SearchOrder_Bean.getOrderSource() + " : " + SearchOrder_Bean.getOrderNumber() + "(" + SearchOrder_Bean.getQuotationNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 刪除失敗 !");
            }
            OrderSearch();
        }
    }
    private void deletePurchase_Shipment_ReturnOrder(SearchOrder_Bean SearchOrder_Bean, boolean isHandleProductStatus){
        if(SearchOrder_Bean.getInvoiceNumber() != null && !SearchOrder_Bean.getInvoiceNumber().equals("")){
            if(SearchOrder_Bean.getInvoiceNumber().contains("廢")){
                if(DialogUI.AlarmDialog("※ 刪除被拒，此貨單存在「作廢發票」!") && !DialogUI.requestAuthorization(Authority.管理者, AuthorizedLocation.刪除作廢發票的貨單))
                    return;
            }else{
                DialogUI.AlarmDialog("※ 刪除被拒，此貨單「已開立發票」 !");
                return;
            }
        }
        Boolean deleteStatus = null;
        if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨子貨單 && DialogUI.ConfirmDialog("※ 是否刪除此 「進貨單」 ?",true,false,0,0)){
            String AlreadyOrderNumber = Order_Model.isSubBillOfMainOrderTransferAlready(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getWaitingOrderNumber());
            if(AlreadyOrderNumber != null)    DialogUI.AlarmDialog("※ 刪除失敗，主「待入倉單」已轉 「進貨」! 出貨單號 ： " + AlreadyOrderNumber);
            else{
                ObservableList<OrderProduct_Bean> ProductList = Order_Model.getOrderItems(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getId());
                if(isPurchaseInStockSmallThenZero(ProductList) && isHandleProductStatus)  DialogUI.AlarmDialog("※ 刪除失敗，刪除此進貨單後商品「庫存為負」!");
                else{
                    if(isPurchaseInStockNotEnough(ProductList) && !DialogUI.ConfirmDialog("※ 刪除後，庫存不足出貨，是否繼續刪除 ?",true,false,0,0))  return;
                    deleteStatus = deleteAlreadyPurchaseOrder(SearchOrder_Bean, ProductList, isHandleProductStatus);
                }
            }
        }else if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨單 && DialogUI.ConfirmDialog("※ 是否刪除此 「進貨單」 ?",true,false,0,0)){
            ObservableList<OrderProduct_Bean> ProductList = Order_Model.getOrderItems(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getId());
            ArrayList<Order_Bean> SubBillList = getSubBillOfOrder(OrderObject, SearchOrder_Bean);
            if(SubBillList == null){
                if(isPurchaseInStockSmallThenZero(ProductList) && isHandleProductStatus)  DialogUI.AlarmDialog("※ 刪除失敗，刪除此進貨單後商品「庫存為負」!");
                else{
                    if(isPurchaseInStockNotEnough(ProductList) && !DialogUI.ConfirmDialog("※ 刪除後，庫存不足出貨，是否繼續刪除 ?",true,false,0,0))  return;
                    deleteStatus = deleteAlreadyPurchaseOrder(SearchOrder_Bean, ProductList, isHandleProductStatus);
                }
            }else   deleteStatus = deleteAlreadyPurchaseOrder(SearchOrder_Bean, ProductList, false);

        }else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單){
            String alreadyOrderNumber = Order_Model.isSubBillOfMainOrderTransferAlready(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getWaitingOrderNumber());
            if(alreadyOrderNumber != null)    DialogUI.AlarmDialog("※ 刪除失敗，主「待出貨單」已轉 「出貨」! 出貨單號 ： " + alreadyOrderNumber);
            else if(DialogUI.ConfirmDialog("※ 是否刪除此 「出貨單」 ?",true,false,0,0))  deleteStatus = deleteAlreadyShipmentOrder(SearchOrder_Bean, isHandleProductStatus);
        }else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨單 && DialogUI.ConfirmDialog("※ 是否刪除此 「出貨單」 ?",true,false,0,0)){
            if(SearchOrder_Bean.isCheckout())   DialogUI.AlarmDialog("※ 刪除失敗，此貨單已結帳 !");
            else{
                ArrayList<Order_Bean> SubBillList = getSubBillOfOrder(OrderObject, SearchOrder_Bean);
                if (SubBillList == null)  deleteStatus = deleteAlreadyShipmentOrder(SearchOrder_Bean, isHandleProductStatus);
                else    deleteStatus = deleteAlreadyShipmentOrder(SearchOrder_Bean,false);
            }
        }else if((SearchOrder_Bean.getOrderSource() == OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == OrderSource.出貨退貨單) && DialogUI.ConfirmDialog("※ 是否刪除此 「" + SearchOrder_Bean.getOrderSource().name() + "」 ?",true,false,0,0)){
            deleteStatus = deleteReturnOrder(SearchOrder_Bean);
        }
        if(deleteStatus != null) {
            if (deleteStatus){
                if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨子貨單)
                    ERPApplication.Logger.info("※ [成功] 刪除「進貨單(子) : " + SearchOrder_Bean.getAlreadyOrderNumber() + "(" + SearchOrder_Bean.getOrderNumber() + ")」");
                else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單)
                    ERPApplication.Logger.info("※ [成功] 刪除「出貨單(子) : " + SearchOrder_Bean.getAlreadyOrderNumber() + "(" + SearchOrder_Bean.getOrderNumber() + ")」");
                else
                    ERPApplication.Logger.info("※ [成功] 刪除「" + SearchOrder_Bean.getOrderSource() + " : " + SearchOrder_Bean.getOrderNumber() + "(" + SearchOrder_Bean.getQuotationNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 刪除成功 !");
            }else{
                if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨子貨單)
                    ERPApplication.Logger.warn("※ [失敗] 刪除「進貨單(子) : " + SearchOrder_Bean.getAlreadyOrderNumber() + "(" + SearchOrder_Bean.getOrderNumber() + ")」");
                else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單)
                    ERPApplication.Logger.warn("※ [失敗] 刪除「出貨單(子) : " + SearchOrder_Bean.getAlreadyOrderNumber() + "(" + SearchOrder_Bean.getOrderNumber() + ")」");
                else
                    ERPApplication.Logger.warn("※ [失敗] 刪除「" + SearchOrder_Bean.getOrderSource() + " : " + SearchOrder_Bean.getOrderNumber() + "(" + SearchOrder_Bean.getQuotationNumber() + ")」");
                ERPApplication.Logger.info("---------------------------------------------------");
                DialogUI.MessageDialog("※ 刪除失敗 !");
            }
            OrderSearch();
        }
    }
    private ArrayList<Order_Bean> getSubBillOfOrder(OrderObject OrderObject, SearchOrder_Bean SearchOrder_Bean){
        return Order_Model.getSubBillOfOrder(OrderObject, SearchOrder_Bean.getWaitingOrderNumber());
    }
    private boolean isPurchaseInStockNotEnough(ObservableList<OrderProduct_Bean> ProductList){
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(OrderProduct_Bean.getWaitingIntoInStock() != 0){
                if(OrderProduct_Bean.getWaitingShipmentQuantity() > 0 &&
                        (OrderProduct_Bean.getInStock() - OrderProduct_Bean.getSafetyStock() - OrderProduct_Bean.getQuantity() < OrderProduct_Bean.getWaitingShipmentQuantity()))
                    return true;
            }
        }
        return false;
    }
    private boolean isPurchaseInStockSmallThenZero(ObservableList<OrderProduct_Bean> ProductList){
        for(OrderProduct_Bean OrderProduct_Bean : ProductList){
            if(OrderProduct_Bean.getQuantity() > OrderProduct_Bean.getInStock())    return true;
        }
        return false;
    }
    private boolean deleteWaitingPurchaseOrder(SearchOrder_Bean SearchOrder_Bean, boolean isHandleProductStatus){
        return Order_Model.deleteWaitingOrder(SearchOrder_Bean,isHandleProductStatus);
    }
    private boolean deletePurchaseSubBill(SearchOrder_Bean SearchOrder_Bean){
        return Order_Model.deleteOrder(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getId(), SearchOrder_Bean.getOrderNumber(),false);
    }
    private boolean deleteAlreadyPurchaseOrder(SearchOrder_Bean SearchOrder_Bean, ObservableList<OrderProduct_Bean> ProductList, boolean isHandleProductStatus){
        return Order_Model.deleteAlreadyOrder(SearchOrder_Bean,ProductList,isHandleProductStatus);
    }
    private boolean deleteWaitingShipmentOrder(SearchOrder_Bean SearchOrder_Bean, boolean isHandleProductStatus){
        return Order_Model.deleteWaitingOrder(SearchOrder_Bean,isHandleProductStatus);
    }
    private boolean deleteShipmentSubBill(SearchOrder_Bean SearchOrder_Bean){
        return Order_Model.deleteOrder(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getId(), SearchOrder_Bean.getOrderNumber(),false);
    }
    private boolean deleteAlreadyShipmentOrder(SearchOrder_Bean SearchOrder_Bean, boolean isHandleProductSaleStatus){
        return Order_Model.deleteAlreadyOrder(SearchOrder_Bean,null,isHandleProductSaleStatus);
    }
    private boolean deleteReturnOrder(SearchOrder_Bean SearchOrder_Bean){
        return Order_Model.deleteOrder(SearchOrder_Bean.getOrderSource(), SearchOrder_Bean.getId(), SearchOrder_Bean.getOrderNumber(),true);
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowSearchOrderTableViewSetting(MainStage, SearchOrderSource,this,TableViewSettingMap);
        }
    }
    /** Refresh TableView of TableColumn
     * @param TableViewSettingMap the sorting of TableColumn in TableView
     * */
    public void refreshTableView(JSONObject TableViewSettingMap){
        this.TableViewSettingMap = TableViewSettingMap;
        TableView.getColumns().clear();
        if(SearchOrderSource == Order_Enum.SearchOrderSource.採購單 || SearchOrderSource == Order_Enum.SearchOrderSource.報價單)
            TableView.getColumns().add(Select_TableColumn);
        for(String ColumnName : TableViewSettingMap.keySet()){
            /*if(ColumnName.equals("廠商編號") || ColumnName.equals("客戶編號")) {
                if(TableViewSettingMap.getBoolean(SearchOrderTableColumn.對象編號.name()))
                    TableView.getColumns().add(getTableColumn(SearchOrderTableColumn.對象編號));
                ComponentToolKit.setTableColumnVisible(getTableColumn(SearchOrderTableColumn.對象編號), TableViewSettingMap.getBoolean(SearchOrderTableColumn.對象編號.name()));
            }else if(ColumnName.equals("廠商名稱") || ColumnName.equals("客戶名稱")) {
                if(TableViewSettingMap.getBoolean(SearchOrderTableColumn.對象名稱.name()))
                    TableView.getColumns().add(getTableColumn(SearchOrderTableColumn.對象名稱));
                ComponentToolKit.setTableColumnVisible(getTableColumn(SearchOrderTableColumn.對象名稱), TableViewSettingMap.getBoolean(SearchOrderTableColumn.對象名稱.name()));
            }else {
                if(TableViewSettingMap.getBoolean(ColumnName))
                    TableView.getColumns().add(getTableColumn(SearchOrderTableColumn.valueOf(ColumnName)));
                ComponentToolKit.setTableColumnVisible(getTableColumn(SearchOrderTableColumn.valueOf(ColumnName)), TableViewSettingMap.getBoolean(ColumnName));
            }*/
            if(TableViewSettingMap.getBoolean(ColumnName))
                TableView.getColumns().add(getTableColumn(SearchOrderTableColumn.valueOf(ColumnName)));
            ComponentToolKit.setTableColumnVisible(getTableColumn(SearchOrderTableColumn.valueOf(ColumnName)), TableViewSettingMap.getBoolean(ColumnName));
        }
    }
    private TableColumn<SearchOrder_Bean,String> getTableColumn(SearchOrderTableColumn SearchOrderTableColumn){
        if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.建立來源)   return EstablishSourceColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.單別)  return OrderSourceColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.單號)  return OrderNumberColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.日期)  return OrderDateColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.對象編號)  return ObjectIDColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.對象名稱)  return ObjectNameColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.項目個數)  return NumberOfItemsColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.總金額)  return TotalPriceIncludeTaxColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.發票號碼)  return InvoiceNumberColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.專案編號)  return ProjectCodeColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.專案名稱)  return ProjectNameColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.報價單匯出廠商)  return ExportQuotationManufacturerNickNameColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.審查狀態)  return ReviewStatusColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.出納備註)   return CashierRemarkColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.專案價)  return ProjectTotalPriceIncludeTaxColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.差額)  return ProjectDifferentPriceColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.餘額)  return BalanceColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.結帳與否)  return isCheckoutColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.是否沖帳)  return isOffsetColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.圖片數量)  return pictureCountColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.報價日期)  return QuotationDateColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.報價單號)  return QuotationNumberColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.待處理貨單日期)  return WaitingOrderDateColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.待處理貨單單號)  return WaitingOrderNumberColumn;
        else if(SearchOrderTableColumn == Order_Enum.SearchOrderTableColumn.已處理貨單日期)  return AlreadyOrderDateColumn;
        else    return AlreadyOrderNumberColumn;
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        SearchOrder_Bean SearchOrder_Bean = ComponentToolKit.getSearchOrderTableViewSelectItem(TableView);
        if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent) && ComponentToolKit.getTableViewItemsSize(TableView) != 0){
            if (SearchOrder_Bean != null) {
                ERPApplication.Logger.info("開啟貨單：【" + SearchOrder_Bean.getOrderSource() + "】" + SearchOrder_Bean.getObjectID() + "   " + SearchOrder_Bean.getOrderNumber());
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(SearchOrder_Bean.getOrderSource(), setOrder_BeanInfo(SearchOrder_Bean));
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }else if(KeyPressed.isMouseLeftClicked(MouseEvent) && SearchOrder_Bean != null){
            if(SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單) {
                if (SearchOrder_Bean.getAlreadyOrderDate() == null && SearchOrder_Bean.getAlreadyOrderNumber() == null)
                    ComponentToolKit.setButtonDisable(DeleteOrder_Button, false);
                else ComponentToolKit.setButtonDisable(DeleteOrder_Button, true);
            }else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單){
                ComponentToolKit.setButtonDisable(DeleteOrder_Button, (SearchOrder_Bean.getOrderSource() == OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == OrderSource.出貨退貨單));
            }
        }
    }
    private Order_Bean setOrder_BeanInfo(SearchOrder_Bean searchOrder_bean){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setNowOrderNumber(getSearchOrder_NowOrderNumber(searchOrder_bean));
        Order_Bean.setObjectID(searchOrder_bean.getObjectID());
        Order_Bean.setObjectName(searchOrder_bean.getObjectName());
        Order_Bean.setEstablishSource(searchOrder_bean.getEstablishSource());
        Order_Bean.setWaitingOrderDate(searchOrder_bean.getWaitingOrderDate());
        Order_Bean.setWaitingOrderNumber(searchOrder_bean.getWaitingOrderNumber());
        Order_Bean.setAlreadyOrderDate(searchOrder_bean.getAlreadyOrderDate());
        Order_Bean.setAlreadyOrderNumber(searchOrder_bean.getAlreadyOrderNumber());
        Order_Bean.setIsCheckout(searchOrder_bean.isCheckout());
        Order_Bean.setOrderSource(searchOrder_bean.getOrderSource());
        Order_Bean.setExistInstallment(searchOrder_bean.getInstallment() != null);
        return Order_Bean;
    }
    private String getSearchOrder_NowOrderNumber(SearchOrder_Bean SearchOrder_Bean){
        if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.採購單 || SearchOrder_Bean.getOrderSource() == OrderSource.待入倉單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 ||
                SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.報價單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
            return SearchOrder_Bean.getQuotationNumber();
        else if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 ||
                SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
            return SearchOrder_Bean.getOrderNumber();
        return "";
    }
    private EstablishOrder_NewStage_Bean createEstablishOrderNewStageBean(OrderSource orderSource, Order_Bean order_bean){
        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
        establishOrder_NewStage_Bean.setOrderStatus(OrderStatus.有效貨單);
        establishOrder_NewStage_Bean.setOrderSource(orderSource);
        establishOrder_NewStage_Bean.setOrderExist(OrderExist.已存在);
        establishOrder_NewStage_Bean.setOrder_Bean(order_bean);
        establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
        establishOrder_NewStage_Bean.setSearchOrder_Controller(this);
        establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
        return establishOrder_NewStage_Bean;
    }
    private void showCombinedOrderComponent(boolean visible){
        combinedOrder_CheckBox.setSelected(false);
        combinedOrder_Button.setText(OrderObject == Order_Enum.OrderObject.廠商 ? "合併採購" : "合併報價");
        ComponentToolKit.setCheckBoxVisible(combinedOrder_CheckBox,visible);
        ComponentToolKit.setButtonVisible(combinedOrder_Button, visible);
    }
    private OrderSearchColumn getOrderSearchColumn(OrderSource OrderSource){
        String searchColumn = SearchColumnText.getText();
        if(!searchColumn.equals("") && searchColumn.substring(0,1).equals("#")){
            return OrderSearchColumn.專案編號;
        }else if(SearchObjectOrProjectCode_RadioButton.isSelected()){
            return OrderSearchColumn.對象編號與名稱;
        }else if(ToolKit.isDigital(searchColumn)){
            if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨子貨單){
                if(searchColumn.length() == orderNumberLength+2)    return OrderSearchColumn.子貨單號;
                else    return OrderSearchColumn.對象與專案名稱;
            }else if(searchColumn.length() == 11 && (OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)){
                if(OrderSource == Order_Enum.OrderSource.進貨退貨單) return OrderSearchColumn.進退貨單號;
                else if(OrderSource == Order_Enum.OrderSource.出貨退貨單) return OrderSearchColumn.出退貨單號;
            }else if(searchColumn.length() == orderNumberLength){
                if(OrderSource == Order_Enum.OrderSource.採購單) return OrderSearchColumn.採購單號;
                else if(OrderSource == Order_Enum.OrderSource.待入倉單) return OrderSearchColumn.待入倉單號;
                else if(OrderSource == Order_Enum.OrderSource.進貨單) return OrderSearchColumn.進貨單號;
                else if(OrderSource == Order_Enum.OrderSource.報價單) return OrderSearchColumn.報價單號;
                else if(OrderSource == Order_Enum.OrderSource.待出貨單) return OrderSearchColumn.待出貨單號;
                else if(OrderSource == Order_Enum.OrderSource.出貨單) return OrderSearchColumn.出貨單號;
                else    return OrderSearchColumn.對象與專案名稱;
            }else
                return OrderSearchColumn.對象與專案名稱;
        }else if(!ToolKit.isInvoiceFormatError(searchColumn))   return OrderSearchColumn.發票號碼;
        else    return OrderSearchColumn.對象與專案名稱;
        return null;
    }
    private void setColumnCellValueAndCheckBox(TableColumn<SearchOrder_Bean,Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<SearchOrder_Bean, Boolean> {
        final CheckBox CheckBox = new CheckBox();
        private String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);

                SearchOrder_Bean searchOrder_Bean = ComponentToolKit.getSearchOrderTableViewSelectItem(TableView, getIndex());
                if(searchOrder_Bean.getOrderStatus() != OrderStatus.有效貨單 ||
                    (searchOrder_Bean.getWaitingOrderDate() != null && searchOrder_Bean.getWaitingOrderNumber() != null)) {
                    searchOrder_Bean.setCheckBoxSelect(null);
                    setGraphic(null);
                }else {
                    setGraphic(CheckBox);
                    CheckBox.setSelected(searchOrder_Bean.isCheckBoxSelect());
                    CheckBox.setOnAction(ActionEvent -> {
                        searchOrder_Bean.setCheckBoxSelect(CheckBox.isSelected());
                        boolean isSelectAll = true;
                        ObservableList<SearchOrder_Bean> searchOrderList = ComponentToolKit.getSearchOrderTableViewItemList(TableView);
                        for(SearchOrder_Bean selectSearchOrder_Bean : searchOrderList){
                            if(selectSearchOrder_Bean.isCheckBoxSelect() != null && !selectSearchOrder_Bean.isCheckBoxSelect()) {
                                isSelectAll = false;
                                break;
                            }
                        }
                        ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,isSelectAll);
                    });
                }
            }else   setGraphic(null);
        }
    }
    private void setColumnCellValueAndTextFill_EstablishSource(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setEstablishSourceTextFill(Alignment, FontSize));
    }
    private class setEstablishSourceTextFill extends TableCell<SearchOrder_Bean, String> {
        String Alignment, FontSize;
        setEstablishSourceTextFill(String Alignment, String FontSize){
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
                SearchOrder_Bean SearchOrder_Bean = getTableView().getItems().get(getIndex());
                EstablishSource EstablishSource = SearchOrder_Bean.getEstablishSource();
                if(EstablishSource == Order_Enum.EstablishSource.系統建立)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:#ff3399");
                else    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
            }
        }
    }
    private void setTotalPriceIncludeTaxTableCell(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(tc -> {
            TableCell<SearchOrder_Bean, String> cell = new TableCell<SearchOrder_Bean, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(ToolKit.fmtMicrometer(item));
                }
                }
            };
            cell.setOnMouseEntered(e -> {
                if (!cell.isEmpty()) {
                    SearchOrder_Bean searchOrder_bean = ComponentToolKit.getSearchOrderTableViewSelectItem(TableView, cell.getIndex());
                    if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.報價單 ||
                            searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 ||
                            searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨單){
                        if(searchOrder_bean.getProductGroupTotalPriceIncludeTax() != null){
                            mouseTooltip.setText("【群組金額】" + ToolKit.fmtMicrometer(searchOrder_bean.getProductGroupTotalPriceIncludeTax()));
                            mouseTooltip.show(cell,MainStage.getX()+e.getSceneX()+10,MainStage.getY()+e.getSceneY()+50);
                        }
                    }
                }
            });
            cell.setOnMouseExited(e -> {
                if (!cell.isEmpty()) {
                    if(mouseTooltip.isShowing())
                        mouseTooltip.hide();
                }
            });
            return cell ;
        });
    }
    private void setColumnCellValueAndTextFill_OrderSource(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setOrderSourceTextFill(Alignment, FontSize));
    }
    private class setOrderSourceTextFill extends TableCell<SearchOrder_Bean, String> {
        String Alignment, FontSize;
        setOrderSourceTextFill(String Alignment, String FontSize){
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
                SearchOrder_Bean SearchOrder_Bean = getTableView().getItems().get(getIndex());

                if(SearchOrderSource == Order_Enum.SearchOrderSource.採購單 || SearchOrderSource == Order_Enum.SearchOrderSource.報價單 ||
                        SearchOrderSource == Order_Enum.SearchOrderSource.進貨退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.出貨退貨單)
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨單){
                    if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                    else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:blue");
                }else if(SearchOrderSource == Order_Enum.SearchOrderSource.待入倉與子貨單){
                    if(SearchOrder_Bean.getOrderSource() == OrderSource.待入倉單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                    else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:blue");
                }else if(SearchOrderSource == Order_Enum.SearchOrderSource.出貨單){
                    if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                    else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:blue");
                }else if(SearchOrderSource == Order_Enum.SearchOrderSource.待出貨與子貨單){
                    if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                    else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:blue");
                }else if(SearchOrderSource == Order_Enum.SearchOrderSource.進貨與進退貨單 || SearchOrderSource == Order_Enum.SearchOrderSource.出貨與出退貨單){
                    if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
                        setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                    else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單)
                        setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:blue");
                    else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
                        setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
                }
            }
        }
    }
    private void setCashierRemarkColumnMouseEvent(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(tc -> {
            TableCell<SearchOrder_Bean, String> cell = new TableCell<SearchOrder_Bean, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    setText(empty ? null : item);
                }
            };
            cell.setOnMouseEntered(e -> {
                if (!cell.isEmpty()) {
                    String cashierRemark = cell.getItem();
                    if(cashierRemark != null && !cashierRemark.equals("")){
                        mouseTooltip.setText(cashierRemark);
                        mouseTooltip.show(cell,MainStage.getX()+e.getSceneX()+10,MainStage.getY()+e.getSceneY()+50);
                    }
                }
            });
            cell.setOnMouseExited(e -> {
                if (!cell.isEmpty()) {
                    if(mouseTooltip.isShowing())
                        mouseTooltip.hide();
                }
            });
            return cell ;
        });
    }
    private void setStringPriceColumnMicrometerFormat(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setStringPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setStringPriceColumnMicrometerFormat extends TableCell<SearchOrder_Bean, String> {
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
    private void setColumnCellValueAndTextFill_InvoiceNumber(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setInvoiceNumberTextFill(Alignment, FontSize));
    }
    private class setInvoiceNumberTextFill extends TableCell<SearchOrder_Bean, String> {
        String Alignment, FontSize;
        setInvoiceNumberTextFill(String Alignment, String FontSize){
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
                SearchOrder_Bean SearchOrder_Bean = getTableView().getItems().get(getIndex());
                String invoiceNumber = SearchOrder_Bean.getInvoiceNumber();
                if(invoiceNumber != null && invoiceNumber.contains("廢"))
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
                else if(SearchOrder_Bean.getAlreadyOrderNumber() == null)
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:blue");
                else
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
            }
        }
    }
    private void setColumnCellValueAndTextFill_ProjectCode(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setProjectCodeTextFill(Alignment, FontSize));
    }
    private class setProjectCodeTextFill extends TableCell<SearchOrder_Bean, String> {
        String Alignment, FontSize;
        setProjectCodeTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText((!item.equals("") ? "#" : "") + item);
            }
        }
    }
    private void setReviewStatusTableCell(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(tc -> {
            TableCell<SearchOrder_Bean, String> cell = new TableCell<SearchOrder_Bean, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        SearchOrder_Bean SearchOrder_Bean = getTableView().getItems().get(getIndex());
                        Order_Enum.ReviewStatus item_review_status = SearchOrder_Bean.getItemReviewStatus();
                        Order_Enum.ReviewStatus group_review_status = SearchOrder_Bean.getGroupReviewStatus();

                        if(item_review_status != Order_Enum.ReviewStatus.無){
                            if(item_review_status == Order_Enum.ReviewStatus.待審查)
                                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:#4053ec");
                            else if(item_review_status == Order_Enum.ReviewStatus.待修改)
                                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
                            else
                                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                        }else if(group_review_status != Order_Enum.ReviewStatus.無){
                            if(group_review_status == Order_Enum.ReviewStatus.待審查)
                                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:#4053ec");
                            else if(group_review_status == Order_Enum.ReviewStatus.待修改)
                                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
                            else
                                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                        }else {
                            setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
                        }
                    }
                }
            };
            cell.setOnMouseEntered(e -> {
                if (!cell.isEmpty()) {
                    SearchOrder_Bean searchOrder_bean = ComponentToolKit.getSearchOrderTableViewSelectItem(TableView, cell.getIndex());
                    Order_Enum.ReviewStatus item_review_status = searchOrder_bean.getItemReviewStatus();
                    Order_Enum.ReviewStatus group_review_status = searchOrder_bean.getGroupReviewStatus();
                    if(item_review_status != Order_Enum.ReviewStatus.無 && group_review_status != Order_Enum.ReviewStatus.無){
                        mouseTooltip.setText("【商品群組】" + group_review_status.name());
                        mouseTooltip.show(cell,MainStage.getX()+e.getSceneX()+10,MainStage.getY()+e.getSceneY()+50);
                    }
                }
            });
            cell.setOnMouseExited(e -> {
                if (!cell.isEmpty()) {
                    if(mouseTooltip.isShowing())
                        mouseTooltip.hide();
                }
            });
            return cell ;
        });
    }
    private void setColumnCellValueAndTextFill_DifferentPrice(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setDifferentPriceTextFill(Alignment, FontSize));
    }
    private class setDifferentPriceTextFill extends TableCell<SearchOrder_Bean, String> {
        String Alignment, FontSize;
        setDifferentPriceTextFill(String Alignment, String FontSize){
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
                SearchOrder_Bean SearchOrder_Bean = getTableView().getItems().get(getIndex());
                String DifferentPrice = SearchOrder_Bean.getProjectDifferentPrice();
                if(ToolKit.isDigital(DifferentPrice)){
                    double Price = Double.parseDouble(DifferentPrice);
                    if(Price < 0)  setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
                    else setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:#4053ec");
                }
            }
        }
    }
    private void setColumnCellValueAndTextFill_isCheckout(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setCheckoutTextFill(Alignment, FontSize));
    }
    private class setCheckoutTextFill extends TableCell<SearchOrder_Bean, String> {
        String Alignment, FontSize;
        setCheckoutTextFill(String Alignment, String FontSize){
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
                SearchOrder_Bean SearchOrder_Bean = getTableView().getItems().get(getIndex());
                if(SearchOrder_Bean.isCheckout())   setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:green");
                else    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
            }
        }
    }
}
