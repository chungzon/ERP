package ERP.Bean.Order;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/** [ERP.Bean] Use Save or Print to export order */
public class ExportOrder_Bean {
    private SimpleStringProperty ISBN, ProductName;
    private SimpleIntegerProperty ItemNumber;

    private String Unit, Remark;
    private double SalePrice, PriceAmount;
    private int Quantity;

    public ExportOrder_Bean(){
        this.ISBN = new SimpleStringProperty();
        this.ProductName = new SimpleStringProperty();
        this.ItemNumber = new SimpleIntegerProperty();
    }

    public String getISBN() {   return ISBN.get();  }
    public SimpleStringProperty ISBNProperty() {    return ISBN;    }
    public void setISBN(String ISBN) {  this.ISBN.set(ISBN);    }

    public String getProductName() {    return ProductName.get();   }
    public SimpleStringProperty ProductNameProperty() { return ProductName; }
    public void setProductName(String ProductName) {    this.ProductName.set(ProductName);  }

    public String getUnit() {   return Unit;  }
    public void setUnit(String Unit) {  this.Unit = Unit;    }

    public String getRemark(){  return Remark;  }
    public void setRemark(String Remark){   this.Remark = Remark;   }

    public double getSalePrice() {    return SalePrice;   }
    public void setSalePrice(double SalePrice) {    this.SalePrice = SalePrice;  }

    public double getPriceAmount() {    return PriceAmount;   }
    public void setPriceAmount(double PriceAmount) {    this.PriceAmount = PriceAmount;  }

    public int getItemNumber() {    return ItemNumber.get();    }
    public SimpleIntegerProperty ItemNumberProperty() { return ItemNumber;  }
    public void setItemNumber(int ItemNumber) { this.ItemNumber.set(ItemNumber);    }

    public int getQuantity() {  return Quantity;  }
    public void setQuantity(int Quantity) { this.Quantity = Quantity;    }
}
