package ERP.Controller.Toolkit.ShowTransactionDetail;

import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.TransactionDetail.SearchTransactionDetail_Bean;
import ERP.Bean.ToolKit.TransactionDetail.TransactionDetail_Bean;
import ERP.Controller.ManageProductInfo.ManageProductInfo.ManageProductInfo_Controller;
import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.OrderSearchMethod;
import ERP.Enum.ToolKit.ToolKit_Enum.Authority;
import ERP.Enum.ToolKit.ToolKit_Enum.AuthorizedLocation;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.Product_Model;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/** [Controller] Show transaction details */
public class ShowTransactionDetail_Controller {
    @FXML private TextField ISBNText, ObjectIDText,calibrateInStockText;
    @FXML private DatePicker StartDate_DatePicker, EndDate_DatePicker;
    @FXML private CheckBox includeQuotation_CheckBox, includeWaitingOrder_CheckBox;
    @FXML private TableColumn<TransactionDetail_Bean, Integer> indexColumn, quantityColumn, numberOfItemsColumn;
    @FXML private TableColumn<TransactionDetail_Bean, Double> itemPriceColumn, totalPriceColumn;
    @FXML private TableColumn<TransactionDetail_Bean, String> orderTypeColumn, objectIDColumn, orderDateColumn, orderNumberColumn, waitingOrderDateColumn, waitingOrderNumberColumn, alreadyOrderDateColumn, alreadyOrderNumberColumn,returnOrderDateColumn, returnOrderNumberColumn, productNameColumn, checkoutStatusColumn;
    @FXML private TableView<TransactionDetail_Bean> TableView;
    @FXML private Label PurchaseQuantity_Label, ShipmentQuantity_Label, PurchaseBorrowQuantity_Label, ShipmentBorrowQuantity_Label, PurchaseReturnQuantity_Label, ShipmentReturnQuantity_Label, detailInStock_Label, infactInStock_Label;
    @FXML Label tooltip_Label;
    @FXML Tooltip tooltip;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.Tooltip Tooltip;
    private EstablishOrder_Controller EstablishOrder_Controller;
    private Product_Model Product_Model;
    private ManageProductInfo_Controller ManageProductInfo_Controller;
    private Order_Model Order_Model;
    private Stage Stage;
    public ShowTransactionDetail_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallFXML = ToolKit.CallFXML;
        Tooltip = ToolKit.Tooltip;
        Product_Model = ToolKit.ModelToolKit.getProductModel();
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }

    public void setManageProductInfo_Controller(ManageProductInfo_Controller ManageProductInfo_Controller){ this.ManageProductInfo_Controller = ManageProductInfo_Controller; }
    public void setEstablishOrder_Controller(EstablishOrder_Controller EstablishOrder_Controller){ this.EstablishOrder_Controller = EstablishOrder_Controller; }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    /** Set component of show transaction details */
    public void setComponent(){
        InitialTableView();
        ComponentToolKit.addTextFieldLimitDigital(calibrateInStockText,false);
        ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ToolKit.getMonthFirstDate());
        ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.addTextFieldLimitDigital(ISBNText,false);
        ComponentToolKit.addKeyWordTextLimitLength(ISBNText, 13);
        ISBNText.setText(ManageProductInfo_Controller != null ? ManageProductInfo_Controller.getSelectProductBean().getISBN() : EstablishOrder_Controller.getSelectProductBean().getISBN());
        SearchTransactionDetail(OrderSearchMethod.關鍵字搜尋,null,null,null);
        calculateQuantity(null);
        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.transactionDetail());
    }
    private void InitialTableView(){
        setColumnCellIntegerValueAndTextFill_OrderType(indexColumn, "index","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(orderTypeColumn,"orderType","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(objectIDColumn, "objectID","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(orderDateColumn, "orderDate","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(orderNumberColumn, "orderNumber","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(waitingOrderDateColumn, "waitingOrderDate","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(waitingOrderNumberColumn, "waitingOrderNumber","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(alreadyOrderDateColumn, "alreadyOrderDate","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(alreadyOrderNumberColumn, "alreadyOrderNumber","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(returnOrderDateColumn, "returnOrderDate","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(returnOrderNumberColumn, "returnOrderNumber","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(productNameColumn, "productName","CENTER-LEFT","16");
        setColumnCellIntegerValueAndTextFill_OrderType(quantityColumn, "quantity","CENTER-LEFT","16");
        setColumnCellDoubleValueAndTextFill_OrderType(itemPriceColumn, "itemPrice","CENTER-LEFT","16");
        setColumnCellIntegerValueAndTextFill_OrderType(numberOfItemsColumn, "numberOfItems","CENTER-LEFT","16");
        setColumnCellDoubleValueAndTextFill_OrderType(totalPriceColumn, "totalPriceNoneTax","CENTER-LEFT","16");
        setColumnCellStringValueAndTextFill_OrderType(checkoutStatusColumn, "isCheckout","CENTER-LEFT","16");
    }
    @FXML protected void closeMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))   Stage.close();
    }
    @FXML protected void ObjectIDKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String ObjectID = ObjectIDText.getText();
            if(ObjectID == null || ObjectID.equals(""))
                DialogUI.MessageDialog("※ 請輸入廠商或客戶編號");
            else{
                SearchTransactionDetail(OrderSearchMethod.關鍵字搜尋,ObjectID,null,null);
                calculateQuantity(null);
            }
        }
    }
    @FXML protected void ObjectIDSearchMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String ObjectID = ObjectIDText.getText();
            if(ObjectID == null || ObjectID.equals(""))
                DialogUI.MessageDialog("※ 請輸入廠商或客戶編號");
            else{
                SearchTransactionDetail(OrderSearchMethod.關鍵字搜尋,ObjectID,null,null);
                calculateQuantity(null);
            }
        }
    }
    @FXML protected void includeQuotationOnAction(){
        boolean isIncludeQuotation = includeQuotation_CheckBox.isSelected();
        ComponentToolKit.setCheckBoxSelect(includeWaitingOrder_CheckBox, isIncludeQuotation);
        ComponentToolKit.setCheckBoxDisable(includeWaitingOrder_CheckBox, isIncludeQuotation);
    }
    /** Button Mouse Clicked - 日期查詢 */
    @FXML protected void DateSearchMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if (ToolKit.isDateRangeError(StartDate_DatePicker, EndDate_DatePicker)) {
                ComponentToolKit.getTransactionDetailTableViewList(TableView).clear();
                DialogUI.MessageDialog("※ 日期設定錯誤");
            } else {
                String objectID = ObjectIDText.getText();
                if(objectID.equals(""))
                    objectID = null;
                String startDate = ComponentToolKit.getDatePickerValue(StartDate_DatePicker, "yyyy-MM-dd");
                String endDate = ComponentToolKit.getDatePickerValue(EndDate_DatePicker, "yyyy-MM-dd");
                SearchTransactionDetail(OrderSearchMethod.日期搜尋, objectID, startDate, endDate);
                calculateQuantity(null);
            }
        }
    }
    private void SearchTransactionDetail(OrderSearchMethod OrderSearchMethod, String objectID, String startDate, String endDate){
        SearchTransactionDetail_Bean transactionDetail_bean = new SearchTransactionDetail_Bean();
        transactionDetail_bean.setOrderSearchMethod(OrderSearchMethod);
        transactionDetail_bean.setISBN(ISBNText.getText());
        transactionDetail_bean.setGetQuotation(includeQuotation_CheckBox.isSelected());
        transactionDetail_bean.setGetWaitingOrder(includeWaitingOrder_CheckBox.isSelected());
        transactionDetail_bean.setObjectID(objectID);
        transactionDetail_bean.setStartDate(startDate);
        transactionDetail_bean.setEndDate(endDate);

        ComponentToolKit.getTransactionDetailTableViewList(TableView).clear();
        ObservableList<TransactionDetail_Bean> TransactionDetailList = Order_Model.getTransactionDetail(transactionDetail_bean);
        if(TransactionDetailList.size() != 0)   TableView.setItems(TransactionDetailList);
        else    DialogUI.MessageDialog("※ 查無相關交易明細");
    }
    private void calculateQuantity(Integer selectIndex){
        int purchaseQuantity = 0, purchaseBorrowQuantity = 0, purchaseReturnQuantity = 0, shipmentQuantity = 0, shipmentBorrowQuantity = 0, shipmentReturnQuantity = 0, inStockQuantity;
        ObservableList<TransactionDetail_Bean> transactionDetailList = ComponentToolKit.getTransactionDetailTableViewList(TableView);
        if(selectIndex == null){
            selectIndex = transactionDetailList.size();
        }else{
            selectIndex++;
        }
        for(int index = 0; index < selectIndex; index++){
            TransactionDetail_Bean transactionDetail_bean = transactionDetailList.get(index);
            if(transactionDetail_bean.isOffset()){
                if(transactionDetail_bean.getOffsetOrderStatus() == Order_Enum.OffsetOrderStatus.全沖帳){
                    continue;
                }else if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單){
                    continue;
                }
            }
            if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨單 ||
                    (transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 && transactionDetail_bean.getAlreadyOrderNumber() != null)){
                if(transactionDetail_bean.isBorrowed().value()){
                    purchaseBorrowQuantity = purchaseBorrowQuantity + transactionDetail_bean.getQuantity();
                }else{
                    purchaseQuantity = purchaseQuantity + transactionDetail_bean.getQuantity();
                }
            }else if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單){
                purchaseReturnQuantity = purchaseReturnQuantity + transactionDetail_bean.getQuantity();
            }

            if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨單 ||
                    (transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 && transactionDetail_bean.getAlreadyOrderNumber() != null)){
                if(transactionDetail_bean.isBorrowed().value()){
                    shipmentBorrowQuantity = shipmentBorrowQuantity + transactionDetail_bean.getQuantity();
                }else{
                    shipmentQuantity = shipmentQuantity + transactionDetail_bean.getQuantity();
                }
            }else if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單){
                shipmentReturnQuantity = shipmentReturnQuantity + transactionDetail_bean.getQuantity();
            }
        }
        inStockQuantity = (purchaseQuantity + purchaseBorrowQuantity - purchaseReturnQuantity) - (shipmentQuantity + shipmentBorrowQuantity - shipmentReturnQuantity);
        PurchaseQuantity_Label.setText(String.valueOf(purchaseQuantity));
        PurchaseBorrowQuantity_Label.setText(String.valueOf(purchaseBorrowQuantity));
        PurchaseReturnQuantity_Label.setText(String.valueOf(purchaseReturnQuantity));
        ShipmentQuantity_Label.setText(String.valueOf(shipmentQuantity));
        ShipmentBorrowQuantity_Label.setText(String.valueOf(shipmentBorrowQuantity));
        ShipmentReturnQuantity_Label.setText(String.valueOf(shipmentReturnQuantity));
        detailInStock_Label.setText(String.valueOf(inStockQuantity));
        infactInStock_Label.setText(ManageProductInfo_Controller != null ? ManageProductInfo_Controller.getSelectProductBean().getInStock() : String.valueOf(EstablishOrder_Controller.getSelectProductBean().getInStock()));
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent mouseEvent){
        TransactionDetail_Bean transactionDetail_bean = ComponentToolKit.getTransactionDetailTableViewSelectItem(TableView);
        if(transactionDetail_bean == null){
            return;
        }
        if (KeyPressed.isMouseLeftDoubleClicked(mouseEvent)) {
            Order_Bean Order_Bean = new Order_Bean();
            if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
                Order_Bean.setNowOrderNumber(transactionDetail_bean.getReturnOrderNumber());
            else
                Order_Bean.setNowOrderNumber(transactionDetail_bean.getOrderNumber());
            Order_Bean.setObjectID(transactionDetail_bean.getObjectID());
            Order_Bean.setObjectName(transactionDetail_bean.getObjectName());
            Order_Bean.setWaitingOrderDate(transactionDetail_bean.getWaitingOrderDate());
            Order_Bean.setWaitingOrderNumber(transactionDetail_bean.getWaitingOrderNumber());
            Order_Bean.setAlreadyOrderDate(transactionDetail_bean.getAlreadyOrderDate());
            Order_Bean.setAlreadyOrderNumber(transactionDetail_bean.getAlreadyOrderNumber());
            Order_Bean.setIsCheckout(transactionDetail_bean.getIsCheckout());
            Order_Bean.setIsOffset(transactionDetail_bean.getOffsetOrderStatus());
            OrderObject OrderObject = null;
            if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.採購單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單)
                OrderObject = Order_Enum.OrderObject.廠商;
            else if(transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.報價單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨單 || transactionDetail_bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
                OrderObject = Order_Enum.OrderObject.客戶;

            EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
            establishOrder_NewStage_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
            establishOrder_NewStage_Bean.setOrderSource(transactionDetail_bean.getOrderSource());
            establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.已存在);
            establishOrder_NewStage_Bean.setOrder_Bean(Order_Bean);
            establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
            establishOrder_NewStage_Bean.setSearchOrder_Controller(null);
            establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
            CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
        }else if(KeyPressed.isMouseLeftClicked(mouseEvent)){
            calculateQuantity(ComponentToolKit.getTransactionDetailTableViewSelectIndex(TableView));
        }
    }
    @FXML protected void calibrateInStockKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)) {
            calibrateInStock();
        }
    }
    @FXML protected void calibrateInStockMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            calibrateInStock();
        }
    }
    private void calibrateInStock(){
        String calibrateInStock = calibrateInStockText.getText();
        if(calibrateInStock == null || calibrateInStock.equals("")) {
            DialogUI.MessageDialog("※ 請輸入欲校正存量");
            return;
        }else if(!DialogUI.requestAuthorization(Authority.用戶, AuthorizedLocation.校正存量))
            return;
        if(DialogUI.ConfirmDialog("※ 確定校正存量 ?",true,false,0,0)){
            String ISBN = ManageProductInfo_Controller != null ? ManageProductInfo_Controller.getSelectProductBean().getISBN() : EstablishOrder_Controller.getSelectProductBean().getISBN();
            int newInStock = Integer.parseInt(calibrateInStock);
            if(Product_Model.updateProductInStock(ISBN, newInStock)){
                infactInStock_Label.setText(calibrateInStock);
                if(ManageProductInfo_Controller != null && EstablishOrder_Controller == null)   ManageProductInfo_Controller.refreshProductInStockByTransactionDetail(newInStock);
                else if(ManageProductInfo_Controller == null && EstablishOrder_Controller != null)  EstablishOrder_Controller.refreshProductInStockByTransactionDetail(newInStock);
                calibrateInStockText.setText("");
                DialogUI.MessageDialog("※ 修改成功");
            }else
                DialogUI.MessageDialog("※ 修改失敗");
        }
    }
    private void setColumnCellStringValueAndTextFill_OrderType(TableColumn<TransactionDetail_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setCellFactory(column -> new setStringTextFill(Alignment, FontSize));
    }
    private class setStringTextFill extends TableCell<TransactionDetail_Bean, String> {
        String Alignment, FontSize;
        setStringTextFill(String Alignment, String FontSize){
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
                TransactionDetail_Bean TransactionDetail_Bean = getTableView().getItems().get(getIndex());
                String OrderType = TransactionDetail_Bean.getOrderType();
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:" + getTextFillColor(OrderType));
            }
        }
    }

    private void setColumnCellIntegerValueAndTextFill_OrderType(TableColumn<TransactionDetail_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setCellFactory(column -> new setIntegerTextFill(Alignment, FontSize));
    }
    private class setIntegerTextFill extends TableCell<TransactionDetail_Bean, Integer> {
        String Alignment, FontSize;
        setIntegerTextFill(String Alignment, String FontSize){
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
                TransactionDetail_Bean TransactionDetail_Bean = getTableView().getItems().get(getIndex());
                String OrderType = TransactionDetail_Bean.getOrderType();
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:" + getTextFillColor(OrderType));
            }
        }
    }
    private void setColumnCellDoubleValueAndTextFill_OrderType(TableColumn<TransactionDetail_Bean,Double> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setCellFactory(column -> new setDoubleTextFill(Alignment, FontSize));
    }
    private class setDoubleTextFill extends TableCell<TransactionDetail_Bean, Double> {
        String Alignment, FontSize;
        setDoubleTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(String.valueOf(item));
                TransactionDetail_Bean TransactionDetail_Bean = getTableView().getItems().get(getIndex());
                String OrderType = TransactionDetail_Bean.getOrderType();
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:" + getTextFillColor(OrderType));
            }
        }
    }
    private String getTextFillColor(String OrderType){
        switch (OrderType) {
            case "採購":
                return "#7D7DFF";
            case "進貨":
            case "進貨(全沖)":
            case "進貨(部分沖)":
            case "子進貨":
            case "子進貨(沖)":
            case "子進貨(部分沖)":
                return "#143BBA";
            case "進退貨":
                return "#EB7500";
            case "報價":
                return "#4F9D9D";
            case "出貨":
            case "出貨(全沖)":
            case "出貨(部分沖)":
            case "子出貨":
            case "子出貨(全沖)":
            case "子出貨(部分沖)":
                return "#009900";
            case "出退貨":
                return "#FF3300";
            case "進貨(借測)":
            case "出貨(借測)":
                return "#CC00FF";
        }
        return "";
    }
}
