package ERP.Model.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.Connection.Method;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class XanderGetWebInfo_Model {
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe, NewProductRemark;
    private Map CaptureCookie = new HashMap();
    private Map LoginCookie = new HashMap();

    private ToolKit ToolKit;
    public XanderGetWebInfo_Model(){
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
    public Product_Enum.SingleUpdateStatus getProductInfo(String Url, String ProductCode, String CookieString) throws Exception {
        HandleCookie(CookieString);
        Response Response = SearchUrl(Url);
        if(!Response.body().contains(ProductCode) && Response.body().contains("很抱歉，您所找尋的頁面無法瀏覽或不存在，可能網頁連結錯誤，或已經下架！"))   return Product_Enum.SingleUpdateStatus.商品下架;
        else if (!Response.body().contains("展源浩科技有限公司")) return Product_Enum.SingleUpdateStatus.Cookie過期;
        else {
            Document Doc = Response.parse();
            String ProductName = Doc.select("h1.title_name.pname").text().replace("\'", "");
            NewProductName.setText(ProductName);

            String SuggestPrice = Doc.select("ul.pd_price>li").eq(1).text();
            if(SuggestPrice.contains("建議售價")) {
                SuggestPrice = SuggestPrice.substring(SuggestPrice.indexOf("：")+1);
                SuggestPrice = SuggestPrice.replace("$", "").replace(" ","").replace("(含稅)", "").replace(",", "");
                if(SuggestPrice.equals("請洽內業"))
                    SuggestPrice = "999999";
                NewProductPricing.setText(SuggestPrice);
            }

            //經銷價(batchprice 未稅、singlePrice 含稅)
            String Judge = Doc.select("ul.pd_price>li").eq(3).text();
            String BatchPrice = "", Remark = "", SupplyStatus = "";
            if(Judge.contains("促 銷 價")) {
                Judge = Judge.substring(Judge.indexOf("：")+1);
                Judge = Judge.replace("$", "").replace(" ","").replace(",", "");

                BatchPrice = Judge.substring(0,Judge.indexOf("／"));
                BatchPrice = BatchPrice.replace("(未稅)", "");
                if(BatchPrice.equals("請洽內業"))
                    BatchPrice = "999999";

                //備註
                Remark = Doc.select("ul.pd_price>li").eq(4).text();
                //供貨狀態
                SupplyStatus = Doc.select("ul.pd_price>li").eq(5).text();
                if(SupplyStatus.contains("供貨狀態"))
                    SupplyStatus = SupplyStatus.substring(SupplyStatus.indexOf("：")+1).replace(" ", "");
            }else if(Judge.contains("VIP 價")) {
                String TotalPrice =  Doc.select("ul.pd_price>li").eq(3).text();

                TotalPrice = TotalPrice.substring(TotalPrice.indexOf("：")+1).replace("$", "").replace(" ","").replace(",", "");
                BatchPrice = TotalPrice.substring(0,TotalPrice.indexOf("／"));
                BatchPrice = BatchPrice.replace("(未稅)", "");
                if(BatchPrice.equals("請洽內業"))
                    BatchPrice = "999999";

                //供貨狀態
                SupplyStatus = Doc.select("ul.pd_price>li").eq(4).text();
                if(SupplyStatus.contains("供貨狀態")) {
                    SupplyStatus = SupplyStatus.substring(SupplyStatus.indexOf("：")+1).replace(" ", "");
                }
            }else if(Judge.contains("促銷期間")) {
                String TotalPrice =  Doc.select("ul.pd_price>li").eq(2).text();

                TotalPrice = TotalPrice.substring(TotalPrice.indexOf("：")+1).replace("$", "").replace(" ","").replace(",", "");
                BatchPrice = TotalPrice.substring(0,TotalPrice.indexOf("／"));
                BatchPrice = BatchPrice.replace("(未稅)", "");
                if(BatchPrice.equals("請洽內業"))
                    BatchPrice = "999999";

                //供貨狀態
                SupplyStatus = Doc.select("ul.pd_price>li").eq(4).text();
                if(SupplyStatus.contains("供貨狀態"))
                    SupplyStatus = SupplyStatus.substring(SupplyStatus.indexOf("：")+1).replace(" ", "");
                Remark = Judge;
            }else if(Judge.contains("供貨狀態")) {
                String TotalPrice =  Doc.select("ul.pd_price>li").eq(2).text();

                TotalPrice = TotalPrice.substring(TotalPrice.indexOf("：")+1).replace("$", "").replace(" ","").replace(",", "");

                BatchPrice = TotalPrice.substring(0,TotalPrice.indexOf("／"));
                BatchPrice = BatchPrice.replace("(未稅)", "");
                if(BatchPrice.equals("請洽內業"))
                    BatchPrice = "999999";

                //供貨狀態
                Judge = Judge.substring(Judge.indexOf("：")+1);
                SupplyStatus = Judge.replace(" ", "");
            }
            NewProductBatchPrice.setText(BatchPrice);
            NewProductSupplyStatus.setText(SupplyStatus);
            NewProductRemark.setText(Remark);
            String Describe = Doc.select("div.col-md-7.col-sm-7.col-xs-12.pro_copywriter>ul").toString().replace("'", "");
            NewProductDescribe.setText(Describe);
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
    private Response SearchUrl(String Url){
        Response Response = null;
        boolean Connect_Check = true;
        while(Connect_Check){
            ToolKit.SSLAttack();
            Connect_Check = false;
            try {
                Response = Jsoup.connect(Url).cookies(LoginCookie).ignoreContentType(true).method(Connection.Method.GET).timeout(100000).execute();
            } catch (Exception Ex) {
                if(Ex.toString().contains("Connection timed out: connect")) Connect_Check = true;
                else {
                    Ex.printStackTrace();
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
        return Response;
    }
    //	獲取登入驗證圖
    public Image getLoginCaptcha() throws Exception {
        Response Response = null;
        Boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://www.xander.com.tw/b2b/global/CaptchaImage.aspx").ignoreContentType(true).method(Method.GET).timeout(50000).execute();
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
    public HashMap<Boolean,Map> Login(String CompanyTax, String Account, String Password, String CaptchaText, Label CaptchaError){
        HashMap<Boolean,Map> LoginCookieMap = new HashMap<>();
        getWebCookie();
        Response Response = null;
        Boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://www.xander.com.tw/b2b/login/login_act.aspx")
                        .cookies(LoginCookie)
                        .cookies(CaptureCookie)
                        .data("cureg",CompanyTax)
                        .data("acc",Account)
                        .data("password",Password)
                        .data("checkcode",CaptchaText)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .method(Method.POST)
                        .timeout(50000)
                        .execute();
                JudgeTimeOut = false;
            } catch (Exception Ex) {
                if(!Ex.toString().contains("Connection timed out: connect"))
                    Ex.printStackTrace();
            }
        }
        String ResBody = Response.body();
        if(ResBody.contains("success:true") && CaptureCookie.size() > 0){
            LoginCookieMap.put(true,CaptureCookie);
            CaptchaError.setText("");
        }else {
            LoginCookieMap.put(false,null);
            if(ResBody.contains("不明帳號 或 密碼錯誤 ") || ResBody.contains("欄位輸入錯誤"))
                CaptchaError.setText("輸入資訊錯誤!");
            else if(ResBody.contains("驗證碼錯誤"))
                CaptchaError.setText("驗證碼輸入錯誤!");
        }
        return LoginCookieMap;
    }
    private void getWebCookie(){
        Response Response = null;
        boolean JudgeTimeOut = true;
        while(JudgeTimeOut){
            ToolKit.SSLAttack();
            JudgeTimeOut = false;
            try {
                Response = Jsoup.connect("https://www.xander.com.tw/b2b/login/")
                        .timeout(10000)
                        .method(Method.GET)
                        .execute();
            } catch (Exception Ex) {
                if(Ex.toString().contains("Connection timed out: connect") || Ex.toString().contains("connect timed out")) {
                    JudgeTimeOut = true;
                }else {
                    Ex.printStackTrace();
                }
            }
        }
        LoginCookie = Response.cookies();
    }
}
