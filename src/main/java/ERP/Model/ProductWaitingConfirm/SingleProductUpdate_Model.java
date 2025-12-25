package ERP.Model.ProductWaitingConfirm;

import ERP.Bean.ProductWaitConfirm.WaitConfirmProductInfo_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.Vendor;
import ERP.Enum.Product.Product_Enum.SingleUpdateStatus;
import ERP.Model.ProductWaitingConfirm.SingleProductUpdate.*;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ToolKit;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/** [ERP.Model] Single product update in wait confirm */
public class SingleProductUpdate_Model {
    private ToolKit ToolKit;
    private CallFXML CallFXML;
    private Button UpdateButton;
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus,  UpdateStatus;
    private TextArea NewProductDescribe, NewProductRemark;
    private XanderGetWebInfo_Model XanderGetWebInfo_Model;
    private KtnetGetWebInfo_Model KtnetGetWebInfo_Model;
    private Genb2bGetWebInfo_Model Genb2bGetWebInfo_Model;
    private SynnexGetWebInfo_Model SynnexGetWebInfo_Model;
    private UnitechGetWebInfo_Model UnitechGetWebInfo_Model;
    private JinghaoGetWebInfo_Model JinghaoGetWebInfo_Model;
    private WeblinkGetWebInfo_Model WeblinkGetWebInfo_Model;
    private SenaoGetWebInfo_Model SenaoGetWebInfo_Model;

    private Stage MainStage;
    public SingleProductUpdate_Model(Stage MainStage){
        this.MainStage = MainStage;
        ToolKit = ERPApplication.ToolKit;
        CallFXML = ToolKit.CallFXML;

        XanderGetWebInfo_Model = ToolKit.ModelToolKit.getXanderGetWebInfoModel();
        KtnetGetWebInfo_Model = ToolKit.ModelToolKit.getKtnetGetWebInfoModel();
        Genb2bGetWebInfo_Model = ToolKit.ModelToolKit.getGenb2bGetWebInfoModel();
        SynnexGetWebInfo_Model = ToolKit.ModelToolKit.getSynnexGetWebInfoModel();
        UnitechGetWebInfo_Model = ToolKit.ModelToolKit.getUnitechGetWebInfoModel();
        JinghaoGetWebInfo_Model = ToolKit.ModelToolKit.getJinghaoGetWebInfo_Model();
        WeblinkGetWebInfo_Model = ToolKit.ModelToolKit.getWeblinkGetWebInfoModel();
        SenaoGetWebInfo_Model = ToolKit.ModelToolKit.getSenaoGetWebInfoModel();
    }
    public void initialWebDriver(){
        SenaoGetWebInfo_Model.closeChromeWebDriver();

    }
    public void setNewLabel(Button UpdateButton, Label NewProductName, Label NewProductBatchPrice, Label NewProductPricing, Label NewProductSupplyStatus, TextArea NewProductDescribe, TextArea NewProductRemark, Label UpdateStatus){
        this.UpdateButton = UpdateButton;
        this.NewProductName = NewProductName;
        this.NewProductBatchPrice = NewProductBatchPrice;
        this.NewProductPricing = NewProductPricing;
        this.NewProductSupplyStatus = NewProductSupplyStatus;
        this.NewProductDescribe = NewProductDescribe;
        this.NewProductRemark = NewProductRemark;
        this.UpdateStatus = UpdateStatus;
    }

