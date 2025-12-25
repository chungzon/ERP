package ERP.Model.Order.ExportPurchaseQuotationDocument;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_ExportPurchaseFunction;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.TemplatePath;
import ERP.ToolKit.ToolKit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;


class ExportPurchaseQuotation_ModelTest {

    private CallConfig CallConfig;

    public ExportPurchaseQuotation_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,0);

        ERPApplication.ToolKit = new ToolKit();
        this.CallConfig = ERPApplication.ToolKit.CallConfig;
    }

    @Test public void createExportPurchaseQuotationDocument() throws IOException {
        Order_ExportPurchaseFunction exportPurchaseFunction = Order_ExportPurchaseFunction.PurchaseOrder;

        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();

        for(int index = 0 ; index < 37; index++){
            OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
            OrderProduct_Bean.setISBN("202000000000" + index);
            OrderProduct_Bean.setProductName("品名" + index);
            OrderProduct_Bean.setUnit("PCS");
            OrderProduct_Bean.setQuantity(index);
            OrderProduct_Bean.setBatchPrice(index);
            OrderProduct_Bean.setRemark("備註" + index);
            productList.add(OrderProduct_Bean);
        }
        int pageSize = productList.size()/15;
        if(productList.size()%15 == 0)
            pageSize = pageSize - 1;
        for(int page = 0 ; page <= pageSize ; page++) {
            int startIndex = page*15;
            int endIndex;
            if(productList.size()%15 == 0 || page != pageSize){
                endIndex = (page+1)*15;
            }else {
                endIndex = page * 15 + productList.size() % 15;
            }
            ExportPurchaseQuotation_Model document = new ExportPurchaseQuotation_Model();
            document.readTemplate(TemplatePath.PurchaseQuotationOrAskPriceDocument);
            document.setData(exportPurchaseFunction,
                    "2021-09-07",
                    "202109070001",
                    Order_Enum.OrderTaxStatus.含稅,
                    productList.subList(startIndex,endIndex),
                    "貨單備註",
                    true);
            String outputName = "單元測試 - " + exportPurchaseFunction.getDescribe() + "匯出_" + (page+1) + ".xlsx";
            document.build(CallConfig.getFile_OutputPath() + "\\" + outputName);
        }
    }
}