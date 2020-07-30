package util;

import tank.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * 子弹对象池类
 */
public class BulletsPool {
    //用于保存所有的子弹的容器
    private static List<Bullet> pool = new ArrayList<>();
    //默认创建200个对象
    public static final int DEFAULT_POOL_SIZE = 200;
    //最多300个对象
    public static final int DEFAULT_POOL_MAX_SIZE = 300;
    static {    //类加载时创建子类对象添加到容器中
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Bullet());
        }
    }

    /**
     * 从池中获得一个子弹对象
     * @return
     */
    public static Bullet get(){
        Bullet bullet = null;
        if(pool.size() == 0){
            //池中没有子弹了
            bullet = new Bullet();
        }else {
            bullet = pool.remove(0);
        }
        //System.out.println("从对象池中获取了一个对象");
        return bullet;
    }

    /**
     * 子弹被销毁的时候，归还到池中
     */
    public static void giveBack(Bullet bullet){
        if(pool.size() == DEFAULT_POOL_MAX_SIZE){
            //池中子弹的个数已经达到最多
            return;
        }
        pool.add(bullet);
        //System.out.println("向对象池中归还了一个对象");
    }
}
