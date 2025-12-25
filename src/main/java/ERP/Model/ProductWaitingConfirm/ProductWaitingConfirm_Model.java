package ERP.Model.ProductWaitingConfirm;

import ERP.Bean.ProductWaitConfirm.*;
import ERP.Bean.ToolKit.BigGoFilter.BigGoFilter_Bean;
import ERP.Controller.ProductWaitingConfirm.ProductWaitingConfirm.ProductWaitingConfirm_Controller;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.*;
import ERP.Model.Product.Product_Model;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/** [ERP.Model] Product waiting confirm */
public class ProductWaitingConfirm_Model{
    private ToolKit ToolKit;
    private Product_Model Product_Model;
    private HashMap<Object,Integer> CheckStoreVendor = null;

    public ProductWaitingConfirm_Model(){
        this.ToolKit = ERPApplication.ToolKit;
        this.Product_Model = ToolKit.ModelToolKit.getProductModel();
    }
    /** Get all vendor in CheckStore of database*/
    public ObservableList<Vendor_Bean> getCheckStoreVendor(){
        ObservableList<Vendor_Bean> VendorList = FXCollections.observableArrayList();
        VendorList.add(new Vendor_Bean(0,""));
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("SELECT DISTINCT VendorCode,Vendor FROM checkstore WHERE VendorCode <> '' AND VendorCode IS NOT NULL AND Vendor <> '0' AND Vendor IS NOT NULL ORDER BY VendorCode");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                VendorList.add(new Vendor_Bean(Rs.getInt(1), Rs.getString(2)));
                if(this.CheckStoreVendor == null)    this.CheckStoreVendor = new HashMap<>();
                this.CheckStoreVendor.put(Rs.getString(2),Rs.getInt(1));
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return VendorList;
    }
    /** Get all category of vendor
     * @param VendorSelectItem the bean of vendor which selected
     * @param CategoryLayer the layer of category
     * @param FirstCategorySelectItem the first category which selected
     * @param SecondCategorySelectItem the second category which selected
     * */
    public ObservableList<VendorCategory_Bean> getVendorCategory(Vendor_Bean VendorSelectItem, CategoryLayer CategoryLayer, VendorCategory_Bean FirstCategorySelectItem, VendorCategory_Bean SecondCategorySelectItem){
        ObservableList<VendorCategory_Bean> CategoryBeanList = FXCollections.observableArrayList();
        CategoryBeanList.add(new VendorCategory_Bean(null,"",""));
        String Query = "select id,Name,Value from " + Product_Enum.Vendor.valueOf(VendorSelectItem.getVendorName()).getCategoryTable() +" where Layer = ? and Exist = ?";
        if(CategoryLayer == Product_Enum.CategoryLayer.中類別 || CategoryLayer == Product_Enum.CategoryLayer.小類別)
            Query = Query + " and Category = ?";

//        ERPApplication.Logger.info("[ProductWaitConfirm GetCategory] : " + Query);
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,CategoryLayer.getLayer());
            PreparedStatement.setInt(2,1);
            if(CategoryLayer == Product_Enum.CategoryLayer.中類別)
                PreparedStatement.setString(3,FirstCategorySelectItem.getCategoryValue());
            else if(CategoryLayer == Product_Enum.CategoryLayer.小類別)
                PreparedStatement.setString(3,SecondCategorySelectItem.getCategoryValue());

            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                int id = Rs.getInt(1);
                String Name = Rs.getString(2);
                String Value = Rs.getString(3);
                CategoryBeanList.add(new VendorCategory_Bean(id,Name, Value));
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return CategoryBeanList;
    }
    /** Conditional search for products
     * @param ConditionalDBSearch_Bean the bean of conditional search for database
     * */
    public ObservableList<WaitConfirmProductInfo_Bean> ProductConditionalSearch(ConditionalDBSearch_Bean ConditionalDBSearch_Bean){
        ObservableList<WaitConfirmProductInfo_Bean> WaitConfirmProductList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(getSelectQuery(ConditionalDBSearch_Bean));
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean = new WaitConfirmProductInfo_Bean();
                WaitConfirmProductInfo_Bean.setVendor(Rs.getString(1));
                WaitConfirmProductInfo_Bean.setKeyInDate(Rs.getString(2));
                WaitConfirmProductInfo_Bean.setUpdateDate(Rs.getString(3));
                WaitConfirmProductInfo_Bean.setProductCode(Rs.getString(4));
                WaitConfirmProductInfo_Bean.setProductName(Rs.getString(5));
                WaitConfirmProductInfo_Bean.setPricing(ToolKit.RoundingDouble(Rs.getDouble(6)));
                WaitConfirmProductInfo_Bean.setSinglePrice(ToolKit.RoundingDouble(Rs.getDouble(7)));
                WaitConfirmProductInfo_Bean.setBatchPrice(ToolKit.RoundingDouble(Rs.getDouble(8)));
                WaitConfirmProductInfo_Bean.setVipPrice1(ToolKit.RoundingDouble(Rs.getDouble(9)));
                WaitConfirmProductInfo_Bean.setVipPrice2(ToolKit.RoundingDouble(Rs.getDouble(10)));
                WaitConfirmProductInfo_Bean.setVipPrice3(ToolKit.RoundingDouble(Rs.getDouble(11)));
                WaitConfirmProductInfo_Bean.setNewFirstCategory(Rs.getString(12));
                WaitConfirmProductInfo_Bean.setSupplyStatus(Rs.getString(13));

                WaitConfirmProductInfo_Bean.setVendorCode(Rs.getInt(14));
                WaitConfirmProductInfo_Bean.setBrand(Rs.getString(15));
                WaitConfirmProductInfo_Bean.setUnit(Rs.getString(16));
                WaitConfirmProductInfo_Bean.setPreviousStatus(Rs.getInt(17));
                WaitConfirmProductInfo_Bean.setDescribe(Rs.getString(18));
                WaitConfirmProductInfo_Bean.setRemark(Rs.getString(19));
                WaitConfirmProductInfo_Bean.setFirstCategory_Id(Rs.getObject(20) == null ? null : Rs.getInt(20));
                WaitConfirmProductInfo_Bean.setSecondCategory_Id(Rs.getObject(21) == null ? null : Rs.getInt(21));
                WaitConfirmProductInfo_Bean.setThirdCategory_Id(Rs.getObject(22) == null ? null : Rs.getInt(22));
                WaitConfirmProductInfo_Bean.setISBN(Rs.getString(23));
                WaitConfirmProductInfo_Bean.setWaitConfirmTable(WaitConfirmTable.valueOf(Rs.getString(24)));

                if(ConditionalDBSearch_Bean.getWaitConfirmStatus() == WaitConfirmStatus.更新) {
                    WaitConfirmProductInfo_Bean.setStorePricing(Rs.getFloat(25));
                    WaitConfirmProductInfo_Bean.setStoreSinglePrice(Rs.getFloat(26));
                    WaitConfirmProductInfo_Bean.setStoreBatchPrice(Rs.getFloat(27));
                }
                WaitConfirmProductList.add(WaitConfirmProductInfo_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return WaitConfirmProductList;
    }
    private String getSelectQuery(ConditionalDBSearch_Bean ConditionalSearch_Bean){
        WaitConfirmTable WaitConfirmTable = ConditionalSearch_Bean.getWaitConfirmTable();
        WaitConfirmStatus WaitConfirmStatus = ConditionalSearch_Bean.getWaitConfirmStatus();
        int StoreStatus = WaitConfirmStatus.ordinal();
        String ProductNameQuery = addProductNameQuery(ConditionalSearch_Bean.getProductName(), ConditionalSearch_Bean.isSearchProductNameByOr());

        String SelectQuery = null;
        if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.更新){
            SelectQuery = "select T1.Vendor,T1.KeyinDate,CONVERT(nvarchar,T1.UpdateDate),T1.ProductCode,T1.ProductName,T1.Pricing,T1.SinglePrice,T1.BatchPrice,T1.VipPrice1,T1.VipPrice2,T1.VipPrice3,T1.NewFirstCategory,T1.SupplyStatus,T1.VendorCode,T1.Brand,T1.Unit,T1.PreviousStatus,T1.Describe,T1.Remark,T1.FirstCategory_Id,T1.SecondCategory_Id,T1.ThirdCategory_Id,T2.ISBN," +
                    "'" + Product_Enum.WaitConfirmTable.待確認商品.name() + "' as WaitConfirmTable,A.Pricing,A.SinglePrice,A.BatchPrice from CheckStore T1 left join Store T2 on T1.ProductCode=T2.ProductCode inner join store_price A on T2.id = A.store_id inner join store_category B on T2.id = B.store_id where " + addVendorQuery(ConditionalSearch_Bean.getVendor()) + " and T1.Status='" + StoreStatus +"' "
                    + addFirstCategoryQuery(ConditionalSearch_Bean.getFirstCategory(), Product_Enum.WaitConfirmTable.進銷存商品)
                    + addSecondCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getSecondCategory(), Product_Enum.WaitConfirmTable.進銷存商品)
                    + addThirdCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getThirdCategory(),Product_Enum.WaitConfirmTable.進銷存商品)
                    + addSinglePrice_Minus_BatchPrice(ConditionalSearch_Bean.getSinglePrice_Minus_BatchPrice(),Product_Enum.WaitConfirmTable.進銷存商品)
                    + ProductNameQuery + " order by T1.ProductCode";
        }else {
            if(WaitConfirmTable == Product_Enum.WaitConfirmTable.全部){
                String CheckStore = "select T1.Vendor,T1.KeyinDate,CONVERT(nvarchar,T1.UpdateDate),T1.ProductCode as ProductCode,T1.ProductName,T1.Pricing,T1.SinglePrice,T1.BatchPrice,T1.VipPrice1,T1.VipPrice2,T1.VipPrice3,T1.NewFirstCategory,T1.SupplyStatus,T1.VendorCode,T1.Brand,T1.Unit,T1.PreviousStatus,T1.Describe,T1.Remark," +
                        "T1.FirstCategory_Id,T1.SecondCategory_Id,T1.ThirdCategory_Id," +
                        "'','" + Product_Enum.WaitConfirmTable.待確認商品.name() + "' as WaitConfirmTable from checkstore T1 where " + addVendorQuery(ConditionalSearch_Bean.getVendor()) + " and T1.Status='" + StoreStatus + "' "
                        + addFirstCategoryQuery(ConditionalSearch_Bean.getFirstCategory(), Product_Enum.WaitConfirmTable.待確認商品)
                        + addSecondCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getSecondCategory(), Product_Enum.WaitConfirmTable.待確認商品)
                        + addThirdCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getThirdCategory(),Product_Enum.WaitConfirmTable.待確認商品)
                        + addSinglePrice_Minus_BatchPrice(ConditionalSearch_Bean.getSinglePrice_Minus_BatchPrice(),Product_Enum.WaitConfirmTable.待確認商品)
                        + ProductNameQuery;
                if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.新增)  StoreStatus = Product_Enum.StoreStatus.已確認.ordinal();
                String Store = "select T1.Vendor,C.KeyinDate,CONVERT(nvarchar,C.UpdateDate),T1.ProductCode as ProductCode,T1.ProductName,A.Pricing,A.SinglePrice,A.BatchPrice,A.VipPrice1,A.VipPrice2,A.VipPrice3,B.NewFirstCategory,T1.SupplyStatus,T1.VendorCode,T1.Brand,T1.Unit,NULL,T1.Describe,T1.Remark," +
                        "B.FirstCategory_Id,B.SecondCategory_Id,B.ThirdCategory_Id," +
