package ERP.Bean.ToolKit.ProductGroup;

public class ItemGroup_Bean {
    private Integer id,group_id;
    private int item_id,item_quantity;
    private String isbn, productName;

    public ItemGroup_Bean(Integer id,int item_id,int item_quantity,Integer group_id, String isbn, String productName){
        this.id = id;
        this.item_id = item_id;
        this.item_quantity = item_quantity;
        this.group_id = group_id;
        this.isbn = isbn;
        this.productName = productName;
    }

    public Integer getId() {    return id;  }

    public void setItem_id(int item_id) {   this.item_id = item_id; }
    public int getItem_id() {   return item_id; }

    public int getItem_quantity() { return item_quantity;   }

    public Integer getGroup_id() {  return group_id;    }

    public String getIsbn() {   return isbn; }

    public String getProductName() {    return productName; }
}
