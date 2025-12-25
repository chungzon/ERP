package ERP.Bean.ManagePayableReceivable;

import ERP.Enum.Order.Order_Enum.OrderObject;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.SearchColumn;
import ERP.Enum.PayableReceivable.PayableReceivable_Enum.SearchMethod;

public class ConditionalSearchPayReceive_Bean {
    private boolean  findMoreThenDaysWaitingOrder;
    private int moreThenDays;
    private OrderObject orderObject;
    private SearchMethod searchMethod;
    private SearchColumn searchColumn;
    private String searchKeyWord, startDate, endDate;

    public ConditionalSearchPayReceive_Bean(boolean findMoreThenDaysWaitingOrder){
        this.findMoreThenDaysWaitingOrder = findMoreThenDaysWaitingOrder;
    }

    public boolean isFindMoreThenDaysWaitingOrder() {   return findMoreThenDaysWaitingOrder;    }
    public int getMoreThenDays() {  return moreThenDays;    }
    public void setMoreThenDays(int moreThenDays) { this.moreThenDays = moreThenDays;   }

    public OrderObject getOrderObject() {
        return orderObject;
    }

    public void setOrderObject(OrderObject orderObject) {
        this.orderObject = orderObject;
    }

    public SearchMethod getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
    }

    public SearchColumn getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(SearchColumn searchColumn) {
        this.searchColumn = searchColumn;
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
}
