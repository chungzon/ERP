package ERP.Model.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.SingleUpdateStatus;
import ERP.ToolKit.ToolKit;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Genb2bGetWebInfo_Model {
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe;
    private Map<String,String> LoginCookie = new HashMap<>();

    private ToolKit ToolKit;
    public Genb2bGetWebInfo_Model(){
        this.ToolKit = ERPApplication.ToolKit;
    }
    public void setNewLabel(Label NewProductName, Label NewProductBatchPrice, Label NewProductPricing, Label NewProductSupplyStatus, TextArea NewProductDescribe){
        this.NewProductName = NewProductName;
        this.NewProductBatchPrice = NewProductBatchPrice;
        this.NewProductPricing = NewProductPricing;
        this.NewProductSupplyStatus = NewProductSupplyStatus;
        this.NewProductDescribe = NewProductDescribe;
    }

    public SingleUpdateStatus getProductInfo(String Url, String ProductCode, String ProductOther, String CookieString) throws Exception {
        HandleCookie(CookieString);
        Response Response = SearchUrl(Url);
        if(!Response.body().contains("訂單查詢"))  return Product_Enum.SingleUpdateStatus.Cookie過期;
        else {
            Document Doc = Response.parse();
            String urlBody = Response.body();

            boolean isWelfareGoods = false;
            if(Url.contains("&BCode="))
                isWelfareGoods = true;

            String productName = "",productDescribe = "", suggestPrice = "",batchPrice = "";
            productName = Doc.select("span#ProInfo_lblProName").text().replace("\'", "");
            //	商品敘述
            productDescribe = Doc.select("div#ProDetails_con_one_1").toString().replace("?","").replace("\'", "");
            String ProID;
            if(isWelfareGoods) {
                ProID = Url.substring(Url.indexOf("ProID=")+6,Url.indexOf("&CategoryNo="));
                String BCode = Url.substring(Url.lastIndexOf("BCode=")+6);
                Response = SearchUrl("http://www.genb2b.com/Product/ProductBTO.aspx?callBack=GetProPriceInfo&ProID="+ ProID + "&isB2B=True&BCode="+BCode);
            }else {
                ProID = Url.substring(Url.indexOf("ProID=")+6,Url.indexOf("&adid"));
                Response = SearchUrl("http://www.genb2b.com/Product/ProductBTO.aspx?callBack=GetProPriceInfo&ProID="+ ProID + "&isB2B=True&BCode=");
            }

            String PriceJson = Response.body().replace(",", "").replace("\"", "");
            if(PriceJson.contains("{spCProPrice:") && PriceJson.contains("spProPrice:")) {
                //建議售價
                suggestPrice = PriceJson.substring(PriceJson.indexOf(":")+1,PriceJson.indexOf("spProPrice:"));
                //經銷價 (BatchPrice 未稅)
                batchPrice = PriceJson.substring(PriceJson.indexOf("spProPrice:")+11,PriceJson.lastIndexOf("}"));

            }
            if(!urlBody.contains(ProductCode) || urlBody.contains("404錯誤頁面") || batchPrice.equals("0")){
                return SingleUpdateStatus.商品下架;
            }else{
                NewProductName.setText(productName);
                NewProductDescribe.setText(productDescribe);
                NewProductPricing.setText(suggestPrice);
                NewProductBatchPrice.setText(batchPrice);
                NewProductSupplyStatus.setText(ProductOther);
                return Product_Enum.SingleUpdateStatus.更新完成;
            }
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
                Response = Jsoup.connect(Url)
                        .cookies(LoginCookie)
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(100000)
                        .method(Connection.Method.GET)
                        .execute();
            } catch (IOException Ex) {
                if(Ex.toString().contains("Connection timed out: connect"))
                    Connect_Check = true;
                else
                    Ex.printStackTrace();
            }
        }
        return Response;
    }
    public String AutomaticLogin(String Account, String Password) throws Exception {
        String LoginCookieInfo = "";
        Response Response = null;
        try {
            ToolKit.SSLAttack();
            Response = Jsoup.connect("https://www.genb2b.com/Login.aspx?RedirUrl=/index.aspx")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
                    .timeout(50000)
                    .method(Method.GET)
                    .execute();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        Map<String, String> WebCookie = Response.cookies();
        Document Doc = Response.parse();
        String[] Parameter = getLoginParameter(Doc).split("@");
        String LoginStatus = Login(Parameter, Account, Password, WebCookie).body();

        if (LoginStatus.contains("訂單查詢"))
            LoginCookieInfo =  LoginCookie.toString();
        return LoginCookieInfo;
    }
    private String getLoginParameter(Document Doc) {
        String __VIEWSTATE = "",__VIEWSTATEGENERATOR = "";
        Elements Elements = Doc.select("div.aspNetHidden>input");  //A,B
        for (Element tbody : Elements) {
            String judge = tbody.attr("name");
            if (judge.equals("__VIEWSTATE")) {
                __VIEWSTATE = tbody.attr("value");
            } else if (judge.equals("__VIEWSTATEGENERATOR")) {
                __VIEWSTATEGENERATOR = tbody.attr("value");
            }
        }
        return __VIEWSTATE + "@" + __VIEWSTATEGENERATOR;
    }
    private Response Login(String[] Parameter, String Account, String Password, Map<String, String> WebCookie) {
        Response Response = null;
        try {
            Response = Jsoup.connect("https://www.genb2b.com/Login.aspx?RedirUrl=/index.aspx")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                    .data("radlLoginType","2")
                    .data("btnSend_h","登入")
                    .data("btnLogin",Account)
                    .data("btnPassword",Password)
                    .data("__VIEWSTATE", Parameter[0])
                    .data("__VIEWSTATEGENERATOR",Parameter[1])
                    .cookies(WebCookie)
                    .timeout(10000)
                    .method(Method.POST)
                    .execute();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        LoginCookie.clear();
        LoginCookie.put("ASP.NET_SessionId", WebCookie.get("ASP.NET_SessionId"));

        try {
            Response = Jsoup.connect("http://www.genb2b.com/HttpsSessionAddShopID.aspx?url=%2fIndex.aspx&SessionID=ogw5uxtt1rdm413ez01pmffx")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
                    .cookies(LoginCookie)
                    .timeout(10000)
                    .method(Method.GET)
                    .execute();
        }catch(Exception Ex){
            Ex.printStackTrace();
        }
        return Response;
    }
}
