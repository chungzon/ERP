package ERP.Bean.Order;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

/** [ERP.Bean] Product info in order.  Manage purchase or shipment system */
public class OrderProduct_Bean {
    private CheckBox CheckBox;
    private int store_id;
    private Integer item_id;
    private SimpleStringProperty ISBN, InternationalCode, FirmCode, ProductCode, ProductName, Unit;
    private SimpleDoubleProperty BatchPrice, SinglePrice, Pricing, VipPrice1, VipPrice2, VipPrice3;
    private SimpleIntegerProperty PriceAmount;
    private SimpleStringProperty SupplyStatus, Remark;

    private SimpleStringProperty MinimumPrice;  //  商品搜尋 - 特別價
    private SimpleIntegerProperty ItemNumber, Quantity, InStock, SafetyStock, SaleQuantity, WaitingPurchaseQuantity, WaitingIntoInStock, NeededPurchaseQuantity, WaitingShipmentQuantity;


    private SimpleStringProperty KeyInDate, PurchaseDate, SaleDate;

    private ObservableList<String> ProductTag;

    private String firstCategory;
    private String specificationProductName, specificationContent;

    private boolean isAddExportQuotationItem = false;

    /**      SeriesNumber 拆單(子貨單)用   */
    private SimpleIntegerProperty SeriesNumber;

    public OrderProduct_Bean(){
        CheckBox = new CheckBox();
        this.ISBN = new SimpleStringProperty();
        this.InternationalCode = new SimpleStringProperty();
        this.FirmCode = new SimpleStringProperty();
        this.ProductCode = new SimpleStringProperty();
        this.ProductName = new SimpleStringProperty();
        this.Unit = new SimpleStringProperty();
        this.BatchPrice = new SimpleDoubleProperty();
        this.SinglePrice = new SimpleDoubleProperty();
        this.Pricing = new SimpleDoubleProperty();
        this.VipPrice1 = new SimpleDoubleProperty();
        this.VipPrice2 = new SimpleDoubleProperty();
        this.VipPrice3 = new SimpleDoubleProperty();
        this.InStock = new SimpleIntegerProperty();
        this.SafetyStock = new SimpleIntegerProperty();
        this.SaleQuantity = new SimpleIntegerProperty();
        this.SupplyStatus = new SimpleStringProperty();
        this.Remark = new SimpleStringProperty();
        this.MinimumPrice = new SimpleStringProperty();
        this.ItemNumber = new SimpleIntegerProperty();
        this.Quantity = new SimpleIntegerProperty();
        this.PriceAmount = new SimpleIntegerProperty();
        this.KeyInDate = new SimpleStringProperty();
        this.PurchaseDate = new SimpleStringProperty();
        this.SaleDate = new SimpleStringProperty();
        this.WaitingPurchaseQuantity= new SimpleIntegerProperty();
        this.WaitingIntoInStock = new SimpleIntegerProperty();
        this.NeededPurchaseQuantity = new SimpleIntegerProperty();
        this.WaitingShipmentQuantity = new SimpleIntegerProperty();

        this.SeriesNumber = new SimpleIntegerProperty();
    }
    public void setCheckBox(CheckBox CheckBox){ this.CheckBox = CheckBox;   }
    public CheckBox getCheckBox(){  return CheckBox;    }
    public void setCheckBoxDisable(boolean disable){    this.CheckBox.setDisable(disable);  }
    public boolean isCheckBoxDisable(){  return this.CheckBox.isDisable();   }
    public void setCheckBoxSelect(Boolean select){  CheckBox.setSelected(select);    }
    public boolean isCheckBoxSelect(){  return CheckBox.isSelected();   }

    public int getStore_id() {  return store_id;    }
    public void setStore_id(int store_id) { this.store_id = store_id;   }

    public Integer getItem_id() {   return item_id; }
    public void setItem_id(Integer item_id) {   this.item_id = item_id; }

    public void setISBN(String ISBN) {  this.ISBN.set(ISBN);    }
    public SimpleStringProperty ISBNProperty() {    return ISBN;    }
    public String getISBN() {   return ISBN.get();  }

