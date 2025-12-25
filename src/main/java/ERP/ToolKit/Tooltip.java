package ERP.ToolKit;

public class Tooltip {
    public String showSeparateOrder(){
        return "「正常」子貨單：需選擇商品才能建立\n「沖帳」子貨單：不需要選擇商品亦能建立";
    }
    public String establishOrder(){
        return "F3：跳回主碼欄位\nF4：跳至成本\nF5：跳至單價\nCtrl+S：儲存貨單\n【廠商(客戶)編號、名稱】\n    F8：修改廠商、客戶資訊\n【主碼】\n    F1：新增商品\n    F2：複製商品\n    F6：商品查詢\n" +
                "【表格商品】\n    Ctrl+Q：商品群組關聯\n    F8：商品價格\n    Shift+Up/Down：移動商品順序\n" +
                "【查詢功能】\n    右鍵：貨單總覽";
    }
    public String exportQuotation(){
        return "【二聯金額】\n    (1)單價 * 稅額 = 含稅價(四捨五入)\n    (2)含稅價 * 數量 = 小計(整數)\n【三聯金額】\n    (1)單價 * 數量 = 小計(未稅)\n    (2)小計 + 稅額(四捨五入) = 總計(整數)";
    }
    public String order_defaultProductConnection_switchButton(){
        return "【商品關聯總覽】\n    開啟 - 新增商品後自動開啟畫面\n    關閉 - 點選商品後快捷鍵：ctrl+q";
    }
    public String order_offsetStatus_switchButton(){
        return "【冲帳狀態】\n    開啟 - 全冲帳\n    關閉 - 部分冲帳";
    }
    public String order_transferQuotation_switchButton(){
        return "【採購、報價單互轉】\n    開啟 - 轉報價單\n    關閉 - 轉採購單\n    * 報價轉採購 - 採購單的商品成本參考報價單\n    * 報價轉報價 - 商品金額是否參照該此貨單";
    }
    public String order_productGroup_switchButton(){
        return "【商品群組】\n    開啟 - 商品群組\n    關閉 - 商品明細";
    }
    public String modifyProduct(){
        return "「選取功能」：\n    是否一同修改採購、報價單上資訊(VipPrice1～3一律修改)\n「金額名稱」Highlight：\n    客戶商品的出售模式\n「左邊資訊」紅色字體：\n    該貨單上的商品資訊與 store 不同\n「右邊資訊」紅色字體、「打勾」自動勾選且 Highlight：\n    該欄位資訊與【左邊資訊】不同";
    }
    public String transactionDetail(){
//        return "「庫存明細」計算貨單：\n    (1) 進(出)貨單\n    (2) 進(出)貨退貨單\n    (3) 已轉進(出)貨的子貨單\n\n☑ 包含待入倉(待出貨)單：\n    【庫存明細】納入計算";
        return "※ 以下貨單加總為「庫存明細」計算方式\n    (1) 進(出)貨單\n    (2) 部分沖帳之進(出)貨單\n    (3) 非沖帳且已轉進(出)貨之子貨單\n    (4) 進(出)貨退貨單\n\n☑ 包含待入倉(待出貨)單：\n    【庫存明細】納入計算";
    }
    public String showPictureEditor(){
        return "【長按滑鼠左鍵】路徑塗鴉\n【長按滑鼠右鍵】路徑清除\n【連點滑鼠右鍵】清除該次路徑塗鴉";
    }
    public String showOrderReference(){
        return "【出貨單】優先\n    ☑ 出貨單日期\n    □ 報價單日期\n【計算載入】      (沖帳單不納入計算)\n    (1)相同客戶編號\n    (2)同編號的情況下，根據勾選優先... 出貨單；報價單\n    (3)不同客戶編號下，則以最近的出貨單為考量，無出貨單則改以報價單為考量";
    }
    public String searchOrder(){
        return "編號/名稱查詢：「純數字」搜尋時只搜尋編號\n全部：根據以下規則\n    (1)單號搜尋 - 純數字且符合單號長度\n    (2)發票搜尋 - 符合發票規則\n    (3)廠商(客戶)名稱、編號與專案名稱";
    }
    public String searchOrder_ConditionalSearch(){
        return "【全部 - 欄位模糊搜尋】\n    開啟 - 字串間「and」搜尋\n    關閉 - 字串間「or」搜尋";
    }
    public String searchOrderProgress(){
        return "貨單進度查詢欄位：目前只能根據廠商、客戶與專案名稱";
    }
    public String establishPayReceiveFromOrder(){
        return "如果子貨單【已結帳】，則母貨單無法建立【分期收/付款】\n\n待入倉(出貨)單【建立應收/付款】功能使用如下：\n    (1) 有建立【分期收/付款】：\n            不管有無子貨單，都可以【建立應收/付帳款】\n    (2) 沒有建立【分期收/付款】：\n            有建立子貨單(未轉進、出貨)會無法【建立應收/付帳款】";
    }
    public String establishPayReceiveFromSubBill(){
        return "如果母貨單已建立【分期收/付款】，則子貨單無法建立【應收應付帳款】";
    }
    public String searchPayReceive(){
        return "「查詢貨單」搜尋條件：\n    * 如果母貨單已建立【分期收/付款】，且存在子貨單，則以母貨單的【分期收/付款】為最高優先";
    }
    /*(1) 如果母貨單已建立【分期收/付款】，則子貨單無法建立【應收應付帳款】
    (2) 如果子貨單【已結帳】，則待入倉(出貨)單無法建立【分期收/付款】*/
    public String iaeCompare(){
        return "「相似貨單」搜尋條件：\n    (1) 發票未做廢且發票號碼吻合\n    (2) 出納存在發票 - 貨單(商品群組)金額符合發票金額\n    (3) 出納不存在發票 - 貨單(商品群組)金額符合付款金額\n「綁定貨單」自動綁定條件：\n    (1) 發票號碼與廠商吻合\n\n** 特殊條件 **\n    無發票號碼的出納款項可以綁定多張貨單";
    }
    public String iaeCompare_searchOrder(){
        return "「搜尋廠商貨單」搜尋條件：\n    (1) 根據起始、結束日期來搜尋(出貨單日期)\n    (2) 已建立該廠商發票且未與出納綁定的出貨(子貨)單";
    }
    public String purchaseNote(){
        return "「採購備記」搜尋條件：\n    (1) 有建立發票的出貨單(必須帶入報價單廠商)\n    (2) 搜尋的日期為發票日期";
    }
    public String manufacturerContactDetail(){
        return "「廠商往來」搜尋條件：\n    (1) 搜尋所選廠商的進貨單\n    (2) 綁定發票是該廠商的出貨單(未與出納帳戶綁定)\n    (3) 學校已給付的出貨單(出貨單與出納帳戶綁定)\n\n【學校給付金額】可以編輯";
    }
    public String waitingConfirm(){
        return "【表格商品】\n    F8：修改總類(只允許 CheckStore)";
    }
    public String waitingConfirm_comparePrice(){
        return "「匯入比價金額」「BigGo新增進銷存」金額來源：\n    (1) 單價 - 商城:最小金額\n    (2) 定價 - 商城:最大金額\n    (3) VipPrice1 - 拍賣:最大金額\n    (4) VipPrice2 - 拍賣:最小與最大金額的平均值\n    (5) VipPrice3 - 拍賣:最小金額";
    }
    public String waitingConfirm_ConditionalSearch(){
        return "【品名模糊搜尋】\n    開啟 - 字串間「and」搜尋\n    關閉 - 字串間「or」搜尋";
    }
}