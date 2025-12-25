package ERP.Controller.ManagePayableReceivable.SearchPayableReceivable;

import ERP.Bean.ManagePayableReceivable.ConditionalSearchPayReceive_Bean;
import ERP.Bean.ManagePayableReceivable.PayableReceivable_Bean;
import ERP.Bean.ManagePayableReceivable.SearchPayableReceivable_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.SearchColumn;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PayableReceivableStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.SearchMethod;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/** [Controller] Search payable receivable*/
public class SearchPayableReceivable_Controller {
    @FXML BorderPane SearchPayableReceivable_BorderPane;
    @FXML RadioButton ObjectID_RadioButton, InvoiceNumber_RadioButton, CheckNumber_RadioButton , PayableReceivableDate_RadioButton, CheckDueDate_RadioButton;
    @FXML TextField SearchKeyWordText;
    @FXML DatePicker StartDate_DatePicker, EndDate_DatePicker;
    @FXML Label TotalOrderCount_Label, TotalCheckPrice_Label, TotalCash_Label, TotalOffsetPrice_Label;
    @FXML TableColumn<SearchPayableReceivable_Bean,Boolean> existWaitingOrder_tableColumn;
    @FXML TableColumn<SearchPayableReceivable_Bean,String> OrderNumberColumn, ObjectNickNameColumn, CheckTitleColumn, CheckNumberColumn, CheckDueDateColumn, ObjectBankNickNameColumn, RemarkColumn;
    @FXML TableColumn<SearchPayableReceivable_Bean,Integer> CheckPriceColumn, CashColumn, DepositColumn, OtherDiscountColumn, RemittanceFeeAndPostageColumn, CashDiscountColumn, OffsetPriceColumn;
    @FXML TableView<SearchPayableReceivable_Bean> TableView;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.CallConfig CallConfig;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private OrderObject OrderObject;
    private Order_Model Order_Model;
    private SearchMethod SearchMethod;
    private Stage MainStage;

