package ERP.Bean.ToolKit.ProductGenerator;

import ERP.Bean.Order.OrderProduct_Bean;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ProductGenerator_Bean {

    private int ProductQuantity, SinglePrice, duplicateProductDate, TotalPrice;
    private ArrayList<String> FirstCategoryIDList;
    private boolean isRemoveDuplicateProduct, isIncludeWaitingConfirmProduct;

    private ObservableList<OrderProduct_Bean> saveProductList;
    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public int getSinglePrice() {
        return SinglePrice;
    }

    public void setSinglePrice(int singlePrice) {
        SinglePrice = singlePrice;
    }

    public int getDuplicateProductDate() {
        return duplicateProductDate;
    }

    public void setDuplicateProductDate(int duplicateProductDate) {
        this.duplicateProductDate = duplicateProductDate;
    }

    public int getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public ArrayList<String> getFirstCategoryIDList() {
        return FirstCategoryIDList;
    }

    public void setFirstCategoryIDList(ArrayList<String> firstCategoryIDList) {
        FirstCategoryIDList = firstCategoryIDList;
    }

    public boolean isRemoveDuplicateProduct() {
        return isRemoveDuplicateProduct;
    }

    public void setRemoveDuplicateProduct(boolean removeDuplicateProduct) {
        isRemoveDuplicateProduct = removeDuplicateProduct;
    }

    public boolean isIncludeWaitingConfirmProduct() {   return isIncludeWaitingConfirmProduct;  }
    public void setIncludeWaitingConfirmProduct(boolean includeWaitingConfirmProduct) { isIncludeWaitingConfirmProduct = includeWaitingConfirmProduct;  }

    public ObservableList<OrderProduct_Bean> getSaveProductList() {
        return saveProductList;
    }

    public void setSaveProductList(ObservableList<OrderProduct_Bean> saveProductList) {
        this.saveProductList = saveProductList;
    }
}
