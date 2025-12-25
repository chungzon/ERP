package ERP.Bean.SystemSetting;

public class Version_Bean {
    public Integer id;
    public String version,versionContent;

    public Integer getId() {    return id;  }
    public void setId(Integer id) { this.id = id;       }

    public String getVersion() {    return version; }
    public void setVersion(String version) {    this.version = version; }

    public String getVersionContent() { return versionContent;  }
    public void setVersionContent(String versionContent) {  this.versionContent = versionContent;   }
}
