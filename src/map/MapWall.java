package map;

import tank.Bullet;
import util.BulletsPool;
import util.MyUtil;

import java.awt.*;
import java.util.List;

/**
 * 地图元素块
 */
public class MapWall {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_HOUSE = 1;
    public static final int TYPE_HOUSE_BLAST = 2;
    public static final int TYPE_COVER = 3;
    public static final int TYPE_HARD = 4;
    public static final int TYPE_WATER = 5;
    //private static int wallH = 60;
    //private static int wallW = 60;
    private static int WallSide = 60;
    private int x,y;//图片资源的左上角坐标
    private int type = TYPE_NORMAL;
    private boolean visible = true;
    private static Image[] wallImgs;
    static {
        wallImgs = new Image[6];
        wallImgs[TYPE_NORMAL] = MyUtil.createImg("src\\image\\map\\walls.png");
        wallImgs[TYPE_HOUSE] = MyUtil.createImg("src\\image\\map\\home.png");
        wallImgs[TYPE_HOUSE_BLAST] = MyUtil.createImg("src\\image\\map\\destroy.png");
        wallImgs[TYPE_COVER] = MyUtil.createImg("src\\image\\map\\grass.png");
        wallImgs[TYPE_HARD] = MyUtil.createImg("src\\image\\map\\steels.png");
        wallImgs[TYPE_WATER] = MyUtil.createImg("src\\image\\map\\water.png");
    }
    public MapWall() {
    }

    public MapWall(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g){
        if(!visible){
            return;
        }
        g.drawImage(wallImgs[type],x,y,null);
    }
    public void drawhomeDestroyed(Graphics g){
        g.drawImage(wallImgs[TYPE_HOUSE_BLAST],x,y,null);
    }
    /**
     * 地图块和子弹是否有碰撞
     * @param bullets
     * @return
     */
    public boolean isCrashedByBullet(List<Bullet> bullets){
        if(!visible || type==TYPE_COVER || type == TYPE_WATER)
            return false;
        for(Bullet bullet : bullets) {
            int bulletX = bullet.getX();
            int bulletY = bullet.getY();
            boolean iscrash = MyUtil.isCrash(x + WallSide / 2, y + WallSide / 2, bulletX, bulletY, WallSide / 2);
            if(iscrash){
                //子弹的销毁
                bullet.setVisible(false);
                BulletsPool.giveBack(bullet);
                return true;
            }
        }
        return false;
    }
    //判断当前地图块是不是老巢
    public boolean isHouse(){
        return type == TYPE_HOUSE;
    }
    public boolean isVisible() {
        return visible;
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

    public static int getWallSide() {
        return WallSide;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static void setWallSide(int wallSide) {
        WallSide = wallSide;
    }
}
