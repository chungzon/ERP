package ERP.Model.SystemSetting;

import ERP.Bean.DataBaseServer_Bean;
import ERP.Bean.ManagePayableReceivable.IAECrawlerAccount_Bean;
import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.ToolKit.ShowSnapshotOrderPicture.IpCamInfo_Bean;
import ERP.Bean.SystemSetting.*;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum.SystemDefaultName;
import ERP.ToolKit.ConfigPath;
import ERP.ERPApplication;
import ERP.Sql.SqlAdapter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ProductLevel;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Enum.SystemSetting.SystemSetting_Enum.ObjectStatus;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/** [ERP.Model] System setting */
public class SystemSetting_Model {

    public ObservableList<Version_Bean> getAllVersion(){
        ObservableList<Version_Bean> allVersionList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from Version");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(allVersionList.size() == 0){
                    Version_Bean Version_Bean = new Version_Bean();
                    Version_Bean.setId(null);
                    Version_Bean.setVersion("所有");
                    Version_Bean.setVersionContent("");
                    allVersionList.add(Version_Bean);
                }
                Version_Bean Version_Bean = new Version_Bean();
                Version_Bean.setId(Rs.getInt("id"));
                Version_Bean.setVersion(Rs.getString("version"));
                Version_Bean.setVersionContent(Rs.getString("versionContent"));
                allVersionList.add(Version_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return allVersionList;
    }
    public boolean insertVersion(Version_Bean Version_Bean){
        boolean isInsert = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            Query = Query + "INSERT INTO Version (version,versionContent) VALUES (?,?) ";
            Query = Query + "SELECT IDENT_CURRENT('Version')" +
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
            PreparedStatement.setString(1, Version_Bean.getVersion());
            PreparedStatement.setString(2, Version_Bean.getVersionContent());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                Version_Bean.setId(Rs.getInt(1));
            isInsert = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
//            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isInsert;
    }
    public boolean modifyVersion(Version_Bean Version_Bean, String versionContent){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "update Version set versionContent = ? where id = ? and version = ? " +
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
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, versionContent);
            preparedStatement.setInt(2, Version_Bean.getId());
            preparedStatement.setString(3, Version_Bean.getVersion());
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
//            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    public boolean deleteVersion(Version_Bean Version_Bean){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "delete from Version where id = ? and version = ? " +
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
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setInt(1, Version_Bean.getId());
            preparedStatement.setString(2, Version_Bean.getVersion());
            preparedStatement.executeUpdate();
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
//            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally {
            SqlAdapter.closeConnectParameter(preparedStatement,null);
        }
        return status;
    }
    public LinkedHashMap<String, Version_Bean> getTestDBVersion(DataBaseServer_Bean dataBaseServer_bean) {
        LinkedHashMap<String, Version_Bean> testDBMap = null;
        Connection connect = SqlAdapter.getTestDBConnect();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try{
            Class.forName(dataBaseServer_bean.getDriver());
            preparedStatement = connect.prepareStatement("select * from Version");
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                if(testDBMap == null){
                    testDBMap = new LinkedHashMap<>();
                }
                Version_Bean version_bean = new Version_Bean();
                String version = rs.getString("version");
                String versionContent = rs.getString("versionContent");
                version_bean.setId(rs.getInt("id"));
                version_bean.setVersion(version);
                version_bean.setVersionContent(versionContent);
                testDBMap.put(version, version_bean);
            }
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(ex));
        }finally {
            if(connect != null){
                SqlAdapter.closeConnection(connect);
            }
            SqlAdapter.closeConnectParameter(preparedStatement, rs);
        }
        return testDBMap;
    }
    /** Load all data of system setting */
    public SystemSettingConfig_Bean loadAllSystemSettingData(){
        SystemSettingConfig_Bean SystemSettingConfig_Bean = new SystemSettingConfig_Bean();
        SystemSettingConfig_Bean.setDefaultPrinter(getConfigValue(SystemSettingConfig.預設列表機設定.getColumnName()));
        SystemSettingConfig_Bean.setConsignmentPrinter(getConfigValue(SystemSettingConfig.託運單列表機設定.getColumnName()));
        SystemSettingConfig_Bean.setLabelPrinter(getConfigValue(SystemSettingConfig.標籤列表機設定.getColumnName()));
        SystemSettingConfig_Bean.setCustomerIDCharacter(getConfigValue(SystemSettingConfig.客戶編號起始字母.getColumnName()));
        SystemSettingConfig_Bean.setCustomerIDLength(getConfigValue(SystemSettingConfig.客戶位數.getColumnName()));
        SystemSettingConfig_Bean.setCustomerIDAreaLength(getConfigValue(SystemSettingConfig.地區客戶位數.getColumnName()));
        SystemSettingConfig_Bean.setManufacturerIDLength(getConfigValue(SystemSettingConfig.廠商位數.getColumnName()));
        SystemSettingConfig_Bean.setOrderNumberLength(getConfigValue(SystemSettingConfig.貨單號長度.getColumnName()));
        SystemSettingConfig_Bean.setProjectCodeDefaultUrl(getConfigValue(SystemSettingConfig.專案編號預設網址.getColumnName()));
        SystemSettingConfig_Bean.setInitialCheckNumber(getConfigValue(SystemSettingConfig.票據起始號.getColumnName()));
        SystemSettingConfig_Bean.setNowCheckNumber(getConfigValue(SystemSettingConfig.票據流水號.getColumnName()));
        SystemSettingConfig_Bean.setExportCompanyFormat(getConfigValue(SystemSettingConfig.公司匯出格式.getColumnName()) == null ? null : JSONObject.parseObject(getConfigValue(SystemSettingConfig.公司匯出格式.getColumnName())));
        SystemSettingConfig_Bean.setTestDatabase(getConfigValue(SystemSettingConfig.測試資料庫.getColumnName()) == null ? null : JSONObject.parseObject(getConfigValue(SystemSettingConfig.測試資料庫.getColumnName())));
        return SystemSettingConfig_Bean;
    }
    /** Load specific data of system setting */
    public String getSpecificSystemSettingData(SystemSettingConfig SystemSettingConfig){
        return getConfigValue(SystemSettingConfig.getColumnName());
    }
    /** Get printer service in windows */
    public ArrayList<String> getPrinterService(){
        PrintService[] PrintService = PrintServiceLookup.lookupPrintServices(null,null);
        ArrayList<String> PrinterList = null;
        for(PrintService PrintServer : PrintService){
            if(PrinterList == null){
                PrinterList = new ArrayList<>();
                PrinterList.add("");
            }
            PrinterList.add(PrintServer.getName());
        }
        return PrinterList;
    }
    /** Get product bookcase from database */
    public ObservableList<ProductBookCase_Bean> getProductBookCase(){
        ObservableList<ProductBookCase_Bean> ProductBookCaseList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from BookCase");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                ProductBookCase_Bean ProductBookCase_Bean = new ProductBookCase_Bean();
                ProductBookCase_Bean.setProductArea(Rs.getString("ProductArea"));
                ProductBookCase_Bean.setProductFloor(Rs.getString("ProductFloor"));
                ProductBookCase_Bean.setProductLevel(SystemSetting_Enum.ProductLevel.values()[Rs.getInt("Level")]);
                sortProductBookCase(ProductBookCaseList, ProductBookCase_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ProductBookCaseList;
    }
    /** Add the area or floor into bookcase in correct index
     * @param ProductBookCaseList the list of bookcase
     * @param ProductBookCase_Bean the floor or area which be added
     * */
    public void sortProductBookCase(ObservableList<ProductBookCase_Bean> ProductBookCaseList, ProductBookCase_Bean ProductBookCase_Bean){
        for(int Index = 0 ; Index < ProductBookCaseList.size() ; Index++){
            if(ProductBookCaseList.get(Index).getProductArea().equals(ProductBookCase_Bean.getProductArea()) && ProductBookCaseList.get(Index).getProductLevel() == ProductLevel.商品儲存區 && ProductBookCase_Bean.getProductLevel() == ProductLevel.商品儲存層){
                ProductBookCaseList.add((Index+1), ProductBookCase_Bean);
                return;
            }
        }
        ProductBookCaseList.add(ProductBookCase_Bean);
    }

    /** Insert the floor or area of bookcase into database
     * @param ProductBookCase_Bean the floor or area which be inserted
     * */
    public boolean insertAreaOrFloorOfBookCase(ProductBookCase_Bean ProductBookCase_Bean){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存區)
                Query = Query + "insert into BookCase (ProductArea,ProductFloor,Level) values (?,?,?)";
            else if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存層)
                Query = Query + "insert into BookCase (ProductArea,ProductFloor,Level) values (?,?,?)";
            else
                return false;
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
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, ProductBookCase_Bean.getProductArea());
            if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存區) {
                preparedStatement.setString(2, "");
                preparedStatement.setInt(3, ProductBookCase_Bean.getProductLevel().ordinal());
            }else if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存層) {
                preparedStatement.setString(2, ProductBookCase_Bean.getProductFloor());
                preparedStatement.setInt(3, ProductBookCase_Bean.getProductLevel().ordinal());
            }
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
    /** Update the floor or area of bookcase in database
     * @param ProductBookCase_Bean the floor or area which be updated
     * @param NewProductArea the name of area
     * @param NewProductFloor the name of floor
     * */
    public boolean modifyProductSave(ProductBookCase_Bean ProductBookCase_Bean, String NewProductArea, String NewProductFloor){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存區)
                Query = Query + "update BookCase set ProductArea = ? where ProductArea = ? ";
            else if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存層)
                Query = Query + "update BookCase set ProductFloor = ? where ProductArea = ? and ProductFloor = ? and Level = ? ";
            else
                return false;
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
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存區) {
                preparedStatement.setString(1, NewProductArea);
                preparedStatement.setString(2, ProductBookCase_Bean.getProductArea());
            }else if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存層) {
                preparedStatement.setString(1, NewProductFloor);
                preparedStatement.setString(2, ProductBookCase_Bean.getProductArea());
                preparedStatement.setString(3, ProductBookCase_Bean.getProductFloor());
                preparedStatement.setInt(4, ProductBookCase_Bean.getProductLevel().ordinal());
            }
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
    /** Delete the floor or area of bookcase in database
     * @param ProductBookCase_Bean the floor or area which be deleted
     * */
    public boolean deleteProductSave(ProductBookCase_Bean ProductBookCase_Bean){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存區) {
                Query = Query + "delete from BookCase where ProductArea = ? ";
                Query = Query + "delete from ProductBookCase where ProductArea = ? ";
            }else if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存層) {
                Query = Query + "delete from BookCase where ProductArea = ? and ProductFloor = ? and Level = ? ";
                Query = Query + "update ProductBookCase set ProductFloor = '' where ProductArea = ? and ProductFloor = ? ";
            }else
                return false;
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
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存區) {
                preparedStatement.setString(1, ProductBookCase_Bean.getProductArea());
                preparedStatement.setString(2, ProductBookCase_Bean.getProductArea());
            }else if(ProductBookCase_Bean.getProductLevel() == SystemSetting_Enum.ProductLevel.商品儲存層) {
                preparedStatement.setString(1, ProductBookCase_Bean.getProductArea());
                preparedStatement.setString(2, ProductBookCase_Bean.getProductFloor());
                preparedStatement.setInt(3, ProductBookCase_Bean.getProductLevel().ordinal());
                preparedStatement.setString(4, ProductBookCase_Bean.getProductArea());
                preparedStatement.setString(5, ProductBookCase_Bean.getProductFloor());
            }
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
    private String getConfigValue(String configName){
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        String configValue = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement("select ConfigValue from SystemSetting where ConfigName= ?");
            preparedStatement.setString(1,configName);
            rs = preparedStatement.executeQuery();
            if (rs.next())
                configValue = rs.getString("ConfigValue");
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return configValue;
    }
    /** Insert config of system setting into database
     * @param SystemSettingConfig the column of system setting
     * @param configValue the value of config
     * */
    public boolean insertSystemSettingConfig(SystemSettingConfig SystemSettingConfig, String configValue){
        boolean status = false;
        PreparedStatement preparedStatement  = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "insert into SystemSetting (ConfigName,ConfigValue) values (?,?) " +
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
            preparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            preparedStatement.setString(1, SystemSettingConfig.getColumnName());
            preparedStatement.setString(2, configValue);
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
    /** Update config of system setting in database
     * @param SystemSettingConfig the column of system setting
     * @param ConfigValue the value of config
     * */
    public boolean updateSystemSettingConfigValue(SystemSettingConfig SystemSettingConfig, String ConfigValue){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "if EXISTS (select 1 from SystemSetting where ConfigName = ?) begin " +
                    "update SystemSetting set ConfigValue = ? where ConfigName = ? " +
                    "END ELSE BEGIN " +
                    "insert into SystemSetting VALUES (?,?) END " +
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
            PreparedStatement.setString(1, SystemSettingConfig.getColumnName());
            PreparedStatement.setString(2, ConfigValue);
            PreparedStatement.setString(3, SystemSettingConfig.getColumnName());
            PreparedStatement.setObject(4, SystemSettingConfig.getColumnName());
            PreparedStatement.setObject(5, ConfigValue);
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
    public Integer getSystemDefaultConfig(SystemDefaultName SystemDefaultName){
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Integer defaultValue = null;
        try {
            preparedStatement = SqlAdapter.getConnect().prepareStatement("select default_value from SystemDefaultConfig where config_name= ?");
            preparedStatement.setString(1,SystemDefaultName.name());
            rs = preparedStatement.executeQuery();
            if (rs.next())
                defaultValue = rs.getObject(1) == null ? null : rs.getInt(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(preparedStatement,rs);
        }
        return defaultValue;
    }
    public boolean updateSystemDefaultConfig(String configName, int defaultValue){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "if EXISTS (select 1 from SystemDefaultConfig where config_name = ?) begin " +
                    "update SystemDefaultConfig set default_value = ? where config_name = ? " +
                    "END ELSE BEGIN " +
                    "insert into SystemDefaultConfig (config_name,default_value)VALUES (?,?) END " +
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
            PreparedStatement.setString(1, configName);
            PreparedStatement.setInt(2, defaultValue);
            PreparedStatement.setString(3, configName);
            PreparedStatement.setString(4, configName);
            PreparedStatement.setInt(5, defaultValue);
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
    public boolean saveFile_OutputPath(String outputFile){
        boolean status = false;
        try{
            outputFile = outputFile.replace("\\","\\\\");
            FileWriter myWriter = new FileWriter(ConfigPath.File_OutputPath);
            myWriter.write("File_OutputPath=" + outputFile);
            myWriter.close();
            File file =new File(outputFile);
            if  (!file.exists()  && !file.isDirectory())
                file.mkdir();

            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
        return status;
    }
    /** Whether customer or manufacturer is exist
     * @param ObjectStatus the status of object. whether the first character of id contains letter
     * */
    public boolean isExistObject(ObjectStatus ObjectStatus){
        String Query = "";
        if(ObjectStatus == SystemSetting_Enum.ObjectStatus.純數字客戶 || ObjectStatus == SystemSetting_Enum.ObjectStatus.地區客戶)
            Query = "select MAX(ObjectID) from Customer where isnumeric(ObjectID) = ?";
        else if(ObjectStatus == SystemSetting_Enum.ObjectStatus.純數字廠商)
            Query = "select MAX(ObjectID) from Manufacturer";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        boolean exist = false;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            if(ObjectStatus != SystemSetting_Enum.ObjectStatus.純數字廠商)
                PreparedStatement.setInt(1,ObjectStatus.ordinal());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) {
                if(Rs.getString(1) != null)  exist = true;
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return exist;
    }
    public boolean isExistOrder(){
        String Query = "select 1 from Orders union all select 1 from ReturnOrder";
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        boolean exist = false;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                exist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return exist;
    }
    public boolean insertIAECrawlerBelong(IAECrawlerBelong_Bean IAECrawlerBelong_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            Query = Query + "insert into IAECrawlerBelong (BelongName,BelongURL) values (?,?) " +
                    "SELECT IDENT_CURRENT('IAECrawlerBelong') AS id " +
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
            PreparedStatement.setString(1, IAECrawlerBelong_Bean.getBelongName());
            PreparedStatement.setString(2, IAECrawlerBelong_Bean.getBelongURL());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                IAECrawlerBelong_Bean.setId(Rs.getInt(1));

            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public boolean modifyIAECrawlerBelong(IAECrawlerBelong_Bean IAECrawlerBelong_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "update IAECrawlerBelong set BelongURL=? where id = ? and BelongName = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,IAECrawlerBelong_Bean.getBelongURL());
            PreparedStatement.setInt(2,IAECrawlerBelong_Bean.getId());
            PreparedStatement.setString(3,IAECrawlerBelong_Bean.getBelongName());
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
    public boolean isIAECrawlerAccountBelongHaveThisBelong(int id){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select 1 from IAECrawlerAccount_Belong where belong_id = ? and account_id is not null");
            PreparedStatement.setInt(1,id);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next()) status = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            status = true;
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    public boolean deleteIAECrawlerBelong(IAECrawlerBelong_Bean IAECrawlerBelong_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "delete IAECrawlerBelong where BelongURL=? and id = ? and BelongName = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,IAECrawlerBelong_Bean.getBelongURL());
            PreparedStatement.setInt(2,IAECrawlerBelong_Bean.getId());
            PreparedStatement.setString(3,IAECrawlerBelong_Bean.getBelongName());
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
    public ObservableList<IAECrawlerBelong_Bean> getIAECrawlerBelong(){
        ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from IAECrawlerBelong");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                IAECrawlerBelong_Bean IAECrawlerBelong_Bean = new IAECrawlerBelong_Bean();
                IAECrawlerBelong_Bean.setId(Rs.getInt(1));
                IAECrawlerBelong_Bean.setBelongName(Rs.getString(2));
                IAECrawlerBelong_Bean.setBelongURL(Rs.getString(3));
                IAECrawlerBelongList.add(IAECrawlerBelong_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return IAECrawlerBelongList;
    }

    public boolean isIAECrawlerAccount_ExportQuotation_ManufacturerNickNameExist(String Account, String ManufacturerNickName){
        boolean isNickName = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select 1 from IAECrawlerAccount_ExportQuotation_Manufacturer where (ManufacturerNickName = ? or Account = ?) and Account != ?");
            PreparedStatement.setString(1,ManufacturerNickName);
            PreparedStatement.setString(2,Account);
            PreparedStatement.setString(3,Account);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isNickName = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            isNickName = true;
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isNickName;
    }
    public boolean isExistIAECrawlerAccount(Integer account_id, String Account){
        boolean isAccountExist = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "select 1 from IAECrawlerAccount where Account = ?";
            if(account_id != null)
                Query = Query + " and id != ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,Account);
            if(account_id != null)
                PreparedStatement.setInt(2,account_id);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isAccountExist = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            isAccountExist = true;
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isAccountExist;
    }

    public boolean insertIAECrawlerAccount(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            Query = Query + "insert into IAECrawlerAccount (ObjectID,Account,Password) values (?,?,?) ";
            Query = Query + getIAECrawlerBelongQuery(null,IAECrawlerAccount_Bean.getIAECrawlerBelongList());
            Query = Query + "insert into IAECrawlerAccount_ExportQuotation_Manufacturer (Account,Manufacturer_id,ManufacturerNickName,ManufacturerAllName,DefaultSelect,ExportQuotation,TemplateFormat) values (?,?,?,?,?,?,?) ";
            Query = Query + "SELECT IDENT_CURRENT('IAECrawlerAccount'),IDENT_CURRENT('IAECrawlerAccount_ExportQuotation_Manufacturer') " +
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
            PreparedStatement.setString(1, IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID());
            PreparedStatement.setString(2, IAECrawlerAccount_Bean.getAccount());
            PreparedStatement.setString(3, IAECrawlerAccount_Bean.getPassword());
            PreparedStatement.setString(4, IAECrawlerAccount_Bean.getAccount());
            PreparedStatement.setInt(5, IAECrawlerAccount_Bean.getObjectInfo_Bean().getId());
            PreparedStatement.setString(6, IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
            PreparedStatement.setString(7, IAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());
            PreparedStatement.setBoolean(8, IAECrawlerAccount_Bean.isExportQuotation_defaultSelect());
            PreparedStatement.setBoolean(9,IAECrawlerAccount_Bean.isExportQuotation());
            PreparedStatement.setObject(10,IAECrawlerAccount_Bean.getTemplateFormatJsonObject() == null ? null : IAECrawlerAccount_Bean.getTemplateFormatJsonObject().toJSONString());

            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                IAECrawlerAccount_Bean.setId(Rs.getInt(1));
                IAECrawlerAccount_Bean.setExportQuotation_id(Rs.getInt(2));
            }
            status = true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return status;
    }
    private String getIAECrawlerBelongQuery(Integer account_id, ArrayList<IAECrawlerBelong_Bean> selectBelongList){
        String Query = "";
        if(selectBelongList == null)    return " ";
        for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : selectBelongList){
            if(account_id != null)
                Query = Query + "insert into IAECrawlerAccount_Belong (account_id,belong_id) values ('" + account_id + "','" + IAECrawlerBelong_Bean.getId() + "') ";
            else
                Query = Query + "insert into IAECrawlerAccount_Belong (account_id,belong_id) values ((SELECT IDENT_CURRENT('IAECrawlerAccount')),'" + IAECrawlerBelong_Bean.getId() + "') ";
        }
        return Query;
    }
    public boolean modifyIAECrawlerAccount(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                "update IAECrawlerAccount set ObjectID=?,Account=?,Password=? where id = ? ";
            Query = Query + "delete IAECrawlerAccount_Belong where account_id = ? ";
            Query = Query + getIAECrawlerBelongQuery(IAECrawlerAccount_Bean.getId(), IAECrawlerAccount_Bean.getIAECrawlerBelongList());
            Query = Query + "if EXISTS(select 1 from IAECrawlerAccount_ExportQuotation_Manufacturer where Account = ?) BEGIN " +
                        "update IAECrawlerAccount_ExportQuotation_Manufacturer set ManufacturerNickName = ?,ManufacturerAllName = ?,ExportQuotation = ?,TemplateFormat = ?,Manufacturer_id = (select id from Manufacturer where ObjectID = ?) where Account = ? END ELSE BEGIN " +
                        "insert into IAECrawlerAccount_ExportQuotation_Manufacturer (Account,Manufacturer_id,ManufacturerNickName,ManufacturerAllName,DefaultSelect,ExportQuotation,TemplateFormat) values (?,(select id from Manufacturer where ObjectID = ?),?,?,?,?,?) END ";
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
            PreparedStatement.setString(1,IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID());
            PreparedStatement.setString(2,IAECrawlerAccount_Bean.getAccount());
            PreparedStatement.setString(3,IAECrawlerAccount_Bean.getPassword());
            PreparedStatement.setInt(4,IAECrawlerAccount_Bean.getId());
            PreparedStatement.setInt(5,IAECrawlerAccount_Bean.getId());

            PreparedStatement.setString(6,IAECrawlerAccount_Bean.getAccount());
            PreparedStatement.setString(7,IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
            PreparedStatement.setString(8,IAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());
            PreparedStatement.setBoolean(9,IAECrawlerAccount_Bean.isExportQuotation());
            PreparedStatement.setObject(10,IAECrawlerAccount_Bean.getTemplateFormatJsonObject() == null ? null : IAECrawlerAccount_Bean.getTemplateFormatJsonObject().toJSONString());
            PreparedStatement.setString(11,IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID());
            PreparedStatement.setString(12,IAECrawlerAccount_Bean.getAccount());

            PreparedStatement.setString(13,IAECrawlerAccount_Bean.getAccount());
            PreparedStatement.setString(14,IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID());
            PreparedStatement.setString(15,IAECrawlerAccount_Bean.getExportQuotation_ManufacturerNickName());
            PreparedStatement.setString(16,IAECrawlerAccount_Bean.getExportQuotation_ManufacturerAllName());
            PreparedStatement.setBoolean(17,false);
            PreparedStatement.setBoolean(18,IAECrawlerAccount_Bean.isExportQuotation());
            PreparedStatement.setObject(19,IAECrawlerAccount_Bean.getTemplateFormatJsonObject() == null ? null : IAECrawlerAccount_Bean.getTemplateFormatJsonObject().toJSONString());
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
    public boolean isExistBindingInvoice(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        boolean isExistBindingInvoice = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "select 1 from Orders_InvoiceInfo A inner join IAECrawlerAccount_ExportQuotation_Manufacturer B on A.invoice_manufacturerNickName_id = B.id where B.Account = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1, IAECrawlerAccount_Bean.getAccount());
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isExistBindingInvoice = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            isExistBindingInvoice = true;
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isExistBindingInvoice;
    }
    public boolean isIAECrawlerAccountExistPayment(String account){
        boolean isIAECrawlerAccountExistPayment = false;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            String Query = "SELECT 1 FROM IAECrawlerAccount A INNER JOIN IAECrawlerAccount_Payment B ON A.id = B.Account_id WHERE A.Account = ?";
            PreparedStatement = SqlAdapter.getConnect().prepareStatement(Query);
            PreparedStatement.setString(1,account);
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())  isIAECrawlerAccountExistPayment = true;
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            isIAECrawlerAccountExistPayment = true;
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return isIAECrawlerAccountExistPayment;
    }
    public boolean deleteIAECrawlerAccount(IAECrawlerAccount_Bean IAECrawlerAccount_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "delete IAECrawlerAccount where id = ? and Account = ? " +
                    "delete IAECrawlerAccount_Belong where account_id = ? " +
                    "delete IAECrawlerAccount_ExportQuotation_Manufacturer where Manufacturer_id = (select id from Manufacturer where ObjectID = ?) and Account = ? " +
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
            PreparedStatement.setInt(1,IAECrawlerAccount_Bean.getId());
            PreparedStatement.setString(2,IAECrawlerAccount_Bean.getAccount());
            PreparedStatement.setInt(3,IAECrawlerAccount_Bean.getId());
            PreparedStatement.setString(4,IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID());
            PreparedStatement.setString(5,IAECrawlerAccount_Bean.getAccount());
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
    public ObservableList<IAECrawlerAccount_Bean> getIAECrawlerAccount(ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList){
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select A.id,A.ObjectID,B.ObjectName,A.Account,A.Password,C.id,C.ManufacturerNickName,C.ManufacturerAllName,C.DefaultSelect,C.ExportQuotation,C.TemplateFormat,C.TheSameAsNoneInvoice from IAECrawlerAccount A inner join Manufacturer B on A.ObjectID = B.ObjectID left join IAECrawlerAccount_ExportQuotation_Manufacturer C on B.id = C.Manufacturer_id and A.Account = C.Account");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
                IAECrawlerAccount_Bean.setId(Rs.getInt(1));
                ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
                ObjectInfo_Bean.setObjectID(Rs.getString(2));
                ObjectInfo_Bean.setObjectName(Rs.getString(3));
                IAECrawlerAccount_Bean.setObjectInfo_Bean(ObjectInfo_Bean);
                IAECrawlerAccount_Bean.setAccount(Rs.getString(4));
                IAECrawlerAccount_Bean.setPassword(Rs.getString(5));
                IAECrawlerAccount_Bean.setIAECrawlerBelongList(getIAECrawlerBelongList(IAECrawlerBelongList,IAECrawlerAccount_Bean.getId()));

                IAECrawlerAccount_Bean.setExportQuotation_id(Rs.getInt(6));
                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName(Rs.getString(7));
                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName(Rs.getString(8));
                IAECrawlerAccount_Bean.setExportQuotation_defaultSelect(Rs.getBoolean(9));
                IAECrawlerAccount_Bean.setExportQuotation(Rs.getBoolean(10));
                IAECrawlerAccount_Bean.setTemplateFormatJsonObject(Rs.getObject(11) == null ? null : JSONObject.parseObject(Rs.getString(11)));
                IAECrawlerAccount_Bean.setTheSameAsNoneInvoice(Rs.getBoolean(12));
                IAECrawlerAccountList.add(IAECrawlerAccount_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return IAECrawlerAccountList;
    }
    private ArrayList<IAECrawlerBelong_Bean> getIAECrawlerBelongList(ObservableList<IAECrawlerBelong_Bean> IAECrawlerBelongList,int id){
        ArrayList<IAECrawlerBelong_Bean> belongList = new ArrayList<>();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select belong_id from IAECrawlerAccount_Belong where account_id = '" + id +"'");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                for(IAECrawlerBelong_Bean IAECrawlerBelong_Bean : IAECrawlerBelongList){
                    if(IAECrawlerBelong_Bean.getId() == Rs.getInt(1)){
                        belongList.add(IAECrawlerBelong_Bean);
                        break;
                    }
                }
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return belongList;
    }
    private Integer getLastExportQuotationManufacturer(){
        Integer exportManufacturerId = null;
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("SELECT TOP 1 export_manufacturer_id FROM (SELECT export_manufacturer_id, insert_dateTime FROM Orders_ExportQuotationRecord WHERE insert_dateTime = (SELECT MAX(insert_dateTime) FROM Orders_ExportQuotationRecord)  UNION SELECT export_manufacturer_id, insert_dateTime FROM SubBill_ExportQuotationRecord WHERE insert_dateTime = (SELECT MAX(insert_dateTime) FROM SubBill_ExportQuotationRecord) ) AS M  ORDER BY insert_dateTime DESC ");
            Rs = PreparedStatement.executeQuery();
            if (Rs.next())
                exportManufacturerId = Rs.getInt(1);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return exportManufacturerId;
    }
    public ObservableList<IAECrawlerAccount_Bean> getIAEAccount_ManufacturerNickName(boolean isNotRepeat){
        HashMap<String,Boolean> existObjectMap = null;
        ObservableList<IAECrawlerAccount_Bean> IAECrawlerAccountList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select A.id,A.Account,B.ObjectID,B.ObjectName,B.ObjectNickName,A.ManufacturerNickName,A.ManufacturerAllName,A.ExportQuotation,A.TemplateFormat,A.TheSameAsNoneInvoice,A.DefaultSelect from IAECrawlerAccount_ExportQuotation_Manufacturer A inner join Manufacturer B on A.Manufacturer_id = (select id from Manufacturer where ObjectID = B.ObjectID) ORDER BY ObjectID");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                if(isNotRepeat && existObjectMap == null)   existObjectMap = new HashMap<>();
                IAECrawlerAccount_Bean IAECrawlerAccount_Bean = new IAECrawlerAccount_Bean();
                IAECrawlerAccount_Bean.setExportQuotation_id(Rs.getInt(1));
                IAECrawlerAccount_Bean.setAccount(Rs.getString(2));
                ObjectInfo_Bean ObjectInfo_Bean = new ObjectInfo_Bean();
                ObjectInfo_Bean.setObjectID(Rs.getString(3));
                ObjectInfo_Bean.setObjectName(Rs.getString(4));
                ObjectInfo_Bean.setObjectNickName(Rs.getString(5));
                IAECrawlerAccount_Bean.setObjectInfo_Bean(ObjectInfo_Bean);
                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerNickName(Rs.getString(6));
                IAECrawlerAccount_Bean.setExportQuotation_ManufacturerAllName(Rs.getString(7));
                IAECrawlerAccount_Bean.setExportQuotation(Rs.getBoolean(8));
                IAECrawlerAccount_Bean.setTemplateFormatJsonObject(Rs.getObject(9) == null ? null : JSONObject.parseObject(Rs.getString(9)));
                IAECrawlerAccount_Bean.setTheSameAsNoneInvoice(Rs.getBoolean(10));
                IAECrawlerAccount_Bean.setExportQuotation_defaultSelect(Rs.getBoolean(11));

                if(isNotRepeat){
                    if(!existObjectMap.containsKey(IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID()))
                        IAECrawlerAccountList.add(IAECrawlerAccount_Bean);
                    existObjectMap.put(IAECrawlerAccount_Bean.getObjectInfo_Bean().getObjectID(),true);
                }else
                    IAECrawlerAccountList.add(IAECrawlerAccount_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return IAECrawlerAccountList;
    }
    public boolean updateExportQuotation_theSameAsNoneInvoiceVendor (int id, boolean theSameAsNoneInvoice){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
            "update IAECrawlerAccount_ExportQuotation_Manufacturer set TheSameAsNoneInvoice = ? " +
            "update IAECrawlerAccount_ExportQuotation_Manufacturer set TheSameAsNoneInvoice = ? where id = ? " +
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
            PreparedStatement.setBoolean(1, false);
            PreparedStatement.setBoolean(2, theSameAsNoneInvoice);
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
    public boolean insertIpCam(IpCamInfo_Bean IpCamInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
                    if(IpCamInfo_Bean.getDefaultPreview())
                        Query = Query + "update IpCamera set default_preview = '0' ";
                    Query = Query +  "insert into IpCamera(default_preview,name,rtsp) values (?,?,?)" +
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
            PreparedStatement.setInt(1, IpCamInfo_Bean.getDefaultPreview() ? 1 : 0);
            PreparedStatement.setString(2, IpCamInfo_Bean.getName());
            PreparedStatement.setString(3, IpCamInfo_Bean.getRTSP());
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
    public boolean modifyIpCam(int id, boolean defaultPreview, String modifyName, String modifyRTSP){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION ";
            if(defaultPreview)
                Query = Query + "update IpCamera set default_preview = '0' ";
            Query = Query + "update IpCamera set default_preview = ?, name = ?, rtsp = ? where id = ? " +
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
            PreparedStatement.setInt(1,defaultPreview ? 1 : 0);
            PreparedStatement.setString(2,modifyName);
            PreparedStatement.setString(3,modifyRTSP);
            PreparedStatement.setInt(4,id);
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
    public boolean deleteIpCam(IpCamInfo_Bean IpCamInfo_Bean){
        boolean status = false;
        PreparedStatement PreparedStatement = null;
        try{
            String Query = "BEGIN TRY BEGIN TRANSACTION " +
                    "delete from IpCamera where id = ? and rtsp = ? " +
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
            PreparedStatement.setInt(1, IpCamInfo_Bean.getId());
            PreparedStatement.setString(2, IpCamInfo_Bean.getRTSP());
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
    public ObservableList<IpCamInfo_Bean> getIpCamera(){
        ObservableList<IpCamInfo_Bean> ipCamList = FXCollections.observableArrayList();
        PreparedStatement PreparedStatement = null;
        ResultSet Rs = null;
        try {
            PreparedStatement = SqlAdapter.getConnect().prepareStatement("select * from IpCamera");
            Rs = PreparedStatement.executeQuery();
            while (Rs.next()) {
                IpCamInfo_Bean IpCamInfo_Bean = new IpCamInfo_Bean();
                IpCamInfo_Bean.setId(Rs.getInt("id"));
                IpCamInfo_Bean.setDefaultPreview(Rs.getBoolean("default_preview"));
                IpCamInfo_Bean.setName(Rs.getString("name"));
                IpCamInfo_Bean.setRTSP(Rs.getString("rtsp"));
                ipCamList.add(IpCamInfo_Bean);
            }
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }finally{
            SqlAdapter.closeConnectParameter(PreparedStatement,Rs);
        }
        return ipCamList;
    }
}
