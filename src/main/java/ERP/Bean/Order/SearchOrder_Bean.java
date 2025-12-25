package ERP.Bean.Order;

import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.EstablishSource;
import ERP.Enum.Order.Order_Enum.ReviewStatus;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/** [ERP.Bean] Search order info
 * 以下參數為 應收應付帳總表及明細 使用
 * ObjectID
 * ObjectNickName
 * OrderDate
 * OrderNumber
 * TotalPriceNoneTax
 * Tax, Discount
 * TotalPriceIncludeTax
 * PayableReceivableDiscount
 */
public class SearchOrder_Bean {
    private int id;
    private SimpleIntegerProperty serialNumber;
    private SimpleStringProperty OrderNumber, OrderDate, OrderObject, ObjectID, ObjectName, PersonInCharge, InvoiceNumber, ProjectCode, ProjectName, ReviewStatus, CashierRemark, ProjectTotalPriceIncludeTax, ProjectDifferentPrice, balance, isCheckout, isOffset, QuotationDate, QuotationNumber, WaitingOrderDate, WaitingOrderNumber, AlreadyOrderDate, AlreadyOrderNumber;
    private Integer ProductGroupTotalPriceNoneTax, ProductGroupTax, ProductGroupDiscount, ProductGroupTotalPriceIncludeTax;

    private SimpleIntegerProperty NumberOfItems;
    private SimpleStringProperty TotalPriceIncludeTax;

    private SimpleStringProperty OrderSource;
    private OrderStatus orderStatus;
    private SimpleStringProperty ObjectNickName;
    private SimpleIntegerProperty TotalPriceNoneTax, Tax, Discount;

    private SimpleObjectProperty<Integer> installment, pictureCount;

    private SimpleFloatProperty PayableReceivableDiscount;
    private SimpleStringProperty EstablishSource;

    private int invoice_manufacturerNickName_id;
    private SimpleStringProperty invoiceManufacturerNickName;

    private SimpleStringProperty ExportQuotationManufacturerNickName;

    private ReviewStatus itemReviewStatus, groupReviewStatus;

    private Boolean CheckBoxSelect;
    private Integer bindingIAECrawlerPayment_id;
    private Button Button;

    public SearchOrder_Bean(){
        CheckBoxSelect = true;
        serialNumber = new SimpleIntegerProperty();
        this.OrderSource = new SimpleStringProperty();
        this.OrderNumber = new SimpleStringProperty();
        this.OrderDate = new SimpleStringProperty();
        this.OrderObject = new SimpleStringProperty();
        this.EstablishSource = new SimpleStringProperty();
        this.ObjectID = new SimpleStringProperty();
        this.ObjectName = new SimpleStringProperty();
        this.PersonInCharge = new SimpleStringProperty();
        this.NumberOfItems = new SimpleIntegerProperty();
        this.TotalPriceIncludeTax = new SimpleStringProperty();
        this.InvoiceNumber = new SimpleStringProperty();
        this.ProjectCode = new SimpleStringProperty();
        this.ProjectName = new SimpleStringProperty();
        this.ReviewStatus = new SimpleStringProperty();
        this.CashierRemark = new SimpleStringProperty();
        this.ProjectTotalPriceIncludeTax = new SimpleStringProperty();
        this.ProjectDifferentPrice = new SimpleStringProperty();
        this.balance = new SimpleStringProperty();
        this.isCheckout = new SimpleStringProperty();
        this.isOffset = new SimpleStringProperty();
        this.QuotationDate = new SimpleStringProperty();
        this.QuotationNumber = new SimpleStringProperty();
        this.WaitingOrderDate = new SimpleStringProperty();
        this.WaitingOrderNumber = new SimpleStringProperty();
        this.AlreadyOrderDate = new SimpleStringProperty();
        this.AlreadyOrderNumber = new SimpleStringProperty();

        this.ObjectNickName = new SimpleStringProperty();
        this.TotalPriceNoneTax = new SimpleIntegerProperty();
        this.Tax = new SimpleIntegerProperty();
        this.Discount = new SimpleIntegerProperty();
        this.PayableReceivableDiscount = new SimpleFloatProperty();
        Button = new Button();
        this.invoiceManufacturerNickName = new SimpleStringProperty();
        this.ExportQuotationManufacturerNickName = new SimpleStringProperty();
        this.installment = new SimpleObjectProperty<>();
        this.pictureCount = new SimpleObjectProperty<>();
    }

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public Boolean isCheckBoxSelect() {    return CheckBoxSelect;    }
    public void setCheckBoxSelect(Boolean CheckBoxSelect) {   this.CheckBoxSelect = CheckBoxSelect;    }