    /** Update product of Xander */
    public void Xander(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")){
                SingleUpdateStatus SingleUpdateStatus = XanderGetWebInfo_Model.getProductInfo(Url, WaitConfirmProductInfo_Bean.getProductCode(), getVendorCookie(Vendor.建達國際.getEnglishName()));
                if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期){
                    UpdateButton.setDisable(true);
                    CallFXML.ShowXanderLogin(MainStage, UpdateStatus);
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else {
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在或已下架!");
                DialogUI.MessageDialog("商品網址不存在或已下架!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** Update product of Ktnet */
    public void Ktnet(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")){
                SingleUpdateStatus SingleUpdateStatus = KtnetGetWebInfo_Model.getProductInfo(Url, getProductOther(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode()), getVendorCookie(Vendor.廣鐸企業.getEnglishName()));
                if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期){
                    UpdateButton.setDisable(true);
                    ERPApplication.Logger.info("[單筆更新] 廣鐸 Cookie 過期，自動登入中...");
                    UpdateStatus.setText("廣鐸 Cookie 過期，自動登入中...");
                    String LoginCookieInfo = KtnetGetWebInfo_Model.AutomaticLogin(getVendorAccount(Vendor.廣鐸企業.getEnglishName()),getVendorPassword(Vendor.廣鐸企業.getEnglishName()));

                    if(!LoginCookieInfo.equals("") && !LoginCookieInfo.equals("{}")) {
                        UpdateVendorCookie(Vendor.廣鐸企業.getEnglishName(),LoginCookieInfo);
                        ERPApplication.Logger.info("[單筆更新] 廣鐸 Cookie 過期，登入成功!");
                        UpdateStatus.setText("廣鐸登入成功，請重新執行!");
                        DialogUI.MessageDialog("廣鐸登入成功，請重新執行!");
                    }else{
                        ERPApplication.Logger.info("[單筆更新] 廣鐸 Cookie 過期，登入失敗!");
                        UpdateStatus.setText("廣鐸登入失敗!");
                        DialogUI.MessageDialog("廣鐸登入失敗!");
                    }
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else {
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在或已下架!");
                DialogUI.MessageDialog("商品網址不存在或已下架!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** Update product of Genb2b */
    public void Genb2b(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try{
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")) {
                SingleUpdateStatus SingleUpdateStatus = Genb2bGetWebInfo_Model.getProductInfo(Url, WaitConfirmProductInfo_Bean.getProductCode(), getProductOther(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode()), getVendorCookie(Vendor.Genuine捷元.getEnglishName()));
                if (SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期) {
                    UpdateButton.setDisable(true);
                    ERPApplication.Logger.info("[單筆更新] 捷元 Cookie 過期，自動登入中...");
                    UpdateStatus.setText("捷元 Cookie 過期，自動登入中...");
                    String LoginCookieInfo = Genb2bGetWebInfo_Model.AutomaticLogin(getVendorAccount(Vendor.Genuine捷元.getEnglishName()), getVendorPassword(Vendor.Genuine捷元.getEnglishName()));
                    if(!LoginCookieInfo.equals("") && !LoginCookieInfo.equals("{}")) {
                        UpdateVendorCookie(Vendor.Genuine捷元.getEnglishName(),LoginCookieInfo);
                        ERPApplication.Logger.info("[單筆更新] 捷元 Cookie 過期，登入成功!");
                        UpdateStatus.setText("捷元登入成功，請重新執行!");
                        DialogUI.MessageDialog("捷元登入成功，請重新執行!");
                    }else{
                        ERPApplication.Logger.info("[單筆更新] 捷元 Cookie 過期，登入失敗!");
                        UpdateStatus.setText("捷元登入失敗!");
                        DialogUI.MessageDialog("捷元登入失敗!");
                    }
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else{
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在!");
                DialogUI.MessageDialog("商品網址不存在!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** Update product of Synnex */
    public void Synnex(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try{
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")) {
                SingleUpdateStatus SingleUpdateStatus = SynnexGetWebInfo_Model.getProductInfo(Url, WaitConfirmProductInfo_Bean.getProductCode(), getSettingPricing(Vendor.聯強國際.getEnglishName()), getVendorCookie(Vendor.聯強國際.getEnglishName()));
                if (SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期) {
                    UpdateButton.setDisable(true);
                    ERPApplication.Logger.info("[單筆更新] 聯強 Cookie 過期，自動登入中...");
                    UpdateStatus.setText("聯強 Cookie 過期，自動登入中...");
                    String LoginCookieInfo = SynnexGetWebInfo_Model.AutomaticLogin(getVendorAccount(Vendor.聯強國際.getEnglishName()), getVendorPassword(Vendor.聯強國際.getEnglishName()));
                    if(!LoginCookieInfo.equals("") && !LoginCookieInfo.equals("{}")) {
                        UpdateVendorCookie(Vendor.聯強國際.getEnglishName(),LoginCookieInfo);
                        ERPApplication.Logger.info("[單筆更新] 聯強 Cookie 過期，登入成功!");
                        UpdateStatus.setText("聯強登入成功，請重新執行!");
                        DialogUI.MessageDialog("聯強登入成功，請重新執行!");
                    }else{
                        ERPApplication.Logger.info("[單筆更新] 聯強 Cookie 過期，登入失敗!");
                        UpdateStatus.setText("聯強登入失敗!");
                        DialogUI.MessageDialog("聯強登入失敗!");
                    }
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else{
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在!");
                DialogUI.MessageDialog("商品網址不存在!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** Update product of Unitech */
    public void Unitech(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")){
                SingleUpdateStatus SingleUpdateStatus = UnitechGetWebInfo_Model.getProductInfo(WaitConfirmProductInfo_Bean.getProductCode(), getProductOther(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode()), getSettingPricing(Vendor.精技電腦.getEnglishName()), getVendorCookie(Vendor.精技電腦.getEnglishName()));
                if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期){
                    UpdateButton.setDisable(true);
                    CallFXML.ShowUnitechLogin(MainStage, UpdateStatus);
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.驗證碼錯誤){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("驗證碼輸入錯誤!");
                    DialogUI.MessageDialog("驗證碼輸入錯誤!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else {
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在或已下架!");
                DialogUI.MessageDialog("商品網址不存在或已下架!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** Update product of Jinghao */
    public void Jinghao(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")){
                SingleUpdateStatus SingleUpdateStatus = JinghaoGetWebInfo_Model.getProductInfo(getProductOther(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode()), getSettingPricing(Vendor.精豪電腦.getEnglishName()), getVendorCookie(Vendor.精豪電腦.getEnglishName()));
                if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期){
                    UpdateButton.setDisable(true);
                    CallFXML.ShowJinghaoLogin(MainStage, UpdateStatus);
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.驗證碼錯誤){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("驗證碼輸入錯誤!");
                    DialogUI.MessageDialog("驗證碼輸入錯誤!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else {
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在或已下架!");
                DialogUI.MessageDialog("商品網址不存在或已下架!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** Update product of Weblink */
    public void Weblink(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")){
                SingleUpdateStatus SingleUpdateStatus = WeblinkGetWebInfo_Model.getProductInfo(Url, getVendorCookie(Vendor.展碁國際.getEnglishName()));
                if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期){
                    UpdateButton.setDisable(true);
                    CallFXML.ShowWeblinkLogin(MainStage, UpdateStatus);
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else {
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在或已下架!");
                DialogUI.MessageDialog("商品網址不存在或已下架!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** Update product of Senao */
    public void Senao(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        try {
            UpdateStatus.setText("執行中...");
            String Url = getProductUrl(WaitConfirmProductInfo_Bean.getVendorCode(), WaitConfirmProductInfo_Bean.getProductCode());
            if(!Url.equals("")){
                SingleUpdateStatus SingleUpdateStatus = SenaoGetWebInfo_Model.getProductInfo(Url, getSettingPricing(Vendor.神腦國際.getEnglishName()), getVendorAccount(Vendor.神腦國際.getEnglishName()),getVendorPassword(Vendor.神腦國際.getEnglishName()));
                if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.Cookie過期){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("神腦 Cookie 過期，請透過經銷商登入!");
                    ERPApplication.Logger.info("[單筆更新] 神腦 Cookie 過期，請透過經銷商登入!");
                    DialogUI.AlarmDialog("[單筆更新] 神腦 Cookie 過期，請透過經銷商登入!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.商品下架){
                    UpdateButton.setDisable(true);
                    UpdateStatus.setText("此商品已下架!");
                    DialogUI.MessageDialog("此商品已下架!");
                }else if(SingleUpdateStatus == Product_Enum.SingleUpdateStatus.更新完成){
                    setTextFill(WaitConfirmProductInfo_Bean);
                    if(setUpdateButton())   UpdateButton.setDisable(false);
                    UpdateStatus.setText("執行完成");
                }
            }else {
                UpdateButton.setDisable(true);
                UpdateStatus.setText("商品網址不存在或已下架!");
                DialogUI.MessageDialog("商品網址不存在或已下架!");
            }
        }catch(Exception Ex){
            UpdateButton.setDisable(true);
            UpdateStatus.setText("執行異常!");
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }

    //	取得 Product_Url 資料庫內商品網址
    private String getProductUrl(int VendorCode, String ProductCode){
        String ProductUrl = "";
        PreparedStatement Statement = null;
        ResultSet Rs = null;
        try {
            Statement = SqlAdapter.getConnect().prepareStatement("select Url from product_url where VendorCode = '" + VendorCode + "' and ProductCode = '" + ProductCode + "' and Url_Exist =1");
            Rs = Statement.executeQuery();
            if (Rs.next())
                ProductUrl = Rs.getString(1);
        }catch (Exception Ex){
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        SqlAdapter.closeConnectParameter(Statement, Rs);
        return ProductUrl;
    }
    //	取得 Product_Url 資料庫內 Other
    private String getProductOther(int VendorCode, String ProductCode){
        String ProductOther = "";
        PreparedStatement Statement = null;
        ResultSet Rs = null;
        try {
            Statement = SqlAdapter.getConnect().prepareStatement("select Other from product_url where VendorCode = '" + VendorCode + "' and ProductCode = '" + ProductCode + "' and Url_Exist =1");
            Rs = Statement.executeQuery();
            if (Rs.next())
                ProductOther = Rs.getString(1);
        }catch (Exception Ex){
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        SqlAdapter.closeConnectParameter(Statement, Rs);
        return ProductOther;
    }
    /** Update cookies of vendor in database */
    public void UpdateVendorCookie(String VendorEn, String Cookie) throws Exception {
        boolean CookieExist = false;
        PreparedStatement Statement = null;
        ResultSet Rs = null;
        try {
            Statement = SqlAdapter.getConnect().prepareStatement("SELECT * FROM LoginData where Name = '" + VendorEn + "_Cookie'");
            Rs = Statement.executeQuery();
            if (Rs.next())  CookieExist = true;
        }catch (Exception Ex){
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        SqlAdapter.closeConnectParameter(Statement, Rs);
        if(CookieExist) SqlAdapter.DataBaseQuery("update LoginData set Value = '" + Cookie + "'  where Name = '"+ VendorEn + "_Cookie'");
        else    SqlAdapter.DataBaseQuery("insert into LoginData (name,value) values ('" + VendorEn + "_Cookie','" + Cookie + "')");
    }
    private String getSettingPricing(String Vendor){
        String Pricing = "";
        PreparedStatement Statement = null;
        ResultSet Rs = null;
        try {
            Statement = SqlAdapter.getConnect().prepareStatement("select value from parameter where name = '" + Vendor + "_Pricing'");
            Rs = Statement.executeQuery();
            if (Rs.next())
                Pricing = Rs.getString(1);
        }catch (Exception Ex){
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        SqlAdapter.closeConnectParameter(Statement, Rs);
        return Pricing;
    }
    //	取得資料庫內各代理商的Cookie - 判斷Cookie是否過期
    private String getVendorCookie(String VendorEn){
        String CookieString = "";
        PreparedStatement Statement = null;
        ResultSet Rs = null;
        try {
            Statement = SqlAdapter.getConnect().prepareStatement("SELECT * FROM LoginData where Name = '" + VendorEn + "_Cookie'");
            Rs = Statement.executeQuery();
            if (Rs.next())
                CookieString = Rs.getString("Value");
        }catch (Exception Ex){
            Ex.printStackTrace();
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        SqlAdapter.closeConnectParameter(Statement, Rs);
        return CookieString;
    }
    /** Get company tax from database */
    public String getVendorCompanyTax() throws Exception {
        String Account="";
        PreparedStatement Statement = SqlAdapter.getConnect().prepareStatement("SELECT * FROM LoginData where Name = 'Company_Tax'");
        ResultSet Rs = Statement.executeQuery();
        if(Rs.next())
            Account = Rs.getString("Value");
        SqlAdapter.closeConnectParameter(Statement, Rs);
        return Account;
    }
    /** Get account of vendor from database
     * @param VendorEn the english name of vendor
     * */
    public String getVendorAccount(String VendorEn) throws Exception {
        String Account="";
        PreparedStatement Statement = SqlAdapter.getConnect().prepareStatement("SELECT * FROM LoginData where Name = '" + VendorEn + "_Account'");
        ResultSet Rs = Statement.executeQuery();
        if(Rs.next())
            Account = Rs.getString("Value");
        SqlAdapter.closeConnectParameter(Statement, Rs);
        return Account;
    }
    /** Get password of vendor from database
     * @param VendorEn the english name of vendor
     * */
    public String getVendorPassword(String VendorEn) throws Exception {
        String Password="";
        PreparedStatement Statement = SqlAdapter.getConnect().prepareStatement("SELECT * FROM LoginData where Name = '" + VendorEn + "_Password'");
        ResultSet Rs = Statement.executeQuery();
        if(Rs.next())
            Password = Rs.getString("Value");
        SqlAdapter.closeConnectParameter(Statement, Rs);
        return Password;
    }
    /** Update product in database
     * @param WaitConfirmProductInfo_Bean the product info of wait confirm
     * */
    public boolean UpdateProduct(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean) {
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.進銷存商品){
                Query = Query + "update Store set ProductName = ?, Describe = ?, Remark = ?, SupplyStatus = ?, Status = ? where ProductCode = ? " +
                        "update store_price set BatchPrice = ?, SinglePrice = ?, Pricing = ?, VipPrice1 = ?, VipPrice2 = ?, VipPrice3 = ? from Store A inner join store_price B on A.id = B.store_id where A.ProductCode = ? " +
                        "update store_date set UpdateDate = ? from Store A inner join store_date B on A.id = B.store_id where A.ProductCode = ? ";
/*
            SqlAdapter.DataBaseQuery("update Store set ProductName = '" + WaitConfirmProductInfo_Bean.getProductName() + "', BatchPrice = '" + WaitConfirmProductInfo_Bean.getBatchPrice() + "'," +
                    " Pricing = '" + WaitConfirmProductInfo_Bean.getPricing() + "', SupplyStatus = '" + WaitConfirmProductInfo_Bean.getSupplyStatus() + "', " +
                    "Describe = '" + WaitConfirmProductInfo_Bean.getDescribe() + "', Remark = '" + WaitConfirmProductInfo_Bean.getRemark() + "',UpdateDate = '" + ToolKit.getToday("yyyy/MM/dd") + "', " +
                    "Status = '" + Product_Enum.StoreStatus.已確認.ordinal()+ "' where ProductCode = '" + WaitConfirmProductInfo_Bean.getProductCode() + "'");*/


            }else if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品){
                Query = Query + "update CheckStore set ProductName = ?, BatchPrice = ?, SinglePrice = ?, Pricing = ?, VipPrice1 = ?, VipPrice2 = ?, VipPrice3 = ?, SupplyStatus = ?, Describe = ?, Remark = ?, UpdateDate = ?, Status = ? where ProductCode = ? ";
                /*
                SqlAdapter.DataBaseQuery("update CheckStore set ProductName = '" + WaitConfirmProductInfo_Bean.getProductName() + "', BatchPrice = '" + WaitConfirmProductInfo_Bean.getBatchPrice() + "'," +
                        " Pricing = '" + WaitConfirmProductInfo_Bean.getPricing() + "', SupplyStatus = '" + WaitConfirmProductInfo_Bean.getSupplyStatus() + "', " +
                        "Describe = '" + WaitConfirmProductInfo_Bean.getDescribe() + "', Remark = '" + WaitConfirmProductInfo_Bean.getRemark() + "', KeyinDate = '" + WaitConfirmProductInfo_Bean.getKeyInDate() + "', " +
                        "Status = '" + Product_Enum.CheckStoreStatus.新增.ordinal() + "' where ProductCode = '" + WaitConfirmProductInfo_Bean.getProductCode() + "'");*/
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";

            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.進銷存商品){
                PreparedStatement.setString(1,WaitConfirmProductInfo_Bean.getProductName());
                PreparedStatement.setString(2,WaitConfirmProductInfo_Bean.getDescribe());
                PreparedStatement.setString(3,WaitConfirmProductInfo_Bean.getRemark());
                PreparedStatement.setString(4,WaitConfirmProductInfo_Bean.getSupplyStatus());
                PreparedStatement.setInt(5,Product_Enum.StoreStatus.已確認.ordinal());
                PreparedStatement.setString(6,WaitConfirmProductInfo_Bean.getProductCode());

                PreparedStatement.setBigDecimal(7, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getBatchPrice()));
                PreparedStatement.setBigDecimal(8, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getSinglePrice()));
                PreparedStatement.setBigDecimal(9, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getPricing()));
                PreparedStatement.setBigDecimal(10, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getVipPrice1()));
                PreparedStatement.setBigDecimal(11, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getVipPrice2()));
                PreparedStatement.setBigDecimal(12, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getVipPrice3()));
                PreparedStatement.setString(13,WaitConfirmProductInfo_Bean.getProductCode());

                PreparedStatement.setDate(14, Date.valueOf(ToolKit.getToday("yyyy-MM-dd")));
                PreparedStatement.setString(15,WaitConfirmProductInfo_Bean.getProductCode());
            }else if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品){
                PreparedStatement.setString(1,WaitConfirmProductInfo_Bean.getProductName());
                PreparedStatement.setBigDecimal(2, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getBatchPrice()));
                PreparedStatement.setBigDecimal(3, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getSinglePrice()));
                PreparedStatement.setBigDecimal(4, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getPricing()));
                PreparedStatement.setBigDecimal(5, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getVipPrice1()));
                PreparedStatement.setBigDecimal(6, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getVipPrice2()));
                PreparedStatement.setBigDecimal(7, BigDecimal.valueOf(WaitConfirmProductInfo_Bean.getVipPrice3()));
                PreparedStatement.setString(8,WaitConfirmProductInfo_Bean.getSupplyStatus());
                PreparedStatement.setString(9,WaitConfirmProductInfo_Bean.getDescribe());
                PreparedStatement.setString(10,WaitConfirmProductInfo_Bean.getRemark());
                PreparedStatement.setDate(11, Date.valueOf(ToolKit.getToday("yyyy-MM-dd")));
                PreparedStatement.setInt(12,Product_Enum.CheckStoreStatus.新增.ordinal());
                PreparedStatement.setString(13,WaitConfirmProductInfo_Bean.getProductCode());
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(()-> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    private void setTextFill(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        if(WaitConfirmProductInfo_Bean.getProductName().equals(NewProductName.getText()))   NewProductName.setTextFill(Color.BLACK);
        else    NewProductName.setTextFill(Color.RED);
        if(WaitConfirmProductInfo_Bean.getBatchPrice() == ToolKit.RoundingDouble(Double.parseDouble(NewProductBatchPrice.getText())))  NewProductBatchPrice.setTextFill(Color.BLACK);
        else    NewProductBatchPrice.setTextFill(Color.RED);
        if(WaitConfirmProductInfo_Bean.getPricing() == ToolKit.RoundingDouble(Double.parseDouble(NewProductPricing.getText())))    NewProductPricing.setTextFill(Color.BLACK);
        else    NewProductPricing.setTextFill(Color.RED);
        if(WaitConfirmProductInfo_Bean.getSupplyStatus().equals(NewProductSupplyStatus.getText()))  NewProductSupplyStatus.setTextFill(Color.BLACK);
        else    NewProductSupplyStatus.setTextFill(Color.RED);
        if(WaitConfirmProductInfo_Bean.getDescribe().equals(NewProductDescribe.getText()))  NewProductDescribe.setStyle("-fx-text-fill: black");
        else    NewProductDescribe.setStyle("-fx-text-fill: red");
        if(WaitConfirmProductInfo_Bean.getRemark().equals(NewProductRemark.getText()))  NewProductRemark.setStyle("-fx-text-fill: black");
        else    NewProductRemark.setStyle("-fx-text-fill: red");
    }
    private Boolean setUpdateButton(){
        return NewProductName.getTextFill().equals(Paint.valueOf("0xff0000")) || NewProductBatchPrice.getTextFill().equals(Paint.valueOf("0xff0000")) ||
                NewProductPricing.getTextFill().equals(Paint.valueOf("0xff0000")) || NewProductSupplyStatus.getTextFill().equals(Paint.valueOf("0xff0000")) ||
                NewProductDescribe.getStyle().equals("-fx-text-fill: red") || NewProductRemark.getStyle().equals("-fx-text-fill: red");
    }
}
