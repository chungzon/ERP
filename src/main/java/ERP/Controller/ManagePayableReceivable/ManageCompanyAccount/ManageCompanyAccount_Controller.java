package ERP.Controller.ManagePayableReceivable.ManageCompanyAccount;

import ERP.Bean.ManagePayableReceivable.BankInfo_Bean;
import ERP.Bean.ManagePayableReceivable.CompanyBank_Bean;
import ERP.ERPApplication;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/** [Controller] Manage company account */
public class ManageCompanyAccount_Controller {

    @FXML ComboBox<BankInfo_Bean> AllBank_ComboBox;
    @FXML TextField  AccountNickNameText, BankBranchText, BankAccountText, BankAccountNameText;
    @FXML TextArea RemarkText;
    @FXML Button ModifyAccount_Button, DeleteAccount_Button;
    @FXML Slider DeleteSlider;
    @FXML Tab SearchAccountTab;
    @FXML TableColumn<CompanyBank_Bean,String> BankCodeColumn, BankNameColumn, BankBranchColumn, BankAccountColumn, BankAccountNameColumn, RemarkColumn;
    @FXML TableView<CompanyBank_Bean> TableView;

    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    public ManageCompanyAccount_Controller(){
        ToolKit toolKit = ERPApplication.ToolKit;
        this.ComponentToolKit = toolKit.ComponentToolKit;
        this.KeyPressed = toolKit.KeyPressed;
        this.ManagePayableReceivable_Model = toolKit.ModelToolKit.getManagePayableReceivableModel();
    }
    /** Set component of manage company account */
    public void setComponent(){
        AllBank_ComboBox.setItems(ManagePayableReceivable_Model.getAllBankInfo(true));
        ComponentToolKit.setAllBankBeanComboBoxObj(AllBank_ComboBox);
        initialTableView();
        ComponentToolKit.addTextFieldLimitDigital(BankAccountText,false);
        ComponentToolKit.addKeyWordTextLimitLength(BankAccountText, 14);
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(BankCodeColumn,"BankCode","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(BankNameColumn,"BankName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(BankBranchColumn,"BankBranch","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(BankAccountColumn,"BankAccount","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(BankAccountNameColumn,"BankAccountName","CENTER-LEFT","16",null);
        ComponentToolKit.setColumnCellValue(RemarkColumn,"Remark","CENTER-LEFT","16",null);
    }
    /** Tab On Selection Changed - 公司帳戶查詢 */
    @FXML protected void searchAccountTabOnSelectionChanged(){
        initialComponent();
        if(SearchAccountTab.isSelected()) {
            ComponentToolKit.setTextFieldEditable(BankAccountText,false);
            TableView.setItems(ManagePayableReceivable_Model.getCompanyBankInfo(false));
        }
    }
    /** Button Mouse Clicked - 新增 */
    @FXML protected void insertAccountMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            BankInfo_Bean BankInfo_Bean = ComponentToolKit.getAllBankComboBoxSelectItem(AllBank_ComboBox);
            if(BankInfo_Bean.getBankID() == 0) DialogUI.MessageDialog("※ 請選擇銀行");
            else{
                CompanyBank_Bean CompanyBank_Bean = RecordCompanyBankInfo(BankInfo_Bean,null);
                if(!ManagePayableReceivable_Model.isExistCompanyAccount(CompanyBank_Bean.getBankID(),CompanyBank_Bean.getBankAccount())){
                    if(ManagePayableReceivable_Model.insertCompanyAccount(CompanyBank_Bean)){
                        DialogUI.MessageDialog("※ 新增成功");
                        initialComponent();
                    }else    DialogUI.MessageDialog("※ 新增失敗");
                }else   DialogUI.MessageDialog("※ 帳戶資料已存在");
            }
        }
    }
    /** Button Mouse Clicked - 清空 */
    @FXML protected void ClearComponentMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            initialComponent();
    }
    private void initialComponent(){
        AllBank_ComboBox.getSelectionModel().selectFirst();
        AccountNickNameText.setText("");
        BankBranchText.setText("");
        BankAccountText.setText("");
        BankAccountNameText.setText("");
        RemarkText.setText("");
        ComponentToolKit.setTextFieldEditable(BankAccountText,true);
        ComponentToolKit.setButtonDisable(ModifyAccount_Button,true);
        ComponentToolKit.setButtonDisable(DeleteAccount_Button,true);
        DeleteSlider.setValue(0);
    }
    /** Button Mouse Clicked - 修改 */
    @FXML protected void modifyAccountMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            BankInfo_Bean BankInfo_Bean = ComponentToolKit.getAllBankComboBoxSelectItem(AllBank_ComboBox);
            if(BankInfo_Bean.getBankID() == 0) DialogUI.MessageDialog("※ 請選擇銀行");
            else if(DialogUI.ConfirmDialog("※ 是否確定修改 ?",true,false,0,0)){
                CompanyBank_Bean CompanyBank_Bean = ComponentToolKit.getCompanyBankTableViewSelectItem(TableView);
                CompanyBank_Bean = RecordCompanyBankInfo(BankInfo_Bean, CompanyBank_Bean);
                if(ManagePayableReceivable_Model.modifyCompanyAccount(CompanyBank_Bean)){
                    DialogUI.MessageDialog("※ 修改成功");
                    TableView.setItems(ManagePayableReceivable_Model.getCompanyBankInfo(false));
                }else    DialogUI.MessageDialog("※ 修改失敗");
            }
        }
    }
    private CompanyBank_Bean RecordCompanyBankInfo(BankInfo_Bean BankInfo_Bean, CompanyBank_Bean CompanyBank_Bean){
        if(CompanyBank_Bean == null)    CompanyBank_Bean = new CompanyBank_Bean();
        CompanyBank_Bean.setBankID(BankInfo_Bean.getBankID());
        CompanyBank_Bean.setBankAccount(BankAccountText.getText());
        CompanyBank_Bean.setBankBranch(BankBranchText.getText());
        CompanyBank_Bean.setBankAccountName(BankAccountNameText.getText());
        CompanyBank_Bean.setAccountNickName(AccountNickNameText.getText());
        CompanyBank_Bean.setRemark(RemarkText.getText());
        return CompanyBank_Bean;
    }
    /** Button Mouse Clicked - 刪除 */
    @FXML protected void deleteAccountMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CompanyBank_Bean CompanyBank_Bean = ComponentToolKit.getCompanyBankTableViewSelectItem(TableView);
            if(DialogUI.ConfirmDialog("※ 是否確定修改 ?",true,false,0,0)){
                if(ManagePayableReceivable_Model.deleteCompanyAccount(CompanyBank_Bean)){
                    DialogUI.MessageDialog("※ 刪除成功");
                    TableView.setItems(ManagePayableReceivable_Model.getCompanyBankInfo(false));
                    initialComponent();
                }else    DialogUI.MessageDialog("※ 刪除失敗");
            }
        }
    }
    /** Slider Mouse Released - 刪除的滑軌 */
    @FXML protected void deleteSliderMouseReleased(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            double SliderValue = DeleteSlider.getValue();
            if(SliderValue == 1)    ComponentToolKit.setButtonDisable(DeleteAccount_Button,false);
            else    ComponentToolKit.setButtonDisable(DeleteAccount_Button,true);
        }
    }
    /** TableView Mouse Clicked - 表格事件 */
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            CompanyBank_Bean CompanyBank_Bean = ComponentToolKit.getCompanyBankTableViewSelectItem(TableView);
            if(CompanyBank_Bean != null){
                DeleteSlider.setValue(0);
                ComponentToolKit.setButtonDisable(ModifyAccount_Button,false);
                ComponentToolKit.setButtonDisable(DeleteAccount_Button,true);
                ComponentToolKit.setAllBankComboBox(AllBank_ComboBox, CompanyBank_Bean.getBankID());
                AccountNickNameText.setText(CompanyBank_Bean.getAccountNickName());
                BankBranchText.setText(CompanyBank_Bean.getBankBranch());
                BankAccountText.setText(CompanyBank_Bean.getBankAccount());
                BankAccountNameText.setText(CompanyBank_Bean.getBankAccountName());
                RemarkText.setText(CompanyBank_Bean.getRemark());
            }

        }
    }
}
