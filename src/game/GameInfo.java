package game;

import javafx.geometry.HorizontalDirection;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 和游戏相关的类
 */
public class GameInfo {
    //从配置文件中读取：
    //   关卡数量
    private static int levelCount;
    static {
        FileInputStream fis = null;
        Properties prop = new Properties();
        try {
            fis = new FileInputStream("gamelevel/gameinfo");
            prop.load(fis);
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getLevelCount() {
        return levelCount;
    }
}
