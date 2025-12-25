package ERP.Model.ManagePayableReceivable;

import ERP.Bean.ManagePayableReceivable.IAECrawler_ManufacturerContactDetail_Bean;
import ERP.ERPApplication;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.ToolKit.TemplatePath;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

public class ManufacturerContactDocument extends BasicExcelBuilder{
    private XSSFSheetConditionalFormatting conditionalFormatting;

    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    public ManufacturerContactDocument(){
        this.ManagePayableReceivable_Model = ERPApplication.ToolKit.ModelToolKit.getManagePayableReceivableModel();
    }

    /***
     * create work book by designated template xlsx file
     * @param filePath template file path
     * @throws IOException throw IOException when template file read fail
     */
    public void readTemplate(String filePath) throws IOException {
        fileInputStream = new FileInputStream(new File(filePath));
        workbook = new XSSFWorkbook(fileInputStream);
        Font font = workbook.createFont();
        font.setCharSet(XSSFFont.ANSI_CHARSET);
        template = workbook.getSheetAt(0);
        sheet = workbook.createSheet();
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short)1);
        sheet.getPrintSetup().setFitHeight((short)0);
        copyPrintSetup(template.getPrintSetup(),sheet.getPrintSetup());
        copyColumn(11);
        XSSFRow titleRow = sheet.createRow(0);
        copyRow(titleRow,0);
        XSSFRow blankRow = sheet.createRow(1);
        copyRowStyle(blankRow,1);
        blankRow.setHeight(getExcelHeight(9));
        conditionalFormatting = sheet.getSheetConditionalFormatting();
    }

    /***
     * add row to work book
     * @param date 日期
     * @param invoiceAmount 已開發票
     * @param noInvoiceAmount 未開發票
     * @param productName 品名
     * @param invoiceNumber 發票號碼
     * @param invoiceType 聯單
     * @param schoolPaid 學校已付金額
     * @param schoolUnPaid 未給付金額
     * @param refund 繳、退金額
     * @param description 備註
     */
    public void addRow(boolean setErrorBackgroundColor,
            LocalDate date,
            Integer invoiceAmount,
            Integer noInvoiceAmount,
            String productName,
            String invoiceNumber,
            String invoiceType,
            Integer schoolPaid,
            Integer schoolUnPaid,
            Integer refund,
            String description
    ){
        int rowIndex = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowIndex);
        copyRowStyle(row,5);
        XSSFCell cell = row.getCell(0);
        CellStyle dateStyle = getBasicStyle();
        CreationHelper helper = workbook.getCreationHelper();
        dateStyle.setDataFormat(helper.createDataFormat().getFormat("yyyy年MM月dd日"));
        cell.setCellStyle(dateStyle);
        cell.setCellValue(
                Date.from(
                    date.atStartOfDay()
                            .atZone(ZoneId.systemDefault())
                            .toInstant()
        ));
        if(invoiceAmount != null)   row.getCell(1).setCellValue(invoiceAmount);
        if(noInvoiceAmount != null) row.getCell(2).setCellValue(noInvoiceAmount);
        addConditionalSetting(rowIndex+1);
        XSSFCell totalCell = row.createCell(3);
        totalCell.setCellFormula("(C"+(rowIndex+1)+"+D"+rowIndex+")-B"+(rowIndex+1));
        XSSFCellStyle moneyStyle = template.getRow(2).getCell(3).getCellStyle();
        moneyStyle.setDataFormat(getMoneyFormat("$0"));
        totalCell.setCellStyle(moneyStyle);
        row.getCell(4).setCellValue(productName);
        row.getCell(5).setCellValue(invoiceNumber);
        row.getCell(6).setCellValue(invoiceType);
        if(schoolPaid != null)  row.getCell(7).setCellValue(schoolPaid);
        if(schoolUnPaid != null)    row.getCell(8).setCellValue(schoolUnPaid);
        if(refund != null)  row.getCell(9).setCellValue(refund);
        row.getCell(10).setCellValue(description);

        if(setErrorBackgroundColor){
            CellStyle stringCellStyle = getBasicStyle();
            stringCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            stringCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle numericCellStyle = getBasicStyle();
            numericCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            numericCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            numericCellStyle.setDataFormat(getMoneyFormat("#,###,###,###"));

            CellStyle moneyCellStyle = getBasicStyle();
            moneyCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            moneyCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            moneyCellStyle.setDataFormat(getMoneyFormat("$0"));

            row.getCell(0).getCellStyle().setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            row.getCell(0).getCellStyle().setFillPattern(FillPatternType.SOLID_FOREGROUND);
            row.getCell(1).setCellStyle(numericCellStyle);
            row.getCell(2).setCellStyle(numericCellStyle);
            row.getCell(3).setCellStyle(moneyCellStyle);
            row.getCell(4).setCellStyle(stringCellStyle);
            row.getCell(5).setCellStyle(stringCellStyle);
            row.getCell(6).setCellStyle(stringCellStyle);
            row.getCell(7).setCellStyle(numericCellStyle);
            row.getCell(8).setCellStyle(numericCellStyle);
            row.getCell(9).setCellStyle(stringCellStyle);
            row.getCell(10).setCellStyle(stringCellStyle);
        }
    }
    private void createTotalRow(){
        int rowIndex = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowIndex);
        copyRowStyle(row,11);
        XSSFRow templateRow = template.getRow(11);
        CellStyle style = templateRow.getCell(1).getCellStyle();
        XSSFCell title = row.getCell(0);
        title.setCellValue(
                templateRow
                        .getCell(0)
                        .getStringCellValue()
        );
        XSSFCell invoiceTotal = row.getCell(1);
        style.setDataFormat(getMoneyFormat("$0"));
        invoiceTotal.setCellStyle(style);
        invoiceTotal.setCellFormula("SUM(B3:B"+rowIndex+")");
        XSSFCell unTotal = row.getCell(2);
        unTotal.setCellStyle(style);
        unTotal.setCellFormula("SUM(C3:C"+rowIndex+")");
        XSSFCell remain = row.getCell(3);
        remain.setCellStyle(style);
        remain.setCellFormula("D"+rowIndex);
        XSSFCell schoolTotal = row.getCell(7);
        schoolTotal.setCellStyle(style);
        schoolTotal.setCellFormula("SUM(H3:H"+rowIndex+")");
        XSSFCell unPaidTotal = row.getCell(8);
        unPaidTotal.setCellStyle(style);
        unPaidTotal.setCellFormula("SUM(I3:I"+rowIndex+")");
        XSSFCell comment = row.getCell(10);
        XSSFCell taxTitle = template.getRow(11).getCell(10);
        comment.setCellValue(taxTitle.getStringCellValue());
        comment.setCellStyle(taxTitle.getCellStyle());
    }
    private void createTaxRow(){
        int rowIndex = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowIndex);
        copyRowStyle(row,12);
        XSSFRow templateRow = template.getRow(12);
        row.getCell(4).setCellValue(templateRow.getCell(4).getStringCellValue());
        if(isTemplateExistTax){
            XSSFCell sumCell = row.getCell(8);
            sumCell.setCellFormula("(-D"+(rowIndex-1)+"/1.05)*K"+(rowIndex+1));
            XSSFCell taxCell = row.getCell(10);
            taxCell.setCellValue(templateRow.getCell(10).getNumericCellValue());
        }
    }
    private void createSumRow(){
        int rowIndex = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowIndex);
        copyRowStyle(row,13);
        XSSFRow templateRow = template.getRow(13);
        row.getCell(0).setCellValue(templateRow.getCell(0).getStringCellValue());
        XSSFCell schoolSum = row.getCell(7);
        schoolSum.setCellFormula("SUM(H"+(rowIndex-1)+")");
        XSSFCell unPaidSum = row.getCell(8);
        unPaidSum.setCellFormula("SUM(I"+rowIndex+":I"+(rowIndex-1)+")");
        XSSFCell refundSum = row.getCell(9);
        refundSum.setCellFormula("H"+(rowIndex+1)+"-I"+(rowIndex+1));
    }
    private void addConditionalSetting(int index){
        ConditionalFormattingRule rule = createConditionalSettingRule("AND($D"+index+"<0)");
        FontFormatting fontFormatting = rule.createFontFormatting();
        fontFormatting.setFontColorIndex(IndexedColors.RED.index);
        ConditionalFormattingRule[] cfRules = new ConditionalFormattingRule[]{rule};
        CellRangeAddress[] regions = new CellRangeAddress[]{CellRangeAddress.valueOf("D"+index)};
        conditionalFormatting.addConditionalFormatting(regions,cfRules);
    }
    private ConditionalFormattingRule createConditionalSettingRule(String formula){
        return conditionalFormatting.createConditionalFormattingRule(formula);
    }

    /***
     * build workbook result and close it
     * @param filePath export file to designated file path
     * @return return generated file
     * @throws IOException throw IOException when fail to export file
     */
    @Override
    public File build(String filePath) throws IOException {
        sheet.setForceFormulaRecalculation(true);
        createTotalRow();
        createTaxRow();
        createSumRow();
        workbook.removeSheetAt(0);
        return super.build(filePath);
    }
    private boolean isTemplateExistTax = true;
    private HashMap<Integer,Boolean> differentIndexMap = null;
    public boolean formulaVerification(File templateFile, String outputFilePath, String objectID){
        if(differentIndexMap != null)   differentIndexMap.clear();
        try{
            ObservableList<IAECrawler_ManufacturerContactDetail_Bean> templateContactDetailList = generateTemplateContactDetailList(templateFile);
            ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailList = getManufacturerContactDetailList(objectID,templateContactDetailList);
            differentIndexMap = verifyDataDifferentIndex(templateContactDetailList,manufacturerContactDetailList);
            this.readTemplate(TemplatePath.IAECrawlerInvoiceOverview);
            for(int index = 0 ; index < manufacturerContactDetailList.size() ; index++){
                IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean = manufacturerContactDetailList.get(index);
                this.addRow((differentIndexMap != null && differentIndexMap.containsKey(index)),
                        LocalDate.parse(IAECrawler_ManufacturerContactDetail_Bean.getOrderDate()),
                        IAECrawler_ManufacturerContactDetail_Bean.getAlreadyInvoiceAmount(),
                        (IAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource() == PayableReceivable_Enum.ManufacturerContactDetailSource.廠商進貨 && IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已開發票) ?
                                null: IAECrawler_ManufacturerContactDetail_Bean.getNoneInvoiceAmount(),
                        IAECrawler_ManufacturerContactDetail_Bean.getProjectName(),
                        IAECrawler_ManufacturerContactDetail_Bean.getInvoiceNumber(),
                        IAECrawler_ManufacturerContactDetail_Bean.getInvoiceType(),
                        IAECrawler_ManufacturerContactDetail_Bean.getSchoolAlreadyAmount(),
                        (IAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource() == PayableReceivable_Enum.ManufacturerContactDetailSource.廠商進貨 && IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已給付貨款金額) ?
                                null : IAECrawler_ManufacturerContactDetail_Bean.getSchoolNoneAmount(),
                        null,
                        IAECrawler_ManufacturerContactDetail_Bean.getCashierRemark()
                );
            }
            this.build(outputFilePath);
            return true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            DialogUI.ExceptionDialog(Ex);
            return false;
        }
    }
    private ObservableList<IAECrawler_ManufacturerContactDetail_Bean> getManufacturerContactDetailList(String objectID, ObservableList<IAECrawler_ManufacturerContactDetail_Bean> templateContactDetailList){
        String startDate = templateContactDetailList.get(0).getOrderDate();
        String endDate = templateContactDetailList.get(templateContactDetailList.size()-1).getOrderDate();

        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetail = ManagePayableReceivable_Model.searchManufacturerContactDetail(objectID,startDate,endDate, null);
        for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : manufacturerContactDetail){
            if(!IAECrawler_ManufacturerContactDetail_Bean.getIAECrawlerAccount_BelongName().equals(""))
                IAECrawler_ManufacturerContactDetail_Bean.setCashierRemark("* 匯費：" + IAECrawler_ManufacturerContactDetail_Bean.getRemittanceFee() + " 元；" + IAECrawler_ManufacturerContactDetail_Bean.getCashierRemark() + "(" + IAECrawler_ManufacturerContactDetail_Bean.getIAECrawlerAccount_BelongName() + ")");
        }
        return manufacturerContactDetail;
    }
    private HashMap<Integer,Boolean> verifyDataDifferentIndex(ObservableList<IAECrawler_ManufacturerContactDetail_Bean> templateContactDetailList,
                                                        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailList){
        HashMap<Integer,Boolean> differentIndexMap = null;
        for(int index = 0; index < templateContactDetailList.size() ; index++){
            IAECrawler_ManufacturerContactDetail_Bean templateContactDetailBean = templateContactDetailList.get(index);
//            System.out.println("上傳：" + templateContactDetailList.get(index).getOrderDate() + " " + templateContactDetailList.get(index).getProjectName());
            if(index <= manufacturerContactDetailList.size()-1){
                IAECrawler_ManufacturerContactDetail_Bean manufacturerContactDetailBean = manufacturerContactDetailList.get(index);
                if(!templateContactDetailBean.getOrderDate().equals(manufacturerContactDetailBean.getOrderDate()) ||

                    (templateContactDetailBean.getAlreadyInvoiceAmount() != null && manufacturerContactDetailBean.getAlreadyInvoiceAmount() == null) ||
                    (templateContactDetailBean.getAlreadyInvoiceAmount() == null && manufacturerContactDetailBean.getAlreadyInvoiceAmount() != null) ||
                    (templateContactDetailBean.getAlreadyInvoiceAmount() != null && manufacturerContactDetailBean.getAlreadyInvoiceAmount() != null &&
                    !templateContactDetailBean.getAlreadyInvoiceAmount().equals(manufacturerContactDetailBean.getAlreadyInvoiceAmount())) ||

                    (templateContactDetailBean.getNoneInvoiceAmount() != null && manufacturerContactDetailBean.getNoneInvoiceAmount() == null) ||
                    (templateContactDetailBean.getNoneInvoiceAmount() == null && manufacturerContactDetailBean.getNoneInvoiceAmount() != null) ||
                    (templateContactDetailBean.getNoneInvoiceAmount() != null && manufacturerContactDetailBean.getNoneInvoiceAmount() != null &&
                    !templateContactDetailBean.getNoneInvoiceAmount().equals(manufacturerContactDetailBean.getNoneInvoiceAmount())) ||

                    !templateContactDetailBean.getProjectName().equals(manufacturerContactDetailBean.getProjectName()) ||

                    (templateContactDetailBean.getInvoiceNumber() != null && manufacturerContactDetailBean.getInvoiceNumber() == null) ||
                    (templateContactDetailBean.getInvoiceNumber() == null && manufacturerContactDetailBean.getInvoiceNumber() != null) ||
                    (templateContactDetailBean.getInvoiceNumber() != null && manufacturerContactDetailBean.getInvoiceNumber() != null &&
                    !templateContactDetailBean.getInvoiceType().equals(manufacturerContactDetailBean.getInvoiceType())) ||

                    (templateContactDetailBean.getInvoiceType() != null && manufacturerContactDetailBean.getInvoiceType() == null) ||
                    (templateContactDetailBean.getInvoiceType() == null && manufacturerContactDetailBean.getInvoiceType() != null) ||
                    (templateContactDetailBean.getInvoiceType() != null && manufacturerContactDetailBean.getInvoiceType() != null &&
                    !templateContactDetailBean.getInvoiceType().equals(manufacturerContactDetailBean.getInvoiceType())) ||

                    (templateContactDetailBean.getSchoolAlreadyAmount() != null && manufacturerContactDetailBean.getSchoolAlreadyAmount() == null) ||
                    (templateContactDetailBean.getSchoolAlreadyAmount() == null && manufacturerContactDetailBean.getSchoolAlreadyAmount() != null) ||
                    (templateContactDetailBean.getSchoolAlreadyAmount() != null && manufacturerContactDetailBean.getSchoolAlreadyAmount() != null &&
                    !templateContactDetailBean.getSchoolAlreadyAmount().equals(manufacturerContactDetailBean.getSchoolAlreadyAmount())) ||

                    (templateContactDetailBean.getSchoolNoneAmount() != null && manufacturerContactDetailBean.getSchoolNoneAmount() == null) ||
                    (templateContactDetailBean.getSchoolNoneAmount() == null && manufacturerContactDetailBean.getSchoolNoneAmount() != null) ||
                    (templateContactDetailBean.getSchoolNoneAmount() != null && manufacturerContactDetailBean.getSchoolNoneAmount() != null &&
                    !templateContactDetailBean.getSchoolNoneAmount().equals(manufacturerContactDetailBean.getSchoolNoneAmount())) ||

                    !templateContactDetailBean.getCashierRemark().equals(manufacturerContactDetailBean.getCashierRemark())){
                    if(differentIndexMap == null)  differentIndexMap = new HashMap<>();
                    differentIndexMap.put(index,true);
                }
            }else{
                if(differentIndexMap == null)   differentIndexMap = new HashMap<>();
                    differentIndexMap.put(index,true);
            }

//            System.out.println("--------------------------------------------------------------------------------");
        }
        return differentIndexMap;
    }

    private ObservableList<IAECrawler_ManufacturerContactDetail_Bean> generateTemplateContactDetailList(File templateFile) throws IOException {
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> templateContactDetailList = FXCollections.observableArrayList();
        InputStream inputStream = new FileInputStream(templateFile);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet template = workbook.getSheetAt(0);
        int lastRow = template.getLastRowNum();
        for(int row = 2 ; row < lastRow-2 ; row++){
            IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean = new IAECrawler_ManufacturerContactDetail_Bean();
            String orderDate;
            Integer invoiceAmount = null,noInvoiceAmount = null;
            Integer schoolPaid = null,schoolUnPaid = null;
            for(int cell = 0; cell < template.getRow(lastRow).getLastCellNum() ; cell++){
                DataFormatter formatter = new DataFormatter();
                String data = formatter.formatCellValue(template.getRow(row).getCell(cell));
//                    System.out.println(row + "-" + cell + " = " + data);
                if(cell == 0){
                    orderDate = data.replace("年","-").replace("月","-").replace("日","").replace("\"","");
                    IAECrawler_ManufacturerContactDetail_Bean.setOrderDate(orderDate);
                }else if(cell == 1){
                    if(!data.equals(""))
                        invoiceAmount = Integer.parseInt(data.replace(",",""));
                    IAECrawler_ManufacturerContactDetail_Bean.setAlreadyInvoiceAmount(invoiceAmount);
                }else if(cell == 2){
                    if(!data.equals("")) {
                        noInvoiceAmount = Integer.parseInt(data.replace(",", ""));
                        IAECrawler_ManufacturerContactDetail_Bean.setManufacturerContactDetailSource(PayableReceivable_Enum.ManufacturerContactDetailSource.廠商進貨);
                        IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部);
                    }
                    IAECrawler_ManufacturerContactDetail_Bean.setNoneInvoiceAmount(noInvoiceAmount);
                }else if(cell == 4)  IAECrawler_ManufacturerContactDetail_Bean.setProjectName(data);
                else if(cell == 5)  IAECrawler_ManufacturerContactDetail_Bean.setInvoiceNumber(data.equals("") ? null : data);
                else if(cell == 6)  IAECrawler_ManufacturerContactDetail_Bean.setInvoiceType(data.equals("") ? null : data);
                else if(cell == 7){
                    if(!data.equals(""))
                        schoolPaid = Integer.parseInt(data.replace(",",""));
                    IAECrawler_ManufacturerContactDetail_Bean.setSchoolAlreadyAmount(schoolPaid);
                }else if(cell == 8) {
                    if(!data.equals("")) {
                        schoolUnPaid = Integer.parseInt(data.replace(",", ""));
                        IAECrawler_ManufacturerContactDetail_Bean.setManufacturerContactDetailSource(PayableReceivable_Enum.ManufacturerContactDetailSource.廠商進貨);
                        IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部);
                    }
                    IAECrawler_ManufacturerContactDetail_Bean.setSchoolNoneAmount(schoolUnPaid);
                }else if(cell == 10)  IAECrawler_ManufacturerContactDetail_Bean.setCashierRemark(data);
            }
            templateContactDetailList.add(IAECrawler_ManufacturerContactDetail_Bean);
        }
        DataFormatter formatter = new DataFormatter();
        String data = formatter.formatCellValue(template.getRow(lastRow-1).getCell(8));
        isTemplateExistTax = !data.equals("");

        inputStream.close();
        workbook.close();
        return templateContactDetailList;
    }
    public double getVerifyFinallyPrice(String outputFilePath){
        double finallyPrice = 0;
        try{
            InputStream inputStream = new FileInputStream(new File(outputFilePath));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet template = workbook.getSheetAt(0);
            int lastRow = template.getLastRowNum();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            Cell cell = template.getRow(lastRow).getCell(9);
            CellValue value = evaluator.evaluate(cell);
            finallyPrice = value.getNumberValue();

            inputStream.close();
            workbook.close();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            DialogUI.ExceptionDialog(Ex);
        }
        return finallyPrice;
    }
    public int getVerifyDifferentDataSize(){
        return differentIndexMap == null ? 0:differentIndexMap.size();
    }
}
