package ERP.Model.ManageCustomerInfo;

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
class ManageCustomerInfo_ModelTest {
    private ManageCustomerInfo_Model ManageCustomerInfo_Model;
    public ManageCustomerInfo_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);

        ERPApplication.ToolKit = new ToolKit();
        this.ManageCustomerInfo_Model = ERPApplication.ToolKit.ModelToolKit.getManageCustomerInfoModel();
    }
    @Test
    void getNewestCustomerID() {
        String objectIDCharacter = "A";
        String CustomerID = ManageCustomerInfo_Model.getNewestCustomerID(objectIDCharacter);
        System.out.println(CustomerID);
        assertEquals(CustomerID,"000010");
    }

    @Test
    void isCustomerIDExist() {
        String customerID = "1521";
        boolean isIDExist = ManageCustomerInfo_Model.isCustomerIDExist(customerID,false);
        assertTrue(isIDExist);
    }

    @Test
    void insertCustomer() {
        ObjectInfo_Bean CustomerInfo_Bean = generateObjectInfoBean();
        boolean status = ManageCustomerInfo_Model.insertCustomer(CustomerInfo_Bean,false);
        assertTrue(status);
    }
    private ObjectInfo_Bean generateObjectInfoBean(){
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setObjectID("");
        ObjectInfo_Bean.setObjectName("");
        ObjectInfo_Bean.setObjectNickName("");
        ObjectInfo_Bean.setPersonInCharge("");
        ObjectInfo_Bean.setContactPerson("");
        ObjectInfo_Bean.setTelephone1("");
        ObjectInfo_Bean.setTelephone2("");
        ObjectInfo_Bean.setCellPhone("");
        ObjectInfo_Bean.setFax("");
        ObjectInfo_Bean.setEmail("");
        ObjectInfo_Bean.setMemberID("");
        ObjectInfo_Bean.setCompanyAddress("");
        ObjectInfo_Bean.setDeliveryAddress("");
        ObjectInfo_Bean.setInvoiceTitle("");
        ObjectInfo_Bean.setTaxIDNumber("");
        ObjectInfo_Bean.setInvoiceAddress("");

        ObjectInfo_Bean.setOrderTax(ObjectInfo_Enum.OrderTax.未稅);
        ObjectInfo_Bean.setPayableReceivableDiscount(1.0);
        ObjectInfo_Bean.setPrintPricing(ObjectInfo_Enum.ShipmentInvoicePrintPricing.Y);
        ObjectInfo_Bean.setSaleModel(ObjectInfo_Enum.CustomerSaleModel.單價);
        ObjectInfo_Bean.setSaleDiscount(1.0);
        ObjectInfo_Bean.setRemark("");
        ObjectInfo_Bean.setReceivableDay(25);
        ObjectInfo_Bean.setStoreCode("");
        return ObjectInfo_Bean;
    }
    @Test
    void modifyCustomer() {
        ObjectInfo_Bean CustomerInfo_Bean = generateObjectInfoBean();
        boolean status = ManageCustomerInfo_Model.modifyCustomer(CustomerInfo_Bean);
        assertTrue(status);
    }

    @Test
    void deleteCustomer() {
        String objectID = "";
        boolean status = ManageCustomerInfo_Model.deleteCustomer(objectID);
        assertTrue(status);
    }

    @Test
    void isCustomerExistOrder() {
        String objectID = "";
        boolean isExistOrder = ManageCustomerInfo_Model.isCustomerExistOrder(objectID);
        assertTrue(isExistOrder);
    }

    @Test
    void printCustomerInfo() {
        ObservableList<ObjectInfo_Bean> CustomerList = FXCollections.observableArrayList();
        String StartCustomer = "", EndCustomer = "";
        ManageCustomerInfo_Model.printCustomerInfo(CustomerList,StartCustomer,EndCustomer);
    }
}