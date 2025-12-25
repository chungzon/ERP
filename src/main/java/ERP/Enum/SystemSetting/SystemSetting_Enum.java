package ERP.Enum.SystemSetting;

/** [ERP.Enum] System setting */
public class SystemSetting_Enum {

    /** System setting config */
    public enum SystemSettingConfig{    //  系統設定
        預設列表機設定("DefaultPrinter"),
        託運單列表機設定("ConsignmentPrinter"),
        標籤列表機設定("LabelPrinter"),
        客戶編號起始字母("CustomerIDCharacter"),
        客戶位數("CustomerIDLength"),
        地區客戶位數("CustomerIDAreaLength"),
        廠商位數("ManufacturerIDLength"),
        貨單號長度("OrderNumberLength"),
        專案編號預設網址("ProjectCodeDefaultUrl"),
        票據起始號("InitialCheckNumber"),
        票據流水號("NowCheckNumber"),
        公司匯出格式("ExportCompanyFormat"),
        預設運費("DefaultShippingFee"),
        待確認_Store參數("WaitConfirm_StoreParameter"),
        測試資料庫("TestDatabase");
        private String ColumnName;
        SystemSettingConfig(String ColumnName) {  this.ColumnName = ColumnName; }
        public String getColumnName() { return this.ColumnName;  }
    }
    /** Character from Object where they come from */
    public enum ObjectIDCharacter {
//        A("臺北市"), B("臺中市"), C("基隆市"), D("臺南市"), E("高雄市"), F("新北市"), G("宜蘭縣"), H("桃園市"), I("嘉義市"),
//        J("新竹縣"), K("苗栗縣"), M("南投縣"), N("彰化縣"), O("新竹市"), P("雲林縣"), Q("嘉義縣"), T("屏東縣"), U("花蓮縣"),
//        V("臺東縣"), W("金門縣"), X("澎湖縣"), Z("連江縣");
        無(""), 臺北市("A"), 臺中市("B"), 基隆市("C"), 臺南市("D"), 高雄市("E"), 新北市("F"), 宜蘭市("G"), 桃園市("H"),
        嘉義市("I"), 新竹縣("J"), 苗栗縣("K"), 南投縣("M"), 彰化縣("N"), 新竹市("O"), 雲林縣("P"), 嘉義縣("Q"), 屏東縣("T"),
        花蓮縣("U"), 臺東縣("V"), 金門縣("W"), 澎湖縣("X"), 連江縣("Z");
        private String Character;
        ObjectIDCharacter(String Character) {  this.Character = Character; }
        public String Character() { return this.Character;  }
    }
    /** Separate product by level */
    public enum ProductLevel{
        商品儲存區, 商品儲存層
    }
    /** Object's status. Whether the first character of id contains letter */
    public enum ObjectStatus{
        地區客戶, 純數字客戶, 純數字廠商
    }
    public enum ExportCompanyFormat{
        storeName("商家名稱"),
        companyName("公司名稱"),
        companyName_fontSize("公司名稱_字體大小"),
        companyAddress("公司地址"),
        companyAddress_fontSize("公司地址_字體大小"),
        companyTelephone_fax("公司電話與傳真"),
        companyTelephone_fax_fontSize("公司電話與傳真_字體大小"),
        item1("品項1"),
        item2("品項2"),
        item3("品項3"),
        item4("品項4"),
        item5("品項5"),
        productName("商品名稱");
        private String ColumnName;
        ExportCompanyFormat(String ColumnName){ this.ColumnName = ColumnName;   }
        public String getColumnName(){  return ColumnName;  }
    }
    public enum ExportQuotationTemplateTitle{
        quotationDate("報價日期"), quotationNumber("報價單號"), customerName("客戶名稱"),
        contactPhone("客戶電話"), taxIdNumber("客戶統編"), faxIdNumber("客戶傳真"),
        projectName("專案名稱"), projectCode("專案編號"), contactPerson("聯絡人員"), customerAddress("客戶地址"), exportCount("匯出次數"),

        itemRange("項次範圍"),itemIndex("項次"), productName("商品名稱"), quantity("數量"), unit("單位"), singlePrice("單價"),
        singlePrice_priceAmount("複價"), productRemark("商品備註"), batchPrice("成本"), batchPrice_priceAmount("成本總額"),

        singlePrice_totalPriceNoneTax("單價 - 小計"), singlePrice_tax("單價 - 稅額"), singlePrice_totalPriceIncludeTax("單價 - 總計(含稅)"),
        singlePrice_chineseFormat("總計(新台幣)"), remark("備註說明"), batchPrice_totalPriceNoneTax("成本 - 小計"), batchPrice_tax("成本 - 稅額"),
        batchPrice_totalPriceIncludeTax("成本 - 總計(含稅)"), profit_includeTax("利潤(含稅)"), profit_noneTax("利潤(未稅)"),

        orderReference_orderSource("參考貨單 - 單別"), orderReference_customerID("參考貨單 - 客戶編號"),
        orderReference_orderNumber("參考貨單 - 單號");

        private String describe;
        ExportQuotationTemplateTitle(String describe){
            this.describe = describe;
        }
        public String getDescribe(){
            return describe;
        }
    }
    public enum ExportTemplateName{
        productCategoryAndOrderExport("商品類別、貨單匯出"), customerInfo("客戶資料"), payableReceivableInvoice("應收應付請款單"),
        exportCheckFormat("支票匯出範本"), payableReceivableDetail("應收應付明細表"), iAECrawlerPurchaseNote("採購備記"),
        iAECrawlerInvoiceOverview("廠商往來明細"), exportPayableDocument("應付帳款總表"), logistics_Note("訂購、收件人資訊"),
        specificationDocument("採購規格書"), purchaseQuotationOrAskPriceDocument("詢價、採購單"),

//        exportQuotation("報價單範本"),
        responsibilityGuaranteeTemplate("責任切結書"), selfPurchaseApplicationFormTemplate("不利用共同合約");
//        procurementDocument_responsibilityGuarantee("廠商 - 責任切結書"),
//        procurementDocument_ProductList("廠商 - 規格表");
        private String describe;
        ExportTemplateName(String describe){
            this.describe = describe;
        }
        public String getDescribe(){
            return describe;
        }
    }
}
