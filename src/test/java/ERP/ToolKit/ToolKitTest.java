package ERP.ToolKit;

import ERP.ERPApplication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolKitTest {

    private ToolKit ToolKit;

    public ToolKitTest(){
        ToolKit = new ToolKit();
    }
    @Test
    void encodeURIComponent() {
//        String input = "多媒體廣告播放機 (非觸控)";
        String input = "多媒體廣告播放機";
        String output = ToolKit.encodeURIComponent(input);
        System.out.println(output);
    }
}