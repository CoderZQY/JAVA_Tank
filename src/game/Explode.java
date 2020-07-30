package game;

import util.MyUtil;

import java.awt.*;

/**
 * 控制爆炸效果的类
 */
public class Explode {
    public static final int EXPLODE_FRAME_COUNT = 12;
    //爆炸效果图的宽和高
    private static int explodeWidth;
    private static int explodeHeight;
    //导入资源
    private static Image[] img;
    static {
        img = new Image[EXPLODE_FRAME_COUNT>>1];
        for (int i = 0; i < img.length; i++) {
            img[i] = MyUtil.createImg("src\\image\\blast"+i+".png");
        }
    }
    //爆炸属性
    private int x,y;
    private boolean visible = true;
    //当前播放的帧的下标[0~EXPLODE_FRAME_COUNT]
    private int index;

    public Explode() {
        index = 0;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        this.index = 0;
    }

    public void draw(Graphics g){
        if(!visible){
            return;
        }
        //对爆炸效果图片宽高的确定
        if(explodeHeight <= 0){
            explodeWidth = img[0].getWidth(null);
            explodeHeight = img[0].getHeight(null);
        }
        g.drawImage(img[index>>1],x-explodeWidth/2,y-explodeHeight/2,null);
        index++;
        //播放完最后一帧，设置为不可见
        if(index >= EXPLODE_FRAME_COUNT){
            visible = false;
        }
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
