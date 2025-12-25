package ERP.Bean;


public class DataBaseServer_Bean {
    private int Index;
    private String Display,Driver,Url,User,Password;
    public void setIndex(int Index){    this.Index = Index; }
    public int getIndex(){  return Index;   }
    public void setDriver(String Driver){   this.Driver = Driver;   }
    public void setDisplay(String Display){ this.Display = Display; }
    public String getDisplay(){ return Display; }
    public String getDriver(){  return Driver;  }
    public void setUrl(String Url){ this.Url = Url; }
    public String getUrl(){ return Url; }
    public void setUser(String User){   this.User = User;   }
    public String getUser(){    return User;    }
    public void setPassword(String Password){   this.Password = Password;   }
    public String getPassword(){    return Password;    }
}
