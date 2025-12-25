package ERP.Controller.ManageShipmentSystem.ManageCustomerInfo;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ObjectIDCharacter;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.ManageCustomerInfo.ManageCustomerInfo_Model;
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

/** [Controller] Manage customer info */
public class ManageCustomerInfo_Controller {
    @FXML RadioButton CheckoutByMonth_RadioButton,NoneCheckoutByMonth_RadioButton;
    @FXML TextField CustomerIDText, CustomerNameText, CustomerNickNameText, PersonInChargeText;
    @FXML TextField ContactPersonText, Telephone1TitleText, Telephone1NumberText, Telephone1TransferText, Telephone2TitleText, Telephone2NumberText, Telephone2TransferText, CellPhoneTitleText, CellPhoneNumberText, FaxTitleText, FaxNumberText, EmailAccountText, EmailAddressText, MemberIDText;
    @FXML TextField CompanyAddressText, DeliveryAddressText, InvoiceTitleText, TaxIDNumberText, InvoiceAddressText, ReceivableDayText, StoreCodeText;
    @FXML TextArea RemarkText;
    @FXML CheckBox DeliveryAddress_SameAsCompanyAddress_CheckBox, InvoiceAddress_SameAsCompanyAddress_CheckBox, InvoiceAddress_SameAsDeliveryAddress_CheckBox;
    @FXML Spinner<Double> ReceivableDiscountSpinner, SaleDiscountSpinner;
    @FXML ComboBox<SystemSetting_Enum.ObjectIDCharacter> CustomerAreaTitle_ComboBox;
    @FXML ComboBox<ObjectInfo_Enum.OrderTax> OrderTax_ComboBox;
    @FXML ComboBox<ObjectInfo_Enum.CustomerSaleModel> SaleMode_ComboBox;
    @FXML ComboBox<ObjectInfo_Enum.ShipmentInvoicePrintPricing> PrintPricing_ComboBox;
    @FXML ComboBox<ObjectInfo_Bean> StartCustomerID_ComboBox, EndCustomerID_ComboBox;
    @FXML TextField SearchCustomerNameText;
    @FXML Button SearchCustomer_Button, PrintCustomer_Button, ModifyCustomer_Button, DeleteCustomer_Button;
    @FXML Slider DeleteSlider;
    @FXML TabPane tabPane;
    @FXML Tab InsertCustomerTab, SearchCustomerTab;
    @FXML TableView<ObjectInfo_Bean> TableView;
    @FXML TableColumn<ObjectInfo_Bean,String> CustomerIDColumn, CustomerNameColumn, CustomerNickNameColumn, ContactPersonColumn, Telephone1Column, Telephone2Column, FaxColumn, CompanyAddressColumn;

    private ToolKit ToolKit;
    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private CallFXML CallFXML;
    private String StartCustomerID = "", EndCustomerID = "";

    private ManageCustomerInfo_Model ManageCustomerInfo_Model;
    private SystemSetting_Model SystemSetting_Model;
    private ObservableList<ObjectInfo_Bean> CustomerList;
    private Order_Model Order_Model;
    private ObjectInfo_Bean ObjectInfo_Bean;
    private SystemSettingConfig_Bean SystemSettingConfig_Bean;
    private Stage Stage;

