package ERP.Model.Order;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.ERPApplication;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ToolKit;
import ERP.Enum.ToolKit.ToolKit_Enum.ConvertMathToChinese;
import ERP.ToolKit.TemplatePath;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class OpenSpecificationWordDocument_Model {
    private ToolKit ToolKit;
    private CallConfig CallConfig;
    public OpenSpecificationWordDocument_Model(){
        this.ToolKit = ERPApplication.ToolKit;
        this.CallConfig = ToolKit.CallConfig;
    }
    public boolean generateSpecificationDocument(String projectName, String basicNeedTemplate, ObservableList<OrderProduct_Bean> productList, String warrantyAndOtherTemplate, String educationTraining){
        boolean status = false;
        try{
            FileInputStream fileInputStream = new FileInputStream(new File(TemplatePath.SpecificationDocument).getAbsolutePath());
            XWPFDocument document = new XWPFDocument(fileInputStream);
            setProjectName(document, projectName);

            String content = getBasicNeedContent(basicNeedTemplate);
            content = content + getProductContent(productList);
            content = content + getWarrantyAndOtherContent(warrantyAndOtherTemplate);

            String[] contentArray = content.split("\n");
            createXWPFTable(document,contentArray);
            writeItemNumber(document.getTables().get(0), contentArray);
            writeQuantityAndUnit(document.getTables().get(0), contentArray, productList);
            writeContent(document, document.getTables().get(0), contentArray);

            FileOutputStream fos = new FileOutputStream(CallConfig.getFile_OutputPath() + "/採購規格書-" + projectName + ".docx");
            document.write(fos);
            fos.close();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return status;
    }
    private void setProjectName(XWPFDocument document, String projectName){
        XWPFRun XWPFRun = document.getParagraphs().get(1).getRuns().get(0);
        if(XWPFRun.getText(XWPFRun.getTextPosition()).contains("財物名稱"))
            XWPFRun.setText(projectName);
    }
    private void writeItemNumber(XWPFTable xwpfTable, String[] contentArray){
        int itemNumber = 1;
        int row = 0;
        for (String line : contentArray) {
            List<XWPFTableRow> rowList = xwpfTable.getRows();
            if (!line.equals("") && line.substring(0, 1).equals("※")) {
                row++;
                XWPFParagraph XWPFParagraph = rowList.get(row).getTableCells().get(0).getParagraphs().get(0);
                createXWPFRun(XWPFParagraph, 12,false,false).setText(ConvertMathToChinese.values()[itemNumber].getChineseMath());
                itemNumber++;
            } else if (line.contains("、") && ToolKit.isChineseCount(line.substring(0, 1))) {
                row++;
            }
        }
    }
    private void writeQuantityAndUnit(XWPFTable xwpfTable, String[] contentArray, ObservableList<OrderProduct_Bean> productList){
        List<XWPFTableRow> rowList = xwpfTable.getRows();
        int row = 1;
        for(String line : contentArray){
            if(line.contains("※") || (line.contains("、") && ToolKit.isChineseCount(line.substring(0, 1)))){
                row++;
                if(line.contains("本套設備包含"))
                    break;
            }
        }
        for(OrderProduct_Bean OrderProduct_Bean : productList) {
            XWPFParagraph quantityParagraph = rowList.get(row).getTableCells().get(2).getParagraphs().get(0);
            createXWPFRun(quantityParagraph, 12, false, false).setText(String.valueOf(OrderProduct_Bean.getQuantity()));
            XWPFParagraph unitParagraph = rowList.get(row).getTableCells().get(3).getParagraphs().get(0);
            createXWPFRun(unitParagraph, 12, false, false).setText(OrderProduct_Bean.getUnit());
            row++;
        }
    }

    private void writeContent(XWPFDocument document, XWPFTable xwpfTable, String[] contentArray){
        BigInteger numberID = generateNumID(document);

        boolean isWarrantyAndOtherContent = false;
        List<XWPFTableRow> rowList = xwpfTable.getRows();
        for(int index = 1 ; index < rowList.size() ; index++){
            ArrayList<String> returnContent = getContentOfArray(contentArray);
            XWPFTableCell tableCell = rowList.get(index).getTableCells().get(1);
            for(int j = 0; j < returnContent.size(); j++){
                if(j != 0)
                    tableCell.addParagraph();
                String line = returnContent.get(j);
                XWPFParagraph XWPFParagraph = tableCell.getParagraphs().get(j);

                if(!line.equals("") && line.substring(0, 1).equals("※")) {
                    numberID = refreshFirstLevelNumID(document);

                    createXWPFRun(XWPFParagraph, 12, true, true).setText(line);
                    if(line.contains("保固維護與其他"))
                        isWarrantyAndOtherContent = true;
                }else{
                    if(line.contains("、") && ToolKit.isChineseCount(line.substring(0,1))){
                        setTitleNumber(XWPFParagraph, numberID,0);
                        line = line.substring(line.indexOf("、")+1);
                    }else if(line.contains(".") && ToolKit.isDigital(line.substring(0,line.indexOf(".")))){
                        setTitleNumber(XWPFParagraph, numberID,1);
                        line = line.substring(line.indexOf(".")+1);
                    }else if(line.contains(".") && ToolKit.isUpperCaseEnglish(line.substring(0,line.indexOf(".")))){
                        setTitleNumber(XWPFParagraph, numberID,2);
                        line = line.substring(line.indexOf(".")+1);
                    }else if(line.contains(".") && ToolKit.isLowerCaseEnglish(line.substring(0,line.indexOf(".")))){
                        setTitleNumber(XWPFParagraph, numberID,3);
                        line = line.substring(line.indexOf(".")+1);
                    }else if(line.contains("●") && line.substring(0,1).equals("●")){
                        setTitleNumber(XWPFParagraph, numberID,4);
                        line = line.substring(1);
                    }else{
                        XWPFParagraph.setIndentationLeft(isWarrantyAndOtherContent ? 720 : 960);
                    }
                    createXWPFRun(XWPFParagraph, 12, false, false).setText(line);
                }
            }
        }
    }
    private void setTitleNumber(XWPFParagraph XWPFParagraph, BigInteger numberID, int level){
        XWPFParagraph.setFirstLineIndent(-480);
        XWPFParagraph.setIndentationLeft(480*(level+1));

        XWPFParagraph.setNumID(numberID);
        CTNumPr cTNumPr = XWPFParagraph.getCTP().getPPr().getNumPr();
        cTNumPr.addNewIlvl().setVal(BigInteger.valueOf(level));
        if(level != 0 && level != 4)
            cTNumPr.addNewNumId().setVal(BigInteger.valueOf(level));
    }
    private ArrayList<String> getContentOfArray(String[] contentArray){
        ArrayList<String> returnContent = new ArrayList<>();
        boolean startRecord = false;
        for(int j = 0; j < contentArray.length; j++){
            String line = contentArray[j];
            if(line != null){
                if(startRecord && ((!line.equals("") && line.substring(0, 1).equals("※")) ||
                        (line.contains("、") && ToolKit.isChineseCount(line.substring(0, 1))))){
                    break;
                }
                startRecord = true;
                returnContent.add(line);
                contentArray[j] = null;
            }
        }
        return returnContent;
    }
    private void createXWPFTable(XWPFDocument document, String[] contentArray){
        List<Integer> tableRowSizeAndParagraphInEachCell = calculateTableRow(contentArray);
        XWPFTable xwpfTable = document.createTable(tableRowSizeAndParagraphInEachCell.size(),4);
        xwpfTable.setTableAlignment(TableRowAlign.CENTER);
        CTTblPr tablePr = xwpfTable.getCTTbl().addNewTblPr();
        CTTblWidth width = tablePr.addNewTblW();
        width.setW(BigInteger.valueOf(9800));

        List<XWPFTableRow> rowList = xwpfTable.getRows();
        for (int index = 0; index < rowList.size(); index++) {
            XWPFTableRow tableRow = rowList.get(index);
            List<XWPFTableCell> cellList = tableRow.getTableCells();
            for(int j = 0 ; j < cellList.size() ; j++){
                XWPFTableCell tableCell = cellList.get(j);
                XWPFParagraph XWPFParagraph = tableCell.getParagraphs().get(0);
                if(index == 0) {
                    XWPFParagraph.setAlignment(ParagraphAlignment.CENTER);
                    if(j == 0)  createXWPFRun(XWPFParagraph,12,false,true).setText("項次");
                    if(j == 1)  createXWPFRun(XWPFParagraph,12,false,true).setText("品名及規格");
                    if(j == 2)  createXWPFRun(XWPFParagraph,12,false,true).setText("數量");
                    if(j == 3)  createXWPFRun(XWPFParagraph,12,false,true).setText("單位");
                }else{
                    if(j != 1) {
                        tableCell.setWidth("800");
                        XWPFParagraph.setAlignment(ParagraphAlignment.CENTER);
                    }else {
                        tableCell.setWidth("8400");
                        XWPFParagraph.setAlignment(ParagraphAlignment.LEFT);
                    }
                }
                if(index != 0){  //  移除下框線  除了最後一行
                    tableCell.getCTTc().addNewTcPr();
                    CTTcBorders ctTcBorders = tableCell.getCTTc().getTcPr().addNewTcBorders();
                    ctTcBorders.addNewTop().setVal(STBorder.NIL);
                    if(index != rowList.size()-1)
                        ctTcBorders.addNewBottom().setVal(STBorder.NIL);
                }
            }
        }
    }
    private List<Integer> calculateTableRow(String[] contentArray){
        List<Integer> tableRowSizeAndParagraphInEachCell = new ArrayList<Integer>(){{add(0);}};
        int calculateLineSize = 0;
        boolean startCalculate = false;
        for (String line : contentArray) {
            if (!line.equals("") && line.substring(0, 1).equals("※")) {
                if(calculateLineSize != 0)
                    tableRowSizeAndParagraphInEachCell.add(calculateLineSize-1);
                else
                    tableRowSizeAndParagraphInEachCell.add(0);
                calculateLineSize = 0;
            }else if (!line.equals("") && (line.contains("、") && ToolKit.isChineseCount(line.substring(0, 1)))) {
                if(startCalculate){
                    tableRowSizeAndParagraphInEachCell.add(calculateLineSize-1);
                    calculateLineSize = 0;
                }
                startCalculate = true;
            }
            if(startCalculate)
                calculateLineSize = calculateLineSize + 1;
        }
        if(calculateLineSize != 0){
            tableRowSizeAndParagraphInEachCell.add(calculateLineSize-1);
        }
        return tableRowSizeAndParagraphInEachCell;
    }
    private XWPFRun createXWPFRun(XWPFParagraph XWPFParagraph, int fontSize, boolean underline, boolean bold){
        XWPFRun XWPFRun = XWPFParagraph.createRun();
        XWPFRun.setFontFamily("標楷體");
        XWPFRun.setFontFamily("Times New Romam");
        XWPFRun.setFontSize(12);
        if(underline)
            XWPFRun.setUnderline(UnderlinePatterns.SINGLE);
        XWPFRun.setBold(bold);
        return XWPFRun;
    }
    private String getProductContent(ObservableList<OrderProduct_Bean> productList){
        StringBuilder content = new StringBuilder("※本套設備包含：\n");
        for(int index = 0 ; index < productList.size() ; index++){
            OrderProduct_Bean OrderProduct_Bean = productList.get(index);
            String specificationContent = OrderProduct_Bean.getSpecificationContent();
            String specificationProductName;
            if(specificationContent != null && !specificationContent.equals("")) {
                specificationProductName = ConvertMathToChinese.values()[index+1].name() + "、" + OrderProduct_Bean.getSpecificationProductName() + "：";
                content.append(specificationProductName).append("\n").append(specificationContent).append("\n");
            }else {
                specificationProductName = ConvertMathToChinese.values()[index+1].name() + "、" + OrderProduct_Bean.getSpecificationProductName();
                content.append(specificationProductName).append("\n");
            }
        }
        return content.toString();
    }
    private String getBasicNeedContent(String basicNeedTemplate){
        if(basicNeedTemplate != null)
            return "※基本需求：\n" + basicNeedTemplate;
        return "";
    }
    private String getWarrantyAndOtherContent(String warrantyAndOtherTemplate){
        if(warrantyAndOtherTemplate != null)
            return "※保固維護與其他：\n" + warrantyAndOtherTemplate;
        return "";
    }
    private String getEducationTrainingContent(String educationTrainingTemplate){
        if(educationTrainingTemplate != null)
            return "※教育訓練：\n" + educationTrainingTemplate;
        return "";
    }
    private BigInteger generateNumID(XWPFDocument document){
        CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();      //create a numbering scheme
        cTAbstractNum.setAbstractNumId(BigInteger.valueOf(0));                  //give the scheme an ID

        /*first level*/
        CTLvl cTLvl0 = cTAbstractNum.addNewLvl();               //create the first numbering level
        cTLvl0.setIlvl(BigInteger.ZERO);                        //mark it as the top outline level
        cTLvl0.addNewNumFmt().setVal(STNumberFormat.CHINESE_COUNTING);   //set the number format
        cTLvl0.addNewLvlText().setVal("%1､");                   //set the adornment; %1 is the first-level number or letter as set by number format
        cTLvl0.addNewStart().setVal(BigInteger.ONE);            //set the starting number (here, index from 1)

        /*second level*/
        CTLvl cTLvl1 = cTAbstractNum.addNewLvl();               //create another numbering level
        cTLvl1.setIlvl(BigInteger.ONE);                         //specify that it's the first indent
        cTLvl1.addNewNumFmt().setVal(STNumberFormat.DECIMAL);   //the rest is fairly similar
        cTLvl1.addNewLvlText().setVal("%2.");                //setup to get 1.1, 1.2, ect.
        cTLvl1.addNewStart().setVal(BigInteger.ONE);

        /*third level*/
        CTLvl cTLvl2 = cTAbstractNum.addNewLvl();               //create another numbering level
        cTLvl2.setIlvl(BigInteger.valueOf(2));                         //specify that it's the first indent
        cTLvl2.addNewNumFmt().setVal(STNumberFormat.UPPER_LETTER);   //the rest is fairly similar
        cTLvl2.addNewLvlText().setVal("%3.");                //setup to get 1.1, 1.2, ect.
        cTLvl2.addNewStart().setVal(BigInteger.ONE);

        /*fourth level*/
        CTLvl cTLvl3 = cTAbstractNum.addNewLvl();               //create another numbering level
        cTLvl3.setIlvl(BigInteger.valueOf(3));                         //specify that it's the first indent
        cTLvl3.addNewNumFmt().setVal(STNumberFormat.LOWER_LETTER);   //the rest is fairly similar
        cTLvl3.addNewLvlText().setVal("%4.");                //setup to get 1.1, 1.2, ect.
        cTLvl3.addNewStart().setVal(BigInteger.ONE);

        /*fifth level*/
        CTLvl cTLvl4 = cTAbstractNum.addNewLvl();
        cTLvl4.setIlvl(BigInteger.valueOf(4));
        cTLvl4.addNewNumFmt().setVal(STNumberFormat.BULLET);
        cTLvl4.addNewLvlText().setVal("●");
        cTLvl4.addNewStart().setVal(BigInteger.ONE);

        /*associate the numbering scheme with the document's numbering*/
        XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
        XWPFNumbering numbering = document.createNumbering();
        BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
        /*return an ID for the numbering*/
        return numbering.addNum(abstractNumID);
    }
    private BigInteger refreshFirstLevelNumID(XWPFDocument document){
        XWPFNumbering numbering = document.getNumbering();
        XWPFAbstractNum abstractNum = numbering.getAbstractNum(BigInteger.ZERO);
        BigInteger numId = numbering.addNum(abstractNum.getAbstractNum().getAbstractNumId());
        XWPFNum num = numbering.getNum(numId);
        CTNumLvl lvloverride = num.getCTNum().addNewLvlOverride();
        lvloverride.setIlvl(BigInteger.ZERO);
        CTDecimalNumber number = lvloverride.addNewStartOverride();
        number.setVal(BigInteger.ONE);
        return num.getCTNum().getNumId();
    }
}
