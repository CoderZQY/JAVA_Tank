package game;

import util.MyUtil;

import java.awt.*;

/**
 * 处理出生效果的类
 */
public class Born {
    public static final int BORN_FRAME_COUNT = 16;
    //出生效果图的宽和高
    private static int bornWidth;
    private static int bornHeight;
    //导入资源
    private static Image[] img;

    static {
        img = new Image[BORN_FRAME_COUNT/4];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.createImg("src\\image\\born" + i + ".png");
        }
    }

    //出生效果属性
    private int x, y;
    private boolean visible = true;
    //当前播放的帧的下标[0~BORN_FRAME_COUNT]
    private int index;

    public Born() {
        index = 0;
    }

    public Born(int x, int y) {
        this.x = x;
        this.y = y;
        this.index = 0;
    }

    public void draw(Graphics g) {
        if (!visible) {
            return;
        }
        //对出生效果图片宽高的确定
        if (bornHeight <= 0) {
            bornWidth = img[0].getWidth(null);
            bornHeight = img[0].getHeight(null);
        }
        g.drawImage(img[index/4],x-bornWidth/2,y-bornHeight/2,null);
        index++;
        //播放完最后一帧，设置为不可见
        if(index >= BORN_FRAME_COUNT){
            visible = false;
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}