package ERP.Model.Order.ExportShipmentQuotationDocument;

import ERP.Bean.Object.ObjectInfo_Bean;
import ERP.Bean.Order.ExportQuotation_Bean;
import ERP.Bean.Order.OrderProduct_Bean;
import ERP.Bean.Order.Order_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.ERPApplication;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.SystemSetting.SystemSetting_Enum;
import ERP.Enum.ToolKit.ToolKit_Enum;
import ERP.Model.ManagePayableReceivable.BasicExcelBuilder;
import ERP.Model.Order.Order_Model;
import ERP.ToolKit.TemplatePath;
import ERP.ToolKit.ToolKit;
import ERP.ToolKit.CallConfig;
import ERP.View.DialogUI;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExportShipmentQuotationDocument extends BasicExcelBuilder {

    private ToolKit ToolKit;
    private CallConfig CallConfig;
    private Order_Model Order_Model;
    private ExportQuotation_Bean exportQuotation_Bean;
    private ObjectInfo_Bean objectInfo_Bean;
    private String template;
    private String outputFilePath;
    private JSONObject templateFormat;
    double totalPriceIncludeTax = 0;
    public ExportShipmentQuotationDocument(){
        this.ToolKit = ERPApplication.ToolKit;
        this.CallConfig = ToolKit.CallConfig;
        this.Order_Model = ToolKit.ModelToolKit.getOrderModel();
    }
    private void readTemplate(String filePath) throws IOException {
        fileInputStream = new FileInputStream(new File(filePath));
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheetAt(0);
    }
    public boolean setExportDataAndExportQuotation(OrderObject OrderObject, ExportQuotation_Bean exportQuotation_bean) {
        this.objectInfo_Bean = Order_Model.getObjectFromSearchColumn(OrderObject, Order_Enum.ObjectSearchColumn.客戶編號, exportQuotation_bean.getCustomerID()).get(0);
        this.exportQuotation_Bean = exportQuotation_bean;

        outputFilePath = CallConfig.getFile_OutputPath();
        Order_Model.deleteRepeatExportQuotationFileName(outputFilePath, exportQuotation_bean);

        template = TemplatePath.ExportQuotation(exportQuotation_bean.getVendorNickName(),exportQuotation_bean.getExportFormat());

        if(exportQuotation_bean.getExportContent() == Order_Enum.ExportQuotationContent.專案){
            ObservableList<OrderProduct_Bean> productList = exportQuotation_bean.getProductList();
            double totalBatchPrice = 0;
            for(OrderProduct_Bean OrderProduct_Bean : productList)
                totalBatchPrice = totalBatchPrice + OrderProduct_Bean.getBatchPrice()*OrderProduct_Bean.getQuantity();
            OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
            OrderProduct_Bean.setItemNumber(1);
            OrderProduct_Bean.setProductName(exportQuotation_bean.getProjectName());
            OrderProduct_Bean.setUnit(exportQuotation_bean.getProjectUnit());
            OrderProduct_Bean.setQuantity(ToolKit.RoundingInteger(exportQuotation_bean.getProjectQuantity()));
            OrderProduct_Bean.setBatchPrice(ToolKit.RoundingInteger(totalBatchPrice));
            OrderProduct_Bean.setSinglePrice(ToolKit.RoundingInteger(exportQuotation_bean.getProjectPriceAmount()));
            OrderProduct_Bean.setPriceAmount(ToolKit.RoundingInteger(exportQuotation_bean.getProjectTotalPriceNoneTax()));
            OrderProduct_Bean.setAddExportQuotationItem(false);
            productList.clear();
            productList.add(OrderProduct_Bean);
        }
        return exportQuotation();
    }
    private boolean exportQuotation() {
        Order_Enum.ExportQuotationFormat exportQuotationFormat = exportQuotation_Bean.getExportFormat();
        templateFormat = exportQuotation_Bean.getTemplateFormat().getJSONObject(exportQuotationFormat.name());

        Integer startRowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.itemRange.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.itemRange.name()).getInteger("startIndex");
        Integer endRowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.itemRange.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.itemRange.name()).getInteger("endIndex");
        if(startRowIndex == null || endRowIndex == null) {
            DialogUI.AlarmDialog("品項範圍設定錯誤");
            return false;
        }
        ObservableList<OrderProduct_Bean> productList = exportQuotation_Bean.getProductList();
        int itemRowCount = endRowIndex-startRowIndex+1;
        int itemSize = productList.size();
        int pageSize = itemSize/itemRowCount;
        if(itemSize%itemRowCount == 0)
            pageSize = pageSize - 1;
        try{
            boolean isExistProductGroup = (exportQuotation_Bean.getProductGroup() != null);
            for(int WordPage = 0 ; WordPage <= pageSize ; WordPage++) {
                readTemplate(template);
                setTitle();
                setExportCount();
                String orderRemark = exportQuotation_Bean.getOrderRemark();
                for (int index = 0; index < itemRowCount; index++) {
                    int itemListIndex = index + (WordPage*itemRowCount);
                    final int rowIndex = index + startRowIndex;
                    if (itemListIndex >= itemSize) {
                        if(itemListIndex == itemSize) {
                            addRow(rowIndex - 1,(index + 1),"以下空白",null,null,null,null,null,null,null);
                            break;
                        }
                    }else{
                        int itemNumber = productList.get(itemListIndex).getItemNumber();
                        String productName = productList.get(itemListIndex).getProductName();
                        String unit = productList.get(itemListIndex).getUnit();
                        int quantity = productList.get(itemListIndex).getQuantity();
                        double batchPrice = productList.get(itemListIndex).getBatchPrice();
                        double singlePrice = productList.get(itemListIndex).getSinglePrice();
                        double priceAmount = productList.get(itemListIndex).getPriceAmount();
                        String productRemark = productList.get(itemListIndex).getRemark();
                        boolean isAddExportQuotationItem = productList.get(itemListIndex).isAddExportQuotationItem();
                        addRow(rowIndex-1,itemNumber,productName,quantity,unit,batchPrice,singlePrice,priceAmount,productRemark,isAddExportQuotationItem);

                        if(exportQuotation_Bean.isExportGroup() && isExistProductGroup) {
                            if(index == 0 && orderRemark != null && !orderRemark.equals("")){
                                orderRemark = orderRemark + "\n";
                            }
                            orderRemark = orderRemark + getOrderGroupRemark(index, itemNumber, exportQuotation_Bean.getProductGroup());
                        }
                    }
                }
                if(!exportQuotation_Bean.isExportGroup() && isExistProductGroup) {
                    orderRemark = generateOrderRemark(exportQuotation_Bean.getProductGroup());
                }
                addOrderRemark(orderRemark);

                String outPutFileName = exportQuotation_Bean.getOrderNumber() + "-" + (WordPage+1) + "-" + exportQuotation_Bean.getProjectName() + "-" + exportQuotation_Bean.getVendorNickName() + "(估-" + exportQuotation_Bean.getExportFormat().name() + ").xlsx";
                build(outputFilePath + "\\" + outPutFileName);
            }
            return true;
        }catch (Exception ex){
            ERPApplication.Logger.catching(ex);
            DialogUI.ExceptionDialog(ex);
        }
        return false;
    }
    private String getOrderGroupRemark(int index, int group_itemNumber, HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap){
        ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
        HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);

        String orderRemark = ToolKit_Enum.ConvertMathToUpperEnglish.values()[index].name() + ". " + ProductGroup_Bean.getGroupName() + "\n";
        int itemIndex = 1;
        for(Integer item_id : itemMap.keySet()) {
            ItemGroup_Bean itemGroup_bean = itemMap.get(item_id);
            orderRemark = orderRemark + "    " + itemIndex + ". " + itemGroup_bean.getProductName() + "\n";
            itemIndex = itemIndex + 1;
        }
        return orderRemark;
    }
    private String generateOrderRemark(HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap){
        String remark = exportQuotation_Bean.getOrderRemark();
        if(productGroupMap != null) {
            if(!remark.equals(""))
                remark = remark + "\n";
            int groupIndex = 0;
            for(Integer group_itemNumber : productGroupMap.keySet()){
                int itemIndex = 1;
                ProductGroup_Bean ProductGroup_Bean = productGroupMap.get(group_itemNumber).entrySet().iterator().next().getKey();
                remark = remark + ToolKit_Enum.ConvertMathToUpperEnglish.values()[groupIndex].name() + ". " + ProductGroup_Bean.getGroupName() + "\n";
                HashMap<Integer, ItemGroup_Bean> itemMap = productGroupMap.get(group_itemNumber).get(ProductGroup_Bean);
                for(Integer item_id : itemMap.keySet()) {
                    ItemGroup_Bean itemGroup_bean = itemMap.get(item_id);
                    remark = remark + "    " + itemIndex + ". " + itemGroup_bean.getProductName() + "\n";
                    itemIndex = itemIndex + 1;
                }
                groupIndex = groupIndex + 1;
            }
        }
        return remark;
    }
    private void addOrderRemark(String orderRemark){
        String remark_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.remark.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.remark.name()).getString("cell");
        Integer remark_cellIndex = remark_cell.equals("") ? null : ToolKit.getEnglishIndex(true, remark_cell);
        Integer remark_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.remark.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.remark.name()).getInteger("row");
        remark_rowIndex = remark_rowIndex == null ? null : remark_rowIndex-1;

        setXSSFSheetCell(sheet,remark_rowIndex,remark_cellIndex, orderRemark);
    }
    private void setTitle(){
        String quotationDate_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationDate.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationDate.name()).getString("cell");
        Integer quotationDate_cellIndex = quotationDate_cell.equals("") ? null : ToolKit.getEnglishIndex(true, quotationDate_cell);
        Integer quotationDate_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationDate.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationDate.name()).getInteger("row");
        quotationDate_rowIndex = quotationDate_rowIndex == null ? null : quotationDate_rowIndex-1;

        String quotationNumber_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationNumber.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationNumber.name()).getString("cell");
        Integer quotationNumber_cellIndex = quotationNumber_cell.equals("") ? null : ToolKit.getEnglishIndex(true, quotationNumber_cell);
        Integer quotationNumber_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationNumber.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.quotationNumber.name()).getInteger("row");
        quotationNumber_rowIndex = quotationNumber_rowIndex == null ? null : quotationNumber_rowIndex-1;

        String projectCode_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.projectCode.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.projectCode.name()).getString("cell");
        Integer projectCode_cellIndex = projectCode_cell.equals("") ? null : ToolKit.getEnglishIndex(true, projectCode_cell);
        Integer projectCode_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.projectCode.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.projectCode.name()).getInteger("row");
        projectCode_rowIndex = projectCode_rowIndex == null ? null : projectCode_rowIndex-1;

        String projectName_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.projectName.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.projectName.name()).getString("cell");
        Integer projectName_cellIndex = projectName_cell.equals("") ? null : ToolKit.getEnglishIndex(true, projectName_cell);
        Integer projectName_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.projectName.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.projectName.name()).getInteger("row");
        projectName_rowIndex = projectName_rowIndex == null ? null : projectName_rowIndex-1;

        String customerName_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.customerName.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.customerName.name()).getString("cell");
        Integer customerName_cellIndex = customerName_cell.equals("") ? null : ToolKit.getEnglishIndex(true, customerName_cell);
        Integer customerName_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.customerName.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.customerName.name()).getInteger("row");
        customerName_rowIndex = customerName_rowIndex == null ? null : customerName_rowIndex-1;

        String taxIdNumber_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.taxIdNumber.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.taxIdNumber.name()).getString("cell");
        Integer taxIdNumber_cellIndex = taxIdNumber_cell.equals("") ? null : ToolKit.getEnglishIndex(true, taxIdNumber_cell);
        Integer taxIdNumber_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.taxIdNumber.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.taxIdNumber.name()).getInteger("row");
        taxIdNumber_rowIndex = taxIdNumber_rowIndex == null ? null : taxIdNumber_rowIndex-1;

        String faxIdNumber_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.faxIdNumber.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.faxIdNumber.name()).getString("cell");
        Integer faxIdNumber_cellIndex = faxIdNumber_cell.equals("") ? null : ToolKit.getEnglishIndex(true, faxIdNumber_cell);
        Integer faxIdNumber_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.faxIdNumber.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.faxIdNumber.name()).getInteger("row");
        faxIdNumber_rowIndex = faxIdNumber_rowIndex == null ? null : faxIdNumber_rowIndex-1;

        String contactPhone_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPhone.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPhone.name()).getString("cell");
        Integer contactPhone_cellIndex = contactPhone_cell.equals("") ? null : ToolKit.getEnglishIndex(true, contactPhone_cell);
        Integer contactPhone_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPhone.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPhone.name()).getInteger("row");
        contactPhone_rowIndex = contactPhone_rowIndex == null ? null : contactPhone_rowIndex-1;

        String contactPerson_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPerson.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPerson.name()).getString("cell");
        Integer contactPerson_cellIndex = contactPerson_cell.equals("") ? null : ToolKit.getEnglishIndex(true, contactPerson_cell);
        Integer contactPerson_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPerson.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.contactPerson.name()).getInteger("row");
        contactPerson_rowIndex = contactPerson_rowIndex == null ? null : contactPerson_rowIndex-1;

        String address_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.customerAddress.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.customerAddress.name()).getString("cell");
        Integer address_cellIndex = address_cell.equals("") ? null : ToolKit.getEnglishIndex(true, address_cell);
        Integer address_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.customerAddress.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.customerAddress.name()).getInteger("row");
        address_rowIndex = address_rowIndex == null ? null : address_rowIndex-1;

        //  報價單號
        setXSSFSheetCell(sheet,quotationNumber_rowIndex,quotationNumber_cellIndex, exportQuotation_Bean.getOrderNumber());
        // 報價日期
        setXSSFSheetCell(sheet,quotationDate_rowIndex,quotationDate_cellIndex, exportQuotation_Bean.getOrderDate());
        // 專案編號
        setXSSFSheetCell(sheet,projectCode_rowIndex,projectCode_cellIndex, "#" + exportQuotation_Bean.getProjectCode());
        // 專案名稱
        setXSSFSheetCell(sheet,projectName_rowIndex,projectName_cellIndex, exportQuotation_Bean.getProjectName());
        // 送貨地址
        setXSSFSheetCell(sheet,address_rowIndex,address_cellIndex, objectInfo_Bean.getDeliveryAddress());
        // 客戶名稱
        setXSSFSheetCell(sheet,customerName_rowIndex,customerName_cellIndex, objectInfo_Bean.getObjectName());
        // 客戶統編
        setXSSFSheetCell(sheet, taxIdNumber_rowIndex, taxIdNumber_cellIndex, objectInfo_Bean.getTaxIDNumber());
        // 客戶聯絡人
        setXSSFSheetCell(sheet,contactPerson_rowIndex,contactPerson_cellIndex, objectInfo_Bean.getContactPerson());
        // 客戶電話
        setXSSFSheetCell(sheet,contactPhone_rowIndex,contactPhone_cellIndex, objectInfo_Bean.getTelephone1());
        // 客戶傳真
        setXSSFSheetCell(sheet,faxIdNumber_rowIndex,faxIdNumber_cellIndex, objectInfo_Bean.getFax());
    }
    private void setExportCount(){
        String exportCount_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.exportCount.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.exportCount.name()).getString("cell");
        Integer exportCount_cellIndex = exportCount_cell.equals("") ? null : ToolKit.getEnglishIndex(true, exportCount_cell);
        Integer exportCount_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.exportCount.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.exportCount.name()).getInteger("row");
        exportCount_rowIndex = exportCount_rowIndex == null ? null : exportCount_rowIndex-1;

        setXSSFSheetCell(sheet, exportCount_rowIndex, exportCount_cellIndex,"V" + (exportQuotation_Bean.getExportCount()+1));
    }
    private void addRow(int rowIndex,
                       Integer itemIndex,
                       String productName,
                       Integer quantity,
                       String unit,
                       Double batchPrice,
                       Double singlePrice,
                       Double priceAmount,
                       String productRemark,
                       Boolean isAddExportQuotationItem
    ){
        String itemIndex_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.itemIndex.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.itemIndex.name()).getString("cell");
        Integer itemIndex_cellIndex = itemIndex_cell.equals("") ? null : ToolKit.getEnglishIndex(true, itemIndex_cell);

        String productName_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.productName.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.productName.name()).getString("cell");
        Integer productName_cellIndex = productName_cell.equals("") ? null : ToolKit.getEnglishIndex(true, productName_cell);

        String quantity_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.quantity.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.quantity.name()).getString("cell");
        Integer quantity_cellIndex = quantity_cell.equals("") ? null : ToolKit.getEnglishIndex(true, quantity_cell);

        String unit_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.unit.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.unit.name()).getString("cell");
        Integer unit_cellIndex = unit_cell.equals("") ? null : ToolKit.getEnglishIndex(true, unit_cell);

        String singlePrice_cellI = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice.name()).getString("cell");
        Integer singlePrice_cellIndex = singlePrice_cellI.equals("") ? null : ToolKit.getEnglishIndex(true, singlePrice_cellI);

        String singlePrice_priceAmount_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_priceAmount.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_priceAmount.name()).getString("cell");
        Integer singlePrice_priceAmount_cellIndex = singlePrice_priceAmount_cell.equals("") ? null : ToolKit.getEnglishIndex(true, singlePrice_priceAmount_cell);

        String productRemark_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.productRemark.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.productRemark.name()).getString("cell");
        Integer productRemark_cellIndex = productRemark_cell.equals("") ? null : ToolKit.getEnglishIndex(true, productRemark_cell);

        String batchPrice_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice.name()).getString("cell");
        Integer batchPrice_cellIndex = batchPrice_cell.equals("") ? null : ToolKit.getEnglishIndex(true, batchPrice_cell);

        String batchPrice_priceAmount_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_priceAmount.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_priceAmount.name()).getString("cell");
        Integer batchPrice_priceAmount_cellIndex = batchPrice_priceAmount_cell.equals("") ? null : ToolKit.getEnglishIndex(true, batchPrice_priceAmount_cell);

        setXSSFSheetCell(sheet,rowIndex,productName_cellIndex, productName);
        if(productName.equals("以下空白"))
            return;
        setXSSFSheetCell(sheet, rowIndex, itemIndex_cellIndex, itemIndex);
        setXSSFSheetCell(sheet, rowIndex, quantity_cellIndex, quantity);
        setXSSFSheetCell(sheet, rowIndex, unit_cellIndex, unit);
        //	如果是二聯式，價錢都要含稅(*1.05)
        if(exportQuotation_Bean.getExportFormat() == Order_Enum.ExportQuotationFormat.二聯){
            /*singlePrice = (isAddExportQuotationItem != null && isAddExportQuotationItem) ? ToolKit.RoundingInteger(singlePrice * 1.05) : singlePrice * 1.05;
            batchPrice = batchPrice * 1.05;
            priceAmount = priceAmount * 1.05;*/
            singlePrice = (double)ToolKit.RoundingInteger(singlePrice * 1.05);
            batchPrice = (double)ToolKit.RoundingInteger(batchPrice * 1.05);
            priceAmount = singlePrice*quantity;
        }
        setXSSFSheetCell(sheet, rowIndex, singlePrice_cellIndex, singlePrice);
        setXSSFSheetCell(sheet, rowIndex, singlePrice_priceAmount_cellIndex, priceAmount);
        setXSSFSheetCell(sheet, rowIndex, productRemark_cellIndex,productRemark);
        totalPriceIncludeTax = totalPriceIncludeTax + priceAmount;

        if(exportQuotation_Bean.getExportContent() == Order_Enum.ExportQuotationContent.項目明細 && exportQuotation_Bean.isShowBatchPrice()) {
            setXSSFSheetCell(sheet, rowIndex, batchPrice_cellIndex, batchPrice);
            double batchPriceAmount = batchPrice*quantity;
            setXSSFSheetCell(sheet, rowIndex, batchPrice_priceAmount_cellIndex, batchPriceAmount);
        }else if(exportQuotation_Bean.getExportContent() == Order_Enum.ExportQuotationContent.專案){
            setXSSFSheetCell(sheet, rowIndex, batchPrice_cellIndex, batchPrice);
            setXSSFSheetCell(sheet, rowIndex, batchPrice_priceAmount_cellIndex, batchPrice);
            setXSSFSheetCell(sheet, rowIndex, batchPrice_cellIndex, "");
            setXSSFSheetCell(sheet, rowIndex,batchPrice_priceAmount_cellIndex, ToolKit.RoundingInteger(exportQuotation_Bean.getProjectDifferencePrice()));
        }else{
            setXSSFSheetCell(sheet,rowIndex,batchPrice_cellIndex,"------------");
            setXSSFSheetCell(sheet,rowIndex,batchPrice_priceAmount_cellIndex,"------------");
        }
    }
    private void createOrderReference(){
        ObservableList<Order_Bean> orderReferenceList = exportQuotation_Bean.getOrderReferenceList();
        if(orderReferenceList != null && orderReferenceList.size() != 0){
            Integer startRowIndex = templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.itemRange.name()).getInteger("startIndex");

            String orderSource_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.orderReference_orderSource.name()) ?
                    "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.orderReference_orderSource.name()).getString("cell");
            Integer orderSource_cellIndex = orderSource_cell.equals("") ? null : ToolKit.getEnglishIndex(true, orderSource_cell);

            String customerID_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.orderReference_customerID.name()) ?
                    "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.orderReference_customerID.name()).getString("cell");
            Integer customerID_cellIndex = customerID_cell.equals("") ? null : ToolKit.getEnglishIndex(true, customerID_cell);

            String orderNumber_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.orderReference_orderNumber.name()) ?
                    "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.orderReference_orderNumber.name()).getString("cell");
            Integer orderNumber_cellIndex = orderNumber_cell.equals("") ? null : ToolKit.getEnglishIndex(true, orderNumber_cell);

            for(int index = 0; index < orderReferenceList.size(); index++){
                Order_Bean order_Bean = orderReferenceList.get(index);
                setXSSFSheetCell(sheet,index + startRowIndex - 1, orderSource_cellIndex, order_Bean.getOrderSource().name());
                setXSSFSheetCell(sheet,index + startRowIndex - 1, customerID_cellIndex, order_Bean.getObjectID());
                if(order_Bean.getOrderSource() == Order_Enum.OrderSource.報價單 || order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨子貨單 || order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨退貨單)
                    setXSSFSheetCell(sheet,index + startRowIndex - 1, orderNumber_cellIndex, order_Bean.getNowOrderNumber());
                else if(order_Bean.getOrderSource() == Order_Enum.OrderSource.待出貨單)
                    setXSSFSheetCell(sheet,index + startRowIndex - 1, orderNumber_cellIndex, order_Bean.getWaitingOrderNumber());
                else if(order_Bean.getOrderSource() == Order_Enum.OrderSource.出貨單)
                    setXSSFSheetCell(sheet,index + startRowIndex - 1, orderNumber_cellIndex, order_Bean.getAlreadyOrderNumber());
            }
        }
    }
    private void createPriceInfoAndRemark(){
        Integer startRowIndex = templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.itemRange.name()).getInteger("startIndex");
        Integer endRowIndex = templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.itemRange.name()).getInteger("endIndex");

        String singlePrice_chineseFormat_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_chineseFormat.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_chineseFormat.name()).getString("cell");
        Integer singlePrice_chineseFormat_cellIndex = singlePrice_chineseFormat_cell.equals("") ? null : ToolKit.getEnglishIndex(true, singlePrice_chineseFormat_cell);
        Integer singlePrice_chineseFormat_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_chineseFormat.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_chineseFormat.name()).getInteger("row");
        singlePrice_chineseFormat_rowIndex = singlePrice_chineseFormat_rowIndex == null ? null : singlePrice_chineseFormat_rowIndex-1;

        String batchPrice_priceAmount_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_priceAmount.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_priceAmount.name()).getString("cell");

        String batchPrice_totalPriceNoneTax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()).getString("cell");
        Integer batchPrice_totalPriceNoneTax_cellIndex = batchPrice_totalPriceNoneTax_cell.equals("") ? null : ToolKit.getEnglishIndex(true, batchPrice_totalPriceNoneTax_cell);

        String batchPrice_tax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_tax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_tax.name()).getString("cell");
        Integer batchPrice_tax_cellIndex = batchPrice_tax_cell.equals("") ? null : ToolKit.getEnglishIndex(true, batchPrice_tax_cell);

        Integer batchPrice_totalPriceNoneTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()).getInteger("row");
        batchPrice_totalPriceNoneTax_rowIndex = batchPrice_totalPriceNoneTax_rowIndex == null ? null : batchPrice_totalPriceNoneTax_rowIndex-1;

        Integer batchPrice_tax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_tax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_tax.name()).getInteger("row");
        batchPrice_tax_rowIndex = batchPrice_tax_rowIndex == null ? null : batchPrice_tax_rowIndex-1;

        String batchPrice_totalPriceIncludeTax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()).getString("cell");
        Integer batchPrice_totalPriceIncludeTax_cellIndex = batchPrice_totalPriceIncludeTax_cell.equals("") ? null : ToolKit.getEnglishIndex(true, batchPrice_totalPriceIncludeTax_cell);

        Integer batchPrice_totalPriceIncludeTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()).getInteger("row");
        batchPrice_totalPriceIncludeTax_rowIndex = batchPrice_totalPriceIncludeTax_rowIndex == null ? null : batchPrice_totalPriceIncludeTax_rowIndex-1;

        if(exportQuotation_Bean.getExportFormat() == Order_Enum.ExportQuotationFormat.三聯){
            totalPriceIncludeTax = totalPriceIncludeTax * 1.05;
        }
        exportQuotation_Bean.setExport_TotalPriceIncludeTax(ToolKit.RoundingInteger(totalPriceIncludeTax));
        String ChinesePrice = "總計(新台幣)：" + ToolKit.CalculateChinesePrice(ToolKit.RoundingInteger(totalPriceIncludeTax)) + "元整";
        setXSSFSheetCell(sheet, singlePrice_chineseFormat_rowIndex, singlePrice_chineseFormat_cellIndex, ChinesePrice);

        if(!exportQuotation_Bean.isShowBatchPrice()){
            setXSSFSheetCell(sheet, batchPrice_totalPriceIncludeTax_rowIndex, batchPrice_totalPriceIncludeTax_cellIndex,"------------");
            if (exportQuotation_Bean.getExportFormat() == Order_Enum.ExportQuotationFormat.三聯) {
                setXSSFSheetCell(sheet, batchPrice_totalPriceNoneTax_rowIndex, batchPrice_totalPriceNoneTax_cellIndex, "------------");
                setXSSFSheetCell(sheet, batchPrice_tax_rowIndex, batchPrice_tax_cellIndex, "------------");
            }
            return;
        }

        if (exportQuotation_Bean.getExportFormat() == Order_Enum.ExportQuotationFormat.二聯) {
            setXSSFSheetCellFormula(sheet,
                    batchPrice_totalPriceIncludeTax_rowIndex,
                    batchPrice_totalPriceIncludeTax_cellIndex,
                    "SUM(" + batchPrice_priceAmount_cell + startRowIndex + ":" + batchPrice_priceAmount_cell + endRowIndex + ")");
        }else if (exportQuotation_Bean.getExportFormat() == Order_Enum.ExportQuotationFormat.三聯) {
            //	成本總金額(未稅)
            setXSSFSheetCellFormula(sheet,
                    batchPrice_totalPriceNoneTax_rowIndex,
                    batchPrice_totalPriceNoneTax_cellIndex,
                    "SUM(" + batchPrice_priceAmount_cell + startRowIndex + ":" + batchPrice_priceAmount_cell + endRowIndex + ")");
            if(batchPrice_totalPriceNoneTax_rowIndex != null){
                //	成本稅價
                setXSSFSheetCellFormula(sheet,
                        batchPrice_tax_rowIndex,
                        batchPrice_tax_cellIndex,
                        batchPrice_totalPriceNoneTax_cell + (batchPrice_totalPriceNoneTax_rowIndex + 1) + "*0.05");
                if(batchPrice_tax_rowIndex != null){
                    //  成本總金額(含稅)
                    setXSSFSheetCellFormula(sheet,
                            batchPrice_totalPriceIncludeTax_rowIndex,
                            batchPrice_totalPriceIncludeTax_cellIndex,
                            "SUM(" + batchPrice_totalPriceNoneTax_cell + (batchPrice_totalPriceNoneTax_rowIndex + 1) + ":" + batchPrice_tax_cell + (batchPrice_tax_rowIndex+1) + ")");
                }
            }
        }
    }
    private void createProfit(){
        String singlePrice_totalPriceIncludeTax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceIncludeTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceIncludeTax.name()).getString("cell");
        Integer singlePrice_totalPriceIncludeTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceIncludeTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceIncludeTax.name()).getInteger("row");
        singlePrice_totalPriceIncludeTax_rowIndex = singlePrice_totalPriceIncludeTax_rowIndex == null ? null : singlePrice_totalPriceIncludeTax_rowIndex-1;

        String singlePrice_totalPriceNoneTax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax.name()).getString("cell");
        Integer singlePrice_totalPriceNoneTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.singlePrice_totalPriceNoneTax.name()).getInteger("row");
        singlePrice_totalPriceNoneTax_rowIndex = singlePrice_totalPriceNoneTax_rowIndex == null ? null : singlePrice_totalPriceNoneTax_rowIndex-1;

        String profit_noneTax_Cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_noneTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_noneTax.name()).getString("cell");
        Integer profit_noneTax_cellIndex = profit_noneTax_Cell.equals("") ? null : ToolKit.getEnglishIndex(true, profit_noneTax_Cell);
        Integer profit_noneTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_noneTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_noneTax.name()).getInteger("row");
        profit_noneTax_rowIndex = profit_noneTax_rowIndex == null ? null : profit_noneTax_rowIndex-1;

        String profit_includeTax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_includeTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_includeTax.name()).getString("cell");
        Integer profit_includeTax_cellIndex = profit_includeTax_cell.equals("") ? null : ToolKit.getEnglishIndex(true, profit_includeTax_cell);
        Integer profit_includeTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_includeTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.profit_includeTax.name()).getInteger("row");
        profit_includeTax_rowIndex = profit_includeTax_rowIndex == null ? null : profit_includeTax_rowIndex-1;

        String batchPrice_totalPriceNoneTax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()).getString("cell");
        Integer batchPrice_totalPriceNoneTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceNoneTax.name()).getInteger("row");
        batchPrice_totalPriceNoneTax_rowIndex = batchPrice_totalPriceNoneTax_rowIndex == null ? null : batchPrice_totalPriceNoneTax_rowIndex-1;

        String batchPrice_totalPriceIncludeTax_cell = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()) ?
                "" : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()).getString("cell");
        Integer batchPrice_totalPriceIncludeTax_rowIndex = !templateFormat.containsKey(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()) ?
                null : templateFormat.getJSONObject(SystemSetting_Enum.ExportQuotationTemplateTitle.batchPrice_totalPriceIncludeTax.name()).getInteger("row");
        batchPrice_totalPriceIncludeTax_rowIndex = batchPrice_totalPriceIncludeTax_rowIndex == null ? null : batchPrice_totalPriceIncludeTax_rowIndex-1;

        if(!exportQuotation_Bean.isShowProfit()){
            setXSSFSheetCell(sheet, profit_includeTax_rowIndex, profit_includeTax_cellIndex,"------------");
            setXSSFSheetCell(sheet, profit_noneTax_rowIndex, profit_noneTax_cellIndex,"------------");
            return;
        }

        //	利潤(含稅)
        if(singlePrice_totalPriceIncludeTax_rowIndex == null || batchPrice_totalPriceIncludeTax_rowIndex == null){
            DialogUI.AlarmDialog("利潤(含稅)相關格式未設定：\n(1) 單價-總計(含稅)\n(2) 成本-總計(含稅)");
            return;
        }else{
            setXSSFSheetCellFormula(sheet,
                    profit_includeTax_rowIndex,
                    profit_includeTax_cellIndex,
                    singlePrice_totalPriceIncludeTax_cell + (singlePrice_totalPriceIncludeTax_rowIndex+1) + "-" + batchPrice_totalPriceIncludeTax_cell + (batchPrice_totalPriceIncludeTax_rowIndex+1));
        }
        //	利潤(未稅)
        if(exportQuotation_Bean.getExportFormat() == Order_Enum.ExportQuotationFormat.二聯) {
            setXSSFSheetCellFormula(sheet,
                    profit_noneTax_rowIndex,
                    profit_noneTax_cellIndex,
                    "ROUND(ABS(" + singlePrice_totalPriceIncludeTax_cell + (singlePrice_totalPriceIncludeTax_rowIndex + 1) + "/1.05),0)-ROUND(ABS(" + batchPrice_totalPriceIncludeTax_cell + (batchPrice_totalPriceIncludeTax_rowIndex + 1) + "/1.05),0)");
        }else if(exportQuotation_Bean.getExportFormat() == Order_Enum.ExportQuotationFormat.三聯) {
            if(singlePrice_totalPriceNoneTax_rowIndex == null || batchPrice_totalPriceNoneTax_rowIndex == null){
                DialogUI.AlarmDialog("利潤(未稅)相關格式未設定：\n(1) 單價-小計\n(2) 成本-小計");
                return;
            }
            setXSSFSheetCellFormula(sheet,
                    profit_noneTax_rowIndex,
                    profit_noneTax_cellIndex,
                    singlePrice_totalPriceNoneTax_cell + (singlePrice_totalPriceNoneTax_rowIndex+1) + "-" + batchPrice_totalPriceNoneTax_cell + (batchPrice_totalPriceNoneTax_rowIndex+1));
        }
    }
    private XSSFCellStyle createStyle(XSSFWorkbook Workbook, int fontSize){
        XSSFFont font = Workbook.createFont();
        font.setFontHeightInPoints((short) fontSize);
        font.setFontName("新細明體");

        XSSFCellStyle style = Workbook.createCellStyle();
        style.setFont(font);
        return style;
    }
    public double getTotalPriceIncludeTax(){
        return totalPriceIncludeTax;
    }
    public File build(String filePath) throws IOException {
        sheet.setForceFormulaRecalculation(true);
        createOrderReference();
        createPriceInfoAndRemark();
        createProfit();
        return super.build(filePath);
    }
}
