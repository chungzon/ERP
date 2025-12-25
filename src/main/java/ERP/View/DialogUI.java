package ERP.View;

import ERP.ERPApplication;
import ERP.Enum.ToolKit.ToolKit_Enum.Authority;
import ERP.Enum.ToolKit.ToolKit_Enum.AuthorizedLocation;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class DialogUI{
    public static String TextInputDialog(String Title, String ContentText, String inputText){
        final TextInputDialog TextInputDialog = new TextInputDialog();
        TextInputDialog.setTitle(Title);
        TextInputDialog.setHeaderText("");
        TextInputDialog.setContentText(ContentText);
        TextInputDialog.getDialogPane().setStyle("-fx-font-size: 16px;");
        if(inputText != null) {
            TextInputDialog.getEditor().setText(inputText);
            TextInputDialog.getEditor().setEditable(false);
        }
        final Optional<String> Optional = TextInputDialog.showAndWait();
        String InputText;
        try{
            InputText = Optional.get();
        }catch(final Exception ex){
            InputText = null;
        }
        return InputText;
    }
    public static boolean AlarmDialog(String Content){
        Alert Dialog = new Alert(Alert.AlertType.WARNING);
        Dialog.setTitle("Alarm Dialog"); //設定對話框視窗的標題列文字
        Dialog.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        Dialog.setContentText(Content);
        Dialog.getDialogPane().setStyle("-fx-font-size: 16px;");
        Dialog.initModality(Modality.WINDOW_MODAL);
        Stage stage = (Stage) Dialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        Dialog.showAndWait();
        return true;
    }
    public static Boolean ConfirmDialog(String ContentText, boolean defaultButton, boolean TextFill, int startIndex, int endIndex){
        final Alert ConfirmDialog;
        if(TextFill)    ConfirmDialog = new Alert(Alert.AlertType.CONFIRMATION,"",ButtonType.YES,ButtonType.NO);
        else    ConfirmDialog = new Alert(Alert.AlertType.CONFIRMATION, ContentText, ButtonType.YES, ButtonType.NO);
        ConfirmDialog.setTitle("Confirm Dialog");
        ConfirmDialog.setHeaderText("");
        ConfirmDialog.initModality(Modality.WINDOW_MODAL);
        Stage stage = (Stage) ConfirmDialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        ConfirmDialog.getDialogPane().setStyle("-fx-font-size:16px;");
        if(TextFill) {
            Text ChangeLineText = new Text("\n");

            Text titleText = new Text(ContentText.substring(0, startIndex));
            titleText.setStyle("-fx-font-size:16px;");
            Text ColorText = new Text(ContentText.substring(startIndex, endIndex));
            ColorText.setStyle("-fx-font-size:16px; -fx-fill:red;");
            Text endText = new Text(ContentText.substring(endIndex));
            endText.setStyle("-fx-font-size:16px;");
            TextFlow TextFlow = new TextFlow(ChangeLineText, titleText, ColorText, endText);
            ConfirmDialog.getDialogPane().setContent(TextFlow);
            ConfirmDialog.getDialogPane().setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
        Button yesButton = (Button) ConfirmDialog.getDialogPane().lookupButton( ButtonType.YES );
        Button noButton = (Button) ConfirmDialog.getDialogPane().lookupButton( ButtonType.NO );

        if(defaultButton){
            yesButton.setDefaultButton(true);
            noButton.setDefaultButton(false);
        }else{
            yesButton.setDefaultButton(false);
            noButton.setDefaultButton(true);
        }
        yesButton.setOnKeyPressed(KeyEvent -> {
            if(KeyEvent.getCode().toString().equals("RIGHT")){
                yesButton.setDefaultButton(false);
                noButton.setDefaultButton(true);
            }
        });

        noButton.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode().toString().equals("LEFT")) {
                yesButton.setDefaultButton(true);
                noButton.setDefaultButton(false);
            }
        });
        Optional<ButtonType> Optional = ConfirmDialog.showAndWait();
        ButtonType ButtonType = Optional.get();
        return ButtonType == ButtonType.YES;
    }
    public static void MessageDialog(String content){
        Alert Dialog = new Alert(Alert.AlertType.INFORMATION);
        Dialog.setTitle("Message Dialog"); //設定對話框視窗的標題列文字
        Dialog.setHeaderText(""); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        Dialog.setContentText(content);
        Dialog.getDialogPane().setStyle("-fx-font-size: 16px;");
        Dialog.initModality(Modality.WINDOW_MODAL);
        Stage stage = (Stage) Dialog.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        Node Node = Dialog.getDialogPane().lookupButton(ButtonType.OK);
        Node.setDisable(true);
        Dialog.show();
        Timer Timer = new Timer();
        TimerTask TimerTask= new TimerTask(){   public void run() { Platform.runLater(Dialog::close); }   };
        Timer.schedule(TimerTask, 1000);
    }
    public static void ExceptionDialog(Exception Ex){
        StringWriter SW = new StringWriter();
        Ex.printStackTrace(new PrintWriter(SW));
        String ExString = SW.toString();

        if(ExString.contains("Connection reset")) {
            AlarmDialog("※ 網路連線異常(逾時等...)");
            return;
        }else if(ExString.contains("Communications link failure")){
            AlarmDialog("※ 資料庫連線異常");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("※ 發生例外");

        TextArea textArea = new TextArea(ExString);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane GridPane = new GridPane();
        GridPane.setMaxWidth(Double.MAX_VALUE);
        GridPane.add(textArea, 0, 0);

//        alert.getDialogPane().setExpandableContent(GridPane);
        alert.getDialogPane().setContent(GridPane);

        alert.showAndWait();
    }
    public static boolean requestAuthorization(Authority authority, AuthorizedLocation authorizedLocation){
        String authorizationCode = null;
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("版本授權");
        dialog.setHeaderText("");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-font-size: 16px;");
        PasswordField pwd = new PasswordField();
        Platform.runLater(pwd::requestFocus);
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER_LEFT);
        content.setSpacing(10);
        content.getChildren().addAll(new Label("請輸入授權碼："), pwd);
        dialog.getDialogPane().setContent(content);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return pwd.getText();
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
            authorizationCode = result.get();
        if(authorizationCode == null)
            return false;
        else if(!authority.getAuthorizationCode().contains(authorizationCode)){
            ERPApplication.Logger.info("※ 無授權：" + authorizedLocation.name());
            DialogUI.MessageDialog("※ 無授權：" + ERPApplication.ToolKit.hidePassword(authorizationCode) + " - " + authorizedLocation.name());
            return false;
        }else {
            ERPApplication.Logger.info("※ 授權進入：" + ERPApplication.ToolKit.hidePassword(authorizationCode) + " - " + authorizedLocation.name());
            return true;
        }
    }
}
