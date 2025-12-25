package ERP.Model.Order.SearchNonePayReceive;

import ERP.Bean.Order.SearchNonePayReceive.ConditionalPayReceiveSearch_Bean;
import ERP.Bean.Order.SearchNonePayReceive.SearchNonePayReceive_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.Order_ExportPayReceiveDetails;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExportNonePayReceive_DocumentTest {
    private SearchNonePayReceive_Model SearchNonePayReceive_Model;
    public ExportNonePayReceive_DocumentTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);
        ERPApplication.ToolKit = new ToolKit();
        this.SearchNonePayReceive_Model = ERPApplication.ToolKit.ModelToolKit.getSearchNonePayReceiveModel();
    }
    @Test
    void exportSearchNonePayReceive() throws Exception {
        OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean = generateConditionalPayReceiveSearch();

        ObservableList<SearchNonePayReceive_Bean> searchNonePayReceivableList = SearchNonePayReceive_Model.getNonePayableReceivableOrderInfo(OrderObject, ConditionalPayReceiveSearch_Bean);
        if(searchNonePayReceivableList.size() == 0){
            DialogUI.MessageDialog("※ 無列印資料!");
            return;
        }else{
            boolean isSomeoneSelect = false;
            for(SearchNonePayReceive_Bean SearchNonePayReceive_Bean : searchNonePayReceivableList){
                if(SearchNonePayReceive_Bean.isCheckBoxSelect()){
                    isSomeoneSelect = true;
                    break;
                }
            }
            if(!isSomeoneSelect){
                DialogUI.MessageDialog("※ 未選擇任何資料!");
                return;
            }
        }
        ObservableList<SearchNonePayReceive_Bean> selectNonePayReceivableList = FXCollections.observableArrayList();
        for(SearchNonePayReceive_Bean searchNonePayReceive_Bean : searchNonePayReceivableList){
            if(searchNonePayReceive_Bean.isCheckBoxSelect()){
                selectNonePayReceivableList.add(searchNonePayReceive_Bean);
            }
        }
        ExportNonePayReceiveDocument exportNonePayReceiveDocument = new ExportNonePayReceiveDocument();
        if(exportNonePayReceiveDocument.exportSearchNonePayReceive(ConditionalPayReceiveSearch_Bean, selectNonePayReceivableList,OrderObject))
            DialogUI.MessageDialog("※ 列印成功!");
        else
            DialogUI.MessageDialog("※ 列印失敗!");
    }
    private ConditionalPayReceiveSearch_Bean generateConditionalPayReceiveSearch(){
        ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean = new ConditionalPayReceiveSearch_Bean();
        ConditionalPayReceiveSearch_Bean.setCheckoutByMonthStartDate("2020-01-26");
        ConditionalPayReceiveSearch_Bean.setCheckoutByMonthEndDate("2021-02-25");
        ConditionalPayReceiveSearch_Bean.setNoneCheckoutByMonthStartDate("2021-01-21");
        ConditionalPayReceiveSearch_Bean.setNoneCheckoutByMonthEndDate("2021-02-20");
        ConditionalPayReceiveSearch_Bean.setObjectName("");
        ConditionalPayReceiveSearch_Bean.setStartObjectID(null);
        ConditionalPayReceiveSearch_Bean.setEndObjectID(null);
        return ConditionalPayReceiveSearch_Bean;
    }
    @Test
    void exportNonePayReceiveDetails() {
        OrderObject OrderObject = Order_Enum.OrderObject.客戶;
        ConditionalPayReceiveSearch_Bean ConditionalPayReceiveSearch_Bean = generateConditionalPayReceiveSearch();
        ObservableList<SearchNonePayReceive_Bean> SearchNonePayReceivableList = SearchNonePayReceive_Model.getNonePayableReceivableOrderInfo(OrderObject, ConditionalPayReceiveSearch_Bean);
        SearchNonePayReceive_Bean SearchNonePayReceive_Bean = SearchNonePayReceivableList.get(0);
        ObservableList<SearchOrder_Bean> NonePayReceiveDetailsList = SearchNonePayReceive_Bean.getOrderList();
        Order_ExportPayReceiveDetails order_exportPayReceiveDetails = Order_ExportPayReceiveDetails.export;

        ExportNonePayReceiveDocument exportNonePayReceiveDocument = new ExportNonePayReceiveDocument();
        boolean status = exportNonePayReceiveDocument.exportNonePayReceiveDetails(ConditionalPayReceiveSearch_Bean,NonePayReceiveDetailsList,SearchNonePayReceive_Bean,OrderObject, order_exportPayReceiveDetails);
        assertTrue(status);
    }
}