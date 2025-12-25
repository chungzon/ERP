package ERP.Model.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.ToolKit.ToolKit;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SynnexGetWebInfo_Model {
    private String[] day = new String []{"Mon", "Tue", "Web", "Thu", "Fri", "Sat", "Sun"};
    private String[] month = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe;
    private Map LoginCookie = new HashMap();

    private ToolKit ToolKit;
    public SynnexGetWebInfo_Model(){
        this.ToolKit = ERPApplication.ToolKit;
    }
    public void setNewLabel(Label NewProductName, Label NewProductBatchPrice, Label NewProductPricing, Label NewProductSupplyStatus, TextArea NewProductDescribe){
        this.NewProductName = NewProductName;
        this.NewProductBatchPrice = NewProductBatchPrice;
        this.NewProductPricing = NewProductPricing;
        this.NewProductSupplyStatus = NewProductSupplyStatus;
        this.NewProductDescribe = NewProductDescribe;
    }
    public Product_Enum.SingleUpdateStatus getProductInfo(String Url, String ProductCode, String SynnexPricing, String CookieString) throws Exception {
        HandleCookie(CookieString);
        Response Response = SearchUrl(Url);
        if(Response.body().contains("※如果您尚未申請授權，請選擇立即申請，我們將有專人為您服務"))  return Product_Enum.SingleUpdateStatus.Cookie過期;
        else if(!Response.body().contains(ProductCode)) return Product_Enum.SingleUpdateStatus.商品下架;
        else {
            Document Doc = Response.parse();
            String ProductName = Doc.select("div#list_topproduct_title").text().replace("\'", "");
            NewProductName.setText(ProductName);

            //經銷價 (BatchPrice 未稅)
            String BatchPrice = Doc.select("td.text_red>div[align]").text().replace("NT", "").replace(",", "");
            if(BatchPrice.equals("進電下單"))
                BatchPrice = "999999";
            NewProductBatchPrice.setText(BatchPrice);

            //建議售價
            if(BatchPrice.equals("999999"))
                NewProductPricing.setText("999999");
            else {
                double Pricing = Math.round(Double.parseDouble(BatchPrice)*Double.parseDouble(SynnexPricing));
                NewProductPricing.setText(String.valueOf(Pricing).replace(".0",""));
            }

            //	商品敘述(移除?)
            Elements SubNewElements = Doc.select("table[class=subnewstext]");
            String Describe = SubNewElements.select("tr:eq(3)").toString().replaceAll("\n", "").replace("'", "");
            Describe = Describe.replaceAll("<tr>", "");
            String[] Describe_Sp = Describe.split("</tr>");
            Describe = Describe_Sp[0].replaceFirst(" ", "");
            if(Describe_Sp[0].equals("<td>&nbsp;</td>")){
                Describe = SubNewElements.select("tr:eq(5)").toString().replaceAll("\n", "").replaceAll("<tr>", "");
                Describe_Sp = Describe.split("</tr>");
                Describe = Describe_Sp[0].replaceFirst(" ", "");
            }
            NewProductDescribe.setText(Describe);

            //供貨狀態
            String SupplyStatus = "";
            String Response_Text = Doc.toString();
            String temp = Response_Text.substring(Response_Text.indexOf("庫存：")+3);
            temp = temp.substring(0,temp.indexOf(".gif")+4);
            String SupplyStatus_Picture = temp.substring(temp.lastIndexOf("/")+1);
            if(SupplyStatus_Picture.equals("icon_g.gif"))
                SupplyStatus = "庫存充足";
            if(SupplyStatus_Picture.equals("icon_y.gif"))
                SupplyStatus = "尚有少量庫存即將短缺";
            if(SupplyStatus_Picture.equals("icon_r.gif"))
                SupplyStatus = "庫存短缺";
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
                        .cookies(LoginCookie)
                        .header("Upgrade-Insecure-Requests", "1")
                        .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                        .ignoreHttpErrors(true)
                        .ignoreContentType(true)
                        .timeout(100000)
                        .method(Method.GET)
                        .execute();
            } catch (Exception Ex) {
                if(Ex.toString().contains("Connection timed out: connect")) Connect_Check = true;
                else    Ex.printStackTrace();
            }
        }
        return Response;
    }
    public String AutomaticLogin(String Account, String Password) throws Exception {
        String LoginCookieInfo = "";
        Response Response = null;
        try {
            ToolKit.SSLAttack();
            Response = Jsoup.connect("https://eo.synnex.com.tw/Login.aspx")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                    .timeout(10000)
                    .method(Method.GET)
                    .execute();
        } catch (Exception Ex) {    Ex.printStackTrace();   }
        LoginCookie = Response.cookies();
        Document Doc = Response.parse();
        String __VIEWSTATE = getLoginParameter(Doc);
        //	認證
        String LoginStatus = Login(Account, Password, __VIEWSTATE);
        if (!LoginStatus.contains("false"))
            LoginCookieInfo = LoginCookie.toString();
        return LoginCookieInfo;
    }
    private String getLoginParameter(Document Doc) {
        String __VIEWSTATE = "";
        Elements Elements = Doc.select("div");
        for (org.jsoup.nodes.Element Element : Elements) {
            String Parameter = Element.select("input").attr("name");
            if (Parameter.equals("__VIEWSTATE"))    __VIEWSTATE = Element.select("input").attr("value");
        }
        return __VIEWSTATE;
    }
    //認證
    private String Login(String Account, String Password, String __VIEWSTATE){
        Response Response = null;
        try {
            Response = Jsoup.connect("https://eo.synnex.com.tw/Handler/LoginHandler.aspx?timestamp=" + getTimeStamp())
                    .cookies(LoginCookie)
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                    .data("userid", Account)
                    .data("pwd", Password)
                    .timeout(10000)
                    .method(Method.POST)
                    .execute();
        } catch (Exception Ex) {    Ex.printStackTrace();   }
        LoginCookie = Response.cookies();
        try {
            Response = Jsoup.connect("https://eo.synnex.com.tw/Login.aspx?oldURL=%2findex.aspx")
                    .cookies(LoginCookie)
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36")
                    .data("__VIEWSTATE", __VIEWSTATE)
                    .data("txtAccount", Account)
                    .data("txtPassword", Password)
                    .data("btnLogin","確定")
                    .timeout(10000)
                    .ignoreHttpErrors(true)
                    .method(Method.POST)
                    .execute();
        }catch(Exception Ex){   Ex.printStackTrace();   }
        return Response.body();
    }

    private String getTimeStamp() throws Exception {
        //時間戳記轉換
        //format的格式可以任意
        String[] Day_Sp = date2Day(getNowTime()).split("@");
        DateFormat DateFormat = new SimpleDateFormat("dd%20yyyy%20HH:mm:ss");
        String TimeStamp = DateFormat.format(new Date());
        TimeStamp = day[Integer.parseInt(Day_Sp[0])-1] + "%20" + month[Integer.parseInt(Day_Sp[1])-1] + "%20" + TimeStamp + "%20" + "GMT+0800%20(%E5%8F%B0%E5%8C%97%E6%A8%99%E6%BA%96%E6%99%82%E9%96%93)";
        return TimeStamp;
    }
    private String date2Day( String dateString ) throws Exception{
        SimpleDateFormat dateStringFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = dateStringFormat.parse( dateString );
        SimpleDateFormat date2DayFormat = new SimpleDateFormat( "u@M" );
        return date2DayFormat.format( date );
    }
    //	取得現在時間
    private String getNowTime() {
        SimpleDateFormat DateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return DateFormat.format(new Date());
    }
}
