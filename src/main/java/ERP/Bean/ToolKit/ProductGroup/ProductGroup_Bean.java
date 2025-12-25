package ERP.Bean.ToolKit.ProductGroup;

import ERP.Bean.Order.OrderProduct_Bean;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class ProductGroup_Bean {
    private int id;
    private String orderNumber;
    private SimpleIntegerProperty itemNumber, quantity, priceAmount;
    private SimpleStringProperty groupName, unit;
    private SimpleDoubleProperty batchPrice, singlePrice;

    private double originalSinglePrice, differentPriceAmount;

    private boolean isCombineGroup;
    private Integer smallQuantity, smallPriceAmount;
    private Double smallSinglePrice;

    private ObservableList<OrderProduct_Bean> productList;

    public ProductGroup_Bean(){
        this.itemNumber = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
        this.groupName = new SimpleStringProperty();
        this.unit = new SimpleStringProperty();
        this.batchPrice = new SimpleDoubleProperty();
        this.singlePrice = new SimpleDoubleProperty();
        this.priceAmount = new SimpleIntegerProperty();
    }

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public String getOrderNumber() {    return orderNumber; }
    public void setOrderNumber(String orderNumber) {    this.orderNumber = orderNumber; }

    public int getItemNumber() {    return itemNumber.get();    }
    public SimpleIntegerProperty itemNumberProperty() { return itemNumber;  }
    public void setItemNumber(int itemNumber) { this.itemNumber.set(itemNumber);    }

    public int getQuantity() {  return quantity.get();  }
    public SimpleIntegerProperty quantityProperty() {   return quantity;    }
    public void setQuantity(int quantity) { this.quantity.set(quantity);    }

    public String getGroupName() {  return groupName.get(); }
    public SimpleStringProperty groupNameProperty() {   return groupName;   }
    public void setGroupName(String groupName) {    this.groupName.set(groupName);  }

    public String getUnit() {   return unit.get();  }
    public SimpleStringProperty unitProperty() {    return unit;    }
    public void setUnit(String unit) {  this.unit.set(unit);    }

    public double getBatchPrice() { return batchPrice.get();    }
    public SimpleDoubleProperty batchPriceProperty() {  return batchPrice;  }
    public void setBatchPrice(double batchPrice) {  this.batchPrice.set(batchPrice);    }

    public double getSinglePrice() {    return singlePrice.get();   }
    public SimpleDoubleProperty singlePriceProperty() { return singlePrice; }
    public void setSinglePrice(double singlePrice) {    this.singlePrice.set(singlePrice);  }

    public Integer getPriceAmount() {  return priceAmount.get(); }
    public SimpleIntegerProperty priceAmountProperty() {   return priceAmount;   }
    public void setPriceAmount(int priceAmount) {    this.priceAmount.set(priceAmount);  }

    public double getOriginalSinglePrice() {    return originalSinglePrice; }
    public void setOriginalSinglePrice(double originalSinglePrice) {    this.originalSinglePrice = originalSinglePrice; }

    public double getDifferentPriceAmount() {   return differentPriceAmount;    }
    public void setDifferentPriceAmount(double differentPriceAmount) {  this.differentPriceAmount = differentPriceAmount;   }

    public boolean isCombineGroup() {   return isCombineGroup;  }
    public void setCombineGroup(boolean combineGroup) { isCombineGroup = combineGroup;  }

    public Integer getSmallQuantity() {    return smallQuantity;  }
    public void setSmallQuantity(Integer smallQuantity) { this.smallQuantity = smallQuantity;   }

    public Double getSmallSinglePrice() {  return smallSinglePrice;    }
    public void setSmallSinglePrice(Double smallSinglePrice) { this.smallSinglePrice = smallSinglePrice;   }

    public Integer getSmallPriceAmount() {  return smallPriceAmount;    }
    public void setSmallPriceAmount(Integer smallPriceAmount) { this.smallPriceAmount = smallPriceAmount;   }

    public ObservableList<OrderProduct_Bean> getProductList() { return productList; }
    public void setProductList(ObservableList<OrderProduct_Bean> productList) { this.productList = productList; }
}