    public int getSerialNumber() {  return serialNumber.get();  }
    public SimpleIntegerProperty serialNumberProperty() {   return serialNumber;    }
    public void setSerialNumber(int serialNumber) { this.serialNumber.set(serialNumber);    }

    public String getOrderNumber() {    return OrderNumber.get();   }
    public SimpleStringProperty OrderNumberProperty() { return OrderNumber; }
    public void setOrderNumber(String OrderNumber) {    this.OrderNumber.set(OrderNumber);  }

    public String getOrderDate() {   return OrderDate.get();  }
    public SimpleStringProperty OrderDateProperty() {    return OrderDate;    }
    public void setOrderDate(String OrderDate) {  this.OrderDate.set(OrderDate);    }

    public Order_Enum.OrderObject getOrderObject() {   return Order_Enum.OrderObject.valueOf(OrderObject.get());  }
    public SimpleStringProperty OrderObjectProperty() {    return OrderObject;    }
    public void setOrderObject(Order_Enum.OrderObject OrderObject) {  this.OrderObject.set(OrderObject.name());    }

    public String getObjectID() {   return ObjectID.get();  }
    public SimpleStringProperty ObjectIDProperty() {    return ObjectID;    }
    public void setObjectID(String ObjectID) {  this.ObjectID.set(ObjectID);    }

    public String getObjectName() { return ObjectName.get();    }
    public SimpleStringProperty ObjectNameProperty() {  return ObjectName;  }
    public void setObjectName(String ObjectName) {  this.ObjectName.set(ObjectName);    }

    public String getPersonInCharge() { return PersonInCharge.get();    }
    public SimpleStringProperty PersonInChargeProperty() {  return PersonInCharge;  }
    public void setPersonInCharge(String PersonInCharge) {  this.PersonInCharge.set(PersonInCharge);    }

    public int getNumberOfItems() { return NumberOfItems.get(); }
    public SimpleIntegerProperty NumberOfItemsProperty() {  return NumberOfItems;   }
    public void setNumberOfItems(int NumberOfItems) {   this.NumberOfItems.set(NumberOfItems);  }

    public int getTotalPriceIncludeTax() {    return Integer.parseInt(TotalPriceIncludeTax.get());    }
    public SimpleStringProperty TotalPriceIncludeTaxProperty() { return TotalPriceIncludeTax;  }
    public void setTotalPriceIncludeTax(int TotalPriceIncludeTax) { this.TotalPriceIncludeTax.set(String.valueOf(TotalPriceIncludeTax));    }

    public String getInvoiceNumber(){ return InvoiceNumber.get();   }
    public SimpleStringProperty InvoiceNumberProperty() {  return InvoiceNumber;  }
    public void setInvoiceNumber(String InvoiceNumber){ this.InvoiceNumber.set(InvoiceNumber);}

    public int getInvoice_manufacturerNickName_id() {   return invoice_manufacturerNickName_id; }
    public void setInvoice_manufacturerNickName_id(int invoice_manufacturerNickName_id) {   this.invoice_manufacturerNickName_id = invoice_manufacturerNickName_id; }

    public String getInvoiceManufacturerNickName() {   return invoiceManufacturerNickName.get();  }
    public SimpleStringProperty invoiceManufacturerNickNameProperty() {    return invoiceManufacturerNickName;    }
    public void setInvoiceManufacturerNickName(String invoiceManufacturerNickName) {  this.invoiceManufacturerNickName.set(invoiceManufacturerNickName);    }

    public String getExportQuotationManufacturerNickName() {   return ExportQuotationManufacturerNickName.get();  }
    public SimpleStringProperty ExportQuotation_ManufacturerNickNameProperty() {    return ExportQuotationManufacturerNickName;    }
    public void setExportQuotationManufacturerNickName(String exportQuotationManufacturerNickName) {  this.ExportQuotationManufacturerNickName.set(exportQuotationManufacturerNickName);    }

    public String getProjectCode() {    return ProjectCode.get();   }
    public SimpleStringProperty ProjectCodeProperty() { return ProjectCode; }
    public void setProjectCode(String projectCode) {    this.ProjectCode.set(projectCode);  }

    public String getProjectName() {    return ProjectName.get();   }
    public SimpleStringProperty ProjectNameProperty() { return ProjectName; }
    public void setProjectName(String ProjectName) {    this.ProjectName.set(ProjectName);  }

