package ERP.Bean.Order;


import ERP.Enum.Invoice.Invoice_Enum;
import ERP.Enum.Order.Order_Enum;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Invoice.Invoice_Enum;
import ERP.Enum.Invoice.Invoice_Enum.InvoiceType;
import ERP.Enum.Invoice.Invoice_Enum.InvoiceInvalid;
import ERP.Enum.Invoice.Invoice_Enum.InvoiceIgnore;

public class Invoice_Bean {
    private int id;
    private String InvoiceNumber,InvoiceDate;
    private InvoiceType InvoiceType;
    private InvoiceInvalid InvoiceInvalid;
    private InvoiceIgnore InvoiceIgnore;
    private int InvoiceYear, InvoicePrice;
    private Integer Order_id;
    private String OrderNumber;
    private OrderSource OrderSource;
    private String ManufacturerNickName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public int getInvoiceYear() {
        return InvoiceYear;
    }

    public void setInvoiceYear(int invoiceYear) {
        InvoiceYear = invoiceYear;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public int getInvoicePrice() {
        return InvoicePrice;
    }

    public void setInvoicePrice(int invoicePrice) {
        InvoicePrice = invoicePrice;
    }

    public InvoiceType getInvoiceType() {
        return InvoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        InvoiceType = invoiceType;
    }

    public Invoice_Enum.InvoiceInvalid getInvoiceInvalid() {
        return InvoiceInvalid;
    }

    public void setInvoiceInvalid(Invoice_Enum.InvoiceInvalid invoiceInvalid) {
        InvoiceInvalid = invoiceInvalid;
    }

    public Invoice_Enum.InvoiceIgnore getInvoiceIgnore() {  return InvoiceIgnore;   }

    public void setInvoiceIgnore(Invoice_Enum.InvoiceIgnore invoiceIgnore) {    InvoiceIgnore = invoiceIgnore;  }

    public Integer getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(Integer order_id) {
        Order_id = order_id;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public Order_Enum.OrderSource getOrderSource() {
        return OrderSource;
    }

    public void setOrderSource(Order_Enum.OrderSource orderSource) {
        OrderSource = orderSource;
    }

    public String getManufacturerNickName() {   return ManufacturerNickName;    }

    public void setManufacturerNickName(String manufacturerNickName) {  ManufacturerNickName = manufacturerNickName;    }
}
