package ERP.Model.Order;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.ExportOrder_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import java.io.File;

class ExportOrder_ModelTest {
    private CallConfig CallConfig;
    private ExportOrder_Model ExportOrder_Model;
    public ExportOrder_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);

        ERPApplication.ToolKit = new ToolKit();
        this.CallConfig = ERPApplication.ToolKit.CallConfig;

        String outputFilePath = CallConfig.getFile_OutputPath();
        Order_Bean Order_Bean = generateOrderInfo();
        ObjectInfo_Bean ObjectInfo_Bean = generateObjectInfo();
        ObservableList<ExportOrder_Bean> productList = generateProductList();
        this.ExportOrder_Model = ERPApplication.ToolKit.ModelToolKit.getExportOrderModel(outputFilePath,Order_Bean,ObjectInfo_Bean,productList);
    }
    @Test
    void exportOrder() {
        boolean isShowPrice = false;
        boolean status = ExportOrder_Model.ExportOrder(isShowPrice);
        assertTrue(status);
    }

    @Test
    void printOrder() {
        boolean isShowPrice = false;
        String outputFilePath = CallConfig.getFile_OutputPath();
        if(ExportOrder_Model.ExportOrder(isShowPrice)){
            try{
                if(Desktop.isDesktopSupported()){
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.PRINT))
                        desktop.print(new File(outputFilePath));
                }else    System.out.println("debug: desktop not supported!");
            }catch (Exception Ex){
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }
        }
    }
    private Order_Bean generateOrderInfo(){
        Order_Bean Order_Bean = new Order_Bean();
        Order_Bean.setOrderSource(Order_Enum.OrderSource.採購單);
        Order_Bean.setOrderDate("2021-02-03");
        Order_Bean.setNowOrderNumber("202102030001");
        return Order_Bean;
    }
    private ObjectInfo_Bean generateObjectInfo(){
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setObjectID("999");
        ObjectInfo_Bean.setObjectName("展源浩");
        ObjectInfo_Bean.setContactPerson("展源浩工程師");
        ObjectInfo_Bean.setTelephone1("07-3103102");
        ObjectInfo_Bean.setDeliveryAddress("高雄市三民區建工路270號");
        return ObjectInfo_Bean;
    }
    private ObservableList<ExportOrder_Bean> generateProductList(){
        ObservableList<ExportOrder_Bean> productList = FXCollections.observableArrayList();
        ExportOrder_Bean ExportOrder_Bean = new ExportOrder_Bean();
        ExportOrder_Bean.setItemNumber(1);
        ExportOrder_Bean.setISBN("8881500000019");
        ExportOrder_Bean.setProductName("影音現場製作切換編輯器");
        ExportOrder_Bean.setQuantity(10);
        ExportOrder_Bean.setUnit("組");
        ExportOrder_Bean.setSalePrice(5010);
        ExportOrder_Bean.setPriceAmount(50100);
        ExportOrder_Bean.setRemark("備註(影音現場製作切換編輯器)");
        productList.add(ExportOrder_Bean);

        ExportOrder_Bean = new ExportOrder_Bean();
        ExportOrder_Bean.setItemNumber(2);
        ExportOrder_Bean.setISBN("8881000000014");
        ExportOrder_Bean.setProductName("智能遠距遙控飛行器暨直播系統");
        ExportOrder_Bean.setQuantity(2);
        ExportOrder_Bean.setUnit("PCS");
        ExportOrder_Bean.setSalePrice(160);
        ExportOrder_Bean.setPriceAmount(320);
        ExportOrder_Bean.setRemark("備註(智能遠距遙控飛行器暨直播系統)");
        productList.add(ExportOrder_Bean);

        ExportOrder_Bean = new ExportOrder_Bean();
        ExportOrder_Bean.setItemNumber(3);
        ExportOrder_Bean.setISBN("8880800000019");
        ExportOrder_Bean.setProductName("360度環景攝影機");
        ExportOrder_Bean.setQuantity(4);
        ExportOrder_Bean.setUnit("組");
        ExportOrder_Bean.setSalePrice(780);
        ExportOrder_Bean.setPriceAmount(3120);
        ExportOrder_Bean.setRemark("備註(360度環景攝影機)");
        productList.add(ExportOrder_Bean);
        return productList;
    }
}