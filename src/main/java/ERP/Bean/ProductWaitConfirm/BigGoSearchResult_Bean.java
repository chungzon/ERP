package ERP.Bean.ProductWaitConfirm;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class BigGoSearchResult_Bean {

    private int nowPage, totalPage;
    private String hyperlink;
    private ArrayList<BigGoProductInfo_Bean> productList;

    private VBox VBox;
    private ObservableList<Integer> bigGoPageList;
    public BigGoSearchResult_Bean(VBox VBox){
        this.VBox = VBox;
    }

    public int getNowPage() {   return nowPage; }
    public void setNowPage(int nowPage) {   this.nowPage = nowPage; }

    public int getTotalPage() { return totalPage;   }
    public void setTotalPage(int totalPage) {   this.totalPage = totalPage; }

    public String getHyperlink() {  return hyperlink;   }
    public void setHyperlink(String hyperlink) {    this.hyperlink = hyperlink; }

    public ArrayList<BigGoProductInfo_Bean> getProductList() {  return productList; }
    public void setProductList(ArrayList<BigGoProductInfo_Bean> productList) {  this.productList = productList; }

    public ObservableList<Integer> getBigGoPageList() {  return bigGoPageList;    }
    public void setBigGoPageList(ObservableList<Integer> bigGoPageList) { this.bigGoPageList = bigGoPageList;   }

    public VBox getVBox() { return VBox;    }
}
