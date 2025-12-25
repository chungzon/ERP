package ERP.Model.Order.ExportReportGenerator;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// 採購文件
public class ExportProcurementDocument_Model {
    private ExportProcurementDocument_Model(){}
    private XWPFDocument template;
    private XWPFNumbering numbering;
    private String productName;
    private final List<String> productList = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy.MM.dd");
    public static ExportProcurementDocument_Model createTemplate(String templatePath) throws IOException {
        ExportProcurementDocument_Model document = new ExportProcurementDocument_Model();
        document.template = new XWPFDocument(new FileInputStream(templatePath));
        return document;
    }
    public ExportProcurementDocument_Model setProductName(String name){
        this.productName = name;
        return this;
    }
    public ExportProcurementDocument_Model setProductList(Collection<String> collection ){
        this.productList.addAll(collection);
        return this;
    }

    /**
     * 啟聯採購單
     * @return template builder
     */
    public ExportProcurementDocument_Model buildChiLen() {
        numbering = template.getNumbering();
        replace(template.getTables().get(0).getRow(1).getCell(0).getParagraphs().get(0),
                template.getTables().get(0).getRow(1).getCell(0).getParagraphs().get(0).getRuns().size(),
                1,
                this.productName);
        template.getTables().get(0).getRow(3).getCell(0).removeParagraph(0);
        template.getTables().get(0).getRow(3).getCell(0).removeParagraph(0);
        template.getTables().get(0).getRow(3).getCell(0).removeParagraph(0);
        template.getTables().get(0).getRow(3).getCell(0).removeParagraph(0);
        template.getTables().get(0).getRow(3).getCell(0).removeParagraph(0);
        for(String s : productList){
            replace(template.getTables().get(0).getRow(3).getCell(0).addParagraph(),s);
        }
        return this;
    }

    /**
     * 協新採購單
     * @return template builder
     */
    public ExportProcurementDocument_Model buildSheiSin(){
        template.getParagraphs().get(0).removeRun(1);
        template.getParagraphs().get(0).removeRun(1);
        template.getParagraphs().get(0).removeRun(1);
        template.getParagraphs().get(0).removeRun(1);
        XWPFRun run = template.getParagraphs().get(0).createRun();
        run.setBold(true);
        run.setFontSize(14);
        run.setFontFamily("標楷體");
        run.setText(productName);

        int position = template.getPosOfTable(template.getTables().get(1)); //  移除舊表格
        template.removeBodyElement(position);
        position = template.getPosOfParagraph(template.getParagraphs().get(2)); //  移除舊段落
        template.removeBodyElement(position);

        createSheiSinXWPFTable(template);
        BigInteger numberID = generateSheiSinNumID(template);

        XWPFTable xwpfTable = template.getTables().get(1);
        List<XWPFTableRow> rowList = xwpfTable.getRows();
        int row = 1;
        for (String itemName : productList) {
            XWPFParagraph XWPFParagraph = rowList.get(row).getTableCells().get(0).getParagraphs().get(0);
            XWPFParagraph.setNumID(numberID);
            createXWPFRun(XWPFParagraph, 13, false, false).setText(itemName);
            row++;
        }
        return this;
    }
    private void createSheiSinXWPFTable(XWPFDocument document){
        XWPFTable xwpfTable = document.createTable(productList.size()+1,1);
        xwpfTable.setTableAlignment(TableRowAlign.LEFT);
        CTTblPr tablePr = xwpfTable.getCTTbl().addNewTblPr();
        CTTblWidth width = tablePr.addNewTblW();
        width.setW(BigInteger.valueOf(10100));

        List<XWPFTableRow> rowList = xwpfTable.getRows();
        for (int index = 0; index < rowList.size(); index++) {
            XWPFTableRow tableRow = rowList.get(index);
            XWPFTableCell tableCell = tableRow.getTableCells().get(0);
            tableCell.getTableRow().setHeight(600);
            XWPFParagraph XWPFParagraph = tableCell.getParagraphs().get(0);
            XWPFParagraph.setAlignment(ParagraphAlignment.LEFT);
            setCellLocation(tableCell, STJc.LEFT, XWPFTableCell.XWPFVertAlign.CENTER);
            if(index == 0) {
                XWPFParagraph.setFirstLineIndent(240);
                createXWPFRun(XWPFParagraph,13,false,false).setText("規格");
            }
        }
    }
    private BigInteger generateSheiSinNumID(XWPFDocument document){
        CTAbstractNum cTAbstractNum = CTAbstractNum.Factory.newInstance();      //create a numbering scheme
        cTAbstractNum.setAbstractNumId(BigInteger.valueOf(0));                  //give the scheme an ID

        /*first level*/
        CTLvl cTLvl0 = cTAbstractNum.addNewLvl();               //create the first numbering level
        cTLvl0.setIlvl(BigInteger.ZERO);                        //mark it as the top outline level
        cTLvl0.addNewNumFmt().setVal(STNumberFormat.DECIMAL);   //set the number format
        cTLvl0.addNewLvlText().setVal("%1.");                   //set the adornment; %1 is the first-level number or letter as set by number format
        cTLvl0.addNewStart().setVal(BigInteger.ONE);            //set the starting number (here, index from 1)

        /*associate the numbering scheme with the document's numbering*/
        XWPFAbstractNum abstractNum = new XWPFAbstractNum(cTAbstractNum);
        XWPFNumbering numbering = document.createNumbering();
        BigInteger abstractNumID = numbering.addAbstractNum(abstractNum);
        /*return an ID for the numbering*/
        return numbering.addNum(abstractNumID);
    }
    /**
     * 福大鑫採購單
     * @return template builder
     */
    public ExportProcurementDocument_Model buildFuDaXing() {
        XWPFTable table = template.getTables().get(0);
        for(int i=0;i<productList.size();i++){
            table.getRow(i+1).getTableCells().get(1).setText(productList.get(i));
        }
        return this;
    }

