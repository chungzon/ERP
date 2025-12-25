package ERP.Model.Document;

import ERP.Model.ManagePayableReceivable.ManufacturerContactDocument;
import ERP.Model.ManagePayableReceivable.PurchaseAnnotationDocument;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.TemplatePath;
import ERP.ToolKit.ToolKit;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class DocumentGenerateTest {

    private String templateFilePath = "Template/template.xlsx";
    private ToolKit ToolKit = new ToolKit();
    private CallConfig CallConfig;
    public DocumentGenerateTest(){
        this.CallConfig = ToolKit.CallConfig;
    }
    /***
     * test's successful is not depend on result.
     * depend on out put file.
     * must check out put file's content is correct
     * @throws IOException throw exception when fail to export
     */
    @Test
    public void createManufacturerContactDocument() throws IOException {
        ManufacturerContactDocument document = new ManufacturerContactDocument();
        document.readTemplate(TemplatePath.IAECrawlerInvoiceOverview);
        for(int i=0;i<10;i++)
            document.addRow(false,
                    LocalDate.now(),
                    0,
                    100000,
                    "test",
                    "aa",
                    "qweq",
                    100,
                    0,
                    0,
                    ""
            );
        for(int i=0;i<10;i++)
            document.addRow(false,
                    LocalDate.now(),
                    100000,
                    0,
                    "test",
                    "aa",
                    "qweq",
                    100,
                    0,
                    0,
                    ""
            );
        for(int i=0;i<10;i++)
            document.addRow(false,
                    LocalDate.now(),
                    100000,
                    0,
                    "test",
                    "aa",
                    "qweq",
                    0,
                    100,
                    0,
                    ""
            );
        for(int i=0;i<10;i++)
            document.addRow(false,
                    LocalDate.now(),
                    0,
                    100000,
                    "test",
                    "aa",
                    "qweq",
                    0,
                    100,
                    0,
                    ""
            );
        document.build(CallConfig.getFile_OutputPath() + "\\單元測試 - 廠商往來明細.xlsx");

    }
    @Test
    public void createPurchaseAnnotationDocument() throws IOException {
        PurchaseAnnotationDocument document = new PurchaseAnnotationDocument();
        document.readTemplate(TemplatePath.IAECrawlerPurchaseNote);
        for(int i=0;i<10;i++)
            document.addRow(
                    "",
                    LocalDate.now(),
                    999,
                    "aaaaa",
                    LocalDate.now(),
                    "wwww",
                    "qweqwe",
                    4444,
                    "qgeqg",
                    LocalDate.now(),
                    "qegqegqe",
                    "qweqwe",
                    8888,
                    "qqqqqqq"
            );
        document.build(CallConfig.getFile_OutputPath() + "\\單元測試 - 採購備記.xlsx");
    }
    @Test
    void FormulaVerification(){
        String fileName = "2021-05-27_廠商往來明細(協新科技有限公司).xlsx";
        File templateFile = new File("outputfile/2021-05-27_廠商往來明細(協新科技有限公司).xlsx");
        String outputFilePath = CallConfig.getFile_OutputPath() + "\\(公式驗證)" + fileName;
        String objectID = "774";

        ManufacturerContactDocument document = new ManufacturerContactDocument();
        boolean status = document.formulaVerification(templateFile, outputFilePath, objectID);
    }
    @Test
    void verifyFinallyPrice(){
        ManufacturerContactDocument document = new ManufacturerContactDocument();
        String outputFilePath = CallConfig.getFile_OutputPath() + "\\2021-03-09_廠商往來明細(協新科技有限公司)_$-152586.xlsx";
        document.getVerifyFinallyPrice(outputFilePath);
    }
}
