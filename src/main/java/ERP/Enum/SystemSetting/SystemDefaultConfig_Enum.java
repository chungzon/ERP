package ERP.Enum.SystemSetting;

public class SystemDefaultConfig_Enum {
    public enum SystemDefaultName{
        UploadPictureFunction, Order_SnapshotCamera, Order_DefaultProductConnection, Order_SearchFunction, Order_PrintFunction, Order_ExportPurchaseFunction, Order_ExportPayReceiveDetails ,
        WaitingConfirm_DefaultSearchProductNameFunction,SearchOrder_DefaultSearchTextFunction
    }
    public enum UploadPictureFunction{
        PastePicture("已複製圖片"), UploadLocation("本地上傳");
        String describe;
        UploadPictureFunction(String describe){
            this.describe = describe;
        }
        public String getDescribe(){
            return describe;
        }
    }
    public enum Order_SnapshotCamera{
        IPCamera, WebCamera
    }
    public enum Order_DefaultProductConnection{
        CloseProductConnection, OpenProductConnection
    }
    public enum Order_SearchFunction{
        TransactionDetail("商品交易明細"), ProductWaitingConfirm("商品待確認");
        String describe;
        Order_SearchFunction(String describe){
            this.describe = describe;
        }
        public String getDescribe(){
            return describe;
        }
    }
    public enum Order_PrintFunction{
        PrintOrder("貨單匯出/列印"), PrintLabel("標籤列印");
        String describe;
        Order_PrintFunction(String describe){
            this.describe = describe;
        }
        public String getDescribe(){
            return describe;
        }
    }
    public enum Order_ExportPurchaseFunction{
        PurchaseOrder("採購單"), AskPriceOrder("詢價單");
        String describe;
        Order_ExportPurchaseFunction(String describe){
            this.describe = describe;
        }
        public String getDescribe(){    return describe;    }
    }
    public enum Order_ExportPayReceiveDetails{
        export("匯出"), print("列印");
        String describe;
        Order_ExportPayReceiveDetails(String describe){
            this.describe = describe;
        }
        public String getDescribe(){
            return describe;
        }
    }
    public enum WaitingConfirm_DefaultSearchProductNameFunction{
        Or, And
    }
    public enum SearchOrder_DefaultSearchTextFunction{
        Or, And
    }
}
