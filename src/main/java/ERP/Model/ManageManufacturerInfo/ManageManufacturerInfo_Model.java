package ERP.Model.ManageManufacturerInfo;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Enum.Order.Order_Enum;
import ERP.ERPApplication;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** [ERP.Model] Manage manufacturer info */
public class ManageManufacturerInfo_Model {

    /** generate the newest manufacturer id */
    public String generateNewestManufacturerID(String ManufacturerIDTitle){
        String ManufacturerID = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        String Query = "select dbo.getNewestManufacturerID(?)";
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,ManufacturerIDTitle);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  ManufacturerID = Rs.getString(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ManufacturerID;
    }
    public boolean isManufacturerIDExist(String ManufacturerID, boolean isTestDB){
        boolean isIDExist = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        String Query = "select TOP 1 ObjectID from Manufacturer where ObjectID = ?";
        try {
            PreparedStatement = connect.prepareStatement(Query);
            PreparedStatement.setString(1,ManufacturerID);
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
    /** insert manufacturer into database
     * @param ManufacturerInfo_Bean the bean of manufacturer
     * */
    public boolean insertManufacturer(ObjectInfo_Bean ManufacturerInfo_Bean, boolean isTestDB){
        boolean status = false;
        Connection connect = (isTestDB ? SqlAdapter.getTestDBConnect() : SqlAdapter.getConnect());
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into Manufacturer (ObjectID, ObjectName, ObjectNickName, PersonInCharge, ContactPerson, Email, InvoiceTitle, TaxIDNumber, OrderTax, PayableDiscount, Remark, DefaultPaymentMethod) values(?,?,?,?,?,?,?,?,?,?,?,?)" +
                    "insert into Manufacturer_Phone (Manufacturer_id, ObjectID, Telephone1, Telephone2, Cellphone, Fax) values ((select IDENT_CURRENT('Manufacturer')),?,?,?,?,?)" +
                    "insert into Manufacturer_Address (Manufacturer_id, ObjectID, CompanyAddress, DeliveryAddress, InvoiceAddress) values ((select IDENT_CURRENT('Manufacturer')),?,?,?,?)" +
                    "insert into Manufacturer_PayInfo (Manufacturer_id, ObjectID, PayableDay, CheckTitle, CheckDueDay, DiscountRemittanceFee, RemittanceFee, DiscountPostage, Postage," +
                    "BankID, BankBranch, AccountName, BankAccount, IsCheckoutByMonth) values ((select IDENT_CURRENT('Manufacturer')),?,?,?,?,?,?,?,?,?,?,?,?,?)" +
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
            PreparedStatement.setString(1, ManufacturerInfo_Bean.getObjectID());
            PreparedStatement.setString(2, ManufacturerInfo_Bean.getObjectName());
            PreparedStatement.setString(3, ManufacturerInfo_Bean.getObjectNickName());
            PreparedStatement.setString(4, ManufacturerInfo_Bean.getPersonInCharge());
            PreparedStatement.setString(5, ManufacturerInfo_Bean.getContactPerson());
            PreparedStatement.setString(6, ManufacturerInfo_Bean.getEmail());
            PreparedStatement.setString(7, ManufacturerInfo_Bean.getInvoiceTitle());
            PreparedStatement.setString(8, ManufacturerInfo_Bean.getTaxIDNumber());
            PreparedStatement.setInt(9, ManufacturerInfo_Bean.getOrderTax().ordinal());
            PreparedStatement.setBigDecimal(10, BigDecimal.valueOf(ManufacturerInfo_Bean.getPayableReceivableDiscount()));
            PreparedStatement.setString(11, ManufacturerInfo_Bean.getRemark());
            PreparedStatement.setInt(12, ManufacturerInfo_Bean.getDefaultPaymentMethod().ordinal());

            PreparedStatement.setString(13, ManufacturerInfo_Bean.getObjectID());
            PreparedStatement.setString(14, ManufacturerInfo_Bean.getTelephone1());
            PreparedStatement.setString(15, ManufacturerInfo_Bean.getTelephone2());
            PreparedStatement.setString(16, ManufacturerInfo_Bean.getCellPhone());
            PreparedStatement.setString(17, ManufacturerInfo_Bean.getFax());

            PreparedStatement.setString(18, ManufacturerInfo_Bean.getObjectID());
            PreparedStatement.setString(19, ManufacturerInfo_Bean.getCompanyAddress());
            PreparedStatement.setString(20, ManufacturerInfo_Bean.getDeliveryAddress());
            PreparedStatement.setString(21, ManufacturerInfo_Bean.getInvoiceAddress());

            PreparedStatement.setString(22, ManufacturerInfo_Bean.getObjectID());
            PreparedStatement.setInt(23, ManufacturerInfo_Bean.getPayableDay());
            PreparedStatement.setString(24, ManufacturerInfo_Bean.getCheckTitle());
            PreparedStatement.setInt(25, ManufacturerInfo_Bean.getCheckDueDay());
            PreparedStatement.setInt(26, ManufacturerInfo_Bean.getDiscountRemittanceFee().ordinal());
            PreparedStatement.setInt(27, ManufacturerInfo_Bean.getRemittanceFee());
            PreparedStatement.setInt(28, ManufacturerInfo_Bean.getDiscountPostage().ordinal());
            PreparedStatement.setInt(29, ManufacturerInfo_Bean.getPostage());
            PreparedStatement.setObject(30, ManufacturerInfo_Bean.getBankID());
            PreparedStatement.setString(31, ManufacturerInfo_Bean.getBankBranch());
            PreparedStatement.setString(32, ManufacturerInfo_Bean.getAccountName());
            PreparedStatement.setString(33, ManufacturerInfo_Bean.getBankAccount());
            PreparedStatement.setBoolean(34, ManufacturerInfo_Bean.isCheckoutByMonth());
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
    /** update manufacturer info in database
     * @param ManufacturerInfo_Bean the bean of manufacturer
     * */
    public boolean modifyManufacturer(ObjectInfo_Bean ManufacturerInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update Manufacturer set ObjectName=?,ObjectNickName=?,PersonInCharge=?,ContactPerson=?,Email=?,InvoiceTitle=?,TaxIDNumber=?,OrderTax=?,PayableDiscount=?,Remark=?,DefaultPaymentMethod=? where ObjectID=? " +
                    "update Manufacturer_Phone set Telephone1=?,Telephone2=?,Cellphone=?,Fax=? from Manufacturer A inner join Manufacturer_Phone B on A.id = B.Manufacturer_id where A.ObjectID=? " +
                    "update Manufacturer_Address set CompanyAddress=?,DeliveryAddress=?,InvoiceAddress=? from Manufacturer A inner join Manufacturer_Address B on A.id = B.Manufacturer_id where A.ObjectID=? " +
                    "update Manufacturer_PayInfo set PayableDay=?,CheckTitle=?,CheckDueDay=?,DiscountRemittanceFee=?,RemittanceFee=?,DiscountPostage=?,Postage=?,BankID=?,BankBranch=?,AccountName=?,BankAccount=?,IsCheckoutByMonth=? from Manufacturer A inner join Manufacturer_PayInfo B on A.id = B.Manufacturer_id where A.ObjectID=? " +
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
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, ManufacturerInfo_Bean.getObjectName());
            PreparedStatement.setString(2, ManufacturerInfo_Bean.getObjectNickName());
            PreparedStatement.setString(3, ManufacturerInfo_Bean.getPersonInCharge());
            PreparedStatement.setString(4, ManufacturerInfo_Bean.getContactPerson());
            PreparedStatement.setString(5, ManufacturerInfo_Bean.getEmail());
            PreparedStatement.setString(6, ManufacturerInfo_Bean.getInvoiceTitle());
            PreparedStatement.setString(7, ManufacturerInfo_Bean.getTaxIDNumber());
            PreparedStatement.setInt(8, ManufacturerInfo_Bean.getOrderTax().ordinal());
            PreparedStatement.setBigDecimal(9, BigDecimal.valueOf(ManufacturerInfo_Bean.getPayableReceivableDiscount()));
            PreparedStatement.setString(10, ManufacturerInfo_Bean.getRemark());
            PreparedStatement.setInt(11, ManufacturerInfo_Bean.getDefaultPaymentMethod().ordinal());
            PreparedStatement.setString(12, ManufacturerInfo_Bean.getObjectID());

            PreparedStatement.setString(13, ManufacturerInfo_Bean.getTelephone1());
            PreparedStatement.setString(14, ManufacturerInfo_Bean.getTelephone2());
            PreparedStatement.setString(15, ManufacturerInfo_Bean.getCellPhone());
            PreparedStatement.setString(16, ManufacturerInfo_Bean.getFax());
            PreparedStatement.setString(17, ManufacturerInfo_Bean.getObjectID());

            PreparedStatement.setString(18, ManufacturerInfo_Bean.getCompanyAddress());
            PreparedStatement.setString(19, ManufacturerInfo_Bean.getDeliveryAddress());
            PreparedStatement.setString(20, ManufacturerInfo_Bean.getInvoiceAddress());
            PreparedStatement.setString(21, ManufacturerInfo_Bean.getObjectID());

            PreparedStatement.setInt(22, ManufacturerInfo_Bean.getPayableDay());
            PreparedStatement.setString(23, ManufacturerInfo_Bean.getCheckTitle());
            PreparedStatement.setInt(24, ManufacturerInfo_Bean.getCheckDueDay());
            PreparedStatement.setInt(25, ManufacturerInfo_Bean.getDiscountRemittanceFee().ordinal());
            PreparedStatement.setInt(26, ManufacturerInfo_Bean.getRemittanceFee());
            PreparedStatement.setInt(27, ManufacturerInfo_Bean.getDiscountPostage().ordinal());
            PreparedStatement.setInt(28, ManufacturerInfo_Bean.getPostage());
            PreparedStatement.setObject(29, ManufacturerInfo_Bean.getBankID());
            PreparedStatement.setString(30, ManufacturerInfo_Bean.getBankBranch());
            PreparedStatement.setString(31, ManufacturerInfo_Bean.getAccountName());
            PreparedStatement.setString(32, ManufacturerInfo_Bean.getBankAccount());
            PreparedStatement.setBoolean(33, ManufacturerInfo_Bean.isCheckoutByMonth());
            PreparedStatement.setString(34, ManufacturerInfo_Bean.getObjectID());
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
    /** delete manufacturer info in database */
    public boolean deleteManufacturer(String objectID){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        String Query = "delete from Manufacturer where ObjectID = ? ";
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
    /** Whether the manufacturer have an order
     * @param objectID the Manufacturer ID
     * */
    public boolean isManufacturerExistOrder(String objectID){
        boolean isExistOrder = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        String Query = "select TOP 1 OrderNumber from Orders where OrderSource = ? and ObjectID = ? and status = ?";
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,Order_Enum.OrderObject.廠商.ordinal());
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
    /** print manufacturer info */
    public void printManufacturerInfo(ObservableList<ObjectInfo_Bean> ManufacturerList, String StartManufacturer, String EndManufacturer){
        try{
            DialogUI.MessageDialog("舊版無此功能!");
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            DialogUI.MessageDialog("※ 廠商資料列印失敗");
        }
    }
}
