package ERP.Controller.ManagePayableReceivable.PaymentCompareSystem;

import ERP.Bean.ManagePayableReceivable.*;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.Invoice_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Bean.SystemSetting.IAECrawlerBelong_Bean;
import ERP.ERPApplication;
import ERP.Enum.Invoice.Invoice_Enum;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderExist;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerSource;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerReviewStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PaymentCompareTabName;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerDataTableColumn;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.ManufacturerContactDetailSource;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum.ColorCycle;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.ManagePayableReceivable.ManufacturerContactDocument;
import ERP.Model.ManagePayableReceivable.PurchaseAnnotationDocument;
import ERP.Model.Order.Order_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.*;
import ERP.ToolKit.TemplatePath;
import ERP.ToolKit.Tooltip;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ERP.Model.ManagePayableReceivable.IAECrawler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PaymentCompareSystem_Controller {
    @FXML DatePicker StartDate_DatePicker, EndDate_DatePicker;
    @FXML ProgressBar compareProgressBar;
    @FXML SplitMenuButton updateIAECrawler_SplitMenuButton, verifyFormula_SplitMenuButton;
    @FXML TextArea updateIAECrawlerStatus_TextArea;
    @FXML Label compareStatus_Label;

    @FXML RadioButton searchInvoiceNumber_RadioButton, searchPaymentAmount_RadioButton;
    @FXML TextField searchText_TextField,alreadyOrderNumber_AlreadyReview;
    @FXML Button previousData_Button,nextData_Button;

    @FXML ComboBox<CheckoutStatus_Bean> checkoutStatus_ComboBox;
    @FXML Button ignorePaymentButton,deleteIgnoreButton,deleteBindingButton,comparePayment_searchManufacturerOrder_Button,establishReceivableButton;

    @FXML DatePicker purchaseNoteStartDate_DatePicker,purchaseNoteEndDate_DatePicker;
    @FXML ComboBox<IAECrawlerAccount_Bean> manufacturerCodeComboBox_purchaseNote;

    @FXML CheckBox selectAllCheckBox_manufacturerContactDetail;
    @FXML CheckBox selectAllCheckoutCheckBox_search_manufacturerContactDetail,selectNoneCheckoutCheckBox_search_manufacturerContactDetail,selectAlreadyCheckoutCheckBox_search_manufacturerContactDetail;
    @FXML CheckBox selectAllCheckBox_checkoutStatus_manufacturerContactDetail,selectNoneInvoiceCheckBox_checkoutStatus_manufacturerContactDetail,selectNonePayCheckBox_checkoutStatus_manufacturerContactDetail;
    @FXML DatePicker manufacturerContactDetailStartDate_DatePicker,manufacturerContactDetailEndDate_DatePicker;
    @FXML ComboBox<IAECrawlerAccount_Bean> manufacturerCodeComboBox_manufacturerContactDetail;

    @FXML TabPane paymentCompareTabPane;
    @FXML Tab noneReviewTab,alreadyReviewTab,ignoreTab,missingTab,compareTab,purchaseNoteTab,manufacturerContactDetailTab,openFolderTab;
    @FXML TableColumn<IAECrawlerData_Bean,String> serialNumberColumn_noneReview,belongNameColumn_noneReview,summonsNumberColumn_noneReview,objectIDColumn_noneReview,objectNameColumn_noneReview,objectTaxIDColumn_noneReview,payDateColumn_noneReview,payAmountColumn_noneReview,remittanceFeeColumn_noneReview,bankAccountColumn_noneReview,invoiceContentColumn_noneReview,invoiceDateColumn_noneReview,invoiceNumberColumn_noneReview,invoiceAmountColumn_noneReview,projectCodeColumn_noneReview,sourceColumn_noneReview;
    @FXML TableColumn<IAECrawlerData_Bean,Boolean> selectColumn_noneReview, selectColumn_alreadyReview, selectColumn_ignore;
    @FXML TableColumn<IAECrawlerData_Bean,String> serialNumberColumn_alreadyReview,belongNameColumn_alreadyReview,summonsNumberColumn_alreadyReview,objectIDColumn_alreadyReview,objectNameColumn_alreadyReview,objectTaxIDColumn_alreadyReview,payDateColumn_alreadyReview,payAmountColumn_alreadyReview,remittanceFeeColumn_alreadyReview,bankAccountColumn_alreadyReview,invoiceContentColumn_alreadyReview,invoiceDateColumn_alreadyReview,invoiceNumberColumn_alreadyReview,invoiceAmountColumn_alreadyReview,projectCodeColumn_alreadyReview,sourceColumn_alreadyReview;
    @FXML TableColumn<IAECrawlerData_Bean,String> serialNumberColumn_ignore,belongNameColumn_ignore,summonsNumberColumn_ignore,objectIDColumn_ignore,objectNameColumn_ignore,objectTaxIDColumn_ignore,payDateColumn_ignore,payAmountColumn_ignore,remittanceFeeColumn_ignore,bankAccountColumn_ignore,invoiceContentColumn_ignore,invoiceDateColumn_ignore,invoiceNumberColumn_ignore,invoiceAmountColumn_ignore,projectCodeColumn_ignore,isReviewColumn_ignore,sourceColumn_ignore;
    @FXML TableColumn<IAECrawlerData_Bean,String> serialNumberColumn_missing,belongNameColumn_missing,summonsNumberColumn_missing,objectIDColumn_missing,objectNameColumn_missing,objectTaxIDColumn_missing,payDateColumn_missing,payAmountColumn_missing,remittanceFeeColumn_missing,bankAccountColumn_missing,invoiceContentColumn_missing,invoiceDateColumn_missing,invoiceNumberColumn_missing,invoiceAmountColumn_missing,projectCodeColumn_missing,isReviewColumn_missing,sourceColumn_missing;
    @FXML TableColumn<IAECrawlerData_Bean,String> serialNumberColumn_compare,belongNameColumn_compare,summonsNumberColumn_compare,objectIDColumn_compare,objectNameColumn_compare,objectTaxIDColumn_compare,payDateColumn_compare,payAmountColumn_compare,remittanceFeeColumn_compare,bankAccountColumn_compare,invoiceContentColumn_compare,invoiceDateColumn_compare,invoiceNumberColumn_compare,invoiceAmountColumn_compare,projectCodeColumn_compare,sourceColumn_compare;
    @FXML TableColumn<IAECrawler_ExportPurchaseNote_Bean,String> serialNumberColumn_purchaseNote,isCheckoutColumn_purchaseNote,schoolInvoiceDateColumn_purchaseNote,manufacturerNameColumn_purchaseNote,invoiceDateColumn_purchaseNote,invoiceTypeColumn_purchaseNote,invoiceNumberColumn_purchaseNote,customerIDColumn_purchaseNote,orderDateColumn_purchaseNote,orderNumberColumn_purchaseNote,projectNameColumn_purchaseNote,cashierRemarkColumn_purchaseNote;
    @FXML TableColumn<IAECrawler_ExportPurchaseNote_Bean,Integer> schoolInvoiceAmountColumn_purchaseNote,invoiceAmountColumn_purchaseNote,projectTotalPriceIncludeTaxColumn_purchaseNote;
    @FXML TableColumn<IAECrawler_ManufacturerContactDetail_Bean,CheckBox> selectColumn_manufacturerContactDetail;
    @FXML TableColumn<IAECrawler_ManufacturerContactDetail_Bean,Integer> repeatSchoolAlreadyPaymentColumn_manufacturerContactDetail;
    @FXML TableColumn<IAECrawler_ManufacturerContactDetail_Bean,String> serialNumberColumn_manufacturerContactDetail,orderDateColumn_manufacturerContactDetail,projectNameColumn_manufacturerContactDetail,invoiceNumberColumn_manufacturerContactDetail,invoiceTypeColumn_manufacturerContactDetail,cashierRemarkColumn_manufacturerContactDetail,checkoutStatusColumn_manufacturerContactDetail,deleteCheckoutColumn_manufacturerContactDetail;
    @FXML TableColumn<IAECrawler_ManufacturerContactDetail_Bean,Integer> alreadyInvoiceAmountColumn_manufacturerContactDetail;
    @FXML
    TableColumn<IAECrawler_ManufacturerContactDetail_Bean,Integer> noneInvoiceAmountColumn_manufacturerContactDetail;
    @FXML
    TableColumn<IAECrawler_ManufacturerContactDetail_Bean,Integer> schoolAlreadyAmountColumn_manufacturerContactDetail;
    @FXML
    TableColumn<IAECrawler_ManufacturerContactDetail_Bean,Integer> schoolNoneAmountColumn_manufacturerContactDetail;
    @FXML TableView<IAECrawlerData_Bean> noneReviewTableView,alreadyReviewTableView,ignoreTableView,missingTableView,compareTableView;

    @FXML TableView<IAECrawler_ExportPurchaseNote_Bean> purchaseNoteTableView;
    @FXML TableView<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailTableView;

    @FXML TableColumn<SearchOrder_Bean,String> serialNumberColumn_compareOrder,orderDateColumn_compareOrder, orderNumberColumn_compareOrder, projectNameColumn_compareOrder, orderSourceColumn_compareOrder, totalPriceIncludeTaxColumn_compareOrder, invoiceNumberColumn_compareOrder, invoiceManufacturerNickNameColumn_compareOrder, ButtonColumn_compareOrder;
    @FXML TableView<SearchOrder_Bean> compareOrderTableView;

    @FXML Label tooltip_compare_Label, tooltip_compare_searchOrder_Label, tooltip_purchaseNote_Label,tooltip_manufacturerContactDetail_Label;
    @FXML javafx.scene.control.Tooltip tooltip_compare, tooltip_compare_searchOrder, tooltip_compare_searchOrderDate, tooltip_purchaseNote, tooltip_manufacturerContactDetail;

    private TableColumn<IAECrawlerData_Bean,String> nowSerialNumberColumn,nowBelongNameColumn,nowSummonsNumberColumn,nowObjectIDColumn,nowObjectNameColumn,nowObjectTaxIDColumn,nowPayDateColumn,nowPayAmountColumn,nowRemittanceFeeColumn,nowBankAccountColumn,nowInvoiceContentColumn,nowInvoiceDateColumn,nowInvoiceNumberColumn,nowInvoiceAmountColumn,nowProjectCodeColumn, nowSourceColumn;
    private TableView<IAECrawlerData_Bean> nowTableView;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private CallConfig CallConfig;
    private Tooltip Tooltip;
    private Stage MainStage;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private Order_Model Order_Model;
    private SystemSetting_Model SystemSetting_Model;

    private ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList;
    private HashMap<IAECrawlerData_Bean,ObservableList<SearchOrder_Bean>> invoiceAndOrderMap = new HashMap<>();
    private HashMap<IAECrawlerData_Bean,SearchOrder_Bean> IAECrawlerDataAndOrderMap = new HashMap<>();
    private JSONObject TableViewSettingMap;
    private PaymentCompareTabName PaymentCompareTabName;
    private HashMap<IAECrawlerAccount_Bean,CheckBox> IAECrawlerAccount_CheckBoxMap = new HashMap<>();
    private ObservableList<IAECrawlerData_Bean> AllIAECrawlerDataList;
    private ArrayList<HashMap<Integer,Boolean>> highlightTableViewItemList = new ArrayList<>();
    private boolean isHighlightTableView = false;

    public PaymentCompareSystem_Controller(){
        ToolKit = ERPApplication.ToolKit;
        ComponentToolKit = ToolKit.ComponentToolKit;
        KeyPressed = ToolKit.KeyPressed;
        CallFXML = ToolKit.CallFXML;
        CallConfig = ToolKit.CallConfig;
        Tooltip = ToolKit.Tooltip;
        ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        Order_Model = ToolKit.ModelToolKit.getOrderModel();
        SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setMainStage(Stage MainStage){
        this.MainStage = MainStage;
    }
    public void setComponent() throws Exception{
        ComponentToolKit.setDatePickerValue(StartDate_DatePicker,ToolKit.getLastMonthSpecificDay(1));
        ComponentToolKit.setDatePickerValue(EndDate_DatePicker,ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.setDatePickerValue(purchaseNoteStartDate_DatePicker,ToolKit.getMonthFirstDate());
        ComponentToolKit.setDatePickerValue(purchaseNoteEndDate_DatePicker,ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.setDatePickerValue(manufacturerContactDetailStartDate_DatePicker,ToolKit.getMonthFirstDate());
        ComponentToolKit.setDatePickerValue(manufacturerContactDetailEndDate_DatePicker,ToolKit.getToday("yyyy-MM-dd"));
        ComponentToolKit.setCheckoutStatusBeanComboBoxObj(checkoutStatus_ComboBox);
        setCheckoutStatusComboBox();

        ComponentToolKit.setIAECrawlerAccountComboBoxObj_PurchaseNote(manufacturerCodeComboBox_purchaseNote);
        ComponentToolKit.setIAECrawlerAccountComboBoxObj_ManufacturerContactDetail(manufacturerCodeComboBox_manufacturerContactDetail);

        IAECrawlerAccountList = SystemSetting_Model.getIAECrawlerAccount(SystemSetting_Model.getIAECrawlerBelong());
        setUpdateIAECrawlerItems();
        initialTableView();
        setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.未查核發票);
        setTableColumnSort();
        searchNoneReviewData();
        updateIAECrawlerStatus_TextArea.setText("");

        ComponentToolKit.setToolTips(tooltip_compare_Label,tooltip_compare,Tooltip.iaeCompare());
        ComponentToolKit.setToolTips(tooltip_compare_searchOrder_Label,tooltip_compare_searchOrder,Tooltip.iaeCompare_searchOrder());
        ComponentToolKit.setTooltipStyle(tooltip_compare_searchOrderDate);
//        ComponentToolKit.setToolTips(comparePayment_searchManufacturerOrder_Button, tooltip_compare_searchOrderDate, "");
        ComponentToolKit.setToolTips(tooltip_purchaseNote_Label,tooltip_purchaseNote,Tooltip.purchaseNote());
        ComponentToolKit.setToolTips(tooltip_manufacturerContactDetail_Label,tooltip_manufacturerContactDetail,Tooltip.manufacturerContactDetail());
    }
    private void setCheckoutStatusComboBox(){
        ComponentToolKit.getCheckoutStatusComboBoxItemList(checkoutStatus_ComboBox).clear();
        ComponentToolKit.getCheckoutStatusComboBoxItemList(checkoutStatus_ComboBox).add(new CheckoutStatus_Bean("全選", null));
        ComponentToolKit.getCheckoutStatusComboBoxItemList(checkoutStatus_ComboBox).add(new CheckoutStatus_Bean("未結帳", false));
        ComponentToolKit.getCheckoutStatusComboBoxItemList(checkoutStatus_ComboBox).add(new CheckoutStatus_Bean("已結帳", true));
        checkoutStatus_ComboBox.getSelectionModel().selectFirst();
    }

    private void initialTableView(){
        setColumnCellValueAndCheckBox_IAECrawlerData(selectColumn_noneReview,"SelectCheckBox", "CENTER", "16");
        setColumnCellValueAndCheckBox_IAECrawlerData(selectColumn_alreadyReview,"SelectCheckBox", "CENTER", "16");
        setColumnCellValueAndCheckBox_IAECrawlerData(selectColumn_ignore,"SelectCheckBox", "CENTER", "16");

        setAllTableColumnCellValue(serialNumberColumn_noneReview,serialNumberColumn_alreadyReview,serialNumberColumn_ignore,serialNumberColumn_missing,serialNumberColumn_compare,"serialNumber",false);
        setAllTableColumnCellValue(belongNameColumn_noneReview,belongNameColumn_alreadyReview,belongNameColumn_ignore,belongNameColumn_missing,belongNameColumn_compare,"belongName",false);
        setAllTableColumnCellValue(summonsNumberColumn_noneReview,summonsNumberColumn_alreadyReview,summonsNumberColumn_ignore,summonsNumberColumn_missing,summonsNumberColumn_compare,"summonsNumber",false);
        setAllTableColumnCellValue(objectIDColumn_noneReview,objectIDColumn_alreadyReview,objectIDColumn_ignore,objectIDColumn_missing,objectIDColumn_compare,"objectID",false);
        setAllTableColumnCellValue(objectNameColumn_noneReview,objectNameColumn_alreadyReview,objectNameColumn_ignore,objectNameColumn_missing,objectNameColumn_compare,"objectName",false);
        setAllTableColumnCellValue(objectTaxIDColumn_noneReview,objectTaxIDColumn_alreadyReview,objectTaxIDColumn_ignore,objectTaxIDColumn_missing,objectTaxIDColumn_compare,"objectTaxID",false);
        setAllTableColumnCellValue(payDateColumn_noneReview,payDateColumn_alreadyReview,payDateColumn_ignore,payDateColumn_missing,payDateColumn_compare,"payDate",false);
        setColumnCellBackgroundColor(payAmountColumn_noneReview,payAmountColumn_alreadyReview,payAmountColumn_ignore,payAmountColumn_missing,payAmountColumn_compare,"payAmount",true);
        setAllTableColumnCellValue(remittanceFeeColumn_noneReview,remittanceFeeColumn_alreadyReview,remittanceFeeColumn_ignore,remittanceFeeColumn_missing,remittanceFeeColumn_compare,"remittanceFee",false);
        setAllTableColumnCellValue(bankAccountColumn_noneReview,bankAccountColumn_alreadyReview,bankAccountColumn_ignore,bankAccountColumn_missing,bankAccountColumn_compare,"bankAccount",false);
        setAllTableColumnCellValue(invoiceContentColumn_noneReview,invoiceContentColumn_alreadyReview,invoiceContentColumn_ignore,invoiceContentColumn_missing,invoiceContentColumn_compare,"invoiceContent",false);
        setAllTableColumnCellValue(invoiceDateColumn_noneReview,invoiceDateColumn_alreadyReview,invoiceDateColumn_ignore,invoiceDateColumn_missing,invoiceDateColumn_compare,"invoiceDate",false);
        setColumnCellBackgroundColor(invoiceNumberColumn_noneReview,invoiceNumberColumn_alreadyReview,invoiceNumberColumn_ignore,invoiceNumberColumn_missing,invoiceNumberColumn_compare,"invoiceNumber",false);
        setAllTableColumnCellValue(invoiceAmountColumn_noneReview,invoiceAmountColumn_alreadyReview,invoiceAmountColumn_ignore,invoiceAmountColumn_missing,invoiceAmountColumn_compare,"invoiceAmount",true);
        setAllTableColumnCellValue(projectCodeColumn_noneReview,projectCodeColumn_alreadyReview,projectCodeColumn_ignore,projectCodeColumn_missing,projectCodeColumn_compare,"projectCode",false);
        setAllTableColumnCellValue(sourceColumn_noneReview,sourceColumn_alreadyReview,sourceColumn_ignore,sourceColumn_missing,sourceColumn_compare,"source",false);
        setColumnCellValueAndTextFill_isReview(isReviewColumn_missing,"isReview");
        setTableColumnPriceSort();
        setTextFieldTableCell();

        ComponentToolKit.setColumnCellValue(serialNumberColumn_compareOrder,"serialNumber", "CENTER", "16",null);
        ComponentToolKit.setColumnCellValue(orderDateColumn_compareOrder,"OrderDate", "CENTER", "16",null);
        ComponentToolKit.setColumnCellValue(orderNumberColumn_compareOrder,"OrderNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(projectNameColumn_compareOrder,"ProjectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(orderSourceColumn_compareOrder,"OrderSource", "CENTER-LEFT", "16",null);
        setSearchOrderPriceColumnMicrometerFormat(totalPriceIncludeTaxColumn_compareOrder,"TotalPriceIncludeTax", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(invoiceNumberColumn_compareOrder,"invoiceNumber", "CENTER", "16",null);
        ComponentToolKit.setColumnCellValue(invoiceManufacturerNickNameColumn_compareOrder,"invoiceManufacturerNickName", "CENTER-LEFT", "16",null);
        setColumnCellValueAndButtonBySearchOrder(ButtonColumn_compareOrder,"StatusButton","CENTER","16");

        ComponentToolKit.setColumnCellValue(serialNumberColumn_purchaseNote,"serialNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(isCheckoutColumn_purchaseNote,"checkoutStatus", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(schoolInvoiceDateColumn_purchaseNote,"schoolPayDate", "CENTER-LEFT", "16",null);
        setPurchaseNotePriceColumnMicrometerFormat(schoolInvoiceAmountColumn_purchaseNote,"schoolPayAmount", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(manufacturerNameColumn_purchaseNote,"manufacturerName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(invoiceDateColumn_purchaseNote,"invoiceDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(invoiceTypeColumn_purchaseNote,"invoiceType", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(invoiceNumberColumn_purchaseNote,"invoiceNumber", "CENTER-LEFT", "16",null);
        setPurchaseNotePriceColumnMicrometerFormat(invoiceAmountColumn_purchaseNote,"invoiceAmount", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(customerIDColumn_purchaseNote,"customerID", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(orderDateColumn_purchaseNote,"orderDate", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(orderNumberColumn_purchaseNote,"orderNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(projectNameColumn_purchaseNote,"projectName", "CENTER-LEFT", "16",null);
        setPurchaseNotePriceColumnMicrometerFormat(projectTotalPriceIncludeTaxColumn_purchaseNote,"projectTotalPriceIncludeTax", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(cashierRemarkColumn_purchaseNote,"cashierRemark", "CENTER-LEFT", "16",null);

        setColumnCellValueAndCheckBox_ManufacturerContactDetail(selectColumn_manufacturerContactDetail,"selectCheckBox","CENTER", "16");
        setRepeatSchoolAlreadyPaymentColumnTextFill(repeatSchoolAlreadyPaymentColumn_manufacturerContactDetail,"payment_id", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(serialNumberColumn_manufacturerContactDetail,"serialNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(orderDateColumn_manufacturerContactDetail,"orderDate", "CENTER-LEFT", "16",null);
        setManufacturerContactDetailPriceColumnMicrometerFormat(alreadyInvoiceAmountColumn_manufacturerContactDetail,"alreadyInvoiceAmount", "CENTER-LEFT", "16");
        setManufacturerContactDetailPriceColumnMicrometerFormat(noneInvoiceAmountColumn_manufacturerContactDetail,"noneInvoiceAmount", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(projectNameColumn_manufacturerContactDetail,"projectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(invoiceNumberColumn_manufacturerContactDetail,"invoiceNumber", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(invoiceTypeColumn_manufacturerContactDetail,"invoiceType", "CENTER-LEFT", "16",null);
        setManufacturerContactDetailPriceColumnMicrometerFormat(schoolAlreadyAmountColumn_manufacturerContactDetail,"schoolAlreadyAmount", "CENTER-LEFT", "16");
        setManufacturerContactDetailPriceColumnMicrometerFormat(schoolNoneAmountColumn_manufacturerContactDetail,"schoolNoneAmount", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(cashierRemarkColumn_manufacturerContactDetail,"cashierRemark", "CENTER-LEFT", "16",null);
        setColumnCellStringValueAndTextFill(checkoutStatusColumn_manufacturerContactDetail,"checkoutStatus", "CENTER-LEFT", "16");
        setColumnCellValueAndButton(deleteCheckoutColumn_manufacturerContactDetail,"checkoutStatus", "CENTER-LEFT", "14");
    }
    private void setAllTableColumnCellValue(TableColumn<IAECrawlerData_Bean,String> TableColumn1,
                                            TableColumn<IAECrawlerData_Bean,String> TableColumn2,
                                            TableColumn<IAECrawlerData_Bean,String> TableColumn3,
                                            TableColumn<IAECrawlerData_Bean,String> TableColumn4,
                                            TableColumn<IAECrawlerData_Bean,String> TableColumn5,
                                            String ColumnValue,
                                            boolean isFmtMicrometer){
        if(isFmtMicrometer){
            this.setIAECrawlerDataPriceColumnMicrometerFormat(TableColumn1,ColumnValue,"CENTER-LEFT","16");
            this.setIAECrawlerDataPriceColumnMicrometerFormat(TableColumn2,ColumnValue,"CENTER-LEFT","16");
            this.setIAECrawlerDataPriceColumnMicrometerFormat(TableColumn3,ColumnValue,"CENTER-LEFT","16");
            this.setIAECrawlerDataPriceColumnMicrometerFormat(TableColumn4,ColumnValue,"CENTER-LEFT","16");
            this.setIAECrawlerDataPriceColumnMicrometerFormat(TableColumn5,ColumnValue,"CENTER-LEFT","16");
        }else {
            ComponentToolKit.setColumnCellValue(TableColumn1, ColumnValue, "CENTER-LEFT", "16", null);
            ComponentToolKit.setColumnCellValue(TableColumn2, ColumnValue, "CENTER-LEFT", "16", null);
            ComponentToolKit.setColumnCellValue(TableColumn3, ColumnValue, "CENTER-LEFT", "16", null);
            ComponentToolKit.setColumnCellValue(TableColumn4, ColumnValue, "CENTER-LEFT", "16", null);
            ComponentToolKit.setColumnCellValue(TableColumn5, ColumnValue, "CENTER-LEFT", "16", null);
        }
    }
    private void setTableColumnPriceSort(){
        ComponentToolKit.setTableColumnDoubleSort(payAmountColumn_noneReview);
        ComponentToolKit.setTableColumnDoubleSort(payAmountColumn_alreadyReview);
        ComponentToolKit.setTableColumnDoubleSort(payAmountColumn_ignore);
        ComponentToolKit.setTableColumnDoubleSort(payAmountColumn_missing);
        ComponentToolKit.setTableColumnDoubleSort(payAmountColumn_compare);

        ComponentToolKit.setTableColumnDoubleSort(invoiceAmountColumn_noneReview);
        ComponentToolKit.setTableColumnDoubleSort(invoiceAmountColumn_alreadyReview);
        ComponentToolKit.setTableColumnDoubleSort(invoiceAmountColumn_ignore);
        ComponentToolKit.setTableColumnDoubleSort(invoiceAmountColumn_missing);
        ComponentToolKit.setTableColumnDoubleSort(invoiceAmountColumn_compare);
    }
    private void setTextFieldTableCell(){
        invoiceNumberColumn_noneReview.setCellFactory(TextFieldTableCell.forTableColumn());
        invoiceNumberColumn_missing.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    private void setUpdateIAECrawlerItems(){
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            CheckBox CheckBox = ComponentToolKit.initialCheckBox(IAECrawlerAccount_Bean.getAccount() + "    " + IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectName(),18);
            CheckBox.setSelected(true);
            CustomMenuItem CustomMenuItem = new CustomMenuItem(CheckBox);
            CustomMenuItem.setHideOnClick(false);
            updateIAECrawler_SplitMenuButton.getItems().add(CustomMenuItem);
            IAECrawlerAccount_CheckBoxMap.put(IAECrawlerAccount_Bean,CheckBox);
        }
    }
    @FXML protected void updateIAECrawlerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Task Task = crawlerTask();
            new Thread(Task).start();
        }
    }
    private Task crawlerTask(){
        return new Task() {
            @Override public Void call(){
                try{
                    LocalDate startDate = LocalDate.parse(ComponentToolKit.getDatePickerValue(StartDate_DatePicker,"yyyy-MM-dd"));
                    LocalDate endDate = LocalDate.parse(ComponentToolKit.getDatePickerValue(EndDate_DatePicker,"yyyy-MM-dd"));
                    if(ToolKit.isDateRangeError(StartDate_DatePicker,EndDate_DatePicker)){
                        Platform.runLater(()-> DialogUI.MessageDialog("※ 日期區間錯誤"));
                        return null;
                    }
                    writeStatusText("出納帳戶更新中...\n");
                    startCrawler(startDate,endDate);
                    writeStatusText("出納帳戶更新完成!\n");
                }catch (Exception Ex){
                    writeStatusText("出納帳戶更新失敗!\n");
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
                return null;
            }
        };
    }
    private void startCrawler(LocalDate startDate, LocalDate endDate) throws Exception{
        IAECrawler crawler = new IAECrawler();
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            if(IAECrawlerAccount_CheckBoxMap.get(IAECrawlerAccount_Bean).isSelected()){
                ArrayList<IAECrawlerBelong_Bean> IAECrawlerBelongList = IAECrawlerAccount_Bean.getIAECrawlerBelongList();
                for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelongList){
                    crawler.setDestination(IAECrawlerBelong_Bean.getBelongURL());
                    try{
                        if(!crawler.login(IAECrawlerAccount_Bean.getAccount(),IAECrawlerAccount_Bean.getPassword())) {
                            writeStatusText("[ " + IAECrawlerAccount_Bean.getObjectID() + "  " + IAECrawlerAccount_Bean.getObjectName() + " ] " + "出納帳戶登入失敗，帳號：" + IAECrawlerAccount_Bean.getAccount() + "  " + IAECrawlerBelong_Bean.getBelongName() + "\n");
                            ERPApplication.Logger.warn("※ 帳戶登入失敗，資訊：" + IAECrawlerAccount_Bean.getObjectID() + "  " + IAECrawlerAccount_Bean.getObjectName() + "  " + IAECrawlerAccount_Bean.getAccount() + "  " + IAECrawlerBelong_Bean.getBelongName());
                        }else{
                            writeStatusText("[ " + IAECrawlerAccount_Bean.getObjectID() + "  " + IAECrawlerAccount_Bean.getObjectName() + " ] " + "出納帳戶登入成功，帳號：" + IAECrawlerAccount_Bean.getAccount() + "  " + IAECrawlerBelong_Bean.getBelongName() + "\n");
                            ERPApplication.Logger.info("※ 帳戶登入成功，資訊：" + IAECrawlerAccount_Bean.getObjectID() + "  " + IAECrawlerAccount_Bean.getObjectName() + "  " + IAECrawlerAccount_Bean.getAccount() + "  " + IAECrawlerBelong_Bean.getBelongName());

                            ManagePayableReceivable_Model.setIAECrawlerDataStatus(IAECrawlerAccount_Bean,IAECrawlerBelong_Bean,startDate,endDate, null,IAECrawlerStatus.待更新);
                            ArrayList<IAECrawlerData_Bean> IAECrawlerDataList = getIAECrawlerData(crawler,startDate,endDate);
                            combineTheSameInvoiceNumberData(IAECrawlerDataList);
                            HashMap<IAECrawlerData_Bean,IAECrawlerData_Bean> differentObjectMap = null;
                            for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                                IAECrawlerData_Bean.setInvoice_manufacturerNickName_id(IAECrawlerAccount_Bean.getId());
                                IAECrawlerData_Bean.setInvoiceManufacturerNickName(IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
                                IAECrawlerData_Bean originIAECrawlerData_Bean = ManagePayableReceivable_Model.getExistIAECrawlerData(IAECrawlerAccount_Bean,IAECrawlerData_Bean.getRankey());
                                if(originIAECrawlerData_Bean == null){
                                    IAECrawlerData_Bean.setIAECrawlerStatus(IAECrawlerStatus.新增);
                                    IAECrawlerData_Bean.setIAECrawlerReviewStatus(IAECrawlerReviewStatus.未查核);
                                    if(ManagePayableReceivable_Model.insertIAECrawlerPayment(IAECrawlerAccount_Bean, IAECrawlerData_Bean, IAECrawlerBelong_Bean)) {
                                        ERPApplication.Logger.info("[新增成功] 出納資訊 -- 發票日期：" + IAECrawlerData_Bean.getInvoiceDate() + "  發票號碼：" + IAECrawlerData_Bean.getInvoiceNumber() + "  摘要：" + IAECrawlerData_Bean.getInvoiceContent());
                                    }else {
                                        ERPApplication.Logger.warn("[新增失敗] 出納資訊 -- 發票日期：" + IAECrawlerData_Bean.getInvoiceDate() + "  發票號碼：" + IAECrawlerData_Bean.getInvoiceNumber() + "  摘要：" + IAECrawlerData_Bean.getInvoiceContent());
                                    }
                                }else{
                                    if(IAECrawlerData_Bean.getInvoice_manufacturerNickName_id().equals(originIAECrawlerData_Bean.getInvoice_manufacturerNickName_id()) &&
                                            originIAECrawlerData_Bean.getIAECrawlerStatus() == IAECrawlerStatus.忽略){ //  只更新資料
                                        IAECrawlerData_Bean.setIAECrawlerReviewStatus(IAECrawlerReviewStatus.未查核);
                                        ManagePayableReceivable_Model.updateIAECrawlerData(IAECrawlerData_Bean,false,false);
                                    }else{
                                        ManagePayableReceivable_Model.setIAECrawlerDataStatus(IAECrawlerAccount_Bean,IAECrawlerBelong_Bean,null,null,originIAECrawlerData_Bean,IAECrawlerStatus.新增);
                                        if(compareIAECrawlerData(originIAECrawlerData_Bean, IAECrawlerData_Bean)){
                                            if(differentObjectMap == null) differentObjectMap = new HashMap<>();
                                            differentObjectMap.put(originIAECrawlerData_Bean,IAECrawlerData_Bean);
                                        }
                                    }
                                }
                            }
                            if(differentObjectMap != null) {
                                HashMap<IAECrawlerData_Bean, IAECrawlerData_Bean> finalDifferentObjectMap = differentObjectMap;
                                Platform.runLater(()-> CallFXML.ShowCompareIAECrawlerData(MainStage,IAECrawlerAccount_Bean, finalDifferentObjectMap));
                            }
                        }
                    }catch (Exception Ex){
                        Platform.runLater(()-> DialogUI.ExceptionDialog(Ex));
                        ERPApplication.Logger.catching(Ex);
                    }
                    ManagePayableReceivable_Model.setIAECrawlerDataStatus(IAECrawlerAccount_Bean,IAECrawlerBelong_Bean,startDate,endDate,null,IAECrawlerStatus.遺失);
                }
            }
        }
    }
    private ArrayList<IAECrawlerData_Bean> getIAECrawlerData(IAECrawler crawler, LocalDate startDate, LocalDate endDate) throws Exception{
        ArrayList<IAECrawlerData_Bean> IAECrawlerDataList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        getPTCNETList(mapper,crawler,startDate,endDate,IAECrawlerDataList);     //        零用金給付：IAECrawler.PayListFunction.PTCNET
        getCASHNETList(mapper,crawler,startDate,endDate,IAECrawlerDataList);    //        出納給付：IAECrawler.PayListFunction.CASHNET
        return IAECrawlerDataList;
    }
    private void getPTCNETList(ObjectMapper mapper, IAECrawler crawler, LocalDate startDate, LocalDate endDate, ArrayList<IAECrawlerData_Bean> IAECrawlerDataList) throws Exception{
        String PTCList = crawler.getPayListRaw(startDate,endDate,"", IAECrawler.PayListFunction.PTCNET);
        System.out.println(PTCList);
        for (JsonNode node : mapper.readTree(PTCList)) {
            try {
                IAECrawlerData_Bean IAECrawlerData_Bean = mapper.treeToValue(node, IAECrawlerData_Bean.class);
                IAECrawlerData_Bean.setInvoiceAmount(IAECrawlerData_Bean.getPayAmount());
                IAECrawlerData_Bean.setSource(IAECrawlerSource.零用金給付);
                IAECrawlerDataList.add(IAECrawlerData_Bean);
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        }
    }
    private void getCASHNETList(ObjectMapper mapper, IAECrawler crawler, LocalDate startDate, LocalDate endDate, ArrayList<IAECrawlerData_Bean> IAECrawlerDataList) throws Exception{
        String PaymentList = crawler.getPayListRaw(startDate,endDate,"", IAECrawler.PayListFunction.CASHNET);
        for (JsonNode node : mapper.readTree(PaymentList)) {
            try {
                IAECrawlerData_Bean IAECrawlerData_Bean = mapper.treeToValue(node, IAECrawlerData_Bean.class);
                IAECrawlerData_Bean.setInvoiceAmount(ToolKit.RoundingString(IAECrawlerData_Bean.getInvoiceAmount()));
                IAECrawlerData_Bean.setSource(IAECrawlerSource.出納給付);
                IAECrawlerDataList.add(IAECrawlerData_Bean);
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        }
    }
    private void combineTheSameInvoiceNumberData(ArrayList<IAECrawlerData_Bean> allIAECrawlerDataList){
        HashMap<String,ArrayList<IAECrawlerData_Bean>> repeatInvoiceNumberDataMap = new HashMap<>();
        for(IAECrawlerData_Bean IAECrawlerData_Bean : allIAECrawlerDataList){
            if(IAECrawlerData_Bean.getInvoiceNumber() != null){
                if(!repeatInvoiceNumberDataMap.containsKey(IAECrawlerData_Bean.getInvoiceNumber()))
                    repeatInvoiceNumberDataMap.put(IAECrawlerData_Bean.getInvoiceNumber(),new ArrayList<IAECrawlerData_Bean>(){{ add(IAECrawlerData_Bean);}});
                else
                    repeatInvoiceNumberDataMap.get(IAECrawlerData_Bean.getInvoiceNumber()).add(IAECrawlerData_Bean);
            }
        }
        for(String invoiceNumber : repeatInvoiceNumberDataMap.keySet()){
            ArrayList<IAECrawlerData_Bean> IAECrawlerDataList = repeatInvoiceNumberDataMap.get(invoiceNumber);
            if(IAECrawlerDataList.size() > 1){
                int payAmount = 0,invoiceAmount = 0,remittanceFee = 0;
                int minimnmRankKey = IAECrawlerDataList.get(0).getRankey();
                for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                    if(IAECrawlerData_Bean.getRankey() < minimnmRankKey)
                        minimnmRankKey = IAECrawlerData_Bean.getRankey();
                    payAmount = payAmount + Integer.parseInt(IAECrawlerData_Bean.getPayAmount());
                    invoiceAmount = invoiceAmount + Integer.parseInt(IAECrawlerData_Bean.getInvoiceAmount());
                    remittanceFee = remittanceFee + IAECrawlerData_Bean.getRemittanceFee();
                }
                for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                    if(IAECrawlerData_Bean.getRankey() == minimnmRankKey){
                        IAECrawlerData_Bean.setPayAmount(String.valueOf(payAmount));
                        IAECrawlerData_Bean.setInvoiceAmount(String.valueOf(invoiceAmount));
                        IAECrawlerData_Bean.setRemittanceFee(remittanceFee);
                    }else{
                        allIAECrawlerDataList.remove(IAECrawlerData_Bean);
                    }
                }
            }
        }
        repeatInvoiceNumberDataMap.clear();
    }
    private boolean compareIAECrawlerData(IAECrawlerData_Bean originIAECrawlerData_Bean, IAECrawlerData_Bean IAECrawlerData_Bean){
        if(!originIAECrawlerData_Bean.getInvoice_manufacturerNickName_id().equals(IAECrawlerData_Bean.getInvoice_manufacturerNickName_id()))
            return true;
        else if(!originIAECrawlerData_Bean.getInvoiceManufacturerNickName().equals(IAECrawlerData_Bean.getInvoiceManufacturerNickName()))
            return true;
        else if(!originIAECrawlerData_Bean.getBankAccount().equals(IAECrawlerData_Bean.getBankAccount()))
            return true;
        else if(!originIAECrawlerData_Bean.getPayDate().equals(IAECrawlerData_Bean.getPayDate()))
            return true;
        else if(((originIAECrawlerData_Bean.getInvoiceNumber() != null && IAECrawlerData_Bean.getInvoiceNumber() != null) && (!originIAECrawlerData_Bean.getInvoiceNumber().equals(IAECrawlerData_Bean.getInvoiceNumber()))) ||
                (originIAECrawlerData_Bean.getInvoiceNumber() != null && IAECrawlerData_Bean.getInvoiceNumber() == null) ||
                (originIAECrawlerData_Bean.getInvoiceNumber() == null && IAECrawlerData_Bean.getInvoiceNumber() != null))
            return true;
        else if(!originIAECrawlerData_Bean.getInvoiceAmount().equals(IAECrawlerData_Bean.getInvoiceAmount()))
            return true;
        else if(originIAECrawlerData_Bean.getRemittanceFee() != IAECrawlerData_Bean.getRemittanceFee())
            return true;
        else if(!originIAECrawlerData_Bean.getPayAmount().equals(IAECrawlerData_Bean.getPayAmount()))
            return true;
        else if(((originIAECrawlerData_Bean.getInvoiceDate() != null && IAECrawlerData_Bean.getInvoiceDate() != null) && (!originIAECrawlerData_Bean.getInvoiceDate().equals(IAECrawlerData_Bean.getInvoiceDate()))) ||
                (originIAECrawlerData_Bean.getInvoiceDate() != null && IAECrawlerData_Bean.getInvoiceDate() == null) ||
                (originIAECrawlerData_Bean.getInvoiceDate() == null && IAECrawlerData_Bean.getInvoiceDate() != null))
            return true;
        else if(!originIAECrawlerData_Bean.getSummonsNumber().equals(IAECrawlerData_Bean.getSummonsNumber()))
            return true;
        else if(!originIAECrawlerData_Bean.getInvoiceContent().equals(IAECrawlerData_Bean.getInvoiceContent()))
            return true;
        else if(!originIAECrawlerData_Bean.getProjectCode().equals(IAECrawlerData_Bean.getProjectCode()))
            return true;
        return false;
    }
    @FXML protected void previousDataMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            for(int index = 0 ; index < highlightTableViewItemList.size() ; index++){
                HashMap<Integer,Boolean> selectMap = highlightTableViewItemList.get(index);
                int itemIndex = selectMap.entrySet().iterator().next().getKey();
                boolean isSelect = selectMap.entrySet().iterator().next().getValue();
                if(isSelect && index == 0){
                    nowTableView.requestFocus();
                    nowTableView.getSelectionModel().select(itemIndex);
                    nowTableView.scrollTo(itemIndex);
                    nowTableView.getFocusModel().focus(itemIndex);
                   break;
                }else if(isSelect){
                    selectMap.put(itemIndex,false);
                    HashMap<Integer,Boolean> previousItem = highlightTableViewItemList.get(index-1);
                    itemIndex = previousItem.entrySet().iterator().next().getKey();
                    previousItem.put(itemIndex,true);
                    nowTableView.requestFocus();
                    nowTableView.getSelectionModel().select(itemIndex);
                    nowTableView.scrollTo(itemIndex);
                    nowTableView.getFocusModel().focus(itemIndex);
                    break;
                }
            }
        }
    }
    @FXML protected void nextDataMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            for(int index = 0 ; index < highlightTableViewItemList.size() ; index++){
                HashMap<Integer,Boolean> selectMap = highlightTableViewItemList.get(index);
                int itemIndex = selectMap.entrySet().iterator().next().getKey();
                boolean isSelect = selectMap.entrySet().iterator().next().getValue();
                if(isSelect && index == highlightTableViewItemList.size()-1){
                    nowTableView.requestFocus();
                    nowTableView.getSelectionModel().select(itemIndex);
                    nowTableView.scrollTo(itemIndex);
                    nowTableView.getFocusModel().focus(itemIndex);
                    break;
                }else if(isSelect){
                    selectMap.put(itemIndex,false);
                    HashMap<Integer,Boolean> nextItem = highlightTableViewItemList.get(index+1);
                    itemIndex = nextItem.entrySet().iterator().next().getKey();
                    nextItem.put(itemIndex,true);

                    nowTableView.requestFocus();
                    nowTableView.getSelectionModel().select(itemIndex);
                    nowTableView.scrollTo(itemIndex);
                    nowTableView.getFocusModel().focus(itemIndex);
                    break;
                }
            }
        }
    }
    @FXML protected void searchInvoiceTextKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String searchText = searchText_TextField.getText();
            if(searchPaymentAmount_RadioButton.isSelected() && !ToolKit.isDigital(searchText))
                DialogUI.MessageDialog("※ 金額格式錯誤");
            else{
                if(searchText.equals(""))
                    DialogUI.MessageDialog("※ 請輸入搜尋字眼");
                highlightTableViewMatchItems(searchText);
            }
        }
    }
    @FXML protected void searchTextMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String searchText = searchText_TextField.getText();
            if(searchPaymentAmount_RadioButton.isSelected() && !ToolKit.isDigital(searchText))
                DialogUI.MessageDialog("※ 金額格式錯誤");
            else{
                if(searchText.equals(""))
                    DialogUI.MessageDialog("※ 請輸入搜尋字眼");
                highlightTableViewMatchItems(searchText);
            }
        }
    }
    private void highlightTableViewMatchItems(String searchText){
        if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.未查核發票 || PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已查核發票 ||
                PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已忽略發票 || PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已遺失發票 ||
                PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.出納比對){
            isHighlightTableView = true;
            highlightTableViewItemList.clear();
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).addAll(AllIAECrawlerDataList);

            if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已查核發票)
                handleAlreadyTableView_CheckoutStatus();
            getSearchInvoiceResult(searchText);
        }
    }
    private void getSearchInvoiceResult(String searchText){
        highlightTableViewItemList.clear();
        if(searchText.equals("") || !isHighlightTableView)
            return;
        for(int index = 0 ; index < AllIAECrawlerDataList.size() ; index++){
            IAECrawlerData_Bean IAECrawlerData_Bean = AllIAECrawlerDataList.get(index);
            if((searchInvoiceNumber_RadioButton.isSelected() && IAECrawlerData_Bean.getInvoiceNumber() != null && IAECrawlerData_Bean.getInvoiceNumber().contains(searchText)) ||
                    (searchPaymentAmount_RadioButton.isSelected() && IAECrawlerData_Bean.getPayAmount().equals(searchText))) {
                HashMap<Integer,Boolean> selectItemMap = new HashMap<>();
                selectItemMap.put(index,false);
                highlightTableViewItemList.add(selectItemMap);
            }
        }
        if(highlightTableViewItemList.size() == 0){
            ComponentToolKit.setButtonDisable(previousData_Button,true);
            ComponentToolKit.setButtonDisable(nextData_Button,true);
        }else{
            HashMap<Integer,Boolean> firstItem = highlightTableViewItemList.get(0);
            int itemIndex = firstItem.entrySet().iterator().next().getKey();
            highlightTableViewItemList.get(0).put(itemIndex,true);

            Platform.runLater(()-> nowTableView.requestFocus());

            nowTableView.getSelectionModel().select(itemIndex);
            Platform.runLater(()-> nowTableView.scrollTo(itemIndex));
            nowTableView.getFocusModel().focus(itemIndex);
            ComponentToolKit.setButtonDisable(previousData_Button,false);
            ComponentToolKit.setButtonDisable(nextData_Button,false);
        }
        Platform.runLater(()-> DialogUI.MessageDialog("符合筆數 : " + highlightTableViewItemList.size()));
    }
    @FXML protected void clearStatusTextMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            updateIAECrawlerStatus_TextArea.setText("");
        }
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
    /** 未查核發票   */
    @FXML protected void noneReviewTabOnSelectionChanged() throws Exception{
        if(noneReviewTab.isSelected()) {
            setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.未查核發票);
            setTableColumnSort();
            searchNoneReviewData();
            getSearchInvoiceResult(searchText_TextField == null ? "" : searchText_TextField.getText());
        }
    }
    private void searchNoneReviewData(){
        ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
        this.AllIAECrawlerDataList = ManagePayableReceivable_Model.getAllIAECrawlerPaymentData(IAECrawlerStatus.新增, IAECrawlerReviewStatus.未查核,null);
        ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).addAll(AllIAECrawlerDataList);
    }
    @FXML protected void ignorePaymentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(DialogUI.ConfirmDialog("※ 是否忽略所選發票 ?",true,false,0,0)){
                ArrayList<IAECrawlerData_Bean> ignorePaymentList = new ArrayList<>();
                ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView);
                try{
                    for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                        if(IAECrawlerData_Bean.isCheckBoxSelect()){
                            ignorePaymentList.add(IAECrawlerData_Bean);
                        }
                    }
                    for(IAECrawlerData_Bean IAECrawlerData_Bean : ignorePaymentList){
                        if(ManagePayableReceivable_Model.setIAECrawlerDataStatus(null,null,null,null,IAECrawlerData_Bean,IAECrawlerStatus.忽略)) {
                            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).remove(IAECrawlerData_Bean);
                        }
                    }
                    ComponentToolKit.setButtonDisable(ignorePaymentButton,true);
                    refreshTableViewSerialNumber();
                    DialogUI.MessageDialog("※ 忽略成功");
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                    DialogUI.MessageDialog("※ 忽略失敗");
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
    }
    /** 已查核發票   */
    @FXML protected void alreadyReviewTabOnSelectionChanged() throws Exception{
        if(alreadyReviewTab.isSelected()){
            setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.已查核發票);
            checkoutStatus_ComboBox.getSelectionModel().selectFirst();
            setTableColumnSort();
            searchAlreadyReviewTableView();
            getSearchInvoiceResult(searchText_TextField.getText());
        }
    }
    public void searchAlreadyReviewTableView(){
        IAECrawlerDataAndOrderMap.clear();

        ComponentToolKit.setButtonDisable(deleteBindingButton,true);
        ComponentToolKit.setButtonDisable(establishReceivableButton,true);

        ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
        this.AllIAECrawlerDataList = ManagePayableReceivable_Model.getAllIAECrawlerPaymentData(IAECrawlerStatus.新增, IAECrawlerReviewStatus.已查核,IAECrawlerDataAndOrderMap);
        ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).addAll(AllIAECrawlerDataList);
    }
    private void handleAlreadyTableView_CheckoutStatus(){
        ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
        CheckoutStatus_Bean CheckoutStatus_Bean = ComponentToolKit.getCheckoutStatusComboBoxSelectItem(checkoutStatus_ComboBox);
        if(CheckoutStatus_Bean.isCheckout() != null){
            int serialNumber = 0;
            for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataAndOrderMap.keySet()){
                SearchOrder_Bean SearchOrder_Bean = IAECrawlerDataAndOrderMap.get(IAECrawlerData_Bean);
                if((SearchOrder_Bean.isCheckout() && CheckoutStatus_Bean.isCheckout()) || (!SearchOrder_Bean.isCheckout() && !CheckoutStatus_Bean.isCheckout())) {
                    serialNumber++;
                    IAECrawlerData_Bean.setSerialNumber(serialNumber);
                    ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).add(IAECrawlerData_Bean);
                }
            }
        }else
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).addAll(AllIAECrawlerDataList);
    }
    @FXML protected void alreadyReviewTableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
            IAECrawlerData_Bean IAECrawlerData_Bean = ComponentToolKit.getIAECrawlerDataTableViewSelectItem(nowTableView);
            if(IAECrawlerData_Bean != null){
                SearchOrder_Bean SearchOrder_Bean = ManagePayableReceivable_Model.getIARCrawlerDataBindingOrder(IAECrawlerDataAndOrderMap,IAECrawlerData_Bean);
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(SearchOrder_Bean.getOrderSource(), setOrder_BeanInfo(SearchOrder_Bean));
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }else if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerData_Bean IAECrawlerData_Bean = ComponentToolKit.getIAECrawlerDataTableViewSelectItem(nowTableView);
            if(IAECrawlerData_Bean != null){
                SearchOrder_Bean SearchOrder_Bean = IAECrawlerDataAndOrderMap.get(IAECrawlerData_Bean);
                alreadyOrderNumber_AlreadyReview.setText(SearchOrder_Bean.getAlreadyOrderNumber());
            }
        }
    }
    @FXML protected void alreadyReviewTableViewKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isUpKeyPressed(KeyEvent) || KeyPressed.isDownKeyPressed(KeyEvent)){
            IAECrawlerData_Bean IAECrawlerData_Bean = ComponentToolKit.getIAECrawlerDataTableViewSelectItem(nowTableView);
            if(IAECrawlerData_Bean != null){
                SearchOrder_Bean SearchOrder_Bean = IAECrawlerDataAndOrderMap.get(IAECrawlerData_Bean);
                alreadyOrderNumber_AlreadyReview.setText(SearchOrder_Bean.getAlreadyOrderNumber());
            }
        }
    }
    /** 已忽略發票*/
    @FXML protected void ignoreTabOnSelectionChanged() throws Exception{
        if(ignoreTab.isSelected()){
            setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.已忽略發票);
            setTableColumnSort();
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
            this.AllIAECrawlerDataList = ManagePayableReceivable_Model.getAllIAECrawlerPaymentData(IAECrawlerStatus.忽略,null,null);
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).addAll(AllIAECrawlerDataList);
            getSearchInvoiceResult(searchText_TextField.getText());
        }
    }
    @FXML protected void deleteIgnoreMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(DialogUI.ConfirmDialog("※ 是否解除所選發票 ?",true,false,0,0)){
                ArrayList<IAECrawlerData_Bean> deleteIgnorePaymentList = new ArrayList<>();
                ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView);
                try{
                    for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                        if(IAECrawlerData_Bean.isCheckBoxSelect()){
                            deleteIgnorePaymentList.add(IAECrawlerData_Bean);
                        }
                    }
                    for(IAECrawlerData_Bean IAECrawlerData_Bean : deleteIgnorePaymentList){
                        if(ManagePayableReceivable_Model.setIAECrawlerDataStatus(null,null,null,null,IAECrawlerData_Bean,IAECrawlerStatus.新增)) {
                            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).remove(IAECrawlerData_Bean);
                        }
                    }
                    ComponentToolKit.setButtonDisable(deleteIgnoreButton,true);
                    refreshTableViewSerialNumber();
                    DialogUI.MessageDialog("※ 解除成功");
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                    DialogUI.MessageDialog("※ 解除失敗");
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
    }

    /** 已遺失發票*/
    @FXML protected void missingTabOnSelectionChanged() throws Exception{
        if(missingTab.isSelected()){
            setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.已遺失發票);
            setTableColumnSort();
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
            this.AllIAECrawlerDataList = ManagePayableReceivable_Model.getAllIAECrawlerPaymentData(IAECrawlerStatus.遺失,null,null);
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).addAll(AllIAECrawlerDataList);
            getSearchInvoiceResult(searchText_TextField.getText());
        }
    }
    /** 出納比對 */
    @FXML protected void compareTabOnSelectionChanged() throws Exception{
        if(compareTab.isSelected()){
            setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.出納比對);
            setTableColumnSort();
            this.AllIAECrawlerDataList.clear();
            ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
            ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView).clear();
            invoiceAndOrderMap.clear();
            compareStatus_Label.setText("");
        }
    }
    /** 採購備記 */
    @FXML protected void purchaseNoteTabOnSelectionChanged(){
        if(purchaseNoteTab.isSelected()){
            setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.採購備記);
            setPurchaseNoteComboBoxData();
        }
    }
    private void setPurchaseNoteComboBoxData(){
        if(ComponentToolKit.getIAECrawlerAccountComboBoxItemList(manufacturerCodeComboBox_purchaseNote).size() == 0){
            ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAEAccount_ManufacturerNickName(true);
            if(IAECrawlerAccountList.size() != 0)
                manufacturerCodeComboBox_purchaseNote.getItems().add(new IAECrawlerAccount_Bean());
            manufacturerCodeComboBox_purchaseNote.getItems().addAll(IAECrawlerAccountList);
            manufacturerCodeComboBox_purchaseNote.getSelectionModel().selectFirst();
        }
    }
    /** 廠商往來明細 */
    @FXML protected void manufacturerContactDetailTabOnSelectionChanged(){
       if(manufacturerContactDetailTab.isSelected()){
           setNowTableColumnAndTableViewComponent(PayableReceivable_Enum.PaymentCompareTabName.廠商往來明細);
           setManufacturerContactDetailComponentData();
       }
    }
    private void setManufacturerContactDetailComponentData(){
        if(ComponentToolKit.getIAECrawlerAccountComboBoxItemList(manufacturerCodeComboBox_manufacturerContactDetail).size() == 0){
            ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAEAccount_ManufacturerNickName(true);
            manufacturerCodeComboBox_manufacturerContactDetail.getItems().addAll(IAECrawlerAccountList);

            ToggleGroup verifyFormulaToggleGroup = new ToggleGroup();
            for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
                RadioButton radioButton = ComponentToolKit.setRadioButton(IAECrawlerAccount_Bean.getObjectID() + "    " + IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectName(),-1,-1,18);
                radioButton.setToggleGroup(verifyFormulaToggleGroup);
                radioButton.setId(IAECrawlerAccount_Bean.getObjectID());
                if(IAECrawlerAccount_Bean.isExportQuotation_defaultSelect()){
                    ComponentToolKit.setRadioButtonSelect(radioButton,true);
                    manufacturerCodeComboBox_manufacturerContactDetail.getSelectionModel().select(IAECrawlerAccount_Bean);
                }else
                    ComponentToolKit.setRadioButtonSelect(radioButton,false);
                CustomMenuItem CustomMenuItem = new CustomMenuItem(radioButton);
                CustomMenuItem.setHideOnClick(true);
                verifyFormula_SplitMenuButton.getItems().add(CustomMenuItem);
            }
        }
    }
    /** 開啟資料夾 */
    @FXML protected void openFolderTabOnSelectionChanged(){
        Tab selectTab;
        if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.未查核發票) selectTab = noneReviewTab;
        else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已查核發票)    selectTab = alreadyReviewTab;
        else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已忽略發票)    selectTab = ignoreTab;
        else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已遺失發票)    selectTab = missingTab;
        else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.採購備記)    selectTab = purchaseNoteTab;
        else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.出納比對) selectTab = compareTab;
        else    selectTab = manufacturerContactDetailTab;
        if(openFolderTab.isSelected()){
            paymentCompareTabPane.getSelectionModel().select(selectTab);
            try {
                String FolderPath = CallConfig.getFile_OutputPath();
                if (FolderPath.equals("")) DialogUI.MessageDialog("※ 未設定路徑!");
                else Desktop.getDesktop().open(new File(FolderPath));
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    @FXML protected void searchPurchaseNoteMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(manufacturerCodeComboBox_purchaseNote);
            if(ToolKit.isDateRangeError(purchaseNoteStartDate_DatePicker,purchaseNoteEndDate_DatePicker)){
                DialogUI.MessageDialog("※ 日期格式錯誤");
                return;
            }
            String startDate = ComponentToolKit.getDatePickerValue(purchaseNoteStartDate_DatePicker,"yyyy-MM-dd");
            String endDate = ComponentToolKit.getDatePickerValue(purchaseNoteEndDate_DatePicker,"yyyy-MM-dd");
            purchaseNoteTableView.getItems().clear();
            ObservableList<IAECrawler_ExportPurchaseNote_Bean> purchaseNoteList = ManagePayableReceivable_Model.searchPurchaseNoteInfo(IAECrawlerAccount_Bean.getObjectInfo_Bean(),startDate,endDate);
            if(purchaseNoteList.size() == 0)
                DialogUI.MessageDialog("※ 查無相關資訊");
            else
                purchaseNoteTableView.getItems().addAll(purchaseNoteList);
        }
    }
    @FXML protected void exportPurchaseNoteMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<IAECrawler_ExportPurchaseNote_Bean> purchaseNoteList = ComponentToolKit.getIAECrawlerPurchaseNoteTableViewItemList(purchaseNoteTableView);
            if(purchaseNoteList.size() == 0) {
                DialogUI.MessageDialog("※ 無相關資訊");
                return;
            }
            try{
                IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(manufacturerCodeComboBox_purchaseNote);
                int exportErrorCount = 0;
                PurchaseAnnotationDocument document = new PurchaseAnnotationDocument();
                document.readTemplate(TemplatePath.IAECrawlerPurchaseNote);
                for(IAECrawler_ExportPurchaseNote_Bean IAECrawler_ExportPurchaseNote_Bean : purchaseNoteList){
                    if(IAECrawler_ExportPurchaseNote_Bean == null)
                        exportErrorCount ++;
                    else{
                        document.addRow(
                                IAECrawler_ExportPurchaseNote_Bean.getCheckoutStatus(),
                                IAECrawler_ExportPurchaseNote_Bean.getSchoolPayDate() == null ? null : LocalDate.parse(IAECrawler_ExportPurchaseNote_Bean.getSchoolPayDate()),
                                IAECrawler_ExportPurchaseNote_Bean.getSchoolPayAmount() == null ? 0 : IAECrawler_ExportPurchaseNote_Bean.getSchoolPayAmount(),
                                IAECrawler_ExportPurchaseNote_Bean.getManufacturerName(),
                                LocalDate.parse(IAECrawler_ExportPurchaseNote_Bean.getInvoiceDate()),
                                IAECrawler_ExportPurchaseNote_Bean.getInvoiceType(),
                                IAECrawler_ExportPurchaseNote_Bean.getInvoiceNumber(),
                                IAECrawler_ExportPurchaseNote_Bean.getInvoiceAmount(),
                                IAECrawler_ExportPurchaseNote_Bean.getCustomerID(),
                                LocalDate.parse(IAECrawler_ExportPurchaseNote_Bean.getOrderDate()),
                                IAECrawler_ExportPurchaseNote_Bean.getOrderNumber(),
                                IAECrawler_ExportPurchaseNote_Bean.getProjectName(),
                                IAECrawler_ExportPurchaseNote_Bean.getProjectTotalPriceIncludeTax(),
                                IAECrawler_ExportPurchaseNote_Bean.getCashierRemark()
                        );
                    }
                }
                String outputFileName = CallConfig.getFile_OutputPath() + "\\" + ToolKit.getToday("yyyy-MM-dd") + "_採購備記(";
                ObjectInfo_Bean ObjectInfo_Bean = IAECrawlerAccount_Bean.getObjectInfo_Bean();
                if(ObjectInfo_Bean == null) outputFileName = outputFileName + "所有廠商).xlsx";
                else    outputFileName = outputFileName + ObjectInfo_Bean.getObjectName() + ").xlsx";
                document.build(outputFileName);
                if(exportErrorCount != 0)
                    DialogUI.AlarmDialog("※ 匯出完成，匯出失敗筆數：" + exportErrorCount + ";  請確認發票是否綁定貨單");
                else
                    DialogUI.MessageDialog("※ 匯出成功");
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                DialogUI.ExceptionDialog(Ex);
                DialogUI.MessageDialog("※ 匯出失敗");
            }
        }
    }

    @FXML protected void manufacturerContactDetailOnAction(){
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(manufacturerCodeComboBox_manufacturerContactDetail);
        Order_Model.updateExportQuotationVendor(IAECrawlerAccount_Bean);
    }
    @FXML protected void selectAllManufacturerContactDetailOnAction(){
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetail = ComponentToolKit.getIAECrawlerContactDetailTableViewItemList(manufacturerContactDetailTableView);
        boolean selectAllCheckbox = selectAllCheckBox_manufacturerContactDetail.isSelected();
        for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : manufacturerContactDetail){
            if(IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() != CheckoutStatus_ManufacturerContactDetail.全部)
                IAECrawler_ManufacturerContactDetail_Bean.setCheckBoxSelect(selectAllCheckbox);
        }
    }
    @FXML protected void SelectAllCheckout_Search_ManufacturerContactDetailMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.setCheckBoxSelect(selectNoneCheckoutCheckBox_search_manufacturerContactDetail,selectAllCheckoutCheckBox_search_manufacturerContactDetail.isSelected());
            ComponentToolKit.setCheckBoxSelect(selectAlreadyCheckoutCheckBox_search_manufacturerContactDetail,selectAllCheckoutCheckBox_search_manufacturerContactDetail.isSelected());
        }
    }
    @FXML protected void SelectNoneCheckout_Search_ManufacturerContactDetailMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(selectNoneCheckoutCheckBox_search_manufacturerContactDetail.isSelected())
                ComponentToolKit.setCheckBoxSelect(selectAllCheckoutCheckBox_search_manufacturerContactDetail,selectAlreadyCheckoutCheckBox_search_manufacturerContactDetail.isSelected());
            else
                ComponentToolKit.setCheckBoxSelect(selectAllCheckoutCheckBox_search_manufacturerContactDetail,false);
        }
    }
    @FXML protected void SelectAlreadyCheckout_Search_ManufacturerContactDetailMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(selectAlreadyCheckoutCheckBox_search_manufacturerContactDetail.isSelected())
                ComponentToolKit.setCheckBoxSelect(selectAllCheckoutCheckBox_search_manufacturerContactDetail,selectNoneCheckoutCheckBox_search_manufacturerContactDetail.isSelected());
            else
                ComponentToolKit.setCheckBoxSelect(selectAllCheckoutCheckBox_search_manufacturerContactDetail,false);
        }
    }
    @FXML protected void searchManufacturerContactDetailMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.setCheckBoxSelect(selectAllCheckBox_manufacturerContactDetail,false);
            IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(manufacturerCodeComboBox_manufacturerContactDetail);
            if(ToolKit.isDateRangeError(manufacturerContactDetailStartDate_DatePicker,manufacturerContactDetailEndDate_DatePicker)){
                DialogUI.MessageDialog("※ 日期格式錯誤");
                return;
            }else if(IAECrawlerAccount_Bean == null){
                DialogUI.MessageDialog("※ 請選擇廠商");
                return;
            }
            String startDate = ComponentToolKit.getDatePickerValue(manufacturerContactDetailStartDate_DatePicker,"yyyy-MM-dd");
            String endDate = ComponentToolKit.getDatePickerValue(manufacturerContactDetailEndDate_DatePicker,"yyyy-MM-dd");
            CheckoutStatus CheckoutStatus = null;
            if(!selectAllCheckoutCheckBox_search_manufacturerContactDetail.isSelected() && selectNoneCheckoutCheckBox_search_manufacturerContactDetail.isSelected())
                    CheckoutStatus = Order_Enum.CheckoutStatus.未結帳;
            else if(!selectAllCheckoutCheckBox_search_manufacturerContactDetail.isSelected() && selectAlreadyCheckoutCheckBox_search_manufacturerContactDetail.isSelected())
                CheckoutStatus = Order_Enum.CheckoutStatus.已結帳;

            manufacturerContactDetailTableView.getItems().clear();
            ObservableList<IAECrawler_ManufacturerContactDetail_Bean> ManufacturerContactDetailList = ManagePayableReceivable_Model.searchManufacturerContactDetail(IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID(),startDate,endDate,CheckoutStatus);
            if(ManufacturerContactDetailList.size() == 0)
                DialogUI.MessageDialog("※ 查無相關資訊");
            else {
                manufacturerContactDetailTableView.getItems().addAll(ManufacturerContactDetailList);
            }
        }
    }
    @FXML protected void exportManufacturerContactDetailMouseClicked(MouseEvent MouseEvent) {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerAccount_Bean IAECrawlerAccount_Bean = ComponentToolKit.getIAECrawlerComboBoxSelectItem(manufacturerCodeComboBox_manufacturerContactDetail);
            ObservableList<IAECrawler_ManufacturerContactDetail_Bean> ContactDetailList = ComponentToolKit.getIAECrawlerContactDetailTableViewItemList(manufacturerContactDetailTableView);
            if(ContactDetailList.size() == 0)
                DialogUI.MessageDialog("※ 無相關資訊");
            else{
                try{
                    ManufacturerContactDocument document = new ManufacturerContactDocument();
                    document.readTemplate(TemplatePath.IAECrawlerInvoiceOverview);

                    for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : ContactDetailList){
                        String remark;
                        if(!IAECrawler_ManufacturerContactDetail_Bean.getIAECrawlerAccount_BelongName().equals(""))
                            remark = "* 匯費：" + IAECrawler_ManufacturerContactDetail_Bean.getRemittanceFee() + " 元；" + IAECrawler_ManufacturerContactDetail_Bean.getCashierRemark() + "(" + IAECrawler_ManufacturerContactDetail_Bean.getIAECrawlerAccount_BelongName() + ")";
                        else
                            remark = IAECrawler_ManufacturerContactDetail_Bean.getCashierRemark();
                        document.addRow(false,
                                LocalDate.parse(IAECrawler_ManufacturerContactDetail_Bean.getOrderDate()),
                                IAECrawler_ManufacturerContactDetail_Bean.getAlreadyInvoiceAmount(),
                                (IAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource() == ManufacturerContactDetailSource.廠商進貨 && IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.已開發票) ?
                                null: IAECrawler_ManufacturerContactDetail_Bean.getNoneInvoiceAmount(),
                                IAECrawler_ManufacturerContactDetail_Bean.getProjectName(),
                                IAECrawler_ManufacturerContactDetail_Bean.getInvoiceNumber(),
                                IAECrawler_ManufacturerContactDetail_Bean.getInvoiceType(),
                                IAECrawler_ManufacturerContactDetail_Bean.getSchoolAlreadyAmount(),
                                (IAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource() == ManufacturerContactDetailSource.廠商進貨 && IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額) ?
                                null : IAECrawler_ManufacturerContactDetail_Bean.getSchoolNoneAmount(),
                                null,
                                remark
                        );
                    }

                    document.build(CallConfig.getFile_OutputPath() + "\\" + ToolKit.getToday("yyyy-MM-dd") + "_廠商往來明細(" + IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectNickName()+ ").xlsx");
                    DialogUI.MessageDialog("※ 匯出成功");
                }catch (Exception Ex){
                    if(Ex.toString().contains("程序無法存取檔案，因為檔案正由另一個程序使用"))
                        DialogUI.AlarmDialog("※ 匯出失敗，因為檔案正由另一個程序使用");
                    else {
                        ERPApplication.Logger.catching(Ex);
                        DialogUI.ExceptionDialog(Ex);
                        DialogUI.MessageDialog("※ 匯出失敗");
                    }
                }

            }
        }
    }
    @FXML protected void SelectAllCheckBox_CheckoutStatus_ManufacturerContactDetailMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.setCheckBoxSelect(selectNoneInvoiceCheckBox_checkoutStatus_manufacturerContactDetail,selectAllCheckBox_checkoutStatus_manufacturerContactDetail.isSelected());
            ComponentToolKit.setCheckBoxSelect(selectNonePayCheckBox_checkoutStatus_manufacturerContactDetail,selectAllCheckBox_checkoutStatus_manufacturerContactDetail.isSelected());
        }
    }
    @FXML protected void SelectNoneInvoiceCheckBox_CheckoutStatus_ManufacturerContactDetail(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(selectNoneInvoiceCheckBox_checkoutStatus_manufacturerContactDetail.isSelected())
                ComponentToolKit.setCheckBoxSelect(selectAllCheckBox_checkoutStatus_manufacturerContactDetail,selectNonePayCheckBox_checkoutStatus_manufacturerContactDetail.isSelected());
            else
                ComponentToolKit.setCheckBoxSelect(selectAllCheckBox_checkoutStatus_manufacturerContactDetail,false);
        }
    }
    @FXML protected void SelectNonePayCheckBox_CheckoutStatus_ManufacturerContactDetail(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(selectNonePayCheckBox_checkoutStatus_manufacturerContactDetail.isSelected())
                ComponentToolKit.setCheckBoxSelect(selectAllCheckBox_checkoutStatus_manufacturerContactDetail,selectNoneInvoiceCheckBox_checkoutStatus_manufacturerContactDetail.isSelected());
            else
                ComponentToolKit.setCheckBoxSelect(selectAllCheckBox_checkoutStatus_manufacturerContactDetail,false);
        }
    }

    @FXML protected void setCheckoutManufacturerContactDetailMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailList = getSelectManufacturerContactDetailList();
            if(manufacturerContactDetailList == null)
                DialogUI.MessageDialog("※ 請選擇明細");
            else{
                CheckoutStatus_ManufacturerContactDetail checkoutStatus_ManufacturerContactDetail = CheckoutStatus_ManufacturerContactDetail.全部;
                if(!selectAllCheckBox_checkoutStatus_manufacturerContactDetail.isSelected() && selectNoneInvoiceCheckBox_checkoutStatus_manufacturerContactDetail.isSelected())
                    checkoutStatus_ManufacturerContactDetail = CheckoutStatus_ManufacturerContactDetail.已開發票;
                else if(!selectAllCheckBox_checkoutStatus_manufacturerContactDetail.isSelected() && selectNonePayCheckBox_checkoutStatus_manufacturerContactDetail.isSelected())
                    checkoutStatus_ManufacturerContactDetail = CheckoutStatus_ManufacturerContactDetail.已給付貨款金額;

                if(ManagePayableReceivable_Model.insertManufacturerContactDetailIsCheckout(manufacturerContactDetailList, checkoutStatus_ManufacturerContactDetail)){
                    for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : manufacturerContactDetailList){
                        IAECrawler_ManufacturerContactDetail_Bean.setCheckBoxSelect(false);
                        if(IAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource() == ManufacturerContactDetailSource.廠商進貨){
                            if((IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.已開發票 &&
                                    checkoutStatus_ManufacturerContactDetail == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額) ||
                                (IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額 &&
                                            checkoutStatus_ManufacturerContactDetail == CheckoutStatus_ManufacturerContactDetail.已開發票)){
                                IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(CheckoutStatus_ManufacturerContactDetail.全部);
                            }else
                                IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(checkoutStatus_ManufacturerContactDetail);

                            ComponentToolKit.setCheckBoxDisable(IAECrawler_ManufacturerContactDetail_Bean.getSelectCheckBox(),IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.全部);
                        }else {
                            IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(CheckoutStatus_ManufacturerContactDetail.全部);
                            ComponentToolKit.setCheckBoxDisable(IAECrawler_ManufacturerContactDetail_Bean.getSelectCheckBox(),true);
                        }
                    }
                    ComponentToolKit.setCheckBoxSelect(selectAllCheckBox_manufacturerContactDetail,false);
                    DialogUI.MessageDialog("※ 設定結帳成功");
                }else
                    DialogUI.MessageDialog("※ 設定結帳失敗");
            }
        }
    }
    private ObservableList<IAECrawler_ManufacturerContactDetail_Bean> getSelectManufacturerContactDetailList(){
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> selectList = null;
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailList = ComponentToolKit.getIAECrawlerContactDetailTableViewItemList(manufacturerContactDetailTableView);
        for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : manufacturerContactDetailList){

            if(IAECrawler_ManufacturerContactDetail_Bean.isCheckBoxSelect()){
                if(selectList == null)
                    selectList = FXCollections.observableArrayList();
                selectList.add(IAECrawler_ManufacturerContactDetail_Bean);
            }
        }
        return selectList;
    }
    @FXML protected void FormulaVerificationMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try{
                RadioButton selectedRadioButton = ComponentToolKit.getSplitMenuButton_RadioButtonSelected(verifyFormula_SplitMenuButton);
                String objectID = selectedRadioButton.getId();
                String inputText = DialogUI.TextInputDialog("公式驗證","【" + selectedRadioButton.getText() + "】\n檔名格式：檔案名稱_$-99999.xlsx","_$99999");
                if(inputText == null)
                    return;
                FileChooser FileChooser = ComponentToolKit.setFileChooser("上傳【廠商往來明細】");
                File outputFile = new File(CallConfig.getFile_OutputPath());
                if(!outputFile.exists() && !outputFile.isDirectory())
                    outputFile.mkdir();
                FileChooser.setInitialDirectory(outputFile);
                File templateFile = FileChooser.showOpenDialog(ComponentToolKit.setStage());
                if (templateFile != null) {
                    String fileName = templateFile.getName();
                    if(!fileName.contains("xlsx"))    DialogUI.MessageDialog("※ 上傳格式錯誤");
                    else if(!fileName.contains("$"))  DialogUI.MessageDialog("※ 無法驗證，檔名不存在符號【$】");
                    else{
                        String outputFilePath = CallConfig.getFile_OutputPath() + "\\(公式驗證)" + fileName;
                        ManufacturerContactDocument document = new ManufacturerContactDocument();
                        if(document.formulaVerification(templateFile, outputFilePath, objectID)){
                            int fileFinallyPrice = ToolKit.RoundingInteger(fileName.substring(fileName.lastIndexOf("$")+1,fileName.lastIndexOf(".")));
                            int verifyFinallyPrice = ToolKit.RoundingInteger(document.getVerifyFinallyPrice(outputFilePath));
                            int verifyDifferentDataSize = document.getVerifyDifferentDataSize();
                            boolean isDataCorrect = verifyDifferentDataSize == 0;
                            boolean isFinallyPriceCorrect = (fileFinallyPrice == verifyFinallyPrice);

                            int startIndex_textFill = 11,endIndex_textFill;
                            String confirmString = "驗證結果【資料/金額】";
                            if(isDataCorrect && isFinallyPriceCorrect){
                                confirmString = confirmString + "符合/符合";
                                endIndex_textFill = 16;
                            }else if(isDataCorrect || isFinallyPriceCorrect){
                                confirmString = isDataCorrect ? (confirmString + "符合/不符合") : (confirmString + "不符合/符合");
                                endIndex_textFill = 17;
                            }else{
                                confirmString = confirmString + "不符合/不符合";
                                endIndex_textFill = 18;
                            }
                            confirmString = confirmString + " (" + fileFinallyPrice+ "/" + verifyFinallyPrice + ")\n" +
                            "已匯出【(公式驗證)" + templateFile.getName() + "】\n" +
                                    "是否開啟 ?";
                            if(DialogUI.ConfirmDialog(confirmString,true, true, startIndex_textFill, endIndex_textFill)){
                                File file = new File(outputFilePath);
                                Desktop desktop = Desktop.getDesktop();
                                if(file.exists())
                                    desktop.open(file);
                            }
                        }else
                            DialogUI.AlarmDialog("※ 驗證失敗");
                    }
                }
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    @FXML protected void compareTableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerData_Bean IAECrawlerData_Bean = compareTableView.getSelectionModel().getSelectedItem();
            if(IAECrawlerData_Bean != null){
                ObservableList<SearchOrder_Bean> searchOrderList = invoiceAndOrderMap.get(IAECrawlerData_Bean);
                ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView).clear();
                if(searchOrderList.size() == 0)
                    DialogUI.MessageDialog("※ 找無此發票相關貨單");
                else {
                    for(SearchOrder_Bean SearchOrder_Bean : searchOrderList){
                        SearchOrder_Bean.setBindingIAECrawlerPayment_id(ManagePayableReceivable_Model.getOrderBindingPayment_id(SearchOrder_Bean));
                    }
                    ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView).addAll(searchOrderList);
                }
            }
        }
    }
    @FXML protected void comparePaymentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Task Task = comparePaymentTask();
            compareProgressBar.progressProperty().bind(Task.progressProperty());
            new Thread(Task).start();
        }
    }
    private Task comparePaymentTask(){
        return new Task() {
            @Override public Void call(){
                try{
                    ComponentToolKit.setLabelVisible(compareStatus_Label,false);
                    ComponentToolKit.setProgressBarVisible(compareProgressBar,true);
                    ComponentToolKit.setLabelVisible(compareStatus_Label,true);
                    Platform.runLater(()-> compareStatus_Label.setText("比對中..."));
                    invoiceAndOrderMap.clear();
                    ComponentToolKit.getIAECrawlerDataTableViewItemList(compareTableView).clear();
                    ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView).clear();

                    AllIAECrawlerDataList = ManagePayableReceivable_Model.getAllIAECrawlerPaymentData(IAECrawlerStatus.新增,IAECrawlerReviewStatus.未查核,null);
                    ArrayList<IAECrawlerData_Bean> isAutomaticBindingList = findSimilarOrder(AllIAECrawlerDataList);
                    if(isAutomaticBindingList != null){
                        writeStatusText("*** 已自動綁定以下發票 ↓ ↓ ↓"+ "\n");
                        for(int index = 0 ; index < isAutomaticBindingList.size() ; index++){
                            IAECrawlerData_Bean IAECrawlerData_Bean = isAutomaticBindingList.get(index);
                            AllIAECrawlerDataList.remove(IAECrawlerData_Bean);
                            writeStatusText((index+1) + ". [" + IAECrawlerData_Bean.getObjectName() + "] 統編： " + IAECrawlerData_Bean.getObjectTaxID() + "  發票號碼：" + IAECrawlerData_Bean.getInvoiceNumber() + "  金額：" + IAECrawlerData_Bean.getPayAmount() + "\n");
                        }

                        writeStatusText("------------------------------"+ "\n");
                        Platform.runLater(()-> DialogUI.AlarmDialog("※ 發票號碼吻合數量(已綁定)：" + isAutomaticBindingList.size()));
//                        DialogUI.AlarmDialog("※ 發票號碼吻合數量(已綁定)：" + isAutomaticBindingList.size());
                    }
                    if(AllIAECrawlerDataList.size() != 0) {
                        ComponentToolKit.getIAECrawlerDataTableViewItemList(compareTableView).addAll(AllIAECrawlerDataList);
                        getSearchInvoiceResult(searchText_TextField.getText());
                    }
                    Platform.runLater(()-> compareStatus_Label.setText("比對完成!"));
                }catch (Exception Ex){
                    Platform.runLater(()-> compareStatus_Label.setText("比對失敗!"));
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }finally {
                    ComponentToolKit.setProgressBarVisible(compareProgressBar,false);
                }
                return null;
            }
        };
    }
    private ArrayList<IAECrawlerData_Bean> findSimilarOrder(ObservableList<IAECrawlerData_Bean> AllIAECrawlerDataList){
        ArrayList<IAECrawlerData_Bean> isAutomaticBindingList = null;
        for(IAECrawlerData_Bean IAECrawlerData_Bean : AllIAECrawlerDataList){
            ObservableList<SearchOrder_Bean> invoiceSimilarOrderList = ManagePayableReceivable_Model.searchInvoiceSimilarOrder(Order_Enum.OrderSource.出貨單,IAECrawlerData_Bean);
            invoiceSimilarOrderList.addAll(ManagePayableReceivable_Model.searchInvoiceSimilarOrder(Order_Enum.OrderSource.出貨子貨單,IAECrawlerData_Bean));
            SearchOrder_Bean SearchOrder_Bean = getAutomaticBindingOrder(IAECrawlerData_Bean, invoiceSimilarOrderList);
            if(SearchOrder_Bean != null){
                if(isAutomaticBindingList == null)  isAutomaticBindingList = new ArrayList<>();
                if(ManagePayableReceivable_Model.setIAECrawlerInvoiceBindingOrder(false,IAECrawlerData_Bean,SearchOrder_Bean))
                    isAutomaticBindingList.add(IAECrawlerData_Bean);
                else
                    DialogUI.MessageDialog("※ 綁定失敗");
            }else{
                invoiceAndOrderMap.put(IAECrawlerData_Bean,sortInvoiceSimilarOrderList(IAECrawlerData_Bean, invoiceSimilarOrderList));
            }
        }
        return isAutomaticBindingList;
    }
    private SearchOrder_Bean getAutomaticBindingOrder(IAECrawlerData_Bean IAECrawlerData_Bean, ObservableList<SearchOrder_Bean> invoiceSimilarOrderList){
        for(SearchOrder_Bean SearchOrder_Bean : invoiceSimilarOrderList){
            if(SearchOrder_Bean.getInvoiceNumber() != null &&
                    SearchOrder_Bean.getInvoiceNumber().equals(IAECrawlerData_Bean.getInvoiceNumber()) &&
                    IAECrawlerData_Bean.getInvoice_manufacturerNickName_id() == SearchOrder_Bean.getInvoice_manufacturerNickName_id()){
                return SearchOrder_Bean;
            }
        }
        return null;
    }
    private ObservableList<SearchOrder_Bean> sortInvoiceSimilarOrderList(IAECrawlerData_Bean IAECrawlerData_Bean, ObservableList<SearchOrder_Bean> invoiceSimilarOrderList){
        ObservableList<SearchOrder_Bean> sortSimilarOrderList = FXCollections.observableArrayList();
        for(SearchOrder_Bean similarOrder_Bean : invoiceSimilarOrderList){
            if(IAECrawlerData_Bean.getInvoiceNumber() != null &&
                    IAECrawlerData_Bean.getInvoiceNumber().equals(similarOrder_Bean.getInvoiceNumber()) &&
                    IAECrawlerData_Bean.getInvoice_manufacturerNickName_id() != similarOrder_Bean.getInvoice_manufacturerNickName_id()){
                if(sortSimilarOrderList.size() == 0)    sortSimilarOrderList.add(similarOrder_Bean);
                else{
                    int insertIndex = 0;
                    for(int index = 0 ; index < sortSimilarOrderList.size() ; index++){
                        SearchOrder_Bean SearchOrder_Bean = sortSimilarOrderList.get(index);
                        if(SearchOrder_Bean.getInvoice_manufacturerNickName_id() != similarOrder_Bean.getInvoice_manufacturerNickName_id() ||
                                SearchOrder_Bean.getTotalPriceIncludeTax() != similarOrder_Bean.getTotalPriceIncludeTax()){
                            insertIndex = index;
                            break;
                        }
                    }
                    sortSimilarOrderList.add(insertIndex,similarOrder_Bean);
                }
            }else if(IAECrawlerData_Bean.getInvoice_manufacturerNickName_id() == similarOrder_Bean.getInvoice_manufacturerNickName_id() &&
                    Integer.parseInt(IAECrawlerData_Bean.getPayAmount()) == similarOrder_Bean.getTotalPriceIncludeTax()) {
                Integer insertIndex = null;
                for(int index = 0 ; index < sortSimilarOrderList.size() ; index++) {
                    SearchOrder_Bean SearchOrder_Bean = sortSimilarOrderList.get(index);
                    if (SearchOrder_Bean.getInvoice_manufacturerNickName_id() == similarOrder_Bean.getInvoice_manufacturerNickName_id() &&
                            SearchOrder_Bean.getTotalPriceIncludeTax() == similarOrder_Bean.getTotalPriceIncludeTax()) {
                        insertIndex = index;
                        break;
                    }
                }
                if(insertIndex != null) sortSimilarOrderList.add(insertIndex,similarOrder_Bean);
                else    sortSimilarOrderList.add(sortSimilarOrderList.size(),similarOrder_Bean);
            }else
                sortSimilarOrderList.add(sortSimilarOrderList.size(),similarOrder_Bean);
        }
        invoiceSimilarOrderList.clear();
        for(int index = 0 ; index < sortSimilarOrderList.size(); index++){
            sortSimilarOrderList.get(index).setSerialNumber(index+1);
        }
        return sortSimilarOrderList;
    }
    @FXML protected void comparePayment_searchManufacturerOrder_MouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerData_Bean iAECrawlerData_Bean = compareTableView.getSelectionModel().getSelectedItem();
            if(iAECrawlerData_Bean == null){
                DialogUI.MessageDialog("請選擇出納資料");
            }else if(ToolKit.isDateRangeError(StartDate_DatePicker,EndDate_DatePicker)){
                Platform.runLater(()-> DialogUI.MessageDialog("※ 日期區間錯誤"));
            }else{
                LocalDate startDate = LocalDate.parse(ComponentToolKit.getDatePickerValue(StartDate_DatePicker,"yyyy-MM-dd"));
                LocalDate endDate = LocalDate.parse(ComponentToolKit.getDatePickerValue(EndDate_DatePicker,"yyyy-MM-dd"));
                ObservableList<SearchOrder_Bean> searchOrderList = ManagePayableReceivable_Model.searchNoneBindingManufacturerOrder(Order_Enum.OrderSource.出貨子貨單,
                        iAECrawlerData_Bean.getInvoice_manufacturerNickName_id(),
                        startDate,
                        endDate);
                searchOrderList.addAll(ManagePayableReceivable_Model.searchNoneBindingManufacturerOrder(Order_Enum.OrderSource.出貨單,
                        iAECrawlerData_Bean.getInvoice_manufacturerNickName_id(),
                        startDate,
                        endDate));

                ObservableList<SearchOrder_Bean> compareOrderList = invoiceAndOrderMap.get(iAECrawlerData_Bean);
                ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView).clear();
                for(SearchOrder_Bean searchOrder_Bean : compareOrderList){
                    searchOrder_Bean.setBindingIAECrawlerPayment_id(ManagePayableReceivable_Model.getOrderBindingPayment_id(searchOrder_Bean));
                }
                ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView).addAll(compareOrderList);

                if(searchOrderList.size() == 0) {
                    DialogUI.MessageDialog("無其他貨單");
                }else{
                    for(int index = 0; index < searchOrderList.size(); index++){
                        int itemSize = ComponentToolKit.getSearchOrderTableViewItemSize(compareOrderTableView);
                        SearchOrder_Bean searchOrder_Bean = searchOrderList.get(index);
                        searchOrder_Bean.setSerialNumber(itemSize == 0 ? (index+1) : (itemSize+1));
                        ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView).add(searchOrder_Bean);
                    }
                }
            }
        }
    }
    @FXML protected void comparePayment_searchManufacturerOrder_MouseMoved(MouseEvent MouseEvent){
        String startDate = ComponentToolKit.getDatePickerValue(StartDate_DatePicker, "yyyy-MM-dd");
        String endDate = ComponentToolKit.getDatePickerValue(EndDate_DatePicker, "yyyy-MM-dd");
        tooltip_compare_searchOrderDate.setText("搜尋日期區間：" + startDate + " ~ " + endDate);
        tooltip_compare_searchOrderDate.show(comparePayment_searchManufacturerOrder_Button, MouseEvent.getScreenX()+10, MouseEvent.getScreenY()+7);
    }
    @FXML protected void comparePayment_searchManufacturerOrder_MouseExited(){
        tooltip_compare_searchOrderDate.hide();
    }
    @FXML protected void compareOrderTableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
            SearchOrder_Bean SearchOrder_Bean = ComponentToolKit.getSearchOrderTableViewSelectItem(compareOrderTableView);
            if(SearchOrder_Bean != null){
                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = createEstablishOrderNewStageBean(SearchOrder_Bean.getOrderSource(), setOrder_BeanInfo(SearchOrder_Bean));
                CallFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }
    }
    private Order_Bean setOrder_BeanInfo(SearchOrder_Bean SearchOrder_Bean){
        Order_Bean Order_Bean = new Order_Bean();
        if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.採購單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 ||
                SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.報價單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
            Order_Bean.setNowOrderNumber(SearchOrder_Bean.getQuotationNumber());
        else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
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
    private EstablishOrder_NewStage_Bean createEstablishOrderNewStageBean(OrderSource orderSource, Order_Bean order_bean){
        EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
        establishOrder_NewStage_Bean.setOrderStatus(OrderStatus.有效貨單);
        establishOrder_NewStage_Bean.setOrderSource(orderSource);
        establishOrder_NewStage_Bean.setOrderExist(OrderExist.已存在);
        establishOrder_NewStage_Bean.setOrder_Bean(order_bean);
        establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
        establishOrder_NewStage_Bean.setSearchOrder_Controller(null);
        establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
        return establishOrder_NewStage_Bean;
    }
    @FXML protected void checkoutStatusOnAction(){
        ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).clear();
        CheckoutStatus_Bean CheckoutStatus_Bean = ComponentToolKit.getCheckoutStatusComboBoxSelectItem(checkoutStatus_ComboBox);
        int serialNumber = 0;
        for(IAECrawlerData_Bean IAECrawlerData_Bean : AllIAECrawlerDataList){
            SearchOrder_Bean SearchOrder_Bean = IAECrawlerDataAndOrderMap.get(IAECrawlerData_Bean);
            if((CheckoutStatus_Bean.isCheckout() == null) ||
                (CheckoutStatus_Bean.isCheckout() && SearchOrder_Bean.isCheckout()) ||
                (!CheckoutStatus_Bean.isCheckout() && !SearchOrder_Bean.isCheckout())){
                serialNumber++;
                IAECrawlerData_Bean.setCheckBoxSelect(false);
                IAECrawlerData_Bean.setSerialNumber(serialNumber);
                ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView).add(IAECrawlerData_Bean);
            }
        }
    }
    @FXML protected void deleteBindingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(DialogUI.ConfirmDialog("※ 是否確定解除綁定 ?",true,false,0,0)){
                ArrayList<IAECrawlerData_Bean> deleteBindingList = new ArrayList<>();
                ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(alreadyReviewTableView);
                try{
                    for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                        if(IAECrawlerData_Bean.isCheckBoxSelect()){
                            deleteBindingList.add(IAECrawlerData_Bean);
                        }
                    }
                    for(IAECrawlerData_Bean IAECrawlerData_Bean : deleteBindingList){
                        SearchOrder_Bean SearchOrder_Bean = ManagePayableReceivable_Model.getIARCrawlerDataBindingOrder(IAECrawlerDataAndOrderMap,IAECrawlerData_Bean);
                        if(ManagePayableReceivable_Model.deleteIAECrawlerDataBindingOrder(IAECrawlerData_Bean.getId(),SearchOrder_Bean))
                            ComponentToolKit.getIAECrawlerDataTableViewItemList(alreadyReviewTableView).remove(IAECrawlerData_Bean);
                    }
                    ComponentToolKit.setButtonDisable(deleteBindingButton,true);
                    ComponentToolKit.setButtonDisable(establishReceivableButton,true);
                    refreshTableViewSerialNumber();
                    DialogUI.MessageDialog("※ 解除成功");
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                    DialogUI.MessageDialog("※ 解除失敗");
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
    }
    private void refreshTableViewSerialNumber(){
        ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView);
        for(int index = 0 ; index < IAECrawlerDataList.size() ; index++){
            IAECrawlerDataList.get(index).setSerialNumber(index+1);
        }
    }
    @FXML protected void establishReceivableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            HashMap<String,ObservableList<SearchOrder_Bean>> ReceivableOrderMap = generateReceivableOrderList();
            if(ReceivableOrderMap.size() == 0){
                DialogUI.MessageDialog("請選擇款項");
                return;
            }
            for(String ObjectID : ReceivableOrderMap.keySet()){
                CallFXML.EstablishPayableReceivable_NewStage(Order_Enum.OrderObject.客戶, PayableReceivable_Enum.PayableReceivableStatus.建立,null,this, setPayableReceivable_Bean(ObjectID,ReceivableOrderMap.get(ObjectID)),null);
            }
        }
    }
    private HashMap<String,ObservableList<SearchOrder_Bean>> generateReceivableOrderList(){
        HashMap<String,ObservableList<SearchOrder_Bean>> ReceivableOrderMap = new HashMap<>();
        ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(alreadyReviewTableView);
        for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
            if(IAECrawlerData_Bean.isCheckBoxSelect()){
                SearchOrder_Bean SearchOrder_Bean = ManagePayableReceivable_Model.getIARCrawlerDataBindingOrder(IAECrawlerDataAndOrderMap,IAECrawlerData_Bean);
                if(!SearchOrder_Bean.isCheckout()){
                    if(!ReceivableOrderMap.containsKey(SearchOrder_Bean.getObjectID())){
                        ObservableList<SearchOrder_Bean> OrderList = FXCollections.observableArrayList();
                        OrderList.add(SearchOrder_Bean);
                        ReceivableOrderMap.put(SearchOrder_Bean.getObjectID(),OrderList);
                    }else{
                        ReceivableOrderMap.get(SearchOrder_Bean.getObjectID()).add(SearchOrder_Bean);
                    }
                }
            }
        }
        return ReceivableOrderMap;
    }
    private PayableReceivable_Bean setPayableReceivable_Bean(String ObjectID, ObservableList<SearchOrder_Bean> OrderList){
        PayableReceivable_Bean PayableReceivable_Bean = new PayableReceivable_Bean();
        PayableReceivable_Bean.setOrderDate(ToolKit.getToday("yyyy-MM-dd"));
        PayableReceivable_Bean.setOrderObject(Order_Enum.OrderObject.客戶);
        PayableReceivable_Bean.setObjectID(ObjectID);
        PayableReceivable_Bean.setOrderList(OrderList);
        return PayableReceivable_Bean;
    }
   @FXML protected void TableViewSettingMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CallFXML.ShowPaymentCompareTableViewSetting(MainStage, PaymentCompareTabName,this, TableViewSettingMap);
        }
    }
    private void setNowTableColumnAndTableViewComponent(PaymentCompareTabName PaymentCompareTabName){
        isHighlightTableView = false;
        this.PaymentCompareTabName = PaymentCompareTabName;
        if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.未查核發票) {
            nowTableView = noneReviewTableView;
            nowSerialNumberColumn = serialNumberColumn_noneReview;
            nowBelongNameColumn = belongNameColumn_noneReview;
            nowSummonsNumberColumn = summonsNumberColumn_noneReview;
            nowObjectIDColumn = objectIDColumn_noneReview;
            nowObjectNameColumn = objectNameColumn_noneReview;
            nowObjectTaxIDColumn = objectTaxIDColumn_noneReview;
            nowPayDateColumn = payDateColumn_noneReview;
            nowPayAmountColumn = payAmountColumn_noneReview;
            nowRemittanceFeeColumn = remittanceFeeColumn_noneReview;
            nowBankAccountColumn = bankAccountColumn_noneReview;
            nowInvoiceContentColumn = invoiceContentColumn_noneReview;
            nowInvoiceDateColumn = invoiceDateColumn_noneReview;
            nowInvoiceNumberColumn = invoiceNumberColumn_noneReview;
            nowInvoiceAmountColumn = invoiceAmountColumn_noneReview;
            nowProjectCodeColumn = projectCodeColumn_noneReview;
            nowSourceColumn = sourceColumn_noneReview;
        }else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已查核發票) {
            nowTableView = alreadyReviewTableView;
            nowSerialNumberColumn = serialNumberColumn_alreadyReview;
            nowBelongNameColumn = belongNameColumn_alreadyReview;
            nowSummonsNumberColumn = summonsNumberColumn_alreadyReview;
            nowObjectIDColumn = objectIDColumn_alreadyReview;
            nowObjectNameColumn = objectNameColumn_alreadyReview;
            nowObjectTaxIDColumn = objectTaxIDColumn_alreadyReview;
            nowPayDateColumn = payDateColumn_alreadyReview;
            nowPayAmountColumn = payAmountColumn_alreadyReview;
            nowRemittanceFeeColumn = remittanceFeeColumn_alreadyReview;
            nowBankAccountColumn = bankAccountColumn_alreadyReview;
            nowInvoiceContentColumn = invoiceContentColumn_alreadyReview;
            nowInvoiceDateColumn = invoiceDateColumn_alreadyReview;
            nowInvoiceNumberColumn = invoiceNumberColumn_alreadyReview;
            nowInvoiceAmountColumn = invoiceAmountColumn_alreadyReview;
            nowProjectCodeColumn = projectCodeColumn_alreadyReview;
            nowSourceColumn = sourceColumn_alreadyReview;
        }else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已忽略發票) {
            nowTableView = ignoreTableView;
            nowSerialNumberColumn = serialNumberColumn_ignore;
            nowBelongNameColumn = belongNameColumn_ignore;
            nowSummonsNumberColumn = summonsNumberColumn_ignore;
            nowObjectIDColumn = objectIDColumn_ignore;
            nowObjectNameColumn = objectNameColumn_ignore;
            nowObjectTaxIDColumn = objectTaxIDColumn_ignore;
            nowPayDateColumn = payDateColumn_ignore;
            nowPayAmountColumn = payAmountColumn_ignore;
            nowRemittanceFeeColumn = remittanceFeeColumn_ignore;
            nowBankAccountColumn = bankAccountColumn_ignore;
            nowInvoiceContentColumn = invoiceContentColumn_ignore;
            nowInvoiceDateColumn = invoiceDateColumn_ignore;
            nowInvoiceNumberColumn = invoiceNumberColumn_ignore;
            nowInvoiceAmountColumn = invoiceAmountColumn_ignore;
            nowProjectCodeColumn = projectCodeColumn_ignore;
            nowSourceColumn = sourceColumn_ignore;
        }else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已遺失發票) {
            nowTableView = missingTableView;
            nowSerialNumberColumn = serialNumberColumn_missing;
            nowBelongNameColumn = belongNameColumn_missing;
            nowSummonsNumberColumn = summonsNumberColumn_missing;
            nowObjectIDColumn = objectIDColumn_missing;
            nowObjectNameColumn = objectNameColumn_missing;
            nowObjectTaxIDColumn = objectTaxIDColumn_missing;
            nowPayDateColumn = payDateColumn_missing;
            nowPayAmountColumn = payAmountColumn_missing;
            nowRemittanceFeeColumn = remittanceFeeColumn_missing;
            nowBankAccountColumn = bankAccountColumn_missing;
            nowInvoiceContentColumn = invoiceContentColumn_missing;
            nowInvoiceDateColumn = invoiceDateColumn_missing;
            nowInvoiceNumberColumn = invoiceNumberColumn_missing;
            nowInvoiceAmountColumn = invoiceAmountColumn_missing;
            nowProjectCodeColumn = projectCodeColumn_missing;
            nowSourceColumn = sourceColumn_missing;
        }else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.出納比對) {
            nowTableView = compareTableView;
            nowSerialNumberColumn = serialNumberColumn_compare;
            nowBelongNameColumn = belongNameColumn_compare;
            nowSummonsNumberColumn = summonsNumberColumn_compare;
            nowObjectIDColumn = objectIDColumn_compare;
            nowObjectNameColumn = objectNameColumn_compare;
            nowObjectTaxIDColumn = objectTaxIDColumn_compare;
            nowPayDateColumn = payDateColumn_compare;
            nowPayAmountColumn = payAmountColumn_compare;
            nowRemittanceFeeColumn = remittanceFeeColumn_compare;
            nowBankAccountColumn = bankAccountColumn_compare;
            nowInvoiceContentColumn = invoiceContentColumn_compare;
            nowInvoiceDateColumn = invoiceDateColumn_compare;
            nowInvoiceNumberColumn = invoiceNumberColumn_compare;
            nowInvoiceAmountColumn = invoiceAmountColumn_compare;
            nowProjectCodeColumn = projectCodeColumn_compare;
            nowSourceColumn = sourceColumn_compare;
        }
    }
    private void setTableColumnSort() throws Exception{
        TableViewSettingMap = CallConfig.getPaymentCompareTableViewSettingJson(PaymentCompareTabName);
        refreshTableView(TableViewSettingMap);
    }
    public void refreshTableView(JSONObject TableViewSettingMap){
        this.TableViewSettingMap = TableViewSettingMap;
        nowTableView.getColumns().clear();
        if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.未查核發票) nowTableView.getColumns().add(selectColumn_noneReview);
        else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已查核發票)    nowTableView.getColumns().add(selectColumn_alreadyReview);
        else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已忽略發票)    nowTableView.getColumns().add(selectColumn_ignore);
        nowTableView.getColumns().add(nowSerialNumberColumn);
        for(String ColumnName : TableViewSettingMap.keySet()){
            if(TableViewSettingMap.getBoolean(ColumnName))   nowTableView.getColumns().add(getTableColumn(IAECrawlerDataTableColumn.valueOf(ColumnName)));
            ComponentToolKit.setTableColumnVisible(getTableColumn(IAECrawlerDataTableColumn.valueOf(ColumnName)),TableViewSettingMap.getBoolean(ColumnName));
        }
    }
    private TableColumn<IAECrawlerData_Bean,String> getTableColumn(PayableReceivable_Enum.IAECrawlerDataTableColumn IAECrawlerDataTableColumn){
        if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.學校)  return nowBelongNameColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.傳票編號)  return nowSummonsNumberColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.廠商編號)  return nowObjectIDColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.廠商名稱)  return nowObjectNameColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.廠商統編) return nowObjectTaxIDColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.付款日期)  return nowPayDateColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.付款金額)  return nowPayAmountColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.銀行帳戶)  return nowBankAccountColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.匯費)  return nowRemittanceFeeColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.摘要)  return nowInvoiceContentColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.發票日期)  return nowInvoiceDateColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.發票號碼)  return nowInvoiceNumberColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.發票金額)  return nowInvoiceAmountColumn;
        else if(IAECrawlerDataTableColumn == PayableReceivable_Enum.IAECrawlerDataTableColumn.來源)  return nowSourceColumn;
        else return nowProjectCodeColumn;
    }
    private void writeStatusText(String statusText){
        updateIAECrawlerStatus_TextArea.appendText(statusText);
    }
    private void setColumnCellBackgroundColor(TableColumn<IAECrawlerData_Bean, String> TableColumn1,
                                              TableColumn<IAECrawlerData_Bean, String> TableColumn2,
                                              TableColumn<IAECrawlerData_Bean, String> TableColumn3,
                                              TableColumn<IAECrawlerData_Bean, String> TableColumn4,
                                              TableColumn<IAECrawlerData_Bean, String> TableColumn5, String ColumnValue,
                                              boolean isFmtMicrometer){
        ComponentToolKit.setColumnCellValue(TableColumn1,ColumnValue,"CENTER-LEFT","16",null);
        TableColumn1.setCellFactory(column -> new setColumnCellBackgroundColor("CENTER-LEFT", "16",isFmtMicrometer));

        ComponentToolKit.setColumnCellValue(TableColumn2,ColumnValue,"CENTER-LEFT","16",null);
        TableColumn2.setCellFactory(column -> new setColumnCellBackgroundColor("CENTER-LEFT", "16",isFmtMicrometer));

        ComponentToolKit.setColumnCellValue(TableColumn3,ColumnValue,"CENTER-LEFT","16",null);
        TableColumn3.setCellFactory(column -> new setColumnCellBackgroundColor("CENTER-LEFT", "16",isFmtMicrometer));

        ComponentToolKit.setColumnCellValue(TableColumn4,ColumnValue,"CENTER-LEFT","16",null);
        TableColumn4.setCellFactory(column -> new setColumnCellBackgroundColor("CENTER-LEFT", "16",isFmtMicrometer));

        ComponentToolKit.setColumnCellValue(TableColumn5,ColumnValue,"CENTER-LEFT","16",null);
        TableColumn5.setCellFactory(column -> new setColumnCellBackgroundColor("CENTER-LEFT", "16",isFmtMicrometer));
    }
    private class setColumnCellBackgroundColor extends TableCell<IAECrawlerData_Bean, String> {
        private String Alignment, FontSize;
        private boolean isFmtMicrometer;
        setColumnCellBackgroundColor(String Alignment, String FontSize, boolean isFmtMicrometer){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
            this.isFmtMicrometer = isFmtMicrometer;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty){
                setText(isFmtMicrometer ? ToolKit.fmtMicrometer(item) : item);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                String searchText = searchText_TextField.getText();
                if(!searchText.equals("") && isHighlightTableView){
                    IAECrawlerData_Bean IAECrawlerData_Bean = ComponentToolKit.getIAECrawlerDataTableViewSelectItem(nowTableView, getIndex());
                    if(searchInvoiceNumber_RadioButton.isSelected()){
                        if(IAECrawlerData_Bean.getInvoiceNumber() != null && IAECrawlerData_Bean.getInvoiceNumber().contains(searchText))
                            getTableRow().setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-background-color: #C79F9F");
                        else
                            getTableRow().setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                    }else if(searchPaymentAmount_RadioButton.isSelected()){
                        if(IAECrawlerData_Bean.getPayAmount() != null && IAECrawlerData_Bean.getPayAmount().equals(searchText))
                            getTableRow().setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-background-color: #C79F9F");
                        else
                            getTableRow().setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                    }
                }else
                    getTableRow().setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
            }else{
                setText(null);
                getTableRow().setStyle(null);
            }
        }
    }
    private void setColumnCellValueAndTextFill_isReview(TableColumn<IAECrawlerData_Bean,String> TableColumn, String ColumnValue){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, "CENTER-LEFT", "16",null);
        TableColumn.setCellFactory(column -> new setIsReviewTextFill());
    }
    private class setIsReviewTextFill extends TableCell<IAECrawlerData_Bean, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                setText(item);
                IAECrawlerData_Bean IAECrawlerData_Bean = getTableView().getItems().get(getIndex());
                if(IAECrawlerData_Bean.getIAECrawlerReviewStatus() == IAECrawlerReviewStatus.已查核)
                    setStyle("-fx-font-size:16px;-fx-alignment: CENTER-LEFT;-fx-text-fill:red");
                else
                    setStyle("-fx-font-size:16px;-fx-alignment: CENTER-LEFT;-fx-text-fill:black");
            }
        }
    }
    private void setColumnCellValueAndCheckBox_IAECrawlerData(TableColumn<IAECrawlerData_Bean, Boolean> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<IAECrawlerData_Bean, Boolean> {
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
                IAECrawlerData_Bean thisIAECrawlerData_Bean = ComponentToolKit.getIAECrawlerDataTableViewSelectItem(nowTableView, getIndex());
                if(thisIAECrawlerData_Bean.getIAECrawlerStatus() == IAECrawlerStatus.遺失)
                    return;
                setGraphic(CheckBox);
                SearchOrder_Bean SearchOrder_Bean = IAECrawlerDataAndOrderMap.get(thisIAECrawlerData_Bean);
                if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已查核發票 && SearchOrder_Bean != null) {
                    ComponentToolKit.setCheckBoxDisable(CheckBox, SearchOrder_Bean.isCheckout());
                    if(SearchOrder_Bean.isCheckout()) {
                        setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment + ";-fx-background-color:yellow");
                    }else{
                        setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                    }
                }else{
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                }
                CheckBox.setSelected(thisIAECrawlerData_Bean.isCheckBoxSelect());
                CheckBox.setOnAction(ActionEvent -> {
                    if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.未查核發票)
                        setNoneReviewOnAction(CheckBox.isSelected(),thisIAECrawlerData_Bean);
                    if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已查核發票)
                        setAlreadyReviewOnAction(CheckBox.isSelected(),thisIAECrawlerData_Bean);
                    else if(PaymentCompareTabName == PayableReceivable_Enum.PaymentCompareTabName.已忽略發票)
                        setAlreadyIgnoreOnAction(CheckBox.isSelected(),thisIAECrawlerData_Bean);
                });
            }else{
                setGraphic(null);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
            }
        }
        private void setNoneReviewOnAction(boolean isCheckBoxSelect,IAECrawlerData_Bean thisIAECrawlerData_Bean){
            if(isCheckBoxSelect){
                ComponentToolKit.setButtonDisable(ignorePaymentButton,false);
                thisIAECrawlerData_Bean.setCheckBoxSelect(true);
            }else{
                thisIAECrawlerData_Bean.setCheckBoxSelect(false);
                ComponentToolKit.setButtonDisable(ignorePaymentButton,true);
                ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView);
                for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                    if(IAECrawlerData_Bean.isCheckBoxSelect()) {
                        ComponentToolKit.setButtonDisable(ignorePaymentButton,false);
                        break;
                    }
                }
            }
        }
        private void setAlreadyReviewOnAction(boolean isCheckBoxSelect,IAECrawlerData_Bean thisIAECrawlerData_Bean){
            if(isCheckBoxSelect){
                SearchOrder_Bean SearchOrder_Bean = IAECrawlerDataAndOrderMap.get(thisIAECrawlerData_Bean);
                if(!SearchOrder_Bean.isCheckout())
                    ComponentToolKit.setButtonDisable(establishReceivableButton,false);
                ComponentToolKit.setButtonDisable(deleteBindingButton,false);
                thisIAECrawlerData_Bean.setCheckBoxSelect(true);
            }else{
                thisIAECrawlerData_Bean.setCheckBoxSelect(false);
                ComponentToolKit.setButtonDisable(establishReceivableButton,true);
                ComponentToolKit.setButtonDisable(deleteBindingButton,true);
                ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView);
                for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                    if(IAECrawlerData_Bean.isCheckBoxSelect()) {
                        ComponentToolKit.setButtonDisable(deleteBindingButton,false);
                        break;
                    }
                }
                for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                    SearchOrder_Bean SearchOrder_Bean = IAECrawlerDataAndOrderMap.get(IAECrawlerData_Bean);
                    if(IAECrawlerData_Bean.isCheckBoxSelect() && !SearchOrder_Bean.isCheckout()) {
                        ComponentToolKit.setButtonDisable(establishReceivableButton, false);
                        break;
                    }
                }
            }
        }
        private void setAlreadyIgnoreOnAction(boolean isCheckBoxSelect,IAECrawlerData_Bean thisIAECrawlerData_Bean){
            if(isCheckBoxSelect){
                ComponentToolKit.setButtonDisable(deleteIgnoreButton,false);
                thisIAECrawlerData_Bean.setCheckBoxSelect(true);
            }else{
                thisIAECrawlerData_Bean.setCheckBoxSelect(false);
                ComponentToolKit.setButtonDisable(deleteIgnoreButton,true);
                ObservableList<IAECrawlerData_Bean> IAECrawlerDataList = ComponentToolKit.getIAECrawlerDataTableViewItemList(nowTableView);
                for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                    if(IAECrawlerData_Bean.isCheckBoxSelect()) {
                        ComponentToolKit.setButtonDisable(deleteIgnoreButton,false);
                        break;
                    }
                }
            }
        }
    }
    private void setColumnCellValueAndCheckBox_ManufacturerContactDetail(TableColumn<IAECrawler_ManufacturerContactDetail_Bean, CheckBox> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox_ManufacturerContactDetail(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox_ManufacturerContactDetail extends TableCell<IAECrawler_ManufacturerContactDetail_Bean, CheckBox> {
        String Alignment, FontSize;
        setColumnCellValueAndCheckBox_ManufacturerContactDetail(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(CheckBox CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                IAECrawler_ManufacturerContactDetail_Bean thisManufacturerContactDetail_Bean = getTableView().getItems().get(getIndex());
                CheckBox CheckBox = thisManufacturerContactDetail_Bean.getSelectCheckBox();
                setGraphic(CheckBox);
                ComponentToolKit.setCheckBoxSelect(CheckBox,thisManufacturerContactDetail_Bean.isCheckBoxSelect());
                ComponentToolKit.setCheckBoxDisable(CheckBox,thisManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.全部);
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
                CheckBox.setOnAction(ActionEvent -> {
                    thisManufacturerContactDetail_Bean.setCheckBoxSelect(CheckBox.isSelected());
                    boolean isAllProductSelect = true;
                    ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailList = ComponentToolKit.getIAECrawlerContactDetailTableViewItemList(manufacturerContactDetailTableView);
                    for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : manufacturerContactDetailList){
                        boolean isCheckBoxSelect = IAECrawler_ManufacturerContactDetail_Bean.isCheckBoxSelect();
                        if(IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() != CheckoutStatus_ManufacturerContactDetail.全部 && !isCheckBoxSelect) {
                            isAllProductSelect = false;
                            break;
                        }
                    }
                    selectAllCheckBox_manufacturerContactDetail.setSelected(isAllProductSelect);
                });
            }else   setGraphic(null);
        }
    }
    private void setRepeatSchoolAlreadyPaymentColumnTextFill(TableColumn<IAECrawler_ManufacturerContactDetail_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new repeatSchoolAlreadyPaymentColumnTextFill(Alignment, FontSize));
    }
    private class repeatSchoolAlreadyPaymentColumnTextFill extends TableCell<IAECrawler_ManufacturerContactDetail_Bean, Integer> {
        String Alignment, FontSize;
        repeatSchoolAlreadyPaymentColumnTextFill(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(Integer payment_id, boolean empty) {
            super.updateItem(payment_id, empty);
            if (payment_id == null || empty) {
                setText(null);
                setStyle("-fx-background-color:white");
            } else {
                setText("");
                HashMap<Integer, Boolean> repeatMap = new HashMap<>();
                LinkedHashMap<Integer, String> colorMap = new LinkedHashMap<>();
                ObservableList<IAECrawler_ManufacturerContactDetail_Bean> iAECrawler_ManufacturerContactDetailList = ComponentToolKit.getIAECrawlerContactDetailTableViewItemList(manufacturerContactDetailTableView);
                for(IAECrawler_ManufacturerContactDetail_Bean iaeCrawler_manufacturerContactDetail_bean : iAECrawler_ManufacturerContactDetailList){
                    if(iaeCrawler_manufacturerContactDetail_bean.getPayment_id() != null){
                        if(!repeatMap.containsKey(iaeCrawler_manufacturerContactDetail_bean.getPayment_id())){
                            repeatMap.put(iaeCrawler_manufacturerContactDetail_bean.getPayment_id(), true);
                        }else{
                            if(!colorMap.containsKey(iaeCrawler_manufacturerContactDetail_bean.getPayment_id())){
                                colorMap.put(iaeCrawler_manufacturerContactDetail_bean.getPayment_id(), ColorCycle.values()[colorMap.size()%ColorCycle.values().length].getRGB());
                            }
                        }
                    }
                }
                if(colorMap.containsKey(payment_id)){
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-background-color:" + colorMap.get(payment_id));
                }
            }
        }
    }
    private void setColumnCellStringValueAndTextFill(TableColumn<IAECrawler_ManufacturerContactDetail_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new columnCellStringValueAndTextFill(Alignment, FontSize));
    }
    private class columnCellStringValueAndTextFill extends TableCell<IAECrawler_ManufacturerContactDetail_Bean, String> {
        String Alignment, FontSize;
        columnCellStringValueAndTextFill(String Alignment, String FontSize){
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
                IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean = getTableView().getItems().get(getIndex());
                CheckoutStatus_ManufacturerContactDetail checkStatus = IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail();
                if(checkStatus == CheckoutStatus_ManufacturerContactDetail.全部)
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:green");
                else if(checkStatus == CheckoutStatus_ManufacturerContactDetail.已開發票)
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:#4053ec");
                else if(checkStatus == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額)
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:red");
                else
                    setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment +";-fx-text-fill:black");
            }
        }
    }
    private void setColumnCellValueAndButton(TableColumn<IAECrawler_ManufacturerContactDetail_Bean, String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn, ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndButton(Alignment, FontSize));
    }
    private class setColumnCellValueAndButton extends TableCell<IAECrawler_ManufacturerContactDetail_Bean, String> {
        String Alignment, FontSize;
        setColumnCellValueAndButton(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String CheckBoxSelect, boolean empty) {
            super.updateItem(CheckBoxSelect, empty);
            if(!empty){
                IAECrawler_ManufacturerContactDetail_Bean selectIAECrawler_ManufacturerContactDetail_Bean = getTableView().getItems().get(getIndex());
                ManufacturerContactDetailSource manufacturerContactDetailSource = selectIAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource();
                if(manufacturerContactDetailSource == ManufacturerContactDetailSource.廠商進貨){
                    MenuButton deleteCheckoutMenuButton = setDeleteMenuButton_ManufacturerContactDetail(selectIAECrawler_ManufacturerContactDetail_Bean);
                    setGraphic(deleteCheckoutMenuButton);
                }else{
                    Button deleteCheckoutButton = setDeleteButton_ManufacturerContactDetail(selectIAECrawler_ManufacturerContactDetail_Bean);
                    setGraphic(deleteCheckoutButton);
                }
                setStyle("-fx-font-size:" + FontSize + "px;-fx-alignment: " + Alignment);
            }else   setGraphic(null);
        }
    }
    private Button setDeleteButton_ManufacturerContactDetail(IAECrawler_ManufacturerContactDetail_Bean selectIAECrawler_ManufacturerContactDetail_Bean){
        CheckoutStatus_ManufacturerContactDetail checkoutStatus_ManufacturerContactDetail = selectIAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail();
        Button deleteCheckoutButton = ComponentToolKit.setButton("取消",80,25,14);
        selectIAECrawler_ManufacturerContactDetail_Bean.setDeleteCheckoutButton(deleteCheckoutButton);
        ComponentToolKit.setButtonDisable(deleteCheckoutButton, checkoutStatus_ManufacturerContactDetail == null);
        deleteCheckoutButton.setOnAction(ActionEvent -> {
            if(DialogUI.ConfirmDialog("※ 是否確定取消所選帳款 ?",true,false,0,0)){
                if(ManagePayableReceivable_Model.deleteManufacturerContactDetailIsCheckout(selectIAECrawler_ManufacturerContactDetail_Bean,CheckoutStatus_ManufacturerContactDetail.全部)) {
                    selectIAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(null);
                    ComponentToolKit.setCheckBoxSelect(selectIAECrawler_ManufacturerContactDetail_Bean.getSelectCheckBox(),false);
                    ComponentToolKit.setCheckBoxDisable(selectIAECrawler_ManufacturerContactDetail_Bean.getSelectCheckBox(),false);
                    ComponentToolKit.setButtonDisable(selectIAECrawler_ManufacturerContactDetail_Bean.getDeleteCheckoutButton(),true);
                    DialogUI.MessageDialog("※ 取消成功");
                }else
                    DialogUI.MessageDialog("※ 取消失敗");
            }
        });
        return deleteCheckoutButton;
    }
    private MenuButton setDeleteMenuButton_ManufacturerContactDetail(IAECrawler_ManufacturerContactDetail_Bean selectIAECrawler_ManufacturerContactDetail_Bean){
        CheckoutStatus_ManufacturerContactDetail checkoutStatus_ManufacturerContactDetail = selectIAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail();
        MenuButton deleteCheckoutMenuButton = ComponentToolKit.setMenuButton("取消",80,25,14);
        HashMap<CheckoutStatus_ManufacturerContactDetail,MenuItem> checkStatusMenuItemMap = new HashMap<>();
        for(CheckoutStatus_ManufacturerContactDetail checkoutStatus_MenuItem : CheckoutStatus_ManufacturerContactDetail.values()){
            MenuItem MenuItem;
            if(checkoutStatus_MenuItem == CheckoutStatus_ManufacturerContactDetail.已開發票) {
                MenuItem = ComponentToolKit.setMenuItem("已開發票", null);
                ComponentToolKit.setMenuItemDisable(MenuItem,checkoutStatus_ManufacturerContactDetail == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額);
            }else if(checkoutStatus_MenuItem == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額) {
                MenuItem = ComponentToolKit.setMenuItem("已結貨款", null);
                ComponentToolKit.setMenuItemDisable(MenuItem, checkoutStatus_ManufacturerContactDetail == CheckoutStatus_ManufacturerContactDetail.已開發票);
            }else {
                MenuItem = ComponentToolKit.setMenuItem("全部", null);
                ComponentToolKit.setMenuItemDisable(MenuItem, checkoutStatus_ManufacturerContactDetail == CheckoutStatus_ManufacturerContactDetail.已開發票 || checkoutStatus_ManufacturerContactDetail == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額);
            }
            checkStatusMenuItemMap.put(checkoutStatus_MenuItem,MenuItem);
            MenuItem.setOnAction(ActionEvent -> {
                if(DialogUI.ConfirmDialog("※ 是否確定取消所選帳款 ?",true,false,0,0)){
                    if(ManagePayableReceivable_Model.deleteManufacturerContactDetailIsCheckout(selectIAECrawler_ManufacturerContactDetail_Bean, checkoutStatus_MenuItem)) {
                        if(checkoutStatus_MenuItem == CheckoutStatus_ManufacturerContactDetail.全部) {
                            selectIAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(null);
                        }else{
                            ComponentToolKit.setMenuItemDisable(MenuItem,true);
                            if(checkoutStatus_MenuItem == CheckoutStatus_ManufacturerContactDetail.已開發票) {
                                selectIAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(
                                    selectIAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.全部 ?
                                    CheckoutStatus_ManufacturerContactDetail.已給付貨款金額 : null);
                                ComponentToolKit.setMenuItemDisable(checkStatusMenuItemMap.get(CheckoutStatus_ManufacturerContactDetail.全部),true);
                            }else if(checkoutStatus_MenuItem == CheckoutStatus_ManufacturerContactDetail.已給付貨款金額) {
                                selectIAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(
                                    selectIAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == CheckoutStatus_ManufacturerContactDetail.全部 ?
                                    CheckoutStatus_ManufacturerContactDetail.已開發票 : null);
                                ComponentToolKit.setMenuItemDisable(checkStatusMenuItemMap.get(CheckoutStatus_ManufacturerContactDetail.全部),true);
                            }
                        }
                        ComponentToolKit.setCheckBoxSelect(selectIAECrawler_ManufacturerContactDetail_Bean.getSelectCheckBox(),false);
                        ComponentToolKit.setCheckBoxDisable(selectIAECrawler_ManufacturerContactDetail_Bean.getSelectCheckBox(),false);
                        ComponentToolKit.setMenuButtonDisable(selectIAECrawler_ManufacturerContactDetail_Bean.getDeleteCheckoutMenuButton(),selectIAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == null);
                        DialogUI.MessageDialog("※ 取消成功");
                }else
                    DialogUI.MessageDialog("※ 取消失敗");

                }
            });
            deleteCheckoutMenuButton.getItems().add(MenuItem);
        }
        selectIAECrawler_ManufacturerContactDetail_Bean.setDeleteCheckoutMenuButton(deleteCheckoutMenuButton);
        ComponentToolKit.setMenuButtonDisable(deleteCheckoutMenuButton, checkoutStatus_ManufacturerContactDetail == null);
        return deleteCheckoutMenuButton;
    }
    private void setIAECrawlerDataPriceColumnMicrometerFormat(TableColumn<IAECrawlerData_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setIAECrawlerDataPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setIAECrawlerDataPriceColumnMicrometerFormat extends TableCell<IAECrawlerData_Bean, String> {
        String Alignment, FontSize;
        setIAECrawlerDataPriceColumnMicrometerFormat(String Alignment, String FontSize){
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
    private void setSearchOrderPriceColumnMicrometerFormat(TableColumn<SearchOrder_Bean,String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setSearchOrderPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setSearchOrderPriceColumnMicrometerFormat extends TableCell<SearchOrder_Bean, String> {
        String Alignment, FontSize;
        setSearchOrderPriceColumnMicrometerFormat(String Alignment, String FontSize){
            this.Alignment = Alignment;
            this.FontSize = FontSize;
        }
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty) {
                setText(null);
            } else {
                IAECrawlerData_Bean iaeCrawlerData_bean = ComponentToolKit.getIAECrawlerDataTableViewSelectItem(compareTableView);
                SearchOrder_Bean searchOrder_bean = ComponentToolKit.getSearchOrderTableViewSelectItem(compareOrderTableView, getIndex());
                boolean isProductGroupPrice = false;
                if(iaeCrawlerData_bean.getInvoiceNumber() != null){
                    if(ToolKit.RoundingInteger(iaeCrawlerData_bean.getInvoiceAmount()).equals(searchOrder_bean.getProductGroupTotalPriceIncludeTax())){
                        item = ToolKit.RoundingString(searchOrder_bean.getProductGroupTotalPriceIncludeTax());
                        isProductGroupPrice = true;
                    }
                }else if(iaeCrawlerData_bean.getPayAmount() != null){
                    if(ToolKit.RoundingInteger(iaeCrawlerData_bean.getPayAmount()).equals(searchOrder_bean.getProductGroupTotalPriceIncludeTax())){
                        item = ToolKit.RoundingString(searchOrder_bean.getProductGroupTotalPriceIncludeTax());
                        isProductGroupPrice = true;
                    }
                }
                item = ToolKit.fmtMicrometer(item);
                setText(isProductGroupPrice ? item + "(群)" : item);
            }
        }
    }
    private void setPurchaseNotePriceColumnMicrometerFormat(TableColumn<IAECrawler_ExportPurchaseNote_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setPurchaseNotePriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setPurchaseNotePriceColumnMicrometerFormat extends TableCell<IAECrawler_ExportPurchaseNote_Bean, Integer> {
        String Alignment, FontSize;
        setPurchaseNotePriceColumnMicrometerFormat(String Alignment, String FontSize) {
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
    private void setManufacturerContactDetailPriceColumnMicrometerFormat(TableColumn<IAECrawler_ManufacturerContactDetail_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setManufacturerContactDetailPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setManufacturerContactDetailPriceColumnMicrometerFormat extends TableCell<IAECrawler_ManufacturerContactDetail_Bean, Integer> {
        String Alignment, FontSize;
        setManufacturerContactDetailPriceColumnMicrometerFormat(String Alignment, String FontSize) {
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
    private void setColumnCellValueAndButtonBySearchOrder(TableColumn<SearchOrder_Bean, String> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndButtonBySearchOrder());
    }
    private class setColumnCellValueAndButtonBySearchOrder extends TableCell<SearchOrder_Bean, String> {
        setColumnCellValueAndButtonBySearchOrder() {
        }
        @Override
        protected void updateItem(String WaitConfirmProductInfo_Bean, boolean empty) {
            super.updateItem(WaitConfirmProductInfo_Bean, empty);
            if (empty)
                setGraphic(null);
            else{
                SearchOrder_Bean thisSearchOrder_Bean = compareOrderTableView.getItems().get(getIndex());
                Button Button = thisSearchOrder_Bean.getButton();
                if(thisSearchOrder_Bean.getBindingIAECrawlerPayment_id() != null){
                    Button.setText("解除綁定(已綁定)");
                    Button.setStyle("-fx-text-fill:red");
                    Button.setPrefWidth(250);
                }else{
                    Button.setText("綁定");
                    Button.setPrefWidth(80);
                }

                setGraphic(Button);
                Button.setOnAction(e -> {
                    IAECrawlerData_Bean iAECrawlerData_Bean = compareTableView.getSelectionModel().getSelectedItem();
                    boolean isAllowManyOrder = (iAECrawlerData_Bean.getInvoiceNumber() == null);
                    if(thisSearchOrder_Bean.getBindingIAECrawlerPayment_id() != null){
                        String inputText = DialogUI.TextInputDialog("解除綁定","※ 此貨單已被綁定，請輸入「OK」來解除綁定 !",null);
                        if(inputText != null){
                            if(inputText.equals("OK")){
                                if(ManagePayableReceivable_Model.deleteIAECrawlerDataBindingOrder(thisSearchOrder_Bean.getBindingIAECrawlerPayment_id(),thisSearchOrder_Bean)){
                                    DialogUI.MessageDialog("※ 解除成功");
                                    Button.setText("綁定");
                                    Button.setStyle("");
                                    Button.setPrefWidth(80);
                                    thisSearchOrder_Bean.setBindingIAECrawlerPayment_id(null);
                                }else
                                    DialogUI.MessageDialog("※ 解除失敗");
                            }else
                                DialogUI.MessageDialog("※ 請輸入「OK」");
                        }
                    }else{
                        ObservableList<SearchOrder_Bean> allOrderList = ComponentToolKit.getSearchOrderTableViewItemList(compareOrderTableView);
                        for(SearchOrder_Bean SearchOrder_Bean : allOrderList){
                            try {
                                if(SearchOrder_Bean.getOrderNumber().equals(thisSearchOrder_Bean.getOrderNumber())) {
                                    System.out.println("for id:" + SearchOrder_Bean.getId() + "  button id:" + thisSearchOrder_Bean.getId());
                                    boolean doingBinding = false;
                                    IAECrawlerData_Bean IAECrawlerData_Bean = compareTableView.getSelectionModel().getSelectedItem();
                                    if(ManagePayableReceivable_Model.getOrderBindingPayment_id(thisSearchOrder_Bean) != null)
                                        DialogUI.MessageDialog("※ 此貨單已被綁定!");
                                    else if(IAECrawlerData_Bean.getInvoiceNumber() == null && thisSearchOrder_Bean.getInvoiceNumber() == null && IAECrawlerData_Bean.getInvoiceAmount().equals("0")){
                                        doingBinding = DialogUI.ConfirmDialog("※ 不存在出納發票與貨單發票，是否仍要綁定 ?", true, true, 2, 14);
                                    }else if(thisSearchOrder_Bean.getInvoiceNumber() == null)
                                        doingBinding = fillBackOrderNoneInvoice(thisSearchOrder_Bean,IAECrawlerData_Bean);
                                    else if(IAECrawlerData_Bean.getInvoiceNumber() != null && !thisSearchOrder_Bean.getInvoiceNumber().equals(IAECrawlerData_Bean.getInvoiceNumber())) {
                                        doingBinding = DialogUI.ConfirmDialog("貨單發票號碼不符合! 仍要綁定 ?",true,false,0,0) &&
                                                DialogUI.requestAuthorization(ToolKit_Enum.Authority.管理者, ToolKit_Enum.AuthorizedLocation.綁定發票不符合的出納資料);
                                    }else if(thisSearchOrder_Bean.getInvoice_manufacturerNickName_id() == 0)
                                        doingBinding = fillBackOrderInvoiceNoneManufacturer(thisSearchOrder_Bean,IAECrawlerData_Bean);
                                    else if(thisSearchOrder_Bean.getInvoice_manufacturerNickName_id() != IAECrawlerData_Bean.getInvoice_manufacturerNickName_id())
                                        doingBinding = changeOrderInvoiceManufacturer(thisSearchOrder_Bean,IAECrawlerData_Bean);
                                    else
                                        doingBinding = true;
                                    if(doingBinding){
                                        if(ManagePayableReceivable_Model.setIAECrawlerInvoiceBindingOrder(isAllowManyOrder, IAECrawlerData_Bean,thisSearchOrder_Bean)) {
                                            thisSearchOrder_Bean.setBindingIAECrawlerPayment_id(IAECrawlerData_Bean.getId());
                                            SearchOrder_Bean.getButton().setText("解除綁定(已綁定)");
                                            SearchOrder_Bean.getButton().setStyle("-fx-text-fill:red");
                                            SearchOrder_Bean.getButton().setPrefWidth(250);
                                            DialogUI.MessageDialog("※ 綁定成功");
                                        }else
                                            DialogUI.MessageDialog("※ 綁定失敗");
                                    }
                                }
                            }catch (Exception Ex){
                                ERPApplication.Logger.catching(Ex);
                                DialogUI.ExceptionDialog(Ex);
                            }
                        }
                    }
                });
            }
            setText(null);
        }
    }
    private boolean fillBackOrderNoneInvoice(SearchOrder_Bean thisSearchOrder_Bean, IAECrawlerData_Bean IAECrawlerData_Bean){
        boolean doingBinding = false;
        if (DialogUI.ConfirmDialog("※ 此貨單發票不存在，是否回填出納發票 ?", true, true, 5, 10)) {
            if(IAECrawlerData_Bean.getInvoiceNumber() == null || IAECrawlerData_Bean.getInvoiceDate() == null)
                DialogUI.MessageDialog("※ 出納發票不存在!");
            else{
                Invoice_Bean Invoice_Bean = new Invoice_Bean();
                Invoice_Bean.setInvoiceType(Invoice_Enum.InvoiceType.二聯式);
                Invoice_Bean.setInvoiceDate(IAECrawlerData_Bean.getInvoiceDate().toString());
                Invoice_Bean.setInvoiceYear(Integer.parseInt(Invoice_Bean.getInvoiceDate().substring(0,Invoice_Bean.getInvoiceDate().indexOf("-")))-1911);
                Invoice_Bean.setInvoiceNumber(IAECrawlerData_Bean.getInvoiceNumber());
                if(!ManagePayableReceivable_Model.isInvoiceExist(Invoice_Bean)){
                    if(ManagePayableReceivable_Model.fillBackOrderInvoiceInfo(thisSearchOrder_Bean,IAECrawlerData_Bean)){
                        thisSearchOrder_Bean.setInvoice_manufacturerNickName_id(IAECrawlerData_Bean.getInvoice_manufacturerNickName_id());
                        thisSearchOrder_Bean.setInvoiceManufacturerNickName(IAECrawlerData_Bean.getInvoiceManufacturerNickName());
                        thisSearchOrder_Bean.setInvoiceNumber(IAECrawlerData_Bean.getInvoiceNumber());
                        doingBinding = true;
                    }else
                        DialogUI.MessageDialog("※ 貨單發票回填失敗!");
                }else
                    DialogUI.MessageDialog("※ 發票建立失敗，此發票已存在!");
            }
        }
        return doingBinding;
    }
    private boolean fillBackOrderInvoiceNoneManufacturer(SearchOrder_Bean thisSearchOrder_Bean,IAECrawlerData_Bean IAECrawlerData_Bean){
        boolean doingBinding = false;
        if (DialogUI.ConfirmDialog("※ 此貨單發票不存在廠商，是否回填出納廠商 ?", true, true, 5, 12)) {
            if(ManagePayableReceivable_Model.fillBackOrderInvoiceInfo(thisSearchOrder_Bean,IAECrawlerData_Bean)){
                thisSearchOrder_Bean.setInvoice_manufacturerNickName_id(IAECrawlerData_Bean.getInvoice_manufacturerNickName_id());
                thisSearchOrder_Bean.setInvoiceManufacturerNickName(IAECrawlerData_Bean.getInvoiceManufacturerNickName());
                doingBinding = true;
            }else
                DialogUI.MessageDialog("※ 回填失敗!");
        }
        return doingBinding;
    }
    private boolean changeOrderInvoiceManufacturer(SearchOrder_Bean thisSearchOrder_Bean,IAECrawlerData_Bean IAECrawlerData_Bean){
        boolean doingBinding = false;
        if(DialogUI.ConfirmDialog("※ 廠商簡稱「" + thisSearchOrder_Bean.getInvoiceManufacturerNickName() + "」不符合「" + IAECrawlerData_Bean.getInvoiceManufacturerNickName()+ "」 是否變更 ?",true,false,0,0)){
            if(ManagePayableReceivable_Model.updateOrderInvoiceManufacturerNickNameID(thisSearchOrder_Bean,IAECrawlerData_Bean.getInvoice_manufacturerNickName_id())) {
                thisSearchOrder_Bean.setInvoice_manufacturerNickName_id(IAECrawlerData_Bean.getInvoice_manufacturerNickName_id());
                thisSearchOrder_Bean.setInvoiceManufacturerNickName(IAECrawlerData_Bean.getInvoiceManufacturerNickName());
                doingBinding = true;
            }else
                DialogUI.MessageDialog("※ 變更失敗!");
        }
        return doingBinding;
    }
}