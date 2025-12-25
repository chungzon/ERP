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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class WeblinkGetWebInfo_Model {
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe;
    private Map<String,String> CaptureCookie = new HashMap<>();
    private Map<String,String> LoginCookie = new HashMap<>();

    private ToolKit ToolKit;
    public WeblinkGetWebInfo_Model(){
        this.ToolKit = ERPApplication.ToolKit;
    }

    public void setNewLabel(Label NewProductName, Label NewProductBatchPrice, Label NewProductPricing, Label NewProductSupplyStatus, TextArea NewProductDescribe){
        this.NewProductName = NewProductName;
        this.NewProductBatchPrice = NewProductBatchPrice;
        this.NewProductPricing = NewProductPricing;
        this.NewProductSupplyStatus = NewProductSupplyStatus;
        this.NewProductDescribe = NewProductDescribe;
    }
    public Product_Enum.SingleUpdateStatus getProductInfo(String Url, String CookieString) throws Exception {
        HandleCookie(CookieString);
        Response Response = SearchUrl(Url);
        if (Response.body().contains("我要申請註冊")) return Product_Enum.SingleUpdateStatus.Cookie過期;
        else if(Response.body().contains("很抱歉, 查無此料號"))    return Product_Enum.SingleUpdateStatus.商品下架;
        else {
            Document Doc = Response.parse();
            String ProductName = Doc.select("td.list_title").text().replace("  ", "").replace("'", " ");
            if(ProductName.equals(""))
                ProductName = Doc.select("table.text_black_12>tbody>tr:eq(3)>td:eq(1)").text();
            NewProductName.setText(ProductName);
            //  成本BatchPrice (byTheSellingPrice)
            String BatchPrice = Doc.select("td.text_red_14>span>strong").text();
            if(!BatchPrice.contains("CALL")){
                //經銷價
                BatchPrice = BatchPrice.replace("$", "").replace(",", "");
                NewProductBatchPrice.setText(BatchPrice);
                //建議售價
                String SuggestPrice;
                if(Doc.toString().contains("/ec/img/listicon/icon_menu07-2.jpg"))
                    SuggestPrice = Doc.select("tbody").get(32).select("tr:eq(5)").select("td").get(1).text().replace("$", "").replace(",", "");
                else
                    SuggestPrice = Doc.select("tbody").get(18).select("tr:eq(5)").select("td").get(1).text().replace("$", "").replace(",", "");
                NewProductPricing.setText(SuggestPrice);
            }else{
                NewProductBatchPrice.setText("999999");
                NewProductPricing.setText("999999");
            }
            //	商品敘述
            String Describe = Doc.select("table[class=table_border]").select("table[width=98%]").html().replace("'", "");
            NewProductDescribe.setText(Describe);

            int SupplyCount = 0;
            Elements Status  = Doc.select("td.text_black_12>img");
            for (Element status : Status) {
                String Picture = status.toString();
                if (Picture.contains("iconTW")) {
                    Picture = Picture.substring(Picture.indexOf("TW") + 2, Picture.indexOf(".gif"));
                    if (Picture.contains("03"))
                        SupplyCount++;
                } else if (Picture.contains("iconTC")) {
                    Picture = Picture.substring(Picture.indexOf("TC") + 2, Picture.indexOf(".gif"));
                    if (Picture.contains("03"))
                        SupplyCount++;
                } else if (Picture.contains("iconKS")) {
                    Picture = Picture.substring(Picture.indexOf("KS") + 2, Picture.indexOf(".gif"));
                    if (Picture.contains("03"))
                        SupplyCount++;
                }
            }
            String SupplyStatus;
            if(SupplyCount==3)  SupplyStatus = "缺貨中";
            else if(Doc.toString().contains("/ec/img/07product/contactus.jpg")) SupplyStatus = "請洽經銷商";
            else    SupplyStatus = "正常供貨";
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
    private Response SearchUrl(String Url){
        Response Response = null;
        boolean Connect_Check = true;
        while(Connect_Check){
            ToolKit.SSLAttack();
            Connect_Check = false;
            try {
                Response = Jsoup.connect(Url)
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                        .cookies(LoginCookie)
                        .timeout(100000)
                        .method(Method.GET)
                        .ignoreContentType(true)
                        .ignoreHttpErrors(true)
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
        Response Response = null;
        boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://channel.weblink.com.tw/ec/couponPic?")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                        .ignoreContentType(true)
                        .method(Method.GET)
                        .timeout(50000)
                        .execute();
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
    public HashMap<Boolean,Map<String,String>> Login(String Account, String Password, String CaptchaText, Label CaptchaError){
        HashMap<Boolean,Map<String,String>> LoginCookieMap = new HashMap<>();
        Response Response = null;
        boolean JudgeTimeOut = true;
        while(JudgeTimeOut) {
            try {
                Response = Jsoup.connect("https://channel.weblink.com.tw/ec/login.do")
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                        .ignoreContentType(true)
                        .cookies(CaptureCookie)
                        .data("action","login")
                        .data("uid", Account)
                        .data("pwd", Password)
                        .data("userstring",CaptchaText)
                        .method(Method.POST)
                        .timeout(50000)
                        .execute();
                JudgeTimeOut = false;
            } catch (Exception Ex) {
                if(!Ex.toString().contains("Connection timed out: connect"))
                    Ex.printStackTrace();
            }
        }
        try {
            Response = Jsoup.connect("https://channel.weblink.com.tw/ec/action/pdt/category.do?fnid=ec200_Purchase&cfnid=ec230_category_qry")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                    .cookies(CaptureCookie)
                    .timeout(50000)
                    .method(Method.GET)
                    .execute();
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        String ResBody = Response.body();
        if(!ResBody.contains("我要申請註冊")){
            CaptchaError.setText("");
            LoginCookieMap.put(true,CaptureCookie);
        }else {
            CaptchaError.setText("登入失敗!");
            LoginCookieMap.put(false,null);
        }
        return LoginCookieMap;
    }
}
