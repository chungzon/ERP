package ERP.Bean.Product;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/** [ERP.Bean] Product on shelf in manage product system */
public class ManageProductOnShelf_Bean {
    public CheckBox WebCheckBox, YahooCheckBox, RutenCheckBox, ShopeeCheckBox;
    public SimpleDoubleProperty BatchPrice, SinglePrice, Pricing, VipPrice1, VipPrice2, VipPrice3;
    public SimpleStringProperty ISBN, InternationalCode, FirmCode, ProductName, Unit, SpecialOffer, Visitors, Followers, SellingVolume, BidCode;

    private Integer packageLength,packageWidth,packageHeight;
    private int dayOfPrepare;
    private double packageWeight;
    private Boolean isPrepareOrder;

    private boolean isAllowBlackCat, isAllowSevenEleven, isAllowFamily, isAllowHiLife;
    private Boolean isBlackCatOwnExpense, isSevenElvenOwnExpense, isFamilyOwnExpense, isHiLifeOwnExpense;
    private Integer blackCatShippingFee, sevenElevenShippingFee, familyShippingFee, hiLifeShippingFee;

    public ManageProductOnShelf_Bean(){
        ISBN = new SimpleStringProperty();
        InternationalCode = new SimpleStringProperty();
        FirmCode = new SimpleStringProperty();
        ProductName = new SimpleStringProperty();
        Unit = new SimpleStringProperty();
        BatchPrice = new SimpleDoubleProperty();
        SinglePrice = new SimpleDoubleProperty();
        Pricing = new SimpleDoubleProperty();
        VipPrice1 = new SimpleDoubleProperty();
        VipPrice2 = new SimpleDoubleProperty();
        VipPrice3 = new SimpleDoubleProperty();
        SpecialOffer = new SimpleStringProperty();

        Visitors = new SimpleStringProperty();
        Followers = new SimpleStringProperty();
        SellingVolume = new SimpleStringProperty();
        BidCode = new SimpleStringProperty();
    }
    public void setWebCheckBox(CheckBox CheckBox, boolean defaultSelect){
        this.WebCheckBox = CheckBox;
        this.WebCheckBox.setSelected(defaultSelect);
    }
    public CheckBox getWebCheckBox(){  return WebCheckBox;    }
    public void setWebCheckBoxSelect(boolean Select){   WebCheckBox.setSelected(Select);    }
    public boolean isWebCheckBoxSelect(){  return WebCheckBox.isSelected();   }

    public void setYahooCheckBox(CheckBox CheckBox, boolean defaultSelect){
        this.YahooCheckBox = CheckBox;
        this.YahooCheckBox.setSelected(defaultSelect);
    }
    public CheckBox getYahooCheckBox(){  return YahooCheckBox;    }
    public void setYahooCheckBoxSelect(boolean Select){   YahooCheckBox.setSelected(Select);    }
    public boolean isYahooCheckBoxSelect(){  return YahooCheckBox.isSelected();   }

    public void setRutenCheckBox(CheckBox CheckBox, boolean defaultSelect){
        this.RutenCheckBox = CheckBox;
        this.RutenCheckBox.setSelected(defaultSelect);
    }
    public CheckBox getRutenCheckBox(){  return RutenCheckBox;    }
    public void setRutenCheckBoxSelect(boolean Select){   RutenCheckBox.setSelected(Select);    }
    public boolean isRutenCheckBoxSelect(){  return RutenCheckBox.isSelected();   }

    public void setShopeeCheckBox(CheckBox CheckBox, boolean defaultSelect){
        this.ShopeeCheckBox = CheckBox;
        this.ShopeeCheckBox.setSelected(defaultSelect);
    }
    public CheckBox getShopeeCheckBox(){  return ShopeeCheckBox;    }
    public void setShopeeCheckBoxSelect(boolean Select){   ShopeeCheckBox.setSelected(Select);    }
    public boolean isShopeeCheckBoxSelect(){  return ShopeeCheckBox.isSelected();   }

    public String getISBN() {   return ISBN.get();  }
    public SimpleStringProperty ISBNProperty() {    return ISBN;    }
    public void setISBN(String ISBN) {  this.ISBN.set(ISBN);    }

    public String getInternationalCode() {  return InternationalCode.get(); }
    public SimpleStringProperty InternationalCodeProperty() {   return InternationalCode;   }
    public void setInternationalCode(String internationalCode) {    this.InternationalCode.set(internationalCode);  }

    public String getFirmCode() {   return FirmCode.get();  }
    public SimpleStringProperty FirmCodeProperty() {    return FirmCode;    }
    public void setFirmCode(String firmCode) {  this.FirmCode.set(firmCode);    }

    public String getProductName() {    return ProductName.get();   }
    public SimpleStringProperty ProductNameProperty() { return ProductName; }
    public void setProductName(String productName) {    this.ProductName.set(productName);  }

    public String getUnit() {   return Unit.get();  }
    public SimpleStringProperty UnitProperty() {    return Unit;    }
    public void setUnit(String unit) {  this.Unit.set(unit);    }

    public double getBatchPrice() { return BatchPrice.get();    }
    public SimpleDoubleProperty BatchPriceProperty() {  return BatchPrice;  }
    public void setBatchPrice(double batchPrice) {  this.BatchPrice.set(batchPrice);    }

    public double getSinglePrice() {    return SinglePrice.get();   }
    public SimpleDoubleProperty SinglePriceProperty() { return SinglePrice; }
    public void setSinglePrice(double singlePrice) {    this.SinglePrice.set(singlePrice);  }

