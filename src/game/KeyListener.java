package game;

import map.GameMap;
import tank.EnemyTank;
import tank.MyTank;
import tank.Tank;
import util.MusicUtil;
import util.MyUtil;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Statement;

import static util.Constant.*;
import static java.awt.event.KeyEvent.*;

public class KeyListener extends KeyAdapter {
    private static GameStart gs;
    public KeyListener(GameStart gs) {
        this.gs = gs;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int gameState = GameStart.getGameState();
        switch(gameState){
            case STATE_MENU:
                KeyEventMenu(keyCode);
                break;
            case STATE_ABOUT:
                KeyEventAbout(keyCode);
                break;
            case STATE_HELP:
                KeyEventHelp(keyCode);
                break;
            case STATE_GAMEOVER:
                KeyEventGameOver(keyCode);
                break;
            case STATE_RUN:
                KeyEventRun(keyCode);
                break;
            case STATE_EXIT:
                System.exit(0);
                break;
            case STATE_WIN:
                KeyEventGameWin(keyCode);
                break;
        }
    }

    /**
     * 游戏通关的按键处理
     * @param keyCode
     */
    private void KeyEventGameWin(int keyCode) {
        if(keyCode == VK_ENTER){
            //回主菜单
            GameStart.setGameState(STATE_MENU);
        }
    }

    private void KeyEventRun(int keyCode) {
        MyTank mt = gs.getMytank();
        switch (keyCode){
            case VK_UP:
            case VK_W:
                mt.setState(Tank.STATE_MOVE);
                mt.setDir(Tank.DIR_UP);
                break;
            case VK_DOWN:
            case VK_S:
                mt.setState(Tank.STATE_MOVE);
                mt.setDir(Tank.DIR_DOWN);
                break;
            case VK_LEFT:
            case VK_A:
                mt.setState(Tank.STATE_MOVE);
                mt.setDir(Tank.DIR_LEFT);
                break;
            case VK_RIGHT:
            case VK_D:
                mt.setState(Tank.STATE_MOVE);
                mt.setDir(Tank.DIR_RIGHT);
                break;
            case VK_SPACE:
                mt.fire();
                break;
        }
        gs.setMytank(mt);
    }

    private void KeyEventAbout(int keyCode) {
        gs.setGameState(STATE_ABOUT);
        if(keyCode == VK_ESCAPE){
            GameStart.setGameState(STATE_MENU);
        }
    }

    /**
     * 游戏结束按键处理
     * @param keyCode
     */
    private void KeyEventGameOver(int keyCode) {
        if(keyCode == VK_ENTER){
            //重新开始
            startGame(1);
        }else if(keyCode == VK_ESCAPE){
            //回主菜单
            resetGame();
            GameStart.setGameState(STATE_MENU);
        }
    }

    private static void resetGame() {
        GameStart.setMenuIndex(0); //重置menu索引
        //清空所有的子弹
        if(gs.getMytank()!=null){
            gs.getMytank().clearAllBullets();   //清空自己子弹
        }
        if(gs.getEnemies()!=null){
            for (Tank enemy : gs.getEnemies()) {    //清空敌人子弹
                enemy.clearAllBullets();
            }
        }
        gs.setMytank(null); //销毁自己
        gs.getEnemies().clear();    //销毁敌人
        gs.getGameMap().setWalls(null); //清空地图块资源
        gs.setGameMap(new GameMap());        //重置地图资源
        GameStart.bornEnemyCount = 0;   //出生的敌人数置为0
        GameStart.killEnemyCount = 0;   //杀死的敌人数置为0
    }

    /**
     * 开始游戏
     * @param level 加载的level关卡编号
     */
    public static void startGame(int level) {
        resetGame();
        gs.getGameMap().initMap(level);
        MusicUtil.playStart();
        GameStart.setGameState(STATE_RUN);
        //创建坦克对象，创建敌人
        MyTank mt = new MyTank(FRAME_WIDTH/3 ,FRAME_HEIGHT-(Tank.RADIUS<<1), Tank.DIR_UP);
        gs.setMytank(mt);
        //使用一个单独的线程用于控制生产敌人的坦克
        new Thread(){
            @Override
            public void run() {
                while(true){
                    //如果本关卡的敌人数量大于出生的敌人数量，以及不超过屏幕容纳的最多的坦克数量，那么才创建
                    if(Levelinfo.getInstance().getEnemyCount()>GameStart.bornEnemyCount
                            && gs.getEnemies().size() < ENEMY_MAX_NUM){
                        Tank enemy = EnemyTank.createEnemy();
                        gs.addEnemy(enemy);
                        GameStart.bornEnemyCount++;
                    }
                    try {
                        Thread.sleep(ENEMY_BORN_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //只有在run状态下执行
                    if(GameStart.getGameState()!= STATE_RUN){
                        break;
                    }
                }
            }
        }.start();
    }

    private void KeyEventHelp(int keyCode) {
        GameStart.setGameState(STATE_ABOUT);
        if(keyCode == VK_ESCAPE){
            GameStart.setGameState(STATE_MENU);
        }
    }
    //菜单状态下的按钮处理
    private void KeyEventMenu(int keyCode) {
        int menuIndex = GameStart.getMenuIndex();
        switch (keyCode){
            case VK_UP:
            case VK_W:
                if(--menuIndex<0){
                    menuIndex = MENUS.length-1;
                }
                GameStart.setMenuIndex(menuIndex);
                break;
            case VK_DOWN:
            case VK_S:
                menuIndex = GameStart.getMenuIndex();
                if(++menuIndex > MENUS.length-1){
                    menuIndex = 0;
                }
                GameStart.setMenuIndex(menuIndex);
                break;
            case VK_ENTER:
                switch (menuIndex){
                    case 0:
                        resetGame();
                        startGame(1);
                        break;
                    case 1:
                        //继续游戏，选择关卡的界面
                        break;
                    case 2:
                        //帮助
                        GameStart.setGameState(STATE_HELP);
                        break;
                    case 3:
                        //关于
                        GameStart.setGameState(STATE_ABOUT);
                        break;
                    case 4:
                        //退出
                        GameStart.setGameState(STATE_EXIT);
                        break;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        int gameState = GameStart.getGameState();
        if(gameState == STATE_RUN){
            keyReleasedRun(keyCode);
        }
    }
    //运行时按键松开时，对应的处理方法
    private void keyReleasedRun(int keyCode) {
        MyTank mt = gs.getMytank();
        switch (keyCode){
            case VK_UP:
            case VK_W:
            case VK_DOWN:
            case VK_S:
            case VK_LEFT:
            case VK_A:
            case VK_RIGHT:
            case VK_D:
                mt.setState(Tank.STATE_STAND);
                gs.setMytank(mt);
                break;
        }}
}