    public Order_Enum.ReviewStatus getItemReviewStatus() {  return this.itemReviewStatus;  }
    public Order_Enum.ReviewStatus getGroupReviewStatus() {  return this.groupReviewStatus;  }
    public SimpleStringProperty ReviewStatusProperty() {    return ReviewStatus;    }
    public void setReviewStatus(ReviewStatus itemReviewStatus, ReviewStatus groupReviewStatus) {
        this.itemReviewStatus = itemReviewStatus;
        this.groupReviewStatus = groupReviewStatus;
        if(itemReviewStatus == Order_Enum.ReviewStatus.無 && groupReviewStatus != Order_Enum.ReviewStatus.無){
            this.ReviewStatus.set(groupReviewStatus.name() + "(群)");
        }else if(itemReviewStatus != Order_Enum.ReviewStatus.無){
            this.ReviewStatus.set(itemReviewStatus.name());
        }
    }

    public String getCashierRemark() {  return CashierRemark.get(); }
    public SimpleStringProperty CashierRemarkProperty() {   return CashierRemark;   }
    public void setCashierRemark(String cashierRemark) {    this.CashierRemark.set(cashierRemark);  }

    public String getProjectTotalPriceIncludeTax() {    return ProjectTotalPriceIncludeTax.get();   }
    public SimpleStringProperty ProjectTotalPriceIncludeTaxProperty() { return ProjectTotalPriceIncludeTax; }
    public void setProjectTotalPriceIncludeTax(String ProjectTotalPriceIncludeTax) {    this.ProjectTotalPriceIncludeTax.set(ProjectTotalPriceIncludeTax);  }

    public String getProjectDifferentPrice() {  return ProjectDifferentPrice.get(); }
    public SimpleStringProperty ProjectDifferentPriceProperty() {   return ProjectDifferentPrice;   }
    public void setProjectDifferentPrice(String ProjectDifferentPrice) {    this.ProjectDifferentPrice.set(ProjectDifferentPrice);  }

    public String getBalance() {    return balance.get();   }
    public SimpleStringProperty balanceProperty() { return balance; }
    public void setBalance(String balance) {    this.balance.set(balance);  }

    public boolean isCheckout() {   return isCheckout.get().equals("已結");    }
    public SimpleStringProperty isCheckoutProperty() { return isCheckout;  }
    public void setIsCheckout(boolean isCheckout) {
        if(isCheckout)  this.isCheckout.set("已結");
        else this.isCheckout.set("未結");
    }

    public boolean isOffset() { return isOffset.get().contains("沖");    }
    public boolean getOffsetOrderStatus() {
        return isOffset.get().contains("沖");
    }
    public SimpleStringProperty isOffsetProperty() { return isOffset;  }
    public void setIsOffset(Order_Enum.OffsetOrderStatus offsetOrderStatus) {
        if(!offsetOrderStatus.value()){
            this.isOffset.set("");
        }else{
            this.isOffset.set(offsetOrderStatus.getDescribe());
        }
    }

    public String getQuotationDate() {    return QuotationDate.get();   }
    public SimpleStringProperty QuotationDateProperty() { return QuotationDate; }
    public void setQuotationDate(String QuotationDate) {    this.QuotationDate.set(QuotationDate);  }

    public String getQuotationNumber() {    return QuotationNumber.get();   }
    public SimpleStringProperty QuotationNumberProperty() {  return QuotationNumber;  }
    public void setQuotationNumber(String QuotationNumber) {    this.QuotationNumber.set(QuotationNumber);  }

    public String getWaitingOrderDate() {    return WaitingOrderDate.get();   }
    public SimpleStringProperty WaitingOrderDateProperty() { return WaitingOrderDate; }
    public void setWaitingOrderDate(String WaitingOrderDate) {    this.WaitingOrderDate.set(WaitingOrderDate);  }

    public String getWaitingOrderNumber() {    return WaitingOrderNumber.get();   }
    public SimpleStringProperty WaitingOrderNumberProperty() {  return WaitingOrderNumber;  }
    public void setWaitingOrderNumber(String WaitingOrderNumber) {    this.WaitingOrderNumber.set(WaitingOrderNumber);  }

    public String getAlreadyOrderDate() {    return AlreadyOrderDate.get();   }
    public SimpleStringProperty AlreadyOrderDateProperty() { return AlreadyOrderDate; }
    public void setAlreadyOrderDate(String AlreadyOrderDate) {    this.AlreadyOrderDate.set(AlreadyOrderDate);  }