    public double getPricing() {    return Pricing.get();   }
    public SimpleDoubleProperty PricingProperty() { return Pricing; }
    public void setPricing(double pricing) {    this.Pricing.set(pricing);  }

    public double getVipPrice1() {  return VipPrice1.get(); }
    public SimpleDoubleProperty VipPrice1Property() {   return VipPrice1;   }
    public void setVipPrice1(double vipPrice1) {    this.VipPrice1.set(vipPrice1);  }

    public double getVipPrice2() {  return VipPrice2.get(); }
    public SimpleDoubleProperty VipPrice2Property() {   return VipPrice2;   }
    public void setVipPrice2(double vipPrice2) {    this.VipPrice2.set(vipPrice2);  }

    public double getVipPrice3() {  return VipPrice3.get(); }
    public SimpleDoubleProperty VipPrice3Property() {   return VipPrice3;   }
    public void setVipPrice3(double vipPrice3) {    this.VipPrice3.set(vipPrice3);  }

    public String getSpecialOffer() {   return SpecialOffer.get();  }
    public SimpleStringProperty SpecialOfferProperty() {    return SpecialOffer;    }
    public void setSpecialOffer(String specialOffer) {  this.SpecialOffer.set(specialOffer);    }

    public String getVisitors() {   return Visitors.get();  }
    public SimpleStringProperty VisitorsProperty() {    return Visitors;    }
    public void setVisitors(String visitors) {  this.Visitors.set(visitors);    }

    public String getFollowers() {  return Followers.get(); }
    public SimpleStringProperty FollowersProperty() {   return Followers;   }
    public void setFollowers(String followers) {    this.Followers.set(followers);  }

    public String getSellingVolume() {  return SellingVolume.get(); }
    public SimpleStringProperty SellingVolumeProperty() {   return SellingVolume;   }
    public void setSellingVolume(String sellingVolume) {    this.SellingVolume.set(sellingVolume);  }

    public String getBidCode() {    return BidCode.get();   }
    public SimpleStringProperty BidCodeProperty() { return BidCode; }
    public void setBidCode(String bidCode) {    this.BidCode.set(bidCode);  }

    public Integer getPackageLength() { return packageLength;   }
    public void setPackageLength(Integer packageLength) {   this.packageLength = packageLength; }

    public Integer getPackageWidth() {  return packageWidth;    }
    public void setPackageWidth(Integer packageWidth) { this.packageWidth = packageWidth;   }

    public Integer getPackageHeight() { return packageHeight;   }
    public void setPackageHeight(Integer packageHeight) {   this.packageHeight = packageHeight; }

    public int getDayOfPrepare() {  return dayOfPrepare;    }
    public void setDayOfPrepare(int dayOfPrepare) { this.dayOfPrepare = dayOfPrepare;   }

    public double getPackageWeight() {  return packageWeight;   }
    public void setPackageWeight(double packageWeight) {    this.packageWeight = packageWeight; }

    public Boolean isPrepareOrder() {   return isPrepareOrder;  }
    public void setPrepareOrder(Boolean prepareOrder) { isPrepareOrder = prepareOrder;  }

    public boolean isAllowBlackCat() {  return isAllowBlackCat; }
    public void setAllowBlackCat(boolean allowBlackCat) {   isAllowBlackCat = allowBlackCat;    }
    public Boolean isBlackCatOwnExpense() { return isBlackCatOwnExpense;    }
    public void setBlackCatOwnExpense(Boolean blackCatOwnExpense) { isBlackCatOwnExpense = blackCatOwnExpense;  }
    public Integer getBlackCatShippingFee() {   return blackCatShippingFee; }
    public void setBlackCatShippingFee(Integer blackCatShippingFee) {   this.blackCatShippingFee = blackCatShippingFee; }

    public boolean isAllowFamily() {    return isAllowFamily;   }
    public void setAllowFamily(boolean allowFamily) {   isAllowFamily = allowFamily;    }
    public Boolean isFamilyOwnExpense() {   return isFamilyOwnExpense;  }
    public void setFamilyOwnExpense(Boolean familyOwnExpense) { isFamilyOwnExpense = familyOwnExpense;  }
    public Integer getFamilyShippingFee() { return familyShippingFee;   }
    public void setFamilyShippingFee(Integer familyShippingFee) {   this.familyShippingFee = familyShippingFee; }

    public boolean isAllowSevenEleven() {   return isAllowSevenEleven;  }
    public void setAllowSevenEleven(boolean allowSevenEleven) { isAllowSevenEleven = allowSevenEleven;  }
    public Boolean isSevenElvenOwnExpense() {   return isSevenElvenOwnExpense;  }
    public void setSevenElvenOwnExpense(Boolean sevenElvenOwnExpense) { isSevenElvenOwnExpense = sevenElvenOwnExpense;  }
    public Integer getSevenElevenShippingFee() {    return sevenElevenShippingFee;  }
    public void setSevenElevenShippingFee(Integer sevenElevenShippingFee) { this.sevenElevenShippingFee = sevenElevenShippingFee;   }

    public boolean isAllowHiLife() {    return isAllowHiLife;   }
    public void setAllowHiLife(boolean allowHiLife) {   isAllowHiLife = allowHiLife;    }
    public Boolean isHiLifeOwnExpense() {   return isHiLifeOwnExpense;  }
    public void setHiLifeOwnExpense(Boolean hiLifeOwnExpense) { isHiLifeOwnExpense = hiLifeOwnExpense;  }
    public Integer getHiLifeShippingFee() { return hiLifeShippingFee;   }
    public void setHiLifeShippingFee(Integer hiLifeShippingFee) {   this.hiLifeShippingFee = hiLifeShippingFee; }
}
