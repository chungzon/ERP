package ERP.Enum.ObjectInfo;

/** [ERP.Enum] Object info */
public class ObjectInfo_Enum {

    /** Whether the object's order include tax */
    public enum OrderTax{
        未稅(false),應稅(true);
        private boolean value;
        OrderTax(boolean value) {  this.value = value; }
        public boolean value() { return this.value;  }
    }
    public enum DefaultPaymentMethod {
        現金,支票
    }
    /** Whether the manufacturer's order deducts remittance fee */
    public enum DiscountRemittanceFee{
        未扣(false), 已扣(true);
        private boolean Select;
        DiscountRemittanceFee(boolean Select) {  this.Select = Select; }
        public boolean SelectStatus() { return this.Select;  }
    }
    /** Whether the manufacturer's order deducts postage */
    public enum DiscountPostage{
        未扣(false), 已扣(true);
        private boolean Select;
        DiscountPostage(boolean Select) {  this.Select = Select; }
        public boolean SelectStatus() { return this.Select;  }
    }
    /** Customer's sale model */
    public enum CustomerSaleModel{
        VipPrice1, VipPrice2, VipPrice3, 成本價, 單價, 定價
    }
    /** Whether the Customer's order print pricing */
    public enum ShipmentInvoicePrintPricing{
        N(false), Y(true);
        private boolean value;
        ShipmentInvoicePrintPricing(boolean value) {  this.value = value; }
        public boolean value() { return this.value;  }
    }
    public enum CheckoutByMonth{
        非月結(false),月結(true);
        private boolean isCheckoutByMonth;
        CheckoutByMonth(boolean isCheckoutByMonth) {  this.isCheckoutByMonth = isCheckoutByMonth; }
        public boolean isCheckoutByMonth() { return this.isCheckoutByMonth;  }
    }
}
