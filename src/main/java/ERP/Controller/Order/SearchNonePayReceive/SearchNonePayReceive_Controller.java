package ERP.Controller.Order.SearchNonePayReceive;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.SearchNonePayReceive.ConditionalPayReceiveSearch_Bean;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum;
import ERP.Model.ManagePayableReceivable.ExportPayableDocument;
import ERP.Model.Order.SearchNonePayReceive.ExportNonePayReceiveDocument;
import ERP.Model.Order.SearchNonePayReceive.SearchNonePayReceive_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.TemplatePath;
import ERP.View.DialogUI;
import ERP.ERPApplication;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** [Controller] Establish none pay or receive */
public class SearchNonePayReceive_Controller {
    @FXML private BorderPane BorderPane;
    @FXML private DatePicker CheckoutByMonthStartDate_DatePicker, CheckoutByMonthEndDate_DatePicker, NoneCheckoutByMonthStartDate_DatePicker, NoneCheckoutByMonthEndDate_DatePicker;
    @FXML private TextField ObjectNameText;
    @FXML private ComboBox<ObjectInfo_Bean> StartObjectID_ComboBox, EndObjectID_ComboBox;
    @FXML private Button exportPayable_Button;
    @FXML private CheckBox SelectAll_CheckBox;
    @FXML private TableColumn<SearchNonePayReceive_Bean,CheckBox> Select_TableColumn;
    @FXML private TableColumn<SearchNonePayReceive_Bean,String> ObjectID_TableColumn, ObjectName_TableColumn, OrderQuantity_TableColumn, NonePayReceiveHistoryDate_TableColumn;
    @FXML private TableColumn<SearchNonePayReceive_Bean,Integer>  OrderPriceAmount_TableColumn, ReturnOrderQuantity_TableColumn, ReturnOrderPriceAmount_TableColumn, Tax_TableColumn, Discount_TableColumn, PayReceivePrice_TableColumn, ActualPayReceivePrice_TableColumn;
    @FXML private TableView<SearchNonePayReceive_Bean> TableView;
    @FXML Label TotalOrderCountTitle_Label, TotalOrderPriceTitle_Label, PayReceiveTotalPriceTitle_Label, ActualPayReceiveTotalPriceTitle_Label;
    @FXML Label TotalOrderCount_Label, TotalOrderPrice_Label, TotalReturnOrderCount_Label, TotalReturnOrderPrice_Label, TotalTax_Label, TotalDiscount_Label, PayReceiveTotalPrice_Label, ActualPayReceiveTotalPrice_Label;

    private ERP.ToolKit.ToolKit ToolKit;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.CallFXML CallFXML;
    private ERP.ToolKit.CallConfig CallConfig;
    private SystemSettingConfig_Bean SystemSettingConfig_Bean;
    private String StartObjectID = "", EndObjectID = "";