//                        getStoreCategoryValueQuery(Product_Enum.WaitConfirmTable.進銷存商品, CategoryLayer.大類別) + "," +
//                        getStoreCategoryValueQuery(Product_Enum.WaitConfirmTable.進銷存商品, CategoryLayer.中類別) + "," +
//                        getStoreCategoryValueQuery(Product_Enum.WaitConfirmTable.進銷存商品, CategoryLayer.小類別) + "," +
                        "T1.ISBN,'" + Product_Enum.WaitConfirmTable.進銷存商品.name() + "' as WaitConfirmTable from store T1 INNER JOIN store_price A on T1.id = A.store_id inner join store_category B on T1.id = B.store_id inner join store_date C on T1.id = C.store_id where " + addVendorQuery(ConditionalSearch_Bean.getVendor()) + " and T1.Status='" + StoreStatus + "' "
                        + addFirstCategoryQuery(ConditionalSearch_Bean.getFirstCategory(), Product_Enum.WaitConfirmTable.進銷存商品)
                        + addSecondCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getSecondCategory(), Product_Enum.WaitConfirmTable.進銷存商品)
                        + addThirdCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getThirdCategory(),Product_Enum.WaitConfirmTable.進銷存商品)
                        + addSinglePrice_Minus_BatchPrice(ConditionalSearch_Bean.getSinglePrice_Minus_BatchPrice(),Product_Enum.WaitConfirmTable.進銷存商品)
                        + ProductNameQuery;
                SelectQuery = Store + " union all " + CheckStore + " order by WaitConfirmTable DESC , ProductCode";
            } else if (WaitConfirmTable == Product_Enum.WaitConfirmTable.進銷存商品) {
                if(WaitConfirmStatus == Product_Enum.WaitConfirmStatus.新增)  StoreStatus = Product_Enum.StoreStatus.已確認.ordinal();
                SelectQuery = "select T1.Vendor,C.KeyinDate,CONVERT(nvarchar,C.UpdateDate),T1.ProductCode,T1.ProductName,A.Pricing,A.SinglePrice,A.BatchPrice,A.VipPrice1,A.VipPrice2,A.VipPrice3,B.NewFirstCategory,T1.SupplyStatus,T1.VendorCode,T1.Brand,T1.Unit,NULL,T1.Describe,T1.Remark," +
                        "B.FirstCategory_Id,B.SecondCategory_Id,B.ThirdCategory_Id," +
                        "T1.ISBN,'" + Product_Enum.WaitConfirmTable.進銷存商品.name() +"' as WaitConfirmTable from " + WaitConfirmTable.getTableName() + " T1 INNER JOIN store_price A on T1.id = A.store_id inner join store_category B on T1.id = B.store_id inner join store_date C on T1.id = C.store_id where " + addVendorQuery(ConditionalSearch_Bean.getVendor()) + " and T1.Status='" + StoreStatus + "' "
                        + addFirstCategoryQuery(ConditionalSearch_Bean.getFirstCategory(), Product_Enum.WaitConfirmTable.進銷存商品)
                        + addSecondCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getSecondCategory(), Product_Enum.WaitConfirmTable.進銷存商品)
                        + addThirdCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getThirdCategory(),Product_Enum.WaitConfirmTable.進銷存商品)
                        + addSinglePrice_Minus_BatchPrice(ConditionalSearch_Bean.getSinglePrice_Minus_BatchPrice(),Product_Enum.WaitConfirmTable.進銷存商品)
                        + ProductNameQuery + " order by T1.ProductCode";
            }else if(WaitConfirmTable == Product_Enum.WaitConfirmTable.待確認商品)
                SelectQuery = "select T1.Vendor,T1.KeyinDate,CONVERT(nvarchar,T1.UpdateDate),T1.ProductCode,T1.ProductName,T1.Pricing,T1.SinglePrice,T1.BatchPrice,T1.VipPrice1,T1.VipPrice2,T1.VipPrice3,T1.NewFirstCategory,T1.SupplyStatus,T1.VendorCode,T1.Brand,T1.Unit,T1.PreviousStatus,T1.Describe,T1.Remark," +
                        "T1.FirstCategory_Id,T1.SecondCategory_Id,T1.ThirdCategory_Id," +
