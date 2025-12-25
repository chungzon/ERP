package ERP.Model.Order;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.Bean.Order.ExportOrder_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.TemplatePath;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import jxl.CellView;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.PaperSize;
import jxl.write.*;
import jxl.write.Label;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

/** [ERP.Model] Export order */
public class ExportOrder_Model {

    private String outputFilePath;
    private Order_Bean Order_Bean;
    private ObjectInfo_Bean ObjectInfo_Bean;
    private ObservableList<ExportOrder_Bean> ProductList;
    private Order_Model Order_Model;
    private SystemSettingConfig_Bean SystemSettingConfig_Bean;
    public ExportOrder_Model(String outputFilePath, Order_Bean Order_Bean, ObjectInfo_Bean ObjectInfo_Bean, ObservableList<ExportOrder_Bean> ProductList){
        this.outputFilePath = outputFilePath;
        this.Order_Bean = Order_Bean;
        this.ObjectInfo_Bean = ObjectInfo_Bean;
        this.ProductList = ProductList;
        this.Order_Model = ERPApplication.ToolKit.ModelToolKit.getOrderModel();
        this.SystemSettingConfig_Bean = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel().loadAllSystemSettingData();
    }
    /** Export order to Excel
     * @param isShowPrice whether to show price
     * */
    public boolean ExportOrder(boolean isShowPrice){
        try{
            JSONObject exportCompanyFormat = SystemSettingConfig_Bean.getExportCompanyFormat();
            if(exportCompanyFormat == null){
                DialogUI.MessageDialog("※ 請設定 [公司資訊] 檔案匯出格式");
                return false;
            }


            WritableWorkbook WritableWorkbook = Workbook.createWorkbook(new File(outputFilePath));
            WritableCellFormat[] WritableCellFormat = new WritableCellFormat[7];
            WritableFont[] Font = new WritableFont[7];

//            InputStream FileInput = new ClassPathResource("Template/商品類別、貨單匯出.xls").getInputStream();
            FileInputStream FileInput = new FileInputStream(ResourceUtils.getFile(TemplatePath.ProductCategoryAndOrderExport));
            Workbook workbook = Workbook.getWorkbook(FileInput);
            WritableSheet WritableSheet = WritableWorkbook.createSheet("貨單匯出", 0);
            for (int i = 0; i < WritableCellFormat.length; i++) {
                if (i == 0) Font[i] = new WritableFont(WritableFont.TIMES, 20, WritableFont.NO_BOLD);
                else if (i == 1)    Font[i] = new WritableFont(WritableFont.createFont("細明體"), 16, WritableFont.BOLD);
                else if (i == 2)    Font[i] = new WritableFont(WritableFont.createFont("Times New Roman"), 16,  WritableFont.NO_BOLD);
                else if (i == 3)    Font[i] = new WritableFont(WritableFont.createFont("Times New Roman"), 11, WritableFont.NO_BOLD);
                else    Font[i] = new WritableFont(WritableFont.createFont("Times New Roman"), 11,  WritableFont.NO_BOLD);
                WritableCellFormat[i] = new WritableCellFormat(Font[i]);
                WritableCellFormat[i].setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.NONE);
                if (i == 1 || i == 4)   WritableCellFormat[i].setAlignment(jxl.format.Alignment.CENTRE);
                else if (i == 5)    WritableCellFormat[i].setAlignment(jxl.format.Alignment.LEFT);
                else if (i == 6)    WritableCellFormat[i].setAlignment(jxl.format.Alignment.RIGHT);
            }
            int ExcelPage = ProductList.size() % 10 == 0 ? ProductList.size() / 10 : ProductList.size() / 10 + 1;
            String OrderTitle = Order_Bean.getOrderSource().name();
            String CheckTitle = "";
            String TaxIDNumber = "";
            String Fax = "";
            if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.進貨單){
                OrderTitle = "進 貨 單";
            }else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單){
                OrderTitle = "廠商退貨單";
            }else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單 || Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單){
                OrderTitle = "出 貨 單";
            }else if(Order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單){
                OrderTitle = "退 貨 單";
            }
            for (int page = 0; page < ExcelPage; page++) {
                int Row = page * 26;
                // 公司名
                WritableFont companyWriteFont = new WritableFont(WritableFont.createFont("標楷體"), exportCompanyFormat.getInteger(SystemSetting_Enum.ExportCompanyFormat.companyName_fontSize.name()), WritableFont.BOLD);
                WritableCellFormat companyWriteFormat = new WritableCellFormat(companyWriteFont);
                WritableSheet.addCell(new Label(1, Row, "" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.companyName.name()), companyWriteFormat));
                WritableSheet.mergeCells(1, Row, 4, Row);
                WritableFont addressWriteFont = new WritableFont(WritableFont.createFont("標楷體"), exportCompanyFormat.getInteger(SystemSetting_Enum.ExportCompanyFormat.companyAddress_fontSize.name()), WritableFont.NO_BOLD);
                WritableCellFormat addressWriteFormat = new WritableCellFormat(addressWriteFont);
                WritableSheet.addCell(new Label(1, 1 + Row, "" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.companyAddress.name()), addressWriteFormat));
                WritableSheet.mergeCells(1, 1 + Row, 4, 1 + Row);
                WritableFont telWriteFont = new WritableFont(WritableFont.createFont("標楷體"), exportCompanyFormat.getInteger(SystemSetting_Enum.ExportCompanyFormat.companyTelephone_fax_fontSize.name()), WritableFont.NO_BOLD);
                WritableCellFormat telWriteFormat = new WritableCellFormat(telWriteFont);
                WritableSheet.addCell(new Label(1, 2 + Row,"" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.companyTelephone_fax.name()), telWriteFormat));
                WritableSheet.mergeCells(1, 2 + Row, 4, 2 + Row);

                // 標題
                WritableFont titleWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 22, WritableFont.BOLD);
                WritableCellFormat titleWriteFormat = new WritableCellFormat(titleWriteFont);
                titleWriteFormat.setAlignment(Alignment.CENTRE);
                titleWriteFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
                WritableSheet.addCell(new Label(5, Row, OrderTitle, titleWriteFormat));
                WritableSheet.mergeCells(5, Row, 7, 1 + Row);

                // 日期
                String OrderDate = Order_Model.getOrderDateBySpecificFormat(Order_Bean.getOrderDate());
                WritableFont dateWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 11, WritableFont.NO_BOLD);
                WritableCellFormat dateWriteFormat = new WritableCellFormat(dateWriteFont);
                dateWriteFormat.setAlignment(Alignment.CENTRE);
                dateWriteFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
                WritableSheet.addCell(new Label(5, 3 + Row, OrderDate.substring(0,OrderDate.indexOf("/")) + "年 " + OrderDate.substring(OrderDate.indexOf("/")+1,OrderDate.lastIndexOf("/")) + "月 " + OrderDate.substring(OrderDate.lastIndexOf("/")+1) + "日", dateWriteFormat));
                WritableSheet.mergeCells(5, 3 + Row, 7, 3 + Row);

                // 單號
                WritableFont snWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.NO_BOLD);
                WritableCellFormat snWriteFormat = new WritableCellFormat(snWriteFont);
                snWriteFormat.setAlignment(Alignment.LEFT);
                snWriteFormat.setVerticalAlignment(VerticalAlignment.BOTTOM);
                snWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE, Colour.BLACK);
                WritableSheet.addCell(new Label(1, 4 + Row, "單 號: " + Order_Bean.getNowOrderNumber(), snWriteFormat));
                WritableSheet.mergeCells(1, 4 + Row, 2, 4 + Row);

                // page number
                WritableCellFormat pagenumWriteFontWriteFormat = new WritableCellFormat(snWriteFormat);
                pagenumWriteFontWriteFormat.setAlignment(Alignment.RIGHT);
                pagenumWriteFontWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE, Colour.BLACK);
                WritableSheet.addCell(new Label(3, 4 + Row, "P:" + (page + 1) + "/" + ExcelPage, pagenumWriteFontWriteFormat));
                WritableSheet.mergeCells(3, 4 + Row, 4, 4 + Row);
                WritableSheet.addCell(new Label(5, 4 + Row, "", pagenumWriteFontWriteFormat));
                WritableSheet.mergeCells(5, 4 + Row, 7, 4 + Row);

                // 營業項目 start
                WritableFont itemWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 10, WritableFont.NO_BOLD);
                WritableCellFormat itemWriteFormat = new WritableCellFormat(itemWriteFont);
                itemWriteFormat.setVerticalAlignment(VerticalAlignment.BOTTOM);
                itemWriteFormat.setAlignment(Alignment.RIGHT);
                WritableSheet.addCell(new Label(8, Row,"" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.item1.name()), itemWriteFormat));
                WritableSheet.mergeCells(8, Row, 11, Row);
                WritableSheet.addCell(new Label(8, 1 + Row,"" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.item2.name()), itemWriteFormat));
                WritableSheet.mergeCells(8, 1 + Row, 11, 1 + Row);
                WritableSheet.addCell(new Label(8, 2 + Row,"" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.item3.name()), itemWriteFormat));
                WritableSheet.mergeCells(8, 2 + Row, 11, 2 + Row);
                WritableSheet.addCell(new Label(8, 3 + Row,"" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.item4.name()), itemWriteFormat));
                WritableSheet.mergeCells(8, 3 + Row, 11, 3 + Row);
                WritableCellFormat itemLastWriteFormat = new WritableCellFormat(itemWriteFont);
                itemLastWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE, Colour.BLACK);
                itemLastWriteFormat.setAlignment(Alignment.RIGHT);
                WritableSheet.addCell(new Label(8, 4 + Row, "" + exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.item5.name()) + "     ", itemLastWriteFormat));
                WritableSheet.mergeCells(8, 4 + Row, 11, 4 + Row);

                // 客戶資訊 start
                WritableFont customerWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 11, WritableFont.NO_BOLD);
                WritableCellFormat customerWriteFormat = new WritableCellFormat(customerWriteFont);
                WritableSheet.addCell(new Label(1, 5 + Row, "公司名稱:(" + ObjectInfo_Bean.getObjectID() + ") " + ObjectInfo_Bean.getObjectName(), customerWriteFormat));
                WritableSheet.mergeCells(1, 5 + Row, 5, 5 + Row);
                WritableSheet.addCell(new Label(6, 5 + Row, " 聯絡人:" + ObjectInfo_Bean.getContactPerson(), customerWriteFormat));
                WritableSheet.mergeCells(6, 5 + Row, 8, 5 + Row);
                WritableCellFormat customerRightWriteFormat = new WritableCellFormat(customerWriteFont);
                customerRightWriteFormat.setAlignment(Alignment.RIGHT);
                WritableSheet.addCell(new Label(9, 5 + Row, "電話:", customerRightWriteFormat));
                WritableSheet.addCell(new Label(10, 5 + Row, "" + ObjectInfo_Bean.getTelephone1(), customerWriteFormat));
                WritableSheet.mergeCells(10, 5 + Row, 11, 5 + Row);
                WritableSheet.addCell(new Label(1, 6 + Row, "出貨客戶:" + CheckTitle, customerWriteFormat));
                WritableSheet.mergeCells(1, 6 + Row, 5, 6 + Row);
                WritableSheet.addCell(new Label(6, 6 + Row, " 統  編:" + TaxIDNumber, customerWriteFormat));
                WritableSheet.mergeCells(6, 6 + Row, 8, 6 + Row);
                WritableSheet.addCell(new Label(9, 6 + Row, "傳真:", customerRightWriteFormat));
                WritableSheet.addCell(new Label(10, 6 + Row, "" + Fax, customerWriteFormat));
                WritableSheet.mergeCells(10, 6 + Row, 11, 6 + Row);
                WritableFont customerLastWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 11, WritableFont.NO_BOLD);
                WritableCellFormat customerLastWriteFormat = new WritableCellFormat(customerLastWriteFont);
                customerLastWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE, Colour.BLACK);
                WritableSheet.addCell(new Label(1, 7 + Row, "出貨地址:" + ObjectInfo_Bean.getDeliveryAddress(), customerLastWriteFormat));
                WritableSheet.mergeCells(1, 7 + Row, 7, 7 + Row);
                WritableCellFormat customerRightLastWriteFormat = new WritableCellFormat(customerLastWriteFont);
                customerRightLastWriteFormat.setAlignment(Alignment.RIGHT);
                customerRightLastWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE, Colour.BLACK);
                WritableSheet.addCell(new Label(8, 7 + Row, "發票號碼:", customerRightLastWriteFormat));
                WritableSheet.mergeCells(8, 7 + Row, 9, 7 + Row);
                WritableSheet.addCell(new Label(10, 7 + Row, "", customerRightLastWriteFormat));
                WritableSheet.mergeCells(10, 7 + Row, 11, 7 + Row);

                // 內容標頭 start
                WritableFont contentHeaderWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.BOLD);
                WritableCellFormat contentHeaderWriteFormat = new WritableCellFormat(contentHeaderWriteFont);
                contentHeaderWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE, Colour.BLACK);
                contentHeaderWriteFormat.setAlignment(Alignment.CENTRE);
                WritableFont noHeaderWriteFont = new WritableFont(WritableFont.createFont("Times New Roman"),12, WritableFont.NO_BOLD);
                WritableCellFormat noHeaderWriteFormat = new WritableCellFormat(noHeaderWriteFont);
                noHeaderWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.DOUBLE, Colour.BLACK);
                noHeaderWriteFormat.setAlignment(Alignment.CENTRE);
                WritableSheet.addCell(new Label(1, 8 + Row, "NO", noHeaderWriteFormat));
                WritableSheet.addCell(new Label(2, 8 + Row, "商品號碼", contentHeaderWriteFormat));
                WritableSheet.addCell(new Label(3, 8 + Row, "品名    /     規格", contentHeaderWriteFormat));
                WritableSheet.mergeCells(3, 8 + Row, 7, 8 + Row);
                WritableSheet.addCell(new Label(8, 8 + Row, "數量", contentHeaderWriteFormat));
                WritableSheet.addCell(new Label(9, 8 + Row, "單位", contentHeaderWriteFormat));
                WritableSheet.addCell(new Label(10, 8 + Row, "單價", contentHeaderWriteFormat));
                WritableSheet.addCell(new Label(11, 8 + Row, "金 額", contentHeaderWriteFormat));

                // 內容 start
                WritableFont contentWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.NO_BOLD);
                WritableCellFormat contentWriteFormat = new WritableCellFormat(contentWriteFont);
                WritableFont contenLasttWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.NO_BOLD);
                WritableCellFormat contentLastWriteFormat = new WritableCellFormat(contenLasttWriteFont);
                contentLastWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM, Colour.BLACK);
                WritableCellFormat noFormat = new WritableCellFormat(contentWriteFont);
                noFormat.setAlignment(Alignment.CENTRE);
                WritableCellFormat noFooterFormat = new WritableCellFormat(contentWriteFont);
                noFooterFormat.setAlignment(Alignment.CENTRE);
                noFooterFormat.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM, Colour.BLACK);
                WritableCellFormat contentCentreWriteFormat = new WritableCellFormat(contentWriteFormat);
                contentCentreWriteFormat.setAlignment(Alignment.CENTRE);
                WritableCellFormat contentLastCentreWriteFormat = new WritableCellFormat(contentLastWriteFormat);
                contentLastCentreWriteFormat.setAlignment(Alignment.CENTRE);
                int total_count = 0;
                String Remark = "";
                for (int index = page * 10; index < (page + 1) * 10; index++) {
                    if (index < ProductList.size()) {
                        ExportOrder_Bean ExportOrder_Bean =  ProductList.get(index);
                        WritableCellFormat tmpFormat = (index + 1) % 10 == 0 ? contentLastWriteFormat : contentWriteFormat;
                        WritableCellFormat tmpNoFormat = (index + 1) % 10 == 0 ? noFooterFormat : noFormat;
                        WritableCellFormat tmpCentreFormat = (index + 1) % 10 == 0 ? contentLastCentreWriteFormat : contentCentreWriteFormat;

                        WritableSheet.addCell(new Label(1, 9 + Row + total_count, "" + (int) (index + 1), tmpNoFormat));
                        WritableSheet.addCell(new Label(2, 9 + Row + total_count, "" + ExportOrder_Bean.getISBN(), tmpFormat));
                        WritableSheet.addCell(new Label(3, 9 + Row + total_count, "" + ExportOrder_Bean.getProductName(), tmpFormat));
                        WritableSheet.mergeCells(3, 9 + Row + total_count, 7, 9 + Row + total_count);
                        WritableSheet.addCell(new Label(8, 9 + Row + total_count,"" + ExportOrder_Bean.getQuantity(), tmpCentreFormat));
                        WritableSheet.addCell(new Label(9, 9 + Row + total_count, "" + ExportOrder_Bean.getUnit(), tmpCentreFormat));
                        if(isShowPrice) {
                            WritableSheet.addCell(new Label(10, 9 + Row + total_count, "" + ExportOrder_Bean.getSalePrice(), tmpCentreFormat));
                            WritableSheet.addCell(new Label(11, 9 + Row + total_count, "" + ExportOrder_Bean.getPriceAmount(), tmpCentreFormat));
                        }else{
                            WritableSheet.addCell(new Label(10, 9 + Row + total_count, "", tmpCentreFormat));
                            WritableSheet.addCell(new Label(11, 9 + Row + total_count, "", tmpCentreFormat));
                        }
                        if(!ExportOrder_Bean.getRemark().equals(""))    Remark = Remark + (index + 1) + ": " + ExportOrder_Bean.getRemark() + "; ";
                    }
                    total_count++;
                    WritableFont summaryTopWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.NO_BOLD);
                    WritableCellFormat summaryTopWriteFormat = new WritableCellFormat(summaryTopWriteFont);
                    summaryTopWriteFormat.setBorder(Border.TOP, BorderLineStyle.MEDIUM, Colour.BLACK);
                    WritableFont totalWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.BOLD);
                    WritableCellFormat totalTopWriteFormat = new WritableCellFormat(totalWriteFont);
                    totalTopWriteFormat.setAlignment(Alignment.RIGHT);
                    totalTopWriteFormat.setBorder(Border.TOP, BorderLineStyle.MEDIUM, Colour.BLACK);
                    WritableSheet.addCell(new Label(11, 19 + Row, "0", totalTopWriteFormat));
                    WritableSheet.addCell(new Label(10, 19 + Row, "合    計:", summaryTopWriteFormat));
                    if(isShowPrice) WritableSheet.addCell(new Label(11, 19 + Row, "" + Order_Bean.getTotalPriceNoneTax(), totalTopWriteFormat));
                    else    WritableSheet.addCell(new Label(11, 19 + Row, "", totalTopWriteFormat));
                }

                if (page == ExcelPage - 1) {
                    if (ProductList.size() % 10 != 0) {
                        int n = ProductList.size() % 10;
                        int m = 10 - n;
                        for (int index = 0; index < m; index++) {
                            WritableSheet.addCell(new Label(1, n + index - 1 + Row + 10, "", noFormat));
                            WritableSheet.addCell(new Label(2, n + index - 1 + Row + 10, "", contentWriteFormat));
                            WritableSheet.addCell(new Label(3, n + index - 1 + Row + 10, "", contentWriteFormat));
                            WritableSheet.mergeCells(3, n + index - 1 + Row + 10, 7, n + index - 1 + Row + 10);
                            WritableSheet.addCell(new Label(8, n + index - 1 + Row + 10, "", contentWriteFormat));
                            WritableSheet.addCell(new Label(9, n + index - 1 + Row + 10, "", contentWriteFormat));
                            WritableSheet.addCell(new Label(10, n + index - 1 + Row + 10, "", contentWriteFormat));
                            WritableSheet.addCell(new Label(11, n + index - 1 + Row + 10, "", contentWriteFormat));
                        }
                    }
                    WritableFont summaryWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.NO_BOLD);
                    WritableCellFormat summaryWriteFormat = new WritableCellFormat(summaryWriteFont);
                    WritableCellFormat summaryTopWriteFormat = new WritableCellFormat(summaryWriteFormat);
                    summaryTopWriteFormat.setBorder(Border.TOP, BorderLineStyle.MEDIUM, Colour.BLACK);
                    WritableSheet.addCell(new Label(1, 19 + Row, "送貨方式:", summaryTopWriteFormat));
                    WritableSheet.mergeCells(1, 19 + Row, 3, 19 + Row);
                    WritableSheet.addCell(new Label(4, 19 + Row, "現金:", summaryTopWriteFormat));
                    WritableSheet.mergeCells(4, 19 + Row, 6, 19 + Row);
                    WritableSheet.addCell(new Label(7, 19 + Row, "月結:", summaryTopWriteFormat));
                    WritableSheet.mergeCells(7, 19 + Row, 9, 19 + Row);
                    WritableFont totalWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.BOLD);
                    WritableCellFormat totalWriteFormat = new WritableCellFormat(totalWriteFont);
                    totalWriteFormat.setAlignment(Alignment.RIGHT);
                    WritableCellFormat totalTopWriteFormat = new WritableCellFormat(totalWriteFont);
                    totalTopWriteFormat.setAlignment(Alignment.RIGHT);
                    if(isShowPrice) WritableSheet.addCell(new Label(1, 20 + Row, "折    讓:" + Order_Bean.getDiscount(), summaryWriteFormat));
                    else    WritableSheet.addCell(new Label(1, 20 + Row, "折    讓:", summaryWriteFormat));
                    WritableSheet.mergeCells(1, 20 + Row, 3, 20 + Row);
                    WritableSheet.addCell(new Label(10, 20 + Row, "營 業 稅:", summaryWriteFormat));
                    if(isShowPrice) WritableSheet.addCell(new Label(11, 20 + Row, Order_Bean.getTax().equals("") ? "0" : Order_Bean.getTax(), totalWriteFormat));
                    else    WritableSheet.addCell(new Label(11, 20 + Row, "", totalWriteFormat));
                    WritableFont summaryFooterWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.NO_BOLD);
                    WritableCellFormat summaryFooterWriteFormat = new WritableCellFormat(summaryFooterWriteFont);
                    summaryFooterWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM, Colour.BLACK);
                    WritableFont totalFooterWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.BOLD);
                    WritableCellFormat totalFooterWriteFormat = new WritableCellFormat(totalFooterWriteFont);
                    totalFooterWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM, Colour.BLACK);
                    WritableSheet.addCell(new Label(1, 21 + Row, "未    收:", summaryFooterWriteFormat));
                    WritableSheet.mergeCells(1, 21 + Row, 9, 21 + Row);
                    WritableSheet.addCell(new Label(10, 21 + Row, "總計金額:", summaryFooterWriteFormat));
                    WritableCellFormat totalRightFooterWriteFormat = new WritableCellFormat(totalFooterWriteFont);
                    totalRightFooterWriteFormat.setBorder(Border.BOTTOM, BorderLineStyle.MEDIUM, Colour.BLACK);
                    totalRightFooterWriteFormat.setAlignment(Alignment.RIGHT);
                    if(isShowPrice) WritableSheet.addCell(new Label(11, 21 + Row, Order_Bean.getTotalPriceIncludeTax(), totalRightFooterWriteFormat));
                    else    WritableSheet.addCell(new Label(11, 21 + Row, "", totalRightFooterWriteFormat));

                    // 備註 start
                    WritableFont remarkWriteFont = new WritableFont(WritableFont.createFont("標楷體"), 10,WritableFont.NO_BOLD);
                    WritableCellFormat remarkFooterWriteFormat = new WritableCellFormat(remarkWriteFont);
                    remarkFooterWriteFormat.setVerticalAlignment(VerticalAlignment.BOTTOM);
                    WritableSheet.addCell(new Label(1, 22 + Row, "備註:", remarkFooterWriteFormat));
                    WritableSheet.mergeCells(2, 22 + Row, 11, 22 + Row);
                    WritableSheet.addCell(new Label(2, 22 + Row, Remark, remarkFooterWriteFormat));

                    WritableSheet.addCell(new Label(1, 23 + Row, "注意:", remarkFooterWriteFormat));
                    WritableSheet.addCell(new Label(2, 23 + Row, "1.貨款尚未全部兌現前，貨物所有權仍歸本公司所有。", remarkFooterWriteFormat));
                    WritableSheet.mergeCells(2, 23 + Row, 11, 23 + Row);
                    WritableSheet.addCell(new Label(2, 24 + Row, "2.如有疑問，請於收貨日起三日內提出，否則視同接受本單所載事項。",remarkFooterWriteFormat));
                    WritableSheet.mergeCells(2, 24 + Row, 11, 24 + Row);
                }
                // unit * 20
                WritableSheet.setRowView(Row, 420);
                WritableSheet.setRowView(1 + Row, 270);
                WritableSheet.setRowView(2 + Row, 270);
                WritableSheet.setRowView(3 + Row, 270);
                WritableSheet.setRowView(4 + Row, 270);
                WritableSheet.setRowView(5 + Row, 300);
                WritableSheet.setRowView(6 + Row, 300);
                WritableSheet.setRowView(7 + Row, 300);
                WritableSheet.setRowView(8 + Row, 300);
                WritableSheet.setRowView(9 + Row, 315);
                WritableSheet.setRowView(10 + Row, 315);
                WritableSheet.setRowView(11 + Row, 315);
                WritableSheet.setRowView(12 + Row, 315);
                WritableSheet.setRowView(13 + Row, 315);
                WritableSheet.setRowView(14 + Row, 315);
                WritableSheet.setRowView(15 + Row, 315);
                WritableSheet.setRowView(16 + Row, 315);
                WritableSheet.setRowView(17 + Row, 315);
                WritableSheet.setRowView(18 + Row, 315);
                WritableSheet.setRowView(19 + Row, 400);
                WritableSheet.setRowView(20 + Row, 270);
                WritableSheet.setRowView(21 + Row, 270);
                WritableSheet.setRowView(22 + Row, 360);
                WritableSheet.setRowView(23 + Row, 360);
                WritableSheet.setRowView(24 + Row, 270);
                // WritableSheet.setRowView(25 + Row, 510);
                WritableSheet.setRowView(25 + Row, 660); // 380 + 65
            }
            // 1px is 0.0271
            CellView CellView = new CellView();
            CellView.setSize(812);
            WritableSheet.setColumnView(0, CellView);
            CellView.setSize(1439);
            WritableSheet.setColumnView(1, CellView);
            CellView.setSize(4100);
            WritableSheet.setColumnView(2, CellView);
            CellView.setSize(1845);
            WritableSheet.setColumnView(3, CellView);
            CellView.setSize(1875);
            WritableSheet.setColumnView(4, CellView);
            CellView.setSize(4612);
            WritableSheet.setColumnView(5, CellView);
            CellView.setSize(1217);
            WritableSheet.setColumnView(6, CellView);
            CellView.setSize(1476);
            WritableSheet.setColumnView(7, CellView);
            CellView.setSize(2103);
            WritableSheet.setColumnView(8, CellView);
            CellView.setSize(2103);
            WritableSheet.setColumnView(9, CellView);
            CellView.setSize(2730);
            WritableSheet.setColumnView(10, CellView);
            CellView.setSize(3505);
            WritableSheet.setColumnView(11, CellView);

            SheetSettings SheetSettings = WritableSheet.getSettings();
            SheetSettings.setPaperSize(PaperSize.NOTE);
            SheetSettings.setFitToPages(true);
            SheetSettings.setFitWidth(1); // 一頁寬度
            SheetSettings.setHorizontalCentre(true);

            SheetSettings.setLeftMargin(0.0);
            SheetSettings.setRightMargin(0.0);
            SheetSettings.setTopMargin(0.0);
            SheetSettings.setBottomMargin(0.0);
            WritableWorkbook.write();
            WritableWorkbook.close();
            workbook.close();
            // 不直接列印,只要存檔
            return true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            return false;
        }
    }
    /** Print order
     * @param isShowPrice whether to show price
     * */
    public boolean PrintOrder(boolean isShowPrice){
        if(ExportOrder(isShowPrice)){
            try{
                if(Desktop.isDesktopSupported()){
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.PRINT))
                        desktop.print(new File(outputFilePath));
                    return true;
                }else    System.out.println("debug: desktop not supported!");
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
        return false;
    }
}
