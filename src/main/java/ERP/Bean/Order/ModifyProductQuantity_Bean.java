package ERP.Bean.Order;

/** [ERP.Bean] Modify product quantity in quotation */
public class ModifyProductQuantity_Bean {
    private String ISBN;
    private OrderProduct_Bean newOrderProduct_Bean, oldOrderProduct_Bean;

    public ModifyProductQuantity_Bean(String ISBN, OrderProduct_Bean oldOrderProduct_Bean, OrderProduct_Bean newOrderProduct_Bean){
        this.ISBN = ISBN;
        this.oldOrderProduct_Bean = oldOrderProduct_Bean;
        this.newOrderProduct_Bean = newOrderProduct_Bean;
    }
    public String getISBN() {   return ISBN;    }
    public void setISBN(String ISBN) {  this.ISBN = ISBN;   }
    public OrderProduct_Bean getNewOrderProduct_Bean() {    return newOrderProduct_Bean;    }
    public void setNewOrderProduct_Bean(OrderProduct_Bean newOrderProduct_Bean) {   this.newOrderProduct_Bean = newOrderProduct_Bean;   }
    public OrderProduct_Bean getOldOrderProduct_Bean() {    return oldOrderProduct_Bean;    }

    public void setOldOrderProduct_Bean(OrderProduct_Bean oldOrderProduct_Bean) {
        this.oldOrderProduct_Bean = oldOrderProduct_Bean;
    }
}
