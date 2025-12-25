package ERP.Bean.Product;

import java.time.LocalDate;

public class PreparativeProduction_Bean {
    private int id, Store_id, category, attribute, stock;
    private String ISBN, productName,attributeOption,descript;
    private double price,packageWeight;
    private int packageWidth,packageHeight,packageLength,daysToShip;
    private boolean preOrder;
    private LocalDate createDateTime,updateDateTime;
    private boolean isAcution;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return Store_id;
    }

    public void setStore_id(int store_id) {
        Store_id = store_id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAttributeOption() {
        return attributeOption;
    }

    public void setAttributeOption(String attributeOption) {
        this.attributeOption = attributeOption;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public int getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(int packageWidth) {
        this.packageWidth = packageWidth;
    }

    public int getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(int packageHeight) {
        this.packageHeight = packageHeight;
    }

    public int getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(int packageLength) {
        this.packageLength = packageLength;
    }

    public int getDaysToShip() {
        return daysToShip;
    }

    public void setDaysToShip(int daysToShip) {
        this.daysToShip = daysToShip;
    }

    public boolean isPreOrder() {
        return preOrder;
    }

    public void setPreOrder(boolean preOrder) {
        this.preOrder = preOrder;
    }

    public LocalDate getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDate createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDate getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDate updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public boolean isAcution() {
        return isAcution;
    }

    public void setAcution(boolean acution) {
        isAcution = acution;
    }
}
