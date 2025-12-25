package ERP.Model.ManageCustomerInfo;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.ToolKit.ToolKit;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.TemplatePath;
import ERP.View.DialogUI;
import javafx.application.Platform;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/** [ERP.Model] Manage customer info */
public class ManageCustomerInfo_Model {

    private ToolKit ToolKit;
    public ManageCustomerInfo_Model(){
        ToolKit = ERPApplication.ToolKit;
    }
    /** generate the newest customer id
     * @param objectIDCharacter the character corresponding to the customer's area
     * */
    public String getNewestCustomerID(String objectIDCharacter){
        String CustomerID = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        String Query = "select dbo.getNewestCustomerID(?)";
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,objectIDCharacter);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  CustomerID = Rs.getString(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return CustomerID;
    }
    public boolean isCustomerIDExist(String customerID, boolean isTestDB){
        boolean isIDExist = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        String Query = "select TOP 1 ObjectID from Customer where ObjectID = ?";
        try {
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setString(1,customerID);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isIDExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isIDExist;
    }
    public boolean insertCustomer(ObjectInfo_Bean CustomerInfo_Bean, boolean isTestDB){
        boolean status = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into Customer (ObjectID, ObjectName, ObjectNickName, PersonInCharge, ContactPerson, Email, MemberID, InvoiceTitle, TaxIDNumber, OrderTax, ReceivableDiscount, PrintPricing, SaleModel, SaleDiscount,Remark,StoreCode) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" +
                    "insert into Customer_Phone (Customer_id, ObjectID, Telephone1, Telephone2, Cellphone, Fax) values ((select IDENT_CURRENT('Customer')),?,?,?,?,?)" +
                    "insert into Customer_Address (Customer_id, ObjectID, CompanyAddress, DeliveryAddress, InvoiceAddress) values ((select IDENT_CURRENT('Customer')),?,?,?,?)" +
                    "insert into Customer_ReceiveInfo (Customer_id, ObjectID, ReceivableDay, IsCheckoutByMonth) values ((select IDENT_CURRENT('Customer')),?,?,?)" +
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
            ERPApplication.Logger.info(Query);
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setString(1, CustomerInfo_Bean.getObjectID());
            PreparedStatement.setString(2, CustomerInfo_Bean.getObjectName());
            PreparedStatement.setString(3, CustomerInfo_Bean.getObjectNickName());
            PreparedStatement.setString(4, CustomerInfo_Bean.getPersonInCharge());
            PreparedStatement.setString(5, CustomerInfo_Bean.getContactPerson());
            PreparedStatement.setString(6, CustomerInfo_Bean.getEmail());
            PreparedStatement.setString(7, CustomerInfo_Bean.getMemberID());
            PreparedStatement.setString(8, CustomerInfo_Bean.getInvoiceTitle());
            PreparedStatement.setString(9, CustomerInfo_Bean.getTaxIDNumber());
            PreparedStatement.setInt(10, CustomerInfo_Bean.getOrderTax().ordinal());
            PreparedStatement.setBigDecimal(11, BigDecimal.valueOf(CustomerInfo_Bean.getPayableReceivableDiscount()));
            PreparedStatement.setInt(12, CustomerInfo_Bean.getPrintPricing().ordinal());
            PreparedStatement.setInt(13, CustomerInfo_Bean.getSaleModel().ordinal());
            PreparedStatement.setBigDecimal(14, BigDecimal.valueOf(CustomerInfo_Bean.getSaleDiscount()));
            PreparedStatement.setString(15, CustomerInfo_Bean.getRemark());
            PreparedStatement.setString(16, CustomerInfo_Bean.getStoreCode());

            PreparedStatement.setString(17, CustomerInfo_Bean.getObjectID());
            PreparedStatement.setString(18, CustomerInfo_Bean.getTelephone1());
            PreparedStatement.setString(19, CustomerInfo_Bean.getTelephone2());
            PreparedStatement.setString(20, CustomerInfo_Bean.getCellPhone());
            PreparedStatement.setString(21, CustomerInfo_Bean.getFax());

            PreparedStatement.setString(22, CustomerInfo_Bean.getObjectID());
            PreparedStatement.setString(23, CustomerInfo_Bean.getCompanyAddress());
            PreparedStatement.setString(24, CustomerInfo_Bean.getDeliveryAddress());
            PreparedStatement.setString(25, CustomerInfo_Bean.getInvoiceAddress());

            PreparedStatement.setString(26, CustomerInfo_Bean.getObjectID());
            PreparedStatement.setInt(27, CustomerInfo_Bean.getReceivableDay());
            PreparedStatement.setBoolean(28, CustomerInfo_Bean.isCheckoutByMonth());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            if(isTestDB){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** update customer info in database
     * @param CustomerInfo_Bean the bean of customer
     * */
    public boolean modifyCustomer(ObjectInfo_Bean CustomerInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Customer set ObjectName=?,ObjectNickName=?,PersonInCharge=?,ContactPerson=?,Email=?,MemberID=?,InvoiceTitle=?,TaxIDNumber=?,OrderTax=?,ReceivableDiscount=?,PrintPricing=?,SaleModel=?,SaleDiscount=?,Remark=?,StoreCode=? where ObjectID = ? " +
                    "update Customer_Phone set Telephone1=?,Telephone2=?,Cellphone=?,Fax=? from Customer A inner join Customer_Phone B on A.id = B.Customer_id where A.ObjectID=? " +
                    "update Customer_Address set CompanyAddress=?,DeliveryAddress=?,InvoiceAddress=? from Customer A inner join Customer_Address B on A.id = B.Customer_id where A.ObjectID=? " +
                    "update Customer_ReceiveInfo set ReceivableDay=?,IsCheckoutByMonth=? from Customer A inner join Customer_ReceiveInfo B on A.id = B.Customer_id where A.ObjectID=? " +
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

//            ERPApplication.Logger.info(Query);
            ERPApplication.Logger.info("modify customer:" + CustomerInfo_Bean.getObjectID() + "  " + CustomerInfo_Bean.getObjectName());
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, CustomerInfo_Bean.getObjectName());
            PreparedStatement.setString(2, CustomerInfo_Bean.getObjectNickName());
            PreparedStatement.setString(3, CustomerInfo_Bean.getPersonInCharge());
            PreparedStatement.setString(4, CustomerInfo_Bean.getContactPerson());
            PreparedStatement.setString(5, CustomerInfo_Bean.getEmail());
            PreparedStatement.setString(6, CustomerInfo_Bean.getMemberID());
            PreparedStatement.setString(7, CustomerInfo_Bean.getInvoiceTitle());
            PreparedStatement.setString(8, CustomerInfo_Bean.getTaxIDNumber());
            PreparedStatement.setInt(9, CustomerInfo_Bean.getOrderTax().ordinal());
            PreparedStatement.setBigDecimal(10, BigDecimal.valueOf(CustomerInfo_Bean.getPayableReceivableDiscount()));
            PreparedStatement.setInt(11, CustomerInfo_Bean.getPrintPricing().ordinal());
            PreparedStatement.setInt(12, CustomerInfo_Bean.getSaleModel().ordinal());
            PreparedStatement.setBigDecimal(13, BigDecimal.valueOf(CustomerInfo_Bean.getSaleDiscount()));
            PreparedStatement.setString(14, CustomerInfo_Bean.getRemark());
            PreparedStatement.setString(15, CustomerInfo_Bean.getStoreCode());
            PreparedStatement.setString(16, CustomerInfo_Bean.getObjectID());

            PreparedStatement.setString(17, CustomerInfo_Bean.getTelephone1());
            PreparedStatement.setString(18, CustomerInfo_Bean.getTelephone2());
            PreparedStatement.setString(19, CustomerInfo_Bean.getCellPhone());
            PreparedStatement.setString(20, CustomerInfo_Bean.getFax());
            PreparedStatement.setString(21, CustomerInfo_Bean.getObjectID());

            PreparedStatement.setString(22, CustomerInfo_Bean.getCompanyAddress());
            PreparedStatement.setString(23, CustomerInfo_Bean.getDeliveryAddress());
            PreparedStatement.setString(24, CustomerInfo_Bean.getInvoiceAddress());
            PreparedStatement.setString(25, CustomerInfo_Bean.getObjectID());

            PreparedStatement.setInt(26, CustomerInfo_Bean.getReceivableDay());
            PreparedStatement.setBoolean(27, CustomerInfo_Bean.isCheckoutByMonth());
            PreparedStatement.setString(28, CustomerInfo_Bean.getObjectID());
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
    /** delete customer info in database */
    public boolean deleteCustomer(String objectID){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        String Query = "delete from Customer where ObjectID = ? ";
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,objectID);
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

    /** Whether the customer have an order */
    public boolean isCustomerExistOrder(String objectID){
        boolean isExistOrder = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        String Query =  "select TOP 1 OrderNumber from Orders where OrderSource = ? and ObjectID = ? and status = ?";
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,Order_Enum.OrderObject.客戶.ordinal());
            PreparedStatement.setString(2,objectID);
            PreparedStatement.setInt(3, Order_Enum.OrderStatus.有效貨單.ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isExistOrder = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isExistOrder;
    }

    /** print customer info
     * @param CustomerList the customer list of TableView
     * @param StartCustomer the start customer info for conditional search
     * @param EndCustomer the end customer info for conditional search
     * */
    public void printCustomerInfo(ObservableList<ObjectInfo_Bean> CustomerList, String StartCustomer, String EndCustomer){
        try{
            String[] ColumnName = {"客戶代碼", "客戶全名", "電話", "FAX", "地址"};

            File outputFile = new File("outputFile/" + ToolKit.getToday("yyyy-MM-dd") + " 客戶資料.xls");
            WritableWorkbook WritableWorkbook = Workbook.createWorkbook(outputFile);

//            InputStream FileInput = new ClassPathResource("Template/客戶資料.xls").getInputStream();
            FileInputStream FileInput = new FileInputStream(ResourceUtils.getFile(TemplatePath.CustomerInfo));
            Workbook workbook = Workbook.getWorkbook(FileInput);
            Sheet Sheet = workbook.getSheet(0); // iam reading data from the sheet1

            WritableSheet WritableSheet = WritableWorkbook.createSheet("客戶資料列印",0);

            WritableCellFormat TitleCell = setWritableCellFormat(20, Alignment.CENTRE);
            WritableCellFormat EmptyCell = setWritableCellFormat(16,Alignment.CENTRE);
            WritableCellFormat ColumnCell = setWritableCellFormat(10,Alignment.LEFT);

            int PageItemCount = 10;
            int PageCount = CustomerList.size()%PageItemCount == 0 ? CustomerList.size() / PageItemCount : CustomerList.size()/PageItemCount+1;
            for(int PageIndex = 0 ; PageIndex < PageCount ; PageIndex++) {
                if (PageIndex % 2 == 0) for (int index = 0; index < 47; index++)
                    WritableSheet.setRowView(index + (PageIndex / 2) * 47, Sheet.getRowView(index));
                WritableSheet.addCell(new Label(0, PageIndex * 24 - (PageIndex / 2), "", TitleCell));
                WritableSheet.mergeCells(0, PageIndex * 24 - (PageIndex / 2), 7, PageIndex * 24 - (PageIndex / 2));
                WritableSheet.addCell(new Label(0, 1 + PageIndex * 24 - (PageIndex / 2), "客戶資料查詢(P." + (PageIndex + 1) + "/" + PageCount + ")", TitleCell));
                WritableSheet.mergeCells(0, 1 + PageIndex * 24 - (PageIndex / 2), 7, 2 + PageIndex * 24 - (PageIndex / 2));
                WritableSheet.addCell(new Label(0, 3 + PageIndex * 24 - (PageIndex / 2), "                      ", EmptyCell));
                WritableSheet.setRowView(3 + PageIndex * 24 - (PageIndex / 2), Sheet.getRowView(3));
                WritableSheet.addCell(new Label(0,5 + PageIndex*24 - (PageIndex/2),"起始客戶:" + StartCustomer + "          結束客戶:" + EndCustomer, ColumnCell));
                WritableSheet.mergeCells(0,5 + PageIndex*24 - (PageIndex/2),7,5 + PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0,6 + PageIndex*24 - (PageIndex/2),"",EmptyCell));
                WritableSheet.mergeCells(0,6 + PageIndex*24 - (PageIndex/2),7,6 + PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0,7 + PageIndex*24 - (PageIndex/2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", ColumnCell));
                WritableSheet.mergeCells(0,7 + PageIndex*24 - (PageIndex/2),7,7 + PageIndex*24 - (PageIndex/2));
                WritableSheet.addCell(new Label(0,8 + PageIndex*24 - (PageIndex/2),"No",ColumnCell));
                for(int index = 0 ; index < ColumnName.length ; index++)  WritableSheet.addCell(new Label(1+index,8 + PageIndex*24 - (PageIndex/2), ColumnName[index],ColumnCell));
                int ItemCount=0;
                WritableSheet.addCell(new Label(0,9 + PageIndex*24 - (PageIndex/2),"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", ColumnCell));
                WritableSheet.mergeCells(0,9 + PageIndex*24 - (PageIndex/2),7,9 + PageIndex*24 - (PageIndex/2));
                for(int index = PageIndex*PageItemCount ; index < (PageIndex + 1)*PageItemCount ; index++){
                    if(index < CustomerList.size()){
                        int row = 11 + PageIndex*24 - (PageIndex/2) + ItemCount;
                        WritableSheet.addCell(new Label(0,row,""+(ItemCount + 1),ColumnCell));
                        for(int j=0;j < ColumnName.length;j++){
                            if(j == 0)  WritableSheet.addCell(new Label(1+j, row,CustomerList.get(index).getObjectID(),ColumnCell));
                            if(j == 1)  WritableSheet.addCell(new Label(1+j, row,CustomerList.get(index).getObjectName(),ColumnCell));
                            if(j == 2)  WritableSheet.addCell(new Label(1+j, row,CustomerList.get(index).getTelephone1(),ColumnCell));
                            if(j == 3)  WritableSheet.addCell(new Label(1+j, row,CustomerList.get(index).getFax(),ColumnCell));
                            if(j == 4)  WritableSheet.addCell(new Label(1+j, row,CustomerList.get(index).getCompanyAddress(),ColumnCell));
                        }
                    }
                    ItemCount++;
                }
            }
            //  欄位寬度
            for(int columnIndex = 0 ; columnIndex < 8; columnIndex++)   WritableSheet.setColumnView(columnIndex,Sheet.getColumnView(columnIndex));

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
            DialogUI.MessageDialog("※ 客戶資料列印失敗");
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
