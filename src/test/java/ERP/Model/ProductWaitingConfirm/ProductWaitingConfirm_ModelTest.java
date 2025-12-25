package ERP.Model.ProductWaitingConfirm;

import ERP.Bean.ProductWaitConfirm.*;
import ERP.Bean.ToolKit.BigGoFilter.BigGoFilter_Bean;
import ERP.Controller.ProductWaitingConfirm.ProductWaitingConfirm.ProductWaitingConfirm_Controller;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ToolKit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductWaitingConfirm_ModelTest {
    private ProductWaitingConfirm_Model ProductWaitingConfirm_Model;
    private CallConfig CallConfig;
    public ProductWaitingConfirm_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);
        ERPApplication.ToolKit = new ToolKit();
        this.CallConfig = ERPApplication.ToolKit.CallConfig;
        this.ProductWaitingConfirm_Model = ERPApplication.ToolKit.ModelToolKit.getProductWaitingConfirmModel();
    }
    @Test
    void getCheckStoreVendor() {
        ObservableList<Vendor_Bean> VendorList = ProductWaitingConfirm_Model.getCheckStoreVendor();
        VendorList.add(new Vendor_Bean(0,""));
        Assertions.assertEquals(VendorList.size(),8);
    }

    @Test
    void getVendorCategory() {
        Vendor_Bean VendorSelectItem = ProductWaitingConfirm_Model.getCheckStoreVendor().get(1);
        Product_Enum.CategoryLayer CategoryLayer = Product_Enum.CategoryLayer.中類別;
        VendorCategory_Bean FirstCategorySelectItem = new VendorCategory_Bean(null,"","22");
        VendorCategory_Bean SecondCategorySelectItem = null;

        ObservableList<VendorCategory_Bean> CategoryBeanList = ProductWaitingConfirm_Model.getVendorCategory(VendorSelectItem, Product_Enum.CategoryLayer.小類別, FirstCategorySelectItem, SecondCategorySelectItem);
        System.out.println(CategoryBeanList);
        assertFalse(CategoryBeanList.isEmpty());
    }
    @Test
    void productConditionalSearch() {
        ConditionalDBSearch_Bean ConditionalDBSearch_Bean = generateConditionalDBSearch();
        ObservableList<WaitConfirmProductInfo_Bean> WaitConfirmProductList = ProductWaitingConfirm_Model.ProductConditionalSearch(ConditionalDBSearch_Bean);
        System.out.println(WaitConfirmProductList.size());
        assertFalse(WaitConfirmProductList.isEmpty());
    }
    private ConditionalDBSearch_Bean generateConditionalDBSearch(){
        ConditionalDBSearch_Bean ConditionalDBSearch_Bean = new ConditionalDBSearch_Bean();
        ConditionalDBSearch_Bean.setWaitConfirmStatus(Product_Enum.WaitConfirmStatus.新增);
        ConditionalDBSearch_Bean.setWaitConfirmTable(Product_Enum.WaitConfirmTable.進銷存商品);

        ConditionalDBSearch_Bean.setVendor(ProductWaitingConfirm_Model.getCheckStoreVendor().get(4));
        ConditionalDBSearch_Bean.setFirstCategory(null);
        ConditionalDBSearch_Bean.setSecondCategory(null);
        ConditionalDBSearch_Bean.setThirdCategory(null);
        ConditionalDBSearch_Bean.setSinglePrice_Minus_BatchPrice("");
        ConditionalDBSearch_Bean.setProductName("");
        ConditionalDBSearch_Bean.setPriceIncludeTax(false);
        return ConditionalDBSearch_Bean;
    }
    @Test
    void getBigGoFilter() {
       HashMap<Product_Enum.BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap = ProductWaitingConfirm_Model.getBigGoFilter();
       for(Product_Enum.BigGoFilterSource BigGoFilterSource : bigGoFilterMap.keySet()){
           for(BigGoFilter_Bean BigGoFilter_Bean : bigGoFilterMap.get(BigGoFilterSource)) {
               System.out.println(BigGoFilter_Bean.getSource() + " " + BigGoFilter_Bean.getStoreName());
           }
       }
        assertFalse(bigGoFilterMap.isEmpty());
    }

    @Test
    void saveBigGoFilterWhichSelected() {
        HashMap<Product_Enum.BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap = ProductWaitingConfirm_Model.getBigGoFilter();
//        boolean status = ProductWaitingConfirm_Model.saveBigGoFilterWhichSelected(ProductWaitingConfirm_Model.getBigGoFilter());
//        assertTrue(status);
    }

    @Test
    void conditionalSearchForBigGo() {
        HashMap<Product_Enum.BigGoFilterSource, BigGoSearchResult_Bean> bigGoSearchResultMap = new HashMap<>();
        bigGoSearchResultMap.put(Product_Enum.BigGoFilterSource.未篩選, new BigGoSearchResult_Bean(new VBox()));
        bigGoSearchResultMap.put(Product_Enum.BigGoFilterSource.商城, new BigGoSearchResult_Bean(new VBox()));
        bigGoSearchResultMap.put(Product_Enum.BigGoFilterSource.拍賣, new BigGoSearchResult_Bean(new VBox()));

        ConditionalBigGoSearch_Bean ConditionalBigGoSearch_Bean = generateConditionalBigGoSearch();

        ProductWaitingConfirm_Model.ConditionalSearchForBigGo(
                new ProductWaitingConfirm_Controller(),
                bigGoSearchResultMap,
                ConditionalBigGoSearch_Bean);
        /*HashMap<String, ArrayList<BigGoProductInfo_Bean>> BigGoProductInfo = ProductWaitingConfirm_Model.ConditionalSearchForBigGo(new ProductWaitingConfirm_Controller(),
                BigGoFilterSource,
                ConditionalBigGoSearch_Bean,
                new BigGoSearchResult_Bean(new VBox()));
        for(String searchUrl : BigGoProductInfo.keySet()){
            System.out.println("searchUrl = " + searchUrl + "  ↓ ↓ ↓ ↓");
            ArrayList<BigGoProductInfo_Bean> bigGoProductList = BigGoProductInfo.get(searchUrl);
            for(BigGoProductInfo_Bean BigGoProductInfo_Bean : bigGoProductList){
                System.out.println(BigGoProductInfo_Bean.getProductName());
            }
            System.out.println("--------------------------------------");
        }
        assertFalse(BigGoProductInfo.isEmpty());*/
    }
    private ConditionalBigGoSearch_Bean generateConditionalBigGoSearch(){
        ConditionalBigGoSearch_Bean ConditionalBigGoSearch_Bean = new ConditionalBigGoSearch_Bean();
        ConditionalBigGoSearch_Bean.setBigGoSearchText("電腦");
        ConditionalBigGoSearch_Bean.setBatchPrice(9999);
        ConditionalBigGoSearch_Bean.setGreaterThanBatchPrice(false);
        ConditionalBigGoSearch_Bean.setSortByLargeToSmall(false);

        HashMap<Product_Enum.BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap = new HashMap<>();
        for(Product_Enum.BigGoFilterSource bigGoFilterSource : Product_Enum.BigGoFilterSource.values()){
            bigGoFilterMap.put(bigGoFilterSource, FXCollections.observableArrayList());
        }
        ConditionalBigGoSearch_Bean.setBigGoFilterMap(bigGoFilterMap);
        return ConditionalBigGoSearch_Bean;
    }
    @Test
    void downloadPicture() {
        String outfilePath = CallConfig.getFile_OutputPath();
        ImageView imageView = new ImageView();
        boolean status = ProductWaitingConfirm_Model.downloadPicture(imageView.getImage(),outfilePath);
        assertTrue(status);
    }

    @Test
    void saveEditProductDescribeOrRemark() {
        Product_Enum.EditProductDescribeOrRemark EditProductDescribeOrRemark = Product_Enum.EditProductDescribeOrRemark.編輯商品備註;
        WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = new WaitConfirmProductInfo_Bean();
        WaitConfirmProductInfo_Bean.setWaitConfirmTable(Product_Enum.WaitConfirmTable.待確認商品);
        WaitConfirmProductInfo_Bean.setProductCode("00184");
        WaitConfirmProductInfo_Bean.setISBN("7750700000017");
        String text = "單元測試-編輯商品備註";

        boolean status = ProductWaitingConfirm_Model.saveEditProductDescribeOrRemark(EditProductDescribeOrRemark,text,WaitConfirmProductInfo_Bean);
        assertTrue(status);
    }

    @Test
    void deleteCheckStoreProduct() {
        String productCode = "";
        boolean status = ProductWaitingConfirm_Model.deleteCheckStoreProduct(productCode);
        assertTrue(status);
    }

    @Test
    void archiveCheckStoreProduct() {
        String productCode = "";
        boolean status = ProductWaitingConfirm_Model.archiveCheckStoreProduct(productCode);
        assertTrue(status);
    }

    @Test
    void ignoreCheckStoreProduct() {
        String productCode = "";
        Product_Enum.WaitConfirmStatus WaitConfirmStatus = Product_Enum.WaitConfirmStatus.新增;
        boolean status = ProductWaitingConfirm_Model.ignoreCheckStoreProduct(productCode,WaitConfirmStatus);
        assertTrue(status);
    }

    @Test
    void recoverCheckStoreProduct() {
        int previousStatus = 0;
        String productCode = "";
        boolean status = ProductWaitingConfirm_Model.recoverCheckStoreProduct(productCode,previousStatus);
        assertTrue(status);
    }

    @Test
    void getProductUrl() {
        String productCode = "ACEU2100";
        String productUrl = ProductWaitingConfirm_Model.getProductUrl(productCode);
        System.out.println(productUrl);
        assertEquals(productUrl,"https://www.xander.com.tw/b2b/item/?id=74D9R960IGIY6XR");
    }

    @Test
    void getProductPicture() {
        WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = new WaitConfirmProductInfo_Bean();
        WaitConfirmProductInfo_Bean.setWaitConfirmTable(Product_Enum.WaitConfirmTable.進銷存商品);
        WaitConfirmProductInfo_Bean.setProductCode("");
        WaitConfirmProductInfo_Bean.setISBN("");

        ProductWaitingConfirm_Model.getProductPicture(WaitConfirmProductInfo_Bean);
        System.out.println(WaitConfirmProductInfo_Bean.getPicture1());
        System.out.println(WaitConfirmProductInfo_Bean.getPicture2());
        System.out.println(WaitConfirmProductInfo_Bean.getPicture3());
    }
}
