package ERP.Model.Order;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.ManageProductCategory.ProductCategory_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.*;
import ERP.Bean.ToolKit.Installment.Installment_Bean;
import ERP.Bean.ToolKit.ProductGenerator.ProductGenerator_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Bean.ToolKit.ShowReportGenerator.ReportGenerator_Bean;
import ERP.Bean.ToolKit.ShowReportGenerator.ReportGenerator_Item_Bean;
import ERP.Bean.ToolKit.TransactionDetail.SearchTransactionDetail_Bean;
import ERP.Bean.ToolKit.TransactionDetail.TransactionDetail_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.ProductSaleStatus;
import ERP.Enum.Order.Order_Enum.OrderSearchMethod;
import ERP.Enum.Order.Order_Enum.OrderSearchColumn;
import ERP.Enum.Order.Order_Enum.SpecificationColumn;
import ERP.Enum.Order.Order_Enum.ProductSearchColumn;
import ERP.Enum.Order.Order_Enum.OrderTaxStatus;
import ERP.Enum.Product.Product_Enum;
import ERP.Model.Product.Product_Model;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ToolKit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Order_ModelTest {
    private CallConfig CallConfig;
    private Product_Model Product_Model;
    private Order_Model Order_Model;

    public Order_ModelTest() throws Exception {
        ERPApplication.ToolKit = new ToolKit();
        this.CallConfig = ERPApplication.ToolKit.CallConfig;
        this.Product_Model = ERPApplication.ToolKit.ModelToolKit.getProductModel();
        this.Order_Model = ERPApplication.ToolKit.ModelToolKit.getOrderModel();

        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,0);
    }
    @Test
    void getOrderDateBySpecificFormat() {
        String dateFormat = "2021-01-11";
        String output = Order_Model.getOrderDateBySpecificFormat(dateFormat);
        assertEquals(output,"110/01/11");
    }

    @Test
    void generateNewestOrderNumberOfEstablishOrder() {
        Order_Enum.generateOrderNumberMethod generateOrderNumberMethod = Order_Enum.generateOrderNumberMethod.日期;
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨子貨單;
        String inputText = "2020-11-26";
        String output = Order_Model.generateNewestOrderNumberOfEstablishOrder(OrderSource,generateOrderNumberMethod,inputText,false);
        assertEquals(output,"202001010001");
    }
    @Test
    void isOrderNumberExist() {
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨退貨單;
        String orderNumber = "202011180001";

        boolean isOrderNumberExist = Order_Model.isOrderNumberExist(OrderSource,orderNumber);
        assertTrue(isOrderNumberExist);
    }

    @Test
    void isOrderTransferWaitingOrAlready() {
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        String orderNumber = "202007270007";
        int order_id = 3;

        boolean isTransferWaitingOrAlready = Order_Model.isOrderTransferWaitingOrAlready(OrderSource,order_id,orderNumber);
        assertTrue(isTransferWaitingOrAlready);
    }

    @Test
    void isSubBillOfMainOrderTransferAlready() {
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        String waitingOrderNumber = "202007270008";
        String isTransferAlready = Order_Model.isSubBillOfMainOrderTransferAlready(OrderSource,waitingOrderNumber);
        assertNotNull(isTransferAlready);
    }

    @Test
    void getAllSubBillTotalPriceIncludeTax() {
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.報價單;
        String waitingOrderNumber = "202012170009";
        boolean isTransferAlready = false;
        Boolean isCheckout = null;
        int totalPriceIncludeTax = Order_Model.getAllSubBillTotalPriceIncludeTax(OrderSource,waitingOrderNumber,isTransferAlready,isCheckout);
        assertEquals(totalPriceIncludeTax,43500);
    }

    @Test
    void isProductInStockShortage() {
        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();
        boolean status = Order_Model.isProductInStockShortage(productList);
        assertFalse(status);
    }

    @Test
    void getObjectFromSearchColumn() {
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        Order_Enum.ObjectSearchColumn ObjectSearchColumn = Order_Enum.ObjectSearchColumn.客戶編號;
        String ObjectColumnText = "15";

        ObservableList<ObjectInfo_Bean> objectList = Order_Model.getObjectFromSearchColumn(OrderObject,ObjectSearchColumn,ObjectColumnText);
        for(ObjectInfo_Bean ObjectInfo_Bean : objectList){
            System.out.println(ObjectInfo_Bean.getObjectID() + "  " + ObjectInfo_Bean.getObjectName());
        }
        assertFalse(objectList.isEmpty());
    }

    @Test
    void insertOrderPicture() {
        int OrderID = 594;
        OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        String Base64Format = "1234";

        boolean status = Order_Model.insertOrderPicture(OrderID,Base64Format,OrderSource);
        assertTrue(status);
    }
    @Test
    void deleteOrderPicture() {
        int OrderID = 594;
        OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        int deleteItemNumber = 1;

        boolean status = Order_Model.deleteOrderPicture(OrderID,OrderSource,deleteItemNumber);
        assertTrue(status);
    }
    @Test
    void getOrderPicture() {
        int OrderID = 594;
        OrderSource OrderSource = Order_Enum.OrderSource.報價單;

        ArrayList<HashMap<Boolean,String>> OrderPictureList = Order_Model.getOrderPicture(OrderID,OrderSource);
        assertNull(OrderPictureList);
    }

    @Test
    void getOrderPictureDefaultPrinter() {
        String PrinterName = Order_Model.getOrderPictureDefaultPrinter();
        assertEquals(PrinterName,"");
    }

    @Test
    void printOrderImage() {
//        Order_Model.printOrderImage();
    }

    @Test
    void insertOrder() {
        OrderSource OrderSource = Order_Enum.OrderSource.報價單;
        Order_Bean Order_Bean = recordOrder_Bean();
        ObservableList<OrderProduct_Bean> wannaChangeSaleStatusProductList = null;
        boolean isHandleProductStatus = false;
        boolean status = Order_Model.insertOrder(OrderSource,Order_Bean,wannaChangeSaleStatusProductList,isHandleProductStatus,false);
        assertTrue(status);
    }
    private Order_Bean recordOrder_Bean(){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setOrderSource(OrderSource.出貨子貨單);
        Order_Bean.setNowOrderNumber("20200831003701");
        Order_Bean.setOrderDate("2020-08-31");
        Order_Bean.setObjectID("1521");
        Order_Bean.setObjectName("高科大-實驗室");
        Order_Bean.setIsBorrowed(false);
        Order_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.未沖帳);
        Order_Bean.setNumberOfItems("2");
        Order_Bean.setTotalPriceNoneTax("21400");
        Order_Bean.setTax("0");
        Order_Bean.setDiscount("0");
        Order_Bean.setTotalPriceIncludeTax("21400");
        Order_Bean.setOrderTaxStatus(Order_Enum.OrderTaxStatus.未稅);

        Order_Bean.setPurchaserName("");
        Order_Bean.setPurchaserTelephone("");
        Order_Bean.setPurchaserCellphone("");
        Order_Bean.setPurchaserAddress("");
        Order_Bean.setRecipientName("");
        Order_Bean.setRecipientTelephone("");
        Order_Bean.setRecipientCellphone("");
        Order_Bean.setRecipientAddress("");
        Order_Bean.setOrderRemark("");
        Order_Bean.setCashierRemark("");

        Order_Bean.setProjectCode("");
        Order_Bean.setProjectName("單元測試 - 專案名稱");
        Order_Bean.setProjectQuantity("");
        Order_Bean.setProjectUnit("");
        Order_Bean.setProjectPriceAmount("");
        Order_Bean.setProjectTotalPriceNoneTax("");
        Order_Bean.setProjectTax("");
        Order_Bean.setProjectTotalPriceIncludeTax("");
        Order_Bean.setProjectDifferentPrice("");

        HashMap<OrderSource,HashMap<Integer,Boolean>> orderReferenceMap = new HashMap<>();
        HashMap<Integer,Boolean> mainOrderReferenceMap = new HashMap<>();
        mainOrderReferenceMap.put(800,true);
        orderReferenceMap.put(OrderSource.報價單,mainOrderReferenceMap);
        HashMap<Integer,Boolean> subBillReferenceMap = new HashMap<>();
        subBillReferenceMap.put(1,true);
        subBillReferenceMap.put(31,true);
        subBillReferenceMap.put(183,true);
        orderReferenceMap.put(OrderSource.出貨子貨單,subBillReferenceMap);
        Order_Bean.setOrderReferenceMap(orderReferenceMap);

        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setItemNumber(1);
        OrderProduct_Bean.setISBN("2020200000011");
        OrderProduct_Bean.setProductName("測試7");
        OrderProduct_Bean.setQuantity(1);
        OrderProduct_Bean.setUnit("PCS");
        OrderProduct_Bean.setBatchPrice(10.4);
        OrderProduct_Bean.setSinglePrice(6900);
        OrderProduct_Bean.setPricing(1000);
        OrderProduct_Bean.setPriceAmount(6900);
        OrderProduct_Bean.setRemark("");
        productList.add(OrderProduct_Bean);

        OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setItemNumber(2);
        OrderProduct_Bean.setISBN("7787800000029");
        OrderProduct_Bean.setProductName("MSI Pro20EX 8GL-003TW N4000 128W10M ( 9S6-AAC212-030：PRO20EX 8GL-003TW )");
        OrderProduct_Bean.setQuantity(1);
        OrderProduct_Bean.setUnit("PCS");
        OrderProduct_Bean.setBatchPrice(13810);
        OrderProduct_Bean.setSinglePrice(14500);
        OrderProduct_Bean.setPricing(14500);
        OrderProduct_Bean.setPriceAmount(14500);
        OrderProduct_Bean.setRemark("");
        productList.add(OrderProduct_Bean);

        Order_Bean.setProductList(productList);

        Order_Bean.setIsCheckout(Order_Enum.CheckoutStatus.未結帳.value());
        Order_Bean.setEstablishSource(Order_Enum.EstablishSource.人工建立);
        return Order_Bean;
    }
    @Test
    void deleteWaitingOrder() {
        boolean isHandleProductStatus = false;
        OrderSource OrderSource = Order_Enum.OrderSource.待出貨單;
        String waitingOrderNumber = "202007270008";

        SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
        SearchOrder_Bean.setId(3);
        SearchOrder_Bean.setOrderSource(OrderSource);
        SearchOrder_Bean.setOrderNumber(waitingOrderNumber);

        boolean status = Order_Model.deleteWaitingOrder(SearchOrder_Bean, isHandleProductStatus);
        assertTrue(status);
    }

    @Test
    void deleteAlreadyOrder() {
        boolean isHandleProductStatus = false;
        OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        String alreadyOrderNumber = "202007270006";
        int order_id = 1;

        SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
        SearchOrder_Bean.setId(order_id);
        SearchOrder_Bean.setOrderSource(OrderSource);
        SearchOrder_Bean.setOrderNumber(alreadyOrderNumber);

        boolean status = Order_Model.deleteAlreadyOrder(SearchOrder_Bean,null,isHandleProductStatus);
        assertTrue(status);
    }

    @Test
    void deleteOrder() {
        OrderSource OrderSource = Order_Enum.OrderSource.報價單;
        String OrderNumber = "202101260009";
        int order_id = 765;
        boolean isHandleProductStatus = false;

        boolean status = Order_Model.deleteOrder(OrderSource,order_id,OrderNumber,isHandleProductStatus);
        System.out.println("刪除貨單單號 202101260009 : " + status);
        assertTrue(status);
    }

    @Test
    void transferWaitingOrder() {
        OrderSource OrderSource = Order_Enum.OrderSource.採購單;
        String waitingOrderDate = "2021-01-26",waitingOrderNumber = "202101260001",QuotationNumber = "202012280001";
        Order_Bean Order_Bean = new Order_Bean();
        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();

        boolean status = Order_Model.transferWaitingOrder(waitingOrderDate,waitingOrderNumber, Order_Bean, productList);
        assertTrue(status);
    }

    @Test
    void transferAlreadyOrder() {
        OrderSource OrderSource = Order_Enum.OrderSource.待入倉單;
        String alreadyOrderDate = "2021-01-26",alreadyOrderNumber = "202101260002";
        Order_Bean Order_Bean = new Order_Bean();
        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();
        boolean isHandleProductSaleStatus = false;

        boolean status = Order_Model.transferAlreadyOrder(alreadyOrderDate,alreadyOrderNumber,Order_Bean,productList,isHandleProductSaleStatus);
        assertTrue(status);
    }
    @Test
    void findSubBill_AllItems() {
        boolean isTransferAlreadyOrder = false;
        OrderSource OrderSource = Order_Enum.OrderSource.待出貨單;
        String waitingOrderNumber = "202008310037";

        HashMap<Integer, Integer> AllSubBillItemsMap = Order_Model.findSubBill_AllItems(OrderSource,waitingOrderNumber,isTransferAlreadyOrder);
        System.out.println(AllSubBillItemsMap);
        assertNotNull(AllSubBillItemsMap);
    }

    @Test
    void isMainOrderExistSubBill() {
        OrderObject orderObject = Order_Enum.OrderSource.報價單.getOrderObject();
        String waitingOrderNumber = "202008310037";

        boolean isExist = Order_Model.isMainOrderExistSubBill(orderObject, waitingOrderNumber,null,null);
        assertTrue(isExist);
    }
    @Test
    void isMainOrderExistInstallment(){
        OrderObject orderObject = Order_Enum.OrderSource.待入倉單.getOrderObject();
        String waitingOrderNumber = "202204200001";
        boolean isExist = Order_Model.isMainOrderExistInstallment(orderObject, waitingOrderNumber);
        assertTrue(isExist);
    }
    @Test
    void getSubBillOfOrder() {
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        String waitingOrderNumber = "202008260003";

        ArrayList<Order_Bean> separateOrderList = Order_Model.getSubBillOfOrder(OrderObject,waitingOrderNumber);
        for(Order_Bean Order_Bean : separateOrderList){
            System.out.println(Order_Bean.getOrderDate() + " " + Order_Bean.getNowOrderNumber());
        }
        assertFalse(separateOrderList.isEmpty());
    }

    @Test
    void isOrderExistSubBillAndSubBillNoneTransferToAlreadyShipment() {
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        String waitingOrderNumber = "202008310037";

        ArrayList<Order_Bean> subBillNoneTransferShipmentList = Order_Model.isOrderExistSubBillAndSubBillNoneTransferToAlreadyShipment(OrderObject,waitingOrderNumber);
        for(Order_Bean Order_Bean : subBillNoneTransferShipmentList){
            System.out.println(Order_Bean.getOrderDate() + " " + Order_Bean.getNowOrderNumber());
        }
        assertFalse(subBillNoneTransferShipmentList.isEmpty());
    }
    @Test
    void searchOrder() {
        OrderSource OrderSource = Order_Enum.OrderSource.進貨退貨單;
        OrderSearchColumn OrderSearchColumn = Order_Enum.OrderSearchColumn.對象編號與名稱;

        HashMap<Order_Enum.ReviewStatus,Boolean> searchOrderReviewStatus = new HashMap<>();
        searchOrderReviewStatus.put(Order_Enum.ReviewStatus.待審查,false);
        searchOrderReviewStatus.put(Order_Enum.ReviewStatus.待修改,false);
        searchOrderReviewStatus.put(Order_Enum.ReviewStatus.審查通過,false);

        ConditionalOrderSearch_Bean conditionalOrderSearch_Bean = new ConditionalOrderSearch_Bean();
        conditionalOrderSearch_Bean.setOrderObject(Order_Enum.OrderObject.廠商);
        conditionalOrderSearch_Bean.setSearchOrderSource(Order_Enum.SearchOrderSource.進貨退貨單);
        conditionalOrderSearch_Bean.setOrderSearchMethod(Order_Enum.OrderSearchMethod.日期搜尋);
        conditionalOrderSearch_Bean.setBorrowed(false);
        conditionalOrderSearch_Bean.setOrderStatus(Order_Enum.OrderStatus.有效貨單);
        conditionalOrderSearch_Bean.setStartDate("2021-01-01");
        conditionalOrderSearch_Bean.setEndDate("2021-05-20");
        conditionalOrderSearch_Bean.setSearchKeyWord("209");
        conditionalOrderSearch_Bean.setSearchOrderReviewStatus(searchOrderReviewStatus);
        conditionalOrderSearch_Bean.setSearchTextByOr(true);

        ObservableList<SearchOrder_Bean> orderList = Order_Model.searchOrder(OrderSource,OrderSearchColumn,conditionalOrderSearch_Bean);
        System.out.println("客戶編號：1521   日期區間：2021-01-01 ~ 2021-05-20");
        for(SearchOrder_Bean SearchOrder_Bean : orderList){
            System.out.println(SearchOrder_Bean.getOrderNumber() + " " + SearchOrder_Bean.getObjectID() + " " + SearchOrder_Bean.getObjectName() + " " + SearchOrder_Bean.getCashierRemark());
        }
        System.out.println("list is empty");
        assertTrue(orderList.isEmpty());

    }
    @Test
    void searchOrderProgress() {
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.廠商;
        boolean isBorrowed = false;
        String StartDate = "2020-12-25",EndDate = "2021-01-26";
        OrderSearchMethod OrderSearchMethod = Order_Enum.OrderSearchMethod.日期搜尋;
        String SearchKeyWord = "";
        OrderSearchColumn OrderSearchColumn = Order_Enum.OrderSearchColumn.對象與專案名稱;

        ObservableList<SearchOrderProgress_Bean> searchOrderProgressList = Order_Model.searchOrderProgress(OrderSearchMethod,OrderSearchColumn,OrderObject,isBorrowed,SearchKeyWord,StartDate,EndDate,false);
        for(SearchOrderProgress_Bean SearchOrderProgress_Bean : searchOrderProgressList){
            System.out.println(SearchOrderProgress_Bean.getQuotationDate() + " " + SearchOrderProgress_Bean.getQuotationNumber());
        }
        assertFalse(searchOrderProgressList.isEmpty());
    }
    @Test
    void updateExportQuotationVendor() {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setObjectID("774");
        IAECrawlerAccount_Bean.setObjectInfo_Bean(ObjectInfo_Bean);

        IAECrawlerAccount_Bean.setAccount("26099246");

        boolean status = Order_Model.updateExportQuotationVendor(IAECrawlerAccount_Bean);
        assertTrue(status);
    }

    @Test
    void insertExportQuotationItem(){
        String itemName = "測試";
        Double pricePercentage = 6.6, priceDiscount = null;
        ExportQuotationItem ExportQuotationItem = new ExportQuotationItem(null, itemName, pricePercentage, priceDiscount);
        boolean status = Order_Model.insertExportQuotationItem(ExportQuotationItem);
        assertTrue(status);
    }
    @Test
    void updateExportQuotationItemDefaultSelect(){
        int exportQuotationItemId = 23;
        boolean isDefaultSelect = false;
        boolean status = Order_Model.updateExportQuotationItemDefaultSelect(exportQuotationItemId,isDefaultSelect);
        assertTrue(status);
    }
    @Test
    void modifyExportQuotationItemPriceDiscount(){
        int id =24;
        double priceDiscount = 6.6;
        boolean status = Order_Model.modifyExportQuotationItemPriceDiscount(id,priceDiscount);
        assertTrue(status);
    }
    @Test
    void deleteExportQuotationItem(){
        int id = 0;
        boolean status = Order_Model.deleteExportQuotationItem(id);
        assertTrue(status);
    }
    @Test
    void getExportQuotationItem(){
        ObservableList<ExportQuotationItem> exportQuotationItemList = Order_Model.getExportQuotationItem(null);
        if(exportQuotationItemList != null){
            for(ExportQuotationItem ExportQuotationItem : exportQuotationItemList){
                System.out.println(ExportQuotationItem.getItemName() + " " + ExportQuotationItem.getPriceByDiscount());
            }
        }
        assertNotNull(exportQuotationItemList);
    }
    @Test
    void getOrderInfo() {
        OrderSource OrderSource = Order_Enum.OrderSource.採購單;
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setNowOrderNumber("202012280001");

        Order_Bean = Order_Model.getOrderInfo(OrderSource,Order_Bean);
        assertEquals(Order_Bean.getOrderSource(), Order_Enum.OrderSource.進貨單);
    }
    @Test
    void getOrderItems() {
        OrderSource OrderSource = Order_Enum.OrderSource.出貨退貨單;
        int Order_id = 7;

        ObservableList<OrderProduct_Bean> orderItems = Order_Model.getOrderItems(OrderSource,Order_id);
        for(OrderProduct_Bean OrderProduct_Bean : orderItems){
            System.out.println(OrderProduct_Bean.getItemNumber() + " " + OrderProduct_Bean.getISBN() + " " + OrderProduct_Bean.getProductName());
        }
        assertFalse(orderItems.isEmpty());
    }
    @Test
    void refresh_Order_Item_Id(){
        int order_id = 99;
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setItemNumber(1);
        OrderProduct_Bean.setISBN("");
        Integer newId = Order_Model.refresh_Order_Item_Id(order_id,OrderProduct_Bean);
        assertEquals(newId,13);
    }
    @Test
    void getProductGroup(){
        int order_id = 886;
        HashMap<Integer,HashMap<ProductGroup_Bean, LinkedHashMap<Integer,ItemGroup_Bean>>> productGroupMap = Order_Model.getProductGroupByOrderId(order_id);
        assertFalse(productGroupMap.isEmpty());
    }
    @Test
    void updateItemsProductGroup(){
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> originalProductGroupMap = null;
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> newProductGroupMap = null;
        boolean status = Order_Model.updateItemsProductGroup(originalProductGroupMap,newProductGroupMap);
        assertTrue(status);
    }
    @Test
    void getMainOrderItemsOfSubBill() {
        String waitingOrderNumber = "202102020002";
        OrderObject OrderObject = Order_Enum.OrderObject.廠商;

        ObservableList<OrderProduct_Bean> orderItems = Order_Model.getMainOrderItemsOfSubBill(OrderObject,waitingOrderNumber);
        for(OrderProduct_Bean OrderProduct_Bean : orderItems){
            System.out.println(OrderProduct_Bean.getItemNumber() + " " + OrderProduct_Bean.getISBN() + " " + OrderProduct_Bean.getProductName());
        }
        assertFalse(orderItems.isEmpty());
    }

    @Test
    void getTotalPriceIncludeTaxFromMainOrderBySubBill() {
        String waitingOrderNumber = "202007290013";
        OrderSource OrderSource = Order_Enum.OrderSource.報價單;

        int totalPriceIncludeTax = Order_Model.getTotalPriceIncludeTaxFromMainOrderBySubBill(OrderSource,waitingOrderNumber);
        assertEquals(totalPriceIncludeTax,16065);
    }
    @Test
    void getInstallmentByOrderId(){
        int order_id = 914;
        ArrayList<Installment_Bean> installmentList = Order_Model.getInstallmentByOrderId(order_id);
        for(Installment_Bean installment_bean : installmentList){
            System.out.println("id:" + installment_bean.getId());
            System.out.println("order id:" + installment_bean.getOrder_id());
            System.out.println("pay_receive_docuemnt_id:" + installment_bean.getPayReceive_document_id());
            System.out.println("installment:" + installment_bean.getInstallment());
            System.out.println("total_price:" + installment_bean.getTotalPrice());
            System.out.println("---------------------------------------");
        }
    }
    @Test
    void updateInstallment(){
        boolean isDelete = false;
        int order_id = 914;
        ArrayList<Installment_Bean> installmentList = new ArrayList<>();
        int count = 6;
        for(int index = 0; index < count; index++){
            Installment_Bean installment_bean = new Installment_Bean(index);
            installment_bean.setId((index+1));
            installment_bean.setPayReceive_document_id(null);
            installment_bean.setTotalPrice((index+1)*100);
            installmentList.add(installment_bean);
        }
        boolean status = Order_Model.updateInstallment(isDelete, order_id, installmentList);
        assertTrue(status);
    }
    @Test
    void insertReportGenerator(){
        ReportGenerator_Bean ReportGenerator_Bean = new ReportGenerator_Bean();
        ReportGenerator_Bean.setOrderOrSubBill_id(209);
        ReportGenerator_Bean.setOrderSource(OrderSource.出貨子貨單);
        ReportGenerator_Bean.setExport_manufacturer_id(1);
        ReportGenerator_Bean.setRemark("備註");

        ObservableList<ReportGenerator_Item_Bean> itemList = FXCollections.observableArrayList();
        ReportGenerator_Item_Bean ReportGenerator_Item_Bean = new ReportGenerator_Item_Bean();
        ReportGenerator_Item_Bean.setItemNumber(1);
        ReportGenerator_Item_Bean.setItemName("商品1");
        itemList.add(ReportGenerator_Item_Bean);

        ReportGenerator_Item_Bean = new ReportGenerator_Item_Bean();
        ReportGenerator_Item_Bean.setItemNumber(2);
        ReportGenerator_Item_Bean.setItemName("商品2");
        itemList.add(ReportGenerator_Item_Bean);

        ReportGenerator_Item_Bean = new ReportGenerator_Item_Bean();
        ReportGenerator_Item_Bean.setItemNumber(3);
        ReportGenerator_Item_Bean.setItemName("商品3");
        itemList.add(ReportGenerator_Item_Bean);
        ReportGenerator_Bean.setReportGenerator_itemList(itemList);
        assertTrue(Order_Model.insertReportGenerator(ReportGenerator_Bean));
    }
    @Test
    void getReportGenerator(){
        OrderSource OrderSource = Order_Enum.OrderSource.出貨子貨單;
        int orderOrSubBill_id = 209;

//        OrderSource OrderSource = Order_Enum.OrderSource.報價單;
//        int orderOrSubBill_id = 866;

        ObservableList<ReportGenerator_Bean> reportGeneratorList = Order_Model.getReportGenerator(OrderSource, orderOrSubBill_id);
        assertFalse(reportGeneratorList.isEmpty());
    }
    @Test
    void insertSpecificationTemplate(){
        String templateName = "", content = "";
        SpecificationColumn SpecificationColumn = Order_Enum.SpecificationColumn.基本需求;

        SpecificationTemplate_Bean SpecificationTemplate_Bean = Order_Model.insertSpecificationTemplate(templateName,content,SpecificationColumn);
        assertNotNull(SpecificationTemplate_Bean);
    }
    @Test
    void getSpecificationTemplate() {
        HashMap<SpecificationColumn,ObservableList<SpecificationTemplate_Bean>> specificationTemplateMap = Order_Model.getSpecificationTemplate();
        System.out.println(specificationTemplateMap);
        assertNotNull(specificationTemplateMap);
    }
    @Test
    void modifySpecificationName(){
        SpecificationTemplate_Bean SpecificationTemplate_Bean = new SpecificationTemplate_Bean(0,"新範本名稱5","",SpecificationColumn.基本需求);
        String newTemplateName = "新範本名稱576";

        boolean status = Order_Model.modifySpecificationName(SpecificationTemplate_Bean,newTemplateName);
        assertTrue(status);
    }
    @Test
    void modifySpecificationTemplate(){
        SpecificationTemplate_Bean SpecificationTemplate_Bean = new SpecificationTemplate_Bean(0,"範本5","",SpecificationColumn.基本需求);
        String newContent = "新範本內容";

        boolean status = Order_Model.modifySpecificationTemplate(SpecificationTemplate_Bean,newContent);
        assertTrue(status);
    }
    @Test
    void deleteSpecificationTemplate(){
        SpecificationTemplate_Bean SpecificationTemplate_Bean = new SpecificationTemplate_Bean(0,"基本需求範本2","",SpecificationColumn.基本需求);

        boolean status = Order_Model.deleteSpecificationTemplate(SpecificationTemplate_Bean);
        assertTrue(status);
    }
    @Test
    void getItemsProductTag() {
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setISBN("2020200000028");

        ObservableList<String> productTag = Order_Model.getItemsProductTag(OrderProduct_Bean);
        for(String tag : productTag){
            System.out.println(tag);
        }
        assertFalse(productTag.isEmpty());
    }

    @Test
    void updateOrderReviewStatus(){
        int order_id = 803;
        Order_Enum.ReviewObject ReviewObject = Order_Enum.ReviewObject.貨單商品;
        Order_Enum.ReviewStatus ReviewStatus = Order_Enum.ReviewStatus.待修改;
        OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean = new OrderReviewStatusRecord_Bean();
        OrderReviewStatusRecord_Bean.setSubject("主旨");
        OrderReviewStatusRecord_Bean.setRecord("編輯內容");
        OrderReviewStatusRecord_Bean.setReviewStatus(Order_Enum.ReviewStatus.待修改);
        ArrayList<OrderReviewStatusPicture_Bean> pictureList = new ArrayList<>();

        OrderReviewStatusPicture_Bean orderReviewStatusPicture_Bean = new OrderReviewStatusPicture_Bean();
        orderReviewStatusPicture_Bean.setBase64("7");
        pictureList.add(orderReviewStatusPicture_Bean);

        orderReviewStatusPicture_Bean = new OrderReviewStatusPicture_Bean();
        orderReviewStatusPicture_Bean.setBase64("3");
        pictureList.add(orderReviewStatusPicture_Bean);

        OrderReviewStatusRecord_Bean.setPictureList(pictureList);
        boolean status = Order_Model.updateOrderReviewStatus(order_id,ReviewObject,ReviewStatus,OrderReviewStatusRecord_Bean);
        assertTrue(status);
    }
    @Test
    void getOrderReviewStatusRecord(){
        int order_id = 821;
        ObservableList<OrderReviewStatusRecord_Bean> orderReviewStatusRecordList = Order_Model.getOrderReviewStatusRecord(order_id);
        for(OrderReviewStatusRecord_Bean OrderReviewStatusRecord_Bean : orderReviewStatusRecordList){
            System.out.println("審查狀態 = " + OrderReviewStatusRecord_Bean.getReviewStatus());
            System.out.println("主旨 = " +  OrderReviewStatusRecord_Bean.getSubject());
            System.out.println("原因(備註) = " + OrderReviewStatusRecord_Bean.getRecord());
            System.out.println("是否存在圖片 = " + OrderReviewStatusRecord_Bean.isExistPicture());
            System.out.println("-------------------------------------------------------");
        }
        assertFalse(orderReviewStatusRecordList.isEmpty());
    }
    @Test
    void modifyOrderReviewStatusRecord(){
        int id = 1,review_status_id = 820;
        String subject = "主旨";//
        String record = "原因1\n原因100";
        boolean status = Order_Model.modifyOrderReviewStatusRecord(id,review_status_id,subject,record);
        assertTrue(status);
    }
    @Test
    void deleteOrderReviewStatusRecord(){
        int id = 1,review_status_id = 820;
        boolean status = Order_Model.deleteOrderReviewStatusRecord(id,review_status_id);
        assertTrue(status);
    }
    @Test
    void insertOrderReviewStatusPicture(){
        int reviewStatus_record_id = 16;
        String base64 = "1234";
        Integer pictureID = Order_Model.insertOrderReviewStatusPicture(reviewStatus_record_id, base64);
        assertNotNull(pictureID);
    }
    @Test
    void deleteOrderReviewStatusPicture(){
        int id = 12;
        int reviewStatus_record_id = 18;
        boolean status = Order_Model.deleteOrderReviewStatusPicture(id, reviewStatus_record_id);
        assertTrue(status);
    }
    @Test
    void modifyOrder() {
        Order_Bean Order_Bean = recordOrder_Bean();
        Order_Bean.setOldOrderNumber("20200831003701");
        Order_Bean.setOrderID(63);
        ObservableList<OrderProduct_Bean> wantCancelProductList = null;
        boolean isHandleProductStatus = false;
        boolean coverOrderReference = false;
        boolean status = Order_Model.modifyOrder(Order_Bean,wantCancelProductList,isHandleProductStatus,coverOrderReference);
//        System.out.println("修改貨單 (1)單號 202101250001 → 202101260001 (2)日期 2021-01-25 → 2021-01-26 : " + status);
        assertTrue(status);
    }
    @Test
    void refreshProductSaleStatus() {
        ObservableList<OrderProduct_Bean> orderProductList = FXCollections.observableArrayList();
        OrderProduct_Bean OrderProductBean = new OrderProduct_Bean();
        OrderProductBean.setISBN("7787800000074");
        orderProductList.add(OrderProductBean);

        Order_Model.refreshProductSaleStatus(orderProductList);
        for(OrderProduct_Bean OrderProduct_Bean : orderProductList){
            System.out.println(OrderProduct_Bean.getISBN() + " 存量:" + OrderProduct_Bean.getInStock() + "  安全存量:" + OrderProduct_Bean.getSafetyStock() + "  待入倉量:" + OrderProduct_Bean.getWaitingPurchaseQuantity() +
                    "  待入庫存:" + OrderProduct_Bean.getWaitingIntoInStock() + "  需量:" + OrderProduct_Bean.getNeededPurchaseQuantity() + "  待出貨量:" + OrderProduct_Bean.getWaitingShipmentQuantity());
        }
    }

    @Test
    void handleProductSaleStatus(){
        ObservableList<OrderProduct_Bean> ProductList = FXCollections.observableArrayList();
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setISBN("7787800000074");
        ProductList.add(OrderProduct_Bean);

        OrderSource OrderSource = Order_Enum.OrderSource.進貨退貨單;
        ProductSaleStatus ProductSaleStatus = Order_Enum.ProductSaleStatus.新增存量;

        String query = Order_Model.handleProductSaleStatus(OrderSource,ProductSaleStatus,OrderProduct_Bean);
        System.out.println(query);
        assertNotNull(query);
    }
    @Test
    void modifyProduct() {
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();

        boolean status = Order_Model.modifyProduct(OrderProduct_Bean);
        if(status){
            status = Product_Model.updateProductTag(OrderProduct_Bean.getISBN(), OrderProduct_Bean.getProductTag());
        }
        assertTrue(status);
    }
    @Test
    void getProduct() {
        OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        ProductSearchColumn ProductSearchColumn = Order_Enum.ProductSearchColumn.標籤;
        String ObjectID = "1521", ProductColumnText = "778";

        ObservableList<OrderProduct_Bean> productList = Order_Model.getProduct(OrderObject,ProductSearchColumn,ObjectID,ProductColumnText);
        if(productList != null) {
            for (OrderProduct_Bean OrderProduct_Bean : productList) {
                System.out.println(OrderProduct_Bean.getISBN() + "  " + OrderProduct_Bean.getProductCode() + "  " + OrderProduct_Bean.getProductName());
            }
        }
        assertNotNull(productList);
    }

    @Test
    void refreshProductSeriesNumber() {

    }

    @Test
    void refreshProductWannaAmount() {
        OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();

        Order_Model.refreshProductWannaAmount(OrderObject,ObjectInfo_Bean,OrderProduct_Bean);
        System.out.println(OrderProduct_Bean.getPriceAmount());
    }

    @Test
    void getAllFirstCategoryOfProduct() {
        Product_Enum.CategoryLayer CategoryLayer = Product_Enum.CategoryLayer.大類別;
        ObservableList<ProductCategory_Bean> productFirstCategoryList = Order_Model.getAllFirstCategoryOfProduct(CategoryLayer);
        for(ProductCategory_Bean ProductCategory_Bean : productFirstCategoryList){
            System.out.println(ProductCategory_Bean.getCategoryID() + " " + ProductCategory_Bean.getCategoryName());
        }
        assertFalse(productFirstCategoryList.isEmpty());
    }

    @Test
    void getProductGenerator() {
        ProductGenerator_Bean ProductGenerator_Bean = new ProductGenerator_Bean();
        ProductGenerator_Bean.setProductQuantity(10);
        ProductGenerator_Bean.setSinglePrice(5);
        ProductGenerator_Bean.setTotalPrice(5000);
        ProductGenerator_Bean.setFirstCategoryIDList(null);
        ProductGenerator_Bean.setRemoveDuplicateProduct(true);
        if (ProductGenerator_Bean.isRemoveDuplicateProduct())
            ProductGenerator_Bean.setDuplicateProductDate(250);
        else
            ProductGenerator_Bean.setDuplicateProductDate(0);
        ProductGenerator_Bean.setSaveProductList(null);
        String ObjectID = "1521";

        ObservableList<OrderProduct_Bean> productList = Order_Model.getProductGenerator(ObjectID,ProductGenerator_Bean);
        for(OrderProduct_Bean OrderProduct_Bean : productList){
            System.out.println(OrderProduct_Bean.getISBN() + " " + OrderProduct_Bean.getProductName());
        }
        assertTrue(productList.isEmpty());
    }
    @Test
    void deleteRepeatExportQuotationFileName() {
        String outputFilePath = CallConfig.getFile_OutputPath();
        ExportQuotation_Bean ExportQuotation_Bean = new ExportQuotation_Bean();
        Order_Model.deleteRepeatExportQuotationFileName(outputFilePath,ExportQuotation_Bean);
    }
    @Test
    void  getOrderReferenceFromDatabase(){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setOrderSource(OrderSource.出貨單);
        Order_Bean.setOrderID(803);

        HashMap<Order_Enum.OrderSource,HashMap<Integer,Boolean>> orderReferenceMap = Order_Model.getOrderReferenceFromDatabase(Order_Bean);
        System.out.println(orderReferenceMap);
        assertNotNull(orderReferenceMap);
    }
    @Test
    void getOrderReferenceByOrderID(){
        HashMap<Integer,Boolean> mainOrderReferenceMap = new HashMap<Integer, Boolean>(){{put(1,true);put(389,true);}};
        HashMap<Integer,Boolean> subBillReferenceMap = new HashMap<Integer, Boolean>(){{put(31,true);put(7,true);}};

        ObservableList<Order_Bean> orderReferenceList = Order_Model.getOrderReferenceByOrderID(mainOrderReferenceMap,subBillReferenceMap);
        System.out.println(orderReferenceList);
        assertFalse(orderReferenceList.isEmpty());
    }

    @Test
    void findSimilarOrderReference() {
        OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        int order_id = 0;
        String customerCode = "1521", ISBN = "7501100000028";
        boolean isShipmentOrderPriority = true;
        ObservableList<Order_Bean> similarOrderReferenceList = Order_Model.findSimilarOrderReference(OrderSource, order_id, customerCode, ISBN, isShipmentOrderPriority);
        for (Order_Bean Order_Bean : similarOrderReferenceList) {
            System.out.println(Order_Bean.getObjectID() + "  " + Order_Bean.getOrderDate() + " " + Order_Bean.getNowOrderNumber() + " " + Order_Bean.getWaitingOrderDate() + " " + Order_Bean.getAlreadyOrderDate());
        }
        assertFalse(similarOrderReferenceList.isEmpty());
    }
    @Test
    void getTransactionDetail() {
        SearchTransactionDetail_Bean transactionDetail_bean = new SearchTransactionDetail_Bean();
        transactionDetail_bean.setOrderSearchMethod(Order_Enum.OrderSearchMethod.日期搜尋);
        transactionDetail_bean.setISBN("8880800000026");
        transactionDetail_bean.setGetQuotation(true);
        transactionDetail_bean.setGetWaitingOrder(false);
        transactionDetail_bean.setObjectID(null);
        transactionDetail_bean.setStartDate("2021-03-01");
        transactionDetail_bean.setEndDate("2021-11-20");

        ObservableList<TransactionDetail_Bean> transactionDetailList = Order_Model.getTransactionDetail(transactionDetail_bean);
        for(TransactionDetail_Bean TransactionDetail_Bean : transactionDetailList){
            System.out.println(TransactionDetail_Bean.getOrderDate() + " " + TransactionDetail_Bean.getOrderNumber() + " " + TransactionDetail_Bean.getOrderSource());
        }
        assertFalse(transactionDetailList.isEmpty());
    }

    @Test
    void calculateOrderTotalPrice_NoneTax() {
        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();
        String TotalPrice = Order_Model.calculateOrderTotalPrice_NoneTax(false, productList);
        assertEquals(TotalPrice,0);
    }

    @Test
    void calculateOrderTotalPrice_IncludeTax() {
        OrderTaxStatus OrderTaxStatus = Order_Enum.OrderTaxStatus.未稅;
        int totalPriceNoneTax = 5000;
        int totalPriceIncludeTax = Order_Model.calculateOrderTotalPrice_IncludeTax(OrderTaxStatus,totalPriceNoneTax);
        assertEquals(totalPriceIncludeTax,5250);
    }

    @Test
    void calculateProfit() {
        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();
        OrderTaxStatus OrderTaxStatus = Order_Enum.OrderTaxStatus.含稅;
        String totalPriceIncludeTax = "45";
        String profit = Order_Model.calculateProfit(OrderTaxStatus,productList,totalPriceIncludeTax);

        assertEquals(profit,"45");
    }
    @Test
    void calculateProductGroupTotalPrice_NoneTax(){
        HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap = new HashMap<>();
        String totalPriceNoneTax = Order_Model.calculateProductGroupTotalPrice_NoneTax(false, productGroupMap);
        System.out.println(totalPriceNoneTax);
    }
}