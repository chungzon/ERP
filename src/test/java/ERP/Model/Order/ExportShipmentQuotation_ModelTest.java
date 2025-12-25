package ERP.Model.Order;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.Order.ExportQuotationItem;
import ERP.Bean.Order.ExportQuotation_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Bean.ToolKit.ShowExportQuotationRecord.ExportQuotationRecord_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Model.Order.ExportShipmentQuotationDocument.ExportShipmentQuotationDocument;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
class ExportShipmentQuotation_ModelTest {
    private ToolKit ToolKit;
    private Order_Model Order_Model;
    private SystemSetting_Model SystemSetting_Model;
    public ExportShipmentQuotation_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,0);

        ERPApplication.ToolKit = new ToolKit();
        this.ToolKit = ERPApplication.ToolKit;
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
        this.SystemSetting_Model = ToolKit.ModelToolKit.getSystemSettingModel();
    }
    @Test
    void exportQuotation() {
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAEAccount_ManufacturerNickName(false);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            if(IAECrawlerAccount_Bean.isExportQuotation()){
                String vendorNickName = IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName();
                ExportQuotation_Bean ExportQuotation_Bean = generateExportQuotation(Order_Enum.ExportQuotationContent.項目明細, Order_Enum.ExportQuotationFormat.三聯, vendorNickName);

                ExportShipmentQuotationDocument ExportShipmentQuotationDocument = new ExportShipmentQuotationDocument();
                System.out.println(vendorNickName + " - export status:" + ExportShipmentQuotationDocument.setExportDataAndExportQuotation(Order_Enum.OrderObject.客戶, ExportQuotation_Bean));
            }
        }
    }
    @Test
    void getExportQuotationRecordCount(){
        int orderOrSubBill_id = 866;
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        int exportCount = Order_Model.getExportQuotationRecordCount(orderOrSubBill_id, OrderSource);
        assertEquals(exportCount,7);
    }
    @Test void getExportQuotationRecord(){
        int orderOrSubBill_id = 5;
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        ObservableList<ExportQuotationRecord_Bean> exportQuotationRecordList = Order_Model.getExportQuotationRecord(orderOrSubBill_id, OrderSource);
        assertTrue(exportQuotationRecordList.isEmpty());
    }
    @Test
    void insertExportQuotationRecord(){
        int orderOrSubBill_id = 5;
        int exportQuotation_id = 0;
        boolean exportStatus = false;
        ExportQuotation_Bean ExportQuotation_Bean = generateExportQuotation(Order_Enum.ExportQuotationContent.項目明細, Order_Enum.ExportQuotationFormat.三聯, "展源浩");
        boolean status = Order_Model.insertExportQuotationRecord(orderOrSubBill_id,exportQuotation_id,ExportQuotation_Bean,exportStatus);
        assertTrue(status);
    }
    private ExportQuotation_Bean generateExportQuotation(Order_Enum.ExportQuotationContent exportQuotationContent, Order_Enum.ExportQuotationFormat exportQuotationFormat, String vendorNickName){
        ExportQuotation_Bean ExportQuotation_Bean = new ExportQuotation_Bean();
        ExportQuotation_Bean.setOrderNumber("202109100001");
        ExportQuotation_Bean.setOrderDate("2021-09-10");
        ExportQuotation_Bean.setCustomerID("1521");
        ExportQuotation_Bean.setShowBatchPrice(true);
        ExportQuotation_Bean.setShowProfit(true);
        ExportQuotation_Bean.setExportContent(exportQuotationContent);
        ExportQuotation_Bean.setExportFormat(exportQuotationFormat);
        ExportQuotation_Bean.setVendorNickName(vendorNickName);
        ExportQuotation_Bean.setOrderSource(Order_Enum.OrderSource.報價單);
        ExportQuotation_Bean.setProjectName("專案名稱測試");
        ExportQuotation_Bean.setProjectQuantity("20");
        ExportQuotation_Bean.setProjectUnit("台");
        ExportQuotation_Bean.setProjectPriceAmount("500");
        ExportQuotation_Bean.setProjectTotalPriceNoneTax("10000");
        ExportQuotation_Bean.setProjectTax("500");
        ExportQuotation_Bean.setProjectTotalPriceIncludeTax("10500");
        ExportQuotation_Bean.setProjectDifferencePrice("500");
        ExportQuotation_Bean.setProjectCode("569");
        ExportQuotation_Bean.setExportCount(6);

        ExportQuotation_Bean.setOrderRemark("備註測試說明");

        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAEAccount_ManufacturerNickName(false);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
//            if(IAECrawlerAccount_Bean.isExportQuotation_defaultSelect()) {
            if(IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName().equals(vendorNickName)){
                ExportQuotation_Bean.setTemplateFormat(IAECrawlerAccount_Bean.getTemplateFormatJsonObject());
                break;
            }
        }
        ObservableList<Order_Bean> orderReferenceList = FXCollections.observableArrayList();
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setOrderID(969);
        Order_Bean.setOrderSource(Order_Enum.OrderSource.報價單);
        Order_Bean.setObjectID("1521");
        Order_Bean.setNowOrderNumber("202109100001");
        orderReferenceList.add(Order_Bean);
        ExportQuotation_Bean.setOrderReferenceList(orderReferenceList);

        ObservableList<OrderProduct_Bean> ProductList = FXCollections.observableArrayList();
        int totalPriceNoneTax = 0;
