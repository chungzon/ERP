package ERP.ToolKit;

import ERP.Bean.DataBaseServer_Bean;
import ERP.Bean.LineAPI_Bean;
import ERP.Bean.ToolKit.Properties.ExportQuotationProperties_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.ManageProductInfoJsonConfigKey;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.PaymentCompareTabName;
import ERP.Enum.SystemSetting.SystemDefaultConfig_Enum;
import ERP.Sql.SqlAdapter;
import ERP.View.DialogUI;
import com.alibaba.fastjson.parser.Feature;
import javafx.application.Platform;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Call Config file */
public class CallConfig {

    /** [Json] Get database info*/
    public JSONObject getDataBaseJson() throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.DataBaseJson);
        return JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
    }

    public void setERPServerDatabase(DataBaseServer_Bean DataBaseServer_Bean){
        Properties Properties = new Properties();
        InputStream InputStream = null;
        try {
            InputStream = new FileInputStream(ConfigPath.ERPServerDatabase);
            Properties.load(InputStream);
            Properties.setProperty("spring.datasource.driver-class-name",DataBaseServer_Bean.getDriver());
            Properties.setProperty("spring.datasource.url",DataBaseServer_Bean.getUrl());
            Properties.setProperty("spring.datasource.username",DataBaseServer_Bean.getUser());
            Properties.setProperty("spring.datasource.password",DataBaseServer_Bean.getPassword());
            FileOutputStream fr = new FileOutputStream(ConfigPath.ERPServerDatabase);
            Properties.store(fr, "Properties");
            fr.close();
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            if (InputStream != null) {
                try {
                    InputStream.close();
                } catch (Exception Ex) {
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
    }
    public LineAPI_Bean getLineAPIProperties(){
        LineAPI_Bean LineAPI_Bean = new LineAPI_Bean();
        Properties Properties = new Properties();
        InputStream InputStream = null;
        try {
            InputStream = new FileInputStream(ConfigPath.LineAPIProperties);
            Properties.load(InputStream);
            LineAPI_Bean.setLineAPI(Properties.getProperty("LineAPI"));
            LineAPI_Bean.setToken(Properties.getProperty("Token"));
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            if (InputStream != null) {
                try {
                    InputStream.close();
                } catch (Exception Ex) {
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
        return LineAPI_Bean;
    }
    /** [Properties] Get export quotation parameters */
    public ExportQuotationProperties_Bean getExportQuotationProperties() {
        ExportQuotationProperties_Bean ExportQuotationProperties_Bean = new ExportQuotationProperties_Bean();
        Properties Properties = new Properties();
        InputStream InputStream = null;
        try {
            InputStream = new FileInputStream(ConfigPath.ExportQuotation);
            Properties.load(InputStream);
            ExportQuotationProperties_Bean.setStartRow_ZYH(Integer.parseInt(Properties.getProperty("StartRow_ZYH")));
            ExportQuotationProperties_Bean.setEndRow_ZYH(Integer.parseInt(Properties.getProperty("EndRow_ZYH")));
            ExportQuotationProperties_Bean.setStartRow_SheiShin(Integer.parseInt(Properties.getProperty("StartRow_SheiShin")));
            ExportQuotationProperties_Bean.setEndRow_SheiShin(Integer.parseInt(Properties.getProperty("EndRow_SheiShin")));
            ExportQuotationProperties_Bean.setStartRow_HuangJia(Integer.parseInt(Properties.getProperty("StartRow_HuangJia")));
            ExportQuotationProperties_Bean.setEndRow_HuangJia(Integer.parseInt(Properties.getProperty("EndRow_HuangJia")));
            ExportQuotationProperties_Bean.setStartRow_Qilian(Integer.parseInt(Properties.getProperty("StartRow_Qilian")));
            ExportQuotationProperties_Bean.setEndRow_Qilian(Integer.parseInt(Properties.getProperty("EndRow_Qilian")));
            ExportQuotationProperties_Bean.setStartRow_FuDaxing(Integer.parseInt(Properties.getProperty("StartRow_FuDaxing")));
            ExportQuotationProperties_Bean.setEndRow_FuDaxing(Integer.parseInt(Properties.getProperty("EndRow_FuDaxing")));
            ExportQuotationProperties_Bean.setStartRow_Liancheng(Integer.parseInt(Properties.getProperty("StartRow_Liancheng")));
            ExportQuotationProperties_Bean.setEndRow_Liancheng(Integer.parseInt(Properties.getProperty("EndRow_Liancheng")));
            ExportQuotationProperties_Bean.setStartRow_HuiZhan(Integer.parseInt(Properties.getProperty("StartRow_HuiZhan")));
            ExportQuotationProperties_Bean.setEndRow_HuiZhan(Integer.parseInt(Properties.getProperty("EndRow_HuiZhan")));
            ExportQuotationProperties_Bean.setStartRow_NoneInvoice(Integer.parseInt(Properties.getProperty("StartRow_NoneInvoice")));
            ExportQuotationProperties_Bean.setEndRow_NoneInvoice(Integer.parseInt(Properties.getProperty("EndRow_NoneInvoice")));
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            if (InputStream != null) {
                try {
                    InputStream.close();
                } catch (Exception Ex) {
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
        return ExportQuotationProperties_Bean;
    }
    public String getFile_OutputPath() {
        String File_OutputPath = "";
        Properties Properties = new Properties();
        InputStream InputStream = null;
        try {
            InputStream = new FileInputStream(ConfigPath.File_OutputPath);
            Properties.load(InputStream);
            File_OutputPath = Properties.getProperty("File_OutputPath");
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        } finally {
            if (InputStream != null) {
                try {
                    InputStream.close();
                } catch (Exception Ex) {
                    ERPApplication.Logger.catching(Ex);
                    Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
                }
            }
        }
        return File_OutputPath;
    }
    public JSONObject getManageProductInfoJsonConfig_TableViewSetting() throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.ManageProductInfoJsonConfig);
        JSONObject json = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
        return json.getJSONObject(ManageProductInfoJsonConfigKey.商品查詢欄位.name());
    }
    public JSONObject getManageProductInfoJsonConfig_AdvancedSearch() throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.ManageProductInfoJsonConfig);
        JSONObject json = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
        return json.getJSONObject(ManageProductInfoJsonConfigKey.進階搜尋欄位.name());
    }
    public void setManageProductInfoJsonConfig(ManageProductInfoJsonConfigKey ManageProductInfoJsonConfigKey, JSONObject JsonObjectOfConfig) throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.ManageProductInfoJsonConfig);
        JSONObject readJson = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
        JSONObject newJson = new JSONObject();

        BufferedWriter output = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(ConfigPath.ManageProductInfoJsonConfig), StandardCharsets.UTF_8));
        for(String productSearchColumn : readJson.keySet()){
            if (productSearchColumn.equals(ManageProductInfoJsonConfigKey.name())) newJson.put(productSearchColumn, JsonObjectOfConfig);
            else   newJson.put(productSearchColumn, readJson.get(productSearchColumn));
        }
        output.write(newJson.toString());
        output.close();
    }
    public JSONObject getProductGeneratorJsonConfig() throws Exception{
        InputStream inputStream = new FileInputStream(ConfigPath.ProductGeneratorJsonConfig);
        JSONObject json = JSONObject.parseObject(convertInputStreamToString(inputStream),Feature.OrderedField);
        return json.getJSONObject(Product_Enum.CategoryLayer.大類別.name());
    }
    public void setProductGeneratorJsonConfig(Product_Enum.CategoryLayer ProductGeneratorJsonConfigKey, JSONObject JsonObjectOfConfig) throws Exception{
        InputStream inputStream = new FileInputStream(ConfigPath.ProductGeneratorJsonConfig);
        JSONObject readJson = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
        JSONObject newJson = new JSONObject();

        BufferedWriter output = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(ConfigPath.ProductGeneratorJsonConfig), StandardCharsets.UTF_8));
        for (String CategoryName : readJson.keySet()) {
            if (CategoryName.equals(ProductGeneratorJsonConfigKey.name())) newJson.put(CategoryName, JsonObjectOfConfig);
            else   newJson.put(CategoryName, readJson.get(CategoryName));
        }
        output.write(newJson.toString());
        output.close();
    }
    /** [Json] Get sort of TableColumn from search order TableView
     * @param SearchOrderSource search order type of TableView
     * */
    public JSONObject getSearchOrderTableViewSettingJson(SearchOrderSource SearchOrderSource) throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.SearchOrderTableViewSettingJson);
        JSONObject json = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
        return json.getJSONObject(SearchOrderSource.name());
    }
    /** [Json] Set sort of TableColumn from search order TableView
     * @param SearchOrderSource search order type of TableView
     * @param TableViewSettingMap TableColumn of the TableView are already sorted
     * */
    public void setSearchOrderTableViewSettingJson(SearchOrderSource SearchOrderSource, JSONObject TableViewSettingMap) throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.SearchOrderTableViewSettingJson);
        JSONObject readJson = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
        JSONObject newJson = new JSONObject();

        BufferedWriter output = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(ConfigPath.SearchOrderTableViewSettingJson), StandardCharsets.UTF_8));
        for(String OrderSource : readJson.keySet()){
            if (Order_Enum.SearchOrderSource.valueOf(OrderSource) == SearchOrderSource) newJson.put(OrderSource, TableViewSettingMap);
            else   newJson.put(OrderSource, readJson.get(OrderSource));
        }
        output.write(newJson.toString());
        output.close();
    }
    public JSONObject getPaymentCompareTableViewSettingJson(PaymentCompareTabName PaymentCompareTabName) throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.PaymentCompareTableViewSettingJson);
        JSONObject json = JSONObject.parseObject(convertInputStreamToString(inputStream),Feature.OrderedField);
        return json.getJSONObject(PaymentCompareTabName.name());
    }
    public void setPaymentCompareTableViewSettingJson(PaymentCompareTabName PaymentCompareTabName, JSONObject TableViewSettingMap) throws Exception {
        InputStream inputStream = new FileInputStream(ConfigPath.PaymentCompareTableViewSettingJson);
        JSONObject readJson = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
        JSONObject newJson = new JSONObject();

        BufferedWriter output = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(ConfigPath.PaymentCompareTableViewSettingJson), StandardCharsets.UTF_8));
        for(String paymentStatus : readJson.keySet()){
            if (PayableReceivable_Enum.PaymentCompareTabName.valueOf(paymentStatus) == PaymentCompareTabName) newJson.put(paymentStatus, TableViewSettingMap);
            else   newJson.put(paymentStatus, readJson.get(paymentStatus));
        }
        output.write(newJson.toString());
        output.close();
    }
    private String convertInputStreamToString(InputStream inputStream){
        String jsonStr = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try{
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1){
                out.write(buffer, 0, len);
            }
            jsonStr = new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return jsonStr;
    }
    public HashMap<Order_Enum.OrderSource,HashMap<Integer,Boolean>> getOrderReferenceJson() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(ConfigPath.OrderReferenceJson);
        JSONObject json = JSONObject.parseObject(convertInputStreamToString(inputStream),Feature.OrderedField);
        json = json.getJSONObject(SqlAdapter.getDbDisplayName());

        HashMap<Order_Enum.OrderSource,HashMap<Integer,Boolean>> orderReferenceMap = new HashMap<>();
        if(json != null){
            String order_id_list = json.getString(Order_Enum.OrderSource.報價單.name());
            HashMap<Integer,Boolean> referenceMap = new HashMap<>();
            if(!order_id_list.equals("") && order_id_list.contains(",")){
                String[] orderList = order_id_list.split(",");
                for (String s : orderList) {
                    referenceMap.put(Integer.parseInt(s), true);
                }
            }else if(!order_id_list.equals(""))
                referenceMap.put(Integer.parseInt(order_id_list),true);
            orderReferenceMap.put(Order_Enum.OrderSource.報價單,referenceMap);

            referenceMap = new HashMap<>();
            String subBill_id_list = json.getString(Order_Enum.OrderSource.出貨子貨單.name());
            if(!subBill_id_list.equals("") && subBill_id_list.contains(",")){
                String[] orderList = subBill_id_list.split(",");
                for (String s : orderList) {
                    referenceMap.put(Integer.parseInt(s), true);
                }
            }else if(!subBill_id_list.equals(""))
                referenceMap.put(Integer.parseInt(subBill_id_list),true);
            orderReferenceMap.put(Order_Enum.OrderSource.出貨子貨單,referenceMap);
        }else{
            orderReferenceMap.put(Order_Enum.OrderSource.報價單,new HashMap<>());
            orderReferenceMap.put(Order_Enum.OrderSource.出貨子貨單,new HashMap<>());
        }
        return orderReferenceMap;
    }
    public boolean setOrderReferenceJson(HashMap<Integer,Boolean> mainOrderReferenceMap, HashMap<Integer,Boolean> subBillReferenceMap){
        String mainOrderList = "",subBillList = "";
        for(int order_id : mainOrderReferenceMap.keySet()){
            mainOrderList = mainOrderList + order_id + ",";
        }
        if(mainOrderList.contains(","))
            mainOrderList = mainOrderList.substring(0,mainOrderList.lastIndexOf(","));
        for(int subBill_id : subBillReferenceMap.keySet()){
            subBillList = subBillList + subBill_id + ",";
        }
        if(subBillList.contains(","))
            subBillList = subBillList.substring(0,subBillList.lastIndexOf(","));

        try{
            JSONObject orderReferenceMap = new JSONObject();
            orderReferenceMap.put(Order_Enum.OrderSource.報價單.name(),mainOrderList);
            orderReferenceMap.put(Order_Enum.OrderSource.出貨子貨單.name(),subBillList);

            InputStream inputStream = new FileInputStream(ConfigPath.OrderReferenceJson);
            JSONObject readJson = JSONObject.parseObject(convertInputStreamToString(inputStream), Feature.OrderedField);
            JSONObject newJson = new JSONObject();

            BufferedWriter output = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(ConfigPath.OrderReferenceJson), StandardCharsets.UTF_8));
            for (String otherDBName : readJson.keySet()) {
                if (otherDBName.equals(SqlAdapter.getDbDisplayName()))
                    newJson.put(SqlAdapter.getDbDisplayName(), orderReferenceMap);
                else newJson.put(otherDBName, readJson.get(otherDBName));
            }
            if(!newJson.containsKey(SqlAdapter.getDbDisplayName()))
                newJson.put(SqlAdapter.getDbDisplayName(), orderReferenceMap);

            output.write(newJson.toString());
            output.close();
            return true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            DialogUI.ExceptionDialog(Ex);
            return false;
        }
    }
    public JSONObject getSystemDefaultConfigJson() {
        JSONObject returnJson = null;
        try{
            File file = new File(ConfigPath.SystemDefaultConfigJson);
            if(!file.exists()){
                file.createNewFile();
            }
            InputStream inputStream = new FileInputStream(ConfigPath.SystemDefaultConfigJson);
            JSONObject json = JSONObject.parseObject(convertInputStreamToString(inputStream),Feature.OrderedField);
            if(json == null){
                json = new JSONObject();
                JSONObject configJson = new JSONObject();
                configJson.put(SystemDefaultConfig_Enum.UploadPictureFunction.class.getSimpleName(),
                        SystemDefaultConfig_Enum.UploadPictureFunction.UploadLocation.ordinal());
                configJson.put(SystemDefaultConfig_Enum.Order_SnapshotCamera.class.getSimpleName(),
                        SystemDefaultConfig_Enum.Order_SnapshotCamera.WebCamera.ordinal());
                configJson.put(SystemDefaultConfig_Enum.Order_DefaultProductConnection.class.getSimpleName(),
                        SystemDefaultConfig_Enum.Order_DefaultProductConnection.CloseProductConnection.ordinal());
                configJson.put(SystemDefaultConfig_Enum.Order_SearchFunction.class.getSimpleName(),
                        SystemDefaultConfig_Enum.Order_SearchFunction.TransactionDetail.ordinal());
                configJson.put(SystemDefaultConfig_Enum.Order_PrintFunction.class.getSimpleName(),
                        SystemDefaultConfig_Enum.Order_PrintFunction.PrintLabel.ordinal());
                configJson.put(SystemDefaultConfig_Enum.Order_ExportPurchaseFunction.class.getSimpleName(),
                        SystemDefaultConfig_Enum.Order_ExportPurchaseFunction.PurchaseOrder.ordinal());
                json.put(SystemDefaultConfig_Enum.SystemDefaultName.class.getSimpleName(),configJson);
            }
            returnJson = json.getJSONObject(SystemDefaultConfig_Enum.SystemDefaultName.class.getSimpleName());
            inputStream.close();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
        }
        return returnJson;
    }
    public boolean setSystemDefaultConfigJson(SystemDefaultConfig_Enum.SystemDefaultName SystemDefaultName, JSONObject systemDefaultConfigMap) {
        try{
            JSONObject newJson = new JSONObject();
            BufferedWriter output = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(ConfigPath.SystemDefaultConfigJson), StandardCharsets.UTF_8));
            newJson.put(SystemDefaultName.getClass().getSimpleName(), systemDefaultConfigMap);
            output.write(newJson.toString());
            output.close();
            return true;
        }catch (Exception Ex){
            DialogUI.ExceptionDialog(Ex);
            ERPApplication.Logger.catching(Ex);
        }
        return false;
    }
}
