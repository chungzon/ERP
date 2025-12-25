package ERP.Model.SystemSetting;

import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.ToolKit.ShowSnapshotOrderPicture.IpCamInfo_Bean;
import ERP.Bean.SystemSetting.*;
import ERP.ERPApplication;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
class SystemSetting_ModelTest {
    private SystemSetting_Model SystemSetting_Model;
    public SystemSetting_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,0);
        ERPApplication.ToolKit = new ToolKit();
        this.SystemSetting_Model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
    }
    @Test
    @Order(1)
    void getAllVersion() {
        ObservableList<Version_Bean> allVersionList = SystemSetting_Model.getAllVersion();
        for(Version_Bean Version_Bean : allVersionList){
            System.out.println(Version_Bean.getVersion());
        }
    }

    @Test
    @Order(2)
    void insertVersion() {
        Version_Bean Version_Bean = new Version_Bean();
//        Version_Bean.setVersion("tes");
        boolean status = SystemSetting_Model.insertVersion(Version_Bean);
        assertTrue(status);
    }
    @Test
    @Order(3)
    void modifyVersion() {
        Version_Bean Version_Bean = new Version_Bean();
        Version_Bean.setId(75);
        Version_Bean.setVersion("2021/01/19 v.01");
        String versionContent = "(1) 出納比對系統 [搜尋相似貨單]： 新增條件 - 出納不存在發票時比對付款金額";
        boolean status = SystemSetting_Model.modifyVersion(Version_Bean,versionContent);
        assertTrue(status);
    }
    @Test
    @Order(4)
    void deleteVersion() {
        Version_Bean Version_Bean = new Version_Bean();
        Version_Bean.setId(0);
        Version_Bean.setVersion("");
        boolean status = SystemSetting_Model.deleteVersion(Version_Bean);
        assertTrue(status);
    }

    @Test
    @Order(5)
    void loadAllSystemSettingData() {
        SystemSettingConfig_Bean SystemSettingConfig_Bean = SystemSetting_Model.loadAllSystemSettingData();
        System.out.println(SystemSettingConfig_Bean);
    }

    @Test
    @Order(6)
    void getSpecificSystemSettingData() {
        String specificSystemSettingData  = SystemSetting_Model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.客戶位數);
        System.out.println("客戶位數(無英文) = " + specificSystemSettingData);
        assertEquals(specificSystemSettingData,"4");
    }
    @Test
    @Order(7)
    void getPrinterService() {
        ArrayList<String> printService = SystemSetting_Model.getPrinterService();
        System.out.println(printService);
    }

    @Test
    @Order(8)
    void getProductBookCase() {
        ObservableList<ProductBookCase_Bean> ProductBookCaseList = SystemSetting_Model.getProductBookCase();
        for(ProductBookCase_Bean ProductBookCase_Bean : ProductBookCaseList){
            System.out.println(ProductBookCase_Bean.getProductLevel() + "  " + ProductBookCase_Bean.getProductArea() + "  " + ProductBookCase_Bean.getProductFloor());
        }
        assertFalse(ProductBookCaseList.isEmpty());
    }
    @Test
    @Order(9)
    void insertAreaOrFloorOfBookCase() {
        ProductBookCase_Bean ProductBookCase_Bean = new ProductBookCase_Bean();
        ProductBookCase_Bean.setProductArea("儲存區");
        ProductBookCase_Bean.setProductFloor("儲存層");
        ProductBookCase_Bean.setProductLevel(SystemSetting_Enum.ProductLevel.商品儲存層);
        boolean status = SystemSetting_Model.insertAreaOrFloorOfBookCase(ProductBookCase_Bean);
        assertTrue(status);
    }

    @Test
    @Order(10)
    void modifyProductSave() {
        ProductBookCase_Bean ProductBookCase_Bean = new ProductBookCase_Bean();
        ProductBookCase_Bean.setProductArea("儲存區");
        ProductBookCase_Bean.setProductFloor("儲存層");
        ProductBookCase_Bean.setProductLevel(SystemSetting_Enum.ProductLevel.商品儲存層);
        String NewProductArea = "單元測試儲存層", NewProductFloor = "單元測試儲存區";
        boolean status = SystemSetting_Model.modifyProductSave(ProductBookCase_Bean,NewProductArea,NewProductFloor);
        assertTrue(status);
    }

    @Test
    @Order(11)
    void deleteProductSave() {
        ProductBookCase_Bean ProductBookCase_Bean = new ProductBookCase_Bean();
        ProductBookCase_Bean.setProductArea("單元測試儲存區");
        ProductBookCase_Bean.setProductFloor("單元測試儲存層");
        ProductBookCase_Bean.setProductLevel(SystemSetting_Enum.ProductLevel.商品儲存層);
        boolean status = SystemSetting_Model.deleteProductSave(ProductBookCase_Bean);
        assertTrue(status);
    }

    @Test
    void insertSystemSettingConfig() {
        SystemSettingConfig SystemSettingConfig = SystemSetting_Enum.SystemSettingConfig.客戶位數;
        String configValue = "10";
        boolean status = SystemSetting_Model.insertSystemSettingConfig(SystemSettingConfig,configValue);
        assertTrue(status);
    }

    @Test
    void updateSystemSettingConfigValue() {
        SystemSettingConfig SystemSettingConfig = SystemSetting_Enum.SystemSettingConfig.地區客戶位數;
        String configValue = "6";
        boolean status = SystemSetting_Model.updateSystemSettingConfigValue(SystemSettingConfig,configValue);
        assertTrue(status);
    }
    @Test
    void getSystemDefaultConfig(){
        SystemDefaultConfig_Enum.SystemDefaultName SystemDefaultName = SystemDefaultConfig_Enum.SystemDefaultName.Order_SearchFunction;
        Integer defaultConfig = SystemSetting_Model.getSystemDefaultConfig(SystemDefaultName);
        assertEquals(defaultConfig,1);
    }
    @Test
    void updateSystemDefaultConfig(){
        SystemDefaultConfig_Enum.SystemDefaultName SystemDefaultName = SystemDefaultConfig_Enum.SystemDefaultName.Order_SearchFunction;
        SystemDefaultConfig_Enum.Order_SearchFunction Order_SearchFunction = SystemDefaultConfig_Enum.Order_SearchFunction.ProductWaitingConfirm;
        boolean status = SystemSetting_Model.updateSystemDefaultConfig(SystemDefaultName.name(),Order_SearchFunction.ordinal());
        assertTrue(status);
    }
    @Test
    void saveFile_OutputPath() {
        String outputFile = "";
        boolean status = SystemSetting_Model.saveFile_OutputPath(outputFile);
        assertTrue(status);
    }

    @Test
    void isExistObject() {
        SystemSetting_Enum.ObjectStatus ObjectStatus = SystemSetting_Enum.ObjectStatus.地區客戶;
        boolean status = SystemSetting_Model.isExistObject(ObjectStatus);
        assertTrue(status);
    }

    @Test
    void isExistOrder() {
        boolean status = SystemSetting_Model.isExistOrder();
        assertTrue(status);
    }

    @Test
    void insertIAECrawlerBelong() {
        IAECrawlerBelong_Bean IAECrawlerBelong_Bean = new IAECrawlerBelong_Bean();
        IAECrawlerBelong_Bean.setBelongName("高雄科技大學");
        IAECrawlerBelong_Bean.setBelongURL("http://140.133.78.37:81");
        boolean status = SystemSetting_Model.insertIAECrawlerBelong(IAECrawlerBelong_Bean);
        assertTrue(status);
    }

    @Test
    void modifyIAECrawlerBelong() {
        IAECrawlerBelong_Bean IAECrawlerBelong_Bean = new IAECrawlerBelong_Bean();
        IAECrawlerBelong_Bean.setId(1);
        IAECrawlerBelong_Bean.setBelongName("高雄科技大學");
        IAECrawlerBelong_Bean.setBelongURL("http://140.133.78.37:80");
        boolean status = SystemSetting_Model.modifyIAECrawlerBelong(IAECrawlerBelong_Bean);
        assertTrue(status);
    }

    @Test
    void isIAECrawlerAccountBelongHaveThisBelong() {
        int id = 2;
        boolean status = SystemSetting_Model.isIAECrawlerAccountBelongHaveThisBelong(id);
        assertTrue(status);
    }

    @Test
    void deleteIAECrawlerBelong() {
        IAECrawlerBelong_Bean IAECrawlerBelong_Bean = new IAECrawlerBelong_Bean();
        IAECrawlerBelong_Bean.setId(1);
        IAECrawlerBelong_Bean.setBelongName("高雄科技大學");
        IAECrawlerBelong_Bean.setBelongURL("http://140.133.78.37:80");
        boolean status = SystemSetting_Model.deleteIAECrawlerBelong(IAECrawlerBelong_Bean);
        assertTrue(status);
    }

    @Test
    void getIAECrawlerBelong() {
        ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList = SystemSetting_Model.getIAECrawlerBelong();
        for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelongList){
            System.out.println(IAECrawlerBelong_Bean.getId() + " " + IAECrawlerBelong_Bean.getBelongName() + " " + IAECrawlerBelong_Bean.getBelongURL());
        }
        assertFalse(IAECrawlerBelongList.isEmpty());
    }

    @Test
    void isIAECrawlerAccount_ExportQuotation_ManufacturerNickNameExist() {
        String ManufacturerNickName = "",Account = "";
        boolean isNickName = SystemSetting_Model.isIAECrawlerAccount_ExportQuotation_ManufacturerNickNameExist(ManufacturerNickName,Account);
        assertFalse(isNickName);
    }

    @Test
    void isExistIAECrawlerAccount() {
        Integer account_id = null;
        String Account = "";
        boolean isAccountExist = SystemSetting_Model.isExistIAECrawlerAccount(account_id,Account);
        assertFalse(isAccountExist);
    }

    @Test
    void insertIAECrawlerAccount() {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = recordIAECrawlerAccount_Bean();
        boolean status = SystemSetting_Model.insertIAECrawlerAccount(IAECrawlerAccount_Bean);
        assertTrue(status);
    }
    private IAECrawlerAccount_Bean recordIAECrawlerAccount_Bean(){
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
        ObjectInfo_Bean.setId(132);
        ObjectInfo_Bean.setObjectID("202");
        IAECrawlerAccount_Bean.setObjectInfo_Bean(ObjectInfo_Bean);

        IAECrawlerAccount_Bean.setAccount("123");
        IAECrawlerAccount_Bean.setPassword("123");
        IAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName("b");
        IAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName("AAAAA");
        IAECrawlerAccount_Bean.setIAECrawlerBelongList(new ArrayList<>());
        IAECrawlerAccount_Bean.setExportQuotation(true);
        IAECrawlerAccount_Bean.setExportQuotation_defaultSelect(false);
        return IAECrawlerAccount_Bean;
    }
    @Test
    void modifyIAECrawlerAccount() {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = recordIAECrawlerAccount_Bean();
        boolean status = SystemSetting_Model.modifyIAECrawlerAccount(IAECrawlerAccount_Bean);
        assertTrue(status);
    }

    @Test
    void isExistBindingInvoice() {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
        IAECrawlerAccount_Bean.setAccount("82929272");
        boolean isExistBindingInvoice = SystemSetting_Model.isExistBindingInvoice(IAECrawlerAccount_Bean);
        System.out.println("出納帳號：82929272 是否已綁定發票 ? " + isExistBindingInvoice);
        assertTrue(isExistBindingInvoice);
    }

    @Test
    void isIAECrawlerAccountExistPayment() {
        String account = "26099246";
        boolean isIAECrawlerAccountExistPayment = SystemSetting_Model.isIAECrawlerAccountExistPayment(account);
        System.out.println("出納帳號：26099246 是否存在出納資料 ? " + isIAECrawlerAccountExistPayment);
        assertTrue(isIAECrawlerAccountExistPayment);
    }

    @Test
    void deleteIAECrawlerAccount() {
        IAECrawlerAccount_Bean IAECrawlerAccount_Bean = recordIAECrawlerAccount_Bean();
        boolean status = SystemSetting_Model.deleteIAECrawlerAccount(IAECrawlerAccount_Bean);
        assertTrue(status);
    }

    @Test
    void getIAECrawlerAccount() {
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAECrawlerAccount(SystemSetting_Model.getIAECrawlerBelong());
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            System.out.println(IAECrawlerAccount_Bean.getId() + "  " + IAECrawlerAccount_Bean.getAccount() + "  " + IAECrawlerAccount_Bean.getObjectID() + "  " + IAECrawlerAccount_Bean.getObjectName() + "  " + IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName() + "  " + IAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());
        }
        assertFalse(IAECrawlerAccountList.isEmpty());
    }
    @Test
    void getIAEAccount_ManufacturerNickName() {
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = SystemSetting_Model.getIAEAccount_ManufacturerNickName(false);
        for(IAECrawlerAccount_Bean IAECrawlerAccount_Bean : IAECrawlerAccountList){
            System.out.println(IAECrawlerAccount_Bean.getId() + "  " + IAECrawlerAccount_Bean.getObjectID() + "  " + IAECrawlerAccount_Bean.getObjectName() + "  " + IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName() + "  " + IAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());
        }
        assertFalse(IAECrawlerAccountList.isEmpty());
    }
    @Test
    void insertIpCam(){
        IpCamInfo_Bean IpCamInfo_Bean = new IpCamInfo_Bean();
        IpCamInfo_Bean.setDefaultPreview(false);
        IpCamInfo_Bean.setName("");
        IpCamInfo_Bean.setRTSP("");
        assertTrue(SystemSetting_Model.insertIpCam(IpCamInfo_Bean));
    }
    @Test
    void modifyIpCam(){
        IpCamInfo_Bean IpCamInfo_Bean = new IpCamInfo_Bean();
        IpCamInfo_Bean.setId(1);
        boolean default_preview = false;
        String modifyName = "";
        String modifyRTSP = "";
        assertTrue(SystemSetting_Model.modifyIpCam(IpCamInfo_Bean.getId(),default_preview,modifyName,modifyRTSP));
    }
    @Test
    void deleteIpCam(){
        IpCamInfo_Bean IpCamInfo_Bean = new IpCamInfo_Bean();
        IpCamInfo_Bean.setId(1);
        IpCamInfo_Bean.setName("");
        IpCamInfo_Bean.setRTSP("");
        assertTrue(SystemSetting_Model.deleteIpCam(IpCamInfo_Bean));
    }
    @Test
    void getIpCam(){
        ObservableList<IpCamInfo_Bean> ipCamList = SystemSetting_Model.getIpCamera();
        assertFalse(ipCamList.isEmpty());
    }
}