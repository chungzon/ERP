package ERP.Model.Order.ExportPurchaseQuotationDocument;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Enum.Order.Order_Enum.OrderTaxStatus;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_ExportPurchaseFunction;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.Order.Order_Model;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ToolKit;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportPurchaseQuotation_Model {

    private ToolKit ToolKit;
    private CallConfig CallConfig;
    private Order_Model Order_Model;
    private SystemSetting_Model SystemSetting_Model;

    private Order_ExportPurchaseFunction exportPurchaseFunction;

    private XSSFWorkbook workbook;
    private FileInputStream fileInputStream;
    private XSSFSheet sheet;
    public ExportPurchaseQuotation_Model(){
        this.ToolKit = ERPApplication.ToolKit;
        this.CallConfig = ToolKit.CallConfig;
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    public void readTemplate(String templatePath) throws IOException {
        fileInputStream = new FileInputStream(new File(templatePath));
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheetAt(0);//讀取wb內的頁面
    }
    public void setData(Order_ExportPurchaseFunction exportPurchaseFunction,
                        String orderDate,
                        String orderNumber,
                        OrderTaxStatus orderTaxStatus,
                        List<OrderProduct_Bean> productList,
                        String orderRemark,
                        boolean isShowPrice){
            JSONObject companyInfo = SystemSetting_Model.getSpecificSystemSettingData(SystemSettingConfig.公司匯出格式) == null ? null : JSONObject.parseObject(SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.公司匯出格式));

        setXSSFSheetCell(sheet,0,18,exportPurchaseFunction.getDescribe(),null,null);
        if(companyInfo != null){
            setXSSFSheetCell(sheet,0,2,companyInfo.getString(SystemSetting_Enum.ExportCompanyFormat.storeName.name()),HorizontalAlignment.LEFT,VerticalAlignment.CENTER);
            setXSSFSheetCell(sheet,3,4,companyInfo.getString(SystemSetting_Enum.ExportCompanyFormat.storeName.name()),HorizontalAlignment.LEFT,VerticalAlignment.CENTER);
            setXSSFSheetCell(sheet,7,4,companyInfo.getString(SystemSetting_Enum.ExportCompanyFormat.companyAddress.name()),HorizontalAlignment.LEFT,VerticalAlignment.CENTER);
        }
        setXSSFSheetCell(sheet,2,4,orderNumber,HorizontalAlignment.LEFT,VerticalAlignment.CENTER);
        setXSSFSheetCell(sheet,2,15,orderDate.replace("-","/"),HorizontalAlignment.LEFT,VerticalAlignment.CENTER);

//        String combineRemark = "";
        for(int index = 0; index < productList.size(); index++){
            OrderProduct_Bean OrderProduct_Bean = productList.get(index);
            setXSSFSheetCell(sheet,9+index,2,OrderProduct_Bean.getProductName(),HorizontalAlignment.LEFT,VerticalAlignment.CENTER);
            setXSSFSheetCell(sheet,9+index,13,OrderProduct_Bean.getUnit(),HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
            setXSSFSheetCell(sheet,9+index,14,OrderProduct_Bean.getQuantity(),HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
            if(isShowPrice){
                setXSSFSheetCell(sheet,9+index,15,OrderProduct_Bean.getBatchPrice(),HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
                sheet.getRow(9+index).getCell(18).setCellFormula("O" + (10+index) + "*P" + (10+index));
            }
            setXSSFSheetCell(sheet,9+index,20,OrderProduct_Bean.getISBN(),HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
            /*if(OrderProduct_Bean.getRemark() != null && !OrderProduct_Bean.getRemark().equals("")){
                if(index == 0)
                    combineRemark = "【" + OrderProduct_Bean.getISBN() + "】" + OrderProduct_Bean.getRemark();
                else
                    combineRemark = combineRemark + "\n【" + OrderProduct_Bean.getISBN() + "】" + OrderProduct_Bean.getRemark();
            }*/
        }
        sheet.getRow(27).getCell(3).getCellStyle().setWrapText(true);
        setXSSFSheetCell(sheet,27,3,orderRemark,HorizontalAlignment.LEFT,VerticalAlignment.TOP);
        if(isShowPrice){
            sheet.getRow(24).getCell(18).setCellFormula("SUM(S10:S24)");
            sheet.getRow(25).getCell(18).setCellFormula(orderTaxStatus == OrderTaxStatus.含稅 ? "S25*0.05" : "0");
            sheet.getRow(26).getCell(18).setCellFormula("S25+S26");
        }
    }
    public void build(String outputFile) throws IOException {
        sheet.setForceFormulaRecalculation(true);

        File file = new File(outputFile);
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();

        fileInputStream.close();
        workbook.close();
    }
    private void setXSSFSheetCell(XSSFSheet XSSFSheet, int Row, int Cell, String Value, HorizontalAlignment Alignment, VerticalAlignment VerticalAlignment){
        XSSFSheet.getRow(Row).getCell(Cell).setCellValue(Value);
        if(Alignment != null)   XSSFSheet.getRow(Row).getCell(Cell).getCellStyle().setAlignment(Alignment);
        if(VerticalAlignment != null)   XSSFSheet.getRow(Row).getCell(Cell).getCellStyle().setVerticalAlignment(VerticalAlignment);
    }
    private void setXSSFSheetCell(XSSFSheet XSSFSheet, int Row, int Cell, int Value, HorizontalAlignment Alignment, VerticalAlignment VerticalAlignment){
        XSSFSheet.getRow(Row).getCell(Cell).setCellValue(Value);
        if(Alignment != null)   XSSFSheet.getRow(Row).getCell(Cell).getCellStyle().setAlignment(Alignment);
        if(VerticalAlignment != null)   XSSFSheet.getRow(Row).getCell(Cell).getCellStyle().setVerticalAlignment(VerticalAlignment);
    }
    private void setXSSFSheetCell(XSSFSheet XSSFSheet, int Row, int Cell, double Value, HorizontalAlignment Alignment, VerticalAlignment VerticalAlignment){
        XSSFSheet.getRow(Row).getCell(Cell).setCellValue(Value);
        if(Alignment != null)   XSSFSheet.getRow(Row).getCell(Cell).getCellStyle().setAlignment(Alignment);
        if(VerticalAlignment != null)   XSSFSheet.getRow(Row).getCell(Cell).getCellStyle().setVerticalAlignment(VerticalAlignment);
    }
}
