package ERP.Model.ManagePayableReceivable;

import ERP.Bean.ManagePayableReceivable.*;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.Invoice_Bean;
import ERP.Bean.Order.SearchOrder_Bean;
import ERP.Bean.SystemSetting.IAECrawlerBelong_Bean;
import ERP.Bean.SystemSetting.SystemSettingConfig_Bean;
import ERP.ERPApplication;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.CallConfig;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.TemplatePath;
import ERP.Enum.Invoice.Invoice_Enum;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.SearchMethod;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.SearchColumn;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerStatus;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.IAECrawlerReviewStatus;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.ToolKit;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.PaperSize;
import jxl.write.*;
import jxl.write.Label;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.ResourceUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.Boolean;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static java.sql.Types.INTEGER;

/** [ERP.Model] Manage payable or receivable */
public class ManagePayableReceivable_Model {

    private ToolKit toolKit;
    private ComponentToolKit componentToolKit;
    private CallConfig callConfig;
    private Order_Model order_model;
    private SystemSetting_Model systemSetting_model;
    public ManagePayableReceivable_Model(){
        this.toolKit = ERPApplication.ToolKit;
        this.componentToolKit = toolKit.ComponentToolKit;
        this.callConfig = toolKit.CallConfig;
        this.order_model = toolKit.ModelToolKit.getOrderModel();
        this.systemSetting_model = toolKit.ModelToolKit.getSystemSettingModel();
    }

