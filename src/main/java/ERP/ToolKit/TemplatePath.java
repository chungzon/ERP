package ERP.ToolKit;

import ERP.Enum.Order.Order_Enum.ExportQuotationFormat;

public class TemplatePath {
    public static final String ProductCategoryAndOrderExport = "template/商品類別、貨單匯出.xls";
    public static final String CustomerInfo = "template/客戶資料.xls";
    public static final String PayableReceivableInvoice = "template/應收應付請款單.xls";
    public static final String CheckExportFormat = "template/支票匯出範本.xls";
    public static final String PayableReceivableDetail = "template/應收應付明細表.xls";
    public static final String IAECrawlerPurchaseNote = "template/採購備記.xlsx";
    public static final String IAECrawlerInvoiceOverview = "template/廠商往來明細.xlsx";
    public static final String ExportPayableDocument = "template/應付帳款總表.xlsx";
    public static final String Logistics_Note = "template/Logistics_Note_Template.xlsx";
    public static final String SpecificationDocument = "template/採購規格書.docx";
    public static final String PurchaseQuotationOrAskPriceDocument = "template/詢價、採購單.xlsx";
    public static String ExportQuotation(String vendorNickName, ExportQuotationFormat ExportQuotationFormat){
        return "template/" + vendorNickName + "-估(" + ExportQuotationFormat.name()+ ").xlsx";
    }
    public static final String ResponsibilityGuaranteeTemplate = "template/採購文件/責任切結書.docx";;
    public static final String SelfPurchaseApplicationFormTemplate = "template/採購文件/不利用共同合約.docx";
    public static String ProcurementDocument_ResponsibilityGuarantee(String vendorNickName){
        return "template/採購文件/" + vendorNickName + "-責任切結書.docx";
    }
    public static String ProcurementDocument_ProductList(String vendorNickName){
        return "template/採購文件/" + vendorNickName + "-規格表.docx";
    }
}
