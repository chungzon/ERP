package ERP.Model.Product;

import ERP.Bean.Product.ProductInfo_Bean;
import ERP.Bean.Product.ManageProductOnShelf_Bean;
import ERP.Bean.Product.ShopeeCategory_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
import ERP.Enum.Product.Product_Enum.BidStore;
import ERP.ToolKit.ToolKit;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
/** [ERP.Model] Product */
public class Product_Model {
    private ToolKit ToolKit;
    private BidCategorySetting_Model BidCategorySetting_Model;
    public Product_Model(){
        ToolKit = ERPApplication.ToolKit;
        this.BidCategorySetting_Model = ToolKit.ModelToolKit.getBidCategorySettingModel();
    }
//    public BidCategorySetting_Model getBidCategorySetting_Model(){  return BidCategorySetting_Model;    }

    /** Generate newest ISBN of product
     * @param ISBN the ISBN of product
     * */
    public String generateNewestISBN(String ISBN){
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select max(ISBN) from Store where ISBN like ?");
            PreparedStatement.setString(1,ISBN.substring(0,5) +  "%");
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                if (Rs.getObject(1) != null) {
                    ISBN = Rs.getString(1).substring(0, 12);
                    ISBN = Long.toString(Long.parseLong(ISBN) + 1);
                } else  ISBN = ISBN.substring(0, 5) + "0000001";
            } else  ISBN = ISBN.substring(0, 5) + "0000001";
            int even_sum = 0, odd_sum = 0;
            for (int i = 0; i < ISBN.length(); i++) {
                if (i % 2 == 0) odd_sum += ISBN.charAt(i) - 48;
                else    even_sum += ISBN.charAt(i) - 48;
            }
            ISBN = ISBN + (10 - (even_sum * 3 + odd_sum) % 10) % 10;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ISBN;
    }
    public boolean isISBNVerifyCorrect(String ISBN){
        String verifyString = ISBN.substring(0,12);
        int checkNumber = Integer.parseInt(ISBN.substring(12));
        int sum = 0;
        for(int index = 0 ; index < verifyString.length() ; index++){
            int number = Integer.parseInt(verifyString.substring(index,(index+1)));
            if((index+1)%2 != 0)
                sum = sum + number;
            else if((index+1)%2 == 0)
                sum = sum + (number*3);
        }
        int less = sum %10;
        if(less == 0) {
//            ERPApplication.Logger.warn("【驗證】ISBN：" + ISBN + "  總額：" + sum + "  餘數：" + less + "  檢查碼：" + checkNumber + "  " + (less == checkNumber));
            System.out.println("【驗證】ISBN：" + ISBN + "  總額：" + sum + "  餘數：" + less + "  檢查碼：" + checkNumber + "  " + (less == checkNumber));
            return (less == checkNumber);
        }else {
            System.out.println("【驗證】ISBN：" + ISBN + "  總額：" + sum + "  餘數：" + less + "  檢查碼：" + checkNumber + "  " + (10 - less == checkNumber));
//            ERPApplication.Logger.warn("【驗證】ISBN：" + ISBN + "  總額：" + sum + "  餘數：" + less + "  檢查碼：" + checkNumber + "  " + (10 - less == checkNumber));
            return (10 - less == checkNumber);
        }
    }
    /** Get manufacturer name of product
     * @param VendorCode the vendor code is the first three words of ISBN
     * */
    public String getManufacturerNameOfProduct(String VendorCode){
        String ManufacturerName = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select ObjectName from Manufacturer where ObjectID = ?");
            PreparedStatement.setString(1,VendorCode);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  ManufacturerName = Rs.getString(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ManufacturerName;
    }
    /** Get category name of product
     * @param CategoryID the category id of product is the fourth word to fifth word of ISBN
     * @param CategoryLayer the category layer of product
     * */
    public String getCategoryName(String CategoryID, CategoryLayer CategoryLayer){
        String VendorFullName = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select CategoryName from ProductCategory where CategoryID = ? and CategoryLayer = ?");
            PreparedStatement.setString(1,CategoryID);
            PreparedStatement.setInt(2,CategoryLayer.getLayer());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  VendorFullName = Rs.getString(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return VendorFullName;
    }
    /** Get all bookcase that be setting */
    public HashMap<String, ArrayList<String>> getAllBookCase(){
        HashMap<String,ArrayList<String>> BookCase = new HashMap<>();
        ArrayList<String> EmptyProductFloor = new ArrayList<>();
        EmptyProductFloor.add("");
        BookCase.put("", EmptyProductFloor);

        ArrayList<String> FloorList;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from BookCase");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                String ProductArea = Rs.getString("ProductArea");
                String ProductFloor = Rs.getString("ProductFloor");
                int Level = Rs.getInt("Level");
                if(Level == 0){
                    if(!BookCase.containsKey(ProductArea)){
                        FloorList = new ArrayList<>();
                        FloorList.add("");
                        BookCase.put(ProductArea,FloorList);
                    }
                }else if(Level == 1){
                    if(BookCase.containsKey(ProductArea))    BookCase.get(ProductArea).add(ProductFloor);
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return BookCase;
    }
    /** Search product
     * @param KeyWord the key word of search column
     * @param AdvancedSearchMap the Json of advanced search
     * @param KeyinDateSearch whether to search product by date
     * @param KeyinDate the key word of date
     * */
    public ObservableList<ProductInfo_Bean> SearchProduct(String KeyWord, JSONObject AdvancedSearchMap, boolean KeyinDateSearch, String KeyinDate){
        String Query;
        if(KeyinDateSearch && KeyWord.equals(""))   Query = "select A.*,B.*,C.*,D.InventoryDate,D.KeyinDate,D.UpdateDate,D.ShipmentDate,E.*,F.*," +
                "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL) as 'PurchaseDate'," +
                "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL) as 'SaleDate'," +
                "case when EXISTS(select 1 from PreparativeProduction where StoreId = A.id) then '1' else '0' end as isExistWaitingOnShelf," +
                "case when EXISTS(select 1 from ProductTag where ISBN = A.ISBN) then '1' else '0' end as isExistProductTag " +
                "from Store A inner join store_price B on A.id = B.store_id inner join store_category C on A.id = C.store_id inner join store_date D on A.id = D.store_id inner join ProductPicture E on A.id = E.store_id inner join ProductBookCase F on A.id = F.store_id where D.KeyinDate >= '" + KeyinDate +"'";
        else{
            ArrayList<Product_Enum.AdvancedSearchColumn> AdvancedSearchColumnList = getAdvancedSearchColumn(AdvancedSearchMap);
            Query = generateSearchProductQuery(KeyWord, AdvancedSearchColumnList);
            if(KeyinDateSearch) Query += " and D.KeyinDate >= '" + KeyinDate +"'";
            AdvancedSearchColumnList.clear();
        }
        ObservableList<ProductInfo_Bean> ObservableList = FXCollections.observableArrayList();
        ERPApplication.Logger.info(Query);
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ProductInfo_Bean ProductInfo_Bean = new ProductInfo_Bean();
                ProductInfo_Bean.setISBN(Rs.getString("ISBN"));
                ProductInfo_Bean.setInternationalCode(Rs.getString("InternationalCode"));
                ProductInfo_Bean.setFirmCode(Rs.getString("FirmCode"));
                ProductInfo_Bean.setProductName(Rs.getString("ProductName"));
                ProductInfo_Bean.setProductCode(Rs.getString("ProductCode"));
                ProductInfo_Bean.setUnit(Rs.getString("Unit"));
                ProductInfo_Bean.setBatchPrice(ToolKit.RoundingString(true,Rs.getDouble("BatchPrice")));
                ProductInfo_Bean.setSinglePrice(ToolKit.RoundingString(true,Rs.getDouble("SinglePrice")));
                ProductInfo_Bean.setPricing(ToolKit.RoundingString(true,Rs.getDouble("Pricing")));
                ProductInfo_Bean.setVipPrice1(ToolKit.RoundingString(true,Rs.getDouble("VipPrice1")));
                ProductInfo_Bean.setVipPrice2(ToolKit.RoundingString(true,Rs.getDouble("VipPrice2")));
                ProductInfo_Bean.setVipPrice3(ToolKit.RoundingString(true,Rs.getDouble("VipPrice3")));
                ProductInfo_Bean.setDiscount(Rs.getDouble("Discount"));
                ProductInfo_Bean.setBrand(Rs.getString("Brand"));
                ProductInfo_Bean.setDescribe(Rs.getString("Describe"));
                ProductInfo_Bean.setRemark(Rs.getString("Remark"));
                ProductInfo_Bean.setSupplyStatus(Rs.getString("SupplyStatus"));
                ProductInfo_Bean.setFirstCategory(Rs.getString("NewFirstCategory"));
                ProductInfo_Bean.setSecondCategory(Rs.getString("NewSecondCategory"));
                ProductInfo_Bean.setThirdCategory(Rs.getString("NewThirdCategory"));
                ProductInfo_Bean.setInStock(String.valueOf(Rs.getInt("InStock")));
                ProductInfo_Bean.setSafetyStock(String.valueOf(Rs.getInt("SafetyStock")));
                ProductInfo_Bean.setInventoryDate(Rs.getString("InventoryDate"));
                ProductInfo_Bean.setInventoryQuantity(Rs.getInt("InventoryQuantity"));
                ProductInfo_Bean.setVendorName(Rs.getString("Vendor"));
                ProductInfo_Bean.setVendorCode(Rs.getString("VendorCode"));
                ProductInfo_Bean.setKeyinDate(Rs.getString("KeyinDate"));
                ProductInfo_Bean.setUpdateDate(Rs.getString("UpdateDate"));
                ProductInfo_Bean.setPurchaseDate(Rs.getString("PurchaseDate"));
                ProductInfo_Bean.setSaleDate(Rs.getString("SaleDate"));
                ProductInfo_Bean.setShipmentDate(Rs.getString("ShipmentDate"));
                ProductInfo_Bean.setProductArea(Rs.getString("ProductArea"));
                ProductInfo_Bean.setProductFloor(Rs.getString("ProductFloor"));
                ProductInfo_Bean.setPicture1(Rs.getString("Picture1"));
                ProductInfo_Bean.setPicture2(Rs.getString("Picture2"));
                ProductInfo_Bean.setPicture3(Rs.getString("Picture3"));
                ProductInfo_Bean.setWaitingOnShelf(Rs.getBoolean("isExistWaitingOnShelf"));
                ProductInfo_Bean.setExistProductTag(Rs.getBoolean("isExistProductTag"));
//                ProductInfo_Bean.setWaitingOnShelf(isExistWaitingOnShelf(ProductInfo_Bean.getISBN()));
//                getProductTag(ProductInfo_Bean);
//                getProductBidCategory(ProductInfo_Bean);
                ObservableList.add(ProductInfo_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(()-> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ObservableList;
    }
    private ArrayList<Product_Enum.AdvancedSearchColumn> getAdvancedSearchColumn(JSONObject AdvancedSearchMap){
        ArrayList<Product_Enum.AdvancedSearchColumn> SearchColumnList = null;
        for(Product_Enum.AdvancedSearchColumn AdvancedSearchColumn : Product_Enum.AdvancedSearchColumn.values()){
            if(AdvancedSearchMap.getBoolean(AdvancedSearchColumn.name())){
                if(SearchColumnList == null)    SearchColumnList = new ArrayList<>();
                SearchColumnList.add(AdvancedSearchColumn);
            }
        }
        return SearchColumnList;
    }
    private String generateSearchProductQuery(String KeyWord, ArrayList<Product_Enum.AdvancedSearchColumn> AdvancedSearchColumnList){
        StringBuilder Query = new StringBuilder("select A.*,B.*,C.*,D.InventoryDate,D.KeyinDate,D.UpdateDate,D.ShipmentDate,E.*,F.*," +
                "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '0' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL) as 'PurchaseDate'," +
                "(select Max(AlreadyOrderDate) from (select F.AlreadyOrderDate from Orders_Items E inner join Orders F on E.Order_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null union select F.AlreadyOrderDate from SubBill_Items E inner join SubBill F on E.SubBill_id = F.id where E.ISBN = A.ISBN and F.OrderSource = '1' and F.status = '0' and F.AlreadyOrderNumber is not null) TABLE_ALL) as 'SaleDate'," +
                "case when EXISTS(select 1 from PreparativeProduction where StoreId = A.id) then '1' else '0' end as isExistWaitingOnShelf," +
                "case when EXISTS(select 1 from ProductTag where ISBN = A.ISBN) then '1' else '0' end as isExistProductTag " +
                "from Store A inner join store_price B on A.id = B.store_id inner join store_category C on A.id = C.store_id inner join store_date D on A.id = D.store_id inner join ProductPicture E on A.id = E.store_id inner join ProductBookCase F on A.id = F.store_id where (");
        for(int index = 0 ; index < AdvancedSearchColumnList.size() ; index++){
            if(AdvancedSearchColumnList.get(index) == Product_Enum.AdvancedSearchColumn.ISBN)   Query.append("A.ISBN like '").append(KeyWord).append("%'");
            if(AdvancedSearchColumnList.get(index) == Product_Enum.AdvancedSearchColumn.國際碼)   Query.append("A.InternationalCode = '").append(KeyWord).append("'");
            if(AdvancedSearchColumnList.get(index) == Product_Enum.AdvancedSearchColumn.商品碼)   Query.append("A.FirmCode like '%").append(KeyWord).append("%'");
            if(AdvancedSearchColumnList.get(index) == Product_Enum.AdvancedSearchColumn.品名){
                String[] keyWordSp = KeyWord.split(" ");
                if(keyWordSp.length > 1){
                    for(int j = 0 ; j < keyWordSp.length ; j++){
                        if(j == 0)  Query.append("A.ProductName like '%").append(keyWordSp[j]).append("%'");
                        else    Query.append(" or A.ProductName like '%").append(keyWordSp[j]).append("%'");
                    }
                }else
                    Query.append("A.ProductName like '%").append(keyWordSp[0]).append("%'");
            }
            if(AdvancedSearchColumnList.get(index) == Product_Enum.AdvancedSearchColumn.廠商編號)   Query.append("A.VendorCode = '").append(KeyWord).append("'");
            if(index != AdvancedSearchColumnList.size()-1)  Query.append(" or ");
        }
        return Query + ")";
    }
    public double getStoreBatchPriceByID(int store_id){
        double batchPrice = 0;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select BatchPrice from store_price where store_id = ?");
            PreparedStatement.setInt(1,store_id);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  batchPrice = Rs.getDouble(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ToolKit.RoundingDouble(batchPrice);
    }
    public boolean isISBNExist(String ISBN, boolean isTestDB){
        boolean isISBNExist = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = connect.prepareStatement("select 1 from store where ISBN = ?");
            PreparedStatement.setString(1,ISBN);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isISBNExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isISBNExist;
    }
    public boolean isProductCodeExist(String productCode){
        boolean isProductCodeExist = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select 1 from store where ProductCode = ?");
            PreparedStatement.setString(1,productCode);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isProductCodeExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isProductCodeExist;
    }
    public boolean insertProduct(ProductInfo_Bean ProductInfo_Bean, boolean deleteCheckStore, boolean isTestDB){
        boolean status = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "INSERT INTO store (ISBN,InternationalCode,FirmCode,ProductName,ProductCode,Unit,Brand,Describe,Remark,SupplyStatus,InStock,SafetyStock,InventoryQuantity,Vendor,VendorCode,Status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                    "insert into Store_price (store_id,BatchPrice,SinglePrice,Pricing,VipPrice1,VipPrice2,VipPrice3,Discount) values((select IDENT_CURRENT('Store')),?,?,?,?,?,?,?) " +
                    "insert into store_category (store_id,FirstCategory_Id,SecondCategory_Id,ThirdCategory_Id,NewFirstCategory,NewSecondCategory,NewThirdCategory) values((select IDENT_CURRENT('Store')),?,?,?,?,?,?) " +
                    "insert into store_date (store_id,InventoryDate,KeyinDate,UpdateDate,PurchaseDate,SaleDate,ShipmentDate) values((select IDENT_CURRENT('Store')),?,?,?,?,?,?)" +
                    "insert into ProductBookCase (store_id,ISBN,ProductArea,ProductFloor) values ((select IDENT_CURRENT('Store')),?,?,?) " +
                    "insert into ProductBidCategory (store_id,ISBN,YahooCategory,RutenCategory,ShopeeCategory) values ((select IDENT_CURRENT('Store')),?,?,?,?) " +
                    "insert into ProductPicture (store_id,ISBN,Picture1,Picture2,Picture3) values ((select IDENT_CURRENT('Store')),?,?,?,?) " +
                    "insert into ProductSaleStatus (store_id,ISBN,WaitingPurchaseQuantity,WaitingIntoInStock,NeededPurchaseQuantity,WaitingShipmentQuantity) values ((select IDENT_CURRENT('Store')),?,?,?,?,?) ";
            if(deleteCheckStore)
                Query = Query + "delete from CheckStore where ProductCode = ? ";

            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setString(1, ProductInfo_Bean.getISBN());                       // ISBN
            PreparedStatement.setString(2, ProductInfo_Bean.getInternationalCode());          // InternationalCode(國際碼)
            PreparedStatement.setString(3, ProductInfo_Bean.getFirmCode());                   // FirmCode(商品碼)
            PreparedStatement.setString(4, ProductInfo_Bean.getProductName());                // ProductName(品名)
            PreparedStatement.setString(5, ProductInfo_Bean.getProductCode() == null ? "" : ProductInfo_Bean.getProductCode());  // ProductCode(商品編號)
            PreparedStatement.setString(6, ProductInfo_Bean.getUnit());                       // Unit(單位)
            PreparedStatement.setString(7, "");                                           // Brand(廠牌)
            PreparedStatement.setString(8, ProductInfo_Bean.getDescribe());                  // Describe(商品描述)
            PreparedStatement.setString(9, ProductInfo_Bean.getRemark());                    // Remark(商品備註)
            PreparedStatement.setString(10, "");                                           // SupplyStatus(供貨狀態)
            PreparedStatement.setInt(11, Integer.parseInt(ProductInfo_Bean.getInStock()));                      // InStock(存量)
            PreparedStatement.setInt(12, Integer.parseInt(ProductInfo_Bean.getSafetyStock()));                  // SafetyStock(安全存量)
            PreparedStatement.setInt(13, 0);                                               // InventoryQuantity(盤點數量)
            PreparedStatement.setString(14, ProductInfo_Bean.getVendorName());                // Vendor(廠商)
            PreparedStatement.setString(15, String.valueOf(ProductInfo_Bean.getVendorCode())); // VendorCode(廠商編號)
            PreparedStatement.setInt(16, Product_Enum.StoreStatus.已確認.ordinal());// Status(狀態)

            PreparedStatement.setBigDecimal(17, new BigDecimal(ProductInfo_Bean.getBatchPrice()));             // BatchPrice(成本)
            PreparedStatement.setBigDecimal(18, new BigDecimal(ProductInfo_Bean.getSinglePrice()));            // SinglePrice(單價)
            PreparedStatement.setBigDecimal(19, new BigDecimal(ProductInfo_Bean.getPricing()));                // Pricing(定價)
            PreparedStatement.setBigDecimal(20, new BigDecimal(ProductInfo_Bean.getVipPrice1()));             // VipPrice1
            PreparedStatement.setBigDecimal(21, new BigDecimal(ProductInfo_Bean.getVipPrice2()));             // VipPrice2
            PreparedStatement.setBigDecimal(22, new BigDecimal(ProductInfo_Bean.getVipPrice3()));             // VipPrice3
            PreparedStatement.setBigDecimal(23, BigDecimal.valueOf(ProductInfo_Bean.getDiscount()));              // Discount(折扣數)

            PreparedStatement.setObject(24, ProductInfo_Bean.getVendorFirstCategory_id());    // FirstCategory(大類別)
            PreparedStatement.setObject(25, ProductInfo_Bean.getVendorSecondCategory_id());    // SecondCategory(中類別)
            PreparedStatement.setObject(26, ProductInfo_Bean.getVendorThirdCategory_id());    // ThirdCategory(小類別)
            PreparedStatement.setString(27, ProductInfo_Bean.getFirstCategory());             // NewFirstCategory(總類對應：大類別)
            PreparedStatement.setString(28, ProductInfo_Bean.getSecondCategory());            // NewSecondCategory(總類對應：中類別)
            PreparedStatement.setString(29, ProductInfo_Bean.getThirdCategory());             // NewThirdCategory(總類對應：小類別)

            PreparedStatement.setString(30, ProductInfo_Bean.getInventoryDate());                                           // InventoryDate(盤點日期)
            PreparedStatement.setString(31, ToolKit.getToday("yyyy-MM-dd"));                                      // KeyinDate
            PreparedStatement.setString(32, ProductInfo_Bean.getUpdateDate());                                           // UpdateDate
            PreparedStatement.setString(33, ProductInfo_Bean.getPurchaseDate());                                           // PurchaseDate(進貨日期)
            PreparedStatement.setString(34, ProductInfo_Bean.getSaleDate());                                           // SaleDate(最近銷售日期)
            PreparedStatement.setString(35, ProductInfo_Bean.getShipmentDate());                                           // ShipmentDate(出貨日期)

            PreparedStatement.setString(36, ProductInfo_Bean.getISBN());
            PreparedStatement.setString(37, ProductInfo_Bean.getProductArea());
            PreparedStatement.setString(38, ProductInfo_Bean.getProductFloor());

            PreparedStatement.setString(39, ProductInfo_Bean.getISBN());
            PreparedStatement.setString(40, ProductInfo_Bean.getYahooCategory());
            PreparedStatement.setString(41, ProductInfo_Bean.getRutenCategory());
            PreparedStatement.setString(42, ProductInfo_Bean.getShopeeCategory() == null ? "" : String.valueOf(ProductInfo_Bean.getShopeeCategory().getId()));

            PreparedStatement.setString(43, ProductInfo_Bean.getISBN());
            PreparedStatement.setString(44, ProductInfo_Bean.getPicture1() == null ? "" : ProductInfo_Bean.getPicture1());
            PreparedStatement.setString(45, ProductInfo_Bean.getPicture2() == null ? "" : ProductInfo_Bean.getPicture2());
            PreparedStatement.setString(46, ProductInfo_Bean.getPicture3() == null ? "" : ProductInfo_Bean.getPicture3());

            PreparedStatement.setString(47, ProductInfo_Bean.getISBN());
            PreparedStatement.setInt(48, 0);
            PreparedStatement.setInt(49, 0);
            PreparedStatement.setInt(50, 0);
            PreparedStatement.setInt(51, 0);
            if(deleteCheckStore)
                PreparedStatement.setString(52, ProductInfo_Bean.getProductCode());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(()-> DialogUI.ExceptionDialog(Ex));
        }finally {
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Update product in database
     * @param ProductInfo_Bean the bean of product
     * */
    public boolean modifyProduct(ProductInfo_Bean ProductInfo_Bean, boolean deleteCheckStore, boolean isTestDB){
        boolean status = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Store set InternationalCode = ?, FirmCode = ?, ProductName = ?, Unit = ?,SafetyStock = ?,Describe = ?, Remark = ? where ISBN = ? " +
                    "update store_category set NewSecondCategory = ?, NewThirdCategory = ? from Store A inner join store_category B on A.id = B.store_id where A.ISBN = ? " +
                    "update store_price set BatchPrice = ?, SinglePrice = ?, Pricing = ?, Discount = ?,VipPrice1 = ?, VipPrice2 = ?, VipPrice3 = ? from Store A inner join store_price B on A.id = B.store_id where A.ISBN = ? " +
                    "update ProductBookCase set ProductArea = ?, ProductFloor = ? from Store A inner join ProductBookCase B on A.id = B.store_id where A.ISBN = ? " +
                    "update ProductBidCategory set YahooCategory = ?, RutenCategory = ?, ShopeeCategory = ? from Store A inner join ProductBidCategory B on A.id = B.store_id where A.ISBN = ? ";
            if(deleteCheckStore)
                Query = Query + "delete from CheckStore where ProductCode = ? ";
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setString(1,ProductInfo_Bean.getInternationalCode());
            PreparedStatement.setString(2,ProductInfo_Bean.getFirmCode());
            PreparedStatement.setString(3,ProductInfo_Bean.getProductName());
            PreparedStatement.setString(4,ProductInfo_Bean.getUnit());
            PreparedStatement.setInt(5,Integer.parseInt(ProductInfo_Bean.getSafetyStock()));
            PreparedStatement.setString(6,ProductInfo_Bean.getDescribe());
            PreparedStatement.setString(7,ProductInfo_Bean.getRemark());
            PreparedStatement.setString(8,ProductInfo_Bean.getISBN());

            PreparedStatement.setString(9,ProductInfo_Bean.getSecondCategory());
            PreparedStatement.setString(10,ProductInfo_Bean.getThirdCategory());
            PreparedStatement.setString(11,ProductInfo_Bean.getISBN());

            PreparedStatement.setBigDecimal(12,new BigDecimal(ProductInfo_Bean.getBatchPrice()));
            PreparedStatement.setBigDecimal(13,new BigDecimal(ProductInfo_Bean.getSinglePrice()));
            PreparedStatement.setBigDecimal(14,new BigDecimal(ProductInfo_Bean.getPricing()));
            PreparedStatement.setBigDecimal(15,BigDecimal.valueOf(ProductInfo_Bean.getDiscount()));
            PreparedStatement.setBigDecimal(16,new BigDecimal(ProductInfo_Bean.getVipPrice1()));
            PreparedStatement.setBigDecimal(17,new BigDecimal(ProductInfo_Bean.getVipPrice2()));
            PreparedStatement.setBigDecimal(18,new BigDecimal(ProductInfo_Bean.getVipPrice3()));
            PreparedStatement.setString(19,ProductInfo_Bean.getISBN());

            PreparedStatement.setString(20,ProductInfo_Bean.getProductArea());
            PreparedStatement.setString(21,ProductInfo_Bean.getProductFloor());
            PreparedStatement.setString(22,ProductInfo_Bean.getISBN());

            PreparedStatement.setString(23,ProductInfo_Bean.getYahooCategory() == null ? "":ProductInfo_Bean.getYahooCategory());
            PreparedStatement.setString(24,ProductInfo_Bean.getRutenCategory() == null ? "":ProductInfo_Bean.getRutenCategory());
            PreparedStatement.setString(25,ProductInfo_Bean.getShopeeCategory() == null ? "" : String.valueOf(ProductInfo_Bean.getShopeeCategory().getId()));
            PreparedStatement.setString(26,ProductInfo_Bean.getISBN());
            if(deleteCheckStore)
                PreparedStatement.setString(27,ProductInfo_Bean.getProductCode());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(()-> DialogUI.ExceptionDialog(Ex));
        }finally {
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean isProductExistInOrder(String ISBN){
        boolean isProductExistInOrder = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "select 1 from Orders_Items where ISBN = ? and Order_id is not null " +
                    "select 1 from SubBill_Items where ISBN = ? and SubBill_id is not null " +
                    "select 1 from ReturnOrder_Items where ISBN = ? and ReturnOrder_id is not null " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setString(1,ISBN);
            PreparedStatement.setString(2,ISBN);
            PreparedStatement.setString(3,ISBN);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                if(Rs.getObject(1) != null || Rs.getObject(2) != null || Rs.getObject(3) != null)
                    isProductExistInOrder = true;
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isProductExistInOrder;
    }
    /** Delete product in database
     * @param ISBN the ISBN of product
     * */
    public boolean deleteProduct(String ISBN){
        boolean status = false;
        PreparedStatement preparedStatement = null;
        try{
            String query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from Store where ISBN = ? " +
                    "delete from ProductTag where ISBN = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setString(1,ISBN);
            preparedStatement.setString(2,ISBN);
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    public ObservableList<String> getProductTag(String ISBN, String firmCode){
        ObservableList<String> productTag = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query = "select * from ProductTag where ISBN = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setString(1,ISBN);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                String tag = Rs.getString("Tag");
                if(!tag.equals(firmCode)) {
                    productTag.add(Rs.getString("Tag"));
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return productTag;
    }
    public boolean updateProductTag(String isbn, ObservableList<String> productTagList){
        boolean status = false;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from ProductTag where ISBN = ? ";
                    for(String productTag : productTagList){
                        query = query + "insert into ProductTag (ISBN,Tag) values(?,?)";
                    }
                    query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setString(1,isbn);
            int index = 2;
            for(String productTag : productTagList){
                preparedStatement.setString(index++, isbn);
                preparedStatement.setString(index++, productTag);
            }
            preparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return status;
    }
    /***
     * Get product that Waiting to be on shelf
     * @return
     */
    public ObservableList<ManageProductOnShelf_Bean> getProductWaitingOnShelf(){
        ObservableList<ManageProductOnShelf_Bean> ProductWaitingOnShelfList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query = "SELECT B.ISBN,B.InternationalCode,B.FirmCode,B.ProductName,B.Unit,C.BatchPrice,C.SinglePrice,C.Pricing,C.VipPrice1,C.VipPrice2,C.VipPrice3," +
                    "D.PackageLength,D.PackageWidth,D.PackageHeight,D.PackageWeight,D.PreOrder,D.DaysToShip," +
                    "E.IsBlackCatOwnExpense,E.BlackCatShippingFee,E.IsSevenElevenOwnExpense,E.SevenElevenShippingFee,E.IsFamilyOwnExpense,E.FamilyShippingFee,E.IsHiLifeOwnExpense,E.HiLifeShippingFee,IsAllowBlackCat,IsAllowSevenEleven,IsAllowFamily,IsAllowHiLife " +
                    "FROM PreparativeProduction A INNER JOIN Store B ON A.ISBN = B.ISBN " +
                    "INNER JOIN Store_Price C on B.id = C.store_id " +
                    "LEFT JOIN PreparativeProduction D on B.id = D.StoreId " +
                    "LEFT JOIN ShopeeShippingFee E on B.id = E.Store_Id " +
                    "ORDER BY A.ISBN";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                ManageProductOnShelf_Bean ManageProductOnShelf_Bean = new ManageProductOnShelf_Bean();
                ManageProductOnShelf_Bean.setWebCheckBox(new CheckBox(),true);
                ManageProductOnShelf_Bean.setYahooCheckBox(new CheckBox(),true);
                ManageProductOnShelf_Bean.setRutenCheckBox(new CheckBox(),true);
                ManageProductOnShelf_Bean.setShopeeCheckBox(new CheckBox(),true);
                ManageProductOnShelf_Bean.setISBN(Rs.getString(1));
                ManageProductOnShelf_Bean.setInternationalCode(Rs.getString(2));
                ManageProductOnShelf_Bean.setFirmCode(Rs.getString(3));
                ManageProductOnShelf_Bean.setProductName(Rs.getString(4));
                ManageProductOnShelf_Bean.setUnit(Rs.getString(5));
                ManageProductOnShelf_Bean.setBatchPrice(ToolKit.RoundingDouble(Rs.getDouble(6)));
                ManageProductOnShelf_Bean.setSinglePrice(ToolKit.RoundingDouble(Rs.getDouble(7)));
                ManageProductOnShelf_Bean.setPricing(ToolKit.RoundingDouble(Rs.getDouble(8)));
                ManageProductOnShelf_Bean.setVipPrice1(ToolKit.RoundingDouble(Rs.getDouble(9)));
                ManageProductOnShelf_Bean.setVipPrice2(ToolKit.RoundingDouble(Rs.getDouble(10)));
                ManageProductOnShelf_Bean.setVipPrice3(ToolKit.RoundingDouble(Rs.getDouble(11)));

                ManageProductOnShelf_Bean.setPackageLength(Rs.getObject(12) == null ? null : Rs.getInt(12));
                ManageProductOnShelf_Bean.setPackageWidth(Rs.getObject(13) == null ? null : Rs.getInt(13));
                ManageProductOnShelf_Bean.setPackageHeight(Rs.getObject(14) == null ? null : Rs.getInt(14));
                ManageProductOnShelf_Bean.setPackageWeight(ToolKit.RoundingDouble(Rs.getDouble(15)));
                ManageProductOnShelf_Bean.setPrepareOrder(Rs.getObject(16) != null && Rs.getBoolean(16));
                ManageProductOnShelf_Bean.setDayOfPrepare(Rs.getInt(17));

                ManageProductOnShelf_Bean.setBlackCatOwnExpense(Rs.getObject(18) != null && Rs.getBoolean(18));
                ManageProductOnShelf_Bean.setBlackCatShippingFee(Rs.getObject(19) == null ? null : Rs.getInt(19));
                ManageProductOnShelf_Bean.setSevenElvenOwnExpense(Rs.getObject(20) != null && Rs.getBoolean(20));
                ManageProductOnShelf_Bean.setSevenElevenShippingFee(Rs.getObject(21) == null ? null : Rs.getInt(21));
                ManageProductOnShelf_Bean.setFamilyOwnExpense(Rs.getObject(22) != null && Rs.getBoolean(22));
                ManageProductOnShelf_Bean.setFamilyShippingFee(Rs.getObject(23) == null ? null : Rs.getInt(23));
                ManageProductOnShelf_Bean.setHiLifeOwnExpense(Rs.getObject(24) != null && Rs.getBoolean(24));
                ManageProductOnShelf_Bean.setHiLifeShippingFee(Rs.getObject(25) == null ? null : Rs.getInt(25));
                ManageProductOnShelf_Bean.setAllowBlackCat(Rs.getBoolean(26));
                ManageProductOnShelf_Bean.setAllowSevenEleven(Rs.getBoolean(27));
                ManageProductOnShelf_Bean.setAllowFamily(Rs.getBoolean(28));
                ManageProductOnShelf_Bean.setAllowHiLife(Rs.getBoolean(29));
                ProductWaitingOnShelfList.add(ManageProductOnShelf_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ProductWaitingOnShelfList;
    }
    /** Get product that on shelf
     * @param BidStore the store of online
     * @param KeyWord the key word of conditional search
     * */
    public ObservableList<ManageProductOnShelf_Bean> getProductAlreadyOnShelf(BidStore BidStore, String KeyWord){
        String Query = "SELECT B.ISBN,B.InternationalCode,B.FirmCode,B.ProductName,B.Unit,B.BatchPrice,B.Pricing,A.BidCode,A.Visitors,A.Followers,A.SellingVolume FROM " + BidStore.getBidOnShelfTableName() + " A LEFT JOIN Store B ON A.ISBN = B.ISBN WHERE B.ISBN IS NOT NULL AND A.Status = 0";
        if(!KeyWord.equals("")) Query = Query + " AND (B.ISBN LIKE '%" + KeyWord + "%' or B.InternationalCode LIKE '%" + KeyWord + "%' or B.FirmCode LIKE '%" + KeyWord + "%' or B.ProductName LIKE '%" + KeyWord + "%')";

        ERPApplication.Logger.info(Query);
        ObservableList<ManageProductOnShelf_Bean> AlreadyOnShelfList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                ManageProductOnShelf_Bean ManageProductOnShelf_Bean = new ManageProductOnShelf_Bean();
                ManageProductOnShelf_Bean.setISBN(Rs.getString(1));
                ManageProductOnShelf_Bean.setInternationalCode(Rs.getString(2));
                ManageProductOnShelf_Bean.setFirmCode(Rs.getString(3));
                ManageProductOnShelf_Bean.setProductName(Rs.getString(4));
                ManageProductOnShelf_Bean.setUnit(Rs.getString(5));
                ManageProductOnShelf_Bean.setBatchPrice(ToolKit.RoundingDouble(Rs.getDouble(6)));
                ManageProductOnShelf_Bean.setPricing(ToolKit.RoundingDouble(Rs.getDouble(7)));
                ManageProductOnShelf_Bean.setBidCode(Rs.getString(8));
                ManageProductOnShelf_Bean.setVisitors(Rs.getString(9));
                ManageProductOnShelf_Bean.setFollowers(Rs.getString(10));
                ManageProductOnShelf_Bean.setSellingVolume(Rs.getString(11));
                AlreadyOnShelfList.add(ManageProductOnShelf_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return AlreadyOnShelfList;
    }
    /** Get products which are already off shelf */
    public ObservableList<ManageProductOnShelf_Bean> getProductAlreadyOffShelf(){
        String Query = "";
        ERPApplication.Logger.info(Query);
        ObservableList<ManageProductOnShelf_Bean> AlreadyOffShelfList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                ManageProductOnShelf_Bean ManageProductOnShelf_Bean = new ManageProductOnShelf_Bean();
                ManageProductOnShelf_Bean.setISBN(Rs.getString(1));
                ManageProductOnShelf_Bean.setInternationalCode(Rs.getString(2));
                ManageProductOnShelf_Bean.setFirmCode(Rs.getString(3));
                ManageProductOnShelf_Bean.setProductName(Rs.getString(4));
                ManageProductOnShelf_Bean.setUnit(Rs.getString(5));
                ManageProductOnShelf_Bean.setBatchPrice(ToolKit.RoundingDouble(Rs.getDouble(6)));
                ManageProductOnShelf_Bean.setPricing(ToolKit.RoundingDouble(Rs.getDouble(7)));
                ManageProductOnShelf_Bean.setBidCode(Rs.getString(8));
                ManageProductOnShelf_Bean.setVisitors(Rs.getString(9));
                ManageProductOnShelf_Bean.setFollowers(Rs.getString(10));
                ManageProductOnShelf_Bean.setSellingVolume(Rs.getString(11));
                AlreadyOffShelfList.add(ManageProductOnShelf_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return AlreadyOffShelfList;
    }
    /** Get product that Waiting to be off shelf
     * @param BidStore the store of online
     * */
    public ObservableList<ManageProductOnShelf_Bean> getProductOnlineWaitingOffShelf(BidStore BidStore){
        String Query = "select " +
                "B.ISBN,B.InternationalCode,B.FirmCode,B.ProductName,B.Unit,B.BatchPrice,B.Pricing," +
                "A.BidCode,A.Visitors,A.Followers,A.SellingVolume " +
                "FROM " + BidStore.getBidOnShelfTableName() + " A LEFT JOIN Store B ON A.ISBN = B.ISBN WHERE B.Status = '" + Product_Enum.StoreStatus.封存.ordinal() + "' AND A.Status = 0";
        ERPApplication.Logger.info(Query);
        ObservableList<ManageProductOnShelf_Bean> OnlineWaitingOffShelfList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                ManageProductOnShelf_Bean ManageProductOnShelf_Bean = new ManageProductOnShelf_Bean();
                ManageProductOnShelf_Bean.setISBN(Rs.getString(1));
                ManageProductOnShelf_Bean.setInternationalCode(Rs.getString(2));
                ManageProductOnShelf_Bean.setFirmCode(Rs.getString(3));
                ManageProductOnShelf_Bean.setProductName(Rs.getString(4));
                ManageProductOnShelf_Bean.setUnit(Rs.getString(5));
                ManageProductOnShelf_Bean.setBatchPrice(ToolKit.RoundingDouble(Rs.getDouble(6)));
                ManageProductOnShelf_Bean.setPricing(ToolKit.RoundingDouble(Rs.getDouble(7)));
                ManageProductOnShelf_Bean.setBidCode(Rs.getString(8));
                ManageProductOnShelf_Bean.setVisitors(Rs.getString(9));
                ManageProductOnShelf_Bean.setFollowers(Rs.getString(10));
                ManageProductOnShelf_Bean.setSellingVolume(Rs.getString(11));
                OnlineWaitingOffShelfList.add(ManageProductOnShelf_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return OnlineWaitingOffShelfList;
    }
    /** Get product that Waiting to update
     * @param BidStore the store of online
     * */
    public ObservableList<ManageProductOnShelf_Bean> getProductOnlineWaitingUpdate(BidStore BidStore){
        String Query = "SELECT B.ISBN,B.InternationalCode,B.FirmCode,B.ProductName,A.BidCode,A.Visitors,A.Followers FROM " + BidStore.getBidOnShelfTableName() + " A LEFT JOIN Store B ON A.ISBN = B.ISBN WHERE B.Status = '" + Product_Enum.StoreStatus.未確認.ordinal()  + "' AND A.Status = 0";
        ObservableList<ManageProductOnShelf_Bean> OnlineWaitingUpdateList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                ManageProductOnShelf_Bean ManageProductOnShelf_Bean = new ManageProductOnShelf_Bean();
                ManageProductOnShelf_Bean.setISBN(Rs.getString(1));
                ManageProductOnShelf_Bean.setInternationalCode(Rs.getString(2));
                ManageProductOnShelf_Bean.setFirmCode(Rs.getString(3));
                ManageProductOnShelf_Bean.setProductName(Rs.getString(4));
                ManageProductOnShelf_Bean.setBidCode(Rs.getString(5));
                ManageProductOnShelf_Bean.setVisitors(Rs.getString(6));
                ManageProductOnShelf_Bean.setFollowers(Rs.getString(7));
                OnlineWaitingUpdateList.add(ManageProductOnShelf_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return OnlineWaitingUpdateList;
    }
    /** Insert product that waiting to be on shelf into database
     * */
    public boolean insertWaitingOnShelf(ProductInfo_Bean ProductInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            Query = Query + "INSERT INTO PreparativeProduction (StoreId,ISBN,Name,Category,Attribute,AttributeOption,Descript,Stock,Price,PackageWidth,PackageHeight,PackageLength,PackageWeight,DaysToShip,PreOrder,CreateDateTime,UpdateDateTime,isAcution) VALUES " +
                    "((select id from store where ISBN = ?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, ProductInfo_Bean.getISBN());
            PreparedStatement.setString(2, ProductInfo_Bean.getISBN());
            PreparedStatement.setString(3, ProductInfo_Bean.getProductName());
            PreparedStatement.setObject(4, null);
            PreparedStatement.setObject(5, null);
            PreparedStatement.setString(6, null);
            PreparedStatement.setString(7, ProductInfo_Bean.getDescribe());
            PreparedStatement.setInt(8, Integer.parseInt(ProductInfo_Bean.getInStock()));
            PreparedStatement.setBigDecimal(9, new BigDecimal(ProductInfo_Bean.getSinglePrice()));
            PreparedStatement.setObject(10,null);
            PreparedStatement.setObject(11,null);
            PreparedStatement.setObject(12,null);
            PreparedStatement.setBigDecimal(13, BigDecimal.valueOf(0.0));
            PreparedStatement.setInt(14,0);
            PreparedStatement.setObject(15,null);
            PreparedStatement.setString(16, ToolKit.getToday("yyyy-MM-dd HH:mm:ss"));
            PreparedStatement.setString(17, ToolKit.getToday("yyyy-MM-dd HH:mm:ss"));
            PreparedStatement.setBoolean(18, false);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Update product that waiting to be on shelf into database
     * */
    public boolean updateWaitingOnShelfAndShippingFee(ManageProductOnShelf_Bean ManageProductOnShelf_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update PreparativeProduction set PackageLength = ?, PackageWidth = ?, PackageHeight = ?, PackageWeight = ?, DaysToShip = ?, PreOrder = ? where ISBN = ? " +
                    "IF EXISTS (select id from ShopeeShippingFee where store_id = (select id from store where ISBN = ?)) BEGIN " +
                    "update ShopeeShippingFee set IsBlackCatOwnExpense = ?,BlackCatShippingFee = ?,IsSevenElevenOwnExpense = ?,SevenElevenShippingFee = ?,IsFamilyOwnExpense = ?,FamilyShippingFee = ?,IsHiLifeOwnExpense = ?, HiLifeShippingFee = ?, IsAllowBlackCat = ?, IsAllowSevenEleven = ?, IsAllowFamily = ?, IsAllowHiLife = ? where store_id = (select id from store where ISBN = ?) " +
                    "END ELSE BEGIN " +
                    "insert into ShopeeShippingFee (Store_Id,ISBN,IsBlackCatOwnExpense,BlackCatShippingFee,IsSevenElevenOwnExpense,SevenElevenShippingFee,IsFamilyOwnExpense,FamilyShippingFee,IsHiLifeOwnExpense,HiLifeShippingFee,IsAllowBlackCat,IsAllowSevenEleven,IsAllowFamily,IsAllowHiLife) values ((select id from Store where ISBN = ?),?,?,?,?,?,?,?,?,?,?,?,?,?) END " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setObject(1, ManageProductOnShelf_Bean.getPackageLength());
            PreparedStatement.setObject(2, ManageProductOnShelf_Bean.getPackageWidth());
            PreparedStatement.setObject(3, ManageProductOnShelf_Bean.getPackageHeight());
            PreparedStatement.setBigDecimal(4, BigDecimal.valueOf(ManageProductOnShelf_Bean.getPackageWeight()));
            PreparedStatement.setInt(5, ManageProductOnShelf_Bean.getDayOfPrepare());
            PreparedStatement.setBoolean(6, ManageProductOnShelf_Bean.isPrepareOrder());
            PreparedStatement.setString(7, ManageProductOnShelf_Bean.getISBN());

            PreparedStatement.setString(8, ManageProductOnShelf_Bean.getISBN());
            PreparedStatement.setBoolean(9, ManageProductOnShelf_Bean.isBlackCatOwnExpense());
            PreparedStatement.setObject(10, ManageProductOnShelf_Bean.getBlackCatShippingFee());
            PreparedStatement.setBoolean(11, ManageProductOnShelf_Bean.isSevenElvenOwnExpense());
            PreparedStatement.setObject(12, ManageProductOnShelf_Bean.getSevenElevenShippingFee());
            PreparedStatement.setBoolean(13, ManageProductOnShelf_Bean.isFamilyOwnExpense());
            PreparedStatement.setObject(14, ManageProductOnShelf_Bean.getFamilyShippingFee());
            PreparedStatement.setBoolean(15, ManageProductOnShelf_Bean.isHiLifeOwnExpense());
            PreparedStatement.setObject(16, ManageProductOnShelf_Bean.getHiLifeShippingFee());
            PreparedStatement.setBoolean(17, ManageProductOnShelf_Bean.isAllowBlackCat());
            PreparedStatement.setBoolean(18, ManageProductOnShelf_Bean.isAllowSevenEleven());
            PreparedStatement.setBoolean(19, ManageProductOnShelf_Bean.isAllowFamily());
            PreparedStatement.setBoolean(20, ManageProductOnShelf_Bean.isAllowHiLife());
            PreparedStatement.setString(21, ManageProductOnShelf_Bean.getISBN());

            PreparedStatement.setString(22, ManageProductOnShelf_Bean.getISBN());
            PreparedStatement.setString(23, ManageProductOnShelf_Bean.getISBN());
            PreparedStatement.setBoolean(24, ManageProductOnShelf_Bean.isBlackCatOwnExpense());
            PreparedStatement.setObject(25, ManageProductOnShelf_Bean.getBlackCatShippingFee());
            PreparedStatement.setBoolean(26, ManageProductOnShelf_Bean.isSevenElvenOwnExpense());
            PreparedStatement.setObject(27, ManageProductOnShelf_Bean.getSevenElevenShippingFee());
            PreparedStatement.setBoolean(28, ManageProductOnShelf_Bean.isFamilyOwnExpense());
            PreparedStatement.setObject(29, ManageProductOnShelf_Bean.getFamilyShippingFee());
            PreparedStatement.setBoolean(30, ManageProductOnShelf_Bean.isHiLifeOwnExpense());
            PreparedStatement.setObject(31, ManageProductOnShelf_Bean.getHiLifeShippingFee());
            PreparedStatement.setBoolean(32, ManageProductOnShelf_Bean.isAllowBlackCat());
            PreparedStatement.setBoolean(33, ManageProductOnShelf_Bean.isAllowSevenEleven());
            PreparedStatement.setBoolean(34, ManageProductOnShelf_Bean.isAllowFamily());
            PreparedStatement.setBoolean(35, ManageProductOnShelf_Bean.isAllowHiLife());

            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Delete All product that waiting to be on shelf in database */
    public boolean deleteWaitingOnShelf(){
        try {
            SqlAdapter.DataBaseQuery("delete PreparativeProduction");
            return true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            return false;
        }
    }
    /** Apply bid category of selected product to other products
     * @param selectProductInfo_Bean the bean of product which selected
     * */
    public void ApplyProductBidCategory(ProductInfo_Bean selectProductInfo_Bean, ProductInfo_Bean ProductInfo_Bean){
        ProductInfo_Bean.setYahooCategory(selectProductInfo_Bean.getYahooCategory());
        ProductInfo_Bean.setRutenCategory(selectProductInfo_Bean.getRutenCategory());
        ProductInfo_Bean.setShopeeCategory(selectProductInfo_Bean.getShopeeCategory());
        handleProductBidCategory(ProductInfo_Bean);
    }
    private void handleProductBidCategory(ProductInfo_Bean ProductInfo_Bean){
        if(isProductBidCategoryExist(ProductInfo_Bean.getISBN())) UpdateProductBidCategory(ProductInfo_Bean);
        else    InsertProductBidCategory(ProductInfo_Bean);
    }
    private void InsertProductBidCategory(ProductInfo_Bean ProductInfo_Bean){
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "insert into ProductBidCategory (ISBN,YahooCategory,RutenCategory,ShopeeCategory) values (?,?,?,?) " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setObject(1, ProductInfo_Bean.getISBN());
            PreparedStatement.setString(2, ProductInfo_Bean.getYahooCategory());
            PreparedStatement.setString(3, ProductInfo_Bean.getRutenCategory());
            PreparedStatement.setString(4, ProductInfo_Bean.getShopeeCategory() == null ? "" : String.valueOf(ProductInfo_Bean.getShopeeCategory().getId()));
            PreparedStatement.executeUpdate();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
    }
    private void UpdateProductBidCategory(ProductInfo_Bean ProductInfo_Bean){
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update ProductBidCategory set YahooCategory = ?, RutenCategory = ?, ShopeeCategory = ? where ISBN = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, ProductInfo_Bean.getYahooCategory());
            PreparedStatement.setString(2, ProductInfo_Bean.getRutenCategory());
            PreparedStatement.setString(3, ProductInfo_Bean.getShopeeCategory() == null ? "" : String.valueOf(ProductInfo_Bean.getShopeeCategory().getId()));
            PreparedStatement.setObject(4, ProductInfo_Bean.getISBN());
            PreparedStatement.executeUpdate();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
    }
    private boolean isProductBidCategoryExist(String ISBN){
        boolean isProductBidCategoryExist = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from ProductBidCategory where ISBN = '" + ISBN + "'");
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isProductBidCategoryExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isProductBidCategoryExist;
    }
    /** Get bid category of product
     * @param ProductInfo_Bean the bean of product
     * */
    public void getProductBidCategory(ProductInfo_Bean ProductInfo_Bean){
        ProductInfo_Bean.setYahooCategory("");
        ProductInfo_Bean.setRutenCategory("");
        ProductInfo_Bean.setShopeeCategory(null);
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select B.id,B.ParentId,B.Name,C.Name from ProductBidCategory A inner join ShopeeCategory B on A.ShopeeCategory = B.id inner join ShopeeCategoryTree C on B.id = C.SId where A.ISBN = ?");
            PreparedStatement.setString(1,ProductInfo_Bean.getISBN());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                ShopeeCategory_Bean ShopeeCategory_Bean = new ShopeeCategory_Bean();
                ShopeeCategory_Bean.setId(Rs.getInt(1));
                ShopeeCategory_Bean.setParentId(Rs.getInt(2));
                ShopeeCategory_Bean.setCategoryName(Rs.getString(3));
                ShopeeCategory_Bean.setCategoryTreeName(Rs.getString(4));
                ProductInfo_Bean.setShopeeCategory(ShopeeCategory_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
    }
    /** Insert product picture into database
     * @param ProductInfo_Bean the bean of product
     * */
    public boolean saveProductPicture(ProductInfo_Bean ProductInfo_Bean){
        boolean status = false;
        PreparedStatement preparedStatement = null;
        try{
            String query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update ProductPicture set Picture1 = ?, Picture2 = ?, Picture3 = ? where ISBN = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setString(1,ProductInfo_Bean.getPicture1());
            preparedStatement.setString(2,ProductInfo_Bean.getPicture2());
            preparedStatement.setString(3,ProductInfo_Bean.getPicture3());
            preparedStatement.setString(4,ProductInfo_Bean.getISBN());
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(()-> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    public boolean updateProductInStock(String ISBN, int newInStock){
        boolean status = false;
        PreparedStatement PreparedStatement;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Store set InStock = ? where ISBN = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,newInStock);
            PreparedStatement.setString(2,ISBN);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return status;
    }
    public ObservableList<String> getSpecificationProductName(String ISBN){
        ObservableList<String> specificationProductNameList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select name from store_specificationProductName where store_id = (select id from store where ISBN = ?)");
            PreparedStatement.setString(1,ISBN);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next())
                specificationProductNameList.add(Rs.getString(1));
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return specificationProductNameList;
    }
    public boolean insertSpecificationProductName(String ISBN, String productName){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    " insert into store_specificationProductName (store_id,name) values ((select id from Store where ISBN = ?),?) " +
                    " COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, ISBN);
            PreparedStatement.setString(2, productName);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean modifySpecificationProductName(String ISBN, String originProductName, String wannaProductName){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update store_specificationProductName set name = ? where store_id = (select id from store where ISBN = ?) and name = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,wannaProductName);
            PreparedStatement.setString(2,ISBN);
            PreparedStatement.setString(3,originProductName);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean deleteSpecificationProductName(String ISBN, String productName){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete store_specificationProductName where store_id = (select id from store where ISBN = ?) and name = ? " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,ISBN);
            PreparedStatement.setString(2,productName);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public String getSpecificationContent(String ISBN){
        String specificationContent = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "select content from store_specificationContent where store_id = (select id from store where ISBN = ?) " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, ISBN);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next())
                specificationContent = Rs.getString(1);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return specificationContent;
    }
    public boolean saveSpecificationContent(String ISBN, String specificationContent){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "IF EXISTS (select id from store_specificationContent where store_id = (select id from store where ISBN = ?)) BEGIN " +
                    "update store_specificationContent set content = ? where store_id = (select id from store where ISBN = ?) " +
                    "END ELSE BEGIN " +
                    "insert into store_specificationContent (store_id,content) values ((select id from Store where ISBN = ?),?) END " +
                    "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, ISBN);
            PreparedStatement.setString(2, specificationContent);
            PreparedStatement.setString(3, ISBN);
            PreparedStatement.setString(4, ISBN);
            PreparedStatement.setString(5, specificationContent);
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
}
