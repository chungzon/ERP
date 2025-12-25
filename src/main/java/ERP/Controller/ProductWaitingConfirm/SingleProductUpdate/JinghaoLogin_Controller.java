package ERP.Controller.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class JinghaoLogin_Controller {
    private ERP.ToolKit.KeyPressed KeyPressed;
    private ERP.ToolKit.ComponentToolKit ComponentToolKit;
    private javafx.stage.Stage Stage;
    private ERP.Model.ProductWaitingConfirm.SingleProductUpdate.JinghaoGetWebInfo_Model JinghaoGetWebInfo_Model;
    private Product_Enum.Vendor Vendor;
    private ERP.Model.ProductWaitingConfirm.SingleProductUpdate_Model SingleProductUpdate_Model;
    private Label UpdateStatus;
    @FXML private TextField CompanyTaxText, AccountText, CaptchaText;
    @FXML private PasswordField PasswordText;
    @FXML private javafx.scene.image.ImageView ImageView;
    @FXML private Label CaptchaError;

    public JinghaoLogin_Controller(){
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        JinghaoGetWebInfo_Model = ERPApplication.ToolKit.ModelToolKit.getJinghaoGetWebInfo_Model();
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setSingleProductUpdate_Model(){
        SingleProductUpdate_Model = ERPApplication.ToolKit.ModelToolKit.getSingleProductUpdateModel(Stage);
    }
    /** Set object - [Enum] Product_Enum.Vendor */
    public void setVendor(Product_Enum.Vendor Vendor){   this.Vendor = Vendor;   }
    /** Set object - [Label] */
    public void setUpdateStatus(Label UpdateStatus){  this.UpdateStatus = UpdateStatus;   }
    /** Set login info of vendor */
    public void setLoginInfo() throws Exception{
        CompanyTaxText.setText(SingleProductUpdate_Model.getVendorCompanyTax());
        AccountText.setText(SingleProductUpdate_Model.getVendorAccount(Vendor.getEnglishName()));
        PasswordText.setText(SingleProductUpdate_Model.getVendorPassword(Vendor.getEnglishName()));
        Image Image = JinghaoGetWebInfo_Model.getLoginCaptcha();
        ImageView.setImage(Image);
    }
    /** Button Mouse Clicked - 驗證碼刷新  */
    @FXML protected void RefreshLoginCaptchaMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            CaptchaText.setText("");
            Image Image = JinghaoGetWebInfo_Model.getLoginCaptcha();
            ImageView.setImage(Image);
            CaptchaError.setText("");
        }
    }
    /** TextField Key Pressed - 登入  */
    @FXML protected void LoginKeyPressed(KeyEvent KeyEvent) throws Exception{
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            CaptchaError.setText("");
            HashMap<Boolean, Map> LoginCookieMap = JinghaoGetWebInfo_Model.Login(CompanyTaxText.getText(), AccountText.getText(), PasswordText.getText(), CaptchaText.getText(), CaptchaError);
            if (LoginCookieMap.containsKey(true)) {
                SingleProductUpdate_Model.UpdateVendorCookie(Product_Enum.Vendor.精豪電腦.getEnglishName(), LoginCookieMap.get(true).toString());
                UpdateStatus.setText("精豪登入成功，請重新執行!");
                ERPApplication.Logger.info("[單筆更新] 精豪 Cookie 過期，登入成功!");
                ComponentToolKit.closeThisStage(Stage);
            }else{
                UpdateStatus.setText("精豪登入失敗!");
                ERPApplication.Logger.info("[單筆更新] 精豪 Cookie 過期，登入失敗!");
            }
        }
    }
    /** Button Mouse Clicked - 登入  */
    @FXML protected void LoginMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            CaptchaError.setText("");
            HashMap<Boolean, Map> LoginCookieMap = JinghaoGetWebInfo_Model.Login(CompanyTaxText.getText(), AccountText.getText(), PasswordText.getText(), CaptchaText.getText(), CaptchaError);
            if (LoginCookieMap.containsKey(true)) {
                SingleProductUpdate_Model.UpdateVendorCookie(Product_Enum.Vendor.精豪電腦.getEnglishName(), LoginCookieMap.get(true).toString());
                UpdateStatus.setText("精豪登入成功，請重新執行!");
                ERPApplication.Logger.info("[單筆更新] 精豪 Cookie 過期，登入成功!");
                ComponentToolKit.closeThisStage(Stage);
            } else {
                UpdateStatus.setText("精豪登入失敗!");
                ERPApplication.Logger.info("[單筆更新] 精豪 Cookie 過期，登入失敗!");
            }
        }
    }
    /** Button Mouse Clicked - 取消  */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent) {  if(KeyPressed.isMouseLeftClicked(MouseEvent))  ComponentToolKit.closeThisStage(Stage);  }
}
