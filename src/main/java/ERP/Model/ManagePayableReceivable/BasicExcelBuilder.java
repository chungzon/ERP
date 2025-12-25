package ERP.Model.ManagePayableReceivable;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BasicExcelBuilder {
    enum CopyType{
        All,
        Style,
        Value
    }
    public XSSFSheet sheet;
    public FileInputStream fileInputStream;
    public XSSFWorkbook workbook;
    public XSSFSheet template;

    /***
     * copy excel column width
     * @param count how many column width will be copy
     */
    protected void copyColumn(int count){
        for(int i=0;i<count;i++){
            sheet.setColumnWidth(i,template.getColumnWidth(i));
        }
    }
    protected void setXSSFSheetCell(XSSFSheet XSSFSheet, Integer row, Integer cell, String Value){
        if(row == null || cell == null)
            return;
        XSSFRow xssfRow = XSSFSheet.getRow(row);
        if(xssfRow == null) xssfRow = sheet.createRow(row);
        XSSFCell xssfCell = xssfRow.getCell(cell);
        if(xssfCell == null)    xssfCell = xssfRow.createCell(cell);
        xssfCell.setCellValue(Value);
    }
    protected void setXSSFSheetCell(XSSFSheet XSSFSheet, Integer row, Integer cell, int Value){
        if(row == null || cell == null)
            return;
        XSSFRow xssfRow = XSSFSheet.getRow(row);
        if(xssfRow == null) xssfRow = sheet.createRow(row);
        XSSFCell xssfCell = xssfRow.getCell(cell);
        if(xssfCell == null)    xssfCell = xssfRow.createCell(cell);
        xssfCell.setCellValue(Value);
    }
    protected void setXSSFSheetCell(XSSFSheet XSSFSheet, Integer row, Integer cell, double Value){
        if(row == null || cell == null)
            return;
        XSSFRow xssfRow = XSSFSheet.getRow(row);
        if(xssfRow == null) xssfRow = sheet.createRow(row);
        XSSFCell xssfCell = xssfRow.getCell(cell);
        if(xssfCell == null)    xssfCell = xssfRow.createCell(cell);
        xssfCell.setCellValue(Value);
    }
    protected void setXSSFSheetCellFormula(XSSFSheet XSSFSheet, Integer row, Integer cell, String formula){
        if(row == null || cell == null)
            return;
        XSSFRow xssfRow = XSSFSheet.getRow(row);
        if(xssfRow == null) xssfRow = sheet.createRow(row);
        XSSFCell xssfCell = xssfRow.getCell(cell);
        if(xssfCell == null)    xssfCell = xssfRow.createCell(cell);
        xssfCell.setCellFormula(formula);
    }
    protected void copyRowStyle(XSSFRow row, int templateRowIndex){
        copyRow(row,templateRowIndex, CopyType.Style);
    }
    protected void copyRowValue(XSSFRow row, int templateRowIndex){
        copyRow(row,templateRowIndex, CopyType.Value);
    }
    protected void copyRow(XSSFRow row, int templateRowIndex){
        copyRow(row,templateRowIndex, CopyType.All);
    }
    protected void copyRow(XSSFRow row, int templateRowIndex, CopyType type){
        XSSFRow templateRow = template.getRow(templateRowIndex);
        for(int index=0;index<templateRow.getPhysicalNumberOfCells();index++){
            XSSFCell cell = row.createCell(index);
            XSSFCell templateCell = templateRow.getCell(index);
            if(templateCell != null){
                switch (type){
                    case All:
                        cell.setCellStyle(templateCell.getCellStyle());
                        cell.setCellValue(templateCell.getStringCellValue());
                    case Style:
                        cell.setCellStyle(templateCell.getCellStyle());
                        break;
                    case Value:
                        cell.setCellValue(templateCell.getStringCellValue());
                        break;
                }
            }
        }
    }
    protected void copyPrintSetup(XSSFPrintSetup source,XSSFPrintSetup destination){
        destination.setOrientation(source.getOrientation());
        destination.setFooterMargin(source.getFooterMargin());
        destination.setHeaderMargin(source.getHeaderMargin());
        destination.setBottomMargin(source.getBottomMargin());
        destination.setLeftMargin(source.getLeftMargin());
        destination.setRightMargin(source.getRightMargin());
        destination.setTopMargin(source.getTopMargin());
        destination.setScale(source.getScale());
        destination.setPaperSize(source.getPaperSize());
    }
    protected short getExcelHeight(double excelPixel){
        return (short)((excelPixel+0.631)/0.06631);
    }
    public File build(String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();

        fileInputStream.close();
        workbook.close();
//        workbook.close();need apache poi 4.0.0+
        return file;
    }
    protected short getMoneyFormat(String format){
        return workbook.createDataFormat().getFormat(format);
    }
    protected CellRangeAddress getMergeCellRange(int firstRow,int lastRow,int firstCol,int lastCol){
        return new CellRangeAddress(firstRow,lastRow,firstCol,lastCol);
    }
    protected XSSFCellStyle getBasicStyle(){
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }
}
