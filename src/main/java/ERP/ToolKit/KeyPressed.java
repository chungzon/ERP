package ERP.ToolKit;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/** KeyPressed */
public class KeyPressed {
    /** Judge enter key */
    public boolean isEnterKeyPressed(KeyEvent KeyEvent){   return KeyEvent.getCode() == KeyCode.ENTER;   }
    public boolean isNumberLockKeyPressed(KeyEvent KeyEvent){   return KeyEvent.getCode() == KeyCode.NUM_LOCK;  }
    public boolean isF1KeyPressed(KeyEvent KeyEvent){   return KeyEvent.getCode() == KeyCode.F1;  }
    /** Judge F2 key */
    public boolean isF2KeyPressed(KeyEvent KeyEvent){   return KeyEvent.getCode() == KeyCode.F2;  }
    /** Judge F3 key */
    public boolean isF3KeyPressed(KeyEvent KeyEvent){
        return KeyEvent.getCode() == KeyCode.F3;
    }
    /** Judge F4 key */
    public boolean isF4KeyPressed(KeyEvent KeyEvent){
        return KeyEvent.getCode() == KeyCode.F4;
    }
    /** Judge F5 key */
    public boolean isF5KeyPressed(KeyEvent KeyEvent){
        return KeyEvent.getCode() == KeyCode.F5;
    }
    /** Judge F6 key */
    public boolean isF6KeyPressed(KeyEvent KeyEvent){  return KeyEvent.getCode() == KeyCode.F6;  }
    /** Judge F7 key */
    public boolean isF7KeyPressed(KeyEvent KeyEvent){  return KeyEvent.getCode() == KeyCode.F7;  }
    /** Judge F8 key */
    public boolean isF8KeyPressed(KeyEvent KeyEvent){  return KeyEvent.getCode() == KeyCode.F8;  }
    /** Judge digital key */
    public boolean isDigitalKeyPressed(KeyEvent KeyEvent){
        return KeyEvent.getCode().isDigitKey();
    }
    public boolean isEnglishKeyPressed(KeyEvent KeyEvent){
        return KeyEvent.getCode() == KeyCode.A || KeyEvent.getCode() == KeyCode.B || KeyEvent.getCode() == KeyCode.C ||
                KeyEvent.getCode() == KeyCode.D || KeyEvent.getCode() == KeyCode.E || KeyEvent.getCode() == KeyCode.F ||
                KeyEvent.getCode() == KeyCode.G || KeyEvent.getCode() == KeyCode.H || KeyEvent.getCode() == KeyCode.I ||
                KeyEvent.getCode() == KeyCode.J || KeyEvent.getCode() == KeyCode.K || KeyEvent.getCode() == KeyCode.L ||
                KeyEvent.getCode() == KeyCode.M || KeyEvent.getCode() == KeyCode.N || KeyEvent.getCode() == KeyCode.O ||
                KeyEvent.getCode() == KeyCode.P || KeyEvent.getCode() == KeyCode.Q || KeyEvent.getCode() == KeyCode.R ||
                KeyEvent.getCode() == KeyCode.S || KeyEvent.getCode() == KeyCode.T || KeyEvent.getCode() == KeyCode.U ||
                KeyEvent.getCode() == KeyCode.V || KeyEvent.getCode() == KeyCode.W || KeyEvent.getCode() == KeyCode.X ||
                KeyEvent.getCode() == KeyCode.Y || KeyEvent.getCode() == KeyCode.Z;
    }
    /** Judge Digital key pressed */
    public String getDigitalKeyPressed(KeyEvent KeyEvent, String Text){
        if(KeyEvent.getCode() == KeyCode.NUMPAD0)  Text = Text + "0";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD1)  Text = Text + "1";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD2)  Text = Text + "2";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD3)  Text = Text + "3";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD4)  Text = Text + "4";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD5)  Text = Text + "5";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD6)  Text = Text + "6";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD7)  Text = Text + "7";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD8)  Text = Text + "8";
        else if(KeyEvent.getCode() == KeyCode.NUMPAD9)  Text = Text + "9";
        return Text;
    }
    /** Judge Letter key pressed */
    public String getLetterKeyPressed(KeyEvent KeyEvent, String Text){
        if(KeyEvent.getCode() == KeyCode.A)  Text = Text + "A";
        else if(KeyEvent.getCode() == KeyCode.B)  Text = Text + "B";
        else if(KeyEvent.getCode() == KeyCode.C)  Text = Text + "C";
        else if(KeyEvent.getCode() == KeyCode.D)  Text = Text + "D";
        else if(KeyEvent.getCode() == KeyCode.E)  Text = Text + "E";
        else if(KeyEvent.getCode() == KeyCode.F)  Text = Text + "F";
        else if(KeyEvent.getCode() == KeyCode.G)  Text = Text + "G";
        else if(KeyEvent.getCode() == KeyCode.H)  Text = Text + "H";
        else if(KeyEvent.getCode() == KeyCode.I)  Text = Text + "I";
        else if(KeyEvent.getCode() == KeyCode.J)  Text = Text + "J";
        else if(KeyEvent.getCode() == KeyCode.K)  Text = Text + "K";
        else if(KeyEvent.getCode() == KeyCode.L)  Text = Text + "L";
        else if(KeyEvent.getCode() == KeyCode.M)  Text = Text + "M";
        else if(KeyEvent.getCode() == KeyCode.N)  Text = Text + "N";
        else if(KeyEvent.getCode() == KeyCode.O)  Text = Text + "O";
        else if(KeyEvent.getCode() == KeyCode.P)  Text = Text + "P";
        else if(KeyEvent.getCode() == KeyCode.Q)  Text = Text + "Q";
        else if(KeyEvent.getCode() == KeyCode.R)  Text = Text + "R";
        else if(KeyEvent.getCode() == KeyCode.S)  Text = Text + "S";
        else if(KeyEvent.getCode() == KeyCode.T)  Text = Text + "T";
        else if(KeyEvent.getCode() == KeyCode.U)  Text = Text + "U";
        else if(KeyEvent.getCode() == KeyCode.V)  Text = Text + "V";
        else if(KeyEvent.getCode() == KeyCode.W)  Text = Text + "W";
        else if(KeyEvent.getCode() == KeyCode.X)  Text = Text + "X";
        else if(KeyEvent.getCode() == KeyCode.Y)  Text = Text + "Y";
        else if(KeyEvent.getCode() == KeyCode.Z)  Text = Text + "Z";
        return Text;
    }

    public boolean isSubtract(KeyEvent KeyEvent){ return KeyEvent.getCode() == KeyCode.SUBTRACT; }

    /** Judge ESCAPE key pressed */
    public boolean isESCAPEKeyPressed(KeyEvent KeyEvent){ return KeyEvent.getCode() == KeyCode.ESCAPE;  }
    /** Judge Space key pressed */
    public boolean isSpaceKeyPressed(KeyEvent KeyEvent){   return KeyEvent.getCode() == KeyCode.SPACE; }
    /** Judge BackSpace key pressed */
    public boolean isBackSpaceKeyPressed(KeyEvent KeyEvent){    return KeyEvent.getCode() == KeyCode.BACK_SPACE;    }
    /** Judge Delete key pressed */
    public boolean isDeleteKeyPressed(KeyEvent KeyEvent){
        return KeyEvent.getCode() == KeyCode.DELETE;
    }
    /** Judge Direction key pressed */
    public boolean isDirectionKeyPressed(KeyEvent KeyEvent){
        return KeyEvent.getCode() == KeyCode.UP || KeyEvent.getCode() == KeyCode.DOWN || KeyEvent.getCode() == KeyCode.LEFT || KeyEvent.getCode() == KeyCode.RIGHT;
    }
    /** Judge Up key pressed */
    public boolean isUpKeyPressed(KeyEvent KeyEvent){  return KeyEvent.getCode() == KeyCode.UP;  }
    /** Judge Down key pressed */
    public boolean isDownKeyPressed(KeyEvent KeyEvent){  return KeyEvent.getCode() == KeyCode.DOWN;  }
    /** Judge MouseLeft key Clicked */
    public boolean isMouseLeftClicked(MouseEvent MouseEvent){    return MouseEvent.getButton() == MouseButton.PRIMARY;   }
    public boolean isMouseRightClicked(MouseEvent MouseEvent){    return MouseEvent.getButton() == MouseButton.SECONDARY;   }

    /** Judge Mouse Double Clicked */
    public boolean isMouseLeftDoubleClicked(MouseEvent MouseEvent){   return MouseEvent.getButton() == MouseButton.PRIMARY && MouseEvent.getClickCount() == 2;    }
    public boolean isMouseRightDoubleClicked(MouseEvent MouseEvent){   return MouseEvent.getButton() == MouseButton.SECONDARY && MouseEvent.getClickCount() == 2;    }
}
