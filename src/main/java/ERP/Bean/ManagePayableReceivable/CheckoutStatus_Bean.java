package ERP.Bean.ManagePayableReceivable;

public class CheckoutStatus_Bean {
    private String checkStatus;
    private Boolean isCheckout;

    public CheckoutStatus_Bean(String checkStatus, Boolean isCheckout){
        this.checkStatus = checkStatus;
        this.isCheckout = isCheckout;
    }

    public String getCheckStatus() {    return checkStatus; }
    public Boolean isCheckout() {   return isCheckout;  }
}
