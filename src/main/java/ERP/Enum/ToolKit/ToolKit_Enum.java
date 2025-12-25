package ERP.Enum.ToolKit;

import ERP.Controller.Order.EstablishOrder.EstablishOrder_Controller;

import java.util.ArrayList;

public class ToolKit_Enum {
    public enum Authority{
        管理者(new ArrayList<String>(){{ add("waylen");    add("bagsong888");  add("168");  }}),
        用戶(new ArrayList<String>(){{ add("waylen");    add("bagsong888"); add("168");   add("qwert");   }});
        private ArrayList<String> authorizationCode;
        Authority(ArrayList<String> authorizationCode){
            this.authorizationCode = authorizationCode;
        }
        public ArrayList<String> getAuthorizationCode() { return this.authorizationCode;  }
    }
    public enum AuthorizedLocation {
        版本控制,變更貨單對象,變更貨單審查狀態,修改貨單審查紀錄,變更貨單金額,校正存量,刪除作廢發票的貨單,應收應付帳款修改與刪除, 綁定發票不符合的出納資料
    }
    public enum SnapshotPictureLocation{
        貨單, 審查紀錄
    }
    public enum OrderKeyCombination {
        上移, 下移
    }
    public enum UpOrDownFunction {
        previous, next
    }
    public enum ChineseMonth{
        一月份,二月份,三月份,四月份,
        五月份,六月份,七月份,八月份,
        九月份,十月份,十一月份,十二月份
    }
    public enum ConvertMathToChinese{
        零("零",""),一("壹","拾"),二("貳","佰"),三("參","仟"),四("肆","萬"),
        五("伍","拾"),六("陸","佰"),七("柒","仟"),八("捌","億"),九("玖","拾");
        String ChineseMath, ChineseUnit;
        ConvertMathToChinese(String ChineseMath, String ChineseUnit){
            this.ChineseMath = ChineseMath;
            this.ChineseUnit = ChineseUnit;
        }
        public String getChineseMath(){
            return ChineseMath;
        }
        public String getChineseUnit(){
            return ChineseUnit;
        }
    }
    public enum ConvertMathToUpperEnglish {
        A("a"),B("b"),C("c"),D("d"),E("e"),F("f"),G("g"),H("h"),I("i"),J("j"),K("k"),L("l"),M("m"),N("n"),O("o"),P("p"),Q("q"),R("r"),S("s"),T("t"),U("u"),V("v"),W("w"),X("x"),Y("y"),Z("z");
        String lowerCase;
        ConvertMathToUpperEnglish(String lowerCase){
            this.lowerCase = lowerCase;
        }
        public String getLowerCase(){
            return lowerCase;
        }
    }
    public enum ConvertMathToLowerEnglish {
        a("A"),b("B"),c("C"),d("D"),e("E"),f("F"),g("G"),h("H"),i("I"),j("J"),k("K"),l("L"),m("M"),n("N"),o("O"),p("P"),q("Q"),r("R"),s("S"),t("T"),u("U"),v("V"),w("W"),x("X"),y("Y"),z("Z");
        String upperCase;
        ConvertMathToLowerEnglish(String upperCase){
            this.upperCase = upperCase;
        }
        public String getUpperCase(){
            return upperCase;
        }
    }
    public enum ColorCycle {
        lightBlue("#90CAF9"), orange("#F2B980"), blueGreen("#41C7D1"), lightPink("FFCDC8"),
        deepPink("#F48FB1"), lightGreen("#C5E1A5"), lightRed("#DCB8AB"), lightYellow("#ECD76F");
        String rgb;
        ColorCycle(String rgb){
            this.rgb = rgb;
        }
        public String getRGB(){ return rgb; }
    }
}