//                        getStoreCategoryValueQuery(Product_Enum.WaitConfirmTable.待確認商品, CategoryLayer.大類別) + "," +
//                        getStoreCategoryValueQuery(Product_Enum.WaitConfirmTable.待確認商品, CategoryLayer.中類別) + "," +
//                        getStoreCategoryValueQuery(Product_Enum.WaitConfirmTable.待確認商品, CategoryLayer.小類別) + "," +
                        "'','" + Product_Enum.WaitConfirmTable.待確認商品.name() + "' as WaitConfirmTable from " + WaitConfirmTable.getTableName() + " T1 where " + addVendorQuery(ConditionalSearch_Bean.getVendor()) + " and T1.Status='" + StoreStatus +"' "
                        + addFirstCategoryQuery(ConditionalSearch_Bean.getFirstCategory(), Product_Enum.WaitConfirmTable.待確認商品)
                        + addSecondCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getSecondCategory(), Product_Enum.WaitConfirmTable.待確認商品)
                        + addThirdCategoryQuery(ConditionalSearch_Bean.getVendor(),ConditionalSearch_Bean.getThirdCategory(),Product_Enum.WaitConfirmTable.待確認商品)
                        + addSinglePrice_Minus_BatchPrice(ConditionalSearch_Bean.getSinglePrice_Minus_BatchPrice(),Product_Enum.WaitConfirmTable.待確認商品)
                        + ProductNameQuery + " order by T1.ProductCode";
        }
        ERPApplication.Logger.info("[ProductWaitConfirm SelectProduct] : " + SelectQuery);
        return SelectQuery;
    }
    private String getStoreCategoryValueQuery(WaitConfirmTable WaitConfirmTable, CategoryLayer CategoryLayer){
        String categoryColumnQuery = "";
        if (WaitConfirmTable == Product_Enum.WaitConfirmTable.待確認商品) {
            if(CategoryLayer == Product_Enum.CategoryLayer.大類別) categoryColumnQuery = "value = T1.FirstCategory";
            else if(CategoryLayer == Product_Enum.CategoryLayer.中類別)    categoryColumnQuery = "value = T1.SecondCategory";
            else if(CategoryLayer == Product_Enum.CategoryLayer.小類別)    categoryColumnQuery = "value = T1.ThirdCategory";
        }else if (WaitConfirmTable == Product_Enum.WaitConfirmTable.進銷存商品) {
            if(CategoryLayer == Product_Enum.CategoryLayer.大類別) categoryColumnQuery = "id = B.FirstCategory_Id";
            else if(CategoryLayer == Product_Enum.CategoryLayer.中類別)    categoryColumnQuery = "id = B.SecondCategory_Id";
            else if(CategoryLayer == Product_Enum.CategoryLayer.小類別)    categoryColumnQuery = "id = B.ThirdCategory_Id";
        }
        String query = "CASE ";
        for(Product_Enum.Vendor Vendor : Product_Enum.Vendor.values()){
            if(Vendor != Product_Enum.Vendor.未選擇)
                query = query + "WHEN T1.VendorCode = '" + Vendor.getVendorCode() + "' THEN " +
                        "(select id from category_" + Vendor.getEnglishName() + " where " + categoryColumnQuery + " and Layer = '" + CategoryLayer.getLayer() + "') ";
        }
        query = query + "ELSE null END ";
        return query;
    }
    private String addFirstCategoryQuery(VendorCategory_Bean FirstCategory, WaitConfirmTable WaitConfirmTable){
        if(FirstCategory == null)
            return "";
        else if(!FirstCategory.getCategoryValue().equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.進銷存商品)
            return " and B.FirstCategory_Id='" + FirstCategory.getId() + "'";
        else if(!FirstCategory.getCategoryValue().equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.待確認商品)
            return " and T1.FirstCategory_Id='" + FirstCategory.getId() + "'";
        return "";
    }
    private String addSecondCategoryQuery(Vendor_Bean Vendor_Bean, VendorCategory_Bean SecondVendorCategory, WaitConfirmTable WaitConfirmTable){
        if(SecondVendorCategory == null)
            return "";
        else if(!SecondVendorCategory.getCategoryValue().equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.進銷存商品)
            return" and B.SecondCategory_Id='" + SecondVendorCategory.getId() + "'";
        else if(!SecondVendorCategory.getCategoryValue().equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.待確認商品) {
            return " and T1.SecondCategory_Id='" + SecondVendorCategory.getId() + "'";
        }
        return "";
    }
    private String addThirdCategoryQuery(Vendor_Bean Vendor_Bean, VendorCategory_Bean ThirdVendorCategory, WaitConfirmTable WaitConfirmTable){
        if(ThirdVendorCategory == null)
            return "";
        else if(!ThirdVendorCategory.getCategoryValue().equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.進銷存商品)
            return " and B.ThirdCategory_Id='" + ThirdVendorCategory.getCategoryValue() + "'";
        else if(!ThirdVendorCategory.getCategoryValue().equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.待確認商品){
            return " and T1.ThirdCategory_Id='" + ThirdVendorCategory.getId() + "'";
        }
        return "";
    }
    private String addSinglePrice_Minus_BatchPrice(String SinglePrice_Minus_BatchPrice, WaitConfirmTable WaitConfirmTable){
        if(!SinglePrice_Minus_BatchPrice.equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.進銷存商品)
            return " and (A.SinglePrice-A.BatchPrice <= " + SinglePrice_Minus_BatchPrice + ")";
        else if(!SinglePrice_Minus_BatchPrice.equals("") && WaitConfirmTable == Product_Enum.WaitConfirmTable.待確認商品)
            return " and (T1.SinglePrice-T1.BatchPrice <= " + SinglePrice_Minus_BatchPrice + ")";
        return "";
    }
    private String addProductNameQuery(String ProductName, boolean isSearchProductNameByOr){
        if(!ProductName.equals("")) {
            StringBuilder Query = new StringBuilder(" and (");
            String[] KeyWords = ProductName.split(" ");
            for (int index = 0; index < KeyWords.length; index++) {
                String keyWord = KeyWords[index];
                Query.append("T1.ProductName like '%").append(keyWord).append("%'");
                if(index != KeyWords.length-1) {
                    Query.append(isSearchProductNameByOr ? " or " : " and ");
                }else
                    Query.append(")");
            }
            return Query.toString();
        }
        return "";
    }
    private String addVendorQuery(Vendor_Bean Vendor_Bean){
        String VendorQuery = "T1.VendorCode<>'0' AND T1.VendorCode<>''";
        if(Vendor_Bean.getVendorCode() == 0)   return VendorQuery;
        else    return "T1.VendorCode='" + Vendor_Bean.getVendorCode() + "'";
    }
    public ObservableList<BigGoFilter_Bean> refreshBigGoStore(HashMap<String, BigGoFilter_Bean> filterMap){
        if(!initialBigGoFilterExist()){
            DialogUI.AlarmDialog("※ 更新失敗!");
            return null;
        }
        ObservableList<BigGoFilter_Bean> newBigGoFilterList = FXCollections.observableArrayList();
        Response Response;
        try {
            Response = Jsoup.connect("https://biggo.com.tw/store/overview.php")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .method(Method.GET)
                    .execute();
            Document Doc = Response.parse();
            String text = Doc.html();
            text = text.substring(text.indexOf("storeList")+11,text.indexOf("filterList"));
            text = text.substring(0,text.lastIndexOf(","));
            String[] storeSp = text.split(",\\{");
            for(int index = 0; index < storeSp.length; index++){
                if(index == 0)
                    storeSp[index] = storeSp[index].substring(1);
                else
                    storeSp[index] = "{" + storeSp[index];
                if(index == storeSp.length-1)
                    storeSp[index] = storeSp[index].substring(0,storeSp[index].length()-1);
                JSONObject storeJson = JSONObject.parseObject(storeSp[index]);
                String storeName = storeJson.getString("name");
                String linkName = storeJson.getString("nindex");
                if(filterMap.containsKey(linkName)){
                    filterMap.get(linkName).setStoreName(storeName);
                }
                BigGoFilter_Bean bigGoFilter_Bean = insertBigGoStore(storeJson.getString("name"),storeJson.getString("nindex"),BigGoFilterSource.未篩選);
                if(bigGoFilter_Bean != null){
                    if(!filterMap.containsKey(linkName)){
                        newBigGoFilterList.add(bigGoFilter_Bean);
                    }
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return newBigGoFilterList;
    }
    public BigGoFilter_Bean insertBigGoStore(String storeName, String linkName, BigGoFilterSource bigGoFilterSource){
        BigGoFilter_Bean bigGoFilter_Bean = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String query = "BEGIN TRY BEGIN TRANSACTION " +
                    "if EXISTS (select 1 from BigGoFilter where link_name = ?) " +
                    "BEGIN " +
                    "update BigGoFilter set store_name = ?, exist = ? where id = (select id from BigGoFilter where link_name = ?) " +
                    "END ELSE BEGIN " +
                    "insert into BigGoFilter (store_name,link_name,classify) values (?,?,?) " +
                    "END " +
                    "select id,classify from BigGoFilter where link_name = ? and exist = ? " +
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
            PreparedStatement.setString(1, linkName);
            PreparedStatement.setString(2, storeName);
            PreparedStatement.setBoolean(3, true);
            PreparedStatement.setString(4, linkName);
            PreparedStatement.setString(5, storeName);
            PreparedStatement.setString(6, linkName);
            PreparedStatement.setInt(7, bigGoFilterSource.ordinal());
            PreparedStatement.setString(8, linkName);
            PreparedStatement.setBoolean(9, true);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                bigGoFilter_Bean = new BigGoFilter_Bean();
                bigGoFilter_Bean.setId(Rs.getInt(1));
                bigGoFilter_Bean.setStoreName(storeName);
                bigGoFilter_Bean.setLinkName(linkName);
                bigGoFilter_Bean.setSource(bigGoFilterSource);
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return bigGoFilter_Bean;
    }
    public boolean updateBigGoFilter(int id, String storeName, String linkName){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("update BigGoFilter set store_name = ?, link_name = ? where id = ?");
            PreparedStatement.setString(1, storeName);
            PreparedStatement.setString(2, linkName);
            PreparedStatement.setInt(3, id);
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
    private boolean initialBigGoFilterExist(){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("update BigGoFilter set exist = ? where classify != ?");
            PreparedStatement.setBoolean(1, false);
            PreparedStatement.setInt(2, BigGoFilterSource.拍賣.ordinal());
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
    public boolean isBigGoFilterExist(String lineName){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select 1 from BigGoFilter where link_name = ? and exist = ?");
            PreparedStatement.setString(1, lineName);
            PreparedStatement.setBoolean(2, true);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                status = true;
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public boolean setBigGoFilterExistStatus(int id, boolean exist){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("update BigGoFilter set exist = ? where id = ? ");
            PreparedStatement.setBoolean(1, exist);
            PreparedStatement.setInt(2, id);
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
    /** Get all store of BigGo */
    public HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> getBigGoFilter(){
        HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap = new HashMap<>();
        for(BigGoFilterSource bigGoFilterSource : BigGoFilterSource.values()){
            bigGoFilterMap.put(bigGoFilterSource, FXCollections.observableArrayList());
        }
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from BigGoFilter where exist = ?");
            PreparedStatement.setBoolean(1,true);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()){
                BigGoFilterSource bigGoFilterSource = BigGoFilterSource.values()[Rs.getInt("classify")];

                BigGoFilter_Bean BigGoFilter_Bean = new BigGoFilter_Bean();
                BigGoFilter_Bean.setId(Rs.getInt("id"));
                BigGoFilter_Bean.setStoreName(Rs.getString("store_name"));
                BigGoFilter_Bean.setLinkName(Rs.getString("link_name"));
                BigGoFilter_Bean.setSource(bigGoFilterSource);
                bigGoFilterMap.get(bigGoFilterSource).add(BigGoFilter_Bean);
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return bigGoFilterMap;
    }
    public boolean modifyBigGoFilterSource(BigGoFilter_Bean bigGoFilter_Bean, BigGoFilterSource newBigGoFilterSource){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("update BigGoFilter set classify = ? where id = ?");
            PreparedStatement.setInt(1, newBigGoFilterSource.ordinal());
            PreparedStatement.setInt(2, bigGoFilter_Bean.getId());
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
    /** The conditional search for BigGo
     * @param conditionalBigGoSearch_Bean the conditional search for BigGo
     * */
    public void ConditionalSearchForBigGo(
            ProductWaitingConfirm_Controller productWaitingConfirm_Controller,
            HashMap<BigGoFilterSource, BigGoSearchResult_Bean> bigGoSearchResultMap,
            ConditionalBigGoSearch_Bean conditionalBigGoSearch_Bean){
        String encoderString = ToolKit.encodeURIComponent(conditionalBigGoSearch_Bean.getBigGoSearchText());
        String url = "http://biggo.com.tw/s/" + encoderString + getBigGoSort(conditionalBigGoSearch_Bean) + getBigGoGreaterThanBatchPrice(conditionalBigGoSearch_Bean);
        bigGoSearchResultMap.get(BigGoFilterSource.商城).setHyperlink(url + getBigGoFilterUrl(BigGoFilterSource.商城, conditionalBigGoSearch_Bean.getBigGoFilterMap()));
        bigGoSearchResultMap.get(BigGoFilterSource.拍賣).setHyperlink(url + getBigGoFilterUrl(BigGoFilterSource.拍賣, conditionalBigGoSearch_Bean.getBigGoFilterMap()));
        bigGoSearchResultMap.get(BigGoFilterSource.未篩選).setHyperlink(url + getBigGoFilterUrl(BigGoFilterSource.未篩選, conditionalBigGoSearch_Bean.getBigGoFilterMap()));
        url = bigGoSearchResultMap.get(BigGoFilterSource.未篩選).getHyperlink();

        BigGoSearchResult_Bean allStore_bigGoFilter = bigGoSearchResultMap.get(BigGoFilterSource.未篩選);
        BigGoSearchResult_Bean store_bigGoFilter = bigGoSearchResultMap.get(BigGoFilterSource.商城);
        BigGoSearchResult_Bean bid_bigGoFilter = bigGoSearchResultMap.get(BigGoFilterSource.拍賣);
        ERPApplication.Logger.info("[BigGo] : 搜尋KeyWord -> " + conditionalBigGoSearch_Bean.getBigGoSearchText() + "   " + url);
        Response Response;
        try {
            int page = 1;
            String p = "&p=";
            String tmp = "";
            do{
                if(productWaitingConfirm_Controller.stopBigGoSearch.get()){
                    productWaitingConfirm_Controller.stopBigGoSearch();
                    break;
                }
                tmp = url + p + page;
                ERPApplication.Logger.info("[BigGo] : URL -> " + tmp);
                Response = Jsoup.connect(tmp).header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36").ignoreHttpErrors(true).ignoreContentType(true).method(Method.GET).execute();
                if (Response.body().contains("搜尋不到")){
                    productWaitingConfirm_Controller.stopBigGoSearch();
                    Platform.runLater(() -> DialogUI.MessageDialog("※ 找不到相關品項，請重新輸入"));
                    break;
                }else{
                    Document Doc = Response.parse();

                    Elements Elements = Doc.select("div.product-container>div.product-row>div.d-flex");
                    for (Element Element : Elements) {
                        if(!Element.select("div.d-flex.w100>img").attr("src").equals("/images/tag_spotlight_l_tw@2x.png")){
                            BigGoProductInfo_Bean bigGoProductInfo_Bean = new BigGoProductInfo_Bean();
                            bigGoProductInfo_Bean.setProductName(Element.select("div.list-product-name.line-clamp-2>a").text());
                            String productPrice = Element.select("span.price").text();
                            if(productPrice.contains("~")){
                                productPrice = productPrice.substring(productPrice.lastIndexOf("$")+1);
                            }
                            bigGoProductInfo_Bean.setProductPrice(productPrice);
                            getBigGoPictureImage(bigGoProductInfo_Bean,
                                    Element.select("div.list-img-wrap.d-flex.justify-content-center.align-items-center.position-relative>div>a>img").attr("src"));
                            bigGoProductInfo_Bean.setStoreName(Element.select("div.line-clamp-1").text());
                            bigGoProductInfo_Bean.setStoreHyperlink("https://biggo.com.tw" + Element.select("div.list-product-name.line-clamp-2>a").attr("href"));

                            //  放到商城or拍賣
                            BigGoFilterSource bigGoFilterSource = findProductLinkName(bigGoProductInfo_Bean.getStoreHyperlink(), conditionalBigGoSearch_Bean.getBigGoFilterMap());
                            if(bigGoFilterSource == BigGoFilterSource.商城){
                                setBigGoProductInfo_Bean(bigGoProductInfo_Bean, store_bigGoFilter, productWaitingConfirm_Controller);
                            }else if(bigGoFilterSource == BigGoFilterSource.拍賣){
                                setBigGoProductInfo_Bean(bigGoProductInfo_Bean, bid_bigGoFilter, productWaitingConfirm_Controller);
                            }
                            setBigGoProductInfo_Bean(bigGoProductInfo_Bean, allStore_bigGoFilter, productWaitingConfirm_Controller);
                        }
                    }
                    if(Doc.text().contains("已無更多符合的內容")) {
                        productWaitingConfirm_Controller.stopBigGoSearch();
                        break;
                    }else {
//                        url = "https://biggo.com.tw" + Doc.select("a[rel=nofollow nofollow next]").get(0).attr("href");
                        page++;
                    }
                }
            }while(true);
        } catch (Exception Ex) {
            productWaitingConfirm_Controller.stopBigGoSearch();
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    private String getBigGoFilterUrl(BigGoFilterSource bigGoFilterSource, HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap){
        StringBuilder Url = new StringBuilder();
        for(BigGoFilterSource BigGoFilterSource : BigGoFilterSource.values()){
            if((bigGoFilterSource == Product_Enum.BigGoFilterSource.商城 && BigGoFilterSource == Product_Enum.BigGoFilterSource.商城) ||
                (bigGoFilterSource == Product_Enum.BigGoFilterSource.拍賣 && BigGoFilterSource == Product_Enum.BigGoFilterSource.拍賣) ||
                (bigGoFilterSource == Product_Enum.BigGoFilterSource.未篩選 && BigGoFilterSource != Product_Enum.BigGoFilterSource.未篩選)
            ){
                ObservableList<BigGoFilter_Bean> bigGoFilterList = bigGoFilterMap.get(BigGoFilterSource);
                for(BigGoFilter_Bean BigGoFilter_Bean : bigGoFilterList){
                    Url.append("&c[]=").append(BigGoFilter_Bean.getLinkName());
                }
            }
        }
        return Url.toString();
    }
    private String getBigGoSort(ConditionalBigGoSearch_Bean ConditionalBigGoSearch_Bean){
        if(ConditionalBigGoSearch_Bean.getSortByLargeToSmall()) return "/?m=cp&sort=hp";
        return "/?m=cp&sort=lp&view=list";
    }
    private String getBigGoGreaterThanBatchPrice(ConditionalBigGoSearch_Bean ConditionalBigGoSearch_Bean){
        if(ConditionalBigGoSearch_Bean.getGreaterThanBatchPrice())  return "&price=" + ConditionalBigGoSearch_Bean.getBatchPrice();
        return "&price=0";
    }
    private void setBigGoProductInfo_Bean(BigGoProductInfo_Bean bigGoProductInfo_Bean, BigGoSearchResult_Bean bigGoSearchResult_Bean, ProductWaitingConfirm_Controller productWaitingConfirm_Controller){
        if (bigGoSearchResult_Bean.getNowPage() == 0) {
            bigGoSearchResult_Bean.setNowPage(1);
        }
        if(bigGoSearchResult_Bean.getProductList() == null)
            bigGoSearchResult_Bean.setProductList(new ArrayList<>());
        ArrayList<BigGoProductInfo_Bean> bigGoProductList = bigGoSearchResult_Bean.getProductList();
        bigGoProductList.add(bigGoProductInfo_Bean);
        if(bigGoSearchResult_Bean.getTotalPage() != (bigGoProductList.size() / 10 + 1)){
            bigGoSearchResult_Bean.setTotalPage((bigGoProductList.size() / 10 + 1));
            if(bigGoSearchResult_Bean.getBigGoPageList() == null)
                bigGoSearchResult_Bean.setBigGoPageList(FXCollections.observableArrayList());
            bigGoSearchResult_Bean.getBigGoPageList().add(bigGoSearchResult_Bean.getTotalPage());
        }
        if (bigGoProductList.size() < 10){
            Platform.runLater(() -> bigGoSearchResult_Bean.getVBox().getChildren().add(productWaitingConfirm_Controller.generateBigGoProductGridPane(bigGoProductInfo_Bean)));
        }else{
            Platform.runLater(productWaitingConfirm_Controller::refreshBigGoPageComponent);
        }
    }
    private BigGoFilterSource findProductLinkName(String linkName, HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap){
        if(linkName != null){
            for(BigGoFilterSource bigGoFilterSource : BigGoFilterSource.values()){
                ObservableList<BigGoFilter_Bean> bigGoFilterList = bigGoFilterMap.get(bigGoFilterSource);
                for(BigGoFilter_Bean bigGoFilter_Bean : bigGoFilterList){
                    if(linkName.contains(bigGoFilter_Bean.getLinkName())){
                        return bigGoFilterSource;
                    }
                }
            }
        }
        return BigGoFilterSource.未篩選;
    }
    /** Download th picture */
    public boolean downloadPicture(Image Image, String outfilePath){
        try{
            BufferedImage BufferedImage  = SwingFXUtils.fromFXImage(Image, null);
            File file = new File(outfilePath + "\\商品圖片下載.jpg");
            ImageIO.write(BufferedImage, "jpg", file);
            return true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            return false;
        }
    }

    /** Update describe or remark of product in database
     * @param EditProductDescribeOrRemark choose to modify describe or remark of product
     * @param text the text of describe or remark
     * @param WaitConfirmProductInfo_Bean the product info of wait confirm
     * */
    public boolean saveEditProductDescribeOrRemark(EditProductDescribeOrRemark EditProductDescribeOrRemark, String text, WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品)
                Query = Query + "update " + WaitConfirmProductInfo_Bean.getWaitConfirmTable().getTableName() + " set " + EditProductDescribeOrRemark.getColumnName() + " = ? where ProductCode = ? ";
            else if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.進銷存商品)
                Query = Query + "update " + WaitConfirmProductInfo_Bean.getWaitConfirmTable().getTableName() + " set " + EditProductDescribeOrRemark.getColumnName() + " = ? where ISBN = ? ";
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
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,text);
            if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品)
                PreparedStatement.setString(2,WaitConfirmProductInfo_Bean.getProductCode());
            else if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.進銷存商品)
                PreparedStatement.setString(2,WaitConfirmProductInfo_Bean.getISBN());
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean updateCheckStoreNewFirstCategory(String productCode, String newFistCategory){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update CheckStore set NewFirstCategory = ? where ProductCode = ? " +
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
            PreparedStatement.setString(1,newFistCategory);
            PreparedStatement.setString(2,productCode);
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Delete product in database
     * */
    public boolean deleteCheckStoreProduct(String productCode){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "delete from CheckStore where ProductCode = ? " +
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
            PreparedStatement.setString(1,productCode);
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Archive product in database */
    public boolean archiveCheckStoreProduct(String productCode) {
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update Store set Status= ? where ProductCode= ? " +
                    "delete from CheckStore where ProductCode= ? " +
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
            PreparedStatement.setInt(1,Product_Enum.StoreStatus.封存.ordinal());
            PreparedStatement.setString(2,productCode);
            PreparedStatement.setString(3,productCode);
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Ignore product in database */
    public boolean ignoreCheckStoreProduct(String productCode, WaitConfirmStatus WaitConfirmStatus){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update checkStore set Status= ?, PreviousStatus= ? where ProductCode= ? " +
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
            PreparedStatement.setInt(1, Product_Enum.CheckStoreStatus.忽略.ordinal());
            PreparedStatement.setInt(2,WaitConfirmStatus.ordinal());
            PreparedStatement.setString(3,productCode);
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Recovery product in database */
    public boolean recoverCheckStoreProduct(String productCode, int previousStatus){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update CheckStore set Status= ? , PreviousStatus= ? where ProductCode= ? " +
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
            PreparedStatement.setInt(1, previousStatus);
            PreparedStatement.setInt(2,Product_Enum.CheckStoreStatus.忽略.ordinal());
            PreparedStatement.setString(3,productCode);
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Get product url from database
     * @param productCode the code of product
     * */
    public String getProductUrl(String productCode){
        String productUrl = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select Url from Product_Url where ProductCode = ? ");
            PreparedStatement.setString(1,productCode);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                productUrl = Rs.getString(1);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return productUrl;
    }
    /** Get product picture from database
     * @param WaitConfirmProductInfo_Bean the product info of wait confirm
     * */
    public void getProductPicture(WaitConfirmProductInfo_Bean WaitConfirmProductInfo_Bean){
        String Query = "";
        if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品)
            Query = "select Picture1,Picture2,Picture3 from CheckStore where ProductCode = ? ";
        else if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.進銷存商品)
            Query = "select Picture1,Picture2,Picture3 from ProductPicture where ISBN = ? ";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.待確認商品)
                PreparedStatement.setString(1,WaitConfirmProductInfo_Bean.getProductCode());
            else if(WaitConfirmProductInfo_Bean.getWaitConfirmTable() == Product_Enum.WaitConfirmTable.進銷存商品)
                PreparedStatement.setString(1,WaitConfirmProductInfo_Bean.getISBN());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                WaitConfirmProductInfo_Bean.setPicture1(Rs.getString(1));
                WaitConfirmProductInfo_Bean.setPicture2(Rs.getString(2));
                WaitConfirmProductInfo_Bean.setPicture3(Rs.getString(3));
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
    }
    private void getBigGoPictureImage(BigGoProductInfo_Bean BigGoProductInfo_Bean, String ProductPictureUrl){
        try {
            if(ProductPictureUrl != null && !ProductPictureUrl.equals("")){
                ToolKit.SSLAttack();
                Response PictureResponse = Jsoup.connect(ProductPictureUrl).ignoreHttpErrors(true).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36").method(Method.GET).execute();
                ByteArrayInputStream ByteStream = new ByteArrayInputStream(PictureResponse.bodyAsBytes());
                Image Image = new Image(ByteStream);
                BigGoProductInfo_Bean.setProductPicture(Image);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(BigGoProductInfo_Bean.getProductPicture(),null);
                BigGoProductInfo_Bean.setProductPictureBase64(ToolKit.generateBase64(bufferedImage));
            }
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
        }
    }
}
