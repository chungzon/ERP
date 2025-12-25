package ERP;

import ERP.ToolKit.ToolKit;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;

import java.io.File;

public class ERPApplication extends Application {
    public static Logger Logger = LogManager.getLogger(ERPApplication.class);
    public static ToolKit ToolKit;
    private static boolean opencvLoaded = false;
    
    public static void main(String[] args) {
        // 在启动 JavaFX 应用之前加载 OpenCV 库
        loadOpenCVLibrary();
        launch(args);
    }
    
    /**
     * 加载 OpenCV 本地库
     * 优先从 opencv-dll 目录加载，如果失败则尝试从系统路径加载
     */
    private static void loadOpenCVLibrary() {
        if (opencvLoaded) {
            return;
        }
        
        try {
            // 方法1: 尝试从 opencv-dll 目录加载（Windows）
            File dllDir = new File("opencv-dll");
            if (dllDir.exists() && dllDir.isDirectory()) {
                String dllPath = new File(dllDir, Core.NATIVE_LIBRARY_NAME + ".dll").getAbsolutePath();
                File dllFile = new File(dllPath);
                if (dllFile.exists()) {
                    System.load(dllPath);
                    opencvLoaded = true;
                    Logger.info("OpenCV 库已从 opencv-dll 目录成功加载: " + dllPath);
                    return;
                }
            }
            
            // 方法2: 尝试使用 System.loadLibrary（从 java.library.path 加载）
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            opencvLoaded = true;
            Logger.info("OpenCV 库已从系统路径成功加载: " + Core.NATIVE_LIBRARY_NAME);
            
        } catch (UnsatisfiedLinkError e) {
            Logger.error("无法加载 OpenCV 库: " + e.getMessage());
            Logger.error("请确保 opencv-dll 目录存在且包含 " + Core.NATIVE_LIBRARY_NAME + ".dll 文件");
            // 不抛出异常，让应用继续运行，在需要时再提示用户
        } catch (Exception e) {
            Logger.error("加载 OpenCV 库时发生错误: " + e.getMessage(), e);
        }
    }
    
    /**
     * 检查 OpenCV 是否已加载
     */
    public static boolean isOpenCVLoaded() {
        return opencvLoaded;
    }
    
    /**
     * 尝试加载 OpenCV 库（公共方法，可在需要时调用）
     * @return 是否成功加载
     */
    public static boolean tryLoadOpenCVLibrary() {
        if (opencvLoaded) {
            return true;
        }
        
        try {
            // 方法1: 尝试从 opencv-dll 目录加载（Windows）
            File dllDir = new File("opencv-dll");
            if (dllDir.exists() && dllDir.isDirectory()) {
                String dllPath = new File(dllDir, Core.NATIVE_LIBRARY_NAME + ".dll").getAbsolutePath();
                File dllFile = new File(dllPath);
                if (dllFile.exists()) {
                    System.load(dllPath);
                    opencvLoaded = true;
                    Logger.info("OpenCV 库已从 opencv-dll 目录成功加载: " + dllPath);
                    return true;
                }
            }
            
            // 方法2: 尝试使用 System.loadLibrary（从 java.library.path 加载）
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            opencvLoaded = true;
            Logger.info("OpenCV 库已从系统路径成功加载: " + Core.NATIVE_LIBRARY_NAME);
            return true;
            
        } catch (UnsatisfiedLinkError e) {
            Logger.error("无法加载 OpenCV 库: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Logger.error("加载 OpenCV 库时发生错误: " + e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public void start(Stage primaryStage){
        ToolKit = new ToolKit();
        ToolKit.CallFXML.SelectDatabase(false);
    }
}
