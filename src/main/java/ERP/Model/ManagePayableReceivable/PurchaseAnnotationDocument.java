package ERP.Model.ManagePayableReceivable;

import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PurchaseAnnotationDocument extends BasicExcelBuilder {
    private Font font;
    private short dataRowHeight = 0;
    private int rowIndex = 1;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy.MM.dd");
    public void readTemplate(String filePath) throws IOException {
        fileInputStream = new FileInputStream(new File(filePath));
        workbook = new XSSFWorkbook(fileInputStream);
        font = workbook.createFont();
        font.setCharSet(XSSFFont.ANSI_CHARSET);
        sheet = workbook.getSheetAt(0);
        dataRowHeight = sheet.getRow(1).getHeight();
    }
    public void addRow(
            String checkoutStatus,
            LocalDate schoolDate,
            int schoolAmount,
            String factoryName,
            LocalDate invoiceDate,
            String type,
            String number,
            int amount,
            String customerCode,
            LocalDate exportDate,
            String orderNumber,
            String projectName,
            int offerPrice,
            String comment
    ){

        XSSFRow row = sheet.createRow(rowIndex);
        row.setHeight(dataRowHeight);
        createRowCell(row,14);
        int index = 0;
        row.getCell(index++).setCellValue(checkoutStatus);
        row.getCell(index++).setCellValue(schoolDate == null ? "" : schoolDate.minusYears(1911).format(formatter));
        row.getCell(index++).setCellValue(schoolDate == null ? "" : String.valueOf(schoolAmount));
        row.getCell(index++).setCellValue(factoryName);
        row.getCell(index++).setCellValue(invoiceDate.minusYears(1911).format(formatter));
        row.getCell(index++).setCellValue(type);
        row.getCell(index++).setCellValue(number);
        row.getCell(index++).setCellValue(amount);
        row.getCell(index++).setCellValue(customerCode);
        row.getCell(index++).setCellValue(exportDate.minusYears(1911).format(formatter));
        row.getCell(index++).setCellValue(orderNumber);
        row.getCell(index++).setCellValue(projectName);
        row.getCell(index++).setCellValue(offerPrice);
        if(rowIndex != 1){
            sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,index,index+2));
        }
        XSSFCell lastCell = row.getCell(index++);
        lastCell.setCellValue(comment);
        rowIndex++;
    }
    private void createRowCell(XSSFRow row,int count){
        for(int i=0;i<count;i++){
            row.createCell(i);
        }
    }
}
