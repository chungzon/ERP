package ERP.Model.Order.SearchNonePayReceive;

import ERP.Bean.Order.SearchNonePayReceive.ConditionalPayReceiveSearch_Bean;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.ERPApplication;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_ExportPayReceiveDetails;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.TemplatePath;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.PaperSize;
import jxl.write.*;
import jxl.write.Label;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

/** [ERP.Model] Export none payable or receivable to Excel */
public class ExportNonePayReceiveDocument {
    private String[] NonePayReceiveColumnName = {"No", "編號", "名稱", "貨單數", "貨單額", "退貨單數", "退貨額","稅額", "折讓", "總金額", "實金額"};
    private String[] NonePayReceiveDetailsColumnName = {"No", "名稱", "單號", "日期", "單別", "折扣", "總金額", "稅額", "折讓","應收/付", "分期期數"};

    private ToolKit ToolKit;
    private CallConfig CallConfig;
    private SystemSettingConfig_Bean SystemSettingConfig_Bean;
    public ExportNonePayReceiveDocument(){
        ToolKit = ERPApplication.ToolKit;
        CallConfig = ToolKit.CallConfig;
        SystemSettingConfig_Bean = ToolKit.ModelToolKit.getSystemSettingModel().loadAllSystemSettingData();
    }
    /** Export none payable or receivable which be searched to Excel
     * @param ConditionalPayReceiveSearch_Bean the bean of conditional search
     * @param SearchNonePayReceivableList the list of none payable or receivable which be searched
     * @param OrderObject the object of order
     * */
    public boolean exportSearchNonePayReceive(ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean, ObservableList<SearchNonePayReceive_Bean> SearchNonePayReceivableList, OrderObject OrderObject) throws Exception{
        JSONObject exportCompanyFormat = SystemSettingConfig_Bean.getExportCompanyFormat();
        if(exportCompanyFormat == null){
            DialogUI.MessageDialog("※ 請設定 [公司資訊] 檔案匯出格式");
            return false;
        }

        String outputFilePath = CallConfig.getFile_OutputPath() + "\\" + OrderObject.name() + OrderObject.getPayableReceivableName() + "帳總表(" + ToolKit.getToday("yyyy-MM-dd")+ ").xls";
        WritableWorkbook WritableWorkbook = Workbook.createWorkbook(new File(outputFilePath));
        WritableSheet WritableSheet = WritableWorkbook.createSheet("0", 0);
        WritableCellFormat[] WritableCellFormat = setExcelFont();

//        InputStream FileInput = new ClassPathResource("Template/應收應付明細表.xls").getInputStream();
        FileInputStream FileInput = new FileInputStream(ResourceUtils.getFile(TemplatePath.PayableReceivableDetail));
        Workbook PayableReceivableWorkbook = Workbook.getWorkbook(FileInput);
        Sheet PayableReceivableSheet = PayableReceivableWorkbook.getSheet(0);
        Workbook PurchaseShipmentOrderWorkbook = Workbook.getWorkbook(new FileInputStream(ResourceUtils.getFile(TemplatePath.PayableReceivableDetail)));
        Sheet PurchaseShipmentOrderSheet = PurchaseShipmentOrderWorkbook.getSheet(0);

        int ExcelPage = SearchNonePayReceivableList.size() % 10 == 0 ? SearchNonePayReceivableList.size() / 10 : SearchNonePayReceivableList.size() / 10+1;
        for (int page = 0; page < ExcelPage; page++) {
            if(page % 2 == 0) {
                for (int row = 0; row < 47; row++)
                    WritableSheet.setRowView(row + (page / 2) * 47, PurchaseShipmentOrderSheet.getRowView(row));
            }
            WritableSheet.addCell(new Label(0, page * 24 - (page / 2), exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.storeName.name()),WritableCellFormat[0]));
            WritableSheet.mergeCells(0, page * 24 - (page / 2),7, page * 24 - (page / 2));

            WritableSheet.addCell(new Label(0,1 + page * 24 - (page / 2)," " + OrderObject.name() + OrderObject.getPayableReceivableName() + "資料查詢(P." + (page + 1) + "/" + ExcelPage + ")",WritableCellFormat[0]));
            WritableSheet.mergeCells(0,1 + page * 24 - (page / 2),7,2 + page * 24 - (page / 2));

            WritableSheet.addCell(new Label(0,3 + page * 24 - (page / 2),"                      ",WritableCellFormat[2]));
            WritableSheet.setRowView(3 + page * 24 - (page / 2), PurchaseShipmentOrderSheet.getRowView(3));
            WritableSheet.addCell(new Label(0,5 + page * 24 - (page / 2),"起始" + OrderObject.name() + ":" + ConditionalPayReceiveSearch_Bean.getStartObjectID() +"     結束" + OrderObject.name() + ":"+ ConditionalPayReceiveSearch_Bean.getEndObjectID() + "起始日期:" + ConditionalPayReceiveSearch_Bean.getCheckoutByMonthStartDate() + "  結束日期:" + ConditionalPayReceiveSearch_Bean.getCheckoutByMonthEndDate(),WritableCellFormat[5]));

            WritableSheet.mergeCells(0,5 + page * 24 - (page / 2),7,5 + page * 24 - (page / 2));
            WritableSheet.addCell(new Label(0,6 + page * 24 - (page / 2),"",WritableCellFormat[2]));
            WritableSheet.mergeCells(0,6 + page * 24 - (page / 2),7,6 + page * 24 - (page / 2));
            WritableSheet.addCell(new Label(0,7 + page * 24 - (page / 2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", WritableCellFormat[5]));
            WritableSheet.mergeCells(0,7 + page * 24 - (page / 2),7,7 + page * 24 - (page / 2));

            for(int index = 0 ; index < NonePayReceiveColumnName.length ; index++){
                if(index == 1 || index == 2)    WritableSheet.addCell(new Label(index,8 + page * 24 - (page / 2),OrderObject.name() + NonePayReceiveColumnName[index],WritableCellFormat[5]));
                else    WritableSheet.addCell(new Label(index,8 + page * 24 - (page / 2),NonePayReceiveColumnName[index],WritableCellFormat[5]));
            }
            int total_count=0;
            WritableSheet.addCell(new Label(0,9 + page * 24 - (page / 2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^",WritableCellFormat[5]));
            WritableSheet.mergeCells(0,9 + page * 24 - (page / 2),7,9 + page * 24 - (page / 2));
            for(int index= page * 10 ; index < (page + 1) * 10 ; index++){
                if(index < SearchNonePayReceivableList.size()){
                    SearchNonePayReceive_Bean SearchNonePayReceive_Bean = SearchNonePayReceivableList.get(index);
                    int Position = 11 + page * 24 - (page / 2) + total_count;
                    WritableSheet.addCell(new Label(0,Position,"" + (total_count + 1), WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(1,Position, SearchNonePayReceive_Bean.getObjectID(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(2,Position, SearchNonePayReceive_Bean.getObjectName(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(3,Position, ""+SearchNonePayReceive_Bean.getOrderQuantity(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(4,Position, ""+SearchNonePayReceive_Bean.getOrderPriceAmount(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(5,Position, ""+SearchNonePayReceive_Bean.getReturnOrderQuantity(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(6,Position, ""+SearchNonePayReceive_Bean.getReturnOrderPriceAmount(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(7,Position, ""+SearchNonePayReceive_Bean.getTax(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(8,Position, ""+SearchNonePayReceive_Bean.getDiscount(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(9,Position, ""+SearchNonePayReceive_Bean.getPayReceivePrice(),WritableCellFormat[5]));
                    WritableSheet.addCell(new Label(10,Position, ""+SearchNonePayReceive_Bean.getActualPayReceivePrice(),WritableCellFormat[5]));
                }
                total_count++;
            }
        }
        for(int index = 0 ; index < NonePayReceiveColumnName.length ; index++)   WritableSheet.setColumnView(index, PayableReceivableSheet.getColumnView(index));
        SheetSettings sts = WritableSheet.getSettings();
        sts.setPaperSize(PaperSize.NOTE);
        sts.setLeftMargin(0.0);
        sts.setRightMargin(0.0);
        sts.setTopMargin(0.0);
        sts.setBottomMargin(0.0);
        WritableWorkbook.write();
        WritableWorkbook.close();
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
            return false;
        }
        return false;
    }
    /** Export none payable or receivable details to Excel
     * @param ConditionalPayReceiveSearch_Bean the bean of conditional search
     * @param NonePayReceiveDetailsList the list of none payable or receivable details
     * @param SearchNonePayReceive_Bean the bean of none payable or receivable which be searched
     * @param OrderObject the object of order
     * */
    public boolean exportNonePayReceiveDetails(ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean,
                                               ObservableList<SearchOrder_Bean> NonePayReceiveDetailsList,
                                               SearchNonePayReceive_Bean SearchNonePayReceive_Bean,
                                               OrderObject OrderObject,
                                               Order_ExportPayReceiveDetails order_exportPayReceiveDetails) {
        try{
            JSONObject exportCompanyFormat = SystemSettingConfig_Bean.getExportCompanyFormat();
            if(exportCompanyFormat == null){
                DialogUI.MessageDialog("※ 請設定 [公司資訊] 檔案匯出格式");
                return false;
            }

            String outputFilePath = CallConfig.getFile_OutputPath() + "\\" + OrderObject.name() + OrderObject.getPayableReceivableName() + "明細表(" + ToolKit.getToday("yyyy-MM-dd")+ ").xls";
            WritableWorkbook WritableWorkbook = Workbook.createWorkbook(new File(outputFilePath));
            WritableSheet WritableSheet = WritableWorkbook.createSheet("0", 0);
            WritableCellFormat[] WritableCellFormat = setExcelFont();

            Workbook PayableReceivableWorkbook = Workbook.getWorkbook(new FileInputStream(ResourceUtils.getFile(TemplatePath.PayableReceivableDetail)));
            Sheet PayableReceivableSheet = PayableReceivableWorkbook.getSheet(0);
            Workbook PurchaseShipmentOrderWorkbook = Workbook.getWorkbook(new FileInputStream(ResourceUtils.getFile(TemplatePath.ProductCategoryAndOrderExport)));
            Sheet PurchaseShipmentOrderSheet = PurchaseShipmentOrderWorkbook.getSheet(0);

            int ExcelPage = NonePayReceiveDetailsList.size() % 10 == 0 ? NonePayReceiveDetailsList.size() / 10 : NonePayReceiveDetailsList.size() / 10+1;
            for (int page = 0; page < ExcelPage; page++) {
                if(page % 2 == 0) {
                    for (int row = 0; row < 47; row++)
                        WritableSheet.setRowView(row + (page / 2) * 47, PurchaseShipmentOrderSheet.getRowView(row));
                }
                WritableSheet.addCell(new Label(0, page * 24 - (page / 2), exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.storeName.name()),WritableCellFormat[0]));
                WritableSheet.mergeCells(0, page * 24 - (page / 2),7, page * 24 - (page / 2));

                WritableSheet.addCell(new Label(0,1 + page * 24 - (page / 2)," " + OrderObject.name() + OrderObject.getPayableReceivableName() + "明細表(P." + (page + 1) + "/" + ExcelPage + ")",WritableCellFormat[0]));
                WritableSheet.mergeCells(0,1 + page * 24 - (page / 2),7,2 + page * 24 - (page / 2));

                WritableSheet.addCell(new Label(0,3 + page * 24 - (page / 2),"                      ",WritableCellFormat[2]));
                WritableSheet.setRowView(3 + page * 24 - (page / 2), PurchaseShipmentOrderSheet.getRowView(3));
                WritableSheet.addCell(new Label(0,5 + page * 24 - (page / 2),OrderObject.name() + "編號:" + SearchNonePayReceive_Bean.getObjectID() + "                 結案單據:全部           製表日期:" + (Integer.parseInt(ToolKit.getToday("yyyy")) - 1911) + "/" + ToolKit.getToday("MM/dd"),WritableCellFormat[5]));
                WritableSheet.mergeCells(0,5 + page * 24 - (page / 2),7,5 + page * 24 - (page / 2));
                WritableSheet.addCell(new Label(0,6 + page * 24 - (page / 2),"起始/結束期間:" + ConditionalPayReceiveSearch_Bean.getCheckoutByMonthStartDate() + "~" + ConditionalPayReceiveSearch_Bean.getCheckoutByMonthEndDate(),WritableCellFormat[5]));
                WritableSheet.mergeCells(0,6 + page * 24 - (page / 2),7,6 + page * 24 - (page / 2));
                WritableSheet.addCell(new Label(0,7 + page * 24 - (page / 2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", WritableCellFormat[5]));
                WritableSheet.mergeCells(0,7 + page * 24 - (page / 2),7,7 + page * 24 - (page / 2));

                for(int index = 0 ; index < NonePayReceiveDetailsColumnName.length ; index++){
                    if(index == 1)    WritableSheet.addCell(new Label(index,8 + page * 24 - (page / 2),OrderObject.name() + NonePayReceiveDetailsColumnName[index],WritableCellFormat[5]));
                    else if(index == 9)    WritableSheet.addCell(new Label(index,8 + page * 24 - (page / 2),OrderObject.getPayableReceivableName(),WritableCellFormat[5]));
                    else    WritableSheet.addCell(new Label(index,8 + page * 24 - (page / 2),NonePayReceiveDetailsColumnName[index],WritableCellFormat[5]));
                }

                int total_count=0;
                WritableSheet.addCell(new Label(0,9 + page * 24 - (page / 2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^",WritableCellFormat[5]));
                WritableSheet.mergeCells(0,9 + page * 24 - (page / 2),7,9 + page * 24 - (page / 2));

                int TotalPriceIncludeTax = 0;
                for(int index= page * 10 ; index < (page + 1) * 10 ; index++){
                    if(index < NonePayReceiveDetailsList.size()){
                        SearchOrder_Bean SearchOrder_Bean = NonePayReceiveDetailsList.get(index);
                        int Position = 10 + page * 24 - (page / 2) + total_count;
                        WritableSheet.addCell(new Label(0,Position,"" + (total_count + 1), WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(1,Position, SearchOrder_Bean.getObjectID() + " " + SearchOrder_Bean.getObjectNickName(), WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(2,Position, SearchOrder_Bean.getOrderNumber(),WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(3,Position, SearchOrder_Bean.getOrderDate(),WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(4,Position, ""+ SearchOrder_Bean.getOrderSource().name(),WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(5,Position, ""+ SearchOrder_Bean.getPayableReceivableDiscount(),WritableCellFormat[5]));

                        WritableSheet.addCell(new Label(6,Position, ""+ SearchOrder_Bean.getTotalPriceNoneTax(),WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(7,Position, ""+ SearchOrder_Bean.getTax(),WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(8,Position, ""+ SearchOrder_Bean.getDiscount(),WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(9,Position, ""+ SearchOrder_Bean.getTotalPriceIncludeTax(),WritableCellFormat[5]));
                        WritableSheet.addCell(new Label(10,Position, SearchOrder_Bean.getInstallment() == null ? "" : ""+SearchOrder_Bean.getInstallment(),WritableCellFormat[5]));
                        if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 ||  SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
                            TotalPriceIncludeTax = TotalPriceIncludeTax + SearchOrder_Bean.getTotalPriceIncludeTax();
                        else if(SearchOrder_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
                            TotalPriceIncludeTax = TotalPriceIncludeTax - SearchOrder_Bean.getTotalPriceIncludeTax();
                    }
                    total_count++;
                }
                WritableSheet.addCell(new Label(0,20 + page * 24 - (page / 2),"****小計****    " + OrderObject.getPayableReceivableName() + "(" + TotalPriceIncludeTax + ")",WritableCellFormat[6]));
                WritableSheet.mergeCells(0,20 + page * 24 - (page / 2),6,20 + page * 24 - (page / 2));

                if(page == ExcelPage - 1){
                    WritableSheet.addCell(new Label(0,22 + page * 24 - (page / 2),"****總計****    " + OrderObject.getPayableReceivableName() + "(" + SearchNonePayReceive_Bean.getActualPayReceivePrice() + ")",WritableCellFormat[6]));
                    WritableSheet.mergeCells(0,22 + page * 24 - (page / 2),6,22 + page * 24 - (page / 2));
                }
            }
            for(int index = 0 ; index < NonePayReceiveDetailsColumnName.length ; index++)   WritableSheet.setColumnView(index, PayableReceivableSheet.getColumnView(index));
            SheetSettings sts = WritableSheet.getSettings();
            sts.setPaperSize(PaperSize.NOTE);
            sts.setLeftMargin(0.0);
            sts.setRightMargin(0.0);
            sts.setTopMargin(0.0);
            sts.setBottomMargin(0.0);
            WritableWorkbook.write();
            WritableWorkbook.close();
            if(order_exportPayReceiveDetails == Order_ExportPayReceiveDetails.export){
                return true;
            }else if(order_exportPayReceiveDetails == Order_ExportPayReceiveDetails.print){
                try{
                    if(Desktop.isDesktopSupported()){
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.PRINT))
                            desktop.print(new File(outputFilePath));
                        return true;
                    }else{
                        ERPApplication.Logger.warn("debug: desktop not supported!");
                        DialogUI.AlarmDialog("debug: desktop not supported!");
                    }
                }catch (Exception ex){
                    ERPApplication.Logger.catching(ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(ex));
                    return false;
                }
            }
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            DialogUI.ExceptionDialog(ex);
        }
        return false;
    }

    private WritableCellFormat[] setExcelFont() throws Exception{
        WritableCellFormat[] WritableCellFormat =new WritableCellFormat[7];
        WritableFont[] Font = new WritableFont[7];
        for (int i = 0; i < WritableCellFormat.length; i++) {
            if (i == 0) Font[i] = new WritableFont(WritableFont.TIMES, 20, WritableFont.NO_BOLD);
            else if (i == 1)    Font[i] = new WritableFont(WritableFont.createFont("細明體"), 16, WritableFont.BOLD);
            else if (i == 2)    Font[i] = new WritableFont(WritableFont.createFont("Times New Roman"), 16,  WritableFont.NO_BOLD);
            else if (i == 3)    Font[i] = new WritableFont(WritableFont.createFont("Times New Roman"), 11, WritableFont.NO_BOLD);
            else    Font[i] = new WritableFont(WritableFont.createFont("Times New Roman"), 11,  WritableFont.NO_BOLD);
            WritableCellFormat[i] = new WritableCellFormat(Font[i]);
            WritableCellFormat[i].setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.NONE);
            if (i == 0 || i == 1 || i == 4)   WritableCellFormat[i].setAlignment(jxl.format.Alignment.CENTRE);
            else if (i == 5)    WritableCellFormat[i].setAlignment(jxl.format.Alignment.LEFT);
            else if (i == 6)    WritableCellFormat[i].setAlignment(jxl.format.Alignment.RIGHT);
        }
        return WritableCellFormat;
    }
}
