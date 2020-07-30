package game;

import map.GameMap;
import tank.EnemyTank;
import tank.MyTank;
import tank.Tank;
import util.Constant;
import util.MyUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static util.Constant.*;

public class GameStart extends Frame implements Runnable {
    private Image overImg;  //失败画面
    private Image winImg;      //胜利画面
    //1、定义一张和屏幕大小一致的图片
    private BufferedImage bufImg = new BufferedImage(FRAME_WIDTH,FRAME_HEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
    private static int gameState;        //游戏状态
    private static int menuIndex;        //菜单索引
    private MyTank mytank;
    private List<Tank> enemies = new ArrayList<>();
    public static int titleBarHeight;
    //用于记录本关卡产生了多少个敌人
    public static int bornEnemyCount;
    //用于记录本关杀死的敌人数
    public static int killEnemyCount;
    //定义地图相关的内容
    private GameMap gameMap = new GameMap();        //创建一个地图对象

    public GameStart() {
        initFrame();
        initGame();
        initEventListener();
        //启动用于刷新窗口的线程
        new Thread(this).start();
    }

    /**
     * 对游戏进行初始化
     */
    private void initGame(){
        gameState = STATE_MENU;
    }

    private void initFrame(){
        setTitle(GAME_TITLE);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        //this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        //this.setLayout(null);
        //this.setBackground(Color.CYAN);
        //jf.add(this);
        setVisible(true);
        titleBarHeight = getInsets().top;
    }
    private void initEventListener(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        //添加按键监听
        addKeyListener(new KeyListener(this));
    }
    /**
     *是Frame类继承下来的方法
     * 该方法负责调用所有绘制的内容，通过repaint()来回调该方法
     */
    @Override
    public void update(Graphics g1) {
        //2、得到图片的画笔
        Graphics g = bufImg.getGraphics();
        //3、使用图片画笔绘制
        g.setFont(GAME_FONT);
        switch(gameState){
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_ABOUT:
                drawAbout(g);
                break;
            case STATE_HELP:
                drawHelp(g);
                break;
            case STATE_GAMEOVER:
                drawGameOver(g);
                break;
            case STATE_RUN:
                drawRun(g);
                break;
            case STATE_WIN:
                drawWin(g);
                break;
            case STATE_CROSS:
                drawCross(g);
                break;
            case STATE_EXIT:
                System.exit(0);
                break;
        }
        //4、使用系统画笔，将图片绘制到frame上
        g1.drawImage(bufImg,0,0,null);
    }

    /**
     * 绘制游戏胜利画面
     * @param g
     */
    private void drawWin(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(winImg == null){
            winImg = MyUtil.createImg("src\\image\\victory2.png");
        }
        int imgW = winImg.getWidth(null);
        int imgH = winImg.getHeight(null);
        g.drawImage(winImg,FRAME_WIDTH-imgW >> 1,FRAME_HEIGHT-imgH+GameStart.titleBarHeight >> 1,null);
        //添加按键提示信息
        g.setColor(Color.green);
        g.setFont(GAME_FONT);
        g.drawString(OVER_VICTORY_WORDS,FRAME_WIDTH-OVER_VICTORY_WORDS.length()*24>>1,FRAME_HEIGHT>>1);
    }

    /**
     * 绘制游戏结束
     * @param g
     */
    private void drawGameOver(Graphics g) {
        //g.setColor(Color.BLACK);
        //g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        if(overImg == null){
            overImg = MyUtil.createImg("src\\image\\over2.png");
        }
        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);
        g.drawImage(overImg,FRAME_WIDTH-imgW >> 1,FRAME_HEIGHT-imgH >> 1,null);
        //添加按键提示信息
        g.setColor(Color.green);
        g.drawString(OVER_ENTER,100,FRAME_HEIGHT-40);
        g.drawString(OVER_ESC,FRAME_WIDTH-300,FRAME_HEIGHT-40);
    }

    private void drawRun(Graphics g) {
        //绘制黑色的背景
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        //绘制地图
        gameMap.drawMap(g);
        //绘制坦克
        mytank.draw(g);
        drawEnemies(g);
        //绘制地图遮挡块
        gameMap.drawCover(g);
        //子弹和地图块的碰撞
        bulletCrashMapWall();
        //子弹和坦克碰撞的处理方法
        bulletCrashTank();
        drawAllTankExplodes(g);
        //坦克和地图块的碰撞
        TankCrashMapWall();
        //敌人和自己的碰撞
        TankCrashTank();
    }


    /**
     * 绘制所有的敌人坦克
     */
    private void drawEnemies(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isDead()){
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.drawBornImg(g);
            enemy.draw(g);
        }
        //System.out.println("敌人数量："+enemies.size());
    }
    private void drawHelp(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        g.setColor(Color.CYAN);
        final int STR_WIDTH = 900;   //假定字符串的宽度
        int x = FRAME_WIDTH-STR_WIDTH>>1;
        int y = FRAME_HEIGHT/3;
        final int DIS = 30;     //行间距
        for (int i = 0; i < HELP.length; i++) {
            g.drawString(HELP[i],x,y+DIS*i);
        }
        g.drawString(OVER_ESC,x+350,y+300);
    }

    private void drawAbout(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        g.setColor(Color.CYAN);
        final int STR_WIDTH = 450;   //假定字符串的宽度
        int x = FRAME_WIDTH-STR_WIDTH>>1;
        int y = FRAME_HEIGHT/3;
        final int DIS = 30;     //行间距
        for (int i = 0; i < ABOUT.length; i++) {
                g.drawString(ABOUT[i],x,y+DIS*i);
        }
        g.drawString(OVER_ESC,x+150,y+300);
    }

    /**
     * 绘制菜单状态下的方法
     */
    private void drawMenu(Graphics g){
        //背景
        g.setColor(Color.BLACK);
        g.fillRect(0,0,FRAME_WIDTH,FRAME_HEIGHT);
        g.setColor(Color.white);
        final int STR_WIDTH = 66;   //假定字符串的宽度
        int x = FRAME_WIDTH-STR_WIDTH>>1;
        int y = FRAME_HEIGHT/3;
        final int DIS = 70;     //行间距
        for (int i = 0; i < MENUS.length; i++) {
            if(i == menuIndex){
                g.setColor(Color.red);      //选中的菜单项为红色
            }
            else {
                g.setColor(Color.white);    //否则为白色
            }
            g.drawString(MENUS[i],x,y+DIS*i);
        }
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public MyTank getMytank() {
        return mytank;
    }

    public void setMytank(MyTank mytank) {
        this.mytank = mytank;
    }

    public static int getGameState() {
        return gameState;
    }

    public static void setGameState(int gameState) {
        GameStart.gameState = gameState;
    }

    public static int getMenuIndex() {
        return menuIndex;
    }
    public static void setMenuIndex(int menuIndex) {
        GameStart.menuIndex = menuIndex;
    }

    public List<Tank> getEnemies() {
        return enemies;
    }

    public void addEnemy(Tank enemy) {
        this.enemies.add(enemy);
        enemy.addBornImg();
    }

    public void setEnemies(List<Tank> enemies) {
        this.enemies = enemies;
    }

    @Override
    public void run() {
        while (true){
            repaint();
            try {
                Thread.sleep(REPAINT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void bulletCrashTank(){
        //我的子弹和所有敌人的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.crashOnTankAndBullet(mytank.getBullets());
        }
        //敌人的子弹和我的坦克的碰撞
        for (int i = 0; i < enemies.size(); i++) {
            mytank.crashOnTankAndBullet(enemies.get(i).getBullets());
        }
    }
    //Tank和地图块的碰撞
    private void TankCrashMapWall(){
        if(mytank.isCrashedWithWalls(gameMap.getWalls())){
            mytank.back();
        }
        for (Tank enemy : enemies
             ) {
                if(enemy.isCrashedWithWalls(gameMap.getWalls())){
                    enemy.back();
            }
        }
    }
    //Tank之间的碰撞
    private void TankCrashTank(){
        //我和敌人之间碰撞
        for(Tank enemy : enemies){
            if(mytank.isCrashedWithTank(enemy)){
                mytank.back();
                enemy.back();
            }
        }
        //敌人之间的碰撞
        for(Tank enemy1 : enemies){
            for(Tank enemy2 : enemies) {
                if(enemy1 == enemy2){
                    continue;
                }
                if(enemy1.isCrashedWithTank(enemy2)){
                    enemy1.back();
                    enemy2.back();
                }
            }
        }
    }

    //所有子弹和地图块的碰撞
    private void bulletCrashMapWall(){
        //处理自己的子弹和地图块的碰撞
        mytank.bulletCrashMapWall(gameMap.getWalls());
        //处理敌人的子弹和地图块的碰撞
        for(Tank enemy : enemies){
            enemy.bulletCrashMapWall(gameMap.getWalls());
        }
        //对所有的地图块检测,清除掉被销毁的
        gameMap.clearDestroyedWalls();
    }
    //所有坦克上的爆炸效果
    private void drawAllTankExplodes(Graphics g){
        for(Tank enemy : enemies){
            enemy.drawExplodes(g);
        }
        mytank.drawExplodes(g);
    }

    /**
     * 判断是否是最后一关
     * @return
     */
    public static boolean isLastLevel(){
        int currentLevel = Levelinfo.getInstance().getLevel();  //当前的关卡编号
        int levelCount = GameInfo.getLevelCount();              //总关卡数
        //如果当前关卡和总关卡编号一致
        return currentLevel == levelCount;
    }

    /**
     * 判断是否过关
     */
    public static boolean isWinCurrentLevel(){
        //消灭的敌人数量和关卡的敌人设置一致
        return killEnemyCount == Levelinfo.getInstance().getEnemyCount();
    }

    /**
     * 进入下一关的方法
     */
    public static void enterNextLevel() {
        KeyListener.startGame(Levelinfo.getInstance().getLevel()+1);
    }
    //过关动画
    public static int flashTime;
    public static final int RECT_WIDTH = 40;//百叶窗的效果, 每页宽度
    public static final int RECT_COUNT = FRAME_WIDTH/RECT_WIDTH+1;  //一共多少页
    public static boolean isOpen;
    public static void startCrossState(){
        gameState = STATE_CROSS;    //过关的动画状态
        flashTime = 0;
        isOpen = false;
    }
    //绘制过关动画
    public void drawCross(Graphics g){
        //也要画地图
        gameMap.drawBk(g);
        gameMap.drawCover(g);
        g.setColor(Color.BLACK);
        //关闭百叶窗的效果
        if(!isOpen){
            for(int i=0; i<RECT_COUNT; i++){
                g.fillRect(i*RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            //所有的叶片都关闭了
            if(flashTime++ > RECT_WIDTH){
                isOpen = true;
                //初始化下一个地图
                gameMap.initMap(Levelinfo.getInstance().getLevel()+1);
            }
        }else {
            //开百叶窗的效果
            for(int i=0; i<RECT_COUNT; i++){
                g.fillRect(i*RECT_WIDTH,0,flashTime,FRAME_HEIGHT);
            }
            if(flashTime-- == 0){
                KeyListener.startGame(Levelinfo.getInstance().getLevel());
            }
        }
    }

    public static void setKillEnemyCount(int killEnemyCount) {
        GameStart.killEnemyCount = killEnemyCount;
    }

    public static int getKillEnemyCount() {
        return killEnemyCount;
    }
}
