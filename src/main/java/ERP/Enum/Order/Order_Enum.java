package ERP.Enum.Order;

/** [ERP.Enum] Order */
public class Order_Enum {
    public enum OrderStatus{
        有效貨單, 刪除貨單, 併單, 比對貨單
    }

    /** Whether the Order is borrowed */
    public enum OrderBorrowed{
        未借測(false), 已借測(true);
        private boolean value;
        OrderBorrowed(boolean value) {  this.value = value; }
        public boolean value() { return this.value;  }
    }
    /** Whether the Order is checkout */
    public enum CheckoutStatus{
        未結帳(false), 已結帳(true);
        private boolean value;
        CheckoutStatus(boolean value) {  this.value = value; }
        public boolean value() { return this.value;  }
    }
    /** Whether the Order is offset */
    public enum OffsetOrderStatus{
        未沖帳(false,"未沖"), 全沖帳(true,"全沖"), 部分沖帳(true,"部分沖");
        private boolean value;
        private String describe;
        OffsetOrderStatus(boolean value, String describe) {
            this.value = value;
            this.describe = describe;
        }
        public boolean value() { return this.value;  }
        public String getDescribe() {   return describe;    }
    }
    /** The source of the Order */
    public enum EstablishSource{
        人工建立, 系統建立
    }
    /** Export quotation with which content */
    public enum ExportQuotationContent{
        專案, 項目明細
    }
    /** Export quotation with which format */
    public enum ExportQuotationFormat{
        二聯, 三聯
    }
    /** Export quotation with which vendor */
    public enum ExportQuotationVendor{
        noneInvoice("無發票章");
        String describe;
        ExportQuotationVendor(String describe){
            this.describe = describe;
        }
        public String getDescribe() {
            return describe;
        }
    }
    /** Order source */
    public enum OrderSource{
        採購單("採購","Orders","Orders_Items","OrderNumber", Order_Enum.OrderObject.廠商),
        報價單("報價","Orders","Orders_Items","OrderNumber", Order_Enum.OrderObject.客戶),

        待入倉單("待入倉","Orders","Orders_Items","WaitingOrderNumber", Order_Enum.OrderObject.廠商),
        待出貨單("待出貨","Orders","Orders_Items","WaitingOrderNumber", Order_Enum.OrderObject.客戶),

        進貨子貨單("進子","SubBill","SubBill_Items","OrderNumber", Order_Enum.OrderObject.廠商),
        出貨子貨單("出子","SubBill","SubBill_Items","OrderNumber", Order_Enum.OrderObject.客戶),

        進貨單("進貨","Orders","Orders_Items", "AlreadyOrderNumber", Order_Enum.OrderObject.廠商),
        出貨單("出貨","Orders","Orders_Items","AlreadyOrderNumber", Order_Enum.OrderObject.客戶),

        進貨退貨單("進退","ReturnOrder","ReturnOrder_Items","OrderNumber", Order_Enum.OrderObject.廠商),
        出貨退貨單("出退","ReturnOrder","ReturnOrder_Items","OrderNumber", Order_Enum.OrderObject.客戶);

