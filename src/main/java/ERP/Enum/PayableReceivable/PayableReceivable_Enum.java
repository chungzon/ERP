package ERP.Enum.PayableReceivable;

import java.util.HashMap;

/** [ERP.Enum] Payable or receivable */
public class PayableReceivable_Enum {
    /** Payable or receivable status */
    public enum PayableReceivableStatus{
        建立, 修改
    }
    /** Which way to search payable or receivable */
    public enum SearchMethod{
        關鍵字搜尋, 日期搜尋
    }
    /** Which column to search payable or receivable */
    public enum SearchColumn{
        對象碼, 發票號碼, 票據號碼, 收付款日, 票據到期日
    }

    public enum IAECrawlerSource{
        出納給付, 零用金給付
    }
    public enum IAECrawlerReviewStatus {
        未查核(false),已查核(true);
        boolean isReview;
        IAECrawlerReviewStatus(boolean reviewStatus){
            this.isReview = reviewStatus;
        }
        public boolean isReview(){
            return isReview;
        }
    }
    public enum IAECrawlerStatus{
        新增, 待更新, 忽略, 遺失
    }
    public enum PaymentCompareTabName{
        未查核發票,出納比對,已查核發票,已忽略發票,已遺失發票,採購備記,廠商往來明細
    }
    public enum IAECrawlerDataTableColumn{
        學校,傳票編號,廠商編號,廠商名稱,廠商統編,付款日期,付款金額,銀行帳戶,匯費,摘要,發票日期,發票號碼,發票金額,計畫代碼,來源
    }
    public enum ManufacturerContactDetailSource{
        廠商進貨, 出貨已開發票, 學校出納
    }
    public enum CheckoutStatus_ManufacturerContactDetail {
        全部, 已開發票, 已給付貨款金額
    }
}
