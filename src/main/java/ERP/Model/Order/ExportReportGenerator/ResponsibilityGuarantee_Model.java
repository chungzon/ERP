package ERP.Model.Order.ExportReportGenerator;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.tomcat.jni.Local;

import java.io.*;
import java.time.LocalDate;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;

// 責任切結書
public class ResponsibilityGuarantee_Model {

    public ResponsibilityGuarantee_Model(){}
    private XWPFDocument template;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy.MM.dd");
    public static ResponsibilityGuarantee_Model createTemplate(String templatePath) throws IOException {
        ResponsibilityGuarantee_Model document = new ResponsibilityGuarantee_Model();
        document.template = new XWPFDocument(new FileInputStream(templatePath));
        return document;
    }
    public ResponsibilityGuarantee_Model build(
            String productName,
            LocalDate date,
            String companyName
    ){
        for(XWPFParagraph paragraph : template.getParagraphs()){
            for (XWPFRun run : paragraph.getRuns()) {
                replaceRun(run,"@company",companyName);
                replaceRun(run,"@pjname",productName);
                replaceRun(run,"@date", MinguoDate.from(date).format(formatter));
            }
        }
        return this;
    }
    public File export(String filePath) throws IOException {
        File file = new File(filePath);
        FileOutputStream out = new FileOutputStream(file);
        template.write(out);
        out.close();
        return file;
    }
    private void replaceRun(XWPFRun run,String parameter,String value){
        String text = run.getText(0);
        if (text != null && text.contains(parameter)) {
            text = text.replace(parameter, value);
            run.setText(text,0);
        }
    }
}
