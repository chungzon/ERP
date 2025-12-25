package ERP.Model.ManagePayableReceivable;


import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Model.Order.SearchNonePayReceive.SearchNonePayReceive_Model;
import ERP.ToolKit.ToolKit;
import ERP.Enum.ToolKit.ToolKit_Enum.ChineseMonth;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ExportPayableDocument extends BasicExcelBuilder{
    private ToolKit ToolKit;
    private short dataRowHeight = 0;
    private LocalDate startDate,endDate;
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    private SearchNonePayReceive_Model SearchNonePayReceive_Model;
    public ExportPayableDocument(String startDate,String endDate){
        this.ToolKit = ERPApplication.ToolKit;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
        this.ManagePayableReceivable_Model = ToolKit.ModelToolKit.getManagePayableReceivableModel();
        this.SearchNonePayReceive_Model = ToolKit.ModelToolKit.getSearchNonePayReceiveModel();
    }

    public void readTemplate(String filePath) throws IOException {
        fileInputStream = new FileInputStream(new File(filePath));
        workbook = new XSSFWorkbook(fileInputStream);
        Font font = workbook.createFont();
        font.setCharSet(XSSFFont.ANSI_CHARSET);
        template = workbook.getSheetAt(0);
        dataRowHeight = template.getRow(2).getHeight();
        sheet = workbook.createSheet();
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitWidth((short)1);
        sheet.getPrintSetup().setFitHeight((short)0);
        copyPrintSetup(template.getPrintSetup(),sheet.getPrintSetup());
        copyColumn(10);

        sheet.addMergedRegion(getMergeCellRange(0,0,0,3));
        sheet.addMergedRegion(getMergeCellRange(0,0,4,7));
        XSSFRow titleRow = sheet.createRow(0);
        copyRowStyle(titleRow,0);

        int startYear = startDate.getYear()-1911;
        int startMonth = startDate.getMonthValue();
        int endYear = endDate.getYear()-1911;
        int endMonth = endDate.getMonthValue();

        String titleDate;
        if(startYear == endYear){
            titleDate = startYear + "年";
            if(startMonth == endMonth)  titleDate = titleDate + startMonth + "月份";
            else    titleDate = titleDate + startMonth + "~" + endMonth + "月份";
        }else
            titleDate = startYear + "年" + startMonth + "月份 ~ " + endYear + "年" + endMonth + "月份";

        titleRow.getCell(0).setCellValue(titleDate);
        titleRow.getCell(4).setCellValue("廠商帳款總表(應付帳款)");
        String[] dateArray = ToolKit.getToday("yyyy-MM-dd").split("-");
        titleRow.getCell(8).setCellValue((Integer.parseInt(dateArray[0])-1911) + "/" + dateArray[1] + "/" + dateArray[2]);

        XSSFRow secondRow = sheet.createRow(1);
        copyRow(secondRow,1);
    }
    public void createTopTable(SystemSettingConfig_Bean SystemSettingConfig_Bean, ObservableList<SearchNonePayReceive_Bean> SearchNonePayReceiveList){
        int checkNumberIndex = 0;
        for(SearchNonePayReceive_Bean searchNonePayReceive_Bean : SearchNonePayReceiveList){
            if(searchNonePayReceive_Bean.isCheckBoxSelect()){
                SearchNonePayReceive_Model.getManufacturerInfo(searchNonePayReceive_Bean);
                String checkDueDate = null,checkNumber = null;
                if(searchNonePayReceive_Bean.getDefaultPaymentMethod() == ObjectInfo_Enum.DefaultPaymentMethod.支票){
                    checkDueDate = ManagePayableReceivable_Model.generateCheckDueDate(searchNonePayReceive_Bean.getCheckDueDay());
                    checkNumber = SystemSettingConfig_Bean.getNowCheckNumber();
                    if(checkNumber != null && !checkNumber.equals(""))
                        checkNumber = checkNumber.substring(0,2) + ToolKit.fillZero(Integer.parseInt(checkNumber.substring(2))+checkNumberIndex,7);
                }
                addRow(
                        searchNonePayReceive_Bean.getObjectID(),
                        searchNonePayReceive_Bean.getObjectNickName(),
                        searchNonePayReceive_Bean.getCheckTitle(),
                        searchNonePayReceive_Bean.isDiscountPostage() ?
                                searchNonePayReceive_Bean.getActualPayReceivePrice() - searchNonePayReceive_Bean.getPostage() : searchNonePayReceive_Bean.getActualPayReceivePrice(),
                        searchNonePayReceive_Bean.getCheckDueDay(),
                        checkDueDate == null ? null : LocalDate.parse(checkDueDate),
                        checkNumber,
                        searchNonePayReceive_Bean.isDiscountPostage() ? "* 郵資：" + searchNonePayReceive_Bean.getPostage() + " 元" : ""
                );
                if(searchNonePayReceive_Bean.getDefaultPaymentMethod() == ObjectInfo_Enum.DefaultPaymentMethod.支票)
                    checkNumberIndex++;
            }
        }
        createTotalAmount();
    }
    /***
     * add row to work book
     * @param objectID 廠商代號
     * @param objectNickName 廠商名稱
     * @param checkTitle 支票抬頭
     * @param offsetAmount 金額
     * @param checkDueDay 付款方式
     * @param checkDate 核定票期
     * @param checkNumber 支票號碼
     * @param remark 備註
     */
    private void addRow(
            String objectID,
            String objectNickName,
            String checkTitle,
            int offsetAmount,
            Integer checkDueDay,
            LocalDate checkDate,
            String checkNumber,
            String remark
    ){
        int rowIndex = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowIndex);
        row.setHeight(dataRowHeight);
        copyRowStyle(row,2);
        row.getCell(0).setCellValue(rowIndex-1);
        row.getCell(1).setCellValue(objectID);
        row.getCell(2).setCellValue(objectNickName);
        row.getCell(3).setCellValue(checkTitle);

        XSSFCell totalCell = row.createCell(4);
        XSSFCellStyle moneyStyle = template.getRow(2).getCell(4).getCellStyle();
        moneyStyle.setDataFormat(getMoneyFormat("$0"));
        totalCell.setCellStyle(moneyStyle);
        totalCell.setCellValue(offsetAmount);
        row.getCell(5).setCellValue("月結" + checkDueDay + "天");
        row.getCell(6).setCellValue(checkDate);
        row.getCell(7).setCellValue(checkNumber);
        row.getCell(8).setCellValue(remark);
    }
    private void createTotalAmount(){
        int rowIndex = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowIndex);
        copyRowStyle(row,17);
        XSSFRow templateRow = template.getRow(17);

        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,0,2));
        XSSFCell title = row.getCell(0);
        title.setCellValue(templateRow.getCell(0).getStringCellValue());
        CellStyle style = templateRow.getCell(4).getCellStyle();
        XSSFCell totalAmount = row.getCell(4);
        style.setDataFormat(getMoneyFormat("$0"));
        totalAmount.setCellStyle(style);
        if(rowIndex >= 3)
            totalAmount.setCellFormula("SUM(E3:E"+rowIndex+")");
    }

    public void createButtonTable(ObservableList<SearchNonePayReceive_Bean> SearchNonePayReceiveList, HashMap<LocalDate,Integer> checkDueDateAndCheckPriceMap){
        HashMap<Integer,HashMap<Integer,Integer>[]> calculateOffsetAmountArray = new HashMap<>();
        getNonePayReceiveOffsetAmount(calculateOffsetAmountArray,SearchNonePayReceiveList);
        if(checkDueDateAndCheckPriceMap != null)
            getAlreadyPayReceiveCheckPrice(calculateOffsetAmountArray,checkDueDateAndCheckPriceMap);

        int rowIndex = sheet.getLastRowNum()+3;
        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,2,3));
        XSSFRow row = sheet.createRow(rowIndex);
        XSSFCell title1 = row.createCell(2);
        title1.setCellStyle(template.getRow(19).getCell(2).getCellStyle());
        title1.setCellValue(template.getRow(19).getCell(2).getStringCellValue());

        String[] dateArray = ToolKit.getToday("yyyy-MM-dd").split("-");
        XSSFCell title2 = row.createCell(5);
        title2.setCellStyle(template.getRow(19).getCell(6).getCellStyle());
        title2.setCellValue("帳款截止至" + dateArray[1] + "/25止");

        rowIndex = sheet.getLastRowNum()+1;
        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,0,1));
        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,2,3));
        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,5,8));
        XSSFRow titleRow = sheet.createRow(rowIndex);
        copyRow(titleRow,20);

        XSSFCellStyle templateMonthStyle = template.getRow(21).getCell(0).getCellStyle();
        XSSFCellStyle templateCheckDueDateStyle = template.getRow(21).getCell(2).getCellStyle();
        XSSFCellStyle templateOffsetAmountStyle = template.getRow(21).getCell(4).getCellStyle();
        XSSFCellStyle templateRemarkStyle = template.getRow(21).getCell(6).getCellStyle();

        int calculateTableCount = 0;
        for(int checkYear : calculateOffsetAmountArray.keySet()){
            for(int month = 0 ; month < calculateOffsetAmountArray.get(checkYear).length ; month++){
                HashMap<Integer,Integer> dayOfAmountMap = calculateOffsetAmountArray.get(checkYear)[month];
                boolean isMergeCell = false;
                if(dayOfAmountMap != null){
                    LinkedHashMap<Integer, Integer> sortedMap = dayOfAmountMap.entrySet().stream().sorted(Map.Entry.comparingByKey(Integer::compareTo))
                            .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,(e1, e2)->e1,LinkedHashMap::new));
                    for(Integer day : sortedMap.keySet()){
                        calculateTableCount ++ ;
                        rowIndex = sheet.getLastRowNum()+1;
                        XSSFRow XSSFRow = sheet.createRow(rowIndex);
                        copyRowStyle(XSSFRow,23);

                        if(!isMergeCell){
                            if(dayOfAmountMap.size() > 1){
                                sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex+dayOfAmountMap.size()-1,0,1));
                                isMergeCell = true;
                            }else
                                sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,0,1));
                            XSSFCell monthCell = XSSFRow.createCell(0);
                            monthCell.setCellStyle(templateMonthStyle);
                            monthCell.setCellValue(ChineseMonth.values()[month].name());
                        }

                        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,2,3));
                        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,5,8));

                        XSSFCell checkDueDateCell = XSSFRow.createCell(2);
                        checkDueDateCell.setCellStyle(templateCheckDueDateStyle);
                        checkDueDateCell.setCellValue(checkYear + "/" + (month+1) + "/" + day);

                        XSSFCell offsetAmountCell = XSSFRow.createCell(4);
                        offsetAmountCell.setCellStyle(templateOffsetAmountStyle);
                        offsetAmountCell.setCellValue(dayOfAmountMap.get(day));

                        XSSFCell remarkCell = XSSFRow.createCell(6);
                        remarkCell.setCellStyle(templateRemarkStyle);
                        remarkCell.setCellValue("");
                    }
                }
            }
        }
        calculateFinalAmount(calculateTableCount);
    }
    private void getNonePayReceiveOffsetAmount(HashMap<Integer,HashMap<Integer,Integer>[]> calculateOffsetAmountArray,ObservableList<SearchNonePayReceive_Bean> SearchNonePayReceiveList){
        for(SearchNonePayReceive_Bean searchNonePayReceive_Bean : SearchNonePayReceiveList){
            SearchNonePayReceive_Model.getManufacturerInfo(searchNonePayReceive_Bean);
            int offsetAmount;
            if(searchNonePayReceive_Bean.isCheckBoxSelect() && searchNonePayReceive_Bean.getDefaultPaymentMethod() == ObjectInfo_Enum.DefaultPaymentMethod.支票){
                String checkDueDate = ManagePayableReceivable_Model.generateCheckDueDate(searchNonePayReceive_Bean.getCheckDueDay() == 0 ? 30:searchNonePayReceive_Bean.getCheckDueDay());
                if(checkDueDate != null){
                    LocalDate checkDate = LocalDate.parse(checkDueDate);
                    offsetAmount = searchNonePayReceive_Bean.isDiscountPostage() ? searchNonePayReceive_Bean.getActualPayReceivePrice() - searchNonePayReceive_Bean.getPostage() : searchNonePayReceive_Bean.getActualPayReceivePrice();

                    int checkYear = checkDate.getYear();
                    if(!calculateOffsetAmountArray.containsKey(checkYear)){
                        calculateOffsetAmountArray.put(checkYear,new HashMap[12]);
                    }
                    HashMap<Integer,Integer> dayOfAmountMap = calculateOffsetAmountArray.get(checkYear)[checkDate.getMonthValue()-1];
                    if(dayOfAmountMap == null)  dayOfAmountMap = new HashMap<>();
                    if(!dayOfAmountMap.containsKey(checkDate.getDayOfMonth()))
                        dayOfAmountMap.put(checkDate.getDayOfMonth(),offsetAmount);
                    else
                        dayOfAmountMap.put(checkDate.getDayOfMonth(),dayOfAmountMap.get(checkDate.getDayOfMonth())+offsetAmount);

                    calculateOffsetAmountArray.get(checkYear)[checkDate.getMonthValue()-1] = dayOfAmountMap;
                }
            }
        }
    }
    private void getAlreadyPayReceiveCheckPrice(HashMap<Integer,HashMap<Integer,Integer>[]> calculateOffsetAmountArray,HashMap<LocalDate,Integer> checkDueDateAndCheckPriceMap){
        for(LocalDate checkDate : checkDueDateAndCheckPriceMap.keySet()){
            int checkPrice = checkDueDateAndCheckPriceMap.get(checkDate);
            int checkYear = checkDate.getYear();
            if(!calculateOffsetAmountArray.containsKey(checkYear)){
                calculateOffsetAmountArray.put(checkYear,new HashMap[12]);
            }
            HashMap<Integer,Integer> dayOfAmountMap = calculateOffsetAmountArray.get(checkYear)[checkDate.getMonthValue()-1];
            if(dayOfAmountMap == null)  dayOfAmountMap = new HashMap<>();
            if(!dayOfAmountMap.containsKey(checkDate.getDayOfMonth()))
                dayOfAmountMap.put(checkDate.getDayOfMonth(),checkPrice);
            else
                dayOfAmountMap.put(checkDate.getDayOfMonth(),dayOfAmountMap.get(checkDate.getDayOfMonth())+checkPrice);

            calculateOffsetAmountArray.get(checkYear)[checkDate.getMonthValue()-1] = dayOfAmountMap;
        }
    }
    private void calculateFinalAmount(int calculateTableCount){
        int rowIndex = sheet.getLastRowNum()+1;
        XSSFRow row = sheet.createRow(rowIndex);
        copyRowStyle(row,24);
        XSSFRow templateRow = template.getRow(24);

        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,0,1));
        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,2,3));
        sheet.addMergedRegion(getMergeCellRange(rowIndex,rowIndex,5,8));
        XSSFCell title = row.getCell(2);
        title.setCellValue(templateRow.getCell(2).getStringCellValue());
        CellStyle style = templateRow.getCell(4).getCellStyle();
        XSSFCell totalAmount = row.getCell(4);
        style.setDataFormat(getMoneyFormat("$0"));
        totalAmount.setCellStyle(style);
        totalAmount.setCellFormula("SUM(E" + (rowIndex-calculateTableCount) + ":E"+rowIndex+")");
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
        workbook.removeSheetAt(0);
        return super.build(filePath);
    }
}
