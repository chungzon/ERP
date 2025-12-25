package ERP.Sql;

import ERP.Bean.DataBaseServer_Bean;
import ERP.ERPApplication;
import ERP.Enum.SystemSetting.SystemSetting_Enum.SystemSettingConfig;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallConfig;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.application.Platform;

import java.sql.*;
import java.util.ArrayList;

public class SqlAdapter extends CallConfig {
    private static SystemSetting_Model systemSetting_model;

    private ArrayList<DataBaseServer_Bean> dataBaseList;
    private static Connection connect = null;
    private static SqlAdapter SqlAdapter;
    private Thread databaseCheckAliveThread = null;
    private DataBaseServer_Bean testDB_dataBaseServer_bean;
    public static SqlAdapter getInstance() throws Exception {
        if(SqlAdapter == null)
            SqlAdapter = new SqlAdapter();
        return SqlAdapter;
    }
    public SqlAdapter() throws Exception {
        systemSetting_model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
        loadDataBaseJson();
    }
    /** Read database info from Json file */
    private void loadDataBaseJson() throws Exception {
        if(dataBaseList != null)
            dataBaseList.clear();
        JSONObject JsonObject = getDataBaseJson();
        JSONArray JsonArray = JsonObject.getJSONArray("Servers");
        for (int Index = 0; Index < JsonArray.size(); Index++) {
            DataBaseServer_Bean DataBaseServer_Bean = new DataBaseServer_Bean();
            DataBaseServer_Bean.setIndex(JsonArray.getJSONObject(Index).getInteger("Index"));
            DataBaseServer_Bean.setDisplay(JsonArray.getJSONObject(Index).getString("Display"));
            DataBaseServer_Bean.setDriver(JsonArray.getJSONObject(Index).getString("Driver"));
            DataBaseServer_Bean.setUrl(JsonArray.getJSONObject(Index).getString("Url"));
            DataBaseServer_Bean.setUser(JsonArray.getJSONObject(Index).getString("User"));
            DataBaseServer_Bean.setPassword(JsonArray.getJSONObject(Index).getString("Password"));
            serverList().add(DataBaseServer_Bean);
        }
    }
    /** Instance database object */
    private ArrayList<DataBaseServer_Bean> serverList() {
        if (dataBaseList == null)
            dataBaseList = new ArrayList<>();
        return dataBaseList;
    }
    /** Get database object */
    public ArrayList<DataBaseServer_Bean> getDataBaseList() throws Exception {
        loadDataBaseJson();
        return dataBaseList;
    }
    /**
     * Connect to database which selected
     * @param index which database in Json file
     * */
    private DataBaseServer_Bean DataBaseServer_Bean;
    private static String dbDisplayName;
    public DataBaseServer_Bean getDataBaseServer_Bean(int index){
        return dataBaseList.get(index);
    }
    public void connectDB(boolean restart, int index) throws Exception{
        this.DataBaseServer_Bean = dataBaseList.get(index);
        Class.forName(DataBaseServer_Bean.getDriver());
        if(restart) {
            if(databaseCheckAliveThread != null) {
                databaseCheckAliveThread.interrupt();
                databaseCheckAliveThread = null;
            }
            connect.close();
            connect = null;
        }
        if(connect == null)
            connect = DriverManager.getConnection(DataBaseServer_Bean.getUrl(),DataBaseServer_Bean.getUser(),DataBaseServer_Bean.getPassword());

        dbDisplayName = DataBaseServer_Bean.getDisplay();

        databaseCheckAliveThread = new checkAliveThread();
        databaseCheckAliveThread.start();
    }
    public static String getDbDisplayName(){
        return dbDisplayName;
    }
    class checkAliveThread extends Thread{
        @Override
        public void run() {
            while (true) {
                try{
                    Thread.sleep(1000 * 60 * 2);
                    long nowTimeStamp = System.currentTimeMillis();
                    ERPApplication.Logger.info("checkAlive : " + new Timestamp(nowTimeStamp));
                    System.runFinalization();
                    checkAlive();
                }catch (Exception Ex){
                    ERPApplication.Logger.catching(Ex);
                }
            }
        }
    }
    /** Keep database connected
     * */
    private void checkAlive() throws Exception{
//        if(connect.isClosed() || nowTimeStamp - startTimeStamp >= 1000*60*30){
        if(!connect.isValid(0)){
            ERPApplication.Logger.warn("connect is expire or close... starting reConnect ~");
            connect.close();
            Class.forName(DataBaseServer_Bean.getDriver());
            connect = DriverManager.getConnection(DataBaseServer_Bean.getUrl(), DataBaseServer_Bean.getUser(), DataBaseServer_Bean.getPassword());
            ERPApplication.Logger.warn("connect is refresh !!");
        }
    }
    /*public static boolean isConnectValid(){
        boolean isValid = false;
        try{
            isValid = connect.isValid(8);
            if(!isValid) {
                DialogUI.AlarmDialog("※ 網路連線不穩定");
                SqlAdapter.checkAlive();
            }
        }catch (Exception Ex){
            ZyhServerApplication.Logger.catching(Ex);
        }
        return isValid;
    }*/
    public static boolean connectTest(DataBaseServer_Bean dataBaseServer_bean) throws SQLException {
        boolean connectStatus = false;
        Connection connect = null;
        try{
            Class.forName(dataBaseServer_bean.getDriver());
            connect = DriverManager.getConnection(dataBaseServer_bean.getUrl(), dataBaseServer_bean.getUser(), dataBaseServer_bean.getPassword());
            connectStatus = true;
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            DialogUI.ExceptionDialog(ex);
        }finally {
            if(connect != null){
                connect.close();
            }
        }
        return connectStatus;
    }
    public static Connection getTestDBConnect(){
        Connection connect = null;
        try{
            String database = systemSetting_model.getSpecificSystemSettingData(SystemSettingConfig.測試資料庫);
            JSONObject databaseJson = JSONObject.parseObject(database);
            Class.forName(databaseJson.getString("driver"));
            connect = DriverManager.getConnection(databaseJson.getString("url"), databaseJson.getString("user"), databaseJson.getString("password"));
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            DialogUI.ExceptionDialog(ex);
        }
        return connect;
    }
    /** Get database connect object */
    public static Connection getConnect(){
        return connect;
    }
    public static void DataBaseQuery(String Query) throws Exception{
        PreparedStatement PreparedStatement = getConnect().prepareStatement(Query);
        PreparedStatement.executeUpdate();
        closeConnectParameter(PreparedStatement,null);
    }
    public static void closeConnectParameter(PreparedStatement PreparedStatement, ResultSet ResultSet){
        try{
            if(PreparedStatement != null)   PreparedStatement.close();
            if(ResultSet != null)   ResultSet.close();
        }catch(Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    public static void closeConnection(Connection connect){
        try {
            connect.close();
        } catch (SQLException ex) {
            ERPApplication.Logger.catching(ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(ex));
        }
    }
}
