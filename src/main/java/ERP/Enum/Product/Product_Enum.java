package ERP.Enum.Product;

/** [ERP.Enum] Product */
public class Product_Enum {
    /** Manage product status */
    public enum ManageProductStatus{
        建立, 搜尋
    }
    /** Product store status */
    public enum StoreStatus {
        未確認, 已確認, 封存
    }
    /** Product category layer */
    public enum CategoryLayer{
        大類別(1,"NewFirstCategory"), 中類別(2,"NewSecondCategory"), 小類別(3,"NewThirdCategory");
        private int Layer;
        private String ColumnName;
        CategoryLayer(int Layer, String ColumnName) {
            this.Layer = Layer;
            this.ColumnName = ColumnName;
        }
        public int getLayer(){  return this.Layer; }
        public String getColumnName() { return this.ColumnName;  }
    }
    /** The bid that the product on shelf */
    public enum BidStore{
        網站("",""), 奇摩("category_yahoo","YahooOnShelf"), 露天("category_ruten","RutenOnShelf"), 蝦皮("category_shopee","ShopeeOnShelf");

        private String TableName, BidOnShelfTableName;
        BidStore(String TableName, String BidOnShelfTableName) {
            this.TableName = TableName;
            this.BidOnShelfTableName = BidOnShelfTableName;
        }
        public String getTableName() { return this.TableName;  }
        public String getBidOnShelfTableName(){ return this.BidOnShelfTableName;    }
    }
    /** The bid that the product status */
    public enum BidStoreStatus{
        待上架, 已上架, 已下架, 線上待下架, 線上待修改
    }
    public enum BigGoPriceSettingColumn{
        manufacturerCode, singlePrice, pricing, vipPrice1, vipPrice2, vipPrice3, taxStatus;
        public enum priceKey{
            max, min, average, percentage
        }
    }
    /** Which table exist product in database */
    public enum WaitConfirmTable{
        全部("AllStore"),進銷存商品("Store"),待確認商品("CheckStore");
        private String value;
        WaitConfirmTable(String value) {  this.value = value; }
        public String getTableName() { return this.value;  }
    }
    /** Waiting confirm status*/
    public enum WaitConfirmStatus{
        新增, 更新, 封存, 忽略
    }
    /** Edit product describe or remark in waiting confirm */
    public enum EditProductDescribeOrRemark{
        編輯商品描述("Describe"), 編輯商品備註("Remark");
        private String ColumnName;
        EditProductDescribeOrRemark(String ColumnName) {  this.ColumnName = ColumnName; }
        public String getColumnName() { return this.ColumnName;  }
    }
    /** The source of BigGo filter */
    public enum BigGoFilterSource {
        未篩選, 商城, 拍賣
    }
    /** The product status in waiting confirm */
    public enum TableViewButtonStatus{
        新增, 更新, 封存, 忽略, 刪除, 復原
    }
    /** The product status of CheckStore in waiting confirm */
    public enum CheckStoreStatus {
        新增, 更新, 封存, 忽略
    }
    /** The vendor in waiting confirm */
    public enum Vendor{
        未選擇(0,"",""),
        建達國際(778,"Xander","category_xander"),
        廣鐸企業(750,"Ktnet","category_ktnet"),
        Genuine捷元(779,"Genb2b","category_genb2b"),
        聯強國際(777,"Synnex","category_synnex"),
        精技電腦(776,"Unitech","category_unitech"),
        展碁國際(781,"Weblink","category_weblink"),
        神腦國際(787,"Senao","category_senao"),
        精豪電腦(780,"Jinghao","category_Jinghao");
        private int VendorCode;
        private String EnglishName,CategoryTable;
        Vendor(int VendorCode, String EnglishName, String CategoryTable) {
            this.VendorCode = VendorCode;
            this.EnglishName = EnglishName;
            this.CategoryTable = CategoryTable;
        }
        public int getVendorCode() { return this.VendorCode;  }
        public String getEnglishName(){ return this.EnglishName;    }
        public String getCategoryTable(){   return this.CategoryTable;  }
    }
    /** Single update status in waiting confirm */
    public enum SingleUpdateStatus{
        更新完成, Cookie過期, 驗證碼錯誤, 商品下架
    }
    public enum ManageProductInfoJsonConfigKey{
        商品查詢欄位, 進階搜尋欄位
    }
    /** The TableColumn in TableView */
    public enum ManageProductInfoTableColumn{
        選擇, 店內碼,國際碼,商品碼,品名,單位,廠商編號,廠商名稱,大類別,中類別,小類別,ProductCode,成本價,單價,市價,
        Vip金額1,Vip金額2,Vip金額3,存量,安全存量,折扣數,盤點日期,進貨日期,銷售日期,出貨日期,更新日期;
    }
    public enum AdvancedSearchColumn {
        ISBN, 國際碼, 商品碼, 品名, 廠商編號
    }
    public enum BlackCatShippingFee{
        運費1("0 ~ 60 cm",90), 運費2("61 ~ 90 cm",130), 運費3("91 ~ 120 cm",170), 運費4("121 ~ 150 cm",210);
        private String name;
        private int shippingFee;
        BlackCatShippingFee(String name, int shippingFee){
            this.name = name;
            this.shippingFee = shippingFee;
        }
        public String getName(){    return name;    }
        public int getShippingFee(){    return shippingFee; }
    }
}

