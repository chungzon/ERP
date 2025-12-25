package ERP.Model.ManageManufacturerInfo;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.ERPApplication;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManageManufacturerInfo_ModelTest {
    private ManageManufacturerInfo_Model ManageManufacturerInfo_Model;
    public ManageManufacturerInfo_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);

        ERPApplication.ToolKit = new ToolKit();
        this.ManageManufacturerInfo_Model = ERPApplication.ToolKit.ModelToolKit.getManageManufacturerInfoModel();
    }
    @Test
    void generateNewestManufacturerID() {
        String ManufacturerIDTitle = "6";
        String ManufacturerID = ManageManufacturerInfo_Model.generateNewestManufacturerID(ManufacturerIDTitle);
        System.out.println(ManufacturerID);
        assertEquals(ManufacturerID,"604");
    }

    @Test
    void isManufacturerIDExist() {
        String ManufacturerID = "776";
        boolean isIDExist = ManageManufacturerInfo_Model.isManufacturerIDExist(ManufacturerID,false);
        assertTrue(isIDExist);
    }

    @Test
    void insertManufacturer() {
        ObjectInfo_Bean ManufacturerInfo_Bean = generateObjectInfoBean();
        boolean status = ManageManufacturerInfo_Model.insertManufacturer(ManufacturerInfo_Bean,false);
        assertTrue(status);
    }
    private ObjectInfo_Bean generateObjectInfoBean(){
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setObjectID("299");
        ObjectInfo_Bean.setObjectName("地球種子應用科技有限公司");
        ObjectInfo_Bean.setObjectNickName("地球種子應用科技有限公司");
        ObjectInfo_Bean.setPersonInCharge("葉英超");
        ObjectInfo_Bean.setContactPerson("葉英超");
        ObjectInfo_Bean.setTelephone1("02-66177160");
        ObjectInfo_Bean.setTelephone2("");
        ObjectInfo_Bean.setCellPhone("");
        ObjectInfo_Bean.setFax("");
        ObjectInfo_Bean.setEmail("support@benevo.com.tw");
        ObjectInfo_Bean.setCompanyAddress("");
        ObjectInfo_Bean.setDeliveryAddress("");
        ObjectInfo_Bean.setInvoiceTitle("");
        ObjectInfo_Bean.setTaxIDNumber("");
        ObjectInfo_Bean.setInvoiceAddress("");
        ObjectInfo_Bean.setOrderTax(ObjectInfo_Enum.OrderTax.應稅);
        ObjectInfo_Bean.setDefaultPaymentMethod(ObjectInfo_Enum.DefaultPaymentMethod.支票);
        ObjectInfo_Bean.setPayableReceivableDiscount(1.0);
        ObjectInfo_Bean.setRemark("");
        ObjectInfo_Bean.setPayableDay(25);
        ObjectInfo_Bean.setCheckTitle("地球抬頭");
        ObjectInfo_Bean.setCheckoutByMonth(false);
        ObjectInfo_Bean.setCheckDueDay(60);
        ObjectInfo_Bean.setDiscountRemittanceFee(ObjectInfo_Enum.DiscountRemittanceFee.已扣);
        ObjectInfo_Bean.setDiscountPostage(ObjectInfo_Enum.DiscountPostage.已扣);
        ObjectInfo_Bean.setRemittanceFee(30);
        ObjectInfo_Bean.setPostage(28);
        ObjectInfo_Bean.setBankID(null);
        ObjectInfo_Bean.setBankBranch("高雄分行");
        ObjectInfo_Bean.setAccountName("公司匯款");
        ObjectInfo_Bean.setBankAccount("00123456789");
        return ObjectInfo_Bean;
    }
    @Test
    void modifyManufacturer() {
        ObjectInfo_Bean ManufacturerInfo_Bean = generateObjectInfoBean();
        boolean status = ManageManufacturerInfo_Model.modifyManufacturer(ManufacturerInfo_Bean);
        assertTrue(status);
    }

    @Test
    void deleteManufacturer() {
        String objectID = "";
        boolean status = ManageManufacturerInfo_Model.deleteManufacturer(objectID);
        assertTrue(status);
    }

    @Test
    void isManufacturerExistOrder() {
        String objectID = "";
        boolean isExistOrder = ManageManufacturerInfo_Model.isManufacturerExistOrder(objectID);
        assertTrue(isExistOrder);
    }

    @Test
    void printManufacturerInfo() {
        ObservableList<ObjectInfo_Bean> manufacturerList = FXCollections.observableArrayList();
        String startManufacturer = "", endManufacturer = "";
        ManageManufacturerInfo_Model.printManufacturerInfo(manufacturerList,startManufacturer,endManufacturer);
    }
}