    public void setInternationalCode(String InternationalCode) {    this.InternationalCode.set(InternationalCode);  }
    public SimpleStringProperty InternationalCodeProperty() {   return InternationalCode;   }
    public String getInternationalCode() {  return InternationalCode.get(); }

    public void setFirmCode(String FirmCode) {  this.FirmCode.set(FirmCode);    }
    public SimpleStringProperty FirmCodeProperty() {    return FirmCode;    }
    public String getFirmCode() {   return FirmCode.get();  }

    public String getProductCode() {    return ProductCode.get(); }
    public SimpleStringProperty ProductCodeProperty() {    return ProductCode;    }
    public void setProductCode(String ProductCode) {    this.ProductCode.set(ProductCode);  }

    public void setProductTag(ObservableList<String> ProductTag){   this.ProductTag = ProductTag;   }
    public ObservableList<String> getProductTag(){  return ProductTag;  }

    public void setProductName(String ProductName) {    this.ProductName.set(ProductName);  }
    public SimpleStringProperty ProductNameProperty() { return ProductName; }
    public String getProductName() {    return ProductName.get();   }

    public void setUnit(String Unit) {  this.Unit.set(Unit);    }
    public SimpleStringProperty UnitProperty() {    return Unit;    }
    public String getUnit() {   return Unit.get();  }

    public void setBatchPrice(double BatchPrice) {  this.BatchPrice.set(BatchPrice);    }
    public SimpleDoubleProperty BatchPriceProperty() {  return BatchPrice;  }
    public double getBatchPrice() { return BatchPrice.get();    }

    public void setSinglePrice(double SinglePrice) {    this.SinglePrice.set(SinglePrice);  }
    public SimpleDoubleProperty SinglePriceProperty() { return SinglePrice; }
    public double getSinglePrice() {    return SinglePrice.get();   }

    public void setPricing(double Pricing) {    this.Pricing.set(Pricing);  }
    public SimpleDoubleProperty PricingProperty() { return Pricing; }
    public double getPricing() {    return Pricing.get();   }

    public void setVipPrice1(double VipPrice1){ this.VipPrice1.set(VipPrice1); }
    public SimpleDoubleProperty setVipPrice1Property() {  return VipPrice1;  }
    public double getVipPrice1(){   return VipPrice1.get(); }

    public void setVipPrice2(double VipPrice2){ this.VipPrice2.set(VipPrice2); }
    public SimpleDoubleProperty setVipPrice2Property() {  return VipPrice2;  }
    public double getVipPrice2(){   return VipPrice2.get(); }

    public void setVipPrice3(double VipPrice3){ this.VipPrice3.set(VipPrice3); }
    public SimpleDoubleProperty setVipPrice3Property() {  return VipPrice3;  }
    public double getVipPrice3(){   return VipPrice3.get(); }

    public void setInStock(int InStock){    this.InStock.set(InStock); }
    public SimpleIntegerProperty setInStockProperty() {  return InStock;  }
    public int getInStock(){    return InStock.get(); }

    public int getSafetyStock() {   return SafetyStock.get();   }
    public SimpleIntegerProperty safetyStockProperty() {    return SafetyStock; }
    public void setSafetyStock(int safetyStock) {   this.SafetyStock.set(safetyStock);  }

    public void setSaleQuantity(int SaleQuantity){  this.SaleQuantity.set(SaleQuantity);    }
    public SimpleIntegerProperty setSaleQuantityProperty(){ return SaleQuantity;    }
    public int getSaleQuantity(){   return SaleQuantity.get();  }

    public void setMinimumPrice(String MinimumPrice) {  this.MinimumPrice.set(MinimumPrice);    }
    public SimpleStringProperty MinimumPriceProperty() {    return MinimumPrice;    }
    public String getMinimumPrice() {   return MinimumPrice.get();  }

    public void setItemNumber(int ItemNumber) { this.ItemNumber.set(ItemNumber);    }
    public SimpleIntegerProperty ItemNumberProperty() { return ItemNumber;  }
    public int getItemNumber() {    return ItemNumber.get();    }

