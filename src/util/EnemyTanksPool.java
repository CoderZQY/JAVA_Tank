package util;

import tank.EnemyTank;
import tank.Tank;

import java.util.ArrayList;
import java.util.List;

/**
 * 敌人坦克对象池
 */
public class EnemyTanksPool {
    //用于保存所有的Tank的容器
    private static List<EnemyTank> pool = new ArrayList<>();
    //默认创建20个对象
    public static final int DEFAULT_POOL_SIZE = 20;
    //最多20个对象
    public static final int DEFAULT_POOL_MAX_SIZE = 20;
    static {    //类加载时创建子类对象添加到容器中
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new EnemyTank() {
            });
        }
    }

    /**
     * 从池中获得一个敌人Tank对象
     * @return
     */
    public static EnemyTank get(){
        EnemyTank tank = null;
        if(pool.size() == 0){
            //池中没坦克了
            tank = new EnemyTank();
        }else {
            tank = pool.remove(0);
        }
        //System.out.println("从对象池中获取了一个对象");
        return tank;
    }

    /**
     * tank(EnemyTank)被销毁的时候，归还到池中
     */
    public static void giveBack(Tank tank){
        if(pool.size() == DEFAULT_POOL_MAX_SIZE){
            //池中tank的个数已经达到最多
            return;
        }
        pool.add((EnemyTank) tank);
        //System.out.println("向对象池中归还了一个对象");
    }

}
