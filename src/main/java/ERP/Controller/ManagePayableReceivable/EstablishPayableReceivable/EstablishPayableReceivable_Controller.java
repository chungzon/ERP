package ERP.Controller.ManagePayableReceivable.EstablishPayableReceivable;

import ERP.Bean.ManagePayableReceivable.BankInfo_Bean;
import ERP.Bean.ManagePayableReceivable.CompanyBank_Bean;
import ERP.Bean.ManagePayableReceivable.PayableReceivable_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.Order.SearchNonePayReceive.ConditionalPayReceiveSearch_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.Controller.ManagePayableReceivable.PaymentCompareSystem.PaymentCompareSystem_Controller;
import ERP.Controller.ManagePayableReceivable.SearchPayableReceivable.SearchPayableReceivable_Controller;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.ObjectSearchColumn;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PayableReceivableStatus;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** [Controller] Establish payable receivable*/
public class EstablishPayableReceivable_Controller{
    @FXML HBox ObjectInfo_HBox;
    @FXML public DatePicker StartDate_DatePicker, EndDate_DatePicker;
    @FXML public TextField PayableReceivableOrderNumberText, ObjectIDText, ObjectNameText, CashDiscountText;

    @FXML Label ObjectID_Label, ObjectName_Label, CompanyBankName_Label, CompanyAccount_Label, PayReceiveDate_Label, ObjectBank_Label, ObjectAccountName_Label, ObjectAccount_Label, ObjectPerson_Label;
    @FXML DatePicker PayReceiveDate_DatePicker, CheckDueDate_DatePicker;
    @FXML ComboBox<CompanyBank_Bean> CompanyAccountNickName_ComboBox;
    @FXML ComboBox<BankInfo_Bean> AllBank_ComboBox;
    @FXML TextField CheckNumberText, CompanyBankCodeText, CompanyBankNameText, CompanyBankBranchText, CompanyAccountNameText, CompanyAccountText;
    @FXML TextField ObjectBankNameText, ObjectBankBranchText, ObjectAccountNameText, ObjectBankAccountText, CashText, ObjectPersonText;
    @FXML TextField DepositText, OtherDiscountText, remittanceFeeAndPostageText, CheckPriceText, OffsetPriceText, InvoiceNumberText, RemarkText, TotalPriceIncludeTaxText;
    @FXML Button CheckNumberSetting_Button, PreviousTerm_Button, NextTerm_Button, SearchBill_Button, HandleBill_Button, ExportPayReceive_Button;
    @FXML CheckBox SelectAll_CheckBox;
    @FXML TableColumn<SearchOrder_Bean,Boolean> Select_TableColumn;
    @FXML TableColumn<SearchOrder_Bean,String> OrderDate_TableColumn, OrderNumber_TableColumn, ProjectName_TableColumn, OrderSource_TableColumn, TotalPriceIncludeTax_TableColumn;
    @FXML TableColumn<SearchOrder_Bean, Integer> installment_tableColumn;
    @FXML TableView<SearchOrder_Bean> TableView;
    @FXML Label tooltip_Label;
    @FXML javafx.scene.control.Tooltip tooltip;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;
    private ERP.ToolKit.Tooltip Tooltip;
    private PayableReceivableStatus PayableReceivableStatus;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private OrderObject OrderObject;
    private Order_Model Order_Model;
    private SystemSetting_Model SystemSetting_Model;
    private SearchPayableReceivable_Controller SearchPayableReceivable_Controller;
    private PaymentCompareSystem_Controller PaymentCompareSystem_Controller;

