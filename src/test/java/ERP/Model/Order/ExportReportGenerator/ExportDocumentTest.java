package ERP.Model.Order.ExportReportGenerator;

import ERP.ToolKit.TemplatePath;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ExportDocumentTest {
    String productName = "test product name";
    List<String> productList = new ArrayList<>();
    public ExportDocumentTest(){
        productList.add("商品1");
        productList.add("商品2");
        productList.add("商品3");
        productList.add("商品4");
        productList.add("商品5");
        productList.add("商品6");
        productList.add("商品7");
//        productList.add("商品8");
    }
    @Test
    public void createDocument() throws IOException {
        assert createDocument("協新").delete();
        assert createDocument("啟廉").delete();
        assert createDocument("展源浩").delete();
        assert createDocument("皇佳").delete();
        assert createDocument("福大興").delete();
    }
    private File createDocument(
            String vendorNickName
    ) throws IOException {
        ExportProcurementDocument_Model export = ExportProcurementDocument_Model.createTemplate(
                TemplatePath.ProcurementDocument_ProductList(vendorNickName)
        )
                .setProductName(productName)
                .setProductList(productList);
        switch (vendorNickName){
            case "協新":
                export.buildSheiSin();
                break;
            case "啟廉":
                export.buildChiLen();
                break;
            case "展源浩":
                export.buildZhanyh();
                break;
            case "皇佳":
                export.buildHuangGia();
                break;
            case "福大興":
                export.buildFuDaXing();
                break;
        }
        return export.export(vendorNickName+"test.docx");
    }
}

