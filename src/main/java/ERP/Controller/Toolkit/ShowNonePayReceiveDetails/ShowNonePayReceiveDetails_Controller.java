package ERP.Controller.Toolkit.ShowNonePayReceiveDetails;

import ERP.Bean.ManagePayableReceivable.PayableReceivable_Bean;
import ERP.Bean.Order.EstablishOrder_NewStage_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.Order.SearchNonePayReceive.ConditionalPayReceiveSearch_Bean;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PayableReceivableStatus;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_ExportPayReceiveDetails;
import ERP.Model.Order.SearchNonePayReceive.ExportNonePayReceiveDocument;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.*;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.HashMap;

/** [Controller] Show none pay or receive details */
public class ShowNonePayReceiveDetails_Controller {

    @FXML private Label PayableReceivableTitle_Label, ObjectID_Label, ObjectIDText, DateRangeText, TabulationDateText, TotalPriceText;
    @FXML private Button Establish_Button;
    @FXML private SplitMenuButton exportPayReceiveDetails_splitMenuButton;
    @FXML private TableColumn<SearchOrder_Bean, String> OrderNumber_TableColumn, OrderDate_TableColumn, OrderSource_TableColumn,TotalPriceIncludeTax_TableColumn;
    @FXML private TableColumn<SearchOrder_Bean, Integer> PayableReceivableDiscount_TableColumn, TotalPriceNoneTax_TableColumn, Tax_TableColumn, Discount_TableColumn, installment_tableColumn;
    @FXML private TableView<SearchOrder_Bean> TableView;

    private ToolKit toolkit;
    private ComponentToolKit componentToolkit;
    private KeyPressed keyPressed;
    private CallFXML callFXML;
    private CallConfig callConfig;
    private SystemSetting_Model systemSetting_model;

    private SearchNonePayReceive_Bean searchNonePayReceive_bean;
    private OrderObject orderObject;

    private ConditionalPayReceiveSearch_Bean conditionalPayReceiveSearch_bean;
    private Stage MainStage;

    private HashMap<Order_ExportPayReceiveDetails, RadioButton> exportPayReceiveDetailsRadioButtonMap = new HashMap<>();
    private JSONObject systemDefaultConfigJson;
    public ShowNonePayReceiveDetails_Controller(){
        toolkit = ERPApplication.ToolKit;
        componentToolkit = toolkit.ComponentToolKit;
        keyPressed = toolkit.KeyPressed;
        callFXML = toolkit.CallFXML;
        callConfig = toolkit.CallConfig;

        systemSetting_model = toolkit.ModelToolKit.getSystemSettingModel();
    }
    /** Set object - [Bean] ConditionalPayReceiveSearch_Bean */
    public void setConditionalPayReceiveSearch_bean(ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean){    this.conditionalPayReceiveSearch_bean = ConditionalPayReceiveSearch_Bean; }
    /** Set object - [Bean] SearchNonePayReceive_Bean */
    public void setSearchNonePayReceive_bean(SearchNonePayReceive_Bean SearchNonePayReceive_Bean){  this.searchNonePayReceive_bean = SearchNonePayReceive_Bean;   }
    /** Set object - [Enum] Order_Enum.OrderObject */
    public void setOrderObject(OrderObject OrderObject){    this.orderObject = OrderObject; }
    public void setMainStage(Stage MainStage){
        this.MainStage = MainStage;
    }
    /** Set component of show none pay or receive details */
    public void setComponent(){
        setUI();
        setPayableReceivableInfo();
        initialTableView();

        systemDefaultConfigJson = callConfig.getSystemDefaultConfigJson();
        setDefaultExportPayReceiveDetails();

        ObservableList<SearchOrder_Bean> OrderList = searchNonePayReceive_bean.getOrderList();
        toolkit.sortSearchOrderNumber(OrderList);
        componentToolkit.getNonePayableReceivableDetailsTableViewItemList(TableView).addAll(OrderList);
    }
    private void setUI(){
        PayableReceivableTitle_Label.setText(orderObject.name() + orderObject.getPayableReceivableName() + "明細表");
        ObjectID_Label.setText(orderObject.name() + "編號：");
        Establish_Button.setText(orderObject.getPayableReceivableName() + "帳款建立");
    }

