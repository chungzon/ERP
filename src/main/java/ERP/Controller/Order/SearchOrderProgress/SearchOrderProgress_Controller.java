package ERP.Controller.Order.SearchOrderProgress;

import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.Order.SearchOrderProgress_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSearchColumn;
import ERP.Enum.Order.Order_Enum.OrderSearchMethod;
import ERP.Model.Order.Order_Model;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

/** [Controller] Search order progress */
public class SearchOrderProgress_Controller {
    @FXML CheckBox OrderBorrowed_CheckBox;
    @FXML DatePicker StartDate_DatePicker, EndDate_DatePicker;
    @FXML TextField SearchColumnText;
    @FXML Label TotalOrderCount_Label, TotalOrderPrice_Label, TotalOrderProfit_Label;
    @FXML TableColumn<SearchOrderProgress_Bean,ComboBox<Order_Bean>> SubBillComboBoxColumn;
    @FXML TableColumn<SearchOrderProgress_Bean,String> ObjectIDColumn, ObjectNameColumn, QuotationColumn, QuotationDateColumn, QuotationNumberColumn, WaitingOrderDateColumn, WaitingOrderColumn, WaitingOrderNumberColumn, AlreadyOrderColumn, AlreadyOrderDateColumn, AlreadyOrderNumberColumn, ProjectCodeColumn, ProjectNameColumn, ExportQuotationManufacturerNickNameColumn, ReviewStatusColumn, isBorrowedColumn, isOffsetColumn, isCheckoutColumn;
    @FXML TableView<SearchOrderProgress_Bean> TableView;
    @FXML Label tooltip_Label;
    @FXML Tooltip tooltip;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.Tooltip Tooltip;
    private OrderObject OrderObject;
    private Order_Model Order_Model;
    private OrderSearchMethod OrderSearchMethod;
    private Stage Stage;
    public SearchOrderProgress_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallFXML = ToolKit.CallFXML;
        Tooltip = ToolKit.Tooltip;
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    /** Set object - [Enum] Order_Enum.OrderObject */
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set component of search order progress */
    public void setComponent(){
        initialTableView();
        initialComponent();
        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.searchOrderProgress());
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(ObjectIDColumn,"ObjectID", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ObjectNameColumn,"ObjectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuotationColumn,"Quotation", "CENTER-LEFT", "16", ToolKit.getQuotationBackgroundColor());
        ComponentToolKit.setColumnCellValue(QuotationDateColumn,"QuotationDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(QuotationNumberColumn,"QuotationNumber", "CENTER-LEFT", "16",null);
        if(OrderObject == Order_Enum.OrderObject.廠商)
            ComponentToolKit.setColumnCellValue(WaitingOrderColumn,"WaitingOrder", "CENTER-LEFT", "16", ToolKit.getWaitingPurchaseBackgroundColor());
        else if(OrderObject == Order_Enum.OrderObject.客戶)
            ComponentToolKit.setColumnCellValue(WaitingOrderColumn,"WaitingOrder", "CENTER-LEFT", "16", ToolKit.getWaitingShipmentBackgroundColor());
        ComponentToolKit.setColumnCellValue(WaitingOrderDateColumn,"WaitingOrderDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(WaitingOrderNumberColumn,"WaitingOrderNumber", "CENTER-LEFT", "16",null);
        setColumnCellValueAndComboBox(SubBillComboBoxColumn,"SubBillComboBox", "CENTER-LEFT", "16", ToolKit.getSubBillBackgroundColor(),this);
        ComponentToolKit.setColumnCellValue(AlreadyOrderColumn,"AlreadyOrder", "CENTER-LEFT", "16", ToolKit.getAlreadyOrderBackgroundColor());
        ComponentToolKit.setColumnCellValue(AlreadyOrderDateColumn,"AlreadyOrderDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(AlreadyOrderNumberColumn,"AlreadyOrderNumber", "CENTER-LEFT", "16",null);
        setColumnCellValueAndTextFill_ProjectCode(ProjectCodeColumn,"ProjectCode", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(ProjectNameColumn,"ProjectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ExportQuotationManufacturerNickNameColumn,"ExportQuotationManufacturerNickName", "CENTER-LEFT", "16",null);
        setColumnCellValueAndTextFill_ReviewStatus(ReviewStatusColumn,"ReviewStatus", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(isBorrowedColumn,"isBorrowed", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(isOffsetColumn,"isOffset", "CENTER-LEFT", "16",null);
        setColumnCellValueAndTextFill_isCheckout(isCheckoutColumn,"isCheckout", "CENTER-LEFT", "16");
    }
    private void initialComponent(){
        ObjectIDColumn.setText(OrderObject.name() + "編號");
        ObjectNameColumn.setText(OrderObject.name() + "名稱");
        if(OrderObject == Order_Enum.OrderObject.廠商){
            ComponentToolKit.setTableColumnVisible(ExportQuotationManufacturerNickNameColumn,false);
            QuotationColumn.setText(OrderSource.採購單.name());
            WaitingOrderColumn.setText(OrderSource.待入倉單.name());
            AlreadyOrderColumn.setText(OrderSource.進貨單.name());
            ComponentToolKit.setTableColumnVisible(SubBillComboBoxColumn,false);
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            ComponentToolKit.setTableColumnVisible(ExportQuotationManufacturerNickNameColumn,true);
            QuotationColumn.setText(OrderSource.報價單.name());
            WaitingOrderColumn.setText(OrderSource.待出貨單.name());
            AlreadyOrderColumn.setText(OrderSource.出貨單.name());
            ComponentToolKit.setTableColumnVisible(SubBillComboBoxColumn,true);
        }
        OrderBorrowed_CheckBox.setSelected(false);
        SearchColumnText.setText("");
        ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ToolKit.getMonthFirstDate());
        ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.getSearchOrderProgressTableViewItemList(TableView).clear();
        TotalOrderCount_Label.setText("--");
        TotalOrderPrice_Label.setText("--");
        TotalOrderProfit_Label.setText("--");
    }
    /** TextField Key Released - 送出 */
    @FXML protected void SearchColumnKeyReleased(KeyEvent KeyEvent){
        OrderSearchMethod = Order_Enum.OrderSearchMethod.關鍵字搜尋;
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(SearchColumnText.getText().equals("")) DialogUI.MessageDialog("※ 請輸入搜尋資料!");
            else    SearchOrderProgress();
        }
    }
    /** Button Mouse Clicked - 送出 */
    @FXML protected void SearchOrderMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            OrderSearchMethod = Order_Enum.OrderSearchMethod.關鍵字搜尋;
            if(SearchColumnText.getText().equals("")) DialogUI.MessageDialog("※ 請輸入搜尋資料!");
            else    SearchOrderProgress();
        }
    }
    /** Button Mouse Clicked - 日期搜尋 */
    @FXML protected void SearchOrderByDateMouseClicked() throws Exception{
        OrderSearchMethod = Order_Enum.OrderSearchMethod.日期搜尋;
        if(ToolKit.isDateRangeError(StartDate_DatePicker, EndDate_DatePicker)){
            ComponentToolKit.getSearchOrderProgressTableViewItemList(TableView).clear();
            DialogUI.MessageDialog("※ 日期設定錯誤");
        }else    SearchOrderProgress();
    }
    /** Call function of search order */
    public void callOrderSearch(){  SearchOrderProgress();  }
    private void SearchOrderProgress(){
        ObservableList<SearchOrderProgress_Bean> SearchOrderProgressList = getSearchOrderProgressList();
        ToolKit.sortSearchOrderProgress(SearchOrderProgressList);
        if(SearchOrderProgressList.size() != 0) TableView.setItems(SearchOrderProgressList);
        else    DialogUI.MessageDialog("※ 查無相關貨單");
        setSearchOrderProgressInfo(SearchOrderProgressList);
    }
    private ObservableList<SearchOrderProgress_Bean> getSearchOrderProgressList(){
        String SearchKeyWord = SearchColumnText.getText();
        String StartDate = ComponentToolKit.getDatePickerValue(StartDate_DatePicker, "yyyy-MM-dd");
        String EndDate = ComponentToolKit.getDatePickerValue(EndDate_DatePicker, "yyyy-MM-dd");
        ComponentToolKit.getSearchOrderProgressTableViewItemList(TableView).clear();
        return Order_Model.searchOrderProgress(OrderSearchMethod, getOrderSearchColumn(), OrderObject, OrderBorrowed_CheckBox.isSelected(), SearchKeyWord,StartDate,EndDate,false);
    }

    private void setSearchOrderProgressInfo(ObservableList<SearchOrderProgress_Bean> SearchOrderProgressList){
        /*
        TotalOrderProfit_Label.setText("0");
        TotalOrderCount_Label.setText(String.valueOf(SearchOrderProgressList.size()));
        double TotalPrice = 0;
        for(SearchOrder_Bean SearchOrder_Bean : SearchOrderProgressList)
            TotalPrice += SearchOrder_Bean.getTotalPriceIncludeTax();
        TotalOrderPrice_Label.setText(RoundingString(TotalPrice));
        */
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
            SearchOrderProgress_Bean SearchOrderProgress_Bean = ComponentToolKit.getSearchOrderProgressTableViewSelectItem(TableView);
            if(SearchOrderProgress_Bean != null) {
                OrderSource orderSource = getOrderSource(SearchOrderProgress_Bean);
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(orderSource, RecordOrder_Bean(orderSource, SearchOrderProgress_Bean),this);
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }

    }
    private OrderSource getOrderSource(SearchOrderProgress_Bean SearchOrderProgress_Bean){
        OrderSource OrderSource = null;
        if(OrderObject == Order_Enum.OrderObject.廠商) {
            if(SearchOrderProgress_Bean.getAlreadyOrderNumber() != null)    OrderSource = Order_Enum.OrderSource.進貨單;
            else if(SearchOrderProgress_Bean.getWaitingOrderNumber() != null)    OrderSource = Order_Enum.OrderSource.待入倉單;
            else    OrderSource = Order_Enum.OrderSource.採購單;
        }else if(OrderObject == Order_Enum.OrderObject.客戶) {
            if(SearchOrderProgress_Bean.getAlreadyOrderNumber() != null)    OrderSource = Order_Enum.OrderSource.出貨單;
            else if(SearchOrderProgress_Bean.getWaitingOrderNumber() != null)    OrderSource = Order_Enum.OrderSource.待出貨單;
            else    OrderSource = Order_Enum.OrderSource.報價單;
        }
        return OrderSource;
    }
    private Order_Bean RecordOrder_Bean(OrderSource orderSource, SearchOrderProgress_Bean SearchOrderProgress_Bean){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setNowOrderNumber(SearchOrderProgress_Bean.getQuotationNumber());
        Order_Bean.setOrderSource(orderSource);
        Order_Bean.setObjectID(SearchOrderProgress_Bean.getObjectID());
        Order_Bean.setObjectName(SearchOrderProgress_Bean.getObjectName());
        Order_Bean.setIsCheckout(SearchOrderProgress_Bean.isCheckout());
        Order_Bean.setWaitingOrderDate(SearchOrderProgress_Bean.getWaitingOrderDate());
        Order_Bean.setWaitingOrderNumber(SearchOrderProgress_Bean.getWaitingOrderNumber());
        Order_Bean.setAlreadyOrderDate(SearchOrderProgress_Bean.getAlreadyOrderDate());
        Order_Bean.setAlreadyOrderNumber(SearchOrderProgress_Bean.getAlreadyOrderNumber());
        return Order_Bean;
    }

    private OrderSearchColumn getOrderSearchColumn(){
        return OrderSearchColumn.對象與專案名稱;
    }

    private void setColumnCellValueAndComboBox(TableColumn TableColumn, String ColumnValue, String Alignment, String FontSize, String BackgroundColor,SearchOrderProgress_Controller SearchOrderProgress_Controller){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize, BackgroundColor);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize,SearchOrderProgress_Controller));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<SearchOrderProgress_Bean, ComboBox<Order_Bean>> {
        String Alignment, FontSize;
        SearchOrderProgress_Controller SearchOrderProgress_Controller;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize, SearchOrderProgress_Controller SearchOrderProgress_Controller){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
            this.SearchOrderProgress_Controller = SearchOrderProgress_Controller;
        }
        @Override
        protected void updateItem(ComboBox<Order_Bean> ComboBox, boolean empty) {
            super.updateItem(ComboBox, empty);
            setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";");
            if(!empty){
                ObservableList<Order_Bean> SubBillList = ComponentToolKit.getSearchOrderProgressTableViewSelectItem(TableView, getIndex()).getSubBillList();
                if(SubBillList.size() != 0){
                    if(ComboBox == null)    ComboBox = new ComboBox<>();
                    ComboBox<Order_Bean> finalComboBox = ComboBox;
                    setGraphic(finalComboBox);
                    ComponentToolKit.setSearchOrderComboBoxObj(finalComboBox);
                    finalComboBox.setItems(SubBillList);
                    finalComboBox.setOnAction(ActionEvent -> {
                        Order_Bean Order_Bean = finalComboBox.getSelectionModel().getSelectedItem();
                        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(OrderSource.出貨子貨單, Order_Bean, SearchOrderProgress_Controller);
                        CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
                    });
                }else setGraphic(null);
            }else   setGraphic(null);
        }
    }
    private EstablishOrder_NewStage_Bean createEstablishOrderNewStageBean(OrderSource orderSource, Order_Bean order_bean, SearchOrderProgress_Controller searchOrderProgress_controller){
        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
        establishOrder_NewStage_Bean.setOrderStatus(OrderStatus.有效貨單);
        establishOrder_NewStage_Bean.setOrderSource(orderSource);
        establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.已存在);
        establishOrder_NewStage_Bean.setOrder_Bean(order_bean);
        establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
        establishOrder_NewStage_Bean.setSearchOrder_Controller(null);
        establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(searchOrderProgress_controller);
        return establishOrder_NewStage_Bean;
    }
    private void setColumnCellValueAndTextFill_ProjectCode(TableColumn<SearchOrderProgress_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setProjectCodeTextFill(Alignment, FontSize));
    }
    private class setProjectCodeTextFill extends TableCell<SearchOrderProgress_Bean, String> {
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
    private void setColumnCellValueAndTextFill_ReviewStatus(TableColumn<SearchOrderProgress_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setReviewStatusTextFill(Alignment, FontSize));
    }
    private class setReviewStatusTextFill extends TableCell<SearchOrderProgress_Bean, String> {
        String Alignment, FontSize;
        setReviewStatusTextFill(String Alignment, String FontSize){
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
                SearchOrderProgress_Bean SearchOrderProgress_Bean = getTableView().getItems().get(getIndex());
                Order_Enum.ReviewStatus ReviewStatus = SearchOrderProgress_Bean.getReviewStatus();
                if(ReviewStatus == Order_Enum.ReviewStatus.待審查)
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:#4053ec");
                else if(ReviewStatus == Order_Enum.ReviewStatus.待修改)
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
            }
        }
    }
    private void setColumnCellValueAndTextFill_isCheckout(TableColumn<SearchOrderProgress_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setCheckoutTextFill(Alignment, FontSize));
    }
    private class setCheckoutTextFill extends TableCell<SearchOrderProgress_Bean, String> {
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
                SearchOrderProgress_Bean SearchOrderProgress_Bean = getTableView().getItems().get(getIndex());
                if(SearchOrderProgress_Bean.isCheckout())   setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:green");
                else    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
            }
        }
    }
}
