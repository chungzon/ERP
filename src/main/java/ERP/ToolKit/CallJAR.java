package ERP.ToolKit;

import ERP.ERPApplication;
import ERP.View.DialogUI;
import javafx.application.Platform;

/** Call Jar file */
public class CallJAR {

    /** 標籤列印 */
    public void Label_Printer(String ISBN){
        try {
            Runtime.getRuntime().exec("java -jar JAR/Label_Printer.jar " + ISBN);
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** 盤點 */
    public void Label_Reader(){
        try {
            Runtime.getRuntime().exec("java -jar JAR/Label_Reader.jar");
        } catch (Exception Ex) {
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** 維修管理系統 */
    public void Repair_Generate(){
        try {
            Runtime.getRuntime().exec("java -jar JAR/repair-2.0.jar");
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** 發票黏貼明細
     * @param args the parameters of invoice detail to paste
     * */
    public void InvoiceBuilder(String args){
        try {
            Runtime.getRuntime().exec("java -jar JAR/InvoiceBuilder-2.0.jar " + args);
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** 完工報告書 */
    public void FinishReportGenerator(){
        try {
            Runtime.getRuntime().exec("java -jar JAR/Finish_Report_Generator.jar");
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }
    /** 透過windows內建預設軟體開啟圖片 */
    public boolean CallPicture(String PicturePath){
        try {
//            String cmd = "cmd /c start C:/Corporation/IntelliJProject/ZYH/outputFile/下載.jpg";
            String cmd = "cmd /c start " + PicturePath;
            Runtime.getRuntime().exec(cmd);
            return true;
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
            return false;
        }
    }
}
