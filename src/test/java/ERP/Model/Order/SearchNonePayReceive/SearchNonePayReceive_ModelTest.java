package ERP.Model.Order.SearchNonePayReceive;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.SearchNonePayReceive.ConditionalPayReceiveSearch_Bean;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.ERPApplication;
import ERP.Sql.SqlAdapter;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.ToolKit.ToolKit;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.HashMap;

class SearchNonePayReceive_ModelTest {
    private SearchNonePayReceive_Model SearchNonePayReceive_Model;
    public SearchNonePayReceive_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,0);

        ERPApplication.ToolKit = new ToolKit();
        SearchNonePayReceive_Model = ERPApplication.ToolKit.ModelToolKit.getSearchNonePayReceiveModel();
    }
    @Test
    void getAllObjectInfo() {
        OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        ObservableList<ObjectInfo_Bean> objectInfoList = SearchNonePayReceive_Model.getAllObjectInfo(OrderObject);
        for(ObjectInfo_Bean ObjectInfo_Bean : objectInfoList){
            System.out.println(ObjectInfo_Bean.getObjectID() + " " + ObjectInfo_Bean.getObjectName());
        }
        assertFalse(objectInfoList.isEmpty());
    }

    @Test
    void getNonePayableReceivableOrderInfo() {
        OrderObject OrderObject = Order_Enum.OrderObject.廠商;
        ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean = generateConditionalPayReceiveSearch();

        ObservableList<SearchNonePayReceive_Bean> payableReceivableList = SearchNonePayReceive_Model.getNonePayableReceivableOrderInfo(OrderObject,ConditionalPayReceiveSearch_Bean);
        for(SearchNonePayReceive_Bean SearchNonePayReceive_Bean : payableReceivableList){
            System.out.println(SearchNonePayReceive_Bean.getObjectID() + "  " + SearchNonePayReceive_Bean.getObjectName() + "  " + SearchNonePayReceive_Bean.getOrderQuantity() + "/" + SearchNonePayReceive_Bean.getReturnOrderQuantity());
            ObservableList<SearchOrder_Bean> orderList = SearchNonePayReceive_Bean.getOrderList();
            for(SearchOrder_Bean SearchOrder_Bean : orderList){
                System.out.println(SearchOrder_Bean.getOrderDate() + "  " + SearchOrder_Bean.getOrderNumber() + "  " + SearchOrder_Bean.getOrderSource().name());
            }
            System.out.println("------------------------------------------");
        }
        assertFalse(payableReceivableList.isEmpty());
    }
    private ConditionalPayReceiveSearch_Bean generateConditionalPayReceiveSearch(){
        ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean = new ConditionalPayReceiveSearch_Bean();
        ConditionalPayReceiveSearch_Bean.setCheckoutByMonthStartDate("2021-03-26");
        ConditionalPayReceiveSearch_Bean.setCheckoutByMonthEndDate("2021-04-25");
        ConditionalPayReceiveSearch_Bean.setNoneCheckoutByMonthStartDate("2021-06-21");
        ConditionalPayReceiveSearch_Bean.setNoneCheckoutByMonthEndDate("2021-07-20");
        ConditionalPayReceiveSearch_Bean.setObjectName("");
        ConditionalPayReceiveSearch_Bean.setStartObjectID("202");
        ConditionalPayReceiveSearch_Bean.setEndObjectID("202");
        return ConditionalPayReceiveSearch_Bean;
    }
    @Test
    void getAlreadyPayReceiveCheckInfo(){
        OrderObject OrderObject = Order_Enum.OrderObject.廠商;
        HashMap<LocalDate,Integer> checkDueDateAndCheckPriceMap = SearchNonePayReceive_Model.getAlreadyPayReceiveCheckInfo(OrderObject);
        System.out.println(checkDueDateAndCheckPriceMap);
        assertNotNull(checkDueDateAndCheckPriceMap);
    }
    @Test
    void getManufacturerInfo() {
        SearchNonePayReceive_Bean SearchNonePayReceive_Bean = generateSearchNonePayReceive();

        SearchNonePayReceive_Model.getManufacturerInfo(SearchNonePayReceive_Bean);
        System.out.println(SearchNonePayReceive_Bean.getObjectID() + " " + SearchNonePayReceive_Bean.getObjectName());
        assertEquals(SearchNonePayReceive_Bean.getObjectID(),"202");
    }
    private SearchNonePayReceive_Bean generateSearchNonePayReceive(){
        SearchNonePayReceive_Bean SearchNonePayReceive_Bean = new SearchNonePayReceive_Bean();
        SearchNonePayReceive_Bean.setObjectID("202");
        return SearchNonePayReceive_Bean;
    }
}