package util;

import map.MapWall;

import java.util.ArrayList;
import java.util.List;

public class MapWallPool {
    //用于保存所有的砖块的容器
    private static List<MapWall> pool = new ArrayList<>();
    //默认创建50个对象
    public static final int DEFAULT_POOL_SIZE = 50;
    //最多70个对象
    public static final int DEFAULT_POOL_MAX_SIZE = 70;
    static {    //类加载时创建子类对象添加到容器中
        for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
            pool.add(new MapWall());
        }
    }

    /**
     * 从池中获得一个砖块对象
     * @return
     */
    public static MapWall get(){
        MapWall mapWall = null;
        if(pool.size() == 0){
            mapWall = new MapWall();
        }else {
            mapWall = pool.remove(0);
        }
        return mapWall;
    }

    /**
     * 砖块被销毁的时候，归还到池中
     */
    public static void giveBack(MapWall mapWall) {
        if (pool.size() == DEFAULT_POOL_MAX_SIZE) {
            return;
        }
        pool.add(mapWall);
    }
}
