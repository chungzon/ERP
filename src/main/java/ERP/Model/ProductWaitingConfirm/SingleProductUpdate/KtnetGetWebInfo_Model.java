package ERP.Model.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
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
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import static org.jsoup.Connection.Method.GET;

public class KtnetGetWebInfo_Model {
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe;
    private Map LoginCookie1 = new HashMap();
    private Map LoginCookie2 = new HashMap();

    private ToolKit ToolKit;
    public KtnetGetWebInfo_Model(){
        this.ToolKit = ERPApplication.ToolKit;
    }
    public void setNewLabel(Label NewProductName, Label NewProductBatchPrice, Label NewProductPricing, Label NewProductSupplyStatus, TextArea NewProductDescribe){
        this.NewProductName = NewProductName;
        this.NewProductBatchPrice = NewProductBatchPrice;
        this.NewProductPricing = NewProductPricing;
        this.NewProductSupplyStatus = NewProductSupplyStatus;
        this.NewProductDescribe = NewProductDescribe;
    }
    public Product_Enum.SingleUpdateStatus getProductInfo(String Url, String ProductOther, String CookieString) throws Exception {
        HandleCookie(CookieString);
        Response Response = SearchUrl(Url);
        if(!Response.body().contains("旻昱科技有限公司") && !Response.body().contains("經銷價"))   return Product_Enum.SingleUpdateStatus.Cookie過期;
        else if(Response.body().contains("產品不存在或已下架"))  return Product_Enum.SingleUpdateStatus.商品下架;
        else {
            Document Doc = Response.parse();
            //  品名為 Product_Url 內的 Other
            NewProductName.setText(ProductOther);
            //經銷價 (BatchPrice 未稅)
            String BatchPrice = Doc.select("div.price.trueprice.clearfix>span.num>span.numTxt").text();
            if(BatchPrice.equals("Call") || BatchPrice.equals(""))
                BatchPrice = "999999";
            NewProductBatchPrice.setText(BatchPrice);
            //建議售價(含稅)
            String SuggestPrice = Doc.select("div.price.suggested.clearfix>span.num>span.numTxt").text();
            if(SuggestPrice.contains("Call") || SuggestPrice.equals(""))
                SuggestPrice = "999999";
            NewProductPricing.setText(SuggestPrice);
            //供貨狀態
            String SupplyStatus = Doc.select("div.arrival.clearfix").text();
            NewProductSupplyStatus.setText(SupplyStatus);
            //	商品敘述
            String Describe = Doc.select("div.info>div.describe").toString().replace("\'", "");
            NewProductDescribe.setText(Describe);
            return Product_Enum.SingleUpdateStatus.更新完成;
        }
    }
    private void HandleCookie(String CookieString){
        String[] Sp;
        if(CookieString.contains("@")) {
            Sp = CookieString.split("@");
            if(!Sp[0].isEmpty() || !Sp[1].isEmpty()){
                CookieString=Sp[0].replace("{", "");
                CookieString=CookieString.replace("}", "");
                String[] loginCookies_Split = CookieString.split("=");
                LoginCookie1 = new HashMap<String, String>();
                LoginCookie1.put(loginCookies_Split[0], loginCookies_Split[1]);

                CookieString=Sp[1].replace("{", "");
                CookieString=CookieString.replace("}", "");
                loginCookies_Split = CookieString.split("=");
                LoginCookie2 = new HashMap<String, String>();
                LoginCookie2.put(loginCookies_Split[0], loginCookies_Split[1]);
            }
        }
    }
    private Response SearchUrl(String Url){
        Response Response = null;
        boolean Connect_Check = true;
        while(Connect_Check){
            ToolKit.SSLAttack();
            Connect_Check = false;
            try {
                Response = Jsoup.connect(Url)
                        .cookies(LoginCookie1)
                        .cookies(LoginCookie2)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
                        .timeout(100000)
                        .method(GET).execute();
            } catch (Exception Ex) {
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
        String[] Parameter = new String[5];
        Response Response = null;
        try {
            ToolKit.SSLAttack();
            Response = Jsoup.connect("http://www.ktnet.com.tw/index.aspx").timeout(50000).method(GET).execute();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        LoginCookie1 = Response.cookies();
        LoginCookie2.clear();

        getLoginParameter(Parameter);
        LoginCookie2 = Login("https://www.ktnet.com.tw/member/login.aspx?redirecturl=%2fproductindex.aspx", Account, Password, Parameter).cookies();

        if (LoginCookie2.size() > 0)
            LoginCookieInfo = LoginCookie1 + "@" + LoginCookie2 + "@" + Parameter[2] + "@" + Parameter[3] + "@" + Parameter[4];
        return LoginCookieInfo;
    }
    private void getLoginParameter(String[] Parameter) throws Exception {
        Response Response = null;
        try {
            Response = Jsoup.connect("https://www.ktnet.com.tw/member/login.aspx?redirecturl=/productindex.aspx").cookies(LoginCookie1).timeout(10000).method(Method.GET).execute();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        /** 獲得登入參數 **/
        Document Doc = Response.parse();
        Elements newsHeadlines = Doc.getElementsByTag("input");
        for (Element tbody : newsHeadlines) {
            if (tbody.attr("name").equals("__VIEWSTATE")) Parameter[2] = tbody.attr("value");
            if (tbody.attr("name").equals("__VIEWSTATEGENERATOR")) Parameter[3] = tbody.attr("value");
            if (tbody.attr("name").equals("__EVENTVALIDATION")) Parameter[4] = tbody.attr("value");
        }
    }
    private Response Login(String Url, String Account, String Password, String[] Sp){
        Response Response = null;
        boolean Connect_Check = true;
        while(Connect_Check){
            ToolKit.SSLAttack();
            Connect_Check = false;
            try {
                Response = Jsoup.connect(Url)
                        .data("__LASTFOCUS", "").data("ctl00_ToolkitScriptManager1_HiddenField", "")
                        .data("__EVENTTARGET", "").data("__EVENTARGUMENT", "")
                        .data("__VIEWSTATE", Sp[2])
                        .data("ctl00$CPH1$tb_UserName", Account)
                        .data("ctl00$CPH1$tb_Pass", Password)
                        .data("ctl00$CPH1$btn_Login", "登入")
                        .data("__VIEWSTATEGENERATOR", Sp[3])
                        .data("__EVENTVALIDATION", Sp[4])
                        .ignoreHttpErrors(true)
                        .cookies(LoginCookie1)
                        .cookies(LoginCookie2)
                        .timeout(100000)
                        .method(Connection.Method.GET).execute();
            } catch (Exception Ex) {
                if(Ex.toString().contains("Connection timed out: connect")) Connect_Check = true;
                else    Ex.printStackTrace();
            }
        }
        return Response;
    }
}
