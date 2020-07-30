package util;

import game.GameInfo;
import game.GameStart;
import game.Levelinfo;

import java.awt.*;

/**
 * 该类存储游戏中的常量
 */
public class Constant {
    /*---------------------窗口设定-----------------------*/
    public static final String GAME_TITLE = "MyTank";
    public static final int FRAME_WIDTH = 1000;//1000
    public static final int FRAME_HEIGHT = 800;//800
    /*---------------------菜单设定------------------------*/
    public static final int STATE_MENU = 0;
    public static final int STATE_HELP = 1;
    public static final int STATE_ABOUT = 2;
    public static final int STATE_EXIT = 3;
    public static final int STATE_RUN = 4;
    public static final int STATE_GAMEOVER = 5;
    public static final int STATE_WIN = 6;
    public static final int STATE_CROSS = 7;
    public static final int[] GAMESTATE ={
            STATE_MENU,
            STATE_HELP,
            STATE_ABOUT,
            STATE_EXIT
    };
    public static final String[] ABOUT = {
            "--------------关于坦克大战-------------",
            "|游戏版本：                      0.1.0|",
            "|游戏准备日期:         2020/4/12/18:00|",
            "|游戏发行日期:         2020/4/19/21:00|",
            "|游戏策划者：                   张庆洋|",
            "|地图设计者：                   张庆洋|",
            "|图片素材提供：                 张庆洋|",
            "|音乐素材提供：                 张庆洋|",
            "|更多信息了解加qq：         1277565476|",
            "--------------------------------------"
    };
    public static final String[] HELP = {
            "-------------------------------游戏帮助-------------------------------",
            "|游戏背景: 《坦克大战》是由日本南梦宫Namco游戏公司开发的一款平面射击游戏|",
            "|          于1985年发售，游戏以坦克战斗及保卫基地为主题，属于策略型联机 |",
            "|          类，本游戏为致敬坦克大战而推出的迷你版坦克游戏              |",
            "|游戏简介: 目前一共设计了三个关卡，当击杀每个关卡规定数量的敌人方可进入 |",
            "|          下一关，若玩家死亡或者老巢被毁都将导致失败, 此外坦克可击碎普 |",
            "|          通砖块，不可击碎水泥砖块，无法穿越河流块，草丛块会遮挡视野。 |",
            "|          每个关卡拥有不同类型的敌人，难度也会逐渐升温，快来感受一下吧!|",
            "|游戏玩法: 键盘↑↓←→(WSAD)分别控制坦克上下左右行走，空格键控制射击   |",
            "---------------------------------------------------------------------"
    };
    public static final String[] MENUS = {
            "开始游戏",
            "继续游戏",
            "游戏帮助",
            "游戏关于",
            "退出游戏",
    };
    public static final Font GAME_FONT = new Font("宋体", Font.BOLD, 24);
    public static final Font TANK_NAME_FONT = new Font("宋体", Font.BOLD, 12);
    /*---------------------游戏设定-----------------------*/
    public static final int REPAINT_INTERVAL = 30;  //刷新间隔
    public static final int ENEMY_MAX_NUM = 5; //最多同时有5个敌人
    public static final int ENEMY_BORN_INTERVAL = 5000; //出生时间间隔5000ms
    public static final int ENEMY_AI_INTERVAL= 1000;    //获得状态间隔
    public static final double ENEMY_FIRE_PERCENT= 0.05;    //开火几率
    public static final double FIRE_INTERVAL= 300;    //开火间隔最小时间
    public static final String OVER_ESC = "ESC键返回游戏主菜单";
    public static final String OVER_ENTER = "ENTER键重新开始游戏";
    public static final String OVER_VICTORY_WORDS = "ENTER键回到主菜单";
}