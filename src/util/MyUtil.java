package util;

import java.awt.*;

/**
 * 工具类
 */
public class MyUtil {
    private MyUtil(){}

    /**
     * 得到指定区间的随机数
     * @param min   区间最小值，包含
     * @param max   区间最大值，不包含
     * @return      随机数
     */
    public static final int getRandomNumber(int min, int max){
        return (int)(Math.random()*(max-min)+min);
    }

    /**
     * 得到随机颜色
     * @return
     */
    public static final Color getRandomColor(){
        int red = getRandomNumber(0,256);
        int green = getRandomNumber(0,256);
        int blue = getRandomNumber(0,256);
        Color color = new Color(red,green,blue);
        return color;
    }

    /**
     *判断一个点是否在一个正方形的内部
     * @param recX  正方形中心的横坐标
     * @param recY  正方形中心的纵坐标
     * @param pointX    点的横坐标
     * @param pointY    点的纵坐标
     * @param radius    点的半径
     * @return  在返回true，不在返回false
     */
    public static final boolean isCrash(int recX, int recY, int pointX, int pointY,int radius){
        int disX = Math.abs(recX - pointX);
        int disY = Math.abs(recY - pointY);
        if(disX < radius && disY < radius){
            return true;
        }
        return false;
    }

    /**
     * 根据图片资源的路径，来加载图片对象
     * @param path  图片路径
     * @return
     */
    public static final Image createImg(String path){
        return Toolkit.getDefaultToolkit().createImage(path);
    }
    private static final String[] NAMES = {
            "雨润静荷","纽约城门口旳小木匠","烟沫…凡尘","半夏的哀伤","霹雳搅屎棍","凉城旧梦°", "花の物语", "凉城★旧梦",
            "嚒嚒~","静若繁花","ゞ流浪年少ゞ", "雪落樱花美", "しovё冷颜ヾ", "明月风清", "结发绾袖","半字浅眉","倾城月光",
            "寄情书", "醉酒诗人", "忆春风","兰陵王","绣春刀","妆奴笑","浮生醉清风", "o花开月圆﹎","颜小瞳", "毁の灭性",
            "最终メ狂暴","╰嫑忐╯", "泽畔东篱", "西瓜恋南瓜", "醉卧江山","诗韵梵吟","爱卿","浅吻＠","半根烟闯江湖","想飞的鱼",
            "东方清苑","与君共勉","剑留痕","★罄竹懷柔メ", "藍夢玲蝶","ぃ本宫叫帅姐","灰常鸡动","凉生╰╯", "燃烧的蔬菜","念旧人",
            "绻影浮沉", "横笛听雨" ,"私念Ω"
    };

    /**
     * 获取一个随机系统名字
     * @return
     */
    public static final String getRandomName(){
        return NAMES[getRandomNumber(0,NAMES.length)];
    }
}
