package tank;

import game.GameStart;
import game.Levelinfo;
import util.Constant;
import util.EnemyTanksPool;
import util.MyUtil;

import java.awt.*;

public class EnemyTank extends Tank {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_GREEN = 1;
    public static final int TYPE_GRAY = 2;
    private int type = TYPE_NORMAL;
    private long aiTime;
    private static Image[] normalEnemyImg;    //敌人
    private static Image[] greenEnemyImg;    //敌人
    private static Image[] grayEnemyImg;    //敌人
    //静态代码块初始化
    static {
        normalEnemyImg = new Image[4];
        normalEnemyImg[0] = MyUtil.createImg("src\\image\\enemy1U.png");
        normalEnemyImg[1] = MyUtil.createImg("src\\image\\enemy1D.png");
        normalEnemyImg[2] = MyUtil.createImg("src\\image\\enemy1L.png");
        normalEnemyImg[3] = MyUtil.createImg("src\\image\\enemy1R.png");

        greenEnemyImg = new Image[4];
        greenEnemyImg[0] = MyUtil.createImg("src\\image\\enemy2U.png");
        greenEnemyImg[1] = MyUtil.createImg("src\\image\\enemy2D.png");
        greenEnemyImg[2] = MyUtil.createImg("src\\image\\enemy2L.png");
        greenEnemyImg[3] = MyUtil.createImg("src\\image\\enemy2R.png");

        grayEnemyImg = new Image[4];
        grayEnemyImg[0] = MyUtil.createImg("src\\image\\enemy3U.png");
        grayEnemyImg[1] = MyUtil.createImg("src\\image\\enemy3D.png");
        grayEnemyImg[2] = MyUtil.createImg("src\\image\\enemy3L.png");
        grayEnemyImg[3] = MyUtil.createImg("src\\image\\enemy3R.png");
    }
    //构造设为私有，只可以通过createEnemy在对象池中获取
    private EnemyTank(int x, int y, int dir) {
        super(x, y, dir);
        //敌人创建开始计时
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(TYPE_NORMAL,TYPE_GRAY+1);
    }
    public EnemyTank(){
        //记录时间
        aiTime = System.currentTimeMillis();
        //随机类型
        type = MyUtil.getRandomNumber(TYPE_NORMAL,TYPE_GRAY+1);
    }
    //创建一个敌人的tank
    public static Tank createEnemy(){
        int enemyX = RADIUS;
        switch (MyUtil.getRandomNumber(0,3)){
            //三种位置随机生产敌方坦克
            case 0:
                enemyX = RADIUS;
                break;
            case 1:
                enemyX = Constant.FRAME_WIDTH-RADIUS;
                break;
            case 2:
                enemyX = (Constant.FRAME_WIDTH-RADIUS)>>1;
                break;
        }
        int enemyY = GameStart.titleBarHeight+RADIUS;
//        EnemyTank enemy = new EnemyTank(enemyX,enemyY,DIR_DOWN);
        // 从池中拿Enemy
        EnemyTank enemy = EnemyTanksPool.get();
        enemy.setX(enemyX);
        enemy.setY(enemyY);
        enemy.setDir(DIR_DOWN);
        enemy.setEnemy(true);
        enemy.setState(STATE_STAND);
        //通过关卡信息中的敌人类型，来设置当前出生的敌人的类型
        int enemyType = Levelinfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);
        //不同类型不同设定
        int hp = DEFAULT_HP_MAX,
            speed = DEFAULT_SPEED,
            atkMax = DEFAULT_ATK_MAX,
            atkMin = DEFAULT_ATK_MIN;
        switch (enemy.type){
            case TYPE_NORMAL:
                //普通坦克设定为默认值
                break;
            case TYPE_GRAY:
                /*
                 * 重型装甲型坦克：
                 * 攻击力提升百分之20
                 * 生命值提升为原来的百分之50
                 * 速度削减为原来的百分之60
                 */
                hp = (int)(DEFAULT_HP_MAX * 1.5);
                speed = (int)(DEFAULT_SPEED * 0.6);
                atkMax = (int)(DEFAULT_ATK_MAX * 1.2);
                atkMin = (int)(DEFAULT_ATK_MAX * 1.2);
                break;
            case TYPE_GREEN:
                /*
                 * 速度特化型坦克：
                 * 速度提升百分之50
                 * 攻击力提升百分之10
                 * 生命值削减为原来的百分之60
                 */
                hp = (int)(DEFAULT_HP_MAX * 0.6);
                speed = (int)(DEFAULT_SPEED * 1.5);
                atkMax = (int)(DEFAULT_ATK_MAX * 1.1);
                atkMin = (int)(DEFAULT_ATK_MAX * 1.1);
                break;
        }
        enemy.setFullHp(hp);
        enemy.setHp(hp);
        enemy.setSpeed(speed);
        enemy.setAtk(MyUtil.getRandomNumber(atkMin, atkMax+1));
        return enemy;
    }

    @Override
    public void drawImgTank(Graphics g) {
        ai();
        switch (type){
            case TYPE_NORMAL:
                g.drawImage(normalEnemyImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
                break;
            case  TYPE_GREEN:
                g.drawImage(greenEnemyImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
                break;
            case TYPE_GRAY:
                g.drawImage(grayEnemyImg[getDir()],getX()-RADIUS,getY()-RADIUS,null);
                break;
        }
    }

    /**
     * 敌人的AI
     */
    private void ai(){
        if(System.currentTimeMillis()-aiTime > Constant.ENEMY_AI_INTERVAL){
            //随机切换状态
            setDir(MyUtil.getRandomNumber(DIR_UP,DIR_RIGHT+1));
            setState(MyUtil.getRandomNumber(0,2) == 0? STATE_STAND : STATE_MOVE);
            aiTime = System.currentTimeMillis();
        }
        //开火
        if(Math.random() < Constant.ENEMY_FIRE_PERCENT){
            fire();
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