    /**
     * 皇佳採購單
     * @return
     */
    public ExportProcurementDocument_Model buildHuangGia(){
        template.getParagraphs().get(1).removeRun(2);
        XWPFRun run = template.getParagraphs().get(1).createRun();
        run.setFontSize(16);
        run.setFontFamily("標楷體");
        run.setText(" : "+productName);

        BigInteger numberID = template.getTables().get(0).getRows().get(1).getCell(0).getParagraphs().get(0).getCTP().getPPr().getNumPr().getNumId().getVal();

        int position = template.getPosOfTable(template.getTables().get(0)); //  移除舊表格
        template.removeBodyElement(position);
        position = template.getPosOfParagraph(template.getParagraphs().get(2)); //  移除舊段落
        template.removeBodyElement(position);

        createHuangGiaXWPFTable(template);

        XWPFTable xwpfTable = template.getTables().get(0);
        List<XWPFTableRow> rowList = xwpfTable.getRows();
        int row = 1;
        for (String itemName : productList) {
            XWPFParagraph XWPFParagraph = rowList.get(row).getTableCells().get(0).getParagraphs().get(0);
            XWPFParagraph.setNumID(numberID);

            XWPFParagraph = rowList.get(row).getTableCells().get(1).getParagraphs().get(0);
            createXWPFRun(XWPFParagraph, 14, false, false).setText(itemName);
            row++;
        }
        return this;
    }
    private void createHuangGiaXWPFTable(XWPFDocument document){
        XWPFTable xwpfTable = document.createTable(productList.size()+1,2);
        xwpfTable.setTableAlignment(TableRowAlign.LEFT);
        CTTblPr tablePr = xwpfTable.getCTTbl().addNewTblPr();
        CTTblWidth width = tablePr.addNewTblW();
        width.setW(BigInteger.valueOf(9500));
        setTableBorder(xwpfTable);

        List<XWPFTableRow> rowList = xwpfTable.getRows();
        for (int index = 0; index < rowList.size(); index++) {
            XWPFTableRow tableRow = rowList.get(index);
            List<XWPFTableCell> cellList = tableRow.getTableCells();
            for(int j = 0 ; j < cellList.size() ; j++){
                XWPFTableCell tableCell = cellList.get(j);
                tableCell.getTableRow().setHeight(600);
                XWPFParagraph XWPFParagraph = tableCell.getParagraphs().get(0);
                if(index == 0) {
                    if(j == 0){
                        XWPFParagraph.setAlignment(ParagraphAlignment.CENTER);
                        createXWPFRun(XWPFParagraph,16,false,false).setText("項次");
                    }else if(j == 1){
                        XWPFParagraph.setAlignment(ParagraphAlignment.LEFT);
                        createXWPFRun(XWPFParagraph,16,false,false).setText("規格/說明");
                    }
                }else{
                    if(j == 0){
                        tableCell.setWidth("800");
                        XWPFParagraph.setAlignment(ParagraphAlignment.RIGHT);
                        setCellLocation(tableCell, STJc.RIGHT, XWPFTableCell.XWPFVertAlign.CENTER);
                    }else if(j == 1){
                        tableCell.setWidth("8000");
                        XWPFParagraph.setAlignment(ParagraphAlignment.LEFT);
                        setCellLocation(tableCell, STJc.LEFT, XWPFTableCell.XWPFVertAlign.CENTER);
                    }
                }
            }
        }
    }
    private void setTableBorder(XWPFTable xwpfTable){
        CTTblBorders borders=xwpfTable.getCTTbl().getTblPr().addNewTblBorders();
        CTBorder lBorder=borders.addNewTop();
        lBorder.setVal(STBorder.Enum.forString("double"));
        lBorder.setSz(new BigInteger("1"));

        lBorder=borders.addNewLeft();
        lBorder.setVal(STBorder.Enum.forString("double"));
        lBorder.setSz(new BigInteger("1"));

        lBorder=borders.addNewBottom();
        lBorder.setVal(STBorder.Enum.forString("double"));
        lBorder.setSz(new BigInteger("1"));

        lBorder=borders.addNewRight();
        lBorder.setVal(STBorder.Enum.forString("double"));
        lBorder.setSz(new BigInteger("1"));
    }
    private XWPFRun createXWPFRun(XWPFParagraph XWPFParagraph, int fontSize, boolean underline, boolean bold){
        XWPFRun XWPFRun = XWPFParagraph.createRun();
        XWPFRun.setFontFamily("標楷體");
        XWPFRun.setFontFamily("Times New Romam");
        XWPFRun.setFontSize(fontSize);
        if(underline)
            XWPFRun.setUnderline(UnderlinePatterns.SINGLE);
        XWPFRun.setBold(bold);
        return XWPFRun;
    }
    private void setCellLocation(XWPFTableCell tableCell, STJc.Enum STJc, XWPFTableCell.XWPFVertAlign XWPFVertAlign){
        CTTc cttc = tableCell.getCTTc();
        CTP ctp = cttc.getPList().get(0);
        CTPPr ctppr = ctp.getPPr();
        if (ctppr == null) {
            ctppr = ctp.addNewPPr();
        }
        CTJc ctjc = ctppr.getJc();
        if (ctjc == null) {
            ctjc = ctppr.addNewJc();
        }
        ctjc.setVal(STJc);
        tableCell.setVerticalAlignment(XWPFVertAlign);
    }
    /**
     * 展源浩採購單
     * @return template builder
     */
    public ExportProcurementDocument_Model buildZhanyh() {
        XWPFRun run = template.getTables().get(0).getRow(0).getCell(0).getParagraphs().get(1).createRun();
        int font_Size = template.getTables().get(0).getRow(0).getCell(0).getParagraphs().get(1).getRuns().get(0).getFontSize();
        setRunFont(run,productName,font_Size,"標楷體","Times New Roman");
        template.getTables().get(0).removeRow(2);
        template.getTables().get(0).removeRow(2);
        for(String s : productList){
            addNewRow("",s);
        }
        return this;
    }
    private void addNewRow(String title,String value){
        XWPFTableRow row = template.getTables().get(0).createRow();
        XWPFParagraph paragraph = row.getCell(0).getParagraphs().get(0);
        paragraph.setAlignment(ParagraphAlignment.CENTER);
        paragraph.setNumID(BigInteger.valueOf(5));
        setRunFont(paragraph.createRun(),title,12,"標楷體","Times New Roman");
        XWPFParagraph paragraph2 = row.createCell().getParagraphs().get(0);
        paragraph2.setAlignment(ParagraphAlignment.LEFT);
        setRunFont(paragraph2.createRun(),value,12,"標楷體","Times New Roman");

    }
    public File export(String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream out = new FileOutputStream(file);
        template.write(out);
        template.close();
        out.close();
        return file;
    }
    private File buildTemp() throws IOException {
        File output = new File("temp_設備產品規格表.docx");
        FileOutputStream out = new FileOutputStream(output);
        template.write(out);
        template.close();
        out.close();
        output.deleteOnExit();
        return output;
    }
    private void addRow(XWPFTableRow row){
        template.getTables().get(1).addRow(row,1);
    }
    private void addRowValue(int index,String text){
        XWPFTableRow row = template.getTables().get(1).getRow(index);
        XWPFParagraph value;
        value = row.getCell(0).getParagraphs().get(0);
        value.createRun().setText(" ");
        value.setNumID(BigInteger.valueOf(5));

        change(value,text,14,"標楷體","Times New Roman");
    }
    private void replace(XWPFParagraph paragraph,String replaceWith){
        paragraph.setNumID(numbering.addNum(BigInteger.valueOf(0)));
        change(paragraph,replaceWith,13,"新細明體","Lucida Sans Unicode");
    }
    private void replace(XWPFParagraph paragraph, int size, int startPosition, String replaceWith){
        for(int i=startPosition;i<size;i++){
            paragraph.removeRun(startPosition);
        }
        change(paragraph,replaceWith,14,"標楷體","標楷體");
    }
    private void change(
            XWPFParagraph paragraph,
            String insert_Data,
            int font_Size,
            String chineseFontFamily,
            String fontFamily
    ){
        if(insert_Data == null || insert_Data.isEmpty()){
            return;
        }
        for(char character : insert_Data.toCharArray()){
            setRunFont(paragraph.createRun(),character,font_Size,chineseFontFamily,fontFamily);
        }
    }
    private void setRunFont(
            XWPFRun run,
            String text,
            int fontSize,
            String chineseFontFamily,
            String fontFamily
    ){
        if(text == null || text.isEmpty()){
            return;
        }
        for(char c : text.toCharArray()){
            setRunFont(run,c,fontSize,chineseFontFamily,fontFamily);
        }
    }
    private void setRunFont(
            XWPFRun run,
            char character,
            int fontSize,
            String chineseFontFamily,
            String fontFamily
    ){
        if((int)character<=126 && (int)character>=32){
            run.setFontFamily(fontFamily);
        }else{
            run.setFontFamily(chineseFontFamily);
        }
        run.setFontSize(fontSize);
        run.setText(String.valueOf(character));
    }
}
