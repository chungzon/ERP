package ERP.Controller.Toolkit.ShowVersion;

import ERP.Bean.SystemSetting.Version_Bean;
import ERP.Enum.ToolKit.ToolKit_Enum.Authority;
import ERP.Model.SystemSetting.SystemSetting_Model;
import ERP.ToolKit.CallFXML;
import ERP.ToolKit.ComponentToolKit;
import ERP.ToolKit.KeyPressed;
import ERP.Enum.ToolKit.ToolKit_Enum.AuthorizedLocation;
import ERP.View.DialogUI;
import ERP.ERPApplication;
import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowVersion_Controller {
    @FXML private ComboBox<Version_Bean> versionComboBox;
    @FXML private TextField searchKeyWordText;
    @FXML private ScrollPane ScrollPane;
    @FXML private TextFlow versionContent_TextFlow;

    private ComponentToolKit ComponentToolKit;
    private KeyPressed KeyPressed;
    private CallFXML CallFXML;

    private Stage Stage, MainStage;
    private SystemSetting_Model SystemSetting_Model;

    public ShowVersion_Controller(){
        ComponentToolKit = ERPApplication.ToolKit.ComponentToolKit;
        KeyPressed = ERPApplication.ToolKit.KeyPressed;
        CallFXML = ERPApplication.ToolKit.CallFXML;
        SystemSetting_Model = ERPApplication.ToolKit.ModelToolKit.getSystemSettingModel();
    }

    public void setStage(Stage Stage){  this.Stage = Stage; }
    public void setMainStage(Stage MainStage){  this.MainStage = MainStage; }


    public void setComponent(){
        ComponentToolKit.setVersionComboBoxObj(versionComboBox);
        ObservableList<Version_Bean> allVersionList = SystemSetting_Model.getAllVersion();
        ComponentToolKit.getVersionComboBoxItems(versionComboBox).addAll(allVersionList);
        if(allVersionList.size() != 0) {
            versionComboBox.getSelectionModel().selectLast();
            Version_Bean Version_Bean = ComponentToolKit.getVersionComboBoxSelectItem(versionComboBox);
            versionContent_TextFlow.getChildren().clear();
            String versionContent = "                                        ***  " + Version_Bean.getVersion() + "  ***\n" +
                    "------------------------------------------------------------------------------\n" +
                    setVersionContentComposing(new StringBuilder(Version_Bean.getVersionContent()));
            versionContent_TextFlow.getChildren().add(ComponentToolKit.createText(versionContent,20,null));
        }
    }
    @FXML protected void versionOnShowing(){
        ListView<Version_Bean> list = (ListView<Version_Bean>) ((ComboBoxListViewSkin) versionComboBox.getSkin()).getPopupContent();
        list.scrollTo(Math.max(0, versionComboBox.getSelectionModel().getSelectedIndex()));
    }
    @FXML protected void versionOnAction(){
        Version_Bean selectVersion_Bean = ComponentToolKit.getVersionComboBoxSelectItem(versionComboBox);
        if(selectVersion_Bean != null){
            ScrollPane.setVvalue(0);
            String versionContent = "";
            if(selectVersion_Bean.getId() == null){
                ObservableList<Version_Bean> allVersionList = ComponentToolKit.getVersionComboBoxItems(versionComboBox);
                for(Version_Bean Version_Bean: allVersionList){
                    if(Version_Bean.getId() != null){
                        versionContent = versionContent + "                                        ***  " + Version_Bean.getVersion() + "  ***\n";
                        versionContent = versionContent + "------------------------------------------------------------------------------\n";
                        versionContent = versionContent + setVersionContentComposing(new StringBuilder(Version_Bean.getVersionContent())) + "\n\n\n";
                    }
                    versionContent_TextFlow.getChildren().clear();
                    versionContent_TextFlow.getChildren().add(ComponentToolKit.createText(versionContent,20,null));
                }
            }else {
                versionContent_TextFlow.getChildren().clear();
                versionContent = "                                        ***  " + selectVersion_Bean.getVersion() + "  ***\n" +
                        "------------------------------------------------------------------------------\n" +
                        setVersionContentComposing(new StringBuilder(selectVersion_Bean.getVersionContent()));
                versionContent_TextFlow.getChildren().add(ComponentToolKit.createText(versionContent,20,null));
            }
        }
    }
    private String setVersionContentComposing(StringBuilder versionContent){
        String[] versionContentArray = versionContent.toString().split("\n");
        versionContent = new StringBuilder();
        for (String content : versionContentArray) {
            if (content.getBytes(StandardCharsets.UTF_8).length <= 95)
                versionContent.append(content).append("\n");
            else {
                StringBuilder text = new StringBuilder();
                ArrayList<Integer> contentPoint = new ArrayList<>();
                for (int j = 0; j < content.length(); j++) {
                    text.append(content.charAt(j));
                    if (text.toString().getBytes(StandardCharsets.UTF_8).length > 95 * (contentPoint.size() + 1)) contentPoint.add(j);
                }
                text = new StringBuilder();
                for (int j = 0; j < contentPoint.size(); j++) {
                    if (j == 0) text.append(content, 0, contentPoint.get(j));
                    if (j + 1 == contentPoint.size())
                        text.append("\n      ").append(content.substring(contentPoint.get(j)));
                    else
                        text.append("\n      ").append(content, contentPoint.get(j), contentPoint.get(j + 1));
                }
                versionContent.append(text).append("\n");
            }
        }
        return versionContent.toString();
    }
    @FXML protected void searchTextKeyReleased(KeyEvent KeyEvent){
        if(KeyPressed.isEnterKeyPressed(KeyEvent)){
            String searchKeyWord = searchKeyWordText.getText();
            if(!searchKeyWord.equals("")){
                ArrayList<HashMap<String,ArrayList<String>>> matchVersionList = getMatchVersionList(searchKeyWord);

                if(matchVersionList.size() == 0)
                    DialogUI.MessageDialog("※ 查無相關字眼");
                else{
                    ScrollPane.setVvalue(0);
                    StringBuilder versionContent = new StringBuilder();
                    for (HashMap<String, ArrayList<String>> matchContentMap : matchVersionList) {
                        String version = matchContentMap.entrySet().iterator().next().getKey();
                        versionContent.append("                                        ***  ").append(version).append("  ***\n");
                        versionContent.append("------------------------------------------------------------------------------\n");
                        ArrayList<String> contentList = matchContentMap.entrySet().iterator().next().getValue();
                        for(String content : contentList){
                            versionContent.append(setVersionContentComposing(new StringBuilder(content)));
                        }
                        versionContent.append("\n\n\n");
                    }
                    matchVersionList.clear();
                    versionContent_TextFlow.getChildren().clear();
                    String[] contentArray = versionContent.toString().split(searchKeyWord);
                    for(int index = 0 ; index < contentArray.length ; index++){
                        String content = contentArray[index];
                        versionContent_TextFlow.getChildren().add(ComponentToolKit.createText(content,20,null));
                        if(index != contentArray.length-1)
                            versionContent_TextFlow.getChildren().add(ComponentToolKit.createText(searchKeyWord,20,"red"));
                    }
                }
            }else
                DialogUI.MessageDialog("※ 請輸入關鍵字");
        }
    }
    private ArrayList<HashMap<String,ArrayList<String>>> getMatchVersionList(String searchKeyWord){
        ArrayList<HashMap<String,ArrayList<String>>> matchVersionList = new ArrayList<>();
        ObservableList<Version_Bean> allVersionList = ComponentToolKit.getVersionComboBoxItems(versionComboBox);
        for(Version_Bean Version_Bean : allVersionList){
            String[] contentArray = Version_Bean.getVersionContent().split("\n");

            HashMap<String,ArrayList<String>> matchContentMap = null;
            for(String content : contentArray){
                if(content.contains(searchKeyWord)){
                    if(matchContentMap == null) matchContentMap = new HashMap<>();
                    if(matchContentMap.containsKey(Version_Bean.getVersion()))
                        matchContentMap.get(Version_Bean.getVersion()).add(content);
                    else{
                        ArrayList<String> contentList = new ArrayList<>();
                        contentList.add(content);
                        matchContentMap.put(Version_Bean.getVersion(),contentList);
                    }
                }
            }
            if(matchContentMap != null)
                matchVersionList.add(matchContentMap);
        }
        return matchVersionList;
    }
    @FXML protected void versionControllerMouseClicked(MouseEvent MouseEvent){
        if(KeyPressed.isMouseLeftClicked(MouseEvent)){
            if(DialogUI.requestAuthorization(Authority.管理者, AuthorizedLocation.版本控制)){
                ObservableList<Version_Bean> allVersionList = ComponentToolKit.getVersionComboBoxItems(versionComboBox);
                CallFXML.ShowControllerVersion(MainStage,allVersionList);
            }
        }
    }
}
