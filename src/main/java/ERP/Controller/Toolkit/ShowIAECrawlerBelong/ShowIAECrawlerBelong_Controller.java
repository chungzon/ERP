package ERP.Controller.Toolkit.ShowIAECrawlerBelong;

import ERP.Bean.SystemSetting.IAECrawlerBelong_Bean;
import ERP.Controller.SystemSetting.SystemSetting_Controller;
import ERP.ERPApplication;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ShowIAECrawlerBelong_Controller {

    @FXML TextField BelongNameText, BelongURLText;
    @FXML TableColumn<IAECrawlerBelong_Bean,String> BelongName_TableColumn,BelongURL_TableColumn;
    @FXML TableView<IAECrawlerBelong_Bean> TableView;

    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private SystemSetting_Controller SystemSetting_Controller;
    private SystemSetting_Model SystemSetting_Model;
    private Stage Stage;
    private ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList;

    public ShowIAECrawlerBelong_Controller(){
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        SystemSetting_Model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setSystemSetting_Controller(SystemSetting_Controller SystemSetting_Controller){
        this.SystemSetting_Controller = SystemSetting_Controller;
    }
    public void setIAECrawlerBelongList(ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList){
        this.IAECrawlerBelongList = IAECrawlerBelongList;
    }
    public void setStage(Stage Stage){
        this.Stage = Stage;
    }
    public void setComponent(){
        initialTableView();
        TableView.setItems(IAECrawlerBelongList);
    }
    private void initialTableView(){
        ComponentToolKit.setColumnCellValue(BelongName_TableColumn,"BelongName", "CENTER-LEFT", "16",null);
        ComponentToolKit.setColumnCellValue(BelongURL_TableColumn,"BelongURL", "CENTER-LEFT", "16",null);
        BelongURL_TableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }
    @FXML protected void TableViewMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){

        }
    }
    @FXML protected void BelongURLOnEditCommit(TableColumn.CellEditEvent<IAECrawlerBelong_Bean,String> belongURLOnEditCommit){
        IAECrawlerBelong_Bean IAECrawlerBelong_Bean = ComponentToolKit.getIAECrawlerBelongTableViewSelectItem(TableView);
        if(DialogUI.ConfirmDialog("是否確定修改 ?",true,false,0,0)) {
            IAECrawlerBelong_Bean.setBelongURL(belongURLOnEditCommit.getNewValue());
            if(SystemSetting_Model.modifyIAECrawlerBelong(IAECrawlerBelong_Bean))   DialogUI.MessageDialog("※ 修改成功");
            else    DialogUI.MessageDialog("※ 修改失敗");
        }else
            IAECrawlerBelong_Bean.setBelongURL(belongURLOnEditCommit.getOldValue());

    }
    @FXML protected void insertMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String BelongName = BelongNameText.getText();
            String BelongURL = BelongURLText.getText();
            if(BelongName.equals(""))   DialogUI.MessageDialog("※ 請輸入所屬名稱");
            else if(BelongURL.equals(""))   DialogUI.MessageDialog("※ 請輸入網址");
            else{
                IAECrawlerBelong_Bean IAECrawlerBelong_Bean = new IAECrawlerBelong_Bean();
                IAECrawlerBelong_Bean.setBelongName(BelongName);
                IAECrawlerBelong_Bean.setBelongURL(BelongURL);
                if(SystemSetting_Model.insertIAECrawlerBelong(IAECrawlerBelong_Bean)) {
                    IAECrawlerBelongList.add(IAECrawlerBelong_Bean);
                    SystemSetting_Controller.refreshIAECrawlerBelongItems();
                    BelongNameText.setText("");
                    BelongURLText.setText("");
                    DialogUI.MessageDialog("※ 新增成功");
                }else{
                    DialogUI.MessageDialog("※ 新增失敗");
                }
            }
        }
    }
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))
            Stage.close();
    }
    @FXML protected void deleteMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            IAECrawlerBelong_Bean IAECrawlerBelong_Bean = ComponentToolKit.getIAECrawlerBelongTableViewSelectItem(TableView);
            if(IAECrawlerBelong_Bean == null)   DialogUI.MessageDialog("※ 請選擇所屬");
            else if(SystemSetting_Model.isIAECrawlerAccountBelongHaveThisBelong(IAECrawlerBelong_Bean.getId())) DialogUI.MessageDialog("※ 刪除被拒，此所屬有綁定帳戶");
            else{
                if(DialogUI.ConfirmDialog("是否確定刪除 ?",true,false,0,0)) {
                    if(SystemSetting_Model.deleteIAECrawlerBelong(IAECrawlerBelong_Bean)){
                        SystemSetting_Controller.refreshIAECrawlerBelongItems();
                        DialogUI.MessageDialog("※ 刪除成功");
                        TableView.getItems().remove(IAECrawlerBelong_Bean);
                        IAECrawlerBelongList.remove(IAECrawlerBelong_Bean);
                    }else    DialogUI.MessageDialog("※ 刪除失敗");
                }
            }
        }
    }
}