    public String getAlreadyOrderNumber() {    return AlreadyOrderNumber.get();   }
    public SimpleStringProperty AlreadyOrderNumberProperty() {  return AlreadyOrderNumber;  }
    public void setAlreadyOrderNumber(String AlreadyOrderNumber) {    this.AlreadyOrderNumber.set(AlreadyOrderNumber);  }

    public OrderSource getOrderSource() {   return Order_Enum.OrderSource.valueOf(OrderSource.getValue());   }
    public SimpleStringProperty OrderSourceProperty() {  return OrderSource;  }
    public void setOrderSource(OrderSource OrderSource) {    this.OrderSource.set(OrderSource.name());  }

    public OrderStatus getOrderStatus() {   return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) {   this.orderStatus = orderStatus; }

    public String getObjectNickName() { return ObjectNickName.get();    }
    public SimpleStringProperty ObjectNickNameProperty() {  return ObjectNickName;  }
    public void setObjectNickName(String ObjectNickName) {  this.ObjectNickName.set(ObjectNickName);    }

    public int getTotalPriceNoneTax() { return TotalPriceNoneTax.get(); }
    public SimpleIntegerProperty TotalPriceNoneTaxProperty() {  return TotalPriceNoneTax;   }
    public void setTotalPriceNoneTax(int TotalPriceNoneTax) {   this.TotalPriceNoneTax.set(TotalPriceNoneTax);  }

    public int getTax() {   return Tax.get();   }
    public SimpleIntegerProperty TaxProperty() {    return Tax; }
    public void setTax(int Tax) {   this.Tax.set(Tax);  }

    public int getDiscount() {  return Discount.get();  }
    public SimpleIntegerProperty DiscountProperty() {   return Discount;    }
    public void setDiscount(int Discount) { this.Discount.set(Discount);    }

    public Integer getProductGroupTotalPriceNoneTax() { return ProductGroupTotalPriceNoneTax;   }
    public void setProductGroupTotalPriceNoneTax(Integer productGroupTotalPriceNoneTax) {   ProductGroupTotalPriceNoneTax = productGroupTotalPriceNoneTax;  }

    public Integer getProductGroupTax() {   return ProductGroupTax; }
    public void setProductGroupTax(Integer productGroupTax) {   ProductGroupTax = productGroupTax;  }

    public Integer getProductGroupDiscount() {  return ProductGroupDiscount;    }
    public void setProductGroupDiscount(Integer productGroupDiscount) { ProductGroupDiscount = productGroupDiscount;    }

    public Integer getProductGroupTotalPriceIncludeTax() {  return ProductGroupTotalPriceIncludeTax;    }
    public void setProductGroupTotalPriceIncludeTax(Integer productGroupTotalPriceIncludeTax) { ProductGroupTotalPriceIncludeTax = productGroupTotalPriceIncludeTax;    }

    public Integer getInstallment() {   return installment.get();   }
    public SimpleObjectProperty<Integer> installmentProperty() {    return installment; }
    public void setInstallment(Integer installment) {   this.installment.set(installment);  }

    public Integer getPictureCount() {  return pictureCount.get();  }
    public SimpleObjectProperty<Integer> pictureCountProperty() {   return pictureCount;    }
    public void setPictureCount(Integer pictureCount) { this.pictureCount.set(pictureCount);    }

    public float getPayableReceivableDiscount() {   return PayableReceivableDiscount.get(); }
    public SimpleFloatProperty PayableReceivableDiscountProperty() {    return PayableReceivableDiscount;   }
    public void setPayableReceivableDiscount(float PayableReceivableDiscount) { this.PayableReceivableDiscount.set(PayableReceivableDiscount);  }

    public Order_Enum.EstablishSource getEstablishSource() {
        if(EstablishSource.get().equals("拋單"))   return Order_Enum.EstablishSource.系統建立;
        return Order_Enum.EstablishSource.人工建立;
    }
    public SimpleStringProperty EstablishSourceProperty() {    return EstablishSource;   }
    public void setEstablishSource(EstablishSource EstablishSource) {
        if(EstablishSource == Order_Enum.EstablishSource.人工建立)  this.EstablishSource.set("手動");
        else if(EstablishSource == Order_Enum.EstablishSource.系統建立) this.EstablishSource.set("拋單");
    }

    public Button getButton() {    return Button;  }
    public void setButton(Button button) { Button = button;    }

    public Integer getBindingIAECrawlerPayment_id() {   return bindingIAECrawlerPayment_id; }
    public void setBindingIAECrawlerPayment_id(Integer bindingIAECrawlerPayment_id) {   this.bindingIAECrawlerPayment_id = bindingIAECrawlerPayment_id; }
}
