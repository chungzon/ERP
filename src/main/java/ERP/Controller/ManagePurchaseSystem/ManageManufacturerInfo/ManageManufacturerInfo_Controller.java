package ERP.Controller.ManagePurchaseSystem.ManageManufacturerInfo;

import ERP.Bean.ManagePayableReceivable.BankInfo_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.DefaultPaymentMethod;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.OrderTax;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.DiscountRemittanceFee;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.DiscountPostage;
import ERP.Model.ManageManufacturerInfo.ManageManufacturerInfo_Model;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/** [Controller] Manage manufacturer info */
public class ManageManufacturerInfo_Controller {
    @FXML RadioButton CheckoutByMonth_RadioButton,NoneCheckoutByMonth_RadioButton;
    @FXML TextField ManufacturerIDText, ManufacturerNameText, ManufacturerNickNameText, PersonInChargeText;
    @FXML TextField ContactPersonText, Telephone1TitleText, Telephone1NumberText, Telephone1TransferText, Telephone2TitleText, Telephone2NumberText, Telephone2TransferText, CellPhoneTitleText, CellPhoneNumberText, FaxTitleText, FaxNumberText, EmailAccountText, EmailAddressText;
    @FXML TextField CompanyAddressText, DeliveryAddressText, InvoiceTitleText, TaxIDNumberText, InvoiceAddressText, PayableDayText, RemittanceFeeText;
    @FXML TextField BankBranchText, AccountNameText, BankAccountText;
    @FXML TextField CheckTitleText, CheckDueDayText, PostageText;
    @FXML TextArea RemarkText;
    @FXML CheckBox DeliveryAddress_SameAsCompanyAddress_CheckBox, InvoiceAddress_SameAsCompanyAddress_CheckBox, InvoiceAddress_SameAsDeliveryAddress_CheckBox, DiscountRemittanceFee_CheckBox, DiscountPostage_CheckBox;
    @FXML ComboBox<OrderTax> OrderTax_ComboBox;
    @FXML ComboBox<DefaultPaymentMethod> defaultPaymentMethod_ComboBox;
    @FXML Spinner<Double> PayableDiscountSpinner;
    @FXML ComboBox<BankInfo_Bean> AllBank_ComboBox;
    @FXML ComboBox<ObjectInfo_Bean> StartManufacturerID_ComboBox, EndManufacturerID_ComboBox;
    @FXML TextField SearchManufacturerNameText;
    @FXML Button SearchManufacturer_Button, PrintManufacturer_Button, ModifyManufacturer_Button, DeleteManufacturer_Button;
    @FXML Slider DeleteSlider;
    @FXML TabPane tabPane;
    @FXML Tab InsertManufacturerTab, SearchManufacturerTab;
    @FXML TableView<ObjectInfo_Bean> TableView;
    @FXML TableColumn<ObjectInfo_Bean,String> ManufacturerIDColumn, ManufacturerNameColumn, ManufacturerNickNameColumn, ContactPersonColumn, Telephone1Column, Telephone2Column, FaxColumn, CompanyAddressColumn;

    private ToolKit ToolKit;
    private CallFXML CallFXML;
    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private String StartObjectID = "", EndObjectID = "";

    private Stage Stage;
    private ManageManufacturerInfo_Model ManageManufacturerInfo_Model;
    private Order_Model Order_Model;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private SystemSetting_Model SystemSetting_Model;
    private ObservableList<ObjectInfo_Bean> ManufacturerList;
    private ObjectInfo_Bean ObjectInfo_Bean;
    private SystemSettingConfig_Bean SystemSettingConfig_Bean;

