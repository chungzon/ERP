package ERP.Model.ManageProductCategory;

import ERP.Bean.ManageProductCategory.ProductCategory_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ManageProductCategory_ModelTest {
    private ManageProductCategory_Model ManageProductCategory_Model;
    public ManageProductCategory_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);

        ERPApplication.ToolKit = new ToolKit();
        this.ManageProductCategory_Model = ERPApplication.ToolKit.ModelToolKit.getManageProductCategoryModel();
    }
    @Test
    void getNewestProductCategoryID() {
        Product_Enum.CategoryLayer CategoryLayer = Product_Enum.CategoryLayer.中類別;
        String newestCategoryID = ManageProductCategory_Model.getNewestProductCategoryID(CategoryLayer);
        assertEquals(newestCategoryID,"21");
    }

    @Test
    void isCategoryExist() {
        Product_Enum.CategoryLayer CategoryLayer = Product_Enum.CategoryLayer.大類別;
        String categoryID = "01";
        boolean isExist = ManageProductCategory_Model.isCategoryExist(categoryID,CategoryLayer);
        assertTrue(isExist);
    }

    @Test
    void insertProductCategory() {
        ProductCategory_Bean ProductCategory_Bean = new ProductCategory_Bean();
        ProductCategory_Bean.setCategoryID("");
        ProductCategory_Bean.setCategoryName("");
        ProductCategory_Bean.setCategoryLayer(Product_Enum.CategoryLayer.大類別);

        boolean status = ManageProductCategory_Model.insertProductCategory(ProductCategory_Bean);
        assertTrue(status);
    }

    @Test
    void modifyProductCategory() {
        String categoryName = "";
        ProductCategory_Bean ProductCategory_Bean = new ProductCategory_Bean();
        ProductCategory_Bean.setCategoryID("");
        ProductCategory_Bean.setCategoryLayer(Product_Enum.CategoryLayer.大類別);

        boolean status = ManageProductCategory_Model.modifyProductCategory(categoryName,ProductCategory_Bean);
        assertTrue(status);
    }

    @Test
    void deleteProductCategory() {
        ProductCategory_Bean ProductCategory_Bean = new ProductCategory_Bean();
        ProductCategory_Bean.setCategoryID("04");
        ProductCategory_Bean.setCategoryLayer(Product_Enum.CategoryLayer.大類別);
        boolean status = ManageProductCategory_Model.deleteProductCategory(ProductCategory_Bean);
        assertTrue(status);
    }

    @Test
    void isCategoryExistProduct() {
        ProductCategory_Bean ProductCategory_Bean = new ProductCategory_Bean();
        ProductCategory_Bean.setCategoryLayer(Product_Enum.CategoryLayer.大類別);
        ProductCategory_Bean.setCategoryID("08");
        boolean isCategoryExistProduct = ManageProductCategory_Model.isCategoryExistProduct(ProductCategory_Bean);
        assertTrue(isCategoryExistProduct);
    }

    @Test
    void searchProductCategory() {
        int StartCategoryID = 04, EndCategoryID = 07;
        Product_Enum.CategoryLayer CategoryLayer = Product_Enum.CategoryLayer.大類別;
        ObservableList<ProductCategory_Bean> productCategoryList = ManageProductCategory_Model.searchProductCategory(CategoryLayer,StartCategoryID,EndCategoryID);
        System.out.println(productCategoryList.size());
        assertFalse(productCategoryList.isEmpty());
    }

    @Test
    void printProductCategory() {
        ObservableList<ProductCategory_Bean> productCategoryList = FXCollections.observableArrayList();
        String StartCategoryID = "04", EndCategoryID = "07";
        ManageProductCategory_Model.printProductCategory(productCategoryList,StartCategoryID,EndCategoryID);
    }
}