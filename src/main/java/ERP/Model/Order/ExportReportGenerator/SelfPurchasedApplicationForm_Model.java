package ERP.Model.Order.ExportReportGenerator;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// 自行採購申請書
public class SelfPurchasedApplicationForm_Model {

    public static SelfPurchasedApplicationForm_Model createTemplate(String templatePath) throws IOException {
        SelfPurchasedApplicationForm_Model document = new SelfPurchasedApplicationForm_Model();
        document.template = new XWPFDocument(new FileInputStream(templatePath));
        return document;
    }
    private XWPFDocument template;
    private String customer;
    private String phoneBranch;
    private LocalDate date;
    private String productName;
    private int productPrice;
    private String allProduct;
    private String commit;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy年MM月dd日");
    public SelfPurchasedApplicationForm_Model(){}
    public SelfPurchasedApplicationForm_Model setCustomer(String customer) {
        this.customer = customer;
        return this;
    }
    public SelfPurchasedApplicationForm_Model setPhoneBranch(String phoneBranch) {
        this.phoneBranch = phoneBranch;
        return this;
    }
    public SelfPurchasedApplicationForm_Model setDate(LocalDate date) {
        this.date = date.minusYears(1911);
        return this;
    }
    public SelfPurchasedApplicationForm_Model setProductName(String productName) {
        this.productName = productName;
        return this;
    }
    public SelfPurchasedApplicationForm_Model setProductPrice(int productPrice) {
        this.productPrice = productPrice;
        return this;
    }
    public SelfPurchasedApplicationForm_Model setAllProduct(String allProduct) {
        this.allProduct = allProduct;
        return this;
    }
    public SelfPurchasedApplicationForm_Model setCommit(String commit) {
        this.commit = commit;
        return this;
    }
    public SelfPurchasedApplicationForm_Model build(){
        replace(template.getTables().get(0).getRow(3).getCell(1).getParagraphs().get(0),
                template.getTables().get(0).getRow(3).getCell(1).getParagraphs().get(0).getRuns().size(),
                0,
                this.customer,
                12);
        replace(template.getTables().get(0).getRow(3).getCell(1).getParagraphs().get(1),
                template.getTables().get(0).getRow(3).getCell(1).getParagraphs().get(1).getRuns().size(),
                1,
                this.phoneBranch,
                12);
        replace(template.getTables().get(0).getRow(4).getCell(1).getParagraphs().get(3),
                template.getTables().get(0).getRow(4).getCell(1).getParagraphs().get(3).getRuns().size(),
                6,
                this.date.format(formatter),
                12);
        template.getTables().get(1).getRow(2).getCell(0).getParagraphs().get(1).removeRun(2);
        change(template.getTables().get(1).getRow(2).getCell(0).getParagraphs().get(1),
                this.productName,
                14,"標楷體","Times New Roman");
        replace(template.getTables().get(1).getRow(2).getCell(0).getParagraphs().get(2),
                template.getTables().get(1).getRow(2).getCell(0).getParagraphs().get(2).getRuns().size(),
                2,
                this.productPrice+"元",
                14);
        replace(template.getTables().get(1).getRow(3).getCell(1).getParagraphs().get(0),
                template.getTables().get(1).getRow(3).getCell(1).getParagraphs().get(0).getRuns().size(),
                5,
                this.allProduct,
                12);
        replace(template.getTables().get(1).getRow(3).getCell(1).getParagraphs().get(1),
                template.getTables().get(1).getRow(3).getCell(1).getParagraphs().get(1).getRuns().size(),
                0,
                this.commit,
                12);
        return this;
    }
    public File export(String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream out = new FileOutputStream(file);
        template.write(out);
        out.close();
        return file;
    }
    private void replace(
            XWPFParagraph paragraph,
            int size,
            int startPosition,
            String replaceWith,
            int fontSize
    ){
        for(int i=startPosition;i<size;i++){
            paragraph.removeRun(startPosition);
        }
        change(paragraph,replaceWith,fontSize,"標楷體","Times New Roman");
    }
    private String dateFormat(String origin_Date){
        String[] array = origin_Date.split("/");
        if(array.length != 3){
            return origin_Date;
        }
        return array[0]+"年"+array[1]+"月"+array[2]+"日";
    }
    private void change(
            XWPFParagraph paragraph,
            String insertData,
            int fontSize,
            String chineseFontFamily,
            String otherFontFamily
    ){
        if(insertData == null || insertData.isEmpty()){
            return;
        }
        for(char character : insertData.toCharArray()){
            setRunFont(paragraph.createRun(),character,fontSize,chineseFontFamily,otherFontFamily);
        }
    }
    private void setRunFont(
            XWPFRun run,
            String text,
            int fontSize,
            String chineseFontFamily,
            String otherFontFamily
    ){
        if(text == null || text.isEmpty()){
            return;
        }
        for(char c : text.toCharArray()){
            setRunFont(run,c,fontSize,chineseFontFamily,otherFontFamily);
        }
    }
    private void setRunFont(
            XWPFRun run,
            char character,
            int fontSize,
            String chineseFontFamily,
            String otherFontFamily
    ){
        if((int)character<=126 && (int)character>=32){
            run.setFontFamily(otherFontFamily);
        }else{
            run.setFontFamily(chineseFontFamily);
        }
        run.setFontSize(fontSize);
        run.setText(String.valueOf(character));
    }
}
