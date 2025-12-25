package ERP.Bean.Order;

import ERP.Bean.ToolKit.Installment.Installment_Bean;
import ERP.Bean.ToolKit.ProductGroup.ItemGroup_Bean;
import ERP.Bean.ToolKit.ProductGroup.ProductGroup_Bean;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.EstablishSource;
import ERP.Enum.Order.Order_Enum.OrderBorrowed;
import ERP.Enum.Order.Order_Enum.CheckoutStatus;
import ERP.Enum.Order.Order_Enum.OffsetOrderStatus;
import ERP.Enum.Order.Order_Enum.OrderTaxStatus;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/** [ERP.Bean] Order info */
public class Order_Bean {
    private Integer OrderID;
    private String NowOrderNumber, OldOrderNumber, OrderDate, ObjectID, ObjectName, NumberOfItems, TotalPriceNoneTax, Tax, Discount, TotalPriceIncludeTax, ProductGroupTotalPriceNoneTax, ProductGroupTax, ProductGroupDiscount, ProductGroupTotalPriceIncludeTax,
            InvoiceDate, InvoiceNumber, ProjectCode,ProjectName, ProjectQuantity, ProjectUnit, ProjectPriceAmount, ProjectTotalPriceNoneTax, ProjectTax, ProjectTotalPriceIncludeTax, ProjectDifferentPrice, PurchaserName, PurchaserTelephone, PurchaserCellphone, PurchaserAddress, RecipientName, RecipientTelephone, RecipientCellphone, RecipientAddress, OrderRemark, CashierRemark;
    private EstablishSource EstablishSource;    //  報價單使用(系統建立、人工建立)
    private Order_Enum.OrderSource OrderSource;
    private Order_Enum.OrderStatus orderStatus;
    private Order_Enum.OrderObject OrderObject;
    private Order_Enum.CheckoutStatus isCheckout;   //  出貨、進貨單使用(結帳與否)
    private Order_Enum.OrderBorrowed isBorrowed;
    private Order_Enum.OffsetOrderStatus offsetOrderStatus;
    private Order_Enum.OrderTaxStatus OrderTaxStatus;
    private boolean isExistPicture;
    private ArrayList<String> OrderPictureList;
    private boolean isExistInstallment;
    private ArrayList<Installment_Bean> installmentList;
    private ObservableList<OrderProduct_Bean> ProductList;
    private HashMap<Integer,HashMap<ProductGroup_Bean,LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap;

    private boolean isExistOrderReference;
    private HashMap<Order_Enum.OrderSource, HashMap<Integer,Boolean>> orderReferenceMap;
    private String WaitingOrderDate, WaitingOrderNumber, AlreadyOrderDate, AlreadyOrderNumber;
    private String invoice_exportQuotation_Account;
    private String UpdateDateTime;

    private Integer exportQuotation_ManufacturerId;

    private HashMap<Order_Enum.ReviewObject, Order_Enum.ReviewStatus> reviewStatusMap = new HashMap<>();

    public Integer getOrderID() {   return OrderID; }
    public void setOrderID(Integer orderID) {
        OrderID = orderID;
    }

    public String getNowOrderNumber() {    return NowOrderNumber; }
    public void setNowOrderNumber(String OrderNumber) {    this.NowOrderNumber = OrderNumber;  }

    public String getOldOrderNumber() {    return OldOrderNumber; }
    public void setOldOrderNumber(String OldOrderNumber) {    this.OldOrderNumber = OldOrderNumber;  }

    public String getOrderDate() {  return OrderDate;   }
    public void setOrderDate(String OrderDate) {    this.OrderDate = OrderDate;  }


    public Order_Enum.OrderObject getOrderObject() {
        return OrderObject;
    }

    public void setOrderObject(Order_Enum.OrderObject orderObject) {
        OrderObject = orderObject;
    }

    public Order_Enum.OrderSource getOrderSource(){    return OrderSource; }
    public void setOrderSource(Order_Enum.OrderSource OrderSource){   this.OrderSource = OrderSource;  }

    public Order_Enum.OrderStatus getOrderStatus() {    return orderStatus; }
    public void setOrderStatus(Order_Enum.OrderStatus orderStatus) {    this.orderStatus = orderStatus; }

    public String getObjectID() {   return ObjectID;    }
    public void setObjectID(String ObjectID) {  this.ObjectID = ObjectID;    }

    public String getObjectName() { return ObjectName;  }
    public void setObjectName(String ObjectName) {  this.ObjectName = ObjectName;    }

    public CheckoutStatus isCheckout(){   return isCheckout;  }
    public void setIsCheckout(boolean isCheckout){
        if(isCheckout)      this.isCheckout = CheckoutStatus.已結帳;
        else    this.isCheckout = CheckoutStatus.未結帳;
    }

