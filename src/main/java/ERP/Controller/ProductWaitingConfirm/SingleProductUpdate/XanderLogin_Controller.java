package ERP.Controller.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.Vendor;
import ERP.Model.ProductWaitingConfirm.SingleProductUpdate.XanderGetWebInfo_Model;
import ERP.Model.ProductWaitingConfirm.SingleProductUpdate_Model;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.util.HashMap;
import java.util.Map;
/** [Controller] Xander login */
public class XanderLogin_Controller {
    private KeyPressed KeyPressed;
    private ComponentToolKit ComponentToolKit;
    private Stage Stage;
    private XanderGetWebInfo_Model XanderGetWebInfo_Model;
    private Vendor Vendor;
    private SingleProductUpdate_Model SingleProductUpdate_Model;
    private Label UpdateStatus;

    @FXML private TextField CompanyTaxText, AccountText, CaptchaText;
    @FXML private PasswordField PasswordText;
    @FXML private ImageView ImageView;
    @FXML private Label CaptchaError;

    public XanderLogin_Controller(){
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        XanderGetWebInfo_Model = ERPApplication.ToolKit.ModelToolKit.getXanderGetWebInfoModel();
    }
    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setSingleProductUpdate_Model(){
        SingleProductUpdate_Model = ERPApplication.ToolKit.ModelToolKit.getSingleProductUpdateModel(Stage);
    }
    /** Set object - [Enum] Product_Enum.Vendor */
    public void setVendor(Vendor Vendor){   this.Vendor = Vendor;   }
    /** Set object - [Label] */
    public void setUpdateStatus(Label UpdateStatus){  this.UpdateStatus = UpdateStatus;   }
    /** Set login info of vendor */
    public void setLoginInfo() throws Exception{
        CompanyTaxText.setText(SingleProductUpdate_Model.getVendorCompanyTax());
        AccountText.setText(SingleProductUpdate_Model.getVendorAccount(Vendor.getEnglishName()));
        PasswordText.setText(SingleProductUpdate_Model.getVendorPassword(Vendor.getEnglishName()));
        Image Image = XanderGetWebInfo_Model.getLoginCaptcha();
        ImageView.setImage(Image);
    }
    /** Button Mouse Clicked - 驗證碼刷新  */
    @FXML protected void RefreshLoginCaptchaMouseClicked(MouseEvent MouseEvent) throws Exception {
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            CaptchaText.setText("");
            Image Image = XanderGetWebInfo_Model.getLoginCaptcha();
            ImageView.setImage(Image);
            CaptchaError.setText("");
        }
    }
    /** TextField Key Pressed - 登入  */
    @FXML protected void LoginKeyPressed(KeyEvent KeyEvent) throws Exception{
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            CaptchaError.setText("");
            HashMap<Boolean, Map> LoginCookieMap = XanderGetWebInfo_Model.Login(CompanyTaxText.getText(), AccountText.getText(), PasswordText.getText(), CaptchaText.getText(), CaptchaError);
            if (LoginCookieMap.containsKey(true)) {
                SingleProductUpdate_Model.UpdateVendorCookie(Product_Enum.Vendor.建達國際.getEnglishName(), LoginCookieMap.get(true).toString());
                UpdateStatus.setText("建達登入成功，請重新執行!");
                ERPApplication.Logger.info("[單筆更新] 建達 Cookie 過期，登入成功!");
                ComponentToolKit.closeThisStage(Stage);
            }else{
                UpdateStatus.setText("建達登入失敗!");
                ERPApplication.Logger.info("[單筆更新] 建達 Cookie 過期，登入失敗!");
            }
        }
    }
    @FXML protected void LoginMouseClicked(MouseEvent MouseEvent) throws Exception{
        if(KeyPressed.isMouseLeftClicked(MouseEvent)) {
            CaptchaError.setText("");
            HashMap<Boolean, Map> LoginCookieMap = XanderGetWebInfo_Model.Login(CompanyTaxText.getText(), AccountText.getText(), PasswordText.getText(), CaptchaText.getText(), CaptchaError);
            if (LoginCookieMap.containsKey(true)) {
                SingleProductUpdate_Model.UpdateVendorCookie(Product_Enum.Vendor.建達國際.getEnglishName(), LoginCookieMap.get(true).toString());
                UpdateStatus.setText("建達登入成功，請重新執行!");
                ERPApplication.Logger.info("[單筆更新] 建達 Cookie 過期，登入成功!");
                ComponentToolKit.closeThisStage(Stage);
            } else {
                UpdateStatus.setText("建達登入失敗!");
                ERPApplication.Logger.info("[單筆更新] 建達 Cookie 過期，登入失敗!");
            }
        }
    }
    /** Button Mouse Clicked - 取消  */
    @FXML protected void CloseMouseClicked(MouseEvent MouseEvent) {  if(KeyPressed.isMouseLeftClicked(MouseEvent))  ComponentToolKit.closeThisStage(Stage);  }
}