    private SystemSettingConfig_Bean SystemSettingConfig_Bean;
    private PayableReceivable_Bean PayableReceivable_Bean;
    private ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean;
    public ObjectInfo_Bean ObjectInfo_Bean;
    public Stage Stage;
    public EstablishPayableReceivable_Controller(){
        ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit=  ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.Tooltip = ToolKit.Tooltip;
        this.ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    /** Set object - [Enum] PayableReceivable_Enum.PayableReceivableStatus */
    public void setPayableReceivableStatus(PayableReceivableStatus PayableReceivableStatus){    this.PayableReceivableStatus = PayableReceivableStatus; }
    /** Set object - [Enum] Order_Enum.OrderObject */
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    /** Set object - [Controller] SearchPayableReceivable_Controller */
    public void setSearchPayableReceivable_Controller(SearchPayableReceivable_Controller SearchPayableReceivable_Controller){   this.SearchPayableReceivable_Controller = SearchPayableReceivable_Controller;   }
    public void setPaymentCompareSystem_Controller(PaymentCompareSystem_Controller PaymentCompareSystem_Controller){    this.PaymentCompareSystem_Controller = PaymentCompareSystem_Controller;}
    /** Set object - [Bean] PayableReceivable_Bean */
    public void setPayableReceivable_Bean(PayableReceivable_Bean PayableReceivable_Bean){ this.PayableReceivable_Bean = PayableReceivable_Bean;   }
    /** Set object - [Bean] ConditionalPayReceiveSearch_Bean */
    public void setConditionalPayReceiveSearch_Bean(ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean){  this.ConditionalPayReceiveSearch_Bean = ConditionalPayReceiveSearch_Bean;   }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set component of establish payable or receivable */
    public void setComponent(){
        this.SystemSettingConfig_Bean = SystemSetting_Model.loadAllSystemSettingData();
        setUI();
        CompanyAccountNickName_ComboBox.getItems().addAll(ManagePayableReceivable_Model.getCompanyBankInfo(true));
        ComponentToolKit.setCompanyBankInfoBeanComboBoxObj(CompanyAccountNickName_ComboBox);
        AllBank_ComboBox.getItems().addAll(ManagePayableReceivable_Model.getAllBankInfo(true));
        ComponentToolKit.setAllBankBeanComboBoxObj(AllBank_ComboBox);
        initialTableView();
        setKeyWordTextLimitDigital();
        setKeyWordTextLimitLength();
        setCheckPriceTextListener();

        ComponentToolKit.setToolTips(tooltip_Label, tooltip, Tooltip.searchPayReceive());

        if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.建立)    setEstablishPayableReceivableInfo();
        else if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.修改)    setModifyPayableReceivableInfo();
    }
    private void setCheckPriceTextListener(){
        CheckPriceText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("") && !newValue.equals("0")) {
                if(OrderObject == Order_Enum.OrderObject.廠商){
                    CheckNumberText.setText(SystemSettingConfig_Bean.getNowCheckNumber() != null ? SystemSettingConfig_Bean.getNowCheckNumber() : "");
//                    setCheckDueDay(ObjectInfo_Bean.getCheckDueDay());
                    ComponentToolKit.setDatePickerValue(CheckDueDate_DatePicker, ManagePayableReceivable_Model.generateCheckDueDate(ObjectInfo_Bean.getCheckDueDay()));
                }
                setCheckDueDateDatePickerDisable(false);
            }else{
                CheckNumberText.setText("");
                ComponentToolKit.setDatePickerValue(CheckDueDate_DatePicker, ManagePayableReceivable_Model.generateCheckDueDate(null));
//                setCheckDueDay(null);
                setCheckDueDateDatePickerDisable(true);
            }
        });
    }
    private void setUI(){
        if(OrderObject == Order_Enum.OrderObject.廠商)    setManufacturerUI();
        else if(OrderObject == Order_Enum.OrderObject.客戶)  setCustomerUI();
        ObjectID_Label.setText(OrderObject.name() + "編號");
        ObjectName_Label.setText(OrderObject.name() + "名稱");
        ObjectBank_Label.setText(OrderObject.name() + "銀行");
        ObjectAccount_Label.setText(OrderObject.name() + "帳號");
        ObjectAccountName_Label.setText(OrderObject.name() + "帳戶名");
        ObjectPerson_Label.setText(OrderObject.name() + "匯款人員");
        ExportPayReceive_Button.setText(OrderObject.name() + "請款單匯出");
        HandleBill_Button.setText(PayableReceivableStatus.name() + "資料");
        if(OrderObject == Order_Enum.OrderObject.廠商 && PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.建立)
            ComponentToolKit.setButtonVisible(CheckNumberSetting_Button, true);
        else
            ComponentToolKit.setButtonVisible(CheckNumberSetting_Button,false);

        if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.建立)    ComponentToolKit.setButtonDisable(SearchBill_Button,false);
        else if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.修改)   lockComponent();
    }
    private void setManufacturerUI(){
        ObjectInfo_HBox.setStyle("-fx-background-color: " + ToolKit.getPayableBackgroundColor());
        CompanyBankName_Label.setText("付款戶名");
        CompanyAccount_Label.setText("付款帳號");
        PayReceiveDate_Label.setText("付款日");
        setCheckNumberTextEditable(true);
        setCheckDueDateDatePickerDisable(true);
    }
    private void setCustomerUI(){
        ObjectInfo_HBox.setStyle("-fx-background-color: " + ToolKit.getReceivableBackgroundColor());
        CompanyBankName_Label.setText("入款戶名");
        CompanyAccount_Label.setText("入款帳號");
        PayReceiveDate_Label.setText("收款日");
        setCheckNumberTextEditable(true);
        setCheckDueDateDatePickerDisable(true);
    }
    private void lockComponent(){
        ComponentToolKit.setDatePickerDisable(PayReceiveDate_DatePicker,true);
        ComponentToolKit.setTextFieldEditable(ObjectIDText,false);
        ComponentToolKit.setTextFieldEditable(ObjectNameText,false);
        setCheckNumberTextEditable(false);
        setPriceTextFieldEditable(true);
        setCheckDueDateDatePickerDisable(true);
        ComponentToolKit.setDatePickerDisable(StartDate_DatePicker,true);
        ComponentToolKit.setDatePickerDisable(EndDate_DatePicker,true);
        ComponentToolKit.setButtonDisable(PreviousTerm_Button,true);
        ComponentToolKit.setButtonDisable(NextTerm_Button,true);
        ComponentToolKit.setButtonDisable(SearchBill_Button,true);
    }
    private void initialTableView(){
        setColumnCellValueAndCheckBox(Select_TableColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(OrderDate_TableColumn,"OrderDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(OrderNumber_TableColumn,"OrderNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ProjectName_TableColumn,"ProjectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(OrderSource_TableColumn,"OrderSource", "CENTER-LEFT", "16",null);
        setStringPriceColumnMicrometerFormat(TotalPriceIncludeTax_TableColumn,"TotalPriceIncludeTax", "CENTER-LEFT","16");
        ComponentToolKit.setColumnCellValue(installment_tableColumn,"installment","CENTER-LEFT","16",null);
    }
    private void setKeyWordTextLimitLength(){
        ComponentToolKit.addKeyWordTextLimitLength(CheckNumberText,9);
    }
    private void setKeyWordTextLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(CashText,true);
        ComponentToolKit.addTextFieldLimitDigital(DepositText, false);
        ComponentToolKit.addTextFieldLimitDigital(OtherDiscountText, false);
        ComponentToolKit.addTextFieldLimitDigital(remittanceFeeAndPostageText, false);
    }
    /** DatePicker On Action - 付款日 */
    @FXML protected void PayReceiveDateOnAction(){
        String OrderDate = ComponentToolKit.getDatePickerValue(PayReceiveDate_DatePicker, "yyyyMMdd");
        PayableReceivableOrderNumberText.setText(ManagePayableReceivable_Model.generateNewestOrderNumberOfPayableOrReceivable(OrderDate));
    }
    /** TextField Key Pressed - 對象編號 */
    @FXML protected void ObjectIDKeyPressed(KeyEvent KeyEvent){
        String ObjectID = ObjectIDText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && !ObjectID.equals("")) {
            if(OrderObject == Order_Enum.OrderObject.廠商)    SearchObject(ObjectSearchColumn.廠商編號, ObjectID);
            else if(OrderObject == Order_Enum.OrderObject.客戶)    SearchObject(ObjectSearchColumn.客戶編號, ObjectID);
        }
    }
    /** TextField Key Released - 對象名稱 */
    @FXML protected void ObjectNameKeyReleased(KeyEvent KeyEvent){
        String ObjectName = ObjectNameText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent) && !ObjectName.equals("")) {
            if(OrderObject == Order_Enum.OrderObject.廠商)    SearchObject(ObjectSearchColumn.廠商名稱, ObjectName);
            else if(OrderObject == Order_Enum.OrderObject.客戶)    SearchObject(ObjectSearchColumn.客戶名稱, ObjectName);
        }
    }
    private void SearchObject(ObjectSearchColumn ObjectSearchColumn, String ObjectText){
        ObservableList<ObjectInfo_Bean> ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject, ObjectSearchColumn, ObjectText);
        if(ObjectList.size() == 1){
            initialComponent();
            if(OrderObject == Order_Enum.OrderObject.廠商)    setManufacturerInfo(ObjectList.get(0));
            else if(OrderObject == Order_Enum.OrderObject.客戶)   setCustomerInfo(ObjectList.get(0));
            CashText.requestFocus();
        }else if(ObjectList.size() > 1) CallFXML.ShowObject(Stage, OrderObject, ObjectList,true, Order_Enum.ShowObjectSource.建立應收應付帳款,this);
        else{
            ObjectIDText.setText("");
            ObjectNameText.setText("");
            CashDiscountText.setText("");
            ComponentToolKit.setDatePickerValue(StartDate_DatePicker, null);
            ComponentToolKit.setDatePickerValue(EndDate_DatePicker, null);
            DialogUI.MessageDialog("※ 查無相關" + OrderObject.name());
        }
    }
    /** Button Mouse Clicked - 票據流水號設定 */
    @FXML protected void CheckNumberSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    CallFXML.ShowCheckNumberSetting(Stage, SystemSettingConfig_Bean);
    }
    /** ComboBox On Action - 公司帳戶簡稱 */
    @FXML protected void CompanyAccountNickNameOnAction(){
        CompanyBank_Bean CompanyBank_Bean = ComponentToolKit.getCompanyBankInfoComboBoxSelectItem(CompanyAccountNickName_ComboBox);
        if(CompanyBank_Bean != null){
            CompanyBankNameText.setText(CompanyBank_Bean.getBankNickName());
            CompanyBankBranchText.setText(CompanyBank_Bean.getBankBranch());
            CompanyAccountNameText.setText(CompanyBank_Bean.getBankAccountName());
            CompanyAccountText.setText(CompanyBank_Bean.getBankAccount());
            CompanyBankCodeText.setText(CompanyBank_Bean.getBankCode());
        }
    }
    /** ComboBox On Action - 對象銀行 */
    @FXML protected void ObjectBankCodeOnAction(){
        BankInfo_Bean BankInfo_Bean = ComponentToolKit.getAllBankComboBoxSelectItem(AllBank_ComboBox);
        ObjectBankNameText.setText(BankInfo_Bean == null ? "" : BankInfo_Bean.getBankName());
    }
    /** TextField Key Released - 現金額 */
    @FXML protected void CashKeyReleased(){
        if(ObjectInfo_Bean == null)   DialogUI.MessageDialog("※ 請輸入對象編號");
        else    calculateTotalPrice();
    }
    /** TextField Key Released - 訂金 */
    @FXML protected void DepositKeyReleased(){
        if(ObjectInfo_Bean == null)   DialogUI.MessageDialog("※ 請輸入對象編號");
        else    calculateTotalPrice();
    }
    /** TextField Key Released - 其他折讓 */
    @FXML protected void OtherDiscountKeyReleased(){
        if(ObjectInfo_Bean == null)   DialogUI.MessageDialog("※ 請輸入對象編號");
        else    calculateTotalPrice();
    }

    @FXML protected void PreviousTermMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ObjectInfo_Bean != null){
            String PreviousDate;
            List<Integer> DateList = null;
            for(int index = 0 ; index < 2 ; index++){
                if(index == 0)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(StartDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(index == 1)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(EndDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(DateList.get(1) > 1){
                    DateList.set(1,DateList.get(1)-1);
                }else if(DateList.get(1) == 1){
                    DateList.set(1,12);
                    DateList.set(0,DateList.get(0)-1);
                }
                PreviousDate = DateList.get(0) + "-" + ToolKit.fillZero(DateList.get(1),2) + "-" + ToolKit.fillZero(DateList.get(2),2);
                if(index == 0)  ComponentToolKit.setDatePickerValue(StartDate_DatePicker,PreviousDate);
                if(index == 1)  ComponentToolKit.setDatePickerValue(EndDate_DatePicker,PreviousDate);
            }
        }
    }
    @FXML protected void NextTermMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ObjectInfo_Bean != null){
            String NextDate;
            List<Integer> DateList = null;
            for(int index = 0 ; index < 2 ; index++){
                if(index == 0)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(StartDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(index == 1)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(EndDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(DateList.get(1) < 12) {
                    DateList.set(1,DateList.get(1)+1);
                }else if(DateList.get(1) == 12){
                    DateList.set(1,1);
                    DateList.set(0,DateList.get(0)+1);
                }
                NextDate = DateList.get(0) + "-" + ToolKit.fillZero(DateList.get(1),2) + "-" + ToolKit.fillZero(DateList.get(2),2);
                if(index == 0)  ComponentToolKit.setDatePickerValue(StartDate_DatePicker,NextDate);
                if(index == 1)  ComponentToolKit.setDatePickerValue(EndDate_DatePicker,NextDate);
            }
        }
    }

    /** Button Mouse Clicked - 查詢貨單 */
    @FXML protected void SearchOrderMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.getSearchOrderTableViewItemList(TableView).clear();
            if(ObjectInfo_Bean == null)   DialogUI.MessageDialog("※ 請輸入對象編號");
            else if(ToolKit.isDateRangeError(StartDate_DatePicker, EndDate_DatePicker))  DialogUI.MessageDialog("※ 日期設定錯誤");
            else    SearchBill();
        }
    }
    private void SearchBill(){
        String StartDate = ComponentToolKit.getDatePickerValue(StartDate_DatePicker,"yyyy-MM-dd");
        String EndDate = ComponentToolKit.getDatePickerValue(EndDate_DatePicker,"yyyy-MM-dd");
        ObservableList<SearchOrder_Bean> NonePayReceiveOrderList = null;
        if(OrderObject == Order_Enum.OrderObject.廠商){
            NonePayReceiveOrderList = ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.待入倉單, ObjectIDText.getText(), StartDate, EndDate);
            NonePayReceiveOrderList.addAll(ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.進貨單, ObjectIDText.getText(), StartDate, EndDate));
            NonePayReceiveOrderList.addAll(ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.進貨子貨單, ObjectIDText.getText(), StartDate, EndDate));
            NonePayReceiveOrderList.addAll(ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.進貨退貨單, ObjectIDText.getText(), StartDate, EndDate));
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            NonePayReceiveOrderList = ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.待出貨單, ObjectIDText.getText(), StartDate, EndDate);
            NonePayReceiveOrderList.addAll(ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.出貨單, ObjectIDText.getText(), StartDate, EndDate));
            NonePayReceiveOrderList.addAll(ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.出貨子貨單, ObjectIDText.getText(), StartDate, EndDate));
            NonePayReceiveOrderList.addAll(ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject, Order_Enum.OrderSource.出貨退貨單, ObjectIDText.getText(), StartDate, EndDate));
        }
        ToolKit.sortSearchOrderNumber(NonePayReceiveOrderList);
        assert NonePayReceiveOrderList != null;
        if(NonePayReceiveOrderList.size() == 0){
            ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,false);
            DialogUI.MessageDialog("※ 查無相關貨單");
        }else{
            ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,true);
            TableView.setItems(NonePayReceiveOrderList);
        }
        setInitialComponentData(NonePayReceiveOrderList);
    }
    /** Button Mouse Clicked - 建立/修改 資料 */
    @FXML protected void HandleBillMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.建立)    establishBill();
            else if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.修改)    modifyBill();
        }
    }
    private void establishBill(){
        ObservableList<SearchOrder_Bean> SelectOrderList = ComponentToolKit.getSearchOrderSelectList(TableView);
        if(SelectOrderList.size() == 0) DialogUI.MessageDialog("※ 請選擇貨單");
        else if(!errorLog()){
            RecordPayableReceivableInfo();
            if(ManagePayableReceivable_Model.insertPayableReceivable(PayableReceivable_Bean)){
                DialogUI.MessageDialog("※ " + PayableReceivableStatus.name() + OrderObject.getPayableReceivableName() + "帳款成功");
                if(PaymentCompareSystem_Controller != null) {
                    PaymentCompareSystem_Controller.searchAlreadyReviewTableView();
                    Stage.close();
                }
                if(OrderObject == Order_Enum.OrderObject.廠商 && PayableReceivable_Bean.getCheckNumber().equals(SystemSettingConfig_Bean.getNowCheckNumber()))
                    updateCheckNumber();
                PayableReceivable_Bean = null;
                initialComponent();
            }else   DialogUI.MessageDialog("※ " + PayableReceivableStatus.name() + OrderObject.getPayableReceivableName() + "帳款失敗");
        }
    }
    private void updateCheckNumber(){
        String CheckNumber = CheckNumberText.getText();
        if(CheckNumber != null && !CheckNumber.equals("")) {
            String Title = CheckNumber.substring(0, 2), Number = CheckNumber.substring(2);
            CheckNumber = Title + ToolKit.fillZero((Integer.parseInt(Number) + 1), 7);
            SystemSettingConfig_Bean.setNowCheckNumber(CheckNumber);
            SystemSetting_Model.updateSystemSettingConfigValue(SystemSetting_Enum.SystemSettingConfig.票據流水號, CheckNumber);
        }
    }
    private void modifyBill() {
        RecordPayableReceivableInfo();
        if(ManagePayableReceivable_Model.modifyPayableReceivable(PayableReceivable_Bean)){
            DialogUI.MessageDialog("※ " + PayableReceivableStatus.name() + OrderObject.getPayableReceivableName() + "帳款成功");
            if(SearchPayableReceivable_Controller != null)  SearchPayableReceivable_Controller.callSearchPayableReceivable();
            ComponentToolKit.closeThisStage(Stage);
        }else   DialogUI.MessageDialog("※ " + PayableReceivableStatus.name() + OrderObject.getPayableReceivableName() + "帳款失敗");
    }
    private void RecordPayableReceivableInfo(){
        PayableReceivable_Bean.setOrderNumber(PayableReceivableOrderNumberText.getText());
        PayableReceivable_Bean.setOrderDate(ComponentToolKit.getDatePickerValue(PayReceiveDate_DatePicker,"yyyy-MM-dd"));
        PayableReceivable_Bean.setOrderObject(OrderObject);
        PayableReceivable_Bean.setObjectID(ObjectIDText.getText());
        PayableReceivable_Bean.setCheckNumber(CheckNumberText.getText());
        if(CheckDueDate_DatePicker.getValue() == null)  PayableReceivable_Bean.setCheckDueDate(null);
        else    PayableReceivable_Bean.setCheckDueDate(ComponentToolKit.getDatePickerValue(CheckDueDate_DatePicker,"yyyy-MM-dd"));

        CompanyBank_Bean CompanyBank_Bean = ComponentToolKit.getCompanyBankInfoComboBoxSelectItem(CompanyAccountNickName_ComboBox);
        PayableReceivable_Bean.setCompanyBankInfo_id(CompanyBank_Bean.getCompanyBankInfo_id());

        PayableReceivable_Bean.setObjectBankBean(ComponentToolKit.getAllBankComboBoxSelectItem(AllBank_ComboBox));
        PayableReceivable_Bean.setObjectBankBranch(ObjectBankBranchText.getText());
        PayableReceivable_Bean.setObjectBankAccount(ObjectBankAccountText.getText());
        PayableReceivable_Bean.setObjectAccountName(ObjectAccountNameText.getText());
        PayableReceivable_Bean.setObjectPerson(ObjectPersonText.getText());
        PayableReceivable_Bean.setCash((CashText.getText()));
        PayableReceivable_Bean.setDeposit(DepositText.getText());
        PayableReceivable_Bean.setOtherDiscount(OtherDiscountText.getText());
        PayableReceivable_Bean.setRemittanceFeeAndPostage(remittanceFeeAndPostageText.getText());
        PayableReceivable_Bean.setCashDiscount(CashDiscountText.getText());
        PayableReceivable_Bean.setCheckPrice(CheckPriceText.getText());
        PayableReceivable_Bean.setOffsetPrice(OffsetPriceText.getText());
        PayableReceivable_Bean.setTotalPriceIncludeTax(TotalPriceIncludeTaxText.getText());
        PayableReceivable_Bean.setInvoiceNumber(InvoiceNumberText.getText());
        PayableReceivable_Bean.setRemark(RemarkText.getText());
        PayableReceivable_Bean.setOrderList(ComponentToolKit.getSearchOrderTableViewItemList(TableView));
    }
    /** Button Mouse Clicked - 對象請款單匯出 */
    @FXML protected void ExportPayableReceivableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<SearchOrder_Bean> SelectOrderList = ComponentToolKit.getSearchOrderSelectList(TableView);
            if(SelectOrderList.size() == 0) DialogUI.MessageDialog("※ 請選擇貨單");
            else {
                RecordPayableReceivableInfo();
                String outputFile = ManagePayableReceivable_Model.exportPayableReceivable(OrderObject, ObjectInfo_Bean, PayableReceivable_Bean);
                if(outputFile != null) DialogUI.AlarmDialog("※ 匯出成功，檔名：" + outputFile);
                else    DialogUI.MessageDialog("※ 匯出失敗");
            }
        }
    }
    @FXML protected void SelectAllOnAction(){
        ObservableList<SearchOrder_Bean> searchOrderList = ComponentToolKit.getSearchOrderTableViewItemList(TableView);
        if(searchOrderList.size() != 0){
            for(SearchOrder_Bean SearchOrder_Bean : searchOrderList){
                SearchOrder_Bean.setCheckBoxSelect(SelectAll_CheckBox.isSelected());
            }
            TableView.refresh();
            setInitialComponentData(searchOrderList);
//            setSearchNonePayReceiveInfo(nonePayReceiveList);
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
            SearchOrder_Bean SearchOrder_Bean = ComponentToolKit.getSearchOrderTableViewSelectItem(TableView);
            if(SearchOrder_Bean != null) {
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
                establishOrder_NewStage_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
                establishOrder_NewStage_Bean.setOrderSource(SearchOrder_Bean.getOrderSource());
                establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.已存在);
                establishOrder_NewStage_Bean.setOrder_Bean(setOrder_BeanInfo(SearchOrder_Bean));
                establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
                establishOrder_NewStage_Bean.setSearchOrder_Controller(null);
                establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }
    }
    private Order_Bean setOrder_BeanInfo(SearchOrder_Bean SearchOrder_Bean){
        Order_Bean Order_Bean = new Order_Bean();
        if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.採購單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 ||
                SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.報價單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
            Order_Bean.setNowOrderNumber(SearchOrder_Bean.getQuotationNumber());
        else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 ||
                SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
            Order_Bean.setNowOrderNumber(SearchOrder_Bean.getOrderNumber());
        Order_Bean.setObjectID(SearchOrder_Bean.getObjectID());
        Order_Bean.setObjectName(SearchOrder_Bean.getObjectName());
        Order_Bean.setWaitingOrderDate(SearchOrder_Bean.getWaitingOrderDate());
        Order_Bean.setWaitingOrderNumber(SearchOrder_Bean.getWaitingOrderNumber());
        Order_Bean.setAlreadyOrderDate(SearchOrder_Bean.getAlreadyOrderDate());
        Order_Bean.setAlreadyOrderNumber(SearchOrder_Bean.getAlreadyOrderNumber());
        Order_Bean.setIsCheckout(SearchOrder_Bean.isCheckout());
        Order_Bean.setExistInstallment(SearchOrder_Bean.getInstallment() != null);
        return Order_Bean;
    }
    private void calculateTotalPrice(){
        int TotalPrice = Integer.parseInt(TotalPriceIncludeTaxText.getText());
        int Cash = (CashText.getText() == null || CashText.getText().equals("")) ? 0 : Integer.parseInt(CashText.getText());
        int Deposit = (DepositText.getText() == null || DepositText.getText().equals("")) ? 0 : Integer.parseInt(DepositText.getText());
        int OtherDiscount = (OtherDiscountText.getText() == null || OtherDiscountText.getText().equals("")) ? 0 : Integer.parseInt(OtherDiscountText.getText());
        if(Cash == 0 && !CashText.getText().equals(""))   CashDiscountText.setText("0");
        else    CashDiscountText.setText(getOrderCashDiscount());
        int CashDiscount = (CashDiscountText.getText() == null || CashDiscountText.getText().equals("")) ? 0 : Integer.parseInt(CashDiscountText.getText());

        if(OrderObject == Order_Enum.OrderObject.廠商) {
            int Discount = getRemittanceFeeAndPostage(Cash, Deposit, CashDiscount, TotalPrice);
            remittanceFeeAndPostageText.setText(String.valueOf(Discount));
        }
        int remittanceFeeAndPostage = (remittanceFeeAndPostageText.getText() == null || remittanceFeeAndPostageText.getText().equals("")) ? 0 : Integer.parseInt(remittanceFeeAndPostageText.getText());
        if(TotalPrice == 0)
            CheckPriceText.setText("0");
        else
            CheckPriceText.setText("" + (TotalPrice - Cash - remittanceFeeAndPostage - OtherDiscount - CashDiscount - Deposit));
        OffsetPriceText.setText(TotalPriceIncludeTaxText.getText());
    }
    private int getRemittanceFeeAndPostage(int Cash, int Deposit, int CashDiscount, int TotalPrice){
        int Discount = 0;
        if(Cash == 0 && ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣)    //  扣郵資
            Discount = Discount + ObjectInfo_Bean.getPostage();
        else if(Cash != 0){
            if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣 && ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣){
                if(Cash + Deposit + ObjectInfo_Bean.getRemittanceFee() + ObjectInfo_Bean.getPostage() + CashDiscount == TotalPrice) {
                    Discount = ObjectInfo_Bean.getRemittanceFee();
                    Cash = TotalPrice - CashDiscount - ObjectInfo_Bean.getRemittanceFee();
                    CashText.setText(String.valueOf(Cash));
                }else if(Cash + Deposit + ObjectInfo_Bean.getRemittanceFee() + CashDiscount == TotalPrice)  Discount = ObjectInfo_Bean.getRemittanceFee();
                else    Discount = ObjectInfo_Bean.getRemittanceFee() + ObjectInfo_Bean.getPostage();
            }else if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣)    Discount = ObjectInfo_Bean.getRemittanceFee();
            else if(ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣 && Cash + Deposit + CashDiscount != TotalPrice)  Discount = ObjectInfo_Bean.getPostage();
        }
        return Discount;
    }
    private boolean errorLog(){
        if(OffsetPriceText.getText().equals("")){
            DialogUI.MessageDialog("※ 沖帳額 資料空白");
            return true;
        }else if(!ToolKit.isDigital(OffsetPriceText.getText())){
            DialogUI.MessageDialog("※ 沖帳額 金額錯誤");
            return true;
        }else if(!checkTotalPrice()){
            DialogUI.MessageDialog("※ 金額加總 不等於 沖帳額");
            return true;
        }else if(!ToolKit.isDigital(CheckPriceText.getText())){
            DialogUI.MessageDialog("※ 票面額 金額錯誤");
            return true;
        }else if(ManagePayableReceivable_Model.isPayableReceivableNumberExist(PayableReceivableOrderNumberText.getText())){
            DialogUI.MessageDialog("※ 貨單號重複!");
            return true;
        }else if(!CheckPriceText.getText().equals("0")){
            if(CheckNumberText.getText().equals("")){
                DialogUI.MessageDialog("※ 請輸入票據號碼");
                return true;
            }
            if(ManagePayableReceivable_Model.isCheckNumberExist(CheckNumberText.getText())) {
                DialogUI.MessageDialog("※ 票據號碼已重複");
                return true;
            }
            if(CheckDueDate_DatePicker.getValue() == null){
                DialogUI.MessageDialog("※ 請輸入支票到期日");
                return true;
            }
        }else if(CheckPriceText.getText().equals("0")){
            if(!CheckNumberText.getText().equals("")){
                DialogUI.MessageDialog("※ 票據號碼錯誤 票面額不為 0");
                return true;
            }
            if(CheckDueDate_DatePicker.getValue() != null){
                DialogUI.MessageDialog("※ 支票到期日錯誤 票面額不為 0");
                return true;
            }
        }
        return false;
    }

    private boolean checkTotalPrice(){
        int Cash = (CashText.getText() == null || CashText.getText().equals("")) ? 0 : Integer.parseInt(CashText.getText());
        int Deposit = (DepositText.getText() == null || DepositText.getText().equals("")) ? 0 : Integer.parseInt(DepositText.getText());
        int OtherDiscount = (OtherDiscountText.getText() == null || OtherDiscountText.getText().equals("")) ? 0 : Integer.parseInt(OtherDiscountText.getText());
        int remittanceFeeAndPostage = (remittanceFeeAndPostageText.getText() == null || remittanceFeeAndPostageText.getText().equals("")) ? 0 : Integer.parseInt(remittanceFeeAndPostageText.getText());
        int CashDiscount = (CashDiscountText.getText() == null || CashDiscountText.getText().equals("")) ? 0 : Integer.parseInt(CashDiscountText.getText());
        int CheckPrice = (CheckPriceText.getText() == null || CheckPriceText.getText().equals("")) ? 0 : Integer.parseInt(CheckPriceText.getText());
        return (Cash + Deposit + OtherDiscount + remittanceFeeAndPostage + CashDiscount + CheckPrice) == Integer.parseInt(TotalPriceIncludeTaxText.getText());
    }
    /** Initial component */
    public void initialComponent(){
        if(PayableReceivable_Bean == null)  PayableReceivable_Bean = new PayableReceivable_Bean();
        String OrderDate = ComponentToolKit.getDatePickerValue(PayReceiveDate_DatePicker, "yyyy-MM-dd");
        PayableReceivableOrderNumberText.setText(ManagePayableReceivable_Model.generateNewestOrderNumberOfPayableOrReceivable(OrderDate));

        setPriceTextFieldEditable(false);
        this.ObjectInfo_Bean = null;
        ObjectIDText.setText("");
        ObjectNameText.setText("");
        CheckNumberText.setText("");
        ComponentToolKit.setDatePickerValue(CheckDueDate_DatePicker,null);
        CompanyAccountNickName_ComboBox.getSelectionModel().selectFirst();
        AllBank_ComboBox.getSelectionModel().selectFirst();
        ObjectBankBranchText.setText("");
        ObjectBankAccountText.setText("");
        ObjectAccountNameText.setText("");
        CashText.setText("");
        ObjectPersonText.setText("");
        DepositText.setText("");
        OtherDiscountText.setText("");
        remittanceFeeAndPostageText.setText("");
        CashDiscountText.setText("");
        CheckPriceText.setText("");
        OffsetPriceText.setText("");
        InvoiceNumberText.setText("");
        RemarkText.setText("");
        TotalPriceIncludeTaxText.setText("");
        ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,false);
        ComponentToolKit.getSearchOrderTableViewItemList(TableView).clear();
    }
    private void setEstablishPayableReceivableInfo(){
        if(PayableReceivable_Bean == null)  EstablishFromManagePayableReceivable(); //  應收應付 - 建立應收應付帳款
        else EstablishFromOrder();  //  貨單 - 建立應收應付帳款
    }
    private void EstablishFromManagePayableReceivable(){
        ComponentToolKit.setDatePickerValue(PayReceiveDate_DatePicker, ToolKit.getToday("yyyy-MM-dd"));
        String OrderDate = ComponentToolKit.getDatePickerValue(PayReceiveDate_DatePicker, "yyyy-MM-dd");
        PayableReceivableOrderNumberText.setText(ManagePayableReceivable_Model.generateNewestOrderNumberOfPayableOrReceivable(OrderDate));
    }
    private void EstablishFromOrder(){
        ComponentToolKit.setDatePickerValue(PayReceiveDate_DatePicker, PayableReceivable_Bean.getOrderDate());
        String OrderDate = ComponentToolKit.getDatePickerValue(PayReceiveDate_DatePicker, "yyyy-MM-dd");
        PayableReceivableOrderNumberText.setText(ManagePayableReceivable_Model.generateNewestOrderNumberOfPayableOrReceivable(OrderDate));
        if(OrderObject == Order_Enum.OrderObject.廠商)    SearchObject(ObjectSearchColumn.廠商編號, PayableReceivable_Bean.getObjectID());
        else if(OrderObject == Order_Enum.OrderObject.客戶)    SearchObject(ObjectSearchColumn.客戶編號, PayableReceivable_Bean.getObjectID());

        if(PayableReceivable_Bean.getOrderList() == null)
            SearchBill();
        else {
            TableView.setItems(PayableReceivable_Bean.getOrderList());
            setInitialComponentData(PayableReceivable_Bean.getOrderList());
        }
    }
    private void setModifyPayableReceivableInfo(){
        PayableReceivableOrderNumberText.setText(PayableReceivable_Bean.getOrderNumber());
        ComponentToolKit.setDatePickerValue(PayReceiveDate_DatePicker,PayableReceivable_Bean.getOrderDate());
        ObservableList<ObjectInfo_Bean> ObjectList;
        if(OrderObject == Order_Enum.OrderObject.廠商) {
            ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject, ObjectSearchColumn.廠商編號, PayableReceivable_Bean.getObjectID());
            setManufacturerInfo(ObjectList.get(0));
        }else if(OrderObject == Order_Enum.OrderObject.客戶){
            ObjectList = Order_Model.getObjectFromSearchColumn(OrderObject, ObjectSearchColumn.客戶編號, PayableReceivable_Bean.getObjectID());
            setCustomerInfo(ObjectList.get(0));
        }
        setCompanyBankComboBox();

        ComponentToolKit.setAllBankComboBox(AllBank_ComboBox, PayableReceivable_Bean.getObjectBankBean().getBankID());
        ObjectBankCodeOnAction();

        CheckNumberText.setText(PayableReceivable_Bean.getCheckNumber());
        ComponentToolKit.setDatePickerValue(CheckDueDate_DatePicker,PayableReceivable_Bean.getCheckDueDate());
        ObjectBankBranchText.setText(PayableReceivable_Bean.getObjectBankBranch());
        ObjectAccountNameText.setText(PayableReceivable_Bean.getObjectAccountName());
        ObjectBankAccountText.setText(PayableReceivable_Bean.getObjectBankAccount());
        CashText.setText(PayableReceivable_Bean.getCash());
        ObjectPersonText.setText(PayableReceivable_Bean.getObjectPerson());
        DepositText.setText(PayableReceivable_Bean.getDeposit());
        OtherDiscountText.setText(PayableReceivable_Bean.getOtherDiscount());
        remittanceFeeAndPostageText.setText(PayableReceivable_Bean.getRemittanceFeeAndPostage());
        CashDiscountText.setText(PayableReceivable_Bean.getCashDiscount());
        CheckPriceText.setText(PayableReceivable_Bean.getCheckPrice());
        OffsetPriceText.setText(PayableReceivable_Bean.getOffsetPrice());
        InvoiceNumberText.setText(PayableReceivable_Bean.getInvoiceNumber());
        TotalPriceIncludeTaxText.setText(PayableReceivable_Bean.getTotalPriceIncludeTax());
        RemarkText.setText(PayableReceivable_Bean.getRemark());
        TableView.setItems(PayableReceivable_Bean.getOrderList());
    }
    /** Set manufacturer info from search object */
    public void setManufacturerInfo(ObjectInfo_Bean ObjectInfo_Bean){
        this.ObjectInfo_Bean = ObjectInfo_Bean;
        ObjectIDText.setText(ObjectInfo_Bean.getObjectID());
        ObjectNameText.setText(ObjectInfo_Bean.getObjectName());

        ObservableList<BankInfo_Bean> allBankList = ComponentToolKit.getAllBankComboBoxItems(AllBank_ComboBox);
        for(BankInfo_Bean BankInfo_Bean : allBankList){
            if(BankInfo_Bean.getBankID() != null && BankInfo_Bean.getBankID().equals(ObjectInfo_Bean.getBankID())){
                AllBank_ComboBox.getSelectionModel().select(BankInfo_Bean);
                ObjectBankNameText.setText(BankInfo_Bean.getBankName());
                break;
            }
        }
        ObjectBankBranchText.setText(ObjectInfo_Bean.getBankBranch());
        ObjectBankAccountText.setText(ObjectInfo_Bean.getBankAccount());
        ObjectAccountNameText.setText(ObjectInfo_Bean.getAccountName());

        setRemittanceFeeAndPostage(ObjectInfo_Bean);
        setStartAndEndDatePicker(ObjectInfo_Bean.getPayableDay());
    }
    private void setRemittanceFeeAndPostage(ObjectInfo_Bean ObjectInfo_Bean){
        String DiscountRemittanceFee = "", DiscountPostage = "";
        int TotalDiscount = 0;
        if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣){
            DiscountRemittanceFee = "* 匯費：" + ObjectInfo_Bean.getRemittanceFee() + " 元";
            TotalDiscount = TotalDiscount + ObjectInfo_Bean.getRemittanceFee();
        }
        if(ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣){
            DiscountPostage = "* 郵資：" + ObjectInfo_Bean.getPostage() + " 元";
            TotalDiscount = TotalDiscount + ObjectInfo_Bean.getPostage();
        }
        if(!DiscountRemittanceFee.equals("") && !DiscountPostage.equals(""))    RemarkText.setText(DiscountRemittanceFee + " ; " + DiscountPostage);
        else if(!DiscountRemittanceFee.equals(""))    RemarkText.setText(DiscountRemittanceFee);
        else if(!DiscountPostage.equals(""))    RemarkText.setText(DiscountPostage);
        remittanceFeeAndPostageText.setText(String.valueOf(TotalDiscount));
    }
    /** Set customer info from search object */
    public void setCustomerInfo(ObjectInfo_Bean ObjectInfo_Bean){
        this.ObjectInfo_Bean = ObjectInfo_Bean;
        ObjectIDText.setText(ObjectInfo_Bean.getObjectID());
        ObjectNameText.setText(ObjectInfo_Bean.getObjectName());
        setStartAndEndDatePicker(ObjectInfo_Bean.getReceivableDay());
    }
    private void setStartAndEndDatePicker(int PayableReceivableDay){
        if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.建立){
            if(PayableReceivable_Bean == null || PayableReceivable_Bean.getOrderObject() == null) {
                ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ToolKit.getLastMonthSpecificDay(PayableReceivableDay + 1));
                ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ToolKit.getThisMonthSpecifyDay(PayableReceivableDay));
            }else{
                if(ConditionalPayReceiveSearch_Bean != null){
                    ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ConditionalPayReceiveSearch_Bean.getCheckoutByMonthStartDate());
                    ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ConditionalPayReceiveSearch_Bean.getCheckoutByMonthEndDate());
                }else {
                    ComponentToolKit.setDatePickerValue(StartDate_DatePicker, PayableReceivable_Bean.getOrderDate());
                    ComponentToolKit.setDatePickerValue(EndDate_DatePicker, PayableReceivable_Bean.getOrderDate());
                }
            }
        }else if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.修改){
            ComponentToolKit.setDatePickerValue(StartDate_DatePicker, ToolKit.getLastMonthSpecificDay(PayableReceivableDay + 1));
            ComponentToolKit.setDatePickerValue(EndDate_DatePicker, ToolKit.getThisMonthSpecifyDay(PayableReceivableDay));
        }
    }
    private void setCompanyBankComboBox(){
        if(PayableReceivable_Bean.getCompanyBankInfo_id() == null)
            CompanyAccountNickName_ComboBox.getSelectionModel().selectFirst();
        else{
            ObservableList<CompanyBank_Bean> CompanyBankList = ComponentToolKit.getCompanyBankInfoComboBoxItems(CompanyAccountNickName_ComboBox);
            for(CompanyBank_Bean CompanyBank_Bean : CompanyBankList){
                if(CompanyBank_Bean.getCompanyBankInfo_id() != null && CompanyBank_Bean.getCompanyBankInfo_id().equals(PayableReceivable_Bean.getCompanyBankInfo_id())){
                    CompanyAccountNickName_ComboBox.getSelectionModel().select(CompanyBank_Bean);
                    break;
                }
            }
        }
        CompanyAccountNickNameOnAction();
    }
    private void setInitialComponentData(ObservableList<SearchOrder_Bean> NonePayReceiveOrderList){
        if(NonePayReceiveOrderList.size() == 0){
            TotalPriceIncludeTaxText.setText("");
            setPriceTextFieldEditable(false);
        }else{
            TotalPriceIncludeTaxText.setText(getOrderTotalPriceIncludeTax(NonePayReceiveOrderList));
            setPriceTextFieldEditable(true);
        }
        CashText.setText("");
        DepositText.setText("");
        OtherDiscountText.setText("");
        setRemittanceFeeAndPostage(ObjectInfo_Bean);
        CashDiscountText.setText(getOrderCashDiscount());
        CheckPriceText.setText("");
        OffsetPriceText.setText("");
        if(OrderObject == Order_Enum.OrderObject.廠商 && NonePayReceiveOrderList.size() != 0){
            if(ObjectInfo_Bean.getDefaultPaymentMethod() == ObjectInfo_Enum.DefaultPaymentMethod.支票)
                CashText.setText("0");
            else {
                if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣) {
                    if(Integer.parseInt(TotalPriceIncludeTaxText.getText()) != 0)
                        CashText.setText(ToolKit.RoundingString(Integer.parseInt(TotalPriceIncludeTaxText.getText()) - ObjectInfo_Bean.getRemittanceFee()));
                    else
                        CashText.setText("0");
                }else
                    CashText.setText(TotalPriceIncludeTaxText.getText());
            }
            calculateTotalPrice();
        }
    }
    private String getOrderTotalPriceIncludeTax(ObservableList<SearchOrder_Bean> ShipmentOrderList){
        int TotalPriceIncludeTax = 0;
        for(SearchOrder_Bean SearchOrder_Bean : ShipmentOrderList) {
            if ((SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單  ||
                    SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單)
                    && SearchOrder_Bean.isCheckBoxSelect()){
                TotalPriceIncludeTax += SearchOrder_Bean.getTotalPriceIncludeTax();
            }else if (SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單 && SearchOrder_Bean.isCheckBoxSelect()){
                TotalPriceIncludeTax -= SearchOrder_Bean.getTotalPriceIncludeTax();
            }
        }
        return String.valueOf(TotalPriceIncludeTax);
    }
    private String getOrderCashDiscount(){
        double CashDiscountPercentage = ObjectInfo_Bean.getPayableReceivableDiscount();
        int TotalPriceIncludeTax = (TotalPriceIncludeTaxText.getText() == null || TotalPriceIncludeTaxText.getText().equals("")) ? 0 : Integer.parseInt(TotalPriceIncludeTaxText.getText());
        return String.valueOf(TotalPriceIncludeTax - ToolKit.RoundingInteger(CashDiscountPercentage * TotalPriceIncludeTax));
    }
    private void setPriceTextFieldEditable(boolean Editable){
        ComponentToolKit.setTextFieldEditable(CashText, Editable);
        ComponentToolKit.setTextFieldEditable(DepositText, Editable);
        ComponentToolKit.setTextFieldEditable(OtherDiscountText, Editable);
    }
    private void setCheckNumberTextEditable(boolean Editable){
        ComponentToolKit.setTextFieldEditable(CheckNumberText, Editable);
    }
    private void setCheckDueDateDatePickerDisable(boolean disable){
        ComponentToolKit.setDatePickerDisable(CheckDueDate_DatePicker,disable);
    }
    private void setColumnCellValueAndCheckBox(TableColumn<SearchOrder_Bean, Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<SearchOrder_Bean, Boolean> {
        final CheckBox CheckBox = new CheckBox();
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Boolean CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                setGraphic(CheckBox);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                SearchOrder_Bean SearchOrder_Bean = ComponentToolKit.getSearchOrderTableViewSelectItem(TableView, getIndex());
                CheckBox.setSelected(SearchOrder_Bean.isCheckBoxSelect());

                if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.建立)    CheckBox.setDisable(false);
                else if(PayableReceivableStatus == PayableReceivable_Enum.PayableReceivableStatus.修改)   CheckBox.setDisable(true);

                CheckBox.setOnAction(ActionEvent -> {
                    setCheckBoxSelectedTotalPriceIncludeTax(CheckBox,SearchOrder_Bean);
                    if(OrderObject == Order_Enum.OrderObject.廠商 && ObjectInfo_Bean.getDefaultPaymentMethod() == ObjectInfo_Enum.DefaultPaymentMethod.現金){
                        ObservableList<SearchOrder_Bean> searchOrderList = ComponentToolKit.getSearchOrderTableViewItemList(getTableView());
                        int cash = 0;
                        for(SearchOrder_Bean selectSearchOrder_Bean : searchOrderList){
                            if(selectSearchOrder_Bean.isCheckBoxSelect())
                                cash = cash + selectSearchOrder_Bean.getTotalPriceIncludeTax();
                        }
                        if(cash != 0 && ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣)
                            CashText.setText(ToolKit.RoundingString(cash - ObjectInfo_Bean.getRemittanceFee()));
                        else
                            CashText.setText(String.valueOf(cash));
                    }
                    calculateTotalPrice();
                    determineIsSelectAll();
                });
            }else   setGraphic(null);
        }
    }
    private void setCheckBoxSelectedTotalPriceIncludeTax(CheckBox CheckBox, SearchOrder_Bean SearchOrder_Bean){
        if(CheckBox.isSelected()){
            SearchOrder_Bean.setCheckBoxSelect(true);
            if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 ||
                    SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單){
                OffsetPriceText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) + SearchOrder_Bean.getTotalPriceIncludeTax()));
                TotalPriceIncludeTaxText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) + SearchOrder_Bean.getTotalPriceIncludeTax()));
            }else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單){
                OffsetPriceText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) - SearchOrder_Bean.getTotalPriceIncludeTax()));
                TotalPriceIncludeTaxText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) - SearchOrder_Bean.getTotalPriceIncludeTax()));
            }
        }else{
            SearchOrder_Bean.setCheckBoxSelect(false);
            if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 ||
                    SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單){
                OffsetPriceText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) - SearchOrder_Bean.getTotalPriceIncludeTax()));
                TotalPriceIncludeTaxText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) - SearchOrder_Bean.getTotalPriceIncludeTax()));
            }else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單){
                OffsetPriceText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) + SearchOrder_Bean.getTotalPriceIncludeTax()));
                TotalPriceIncludeTaxText.setText(String.valueOf(Integer.parseInt(TotalPriceIncludeTaxText.getText()) + SearchOrder_Bean.getTotalPriceIncludeTax()));
            }
        }
    }
    private void determineIsSelectAll(){
        boolean isSelectAll = true;
        ObservableList<SearchOrder_Bean> searchOrderList = ComponentToolKit.getSearchOrderTableViewItemList(TableView);
        for(SearchOrder_Bean selectSearchOrder_Bean : searchOrderList){
            if(!selectSearchOrder_Bean.isCheckBoxSelect()) {
                isSelectAll = false;
                break;
            }
        }
        ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,isSelectAll);
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
}
