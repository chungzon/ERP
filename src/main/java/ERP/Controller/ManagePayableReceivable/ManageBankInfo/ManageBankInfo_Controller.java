package ERP.Controller.ManagePayableReceivable.ManageBankInfo;

import ERP.Bean.ManagePayableReceivable.BankInfo_Bean;
import ERP.ERPApplication;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/** [Controller] Manage bank info */
public class ManageBankInfo_Controller {
    @FXML TextField BankIDText, BankCodeText, BankNickNameText, BankNameText, ContactPerson1Text, ContactPerson2Text, Telephone1TitleText, Telephone1NumberText, Telephone1TransferText, Telephone2TitleText, Telephone2NumberText, Telephone2TransferText, FaxTitleText, FaxNumberText,  AddressText;
    @FXML TextArea RemarkText;
    @FXML Tab InsertBankTab, SearchBankTab;
    @FXML Button ModifyBankInfo_Button, PrintBankInfo_Button;
    @FXML TableColumn<BankInfo_Bean,Integer> BankIDColumn;
    @FXML TableColumn<BankInfo_Bean,String> BankCodeColumn, BankNickNameColumn, BankNameColumn, ContactPerson1Column, ContactPerson2Column, Telephone1Column, Telephone2Column, FaxColumn, AddressColumn, RemarkColumn;
    @FXML TableView<BankInfo_Bean> TableView;

