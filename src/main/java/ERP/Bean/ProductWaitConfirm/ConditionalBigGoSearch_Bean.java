package ERP.Bean.ProductWaitConfirm;
import ERP.Bean.ToolKit.BigGoFilter.BigGoFilter_Bean;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.BigGoFilterSource;
import javafx.collections.ObservableList;
import java.util.HashMap;

/** [ERP.Bean] The conditional search for BigGo in waiting confirm */
public class ConditionalBigGoSearch_Bean {
    private String BigGoSearchText;
    private int BatchPrice;
    private Boolean GreaterThanBatchPrice, SortByLargeToSmall;
    private HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap;

    public void setBigGoSearchText(String BigGoSearchText){ this.BigGoSearchText = BigGoSearchText; }
    public String getBigGoSearchText(){   return BigGoSearchText;   }
    public void setBatchPrice(int BatchPrice){   this.BatchPrice = BatchPrice;   }
    public int getBatchPrice(){  return BatchPrice;  }
    public void setGreaterThanBatchPrice(Boolean GreaterThanBatchPrice){    this.GreaterThanBatchPrice = GreaterThanBatchPrice; }
    public Boolean getGreaterThanBatchPrice(){  return GreaterThanBatchPrice;   }
    public void setSortByLargeToSmall(Boolean SortByLargeToSmall){  this.SortByLargeToSmall = SortByLargeToSmall;   }
    public Boolean getSortByLargeToSmall(){ return SortByLargeToSmall;  }
    public HashMap<BigGoFilterSource, ObservableList<BigGoFilter_Bean>> getBigGoFilterMap() {  return bigGoFilterMap;  }
    public void setBigGoFilterMap(HashMap<Product_Enum.BigGoFilterSource, ObservableList<BigGoFilter_Bean>> bigGoFilterMap) {   this.bigGoFilterMap = bigGoFilterMap;   }
}
