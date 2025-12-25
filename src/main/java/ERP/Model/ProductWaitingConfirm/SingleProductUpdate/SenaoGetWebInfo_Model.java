package ERP.Model.ProductWaitingConfirm.SingleProductUpdate;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.ToolKit.ConfigPath;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class SenaoGetWebInfo_Model {
    private ToolKit ToolKit;
    private WebDriver ChromeWebDriver;
    private Label NewProductName, NewProductBatchPrice, NewProductPricing, NewProductSupplyStatus;
    private TextArea NewProductDescribe, NewProductRemark;
    public SenaoGetWebInfo_Model(){
        this.ToolKit = ERPApplication.ToolKit;
        System.setProperty("webdriver.chrome.driver", ConfigPath.ChromeDriver);
    }
    public void setNewLabel(Label NewProductName, Label NewProductBatchPrice, Label NewProductPricing, Label NewProductSupplyStatus, TextArea NewProductDescribe, TextArea NewProductRemark){
        this.NewProductName = NewProductName;
        this.NewProductBatchPrice = NewProductBatchPrice;
        this.NewProductPricing = NewProductPricing;
        this.NewProductSupplyStatus = NewProductSupplyStatus;
        this.NewProductDescribe = NewProductDescribe;
        this.NewProductRemark = NewProductRemark;
    }
    public Product_Enum.SingleUpdateStatus getProductInfo(String Url, String WeblinkPricing, String account, String password) throws Exception {
        SearchUrlByWebDriver(Url,account,password);
        String ProductName = ChromeWebDriver.findElement(By.cssSelector("div.product-name")).getAttribute("textContent");
        NewProductName.setText(ProductName);

        //成本BatchPrice (byTheSellingPrice)
        String batchPrice = ChromeWebDriver.findElement(By.cssSelector("div.price>b.fn-md")).getAttribute("textContent");
        batchPrice = batchPrice.replace(" ", "").replace("$", "").replace(",", "");
        NewProductBatchPrice.setText(batchPrice);

        double SuggestPrice = Math.round(Double.parseDouble(batchPrice)*Double.parseDouble(WeblinkPricing));
        NewProductPricing.setText(ToolKit.RoundingString(SuggestPrice));

        //	商品敘述
        String Describe = ChromeWebDriver.findElement(By.cssSelector("div.product-desc")).getAttribute("innerHTML").replace("\n", "");
        NewProductDescribe.setText(Describe);
        //	贈品
        String Remark = "";
        if(!ChromeWebDriver.findElements(By.cssSelector("div.pro-gift")).isEmpty())
            Remark = ChromeWebDriver.findElement(By.cssSelector("div.pro-gift")).getAttribute("innerHTML");
        NewProductRemark.setText(Remark);

        String supplyStatus = "售完，補貨中";
        if(ChromeWebDriver.findElements(By.cssSelector("div.product-btBlock2")).isEmpty())
            supplyStatus = "供貨中";
        NewProductSupplyStatus.setText(supplyStatus);

        return Product_Enum.SingleUpdateStatus.更新完成;
    }
    private void SearchUrlByWebDriver(String Url, String account, String password) throws Exception {
        boolean Connect_Check = true;
        while(Connect_Check){
            Connect_Check = false;
            openWebDriver().get(Url);
            Alert alert = isExistAlert();
            if(alert != null || !ChromeWebDriver.getPageSource().contains(account)) {
                ERPApplication.Logger.warn("神腦 Cookie 過期，重新登入!");
                if(alert != null)
                    alert.accept();
                if(AutomaticLogin(account,password)) {
                    Connect_Check = true;
                    ERPApplication.Logger.warn("神腦 Cookie 已重新登入成功!");
                }else {
                    Thread.sleep(5000);
                }
            }
        }
    }
    private Boolean interruptLogin;
    private Boolean AutomaticLogin(String account, String password) throws Exception {
        interruptLogin = null;
        WebDriver WebDriver = openWebDriver();
        WebDriver.get("https://dealer168.senao.com.tw/Authorize/Login");
        Thread.sleep(2500);
        WebDriver.findElement(By.id("IdentKey")).sendKeys(account);
        WebDriver.findElement(By.id("IdentPassword")).sendKeys(password);
        WebDriver.findElement(By.id("loginSubmitButton")).click();

        Thread listeningLoginStatusThread = new listeningLoginStatusThread();
        listeningLoginStatusThread.start();
        while(true) {
            try {
                if(interruptLogin != null && !interruptLogin)
                    return false;
                Alert Alert = isExistAlert();
                if(Alert != null) {
                    Alert.accept();
                    Thread.sleep(2500);
                    WebDriver.findElement(By.id("IdentKey")).sendKeys(account);
                    WebDriver.findElement(By.id("IdentPassword")).sendKeys(password);
                    WebDriver.findElement(By.id("loginSubmitButton")).click();
                }else if(WebDriver.getPageSource().contains(account)) {
                    listeningLoginStatusThread.interrupt();
                    return true;
                }else
                    Thread.sleep(500);
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                DialogUI.ExceptionDialog(Ex);
            }
        }
    }
    class listeningLoginStatusThread extends Thread{
        public void run(){
            try {
                while(true) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.println("請輸入「n + Enter」來中斷登入");
                    System.out.print(":");
                    String inputString = bufferedReader.readLine();
                    if (inputString != null) {
                        if(!inputString.equals("n")) {
                            System.out.println("請輸入正確字元!!");
                        }else {
                            interruptLogin = false;
                            break;
                        }
                    }
                    System.out.println("----------------------------");
                }
            }catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                DialogUI.ExceptionDialog(Ex);
            }
        }
    }
    private Alert isExistAlert() {
        Alert Alert;
        try{
            Alert = ChromeWebDriver.switchTo().alert();
            return Alert;
        }catch (Exception Ex){
            return null;
        }
    }
    private WebDriver openWebDriver() {
        if(ChromeWebDriver == null)	ChromeWebDriver = new ChromeDriver();
            return ChromeWebDriver;
    }
    public void closeChromeWebDriver() {
        if(ChromeWebDriver != null)	ChromeWebDriver.quit();
        ChromeWebDriver = null;
    }
}
