package ERP.Enum.Invoice;

public class Invoice_Enum {
    public enum SearchSource {
        ID,單號,專案名稱
    }
    public enum InvoiceType{
        二聯式, 三聯式
    }
    public enum InvoiceInvalid{
        未作廢(false), 已作廢(true);
        private boolean isValid;
        InvoiceInvalid(boolean isValid) {  this.isValid = isValid; }
        public boolean isValid(){
            return isValid;
        }
    }
    public enum InvoiceIgnore{
        未忽略(false),已忽略(true);
        private boolean isIgnore;
        InvoiceIgnore(boolean isIgnore){
            this.isIgnore = isIgnore;
        }
        public boolean isIgnore(){
            return isIgnore;
        }
    }
    public enum InvoiceSearchType{
        貨單,發票
    }
}