    public ManageManufacturerInfo_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.CallFXML = ToolKit.CallFXML;
        this.ManageManufacturerInfo_Model = ToolKit.ModelToolKit.getManageManufacturerInfoModel();
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }

    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(ObjectInfo_Bean objectInfo_bean){
        this.SystemSettingConfig_Bean = SystemSetting_Model.loadAllSystemSettingData();
        initialTableView();
        setKeyWordTextLimiter();
        setTextFieldLimitDigital();
        setSpinnerLimitDouble();

        setManufacturerIDText();

        OrderTax_ComboBox.getItems().addAll(OrderTax.values());
        OrderTax_ComboBox.getSelectionModel().select(OrderTax.未稅);
        defaultPaymentMethod_ComboBox.getItems().addAll(DefaultPaymentMethod.values());
        defaultPaymentMethod_ComboBox.getSelectionModel().select(DefaultPaymentMethod.現金);
        AllBank_ComboBox.getItems().addAll(ManagePayableReceivable_Model.getAllBankInfo(true));
        ComponentToolKit.setAllBankBeanComboBoxObj(AllBank_ComboBox);
        AllBank_ComboBox.getSelectionModel().selectFirst();
        ComponentToolKit.setDoubleSpinnerValueFactory(PayableDiscountSpinner,0.01, 1, 1, 0.01);
        PayableDayText.setText("25");
        CheckDueDayText.setText("0");
        RemittanceFeeText.setText("0");
        PostageText.setText("0");


        if(objectInfo_bean != null){
            tabPane.getSelectionModel().select(SearchManufacturerTab);
            this.ObjectInfo_Bean = objectInfo_bean;
            TableView.getItems().add(this.ObjectInfo_Bean);
            TableView.getSelectionModel().select(this.ObjectInfo_Bean);
            ComponentToolKit.setButtonDisable(ModifyManufacturer_Button,false);
            setManufacturerInfo();
        }else{
            this.ObjectInfo_Bean = new ObjectInfo_Bean();
        }
    }
    private void setManufacturerIDTextEditable(boolean editable){
        ComponentToolKit.setTextFieldEditable(ManufacturerIDText,editable);
        if(editable)
            ComponentToolKit.setTextFieldStyle(ManufacturerIDText,"");
        else
            ComponentToolKit.setTextFieldStyle(ManufacturerIDText,"-fx-background-color:#bebebe");
    }
    private void setManufacturerIDText(){
        if(SystemSettingConfig_Bean.getManufacturerIDLength() == null || !ToolKit.isDigital(SystemSettingConfig_Bean.getManufacturerIDLength())){
            setManufacturerIDTextEditable(false);
            DialogUI.AlarmDialog("※ 未設定編號位數");
        }else
            setManufacturerIDTextEditable(true);
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(ManufacturerIDColumn,"ObjectID","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ManufacturerNameColumn,"ObjectName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ManufacturerNickNameColumn,"ObjectNickName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ContactPersonColumn,"ContactPerson","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(Telephone1Column,"Telephone1","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(Telephone2Column,"Telephone2","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(FaxColumn,"Fax","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(CompanyAddressColumn,"CompanyAddress","CENTER-LEFT","16",null);
    }
    private void setKeyWordTextLimiter(){
        if(!SystemSettingConfig_Bean.getManufacturerIDLength().equals(""))
            ComponentToolKit.addKeyWordTextLimitLength(ManufacturerIDText, Integer.parseInt(SystemSettingConfig_Bean.getManufacturerIDLength()));
        ComponentToolKit.addKeyWordTextLimitLength(Telephone1TitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone1NumberText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone1TransferText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone2TitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone2NumberText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone2TransferText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(CellPhoneTitleText, 4);
        ComponentToolKit.addKeyWordTextLimitLength(CellPhoneNumberText, 6);
        ComponentToolKit.addKeyWordTextLimitLength(FaxTitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(FaxNumberText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(TaxIDNumberText, 8);
    }
    private void setTextFieldLimitDigital(){
        ComponentToolKit.addTextFieldLimitDigital(ManufacturerIDText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone1TitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone1NumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone1TransferText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone2TitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone2NumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone2TransferText,false);
        ComponentToolKit.addTextFieldLimitDigital(CellPhoneTitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(CellPhoneNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(FaxTitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(FaxNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(TaxIDNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(PayableDayText,false);
        ComponentToolKit.addTextFieldLimitDigital(RemittanceFeeText,false);
        ComponentToolKit.addTextFieldLimitDigital(PostageText,false);
        ComponentToolKit.addTextFieldLimitDigital(CheckDueDayText,false);
        ComponentToolKit.addTextFiledLimitByBankAccount(BankAccountText);
    }
    private void setSpinnerLimitDouble(){
        ComponentToolKit.addSpinnerLimitDouble(PayableDiscountSpinner,1);
    }
    @FXML protected void CheckoutByMonthMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.setTextFieldDisable(CheckDueDayText,!CheckoutByMonth_RadioButton.isSelected());
        }
    }
    @FXML protected void NoneCheckoutByMonthMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            ComponentToolKit.setTextFieldDisable(CheckDueDayText,NoneCheckoutByMonth_RadioButton.isSelected());
        }
    }
    @FXML protected void ManufacturerIDKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isF6KeyPressed(KeyEvent)){
            String ManufacturerIDTitle = ManufacturerIDText.getText();
            if(ManufacturerIDTitle.equals(""))
                DialogUI.MessageDialog("※ 請輸入編號第一碼");
            else{
                String newManufacturerID = ManageManufacturerInfo_Model.generateNewestManufacturerID(ManufacturerIDTitle.substring(0,1));
                if(newManufacturerID == null) {
                    ManufacturerIDText.setText("");
                    DialogUI.MessageDialog("※ 廠商編號已達上限!");
                }else
                    ManufacturerIDText.setText(newManufacturerID);
                ObservableList<ObjectInfo_Bean> ManufacturerList = Order_Model.getObjectFromSearchColumn(OrderObject.廠商, Order_Enum.ObjectSearchColumn.廠商編號, ManufacturerIDTitle.substring(0,1));
                CallFXML.ShowObject(Stage, OrderObject.廠商, ManufacturerList,false, Order_Enum.ShowObjectSource.建立廠商或客戶,null);
            }
        }
    }
    /** TextField Key Pressed - 廠商名稱 */
    @FXML protected void ManufacturerNameKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   ManufacturerNickNameText.requestFocus();    }
    /** TextField Key Pressed - 廠商簡稱 */
    @FXML protected void ManufacturerNickNameKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   PersonInChargeText.requestFocus();    }
    /** TextField Key Pressed - 負責人 */
    @FXML protected void PersonInChargeKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   ContactPersonText.requestFocus();    }
    /** TextField Key Pressed - 聯絡人 */
    @FXML protected void ContactPersonKeyPressed(KeyEvent KeyEvent){    if(KeyPressed.isEnterKeyPressed(KeyEvent))   Telephone1TitleText.requestFocus();    }
    /** TextField Key Pressed - 電話(一) 開頭 */
    @FXML protected void Telephone1TitleKeyPressed(KeyEvent KeyEvent){    if(KeyPressed.isEnterKeyPressed(KeyEvent))   CellPhoneTitleText.requestFocus(); }
    /** TextField Key Released - 電話(一) 開頭 */
    @FXML protected void Telephone1TitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String TelephoneTitle = Telephone1TitleText.getText();
            if(TelephoneTitle.length() >= 2)    Telephone1NumberText.requestFocus();
        }
    }
    /** TextField Key Released - 電話(一) 號碼 */
    @FXML protected void Telephone1NumberKeyReleased(KeyEvent KeyEvent){
        String TelephoneNumber = Telephone1NumberText.getText();
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) CellPhoneTitleText.requestFocus();
        else if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (TelephoneNumber.equals("")) Telephone1TitleText.requestFocus();
        }else if(KeyPressed.isDeleteKeyPressed(KeyEvent) && KeyPressed.isDirectionKeyPressed(KeyEvent)){
            if(TelephoneNumber.length() >= 7)   Telephone1TransferText.requestFocus();
        }
    }
    /** TextField Key Released - 電話(一) 轉接 */
    @FXML protected void Telephone1TransferKeyReleased(KeyEvent KeyEvent){
        String TelephoneTransfer = Telephone1TransferText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent))   Telephone2TitleText.requestFocus();
        else if(KeyPressed.isBackSpaceKeyPressed(KeyEvent)) {
            if (TelephoneTransfer.equals("")) Telephone1NumberText.requestFocus();
        }
    }
    /** TextField Key Pressed - 電話(二) 開頭 */
    @FXML protected void Telephone2TitleKeyPressed(KeyEvent KeyEvent){    if(KeyPressed.isEnterKeyPressed(KeyEvent))   CellPhoneTitleText.requestFocus(); }
    /** TextField Key Released - 電話(二) 開頭 */
    @FXML protected void Telephone2TitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String TelephoneTitle = Telephone2TitleText.getText();
            if(TelephoneTitle.length() >= 2)    Telephone2NumberText.requestFocus();
        }
    }
    /** TextField Key Released - 電話(二) 號碼 */
    @FXML protected void Telephone2NumberKeyReleased(KeyEvent KeyEvent){
        String TelephoneNumber = Telephone2NumberText.getText();
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) CellPhoneTitleText.requestFocus();
        else if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (TelephoneNumber.equals("")) Telephone2TitleText.requestFocus();
        }else if(KeyPressed.isDeleteKeyPressed(KeyEvent) && KeyPressed.isDirectionKeyPressed(KeyEvent)){
            if(TelephoneNumber.length() >= 7)   Telephone2TransferText.requestFocus();
        }
    }
    /** TextField Key Released - 電話(二) 轉接 */
    @FXML protected void Telephone2TransferKeyReleased(KeyEvent KeyEvent){
        String TelephoneTransfer = Telephone2TransferText.getText();
        if(KeyPressed.isEnterKeyPressed(KeyEvent))   CellPhoneTitleText.requestFocus();
        else if(KeyPressed.isBackSpaceKeyPressed(KeyEvent)) {
            if (TelephoneTransfer.equals("")) Telephone2NumberText.requestFocus();
        }
    }
    /** TextField Key Pressed - 行動電話 開頭 */
    @FXML protected void CellphoneTitleKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(CellPhoneTitleText.getText().equals("")) FaxTitleText.requestFocus();
            else    CellPhoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 行動電話 開頭 */
    @FXML protected void CellphoneTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String CellphoneTitle = CellPhoneTitleText.getText();
            if(CellphoneTitle.length() >= 4)    CellPhoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 行動電話 號碼 */
    @FXML protected void CellphoneNumberKeyReleased(KeyEvent KeyEvent){
        String CellphoneBody = CellPhoneNumberText.getText();
        if (KeyPressed.isEnterKeyPressed(KeyEvent)) FaxTitleText.requestFocus();
        else if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (CellphoneBody.equals("")) CellPhoneTitleText.requestFocus();
        }
    }
    /** TextField Key Pressed - 傳真 開頭 */
    @FXML protected void FaxTitleKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))   EmailAccountText.requestFocus();    }
    /** TextField Key Released - 傳真 開頭 */
    @FXML protected void FaxTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String FaxTitle = FaxTitleText.getText();
            if(FaxTitle.length() >= 2)    FaxNumberText.requestFocus();
        }
    }
    /** TextField Key Pressed - 傳真 號碼 */
    @FXML protected void FaxNumberKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))   EmailAccountText.requestFocus();    }
    /** TextField Key Released - 傳真 號碼 */
    @FXML protected void FaxNumberKeyReleased(KeyEvent KeyEvent){
        String FaxNumber = FaxNumberText.getText();
        if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (FaxNumber.equals("")) FaxTitleText.requestFocus();
        }
    }
    /** TextField Key Pressed - 信箱帳號 */
    @FXML protected void EmailAccountKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent)) EmailAddressText.requestFocus();    }
    /** TextField Key Pressed - 信箱地址 */
    @FXML protected void EmailAddressKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   CompanyAddressText.requestFocus();  }
    /** TextField Key Pressed - 公司地址 */
    @FXML protected void CompanyAddressKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   DeliveryAddressText.requestFocus();    }
    /** TextField Key Pressed - 出貨地址 */
    @FXML protected void DeliveryAddressKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))   InvoiceTitleText.requestFocus();    }
    /** TextField Key Pressed - 發票抬頭 */
    @FXML protected void InvoiceTitleKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   TaxIDNumberText.requestFocus();    }
    /** TextField Key Pressed - 統一編號 */
    @FXML protected void TaxIDNumberKeyPressed(KeyEvent KeyEvent){    if(KeyPressed.isEnterKeyPressed(KeyEvent))   InvoiceAddressText.requestFocus();    }
    /** TextField Key Pressed - 發票地址 */
    @FXML protected void InvoiceAddressKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   RemarkText.requestFocus();    }
    /** TextField Key Pressed - 備註 */
    @FXML protected void RemarkKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   ManufacturerNameText.requestFocus();    }
    /** CheckBox On Action - 出貨地址同公司地址 */
    @FXML protected void DeliveryAddress_SameAsCompanyAddress_OnAction(){
        if(DeliveryAddress_SameAsCompanyAddress_CheckBox.isSelected())  DeliveryAddressText.setText(CompanyAddressText.getText());
        else    DeliveryAddressText.setText("");
    }
    /** CheckBox On Action - 發票地址同公司地址 */
    @FXML protected void InvoiceAddress_SameAsCompanyAddress_OnAction(){
        if(InvoiceAddress_SameAsCompanyAddress_CheckBox.isSelected()){
            InvoiceAddressText.setText(CompanyAddressText.getText());
            InvoiceAddress_SameAsDeliveryAddress_CheckBox.setSelected(false);
        }else   InvoiceAddressText.setText("");
    }
    /** CheckBox On Action - 發票地址同送貨地址 */
    @FXML protected void InvoiceAddress_SameAsDeliveryAddress_OnAction(){
        if(InvoiceAddress_SameAsDeliveryAddress_CheckBox.isSelected()){
            InvoiceAddressText.setText(DeliveryAddressText.getText());
            InvoiceAddress_SameAsCompanyAddress_CheckBox.setSelected(false);
        }else   InvoiceAddressText.setText("");
    }
    /** TextField Key Released - 付款日期 */
    @FXML protected void PayableDayKeyReleased(){
        String PayableDay = PayableDayText.getText();
        if(!PayableDay.equals("") && Integer.parseInt(PayableDay) >= 31)
            PayableDayText.setText("");
    }
    /** Tab On Selection Changed - 廠商資料速查 */
    @FXML protected void SearchManufacturerTabOnSelectionChanged(){
        initialComponent();
        if(SearchManufacturerTab.isSelected()){
            setManufacturerIDTextEditable(false);
            ObjectInfo_Bean = null;
            StartManufacturerID_ComboBox.getItems().clear();
            EndManufacturerID_ComboBox.getItems().clear();
            TableView.getItems().clear();
            ManufacturerList = Order_Model.getObjectFromSearchColumn(OrderObject.廠商, null, null);
            StartManufacturerID_ComboBox.getItems().addAll(ManufacturerList);
            StartManufacturerID_ComboBox.getSelectionModel().selectFirst();
            ComponentToolKit.setObjectInfoBeanComboBoxObj(StartManufacturerID_ComboBox);
            EndManufacturerID_ComboBox.getItems().addAll(ManufacturerList);

            StartManufacturerIDOnAction();
            ComponentToolKit.setObjectInfoBeanComboBoxObj(EndManufacturerID_ComboBox);

            SearchManufacturerNameText.setText("");
            if(ManufacturerList.size() == 0){
                ComponentToolKit.setButtonDisable(SearchManufacturer_Button,true);
                ComponentToolKit.setButtonDisable(PrintManufacturer_Button,true);
            }
        }else {
            setManufacturerIDText();
            ObjectInfo_Bean = new ObjectInfo_Bean();
        }
    }
    /** Button Mouse Clicked - 新增 */
    @FXML protected void InsertManufacturerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(ManufacturerIDText.getText() == null)
                DialogUI.MessageDialog("※ 廠商 編號已達上限!");
            else if(ManufacturerIDText.getText().equals(""))
                DialogUI.MessageDialog("※ 廠商 編號錯誤!");
            else if((ManufacturerIDText.getText().length() != Integer.parseInt(SystemSettingConfig_Bean.getManufacturerIDLength())))
                DialogUI.MessageDialog("※ 廠商 編號長度錯誤!");
            else if(ManageManufacturerInfo_Model.isManufacturerIDExist(ManufacturerIDText.getText(),false))
                DialogUI.MessageDialog("※ 廠商 編號重複!");
            else if(PayableDayText.getText().equals(""))
                DialogUI.MessageDialog("※ 請輸入付款日期!");
            else if(ManufacturerNameText.getText().equals(""))
                DialogUI.MessageDialog("※ 請輸入廠商名稱!");
            else if(CheckoutByMonth_RadioButton.isSelected() && (CheckDueDayText.getText().equals("") || CheckDueDayText.getText().equals("0")))
                DialogUI.MessageDialog("※ 請輸入月結幾天(支票到期日)");
            else if(!isColumnFormatError() && DialogUI.ConfirmDialog("是否新增 ?",true,false,0,0)){
                recordManufacturerInfo();
                if (ManageManufacturerInfo_Model.insertManufacturer(ObjectInfo_Bean,false)) {
                    DialogUI.MessageDialog("※ 新增廠商成功");
                    initialComponent();
                } else
                    DialogUI.MessageDialog("※ 新增廠商失敗");
            }
        }
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void ModifyManufacturerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(PayableDayText.getText().equals(""))
                DialogUI.MessageDialog("※ 請輸入付款日期!");
            else if(ManufacturerIDText.getText() == null)
                DialogUI.MessageDialog("※ 廠商 編號已達上限!");
            else if(ManufacturerIDText.getText().equals(""))
                DialogUI.MessageDialog("※ 廠商 編號錯誤!");
            else if(ManufacturerNameText.getText().equals(""))
                DialogUI.MessageDialog("※ 請輸入廠商名稱!");
            else if(CheckoutByMonth_RadioButton.isSelected() && (CheckDueDayText.getText().equals("") || CheckDueDayText.getText().equals("0")))
                DialogUI.MessageDialog("※ 請輸入月結幾天(支票到期日)");
            else if(!isColumnFormatError() && DialogUI.ConfirmDialog("是否修改 ?",true,false,0,0)){
                recordManufacturerInfo();
                if (ManageManufacturerInfo_Model.modifyManufacturer(ObjectInfo_Bean))
                    DialogUI.MessageDialog("※ 修改廠商成功");
                else
                    DialogUI.MessageDialog("※ 修改廠商失敗");
            }
        }
    }
    private void recordManufacturerInfo(){
        ObjectInfo_Bean.setObjectID(ManufacturerIDText.getText());
        ObjectInfo_Bean.setObjectName(ManufacturerNameText.getText());
        ObjectInfo_Bean.setObjectNickName(ManufacturerNickNameText.getText());
        ObjectInfo_Bean.setPersonInCharge(PersonInChargeText.getText());
        ObjectInfo_Bean.setContactPerson(ContactPersonText.getText());
        if(Telephone1TitleText.getText().equals(""))  ObjectInfo_Bean.setTelephone1("");
        else if(Telephone1TransferText.getText().equals(""))    ObjectInfo_Bean.setTelephone1(Telephone1TitleText.getText() + "-" + Telephone1NumberText.getText());
        else    ObjectInfo_Bean.setTelephone1(Telephone1TitleText.getText() + "-" + Telephone1NumberText.getText() + "#" + Telephone1TransferText.getText());
        if(Telephone2TitleText.getText().equals(""))  ObjectInfo_Bean.setTelephone2("");
        else if(Telephone2TransferText.getText().equals(""))    ObjectInfo_Bean.setTelephone2(Telephone2TitleText.getText() + "-" + Telephone2NumberText.getText());
        else    ObjectInfo_Bean.setTelephone2(Telephone2TitleText.getText() + "-" + Telephone2NumberText.getText() + "#" + Telephone2TransferText.getText());
        if(CellPhoneTitleText.getText().equals(""))   ObjectInfo_Bean.setCellPhone("");
        else    ObjectInfo_Bean.setCellPhone(CellPhoneTitleText.getText() + "-" + CellPhoneNumberText.getText());
        if(FaxTitleText.getText().equals(""))   ObjectInfo_Bean.setFax("");
        else    ObjectInfo_Bean.setFax(FaxTitleText.getText() + "-" + FaxNumberText.getText());
        if(EmailAccountText.getText().equals("")) ObjectInfo_Bean.setEmail("");
        else    ObjectInfo_Bean.setEmail(EmailAccountText.getText() + "@" + EmailAddressText.getText());
        ObjectInfo_Bean.setCompanyAddress(CompanyAddressText.getText());
        ObjectInfo_Bean.setDeliveryAddress(DeliveryAddressText.getText());
        ObjectInfo_Bean.setInvoiceTitle(InvoiceTitleText.getText());
        ObjectInfo_Bean.setTaxIDNumber(TaxIDNumberText.getText());
        ObjectInfo_Bean.setInvoiceAddress(InvoiceAddressText.getText());
        ObjectInfo_Bean.setOrderTax(OrderTax_ComboBox.getSelectionModel().getSelectedItem());
        ObjectInfo_Bean.setDefaultPaymentMethod(defaultPaymentMethod_ComboBox.getSelectionModel().getSelectedItem());
        ObjectInfo_Bean.setPayableReceivableDiscount(PayableDiscountSpinner.getValue());
        ObjectInfo_Bean.setRemark(RemarkText.getText());
        ObjectInfo_Bean.setPayableDay(Integer.parseInt(PayableDayText.getText()));
        ObjectInfo_Bean.setCheckTitle(CheckTitleText.getText());

        ObjectInfo_Bean.setCheckoutByMonth(CheckoutByMonth_RadioButton.isSelected());
        ObjectInfo_Bean.setCheckDueDay(ObjectInfo_Bean.isCheckoutByMonth() ? Integer.parseInt(CheckDueDayText.getText()) : 0);
        if(DiscountRemittanceFee_CheckBox.isSelected()) ObjectInfo_Bean.setDiscountRemittanceFee(DiscountRemittanceFee.已扣);
        else    ObjectInfo_Bean.setDiscountRemittanceFee(DiscountRemittanceFee.未扣);
        if(DiscountPostage_CheckBox.isSelected()) ObjectInfo_Bean.setDiscountPostage(DiscountPostage.已扣);
        else    ObjectInfo_Bean.setDiscountPostage(DiscountPostage.未扣);
        ObjectInfo_Bean.setRemittanceFee(Integer.parseInt(RemittanceFeeText.getText()));
        ObjectInfo_Bean.setPostage(Integer.parseInt(PostageText.getText()));
        ObjectInfo_Bean.setBankID(ComponentToolKit.getAllBankComboBoxSelectItem(AllBank_ComboBox).getBankID());
        ObjectInfo_Bean.setBankBranch(BankBranchText.getText());
        ObjectInfo_Bean.setAccountName(AccountNameText.getText());
        ObjectInfo_Bean.setBankAccount(BankAccountText.getText());
    }
    private boolean isColumnFormatError(){
        boolean errorFormat = false;
        String Telephone1Title = Telephone1TitleText.getText();
        String Telephone1Number = Telephone1NumberText.getText();
        String Telephone1Transfer = Telephone1TransferText.getText();
        String Telephone2Title = Telephone2TitleText.getText();
        String Telephone2Number = Telephone2NumberText.getText();
        String Telephone2Transfer = Telephone2TransferText.getText();
        String CellphoneTitle = CellPhoneTitleText.getText();
        String CellphoneNumber = CellPhoneNumberText.getText();
        String FaxTitle = FaxTitleText.getText();
        String FaxNumber = FaxNumberText.getText();
        String EmailAccount = EmailAccountText.getText();
        String EmailAddress = EmailAddressText.getText();
        if((!Telephone1Title.equals("") && (Telephone1Title.length() != 2 || Telephone1Number.length() < 7)) || (Telephone1Title.equals("") && (Telephone1Number.length() != 0 || Telephone1Transfer.length() != 0))){
            DialogUI.MessageDialog("※ 電話(一) 格式錯誤");
            errorFormat = true;
        }else if((!Telephone2Title.equals("") && (Telephone2Title.length() != 2 || Telephone2Number.length() < 7)) || (Telephone2Title.equals("") && (Telephone2Number.length() != 0 || Telephone2Transfer.length() != 0))) {
            DialogUI.MessageDialog("※ 電話(二) 格式錯誤");
            errorFormat = true;
        }else if((!CellphoneTitle.equals("") && (CellphoneTitle.length() != 4 || CellphoneNumber.length() != 6)) || (CellphoneTitle.equals("") && CellphoneNumber.length() != 0)) {
            DialogUI.MessageDialog("※ 行動電話 格式錯誤");
            errorFormat = true;
        }else if((!FaxTitle.equals("") && (FaxTitle.length() != 2 || FaxNumber.length() < 7)) || (FaxTitle.equals("") && FaxNumber.length() != 0)) {
            DialogUI.MessageDialog("※ 傳真 格式錯誤");
            errorFormat = true;
        }else if((!EmailAccount.equals("") && EmailAddress.equals("")) || (EmailAccount.equals("") && !EmailAddress.equals(""))) {
            DialogUI.MessageDialog("※ 信箱 格式錯誤");
            errorFormat = true;
        }
        return errorFormat;
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeleteManufacturerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            if(ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請選擇廠商");
            else{
                if(ManageManufacturerInfo_Model.isManufacturerExistOrder(ObjectInfo_Bean.getObjectID()))    DialogUI.AlarmDialog("※ 此廠商存在採購(進貨)單!");
                else{
                    if(DialogUI.ConfirmDialog("確定刪除 ?",true,false,0,0)){
                        if (ManageManufacturerInfo_Model.deleteManufacturer(ObjectInfo_Bean.getObjectID())) {
                            DialogUI.MessageDialog("※ 刪除廠商成功");
                            TableView.getItems().remove(ObjectInfo_Bean);
                            ManufacturerList.remove(ObjectInfo_Bean);
                            StartManufacturerID_ComboBox.getItems().remove(ObjectInfo_Bean);
                            EndManufacturerID_ComboBox.getItems().remove(ObjectInfo_Bean);
                            StartManufacturerID_ComboBox.getSelectionModel().selectFirst();
                            EndManufacturerID_ComboBox.getSelectionModel().selectFirst();
                            initialComponent();
                        } else DialogUI.MessageDialog("※ 刪除廠商失敗");
                    }
                }
            }

        }
    }
    /** Slider Mouse Released - 新增的滑軌 */
    @FXML protected void DeleteSliderMouseReleased(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            double SliderValue = DeleteSlider.getValue();
            if (SliderValue == 1) ComponentToolKit.setButtonDisable(DeleteManufacturer_Button, false);
            else ComponentToolKit.setButtonDisable(DeleteManufacturer_Button, true);
        }
    }
    /** Button Mouse Clicked - 清空 */
    @FXML protected void ClearComponentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && DialogUI.ConfirmDialog("確定清空 ?",true,false,0,0)) {
            initialComponent();
        }
    }
    /** ComboBox On Action - 起始廠商 */
    @FXML protected void StartManufacturerIDOnAction(){
        SearchManufacturerNameText.setText("");
        ObjectInfo_Bean selectObjectInfo_Bean = ComponentToolKit.getObjectIDComboBoxSelectItem(StartManufacturerID_ComboBox);
        if(selectObjectInfo_Bean == null)
            return;

        String startObjectIDTitle = selectObjectInfo_Bean.getObjectID().substring(0,1);
        ObservableList<ObjectInfo_Bean> ObjectList = ComponentToolKit.getObjectIDComboBoxItems(StartManufacturerID_ComboBox);

        boolean record = false;
        for(int index = 0 ; index < ObjectList.size() ; index++){
            ObjectInfo_Bean ObjectInfo_Bean = ObjectList.get(index);
            if(!record && ObjectInfo_Bean.getObjectID().substring(0,1).equals(startObjectIDTitle)) {
                record = true;
                if (index == ObjectList.size() - 1) {
                    EndManufacturerID_ComboBox.getSelectionModel().select(index);
                    break;
                }
            }else if(record && !ObjectInfo_Bean.getObjectID().substring(0,1).equals(startObjectIDTitle)) {
                EndManufacturerID_ComboBox.getSelectionModel().select(index-1);
                break;
            }else if(index == ObjectList.size()-1) {
                EndManufacturerID_ComboBox.getSelectionModel().select(index);
                break;
            }
        }
    }
    /** ComboBox Mouse Clicked - 起始廠商 */
    @FXML protected void StartManufacturerIDMouseClicked(MouseEvent MouseEvent){    if(KeyPressed.isMouseLeftClicked(MouseEvent))    StartObjectID = "";  }
    /** ComboBox Keu Released - 起始廠商 */
    @FXML protected void StartManufacturerIDKeyReleased(KeyEvent KeyEvent){
        StartObjectID =  KeyPressed.getLetterKeyPressed(KeyEvent, StartObjectID);
        StartObjectID =  KeyPressed.getDigitalKeyPressed(KeyEvent, StartObjectID);
        ObservableList<ObjectInfo_Bean> ManufacturerList = ComponentToolKit.getObjectIDComboBoxItems(StartManufacturerID_ComboBox);
        for(ObjectInfo_Bean ObjectInfo_Bean : ManufacturerList){
            if(ObjectInfo_Bean.getObjectID() != null && ObjectInfo_Bean.getObjectID().contains(StartObjectID)) {
                StartManufacturerID_ComboBox.getSelectionModel().select(ObjectInfo_Bean);
                break;
            }
        }
        if(StartObjectID.length() == Integer.parseInt(SystemSettingConfig_Bean.getManufacturerIDLength())) StartObjectID = "";
    }
    @FXML protected void StartManufacturerIDOnShowing(){
        ListView list = (ListView) ((ComboBoxListViewSkin) StartManufacturerID_ComboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, StartManufacturerID_ComboBox.getSelectionModel().getSelectedIndex()));
    }
    /** ComboBox Mouse Clicked - 結束廠商 */
    @FXML protected void EndManufacturerIDMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){  EndObjectID = "";   }
    }
    /** ComboBox Key Released - 結束廠商 */
    @FXML protected void EndManufacturerIDKeyReleased(KeyEvent KeyEvent){
        EndObjectID =  KeyPressed.getLetterKeyPressed(KeyEvent, EndObjectID);
        EndObjectID =  KeyPressed.getDigitalKeyPressed(KeyEvent, EndObjectID);
        ObservableList<ObjectInfo_Bean> ManufacturerList = ComponentToolKit.getObjectIDComboBoxItems(EndManufacturerID_ComboBox);
        for(ObjectInfo_Bean ObjectInfo_Bean : ManufacturerList){
            if(ObjectInfo_Bean.getObjectID() != null && ObjectInfo_Bean.getObjectID().contains(EndObjectID)) {
                EndManufacturerID_ComboBox.getSelectionModel().select(ObjectInfo_Bean);
                break;
            }
        }
        if(EndObjectID.length() == Integer.parseInt(SystemSettingConfig_Bean.getManufacturerIDLength())) EndObjectID = "";
    }
    @FXML protected void EndManufacturerIDOnShowing(){
        ListView list = (ListView) ((ComboBoxListViewSkin) EndManufacturerID_ComboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, EndManufacturerID_ComboBox.getSelectionModel().getSelectedIndex()));
    }
    @FXML protected void SearchManufacturerIDMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            initialComponent();
            TableView.getItems().clear();

            int startObjectIndex = ComponentToolKit.getObjectIDComboBoxSelectIndex(StartManufacturerID_ComboBox);
            int EndObjectIDIndex = ComponentToolKit.getObjectIDComboBoxSelectIndex(EndManufacturerID_ComboBox);
            if(EndObjectIDIndex < startObjectIndex)
                DialogUI.MessageDialog("※ 廠商區間錯誤");
            else{
                String StartObjectID = ComponentToolKit.getObjectIDComboBoxSelectItem(StartManufacturerID_ComboBox).getObjectID();
                String EndObjectID = ComponentToolKit.getObjectIDComboBoxSelectItem(EndManufacturerID_ComboBox).getObjectID();
                boolean addManufacturer = false;
                for(ObjectInfo_Bean ObjectInfo_Bean : ManufacturerList){
                    if(ObjectInfo_Bean.getObjectID().equals(StartObjectID))   addManufacturer = true;
                    if(addManufacturer) TableView.getItems().add(ObjectInfo_Bean);
                    if(ObjectInfo_Bean.getObjectID().equals(EndObjectID)) break;
                }
            }
        }
    }
    /** Button Mouse Clicked - 查詢 */
    @FXML protected void SearchManufacturerNameMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            initialComponent();
            TableView.getItems().clear();
            String SearchManufacturerName = SearchManufacturerNameText.getText();
            if(SearchManufacturerName.equals("")) TableView.getItems().addAll(ManufacturerList);
            else    for(ObjectInfo_Bean ObjectInfo_Bean : ManufacturerList) if(ObjectInfo_Bean.getObjectName().contains(SearchManufacturerName))    TableView.getItems().addAll(ObjectInfo_Bean);
            if(ComponentToolKit.getTableViewItemsSize(TableView) == 0) DialogUI.MessageDialog("※ 未找到相關廠商");
        }
    }
    /** Button Key Pressed - 查詢 */
    @FXML protected void SearchManufacturerNameKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            TableView.getItems().clear();
            String SearchManufacturerName = SearchManufacturerNameText.getText();
            if(SearchManufacturerName.equals("")) TableView.getItems().addAll(ManufacturerList);
            else{
                for(ObjectInfo_Bean ObjectInfo_Bean : ManufacturerList) {
                    if (ObjectInfo_Bean.getObjectName().contains(SearchManufacturerName) || ObjectInfo_Bean.getObjectNickName().contains(SearchManufacturerName)
                            || ObjectInfo_Bean.getContactPerson().contains(SearchManufacturerName))
                        TableView.getItems().addAll(ObjectInfo_Bean);
                }
            }
            if(ComponentToolKit.getTableViewItemsSize(TableView) == 0) DialogUI.MessageDialog("※ 未找到相關廠商");
        }
    }
    /** Button Mouse Clicked - 列印 */
    @FXML protected void PrintManufacturerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ObservableList<ObjectInfo_Bean> ManufacturerList = TableView.getItems();
            ObjectInfo_Bean StartManufacturerInfo_Bean = ComponentToolKit.getObjectIDComboBoxSelectItem(StartManufacturerID_ComboBox);
            ObjectInfo_Bean EndManufacturerInfo_Bean = ComponentToolKit.getObjectIDComboBoxSelectItem(EndManufacturerID_ComboBox);
            String StartManufacturer = StartManufacturerInfo_Bean.getObjectID() + "  " + StartManufacturerInfo_Bean.getObjectNickName();
            String EndManufacturer = EndManufacturerInfo_Bean.getObjectID() + "  " + EndManufacturerInfo_Bean.getObjectNickName();
            if (ManufacturerList.size() != 0)
                ManageManufacturerInfo_Model.printManufacturerInfo(ManufacturerList, StartManufacturer, EndManufacturer);
            else DialogUI.MessageDialog("※ 無任何廠商");
        }
    }
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent){
        this.ObjectInfo_Bean = ComponentToolKit.getObjectTableViewSelectItem(TableView);
        DeleteSlider.setValue(0);
        ComponentToolKit.setButtonDisable(DeleteManufacturer_Button,true);
        if((KeyPressed.isUpKeyPressed(KeyEvent) || KeyPressed.isDownKeyPressed(KeyEvent)) && ObjectInfo_Bean != null){
            ComponentToolKit.setButtonDisable(ModifyManufacturer_Button,false);
            setManufacturerInfo();
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        this.ObjectInfo_Bean = ComponentToolKit.getObjectTableViewSelectItem(TableView);
        DeleteSlider.setValue(0);
        ComponentToolKit.setButtonDisable(DeleteManufacturer_Button,true);
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ComponentToolKit.getTableViewItemsSize(TableView) != 0 && ObjectInfo_Bean != null){
            ComponentToolKit.setButtonDisable(ModifyManufacturer_Button,false);
            setManufacturerInfo();
        }
    }
    private void setManufacturerInfo(){
        ManufacturerIDText.setText(ObjectInfo_Bean.getObjectID());
        ManufacturerNameText.setText(ObjectInfo_Bean.getObjectName());
        ManufacturerNickNameText.setText(ObjectInfo_Bean.getObjectNickName());
        PersonInChargeText.setText(ObjectInfo_Bean.getPersonInCharge());
        ContactPersonText.setText(ObjectInfo_Bean.getContactPerson());
        setPhoneText(Telephone1TitleText, Telephone1NumberText, Telephone1TransferText, ObjectInfo_Bean.getTelephone1());
        setPhoneText(Telephone2TitleText, Telephone2NumberText, Telephone2TransferText, ObjectInfo_Bean.getTelephone2());
        setPhoneText(CellPhoneTitleText, CellPhoneNumberText, null, ObjectInfo_Bean.getCellPhone());
        setPhoneText(FaxTitleText, FaxNumberText, null, ObjectInfo_Bean.getFax());
        setEmailText(ObjectInfo_Bean.getEmail());

        CompanyAddressText.setText(ObjectInfo_Bean.getCompanyAddress());
        DeliveryAddressText.setText(ObjectInfo_Bean.getDeliveryAddress());
        if(DeliveryAddressText.getText().equals(CompanyAddressText.getText()))  DeliveryAddress_SameAsCompanyAddress_CheckBox.setSelected(true);
        InvoiceTitleText.setText(ObjectInfo_Bean.getInvoiceTitle());
        TaxIDNumberText.setText(ObjectInfo_Bean.getTaxIDNumber());
        InvoiceAddressText.setText(ObjectInfo_Bean.getInvoiceAddress());
        if(InvoiceAddressText.getText().equals(CompanyAddressText.getText())) {
            InvoiceAddress_SameAsCompanyAddress_CheckBox.setSelected(true);
            InvoiceAddress_SameAsDeliveryAddress_CheckBox.setSelected(false);
        }else if(InvoiceAddressText.getText().equals(DeliveryAddressText.getText())) {
            InvoiceAddress_SameAsDeliveryAddress_CheckBox.setSelected(true);
            InvoiceAddress_SameAsCompanyAddress_CheckBox.setSelected(false);
        }
        OrderTax_ComboBox.getSelectionModel().select(ObjectInfo_Bean.getOrderTax());
        defaultPaymentMethod_ComboBox.getSelectionModel().select(ObjectInfo_Bean.getDefaultPaymentMethod());
        ComponentToolKit.setSpinnerDoubleValue(PayableDiscountSpinner, ObjectInfo_Bean.getPayableReceivableDiscount());
        RemarkText.setText(ObjectInfo_Bean.getRemark());
        PayableDayText.setText(String.valueOf(ObjectInfo_Bean.getPayableDay()));
        DiscountRemittanceFee_CheckBox.setSelected(ObjectInfo_Bean.getDiscountRemittanceFee().SelectStatus());
        RemittanceFeeText.setText(String.valueOf(ObjectInfo_Bean.getRemittanceFee()));
        DiscountPostage_CheckBox.setSelected(ObjectInfo_Bean.getDiscountPostage().SelectStatus());
        PostageText.setText(String.valueOf(ObjectInfo_Bean.getPostage()));

        ComponentToolKit.setAllBankComboBox(AllBank_ComboBox, ObjectInfo_Bean.getBankID());
        BankBranchText.setText(ObjectInfo_Bean.getBankBranch());
        AccountNameText.setText(ObjectInfo_Bean.getAccountName());
        BankAccountText.setText(ObjectInfo_Bean.getBankAccount());
        CheckTitleText.setText(ObjectInfo_Bean.getCheckTitle());
        CheckoutByMonth_RadioButton.setSelected(ObjectInfo_Bean.isCheckoutByMonth());
        NoneCheckoutByMonth_RadioButton.setSelected(!ObjectInfo_Bean.isCheckoutByMonth());
        ComponentToolKit.setTextFieldDisable(CheckDueDayText,!ObjectInfo_Bean.isCheckoutByMonth());
        CheckDueDayText.setText(String.valueOf(ObjectInfo_Bean.getCheckDueDay()));
    }
    private void setPhoneText(TextField TitleText, TextField NumberText, TextField TransferText , String PhoneNumber){
        if(PhoneNumber.contains("#")){
            TitleText.setText(PhoneNumber.substring(0, PhoneNumber.indexOf("-")));
            NumberText.setText(PhoneNumber.substring(PhoneNumber.indexOf("-")+1, PhoneNumber.indexOf("#")));
            TransferText.setText(PhoneNumber.substring(PhoneNumber.indexOf("#")+1));
        }else if(PhoneNumber.contains("-")){
            TitleText.setText(PhoneNumber.substring(0, PhoneNumber.indexOf("-")));
            NumberText.setText(PhoneNumber.substring(PhoneNumber.indexOf("-")+1));
        }else{
            TitleText.setText("");
            NumberText.setText("");
            if(TransferText != null)   TransferText.setText("");
        }
    }
    private void setEmailText(String Email){
        if(Email.contains("@")){
            EmailAccountText.setText(Email.substring(0, Email.indexOf("@")));
            EmailAddressText.setText(Email.substring(Email.indexOf("@")+1));
        }else{
            EmailAccountText.setText("");
            EmailAddressText.setText("");
        }
    }
    private void initialComponent(){
        ManufacturerIDText.setText("");
        ManufacturerNameText.setText("");
        ManufacturerNickNameText.setText("");
        PersonInChargeText.setText("");
        ContactPersonText.setText("");
        Telephone1TitleText.setText("");
        Telephone1NumberText.setText("");
        Telephone1TransferText.setText("");
        Telephone2TitleText.setText("");
        Telephone2NumberText.setText("");
        Telephone2TransferText.setText("");
        CellPhoneTitleText.setText("");
        CellPhoneNumberText.setText("");
        FaxTitleText.setText("");
        FaxNumberText.setText("");
        EmailAccountText.setText("");
        EmailAddressText.setText("");
        CompanyAddressText.setText("");
        DeliveryAddressText.setText("");
        InvoiceTitleText.setText("");
        TaxIDNumberText.setText("");
        InvoiceAddressText.setText("");
        OrderTax_ComboBox.getSelectionModel().select(OrderTax.未稅);
        defaultPaymentMethod_ComboBox.getSelectionModel().select(DefaultPaymentMethod.現金);
        ComponentToolKit.setSpinnerDoubleValue(PayableDiscountSpinner,1.00);
        RemarkText.setText("");
        PayableDayText.setText("25");

//        扣匯費 銀行  分行  戶名  帳號  支票抬頭  月結幾天  扣郵資  分期付款
        DiscountRemittanceFee_CheckBox.setSelected(false);
        RemittanceFeeText.setText("0");

        AllBank_ComboBox.getSelectionModel().selectFirst();
        BankBranchText.setText("");
        AccountNameText.setText("");
        BankAccountText.setText("");
        CheckTitleText.setText("");
        NoneCheckoutByMonth_RadioButton.setSelected(true);
        ComponentToolKit.setTextFieldDisable(CheckDueDayText,NoneCheckoutByMonth_RadioButton.isSelected());
        CheckDueDayText.setText("0");
        DiscountPostage_CheckBox.setSelected(false);
        PostageText.setText("0");


        DeliveryAddress_SameAsCompanyAddress_CheckBox.setSelected(false);
        InvoiceAddress_SameAsCompanyAddress_CheckBox.setSelected(false);
        InvoiceAddress_SameAsDeliveryAddress_CheckBox.setSelected(false);



        ComponentToolKit.setButtonDisable(ModifyManufacturer_Button,true);
        DeleteSlider.setValue(0);
        ComponentToolKit.setButtonDisable(DeleteManufacturer_Button,true);
    }
}