    public ManageCustomerInfo_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.CallFXML = ToolKit.CallFXML;
        this.ManageCustomerInfo_Model = ToolKit.ModelToolKit.getManageCustomerInfoModel();
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setStage(Stage Stage){ this.Stage = Stage; }
    /** Set component of manage customer info */
    public void setComponent(ObjectInfo_Bean objectInfo_bean){
        initialTableView();
        setKeyWordTextLimiter();
        setTextFieldLimitDigital();
        setSpinnerLimitDouble();

        this.SystemSettingConfig_Bean = SystemSetting_Model.loadAllSystemSettingData();
        CustomerAreaTitle_ComboBox.getItems().addAll(SystemSetting_Enum.ObjectIDCharacter.values());
        String CustomerIDCharacter = SystemSettingConfig_Bean.getCustomerIDCharacter();
        for(int index = 0 ; index < SystemSetting_Enum.ObjectIDCharacter.values().length ; index++){
            if(SystemSetting_Enum.ObjectIDCharacter.values()[index].Character().equals(CustomerIDCharacter)) {
                CustomerAreaTitle_ComboBox.getSelectionModel().select(SystemSetting_Enum.ObjectIDCharacter.values()[index]);
                break;
            }
        }
        setCustomerIDText();
        OrderTax_ComboBox.getItems().addAll(ObjectInfo_Enum.OrderTax.values());
        OrderTax_ComboBox.getSelectionModel().select(ObjectInfo_Enum.OrderTax.未稅);
        PrintPricing_ComboBox.getItems().addAll(ObjectInfo_Enum.ShipmentInvoicePrintPricing.values());
        PrintPricing_ComboBox.getSelectionModel().select(ObjectInfo_Enum.ShipmentInvoicePrintPricing.Y);
        SaleMode_ComboBox.getItems().addAll(ObjectInfo_Enum.CustomerSaleModel.values());
        SaleMode_ComboBox.getSelectionModel().select(ObjectInfo_Enum.CustomerSaleModel.定價);
        ComponentToolKit.setDoubleSpinnerValueFactory(ReceivableDiscountSpinner,0.01, 1, 1, 0.01);
        ComponentToolKit.setDoubleSpinnerValueFactory(SaleDiscountSpinner,0.01, 1, 1, 0.01);
        ReceivableDayText.setText("25");
        if(objectInfo_bean != null){
            tabPane.getSelectionModel().select(SearchCustomerTab);
            this.ObjectInfo_Bean = objectInfo_bean;
            TableView.getItems().add(this.ObjectInfo_Bean);
            TableView.getSelectionModel().select(this.ObjectInfo_Bean);
            ComponentToolKit.setButtonDisable(ModifyCustomer_Button,false);
            setCustomerInfo();
        }else{
            this.ObjectInfo_Bean = new ObjectInfo_Bean();
        }
    }
    private void setKeyWordTextLimiter(){
        ComponentToolKit.addTextFieldLimitDigital(CustomerIDText,false);
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
        ComponentToolKit.addTextFieldLimitDigital(MemberIDText,false);
        ComponentToolKit.addTextFieldLimitDigital(TaxIDNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(ReceivableDayText,false);
    }
    private void setSpinnerLimitDouble(){
        ComponentToolKit.addSpinnerLimitDouble(ReceivableDiscountSpinner,1);
    }
    private void setCustomerIDText(){
        if(SystemSettingConfig_Bean.getCustomerIDLength() == null || SystemSettingConfig_Bean.getCustomerIDAreaLength() == null ||
                !ToolKit.isDigital(SystemSettingConfig_Bean.getCustomerIDLength()) || !ToolKit.isDigital(SystemSettingConfig_Bean.getCustomerIDAreaLength())){
            setCustomerIDEditable(false);
            DialogUI.AlarmDialog("※ 未設定編號位數");
        }else {
            setCustomerIDEditable(true);
            setCustomerSerialCode(ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox));
        }
    }
    private void setCustomerIDEditable(boolean editable){
        ComponentToolKit.setTextFieldEditable(CustomerIDText,editable);
        ComponentToolKit.setComboBoxDisable(CustomerAreaTitle_ComboBox,!editable);
        if(editable)
            ComponentToolKit.setTextFieldStyle(CustomerIDText,"");
        else
            ComponentToolKit.setTextFieldStyle(CustomerIDText,"-fx-background-color:#bebebe");
    }
    @FXML protected void CustomerAreaOnAction(){
        CustomerIDText.setText("");
        setCustomerSerialCode(ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox));
    }
    @FXML protected void CustomerIDKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isF6KeyPressed(KeyEvent)){
            ObjectIDCharacter ObjectIDCharacter = ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox);
            setCustomerSerialCode(ObjectIDCharacter);
            ObservableList<ObjectInfo_Bean> customerList;
            if(ObjectIDCharacter == SystemSetting_Enum.ObjectIDCharacter.無) {
                String customerIDTitle = CustomerIDText.getText();
                customerList = Order_Model.getObjectFromSearchColumn(OrderObject.客戶, Order_Enum.ObjectSearchColumn.客戶編號, customerIDTitle.substring(0,1));
            }else
                customerList = Order_Model.getObjectFromSearchColumn(OrderObject.客戶, Order_Enum.ObjectSearchColumn.客戶編號, ObjectIDCharacter.Character());
            CallFXML.ShowObject(Stage, OrderObject.客戶, customerList,false, Order_Enum.ShowObjectSource.建立廠商或客戶,null);
        }
    }
    private void setCustomerSerialCode(ObjectIDCharacter ObjectIDCharacter){
        String newCustomerID;
        String customerIDTitle = CustomerIDText.getText();
        if(ObjectIDCharacter == SystemSetting_Enum.ObjectIDCharacter.無 && !customerIDTitle.equals(""))
            newCustomerID = ManageCustomerInfo_Model.getNewestCustomerID(customerIDTitle);
        else
            newCustomerID = ManageCustomerInfo_Model.getNewestCustomerID(ObjectIDCharacter.Character());
        if(newCustomerID == null){
            CustomerIDText.setText("");
            DialogUI.MessageDialog("※ 客戶流水編號已達上限!");
        }else {
            CustomerIDText.setText(newCustomerID);
        }
    }
    /** TextField Key Pressed - 客戶名稱 */
    @FXML protected void CustomerNameKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   CustomerNickNameText.requestFocus();    }
    /** TextField Key Pressed - 客戶簡稱 */
    @FXML protected void CustomerNickNameKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   PersonInChargeText.requestFocus();    }
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
    /** TextField Key Pressed - 行斷電話 開頭 */
    @FXML protected void CellphoneTitleKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(CellPhoneTitleText.getText().equals("")) FaxTitleText.requestFocus();
            else    CellPhoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 行斷電話 開頭 */
    @FXML protected void CellphoneTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String CellphoneTitle = CellPhoneTitleText.getText();
            if(CellphoneTitle.length() >= 4)    CellPhoneNumberText.requestFocus();
        }
    }
    /** TextField Key Released - 行斷電話 號碼 */
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
    @FXML protected void EmailAccountKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            if(EmailAccountText.getText().equals(""))   MemberIDText.requestFocus();
            else    EmailAddressText.requestFocus();
        }
    }
    /** TextField Key Pressed - 信箱地址 */
    @FXML protected void EmailAddressKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   MemberIDText.requestFocus();  }
    /** TextField Key Pressed - 會員編號 */
    @FXML protected void MemberIDKeyPressed(KeyEvent KeyEvent){    if(KeyPressed.isEnterKeyPressed(KeyEvent))   CompanyAddressText.requestFocus();    }
    /** TextField Key Pressed - 公司地址 */
    @FXML protected void CompanyAddressKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   DeliveryAddressText.requestFocus();    }
    /** TextField Key Pressed - 送貨地址 */
    @FXML protected void DeliveryAddressKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))   InvoiceTitleText.requestFocus();    }
    /** TextField Key Pressed - 發票抬頭 */
    @FXML protected void InvoiceTitleKeyPressed(KeyEvent KeyEvent){ if(KeyPressed.isEnterKeyPressed(KeyEvent))   TaxIDNumberText.requestFocus();    }
    /** TextField Key Pressed - 統一編號 */
    @FXML protected void TaxIDNumberKeyPressed(KeyEvent KeyEvent){    if(KeyPressed.isEnterKeyPressed(KeyEvent))   InvoiceAddressText.requestFocus();    }
    /** TextField Key Pressed - 發票地址 */
    @FXML protected void InvoiceAddressKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   RemarkText.requestFocus();    }
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
    /** ComboBox On Action - 出售模式 */
    @FXML protected void SaleModelOnAction(){
        if(SaleMode_ComboBox.getSelectionModel().getSelectedItem() == this.ObjectInfo_Bean.getSaleModel())
            SaleDiscountSpinner.getValueFactory().setValue(this.ObjectInfo_Bean.getSaleDiscount());
        else    SaleDiscountSpinner.getValueFactory().setValue(1.00);
    }
    /** TextField Key Pressed - 備註 */
    @FXML protected void RemarkKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   ReceivableDayText.requestFocus();    }
    /** TextField Key Pressed - 收款日期 */
    @FXML protected void ReceivableDayKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   StoreCodeText.requestFocus();    }
    /** TextField Key Released - 收款日期 */
    @FXML protected void ReceivableDayKeyReleased(){
        String ReceivableDay = ReceivableDayText.getText();
        if(!ReceivableDay.equals("") && Integer.parseInt(ReceivableDay) >= 31)
            ReceivableDayText.setText("");
    }
    /** TextField Key Pressed - 門市碼 */
    @FXML protected void StoreCodeKeyPressed(KeyEvent KeyEvent){   if(KeyPressed.isEnterKeyPressed(KeyEvent))   CustomerNameText.requestFocus();    }
    /** Tab On Selection Changed - 客戶資料速查 */
    @FXML protected void SearchCustomerTabOnSelectionChanged(){
        initialComponent();
        if(SearchCustomerTab.isSelected()){
            setCustomerIDEditable(false);
            this.ObjectInfo_Bean = null;
            StartCustomerID_ComboBox.getItems().clear();
            EndCustomerID_ComboBox.getItems().clear();
            TableView.getItems().clear();
            CustomerList = Order_Model.getObjectFromSearchColumn(OrderObject.客戶, null, null);
            StartCustomerID_ComboBox.getItems().addAll(CustomerList);
            StartCustomerID_ComboBox.getSelectionModel().selectFirst();
            ComponentToolKit.setObjectInfoBeanComboBoxObj(StartCustomerID_ComboBox);
            EndCustomerID_ComboBox.getItems().addAll(CustomerList);

            StartCustomerIDOnAction();
            ComponentToolKit.setObjectInfoBeanComboBoxObj(EndCustomerID_ComboBox);

            SearchCustomerNameText.setText("");
            if(CustomerList.size() == 0){
                ComponentToolKit.setButtonDisable(SearchCustomer_Button,true);
                ComponentToolKit.setButtonDisable(PrintCustomer_Button,true);
            }
        }else{
            this.ObjectInfo_Bean = new ObjectInfo_Bean();
            CustomerAreaTitle_ComboBox.getSelectionModel().selectFirst();
            setCustomerIDText();
        }
    }
    /** Button Mouse Clicked - 新增 */
    @FXML protected void InsertCustomerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(CustomerIDText.getText() == null)    DialogUI.MessageDialog("※ 客戶 編號已達上限!");
            else if(CustomerIDText.getText().equals(""))    DialogUI.MessageDialog("※ 客戶 編號錯誤!");
            else if((ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox) == ObjectIDCharacter.無 && CustomerIDText.getText().length() != Integer.parseInt(SystemSettingConfig_Bean.getCustomerIDLength())) ||
                    (ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox) != ObjectIDCharacter.無 && CustomerIDText.getText().length() != Integer.parseInt(SystemSettingConfig_Bean.getCustomerIDAreaLength())))
                DialogUI.MessageDialog("※ 客戶 編號長度錯誤!");
            else if(ManageCustomerInfo_Model.isCustomerIDExist(CustomerIDText.getText(),false))   DialogUI.MessageDialog("※ 客戶 編號重複!");
            else if(ReceivableDayText.getText().equals(""))   DialogUI.MessageDialog("※ 請輸入收款日期");
            else if(CustomerIDText.getText() == null)   DialogUI.MessageDialog("※ 客戶 流水編號已達上限!");
            else if(CustomerIDText.getText().equals(""))    DialogUI.MessageDialog("※ 客戶 編號錯誤!");
            else if(CustomerNameText.getText().equals(""))  DialogUI.MessageDialog("※ 請輸入客戶名稱");
            else if(isColumnFormatCorrect() && DialogUI.ConfirmDialog("是否新增 ?",true,false,0,0)){
                recordCustomerInfo();
                if(ManageCustomerInfo_Model.insertCustomer(this.ObjectInfo_Bean,false)){
                    DialogUI.MessageDialog("※ 新增客戶成功");
                    initialComponent();
                    setCustomerSerialCode(ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox));
                }else    DialogUI.MessageDialog("※ 新增客戶失敗");
            }
        }
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void ModifyCustomerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(ReceivableDayText.getText().equals(""))   DialogUI.MessageDialog("※ 請輸入收款日期");
            else if(CustomerIDText.getText() == null)   DialogUI.MessageDialog("※ 客戶 流水編號已達上限!");
            else if(CustomerIDText.getText().equals(""))    DialogUI.MessageDialog("※ 客戶 編號錯誤!");
            else if(CustomerNameText.getText().equals(""))  DialogUI.MessageDialog("※ 請輸入客戶名稱");
            else if(isColumnFormatCorrect() && DialogUI.ConfirmDialog("是否修改 ?",true,false,0,0)){
                recordCustomerInfo();
                if(ManageCustomerInfo_Model.modifyCustomer(this.ObjectInfo_Bean))
                    DialogUI.MessageDialog("※ 修改客戶成功");
                else
                    DialogUI.MessageDialog("※ 修改客戶失敗");
            }
        }
    }
    private void recordCustomerInfo(){
        this.ObjectInfo_Bean.setObjectID(ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox).Character() + CustomerIDText.getText());
        this.ObjectInfo_Bean.setObjectName(CustomerNameText.getText());
        this.ObjectInfo_Bean.setObjectNickName(CustomerNickNameText.getText());
        this.ObjectInfo_Bean.setPersonInCharge(PersonInChargeText.getText());
        this.ObjectInfo_Bean.setContactPerson(ContactPersonText.getText());
        if(Telephone1TitleText.getText().equals(""))  this.ObjectInfo_Bean.setTelephone1("");
        else if(Telephone1TransferText.getText().equals(""))    this.ObjectInfo_Bean.setTelephone1(Telephone1TitleText.getText() + "-" + Telephone1NumberText.getText());
        else    this.ObjectInfo_Bean.setTelephone1(Telephone1TitleText.getText() + "-" + Telephone1NumberText.getText() + "#" + Telephone1TransferText.getText());
        if(Telephone2TitleText.getText().equals(""))  this.ObjectInfo_Bean.setTelephone2("");
        else if(Telephone2TransferText.getText().equals(""))    this.ObjectInfo_Bean.setTelephone2(Telephone2TitleText.getText() + "-" + Telephone2NumberText.getText());
        else    this.ObjectInfo_Bean.setTelephone2(Telephone2TitleText.getText() + "-" + Telephone2NumberText.getText() + "#" + Telephone2TransferText.getText());
        if(CellPhoneTitleText.getText().equals(""))   this.ObjectInfo_Bean.setCellPhone("");
        else    this.ObjectInfo_Bean.setCellPhone(CellPhoneTitleText.getText() + "-" + CellPhoneNumberText.getText());
        if(FaxTitleText.getText().equals(""))   this.ObjectInfo_Bean.setFax("");
        else    this.ObjectInfo_Bean.setFax(FaxTitleText.getText() + "-" + FaxNumberText.getText());
        if(EmailAccountText.getText().equals("")) this.ObjectInfo_Bean.setEmail("");
        else    this.ObjectInfo_Bean.setEmail(EmailAccountText.getText() + "@" + EmailAddressText.getText());
        this.ObjectInfo_Bean.setMemberID(MemberIDText.getText());
        this.ObjectInfo_Bean.setCompanyAddress(CompanyAddressText.getText());
        this.ObjectInfo_Bean.setDeliveryAddress(DeliveryAddressText.getText());
        this.ObjectInfo_Bean.setInvoiceTitle(InvoiceTitleText.getText());
        this.ObjectInfo_Bean.setTaxIDNumber(TaxIDNumberText.getText());
        this.ObjectInfo_Bean.setInvoiceAddress(InvoiceAddressText.getText());

        this.ObjectInfo_Bean.setOrderTax(OrderTax_ComboBox.getSelectionModel().getSelectedItem());
        this.ObjectInfo_Bean.setPayableReceivableDiscount(ReceivableDiscountSpinner.getValue());
        this.ObjectInfo_Bean.setPrintPricing(PrintPricing_ComboBox.getSelectionModel().getSelectedItem());
        this.ObjectInfo_Bean.setSaleModel(SaleMode_ComboBox.getSelectionModel().getSelectedItem());
        this.ObjectInfo_Bean.setSaleDiscount(SaleDiscountSpinner.getValue());
        this.ObjectInfo_Bean.setRemark(RemarkText.getText());
        this.ObjectInfo_Bean.setCheckoutByMonth(CheckoutByMonth_RadioButton.isSelected());
        this.ObjectInfo_Bean.setReceivableDay(Integer.parseInt(ReceivableDayText.getText()));
        this.ObjectInfo_Bean.setStoreCode(StoreCodeText.getText());
    }
    private boolean isColumnFormatCorrect(){
        boolean correctFormat = true;
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
            correctFormat = false;
        }else if((!Telephone2Title.equals("") && (Telephone2Title.length() != 2 || Telephone2Number.length() < 7)) || (Telephone2Title.equals("") && (Telephone2Number.length() != 0 || Telephone2Transfer.length() != 0))) {
            DialogUI.MessageDialog("※ 電話(二) 格式錯誤");
            correctFormat = false;
        }else if((!CellphoneTitle.equals("") && (CellphoneTitle.length() != 4 || CellphoneNumber.length() != 6)) || (CellphoneTitle.equals("") && CellphoneNumber.length() != 0)) {
            DialogUI.MessageDialog("※ 行動電話 格式錯誤");
            correctFormat = false;
        }else if((!FaxTitle.equals("") && (FaxTitle.length() != 2 || FaxNumber.length() < 7)) || (FaxTitle.equals("") && FaxNumber.length() != 0)) {
            DialogUI.MessageDialog("※ 傳真 格式錯誤");
            correctFormat = false;
        }else if((!EmailAccount.equals("") && EmailAddress.equals("")) || (EmailAccount.equals("") && !EmailAddress.equals(""))) {
            DialogUI.MessageDialog("※ 信箱 格式錯誤");
            correctFormat = false;
        }
        return correctFormat;
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void DeleteCustomerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(this.ObjectInfo_Bean == null)
                DialogUI.MessageDialog("※ 請選擇客戶");
            else{
                if(ManageCustomerInfo_Model.isCustomerExistOrder(this.ObjectInfo_Bean.getObjectID()))    DialogUI.AlarmDialog("※ 此客戶存在報價(出貨)單!");
                else{
                    if(DialogUI.ConfirmDialog("確定刪除 ?",true,false,0,0)){
                        if(ManageCustomerInfo_Model.deleteCustomer(this.ObjectInfo_Bean.getObjectID())){
                            DialogUI.MessageDialog("※ 刪除客戶成功");
                            TableView.getItems().remove(this.ObjectInfo_Bean);
                            StartCustomerID_ComboBox.getItems().remove(this.ObjectInfo_Bean);
                            EndCustomerID_ComboBox.getItems().remove(this.ObjectInfo_Bean);
                            StartCustomerID_ComboBox.getSelectionModel().selectFirst();
                            EndCustomerID_ComboBox.getSelectionModel().selectFirst();
                            initialComponent();
                        }else    DialogUI.MessageDialog("※ 刪除客戶失敗");
                    }
                }
            }
        }
    }
    /** Slider Mouse Released - 刪除的滑軌 */
    @FXML protected void DeleteSliderClickMouseReleased(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            double SliderValue = DeleteSlider.getValue();
            if (SliderValue == 1) ComponentToolKit.setButtonDisable(DeleteCustomer_Button, false);
            else ComponentToolKit.setButtonDisable(DeleteCustomer_Button, true);
        }
    }
    /** Button Mouse Clicked - 清空 */
    @FXML protected void ClearComponentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && DialogUI.ConfirmDialog("確定清空 ?",true,false,0,0)) {
            initialComponent();
            setCustomerSerialCode(ComponentToolKit.getCustomerAreaTitleComboBoxSelectItem(CustomerAreaTitle_ComboBox));
        }
    }
    /** ComboBox On Action - 起始客戶 */
    @FXML protected void StartCustomerIDOnAction(){
        SearchCustomerNameText.setText("");
        ObjectInfo_Bean selectObjectInfo_Bean = ComponentToolKit.getObjectIDComboBoxSelectItem(StartCustomerID_ComboBox);
        if(selectObjectInfo_Bean == null)
            return;

        String startObjectIDTitle = selectObjectInfo_Bean.getObjectID().substring(0,1);
        ObservableList<ObjectInfo_Bean> ObjectList = ComponentToolKit.getObjectIDComboBoxItems(StartCustomerID_ComboBox);

        boolean record = false;
        for(int index = 0 ; index < ObjectList.size() ; index++){
            ObjectInfo_Bean ObjectInfo_Bean = ObjectList.get(index);
            if(!record && ObjectInfo_Bean.getObjectID().substring(0,1).equals(startObjectIDTitle)) {
                record = true;
                if(index == ObjectList.size()-1) {
                    EndCustomerID_ComboBox.getSelectionModel().select(index);
                    break;
                }
            }else if(record && !ObjectInfo_Bean.getObjectID().substring(0,1).equals(startObjectIDTitle)) {
                EndCustomerID_ComboBox.getSelectionModel().select(index-1);
                break;
            }else if(index == ObjectList.size()-1) {
                EndCustomerID_ComboBox.getSelectionModel().select(index);
                break;
            }
        }
    }
    /** ComboBox Mouse Clicked - 起始客戶 */
    @FXML protected void StartCustomerIDMouseClicked(MouseEvent MouseEvent){    if(KeyPressed.isMouseLeftClicked(MouseEvent))    StartCustomerID = "";  }
    /** ComboBox Key Released - 起始客戶 */
    @FXML protected void StartCustomerIDKeyReleased(KeyEvent keyEvent){
        String startCustomerID = objectIDComboBoxKeyReleased(keyEvent, StartCustomerID_ComboBox, StartCustomerID);
        if(startCustomerID != null){
            this.StartCustomerID = startCustomerID;
        }
    }
    @FXML protected void StartCustomerIDOnShowing(){
        ListView<ObjectInfo_Bean> list = (ListView)((ComboBoxListViewSkin<String>) StartCustomerID_ComboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, StartCustomerID_ComboBox.getSelectionModel().getSelectedIndex()));
    }

    /** ComboBox Mouse Clicked - 結束客戶 */
    @FXML protected void EndCustomerIDMouseClicked(MouseEvent MouseEvent){  if(KeyPressed.isMouseLeftClicked(MouseEvent))    EndCustomerID = "";    }
    /** ComboBox Key Released - 結束客戶 */
    @FXML protected void EndCustomerIDKeyReleased(KeyEvent keyEvent){
        String endCustomerID = objectIDComboBoxKeyReleased(keyEvent, EndCustomerID_ComboBox, EndCustomerID);
        if(endCustomerID != null){
            this.EndCustomerID = endCustomerID;
        }
    }
    @FXML protected void EndCustomerIDOnShowing(){
        ListView<ObjectInfo_Bean> list = (ListView) ((ComboBoxListViewSkin) EndCustomerID_ComboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, EndCustomerID_ComboBox.getSelectionModel().getSelectedIndex()));
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
    /** Button Mouse Clicked - 送出 */
    @FXML protected void SearchCustomerIDMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            initialComponent();
            CustomerIDText.setText("");
            TableView.getItems().clear();
            int startObjectIndex = ComponentToolKit.getObjectIDComboBoxSelectIndex(StartCustomerID_ComboBox);
            int endObjectIndex = ComponentToolKit.getObjectIDComboBoxSelectIndex(EndCustomerID_ComboBox);
            if(endObjectIndex < startObjectIndex)
                DialogUI.MessageDialog("※ 客戶查詢區間錯誤");
            else{
                String StartCustomerID = ComponentToolKit.getObjectIDComboBoxSelectItem(StartCustomerID_ComboBox).getObjectID();
                String EndCustomerID = ComponentToolKit.getObjectIDComboBoxSelectItem(EndCustomerID_ComboBox).getObjectID();
                boolean addCustomer = false;
                for(ObjectInfo_Bean ObjectInfo_Bean : CustomerList){
                    if(ObjectInfo_Bean.getObjectID().equals(StartCustomerID))   addCustomer = true;
                    if(addCustomer) TableView.getItems().add(ObjectInfo_Bean);
                    if(ObjectInfo_Bean.getObjectID().equals(EndCustomerID)) break;
                }
            }
        }
    }
    /** Button Mouse Clicked - 查詢 */
    @FXML protected void SearchCustomerNameMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            initialComponent();
            TableView.getItems().clear();
            String SearchCustomerName = SearchCustomerNameText.getText();
            if(SearchCustomerName.equals("")) TableView.getItems().addAll(CustomerList);
            else    for(ObjectInfo_Bean ObjectInfo_Bean : CustomerList) if(ObjectInfo_Bean.getObjectName().contains(SearchCustomerName))    TableView.getItems().addAll(ObjectInfo_Bean);
            if(ComponentToolKit.getTableViewItemsSize(TableView) == 0) DialogUI.MessageDialog("※ 未找到相關客戶");
        }
    }
    /** TextField Key Pressed - 查詢 */
    @FXML protected void SearchCustomerNameKeyPressed(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            TableView.getItems().clear();
            String SearchCustomerName = SearchCustomerNameText.getText();
            if(SearchCustomerName.equals("")) TableView.getItems().addAll(CustomerList);
            else{
                for(ObjectInfo_Bean ObjectInfo_Bean : CustomerList) {
                    if (ObjectInfo_Bean.getObjectName().contains(SearchCustomerName) || ObjectInfo_Bean.getObjectNickName().contains(SearchCustomerName)
                            || ObjectInfo_Bean.getContactPerson().contains(SearchCustomerName))
                        TableView.getItems().addAll(ObjectInfo_Bean);
                }
            }
            if(ComponentToolKit.getTableViewItemsSize(TableView) == 0) DialogUI.MessageDialog("※ 未找到相關客戶");
        }
    }
    /** Button Mouse Clicked - 列印 */
    @FXML protected void PrintCustomerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            ObservableList<ObjectInfo_Bean> CustomerList = TableView.getItems();
            ObjectInfo_Bean StartObjectInfo_Bean = ComponentToolKit.getObjectIDComboBoxSelectItem(StartCustomerID_ComboBox);
            ObjectInfo_Bean EndObjectInfo_Bean = ComponentToolKit.getObjectIDComboBoxSelectItem(EndCustomerID_ComboBox);
            String StartCustomer = StartObjectInfo_Bean.getObjectID() + "  " + StartObjectInfo_Bean.getObjectNickName();
            String EndCustomer = EndObjectInfo_Bean.getObjectID() + "  " + EndObjectInfo_Bean.getObjectNickName();
            if (CustomerList.size() != 0)
                ManageCustomerInfo_Model.printCustomerInfo(CustomerList, StartCustomer, EndCustomer);
            else DialogUI.MessageDialog("無商品類別");
        }
    }
    @FXML protected void TableViewKeyReleased(KeyEvent KeyEvent){
        this.ObjectInfo_Bean = ComponentToolKit.getObjectTableViewSelectItem(TableView);
        DeleteSlider.setValue(0);
        ComponentToolKit.setButtonDisable(DeleteCustomer_Button,true);
        if((KeyPressed.isUpKeyPressed(KeyEvent) || KeyPressed.isDownKeyPressed(KeyEvent)) && this.ObjectInfo_Bean != null){
            ComponentToolKit.setButtonDisable(ModifyCustomer_Button,false);
            setCustomerInfo();
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        this.ObjectInfo_Bean = ComponentToolKit.getObjectTableViewSelectItem(TableView);
        DeleteSlider.setValue(0);
        ComponentToolKit.setButtonDisable(DeleteCustomer_Button,true);
        if(KeyPressed.isMouseLeftClicked(MouseEvent) && ComponentToolKit.getTableViewItemsSize(TableView) != 0 && this.ObjectInfo_Bean != null){
            ComponentToolKit.setButtonDisable(ModifyCustomer_Button,false);
            setCustomerInfo();
        }
    }

    private void setCustomerInfo(){
        String CustomerID = this.ObjectInfo_Bean.getObjectID();
        if(ToolKit.isDigital(CustomerID)){
            CustomerAreaTitle_ComboBox.getSelectionModel().selectFirst();
            CustomerIDText.setText(CustomerID);
        }else{
            for(SystemSetting_Enum.ObjectIDCharacter CustomerAreaTitle : SystemSetting_Enum.ObjectIDCharacter.values())
                if(CustomerAreaTitle.Character().equals(CustomerID.substring(0,1))) CustomerAreaTitle_ComboBox.getSelectionModel().select(CustomerAreaTitle);
            CustomerIDText.setText(CustomerID.substring(1));
        }
        CustomerNameText.setText(this.ObjectInfo_Bean.getObjectName());
        CustomerNickNameText.setText(this.ObjectInfo_Bean.getObjectNickName());
        PersonInChargeText.setText(this.ObjectInfo_Bean.getPersonInCharge());
        ContactPersonText.setText(this.ObjectInfo_Bean.getContactPerson());
        setPhoneText(Telephone1TitleText, Telephone1NumberText, Telephone1TransferText, this.ObjectInfo_Bean.getTelephone1());
        setPhoneText(Telephone2TitleText, Telephone2NumberText, Telephone2TransferText, this.ObjectInfo_Bean.getTelephone2());
        setPhoneText(CellPhoneTitleText, CellPhoneNumberText, null, this.ObjectInfo_Bean.getCellPhone());
        setPhoneText(FaxTitleText, FaxNumberText, null, this.ObjectInfo_Bean.getFax());
        setEmailText(this.ObjectInfo_Bean.getEmail());

        MemberIDText.setText(this.ObjectInfo_Bean.getMemberID());
        CompanyAddressText.setText(this.ObjectInfo_Bean.getCompanyAddress());
        DeliveryAddressText.setText(this.ObjectInfo_Bean.getDeliveryAddress());
        if(DeliveryAddressText.getText().equals(CompanyAddressText.getText()))  DeliveryAddress_SameAsCompanyAddress_CheckBox.setSelected(true);
        InvoiceTitleText.setText(this.ObjectInfo_Bean.getInvoiceTitle());
        TaxIDNumberText.setText(this.ObjectInfo_Bean.getTaxIDNumber());
        InvoiceAddressText.setText(this.ObjectInfo_Bean.getInvoiceAddress());
        if(InvoiceAddressText.getText().equals(CompanyAddressText.getText())) {
            InvoiceAddress_SameAsCompanyAddress_CheckBox.setSelected(true);
            InvoiceAddress_SameAsDeliveryAddress_CheckBox.setSelected(false);
        }else if(InvoiceAddressText.getText().equals(DeliveryAddressText.getText())) {
            InvoiceAddress_SameAsDeliveryAddress_CheckBox.setSelected(true);
            InvoiceAddress_SameAsCompanyAddress_CheckBox.setSelected(false);
        }
        OrderTax_ComboBox.getSelectionModel().select(this.ObjectInfo_Bean.getOrderTax());
        ReceivableDiscountSpinner.getValueFactory().setValue(this.ObjectInfo_Bean.getPayableReceivableDiscount());
        PrintPricing_ComboBox.getSelectionModel().select(this.ObjectInfo_Bean.getPrintPricing());
        SaleMode_ComboBox.getSelectionModel().select(this.ObjectInfo_Bean.getSaleModel());
        SaleDiscountSpinner.getValueFactory().setValue(this.ObjectInfo_Bean.getSaleDiscount());
        RemarkText.setText(this.ObjectInfo_Bean.getRemark());
        CheckoutByMonth_RadioButton.setSelected(this.ObjectInfo_Bean.isCheckoutByMonth());
        NoneCheckoutByMonth_RadioButton.setSelected(!this.ObjectInfo_Bean.isCheckoutByMonth());
        ReceivableDayText.setText(String.valueOf(this.ObjectInfo_Bean.getReceivableDay()));
        StoreCodeText.setText(this.ObjectInfo_Bean.getStoreCode());
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
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(CustomerIDColumn,"ObjectID","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(CustomerNameColumn,"ObjectName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(CustomerNickNameColumn,"ObjectNickName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ContactPersonColumn,"ContactPerson","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(Telephone1Column,"Telephone1","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(Telephone2Column,"Telephone2","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(FaxColumn,"Fax","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(CompanyAddressColumn,"CompanyAddress","CENTER-LEFT","16",null);
    }
    private void initialComponent(){
        CustomerAreaTitle_ComboBox.getSelectionModel().selectFirst();
        CustomerIDText.setText("");
        CustomerNameText.setText("");
        CustomerNickNameText.setText("");
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
        MemberIDText.setText("");
        CompanyAddressText.setText("");
        DeliveryAddressText.setText("");
        InvoiceTitleText.setText("");
        TaxIDNumberText.setText("");
        InvoiceAddressText.setText("");
        ReceivableDiscountSpinner.getValueFactory().setValue(1.00);
        SaleDiscountSpinner.getValueFactory().setValue(1.00);
        RemarkText.setText("");
        NoneCheckoutByMonth_RadioButton.setSelected(true);
        ReceivableDayText.setText("25");
        StoreCodeText.setText("");
        OrderTax_ComboBox.getSelectionModel().select(ObjectInfo_Enum.OrderTax.未稅);
        PrintPricing_ComboBox.getSelectionModel().select(ObjectInfo_Enum.ShipmentInvoicePrintPricing.Y);
        SaleMode_ComboBox.getSelectionModel().select(ObjectInfo_Enum.CustomerSaleModel.定價);
        DeliveryAddress_SameAsCompanyAddress_CheckBox.setSelected(false);
        InvoiceAddress_SameAsCompanyAddress_CheckBox.setSelected(false);
        InvoiceAddress_SameAsDeliveryAddress_CheckBox.setSelected(false);
        ComponentToolKit.setButtonDisable(ModifyCustomer_Button,true);
        DeleteSlider.setValue(0);
        ComponentToolKit.setButtonDisable(DeleteCustomer_Button,true);
    }

}