    private ToolKit ToolKit;
    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private BankInfo_Bean BankInfo_Bean;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    public ManageBankInfo_Controller(){
        this.ToolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = ToolKit.ComponentToolKit;
        this.KeyPressed = ToolKit.KeyPressed;
        this.ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
    }
    /** Set component of manage bank info */
    public void setComponent(){
        setKeyWordTextLimiter();
        initialTableView();
        initialComponent();
        BankInfo_Bean = new BankInfo_Bean(Integer.parseInt(BankIDText.getText()));
    }
    private void setKeyWordTextLimiter(){
        ComponentToolKit.addTextFieldLimitDigital(BankCodeText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone1TitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone1NumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone1TransferText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone2TitleText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone2NumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(Telephone2TransferText,false);
        ComponentToolKit.addTextFieldLimitDigital(FaxTitleText, false);
        ComponentToolKit.addTextFieldLimitDigital(FaxNumberText, false);
        ComponentToolKit.addKeyWordTextLimitLength(BankCodeText,3);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone1TitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone1NumberText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone1TransferText, 5);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone2TitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone2NumberText, 8);
        ComponentToolKit.addKeyWordTextLimitLength(Telephone2TransferText, 5);
        ComponentToolKit.addKeyWordTextLimitLength(FaxTitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(FaxNumberText, 8);
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(BankIDColumn,"BankID","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(BankCodeColumn,"BankCode","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(BankNickNameColumn,"BankNickName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(BankNameColumn,"BankName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ContactPerson1Column,"ContactPerson1","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(ContactPerson2Column,"ContactPerson2","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(Telephone1Column,"Telephone1","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(Telephone2Column,"Telephone2","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(FaxColumn,"Fax","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(AddressColumn,"Address","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(RemarkColumn,"Remark","CENTER-LEFT","16",null);
    }
    /** TextField Key Released - 電話1 開頭 */
    @FXML protected void Telephone1TitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String TelephoneTitle = Telephone1TitleText.getText();
            if(!ToolKit.isDigital(TelephoneTitle))     Telephone1TitleText.setText("");
            else if(TelephoneTitle.length() >= 2)    Telephone1NumberText.requestFocus();
        }
    }
    /** TextField Key Released - 電話1 號碼 */
    @FXML protected void Telephone1NumberKeyReleased(KeyEvent KeyEvent){
        String TelephoneNumber = Telephone1NumberText.getText();
        if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (TelephoneNumber.equals("")) Telephone1TitleText.requestFocus();
        }else if(KeyPressed.isDeleteKeyPressed(KeyEvent) && KeyPressed.isDirectionKeyPressed(KeyEvent)){
            if(!ToolKit.isDigital(TelephoneNumber))     Telephone1NumberText.setText("");
            else if(TelephoneNumber.length() >= 7)   Telephone1TransferText.requestFocus();
        }
    }
    /** TextField Key Released - 電話1 轉接 */
    @FXML protected void Telephone1TransferKeyReleased(KeyEvent KeyEvent){
        String TelephoneTransfer = Telephone1TransferText.getText();
        if(KeyPressed.isBackSpaceKeyPressed(KeyEvent)) {
            if (TelephoneTransfer.equals("")) Telephone1NumberText.requestFocus();
        }else if(KeyPressed.isDeleteKeyPressed(KeyEvent)){
            if(!ToolKit.isDigital(TelephoneTransfer))     Telephone1TransferText.setText("");
        }
    }
    /** TextField Key Released - 電話2 開頭 */
    @FXML protected void Telephone2TitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String TelephoneTitle = Telephone2TitleText.getText();
            if(!ToolKit.isDigital(TelephoneTitle))     Telephone2TitleText.setText("");
            else if(TelephoneTitle.length() >= 2)    Telephone2NumberText.requestFocus();
        }
    }
    /** TextField Key Released - 電話2 號碼 */
    @FXML protected void Telephone2NumberKeyReleased(KeyEvent KeyEvent){
        String TelephoneNumber = Telephone2NumberText.getText();
        if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (TelephoneNumber.equals("")) Telephone2TitleText.requestFocus();
        }else if(KeyPressed.isDeleteKeyPressed(KeyEvent) && KeyPressed.isDirectionKeyPressed(KeyEvent)){
            if(!ToolKit.isDigital(TelephoneNumber))     Telephone2NumberText.setText("");
            else if(TelephoneNumber.length() >= 7)   Telephone2TransferText.requestFocus();
        }
    }
    /** TextField Key Released - 電話2 轉接 */
    @FXML protected void Telephone2TransferKeyReleased(KeyEvent KeyEvent){
        String TelephoneTransfer = Telephone2TransferText.getText();
        if(KeyPressed.isBackSpaceKeyPressed(KeyEvent)) {
            if (TelephoneTransfer.equals("")) Telephone2NumberText.requestFocus();
        }else if(KeyPressed.isDeleteKeyPressed(KeyEvent)){
            if(!ToolKit.isDigital(TelephoneTransfer))     Telephone2TransferText.setText("");
        }
    }
    /** TextField Key Released - 傳真開頭 */
    @FXML protected void FaxTitleKeyReleased(KeyEvent KeyEvent){
        if(!KeyPressed.isBackSpaceKeyPressed(KeyEvent) && !KeyPressed.isDeleteKeyPressed(KeyEvent) && !KeyPressed.isDirectionKeyPressed(KeyEvent)){
            String FaxTitle = FaxTitleText.getText();
            if(!ToolKit.isDigital(FaxTitle))     FaxTitleText.setText("");
            else if(FaxTitle.length() >= 2)    FaxNumberText.requestFocus();
        }
    }
    /** TextField Key Pressed - 傳真號碼 */
    @FXML protected void FaxNumberKeyPressed(KeyEvent KeyEvent){  if(KeyPressed.isEnterKeyPressed(KeyEvent))   AddressText.requestFocus();    }
    /** TextField Key Released - 傳真號碼 */
    @FXML protected void FaxNumberKeyReleased(KeyEvent KeyEvent){
        String FaxNumber = FaxNumberText.getText();
        if (KeyPressed.isBackSpaceKeyPressed(KeyEvent)){
            if (FaxNumber.equals("")) FaxTitleText.requestFocus();
        }else if(KeyPressed.isDeleteKeyPressed(KeyEvent) && KeyPressed.isDirectionKeyPressed(KeyEvent)){
            if(!ToolKit.isDigital(FaxNumber))     FaxNumberText.setText("");
        }
    }
    /** Tab On Selection Changed - 銀行查詢 */
    @FXML protected void searchBankTabOnSelectionChanged(){
        if(SearchBankTab.isSelected()) {
            BankIDText.setText("");
            ComponentToolKit.setTextFieldEditable(BankCodeText,false);
            ComponentToolKit.setTextFieldStyle(BankCodeText,"-fx-background-color:#bebebe");
            TableView.setItems(ManagePayableReceivable_Model.getAllBankInfo(false));
        }else    initialComponent();
    }
    private void initialComponent(){
        BankIDText.setText(ToolKit.fillZero(ManagePayableReceivable_Model.getNewestBankID(),3));
        BankCodeText.setText("");
        ComponentToolKit.setTextFieldEditable(BankCodeText,true);
        ComponentToolKit.setTextFieldStyle(BankCodeText,"-fx-light-text-color:white");

        BankNickNameText.setText("");
        BankNameText.setText("");
        ContactPerson1Text.setText("");
        ContactPerson2Text.setText("");
        Telephone1TitleText.setText("");
        Telephone1NumberText.setText("");
        Telephone1TransferText.setText("");
        Telephone2TitleText.setText("");
        Telephone2NumberText.setText("");
        Telephone2TransferText.setText("");
        FaxTitleText.setText("");
        FaxNumberText.setText("");
        AddressText.setText("");
        RemarkText.setText("");
        ComponentToolKit.setButtonDisable(ModifyBankInfo_Button,true);
    }
    /** Button Mouse Clicked - 新增 */
    @FXML protected void insertBankInfoMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if (setBankInfo()) {
                if(!ManagePayableReceivable_Model.isExistBankInfo(BankInfo_Bean.getBankCode())){
                    if (ManagePayableReceivable_Model.insertBankInfo(BankInfo_Bean)) {
                        DialogUI.MessageDialog("※ 新增銀行成功");
                        initialComponent();
                    }else DialogUI.MessageDialog("※ 新增銀行失敗");
                }else   DialogUI.MessageDialog("※ 銀行代碼已存在");

            }
        }
    }
    /** Button Mouse Clicked - 清空 */
    @FXML protected void ClearComponentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    initialComponent();
    }
    /** Button Mouse Clicked - 匯入Excel */
    @FXML protected void imPortBankInfoMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            DialogUI.MessageDialog("※ 暫無功能");
        }
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void modifyBankInfoMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(setBankInfo()){
                if(ManagePayableReceivable_Model.modifyBankInfo(BankInfo_Bean)){
                    DialogUI.MessageDialog("※ 修改成功");
                    TableView.setItems(ManagePayableReceivable_Model.getAllBankInfo(false));
                }else    DialogUI.MessageDialog("※ 修改失敗");
            }
        }
    }
    /** Button Mouse Clicked - 列印 */
    @FXML protected void PrintBankInfo_Button(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            DialogUI.MessageDialog("※ 暫無功能");
        }
    }
    private boolean setBankInfo(){
        String Telephone1Title = Telephone1TitleText.getText();
        String Telephone1Number = Telephone1NumberText.getText();
        String Telephone1Transfer = Telephone1TransferText.getText();
        String Telephone2Title = Telephone2TitleText.getText();
        String Telephone2Number = Telephone2NumberText.getText();
        String Telephone2Transfer = Telephone2TransferText.getText();
        String FaxTitle = FaxTitleText.getText();
        String FaxNumber = FaxNumberText.getText();
        if((!Telephone1Title.equals("") && (Telephone1Title.length() != 2 || Telephone1Number.length() < 7)) || (Telephone1Title.equals("") && (Telephone1Number.length() != 0 || Telephone1Transfer.length() != 0)))  DialogUI.MessageDialog("※ 電話(一) 格式錯誤");
        else if((!Telephone2Title.equals("") && (Telephone2Title.length() != 2 || Telephone2Number.length() < 7)) || (Telephone2Title.equals("") && (Telephone2Number.length() != 0 || Telephone2Transfer.length() != 0)))   DialogUI.MessageDialog("※ 電話(二) 格式錯誤");
        else if((!FaxTitle.equals("") && (FaxTitle.length() != 2 || FaxNumber.length() < 7)) || (FaxTitle.equals("") && FaxNumber.length() != 0))    DialogUI.MessageDialog("※ 傳真 格式錯誤");
        else{
            BankInfo_Bean.setBankCode(BankCodeText.getText());
            BankInfo_Bean.setBankNickName(BankNickNameText.getText());
            BankInfo_Bean.setBankName(BankNameText.getText());
            BankInfo_Bean.setContactPerson1(ContactPerson1Text.getText());
            BankInfo_Bean.setContactPerson2(ContactPerson2Text.getText());
            if(Telephone1Title.equals(""))  BankInfo_Bean.setTelephone1("");
            else if(Telephone1Transfer.equals(""))    BankInfo_Bean.setTelephone1(Telephone1Title + "-" + Telephone1Number);
            else    BankInfo_Bean.setTelephone1(Telephone1Title + "-" + Telephone1Number + "#" + Telephone1Transfer);
            if(Telephone2Title.equals(""))  BankInfo_Bean.setTelephone2("");
            else if(Telephone2Transfer.equals(""))    BankInfo_Bean.setTelephone2(Telephone2Title + "-" + Telephone2Number);
            else    BankInfo_Bean.setTelephone2(Telephone2Title + "-" + Telephone2Number + "#" + Telephone2Transfer);
            if(FaxTitle.equals(""))   BankInfo_Bean.setFax("");
            else    BankInfo_Bean.setFax(FaxTitle + "-" + FaxNumber);
            BankInfo_Bean.setAddress(AddressText.getText());
            BankInfo_Bean.setRemark(RemarkText.getText());
            return true;
        }
        return false;
    }
   /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            this.BankInfo_Bean = ComponentToolKit.getAllBankTableViewSelectItem(TableView);
            if(BankInfo_Bean != null){
                ComponentToolKit.setButtonDisable(ModifyBankInfo_Button,false);

                BankIDText.setText(String.valueOf(BankInfo_Bean.getBankID()));
                BankCodeText.setText(BankInfo_Bean.getBankCode());
                BankNickNameText.setText(BankInfo_Bean.getBankNickName());
                BankNameText.setText(BankInfo_Bean.getBankName());
                ContactPerson1Text.setText(BankInfo_Bean.getContactPerson1());
                ContactPerson2Text.setText(BankInfo_Bean.getContactPerson2());
                setPhoneText(Telephone1TitleText, Telephone1NumberText, Telephone1TransferText, BankInfo_Bean.getTelephone1());
                setPhoneText(Telephone2TitleText, Telephone2NumberText, Telephone2TransferText, BankInfo_Bean.getTelephone2());
                setPhoneText(FaxTitleText, FaxNumberText, null, BankInfo_Bean.getFax());
                AddressText.setText(BankInfo_Bean.getAddress());
                RemarkText.setText(BankInfo_Bean.getRemark());
            }
        }
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
}
