package util;

import game.Born;

import java.util.ArrayList;
import java.util.List;

/**
 * 出生效果对象池
 */

public class BornsPool {
        //用于保存所有的出生效果的容器
        private static List<Born> pool = new ArrayList<>();
        //默认 假设不同时超过5个
        public static final int DEFAULT_POOL_SIZE = 5;
        //最多 5个
        public static final int DEFAULT_POOL_MAX_SIZE = 5;
        static {    //类加载时创建子类对象添加到容器中
            for (int i = 0; i < DEFAULT_POOL_SIZE; i++) {
                pool.add(new Born());
            }
        }

        /**
         * 从池中获得一个出生效果对象
         * @return
         */
        public static Born get(){
            Born born = null;
            if(pool.size() == 0){
                born = new Born();
            }else {
                born = pool.remove(0);
            }
            return born;
        }

        /**
         * 出生效果被销毁的时候，归还到池中
         */
        public static void giveBack(Born born){
            if(pool.size() == DEFAULT_POOL_MAX_SIZE){
                return;
            }
            pool.add(born);
        }
    }

