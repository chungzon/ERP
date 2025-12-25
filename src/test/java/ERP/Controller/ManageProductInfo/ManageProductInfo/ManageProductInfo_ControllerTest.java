package ERP.Controller.ManageProductInfo.ManageProductInfo;

import ERP.ERPApplication;
import ERP.Enum.Product.Product_Enum;
import ERP.Enum.Product.Product_Enum.ManageProductStatus;
import ERP.Model.ManagePayableReceivable.ManagePayableReceivable_Model;
import ERP.Model.Order.Order_Model;
import ERP.Model.Product.BidCategorySetting_Model;
import ERP.Model.Product.Product_Model;
import ERP.Sql.SqlAdapter;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.ToolKit.ToolKit;
import ERP.View.DialogUI;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.LabeledMatchers;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;

@ExtendWith(ApplicationExtension.class)
class ManageProductInfo_ControllerTest extends ApplicationTest {
    private CallFXML CallFXML;
    public ManageProductInfo_ControllerTest() throws Exception {
        SqlAdapter SqlAdapter = ERP.Sql.SqlAdapter.getInstance();
        SqlAdapter.connectDB(false,2);

        ERPApplication.ToolKit = new ToolKit();
        this.CallFXML = ERPApplication.ToolKit.CallFXML;
    }

    private Parent MainRoot;
    @Start
    public void start(Stage MainStage) {
        try {
            FXMLLoader FXMLLoader = new FXMLLoader(getClass().getResource("/fxml/ManageProductInfo/ManageProductInfo.fxml"));
            MainRoot = FXMLLoader.load();
            ManageProductInfo_Controller Controller = FXMLLoader.getController();
            Controller.setManageProductStatus(Product_Enum.ManageProductStatus.建立);
            Controller.setMainStage(MainStage);
            Controller.setComponent(null);
            MainStage.setScene(new Scene(MainRoot));
            MainStage.show();
        }catch (Exception Ex){
            ERPApplication.Logger.catching(Ex);
            Platform.runLater(() -> DialogUI.ExceptionDialog(Ex));
        }
    }




    @Test
    void ISBNKeyPressed() {
        /*
        press(KeyCode.ENTER);
        System.out.println("");
        FxAssert.verifyThat("#ProductTagSetting_Button", LabeledMatchers.hasText("商品碼(標籤)設定"));
        */
        TextField ISBNText = (TextField)MainRoot.lookup("#ISBNText");
        ISBNText.setText("45648948");
        System.out.println(ISBNText.getText());
        Button test = (Button)MainRoot.lookup("#ProductTagSetting_Button");
        System.out.println(test.getText());
        clickOn(test);
        FxAssert.verifyThat("#ProductTagSetting_Button", LabeledMatchers.hasText("商品碼(標籤)設定"));
        System.out.println("商品碼(標籤)設定");
    }
    @Test
    protected void ProductTagSettingMouseClicked(){
    }
    @Test
    protected void InsertProductMouseClicked(){
        Button test = (Button)MainRoot.lookup("#InsertProduct_Button");
        System.out.println(test.getText());
        clickOn(test);
        FxAssert.verifyThat("#InsertProduct_Button", LabeledMatchers.hasText("商品碼(標籤)設定"));
    }
}