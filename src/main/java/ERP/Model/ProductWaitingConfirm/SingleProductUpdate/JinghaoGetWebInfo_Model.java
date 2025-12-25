package ERP.Model.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.ToolKit.ToolKit;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class JinghaoGetWebInfo_Model {
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe, NewProductRemark;
    private Map CaptureCookie = new HashMap();
    private Map LoginCookie = new HashMap();

    private ToolKit ToolKit;
    public JinghaoGetWebInfo_Model(){
        this.ToolKit = ERPApplication.ToolKit;
    }
    public void setNewLabel(Label NewProductName, Label NewProductBatchPrice, Label NewProductPricing, Label NewProductSupplyStatus, TextArea NewProductDescribe, TextArea NewProductRemark){
        this.NewProductName = NewProductName;
        this.NewProductBatchPrice = NewProductBatchPrice;
        this.NewProductPricing = NewProductPricing;
        this.NewProductSupplyStatus = NewProductSupplyStatus;
        this.NewProductDescribe = NewProductDescribe;
        this.NewProductRemark = NewProductRemark;
    }
    public Product_Enum.SingleUpdateStatus getProductInfo(String ProductOther, String JinghaoPricing, String CookieString) throws Exception {
        String[] Parameter = ProductOther.split("@");
        HandleCookie(CookieString);
        Connection.Response Response = SearchUrl("https://iorder.unitech.com.tw:8080/Product/ProductDetail", Parameter);
        if (Response.body().contains("加入會員")) return Product_Enum.SingleUpdateStatus.Cookie過期;
        else if(Response.body().contains("驗證碼不正確")) return Product_Enum.SingleUpdateStatus.驗證碼錯誤;
        else if(Response.body().contains("找不到資源") || Response.body().contains("無此商品資料"))    return Product_Enum.SingleUpdateStatus.商品下架;
        else {
            Document Doc = Response.parse();
            NewProductName.setText(Parameter[3]);
            //	經銷價(促銷價)
            String BatchPrice = Doc.select("div.productPrice>span>strong").text().replace(",", "").replace(" ", "");
            if(BatchPrice.equals("")) {
                BatchPrice = Doc.select("div.productPrice>span").eq(1).text().replace(",", "").replace(" ", "");
            }
            NewProductBatchPrice.setText(BatchPrice);

            //建議售價
            double Pricing = Math.round(Double.parseDouble(BatchPrice)*Double.parseDouble(JinghaoPricing));
            NewProductPricing.setText(String.valueOf(Pricing).replace(".0",""));
            //	商品描述
            String Describe = Doc.select("ul.listDisc").html().replaceAll("'", "");
            NewProductDescribe.setText(Describe);
            //  商品備註
            String Remark = Doc.select("div.productPromotion>span").html().replace("<br>","\n");;
            NewProductRemark.setText(Remark);
            //供貨狀態
            String SupplyStatus = Doc.select("a.AddBuy>img").attr("src");
            if(SupplyStatus.equals("/images/btnAddCart.gif"))   SupplyStatus = "貨物充足";
            else {
                SupplyStatus = Doc.select("a.BackInStock>img").attr("src");
                if(SupplyStatus.equals("/images/btnBackInStock.gif"))   SupplyStatus = "缺貨中";
            }
            NewProductSupplyStatus.setText(SupplyStatus);
            return Product_Enum.SingleUpdateStatus.更新完成;
        }
    }
    private void HandleCookie(String CookieString){
        LoginCookie.clear();
        if(!CookieString.equals("") && CookieString.contains("=")) {
            String[] Cookie_Split = CookieString.replace("{", "").replace("}", "").split("=");
            LoginCookie.put(Cookie_Split[0], Cookie_Split[1]);
        }else
            LoginCookie.put("Empty","");
    }
    private Connection.Response SearchUrl(String Url, String[] Parameter){
        Connection.Response Response = null;
        boolean Connect_Check = true;
        while(Connect_Check){
            ToolKit.SSLAttack();
            Connect_Check = false;
            try {
                Response = Jsoup.connect(Url)
                        .data("LIST_LINE_ID", Parameter[0])
                        .data("orgID", Parameter[1])
                        .data("itemId", Parameter[2])
                        .data("Key", Parameter[0])
                        .header("Accept", "application/json, text/plain, */*")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("Connection", "keep-alive")
                        .cookies(LoginCookie)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .method(Connection.Method.POST)
                        .timeout(100000)
                        .execute();
            } catch (Exception Ex) {
                if(Ex.toString().contains("Connection timed out: connect")) Connect_Check = true;
                else    Ex.printStackTrace();
            }
        }
        return Response;
    }
    //	獲取登入驗證圖
    public Image getLoginCaptcha() throws Exception {
        Connection.Response Response = null;
        Boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://iorder.unitech.com.tw:8080/Captcha/Captcha").ignoreContentType(true).method(Connection.Method.GET).timeout(50000).execute();
                JudgeTimeOut = false;
            } catch (Exception Ex) {
                if(!Ex.toString().contains("Connection timed out: connect"))
                    Ex.printStackTrace();
            }
        }
        CaptureCookie = Response.cookies();
        ByteArrayInputStream ByteStream = new ByteArrayInputStream(Response.bodyAsBytes());
        Image Image = new Image(ByteStream);
        ByteStream.close();
        return Image;
    }
    public HashMap<Boolean,Map> Login(String companyTax, String account, String password, String captchaText, Label CaptchaError){
        HashMap<Boolean,Map> LoginCookieMap = new HashMap<>();
        Connection.Response Response = null;
        Boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://iorder.unitech.com.tw:8080/Account/Login")
                        .cookies(CaptureCookie)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .data("ORG_ID","151")
                        .data("X-Requested-With","XMLHttpRequest")
                        .data("CompanyNo",companyTax)
                        .data("Account",account)
                        .data("Password",password)
                        .data("Captcha",captchaText)
                        .method(Connection.Method.POST)
                        .timeout(50000)
                        .execute();
                JudgeTimeOut = false;
            } catch (Exception Ex) {
                if(!Ex.toString().contains("Connection timed out: connect"))
                    Ex.printStackTrace();
            }
        }
        LoginCookie = Response.cookies();
        String ResBody = Response.body();
        if(ResBody.contains("\"Success\":true")){
            LoginCookieMap.put(true, LoginCookie);
            CaptchaError.setText("");
        }else {
            LoginCookieMap.put(false,null);
            if(ResBody.contains("帳號、使用者代碼或密碼錯誤"))
                CaptchaError.setText("輸入資訊錯誤!");
            if(ResBody.contains("驗證碼錯誤"))
                CaptchaError.setText("驗證碼輸入錯誤!");
        }
        return LoginCookieMap;
    }
}