    private int getOrderNumberLength(){  return Integer.parseInt(systemSetting_model.getSpecificSystemSettingData(SystemSetting_Enum.SystemSettingConfig.貨單號長度));   }
    /** generate the newest order number of payable or receivable
     * @param OrderDate generate order number based on order date
     * */
    public String generateNewestOrderNumberOfPayableOrReceivable(String OrderDate){
        long searchNumber = Long.parseLong(OrderDate.replace("-",""));

        String Query = "select Max(OrderNumber) from PayableReceivable where OrderNumber like ? ";
        long OrderNumber = 0;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,searchNumber + "%");
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  OrderNumber = Rs.getLong(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        if(OrderNumber == 0){
            int orderNumberLength = getOrderNumberLength();
            return searchNumber + toolKit.fillZero("1",orderNumberLength - String.valueOf(searchNumber).length());
        }else
            return String.valueOf(OrderNumber + 1);
    }
    /** generate the newest bank id */
    public int getNewestBankID(){
        int BankID = 0;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select Max(ID) from Bank");
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  BankID = Rs.getInt(1)+1;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return BankID;
    }
    /** get all bank info
     * @param isAddEmptyObject determine whether to add empty object
     * */
    public ObservableList<BankInfo_Bean> getAllBankInfo(boolean isAddEmptyObject){
        ObservableList<BankInfo_Bean> AllBankList = FXCollections.observableArrayList();
        if(isAddEmptyObject) AllBankList.add(new BankInfo_Bean(null));
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from Bank");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                BankInfo_Bean BankInfo_Bean = new BankInfo_Bean(Rs.getInt("id"));
                BankInfo_Bean.setBankCode(toolKit.fillZero(Rs.getString("BankCode"),3));
                BankInfo_Bean.setBankNickName(Rs.getString("BankNickName"));
                BankInfo_Bean.setBankName(Rs.getString("BankName"));
                BankInfo_Bean.setContactPerson1(Rs.getString("ContactPerson1"));
                BankInfo_Bean.setContactPerson2(Rs.getString("ContactPerson2"));
                BankInfo_Bean.setTelephone1(Rs.getString("Telephone1"));
                BankInfo_Bean.setTelephone2(Rs.getString("Telephone2"));
                BankInfo_Bean.setFax(Rs.getString("Fax"));
                BankInfo_Bean.setAddress(Rs.getString("Address"));
                BankInfo_Bean.setRemark(Rs.getString("Remark"));
                AllBankList.add(BankInfo_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return AllBankList;
    }
    /** Whether exist the account of company
     * @param bankID the id of bank
     * @param  BankAccount the account of company
     * */
    public boolean isExistCompanyAccount(int bankID, String BankAccount){
        boolean isExist = false;
        String Query = "select * from CompanyBankInfo where BankID = ?";
        if(BankAccount != null)
            Query = Query + " and BankAccount = ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,bankID);
            if(BankAccount != null)
                PreparedStatement.setString(2,BankAccount);
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
    /** Whether exist the bank code of company
     * @param bankCode the code of bank
     * */
    public boolean isExistBankInfo(String bankCode){
        boolean isExist = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from Bank where BankCode = ?");
            PreparedStatement.setString(1,bankCode);
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
    /** Insert bank info into database
     * @param BankInfo_Bean the bean of bank
     * */
    public boolean insertBankInfo(BankInfo_Bean BankInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("insert into Bank (BankCode,BankNickName,BankName,ContactPerson1,ContactPerson2,Telephone1,Telephone2,Fax,Address,Remark) values (?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement.setString(1,BankInfo_Bean.getBankCode());
            PreparedStatement.setString(2,BankInfo_Bean.getBankNickName());
            PreparedStatement.setString(3,BankInfo_Bean.getBankName());
            PreparedStatement.setString(4,BankInfo_Bean.getContactPerson1());
            PreparedStatement.setString(5,BankInfo_Bean.getContactPerson2());
            PreparedStatement.setString(6,BankInfo_Bean.getTelephone1());
            PreparedStatement.setString(7,BankInfo_Bean.getTelephone2());
            PreparedStatement.setString(8,BankInfo_Bean.getFax());
            PreparedStatement.setString(9,BankInfo_Bean.getAddress());
            PreparedStatement.setString(10,BankInfo_Bean.getRemark());
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
    /** Update bank info in database
     * @param BankInfo_Bean the bean of bank
     * */
    public boolean modifyBankInfo(BankInfo_Bean BankInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("update Bank set BankNickName = ?, BankName = ?, ContactPerson1 = ?," +
                    "ContactPerson2 = ?,Telephone1 = ?, Telephone2 = ?,Fax = ?,Address = ?, Remark = ? where ID = ?");
            PreparedStatement.setString(1,BankInfo_Bean.getBankNickName());
            PreparedStatement.setString(2,BankInfo_Bean.getBankName());
            PreparedStatement.setString(3,BankInfo_Bean.getContactPerson1());
            PreparedStatement.setString(4,BankInfo_Bean.getContactPerson2());
            PreparedStatement.setString(5,BankInfo_Bean.getTelephone1());
            PreparedStatement.setString(6,BankInfo_Bean.getTelephone2());
            PreparedStatement.setString(7,BankInfo_Bean.getFax());
            PreparedStatement.setString(8,BankInfo_Bean.getAddress());
            PreparedStatement.setString(9,BankInfo_Bean.getRemark());
            PreparedStatement.setInt(10,BankInfo_Bean.getBankID());
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
    /** Insert company account into database
     * @param CompanyBank_Bean the bean of company account
     * */
    public boolean insertCompanyAccount(CompanyBank_Bean CompanyBank_Bean){
        boolean status;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into CompanyBankInfo (BankID,BankAccount,BankBranch,BankAccountName,AccountNickName,Remark) VALUES (?,?,?,?,?,?) " +
                    "select IDENT_CURRENT('CompanyBankInfo')" +
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
            PreparedStatement.setInt(1, CompanyBank_Bean.getBankID());
            PreparedStatement.setString(2, CompanyBank_Bean.getBankAccount());
            PreparedStatement.setString(3, CompanyBank_Bean.getBankBranch());
            PreparedStatement.setString(4, CompanyBank_Bean.getBankAccountName());
            PreparedStatement.setString(5, CompanyBank_Bean.getAccountNickName());
            PreparedStatement.setString(6, CompanyBank_Bean.getRemark());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                CompanyBank_Bean.setCompanyBankInfo_id(Rs.getInt(1));
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            status = false;
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    /** Update company account in database
     * @param CompanyBank_Bean the bean of company account
     * */
    public boolean modifyCompanyAccount(CompanyBank_Bean CompanyBank_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update CompanyBankInfo set BankID=?,BankBranch=?,BankAccountName=?,AccountNickName=?,Remark=? where id = ? " +
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
            PreparedStatement.setInt(1, CompanyBank_Bean.getBankID());
            PreparedStatement.setString(2, CompanyBank_Bean.getBankBranch());
            PreparedStatement.setString(3, CompanyBank_Bean.getBankAccountName());
            PreparedStatement.setString(4, CompanyBank_Bean.getAccountNickName());
            PreparedStatement.setString(5, CompanyBank_Bean.getRemark());
            PreparedStatement.setObject(6, CompanyBank_Bean.getCompanyBankInfo_id());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    /** Delete company account in database */
    public boolean deleteCompanyAccount(CompanyBank_Bean CompanyBank_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from CompanyBankInfo where id = ? " +
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
            PreparedStatement.setInt(1, CompanyBank_Bean.getCompanyBankInfo_id());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    /** Get bank info of company
     * @param isAddEmptyObject determine whether to add empty object
     * */
    public ObservableList<CompanyBank_Bean> getCompanyBankInfo(boolean isAddEmptyObject) {
        ObservableList<CompanyBank_Bean> CompanyBankInfoList = FXCollections.observableArrayList();
        if(isAddEmptyObject){
            CompanyBank_Bean emptyCompanyBank_Bean = new CompanyBank_Bean();
            emptyCompanyBank_Bean.setCompanyBankInfo_id(null);
            emptyCompanyBank_Bean.setBankID(0);
            emptyCompanyBank_Bean.setBankAccount("");
            emptyCompanyBank_Bean.setBankNickName("");
            emptyCompanyBank_Bean.setBankBranch("");
            emptyCompanyBank_Bean.setBankAccountName("");
            emptyCompanyBank_Bean.setBankAccount("");
            emptyCompanyBank_Bean.setBankCode("");
            CompanyBankInfoList.add(emptyCompanyBank_Bean);
        }
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select A.id,A.BankID,A.AccountNickName,B.BankCode,B.BankNickName,B.BankName,A.BankBranch,A.BankAccount,A.BankAccountName,A.Remark from CompanyBankInfo A left join Bank B on A.BankID = B.ID");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                CompanyBank_Bean CompanyBank_Bean = new CompanyBank_Bean();
                CompanyBank_Bean.setCompanyBankInfo_id(Rs.getInt(1));
                CompanyBank_Bean.setBankID(Rs.getInt(2));
                CompanyBank_Bean.setAccountNickName(Rs.getString(3));
                CompanyBank_Bean.setBankCode(Rs.getString(4));
                CompanyBank_Bean.setBankNickName(Rs.getString(5));
                CompanyBank_Bean.setBankName(Rs.getString(6));
                CompanyBank_Bean.setBankBranch(Rs.getString(7));
                CompanyBank_Bean.setBankAccount(Rs.getString(8));
                CompanyBank_Bean.setBankAccountName(Rs.getString(9));
                CompanyBank_Bean.setRemark(Rs.getString(10));
                CompanyBankInfoList.add(CompanyBank_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return CompanyBankInfoList;
    }
    /** Search none pay or receive order
     * @param orderObject the object of order
     * @param orderSource the source of order
     * @param objectID the id of object
     * @param startDate the start date for conditional search
     * @param endDate the end date for conditional search
     * */
    public ObservableList<SearchOrder_Bean> searchNonePayReceiveOrder(OrderObject orderObject, OrderSource orderSource, String objectID, String startDate, String endDate){
        ObservableList<SearchOrder_Bean> nonePayReceiveOrderList = FXCollections.observableArrayList();
        String Query = getOrderSourceQuery(orderSource);

        if(orderSource == OrderSource.進貨單 || orderSource == OrderSource.進貨子貨單 ||
                orderSource == OrderSource.出貨單 || orderSource == OrderSource.出貨子貨單) {
            Query = Query + "A.AlreadyOrderDate is not null and A.AlreadyOrderNumber is not null and ";
        }else if(orderSource == OrderSource.待入倉單 || orderSource == OrderSource.待出貨單) {
            Query = Query + "A.WaitingOrderDate is not null and A.WaitingOrderNumber is not null and A.AlreadyOrderDate is null and A.AlreadyOrderNumber is null and ";
        }

        Query = Query + "A.ObjectID = ? and A.isCheckout = ? and ";
        Query = Query + getOrderDateQuery(orderSource);

//        if(orderSource == Order_Enum.OrderSource.待入倉單 || orderSource == Order_Enum.OrderSource.進貨單 ||
//                orderSource == Order_Enum.OrderSource.待出貨單 || orderSource == Order_Enum.OrderSource.出貨單)
//            Query = Query + " and NOT EXISTS(select 1 from SubBill where Order_id = A.id)";

        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,orderSource.getOrderObject().ordinal());
            PreparedStatement.setString(2,objectID);
            PreparedStatement.setInt(3,CheckoutStatus.未結帳.ordinal());
            PreparedStatement.setString(4,startDate);
            PreparedStatement.setString(5,endDate);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                SearchOrder_Bean searchOrder_bean = new SearchOrder_Bean();
                searchOrder_bean.setCheckBoxSelect(true);
                searchOrder_bean.setOrderSource(orderSource);
                searchOrder_bean.setId(Rs.getInt(1));
                searchOrder_bean.setOrderNumber(Rs.getString(2));
                searchOrder_bean.setOrderDate(Rs.getString(3));
                searchOrder_bean.setOrderObject(orderObject);
                searchOrder_bean.setObjectID(Rs.getString(4));
                searchOrder_bean.setObjectName(Rs.getString(5));
                searchOrder_bean.setPersonInCharge(Rs.getString(6));
                searchOrder_bean.setNumberOfItems(Rs.getInt(7));
                searchOrder_bean.setTotalPriceIncludeTax(Rs.getInt(8));
                searchOrder_bean.setInvoiceNumber(Rs.getString(9));
                searchOrder_bean.setProjectName(Rs.getString(10));
                searchOrder_bean.setProjectTotalPriceIncludeTax(Rs.getString(11));
                searchOrder_bean.setProjectDifferentPrice(Rs.getString(12));
                searchOrder_bean.setIsCheckout(CheckoutStatus.values()[Rs.getInt(13)].value());
                if(orderSource != OrderSource.進貨退貨單 && orderSource != OrderSource.出貨退貨單)
                    searchOrder_bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[Rs.getInt(14)]);
                if(orderSource == OrderSource.進貨子貨單 || orderSource == OrderSource.出貨子貨單){
                    searchOrder_bean.setWaitingOrderDate(Rs.getString(15));
                    searchOrder_bean.setWaitingOrderNumber(Rs.getString(16));
                    searchOrder_bean.setAlreadyOrderDate(Rs.getString(17));
                    searchOrder_bean.setAlreadyOrderNumber(Rs.getString(18));
                }else if(orderSource == OrderSource.待入倉單 || orderSource == OrderSource.待出貨單){
                    searchOrder_bean.setWaitingOrderNumber(Rs.getString(2));
                    searchOrder_bean.setWaitingOrderDate(Rs.getString(3));
                    searchOrder_bean.setQuotationDate(Rs.getString(15));
                    searchOrder_bean.setQuotationNumber(Rs.getString(16));
                    searchOrder_bean.setAlreadyOrderNumber(Rs.getString(17));
                    searchOrder_bean.setAlreadyOrderDate(Rs.getString(18));
                }else if(orderSource == OrderSource.進貨單 || orderSource == OrderSource.出貨單){
                    searchOrder_bean.setAlreadyOrderNumber(Rs.getString(2));
                    searchOrder_bean.setAlreadyOrderDate(Rs.getString(3));
                    searchOrder_bean.setQuotationDate(Rs.getString(15));
                    searchOrder_bean.setQuotationNumber(Rs.getString(16));
                    searchOrder_bean.setWaitingOrderDate(Rs.getString(17));
                    searchOrder_bean.setWaitingOrderNumber(Rs.getString(18));
                }

                /*
                (1) O 母貨單:有建立分期收/付款
                (2) O 沒有子貨單的母貨單
                (3) O 沒有分期母貨單的子貨單
                (4) X 有分期母貨單的子貨單
                (5) X 有子貨單的母貨單
                母貨單：if 有建立分期收/付款、則優先權最高  else  只搜尋沒有建立子貨單的母貨單
                子貨單：if 母貨單有建立分期收/付款、則不搜尋 else 搜尋
                **/
//                boolean isMainOrderExistInstallment = false;
                if(orderSource == OrderSource.待入倉單 || orderSource == OrderSource.待出貨單 ||
                        orderSource == OrderSource.進貨單 || orderSource == OrderSource.出貨單){
                    boolean isMainOrderExistInstallment = order_model.findNoneCheckoutInstallment(searchOrder_bean);
                    if(!isMainOrderExistInstallment && order_model.isMainOrderExistSubBill(orderObject, searchOrder_bean.getWaitingOrderNumber(),null,null)){
                        continue;
                    }
                }else if(orderSource == OrderSource.進貨子貨單 || orderSource == OrderSource.出貨子貨單){
                    if(order_model.isMainOrderExistInstallment(orderObject, searchOrder_bean.getWaitingOrderNumber())){
                        continue;
                    }
                }
                nonePayReceiveOrderList.add(searchOrder_bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return nonePayReceiveOrderList;
    }
    private String getOrderSourceQuery(Order_Enum.OrderSource OrderSource){
        if(OrderSource == Order_Enum.OrderSource.進貨子貨單)
            return "select A.id,A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from SubBill A left join Manufacturer B on A.ObjectID = B.ObjectID left join SubBill_ProjectInfo C on A.id = C.SubBill_id left join Orders_InvoiceInfo D on A.id = D.SubBill_id and (D.Invalid != 1 AND ((D.Order_id IS NOT NULL AND D.SubBill_id IS NULL) OR (D.Order_id IS NULL AND D.SubBill_id IS NOT NULL))) left join SubBill_Price E on A.id = E.SubBill_id where A.OrderSource = ? and A.Status = '0' and ";
        else if(OrderSource == Order_Enum.OrderSource.出貨子貨單)
            return "select A.id,A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from SubBill A left join Customer B on A.ObjectID = B.ObjectID left join SubBill_ProjectInfo C on A.id = C.SubBill_id left join Orders_InvoiceInfo D on A.id = D.SubBill_id and (D.Invalid != 1 AND ((D.Order_id IS NOT NULL AND D.SubBill_id IS NULL) OR (D.Order_id IS NULL AND D.SubBill_id IS NOT NULL))) left join SubBill_Price E on A.id = E.SubBill_id where A.OrderSource = ? and A.status = '0' and ";
        else if(OrderSource == Order_Enum.OrderSource.待入倉單)
            return "select A.id,A.WaitingOrderNumber, A.WaitingOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.AlreadyOrderNumber, A.AlreadyOrderDate from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id and (D.Invalid != 1 AND ((D.Order_id IS NOT NULL AND D.SubBill_id IS NULL) OR (D.Order_id IS NULL AND D.SubBill_id IS NOT NULL))) left join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.status = '0' and ";
        else if(OrderSource == Order_Enum.OrderSource.待出貨單)
            return "select A.id,A.WaitingOrderNumber, A.WaitingOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.AlreadyOrderNumber, A.AlreadyOrderDate from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id and (D.Invalid != 1 AND ((D.Order_id IS NOT NULL AND D.SubBill_id IS NULL) OR (D.Order_id IS NULL AND D.SubBill_id IS NOT NULL))) left join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.status = '0' and ";
        else if(OrderSource == Order_Enum.OrderSource.進貨單)
            return "select A.id,A.AlreadyOrderNumber, A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber from Orders A left join Manufacturer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id and (D.Invalid != 1 AND ((D.Order_id IS NOT NULL AND D.SubBill_id IS NULL) OR (D.Order_id IS NULL AND D.SubBill_id IS NOT NULL))) left join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.status = '0' and ";
        else if(OrderSource == Order_Enum.OrderSource.出貨單)
            return "select A.id,A.AlreadyOrderNumber, A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id and (D.Invalid != 1 AND ((D.Order_id IS NOT NULL AND D.SubBill_id IS NULL) OR (D.Order_id IS NULL AND D.SubBill_id IS NOT NULL))) left join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.status = '0' and ";
        else if(OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
            return "select A.id,A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceIncludeTax, '', C.ProjectName, '', '', A.isCheckout from ReturnOrder A left join Customer B on A.ObjectID = B.ObjectID left join ReturnOrder_ProjectInfo C on A.id = C.ReturnOrder_id left join ReturnOrder_Price D on A.id = D.ReturnOrder_id where A.OrderSource = ? and A.status = '0' and ";
        else    return "";
    }
    private String getOrderDateQuery(Order_Enum.OrderSource OrderSource){
        if(OrderSource == Order_Enum.OrderSource.待入倉單 || OrderSource == Order_Enum.OrderSource.待出貨單)
            return "A.WaitingOrderDate between ? and ?";
        if(OrderSource == Order_Enum.OrderSource.進貨子貨單 || OrderSource == Order_Enum.OrderSource.進貨單 ||
                OrderSource == Order_Enum.OrderSource.出貨子貨單 || OrderSource == Order_Enum.OrderSource.出貨單)
            return "A.AlreadyOrderDate between ? and ?";
        else if(OrderSource == Order_Enum.OrderSource.進貨退貨單 || OrderSource == Order_Enum.OrderSource.出貨退貨單)
            return "A.OrderDate between ? and ?";
        return "";
    }
    public boolean isCheckNumberExist(String checkNumber){
        boolean isExist = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select 1 from PayableReceivable where CheckNumber = ?");
            PreparedStatement.setString(1,checkNumber);
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
    public boolean isPayableReceivableNumberExist(String orderNumber){
        boolean isNumberExist = false;
        String Query = "select 1 from PayableReceivable where OrderNumber = ? ";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,orderNumber);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) isNumberExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            SqlAdapter.closeConnectParameter(PreparedStatement, Rs);
        }
        return isNumberExist;
    }
    /** Insert payable or receivable into database
     * @param PayableReceivable_Bean the bean of payable or receivable
     * */
    public boolean insertPayableReceivable(PayableReceivable_Bean PayableReceivable_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "insert into PayableReceivable (OrderNumber,OrderDate,OrderObject, ObjectID, CheckNumber, CheckDueDate, CompanyBank_id, ObjectBank_id ,ObjectBankBranch, " +
                    "ObjectBankAccount, ObjectAccountName, ObjectPerson, InvoiceNumber, Remark) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)" +

                    "insert into PayableReceivable_Price (PayableReceivable_id,OrderNumber, Cash, Deposit, OtherDiscount, RemittanceFeeAndPostage, CashDiscount, CheckPrice, OffsetPrice, TotalPriceIncludeTax) values (" +
                    "(select IDENT_CURRENT('PayableReceivable')),?,?,?,?,?,?,?,?,?)" +
                    getInsertPayableReceivableItemQuery(PayableReceivable_Bean) +
                    getModifyOrderCheckoutQuery(PayableReceivable_Bean.getOrderList(), CheckoutStatus.已結帳) +
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
            PreparedStatement.setLong(1, Long.parseLong(PayableReceivable_Bean.getOrderNumber()));
            PreparedStatement.setString(2, PayableReceivable_Bean.getOrderDate());
            PreparedStatement.setInt(3, PayableReceivable_Bean.getOrderObject().ordinal());
            PreparedStatement.setString(4, PayableReceivable_Bean.getObjectID());
            PreparedStatement.setString(5, PayableReceivable_Bean.getCheckNumber());
            PreparedStatement.setString(6, PayableReceivable_Bean.getCheckDueDate());
            PreparedStatement.setObject(7, PayableReceivable_Bean.getCompanyBankInfo_id());
            PreparedStatement.setObject(8, PayableReceivable_Bean.getObjectBankBean().getBankID());
            PreparedStatement.setString(9, PayableReceivable_Bean.getObjectBankBranch());
            PreparedStatement.setString(10, PayableReceivable_Bean.getObjectBankAccount());
            PreparedStatement.setString(11,PayableReceivable_Bean.getObjectAccountName());
            PreparedStatement.setString(12, PayableReceivable_Bean.getObjectPerson());
            PreparedStatement.setString(13, PayableReceivable_Bean.getInvoiceNumber());
            PreparedStatement.setString(14, PayableReceivable_Bean.getRemark());

            PreparedStatement.setLong(15, Long.parseLong(PayableReceivable_Bean.getOrderNumber()));
            PreparedStatement.setString(16, PayableReceivable_Bean.getCash());
            PreparedStatement.setString(17, PayableReceivable_Bean.getDeposit());
            PreparedStatement.setString(18, PayableReceivable_Bean.getOtherDiscount());
            PreparedStatement.setString(19, PayableReceivable_Bean.getRemittanceFeeAndPostage());
            PreparedStatement.setString(20, PayableReceivable_Bean.getCashDiscount());
            PreparedStatement.setString(21, PayableReceivable_Bean.getCheckPrice());
            PreparedStatement.setString(22, PayableReceivable_Bean.getOffsetPrice());
            PreparedStatement.setString(23, PayableReceivable_Bean.getTotalPriceIncludeTax());
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
    private String getInsertPayableReceivableItemQuery(PayableReceivable_Bean payableReceivable_bean){
        String query = "";
        ObservableList<SearchOrder_Bean> orderList = payableReceivable_bean.getOrderList();
        for(SearchOrder_Bean searchOrder_bean : orderList){
            if(searchOrder_bean.isCheckBoxSelect()) {
                if(searchOrder_bean.getOrderSource() == OrderSource.待入倉單 || searchOrder_bean.getOrderSource() == OrderSource.待出貨單 ||
                        searchOrder_bean.getOrderSource() == OrderSource.進貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨單) {
                    query = query + "insert into PayableReceivable_Document (PayableReceivable_id, Order_id, OrderObject) values ((select IDENT_CURRENT('PayableReceivable'))," +
                            "'" + searchOrder_bean.getId() + "','" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "') ";
                    if(searchOrder_bean.getInstallment() != null){
                        query = query + "update Orders_Installment set pay_receive_document_id = (select IDENT_CURRENT('PayableReceivable_Document')) where order_id = '" + searchOrder_bean.getId() + "' and installment = '" + (searchOrder_bean.getInstallment()-1) + "' ";
                    }
                }else if(searchOrder_bean.getOrderSource() == OrderSource.進貨子貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨子貨單) {
                    query = query + "insert into PayableReceivable_Document (PayableReceivable_id, SubBill_id, OrderObject) values ((select IDENT_CURRENT('PayableReceivable'))," +
                            "'" + searchOrder_bean.getId() + "','" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "') ";
                }else if(searchOrder_bean.getOrderSource() == OrderSource.進貨退貨單 || searchOrder_bean.getOrderSource() == OrderSource.出貨退貨單) {
                    query = query + "insert into PayableReceivable_Document (PayableReceivable_id, ReturnOrder_id, OrderObject) values ((select IDENT_CURRENT('PayableReceivable'))," +
                            "'" + searchOrder_bean.getId() + "','" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "') ";
                }
            }
        }
        return query;
    }
    private String getModifyOrderCheckoutQuery(ObservableList<SearchOrder_Bean> orderList, CheckoutStatus checkoutStatus){
        //  建立：沒有未結帳的分期付款 or 找不到分期付款  checkout = true
        //  刪除：有未結帳的分期付款 or 找不到分期付款    checkout = false
        String query = "";
        for(SearchOrder_Bean searchOrder_bean : orderList){
            if(searchOrder_bean.isCheckBoxSelect()){
                if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待出貨單 ||
                        searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨單) {
                    if(checkoutStatus == CheckoutStatus.已結帳){
                        query = query + "IF NOT EXISTS(select 1 from Orders_Installment where order_id = '" + searchOrder_bean.getId() + "' and pay_receive_document_id is null) BEGIN ";
                    }else{
                        query = query + "IF NOT EXISTS (select 1 from Orders_Installment where order_id = '" + searchOrder_bean.getId() + "' and pay_receive_document_id is null) BEGIN ";

                        query = query + "update " + searchOrder_bean.getOrderSource().getOrderTableName() + " set isCheckout = '" + checkoutStatus.ordinal() + "' " +
                                "where id = '" + searchOrder_bean.getId() + "' and OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' and status = '0' END ";

                        query = query + "ELSE IF EXISTS(select 1 from Orders_Installment where order_id = '" + searchOrder_bean.getId() + "' and pay_receive_document_id is null) BEGIN ";
                    }
                    query = query + "update " + searchOrder_bean.getOrderSource().getOrderTableName() + " set isCheckout = '" + checkoutStatus.ordinal() + "' " +
                            "where id = '" + searchOrder_bean.getId() + "' and OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' and status = '0' END ";
                }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單) {
                    query = query + "update " + searchOrder_bean.getOrderSource().getOrderTableName() + " set isCheckout = '" + checkoutStatus.ordinal()
                            + "' where OrderNumber = '" + searchOrder_bean.getOrderNumber() + "' and OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' and status = '0' ";
                    if (checkoutStatus == Order_Enum.CheckoutStatus.已結帳) {
                        query = query + "if NOT EXISTS " +
                                "(select 1 from SubBill A inner join Orders B on A.Order_id = B.id where A.Order_id = (select Order_id from SubBill where OrderNumber = '" + searchOrder_bean.getOrderNumber() + "' and OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' and status = '0') and A.isCheckout = '" + Order_Enum.CheckoutStatus.未結帳.ordinal() + "') " +
                                "AND EXISTS(select 1 from Orders C inner join Orders_Price D on C.id = D.Order_id where C.id = (SELECT Order_id FROM SubBill WHERE OrderNumber = '" + searchOrder_bean.getOrderNumber() + "' AND OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' AND status = '0') and D.TotalPriceIncludeTax = (select SUM(G.TotalPriceIncludeTax) from SubBill F inner join SubBill_Price G on F.id = G.SubBill_id where F.Order_id = ( SELECT Order_id FROM SubBill WHERE OrderNumber = '" + searchOrder_bean.getOrderNumber() + "' AND OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' AND status = '0' ) )) " +
                                "BEGIN " +
                                "update Orders set isCheckout = '1' where id = (select Order_id from SubBill where OrderNumber = '" + searchOrder_bean.getOrderNumber() + "' and OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' and status = '0') " +
                                "END ";
                    }else if(checkoutStatus == Order_Enum.CheckoutStatus.未結帳) {
                        query = query + "update Orders set isCheckout = '0' where id = (select Order_id from SubBill where OrderNumber = '" + searchOrder_bean.getOrderNumber() + "' and OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' and status = '0') ";
                    }
                }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單) {
                    query = query + "update " + searchOrder_bean.getOrderSource().getOrderTableName() + " set isCheckout = '" + checkoutStatus.ordinal()
                            + "' where OrderNumber = '" + searchOrder_bean.getOrderNumber() + "' and OrderSource = '" + searchOrder_bean.getOrderSource().getOrderObject().ordinal() + "' and status = '0'";
                }
            }
        }
        return query;
    }
    /** Update payable or receivable in database
     * @param PayableReceivable_Bean the bean of payable or receivable
     * */
    public boolean modifyPayableReceivable(PayableReceivable_Bean PayableReceivable_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "update PayableReceivable set CheckNumber = ?,CheckDueDate = ?,CompanyBank_id = ?,ObjectBank_id=?,ObjectBankBranch=?,ObjectBankAccount=?,ObjectAccountName=?,ObjectPerson=?,Remark=? where OrderNumber = ? " +
                    "update PayableReceivable_Price set Cash = ?,Deposit = ?,OtherDiscount=?,CashDiscount=?,CheckPrice=? from PayableReceivable A inner join PayableReceivable_Price B on A.id = B.PayableReceivable_id where A.OrderNumber = ? " +
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
            PreparedStatement.setString(1, PayableReceivable_Bean.getCheckNumber());
            PreparedStatement.setString(2, PayableReceivable_Bean.getCheckDueDate());
            PreparedStatement.setObject(3, PayableReceivable_Bean.getCompanyBankInfo_id());
            PreparedStatement.setObject(4, PayableReceivable_Bean.getObjectBankBean() == null ? null : PayableReceivable_Bean.getObjectBankBean().getBankID());
            PreparedStatement.setString(5, PayableReceivable_Bean.getObjectBankBranch());
            PreparedStatement.setString(6, PayableReceivable_Bean.getObjectBankAccount());
            PreparedStatement.setString(7, PayableReceivable_Bean.getObjectAccountName());
            PreparedStatement.setString(8, PayableReceivable_Bean.getObjectPerson());
            PreparedStatement.setString(9, PayableReceivable_Bean.getRemark());
            PreparedStatement.setString(10, PayableReceivable_Bean.getOrderNumber());

            PreparedStatement.setInt(11, Integer.parseInt(PayableReceivable_Bean.getCash()));
            PreparedStatement.setInt(12, Integer.parseInt(PayableReceivable_Bean.getDeposit()));
            PreparedStatement.setInt(13, Integer.parseInt(PayableReceivable_Bean.getOtherDiscount()));
            PreparedStatement.setInt(14, Integer.parseInt(PayableReceivable_Bean.getCashDiscount()));
            PreparedStatement.setInt(15, Integer.parseInt(PayableReceivable_Bean.getCheckPrice()));
            PreparedStatement.setString(16, PayableReceivable_Bean.getOrderNumber());
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
    public boolean deletePayableReceivable(SearchPayableReceivable_Bean SearchPayableReceivable_Bean, OrderObject OrderObject){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION " +
                    "delete from PayableReceivable_Document where PayableReceivable_id = ? and OrderObject = ? " +
                    "delete from PayableReceivable where id = ? and OrderObject = ? " +
                    getModifyOrderCheckoutQuery(SearchPayableReceivable_Bean.getOrderList(), Order_Enum.CheckoutStatus.未結帳) +
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
            PreparedStatement.setObject(1, SearchPayableReceivable_Bean.getPayableReceivableID());
            PreparedStatement.setObject(2, OrderObject.ordinal());
            PreparedStatement.setObject(3, SearchPayableReceivable_Bean.getPayableReceivableID());
            PreparedStatement.setObject(4, OrderObject.ordinal());
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
    /** Search all payable or receivable
     * */
    public ObservableList<SearchPayableReceivable_Bean> searchAllPayableReceivable(ConditionalSearchPayReceive_Bean conditionalSearchPayReceive_bean){
        ObservableList<SearchPayableReceivable_Bean> payableReceivableList = FXCollections.observableArrayList();
        String query = "";
        if(conditionalSearchPayReceive_bean.isFindMoreThenDaysWaitingOrder()){
            if(conditionalSearchPayReceive_bean.getOrderObject() == Order_Enum.OrderObject.廠商) {
                query = "select DISTINCT C.OrderNumber,C.OrderDate,C.ObjectID,D.ObjectNickName,D.InvoiceTitle,G.CheckTitle,G.CheckDueDay,C.CheckNumber,C.CheckDueDate,C.CompanyBank_id,F.CheckPrice,F.Cash,F.Deposit,F.OtherDiscount,F.RemittanceFeeAndPostage," +
                        "F.CashDiscount,F.OffsetPrice,C.ObjectBank_id,E.BankNickName,C.ObjectBankBranch,C.ObjectBankAccount,C.ObjectAccountName,C.ObjectPerson,C.InvoiceNumber,C.Remark,C.id " +
                        "FROM Orders A inner join PayableReceivable_Document B on A.id = B.Order_id inner join PayableReceivable C on B.PayableReceivable_id = C.id INNER join Manufacturer D on C.ObjectID = D.ObjectID " +
                        "LEFT JOIN Bank E on C.ObjectBank_id = E.ID INNER join PayableReceivable_Price F on C.id = F.PayableReceivable_id inner join Manufacturer_PayInfo G on D.id = G.Manufacturer_id " +
                        "WHERE A.OrderSource = ? and C.OrderDate <= ? AND A.isCheckout = ? AND A.AlreadyOrderDate IS NULL AND A.AlreadyOrderNumber IS NULL";
            }else if(conditionalSearchPayReceive_bean.getOrderObject() == Order_Enum.OrderObject.客戶){
                query = "select DISTINCT C.OrderNumber,C.OrderDate,C.ObjectID,D.ObjectNickName,D.InvoiceTitle,'','',C.CheckNumber,C.CheckDueDate,C.CompanyBank_id,F.CheckPrice,F.Cash,F.Deposit,F.OtherDiscount,F.RemittanceFeeAndPostage,F.CashDiscount," +
                        "F.OffsetPrice,C.ObjectBank_id,E.BankNickName,C.ObjectBankBranch,C.ObjectBankAccount,C.ObjectAccountName,C.ObjectPerson,C.InvoiceNumber,C.Remark,C.id " +
                        "FROM Orders A inner join PayableReceivable_Document B on A.id = B.Order_id inner join PayableReceivable C on B.PayableReceivable_id = C.id INNER join Customer D on C.ObjectID = D.ObjectID LEFT JOIN Bank E on C.ObjectBank_id = E.ID " +
                        "INNER join PayableReceivable_Price F on C.id = F.PayableReceivable_id " +
                        "WHERE A.OrderSource = ? and C.OrderDate <= ? AND A.isCheckout = ? AND A.AlreadyOrderDate IS NULL AND A.AlreadyOrderNumber IS NULL";
            }
        }else{
            if(conditionalSearchPayReceive_bean.getOrderObject() == Order_Enum.OrderObject.廠商){
                query = "select A.OrderNumber,A.OrderDate,A.ObjectID,B.ObjectNickName,B.InvoiceTitle,E.CheckTitle,E.CheckDueDay,A.CheckNumber,A.CheckDueDate,A.CompanyBank_id,D.CheckPrice,D.Cash,D.Deposit,D.OtherDiscount,D.RemittanceFeeAndPostage,D.CashDiscount,D.OffsetPrice,A.ObjectBank_id,C.BankNickName,A.ObjectBankBranch,A.ObjectBankAccount,A.ObjectAccountName,A.ObjectPerson,A.InvoiceNumber,A.Remark,A.id FROM PayableReceivable A INNER join Manufacturer B on A.ObjectID = B.ObjectID LEFT JOIN Bank C on A.ObjectBank_id = C.ID INNER join PayableReceivable_Price D on A.id = D.PayableReceivable_id inner join Manufacturer_PayInfo E on B.id = E.Manufacturer_id WHERE OrderObject = ? and ";
            }else if(conditionalSearchPayReceive_bean.getOrderObject() == Order_Enum.OrderObject.客戶) {
                    query = "select A.OrderNumber,A.OrderDate,A.ObjectID,B.ObjectNickName,B.InvoiceTitle,'','',A.CheckNumber,A.CheckDueDate,A.CompanyBank_id,D.CheckPrice,D.Cash,D.Deposit,D.OtherDiscount,D.RemittanceFeeAndPostage,D.CashDiscount,D.OffsetPrice,A.ObjectBank_id,C.BankNickName,A.ObjectBankBranch,A.ObjectBankAccount,A.ObjectAccountName,A.ObjectPerson,A.InvoiceNumber,A.Remark,A.id FROM PayableReceivable A INNER join Customer B on A.ObjectID = B.ObjectID LEFT JOIN Bank C on A.ObjectBank_id = C.ID INNER join PayableReceivable_Price D on A.id = D.PayableReceivable_id WHERE OrderObject = ? and ";
            }
            query = query + getSearchMethodAndColumnQuery(conditionalSearchPayReceive_bean);
        }
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            ERPApplication.Logger.info(query);
            preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            preparedStatement.setInt(1,conditionalSearchPayReceive_bean.getOrderObject().ordinal());
            if(conditionalSearchPayReceive_bean.isFindMoreThenDaysWaitingOrder()){
                preparedStatement.setString(2, toolKit.getTodayAddOrSubtractDay(conditionalSearchPayReceive_bean.getMoreThenDays()));
                preparedStatement.setBoolean(3, CheckoutStatus.已結帳.value());
            }
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                SearchPayableReceivable_Bean searchPayableReceivable_bean = new SearchPayableReceivable_Bean();
                searchPayableReceivable_bean.setOrderNumber(rs.getString(1));
                searchPayableReceivable_bean.setOrderDate(rs.getString(2));
                searchPayableReceivable_bean.setObjectID(rs.getString(3));
                searchPayableReceivable_bean.setObjectNickName(rs.getString(4));
                searchPayableReceivable_bean.setInvoiceTitle(rs.getString(5));
                searchPayableReceivable_bean.setCheckTitle(rs.getString(6));
                searchPayableReceivable_bean.setCheckDueDay(rs.getObject(7).equals("") ? null : rs.getInt(7));
                searchPayableReceivable_bean.setCheckNumber(rs.getString(8));
                searchPayableReceivable_bean.setCheckDueDate(rs.getString(9));
                searchPayableReceivable_bean.setCompanyBankInfo_id(rs.getObject(10) == null ? null : rs.getInt(10));
                searchPayableReceivable_bean.setCheckPrice(rs.getInt(11));
                searchPayableReceivable_bean.setCash(rs.getInt(12));
                searchPayableReceivable_bean.setDeposit(rs.getInt(13));
                searchPayableReceivable_bean.setOtherDiscount(rs.getInt(14));
                searchPayableReceivable_bean.setRemittanceFeeAndPostage(rs.getInt(15));
                searchPayableReceivable_bean.setCashDiscount(rs.getInt(16));
                searchPayableReceivable_bean.setOffsetPrice(rs.getInt(17));
                BankInfo_Bean BankInfo_Bean = new BankInfo_Bean(rs.getInt(18));
                searchPayableReceivable_bean.setObjectBankNickName(rs.getString(19));
                BankInfo_Bean.setBankNickName(searchPayableReceivable_bean.getObjectBankNickName());
                searchPayableReceivable_bean.setObjectBankBean(BankInfo_Bean);
                searchPayableReceivable_bean.setObjectBankBranch(rs.getString(20));
                searchPayableReceivable_bean.setObjectBankAccount(rs.getString(21));
                searchPayableReceivable_bean.setObjectAccountName(rs.getString(22));
                searchPayableReceivable_bean.setObjectPerson(rs.getString(23));
                searchPayableReceivable_bean.setInvoiceNumber(rs.getString(24));
                searchPayableReceivable_bean.setRemark(rs.getString(25));
                searchPayableReceivable_bean.setPayableReceivableID(rs.getInt(26));

                payableReceivableList.add(searchPayableReceivable_bean);
            }
            if(payableReceivableList.size() != 0)   getPayableReceivable_Document(payableReceivableList);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return payableReceivableList;
    }
    private String getSearchMethodAndColumnQuery(ConditionalSearchPayReceive_Bean conditionalSearchPayReceive_bean) {
        SearchMethod searchMethod = conditionalSearchPayReceive_bean.getSearchMethod();
        SearchColumn searchColumn = conditionalSearchPayReceive_bean.getSearchColumn();
        String searchKeyWord = conditionalSearchPayReceive_bean.getSearchKeyWord();
        String startDate = conditionalSearchPayReceive_bean.getStartDate();
        String endDate = conditionalSearchPayReceive_bean.getEndDate();
        if(searchMethod == PayableReceivable_Enum.SearchMethod.關鍵字搜尋){
            if(searchColumn == PayableReceivable_Enum.SearchColumn.對象碼)
                return "A.ObjectID = '" + searchKeyWord + "'";
            else if(searchColumn == PayableReceivable_Enum.SearchColumn.發票號碼)
                return "A.InvoiceNumber = '" + searchKeyWord + "'";
            else if(searchColumn == PayableReceivable_Enum.SearchColumn.票據號碼)
                return "A.CheckNumber = '" + searchKeyWord + "'";
        }else if(searchMethod == PayableReceivable_Enum.SearchMethod.日期搜尋){
            String query = "";
            if(searchColumn == PayableReceivable_Enum.SearchColumn.收付款日)
                query = "A.OrderDate between '" + startDate + "' and '" + endDate + "'";
            else if(searchColumn == PayableReceivable_Enum.SearchColumn.票據到期日)
                query =  "A.CheckDueDate between '" + startDate + "' and '" + endDate + "'";
            if(searchKeyWord != null && !searchKeyWord.equals(""))
                query = query + " and A.ObjectID like '%" + searchKeyWord + "%'";
            return query;
        }
        return "";
    }
    private void getPayableReceivable_Document(ObservableList<SearchPayableReceivable_Bean> payableReceivableList){
        for(SearchPayableReceivable_Bean searchPayableReceivable_bean : payableReceivableList){
            boolean isWaitingOrder = false;
            ObservableList<SearchOrder_Bean> PayableReceivableOrderList = FXCollections.observableArrayList();
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            try {
                String query = "select A.*,B.WaitingOrderNumber as 'WaitingOrderNumber',B.AlreadyOrderNumber as 'AlreadyOrderNumber',C.OrderNumber as 'SubBillNumber',D.OrderNumber as 'ReturnOrderNumber',E.total_price, E.installment from " +
                        "PayableReceivable_Document A left join Orders B on A.Order_id = B.id " +
                        "left join SubBill C on A.SubBill_id = C.id " +
                        "left join ReturnOrder D on A.ReturnOrder_id = D.id " +
                        "left join Orders_Installment E on A.id = E.pay_receive_document_id " +
                        "where PayableReceivable_id = ?";
//                ERPApplication.Logger.info(Query);
                preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
                preparedStatement.setInt(1, searchPayableReceivable_bean.getPayableReceivableID());
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    SearchOrder_Bean searchOrder_bean = new SearchOrder_Bean();
                    Integer order_id = (rs.getObject("Order_id") == null ? null : rs.getInt("Order_id"));
                    Integer subBill_id = (rs.getObject("SubBill_id") == null ? null : rs.getInt("SubBill_id"));
                    Integer returnOrder_id = (rs.getObject("ReturnOrder_id") == null ? null : rs.getInt("ReturnOrder_id"));
                    OrderObject orderObject = Order_Enum.OrderObject.values()[rs.getInt("OrderObject")];
                    searchOrder_bean.setOrderObject(orderObject);
                    if(order_id != null){
                        searchOrder_bean.setId(order_id);
                        String waitingOrderNumber = rs.getString("WaitingOrderNumber");
                        String alreadyOrderNumber = rs.getString("AlreadyOrderNumber");
                        Integer total_price = (rs.getObject("total_price") == null ? null : rs.getInt("total_price"));
                        Integer installment = (rs.getObject("installment") == null ? null : rs.getInt("installment"));
                        if(orderObject == Order_Enum.OrderObject.廠商){
                            searchOrder_bean.setOrderSource((waitingOrderNumber != null && alreadyOrderNumber == null) ? OrderSource.待入倉單 : OrderSource.進貨單);
                        }else if(orderObject == Order_Enum.OrderObject.客戶){
                            searchOrder_bean.setOrderSource(waitingOrderNumber != null && alreadyOrderNumber == null ? OrderSource.待出貨單 : OrderSource.出貨單);
                        }
                        searchOrder_bean.setWaitingOrderNumber(waitingOrderNumber);
                        searchOrder_bean.setAlreadyOrderNumber(alreadyOrderNumber);
                        if(total_price != null && installment != null){
                            searchOrder_bean.setTotalPriceIncludeTax(total_price);
                            searchOrder_bean.setInstallment(installment+1);
                        }
                        PayableReceivableOrderList.add(searchOrder_bean);
                    }else if(subBill_id != null){
                        searchOrder_bean.setId(subBill_id);
                        if(orderObject == Order_Enum.OrderObject.廠商)    searchOrder_bean.setOrderSource(OrderSource.進貨子貨單);
                        else if(orderObject == Order_Enum.OrderObject.客戶)    searchOrder_bean.setOrderSource(OrderSource.出貨子貨單);
                        searchOrder_bean.setOrderNumber(rs.getString("SubBillNumber"));
                        PayableReceivableOrderList.add(searchOrder_bean);
                    }else if(returnOrder_id != null){
                        searchOrder_bean.setId(returnOrder_id);
                        if(orderObject == Order_Enum.OrderObject.廠商)    searchOrder_bean.setOrderSource(OrderSource.進貨退貨單);
                        else if(orderObject == Order_Enum.OrderObject.客戶)    searchOrder_bean.setOrderSource(OrderSource.出貨退貨單);
                        searchOrder_bean.setOrderNumber(rs.getString("ReturnOrderNumber"));
                        PayableReceivableOrderList.add(searchOrder_bean);
                    }
                    if(!isWaitingOrder && (searchOrder_bean.getOrderSource() == OrderSource.待入倉單 || searchOrder_bean.getOrderSource() == OrderSource.待出貨單)){
                        isWaitingOrder = true;
                    }
                }
                searchPayableReceivable_bean.setIsExistWaitingOrder(isWaitingOrder);
                searchPayableReceivable_bean.setOrderList(PayableReceivableOrderList);
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }finally{
                SqlAdapter.closeConnectParameter(preparedStatement,rs);
            }
        }
    }
    /** Get payable or receivable which selected
     * @param orderList the order list of payable or receivable
     */
    public void getPayableReceivableOrder(ObservableList<SearchOrder_Bean> orderList){
        for(SearchOrder_Bean searchOrder_bean : orderList){
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            try {
                String query = "";
                if(searchOrder_bean.getOrderObject() == Order_Enum.OrderObject.廠商){
                    if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單){
                        query = "select A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from SubBill A INNER join Manufacturer B on A.ObjectID = B.ObjectID INNER join SubBill_ProjectInfo C ON A.id = C.SubBill_id left join Orders_InvoiceInfo D on A.id = D.SubBill_id INNER join SubBill_Price E on A.id = E.SubBill_id where A.OrderSource = ? and A.OrderNumber = ? and A.status = '0'";
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待入倉單){
                        query = "select A.WaitingOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from Orders A INNER join Manufacturer B on A.ObjectID = B.ObjectID INNER join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id INNER join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.WaitingOrderNumber = ? and A.status = '0'";
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨單){
                        query = "select A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber from Orders A INNER join Manufacturer B on A.ObjectID = B.ObjectID INNER join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id INNER join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.AlreadyOrderNumber = ? and A.status = '0'";
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨退貨單){
                        query = "select A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceIncludeTax, '', C.ProjectName, '', '', A.isCheckout from ReturnOrder A INNER join Manufacturer B on A.ObjectID = B.ObjectID INNER join ReturnOrder_ProjectInfo C on A.id = C.ReturnOrder_id INNER join ReturnOrder_Price D on A.id = D.ReturnOrder_id where A.OrderSource = ? and A.OrderNumber = ? and A.status = '0'";
                    }
                }else if(searchOrder_bean.getOrderObject() == Order_Enum.OrderObject.客戶){
                    if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單){
                        query = "select A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from SubBill A INNER join Customer B on A.ObjectID = B.ObjectID INNER join SubBill_ProjectInfo C ON A.id = C.SubBill_id left join Orders_InvoiceInfo D on A.id = D.SubBill_id INNER join SubBill_Price E on A.id = E.SubBill_id where A.OrderSource = ? and A.OrderNumber = ? and A.status = '0'";
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待出貨單){
                        query = "select A.WaitingOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from Orders A INNER join Customer B on A.ObjectID = B.ObjectID INNER join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id INNER join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.WaitingOrderNumber = ? and A.status = '0'";
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨單){
                        query = "select A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber from Orders A INNER join Customer B on A.ObjectID = B.ObjectID INNER join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id INNER join Orders_Price E on A.id = E.Order_id where A.OrderSource = ? and A.AlreadyOrderNumber = ? and A.status = '0'";
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單){
                        query = "select A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, D.TotalPriceIncludeTax, '', C.ProjectName, '', '', A.isCheckout from ReturnOrder A INNER join Customer B on A.ObjectID = B.ObjectID INNER join ReturnOrder_ProjectInfo C on A.id = C.ReturnOrder_id INNER join ReturnOrder_Price D on A.id = D.ReturnOrder_id where A.OrderSource = ? and A.OrderNumber = ? and A.status = '0'";
                    }
                }
                ERPApplication.Logger.info(query);
                preparedStatement = SqlAdapter.getConnect().prepareStatement(query);
                preparedStatement.setInt(1,searchOrder_bean.getOrderSource().getOrderObject().ordinal());
                if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待出貨單) {
                    preparedStatement.setString(2, searchOrder_bean.getWaitingOrderNumber());
                }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨單) {
                    preparedStatement.setString(2, searchOrder_bean.getAlreadyOrderNumber());
                }else {
                    preparedStatement.setString(2, searchOrder_bean.getOrderNumber());
                }
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    searchOrder_bean.setOrderDate(rs.getString(1));
                    searchOrder_bean.setObjectID(rs.getString(2));
                    searchOrder_bean.setObjectName(rs.getString(3));
                    searchOrder_bean.setPersonInCharge(rs.getString(4));
                    searchOrder_bean.setNumberOfItems(rs.getInt(5));
                    if(searchOrder_bean.getInstallment() == null){
                        //  沒有分期收/付款
                        searchOrder_bean.setTotalPriceIncludeTax(rs.getInt(6));
                    }
                    searchOrder_bean.setInvoiceNumber(rs.getString(7));
                    searchOrder_bean.setProjectName(rs.getString(8));
                    searchOrder_bean.setProjectTotalPriceIncludeTax(rs.getString(9));
                    searchOrder_bean.setProjectDifferentPrice(rs.getString(10));
                    searchOrder_bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[rs.getInt(11)].value());
                    if(searchOrder_bean.getOrderSource() != Order_Enum.OrderSource.進貨退貨單 && searchOrder_bean.getOrderSource() != Order_Enum.OrderSource.出貨退貨單)
                        searchOrder_bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[rs.getInt(12)]);
                    if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨子貨單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單){
                        searchOrder_bean.setOrderNumber(searchOrder_bean.getOrderNumber());
                        searchOrder_bean.setWaitingOrderDate(rs.getString(13));
                        searchOrder_bean.setWaitingOrderNumber(rs.getString(14));
                        searchOrder_bean.setAlreadyOrderDate(rs.getString(15));
                        searchOrder_bean.setAlreadyOrderNumber(rs.getString(16));
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待入倉單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.待出貨單){
                        searchOrder_bean.setOrderNumber(searchOrder_bean.getWaitingOrderNumber());
                        searchOrder_bean.setWaitingOrderDate(rs.getString(1));
                        searchOrder_bean.setQuotationDate(rs.getString(13));
                        searchOrder_bean.setQuotationNumber(rs.getString(14));
                        searchOrder_bean.setAlreadyOrderDate(rs.getString(15));
                        searchOrder_bean.setAlreadyOrderNumber(rs.getString(16));
                    }else if(searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.進貨單 || searchOrder_bean.getOrderSource() == Order_Enum.OrderSource.出貨單){
                        searchOrder_bean.setOrderNumber(searchOrder_bean.getAlreadyOrderNumber());
                        searchOrder_bean.setAlreadyOrderDate(rs.getString(1));
                        searchOrder_bean.setQuotationDate(rs.getString(13));
                        searchOrder_bean.setQuotationNumber(rs.getString(14));
                        searchOrder_bean.setWaitingOrderDate(rs.getString(15));
                        searchOrder_bean.setWaitingOrderNumber(rs.getString(16));
                    }
                }
            } catch (Exception Ex) {
                ERPApplication.Logger.catching(Ex);
                Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            }finally{
                SqlAdapter.closeConnectParameter(preparedStatement,rs);
            }
        }
    }
    /** Export payable or receivable to Excel
     * @param OrderObject the object of order
     * @param ObjectInfo_Bean the bean of object
     * @param PayableReceivable_Bean the bean of payable or receivable
     */
    public String exportPayableReceivable(OrderObject OrderObject, ObjectInfo_Bean ObjectInfo_Bean, PayableReceivable_Bean PayableReceivable_Bean){
        String outputFilePath;
        try{
            outputFilePath = PayableReceivable_Bean.getOrderObject().name() + "請款單-" + toolKit.getToday("yyyyMMdd") + "-" + ObjectInfo_Bean.getObjectID() + "-" + ObjectInfo_Bean.getObjectName() + ".xls";
            FileInputStream FileInput = new FileInputStream(ResourceUtils.getFile(TemplatePath.PayableReceivableInvoice));
            Workbook workbook = Workbook.getWorkbook(FileInput);
            WritableWorkbook WritableWorkbook = Workbook.createWorkbook(new File(callConfig.getFile_OutputPath() + "\\" + outputFilePath),workbook);

            // 預設請款單上的含稅、未稅
            WritableSheet WritableSheet = WritableWorkbook.getSheet(0);
            WritableFont taxStatusFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.BOLD);
            WritableCellFormat taxStatusFormat = new WritableCellFormat(taxStatusFont);
            taxStatusFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            WritableSheet.addCell(new Label(2, 24,ObjectInfo_Bean.getOrderTax() == ObjectInfo_Enum.OrderTax.未稅 ? "\u25A0未稅 \u25A1含稅" : "\u25A1未稅 \u25A0含稅",taxStatusFormat));

            WritableFont payMethodFont = new WritableFont(WritableFont.createFont("標楷體"), 12, WritableFont.NO_BOLD);
            WritableCellFormat payMethodFormat = new WritableCellFormat(payMethodFont);
            payMethodFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            if(PayableReceivable_Bean.getCash().equals("0") && !PayableReceivable_Bean.getCheckPrice().equals("0"))
                WritableSheet.addCell(new Label(2, 25,"\u25A1轉帳 \u25A1臨櫃 \u25A1現金 \u25A0支票",payMethodFormat));
            else if(!PayableReceivable_Bean.getCash().equals("0") && PayableReceivable_Bean.getCheckPrice().equals("0"))
                WritableSheet.addCell(new Label(2, 25,"\u25A1轉帳 \u25A0臨櫃 \u25A1現金 \u25A1支票",payMethodFormat));

            // 對象資訊
            WritableSheet WritableSheet1 = WritableWorkbook.getSheet(1);
            WritableSheet1.addCell(new Label(1,1, ObjectInfo_Bean.getObjectID()));
            WritableSheet1.addCell(new Label(2,1, ObjectInfo_Bean.getObjectName()));
            WritableSheet1.addCell(new Label(3,1, ObjectInfo_Bean.getContactPerson()));
            WritableSheet1.addCell(new Label(1,2, ObjectInfo_Bean.getTelephone1()));

            if(PayableReceivable_Bean.getObjectBankBean() == null || PayableReceivable_Bean.getObjectBankBean().getBankID() == null) WritableSheet1.addCell(new Label(1,7,""));
            else    WritableSheet1.addCell(new Label(1,7,PayableReceivable_Bean.getObjectBankBean().getBankCode() + " " + PayableReceivable_Bean.getObjectBankBean().getBankNickName()));
            WritableSheet1.addCell(new Label(1,8, PayableReceivable_Bean.getObjectBankBranch()));
            WritableSheet1.addCell(new Label(1,9, PayableReceivable_Bean.getObjectAccountName()));
            WritableSheet1.addCell(new Label(1,10, PayableReceivable_Bean.getObjectBankAccount()));

            //  扣郵資 匯費
            int Cash = (PayableReceivable_Bean.getCash() == null || PayableReceivable_Bean.getCash().equals("")) ? 0 : Integer.parseInt(PayableReceivable_Bean.getCash());
            int Deposit = (PayableReceivable_Bean.getDeposit() == null || PayableReceivable_Bean.getDeposit().equals("")) ? 0 : Integer.parseInt(PayableReceivable_Bean.getDeposit());
            int CashDiscount = (PayableReceivable_Bean.getCashDiscount() == null || PayableReceivable_Bean.getCashDiscount().equals("")) ? 0 : Integer.parseInt(PayableReceivable_Bean.getCashDiscount());
            int TotalPrice = (PayableReceivable_Bean.getTotalPriceIncludeTax() == null || PayableReceivable_Bean.getTotalPriceIncludeTax().equals("")) ? 0 : Integer.parseInt(PayableReceivable_Bean.getTotalPriceIncludeTax());
            if(OrderObject == Order_Enum.OrderObject.廠商) {
                if(Cash == 0 && ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣) {	//	扣郵資
                    WritableSheet1.addCell(new Label(1, 3, "0"));
                    WritableSheet1.addCell(new Label(1, 4, "" + ObjectInfo_Bean.getPostage()));
                }else if(Cash != 0) {
                    if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣 && ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣){
                        WritableSheet1.addCell(new Label(1, 3, "" + ObjectInfo_Bean.getRemittanceFee()));
                        if(Cash + Deposit + ObjectInfo_Bean.getRemittanceFee() + ObjectInfo_Bean.getPostage() + CashDiscount == TotalPrice ||
                                Cash + Deposit + ObjectInfo_Bean.getRemittanceFee() + CashDiscount == TotalPrice)
                            WritableSheet1.addCell(new Label(1, 4, "0"));
                        else    WritableSheet1.addCell(new Label(1, 4, "" + ObjectInfo_Bean.getPostage()));
                    }else if(ObjectInfo_Bean.getDiscountRemittanceFee() == ObjectInfo_Enum.DiscountRemittanceFee.已扣){
                        WritableSheet1.addCell(new Label(1, 3, "" + ObjectInfo_Bean.getRemittanceFee()));
                        WritableSheet1.addCell(new Label(1, 4, "0"));
                    }else if(ObjectInfo_Bean.getDiscountPostage() == ObjectInfo_Enum.DiscountPostage.已扣 && Cash + Deposit + CashDiscount != TotalPrice){
                        WritableSheet1.addCell(new Label(1, 3, "0"));
                        WritableSheet1.addCell(new Label(1, 4, "" + ObjectInfo_Bean.getPostage()));
                    }else{
                    WritableSheet1.addCell(new Label(1, 3, "0"));
                    WritableSheet1.addCell(new Label(1, 4, "0"));
                    }
                }else{
                    WritableSheet1.addCell(new Label(1, 3, "0"));
                    WritableSheet1.addCell(new Label(1, 4, "0"));
                }
            }else if(OrderObject == Order_Enum.OrderObject.客戶){
                WritableSheet1.addCell(new Label(1, 3, "0"));
                WritableSheet1.addCell(new Label(1, 4, "0"));
            }
            WritableSheet1.addCell(new Label(1, 5, String.valueOf(Deposit)));
            WritableSheet1.addCell(new Label(1, 6, PayableReceivable_Bean.getCheckNumber()));

            // 將單號內容插入到sheet3
            WritableSheet WritableSheet2 = WritableWorkbook.getSheet(2);

            ObservableList<SearchOrder_Bean> selectOrderList = PayableReceivable_Bean.getOrderList();
            for (int index = 0; index < selectOrderList.size(); index++) {
                SearchOrder_Bean SearchOrder_Bean = selectOrderList.get(index);
                WritableSheet2.addCell(new Label(1, index, SearchOrder_Bean.getOrderSource().getOrderNickName()));

                WritableSheet2.addCell(new Label(0, index, SearchOrder_Bean.getOrderNumber()));
                WritableSheet2.addCell(new Label(3, index, SearchOrder_Bean.getOrderDate()));
                if(SearchOrder_Bean.getOrderSource() == OrderSource.進貨退貨單 || SearchOrder_Bean.getOrderSource() == OrderSource.出貨退貨單)
                    WritableSheet2.addCell(new jxl.write.Number(2, index, -SearchOrder_Bean.getTotalPriceIncludeTax(), new WritableCellFormat(NumberFormats.ACCOUNTING_FLOAT)));
                else    WritableSheet2.addCell(new jxl.write.Number(2, index, SearchOrder_Bean.getTotalPriceIncludeTax(), new WritableCellFormat(NumberFormats.ACCOUNTING_FLOAT)));
                WritableSheet2.addCell(new Label(4, index, SearchOrder_Bean.getProjectName()));
            }
            WritableWorkbook.write();
            WritableWorkbook.close();
            workbook.close();
        }catch (Exception Ex) {
            outputFilePath = null;
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return outputFilePath;
    }
    /** Print payable or receivable
     * @param PayableReceivableList the list of payable or receivable
     * @param outputFilePath the path that the file export
     * @param OrderObject the object of order
     * @param StartDate_DatePicker the start date for conditional search
     * @param EndDate_DatePicker the end date for conditional search
     */
    public boolean printPayableReceivable(ObservableList<SearchPayableReceivable_Bean> PayableReceivableList, String outputFilePath, OrderObject OrderObject, DatePicker StartDate_DatePicker, DatePicker EndDate_DatePicker){
        boolean status = false;

        try{
            SystemSettingConfig_Bean SystemSettingConfig_Bean = systemSetting_model.loadAllSystemSettingData();
            JSONObject exportCompanyFormat = SystemSettingConfig_Bean.getExportCompanyFormat();
            if(exportCompanyFormat == null){
                DialogUI.MessageDialog("※ 請設定 [公司資訊] 檔案匯出格式");
                return false;
            }
            WritableWorkbook WritableWorkbook = Workbook.createWorkbook(new File(outputFilePath));
            WritableCellFormat[] WritableCellFormat = new WritableCellFormat[7];
            WritableFont[] Font = new WritableFont[7];
            WritableSheet WritableSheet = WritableWorkbook.createSheet("0", 0);

            for (int i = 0; i < WritableCellFormat.length; i++) {
                if (i == 0) Font[i] = new WritableFont(WritableFont.TIMES, 24, WritableFont.NO_BOLD);
                else if (i == 1)    Font[i] = new WritableFont(WritableFont.TIMES, 16, WritableFont.NO_BOLD);
                else    Font[i] = new WritableFont(WritableFont.TIMES, 10, WritableFont.NO_BOLD);
                WritableCellFormat[i] = new WritableCellFormat(Font[i]);
                WritableCellFormat[i].setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.NONE);
                if (i == 0) WritableCellFormat[i].setAlignment(jxl.format.Alignment.CENTRE);
            }
            int ExcelPage = PayableReceivableList.size() % 10 == 0 ? PayableReceivableList.size() / 10 : PayableReceivableList.size() / 10 + 1;
            for (int page = 0; page < ExcelPage; page++) {
                int Row = page * 29;
                WritableSheet.addCell( new Label(0, Row+1, exportCompanyFormat.getString(SystemSetting_Enum.ExportCompanyFormat.storeName.name()), WritableCellFormat[0]));
                WritableSheet.mergeCells(0, Row+1, 10, Row+1);
                WritableSheet.addCell(new Label(0, Row+2, OrderObject.getPayableReceivableName() + "資料查詢", WritableCellFormat[0]));
                WritableSheet.mergeCells(0, Row+2, 10, Row+2);
                WritableSheet.addCell(new Label(0, Row+3, "                      ", WritableCellFormat[2]));
                WritableSheet.setRowView(Row+3, 200);
                WritableSheet.addCell(new Label(0, Row+5, (OrderObject == Order_Enum.OrderObject.廠商 ? "付款對象:" : "收款對象:") + OrderObject.name() + "         對象編號:" + "" + "      對象名稱:" + "", WritableCellFormat[2]));
                WritableSheet.setRowView(Row+5, 300);
                WritableSheet.mergeCells(0, Row+5, 10, Row+5);
                WritableSheet.addCell(new Label(0, Row+6, "日期:" + componentToolKit.getDatePickerValue(StartDate_DatePicker,"yyyy/MM/dd") + "~" + componentToolKit.getDatePickerValue(EndDate_DatePicker,"yyyy/MM/dd"), WritableCellFormat[2]));
                WritableSheet.mergeCells(0, Row+6, 10, Row+6);
                WritableSheet.addCell(new Label(0, Row+7,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", WritableCellFormat[2]));
                WritableSheet.mergeCells(0, Row+7, 10, Row+7);
                String[] columnNames = { "單號", "對象", "支票抬頭", "票據碼", "票面額", "現金", "訂金" ,"其它折讓", "現金折讓", "沖帳額", "到期日", "付款銀行", "備註" };
                WritableSheet.addCell(new Label(0, Row+8, "No.", WritableCellFormat[2]));
                for (int c = 0; c < columnNames.length; c++)
                    WritableSheet.addCell(new Label(c + 1, Row+8, columnNames[c], WritableCellFormat[2]));
                WritableSheet.addCell(new Label(0, Row+9,"^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^", WritableCellFormat[2]));
                WritableSheet.mergeCells(0, Row+9, 10, Row+9);

                int total_count = 0;
                for (int index = page * 10; index < (page + 1) * 10; index++) {
                    if (index < PayableReceivableList.size()) {
                        SearchPayableReceivable_Bean SearchPayableReceivable_Bean = PayableReceivableList.get(index);
                        WritableSheet.addCell(new Label(0, 10 + Row + total_count, "" + (index + 1), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(1, 10 + Row + total_count, SearchPayableReceivable_Bean.getOrderNumber(), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(2, 10 + Row + total_count, SearchPayableReceivable_Bean.getObjectNickName(), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(3, 10 + Row + total_count, SearchPayableReceivable_Bean.getCheckTitle(), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(4, 10 + Row + total_count, SearchPayableReceivable_Bean.getCheckNumber(), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(5, 10 + Row + total_count, String.valueOf(SearchPayableReceivable_Bean.getCheckPrice()), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(6, 10 + Row + total_count, String.valueOf(SearchPayableReceivable_Bean.getCash()), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(7, 10 + Row + total_count, String.valueOf(SearchPayableReceivable_Bean.getDeposit()), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(8, 10 + Row + total_count, String.valueOf(SearchPayableReceivable_Bean.getOtherDiscount()), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(9, 10 + Row + total_count, String.valueOf(SearchPayableReceivable_Bean.getCashDiscount()), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(10, 10 + Row + total_count, String.valueOf(SearchPayableReceivable_Bean.getOffsetPrice()), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(11, 10 + Row + total_count, SearchPayableReceivable_Bean.getCheckDueDate(), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(12, 10 + Row + total_count, SearchPayableReceivable_Bean.getObjectBankBean().getBankNickName(), WritableCellFormat[2]));
                        WritableSheet.addCell(new Label(13, 10 + Row + total_count, SearchPayableReceivable_Bean.getRemark(), WritableCellFormat[2]));
                    }
                    total_count++;
                }
            }

            WritableSheet.setColumnView(0, 3);
            WritableSheet.setColumnView(1, 10);
            WritableSheet.setColumnView(3, 7);
            WritableSheet.setColumnView(6, 8);
            WritableSheet.setColumnView(7, 10);
            WritableSheet.setColumnView(5, 6);
            WritableSheet.setColumnView(10, 10);
            SheetSettings sts = WritableSheet.getSettings();
            sts.setPaperSize(PaperSize.NOTE);
            sts.setLeftMargin(0.0);
            sts.setRightMargin(0.0);
            sts.setTopMargin(0.0);
            sts.setBottomMargin(0.0);
            WritableWorkbook.write();
            WritableWorkbook.close();
            if(Desktop.isDesktopSupported()){
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.PRINT))
                    desktop.print(new File(outputFilePath));
            }else    System.out.println("debug: desktop not supported!");
            status = true;
        }catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return status;
    }
    /** Export check to Excel
     * @param PayableReceivableList the list of payable or receivable
     * @param OrderObject the object of order
     */
    public boolean exportCheck(ObservableList<SearchPayableReceivable_Bean> PayableReceivableList, OrderObject OrderObject){
        boolean status = false;
        try{
            String outputFilePath = callConfig.getFile_OutputPath() + "\\" + OrderObject.getPayableReceivableName() + "支票匯出.xls";
            POIFSFileSystem POIFSFileSystem = new POIFSFileSystem(new FileInputStream(ResourceUtils.getFile(TemplatePath.CheckExportFormat)));
            HSSFWorkbook HSSFWorkbook = new HSSFWorkbook(POIFSFileSystem);
            HSSFSheet Sheet = HSSFWorkbook.getSheetAt(1);//讀取wb內的頁面

            for (int index = 0; index < PayableReceivableList.size(); index++) {
                SearchPayableReceivable_Bean SearchPayableReceivable_Bean = PayableReceivableList.get(index);
                String CheckTitle = SearchPayableReceivable_Bean.getCheckTitle();
                String CheckNumber = SearchPayableReceivable_Bean.getCheckNumber();
                int CheckPrice = SearchPayableReceivable_Bean.getCheckPrice();
                String CheckEndDate = SearchPayableReceivable_Bean.getCheckDueDate();
//			System.out.println(OrderNumber + "  " + ObjectName + "  " + CheckTitle + "  " + CheckPrice + "  " + CheckEndDate);

                HSSFCell CheckIndex = Sheet.getRow((index+1)).getCell(0);   // 支票張數
                if(CheckIndex == null)  CheckIndex = Sheet.createRow((index+1)).createCell(0);
                CheckIndex.setCellValue((index+1));
                HSSFCell CheckTitleCell = Sheet.getRow((index+1)).getCell(1);   // 支票抬頭
                if(CheckTitleCell == null)  CheckTitleCell = Sheet.createRow((index+1)).createCell(1);
                CheckTitleCell.setCellValue(CheckTitle);
                HSSFCell CheckPriceCell = Sheet.getRow((index+1)).getCell(2);   // 票面額
                if(CheckPriceCell == null)  CheckPriceCell = Sheet.createRow((index+1)).createCell(2);
                CheckPriceCell.setCellValue(CheckPrice);
                HSSFCell CheckEndDateCell = Sheet.getRow((index+1)).getCell(3); // 票據到期日
                if(CheckEndDateCell == null)    CheckEndDateCell = Sheet.createRow((index+1)).createCell(3);
                CheckEndDateCell.setCellValue(CheckEndDate);
                HSSFCell CheckNumberCell = Sheet.getRow((index+1)).getCell(4);  //票據號碼
                if(CheckNumberCell == null) CheckNumberCell = Sheet.createRow((index+1)).createCell(4);
                CheckNumberCell.setCellValue(CheckNumber);
            }
            int rowLength = Sheet.getLastRowNum();
            for (int index = (PayableReceivableList.size()+1); index <= rowLength; index++) {
                Sheet.getRow(index).getCell(0).setCellValue("");
                Sheet.getRow(index).getCell(1).setCellValue("");
                Sheet.getRow(index).getCell(2).setCellValue("");
                Sheet.getRow(index).getCell(3).setCellValue("");
                Sheet.getRow(index).getCell(4).setCellValue("");
            }
            HSSFWorkbook.write(new FileOutputStream(outputFilePath));
            status = true;
        }catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return status;
    }
    public boolean setIAECrawlerDataStatus(IAECrawlerAccount_Bean IAECrawlerAccount_Bean, IAECrawlerBelong_Bean IAECrawlerBelong_Bean, LocalDate startDate, LocalDate endDate, IAECrawlerData_Bean IAECrawlerData_Bean, IAECrawlerStatus IAECrawlerStatus){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "";
            if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.待更新)
                Query = "update IAECrawlerPayment set Status = ? from IAECrawlerAccount_Payment A inner join IAECrawlerPayment B on A.Payment_id = B.id where A.Account_id = ? and Belong_id = ? and PayDate between ? and ? and B.Status != ?";
            else if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.新增)
                Query = "update IAECrawlerPayment set Status = ? where id = ? and Rankey = ?";
            else if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.遺失)
                Query = "update IAECrawlerPayment set Status = ? from IAECrawlerAccount_Payment A inner join IAECrawlerPayment B on A.Payment_id = B.id where A.Account_id = ? and Belong_id = ? and Status = ? and PayDate between ? and ?";
            else if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.忽略)
                Query = "update IAECrawlerPayment set Status = ? where id = ? and Rankey = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, IAECrawlerStatus.ordinal());
            if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.新增 || IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.忽略){
                PreparedStatement.setInt(2, IAECrawlerData_Bean.getId());
                PreparedStatement.setInt(3, IAECrawlerData_Bean.getRankey());
            }else if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.待更新){
                PreparedStatement.setInt(2, IAECrawlerAccount_Bean.getId());
                PreparedStatement.setInt(3, IAECrawlerBelong_Bean.getId());
                PreparedStatement.setDate(4, Date.valueOf(startDate));
                PreparedStatement.setDate(5, Date.valueOf(endDate));
                PreparedStatement.setInt(6, PayableReceivable_Enum.IAECrawlerStatus.忽略.ordinal());
            }else if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.遺失){
                PreparedStatement.setInt(2, IAECrawlerAccount_Bean.getId());
                PreparedStatement.setInt(3, IAECrawlerBelong_Bean.getId());
                PreparedStatement.setInt(4, PayableReceivable_Enum.IAECrawlerStatus.待更新.ordinal());
                PreparedStatement.setDate(5, Date.valueOf(startDate));
                PreparedStatement.setDate(6, Date.valueOf(endDate));
            }
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
    public IAECrawlerData_Bean getExistIAECrawlerData(IAECrawlerAccount_Bean IAECrawlerAccount_Bean, int ranKey){
        IAECrawlerData_Bean IAECrawlerData_Bean = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "select CASE WHEN (SELECT Count(*) FROM IAECrawlerInvoice_Order WHERE invoice_id = B.id ) != '0' then '1' else '0' end,A.Account_id,E.ManufacturerNickName,B.* from IAECrawlerAccount_Payment A inner join IAECrawlerPayment B on A.Payment_id = B.id inner join Manufacturer C on B.ObjectID = C.ObjectID INNER JOIN IAECrawlerAccount D on A.Account_id = D.id INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer E on D.Account = E.Account where RanKey = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, ranKey);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()){
                IAECrawlerData_Bean = new IAECrawlerData_Bean();
                IAECrawlerData_Bean.setIsBindingOrder(Rs.getBoolean(1));
                IAECrawlerData_Bean.setInvoice_manufacturerNickName_id(Rs.getInt(2));
                IAECrawlerData_Bean.setInvoiceManufacturerNickName(Rs.getString(3));
                IAECrawlerData_Bean.setId(Rs.getInt("id"));
                IAECrawlerData_Bean.setRankey(Rs.getInt("RanKey"));
                IAECrawlerData_Bean.setSummonsNumber(Rs.getString("SummonsCode"));
                IAECrawlerData_Bean.setObjectID(Rs.getString("ObjectID"));
                IAECrawlerData_Bean.setPayDate(Rs.getString("PayDate"));
                IAECrawlerData_Bean.setPayAmount(Rs.getString("PayAmount"));
                IAECrawlerData_Bean.setRemittanceFee(Rs.getInt("RemittanceFee"));
                IAECrawlerData_Bean.setBankAccount(Rs.getString("BankAccount"));
                IAECrawlerData_Bean.setProjectCode(Rs.getString("ProjectCode"));
                IAECrawlerData_Bean.setInvoiceContent(Rs.getString("InvoiceContent"));
                String invoiceDate = Rs.getString("InvoiceDate");
                IAECrawlerData_Bean.setInvoiceDate(invoiceDate == null ? null : LocalDate.parse(invoiceDate));
                IAECrawlerData_Bean.setInvoiceNumber(Rs.getString("InvoiceNumber"));
                IAECrawlerData_Bean.setInvoiceAmount(Rs.getString("InvoiceAmount"));
                IAECrawlerData_Bean.setIAECrawlerStatus(IAECrawlerStatus.values()[Rs.getInt("Status")]);
                IAECrawlerData_Bean.setIAECrawlerReviewStatus(IAECrawlerReviewStatus.values()[Rs.getInt("ReviewStatus")]);
                IAECrawlerData_Bean.setSource(PayableReceivable_Enum.IAECrawlerSource.values()[Rs.getInt("Source")]);
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return IAECrawlerData_Bean;
    }
    public boolean insertIAECrawlerPayment(IAECrawlerAccount_Bean IAECrawlerAccount_Bean, IAECrawlerData_Bean IAECrawlerData_Bean, IAECrawlerBelong_Bean IAECrawlerBelong_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            Query = Query + "insert into IAECrawlerPayment (RanKey,SummonsCode,ObjectID,PayDate,PayAmount,RemittanceFee,BankAccount,ProjectCode,InvoiceContent,InvoiceDate,InvoiceNumber,InvoiceAmount,Status,ReviewStatus,Source) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) " +
                    "insert into IAECrawlerAccount_Payment (Account_id,Payment_id,Belong_id) values (?,(SELECT IDENT_CURRENT('IAECrawlerPayment')),?) " +
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
            PreparedStatement.setInt(1, IAECrawlerData_Bean.getRankey());
            PreparedStatement.setString(2, IAECrawlerData_Bean.getSummonsNumber());
            PreparedStatement.setString(3, IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID());
            PreparedStatement.setDate(4, Date.valueOf(IAECrawlerData_Bean.getPayDate()));
            PreparedStatement.setInt(5, Integer.parseInt(IAECrawlerData_Bean.getPayAmount()));
            PreparedStatement.setDouble(6, IAECrawlerData_Bean.getRemittanceFee());
            PreparedStatement.setString(7, IAECrawlerData_Bean.getBankAccount());
            PreparedStatement.setString(8, IAECrawlerData_Bean.getProjectCode());
            PreparedStatement.setString(9, IAECrawlerData_Bean.getInvoiceContent());
            PreparedStatement.setDate(10,IAECrawlerData_Bean.getInvoiceDate() == null ? null : Date.valueOf(IAECrawlerData_Bean.getInvoiceDate()));
            PreparedStatement.setString(11, IAECrawlerData_Bean.getInvoiceNumber());
            PreparedStatement.setInt(12, toolKit.RoundingInteger(IAECrawlerData_Bean.getInvoiceAmount()));
            PreparedStatement.setInt(13, IAECrawlerData_Bean.getIAECrawlerStatus().ordinal());
            PreparedStatement.setInt(14, IAECrawlerData_Bean.getIAECrawlerReviewStatus().ordinal());
            PreparedStatement.setInt(15, IAECrawlerData_Bean.getSource().ordinal());
            PreparedStatement.setInt(16, IAECrawlerAccount_Bean.getId());
            PreparedStatement.setInt(17, IAECrawlerBelong_Bean.getId());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean updateIAECrawlerData(IAECrawlerData_Bean IAECrawlerData_Bean, boolean updateManufacturer, boolean reBinding){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update IAECrawlerPayment set SummonsCode = ?,PayDate = ?,PayAmount = ?,RemittanceFee = ?,BankAccount = ?,ProjectCode = ?,InvoiceContent = ?,InvoiceDate = ?,InvoiceNumber =?,InvoiceAmount =?,ReviewStatus = ?,Source = ? where RanKey = ? ";
            if(updateManufacturer)
                Query = Query + "update IAECrawlerAccount_Payment set Account_id = ? from IAECrawlerPayment A inner join IAECrawlerAccount_Payment B on A.id = B.Payment_id where A.RanKey = ? ";
            if(reBinding)
                Query = Query + "delete IAECrawlerInvoice_Order where invoice_id = ? ";

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
            PreparedStatement.setString(1,IAECrawlerData_Bean.getSummonsNumber());
            PreparedStatement.setDate(2, Date.valueOf(IAECrawlerData_Bean.getPayDate()));
            PreparedStatement.setInt(3, Integer.parseInt(IAECrawlerData_Bean.getPayAmount()));
            PreparedStatement.setDouble(4, IAECrawlerData_Bean.getRemittanceFee());
            PreparedStatement.setString(5, IAECrawlerData_Bean.getBankAccount());
            PreparedStatement.setString(6, IAECrawlerData_Bean.getProjectCode());
            PreparedStatement.setString(7, IAECrawlerData_Bean.getInvoiceContent());
            PreparedStatement.setDate(8,IAECrawlerData_Bean.getInvoiceDate() == null ? null : Date.valueOf(IAECrawlerData_Bean.getInvoiceDate()));
            PreparedStatement.setString(9, IAECrawlerData_Bean.getInvoiceNumber());
            PreparedStatement.setInt(10, Integer.parseInt(IAECrawlerData_Bean.getInvoiceAmount()));
            PreparedStatement.setInt(11, reBinding ? IAECrawlerReviewStatus.未查核.ordinal() : IAECrawlerData_Bean.getIAECrawlerReviewStatus().ordinal());
            PreparedStatement.setInt(12, IAECrawlerData_Bean.getSource().ordinal());
            PreparedStatement.setInt(13, IAECrawlerData_Bean.getRankey());
            if(updateManufacturer){
                PreparedStatement.setInt(14, IAECrawlerData_Bean.getInvoice_manufacturerNickName_id());
                PreparedStatement.setInt(15, IAECrawlerData_Bean.getRankey());
            }
            if(reBinding)
                PreparedStatement.setInt(updateManufacturer ? 16 : 14, IAECrawlerData_Bean.getId());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public ObservableList<IAECrawlerData_Bean> getAllIAECrawlerPaymentData(IAECrawlerStatus IAECrawlerStatus, IAECrawlerReviewStatus IAECrawlerReviewStatus, HashMap<IAECrawlerData_Bean,SearchOrder_Bean> IAECrawlerDataAndOrderMap){
        ObservableList<IAECrawlerData_Bean> AllIAECrawlerDataList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "";
            if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.新增) {
                if(IAECrawlerReviewStatus == PayableReceivable_Enum.IAECrawlerReviewStatus.已查核)
                    Query = "select " +
                            "case when (F.order_id is not null) Then '出貨單' ELSE '出貨子貨單' END," +
                            "case when (F.order_id is not null) Then (select AlreadyOrderNumber from Orders where id = F.Order_id) ELSE (select AlreadyOrderNumber from SubBill where id = F.subbill_id) END as alreadyOrderNumber," +
                            "case when (F.order_id is not null) Then (select isCheckout from Orders where id = F.Order_id) ELSE (select isCheckout from SubBill where id = F.subbill_id) END as isCheckout," +
                            "C.BelongName,E.ObjectName,A.Account as ObjectTaxID,F.invoice_id,G.id as 'invoice_manufacturerNickName_id',G.ManufacturerNickName as 'invoiceManufacturerNickName',D.* from IAECrawlerAccount A INNER JOIN IAECrawlerAccount_Payment B ON A.id= B.Account_id INNER JOIN IAECrawlerBelong C ON B.Belong_id = C.id inner join IAECrawlerPayment D on B.Payment_id = D.id inner join Manufacturer E on D.ObjectID = E.ObjectID LEFT JOIN IAECrawlerInvoice_Order F ON D.id = F.invoice_id INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer G on A.Account = G.Account where D.ReviewStatus = ? and D.Status = ? order by D.PayDate DESC";
                else
                    Query = "select '','','',C.BelongName,E.ObjectName,A.Account as ObjectTaxID,F.invoice_id,G.id as 'invoice_manufacturerNickName_id',G.ManufacturerNickName as 'invoiceManufacturerNickName',D.* from IAECrawlerAccount A INNER JOIN IAECrawlerAccount_Payment B ON A.id= B.Account_id INNER JOIN IAECrawlerBelong C ON B.Belong_id = C.id inner join IAECrawlerPayment D on B.Payment_id = D.id inner join Manufacturer E on D.ObjectID = E.ObjectID LEFT JOIN IAECrawlerInvoice_Order F ON D.id = F.invoice_id INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer G on A.Account = G.Account where D.ReviewStatus = ? and D.Status = ? order by D.PayDate DESC";
            }else if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.忽略 || IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.遺失)
                Query = "select '','','',C.BelongName,E.ObjectName,A.Account as ObjectTaxID,F.invoice_id,G.id as 'invoice_manufacturerNickName_id',G.ManufacturerNickName as 'invoiceManufacturerNickName',D.* from IAECrawlerAccount A INNER JOIN IAECrawlerAccount_Payment B ON A.id= B.Account_id INNER JOIN IAECrawlerBelong C ON B.Belong_id = C.id inner join IAECrawlerPayment D on B.Payment_id = D.id inner join Manufacturer E on D.ObjectID = E.ObjectID LEFT JOIN IAECrawlerInvoice_Order F ON D.id = F.invoice_id INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer G on A.Account = G.Account where D.Status = ? order by D.PayDate DESC";

            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.新增) {
                PreparedStatement.setInt(1, IAECrawlerReviewStatus.ordinal());
                PreparedStatement.setInt(2, IAECrawlerStatus.ordinal());
            }else if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.忽略 || IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.遺失)
                PreparedStatement.setInt(1, IAECrawlerStatus.ordinal());
            Rs = PreparedStatement.executeQuery();
            int serialNumber = 0;
            while (Rs.next()){
                serialNumber = serialNumber + 1;
                IAECrawlerData_Bean IAECrawlerData_Bean = new IAECrawlerData_Bean();
                if(IAECrawlerStatus == PayableReceivable_Enum.IAECrawlerStatus.新增 && IAECrawlerReviewStatus == PayableReceivable_Enum.IAECrawlerReviewStatus.已查核){
                    SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
                    SearchOrder_Bean.setOrderSource(OrderSource.valueOf(Rs.getString(1)));
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(2));
                    SearchOrder_Bean.setIsCheckout(Rs.getBoolean(3));
                    IAECrawlerDataAndOrderMap.put(IAECrawlerData_Bean,SearchOrder_Bean);
                }
                IAECrawlerData_Bean.setId(Rs.getInt("id"));
                IAECrawlerData_Bean.setSerialNumber(serialNumber);
                IAECrawlerData_Bean.setBelongName(Rs.getString("BelongName"));
                IAECrawlerData_Bean.setRankey(Rs.getInt("RanKey"));
                IAECrawlerData_Bean.setSummonsNumber(Rs.getString("SummonsCode"));
                IAECrawlerData_Bean.setObjectID(Rs.getString("ObjectID"));
                IAECrawlerData_Bean.setObjectName(Rs.getString("ObjectName"));
                IAECrawlerData_Bean.setObjectTaxID(Rs.getString("ObjectTaxID"));
                IAECrawlerData_Bean.setPayDate(Rs.getString("PayDate"));
                IAECrawlerData_Bean.setPayAmount(Rs.getString("PayAmount"));
                IAECrawlerData_Bean.setRemittanceFee(Rs.getInt("RemittanceFee"));
                IAECrawlerData_Bean.setBankAccount(Rs.getString("BankAccount"));
                IAECrawlerData_Bean.setProjectCode(Rs.getString("ProjectCode"));
                IAECrawlerData_Bean.setInvoiceContent(Rs.getString("InvoiceContent"));
                String invoiceDate = Rs.getString("InvoiceDate");
                IAECrawlerData_Bean.setInvoiceDate(invoiceDate == null ? null : LocalDate.parse(invoiceDate));

                IAECrawlerData_Bean.setInvoiceNumber(Rs.getString("InvoiceNumber"));
                IAECrawlerData_Bean.setInvoiceAmount(Rs.getString("InvoiceAmount"));
                IAECrawlerData_Bean.setIAECrawlerStatus(PayableReceivable_Enum.IAECrawlerStatus.values()[Rs.getInt("Status")]);
                IAECrawlerData_Bean.setIAECrawlerReviewStatus(PayableReceivable_Enum.IAECrawlerReviewStatus.values()[Rs.getInt("ReviewStatus")]);
                IAECrawlerData_Bean.setSource(PayableReceivable_Enum.IAECrawlerSource.values()[Rs.getInt("Source")]);
                if(Rs.getObject("invoice_id") == null)  IAECrawlerData_Bean.setIsBindingOrder(false);
                else    IAECrawlerData_Bean.setIsBindingOrder(true);
                IAECrawlerData_Bean.setInvoice_manufacturerNickName_id(Rs.getObject("invoice_manufacturerNickName_id") == null ? null:Rs.getInt("invoice_manufacturerNickName_id"));
                IAECrawlerData_Bean.setInvoiceManufacturerNickName(Rs.getString("invoiceManufacturerNickName"));
                AllIAECrawlerDataList.add(IAECrawlerData_Bean);
            }
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return AllIAECrawlerDataList;
    }
    public SearchOrder_Bean getIARCrawlerDataBindingOrder(HashMap<IAECrawlerData_Bean,SearchOrder_Bean> IAECrawlerDataAndOrderMap, IAECrawlerData_Bean IAECrawlerData_Bean){
        String query;
        SearchOrder_Bean searchOrder_Bean = IAECrawlerDataAndOrderMap.get(IAECrawlerData_Bean);
        if(searchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單){
            query = "select '出貨子貨單',A.id,A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax,null, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.CashierRemark, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from SubBill A left join Customer B on A.ObjectID = B.ObjectID left join SubBill_ProjectInfo C on A.id = C.SubBill_id left join Orders_InvoiceInfo D on A.id = D.SubBill_id and D.Invalid != 1 and (D.Order_id is not null or D.SubBill_id is not null) left join SubBill_Price E on A.id = E.SubBill_id where A.id = (select subbill_id from IAECrawlerInvoice_Order E inner join SubBill F on E.subbill_id = F.id where invoice_id = ? and F.AlreadyOrderNumber = ?) ";
        }else if(searchOrder_Bean.getOrderSource() == OrderSource.出貨單){
            query = "select '出貨單',A.id,A.AlreadyOrderNumber, A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, F.total_price_include_tax, D.InvoiceNumber, C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.CashierRemark, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id and D.Invalid != 1 and (D.Order_id is not null or D.SubBill_id is not null) left join Orders_Price E on A.id = E.Order_id left join Orders_ProductGroup_Price F on A.id = F.order_id where A.id = (select order_id from IAECrawlerInvoice_Order E inner join Orders F on E.order_id = F.id where invoice_id = ? and F.AlreadyOrderNumber = ?)";
        }else
            return searchOrder_Bean;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
//            ZyhServerApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,IAECrawlerData_Bean.getId());
            PreparedStatement.setString(2,searchOrder_Bean.getAlreadyOrderNumber());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                OrderSource OrderSource = searchOrder_Bean.getOrderSource();
                searchOrder_Bean.setId(Rs.getInt(2));
                searchOrder_Bean.setOrderNumber(Rs.getString(3));
                searchOrder_Bean.setOrderDate(Rs.getString(4));
                searchOrder_Bean.setOrderObject(OrderObject.客戶);
                searchOrder_Bean.setObjectID(Rs.getString(5));
                searchOrder_Bean.setObjectName(Rs.getString(6));
                searchOrder_Bean.setPersonInCharge(Rs.getString(7));
                searchOrder_Bean.setNumberOfItems(Rs.getInt(8));
                searchOrder_Bean.setTotalPriceIncludeTax(Rs.getInt(9));
                searchOrder_Bean.setProductGroupTotalPriceIncludeTax(Rs.getObject(10) == null ? null : Rs.getInt(10));
                searchOrder_Bean.setInvoiceNumber(Rs.getString(11));
                searchOrder_Bean.setProjectName(Rs.getString(12));
                searchOrder_Bean.setProjectTotalPriceIncludeTax(Rs.getString(13));
                searchOrder_Bean.setProjectDifferentPrice(Rs.getString(14));
                searchOrder_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(15)].value());
                searchOrder_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[Rs.getInt(16)]);
                searchOrder_Bean.setCashierRemark(Rs.getString(17));
                if(OrderSource == Order_Enum.OrderSource.出貨子貨單){
                    searchOrder_Bean.setWaitingOrderDate(Rs.getString(18));
                    searchOrder_Bean.setWaitingOrderNumber(Rs.getString(19));
                    searchOrder_Bean.setAlreadyOrderDate(Rs.getString(20));
                    searchOrder_Bean.setAlreadyOrderNumber(Rs.getString(21));
                }else if(OrderSource == Order_Enum.OrderSource.進貨單 || OrderSource == Order_Enum.OrderSource.出貨單){
                    searchOrder_Bean.setAlreadyOrderNumber(Rs.getString(3));
                    searchOrder_Bean.setAlreadyOrderDate(Rs.getString(4));
                    searchOrder_Bean.setQuotationDate(Rs.getString(18));
                    searchOrder_Bean.setQuotationNumber(Rs.getString(19));
                    searchOrder_Bean.setWaitingOrderDate(Rs.getString(20));
                    searchOrder_Bean.setWaitingOrderNumber(Rs.getString(21));
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return searchOrder_Bean;
    }
    public ObservableList<SearchOrder_Bean> searchInvoiceSimilarOrder(OrderSource OrderSource,IAECrawlerData_Bean IAECrawlerData_Bean){
        ObservableList<SearchOrder_Bean> invoiceSimilarOrderList = FXCollections.observableArrayList();
        String query = "";
        if(OrderSource == Order_Enum.OrderSource.出貨子貨單)
            query = "select A.id,A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, null, D.InvoiceNumber, D.invoice_manufacturerNickName_id,(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = D.invoice_manufacturerNickName_id), C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.CashierRemark, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from SubBill A left join Customer B on A.ObjectID = B.ObjectID left join SubBill_ProjectInfo C on A.id = C.SubBill_id left join Orders_InvoiceInfo D on A.id = D.SubBill_id and D.Invalid != 1 and (D.Order_id is not null or D.SubBill_id is not null) left join SubBill_Price E on A.id = E.SubBill_id LEFT JOIN IAECrawlerAccount_ExportQuotation_Manufacturer F on D.invoice_manufacturerNickName_id = F.id where A.OrderSource = ? and A.status = '0' and ";
        else if(OrderSource == Order_Enum.OrderSource.出貨單)
            query = "select A.id,A.AlreadyOrderNumber, A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, G.total_price_include_tax, D.InvoiceNumber, D.invoice_manufacturerNickName_id,(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = D.invoice_manufacturerNickName_id), C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.CashierRemark, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber from Orders A left join Customer B on A.ObjectID = B.ObjectID left join Orders_ProjectInfo C on A.id = C.Order_id left join Orders_InvoiceInfo D on A.id = D.Order_id and D.Invalid != 1 and (D.Order_id is not null or D.SubBill_id is not null) left join Orders_Price E on A.id = E.Order_id LEFT JOIN IAECrawlerAccount_ExportQuotation_Manufacturer F on D.invoice_manufacturerNickName_id = F.id left join Orders_ProductGroup_Price G on A.id = G.order_id where A.OrderSource = ? and A.status = '0' and ";

        query = query + "A.AlreadyOrderNumber is not null and (";

        if(IAECrawlerData_Bean.getInvoiceNumber() != null)
            query = query + "(D.Invalid != '1' and D.InvoiceNumber = ? and D.invoice_manufacturerNickName_id = (select A.id from IAECrawlerAccount_ExportQuotation_Manufacturer A inner join Manufacturer B on A.Manufacturer_id = B.id where B.ObjectID = ? and A.Account = ?)) or E.TotalPriceIncludeTax = ?";
        else
            query = query + "E.TotalPriceIncludeTax = ?";
        if(OrderSource == Order_Enum.OrderSource.出貨單){
            query = query + " or G.total_price_include_tax = ?";
        }
        query = query + ")";

        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
//            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1,OrderSource.getOrderObject().ordinal());
            if(IAECrawlerData_Bean.getInvoiceNumber() != null){
                PreparedStatement.setString(2,IAECrawlerData_Bean.getInvoiceNumber());
                PreparedStatement.setString(3,IAECrawlerData_Bean.getObjectID());
                PreparedStatement.setString(4,IAECrawlerData_Bean.getObjectTaxID());
                PreparedStatement.setString(5,IAECrawlerData_Bean.getInvoiceAmount());
                if(OrderSource == Order_Enum.OrderSource.出貨單){
                    PreparedStatement.setString(6,IAECrawlerData_Bean.getInvoiceAmount());
                }
            }else {
                PreparedStatement.setString(2, IAECrawlerData_Bean.getPayAmount());
                if(OrderSource == Order_Enum.OrderSource.出貨單){
                    PreparedStatement.setString(3,IAECrawlerData_Bean.getPayAmount());
                }
            }
            Rs = PreparedStatement.executeQuery();
            int serialNumber = 0;
            while (Rs.next()) {
                serialNumber = serialNumber + 1;
                SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
                SearchOrder_Bean.setOrderSource(OrderSource);
                SearchOrder_Bean.setId(Rs.getInt(1));
                SearchOrder_Bean.setSerialNumber(serialNumber);
                SearchOrder_Bean.setOrderNumber(Rs.getString(2));
                SearchOrder_Bean.setOrderDate(Rs.getString(3));
                SearchOrder_Bean.setOrderObject(OrderObject.客戶);
                SearchOrder_Bean.setObjectID(Rs.getString(4));
                SearchOrder_Bean.setObjectName(Rs.getString(5));
                SearchOrder_Bean.setPersonInCharge(Rs.getString(6));
                SearchOrder_Bean.setNumberOfItems(Rs.getInt(7));
                SearchOrder_Bean.setTotalPriceIncludeTax(Rs.getInt(8));
                SearchOrder_Bean.setProductGroupTotalPriceIncludeTax(Rs.getObject(9) == null ? null : Rs.getInt(9));
                SearchOrder_Bean.setInvoiceNumber(Rs.getString(10));
                SearchOrder_Bean.setInvoice_manufacturerNickName_id(Rs.getInt(11));
                SearchOrder_Bean.setInvoiceManufacturerNickName(Rs.getString(12));
                SearchOrder_Bean.setProjectName(Rs.getString(13));
                SearchOrder_Bean.setProjectTotalPriceIncludeTax(Rs.getString(14));
                SearchOrder_Bean.setProjectDifferentPrice(Rs.getString(15));
                SearchOrder_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(16)].value());
                SearchOrder_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[Rs.getInt(17)]);
                SearchOrder_Bean.setCashierRemark(Rs.getString(18));
                if(OrderSource == Order_Enum.OrderSource.出貨子貨單){
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(19));
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(20));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(21));
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(22));
                }else if(OrderSource == Order_Enum.OrderSource.進貨單 || OrderSource == Order_Enum.OrderSource.出貨單){
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(2));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(3));
                    SearchOrder_Bean.setQuotationDate(Rs.getString(19));
                    SearchOrder_Bean.setQuotationNumber(Rs.getString(20));
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(21));
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(22));
                }
                invoiceSimilarOrderList.add(SearchOrder_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return invoiceSimilarOrderList;
    }
    public ObservableList<SearchOrder_Bean> searchNoneBindingManufacturerOrder(OrderSource orderSource, int manufacturerNickName_id, LocalDate startDate, LocalDate endDate){
        ObservableList<SearchOrder_Bean> orderList = FXCollections.observableArrayList();
        String query = "";
        if(orderSource == Order_Enum.OrderSource.出貨子貨單) {
            query = "select A.id,A.OrderNumber, A.OrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, null, D.InvoiceNumber, D.invoice_manufacturerNickName_id,(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = D.invoice_manufacturerNickName_id), C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.CashierRemark, A.WaitingOrderDate, A.WaitingOrderNumber, A.AlreadyOrderDate, A.AlreadyOrderNumber from SubBill A inner join Customer B on A.ObjectID = B.ObjectID inner join SubBill_ProjectInfo C on A.id = C.SubBill_id inner join Orders_InvoiceInfo D on A.id = D.SubBill_id and (D.Order_id is not null or D.SubBill_id is not null) inner join SubBill_Price E on A.id = E.SubBill_id inner JOIN IAECrawlerAccount_ExportQuotation_Manufacturer F on D.invoice_manufacturerNickName_id = F.id where Not Exists(select 1 from IAECrawlerInvoice_Order where SubBill_id = A.id) and ";
        }else if(orderSource == Order_Enum.OrderSource.出貨單) {
            query = "select A.id,A.AlreadyOrderNumber, A.AlreadyOrderDate, A.ObjectID, B.ObjectName, B.PersonInCharge, A.NumberOfItems, E.TotalPriceIncludeTax, G.total_price_include_tax, D.InvoiceNumber, D.invoice_manufacturerNickName_id,(select ManufacturerNickName from IAECrawlerAccount_ExportQuotation_Manufacturer where id = D.invoice_manufacturerNickName_id), C.ProjectName, C.ProjectTotalPriceIncludeTax, C.ProjectDifferentPrice, A.isCheckout, A.isOffset, A.CashierRemark, A.OrderDate, A.OrderNumber, A.WaitingOrderDate, A.WaitingOrderNumber from Orders A inner join Customer B on A.ObjectID = B.ObjectID inner join Orders_ProjectInfo C on A.id = C.Order_id inner join Orders_InvoiceInfo D on A.id = D.Order_id and (D.Order_id is not null or D.SubBill_id is not null) inner join Orders_Price E on A.id = E.Order_id LEFT JOIN IAECrawlerAccount_ExportQuotation_Manufacturer F on D.invoice_manufacturerNickName_id = F.id left join Orders_ProductGroup_Price G on A.id = G.order_id where Not Exists(select 1 from IAECrawlerInvoice_Order where Order_id = A.id) and ";
        }
        query = query + "A.status = ? and A.AlreadyOrderNumber is not null and D.Invalid = ? and D.invoice_manufacturerNickName_id = ? and A.AlreadyOrderDate between ? and ?";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, Order_Enum.OrderStatus.有效貨單.ordinal());
            PreparedStatement.setInt(2, Invoice_Enum.InvoiceInvalid.未作廢.ordinal());
            PreparedStatement.setInt(3, manufacturerNickName_id);
            PreparedStatement.setDate(4, Date.valueOf(startDate));
            PreparedStatement.setDate(5, Date.valueOf(endDate));
            Rs = PreparedStatement.executeQuery();
            int serialNumber = 0;
            while (Rs.next()) {
                serialNumber = serialNumber + 1;
                SearchOrder_Bean SearchOrder_Bean = new SearchOrder_Bean();
                SearchOrder_Bean.setOrderSource(orderSource);
                SearchOrder_Bean.setId(Rs.getInt(1));
                SearchOrder_Bean.setSerialNumber(serialNumber);
                SearchOrder_Bean.setOrderNumber(Rs.getString(2));
                SearchOrder_Bean.setOrderDate(Rs.getString(3));
                SearchOrder_Bean.setOrderObject(OrderObject.客戶);
                SearchOrder_Bean.setObjectID(Rs.getString(4));
                SearchOrder_Bean.setObjectName(Rs.getString(5));
                SearchOrder_Bean.setPersonInCharge(Rs.getString(6));
                SearchOrder_Bean.setNumberOfItems(Rs.getInt(7));
                SearchOrder_Bean.setTotalPriceIncludeTax(Rs.getInt(8));
                SearchOrder_Bean.setProductGroupTotalPriceIncludeTax(Rs.getObject(9) == null ? null : Rs.getInt(9));

                SearchOrder_Bean.setInvoiceNumber(Rs.getString(10));
                SearchOrder_Bean.setInvoice_manufacturerNickName_id(Rs.getInt(11));
                SearchOrder_Bean.setInvoiceManufacturerNickName(Rs.getString(12));
                SearchOrder_Bean.setProjectName(Rs.getString(13));
                SearchOrder_Bean.setProjectTotalPriceIncludeTax(Rs.getString(14));
                SearchOrder_Bean.setProjectDifferentPrice(Rs.getString(15));
                SearchOrder_Bean.setIsCheckout(Order_Enum.CheckoutStatus.values()[Rs.getInt(16)].value());
                SearchOrder_Bean.setIsOffset(Order_Enum.OffsetOrderStatus.values()[Rs.getInt(17)]);
                SearchOrder_Bean.setCashierRemark(Rs.getString(18));
                if(orderSource == Order_Enum.OrderSource.出貨子貨單){
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(19));
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(20));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(21));
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(22));
                }else if(orderSource == Order_Enum.OrderSource.進貨單 || orderSource == Order_Enum.OrderSource.出貨單){
                    SearchOrder_Bean.setAlreadyOrderNumber(Rs.getString(2));
                    SearchOrder_Bean.setAlreadyOrderDate(Rs.getString(3));
                    SearchOrder_Bean.setQuotationDate(Rs.getString(19));
                    SearchOrder_Bean.setQuotationNumber(Rs.getString(20));
                    SearchOrder_Bean.setWaitingOrderDate(Rs.getString(21));
                    SearchOrder_Bean.setWaitingOrderNumber(Rs.getString(22));
                }
                orderList.add(SearchOrder_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return orderList;
    }
    public Integer getOrderBindingPayment_id(SearchOrder_Bean SearchOrder_Bean){
        Integer bindingPayment_id = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "";
            if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨單)
                Query = "select invoice_id from IAECrawlerInvoice_Order where order_id = ?";
            else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單)
                Query = "select invoice_id from IAECrawlerInvoice_Order where subbill_id = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,SearchOrder_Bean.getId());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                bindingPayment_id = Rs.getObject(1) == null ? null : Rs.getInt(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return bindingPayment_id;
    }
    public boolean isInvoiceExist(Invoice_Bean Invoice_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "select 1 from Orders_InvoiceInfo where InvoiceNumber = ? and InvoiceYear= ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,Invoice_Bean.getInvoiceNumber());
            PreparedStatement.setInt(2,Invoice_Bean.getInvoiceYear());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public boolean fillBackOrderInvoiceInfo(SearchOrder_Bean searchOrder_Bean, IAECrawlerData_Bean IAECrawlerData_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION ";
            if(searchOrder_Bean.getOrderSource() == OrderSource.出貨單){
                Query = Query + "IF EXISTS (select id from Orders_InvoiceInfo where Order_id = ?) begin " +
                        "update Orders_InvoiceInfo set invoice_manufacturerNickName_id = ? where Order_id = ? " +
                        "END ELSE BEGIN " +
                        "insert into Orders_InvoiceInfo (InvoiceNumber,InvoiceYear,InvoiceDate,InvoicePrice,InvoiceType,Invalid,Ignore,Order_id,SubBill_id,invoice_manufacturerNickName_id) values (?,?,?,?,?,?,?,?,null,?) END ";
            }else if(searchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單){
                Query = Query + "IF EXISTS (select id from Orders_InvoiceInfo where SubBill_id = ?) begin " +
                        "update Orders_InvoiceInfo set invoice_manufacturerNickName_id = ? where SubBill_id = ? " +
                        "END ELSE BEGIN " +
                        "insert into Orders_InvoiceInfo (InvoiceNumber,InvoiceYear,InvoiceDate,InvoicePrice,InvoiceType,Invalid,Ignore,Order_id,SubBill_id,invoice_manufacturerNickName_id) values (?,?,?,?,?,?,?,null,?,?) END ";
            }
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR) " +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
//            ERP.ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, searchOrder_Bean.getId());
            PreparedStatement.setInt(2, IAECrawlerData_Bean.getInvoice_manufacturerNickName_id());
            PreparedStatement.setInt(3, searchOrder_Bean.getId());

            String invoiceDate = searchOrder_Bean.getOrderDate();
            if(IAECrawlerData_Bean.getInvoiceDate() != null)
                invoiceDate = IAECrawlerData_Bean.getInvoiceDate().toString();
            PreparedStatement.setString(4, IAECrawlerData_Bean.getInvoiceNumber());
            PreparedStatement.setObject(5, invoiceDate == null ? null : Integer.parseInt(invoiceDate.substring(0,invoiceDate.indexOf("-")))-1911);
            PreparedStatement.setDate(6, IAECrawlerData_Bean.getInvoiceDate() == null ? null : Date.valueOf(IAECrawlerData_Bean.getInvoiceDate()));
            PreparedStatement.setInt(7, Integer.parseInt(IAECrawlerData_Bean.getInvoiceAmount()));
            PreparedStatement.setInt(8, Invoice_Enum.InvoiceType.二聯式.ordinal());
            PreparedStatement.setInt(9, Invoice_Enum.InvoiceInvalid.未作廢.ordinal());
            PreparedStatement.setInt(10, Invoice_Enum.InvoiceIgnore.未忽略.ordinal());
            PreparedStatement.setInt(11, searchOrder_Bean.getId());
            PreparedStatement.setInt(12, IAECrawlerData_Bean.getInvoice_manufacturerNickName_id());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean updateOrderInvoiceManufacturerNickNameID(SearchOrder_Bean searchOrder_Bean, int newInvoiceManufacturerNickNameID){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(searchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單)
                Query = Query + "update Orders_InvoiceInfo set invoice_manufacturerNickName_id = ? where SubBill_id = ? ";
            else if(searchOrder_Bean.getOrderSource() == OrderSource.出貨單)
                Query = Query + "update Orders_InvoiceInfo set invoice_manufacturerNickName_id = ? where Order_id = ?  ";
                    Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR) " +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
//            ERP.ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, newInvoiceManufacturerNickNameID);
            PreparedStatement.setInt(2, searchOrder_Bean.getId());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean setIAECrawlerInvoiceBindingOrder(boolean isAllowManyOrder, IAECrawlerData_Bean IAECrawlerData_Bean,SearchOrder_Bean SearchOrder_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            SearchOrder_Bean.setCashierRemark(SearchOrder_Bean.getCashierRemark() + generateBindingOrderCashierRemark(IAECrawlerData_Bean));
            String Query = "BEGIN TRY " +
                    "BEGIN TRANSACTION ";
            if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨單){
                if(isAllowManyOrder){
                    Query = Query + "insert into IAECrawlerInvoice_Order (invoice_id,order_id,subbill_id) values (?,?,?) ";
                }else{
                    Query = Query + "IF EXISTS (select id from IAECrawlerInvoice_Order where invoice_id = ?) begin " +
                            "update IAECrawlerInvoice_Order set order_id = ?,subbill_id = null where invoice_id = ? " +
                            "END ELSE BEGIN " +
                            "insert into IAECrawlerInvoice_Order (invoice_id,order_id,subbill_id) values (?,?,?) END ";
                }
                Query = Query + "update Orders set CashierRemark = ? where id = ? ";
            }else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單){
                if(isAllowManyOrder){
                    Query = Query + "insert into IAECrawlerInvoice_Order (invoice_id,order_id,subbill_id) values (?,?,?) ";
                }else{
                    Query = Query + "IF EXISTS (select id from IAECrawlerInvoice_Order where invoice_id = ?) BEGIN " +
                            "update IAECrawlerInvoice_Order set order_id = null,subbill_id = ? where invoice_id = ? " +
                            "END ELSE BEGIN " +
                            "insert into IAECrawlerInvoice_Order (invoice_id,order_id,subbill_id) values (?,?,?) END ";
                }
                Query = Query + "update SubBill set CashierRemark = ? where id = ? ";
            }
            Query = Query + "update IAECrawlerPayment set ReviewStatus = ? where id = ? ";
            Query = Query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR) " +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1, IAECrawlerData_Bean.getId());
            if(!isAllowManyOrder){
                PreparedStatement.setInt(2, SearchOrder_Bean.getId());
                PreparedStatement.setInt(3, IAECrawlerData_Bean.getId());
                PreparedStatement.setInt(4, IAECrawlerData_Bean.getId());
            }
            if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨單) {
                PreparedStatement.setInt(isAllowManyOrder ? 2 : 5, SearchOrder_Bean.getId());
                PreparedStatement.setNull(isAllowManyOrder ? 3 : 6, INTEGER);
            }else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單) {
                PreparedStatement.setNull(isAllowManyOrder ? 2 : 5, INTEGER);
                PreparedStatement.setInt(isAllowManyOrder ? 3 : 6, SearchOrder_Bean.getId());
            }
            PreparedStatement.setString(isAllowManyOrder ? 4 : 7, SearchOrder_Bean.getCashierRemark());
            PreparedStatement.setInt(isAllowManyOrder ? 5 : 8, SearchOrder_Bean.getId());
            PreparedStatement.setInt(isAllowManyOrder ? 6 : 9, IAECrawlerReviewStatus.已查核.ordinal());
            PreparedStatement.setInt(isAllowManyOrder ? 7 : 10, IAECrawlerData_Bean.getId());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    private String generateBindingOrderCashierRemark(IAECrawlerData_Bean IAECrawlerData_Bean){
        String cashierRemark = "【付款日期】" + IAECrawlerData_Bean.getPayDate().toString() + " ";
        cashierRemark = cashierRemark + "【付款金額】" + IAECrawlerData_Bean.getPayAmount() + " ";
        cashierRemark = cashierRemark + "【匯費】" + IAECrawlerData_Bean.getRemittanceFee() + " ";
        return "@~" + cashierRemark + "~@";
    }
    public boolean deleteIAECrawlerDataBindingOrder(int IAECrawlerPayment_id, SearchOrder_Bean SearchOrder_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String cashierRemark = SearchOrder_Bean.getCashierRemark();
            if(cashierRemark.contains("@~") && cashierRemark.contains("~@"))
                cashierRemark = cashierRemark.substring(0,cashierRemark.indexOf("@~")) + cashierRemark.substring(cashierRemark.lastIndexOf("~@")+2);
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨單) {
                query = query + "delete IAECrawlerInvoice_Order where invoice_id = ? and order_id = ? " +
                        "update Orders set CashierRemark = ? where id = ? ";
            }else if(SearchOrder_Bean.getOrderSource() == OrderSource.出貨子貨單) {
                query = query + "delete IAECrawlerInvoice_Order where invoice_id = ? and subbill_id = ? " +
                            "update SubBill set CashierRemark = ? where id = ? ";
            }
            query = query + "IF ((select count(*) from IAECrawlerInvoice_Order where invoice_id = ?) != 0) begin " +
                    "update IAECrawlerPayment set ReviewStatus = ? where id = ? " +
                    "END ELSE BEGIN " +
                    "update IAECrawlerPayment set ReviewStatus = ? where id = ? end " +
            "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR) " +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, IAECrawlerPayment_id);
            PreparedStatement.setInt(2, SearchOrder_Bean.getId());
            PreparedStatement.setString(3, cashierRemark);
            PreparedStatement.setInt(4, SearchOrder_Bean.getId());
            PreparedStatement.setInt(5, IAECrawlerPayment_id);
            PreparedStatement.setInt(6, IAECrawlerReviewStatus.已查核.ordinal());
            PreparedStatement.setInt(7, IAECrawlerPayment_id);
            PreparedStatement.setInt(8, IAECrawlerReviewStatus.未查核.ordinal());
            PreparedStatement.setInt(9, IAECrawlerPayment_id);
            PreparedStatement.executeUpdate();
            status = true;
            SearchOrder_Bean.setCashierRemark(cashierRemark);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public ObservableList<IAECrawler_ExportPurchaseNote_Bean> searchPurchaseNoteInfo(ObjectInfo_Bean ObjectInfo_Bean, String startDate, String endDate){
        ObservableList<IAECrawler_ExportPurchaseNote_Bean> purchaseNoteList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "(SELECT B.isCheckout,G.PayDate,G.PayAmount,E.ObjectName,A.InvoiceDate as '日期',A.InvoiceType,A.InvoiceNumber,C.TotalPriceIncludeTax,B.ObjectID,B.AlreadyOrderDate,B.AlreadyOrderNumber,D.ProjectName,D.ProjectTotalPriceIncludeTax,B.CashierRemark " +
                    "FROM Orders_InvoiceInfo A " +
                    "INNER JOIN Orders B ON A.Order_id = B.id and B.status = '0' " +
                    "INNER JOIN Orders_Price C ON B.id = C.Order_id " +
                    "INNER JOIN Orders_ProjectInfo D ON B.id = D.Order_id " +
                    "INNER JOIN Manufacturer E on E.id = (select Manufacturer_id from IAECrawlerAccount_ExportQuotation_Manufacturer where id = A.invoice_manufacturerNickName_id) " +
                    "LEFT JOIN IAECrawlerInvoice_Order F on B.id = F.order_id " +
                    "LEFT JOIN IAECrawlerPayment G on F.invoice_id = G.id " +
                    "LEFT JOIN Orders_ProductGroup_Price I on B.id = I.order_id ";
            if(ObjectInfo_Bean != null)
                Query = Query + "INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer H on A.invoice_manufacturerNickName_id = H.id ";
            Query = Query + "WHERE A.Invalid != ? and B.AlreadyOrderDate is not null and A.InvoiceDate BETWEEN ? and ?";
            if(ObjectInfo_Bean != null)
                Query = Query + " and H.Manufacturer_id = (select id from Manufacturer where ObjectID = ?)";

            Query = Query + " union all ";
            Query = Query + "SELECT B.isCheckout,G.PayDate,G.PayAmount,E.ObjectName,A.InvoiceDate as '日期',A.InvoiceType,A.InvoiceNumber,C.TotalPriceIncludeTax,B.ObjectID,B.AlreadyOrderDate,B.AlreadyOrderNumber,D.ProjectName,D.ProjectTotalPriceIncludeTax,B.CashierRemark " +
                    "FROM Orders_InvoiceInfo A " +
                    "INNER JOIN SubBill B ON A.SubBill_id = B.id and B.status = '0' " +
                    "INNER JOIN SubBill_Price C ON B.id = C.SubBill_id " +
                    "INNER JOIN SubBill_ProjectInfo D ON B.id = D.SubBill_id " +
                    "INNER JOIN Manufacturer E on E.id = (select Manufacturer_id from IAECrawlerAccount_ExportQuotation_Manufacturer where id = A.invoice_manufacturerNickName_id) " +
                    "LEFT JOIN IAECrawlerInvoice_Order F on B.id = F.SubBill_id " +
                    "LEFT JOIN IAECrawlerPayment G on F.invoice_id = G.id ";
            if(ObjectInfo_Bean != null)
                Query = Query + "INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer H on A.invoice_manufacturerNickName_id = H.id ";
            Query = Query + "WHERE A.Invalid != ? and B.AlreadyOrderDate is not null and A.InvoiceDate BETWEEN ? and ?";
            if(ObjectInfo_Bean != null)
                Query = Query + " and H.Manufacturer_id = (select id from Manufacturer where ObjectID = ?)";
            Query = Query + ") order by '日期'";

            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,Invoice_Enum.InvoiceInvalid.已作廢.ordinal());
            PreparedStatement.setString(2,startDate);
            PreparedStatement.setString(3,endDate);
            if(ObjectInfo_Bean != null) {
                PreparedStatement.setString(4, ObjectInfo_Bean.getObjectID());
                PreparedStatement.setInt(5,Invoice_Enum.InvoiceInvalid.已作廢.ordinal());
                PreparedStatement.setString(6,startDate);
                PreparedStatement.setString(7,endDate);
                PreparedStatement.setString(8, ObjectInfo_Bean.getObjectID());
            }else{
                PreparedStatement.setInt(4,Invoice_Enum.InvoiceInvalid.已作廢.ordinal());
                PreparedStatement.setString(5,startDate);
                PreparedStatement.setString(6,endDate);
            }
            Rs = PreparedStatement.executeQuery();
            int serialNumber = 0;
            while (Rs.next()) {
                serialNumber = serialNumber + 1;
                IAECrawler_ExportPurchaseNote_Bean IAECrawler_ExportPurchaseNote_Bean = new IAECrawler_ExportPurchaseNote_Bean();
                IAECrawler_ExportPurchaseNote_Bean.setSerialNumber(serialNumber);
                IAECrawler_ExportPurchaseNote_Bean.setCheckoutStatus(CheckoutStatus.values()[Rs.getInt(1)]);
                IAECrawler_ExportPurchaseNote_Bean.setSchoolPayDate(Rs.getString(2));
                IAECrawler_ExportPurchaseNote_Bean.setSchoolPayAmount(Rs.getObject(3) == null ? null : Rs.getInt(3));
                IAECrawler_ExportPurchaseNote_Bean.setManufacturerName(Rs.getString(4));
                IAECrawler_ExportPurchaseNote_Bean.setInvoiceDate(Rs.getString(5));
                IAECrawler_ExportPurchaseNote_Bean.setInvoiceType(Invoice_Enum.InvoiceType.values()[Rs.getInt(6)]);
                IAECrawler_ExportPurchaseNote_Bean.setInvoiceNumber(Rs.getString(7));
                IAECrawler_ExportPurchaseNote_Bean.setInvoiceAmount(Rs.getInt(8));      //  預設出貨單金額
                IAECrawler_ExportPurchaseNote_Bean.setCustomerID(Rs.getString(9));
                IAECrawler_ExportPurchaseNote_Bean.setOrderDate(Rs.getString(10));
                IAECrawler_ExportPurchaseNote_Bean.setOrderNumber(Rs.getString(11));
                IAECrawler_ExportPurchaseNote_Bean.setProjectName(Rs.getString(12));
                IAECrawler_ExportPurchaseNote_Bean.setProjectTotalPriceIncludeTax(Rs.getString(13).equals("") ? Rs.getInt(8) : Integer.parseInt(Rs.getString(13)));
                IAECrawler_ExportPurchaseNote_Bean.setCashierRemark(Rs.getString(14) == null ? "" : Rs.getString(14));
                purchaseNoteList.add(IAECrawler_ExportPurchaseNote_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return purchaseNoteList;
    }
    public boolean insertManufacturerContactDetailIsCheckout(ObservableList<IAECrawler_ManufacturerContactDetail_Bean> manufacturerContactDetailList, PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail checkoutStatus_ManufacturerContactDetail){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : manufacturerContactDetailList){
                if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null)
                    query = query + "if not exists (select 1 from Manufacturer_ContactDetail where order_id = ?) begin ";
                else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null)
                    query = query + "if not exists (select 1 from Manufacturer_ContactDetail where subBill_id = ?) begin ";

                PayableReceivable_Enum.ManufacturerContactDetailSource ManufacturerContactDetailSource = IAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource();
                if(ManufacturerContactDetailSource == PayableReceivable_Enum.ManufacturerContactDetailSource.廠商進貨) {
                    if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null) {
                        if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部){
                            query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (?,null,'" + CheckoutStatus.已結帳.ordinal() + "','" + CheckoutStatus.已結帳.ordinal() + "',0,0) end else begin " +
                                    "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.已結帳.ordinal() + "',source_purchase_alreadyPay = '" + CheckoutStatus.已結帳.ordinal() + "' where order_id = ? ";
                        }else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已開發票){
                            query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (?,null,'" + CheckoutStatus.已結帳.ordinal() + "',0,0,0) end else begin " +
                                    "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.已結帳.ordinal() + "' where order_id = ? ";
                        }else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已給付貨款金額){
                            query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (?,null,0,'" + CheckoutStatus.已結帳.ordinal() + "',0,0) end else begin " +
                                    "update Manufacturer_ContactDetail set source_purchase_alreadyPay = '" + CheckoutStatus.已結帳.ordinal() + "' where order_id = ? ";
                        }
                    }else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null) {
                        if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部){
                            query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (null,?,'" + CheckoutStatus.已結帳.ordinal() + "','" + CheckoutStatus.已結帳.ordinal() + "',0,0) end else begin " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.已結帳.ordinal() + "',source_purchase_alreadyPay = '" + CheckoutStatus.已結帳.ordinal() + "' where subBill_id = ? ";
                        }else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已開發票){
                            query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (null,?,'" + CheckoutStatus.已結帳.ordinal() + "',0,0,0) end else begin " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.已結帳.ordinal() + "' where subBill_id = ? ";
                        }else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已給付貨款金額){
                            query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (null,?,0,'" + CheckoutStatus.已結帳.ordinal() + "',0,0) end else begin " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyPay = '" + CheckoutStatus.已結帳.ordinal() + "' where subBill_id = ? ";
                        }
                    }
                }else if(ManufacturerContactDetailSource == PayableReceivable_Enum.ManufacturerContactDetailSource.出貨已開發票) {
                    if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null)
                        query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (?,null,0,0,'" + CheckoutStatus.已結帳.ordinal() + "',0) end else begin " +
                            "update Manufacturer_ContactDetail set source_shipment = '" + CheckoutStatus.已結帳.ordinal() + "' where order_id = ? ";
                    else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null)
                        query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (null,?,0,0,'" + CheckoutStatus.已結帳.ordinal() + "',0) end else begin " +
                            "update Manufacturer_ContactDetail set source_shipment = '" + CheckoutStatus.已結帳.ordinal() + "' where subBill_id = ? ";
                }else if(ManufacturerContactDetailSource == PayableReceivable_Enum.ManufacturerContactDetailSource.學校出納) {
                    if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null)
                        query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (?,null,0,0,0,'" + CheckoutStatus.已結帳.ordinal() + "') end else begin " +
                            "update Manufacturer_ContactDetail set source_schoolPayment = '" + CheckoutStatus.已結帳.ordinal() + "' where order_id = ? ";
                    else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null)
                        query = query + "insert into Manufacturer_ContactDetail(order_id,subBill_id,source_purchase_alreadyInvoice,source_purchase_alreadyPay,source_shipment,source_schoolPayment) values (null,?,0,0,0,'" + CheckoutStatus.已結帳.ordinal() + "') end else begin " +
                            "update Manufacturer_ContactDetail set source_schoolPayment = '" + CheckoutStatus.已結帳.ordinal() + "' where subBill_id = ? ";
                }
                query = query + "end ";
            }
            query = query + "COMMIT TRANSACTION " +
                "END TRY " +
                "BEGIN CATCH " +
                "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR) " +
                "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                "ROLLBACK TRANSACTION " +
                "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            int index = 1;
            for(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean : manufacturerContactDetailList){
                PreparedStatement.setInt(index, IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null ? IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() : IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id());
                index = index + 1;
                PreparedStatement.setInt(index, IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null ? IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() : IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id());
                index = index + 1;
                PreparedStatement.setInt(index, IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null ? IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() : IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id());
                index = index + 1;
            }
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public boolean deleteManufacturerContactDetailIsCheckout(IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean, PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail checkoutStatus_ManufacturerContactDetail){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try {
            String query = "BEGIN TRY BEGIN TRANSACTION ";
            PayableReceivable_Enum.ManufacturerContactDetailSource ManufacturerContactDetailSource = IAECrawler_ManufacturerContactDetail_Bean.getManufacturerContactDetailSource();
            if(ManufacturerContactDetailSource == PayableReceivable_Enum.ManufacturerContactDetailSource.廠商進貨){
                if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null) {
                    if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        query = query + "if exists(select 1 from Manufacturer_ContactDetail where order_id = ? and (source_shipment != '0' or source_schoolPayment != '0')) BEGIN " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.未結帳.ordinal() + "',source_purchase_alreadyPay = '" + CheckoutStatus.未結帳.ordinal() + "' where order_id = ? ";
                    else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已開發票)
                        query = query + "if exists(select 1 from Manufacturer_ContactDetail where order_id = ? and (source_purchase_alreadyPay != '0' or source_shipment != '0' or source_schoolPayment != '0')) BEGIN " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.未結帳.ordinal() + "' where order_id = ? ";
                    else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已給付貨款金額)
                        query = query + "if exists(select 1 from Manufacturer_ContactDetail where order_id = ? and (source_purchase_alreadyInvoice != '0' or source_shipment != '0' or source_schoolPayment != '0')) BEGIN " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyPay = '" + CheckoutStatus.未結帳.ordinal() + "' where order_id = ? ";
                }else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null) {
                    if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        query = query + "if exists(select 1 from Manufacturer_ContactDetail where subBill_id = ? and (source_shipment != '0' or source_schoolPayment != '0')) BEGIN " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.未結帳.ordinal() + "',source_purchase_alreadyPay = '" + CheckoutStatus.未結帳.ordinal() + "' where subBill_id = ? ";
                    else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已開發票)
                        query = query + "if exists(select 1 from Manufacturer_ContactDetail where subBill_id = ? and (source_purchase_alreadyPay != '0' or source_shipment != '0' or source_schoolPayment != '0')) BEGIN " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyInvoice = '" + CheckoutStatus.未結帳.ordinal() + "' where subBill_id = ? ";
                    else if(checkoutStatus_ManufacturerContactDetail == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已給付貨款金額)
                        query = query + "if exists(select 1 from Manufacturer_ContactDetail where subBill_id = ? and (source_purchase_alreadyInvoice != '0' or source_shipment != '0' or source_schoolPayment != '0')) BEGIN " +
                                "update Manufacturer_ContactDetail set source_purchase_alreadyPay = '" + CheckoutStatus.未結帳.ordinal() + "' where subBill_id = ? ";
                }
            }else if(ManufacturerContactDetailSource == PayableReceivable_Enum.ManufacturerContactDetailSource.出貨已開發票){
                if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null)
                    query = query + "if exists(select 1 from Manufacturer_ContactDetail where order_id = ? and (source_purchase_alreadyInvoice != '0' or source_purchase_alreadyPay != '0' or source_schoolPayment != '0')) BEGIN " +
                        "update Manufacturer_ContactDetail set source_shipment = '" + CheckoutStatus.未結帳.ordinal() + "' where order_id = ? ";
                else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null)
                    query = query + "if exists(select 1 from Manufacturer_ContactDetail where order_id = ? and (source_purchase_alreadyInvoice != '0' or source_purchase_alreadyPay != '0' or source_schoolPayment != '0')) BEGIN " +
                        "update Manufacturer_ContactDetail set source_shipment = '" + CheckoutStatus.未結帳.ordinal() + "' where subBill_id = ? ";
            }else if(ManufacturerContactDetailSource == PayableReceivable_Enum.ManufacturerContactDetailSource.學校出納){
                if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null)
                    query = query + "if exists(select 1 from Manufacturer_ContactDetail where order_id = ? and (source_purchase_alreadyInvoice != '0' or source_purchase_alreadyPay != '0' or source_shipment != '0')) BEGIN " +
                        "update Manufacturer_ContactDetail set source_schoolPayment = '" + CheckoutStatus.未結帳.ordinal() + "' where order_id = ? ";
                else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null)
                    query = query + "if exists(select 1 from Manufacturer_ContactDetail where order_id = ? and (source_purchase_alreadyInvoice != '0' or source_purchase_alreadyPay != '0' or source_shipment != '0')) BEGIN " +
                        "update Manufacturer_ContactDetail set source_schoolPayment = '" + CheckoutStatus.未結帳.ordinal() + "' where subBill_id = ? ";
            }
            if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() == null)
                query = query + "END ELSE BEGIN " +
                        "delete from Manufacturer_ContactDetail where order_id = ? " +
                        "END ";
            else if(IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() == null && IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id() != null)
                query = query + "END ELSE BEGIN " +
                        "delete from Manufacturer_ContactDetail where subBill_id = ? " +
                        "END ";
            query = query + "COMMIT TRANSACTION " +
                    "END TRY " +
                    "BEGIN CATCH " +
                    "DECLARE @ErrorMessage As VARCHAR(1000) = CHAR(10)+'錯誤代碼：' +CAST(ERROR_NUMBER() AS VARCHAR) " +
                    "+CHAR(10)+'錯誤訊息：'+	ERROR_MESSAGE()" +
                    "+CHAR(10)+'錯誤行號：'+CAST(ERROR_LINE() AS VARCHAR)" +
                    "+CHAR(10)+'錯誤程序名稱：'+ISNULL(ERROR_PROCEDURE(),'')"+
                    "DECLARE @ErrorSeverity As Numeric = ERROR_SEVERITY()" +
                    "DECLARE @ErrorState As Numeric = ERROR_STATE()" +
                    "RAISERROR( @ErrorMessage, @ErrorSeverity, @ErrorState);" +
                    "ROLLBACK TRANSACTION " +
                    "END CATCH";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(query);
            PreparedStatement.setInt(1, IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null ? IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() : IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id());
            PreparedStatement.setInt(2,IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null ? IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() : IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id());
            PreparedStatement.setInt(3,IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() != null ? IAECrawler_ManufacturerContactDetail_Bean.getOrder_id() : IAECrawler_ManufacturerContactDetail_Bean.getSubBill_id());
            PreparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,null);
        }
        return status;
    }
    public ObservableList<IAECrawler_ManufacturerContactDetail_Bean> searchManufacturerContactDetail(String objectID,String startDate, String endDate,CheckoutStatus searchCheckoutStatus){
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> iAECrawlerPaymentList = getNonePayment_ContactDetail(objectID, startDate,endDate,searchCheckoutStatus);
        iAECrawlerPaymentList.addAll(getEstablishInvoice_ContactDetail(objectID, startDate,endDate,searchCheckoutStatus));
        iAECrawlerPaymentList.addAll(getAlreadyPayment_ContactDetail(objectID,startDate,endDate,searchCheckoutStatus));
        sortManufacturerContactDetailByDate(iAECrawlerPaymentList);
        handleSameIAEPaymentRepeatSchoolAlreadyPayment(iAECrawlerPaymentList);
        return iAECrawlerPaymentList;
    }
    protected void sortManufacturerContactDetailByDate(ObservableList<IAECrawler_ManufacturerContactDetail_Bean> ManufacturerContactDetailList){
        Collections.sort(ManufacturerContactDetailList, new Comparator<IAECrawler_ManufacturerContactDetail_Bean>() {
            public int compare(IAECrawler_ManufacturerContactDetail_Bean o1, IAECrawler_ManufacturerContactDetail_Bean o2) {
                return o1.getOrderDate().compareTo(o2.getOrderDate());
            }
        });
        for(int index = 0 ; index < ManufacturerContactDetailList.size() ; index++ ){
            ManufacturerContactDetailList.get(index).setSerialNumber(index+1);
        }
    }
    private void handleSameIAEPaymentRepeatSchoolAlreadyPayment(ObservableList<IAECrawler_ManufacturerContactDetail_Bean> iAECrawlerPaymentList){
        HashMap<Integer, Boolean> sameIAEPayment = new HashMap<>();
        for(int index = iAECrawlerPaymentList.size()-1; index >= 0; index--){
            IAECrawler_ManufacturerContactDetail_Bean iAECrawler_ManufacturerContactDetail_Bean = iAECrawlerPaymentList.get(index);
            if(iAECrawler_ManufacturerContactDetail_Bean.getPayment_id() != null){
                if(!sameIAEPayment.containsKey(iAECrawler_ManufacturerContactDetail_Bean.getPayment_id())) {
                    sameIAEPayment.put(iAECrawler_ManufacturerContactDetail_Bean.getPayment_id(), true);
                }else{
                    iAECrawler_ManufacturerContactDetail_Bean.setSchoolAlreadyAmount(null);
                }
            }
        }
    }
    private ObservableList<IAECrawler_ManufacturerContactDetail_Bean> getNonePayment_ContactDetail(String ObjectID, String startDate, String endDate, CheckoutStatus searchCheckoutStatus){
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> IAECrawlerPaymentList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "(SELECT A.AlreadyOrderDate AS '日期','' AS '已開發票',case when(C.Tax = '0') then '' else CAST(C.TotalPriceIncludeTax as nvarchar(10)) end AS '未開發票','' AS '可開發票餘額',B.ProjectName AS '專案名稱',null AS '發票號碼','' AS '聯單','' AS '學校已付金額',C.TotalPriceIncludeTax AS '未給付金額','' AS '繳、退金額',A.CashierRemark as '備註','' as '所屬學校',A.id as 'order_id',null as 'subBill_id'," +
                    "(select source_purchase_alreadyInvoice from Manufacturer_ContactDetail where order_id = A.id), " +
                    "(select source_purchase_alreadyPay from Manufacturer_ContactDetail where order_id = A.id) " +
                    "FROM Orders A " +
                    "INNER JOIN Orders_ProjectInfo B ON A.id = B.Order_id " +
                    "INNER JOIN Orders_Price C ON A.id = C.Order_id " +
                    "INNER JOIN Manufacturer D ON A.ObjectID = D.ObjectID " +
                    "LEFT JOIN Orders_ProductGroup_Price E on A.id = E.order_id " +
                    "WHERE A.alreadyOrderNumber IS NOT NULL AND A.ObjectID = ? and A.status = '0' AND A.alreadyOrderDate BETWEEN ? AND ?";
            Query = Query + " union all ";
            Query = Query + "SELECT A.AlreadyOrderDate AS '日期','' AS '已開發票',case when(C.Tax = '0') then '' else CAST(C.TotalPriceIncludeTax as nvarchar(10)) end AS '未開發票','' AS '可開發票餘額',B.ProjectName AS '專案名稱',null AS '發票號碼','' AS '聯單','' AS '學校已付金額',C.TotalPriceIncludeTax AS '未給付金額','' AS '繳、退金額',A.CashierRemark as '備註','' as '所屬學校',null as 'order_id',A.id as 'subBill_id'," +
                    "(select source_purchase_alreadyInvoice from Manufacturer_ContactDetail where subBill_id = A.id), " +
                    "(select source_purchase_alreadyPay from Manufacturer_ContactDetail where subBill_id = A.id) " +
                    "FROM SubBill A " +
                    "INNER JOIN SubBill_ProjectInfo B ON A.id = B.SubBill_id " +
                    "INNER JOIN SubBill_Price C ON A.id = C.SubBill_id " +
                    "INNER JOIN Manufacturer D ON A.ObjectID = D.ObjectID " +
                    "WHERE A.alreadyOrderNumber IS NOT NULL AND A.ObjectID = ? and A.status = '0' AND A.alreadyOrderDate BETWEEN ? AND ?) order by '日期'";

//            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,ObjectID);
            PreparedStatement.setString(2,startDate);
            PreparedStatement.setString(3,endDate);
            PreparedStatement.setString(4,ObjectID);
            PreparedStatement.setString(5,startDate);
            PreparedStatement.setString(6,endDate);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean = new IAECrawler_ManufacturerContactDetail_Bean();
                IAECrawler_ManufacturerContactDetail_Bean.setManufacturerContactDetailSource(PayableReceivable_Enum.ManufacturerContactDetailSource.廠商進貨);
                IAECrawler_ManufacturerContactDetail_Bean.setSelectCheckBox(new CheckBox());
                IAECrawler_ManufacturerContactDetail_Bean.setOrderDate(Rs.getString(1));
                IAECrawler_ManufacturerContactDetail_Bean.setAlreadyInvoiceAmount(Rs.getString(2).equals("") ? null : Rs.getInt(2));
                IAECrawler_ManufacturerContactDetail_Bean.setNoneInvoiceAmount(Rs.getString(3).equals("") ? null: Rs.getInt(3));

                IAECrawler_ManufacturerContactDetail_Bean.setProjectName(Rs.getString(5));
                IAECrawler_ManufacturerContactDetail_Bean.setInvoiceNumber(Rs.getString(6));
                IAECrawler_ManufacturerContactDetail_Bean.setInvoiceType(Rs.getString(7).equals("") ? null : Invoice_Enum.InvoiceType.values()[Rs.getInt(7)].name());
                IAECrawler_ManufacturerContactDetail_Bean.setSchoolAlreadyAmount(Rs.getString(8).equals("") ? null : Rs.getInt(8));
                IAECrawler_ManufacturerContactDetail_Bean.setSchoolNoneAmount(Rs.getString(9).equals("") ? null:Rs.getInt(9));
                IAECrawler_ManufacturerContactDetail_Bean.setCashierRemark(Rs.getString(11) == null ? "" : Rs.getString(11));
                IAECrawler_ManufacturerContactDetail_Bean.setIAECrawlerAccount_BelongName(Rs.getString(12));
                IAECrawler_ManufacturerContactDetail_Bean.setOrder_id(Rs.getObject(13) == null ? null : Rs.getInt(13));
                IAECrawler_ManufacturerContactDetail_Bean.setSubBill_id(Rs.getObject(14) == null ? null : Rs.getInt(14));

                PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail checkStatus_ManufacturerContactDetail = PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部;
                if(Rs.getBoolean(15) && !Rs.getBoolean(16))
                    checkStatus_ManufacturerContactDetail = PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已開發票;
                else if(!Rs.getBoolean(15) && Rs.getBoolean(16))
                    checkStatus_ManufacturerContactDetail = PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.已給付貨款金額;
                else if(!Rs.getBoolean(15) && !Rs.getBoolean(16))
                    checkStatus_ManufacturerContactDetail = null;
                IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(checkStatus_ManufacturerContactDetail);

                if(searchCheckoutStatus != null){
                    if(searchCheckoutStatus == Order_Enum.CheckoutStatus.未結帳 &&
                            IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() != PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
                    else if(searchCheckoutStatus == Order_Enum.CheckoutStatus.已結帳 &&
                            IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
                }else
                    IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return IAECrawlerPaymentList;
    }
    private ObservableList<IAECrawler_ManufacturerContactDetail_Bean> getEstablishInvoice_ContactDetail(String ObjectID, String startDate, String endDate, CheckoutStatus searchCheckoutStatus){
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> IAECrawlerPaymentList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "(SELECT A.InvoiceDate AS '日期',A.InvoicePrice AS '已開發票','' AS '未開發票','' AS '可開發票餘額',D.ProjectName AS '專案名稱',A.InvoiceNumber AS '發票號碼',A.InvoiceType AS '聯單','' AS '學校已付金額','' AS '未給付金額','' AS '繳、退金額',B.CashierRemark as '備註','' as '所屬學校',B.id as 'order_id',null as 'subBill_id',(select source_shipment from Manufacturer_ContactDetail where order_id = B.id) " +
                    "FROM Orders_InvoiceInfo A " +
                    "INNER JOIN Orders B ON A.Order_id = B.id and B.status = '0' " +
                    "INNER JOIN Orders_Price C ON B.id = C.Order_id " +
                    "INNER JOIN Orders_ProjectInfo D ON B.id = D.Order_id " +
                    "INNER JOIN Manufacturer E on E.id = (select Manufacturer_id from IAECrawlerAccount_ExportQuotation_Manufacturer where id = A.invoice_manufacturerNickName_id) " +
                    "INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer H on A.invoice_manufacturerNickName_id = H.id " +
                    "LEFT JOIN Orders_ProductGroup_Price I on B.id = I.order_id " +
                    "WHERE A.Invalid = ? and A.InvoiceDate BETWEEN ? and ? " +
                    "and H.Manufacturer_id = (select id from Manufacturer where ObjectID = ?)";
            Query = Query + " union all ";
            Query = Query + "SELECT A.InvoiceDate AS '日期',A.InvoicePrice AS '已開發票','' AS '未開發票','' AS '可開發票餘額',D.ProjectName AS '專案名稱',A.InvoiceNumber AS '發票號碼',A.InvoiceType AS '聯單','' AS '學校已付金額','' AS '未給付金額','' AS '繳、退金額',B.CashierRemark as '備註','' as '所屬學校',null as 'order_id',B.id as 'subBill_id',(select source_shipment from Manufacturer_ContactDetail where subBill_id = B.id) " +
                    "FROM Orders_InvoiceInfo A " +
                    "INNER JOIN SubBill B ON A.SubBill_id = B.id and B.status = '0' " +
                    "INNER JOIN SubBill_Price C ON B.id = C.SubBill_id " +
                    "INNER JOIN SubBill_ProjectInfo D ON B.id = D.SubBill_id " +
                    "INNER JOIN Manufacturer E on E.id = (select Manufacturer_id from IAECrawlerAccount_ExportQuotation_Manufacturer where id = A.invoice_manufacturerNickName_id) " +
                    "INNER JOIN IAECrawlerAccount_ExportQuotation_Manufacturer H on A.invoice_manufacturerNickName_id = H.id " +
                    "WHERE A.Invalid = ? and A.InvoiceDate BETWEEN ? and ? " +
                    "and H.Manufacturer_id = (select id from Manufacturer where ObjectID = ?)";
            Query = Query + ") order by '日期'";

//            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,Invoice_Enum.InvoiceInvalid.未作廢.ordinal());
            PreparedStatement.setString(2,startDate);
            PreparedStatement.setString(3,endDate);
            PreparedStatement.setString(4,ObjectID);
            PreparedStatement.setInt(5,Invoice_Enum.InvoiceInvalid.未作廢.ordinal());
            PreparedStatement.setString(6,startDate);
            PreparedStatement.setString(7,endDate);
            PreparedStatement.setString(8,ObjectID);
            Rs = PreparedStatement.executeQuery();
            int serialNumber = 0;
            while (Rs.next()) {
                serialNumber = serialNumber + 1;
                IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean = new IAECrawler_ManufacturerContactDetail_Bean();
                IAECrawler_ManufacturerContactDetail_Bean.setManufacturerContactDetailSource(PayableReceivable_Enum.ManufacturerContactDetailSource.出貨已開發票);
                IAECrawler_ManufacturerContactDetail_Bean.setSelectCheckBox(new CheckBox());
                IAECrawler_ManufacturerContactDetail_Bean.setOrderDate(Rs.getString(1));
                IAECrawler_ManufacturerContactDetail_Bean.setAlreadyInvoiceAmount(Rs.getInt(2));
                IAECrawler_ManufacturerContactDetail_Bean.setNoneInvoiceAmount(Rs.getString(3).equals("") ? null: Rs.getInt(3));

                IAECrawler_ManufacturerContactDetail_Bean.setProjectName(Rs.getString(5));
                IAECrawler_ManufacturerContactDetail_Bean.setInvoiceNumber(Rs.getString(6));
                IAECrawler_ManufacturerContactDetail_Bean.setInvoiceType(Rs.getString(7).equals("") ? null : Invoice_Enum.InvoiceType.values()[Rs.getInt(7)].name());
                IAECrawler_ManufacturerContactDetail_Bean.setSchoolAlreadyAmount(Rs.getString(8).equals("") ? null : Rs.getInt(8));
                IAECrawler_ManufacturerContactDetail_Bean.setSchoolNoneAmount(Rs.getString(9).equals("") ? null:Rs.getInt(9));
                IAECrawler_ManufacturerContactDetail_Bean.setCashierRemark(Rs.getString(11) == null ? "" : Rs.getString(11));
                IAECrawler_ManufacturerContactDetail_Bean.setIAECrawlerAccount_BelongName(Rs.getString(12));
                IAECrawler_ManufacturerContactDetail_Bean.setOrder_id(Rs.getObject(13) == null ? null : Rs.getInt(13));
                IAECrawler_ManufacturerContactDetail_Bean.setSubBill_id(Rs.getObject(14) == null ? null : Rs.getInt(14));
                IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(Rs.getBoolean(15) ? PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部 : null);

                if(searchCheckoutStatus != null){
                    if(searchCheckoutStatus == Order_Enum.CheckoutStatus.未結帳 &&
                            IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() != PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
                    else if(searchCheckoutStatus == Order_Enum.CheckoutStatus.已結帳 &&
                            IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
                }else
                    IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return IAECrawlerPaymentList;
    }
    private ObservableList<IAECrawler_ManufacturerContactDetail_Bean> getAlreadyPayment_ContactDetail(String ObjectID, String startDate, String endDate, CheckoutStatus searchCheckoutStatus){
        ObservableList<IAECrawler_ManufacturerContactDetail_Bean> IAECrawlerPaymentList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "(SELECT A.id,A.PayDate AS '日期','' AS '已開發票','' AS '未開發票','' AS '可開發票餘額',F.ProjectName AS '專案名稱',D.InvoiceNumber AS '發票號碼',D.InvoiceType AS '聯單',A.PayAmount AS '學校已付金額','' AS '未給付金額','' AS '繳、退金額',E.CashierRemark AS '備註',I.BelongName AS '所屬學校',A.RemittanceFee as '匯費',E.id as 'order_id',null as 'subBill_id',(select source_schoolPayment from Manufacturer_ContactDetail where order_id = E.id) " +
                    "FROM IAECrawlerPayment A " +
                    "INNER JOIN IAECrawlerAccount_Payment B on A.id = B.Payment_id " +
                    "INNER JOIN IAECrawlerInvoice_Order C ON A.id = C.invoice_id " +
                    "LEFT JOIN Orders_InvoiceInfo D ON C.Order_id = D.Order_id and D.Invalid = ? " +
                    "INNER JOIN Orders E ON C.order_id = E.id and E.status = '0' " +
                    "INNER JOIN Orders_ProjectInfo F ON C.Order_id = F.Order_id " +
                    "LEFT JOIN IAECrawlerAccount_ExportQuotation_Manufacturer G on D.invoice_manufacturerNickName_id = G.id and G.Manufacturer_id = (SELECT id FROM Manufacturer WHERE ObjectID = A.ObjectID) " +
                    "INNER JOIN IAECrawlerAccount H ON B.Account_id = H.id " +
                    "INNER JOIN IAECrawlerBelong I ON B.Belong_id = I.id " +
                    "WHERE A.PayDate BETWEEN ? AND ? AND A.ObjectID = ?";
            Query = Query + " union all ";
            Query = Query + "SELECT A.id,A.PayDate AS '日期','' AS '已開發票','' AS '未開發票','' AS '可開發票餘額',F.ProjectName AS '專案名稱',D.InvoiceNumber AS '發票號碼',D.InvoiceType AS '聯單',A.PayAmount AS '學校已付金額','' AS '未給付金額','' AS '繳、退金額',E.CashierRemark AS '備註',I.BelongName AS '所屬學校',A.RemittanceFee as '匯費',null as 'order_id',E.id as 'subBill_id',(select source_schoolPayment from Manufacturer_ContactDetail where subBill_id = E.id) " +
                    "FROM IAECrawlerPayment A " +
                    "INNER JOIN IAECrawlerAccount_Payment B on A.id = B.Payment_id " +
                    "INNER JOIN IAECrawlerInvoice_Order C ON A.id = C.invoice_id " +
                    "LEFT JOIN Orders_InvoiceInfo D ON C.SubBill_id = D.SubBill_id and D.Invalid = ? " +
                    "INNER JOIN SubBill E ON C.SubBill_id = E.id and E.status = '0' " +
                    "INNER JOIN SubBill_ProjectInfo F ON C.SubBill_id = F.SubBill_id " +
                    "LEFT JOIN IAECrawlerAccount_ExportQuotation_Manufacturer G on D.invoice_manufacturerNickName_id = G.id and G.Manufacturer_id = (SELECT id FROM Manufacturer WHERE ObjectID = A.ObjectID) " +
                    "INNER JOIN IAECrawlerAccount H ON B.Account_id = H.id " +
                    "INNER JOIN IAECrawlerBelong I ON B.Belong_id = I.id " +
                    "WHERE A.PayDate BETWEEN ? AND ? AND A.ObjectID = ?) order by '日期'";
//            ERPApplication.Logger.info(Query);
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setInt(1,Invoice_Enum.InvoiceInvalid.未作廢.ordinal());
            PreparedStatement.setString(2,startDate);
            PreparedStatement.setString(3,endDate);
            PreparedStatement.setString(4,ObjectID);
            PreparedStatement.setInt(5,Invoice_Enum.InvoiceInvalid.未作廢.ordinal());
            PreparedStatement.setString(6,startDate);
            PreparedStatement.setString(7,endDate);
            PreparedStatement.setString(8,ObjectID);
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                IAECrawler_ManufacturerContactDetail_Bean IAECrawler_ManufacturerContactDetail_Bean = new IAECrawler_ManufacturerContactDetail_Bean();
                IAECrawler_ManufacturerContactDetail_Bean.setManufacturerContactDetailSource(PayableReceivable_Enum.ManufacturerContactDetailSource.學校出納);
                IAECrawler_ManufacturerContactDetail_Bean.setSelectCheckBox(new CheckBox());
                IAECrawler_ManufacturerContactDetail_Bean.setPayment_id(Rs.getInt(1));
                IAECrawler_ManufacturerContactDetail_Bean.setOrderDate(Rs.getString(2));
                IAECrawler_ManufacturerContactDetail_Bean.setAlreadyInvoiceAmount(Rs.getString(3).equals("") ? null:Rs.getInt(3));
                IAECrawler_ManufacturerContactDetail_Bean.setNoneInvoiceAmount(Rs.getString(4).equals("") ? null: Rs.getInt(4));

                IAECrawler_ManufacturerContactDetail_Bean.setProjectName(Rs.getString(6));
                IAECrawler_ManufacturerContactDetail_Bean.setInvoiceNumber(Rs.getString(7));
                IAECrawler_ManufacturerContactDetail_Bean.setInvoiceType(Rs.getString(8) == null || Rs.getString(8).equals("") ? null : Invoice_Enum.InvoiceType.values()[Rs.getInt(8)].name());
                IAECrawler_ManufacturerContactDetail_Bean.setSchoolAlreadyAmount(Rs.getString(9).equals("") ? null : Rs.getInt(9));
                IAECrawler_ManufacturerContactDetail_Bean.setSchoolNoneAmount(Rs.getString(10).equals("") ? null:Rs.getInt(10));
                IAECrawler_ManufacturerContactDetail_Bean.setCashierRemark(Rs.getString(12) == null ? "" : Rs.getString(12));
                IAECrawler_ManufacturerContactDetail_Bean.setIAECrawlerAccount_BelongName(Rs.getString(13));
                IAECrawler_ManufacturerContactDetail_Bean.setRemittanceFee(Rs.getString(14).equals("") ? null : Rs.getInt(14));
                if(IAECrawler_ManufacturerContactDetail_Bean.getRemittanceFee() != null)
                    IAECrawler_ManufacturerContactDetail_Bean.setSchoolAlreadyAmount(IAECrawler_ManufacturerContactDetail_Bean.getSchoolAlreadyAmount()-IAECrawler_ManufacturerContactDetail_Bean.getRemittanceFee());
                IAECrawler_ManufacturerContactDetail_Bean.setOrder_id(Rs.getObject(15) == null ? null : Rs.getInt(15));
                IAECrawler_ManufacturerContactDetail_Bean.setSubBill_id(Rs.getObject(16) == null ? null : Rs.getInt(16));
                IAECrawler_ManufacturerContactDetail_Bean.setCheckoutStatus_ManufacturerContactDetail(Rs.getBoolean(17) ? PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部 : null);
                if(searchCheckoutStatus != null){
                    if(searchCheckoutStatus == Order_Enum.CheckoutStatus.未結帳 &&
                            IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() != PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
                    else if(searchCheckoutStatus == Order_Enum.CheckoutStatus.已結帳 &&
                            IAECrawler_ManufacturerContactDetail_Bean.getCheckoutStatus_ManufacturerContactDetail() == PayableReceivable_Enum.CheckoutStatus_ManufacturerContactDetail.全部)
                        IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
                }else
                    IAECrawlerPaymentList.add(IAECrawler_ManufacturerContactDetail_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return IAECrawlerPaymentList;
    }
    public String generateCheckDueDate(Integer CheckDueDay){
        if(CheckDueDay != null && CheckDueDay != 0) {
            if(CheckDueDay <= 30) {
                if((CheckDueDay < Integer.parseInt(toolKit.getToday("dd")))){
                    if(CheckDueDay == 30)   return toolKit.getSpecificMonthLastDay(1);
                    else return  toolKit.getNextMonthSpecificDay(CheckDueDay);
                }else{
                    if(CheckDueDay == 30)   return toolKit.getSpecificMonthLastDay(0);
                    else return  toolKit.getThisMonthSpecifyDay(CheckDueDay);
                }
            }else {
                int lessDay = CheckDueDay%30;
                if(lessDay == 0) {
                    if(Integer.parseInt(toolKit.getToday("dd")) <= 30)  return toolKit.getSpecificMonthLastDay(CheckDueDay / 30 - 1);
                    else    return toolKit.getSpecificMonthLastDay(CheckDueDay / 30);
                }else
                    return toolKit.getSpecificMonthSpecificDay(CheckDueDay/30, lessDay);
            }
        }
        return null;
    }
}