//        for(int index = 1 ; index < 20 ; index++){
        for(int index = 1 ; index < 49 ; index++){
            OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
            OrderProduct_Bean.setItemNumber(index);
            OrderProduct_Bean.setISBN("8881500000019");
            OrderProduct_Bean.setProductName("影音現場製作切換編輯器" + index);
            OrderProduct_Bean.setQuantity(index);
            OrderProduct_Bean.setUnit("組");
            OrderProduct_Bean.setBatchPrice(index);
            OrderProduct_Bean.setSinglePrice(index);
            OrderProduct_Bean.setPriceAmount(index);
            OrderProduct_Bean.setRemark("備註：" + index);
            ProductList.add(OrderProduct_Bean);
            totalPriceNoneTax = totalPriceNoneTax + OrderProduct_Bean.getPriceAmount();
        }
//        ExportQuotation_Bean.setProductList(generateSelectExportQuotationItem(totalPriceNoneTax, FXCollections.observableArrayList(ProductList)));
        ExportQuotation_Bean.setProductList(ProductList);

        /*
        Order_Bean = Order_Model.getOrderInfo(Order_Enum.OrderSource.報價單,Order_Bean);
        Order_Bean.setProductList(Order_Model.getOrderItems(Order_Enum.OrderSource.報價單, Order_Bean.getOrderID()));

        Order_Bean.setProductGroupMap(Order_Model.getProductGroupByOrderId(Order_Bean.getOrderID()));
        if(Order_Bean.getProductGroupMap() != null)
            ExportQuotation_Bean.setProductList(generateExportProductGroupItem(ExportQuotation_Bean.isShowBatchPrice(), Order_Bean.getProductGroupMap(),Order_Bean.getProductList()));
*/

/*
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setItemNumber(1);
        OrderProduct_Bean.setISBN("8881500000019");
        OrderProduct_Bean.setProductName("影音現場製作切換編輯器");
        OrderProduct_Bean.setQuantity(2);
        OrderProduct_Bean.setUnit("組");
        OrderProduct_Bean.setSinglePrice(500);
        OrderProduct_Bean.setPriceAmount(1000);
        ProductList.add(OrderProduct_Bean);

        OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setItemNumber(2);
        OrderProduct_Bean.setISBN("8881000000014");
        OrderProduct_Bean.setProductName("智能遠距遙控飛行器暨直播系統");
        OrderProduct_Bean.setQuantity(11);
        OrderProduct_Bean.setUnit("PCS");
        OrderProduct_Bean.setSinglePrice(100);
        OrderProduct_Bean.setPriceAmount(1100);
        ProductList.add(OrderProduct_Bean);

        OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setItemNumber(3);
        OrderProduct_Bean.setISBN("8880800000019");
        OrderProduct_Bean.setProductName("360度環景攝影機");
        OrderProduct_Bean.setQuantity(7);
        OrderProduct_Bean.setUnit("組");
        OrderProduct_Bean.setSinglePrice(700);
        OrderProduct_Bean.setPriceAmount(4900);
        ProductList.add(OrderProduct_Bean);

        ExportQuotation_Bean.setProductList(ProductList);
*/
        return ExportQuotation_Bean;
    }
    private ObservableList<OrderProduct_Bean> generateSelectExportQuotationItem(int totalPriceNoneTax, ObservableList<OrderProduct_Bean> orderProductList){
        ObservableList<ExportQuotationItem> exportQuotationItemList = Order_Model.getExportQuotationItem(null);
        for(ExportQuotationItem ExportQuotationItem : exportQuotationItemList){
            if(ExportQuotationItem.isCheckBoxSelect()){
                OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
                OrderProduct_Bean.setAddExportQuotationItem(true);
                OrderProduct_Bean.setItemNumber(orderProductList.size()+1);
                OrderProduct_Bean.setProductName(ExportQuotationItem.getItemName());
                OrderProduct_Bean.setUnit("PCS");
                OrderProduct_Bean.setQuantity(1);
                OrderProduct_Bean.setSinglePrice(ToolKit.RoundingInteger(totalPriceNoneTax*ExportQuotationItem.getPriceByDiscount()));
                OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(OrderProduct_Bean.getQuantity()*OrderProduct_Bean.getSinglePrice()));
                orderProductList.add(OrderProduct_Bean);
            }
        }
        return orderProductList;
    }
    private ObservableList<OrderProduct_Bean> generateExportProductGroupItem(boolean isShowBatchPrice, HashMap<Integer,
            HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap, ObservableList<OrderProduct_Bean> productList){
        ObservableList<OrderProduct_Bean> exportProductList = FXCollections.observableArrayList();
        for(Integer group_itemNumber : productGroupMap.keySet()){
            ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
            OrderProduct_Bean createOrderProduct_Bean = new OrderProduct_Bean();
            createOrderProduct_Bean.setAddExportQuotationItem(true);
            createOrderProduct_Bean.setItemNumber(ProductGroup_Bean.getItemNumber());
            createOrderProduct_Bean.setProductName(ProductGroup_Bean.getGroupName());
            createOrderProduct_Bean.setUnit(ProductGroup_Bean.getUnit());
            createOrderProduct_Bean.setQuantity(ProductGroup_Bean.getQuantity());
            createOrderProduct_Bean.setSinglePrice(ProductGroup_Bean.getSinglePrice());
            createOrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(ProductGroup_Bean.getPriceAmount()));

            if(isShowBatchPrice){
                double batchPrice = 0;
                HashMap<Integer,ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                for(Integer item_id : itemMap.keySet()){
                    ItemGroup_Bean ItemGroup_Bean = itemMap.get(item_id);
                    for(OrderProduct_Bean OrderProduct_Bean : productList) {
                        if(item_id.equals(OrderProduct_Bean.getItem_id())){
                            batchPrice = batchPrice + OrderProduct_Bean.getBatchPrice()*ItemGroup_Bean.getItem_quantity();
                            break;
                        }
                    }
                }
                createOrderProduct_Bean.setBatchPrice(batchPrice);
            }
            exportProductList.add(createOrderProduct_Bean);
        }
        return exportProductList;
    }
}