    public String getTotalPriceNoneTax() {  return TotalPriceNoneTax;   }
    public void setTotalPriceNoneTax(String TotalPriceNoneTax) {    this.TotalPriceNoneTax = TotalPriceNoneTax;  }

    public String getTax() {    return Tax; }
    public void setTax(String Tax) {    this.Tax = Tax;  }

    public String getDiscount() {   return Discount;    }
    public void setDiscount(String Discount) {  this.Discount = Discount;    }

    public String getTotalPriceIncludeTax() {   return TotalPriceIncludeTax;    }
    public void setTotalPriceIncludeTax(String TotalPriceIncludeTax) {  this.TotalPriceIncludeTax = TotalPriceIncludeTax;    }

    public String getProductGroupTotalPriceNoneTax() {  return ProductGroupTotalPriceNoneTax;   }
    public void setProductGroupTotalPriceNoneTax(String productGroupTotalPriceNoneTax) {    ProductGroupTotalPriceNoneTax = productGroupTotalPriceNoneTax;  }

    public String getProductGroupTax() {    return ProductGroupTax; }
    public void setProductGroupTax(String productGroupTax) {    ProductGroupTax = productGroupTax;  }

    public String getProductGroupDiscount() {   return ProductGroupDiscount;    }
    public void setProductGroupDiscount(String productGroupDiscount) {  ProductGroupDiscount = productGroupDiscount;    }

    public String getProductGroupTotalPriceIncludeTax() {   return ProductGroupTotalPriceIncludeTax;    }
    public void setProductGroupTotalPriceIncludeTax(String productGroupTotalPriceIncludeTax) {  ProductGroupTotalPriceIncludeTax = productGroupTotalPriceIncludeTax;    }

    public String getInvoiceDate(){ return InvoiceDate; }
    public void setInvoiceDate(String InvoiceDate){ this.InvoiceDate = InvoiceDate; }

    public String getInvoiceNumber(){ return InvoiceNumber; }
    public void setInvoiceNumber(String InvoiceNumber){ this.InvoiceNumber = InvoiceNumber; }

    public String getInvoice_exportQuotation_Account() {   return invoice_exportQuotation_Account; }
    public void setInvoice_exportQuotation_Account(String invoice_exportQuotation_Account) {   this.invoice_exportQuotation_Account = invoice_exportQuotation_Account; }

    public EstablishSource getEstablishSource() {    return EstablishSource; }
    public void setEstablishSource(EstablishSource EstablishSource) {    this.EstablishSource = EstablishSource;  }

    public String getProjectCode() {    return ProjectCode; }
    public void setProjectCode(String projectCode) {    ProjectCode = projectCode;  }

    public String getProjectName() {    return ProjectName; }
    public void setProjectName(String ProjectName) {    this.ProjectName = ProjectName;  }

    public String getProjectQuantity() {    return ProjectQuantity; }
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

    public String getProjectDifferentPrice(){    return ProjectDifferentPrice;    }
    public void setProjectDifferentPrice(String ProjectDifferentPrice){  this.ProjectDifferentPrice = ProjectDifferentPrice;    }

    public String getPurchaserName() {  return PurchaserName;   }
    public void setPurchaserName(String PurchaserName) {    this.PurchaserName = PurchaserName;  }

    public String getPurchaserTelephone() { return PurchaserTelephone;  }
    public void setPurchaserTelephone(String PurchaserTelephone) {  this.PurchaserTelephone = PurchaserTelephone;    }

    public String getPurchaserCellphone(){  return PurchaserCellphone;  }
    public void setPurchaserCellphone(String PurchaserCellphone){   this.PurchaserCellphone = PurchaserCellphone;   }

    public String getPurchaserAddress() {   return PurchaserAddress;    }
    public void setPurchaserAddress(String PurchaserAddress) {  this.PurchaserAddress = PurchaserAddress;    }

    public String getRecipientName() {  return RecipientName;   }
    public void setRecipientName(String RecipientName) {    this.RecipientName = RecipientName;  }

    public String getRecipientTelephone() { return RecipientTelephone;  }
    public void setRecipientTelephone(String RecipientTelephone) {  this.RecipientTelephone = RecipientTelephone;    }

    public String getRecipientCellphone(){  return RecipientCellphone;  }
    public void setRecipientCellphone(String RecipientCellphone){   this.RecipientCellphone = RecipientCellphone;   }

    public String getRecipientAddress() {   return RecipientAddress;    }
    public void setRecipientAddress(String RecipientAddress) {  this.RecipientAddress = RecipientAddress;    }

    public String getNumberOfItems() { return NumberOfItems;   }
    public void setNumberOfItems(String NumberOfItems) {   this.NumberOfItems = NumberOfItems;  }

