package tank;

import game.Born;
import game.Explode;
import game.GameStart;
import game.Levelinfo;
import map.MapWall;
import util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Tank {
    //方向
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;
    //半径
    public static final int RADIUS = 25;
    //默认速度  每帧10ms
    public static int DEFAULT_SPEED = 10;   //随难度调整，不设为final了
    //初始生命
    public static int DEFAULT_HP_MAX = 500;     //随难度调整，不设为final了
    //坐标
    private int x,y;
    //坦克状态
    public static final int STATE_STAND = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DIE = 2;
    //属性
    private int hp;             //真实hp
    public static int DEFAULT_ATK_MAX = 100;    //随难度调整，不设为final了
    public static int DEFAULT_ATK_MIN = 50;     //随难度调整，不设为final了
    private int atk;
    private int speed;
    private int dir;
    private int state;
    private Color color;
    private boolean isEnemy = false;
    private String name;
    //炮弹
    private List<Bullet> bullets = new ArrayList();
    //使用容器来保存当前坦克上所有的爆炸效果
    private List<Explode> explodes = new ArrayList<>();
    //出生效果
    private List<Born> borns = new ArrayList<>();
    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.state = STATE_STAND;
        initTank();
    }
    public Tank(){
        initTank();
    }
    private void initTank(){
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
        atk = MyUtil.getRandomNumber(DEFAULT_ATK_MIN,DEFAULT_ATK_MAX+1);
        hp = DEFAULT_HP_MAX;
        speed = DEFAULT_SPEED;
    }

    /**
     * 所有绘制的方法集合
     * @param g
     */
    public void draw(Graphics g){
        logic();
        //drawTank(g);
        drawImgTank(g);
        drawBullets(g);
        drawName(g);
        drawBloodBar(g);
    }
    /**
     * 画坦克的出生效果
     */
    public void drawBornImg(Graphics g){
        for(Born born : borns){
            born.draw(g);
        }
        //以下代码不能使用迭代器->foreach
        for (int i = 0; i < borns.size(); i++) {
            Born b = borns.get(i);
            if(!b.isVisible()){
                borns.remove(i);
                BornsPool.giveBack(b);
                i--;
            }
        }
    }
    /**
     * 画名字
     * @param g
     */
    private void drawName(Graphics g){
        g.setColor(color);
        g.setFont(Constant.TANK_NAME_FONT);
        g.drawString(name,x-RADIUS,y-RADIUS*2);
    }
    /**
     * 用图片绘制坦克
     * @param g
     */
    public abstract void drawImgTank(Graphics g);
    /**
     * 使用系统方式绘制坦克
     * @param g
     */
    /*private void drawTank(Graphics g){
        g.setColor(color);
        g.fillOval(x-RADIUS,y-RADIUS,RADIUS<<1,RADIUS<<1);  //身体
        //炮筒
        int endX = x;
        int endY = y;
        switch (dir){
            case DIR_UP:
                endY -= RADIUS<<1;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_DOWN:
                endY += RADIUS<<1;
                g.drawLine(x-1,y,endX-1,endY);
                g.drawLine(x+1,y,endX+1,endY);
                break;
            case DIR_LEFT:
                endX-=RADIUS<<1;
                g.drawLine(x,y+1,endX,endY+1);
                g.drawLine(x,y-1,endX,endY-1);
                break;
            case DIR_RIGHT:
                endX+=RADIUS<<1;
                g.drawLine(x,y+1,endX,endY+1);
                g.drawLine(x,y-1,endX,endY-1);
                break;
        }
        g.drawLine(x,y,endX,endY);
    }*/
    //坦克的逻辑处理
    private void logic(){
        switch (state){
            case STATE_STAND:
                break;
            case STATE_MOVE:
                move();
                //移动音效
                if(!isEnemy){
                    MusicUtil.playMove();
                }
                break;
            case STATE_DIE:
                die();
                break;
        }
    }
    private int oldX = -1,oldY = -1;
    //坦克移动的功能
    private void move(){
        oldX = x;
        oldY = y;
        switch (dir){
            case DIR_UP:
                y -= speed;
                if(y < RADIUS+ GameStart.titleBarHeight){
                    y = RADIUS+GameStart.titleBarHeight;
                }
                break;
            case DIR_DOWN:
                y += speed;
                if(y > Constant.FRAME_HEIGHT-GameStart.titleBarHeight){
                    y = Constant.FRAME_HEIGHT-GameStart.titleBarHeight;
                }
                break;
            case DIR_LEFT:
                x -= speed;
                if(x < RADIUS){
                    x = RADIUS;
                }
                break;
            case DIR_RIGHT:
                x += speed;
                if(x > Constant.FRAME_WIDTH-RADIUS){
                    x = Constant.FRAME_WIDTH-RADIUS;
                }
                break;
        }
    }
    //上一次开火的时间
    private long fireTime;
    //坦克开火的功能
    public void fire(){
        if((System.currentTimeMillis() - fireTime) >= Constant.FIRE_INTERVAL){
            int bulletX = x;
            int bulletY = y;
            switch (dir){
                case DIR_UP:
                    bulletY -= RADIUS;
                    break;
                case DIR_DOWN:
                    bulletY += RADIUS;
                    break;
                case DIR_LEFT:
                    bulletX -= RADIUS;
                    break;
                case DIR_RIGHT:
                    bulletX += RADIUS;
                    break;
            }
            //从子弹池获取一个子弹
            Bullet bullet = BulletsPool.get();
            //设置子弹的属性
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setAtk(atk);
            bullet.setDir(dir);
            bullet.setColor(color);
            bullet.setVisible(true);
            //Bullet bullet = new Bullet(bulletX,bulletY,dir,atk,color);
            bullets.add(bullet);
            //发射子弹之后，记录本次此次发射的时间
            fireTime = System.currentTimeMillis();
            MusicUtil.playFire();
        }

    }

    /**
     * 将当前坦克发射的所有子弹绘制出来
     */
    private void drawBullets(Graphics g){
        for (Bullet b:bullets
             ) {
            b.draw(g);
        }
        //遍历所有的子弹，将不可见的子弹移除并还给子弹池
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            if(!b.isVisible()){
                bullets.remove(i);
                BulletsPool.giveBack(b);
                i--;
            }
        }
        //System.out.println("子弹的数量："+bullets.size());
    }
    //坦克子弹和地图块的碰撞
    public void bulletCrashMapWall(List<MapWall> walls){
        for (MapWall mw : walls){
            //自己坦克的子弹和地图块的碰撞
            if(mw.isCrashedByBullet(bullets)){
                //爆炸效果
                addExplode(mw.getX()+ MapWall.getWallSide()/2,mw.getY()+ MapWall.getWallSide()/2);
                MusicUtil.playBlast();
                //地图水泥块取消击毁处理
                if(mw.getType() == MapWall.TYPE_HARD){
                    continue;
                }
                //地图块销毁, 只有普通砖块才会销毁
                if(mw.getType() == MapWall.TYPE_NORMAL){
                    mw.setVisible(false);
                    //归还对象池
                    MapWallPool.giveBack(mw);
                }
                if(mw.isHouse()){
                    //老巢被毁的处理
                    mw.setVisible(false);
                    delaySecondsToOver(2000);
                }
            }
        }
    }

    /**
     * 延迟若干毫秒结束游戏
     */
    private void delaySecondsToOver(int millSecond){
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameStart.setGameState(Constant.STATE_GAMEOVER);
            }
        }.start();
    }
    /**
     * 坦克销毁时清除所有子弹
     */
    public void clearAllBullets(){
        for(Bullet bullet : bullets){
            BulletsPool.giveBack(bullet);
        }
        bullets.clear();
    }
    public void crashOnTankAndBullet(List<Bullet> bullets) {
        //遍历所有的子弹
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            int bulletX = b.getX();
            int bulletY = b.getY();
            if (MyUtil.isCrash(x, y, bulletX, bulletY, RADIUS)) {
                //子弹消失
                b.setVisible(false);
                //坦克受伤
                hurt(b.getAtk());
                //添加爆炸效果
                addExplode(x, y);
                MusicUtil.playBlast();
            }
        }
    }
    /**
     * 添加爆炸效果，指出爆炸点的x、y坐标
     * @param x
     * @param y
     */
    private void addExplode(int x,int y){
        Explode e = ExplodesPool.get();
        e.setX(x);
        e.setY(y);
        e.setVisible(true);
        e.setIndex(0);
        explodes.add(e);
    }
    public void addBornImg(){
        Born born = BornsPool.get();
        born.setX(x);
        born.setY(y);
        born.setVisible(true);
        born.setIndex(0);
        borns.add(born);
    }
    private void hurt(int atk){
        hp -= atk;
        if(hp <= 0){
            state=STATE_DIE;
            logic();
        }
    }
    //坦克死亡
    private void die(){
        if(isEnemy){
            //敌人回归英灵殿
            GameStart.killEnemyCount++;
            EnemyTanksPool.giveBack(this);
            //本关是否结束
            if(GameStart.isWinCurrentLevel()){
                //过关效果
                if(GameStart.isLastLevel()){
                    GameStart.setGameState(Constant.STATE_WIN);
                }
                else {
                    GameStart.startCrossState();
                }
            }
            //判断游戏是否通关
        }else{
            //gameover
            delaySecondsToOver(1000);
            GameStart.setGameState(Constant.STATE_GAMEOVER);
        }
    }

    /**
     * 判断当前坦克是否死亡
     * @return
     */
    public boolean isDead(){
        return this.hp <= 0;
    }
    /**
     * 绘制所有的爆炸效果
     * @param g
     */
    public void drawExplodes(Graphics g){
        for(Explode e : explodes){
            e.draw(g);
        }
        //以下代码不能使用迭代器->foreach
        for (int i = 0; i < explodes.size(); i++) {
            Explode e = explodes.get(i);
            if(!e.isVisible()){
                explodes.remove(i);
                ExplodesPool.giveBack(e);
                i--;
            }
        }
    }
    /**
     * 判定一个地图块和当前坦克碰撞的方法
     */
    public boolean isCrashedWithWalls(List<MapWall> mapWalls) {
        for (MapWall mapWall : mapWalls) {
            //如果是遮挡块，就不用管
            if(mapWall.getType() == MapWall.TYPE_COVER){
                continue;
            }
            //确定砖块的中心坐标
            int sideLenth = MapWall.getWallSide();
            int wallCenX = mapWall.getX() + sideLenth/2;
            int wallCenY = mapWall.getY() + sideLenth/2;
            //确定坦克中心的横纵坐标与砖块中心横纵坐标距离
            int distX = Math.abs(x - wallCenX);
            int distY = Math.abs(y - wallCenY);
            //确定距离界限值
            int distMax = sideLenth/2+RADIUS;
            if(distX < distMax && distY < distMax)
                return true;
        }
        return false;
    }
    /**
     * 判定和一个坦克碰撞的方法
     */
    public boolean isCrashedWithTank(Tank tank){
        int distX = Math.abs(x - tank.getX());
        int distY = Math.abs(y - tank.getY());
        //确定距离界限值
        int distMax = RADIUS<<1;
        if(distX < distMax && distY < distMax)
            return true;
        else
            return false;
    }
    /**
     * 回退方法
     */
    public void back(){
        x = oldX;
        y = oldY;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public void setEnemy(boolean enemy) {
        isEnemy = enemy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOldX() {
        return oldX;
    }

    public void setOldX(int oldX) {
        this.oldX = oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public void setOldY(int oldY) {
        this.oldY = oldY;
    }

    private static final int BAR_DEFAULT_HEIGHT = 5;   //默认血条长度
    private int fullHp = DEFAULT_HP_MAX;               //用于绘制血条，记录满血时的血量
     /**
     * 绘制血条
     * @param g
     */
    void drawBloodBar(Graphics g){
        int BAR_DEFAULT_LENGTH = (int)(fullHp*0.1);    //默认血条长度为最大血量的百分之10
        //填充底色(黄)
        g.setColor(Color.YELLOW);
        g.fillRect(x-BAR_DEFAULT_LENGTH/2,y-RADIUS-BAR_DEFAULT_HEIGHT*2,BAR_DEFAULT_LENGTH,BAR_DEFAULT_HEIGHT);
        //红色血条
        g.setColor(Color.RED);
        g.fillRect(x-BAR_DEFAULT_LENGTH/2,y-RADIUS-BAR_DEFAULT_HEIGHT*2,BAR_DEFAULT_LENGTH*hp/fullHp,BAR_DEFAULT_HEIGHT);
        //白色边框
        g.setColor(Color.WHITE);
        g.drawRect(x-BAR_DEFAULT_LENGTH/2,y-RADIUS-BAR_DEFAULT_HEIGHT*2,BAR_DEFAULT_LENGTH,BAR_DEFAULT_HEIGHT);
    }

    public int getFullHp() {
        return fullHp;
    }

    public void setFullHp(int fullHp) {
        this.fullHp = fullHp;
    }


}
