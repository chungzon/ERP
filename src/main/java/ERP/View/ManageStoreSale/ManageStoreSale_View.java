package ERP.View.ManageStoreSale;

import ERP.Sql.SqlAdapter;
import ERP.ToolKit.ToolKit;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Observable;

/** [ERP.View] - Manage store sale  */
public class ManageStoreSale_View extends Observable {
    private Stage Stage;
    private Scene Scene;
    private BorderPane BorderPane;
    private Button ManageMember,CreateOnSaleProduct,InquireOnSaleProduct,AnalysisProfit,ManageEmployee,StoreReport,Return;

    private ToolKit ToolKit;
    public ManageStoreSale_View(ToolKit ToolKit){
        this.ToolKit = ToolKit;
        initComponents();
    }
    private void initComponents(){
        Stage = SetStage("門市銷售管理【" + SqlAdapter.getDbDisplayName() + "】");
        Stage.setResizable(false);
        BorderPane = SetBorderPane();

        HBox HBox = new HBox();
        HBox.setSpacing(5);
        ObservableList<Node> HBoxChild = HBox.getChildren();

        ManageMember = SetButton("會員管理");
        HBoxChild.add(ManageMember);
        CreateOnSaleProduct = SetButton("特價商品新增");
        HBoxChild.add(CreateOnSaleProduct);
        InquireOnSaleProduct = SetButton("特價商品查詢");
        HBoxChild.add(InquireOnSaleProduct);
        AnalysisProfit = SetButton("利潤分析");
        HBoxChild.add(AnalysisProfit);
        ManageEmployee = SetButton("員工管理");
        HBoxChild.add(ManageEmployee);
        StoreReport = SetButton("門市報表");
        HBoxChild.add(StoreReport);
        Return = SetButton("返回系統主選單");
        HBoxChild.add(Return);
        BorderPane.setTop(HBox);

        CreateAction();
        Scene = SetScene(BorderPane,1280,850);
        Stage.setScene(Scene);
        Stage.show();
    }
    private void CreateAction(){
        Stage.setOnCloseRequest(event -> {
            setChanged();
            notifyObservers();
            ToolKit.ComponentToolKit.closeThisStage(Stage);
        });
        ManageMember.setOnAction(ActionEvent -> {
            System.out.println("會員管理");
        });
        CreateOnSaleProduct.setOnAction(ActionEvent -> {
            System.out.println("特價商品新增");
        });
        InquireOnSaleProduct.setOnAction(ActionEvent -> {
            System.out.println("特價商品查詢");
        });
        AnalysisProfit.setOnAction(ActionEvent -> {
            System.out.println("利潤分析");
        });
        ManageEmployee.setOnAction(ActionEvent -> {
            System.out.println("員工管理");
        });
        StoreReport.setOnAction(ActionEvent -> {
            System.out.println("門市報表");
        });
        Return.setOnAction(ActionEvent -> {
            setChanged();
            notifyObservers();
            ToolKit.ComponentToolKit.closeThisStage(Stage);
        });
    }
    private Stage SetStage(String Title){
        Stage Stage = new Stage();
        Stage.setTitle(Title);
        return Stage;
    }
    private Scene SetScene(BorderPane BorderPane, int width , int height){
        Scene Scene = new Scene(BorderPane, width, height);
        return Scene;
    }
    private BorderPane SetBorderPane(){
        BorderPane BorderPane = new BorderPane();
        BorderPane.setPadding(new Insets(10, 10, 10, 10));
        return BorderPane;
    }
    private Button SetButton(String Title){
        Button Button = new Button(Title);
        Button.setFont(new Font("微軟正黑體", 18));
        Button.setPrefSize(175,15);
        return Button;
    }
}
