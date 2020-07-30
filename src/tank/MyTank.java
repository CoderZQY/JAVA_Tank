package tank;

import util.MyUtil;

import java.awt.*;

public class MyTank extends Tank{
    private static Image[] tankImg;     //坦克的图片数组
    //静态代码块初始化
    static {
        tankImg = new Image[4];
        tankImg[0] = MyUtil.createImg("src\\image\\p1tankU.png");
        tankImg[1] = MyUtil.createImg("src\\image\\p1tankD.png");
        tankImg[2] = MyUtil.createImg("src\\image\\p1tankL.png");
        tankImg[3] = MyUtil.createImg("src\\image\\p1tankR.png");
    }

    public MyTank(int x, int y, int dir) {
        super(x, y, dir);
    }

    @Override
    public void drawImgTank(Graphics g) {
        g.drawImage(tankImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
    }
}
