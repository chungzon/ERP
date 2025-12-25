package ERP.Bean.ToolKit.Installment;

import ERP.Enum.Order.Order_Enum.CheckoutStatus;

public class Installment_Bean {
    private int id;
    private Integer order_id, payReceive_document_id;
    private int installment, totalPrice;
    private CheckoutStatus isCheckout;   //  出貨、進貨單使用(結帳與否)

    public Installment_Bean(int installment){
        this.installment = installment;
        this.isCheckout = CheckoutStatus.未結帳;
    }
    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public Integer getOrder_id() {  return order_id;    }
    public void setOrder_id(Integer order_id) { this.order_id = order_id;   }

    public Integer getPayReceive_document_id() {    return payReceive_document_id;  }
    public void setPayReceive_document_id(Integer payReceive_document_id) {
        this.payReceive_document_id = payReceive_document_id;
        this.isCheckout = (payReceive_document_id == null ? CheckoutStatus.未結帳 : CheckoutStatus.已結帳);
    }

    public int getInstallment() {   return installment; }
    public void setInstallment(int installment) {   this.installment = installment; }

    public int getTotalPrice() {    return totalPrice;  }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice;   }

    public boolean isCheckout(){
        return isCheckout == CheckoutStatus.已結帳;
    }
    public CheckoutStatus getCheckoutStatus(){
        return isCheckout;
    }
//    public void setIsCheckout(boolean isCheckout){
//        if(isCheckout)      this.isCheckout = CheckoutStatus.已結帳;
//        else    this.isCheckout = CheckoutStatus.未結帳;
//    }
}
