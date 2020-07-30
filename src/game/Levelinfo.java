package game;

import util.MyUtil;

import static tank.Tank.*;

/**
 * 管理当前关卡的信息
 * 单例设计模式：
 *      如果一个类只需要该类具有唯一的一个实例，
 *      那么可以使用单例设计模式来设计它。
 */
public class Levelinfo {
    //构造方法私有化
    private Levelinfo(){}
    //定义静态的本类类型的变量，来指向唯一的实例
    private static Levelinfo instance;
    //懒汉模式的单例 第一次使用该实例时创建唯一的实例
    //所有的访问该类的唯一实例，都是通过该方法
    //该方法安全隐患：多线程的情况下有可能会创建多个实例
    public static Levelinfo getInstance(){
        if(instance == null){
            instance = new Levelinfo();
        }
        return instance;
    }
    //关卡编号
    private int level;
    //关卡敌人的数量
    private int enemyCount;
    //时长限制, -1表示不限时
    private int passTime = -1;
    //敌人类型数组
    private int[] enemyType;
    //游戏难度>=1
    private int gameLevel;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public int getPassTime() {
        return passTime;
    }

    public void setPassTime(int passTime) {
        this.passTime = passTime;
    }

    public int[] getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(int[] enemyType) {
        this.enemyType = enemyType;
    }
    //获得敌人类型数组中的一个随机的一个元素
    //获得一个随机的一个敌人的类型
    public int getRandomEnemyType(){
        int randIndex = MyUtil.getRandomNumber(0,enemyType.length);
        return enemyType[randIndex];
    }

    public int getGameLevel() {
        return gameLevel<=0? 1 : gameLevel;//最小难度为1
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }
}
