package ERP.Bean.Order;

import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.ExportQuotationContent;
import ERP.Enum.Order.Order_Enum.ExportQuotationFormat;
import ERP.Enum.Order.Order_Enum.OrderSource;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.LinkedHashMap;

/** [ERP.Bean] Export quotation to excel */
public class ExportQuotation_Bean {

    private String OrderNumber, OrderDate, CustomerID, ProjectCode, ProjectName ,ProjectQuantity, ProjectUnit, ProjectPriceAmount, ProjectTotalPriceNoneTax, ProjectTax, ProjectTotalPriceIncludeTax, ProjectDifferencePrice, orderRemark;
    private String vendorNickName;

    private boolean ShowBatchPrice, ShowProfit;
    private ExportQuotationContent ExportContent;
    private ExportQuotationFormat ExportFormat;
    private OrderSource OrderSource;
    private ObservableList<OrderProduct_Bean> ProductList;
    private ObservableList<Order_Bean> orderReferenceList;

    private JSONObject templateFormat;

    private int export_TotalPriceIncludeTax;
    private int exportCount;

    private boolean isExportGroup;
    private HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroup;

    public String getOrderNumber() {    return OrderNumber; }
    public void setOrderNumber(String OrderNumber) {    this.OrderNumber = OrderNumber;  }
    public String getOrderDate() {  return OrderDate;   }
    public void setOrderDate(String orderDate) {    OrderDate = orderDate;  }
    public String getCustomerID() { return CustomerID;  }
    public void setCustomerID(String CustomerID) {  this.CustomerID = CustomerID;    }
    public boolean isShowBatchPrice() { return ShowBatchPrice;  }
    public void setShowBatchPrice(boolean ShowBatchPrice) { this.ShowBatchPrice = ShowBatchPrice;    }
    public boolean isShowProfit() { return ShowProfit;  }
    public void setShowProfit(boolean ShowProfit) { this.ShowProfit = ShowProfit;    }
    public ExportQuotationContent getExportContent() {  return ExportContent; }
    public void setExportContent(ExportQuotationContent ExportContent) {    this.ExportContent = ExportContent;  }
    public ExportQuotationFormat getExportFormat() {    return ExportFormat;    }
    public void setExportFormat(ExportQuotationFormat ExportFormat) {   this.ExportFormat = ExportFormat;    }
    public String getVendorNickName() { return vendorNickName;  }
    public void setVendorNickName(String vendorNickName) {  this.vendorNickName = vendorNickName;   }
    public Order_Enum.OrderSource getOrderSource() {    return OrderSource; }
    public void setOrderSource(Order_Enum.OrderSource OrderSource) {    this.OrderSource = OrderSource;  }
    public ObservableList<OrderProduct_Bean> getProductList() {  return ProductList; }
    public void setProductList(ObservableList<OrderProduct_Bean> ProductList) {  this.ProductList = ProductList; }

    public String getProjectCode() {    return ProjectCode; }
    public void setProjectCode(String ProjectCode) {    this.ProjectCode = ProjectCode;  }
    public String getProjectName() {    return ProjectName; }
    public void setProjectName(String ProjectName) {    this.ProjectName = ProjectName;  }
    public String getProjectQuantity() {  return ProjectQuantity;   }
    public void setProjectQuantity(String ProjectQuantity) {    this.ProjectQuantity = ProjectQuantity;  }
    public String getProjectUnit() {    return ProjectUnit; }
    public void setProjectUnit(String ProjectUnit) {    this.ProjectUnit = ProjectUnit;  }
    public String getProjectPriceAmount() { return ProjectPriceAmount;  }
    public void setProjectPriceAmount(String ProjectPriceAmount) {  this.ProjectPriceAmount = ProjectPriceAmount;    }
    public String getProjectTotalPriceNoneTax() {   return ProjectTotalPriceNoneTax;    }
    public void setProjectTotalPriceNoneTax(String ProjectTotalPriceNoneTax) {  this.ProjectTotalPriceNoneTax = ProjectTotalPriceNoneTax;    }
    public String getProjectTax() { return ProjectTax;  }
    public void setProjectTax(String ProjectTax) {  this.ProjectTax = ProjectTax;    }
    public String getProjectTotalPriceIncludeTax() {    return ProjectTotalPriceIncludeTax; }
    public void setProjectTotalPriceIncludeTax(String ProjectTotalPriceIncludeTax) {    this.ProjectTotalPriceIncludeTax = ProjectTotalPriceIncludeTax;  }
    public String getProjectDifferencePrice() { return ProjectDifferencePrice;  }
    public void setProjectDifferencePrice(String projectDifferencePrice) {  ProjectDifferencePrice = projectDifferencePrice;    }
    public ObservableList<Order_Bean> getOrderReferenceList() {    return orderReferenceList;  }
    public void setOrderReferenceList(ObservableList<Order_Bean> orderReferenceList) { this.orderReferenceList = orderReferenceList;   }
    public String getOrderRemark() {    return orderRemark; }
    public void setOrderRemark(String orderRemark) {    this.orderRemark = orderRemark; }

    public JSONObject getTemplateFormat() { return templateFormat;  }
    public void setTemplateFormat(JSONObject templateFormat) {  this.templateFormat = templateFormat;   }

    public int getExport_TotalPriceIncludeTax() {    return export_TotalPriceIncludeTax; }
    public void setExport_TotalPriceIncludeTax(int export_TotalPriceIncludeTax) {    this.export_TotalPriceIncludeTax = export_TotalPriceIncludeTax; }

    public int getExportCount() {   return exportCount; }
    public void setExportCount(int exportCount) {   this.exportCount = exportCount; }

    public boolean isExportGroup() {    return isExportGroup;   }
    public void setExportGroup(boolean exportGroup) {   isExportGroup = exportGroup;    }

    public HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> getProductGroup() { return productGroup;    }
    public void setProductGroup(HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroup) {    this.productGroup = productGroup;   }
}
