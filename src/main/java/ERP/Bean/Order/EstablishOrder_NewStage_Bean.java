package ERP.Bean.Order;

import ERP.Controller.Order.SearchOrder.SearchOrder_Controller;
import ERP.Controller.Order.SearchOrderProgress.SearchOrderProgress_Controller;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import ERP.Enum.Order.Order_Enum.OrderSource;
import ERP.Enum.Order.Order_Enum.OrderExist;

import java.util.ArrayList;

public class EstablishOrder_NewStage_Bean {
    private OrderStatus orderStatus;
    private OrderSource orderSource;
    private OrderExist orderExist;
    private SearchOrder_Controller searchOrder_Controller;
    private SearchOrderProgress_Controller searchOrderProgress_Controller;
    private Order_Bean order_Bean;
    private ArrayList<String> differentOrderInfoList;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderSource getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(OrderSource orderSource) {
        this.orderSource = orderSource;
    }

    public OrderExist getOrderExist() {
        return orderExist;
    }

    public void setOrderExist(OrderExist orderExist) {
        this.orderExist = orderExist;
    }

    public SearchOrder_Controller getSearchOrder_Controller() {
        return searchOrder_Controller;
    }

    public void setSearchOrder_Controller(SearchOrder_Controller searchOrder_Controller) {
        this.searchOrder_Controller = searchOrder_Controller;
    }

    public SearchOrderProgress_Controller getSearchOrderProgress_Controller() {
        return searchOrderProgress_Controller;
    }

    public void setSearchOrderProgress_Controller(SearchOrderProgress_Controller searchOrderProgress_Controller) {
        this.searchOrderProgress_Controller = searchOrderProgress_Controller;
    }

    public Order_Bean getOrder_Bean() {
        return order_Bean;
    }

    public void setOrder_Bean(Order_Bean order_Bean) {
        this.order_Bean = order_Bean;
    }

    public ArrayList<String> getDifferentOrderInfoList() {
        return differentOrderInfoList;
    }

    public void setDifferentOrderInfoList(ArrayList<String> differentOrderInfoList) {
        this.differentOrderInfoList = differentOrderInfoList;
    }
}
