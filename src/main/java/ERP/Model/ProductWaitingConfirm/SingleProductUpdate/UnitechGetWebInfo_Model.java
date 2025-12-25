package ERP.Model.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.ToolKit.ToolKit;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.Connection.Method;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class UnitechGetWebInfo_Model {
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe, NewProductRemark;
    private Map<String,String> CaptureCookie = new HashMap<>();
    private Map<String,String> LoginCookie = new HashMap<>();

    private ToolKit ToolKit;
    public UnitechGetWebInfo_Model(){
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
    public Product_Enum.SingleUpdateStatus getProductInfo(String productCode, String ProductOther, String UnitechPricing, String CookieString) throws Exception {
        String[] Parameter = ProductOther.split("@");
        HandleCookie(CookieString);
        Document Doc = SearchUrl("https://iorder.unitech.com.tw/Product/ProductDetail", productCode.substring(0, productCode.indexOf("@")), Parameter).parse();
        if (Doc.toString().contains("加入 iOrder 會員")) return Product_Enum.SingleUpdateStatus.Cookie過期;
        else if(Doc.toString().contains("驗證碼不正確")) return Product_Enum.SingleUpdateStatus.驗證碼錯誤;
        else if(Doc.toString().contains("找不到資源") || Doc.toString().contains("無此商品資料"))    return Product_Enum.SingleUpdateStatus.商品下架;
        else {
            NewProductName.setText(Parameter[3]);
            //	經銷價(促銷價)
            String BatchPrice = Doc.select("div.productPrice>span>strong").text().replace(",", "");
            if(BatchPrice.equals(""))
                BatchPrice = Doc.select("div.productPrice>span").text().replace(",", "");
            NewProductBatchPrice.setText(BatchPrice);

            //建議售價
            double Pricing = Math.round(Double.parseDouble(BatchPrice)*Double.parseDouble(UnitechPricing));
            NewProductPricing.setText(String.valueOf(Pricing).replace(".0",""));
            //	商品描述
            String Describe = Doc.select("ul.listDisc").html().replaceAll("'", "");
            NewProductDescribe.setText(Describe);
            //  商品備註
            String Remark = Doc.select("div.productPromotion>span").html().replace("<br>","\n");
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
    private Response SearchUrl(String Url, String productCode, String[] Parameter){
        Response Response = null;
        boolean Connect_Check = true;
        while(Connect_Check){
            ToolKit.SSLAttack();
            Connect_Check = false;
            try {
                Url = "https://iorder.unitech.com.tw/Product/ProductDetail";
                Response = Jsoup.connect(Url)
                        .data("LIST_LINE_ID", Parameter[0])
                        .data("orgID", Parameter[1])
                        .data("itemId", Parameter[2])
                        .data("Key", productCode)
                        .header("Accept", "application/json, text/plain, */*")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("Connection", "keep-alive")
                        .cookies(LoginCookie)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .method(Method.POST)
                        .timeout(100000)
                        .execute();
//                Response = Jsoup.connect(Url)
//                        .data("LIST_LINE_ID", Parameter[0])
//                        .data("orgID", Parameter[1])
//                        .data("itemId", Parameter[2])
//                        .data("Key", productCode)
//                        .header("Accept", "application/json, text/plain, */*")
//                        .header("Content-Type", "application/json;charset=UTF-8")
//                        .header("Connection", "keep-alive")
//                        .cookies(LoginCookie)
//                        .ignoreContentType(true)
//                        .ignoreHttpErrors(true)
//                        .method(Method.POST)
//                        .timeout(100000)
//                        .execute();
            } catch (Exception Ex) {
                if(Ex.toString().contains("Connection timed out: connect")) Connect_Check = true;
                else    Ex.printStackTrace();
            }
        }
        return Response;
    }
    //	獲取登入驗證圖
    public Image getLoginCaptcha() throws Exception {
        Response Response = null;
        boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://iorder.unitech.com.tw/Captcha/Captcha").ignoreContentType(true).method(Method.GET).timeout(50000).execute();
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
    public HashMap<Boolean,Map<String,String>> Login(String CompanyTax, String Account, String Password, String CaptchaText, Label CaptchaError){
        HashMap<Boolean,Map<String,String>> LoginCookieMap = new HashMap<>();
        Response Response = null;
        boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://iorder.unitech.com.tw/Account/Login").cookies(CaptureCookie).ignoreContentType(true).ignoreHttpErrors(true)
                        .data("CompanyNo",CompanyTax)
                        .data("Account",Account)
                        .data("Password",Password)
                        .data("Captcha",CaptchaText)
                        .method(Method.POST)
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
