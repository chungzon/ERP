package ERP.Bean.Order;

import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.Order.Order_Enum.SearchOrderSource;
import ERP.Enum.Order.Order_Enum.OrderSearchMethod;
import ERP.Enum.Order.Order_Enum.ReviewStatus;
import ERP.Enum.Order.Order_Enum.OrderStatus;
import java.util.HashMap;

public class ConditionalOrderSearch_Bean {
    private OrderObject orderObject;
    private SearchOrderSource searchOrderSource;
    private OrderSearchMethod orderSearchMethod;
    private boolean isBorrowed;
    private OrderStatus orderStatus;
    private String searchKeyWord;
    private String startDate,endDate;
    private HashMap<ReviewStatus,Boolean> searchOrderReviewStatus;
    boolean isSearchTextByOr;

    public OrderObject getOrderObject() {
        return orderObject;
    }

    public void setOrderObject(OrderObject orderObject) {
        this.orderObject = orderObject;
    }

    public SearchOrderSource getSearchOrderSource() {
        return searchOrderSource;
    }

    public void setSearchOrderSource(SearchOrderSource searchOrderSource) {
        this.searchOrderSource = searchOrderSource;
    }

    public OrderSearchMethod getOrderSearchMethod() {
        return orderSearchMethod;
    }

    public void setOrderSearchMethod(OrderSearchMethod orderSearchMethod) {
        this.orderSearchMethod = orderSearchMethod;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSearchKeyWord() {
        return searchKeyWord;
    }

    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public HashMap<ReviewStatus, Boolean> getSearchOrderReviewStatus() {
        return searchOrderReviewStatus;
    }

    public void setSearchOrderReviewStatus(HashMap<ReviewStatus, Boolean> searchOrderReviewStatus) {
        this.searchOrderReviewStatus = searchOrderReviewStatus;
    }

    public boolean isSearchTextByOr() { return isSearchTextByOr;    }

    public void setSearchTextByOr(boolean searchTextByOr) { isSearchTextByOr = searchTextByOr;  }
}
