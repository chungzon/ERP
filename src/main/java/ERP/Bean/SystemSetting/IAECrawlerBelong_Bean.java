package ERP.Bean.SystemSetting;

import javafx.beans.property.SimpleStringProperty;

public class IAECrawlerBelong_Bean {
    private int id;
    private SimpleStringProperty BelongName, BelongURL;

    public IAECrawlerBelong_Bean(){
        BelongName = new SimpleStringProperty();
        BelongURL = new SimpleStringProperty();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBelongName() {
        return BelongName.get();
    }

    public SimpleStringProperty belongNameProperty() {
        return BelongName;
    }

    public void setBelongName(String belongName) {
        this.BelongName.set(belongName);
    }

    public String getBelongURL() {
        return BelongURL.get();
    }

    public SimpleStringProperty belongURLProperty() {
        return BelongURL;
    }

    public void setBelongURL(String belongURL) {
        this.BelongURL.set(belongURL);
    }
}
