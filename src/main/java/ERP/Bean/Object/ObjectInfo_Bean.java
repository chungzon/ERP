package ERP.Bean.Object;

import ERP.Bean.ManagePayableReceivable.BankInfo_Bean;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.DefaultPaymentMethod;
import javafx.beans.property.SimpleStringProperty;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.OrderTax;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.CustomerSaleModel;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.ShipmentInvoicePrintPricing;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.DiscountRemittanceFee;
import ERP.Enum.ObjectInfo.ObjectInfo_Enum.DiscountPostage;
/** [ERP.Bean] Manage object info in manage purchase or shipment system */
public class ObjectInfo_Bean {

    private int id;
    private SimpleStringProperty ObjectID, ObjectName, ObjectNickName, ContactPerson, Telephone1, Telephone2, Fax, CompanyAddress;
    private String PersonInCharge, CellPhone, Email, MemberID, DeliveryAddress, InvoiceTitle, TaxIDNumber, InvoiceAddress, Remark, StoreCode;
    private OrderTax OrderTax;
    private DefaultPaymentMethod defaultPaymentMethod;
    private ShipmentInvoicePrintPricing PrintPricing;
    private Double PayableReceivableDiscount, SaleDiscount;
    private int ReceivableDay;
    private CustomerSaleModel CustomerSaleModel;

    private boolean isCheckoutByMonth;

    /**  廠商使用    */
//    private Double PayableDiscount;
    private int PayableDay;
    private int CheckDueDay;
    private DiscountRemittanceFee DiscountRemittanceFee;
    private DiscountPostage DiscountPostage;
    private BankInfo_Bean ManufacturerBank;
    private Integer BankID;
    private String BankBranch, AccountName, BankAccount;
    private int RemittanceFee, Postage;
    private String CheckTitle;

