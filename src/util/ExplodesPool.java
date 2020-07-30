package util;

import game.Explode;

import java.util.ArrayList;
import java.util.List;

/**
 * 爆炸效果对象池
 */
public class ExplodesPool {
    //用于保存所有的爆炸效果的容器
    private static List<Explode> pool = new ArrayList<>();
    //默认创建10个对象
    public static final int DEFAULT_POOL_SIZE = 10;
    //最多20个对象
    public static final int DEFAULT_POOL_MAX_SIZE = 20;
    static {    //类加载时创建子类对象添加到容器中
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new Explode());
        }
    }

    /**
     * 从池中获得一个爆炸对象
     * @return
     */
    public static Explode get(){
        Explode explode = null;
        if(pool.size() == 0){
            explode = new Explode();
        }else {
            explode = pool.remove(0);
        }
        return explode;
    }

    /**
     * 爆炸效果被销毁的时候，归还到池中
     */
    public static void giveBack(Explode explode){
        if(pool.size() == DEFAULT_POOL_MAX_SIZE){
            return;
        }
        pool.add(explode);
    }

}