    public void setQuantity(int Quantity) { this.Quantity.set(Quantity);    }
    public SimpleIntegerProperty QuantityProperty() {   return Quantity;    }
    public int getQuantity() {  return Quantity.get();  }

    public void setPriceAmount(int PriceAmount) {    this.PriceAmount.set(PriceAmount);  }
    public SimpleIntegerProperty PriceAmountProperty() { return PriceAmount; }
    public int getPriceAmount() {    return PriceAmount.get();   }

    public void setRemark(String Remark){   this.Remark.set(Remark);   }
    public SimpleStringProperty RemarkProperty() {    return Remark;    }
    public String getRemark(){  return Remark.get();  }

    public void setSupplyStatus(String SupplyStatus){  this.SupplyStatus.set(SupplyStatus);   }
    public SimpleStringProperty SupplyStatusProperty() {    return SupplyStatus;    }
    public String getSupplyStatus(){  return SupplyStatus.get();    }

    public void setKeyInDate(String keyInDate) {    this.KeyInDate.set(keyInDate);  }
    public SimpleStringProperty keyInDateProperty() {   return KeyInDate;   }
    public String getKeyInDate() {  return KeyInDate.get(); }

    public void setPurchaseDate(String PurchaseDate){   this.PurchaseDate.set(PurchaseDate);   }
    public SimpleStringProperty PurchaseDateProperty() {    return PurchaseDate;    }
    public String getPurchaseDate(){    return PurchaseDate.get();    }

    public void setSaleDate(String SaleDate){   this.SaleDate.set(SaleDate);   }
    public SimpleStringProperty SaleDateProperty() {    return SaleDate;    }
    public String getSaleDate(){    return SaleDate.get();    }

    public int getSeriesNumber() {  return SeriesNumber.get();  }
    public SimpleIntegerProperty SeriesNumberProperty() {   return SeriesNumber;    }
    public void setSeriesNumber(int SeriesNumber) { this.SeriesNumber.set(SeriesNumber);    }

    public int getWaitingPurchaseQuantity() {   return WaitingPurchaseQuantity.get(); }
    public SimpleIntegerProperty WaitingPurchaseQuantityProperty() {   return WaitingPurchaseQuantity;    }
    public void setWaitingPurchaseQuantity(int WaitingPurchaseQuantity) {   this.WaitingPurchaseQuantity.set(WaitingPurchaseQuantity);  }

    public int getWaitingIntoInStock() {   return WaitingIntoInStock.get(); }
    public SimpleIntegerProperty WaitingIntoInStockProperty() {   return WaitingIntoInStock;    }
    public void setWaitingIntoInStock(int WaitingIntoInStock) {   this.WaitingIntoInStock.set(WaitingIntoInStock);  }

    public int getNeededPurchaseQuantity() {   return NeededPurchaseQuantity.get(); }
    public SimpleIntegerProperty NeededPurchaseQuantityProperty() {   return NeededPurchaseQuantity;    }
    public void setNeededPurchaseQuantity(int NeededPurchaseQuantity) {   this.NeededPurchaseQuantity.set(NeededPurchaseQuantity);  }

    public int getWaitingShipmentQuantity() {   return WaitingShipmentQuantity.get(); }
    public SimpleIntegerProperty WaitingShipmentQuantityProperty() {   return WaitingShipmentQuantity;    }
    public void setWaitingShipmentQuantity(int WaitingShipmentQuantity) {   this.WaitingShipmentQuantity.set(WaitingShipmentQuantity);  }

    public String getFirstCategory() {  return firstCategory;   }
    public void setFirstCategory(String firstCategory) {    this.firstCategory = firstCategory; }

    public String getSpecificationProductName() {   return specificationProductName;    }
    public void setSpecificationProductName(String specificationProductName) {  this.specificationProductName = specificationProductName;   }

    public String getSpecificationContent() {   return specificationContent;    }
    public void setSpecificationContent(String specificationContent) {  this.specificationContent = specificationContent;   }

    public boolean isAddExportQuotationItem() { return isAddExportQuotationItem;    }
    public void setAddExportQuotationItem(boolean addExportQuotationItem) { isAddExportQuotationItem = addExportQuotationItem;  }
}