        private String OrderNickName, OrderTableName, OrderItemsTableName, OrderNumberColumnName;
        private OrderObject OrderObject;
        OrderSource(String OrderNickName, String OrderTableName, String OrderItemsTableName, String OrderNumberColumnName,OrderObject OrderObject) {
            this.OrderNickName = OrderNickName;
            this.OrderTableName = OrderTableName;
            this.OrderItemsTableName = OrderItemsTableName;
            this.OrderNumberColumnName = OrderNumberColumnName;
            this.OrderObject = OrderObject;
        }
        public String getOrderNickName(){   return this.OrderNickName;  }
        public String getOrderTableName() { return this.OrderTableName;  }
        public String getOrderItemsTableName(){ return this.OrderItemsTableName;  }
        public String getOrderNumberColumnName(){   return this.OrderNumberColumnName;  }
        public OrderObject getOrderObject(){    return this.OrderObject;    }
    }
    /** ※ Product sale status */
    public enum ProductSaleStatus{
        新增存量, 新增待入倉量, 新增待入庫存, 新增需量, 判斷需量, 新增待出貨量, 判斷待入庫存,
        刪除存量, 刪除待入倉量, 刪除待入庫存, 刪除需量, 刪除待出貨量
    }
    /** The source of search order */
    public enum SearchOrderSource{
        採購單, 待入倉與子貨單, 進貨與進退貨單, 進貨單, 進貨退貨單,
        報價單, 待出貨與子貨單, 出貨與出退貨單, 出貨單, 出貨退貨單
    }
    /** Whether the order is exist */
    public enum OrderExist{
        未存在(false), 已存在(true);
        private boolean value;
        OrderExist(boolean value) {  this.value = value; }
        public boolean value() { return this.value;  }
    }
    /** Generate order number in which method */
    public enum generateOrderNumberMethod{
        日期, 單號
    }
    /** The object of order */
    public enum OrderObject{
        廠商("Manufacturer","應付"),
        客戶("Customer","應收");
        private String TableName, ObjectIDTableColumn, PayableReceivableName;
        OrderObject(String TableName, String PayableReceivableName) {
            this.TableName = TableName;
            this.PayableReceivableName = PayableReceivableName;
        }
        public String getTableName() { return this.TableName;  }
        public String getPayableReceivableName(){   return this.PayableReceivableName; }
    }
    /** Which column to search object */
    public enum ObjectSearchColumn{
        客戶編號("ObjectID"), 客戶名稱("ObjectName"), 廠商編號("ObjectID"), 廠商名稱("ObjectName");
        private String value;
        ObjectSearchColumn(String value) {  this.value = value; }
        public String value() { return this.value;  }
    }
    public enum ShowObjectSource{
        建立廠商或客戶,建立貨單,採購或報價單互轉,待出貨庫存不足轉採購,建立應收應付帳款,出納帳戶, 待確認_Store參數
    }
    /** Which column to search product */
    public enum ProductSearchColumn{
        ISBN("ISBN"), 標籤("Tag"), 品名("ProductName");
        private String value;
        ProductSearchColumn(String value) {  this.value = value; }
        public String value() { return this.value;  }
    }
    /** Whether the order's tax status */
    public enum OrderTaxStatus{
        未稅(false), 含稅(true);
        private boolean value;
        OrderTaxStatus(boolean value) {  this.value = value; }
        public boolean value() { return this.value;  }
    }
    /** Which column to search order */
    public enum OrderSearchColumn{
        對象編號與名稱("Object"),
        對象與專案名稱(""),
        專案編號("ProejctCode"),
        發票號碼("InvoiceNumber"),
        採購單號("OrderNumber"),待入倉單號("WaitingOrderNumber"),進貨單號("AlreadyOrderNumber"),進退貨單號("OrderNumber"),
        報價單號("OrderNumber"), 待出貨單號("WaitingOrderNumber"), 子貨單號("OrderNumber"), 出貨單號("AlreadyOrderNumber"), 出退貨單號("OrderNumber");
        private String value;
        OrderSearchColumn(String value) {  this.value = value; }
        public String value() { return this.value;  }
    }
    /** Which way to search order */
    public enum OrderSearchMethod{
        關鍵字搜尋, 日期搜尋
    }

    /** The TableColumn in TableView */
    public enum SearchOrderTableColumn{
        建立來源("EstablishSource"),
        單別("OrderSource"),
        單號("OrderNumber"),
        日期("OrderDate"),
        對象編號("ObjectID"),
        對象名稱("ObjectName"),
        項目個數("NumberOfItems"),
        總金額("TotalPriceIncludeTax"),
        發票號碼("InvoiceNumber"),
        專案編號("ProjectCode"),
        專案名稱("ProjectName"),
        報價單匯出廠商("ExportQuotationManufacturerNickNameColumn"),
        審查狀態("ReviewStatus"),
        出納備註("CashierRemark"),
        專案價("ProjectTotalPriceIncludeTax"),
        差額("ProjectDifferentPrice"),
        餘額("Balance"),
        結帳與否("isCheckout"),
        是否沖帳("isOffset"),
        圖片數量("pictureCount"),
        報價日期("QuotationDate"),
        報價單號("QuotationNumber"),
        待處理貨單日期("WaitingOrderDate"),
        待處理貨單單號("WaitingOrderNumber"),
        已處理貨單日期("AlreadyOrderDate"),
        已處理貨單單號("AlreadyOrderNumber");
        private String ColumnValue;
        SearchOrderTableColumn(String ColumnValue) {  this.ColumnValue = ColumnValue; }
        public String ColumnValue() { return this.ColumnValue;  }
    }
    public enum SpecificationColumn{
        基本需求, 保固維護與其他, 教育訓練;
    }
    public enum ReviewObject{
        貨單商品, 商品群組
    }
    public enum ReviewStatus{
        無, 待審查, 待修改, 審查通過
    }
}