    private Stage MainStage;
    private SystemSetting_Model SystemSetting_Model;
    private SearchNonePayReceive_Model SearchNonePayReceive_Model;
    private OrderObject OrderObject;
    private ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean;
    public SearchNonePayReceive_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.CallFXML = ToolKit.CallFXML;
        this.CallConfig = ToolKit.CallConfig;
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
        this.SearchNonePayReceive_Model = ToolKit.ModelToolKit.getSearchNonePayReceiveModel();
    }
    public void setOrderObject(OrderObject OrderObject){    this.OrderObject = OrderObject; }
    public void setMainStage(Stage MainStage){  this.MainStage = MainStage; }
    /** Set component of search none pay or receive */
    public void setComponent(){
        setUI();
        ComponentToolKit.setDatePickerValue(CheckoutByMonthStartDate_DatePicker, ToolKit.getLastMonthSpecificDay(26));
        ComponentToolKit.setDatePickerValue(CheckoutByMonthEndDate_DatePicker, ToolKit.getThisMonthSpecifyDay(25));
        ComponentToolKit.setDatePickerValue(NoneCheckoutByMonthStartDate_DatePicker, ToolKit.getLastMonthSpecificDay(21));
        ComponentToolKit.setDatePickerValue(NoneCheckoutByMonthEndDate_DatePicker, ToolKit.getThisMonthSpecifyDay(20));

        ObservableList<ObjectInfo_Bean> ObjectInfoList = SearchNonePayReceive_Model.getAllObjectInfo(OrderObject);
        StartObjectID_ComboBox.getItems().addAll(ObjectInfoList);
        StartObjectID_ComboBox.getSelectionModel().selectFirst();
        ComponentToolKit.setObjectInfoBeanComboBoxObj(StartObjectID_ComboBox);
        EndObjectID_ComboBox.getItems().addAll(ObjectInfoList);
        EndObjectID_ComboBox.getSelectionModel().selectFirst();
        ComponentToolKit.setObjectInfoBeanComboBoxObj(EndObjectID_ComboBox);

        this.SystemSettingConfig_Bean = SystemSetting_Model.loadAllSystemSettingData();
        initialTableView();
    }
    private void setUI(){
        BorderPane.setStyle("-fx-background-color: " + ToolKit.getSearchNonePayableReceivableBackgroundColor());
        ObjectID_TableColumn.setText(OrderObject.name() + "編號");
        ObjectName_TableColumn.setText(OrderObject.name() + "名稱");
        if(OrderObject == Order_Enum.OrderObject.廠商)    setPayableUI();
        else if(OrderObject == Order_Enum.OrderObject.客戶)   setReceivableUI();
    }
    private void setPayableUI(){
        PayReceivePrice_TableColumn.setText("應付額");
        ActualPayReceivePrice_TableColumn.setText("實應付");
        TotalOrderCountTitle_Label.setText("進貨單數");
        TotalOrderPriceTitle_Label.setText("進貨額");
        PayReceiveTotalPriceTitle_Label.setText("應付額");
        ActualPayReceiveTotalPriceTitle_Label.setText("實應付");
        ComponentToolKit.setButtonVisible(exportPayable_Button,true);
    }
    private void setReceivableUI(){
        PayReceivePrice_TableColumn.setText("應收額");
        ActualPayReceivePrice_TableColumn.setText("實應收");
        TotalOrderCountTitle_Label.setText("出貨單數");
        TotalOrderPriceTitle_Label.setText("出貨額");
        PayReceiveTotalPriceTitle_Label.setText("應收額");
        ActualPayReceiveTotalPriceTitle_Label.setText("實應收");
        ComponentToolKit.setButtonVisible(exportPayable_Button,false);
    }

    private void initialTableView(){
        setColumnCellValueAndCheckBox(Select_TableColumn,"SelectCheckBox", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(Select_TableColumn,"Select", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ObjectID_TableColumn,"ObjectID", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(ObjectName_TableColumn,"ObjectName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(OrderQuantity_TableColumn,"OrderQuantity", "CENTER-LEFT", "16",null);
        setIntegerPriceColumnMicrometerFormat(OrderPriceAmount_TableColumn,"OrderPriceAmount", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(ReturnOrderQuantity_TableColumn,"ReturnOrderQuantity", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(ReturnOrderPriceAmount_TableColumn,"ReturnOrderPriceAmount", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(Tax_TableColumn,"Tax", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(Discount_TableColumn,"Discount", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(PayReceivePrice_TableColumn,"PayReceivePrice", "CENTER-LEFT", "16");
        setIntegerPriceColumnMicrometerFormat(ActualPayReceivePrice_TableColumn,"ActualPayReceivePrice", "CENTER-LEFT", "16");
        ComponentToolKit.setColumnCellValue(NonePayReceiveHistoryDate_TableColumn,"NonePayReceiveHistoryDate", "CENTER-LEFT", "16",null);
    }
    /** TextField Key Released - 對象名稱 */
    @FXML protected void SearchObjectNameKeyReleased(KeyEvent KeyEvent) throws Exception{
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            SearchNonePayReceive();
        }else{
            String ObjectName = ObjectNameText.getText();
            if(!ObjectName.equals("")){
                StartObjectID_ComboBox.getSelectionModel().selectFirst();
                EndObjectID_ComboBox.getSelectionModel().selectFirst();
            }
        }
    }
    /** ComboBox On Action - 起始對象 */
    @FXML protected void StartObjectIDOnAction(){
        String startObjectID = ComponentToolKit.getObjectIDComboBoxSelectItem(StartObjectID_ComboBox).getObjectID();
        String endObjectID = ComponentToolKit.getObjectIDComboBoxSelectItem(EndObjectID_ComboBox).getObjectID();
        if(startObjectID == null)   EndObjectID_ComboBox.getSelectionModel().selectFirst();
        else{
            ObjectNameText.setText("");
            boolean isSelectNextOne = false;
            if(endObjectID == null ||
                    (ToolKit.isDigital(startObjectID) && ToolKit.isDigital(endObjectID) && Integer.parseInt(startObjectID) > Integer.parseInt(endObjectID))){
                isSelectNextOne = true;
            }else {
                if(!ToolKit.isDigital(startObjectID) && !ToolKit.isDigital(endObjectID)){
                    String startObjectID_index = startObjectID.substring(0,1), startObjectID_number = startObjectID.substring(1);
                    String endObjectID_index = endObjectID.substring(0,1), endObjectID_number = endObjectID.substring(1);
                    if(!startObjectID_index.equals(endObjectID_index) ||
                            (Integer.parseInt(startObjectID_number) > Integer.parseInt(endObjectID_number))){
                        isSelectNextOne = true;
                    }
                }else if(!ToolKit.isDigital(startObjectID) && ToolKit.isDigital(endObjectID)){
                    isSelectNextOne = true;
                }
            }
            if(isSelectNextOne){
                int selectIndex = ComponentToolKit.getObjectIDComboBoxSelectIndex(StartObjectID_ComboBox);
                if(selectIndex == ComponentToolKit.getObjectIDComboBoxItems(StartObjectID_ComboBox).size()-1)
                    EndObjectID_ComboBox.getSelectionModel().select(selectIndex);
                else
                    EndObjectID_ComboBox.getSelectionModel().select(selectIndex+1);
            }
        }
    }
    /** ComboBox Mouse Clicked - 起始對象 */
    @FXML protected void StartObjectIDMouseClicked(MouseEvent MouseEvent){  if(KeyPressed.isMouseLeftClicked(MouseEvent))    StartObjectID = "";    }
    /** ComboBox Key Released - 起始對象 */
    @FXML protected void StartObjectIDKeyReleased(KeyEvent keyEvent){
        String startObjectID = objectIDComboBoxKeyReleased(keyEvent, StartObjectID_ComboBox, StartObjectID);
        if(startObjectID != null){
            this.StartObjectID = startObjectID;
        }
    }
    /** ComboBox Mouse Clicked - 結束對象 */
    @FXML protected void EndObjectIDMouseClicked(MouseEvent MouseEvent){    if(KeyPressed.isMouseLeftClicked(MouseEvent))    EndObjectID = "";  }
    /** ComboBox On Action - 起始對象 */
    @FXML protected void EndObjectIDOnAction(){
        String startObjectID = ComponentToolKit.getObjectIDComboBoxSelectItem(StartObjectID_ComboBox).getObjectID();
        String endObjectID = ComponentToolKit.getObjectIDComboBoxSelectItem(EndObjectID_ComboBox).getObjectID();
        if(startObjectID == null || endObjectID == null)    StartObjectID_ComboBox.getSelectionModel().selectFirst();
        else{
            boolean isSelectNextOne = false;
            String startObjectID_index = startObjectID.substring(0,1), startObjectID_number = startObjectID.substring(1);
            String endObjectID_index = endObjectID.substring(0,1), endObjectID_number = endObjectID.substring(1);
            if(!ToolKit.isDigital(startObjectID) && !ToolKit.isDigital(endObjectID)){
                if(!startObjectID_index.equals(endObjectID_index) ||
                        (Integer.parseInt(endObjectID_number) < Integer.parseInt(startObjectID_number))){
                    isSelectNextOne = true;
                }
            }else if(!ToolKit.isDigital(startObjectID) && ToolKit.isDigital(endObjectID)){
                isSelectNextOne = true;
            }
            else if(ToolKit.isDigital(startObjectID) &&
                    ToolKit.isDigital(endObjectID) &&
                    Integer.parseInt(endObjectID) < Integer.parseInt(startObjectID)){
                isSelectNextOne = true;
            }
            if(isSelectNextOne){
                int selectIndex = ComponentToolKit.getObjectIDComboBoxSelectIndex(EndObjectID_ComboBox);
                if(selectIndex >= 2)
                    StartObjectID_ComboBox.getSelectionModel().select(selectIndex-1);
                else
                    StartObjectID_ComboBox.getSelectionModel().selectFirst();
            }
        }
    }
    @FXML protected void StartObjectIDOnShowing(){
        ListView<ObjectInfo_Bean> list = (ListView<ObjectInfo_Bean>)((ComboBoxListViewSkin<String>) StartObjectID_ComboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, StartObjectID_ComboBox.getSelectionModel().getSelectedIndex()));
    }

    /** ComboBox Key Released - 結束對象 */
    @FXML protected void EndObjectIDKeyReleased(KeyEvent keyEvent){
        String endObjectID = objectIDComboBoxKeyReleased(keyEvent, EndObjectID_ComboBox, EndObjectID);
        if(endObjectID != null){
            this.EndObjectID = endObjectID;
        }
    }
    @FXML protected void EndObjectIDOnShowing(){
        ListView<ObjectInfo_Bean> list = (ListView)((ComboBoxListViewSkin<String>) EndObjectID_ComboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, EndObjectID_ComboBox.getSelectionModel().getSelectedIndex()));
    }
    private String objectIDComboBoxKeyReleased(KeyEvent keyEvent, ComboBox<ObjectInfo_Bean> comboBox, String objectID){
        if(KeyPressed.isNumberLockKeyPressed(keyEvent)){
            DialogUI.MessageDialog("Number Lock Pressed !");
            return null;
        }else if(!KeyPressed.isEnglishKeyPressed(keyEvent) && !KeyPressed.isDigitalKeyPressed(keyEvent)){
            return null;
        }
        if(!objectID.equals("") && KeyPressed.isEnglishKeyPressed(keyEvent)){
            objectID = "";
        }
        objectID =  KeyPressed.getLetterKeyPressed(keyEvent, objectID);
        objectID =  KeyPressed.getDigitalKeyPressed(keyEvent, objectID);
        ObservableList<ObjectInfo_Bean> CustomerList = ComponentToolKit.getObjectIDComboBoxItems(comboBox);
        for(ObjectInfo_Bean ObjectInfo_Bean : CustomerList){
            if(ObjectInfo_Bean.getObjectID() != null && ObjectInfo_Bean.getObjectID().contains(objectID)) {
                comboBox.getSelectionModel().select(ObjectInfo_Bean);
                break;
            }
        }
        if(ToolKit.isEnglish(objectID.substring(0,1))){
            if(objectID.length() == Integer.parseInt(SystemSettingConfig_Bean.getCustomerIDAreaLength())+1) {
                objectID = "";
            }
        }else if(objectID.length() == Integer.parseInt(SystemSettingConfig_Bean.getCustomerIDLength())){
            objectID = "";
        }
        return objectID;
    }
    /** Button Mouse Clicked - 上一期 */
    @FXML protected void PreviousTermMouseClicked_CheckoutByMonth(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String PreviousDate;
            List<Integer> DateList = null;
            for(int index = 0 ; index < 2 ; index++){
                if(index == 0)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(CheckoutByMonthStartDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(index == 1)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(CheckoutByMonthEndDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(DateList.get(1) > 1){
                    DateList.set(1,DateList.get(1)-1);
                }else if(DateList.get(1) == 1){
                    DateList.set(1,12);
                    DateList.set(0,DateList.get(0)-1);
                }
                PreviousDate = DateList.get(0) + "-" + ToolKit.fillZero(DateList.get(1),2) + "-" + ToolKit.fillZero(DateList.get(2),2);
                if(index == 0)  ComponentToolKit.setDatePickerValue(CheckoutByMonthStartDate_DatePicker,PreviousDate);
                if(index == 1)  ComponentToolKit.setDatePickerValue(CheckoutByMonthEndDate_DatePicker,PreviousDate);
            }
        }
    }
    /** Button Mouse Clicked - 下一期 */
    @FXML protected void NextTermMouseClicked_CheckoutByMonth(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String NextDate;
            List<Integer> DateList = null;
            for(int index = 0 ; index < 2 ; index++){
                if(index == 0)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(CheckoutByMonthStartDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(index == 1)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(CheckoutByMonthEndDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(DateList.get(1) < 12) {
                    DateList.set(1,DateList.get(1)+1);
                }else if(DateList.get(1) == 12){
                    DateList.set(1,1);
                    DateList.set(0,DateList.get(0)+1);
                }
                NextDate = DateList.get(0) + "-" + ToolKit.fillZero(DateList.get(1),2) + "-" + ToolKit.fillZero(DateList.get(2),2);
                if(index == 0)  ComponentToolKit.setDatePickerValue(CheckoutByMonthStartDate_DatePicker,NextDate);
                if(index == 1)  ComponentToolKit.setDatePickerValue(CheckoutByMonthEndDate_DatePicker,NextDate);
            }
        }
    }
    /** Button Mouse Clicked - 上一期 */
    @FXML protected void PreviousTermMouseClicked_NoneCheckoutByMonth(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String PreviousDate;
            List<Integer> DateList = null;
            for(int index = 0 ; index < 2 ; index++){
                if(index == 0)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(NoneCheckoutByMonthStartDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(index == 1)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(NoneCheckoutByMonthEndDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(DateList.get(1) > 1){
                    DateList.set(1,DateList.get(1)-1);
                }else if(DateList.get(1) == 1){
                    DateList.set(1,12);
                    DateList.set(0,DateList.get(0)-1);
                }
                PreviousDate = DateList.get(0) + "-" + ToolKit.fillZero(DateList.get(1),2) + "-" + ToolKit.fillZero(DateList.get(2),2);
                if(index == 0)  ComponentToolKit.setDatePickerValue(NoneCheckoutByMonthStartDate_DatePicker,PreviousDate);
                if(index == 1)  ComponentToolKit.setDatePickerValue(NoneCheckoutByMonthEndDate_DatePicker,PreviousDate);
            }
        }
    }
    /** Button Mouse Clicked - 下一期 */
    @FXML protected void NextTermMouseClicked_NoneCheckoutByMonth(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String NextDate;
            List<Integer> DateList = null;
            for(int index = 0 ; index < 2 ; index++){
                if(index == 0)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(NoneCheckoutByMonthStartDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(index == 1)  DateList = Arrays.stream(ComponentToolKit.getDatePickerValue(NoneCheckoutByMonthEndDate_DatePicker, "yyyy-MM-dd").split("-"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                if(DateList.get(1) < 12) {
                    DateList.set(1,DateList.get(1)+1);
                }else if(DateList.get(1) == 12){
                    DateList.set(1,1);
                    DateList.set(0,DateList.get(0)+1);
                }
                NextDate = DateList.get(0) + "-" + ToolKit.fillZero(DateList.get(1),2) + "-" + ToolKit.fillZero(DateList.get(2),2);
                if(index == 0)  ComponentToolKit.setDatePickerValue(NoneCheckoutByMonthStartDate_DatePicker,NextDate);
                if(index == 1)  ComponentToolKit.setDatePickerValue(NoneCheckoutByMonthEndDate_DatePicker,NextDate);
            }
        }
    }
    /** Button Mouse Clicked - 查詢 */
    @FXML protected void SearchMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            SearchNonePayReceive();
        }
    }
    private void SearchNonePayReceive() throws Exception{
        ComponentToolKit.getSearchNonePayReceiveTableViewItemList(TableView).clear();
        this.ConditionalPayReceiveSearch_Bean = getSearchPayableReceivable_Bean();

        if(ToolKit.isDateRangeError(CheckoutByMonthStartDate_DatePicker, CheckoutByMonthEndDate_DatePicker)){
            DialogUI.MessageDialog("※ 日期設定錯誤");
            return;
        }
        ObservableList<SearchNonePayReceive_Bean> NonePayReceivableList = SearchNonePayReceive_Model.getNonePayableReceivableOrderInfo(OrderObject, ConditionalPayReceiveSearch_Bean);
        if(NonePayReceivableList.size() == 0){
            ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,false);
            if(OrderObject == Order_Enum.OrderObject.廠商)
                DialogUI.MessageDialog("※ 查無相關應付帳款");
            else if(OrderObject == Order_Enum.OrderObject.客戶)
                DialogUI.MessageDialog("※ 查無相關應收帳款");
        }else{
            ComponentToolKit.getSearchNonePayReceiveTableViewItemList(TableView).addAll(NonePayReceivableList);
            setSearchNonePayReceiveInfo(NonePayReceivableList);
            ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,true);
        }
    }

    private void setSearchNonePayReceiveInfo(ObservableList<SearchNonePayReceive_Bean> NonePayReceivableList){
        int OrderCount = 0, OrderPriceAmount = 0, ReturnOrderCount = 0, ReturnOrderPriceAmount = 0, TotalTax = 0, TotalDiscount = 0, PayReceiveTotalPrice = 0, ActualPayReceiveTotalPrice = 0;
        for(SearchNonePayReceive_Bean SearchNonePayReceive_Bean : NonePayReceivableList){
            if(SearchNonePayReceive_Bean.isCheckBoxSelect()){
                OrderCount += SearchNonePayReceive_Bean.getOrderQuantity();
                OrderPriceAmount += SearchNonePayReceive_Bean.getOrderPriceAmount();
                ReturnOrderCount += SearchNonePayReceive_Bean.getReturnOrderQuantity();
                ReturnOrderPriceAmount += SearchNonePayReceive_Bean.getReturnOrderPriceAmount();
                TotalTax += SearchNonePayReceive_Bean.getTax();
                TotalDiscount += SearchNonePayReceive_Bean.getDiscount();
                PayReceiveTotalPrice += SearchNonePayReceive_Bean.getPayReceivePrice();
                ActualPayReceiveTotalPrice += SearchNonePayReceive_Bean.getActualPayReceivePrice();
            }
        }
        TotalOrderCount_Label.setText(String.valueOf(OrderCount));
        TotalOrderPrice_Label.setText(ToolKit.fmtMicrometer(OrderPriceAmount));
        TotalReturnOrderCount_Label.setText(String.valueOf(ReturnOrderCount));
        TotalReturnOrderPrice_Label.setText(ToolKit.fmtMicrometer(ReturnOrderPriceAmount));
        TotalTax_Label.setText(ToolKit.fmtMicrometer(TotalTax));
        TotalDiscount_Label.setText(ToolKit.fmtMicrometer(TotalDiscount));
        PayReceiveTotalPrice_Label.setText(ToolKit.fmtMicrometer(PayReceiveTotalPrice));
        ActualPayReceiveTotalPrice_Label.setText(ToolKit.fmtMicrometer(ActualPayReceiveTotalPrice));
    }

    private ConditionalPayReceiveSearch_Bean getSearchPayableReceivable_Bean(){
        ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean = new ConditionalPayReceiveSearch_Bean();
        ConditionalPayReceiveSearch_Bean.setCheckoutByMonthStartDate(ComponentToolKit.getDatePickerValue(CheckoutByMonthStartDate_DatePicker, "yyyy-MM-dd"));
        ConditionalPayReceiveSearch_Bean.setCheckoutByMonthEndDate(ComponentToolKit.getDatePickerValue(CheckoutByMonthEndDate_DatePicker, "yyyy-MM-dd"));
        ConditionalPayReceiveSearch_Bean.setNoneCheckoutByMonthStartDate(ComponentToolKit.getDatePickerValue(NoneCheckoutByMonthStartDate_DatePicker,"yyyy-MM-dd"));
        ConditionalPayReceiveSearch_Bean.setNoneCheckoutByMonthEndDate(ComponentToolKit.getDatePickerValue(NoneCheckoutByMonthEndDate_DatePicker,"yyyy-MM-dd"));
        ConditionalPayReceiveSearch_Bean.setObjectName(ObjectNameText.getText());
        ConditionalPayReceiveSearch_Bean.setStartObjectID(ComponentToolKit.getObjectIDComboBoxSelectItem(StartObjectID_ComboBox).getObjectID());
        ConditionalPayReceiveSearch_Bean.setEndObjectID(ComponentToolKit.getObjectIDComboBoxSelectItem(EndObjectID_ComboBox).getObjectID());
        return ConditionalPayReceiveSearch_Bean;
    }
    /** Button Mouse Clicked - 列印 */
    @FXML protected void PrintMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ObservableList<SearchNonePayReceive_Bean> SearchNonePayReceivableList = ComponentToolKit.getSearchNonePayReceiveTableViewItemList(TableView);
            if(SearchNonePayReceivableList.size() == 0){
                DialogUI.MessageDialog("※ 無列印資料!");
                return;
            }else{
                boolean isSomeoneSelect = false;
                for(SearchNonePayReceive_Bean SearchNonePayReceive_Bean : SearchNonePayReceivableList){
                    if(SearchNonePayReceive_Bean.isCheckBoxSelect()){
                        isSomeoneSelect = true;
                        break;
                    }
                }
                if(!isSomeoneSelect){
                    DialogUI.MessageDialog("※ 未選擇任何資料!");
                    return;
                }
            }
            ObservableList<SearchNonePayReceive_Bean> selectNonePayReceivableList = FXCollections.observableArrayList();
            for(SearchNonePayReceive_Bean searchNonePayReceive_Bean : SearchNonePayReceivableList){
                if(searchNonePayReceive_Bean.isCheckBoxSelect()){
                    selectNonePayReceivableList.add(searchNonePayReceive_Bean);
                }
            }
            ExportNonePayReceiveDocument exportNonePayReceiveDocument = new ExportNonePayReceiveDocument();
            if(exportNonePayReceiveDocument.exportSearchNonePayReceive(ConditionalPayReceiveSearch_Bean, selectNonePayReceivableList,OrderObject))
                DialogUI.MessageDialog("※ 列印成功!");
            else
                DialogUI.MessageDialog("※ 列印失敗!");
        }
    }
    @FXML protected void exportPayableMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            try{
                this.ConditionalPayReceiveSearch_Bean = getSearchPayableReceivable_Bean();
                if(ToolKit.isDateRangeError(CheckoutByMonthStartDate_DatePicker, CheckoutByMonthEndDate_DatePicker)){
                    DialogUI.MessageDialog("※ 日期設定錯誤");
                    return;
                }
                ObservableList<SearchNonePayReceive_Bean> SearchNonePayReceiveList = ComponentToolKit.getSearchNonePayReceiveTableViewItemList(TableView);
                ExportPayableDocument document = new ExportPayableDocument(ComponentToolKit.getDatePickerValue(CheckoutByMonthStartDate_DatePicker,"yyyy-MM-dd"),
                        ComponentToolKit.getDatePickerValue(CheckoutByMonthEndDate_DatePicker,"yyyy-MM-dd"));
                document.readTemplate(TemplatePath.ExportPayableDocument);
                document.createTopTable(SystemSettingConfig_Bean, SearchNonePayReceiveList);
                document.createButtonTable(SearchNonePayReceiveList,SearchNonePayReceive_Model.getAlreadyPayReceiveCheckInfo(OrderObject));
                String outputFileName = CallConfig.getFile_OutputPath() + "\\" + ToolKit.getToday("yyyy-MM-dd") + "_應付帳款總表.xlsx";
                document.build(outputFileName);
                DialogUI.MessageDialog("※ 匯出成功");
            }catch (Exception Ex){
                if(Ex.toString().contains("程序無法存取檔案，因為檔案正由另一個程序使用"))
                    DialogUI.AlarmDialog("※ 匯出失敗，因為檔案正由另一個程序使用");
                else {
                    DialogUI.ExceptionDialog(Ex);
                    ERPApplication.Logger.catching(Ex);
                    DialogUI.MessageDialog("※ 匯出失敗");
                }
            }
        }
    }
    @FXML protected void SelectAllOnAction(){
        ObservableList<SearchNonePayReceive_Bean> nonePayReceiveList = ComponentToolKit.getSearchNonePayReceiveTableViewItemList(TableView);
        if(nonePayReceiveList.size() != 0){
            for(SearchNonePayReceive_Bean SearchNonePayReceive_Bean : nonePayReceiveList){
                SearchNonePayReceive_Bean.setCheckBoxSelect(SelectAll_CheckBox.isSelected());
            }
            setSearchNonePayReceiveInfo(nonePayReceiveList);
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftDoubleClicked(MouseEvent)){
            SearchNonePayReceive_Bean SearchNonePayReceive_Bean = ComponentToolKit.getSearchNonePayReceiveTableViewSelectItem(TableView);
            if(SearchNonePayReceive_Bean != null)
                CallFXML.ShowNonePayReceiveDetails(MainStage, OrderObject, ConditionalPayReceiveSearch_Bean, SearchNonePayReceive_Bean);
        }
    }
    private void setColumnCellValueAndCheckBox(TableColumn<SearchNonePayReceive_Bean,CheckBox> TableColumn, String ColumnValue, String Alignment, String FontSize){
        ComponentToolKit.setColumnCellValue(TableColumn,ColumnValue, Alignment, FontSize,null);
        TableColumn.setCellFactory(column -> new setColumnCellValueAndCheckBox(Alignment, FontSize));
    }
    private class setColumnCellValueAndCheckBox extends TableCell<SearchNonePayReceive_Bean, CheckBox> {
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
                CheckBox checkBox = getTableView().getItems().get(getIndex()).getCheckBox();
                setGraphic(checkBox);
                checkBox.setOnAction(ActionEvent -> {
                    boolean isSelectAll = true;
                    ObservableList<SearchNonePayReceive_Bean> nonePayReceiveList = ComponentToolKit.getSearchNonePayReceiveTableViewItemList(TableView);
                    for(SearchNonePayReceive_Bean SearchNonePayReceive_Bean : nonePayReceiveList){
                        if(!SearchNonePayReceive_Bean.isCheckBoxSelect()) {
                            isSelectAll = false;
                            break;
                        }
                    }
                    ComponentToolKit.setCheckBoxSelect(SelectAll_CheckBox,isSelectAll);
                    setSearchNonePayReceiveInfo(nonePayReceiveList);
                });
            }else   setGraphic(null);
        }
    }
    private void setIntegerPriceColumnMicrometerFormat(TableColumn<SearchNonePayReceive_Bean,Integer> TableColumn, String ColumnValue, String Alignment, String FontSize){
        TableColumn.setCellValueFactory(new PropertyValueFactory<>(ColumnValue));
        TableColumn.setStyle("-fx-alignment:" + Alignment + "; -fx-font-size:" + FontSize + "px;");
        TableColumn.setCellFactory(column -> new setIntegerPriceColumnMicrometerFormat(Alignment, FontSize));
    }
    private class setIntegerPriceColumnMicrometerFormat extends TableCell<SearchNonePayReceive_Bean, Integer> {
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