    public SearchPayableReceivable_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallFXML = ToolKit.CallFXML;
        CallConfig = ToolKit.CallConfig;
        ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    /** Set object - [Enum] Order_Enum.OrderObject */
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    public void setMainStage(Stage MainStage){
        this.MainStage = MainStage;
    }
    /** Set component of search payable receivable */
    public void setComponent(){
        setUI();
        initialTableView();
        initialComponent();
        ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        findMoreThenDaysWaitingOrder(-30);
    }
    private void initialTableView(){
        existWaitingOrder_tableColumn.setText((OrderObject == Order_Enum.OrderObject.廠商 ? Order_Enum.OrderSource.待入倉單.getOrderNickName() : Order_Enum.OrderSource.待出貨單.getOrderNickName()));
        setExistWaitingOrderBackgroundColor(existWaitingOrder_tableColumn,"isExistWaitingOrder", "CENTER", "16");
        ComponentToolKit.setColumnCellValue(OrderNumberColumn,"OrderNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ObjectNickNameColumn,"ObjectNickName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(CheckTitleColumn,"CheckTitle", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(CheckNumberColumn,"CheckNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(CheckDueDateColumn,"CheckDueDate", "CENTER-LEFT", "16",null);
        setIntegerPriceColumnMicrometerFormat(CheckPriceColumn,"CheckPrice", "CENTER-LEFT", "16");

        setIntegerPriceColumnMicrometerFormat(CashColumn,"Cash", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(DepositColumn,"Deposit", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(OtherDiscountColumn,"OtherDiscount", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(RemittanceFeeAndPostageColumn,"RemittanceFeeAndPostage", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(CashDiscountColumn,"CashDiscount", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(OffsetPriceColumn,"OffsetPrice", "CENTER-LEFT", "16");

        ComponentToolKit.setColumnCellValue(ObjectBankNickNameColumn,"ObjectBankNickName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(RemarkColumn,"Remark", "CENTER-LEFT", "16",null);
    }
    private void initialComponent(){
        SearchKeyWordText.setText("");
        ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ToolKit.getMonthFirstDate());
        ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.getSearchPayableReceivableTableViewList(TableView).clear();
        TotalOrderCount_Label.setText("--");
        TotalCheckPrice_Label.setText("--");
        TotalCash_Label.setText("--");
        TotalOffsetPrice_Label.setText("--");
    }
    private void setUI(){
        if(OrderObject == Order_Enum.OrderObject.廠商)    setManufacturerUI();
        else if(OrderObject == Order_Enum.OrderObject.客戶)  setCustomerUI();
        ObjectID_RadioButton.setText(OrderObject.name() + "碼");
        ObjectNickNameColumn.setText(OrderObject.name() + "簡稱");
        ObjectBankNickNameColumn.setText(OrderObject.name() + "銀行簡稱");
    }
    private void setManufacturerUI(){
        SearchPayableReceivable_BorderPane.setStyle("-fx-background-color: " + ToolKit.getPayableBackgroundColor());
        PayableReceivableDate_RadioButton.setText("付款日");
        setCheckTitleTableColumnVisible(true);
    }
    private void setCustomerUI(){
        SearchPayableReceivable_BorderPane.setStyle("-fx-background-color: " + ToolKit.getReceivableBackgroundColor());
        PayableReceivableDate_RadioButton.setText("收款日");
        setCheckTitleTableColumnVisible(false);
    }
    /** RadioButton Mouse Clicked - 對象編號 */
    @FXML protected void ObjectIDMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    initialComponent();
    }
    /** RadioButton Mouse Clicked - 發票號碼 */
    @FXML protected void InvoiceNumberMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    initialComponent();
    }
    /** RadioButton Mouse Clicked - 票據號碼 */
    @FXML protected void CHeckNumberMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    initialComponent();
    }
    /** TextField Key Released - 查詢欄位 */
    @FXML protected void SearchKeyWordKeyReleased(KeyEvent KeyEvent) {
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            this.SearchMethod = PayableReceivable_Enum.SearchMethod.關鍵字搜尋;
            if(SearchKeyWordText.getText().equals("")) DialogUI.MessageDialog("※ 請輸入搜尋資料!");
            else    callSearchPayableReceivable();
        }
    }
    /** Button Mouse Clicked - 送出 */
    @FXML protected void SearchPayableReceivableMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.SearchMethod = PayableReceivable_Enum.SearchMethod.關鍵字搜尋;
            if(SearchKeyWordText.getText().equals("")) DialogUI.MessageDialog("※ 請輸入搜尋資料!");
            else    callSearchPayableReceivable();
        }
    }
    /** Button Mouse Clicked - 日期搜尋 */
    @FXML protected void SearchPayReceiveByDateMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.SearchMethod = PayableReceivable_Enum.SearchMethod.日期搜尋;
            callSearchPayableReceivable();
        }
    }
    private void findMoreThenDaysWaitingOrder(int moreThenDays){
        ConditionalSearchPayReceive_Bean conditionalSearchPayReceive_bean = new ConditionalSearchPayReceive_Bean(true);
        conditionalSearchPayReceive_bean.setOrderObject(OrderObject);
        conditionalSearchPayReceive_bean.setMoreThenDays(moreThenDays);
        ObservableList<SearchPayableReceivable_Bean> payableReceivableList = ManagePayableReceivable_Model.searchAllPayableReceivable(conditionalSearchPayReceive_bean);
        if(payableReceivableList != null && payableReceivableList.size() != 0){
            TableView.setItems(payableReceivableList);
            calculateSearchPayableReceivableInfo(payableReceivableList);
            DialogUI.MessageDialog("超過 " + Math.abs(moreThenDays)+ " 天未轉" + (OrderObject == Order_Enum.OrderObject.廠商 ? "進貨" : "出貨")+ "款項，筆數： " + payableReceivableList.size() + " 筆");
        }
    }
    /** Call function of search payable receivable */
    public void callSearchPayableReceivable() {
        SearchPayableReceivable();
    }
    private void SearchPayableReceivable() {
        ComponentToolKit.getSearchPayableReceivableTableViewList(TableView).clear();
        ConditionalSearchPayReceive_Bean conditionalSearchPayReceive_bean = createConditionalSearchPayReceive_bean();
        if(conditionalSearchPayReceive_bean != null){
            ObservableList<SearchPayableReceivable_Bean> payableReceivableList = ManagePayableReceivable_Model.searchAllPayableReceivable(conditionalSearchPayReceive_bean);
            if(payableReceivableList != null && payableReceivableList.size() != 0){
                TableView.setItems(payableReceivableList);
                calculateSearchPayableReceivableInfo(payableReceivableList);
            }else{
                ComponentToolKit.getSearchPayableReceivableTableViewList(TableView).clear();
                DialogUI.MessageDialog("※ 查無相關資料");
            }
        }
    }
    private ConditionalSearchPayReceive_Bean createConditionalSearchPayReceive_bean(){
        ConditionalSearchPayReceive_Bean conditionalSearchPayReceive_bean = new ConditionalSearchPayReceive_Bean(false);
        conditionalSearchPayReceive_bean.setOrderObject(OrderObject);
        conditionalSearchPayReceive_bean.setSearchMethod(SearchMethod);
        conditionalSearchPayReceive_bean.setSearchKeyWord(SearchKeyWordText.getText());
        if(conditionalSearchPayReceive_bean.getSearchMethod() == PayableReceivable_Enum.SearchMethod.日期搜尋){
            if(InvoiceNumber_RadioButton.isSelected() || CheckNumber_RadioButton.isSelected())
                conditionalSearchPayReceive_bean.setSearchKeyWord("");
            if(ToolKit.isDateRangeError(StartDate_DatePicker, EndDate_DatePicker)) {
                DialogUI.MessageDialog("※ 日期設定錯誤");
                return null;
            }else{
                conditionalSearchPayReceive_bean.setStartDate(ComponentToolKit.getDatePickerValue(StartDate_DatePicker,"yyyy-MM-dd"));
                conditionalSearchPayReceive_bean.setEndDate(ComponentToolKit.getDatePickerValue(EndDate_DatePicker,"yyyy-MM-dd"));
                if(PayableReceivableDate_RadioButton.isSelected()){
                    conditionalSearchPayReceive_bean.setSearchColumn(SearchColumn.收付款日);
                }else if(CheckDueDate_RadioButton.isSelected()){
                    conditionalSearchPayReceive_bean.setSearchColumn(SearchColumn.票據到期日);
                }
            }
        }else if(conditionalSearchPayReceive_bean.getSearchMethod() == PayableReceivable_Enum.SearchMethod.關鍵字搜尋){
            if(ObjectID_RadioButton.isSelected()){
                conditionalSearchPayReceive_bean.setSearchColumn(SearchColumn.對象碼);
            }else if(InvoiceNumber_RadioButton.isSelected()){
                conditionalSearchPayReceive_bean.setSearchColumn(SearchColumn.發票號碼);
            }else if(CheckNumber_RadioButton.isSelected()){
                conditionalSearchPayReceive_bean.setSearchColumn(SearchColumn.票據號碼);
            }
        }
        return conditionalSearchPayReceive_bean;
    }
    private void calculateSearchPayableReceivableInfo(ObservableList<SearchPayableReceivable_Bean> payableReceivableList){
        TotalOrderCount_Label.setText(String.valueOf(payableReceivableList.size()));
        int CheckPrice = 0, Cash = 0, OffsetPrice = 0;
        for(SearchPayableReceivable_Bean SearchPayableReceivable_Bean : payableReceivableList)
            CheckPrice += SearchPayableReceivable_Bean.getCheckPrice();
        TotalCheckPrice_Label.setText(ToolKit.fmtMicrometer(CheckPrice));
        for(SearchPayableReceivable_Bean SearchPayableReceivable_Bean : payableReceivableList)
            Cash += SearchPayableReceivable_Bean.getCash();
        TotalCash_Label.setText(ToolKit.fmtMicrometer(Cash));
        for(SearchPayableReceivable_Bean SearchPayableReceivable_Bean : payableReceivableList)
            OffsetPrice += SearchPayableReceivable_Bean.getOffsetPrice();
        TotalOffsetPrice_Label.setText(ToolKit.fmtMicrometer(OffsetPrice));
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void modifyPayableReceivableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchPayableReceivable_Bean SearchPayableReceivable_Bean = ComponentToolKit.getSearchPayableReceivableTableViewSelectItem(TableView);
            if(SearchPayableReceivable_Bean != null){
                if(DialogUI.requestAuthorization(ToolKit_Enum.Authority.用戶, ToolKit_Enum.AuthorizedLocation.應收應付帳款修改與刪除)){
                    ManagePayableReceivable_Model.getPayableReceivableOrder(SearchPayableReceivable_Bean.getOrderList());
                    CallFXML.EstablishPayableReceivable_NewStage(OrderObject, PayableReceivableStatus.修改,this,null, setPayableReceivable_Bean(SearchPayableReceivable_Bean),null);
                }
            }else   DialogUI.MessageDialog("※ 請選擇款項");
        }
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void deletePayableReceivableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchPayableReceivable_Bean searchPayableReceivable_bean = ComponentToolKit.getSearchPayableReceivableTableViewSelectItem(TableView);
            if(searchPayableReceivable_bean != null){
                if(isOrderExistCheckoutInstallment(searchPayableReceivable_bean.getOrderList())){
                    DialogUI.AlarmDialog("刪除被拒，內部款項存在【已結帳】的後期付款");
                }else if(DialogUI.requestAuthorization(ToolKit_Enum.Authority.用戶, ToolKit_Enum.AuthorizedLocation.應收應付帳款修改與刪除)){
                    if(DialogUI.ConfirmDialog("※ 是否刪除此 「帳款」 ?",true,false,0,0)){
                        if(ManagePayableReceivable_Model.deletePayableReceivable(searchPayableReceivable_bean, OrderObject)){
                            ComponentToolKit.getSearchPayableReceivableTableViewList(TableView).remove(searchPayableReceivable_bean);
                            DialogUI.MessageDialog("※ 刪除成功");
                        }else   DialogUI.MessageDialog("※ 刪除失敗");
                    }
                }
            }else   DialogUI.MessageDialog("※ 請選擇款項");
        }
    }
    private boolean isOrderExistCheckoutInstallment(ObservableList<SearchOrder_Bean> orderList){
        if(orderList == null || orderList.size() == 0)
            return false;
        for(SearchOrder_Bean searchOrder_bean : orderList){
            if(searchOrder_bean.getInstallment() != null && Order_Model.isExistNextInstallmentAndCheckout(searchOrder_bean)){
                return true;
            }
        }
        return false;
    }
    /** Button Mouse Clicked - 列印 */
    @FXML protected void printPayableReceivableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<SearchPayableReceivable_Bean> PayableReceivableList = ComponentToolKit.getSearchPayableReceivableTableViewList(TableView);
            if(PayableReceivableList.size() != 0){
                String FileName = "";
                if(SearchMethod == PayableReceivable_Enum.SearchMethod.關鍵字搜尋){
                    if(ObjectID_RadioButton.isSelected())   FileName = "對象：" + SearchKeyWordText.getText() + " " + OrderObject.getPayableReceivableName() + "帳款資料.xls";
                    else if(InvoiceNumber_RadioButton.isSelected())   FileName = "發票號碼：" + SearchKeyWordText.getText() + " " + OrderObject.getPayableReceivableName() + "帳款資料.xls";
                    else if(CheckNumber_RadioButton.isSelected())   FileName = "票據號碼：" + SearchKeyWordText.getText() + " " + OrderObject.getPayableReceivableName() + "帳款資料.xls";
                }else if(SearchMethod == PayableReceivable_Enum.SearchMethod.日期搜尋)
                    FileName = ComponentToolKit.getDatePickerValue(StartDate_DatePicker,"yyyy-MM-dd") + "~" + ComponentToolKit.getDatePickerValue(EndDate_DatePicker,"yyyy-MM-dd") + "  " + OrderObject.getPayableReceivableName() + "帳款資料.xls";

                FileChooser FileChooser = ComponentToolKit.setFileChooser("選擇匯出位置");
                FileChooser.setInitialDirectory(new File(CallConfig.getFile_OutputPath()));
                FileChooser.setInitialFileName(FileName);
                FileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("xls", "*.xls"));
                File SavePath = FileChooser.showSaveDialog(null);
                String outputFilePath;
                if(SavePath != null)  outputFilePath = SavePath.getAbsolutePath();
                else return;
                if(!ManagePayableReceivable_Model.printPayableReceivable(PayableReceivableList, outputFilePath, OrderObject, StartDate_DatePicker, EndDate_DatePicker))   DialogUI.MessageDialog("※ 列印失敗");
            }else   DialogUI.MessageDialog("※ 無帳款資訊");
        }
    }
    /** Button Mouse Clicked - 支票匯出 */
    @FXML protected void exportCheckMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<SearchPayableReceivable_Bean> PayableReceivableList = ComponentToolKit.getSearchPayableReceivableTableViewList(TableView);
            if(PayableReceivableList.size() != 0){
                if(ManagePayableReceivable_Model.exportCheck(PayableReceivableList, OrderObject)) DialogUI.MessageDialog("※ 匯出成功");
                else    DialogUI.MessageDialog("※ 匯出失敗");
            }else   DialogUI.MessageDialog("※ 無帳款資訊");
        }
    }
    private PayableReceivable_Bean setPayableReceivable_Bean(SearchPayableReceivable_Bean SearchPayableReceivable_Bean){
        PayableReceivable_Bean PayableReceivable_Bean = new PayableReceivable_Bean();
        PayableReceivable_Bean.setOrderNumber(SearchPayableReceivable_Bean.getOrderNumber());
        PayableReceivable_Bean.setOrderDate(SearchPayableReceivable_Bean.getOrderDate());
        PayableReceivable_Bean.setOrderObject(OrderObject);
        PayableReceivable_Bean.setObjectID(SearchPayableReceivable_Bean.getObjectID());
        PayableReceivable_Bean.setCheckNumber(SearchPayableReceivable_Bean.getCheckNumber());
        PayableReceivable_Bean.setCheckDueDate(SearchPayableReceivable_Bean.getCheckDueDate());
        PayableReceivable_Bean.setCompanyBankInfo_id(SearchPayableReceivable_Bean.getCompanyBankInfo_id());
        PayableReceivable_Bean.setObjectBankBean(SearchPayableReceivable_Bean.getObjectBankBean());
        PayableReceivable_Bean.setObjectBankBranch(SearchPayableReceivable_Bean.getObjectBankBranch());
        PayableReceivable_Bean.setObjectBankAccount(SearchPayableReceivable_Bean.getObjectBankAccount());
        PayableReceivable_Bean.setObjectAccountName(SearchPayableReceivable_Bean.getObjectAccountName());
        PayableReceivable_Bean.setObjectPerson(SearchPayableReceivable_Bean.getObjectPerson());
        PayableReceivable_Bean.setCash(String.valueOf(SearchPayableReceivable_Bean.getCash()));
        PayableReceivable_Bean.setDeposit(String.valueOf(SearchPayableReceivable_Bean.getDeposit()));
        PayableReceivable_Bean.setOtherDiscount(String.valueOf(SearchPayableReceivable_Bean.getOtherDiscount()));
        PayableReceivable_Bean.setRemittanceFeeAndPostage(String.valueOf(SearchPayableReceivable_Bean.getRemittanceFeeAndPostage()));
        PayableReceivable_Bean.setCashDiscount(String.valueOf(SearchPayableReceivable_Bean.getCashDiscount()));
        PayableReceivable_Bean.setCheckPrice(String.valueOf(SearchPayableReceivable_Bean.getCheckPrice()));
        PayableReceivable_Bean.setOffsetPrice(String.valueOf(SearchPayableReceivable_Bean.getOffsetPrice()));
        PayableReceivable_Bean.setTotalPriceIncludeTax(String.valueOf(SearchPayableReceivable_Bean.getOffsetPrice()));
        PayableReceivable_Bean.setInvoiceNumber(SearchPayableReceivable_Bean.getInvoiceNumber());
        PayableReceivable_Bean.setRemark(SearchPayableReceivable_Bean.getRemark());
        PayableReceivable_Bean.setOrderList(SearchPayableReceivable_Bean.getOrderList());
        return PayableReceivable_Bean;
    }
    private void setCheckTitleTableColumnVisible(boolean Visible){  ComponentToolKit.setTableColumnVisible(CheckTitleColumn, Visible);  }
    private void setExistWaitingOrderBackgroundColor(TableColumn<SearchPayableReceivable_Bean,Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setExistWaitingOrderBackgroundColor(Alignment, FontSize));
    }
    private class setExistWaitingOrderBackgroundColor extends TableCell<SearchPayableReceivable_Bean, Boolean> {
        String Alignment, FontSize;
        setExistWaitingOrderBackgroundColor(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText((item ? "√" : ""));
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-font-weight: bold;-fx-text-fill:" + (item ? "red": "black"));
            }
        }
    }
    private void setIntegerPriceColumnMicrometerFormat(TableColumn<SearchPayableReceivable_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setIntegerPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setIntegerPriceColumnMicrometerFormat extends TableCell<SearchPayableReceivable_Bean, Integer> {
        String Alignment, FontSize;
        setIntegerPriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(ToolKit.fmtMicrometer(item));
            }
        }
    }
}
