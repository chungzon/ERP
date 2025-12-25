package ERP.Controller.Toolkit.ShowCheckNumberSetting;

import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.ERPApplication;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.View.DialogUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/** [Controller] Show CheckNumber setting */
public class ShowCheckNumberSetting_Controller {
    @FXML TextField initialCheckTitleText, initialCheckNumberText, NowCheckTitleText, NowCheckNumberText;
    @FXML Button saveCheckButton;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;

    private SystemSettingConfig_Bean SystemSettingConfig_Bean;
    private SystemSetting_Model SystemSetting_Model;
    private Stage Stage;
    public ShowCheckNumberSetting_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        SystemSetting_Model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
    }
    /** Set object - [Bean] SystemSettingConfig_Bean */
    public void setSystemSettingConfig_Bean(SystemSettingConfig_Bean SystemSettingConfig_Bean){  this.SystemSettingConfig_Bean = SystemSettingConfig_Bean;   }
    /** Set object - [Stage] */
    public void setStage(Stage Stage){  this.Stage = Stage; }
    /** Set component of show CheckNumber setting */
    public void setComponent(){
        ComponentToolKit.addTextFieldLimitDigital(initialCheckNumberText,false);
        ComponentToolKit.addTextFieldLimitDigital(NowCheckNumberText,false);

        ComponentToolKit.addKeyWordTextLimitLength(initialCheckTitleText, 2);
        ComponentToolKit.addKeyWordTextLimitLength(initialCheckNumberText, 7);
        ComponentToolKit.addKeyWordTextLimitLength(NowCheckNumberText, 7);
        if(SystemSettingConfig_Bean.getInitialCheckNumber() != null && !SystemSettingConfig_Bean.getInitialCheckNumber().equals("")){
            String InitialCheckNumber = SystemSettingConfig_Bean.getInitialCheckNumber();
            initialCheckTitleText.setText(InitialCheckNumber.substring(0,2));
            initialCheckNumberText.setText(InitialCheckNumber.substring(2));
            ComponentToolKit.setButtonDisable(saveCheckButton,false);
        }
        if(SystemSettingConfig_Bean.getNowCheckNumber() != null && !SystemSettingConfig_Bean.getNowCheckNumber().equals("")){
            String NowCheckNumber = SystemSettingConfig_Bean.getNowCheckNumber();
            NowCheckTitleText.setText(NowCheckNumber.substring(0,2));
            NowCheckNumberText.setText(NowCheckNumber.substring(2));
        }
    }
    @FXML protected void initialCheckTitleKeyReleased(){
        String checkTitle = initialCheckTitleText.getText();
        String checkNumber = initialCheckNumberText.getText();
        if((checkTitle.length() == 2 && checkNumber.length() == 7) || (checkTitle.length() == 0 && checkNumber.length() == 0))   ComponentToolKit.setButtonDisable(saveCheckButton,false);
        else    ComponentToolKit.setButtonDisable(saveCheckButton,true);
    }
    @FXML protected void initialCheckNumberKeyReleased(){
        String checkTitle = initialCheckTitleText.getText();
        String checkNumber = initialCheckNumberText.getText();
        if((checkTitle.length() == 2 && checkNumber.length() == 7) || (checkTitle.length() == 0 && checkNumber.length() == 0))   ComponentToolKit.setButtonDisable(saveCheckButton,false);
        else    ComponentToolKit.setButtonDisable(saveCheckButton,true);
    }
    /** Button Mouse Clicked - 儲存 */
    @FXML protected void SaveCheckNumberMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            String InitialCheckNumber = initialCheckTitleText.getText() + initialCheckNumberText.getText();
            if (SystemSettingConfig_Bean.getInitialCheckNumber() == null){
                if(SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.票據起始號, InitialCheckNumber) && SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig.票據流水號, InitialCheckNumber)) {
                    NowCheckTitleText.setText(initialCheckTitleText.getText());
                    NowCheckNumberText.setText(initialCheckNumberText.getText());
                    SystemSettingConfig_Bean.setInitialCheckNumber(InitialCheckNumber);
                    SystemSettingConfig_Bean.setNowCheckNumber(InitialCheckNumber);
                    DialogUI.MessageDialog("※ 設定成功");
                }else   DialogUI.MessageDialog("※ 設定失敗");
            }else {
                if(SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.票據起始號, InitialCheckNumber) && SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig.票據流水號, InitialCheckNumber)) {
                    NowCheckTitleText.setText(initialCheckTitleText.getText());
                    NowCheckNumberText.setText(initialCheckNumberText.getText());
                    SystemSettingConfig_Bean.setInitialCheckNumber(InitialCheckNumber);
                    SystemSettingConfig_Bean.setNowCheckNumber(InitialCheckNumber);
                    DialogUI.MessageDialog("※ 設定成功");
                }else   DialogUI.MessageDialog("※ 設定失敗");
            }
        }
    }
    /** Button Mouse Clicked - 關閉 */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent))    ComponentToolKit.closeThisStage(Stage);
    }
}