    public ObjectInfo_Bean(){
        this.ObjectID = new SimpleStringProperty();
        this.ObjectName = new SimpleStringProperty();
        this.ObjectNickName = new SimpleStringProperty();
        this.ContactPerson = new SimpleStringProperty();
        this.Telephone1 = new SimpleStringProperty();
        this.Telephone2 = new SimpleStringProperty();
        this.Fax = new SimpleStringProperty();
        this.CompanyAddress = new SimpleStringProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectID() { return ObjectID.get();    }
    public SimpleStringProperty ObjectIDProperty() {  return ObjectID;  }
    public void setObjectID(String ObjectID) {  this.ObjectID.set(ObjectID);    }
    public String getObjectName() {   return ObjectName.get();  }
    public SimpleStringProperty ObjectNameProperty() {    return ObjectName;    }
    public void setObjectName(String ObjectName) {  this.ObjectName.set(ObjectName);    }
    public String getObjectNickName() {   return ObjectNickName.get();  }
    public SimpleStringProperty ObjectNickNameProperty() {    return ObjectNickName;    }
    public void setObjectNickName(String ObjectNickName) {  this.ObjectNickName.set(ObjectNickName);    }
    public String getPersonInCharge() { return PersonInCharge;  }
    public void setPersonInCharge(String PersonInCharge) {  this.PersonInCharge = PersonInCharge;    }

    public String getContactPerson() {  return ContactPerson.get(); }
    public SimpleStringProperty ContactPersonProperty() {   return ContactPerson;   }
    public void setContactPerson(String ContactPerson) {    this.ContactPerson.set(ContactPerson);  }
    public String getTelephone1() { return Telephone1.get();    }
    public SimpleStringProperty Telephone1Property() {  return Telephone1;  }
    public void setTelephone1(String Telephone1) {  this.Telephone1.set(Telephone1);    }
    public String getTelephone2() { return Telephone2.get();    }
    public SimpleStringProperty Telephone2Property() {  return Telephone2;  }
    public void setTelephone2(String Telephone2) {  this.Telephone2.set(Telephone2);    }
    public String getCellPhone() {  return CellPhone;   }
    public void setCellPhone(String CellPhone) {    this.CellPhone = CellPhone;  }
    public String getFax() {    return Fax.get();   }
    public SimpleStringProperty FaxProperty() { return Fax; }
    public void setFax(String Fax) {    this.Fax.set(Fax);  }
    public String getEmail() {  return Email;   }
    public void setEmail(String email) {    this.Email = email;  }
    public String getMemberID() {   return MemberID;    }
    public void setMemberID(String MemberID) {  this.MemberID = MemberID;    }
    public String getCompanyAddress() { return CompanyAddress.get();  }
    public SimpleStringProperty CompanyAddressProperty() {  return CompanyAddress;  }
    public void setCompanyAddress(String CompanyAddress) {  this.CompanyAddress.set(CompanyAddress);    }
    public String getDeliveryAddress() {    return DeliveryAddress; }
    public void setDeliveryAddress(String DeliveryAddress) {    this.DeliveryAddress = DeliveryAddress;  }
    public String getInvoiceTitle() {   return InvoiceTitle;    }
    public void setInvoiceTitle(String InvoiceTitle) {  this.InvoiceTitle = InvoiceTitle;    }
    public String getTaxIDNumber() {  return TaxIDNumber;   }
    public void setTaxIDNumber(String TaxIDNumber) {    this.TaxIDNumber = TaxIDNumber;  }
    public String getInvoiceAddress() { return InvoiceAddress;  }
    public void setInvoiceAddress(String InvoiceAddress) {  this.InvoiceAddress = InvoiceAddress;    }

    public OrderTax getOrderTax(){  return OrderTax;    }
    public void setOrderTax(OrderTax OrderTax){ this.OrderTax = OrderTax;   }

    public DefaultPaymentMethod getDefaultPaymentMethod() { return defaultPaymentMethod;    }
    public void setDefaultPaymentMethod(DefaultPaymentMethod defaultPaymentMethod) {    this.defaultPaymentMethod = defaultPaymentMethod;   }

    public ShipmentInvoicePrintPricing getPrintPricing() {   return PrintPricing;    }
    public void setPrintPricing(ShipmentInvoicePrintPricing PrintPricing) { this.PrintPricing = PrintPricing;   }

    public String getRemark() { return Remark;  }
    public void setRemark(String Remark) {  this.Remark = Remark;    }

    public Double getPayableReceivableDiscount() { return PayableReceivableDiscount;  }
    public void setPayableReceivableDiscount(Double PayableReceivableDiscount) {  this.PayableReceivableDiscount = PayableReceivableDiscount;    }
//    public Double getReceivableDiscount() { return ReceivableDiscount;  }
//    public void setReceivableDiscount(Double ReceivableDiscount) {  this.ReceivableDiscount = ReceivableDiscount;    }
    public CustomerSaleModel getSaleModel() {  return CustomerSaleModel;   }
    public void setSaleModel(CustomerSaleModel CustomerSaleModel) {    this.CustomerSaleModel = CustomerSaleModel;  }
    public Double getSaleDiscount() {   return SaleDiscount;    }
    public void setSaleDiscount(Double SaleDiscount) {  this.SaleDiscount = SaleDiscount;    }
    public int getReceivableDay() { return ReceivableDay;  }
    public void setReceivableDay(int ReceivableDay) {  this.ReceivableDay = ReceivableDay;    }
    public String getStoreCode() {  return StoreCode;   }
    public void setStoreCode(String StoreCode) {    this.StoreCode = StoreCode;  }


//    public Double getPayableDiscount() {    return PayableDiscount; }
//    public void setPayableDiscount(Double PayableDiscount) {    this.PayableDiscount = PayableDiscount;  }
    public int getPayableDay() { return PayableDay;  }
    public void setPayableDay(int PayableDay) {  this.PayableDay = PayableDay;    }

    public boolean isCheckoutByMonth() {    return isCheckoutByMonth;   }
    public void setCheckoutByMonth(boolean checkoutByMonth) {   isCheckoutByMonth = checkoutByMonth;    }
    public int getCheckDueDay() {   return CheckDueDay; }
    public void setCheckDueDay(int CheckDueDay) {   this.CheckDueDay = CheckDueDay;  }
    public DiscountRemittanceFee getDiscountRemittanceFee() {  return DiscountRemittanceFee; }
    public void setDiscountRemittanceFee(DiscountRemittanceFee DiscountRemittanceFee) {   this.DiscountRemittanceFee = DiscountRemittanceFee;    }
    public DiscountPostage getDiscountPostage() {    return DiscountPostage;   }
    public void setDiscountPostage(DiscountPostage DiscountPostage) {   this.DiscountPostage = DiscountPostage;    }

    public Integer getBankID(){ return this.BankID; }
    public void setBankID(Integer BankID){  this.BankID = BankID;   }
    public String getBankBranch() { return BankBranch;  }
    public void setBankBranch(String BankBranch) {  this.BankBranch = BankBranch;    }
    public String getAccountName() {    return AccountName; }
    public void setAccountName(String AccountName) {    this.AccountName = AccountName;  }
    public String getBankAccount() {    return BankAccount; }
    public void setBankAccount(String BankAccount) {    this.BankAccount = BankAccount;  }
    public int getRemittanceFee() { return RemittanceFee;   }
    public void setRemittanceFee(int RemittanceFee) {   this.RemittanceFee = RemittanceFee;  }
    public int getPostage() {   return Postage; }
    public void setPostage(int Postage) {   this.Postage = Postage;  }
    public String getCheckTitle() { return CheckTitle;  }
    public void setCheckTitle(String CheckTitle) {  this.CheckTitle = CheckTitle;    }
}
