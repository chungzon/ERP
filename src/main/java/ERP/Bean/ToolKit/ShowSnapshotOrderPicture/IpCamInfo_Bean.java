package ERP.Bean.ToolKit.ShowSnapshotOrderPicture;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class IpCamInfo_Bean {
    private int id;
    private SimpleBooleanProperty defaultPreview;
    private SimpleStringProperty name, RTSP;

    public IpCamInfo_Bean(){
        this.defaultPreview = new SimpleBooleanProperty();
        this.name = new SimpleStringProperty();
        this.RTSP = new SimpleStringProperty();
    }

    public int getId() {    return id;  }
    public void setId(int id) { this.id = id;   }

    public boolean getDefaultPreview() {    return defaultPreview.get();    }
    public SimpleBooleanProperty DefaultPreviewProperty() {  return defaultPreview;  }
    public void setDefaultPreview(boolean defaultPreview) { this.defaultPreview.set(defaultPreview);    }

    public String getName() {   return name.get();  }
    public SimpleStringProperty NameProperty() {    return name;    }
    public void setName(String name) {  this.name.set(name);    }

    public String getRTSP() {   return RTSP.get();  }
    public SimpleStringProperty RTSPProperty() {    return RTSP;    }
    public void setRTSP(String RTSP) {  this.RTSP.set(RTSP);    }
}
