package ERP.Model.ManagePayableReceivable;

import ERP.Bean.ManagePayableReceivable.*;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.Invoice_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Bean.SystemSetting.IAECrawlerBelong_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManagePayableReceivable_ModelTest {
    private ManagePayableReceivable_Model ManagePayableReceivable_Model;
    public ManagePayableReceivable_ModelTest() throws Exception {
        ERPApplication.ToolKit = new ToolKit();
        this.ManagePayableReceivable_Model = ERPApplication.ToolKit.ModelToolKit.getManagePayableReceivableModel();

        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,0);
    }
    @Test
    void generateNewestOrderNumberOfPayableOrReceivable() {
        String orderDate = "2021-03-09";
        String orderNumber = ManagePayableReceivable_Model.generateNewestOrderNumberOfPayableOrReceivable(orderDate);
        assertEquals(orderNumber,"202103090001");
    }

    @Test
    void getNewestBankID() {
        int BankID = ManagePayableReceivable_Model.getNewestBankID();
        assertEquals(BankID,4);
    }

    @Test
    void getAllBankInfo() {
        boolean isAddEmptyObject = true;
        ObservableList<BankInfo_Bean> AllBankList = ManagePayableReceivable_Model.getAllBankInfo(isAddEmptyObject);
        System.out.println(AllBankList.size());
        assertFalse(AllBankList.isEmpty());
    }

    @Test
    void isExistCompanyAccount() {
        int bankID = 4;
        String BankAccount = null;
        boolean isExist = ManagePayableReceivable_Model.isExistCompanyAccount(bankID,BankAccount);
        assertTrue(isExist);
    }

    @Test
    void isExistBankInfo() {
        String bankCode = "";
        boolean isExist = ManagePayableReceivable_Model.isExistBankInfo(bankCode);
        assertTrue(isExist);
    }

    @Test
    void insertBankInfo(){
        BankInfo_Bean BankInfo_Bean = generateInsertBankInfoBean();
        boolean status = ManagePayableReceivable_Model.insertBankInfo(BankInfo_Bean);
        assertTrue(status);
    }
    private BankInfo_Bean generateInsertBankInfoBean(){
        BankInfo_Bean BankInfo_Bean = new BankInfo_Bean(0);
        BankInfo_Bean.setBankCode("678");
        BankInfo_Bean.setBankNickName("測試銀行簡稱");
        BankInfo_Bean.setBankName("銀行全名");
        BankInfo_Bean.setContactPerson1("");
        BankInfo_Bean.setContactPerson2("");
        BankInfo_Bean.setTelephone1("");
        BankInfo_Bean.setTelephone2("");
        BankInfo_Bean.setFax("");
        BankInfo_Bean.setAddress("");
        BankInfo_Bean.setRemark("");
        return BankInfo_Bean;
    }
    @Test
    void modifyBankInfo() {
        BankInfo_Bean BankInfo_Bean = generateModifyBankInfoBean();
        boolean status = ManagePayableReceivable_Model.modifyBankInfo(BankInfo_Bean);
        assertTrue(status);
    }
    private BankInfo_Bean generateModifyBankInfoBean(){
        BankInfo_Bean BankInfo_Bean = new BankInfo_Bean(999);
        BankInfo_Bean.setBankCode("");
        BankInfo_Bean.setBankNickName("");
        BankInfo_Bean.setBankName("");
        BankInfo_Bean.setContactPerson1("");
        BankInfo_Bean.setContactPerson2("");
        BankInfo_Bean.setTelephone1("");
        BankInfo_Bean.setTelephone2("");
        BankInfo_Bean.setFax("");
        BankInfo_Bean.setAddress("");
        BankInfo_Bean.setRemark("");
        return BankInfo_Bean;
    }
    @Test
    void insertCompanyAccount() {
        CompanyBank_Bean CompanyBank_Bean = new CompanyBank_Bean();
        CompanyBank_Bean.setBankID(00);
        CompanyBank_Bean.setBankAccount("");
        CompanyBank_Bean.setBankBranch("");
        CompanyBank_Bean.setBankAccountName("");
        CompanyBank_Bean.setAccountNickName("");
        CompanyBank_Bean.setRemark("");

        boolean status = ManagePayableReceivable_Model.insertCompanyAccount(CompanyBank_Bean);
        assertTrue(status);
    }

    @Test
    void modifyCompanyAccount() {
        CompanyBank_Bean CompanyBank_Bean = new CompanyBank_Bean();
        CompanyBank_Bean.setBankID(00);
        CompanyBank_Bean.setBankAccount("");
        CompanyBank_Bean.setBankBranch("");
        CompanyBank_Bean.setBankAccountName("");
        CompanyBank_Bean.setAccountNickName("");
        CompanyBank_Bean.setRemark("");
        boolean status = ManagePayableReceivable_Model.modifyCompanyAccount(CompanyBank_Bean);
        assertTrue(status);
    }

    @Test
    void deleteCompanyAccount() {
        CompanyBank_Bean CompanyBank_Bean = new CompanyBank_Bean();
        CompanyBank_Bean.setCompanyBankInfo_id(0);
        CompanyBank_Bean.setBankID(00);
        CompanyBank_Bean.setBankAccount("");
        CompanyBank_Bean.setBankBranch("");
        CompanyBank_Bean.setBankAccountName("");
        CompanyBank_Bean.setAccountNickName("");
        CompanyBank_Bean.setRemark("");
        boolean status = ManagePayableReceivable_Model.deleteCompanyAccount(CompanyBank_Bean);
        assertTrue(status);
    }

    @Test
    void getCompanyBankInfo() {
        boolean isAddEmptyObject = false;
        ObservableList<CompanyBank_Bean> CompanyBankInfoList = ManagePayableReceivable_Model.getCompanyBankInfo(isAddEmptyObject);
        System.out.println(CompanyBankInfoList.size());
        assertFalse(CompanyBankInfoList.isEmpty());
    }

    @Test
    void searchNonePayReceiveOrder() {
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨子貨單;
        String ObjectID = "1521", StartDate = "2020-11-01", EndDate = "2021-02-28";

        ObservableList<SearchOrder_Bean> nonePayReceiveOrderList = ManagePayableReceivable_Model.searchNonePayReceiveOrder(OrderObject,OrderSource,ObjectID,StartDate,EndDate);
        System.out.println(nonePayReceiveOrderList.size());
        assertFalse(nonePayReceiveOrderList.isEmpty());
    }
    @Test
    void isCheckNumberExist() {
        String checkNumber = "";
        boolean isExist = ManagePayableReceivable_Model.isCheckNumberExist(checkNumber);
        assertTrue(isExist);
    }

    @Test
    void isPayableReceivableNumberExist() {
        String orderNumber = "";
        boolean isNumberExist = ManagePayableReceivable_Model.isPayableReceivableNumberExist(orderNumber);
        assertTrue(isNumberExist);
    }

    @Test
    void insertPayableReceivable() {
        PayableReceivable_Bean PayableReceivable_Bean = generatePayableReceivableBean();
        boolean status = ManagePayableReceivable_Model.insertPayableReceivable(PayableReceivable_Bean);
        assertTrue(status);
    }
    private PayableReceivable_Bean generatePayableReceivableBean(){
        PayableReceivable_Bean PayableReceivable_Bean = new PayableReceivable_Bean();
        PayableReceivable_Bean.setOrderNumber("");
        PayableReceivable_Bean.setOrderDate("2021-02-03");
        PayableReceivable_Bean.setOrderObject(Order_Enum.OrderObject.客戶);
        PayableReceivable_Bean.setObjectID("");

        PayableReceivable_Bean.setCheckNumber("");
        PayableReceivable_Bean.setCheckDueDate(null);


        CompanyBank_Bean CompanyBank_Bean = ManagePayableReceivable_Model.getCompanyBankInfo(true).get(1);
        PayableReceivable_Bean.setCompanyBankInfo_id(CompanyBank_Bean.getCompanyBankInfo_id());

        PayableReceivable_Bean.setObjectBankBean(ManagePayableReceivable_Model.getAllBankInfo(true).get(1));
        PayableReceivable_Bean.setObjectBankBranch("");
        PayableReceivable_Bean.setObjectBankAccount("");
        PayableReceivable_Bean.setObjectAccountName("");
        PayableReceivable_Bean.setObjectPerson("");
        PayableReceivable_Bean.setCash("");
        PayableReceivable_Bean.setDeposit("");
        PayableReceivable_Bean.setOtherDiscount("");
        PayableReceivable_Bean.setRemittanceFeeAndPostage("");
        PayableReceivable_Bean.setCashDiscount("");
        PayableReceivable_Bean.setCheckPrice("");
        PayableReceivable_Bean.setOffsetPrice("");
        PayableReceivable_Bean.setTotalPriceIncludeTax("");
        PayableReceivable_Bean.setInvoiceNumber("");
        PayableReceivable_Bean.setRemark("");

        ObservableList<SearchOrder_Bean> orderList = FXCollections.observableArrayList();
        PayableReceivable_Bean.setOrderList(orderList);

        return PayableReceivable_Bean;
    }
    @Test
    void modifyPayableReceivable() {
        PayableReceivable_Bean PayableReceivable_Bean = generatePayableReceivableBean();
        boolean status = ManagePayableReceivable_Model.modifyPayableReceivable(PayableReceivable_Bean);
        assertTrue(status);
    }

    @Test
    void deletePayableReceivable() {
        SearchPayableReceivable_Bean SearchPayableReceivable_Bean = new SearchPayableReceivable_Bean();
        SearchPayableReceivable_Bean.setPayableReceivableID(1179);

        ObservableList<SearchOrder_Bean> orderList = FXCollections.observableArrayList();
        SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
        SearchOrder_Bean.setId(914);
        SearchOrder_Bean.setCheckBoxSelect(true);
        SearchOrder_Bean.setOrderSource(Order_Enum.OrderSource.待出貨單);
        SearchOrder_Bean.setWaitingOrderNumber("202112080006");
        orderList.add(SearchOrder_Bean);
        SearchPayableReceivable_Bean.setOrderList(orderList);

        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;

        boolean status = ManagePayableReceivable_Model.deletePayableReceivable(SearchPayableReceivable_Bean,OrderObject);
        assertTrue(status);
    }

    @Test
    void searchAllPayableReceivable() {
        ConditionalSearchPayReceive_Bean conditionalSearchPayReceive_bean = new ConditionalSearchPayReceive_Bean(false);
        conditionalSearchPayReceive_bean.setOrderObject(Order_Enum.OrderObject.客戶);
        conditionalSearchPayReceive_bean.setSearchMethod(PayableReceivable_Enum.SearchMethod.日期搜尋);
        conditionalSearchPayReceive_bean.setSearchKeyWord(null);
        conditionalSearchPayReceive_bean.setStartDate("2020-06-01");
        conditionalSearchPayReceive_bean.setEndDate("2021-02-05");

        ObservableList<SearchPayableReceivable_Bean> payableReceivableList = ManagePayableReceivable_Model.searchAllPayableReceivable(conditionalSearchPayReceive_bean);
        System.out.println(payableReceivableList.size());
        assertFalse(payableReceivableList.isEmpty());
    }
    @Test
    void getPayableReceivableOrder() {
        ObservableList<SearchOrder_Bean> orderList = FXCollections.observableArrayList();
        SearchOrder_Bean SearchOrderBean = new SearchOrder_Bean();
        SearchOrderBean.setOrderObject(Order_Enum.OrderObject.廠商);
        SearchOrderBean.setOrderSource(Order_Enum.OrderSource.進貨子貨單);
        orderList.add(SearchOrderBean);
        ManagePayableReceivable_Model.getPayableReceivableOrder(orderList);

        assertFalse(orderList.isEmpty());
    }

    @Test
    void exportPayableReceivable() {
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        PayableReceivable_Bean PayableReceivable_Bean =new PayableReceivable_Bean();
        PayableReceivable_Bean.setOrderList(FXCollections.observableArrayList());
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();

        String outputFilePath = ManagePayableReceivable_Model.exportPayableReceivable(OrderObject,ObjectInfo_Bean,PayableReceivable_Bean);
        assertEquals(outputFilePath,"");
    }

    @Test
    void printPayableReceivable() {
        ObservableList<SearchPayableReceivable_Bean> payableReceivableList = FXCollections.observableArrayList();
        String outputFilePath = "";
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        DatePicker StartDate_DatePicker = new DatePicker(), EndDate_DatePicker = new DatePicker();

        boolean status = ManagePayableReceivable_Model.printPayableReceivable(payableReceivableList,outputFilePath,OrderObject,StartDate_DatePicker,EndDate_DatePicker);
        assertTrue(status);
    }

    @Test
    void exportCheck() {
        Order_Enum.OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        ObservableList<SearchPayableReceivable_Bean> payableReceivableList = FXCollections.observableArrayList();
        boolean status = ManagePayableReceivable_Model.exportCheck(payableReceivableList,OrderObject);
        assertTrue(status);
    }

    @Test
    void searchIAECrawler() throws IOException {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setId(783);
        ObjectInfo_Bean.setObjectID("783");
        IAECrawlerAccount_Bean.setObjectInfo_Bean(ObjectInfo_Bean);
        IAECrawlerAccount_Bean.setAccount("89740540");
        IAECrawlerAccount_Bean.setPassword("meetg9g9");
        IAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName("青海");
        IAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName("");
        IAECrawlerAccount_Bean.setExportQuotation_defaultSelect(true);

        LocalDate startDate = LocalDate.parse("2022-06-01");
        LocalDate endDate = LocalDate.parse("2022-12-31");
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<IAECrawlerData_Bean> IAECrawlerDataList = new ArrayList<>();
        IAECrawler crawler = new IAECrawler();
        crawler.setDestination("https://cashweb.nkust.edu.tw/");
        if(crawler.login(IAECrawlerAccount_Bean.getAccount(), IAECrawlerAccount_Bean.getPassword())) {
            System.out.println("※ 帳戶登入成功");
            String PTCList = crawler.getPayListRaw(startDate,endDate,"", IAECrawler.PayListFunction.PTCNET);
            for (JsonNode node : mapper.readTree(PTCList)) {
                try {
                    IAECrawlerData_Bean IAECrawlerData_Bean = mapper.treeToValue(node, IAECrawlerData_Bean.class);
                    IAECrawlerData_Bean.setInvoiceAmount(IAECrawlerData_Bean.getPayAmount());
                    IAECrawlerData_Bean.setSource(PayableReceivable_Enum.IAECrawlerSource.零用金給付);
                    IAECrawlerDataList.add(IAECrawlerData_Bean);
                } catch (Exception var11) {
                    var11.printStackTrace();
                }
            }

            String PaymentList = crawler.getPayListRaw(startDate,endDate,"", IAECrawler.PayListFunction.CASHNET);
            for (JsonNode node : mapper.readTree(PaymentList)) {
                try {
                    IAECrawlerData_Bean IAECrawlerData_Bean = mapper.treeToValue(node, IAECrawlerData_Bean.class);
                    IAECrawlerData_Bean.setInvoiceAmount(ERPApplication.ToolKit.RoundingString(IAECrawlerData_Bean.getInvoiceAmount()));
                    IAECrawlerData_Bean.setSource(PayableReceivable_Enum.IAECrawlerSource.出納給付);
                    IAECrawlerDataList.add(IAECrawlerData_Bean);
                } catch (Exception var11) {
                    var11.printStackTrace();
                }
            }

            for(IAECrawlerData_Bean IAECrawlerData_Bean : IAECrawlerDataList){
                IAECrawlerData_Bean.setInvoice_manufacturerNickName_id(IAECrawlerAccount_Bean.getId());
                IAECrawlerData_Bean.setInvoiceManufacturerNickName(IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
                IAECrawlerData_Bean originIAECrawlerData_Bean = ManagePayableReceivable_Model.getExistIAECrawlerData(IAECrawlerAccount_Bean,IAECrawlerData_Bean.getRankey());
                if(originIAECrawlerData_Bean == null){
                    IAECrawlerData_Bean.setIAECrawlerStatus(PayableReceivable_Enum.IAECrawlerStatus.新增);
                    IAECrawlerData_Bean.setIAECrawlerReviewStatus(PayableReceivable_Enum.IAECrawlerReviewStatus.未查核);
                    System.out.println("[新增] 出納資訊 -- 發票日期：" + IAECrawlerData_Bean.getInvoiceDate() + "  發票號碼：" + IAECrawlerData_Bean.getInvoiceNumber() + "  摘要：" + IAECrawlerData_Bean.getInvoiceContent());
                }else{
                    if(IAECrawlerData_Bean.getInvoice_manufacturerNickName_id().equals(originIAECrawlerData_Bean.getInvoice_manufacturerNickName_id()) &&
                            originIAECrawlerData_Bean.getIAECrawlerStatus() == PayableReceivable_Enum.IAECrawlerStatus.忽略){ //  只更新資料
                        IAECrawlerData_Bean.setIAECrawlerReviewStatus(PayableReceivable_Enum.IAECrawlerReviewStatus.未查核);
                        System.out.println("[更新] 出納資訊 -- 發票日期：" + IAECrawlerData_Bean.getInvoiceDate() + "  發票號碼：" + IAECrawlerData_Bean.getInvoiceNumber() + "  摘要：" + IAECrawlerData_Bean.getInvoiceContent());
                    }else{
                        System.out.println("[重複比對] 出納資訊 -- 發票日期：" + IAECrawlerData_Bean.getInvoiceDate() + "  發票號碼：" + IAECrawlerData_Bean.getInvoiceNumber() + "  摘要：" + IAECrawlerData_Bean.getInvoiceContent());
                    }
                }
            }
        }
    }

    @Test
    void setIAECrawlerDataStatus() {
        PayableReceivable_Enum.IAECrawlerStatus IAECrawlerStatus = PayableReceivable_Enum.IAECrawlerStatus.新增;
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();
        IAECrawlerBelong_Bean IAECrawlerBelong_Bean = new IAECrawlerBelong_Bean();
        LocalDate startDate = null, endDate = null;

        boolean status = ManagePayableReceivable_Model.setIAECrawlerDataStatus(IAECrawlerAccount_Bean,IAECrawlerBelong_Bean,startDate,endDate,IAECrawlerData_Bean,IAECrawlerStatus);
        assertTrue(status);
    }

    @Test
    void getExistIAECrawlerData() {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        int ranKey = 0;
        IAECrawlerData_Bean IAECrawlerData_Bean = ManagePayableReceivable_Model.getExistIAECrawlerData(IAECrawlerAccount_Bean,ranKey);
        System.out.println(IAECrawlerData_Bean);
        assertNull(IAECrawlerData_Bean);
    }

    @Test
    void insertIAECrawlerPayment() {
        IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        IAECrawlerAccount_Bean.setObjectInfo_Bean(ObjectInfo_Bean);

        IAECrawlerBelong_Bean IAECrawlerBelong_Bean = new IAECrawlerBelong_Bean();

        boolean status = ManagePayableReceivable_Model.insertIAECrawlerPayment(IAECrawlerAccount_Bean,IAECrawlerData_Bean,IAECrawlerBelong_Bean);
        assertTrue(status);
    }

    @Test
    void updateIAECrawlerData() {
        IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();
        boolean updateManufacturer = false;
        boolean reBinding = false;
        boolean status = ManagePayableReceivable_Model.updateIAECrawlerData(IAECrawlerData_Bean,updateManufacturer,reBinding);
        assertTrue(status);
    }

    @Test
    void getAllIAECrawlerPaymentData() {
        PayableReceivable_Enum.IAECrawlerStatus IAECrawlerStatus = PayableReceivable_Enum.IAECrawlerStatus.新增;
        PayableReceivable_Enum.IAECrawlerReviewStatus IAECrawlerReviewStatus = PayableReceivable_Enum.IAECrawlerReviewStatus.未查核;
        HashMap<IAECrawlerData_Bean,SearchOrder_Bean> IAECrawlerDataAndOrderMap = null;

        ObservableList<IAECrawlerData_Bean> allIAECrawlerDataList = ManagePayableReceivable_Model.getAllIAECrawlerPaymentData(IAECrawlerStatus,IAECrawlerReviewStatus,IAECrawlerDataAndOrderMap);
        System.out.println(allIAECrawlerDataList.size());
        assertTrue(allIAECrawlerDataList.isEmpty());
    }

    @Test
    void getIARCrawlerDataBindingOrder() {
        HashMap<IAECrawlerData_Bean,SearchOrder_Bean> IAECrawlerDataAndOrderMap = new HashMap<>();
        IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();

        SearchOrder_Bean SearchOrder_Bean = ManagePayableReceivable_Model.getIARCrawlerDataBindingOrder(IAECrawlerDataAndOrderMap,IAECrawlerData_Bean);
        System.out.println(SearchOrder_Bean);
        assertNull(SearchOrder_Bean);
    }

    @Test
    void searchInvoiceSimilarOrder() {
        Order_Enum.OrderSource OrderSource = Order_Enum.OrderSource.出貨單;
        IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();
        IAECrawlerData_Bean.setInvoiceNumber("AX25230925");
        IAECrawlerData_Bean.setObjectID("1484");
        IAECrawlerData_Bean.setObjectTaxID("82929272");
        IAECrawlerData_Bean.setInvoiceAmount("1710000");
        IAECrawlerData_Bean.setPayAmount("1710000");
        ObservableList<SearchOrder_Bean> invoiceSimilarOrderList = ManagePayableReceivable_Model.searchInvoiceSimilarOrder(OrderSource,IAECrawlerData_Bean);
        for(SearchOrder_Bean SearchOrder_Bean : invoiceSimilarOrderList){
            System.out.println(SearchOrder_Bean.getOrderNumber() + "  " + SearchOrder_Bean.getCashierRemark());
        }
        System.out.println(invoiceSimilarOrderList.size());
        assertFalse(invoiceSimilarOrderList.isEmpty());
    }
    @Test
    void searchNoneBindingManufacturerOrder() {
        LocalDate startDate = LocalDate.parse("2021-10-01");
        LocalDate endDate = LocalDate.parse("2021-10-31");
        ObservableList<SearchOrder_Bean> invoiceSimilarOrderList = ManagePayableReceivable_Model.searchNoneBindingManufacturerOrder(Order_Enum.OrderSource.出貨單, 1, startDate, endDate);
        invoiceSimilarOrderList.addAll(ManagePayableReceivable_Model.searchNoneBindingManufacturerOrder(Order_Enum.OrderSource.出貨子貨單, 1, startDate, endDate));
        for(SearchOrder_Bean SearchOrder_Bean : invoiceSimilarOrderList){
            System.out.println(SearchOrder_Bean.getOrderNumber() + "  " + SearchOrder_Bean.getOrderSource());
        }
        System.out.println(invoiceSimilarOrderList.size());
        assertFalse(invoiceSimilarOrderList.isEmpty());
    }
    @Test
    void getOrderBindingPayment_id() {
        SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
        SearchOrder_Bean.setOrderSource(Order_Enum.OrderSource.出貨單);
        SearchOrder_Bean.setId(60);

        Integer bindingPayment_id = ManagePayableReceivable_Model.getOrderBindingPayment_id(SearchOrder_Bean);
        System.out.println(bindingPayment_id);
        assertNull(bindingPayment_id);
    }

    @Test
    void isInvoiceExist() {
        Invoice_Bean Invoice_Bean = new Invoice_Bean();
        Invoice_Bean.setInvoiceNumber("");
        Invoice_Bean.setInvoiceYear(109);

        boolean status = ManagePayableReceivable_Model.isInvoiceExist(Invoice_Bean);
        assertTrue(status);
    }

    @Test
    void fillBackOrderInvoiceInfo() {
        IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();
        SearchOrder_Bean searchOrder_Bean = new SearchOrder_Bean();
        boolean status = ManagePayableReceivable_Model.fillBackOrderInvoiceInfo(searchOrder_Bean,IAECrawlerData_Bean);
        assertTrue(status);
    }

    @Test
    void updateOrderInvoiceManufacturerNickNameID() {
        SearchOrder_Bean searchOrder_Bean = new SearchOrder_Bean();
        int newInvoiceManufacturerNickNameID = 0;

        boolean status = ManagePayableReceivable_Model.updateOrderInvoiceManufacturerNickNameID(searchOrder_Bean,newInvoiceManufacturerNickNameID);
        assertTrue(status);
    }

    @Test
    void setIAECrawlerInvoiceBindingOrder() {
        SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
        SearchOrder_Bean.setOrderSource(Order_Enum.OrderSource.出貨單);
        SearchOrder_Bean.setId(0);

        IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();

        boolean status = ManagePayableReceivable_Model.setIAECrawlerInvoiceBindingOrder(false,IAECrawlerData_Bean,SearchOrder_Bean);
        assertTrue(status);
    }

    @Test
    void deleteIAECrawlerDataBindingOrder() {
        int IAECrawlerPayment_id = 0;
        SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
        SearchOrder_Bean.setId(0);
        SearchOrder_Bean.setCashierRemark("");
        boolean status = ManagePayableReceivable_Model.deleteIAECrawlerDataBindingOrder(IAECrawlerPayment_id, SearchOrder_Bean);
        assertTrue(status);
    }

    @Test
    void searchPurchaseNoteInfo() {
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setObjectID("920");
        String startDate = "2020-02-01",endDate = "2021-02-05";

        ObservableList<IAECrawler_ExportPurchaseNote_Bean> purchaseNoteList = ManagePayableReceivable_Model.searchPurchaseNoteInfo(ObjectInfo_Bean,startDate,endDate);
        System.out.println(purchaseNoteList.size());
        assertTrue(purchaseNoteList.isEmpty());
    }
    @Test
    void insertManufacturerContactDetailIsCheckout(){
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailList = FXCollections.observableArrayList();
        boolean status = ManagePayableReceivable_Model.insertManufacturerContactDetailIsCheckout(manufacturerContactDetailList, PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部);
        assertFalse(status);
    }
    @Test
    void deleteManufacturerContactDetailIsCheckout(){
        IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean = new IAECrawler_ManufacturerContactDetail_Bean();
        PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail checkoutStatus_ManufacturerContactDetail = PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部;
        boolean status = ManagePayableReceivable_Model.deleteManufacturerContactDetailIsCheckout(IAECrawler_ManufacturerContactDetail_Bean,checkoutStatus_ManufacturerContactDetail);
        assertFalse(status);

    }
    @Test
    void searchManufacturerContactDetail() {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setObjectID("920");
        IAECrawlerAccount_Bean.setObjectInfo_Bean(ObjectInfo_Bean);
        String startDate = "2020-03-01",endDate = "2021-03-11";
        CheckoutStatus CheckoutStatus = Order_Enum.CheckoutStatus.未結帳;
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> IAECrawlerPaymentList = ManagePayableReceivable_Model.searchManufacturerContactDetail(ObjectInfo_Bean.getObjectID(),startDate,endDate,CheckoutStatus);
        for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : IAECrawlerPaymentList){
            System.out.println(IAECrawler_ManufacturerContactDetail_Bean.getProjectName() + " " + IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() + " " + IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id());
        }
        System.out.println(IAECrawlerPaymentList.size());
        assertEquals(IAECrawlerPaymentList.size(),6);
    }
    @Test
    void generateCheckDueDate() {
        Integer CheckDueDay = 20;
        String day = ManagePayableReceivable_Model.generateCheckDueDate(CheckDueDay);
        System.out.println(day);
        assertEquals(day,"2021-03-19");
    }
}