package ERP.Controller.Toolkit.ShowControllerVersion;

import ERP.Bean.SystemSetting.Version_Bean;
import ERP.ERPApplication;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ShowControllerVersion_Controller {
    @FXML TextField versionTextField;
    @FXML Button modifyVersion_Button,deleteVersion_Button;
    @FXML ComboBox<Version_Bean> versionComboBox;
    @FXML TextArea versionContent_TextArea;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private SystemSetting_Model SystemSetting_Model;

    private ObservableList<Version_Bean> allVersionList;
    public ShowControllerVersion_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        SystemSetting_Model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void setAllVersionList(ObservableList<Version_Bean> allVersionList){
        this.allVersionList = allVersionList;
        ComponentToolKit.getVersionComboBoxItems(versionComboBox).clear();
        ComponentToolKit.getVersionComboBoxItems(versionComboBox).addAll(allVersionList);
    }

    public void setComponent(){
        ComponentToolKit.setVersionComboBoxObj(versionComboBox);
        if(ComponentToolKit.getVersionComboBoxItems(versionComboBox).size() != 0){
            ComponentToolKit.setTextAreaEditable(versionContent_TextArea,false);
            versionComboBox.getSelectionModel().selectFirst();
            versionContent_TextArea.setText(generateAllVersionContent());
        }else
            ComponentToolKit.setTextAreaEditable(versionContent_TextArea,true);
    }
    @FXML protected void versionOnAction(){
        Version_Bean selectVersion_Bean = ComponentToolKit.getVersionComboBoxSelectItem(versionComboBox);
        if(selectVersion_Bean != null){
            if(selectVersion_Bean.getId() == null){
                ComponentToolKit.setTextAreaEditable(versionContent_TextArea,false);
                ComponentToolKit.setButtonDisable(modifyVersion_Button,true);
                ComponentToolKit.setButtonDisable(deleteVersion_Button,true);
                versionContent_TextArea.setText(generateAllVersionContent());
            }else{
                ComponentToolKit.setTextAreaEditable(versionContent_TextArea,true);
                ComponentToolKit.setButtonDisable(modifyVersion_Button,false);
                ComponentToolKit.setButtonDisable(deleteVersion_Button,false);
                versionContent_TextArea.setText(selectVersion_Bean.getVersionContent());
            }
        }
    }
    private String generateAllVersionContent(){
        StringBuilder versionContent = new StringBuilder();
        ObservableList<Version_Bean> allVersionList = ComponentToolKit.getVersionComboBoxItems(versionComboBox);
        for(Version_Bean Version_Bean: allVersionList){
            if(Version_Bean.getId() != null){
                versionContent.append("                                        ***  ").append(Version_Bean.getVersion()).append("  ***\n");
                versionContent.append("--------------------------------------------------------------------------\n");
                versionContent.append(Version_Bean.getVersionContent()).append("\n\n\n");
            }
        }
        return versionContent.toString();
    }
    @FXML protected void insertVersionMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Version_Bean Version_Bean = new Version_Bean();
            Version_Bean.setVersion(versionTextField.getText());
            Version_Bean.setVersionContent(versionContent_TextArea.getText());
            if(SystemSetting_Model.insertVersion(Version_Bean)){
                if(allVersionList.size() == 0){
                    Version_Bean allVersion_Bean = new Version_Bean();
                    allVersion_Bean.setId(null);
                    allVersion_Bean.setVersion("所有");
                    allVersion_Bean.setVersionContent("");
                    allVersionList.add(allVersion_Bean);
                    ComponentToolKit.getVersionComboBoxItems(versionComboBox).add(allVersion_Bean);
                }
                allVersionList.add(Version_Bean);
                ComponentToolKit.getVersionComboBoxItems(versionComboBox).add(Version_Bean);
                versionComboBox.getSelectionModel().select(Version_Bean);
                DialogUI.MessageDialog("※ 新增成功");
            }else
                DialogUI.MessageDialog("※ 新增失敗");
        }
    }
    @FXML protected void clearVersionMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            versionTextField.setText("");
            versionComboBox.getSelectionModel().select(null);
            versionContent_TextArea.setText("");
            ComponentToolKit.setTextAreaEditable(versionContent_TextArea,true);
        }
    }
    @FXML protected void modifyVersionMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Version_Bean selectVersion_Bean = ComponentToolKit.getVersionComboBoxSelectItem(versionComboBox);
            if(selectVersion_Bean != null){
                if(DialogUI.ConfirmDialog("※ 是否修改版本號內容 " + selectVersion_Bean.getVersion() + " ?",false,false,0,0)){
                    String versionContent = versionContent_TextArea.getText();
                    if(SystemSetting_Model.modifyVersion(selectVersion_Bean, versionContent)){
                        selectVersion_Bean.setVersionContent(versionContent);
                        for(Version_Bean Version_Bean : allVersionList){
                            if(Version_Bean.getId() != null && Version_Bean.getId().equals(selectVersion_Bean.getId()) && Version_Bean.getVersion().equals(selectVersion_Bean.getVersion())){
                                Version_Bean.setVersionContent(versionContent);
                                break;
                            }
                        }
                        DialogUI.MessageDialog("※ 修改成功");
                    }else
                        DialogUI.MessageDialog("※ 修改失敗");

                }
            }else
                DialogUI.MessageDialog("※ 請選擇版本");
        }
    }
    @FXML protected void deleteVersionMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            Version_Bean Version_Bean = ComponentToolKit.getVersionComboBoxSelectItem(versionComboBox);
            if(Version_Bean != null){
                if(DialogUI.ConfirmDialog("※ 確認刪除版本號 " + Version_Bean.getVersion() + " ?",false,false,0,0)){
                    if(SystemSetting_Model.deleteVersion(Version_Bean)) {
                        allVersionList.remove(Version_Bean);
                        ComponentToolKit.getVersionComboBoxItems(versionComboBox).remove(Version_Bean);
                        versionComboBox.getSelectionModel().select(null);
                        versionContent_TextArea.setText("");
                        DialogUI.MessageDialog("※ 刪除成功");
                    }else
                        DialogUI.MessageDialog("※ 刪除失敗");
                }
            }else
                DialogUI.MessageDialog("※ 請選擇版本");
        }
    }


}
