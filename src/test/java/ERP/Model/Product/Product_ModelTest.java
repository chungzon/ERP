package ERP.Model.Product;

import ERP.Bean.Product.ManageProductOnShelf_Bean;
import ERP.Bean.Product.ProductInfo_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ToolKit;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Product_ModelTest {
    private CallConfig CallConfig;
    private Product_Model Product_Model;
    public Product_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);

        ERPApplication.ToolKit = new ToolKit();
        this.CallConfig = ERPApplication.ToolKit.CallConfig;
        this.Product_Model = ERPApplication.ToolKit.ModelToolKit.getProductModel();
    }
    @Test
    void generateNewestISBN() {
        String ISBN = "78102";
//        String ISBN = "77404";
        ISBN = Product_Model.generateNewestISBN(ISBN);
//        System.out.println("廠商編號:774  大類別:04(行動通訊類)  generate ISBN：" + ISBN);
//        assertEquals(ISBN,"7740400000011");
        System.out.println("廠商編號:781  大類別:02(客戶訂製套表類)  generate ISBN：" + ISBN);
        Assertions.assertEquals(ISBN,"7810200000045");
    }
    @Test
    void isISBNVerifyCorrect() throws Exception {
        boolean KeyinDateSearch = true;
        JSONObject AdvancedSearchMap = CallConfig.getManageProductInfoJsonConfig_AdvancedSearch();
        String KeyWord = "電腦耗材", KeyinDate = "2019-01-01";
        KeyWord = "";
        ObservableList<ProductInfo_Bean> productList = Product_Model.SearchProduct(KeyWord,AdvancedSearchMap,KeyinDateSearch,KeyinDate);
        for(ProductInfo_Bean ProductInfo_Bean : productList){
            System.out.print("ISBN：" + ProductInfo_Bean.getISBN() + "  ");
            Product_Model.isISBNVerifyCorrect(ProductInfo_Bean.getISBN());
        }
    }
    @Test
    void getManufacturerNameOfProduct() {
        String VendorCode = "776";
        String ManufacturerName = Product_Model.getManufacturerNameOfProduct(VendorCode);
        assertEquals(ManufacturerName, "精技電腦股份有限公司");
    }

    @Test
    void getCategoryName() {
        Product_Enum.CategoryLayer CategoryLayer = Product_Enum.CategoryLayer.大類別;
        String CategoryID = "06";
        String VendorFullName = Product_Model.getCategoryName(CategoryID,CategoryLayer);
        assertEquals(VendorFullName,"傳真機耗材類");
    }

    @Test
    void getAllBookCase() {
        HashMap<String, ArrayList<String>> bookcaseList = Product_Model.getAllBookCase();
        assertFalse(bookcaseList.isEmpty());
    }

    @Test
    void searchProduct() throws Exception {
        boolean KeyinDateSearch = false;
        JSONObject AdvancedSearchMap = CallConfig.getManageProductInfoJsonConfig_AdvancedSearch();
        String KeyWord = "電腦耗材", KeyinDate = "2021-01-01";
        System.out.println("品名：單元測試–修改品名");
        KeyWord = "778";
        ObservableList<ProductInfo_Bean> productList = Product_Model.SearchProduct(KeyWord,AdvancedSearchMap,KeyinDateSearch,KeyinDate);
        for(ProductInfo_Bean ProductInfo_Bean : productList){
            System.out.println(ProductInfo_Bean.getISBN() + "  " + ProductInfo_Bean.getProductName() + "  " + ProductInfo_Bean.getPurchaseDate() + "  " + ProductInfo_Bean.getSaleDate());
        }
        assertFalse(productList.isEmpty());
    }
    @Test
    void getStoreBatchPriceByID(){
        int store_id = 1;
        double batchPrice = Product_Model.getStoreBatchPriceByID(store_id);
        assertEquals(batchPrice,110.5);
    }
    @Test
    void isISBNExist() {
        String ISBN = "";
        boolean isISBNExist = Product_Model.isISBNExist(ISBN,false);
        assertTrue(isISBNExist);
    }
    @Test
    void insertProduct() {
        ProductInfo_Bean ProductInfo_Bean = RecordProductInfo();
        boolean deleteCheckStore = false;
        boolean status = Product_Model.insertProduct(ProductInfo_Bean,deleteCheckStore,false);
        System.out.println("新增商品 [單元測試–品名] : " + status);
        assertTrue(status);
    }
    @Test
    void modifyProduct() {
        ProductInfo_Bean ProductInfo_Bean = RecordProductInfo();
        boolean deleteCheckStore = false;
        boolean isTestDB = false;
        boolean status = Product_Model.modifyProduct(ProductInfo_Bean, deleteCheckStore,isTestDB);
        System.out.println("修改商品名稱 [單元測試–品名 → 單元測試–修改品名] : " + status);
        assertTrue(status);
    }
    private ProductInfo_Bean RecordProductInfo(){
        ProductInfo_Bean ProductInfo_Bean = new ProductInfo_Bean();
        ProductInfo_Bean.setISBN("2180700000345");
//        ProductInfo_Bean.setISBN("7501100000028");
        ProductInfo_Bean.setInternationalCode("");
        ProductInfo_Bean.setFirmCode("");
        ProductInfo_Bean.setProductName("槍型紅外線攝影機");
        ProductInfo_Bean.setUnit("支");
        ProductInfo_Bean.setVendorCode("218");
        ProductInfo_Bean.setVendorName("利凌企業");
        ProductInfo_Bean.setFirstCategory("07");
        ProductInfo_Bean.setSecondCategory("");
        ProductInfo_Bean.setThirdCategory("");
        ProductInfo_Bean.setBatchPrice("450");
        ProductInfo_Bean.setSinglePrice("460");
        ProductInfo_Bean.setPricing("470");
        ProductInfo_Bean.setVipPrice1("455");
        ProductInfo_Bean.setVipPrice2("465");
        ProductInfo_Bean.setVipPrice3("475");
        ProductInfo_Bean.setInStock("0");
        ProductInfo_Bean.setSafetyStock("0");
        ProductInfo_Bean.setDiscount(1.0);
        ProductInfo_Bean.setInventoryDate(null);
        ProductInfo_Bean.setPurchaseDate(null);
        ProductInfo_Bean.setSaleDate(null);
        ProductInfo_Bean.setShipmentDate(null);
        ProductInfo_Bean.setUpdateDate(null);
        if(ProductInfo_Bean.getDescribe() == null)
            ProductInfo_Bean.setDescribe("");
        ProductInfo_Bean.setRemark("");

        ProductInfo_Bean.setProductArea(null);
        ProductInfo_Bean.setProductFloor(null);
        if(ProductInfo_Bean.getRutenCategory() == null)
            ProductInfo_Bean.setRutenCategory("");
        if(ProductInfo_Bean.getYahooCategory() == null)
            ProductInfo_Bean.setYahooCategory("");

        ProductInfo_Bean.setPicture1("");
        ProductInfo_Bean.setPicture2("");
        ProductInfo_Bean.setPicture3("");

        ObservableList<String> productTagList = FXCollections.observableArrayList();
//        productTagList.add("單元測試-門禁");
//        productTagList.add("單元測試-卡機");
        ProductInfo_Bean.setProductTag(productTagList);
        return ProductInfo_Bean;
    }
    @Test
    void isProductExistInOrder() {
        String ISBN = "2020200000099";
        boolean isProductExistInOrder = Product_Model.isProductExistInOrder(ISBN);
        System.out.println(isProductExistInOrder);
        assertTrue(isProductExistInOrder);
    }

    @Test
    void deleteProduct() {
        String ISBN = "2020200000099";
        boolean status = Product_Model.deleteProduct(ISBN);
        System.out.println("刪除商品 [單元測試–修改品名] : " + status);
        assertTrue(status);
    }

    @Test
    void getProductTag() {
        ProductInfo_Bean ProductInfo_Bean = RecordProductInfo();
        ProductInfo_Bean.setProductTag(Product_Model.getProductTag(ProductInfo_Bean.getISBN(), ProductInfo_Bean.getFirmCode()));
        ObservableList<String> productTag = ProductInfo_Bean.getProductTag();
        for(String tag : productTag){
            System.out.println(tag);
        }
        assertFalse(productTag.isEmpty());
    }

    @Test
    void getProductWaitingOnShelf() {
        ObservableList<ManageProductOnShelf_Bean> productWaitingOnShelfList = Product_Model.getProductWaitingOnShelf();
        for(ManageProductOnShelf_Bean ManageProductOnShelf_Bean : productWaitingOnShelfList){
            System.out.println(ManageProductOnShelf_Bean.getISBN() + "  " + ManageProductOnShelf_Bean.getProductName());
        }
        assertFalse(productWaitingOnShelfList.isEmpty());
    }

    @Test
    void getProductAlreadyOnShelf() {
    }

    @Test
    void getProductAlreadyOffShelf() {
    }

    @Test
    void getProductOnlineWaitingOffShelf() {
    }

    @Test
    void getProductOnlineWaitingUpdate() {
    }

    @Test
    void insertWaitingOnShelf() {
        ProductInfo_Bean ProductInfo_Bean = RecordProductInfo();
        boolean status = Product_Model.insertWaitingOnShelf(ProductInfo_Bean);
        assertTrue(status);
    }

    @Test
    void updateWaitingOnShelfAndShippingFee() {
        ManageProductOnShelf_Bean ManageProductOnShelf_Bean = recordProductOnShelfSetting();
        boolean status = Product_Model.updateWaitingOnShelfAndShippingFee(ManageProductOnShelf_Bean);
        assertTrue(status);
    }
    private ManageProductOnShelf_Bean recordProductOnShelfSetting(){
        ManageProductOnShelf_Bean ManageProductOnShelf_Bean = new ManageProductOnShelf_Bean();
        ManageProductOnShelf_Bean.setISBN("2020200000099");
        ManageProductOnShelf_Bean.setPackageLength(20);
        ManageProductOnShelf_Bean.setPackageWidth(16);
        ManageProductOnShelf_Bean.setPackageHeight(8);
        ManageProductOnShelf_Bean.setPackageWeight(4.6);

        ManageProductOnShelf_Bean.setPrepareOrder(false);
        ManageProductOnShelf_Bean.setDayOfPrepare(8);

        ManageProductOnShelf_Bean.setAllowBlackCat(true);
        ManageProductOnShelf_Bean.setAllowSevenEleven(true);
        ManageProductOnShelf_Bean.setAllowFamily(true);
        ManageProductOnShelf_Bean.setAllowHiLife(true);

        ManageProductOnShelf_Bean.setBlackCatOwnExpense(false);
        ManageProductOnShelf_Bean.setBlackCatShippingFee(90);
        ManageProductOnShelf_Bean.setSevenElvenOwnExpense(true);
        ManageProductOnShelf_Bean.setSevenElevenShippingFee(60);
        ManageProductOnShelf_Bean.setFamilyOwnExpense(false);
        ManageProductOnShelf_Bean.setFamilyShippingFee(60);
        ManageProductOnShelf_Bean.setHiLifeOwnExpense(true);
        ManageProductOnShelf_Bean.setHiLifeShippingFee(70);
        return ManageProductOnShelf_Bean;
    }
    /*
    @Test
    void deleteWaitingOnShelf() {
        try {
            DataBaseQuery("delete PreparativeProduction");
        } catch (Exception Ex) {
            Assertions.fail(Ex);
        }
    }*/
    @Test
    void applyProductBidCategory() {
    }

    @Test
    void getProductBidCategory() {
    }

    @Test
    void saveProductPicture() {
        ProductInfo_Bean ProductInfo_Bean = new ProductInfo_Bean();
        boolean status = Product_Model.saveProductPicture(ProductInfo_Bean);
        assertTrue(status);
    }

    @Test
    void updateProductInStock() {
        String ISBN = "2020200000099";
        int newInStock = 1;
        boolean status = Product_Model.updateProductInStock(ISBN,newInStock);
        assertTrue(status);
    }

    @Test
    void getSpecificationProductName() {
        String ISBN = "2020200000099";
        ObservableList<String> specificationProductNameList = Product_Model.getSpecificationProductName(ISBN);
        for(String specificationProductName : specificationProductNameList){
            System.out.println(specificationProductName);
        }
        assertFalse(specificationProductNameList.isEmpty());
    }
    @Test
    void insertSpecificationProductName() {
        String ISBN = "2020200000099", productName = "單元測試 - 規格名稱";
        boolean status = Product_Model.insertSpecificationProductName(ISBN,productName);
        assertTrue(status);
    }

    @Test
    void modifySpecificationProductName() {
        String wannaProductName = "單元測試 - 欲變更規格名稱", ISBN = "2020200000099", originProductName = "單元測試 - 規格名稱";
        boolean status = Product_Model.modifySpecificationProductName(ISBN,originProductName,wannaProductName);
        assertTrue(status);
    }

    @Test
    void deleteSpecificationProductName() {
        String ISBN = "2020200000099", productName = "單元測試 - 欲變更規格名稱";
        boolean status = Product_Model.deleteSpecificationProductName(ISBN,productName);
        assertTrue(status);
    }

    @Test
    void getSpecificationContent() {
        String ISBN = "2020200000011";
        String specificationContent = Product_Model.getSpecificationContent(ISBN);
        System.out.println("specificationContent = " + specificationContent);
    }

    @Test
    void saveSpecificationContent() {
        String ISBN = "2020200000011", specificationContent = "※ 商品規格內容";
        boolean status = Product_Model.saveSpecificationContent(ISBN,specificationContent);
        assertTrue(status);
    }
}