    private void setPayableReceivableInfo(){
        ObjectIDText.setText(searchNonePayReceive_bean.getObjectID());
        DateRangeText.setText(conditionalPayReceiveSearch_bean.getCheckoutByMonthStartDate() + "~" + conditionalPayReceiveSearch_bean.getCheckoutByMonthEndDate());
        TabulationDateText.setText((Integer.parseInt(toolkit.getToday("yyyy")) - 1911) + "/" + toolkit.getToday("MM/dd"));
        TotalPriceText.setText("(" + orderObject.getPayableReceivableName() + "  " + searchNonePayReceive_bean.getActualPayReceivePrice() + ") ");
    }
    private void initialTableView(){
        componentToolkit.setColumnCellValue(OrderNumber_TableColumn,"OrderNumber", "CENTER-LEFT", "16",null);
        componentToolkit.setColumnCellValue(OrderDate_TableColumn,"OrderDate", "CENTER-LEFT", "16",null);
        componentToolkit.setColumnCellValue(OrderSource_TableColumn,"OrderSource", "CENTER-LEFT", "16",null);
        componentToolkit.setColumnCellValue(PayableReceivableDiscount_TableColumn,"PayableReceivableDiscount", "CENTER-LEFT", "16",null);
        setIntegerPriceColumnMicrometerFormat(TotalPriceNoneTax_TableColumn,"TotalPriceNoneTax", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(Tax_TableColumn,"Tax", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(Discount_TableColumn,"Discount", "CENTER-LEFT", "16");
        componentToolkit.setColumnCellValue(installment_tableColumn,"installment", "CENTER-LEFT", "16",null);
        TotalPriceIncludeTax_TableColumn.setText(orderObject.getPayableReceivableName());
        setStringPriceColumnMicrometerFormat(TotalPriceIncludeTax_TableColumn, "CENTER-LEFT", "16");
        componentToolkit.setTableColumnDoubleSort(TotalPriceIncludeTax_TableColumn);
    }
    private void setDefaultExportPayReceiveDetails(){
        if(exportPayReceiveDetails_splitMenuButton != null)
            exportPayReceiveDetails_splitMenuButton.getItems().clear();

        Integer defaultConfig = systemDefaultConfigJson.getInteger(Order_ExportPayReceiveDetails.class.getSimpleName());
        if(defaultConfig == null)
            defaultConfig = Order_ExportPayReceiveDetails.export.ordinal();
        ToggleGroup toggleGroup = new ToggleGroup();
        for(Order_ExportPayReceiveDetails order_exportPayReceiveDetails : Order_ExportPayReceiveDetails.values()){
            RadioButton radioButton = componentToolkit.setRadioButton(order_exportPayReceiveDetails.getDescribe(), -1,-1,16);
            radioButton.setToggleGroup(toggleGroup);
            if(Order_ExportPayReceiveDetails.values()[defaultConfig] == order_exportPayReceiveDetails){
                componentToolkit.setRadioButtonSelect(radioButton,true);
                exportPayReceiveDetails_splitMenuButton.setText(order_exportPayReceiveDetails.getDescribe());
            }else {
                componentToolkit.setRadioButtonSelect(radioButton, false);
            }
            exportPayReceiveDetailsRadioButtonMap.put(order_exportPayReceiveDetails,radioButton);
            radioButton.setOnAction(ActionEvent -> {
                if(order_exportPayReceiveDetails == Order_ExportPayReceiveDetails.export){
                    exportNonePayReceiveDocument(order_exportPayReceiveDetails);
                }else if(order_exportPayReceiveDetails == Order_ExportPayReceiveDetails.print){
                    exportNonePayReceiveDocument(order_exportPayReceiveDetails);
                }
            });

            CustomMenuItem CustomMenuItem = new CustomMenuItem(radioButton);
            CustomMenuItem.setHideOnClick(true);
            exportPayReceiveDetails_splitMenuButton.getItems().add(CustomMenuItem);
        }
    }
    private void returnExportPayReceiveDetailsToDefaultConfig(Order_ExportPayReceiveDetails order_exportPayReceiveDetails){
        Integer defaultConfig = systemSetting_model.getSystemDefaultConfig(SystemDefaultConfig_Enum.SystemDefaultName.Order_ExportPayReceiveDetails);
        componentToolkit.setRadioButtonSelect(exportPayReceiveDetailsRadioButtonMap.get(order_exportPayReceiveDetails),false);
        if(defaultConfig != null)
            componentToolkit.setRadioButtonSelect(exportPayReceiveDetailsRadioButtonMap.get(Order_ExportPayReceiveDetails.values()[defaultConfig]),true);
    }
    /** Button Mouse Clicked - 應收應付建立 */
    @FXML protected void EstablishPayableReceivableMouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            callFXML.EstablishPayableReceivable_NewStage(orderObject, PayableReceivableStatus.建立,null,null, setPayableReceivable_Bean(orderObject), conditionalPayReceiveSearch_bean);
        }
    }
    private PayableReceivable_Bean setPayableReceivable_Bean(OrderObject OrderObject){
        PayableReceivable_Bean PayableReceivable_Bean = new PayableReceivable_Bean();
        PayableReceivable_Bean.setOrderDate(toolkit.getToday("yyyy-MM-dd"));
        PayableReceivable_Bean.setOrderObject(OrderObject);
        PayableReceivable_Bean.setObjectID(searchNonePayReceive_bean.getObjectID());
        return PayableReceivable_Bean;
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent) {
        if (keyPressed.isMouseLeftDoubleClicked(MouseEvent)) {
            SearchOrder_Bean SearchOrder_Bean = componentToolkit.getNonePayableReceivableDetailsTableViewSelectItem(TableView);
            if (SearchOrder_Bean != null) {
                Order_Bean Order_Bean = new Order_Bean();
                if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
                    Order_Bean.setNowOrderNumber(SearchOrder_Bean.getQuotationNumber());
                else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 || SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
                    Order_Bean.setNowOrderNumber(SearchOrder_Bean.getOrderNumber());
                Order_Bean.setObjectID(SearchOrder_Bean.getObjectID());
                Order_Bean.setObjectName(SearchOrder_Bean.getObjectName());
                Order_Bean.setWaitingOrderDate(SearchOrder_Bean.getWaitingOrderDate());
                Order_Bean.setWaitingOrderNumber(SearchOrder_Bean.getWaitingOrderNumber());
                Order_Bean.setAlreadyOrderDate(SearchOrder_Bean.getAlreadyOrderDate());
                Order_Bean.setAlreadyOrderNumber(SearchOrder_Bean.getAlreadyOrderNumber());
                Order_Bean.setIsCheckout(SearchOrder_Bean.isCheckout());
                Order_Bean.setExistInstallment(SearchOrder_Bean.getInstallment() != null);

                EstablishOrder_NewStage_Bean establishOrder_NewStage_Bean = new EstablishOrder_NewStage_Bean();
                establishOrder_NewStage_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
                establishOrder_NewStage_Bean.setOrderSource(SearchOrder_Bean.getOrderSource());
                establishOrder_NewStage_Bean.setOrderExist(Order_Enum.OrderExist.已存在);
                establishOrder_NewStage_Bean.setOrder_Bean(Order_Bean);
                establishOrder_NewStage_Bean.setDifferentOrderInfoList(null);
                establishOrder_NewStage_Bean.setSearchOrder_Controller(null);
                establishOrder_NewStage_Bean.setSearchOrderProgress_Controller(null);
                callFXML.EstablishOrder_NewStage(establishOrder_NewStage_Bean);
            }
        }
    }
    /** Button Mouse Clicked - 匯出/列印 */
    @FXML protected void ExportOrPrintMouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)){
            for(Order_ExportPayReceiveDetails exportPayReceiveDetails : exportPayReceiveDetailsRadioButtonMap.keySet()){
                if(exportPayReceiveDetailsRadioButtonMap.get(exportPayReceiveDetails).isSelected()){
                    if(exportPayReceiveDetails == Order_ExportPayReceiveDetails.export)
                        exportNonePayReceiveDocument(exportPayReceiveDetails);
                    else if(exportPayReceiveDetails == Order_ExportPayReceiveDetails.print)
                        exportNonePayReceiveDocument(exportPayReceiveDetails);
                    break;
                }
            }
        }
    }
    private void exportNonePayReceiveDocument(Order_ExportPayReceiveDetails order_exportPayReceiveDetails){
        try {
            systemDefaultConfigJson.put(SystemDefaultConfig_Enum.Order_ExportPayReceiveDetails.class.getSimpleName(), order_exportPayReceiveDetails.ordinal());
            if(callConfig.setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName.Order_ExportPayReceiveDetails, systemDefaultConfigJson)){
                exportPayReceiveDetails_splitMenuButton.setText(order_exportPayReceiveDetails.getDescribe());
                ExportNonePayReceiveDocument exportNonePayReceiveDocument = new ExportNonePayReceiveDocument();
                if(exportNonePayReceiveDocument.exportNonePayReceiveDetails(conditionalPayReceiveSearch_bean, componentToolkit.getSearchOrderTableViewItemList(TableView), searchNonePayReceive_bean, orderObject, order_exportPayReceiveDetails))
                    DialogUI.MessageDialog("※ " + order_exportPayReceiveDetails.getDescribe() + "成功!");
                else
                    DialogUI.MessageDialog("※ " + order_exportPayReceiveDetails.getDescribe()  + "失敗!");
            }else{
                returnExportPayReceiveDetailsToDefaultConfig(order_exportPayReceiveDetails);
                DialogUI.AlarmDialog("※ 【預設值】更新失敗");
            }
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            DialogUI.ExceptionDialog(ex);
        }
    }
    /** Button Mouse Clicked - 開啟資料夾 */
    @FXML protected void OpenExportFolderMouseClicked(MouseEvent MouseEvent){
        if(keyPressed.isMouseLeftClicked(MouseEvent)) {
            try {
                String FolderPath = callConfig.getFile_OutputPath();
                if (FolderPath.equals("")) DialogUI.MessageDialog("※ 未設定路徑!");
                else Desktop.getDesktop().open(new File(FolderPath));
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    private void setStringPriceColumnMicrometerFormat(TableColumn<SearchOrder_Bean, String> TableColumn, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>("TotalPriceIncludeTax"));
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
                setText(toolkit.fmtMicrometer(item));
            }
        }
    }
    private void setIntegerPriceColumnMicrometerFormat(TableColumn<SearchOrder_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setIntegerPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setIntegerPriceColumnMicrometerFormat extends TableCell<SearchOrder_Bean, Integer> {
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
                setText(toolkit.fmtMicrometer(item));
            }
        }
    }
}