    public OrderBorrowed isBorrowed() { return isBorrowed;  }
    public void setIsBorrowed(boolean isBorrowed) {
        if(isBorrowed)      this.isBorrowed = OrderBorrowed.已借測;
        else    this.isBorrowed = OrderBorrowed.未借測;
    }

    public boolean isOffset(){    return offsetOrderStatus.value();   }
    public OffsetOrderStatus getOffsetOrderStatus(){    return offsetOrderStatus;   }
    public void setIsOffset(OffsetOrderStatus offsetOrderStatus){
        this.offsetOrderStatus =  offsetOrderStatus;
    }

    public void setOrderTaxStatus(OrderTaxStatus OrderTaxStatus){ this.OrderTaxStatus = OrderTaxStatus;   }
    public OrderTaxStatus getOrderTaxStatus(){  return OrderTaxStatus;  }

    public boolean isExistPicture() {   return isExistPicture;  }
    public void setExistPicture(boolean existPicture) { isExistPicture = existPicture;  }

    public ArrayList<String> getOrderPictureList() {    return OrderPictureList;    }
    public void setOrderPictureList(ArrayList<String> OrderPictureList) {   this.OrderPictureList = OrderPictureList;    }

    public boolean isExistInstallment() {   return isExistInstallment;  }
    public void setExistInstallment(boolean existInstallment) { isExistInstallment = existInstallment;  }

    public ArrayList<Installment_Bean> getInstallmentList() {   return installmentList; }
    public void setInstallmentList(ArrayList<Installment_Bean> installmentList) {   this.installmentList = installmentList; }

    public ObservableList<OrderProduct_Bean> getProductList() {  return ProductList; }
    public void setProductList(ObservableList<OrderProduct_Bean> ProductList) {  this.ProductList = ProductList;  }

    public HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> getProductGroupMap() {   return productGroupMap; }
    public void setProductGroupMap(HashMap<Integer, HashMap<ProductGroup_Bean, LinkedHashMap<Integer, ItemGroup_Bean>>> productGroupMap) {   this.productGroupMap = productGroupMap; }

    public boolean isExistOrderReference() {    return isExistOrderReference;   }
    public void setExistOrderReference(boolean existOrderReference) {   isExistOrderReference = existOrderReference;    }

    public HashMap<Order_Enum.OrderSource, HashMap<Integer, Boolean>> getOrderReferenceMap() {  return orderReferenceMap;   }
    public void setOrderReferenceMap(HashMap<Order_Enum.OrderSource, HashMap<Integer, Boolean>> orderReferenceMap) {    this.orderReferenceMap = orderReferenceMap; }

    public String getWaitingOrderDate() {    return WaitingOrderDate; }
    public void setWaitingOrderDate(String WaitingOrderDate) {    this.WaitingOrderDate = WaitingOrderDate;  }

    public String getWaitingOrderNumber() { return WaitingOrderNumber;  }
    public void setWaitingOrderNumber(String WaitingOrderNumber) {  this.WaitingOrderNumber = WaitingOrderNumber;    }

    public String getAlreadyOrderDate() {  return AlreadyOrderDate;   }
    public void setAlreadyOrderDate(String AlreadyOrderDate) {    this.AlreadyOrderDate = AlreadyOrderDate;  }

    public String getAlreadyOrderNumber() {  return AlreadyOrderNumber;   }
    public void setAlreadyOrderNumber(String AlreadyOrderNumber) {    this.AlreadyOrderNumber = AlreadyOrderNumber;  }

    public String getUpdateDateTime() { return UpdateDateTime;  }

    public void setUpdateDateTime(String updateDateTime) {  UpdateDateTime = updateDateTime;    }

    public String getOrderRemark() {    return OrderRemark; }
    public void setOrderRemark(String orderRemark) {    this.OrderRemark = orderRemark; }

    public String getCashierRemark() {  return CashierRemark;   }
    public void setCashierRemark(String cashierRemark) {    CashierRemark = cashierRemark;  }

    public Integer getExportQuotation_ManufacturerId() {    return exportQuotation_ManufacturerId;  }
    public void setExportQuotation_ManufacturerId(Integer exportQuotation_ManufacturerId) { this.exportQuotation_ManufacturerId = exportQuotation_ManufacturerId;   }

    public HashMap<Order_Enum.ReviewObject, Order_Enum.ReviewStatus> getReviewStatusMap() { return reviewStatusMap; }
    public void setReviewStatusMap(Order_Enum.ReviewObject ReviewObject, Order_Enum.ReviewStatus ReviewStatus) {    this.reviewStatusMap.put(ReviewObject, ReviewStatus);   }
}
