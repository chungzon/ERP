package ERP.Model.ManageProductCategory;

import ERP.Bean.ManageProductCategory.ProductCategory_Bean;
import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.ToolKit.TemplatePath;
import ERP.ToolKit.ToolKit;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import ERP.Enum.Product.Product_Enum.CategoryLayer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.PaperSize;
import jxl.write.*;
import jxl.write.Label;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/** [ERP.Model] Manage product category */
public class ManageProductCategory_Model {

    private ToolKit ToolKit;
    public ManageProductCategory_Model(){
        this.ToolKit = ERPApplication.ToolKit;
    }

    /** Get newest product category id
     * @param CategoryLayer the layer of category
     * */
    public String getNewestProductCategoryID(CategoryLayer CategoryLayer){
        int CategoryID = 1;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select MAX(convert(int,CategoryID)) from ProductCategory where CategoryLayer = ? ");
            PreparedStatement.setInt(1,CategoryLayer.getLayer());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                CategoryID = Rs.getInt(1)+1;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        if(CategoryLayer == Product_Enum.CategoryLayer.大類別 || CategoryLayer == Product_Enum.CategoryLayer.中類別)
            return ToolKit.fillZero(CategoryID,2);
        else
            return ToolKit.fillZero(CategoryID,3);
    }
    public boolean isCategoryExist(String categoryID, CategoryLayer CategoryLayer){
        boolean isExist = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select 1 from ProductCategory where CategoryID = ? and CategoryLayer = ?");
            PreparedStatement.setString(1,categoryID);
            PreparedStatement.setInt(2,CategoryLayer.getLayer());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isExist;
    }
    /** Insert product category into database
     * @param ProductCategory_Bean the bean of product category
     * */
    public boolean insertProductCategory(ProductCategory_Bean ProductCategory_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("insert into ProductCategory (CategoryID,CategoryName,DiscountQuantity,Discount,PreferentialDiscount,VipDiscount,StartDate,EndDate,CategoryLayer) values (?,?,0,1,1,1,null,null,?)");
            PreparedStatement.setString(1,ProductCategory_Bean.getCategoryID());
            PreparedStatement.setString(2,ProductCategory_Bean.getCategoryName());
            PreparedStatement.setInt(3,ProductCategory_Bean.getCategoryLayer().getLayer());
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    /** Update product category in database
     * @param categoryName the name of category
     * @param ProductCategory_Bean the bean of product category
     * */
    public boolean modifyProductCategory(String categoryName, ProductCategory_Bean ProductCategory_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("update ProductCategory set CategoryName = ? where CategoryID = ? and CategoryLayer = ?");
            PreparedStatement.setString(1,categoryName);
            PreparedStatement.setString(2,ProductCategory_Bean.getCategoryID());
            PreparedStatement.setInt(3,ProductCategory_Bean.getCategoryLayer().getLayer());
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Delete product category in database
     * @param ProductCategory_Bean the bean of product category
     * */
    public boolean deleteProductCategory(ProductCategory_Bean ProductCategory_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("delete from ProductCategory where CategoryID = ? and CategoryLayer = ? ");
            PreparedStatement.setString(1,ProductCategory_Bean.getCategoryID());
            PreparedStatement.setInt(2,ProductCategory_Bean.getCategoryLayer().getLayer());
            PreparedStatement.executeUpdate();
            status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    /** Whether the category have product */
    public boolean isCategoryExistProduct(ProductCategory_Bean ProductCategory_Bean){
        boolean isCategoryExistProduct = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "select top 1 " + ProductCategory_Bean.getCategoryLayer().getColumnName() + " from store_category where " + ProductCategory_Bean.getCategoryLayer().getColumnName() + " = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,ProductCategory_Bean.getCategoryID());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                isCategoryExistProduct = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isCategoryExistProduct;
    }
    /** Search product category
     * @param CategoryLayer the layer of category
     * @param StartCategoryID the start category id for conditional search
     * @param EndCategoryID the end category id for conditional search
     * */
    public ObservableList<ProductCategory_Bean> searchProductCategory(CategoryLayer CategoryLayer, int StartCategoryID, int EndCategoryID){
        ObservableList<ProductCategory_Bean> ObservableList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "select CategoryID,CategoryName,(select SUM(A.InStock) from store A inner join store_category B on A.id = B.store_id where B." + CategoryLayer.getColumnName() + " = ProductCategory.CategoryID) from ProductCategory where CategoryLayer = ? ";

            if(StartCategoryID != 0 && EndCategoryID != 0)
                Query = Query + " and (CategoryID >= ? and CategoryID <= ?)";
            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,CategoryLayer.getLayer());
            if(StartCategoryID != 0 && EndCategoryID != 0){
                PreparedStatement.setInt(2,StartCategoryID);
                PreparedStatement.setInt(3,EndCategoryID);
            }

            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ProductCategory_Bean ProductCategory_Bean = new ProductCategory_Bean();
                String CategoryID = Rs.getString(1);
                String CategoryName = Rs.getString(2);
                int InStock = Rs.getInt(3);
                if(CategoryID.length() == 1)    CategoryID = "0" + CategoryID;
                ProductCategory_Bean.setCategoryID(CategoryID);
                ProductCategory_Bean.setCategoryName(CategoryName);
                ProductCategory_Bean.setCategoryLayer(CategoryLayer);
                ProductCategory_Bean.setInStock(InStock);
                ObservableList.add(ProductCategory_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{   SqlAdapter.closeConnectParameter(PreparedStatement,Rs);    }
        return ObservableList;
    }
    /** Print product category
     * @param ProductCategoryList the list of product category
     * @param StartCategoryID the start category id for conditional search
     * @param EndCategoryID the end category id for conditional search
     * */
    public void printProductCategory(ObservableList<ProductCategory_Bean> ProductCategoryList, String StartCategoryID, String EndCategoryID){
        try{
            String[] ColumnName = {"總類代碼","類別名稱","存量","折扣數量","折扣","優惠折扣","VIP折扣","起始日期","截止日期"};
            File outputFile = new File("outputFile/" + ToolKit.getToday("yyyy-MM-dd") + " 商品類別(" + ProductCategoryList.get(0).getCategoryLayer().name() + ").xls");
            WritableWorkbook WritableWorkbook = Workbook.createWorkbook(outputFile);

//            InputStream FileInput = new ClassPathResource("Template/商品類別、貨單匯出.xls").getInputStream();
            FileInputStream FileInput = new FileInputStream(ResourceUtils.getFile(TemplatePath.ProductCategoryAndOrderExport));
            Workbook workbook = Workbook.getWorkbook(FileInput);
            Sheet Sheet = workbook.getSheet(0); // iam reading data from the sheet1

            WritableSheet WritableSheet = WritableWorkbook.createSheet("商品類別列印",0);

            WritableCellFormat TitleCell = setWritableCellFormat(20, Alignment.CENTRE);
            WritableCellFormat EmptyCell = setWritableCellFormat(16,Alignment.CENTRE);
            WritableCellFormat ColumnCell = setWritableCellFormat(10,Alignment.LEFT);

            int PageMaxCount = 10;
            int PageCount = ProductCategoryList.size()%PageMaxCount == 0 ? ProductCategoryList.size()/PageMaxCount : ProductCategoryList.size()/PageMaxCount+1;

            for(int PageIndex = 0 ; PageIndex < PageCount ; PageIndex++) {
                if(PageIndex%2 == 0)    for(int index = 0 ; index < 47; index++)    WritableSheet.setRowView(index + (PageIndex/2) * 47, Sheet.getRowView(index));
                WritableSheet.addCell(new Label(0, PageIndex*24 - (PageIndex/2), "", TitleCell));
                WritableSheet.mergeCells(0,PageIndex*24 - (PageIndex/2),7,PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0,1 + PageIndex*24 - (PageIndex/2), "商品類別列印(P." + (PageIndex+1) + "/" + PageCount + ")", TitleCell));
                WritableSheet.mergeCells(0,1 + PageIndex*24 - (PageIndex/2),7,2 + PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0, 3 + PageIndex*24 - (PageIndex/2), "                      ", EmptyCell));
                WritableSheet.setRowView(3 + PageIndex*24 - (PageIndex/2), Sheet.getRowView(3));
                WritableSheet.addCell(new Label(0,5 + PageIndex*24 - (PageIndex/2),"起始類別:" + StartCategoryID + "          結束類別:" + EndCategoryID, ColumnCell));
                WritableSheet.mergeCells(0,5 + PageIndex*24 - (PageIndex/2),7,5 + PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0,6 + PageIndex*24 - (PageIndex/2),"",EmptyCell));
                WritableSheet.mergeCells(0,6 + PageIndex*24 - (PageIndex/2),7,6 + PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0,7 + PageIndex*24 - (PageIndex/2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", ColumnCell));
                WritableSheet.mergeCells(0,7 + PageIndex*24 - (PageIndex/2),7,7 + PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0,8 + PageIndex*24 - (PageIndex/2),"No",ColumnCell));
                for(int index=0 ; index < ColumnName.length ; index++)  WritableSheet.addCell(new Label(1+index,8 + PageIndex*24 - (PageIndex/2), ColumnName[index],ColumnCell));
                int ItemCount=0;
                WritableSheet.addCell(new Label(0,9 + PageIndex*24 - (PageIndex/2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", ColumnCell));
                WritableSheet.mergeCells(0,9 + PageIndex*24 - (PageIndex/2),7,9 + PageIndex*24 - (PageIndex/2));
                for(int index = PageIndex*PageMaxCount ; index < (PageIndex + 1)*PageMaxCount ; index++){
                    if(index < ProductCategoryList.size()){
                        WritableSheet.addCell(new Label(0,11 + PageIndex*24 - (PageIndex/2) + ItemCount,""+(ItemCount + 1),ColumnCell));
                        for(int j=0;j < ColumnName.length;j++){
                            if(j == 0)  WritableSheet.addCell(new Label(1+j,11+PageIndex*24-(PageIndex/2)+ItemCount,ProductCategoryList.get(index).getCategoryID(),ColumnCell));
                            if(j == 1)  WritableSheet.addCell(new Label(1+j,11+PageIndex*24-(PageIndex/2)+ItemCount,ProductCategoryList.get(index).getCategoryName(),ColumnCell));
                            if(j == 2)  WritableSheet.addCell(new Label(1+j,11+PageIndex*24-(PageIndex/2)+ItemCount,ProductCategoryList.get(index).getInStock(),ColumnCell));
                        }
                    }
                    ItemCount++;
                }
            }
            SheetSettings SheetSettings = WritableSheet.getSettings();
            SheetSettings.setPaperSize(PaperSize.NOTE);
            SheetSettings.setLeftMargin(0.0);
            SheetSettings.setRightMargin(0.0);
            SheetSettings.setTopMargin(0.0);
            SheetSettings.setBottomMargin(0.0);
            WritableWorkbook.write();
            WritableWorkbook.close();
            workbook.close();
            if(Desktop.isDesktopSupported()){
                Desktop desktop=Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.PRINT))    desktop.print(outputFile);
            }
            else    System.out.println("debug: desktop not supported!");
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            DialogUI.MessageDialog("※ 商品類別列印失敗");
        }
    }
    private WritableCellFormat setWritableCellFormat(int FontSize, Alignment Alignment) throws Exception{
        WritableFont CellFont = new WritableFont(WritableFont.TIMES,FontSize,WritableFont.NO_BOLD);
        WritableCellFormat CellFormat = new WritableCellFormat(CellFont);
        CellFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.NONE);
        CellFormat.setAlignment(Alignment);
        return CellFormat;
    }
}
