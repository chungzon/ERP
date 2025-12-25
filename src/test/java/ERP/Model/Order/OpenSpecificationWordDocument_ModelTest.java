package ERP.Model.Order;

import ERP.Bean.Order.OrderProduct_Bean;
import ERP.ERPApplication;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenSpecificationWordDocument_ModelTest {
    private OpenSpecificationWordDocument_Model OpenSpecificationWordDocument_Model;
    public OpenSpecificationWordDocument_ModelTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);

        ERPApplication.ToolKit = new ToolKit();
        this.OpenSpecificationWordDocument_Model = ERPApplication.ToolKit.ModelToolKit.getOpenSpecificationWordDocument_Model();
    }
    @Test
    void generateSpecificationDocument(){
        String projectName = "展源浩專案名稱";
        ObservableList<OrderProduct_Bean> productList = generateProductList();
        String basicNeedTemplate = "一、雲端影音直播設備及管理系統：須能支援將多部攝影機影像透過網路以1080P以上解析度進行現場影音製作切換編輯。所需設備及軟體模組以及其規格，如項次壹中所述。\n" +
                "二、廠商須配合協助介接校園直播軟體模組整合入校內現行之SSO (Single sign-on)服務及校內單一介面服務平台之管理介面並整合延伸功能之擴充需求。\n" +
                "三、此系統介接校內SSO系統軟體模組須內含：人員管理模組、權限管理模組、操作紀錄查閱模組，上述校園直播軟體模組授權須整合於校內SSO服務及校內單一介面服務平台之管理介面整合延伸功能擴充需求，且此模組及授權方式需為Client-Server架構，須能提供日後其它直播站點需求時擴充設備使用。\n" +
                "四、除上述系統所提供之功能外，須提供上述系統介接之SDK及API以備使用單位日後維護及開發新功能時使用。\n";
        String warrantyAndOtherTemplate = "A.本案履約期限為決標次日起15個日曆天。\n" +
                "B.本案為「智能遠距遙控飛行器暨直播系統」採購皆原廠保固一年(含相關配件與一年內維護等)，保固期間內須提供原廠授權廠商到府維修服務、壞品與良品一對一更換(註:非到府收送)，若無法於第二個工作天內排除硬體或軟體故障問題須提供同規格備機服務，若使用單位發現產品為水貨或非原廠授權之台灣廠商販賣商品，單位有權維護權益更換原廠之產品且保有法律追訴之權利。\n" +
                "C.廠商須提供上述系統之SDK及API以備日後開發新功能時使用。\n" +
                "D.廠商須保留未來擴充之SDK及API與SSO服務之介接方式。\n" +
                "E.廠商須提供所有系統相關安裝、設定、修改及測試，並提供二小時內到府及四小時完修之保固一年。\n" +
                "F.廠商須於保固期內免費提供相同功能之備用系統、以確保系統運作不中斷。\n" +
                "G.廠商須先行與使用單位討論且需實際了解系統需求。\n" +
                "H.上述軟體模組，必要時須能依照使用單位需求調整。";
        String educationTraining = "";

        boolean status = OpenSpecificationWordDocument_Model.generateSpecificationDocument(projectName,basicNeedTemplate,productList,warrantyAndOtherTemplate,educationTraining);
        assertTrue(status);
    }
    private ObservableList<OrderProduct_Bean> generateProductList(){
        ObservableList<OrderProduct_Bean> productList = FXCollections.observableArrayList();
        OrderProduct_Bean OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setSpecificationProductName("影音現場製作切換編輯器");
        OrderProduct_Bean.setQuantity(10);
        OrderProduct_Bean.setUnit("組");
        OrderProduct_Bean.setSpecificationContent("1.HDMI影像输入：4路HDMI A類。可切換10bit SD/HD/Ultra HD 4K影像畫面。\n" +
                "2.HDMI節目输出：1路HDMI A類。可切換10bit SD/HD/Ultra HD 4K影像畫面。\n" +
                "3.SDI影像输入：可切換4路10bit SD/HD/Ultra HD 4K影像畫面。\n" +
                "4.SDI節目输出：可切換1路10bit SD/HD/Ultra HD 4K影像畫面。\n" +
                "5.SDI速率\t：270Mb、1.5G、3G、6G。\n" +
                "6.影像输入接口數：8。\n" +
                "7.影像输出接口數：6。\n" +
                "8.SD支援格式：525i59.94 NTSC、625i50 PAL。\n" +
                "9.混音器：2通道混音器，具10路输入。\n" +
                "10.Media Players：2。\n" +
                "11.Multi View畫面監看：1路10畫面，HD影像格式。\n" +
                "12.操作系統\n" +
                "●Mac OS X 10.10 Yosemite、Mac OS X 10.11 El Capitan或更高版本。\n" +
                "●Windows 8.1 64-bit或Windows 10 64-bit。\n" +
                "13.外觀尺寸：482(長)mm*147(寬)mm*43(高)mm。\n" +
                "14.須提供SDK供系統開發介接使用。");
        productList.add(OrderProduct_Bean);

        OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setSpecificationProductName("智能遠距遙控飛行器暨直播系統");
        OrderProduct_Bean.setQuantity(2);
        OrderProduct_Bean.setUnit("PCS");
        OrderProduct_Bean.setSpecificationContent("1.最大上升速度須可達6 m/s。\n" +
                "2.最大下降速度須可達4 m/s。\n" +
                "3.須可承受最大風速：10 m/s。\n" +
                "4.單次飛行時間須可達30分鐘。\n" +
                "5.衛星定位模組採GPS/GLONASS雙模。\n" +
                "6.懸停精確度：\n" +
                "●垂直：正負0.1公尺(視覺定位正常運作時)；正負0.5公尺(GPS定位正常運作時)。\n" +
                "●水平：正負0.3公尺(視覺定位正常運作時)；正負1.5公尺(GPS定位正常運作時)。\n" +
                "7.雲台穩定系統：三軸(俯仰、橫滾、偏航)。\n" +
                "8.視覺系統速度測量範圍：飛行速度小於等於50公里/s(高度2公尺，光照充足)。\n" +
                "9.障礙物感知範圍：\n" +
                "視覺系統\n" +
                "●0.7~30公尺。\n" +
                "紅外線感知系統\n" +
                "●0.2~7公尺。\n" +
                "10.FOV：\n" +
                "視覺系統\n" +
                "●前/後視：水平60度，垂直±27度。\n" +
                "●下視：前後70度，左右50度。\n" +
                "紅外線感知系統\n" +
                "●水平：70度，垂直±10度。\n" +
                "11.須含攝影機影像感測器：1英吋CMOS；有效像素2000萬。\n" +
                "12.須含攝影機鏡頭：\n" +
                "●FOV 84度。\n" +
                "●焦距8.8mm(35mm格式同級焦距24mm)。\n" +
                "●光圈f/2.8~f/11。\n" +
                "●自動對焦(對焦距離1公尺~∞)。\n" +
                "13.\t照片拍攝模式：\n" +
                "●單張拍攝。\n" +
                "●多張連拍(高速)：3/5/7/10/14畫格。\n" +
                "●定時拍攝(間隔：2/3/5/7/10/15/20/30/60s)。\n" +
                "14.\t錄影解析度：\n" +
                "H.265\n" +
                "●C4K：4096×2160 24/25/30p @100Mbps。\n" +
                "●4K：3840×2160 24/25/30p @100Mbps。\n" +
                "●2.7K：2720×1530 24/25/30p @65Mbps。\n" +
                "●2.7K：2720×1530 48/50/60p @80Mbps。\n" +
                "H.264\n" +
                "●C4K：4096×2160 24/25/30/48/50/60p @100Mbps。\n" +
                "●4K：3840×2160 24/25/30/48/50/60p @100Mbps。\n" +
                "●2.7K：2720×1530 24/25/30p @80Mbps。\n" +
                "●2.7K：2720×1530 48/50/60p @100Mbps。\n" +
                "15.影片最大資料傳輸率可達100 Mbps。\n" +
                "16.圖片格式：JPEG，DNG（RAW），JPEG + DNG。\n" +
                "17.影片格式：MP4/MOV（AVC/H.264；HEVC/H.265）。\n" +
                "18.須能於愛閱館B1及圖書館目前使用之廣告撥放機進行直播，其已具備功能及規格如下。\n" +
                "●此直播模組及授權方式為Client-Server架構，且Server可提供其它直播站點需求擴充使用。\n" +
                "●人員管理模組：透過校內之SSO服務載入使用人之基本資料。取得之資料內容須能包含使用人相關資訊用以判斷此人員是否具有權限，以及其權限可以使用之相對應功能。\n" +
                "●權限管理模組：須可透過校內SSO系統設定管理權限，以及其相關權限所對應允許開放直播以及跑馬燈內容的編輯。\n" +
                "●操作紀錄查閱模組：須可透過校內SSO系統登入後依照身分別進行調閱操作及撥放之紀錄。\n" +
                "●上述模組傳送之TCP Socket封包於傳送前或接收後，皆經過Base64 Encode / Decode流程外，還包含加密機制及其它之Encode / Decode演算法。\n" +
                "●如額外提供直播模組，則除上述系統所提供之功能外，尚須提供上述系統介接之SDK及API以備使用單位日後維護及開發新功能時使用。\n" +
                "●直播之即時影音串流須能提供於現有之影音現場製作切換編輯器使用(註：BMD ATEM 1 M/E Production Studio 4K現場製作切換台)。\n" +
                "●須整合於校內SSO(Single sign-on)服務及校內單一介面服務平台之管理介面並整合延伸功能擴充需求。");
        productList.add(OrderProduct_Bean);

        OrderProduct_Bean = new OrderProduct_Bean();
        OrderProduct_Bean.setSpecificationProductName("360度環景攝影機");
        OrderProduct_Bean.setQuantity(4);
        OrderProduct_Bean.setUnit("組");
        OrderProduct_Bean.setSpecificationContent("1.物距：約 10cm 至無限遠（從鏡頭前端起算）。\n" +
                "2.拍攝模式：靜態圖像/視訊/即時串流：自動。\n" +
                "3.ISO感光度\n" +
                "●靜態圖像：ISO100–1600。\n" +
                "●視訊：ISO100–1600。\n" +
                "●即時串流：ISO100–1600。\n" +
                "4.錄製介質：8GB內建記憶體。\n" +
                "5.外部介面\n" +
                "●微型USB埠USB 2.0。\n" +
                "●HDMI微型埠(D型) HDMI 1.4。\n" +
                "6.有效畫素/輸出畫素： 1200萬畫素/等同1400萬畫素。\n" +
                "7.靜態影像解析度：大5376 x 2688、中2048 x 1024。\n" +
                "8.視訊解析度/畫面播放速率/位元速率：大1920 x 1080/30 fps/16 Mbps、中1280 x 720/15 fps/6 Mbps。\n" +
                "9.即時串流解析度/畫面播放速率（USB）：大1920 × 1080/30fps、中1280 × 720/15fps (Windows 7中的解析度與畫面播放速率為1280 × 720與15fps) 。\n" +
                "10.即時串流解析度/畫面播放速率（HDMI）：大1920 x 1080/30 fps、中1280 x 720/30 fps、小720 x 480/30 fps。(自動變更以符合顯示器)。\n" +
                "11.Wi-Fi通訊協定：HTTP (相容於Open Spherical Camera API *7) 。");
        productList.add(OrderProduct_Bean);
        return productList;
    }

}