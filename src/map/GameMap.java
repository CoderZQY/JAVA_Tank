package map;

import game.GameStart;
import game.Levelinfo;
import tank.Tank;
import util.Constant;
import util.MapWallPool;
import util.MyUtil;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static tank.Tank.*;

/**
 * 游戏地图类
 */
public class GameMap {
    private static int MAP_X;
    private static int MAP_Y;
    private static final int MAP_WIDTH = Constant.FRAME_WIDTH-MAP_X*2;
    private static final int MAP_HEIGHT = Constant.FRAME_HEIGHT-MAP_Y-Tank.RADIUS*3;
    //地图元素块的容器
    private List<MapWall> walls = new ArrayList<>();
    //大本营
    private TankHouse house;
    public GameMap() {
        MAP_X = Tank.RADIUS*2;
        MAP_Y = Tank.RADIUS*2 + GameStart.titleBarHeight;
    }

    /**
     * 初始化地图元素块, level:第几关
     */
    public void initMap(int level){
        if(walls!=null){
            walls.clear();
        }
        //加载关卡
        loadLevel(level);
        //初始化老巢
        house = new TankHouse();
        addHouse();
    }

    /**
     * 加载关卡信息
     * @param level
     */
    private void loadLevel(int level) {
        //获得关卡信息类的唯一实例对象
        Levelinfo levelinfo = Levelinfo.getInstance();
        levelinfo.setLevel(level);  //设置关卡等级编号
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("GameLevel\\level_"+level);
            prop.load(fis);
            //将地图信息加载进来
            //1、解析敌人数量, 用于确定是否通关
            int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
            levelinfo.setEnemyCount(enemyCount);

            //2、对敌人类型进行解析
            String[] enemyType = prop.getProperty("enemyType").split(",");
            int[] type = new int[enemyType.length];
            for (int i = 0; i < type.length; i++) {
                type[i] = Integer.parseInt(enemyType[i]);
            }
            levelinfo.setEnemyType(type);
            //3、解析关卡数量
            String gameLevel = prop.getProperty("gameLevel");
            levelinfo.setLevel(Integer.parseInt(gameLevel==null?"1" : gameLevel));
            //根据游戏难度，调整敌人的默认速度、血量、攻击力
            int radix = Levelinfo.getInstance().getGameLevel();
            DEFAULT_ATK_MAX = (int)(DEFAULT_ATK_MAX+DEFAULT_ATK_MIN*0.2*(radix-1)); //每次难度提升1，数值提升百分之20
            DEFAULT_ATK_MIN = (int)(DEFAULT_ATK_MAX+DEFAULT_ATK_MIN*0.2*(radix-1)); //每次难度提升1，数值提升百分之20
            DEFAULT_HP_MAX = (int)(DEFAULT_HP_MAX+DEFAULT_HP_MAX*0.2*(radix-1));    //每次难度提升1，数值提升百分之20
            DEFAULT_SPEED = (int)(DEFAULT_SPEED+DEFAULT_SPEED*0.1*(radix-1));       //每次难度提升1，数值提升百分之10

            //4、解析调用的方法及次数
            String method = prop.getProperty("method");
            int invokeTime = Integer.parseInt(prop.getProperty("invokeTime"));

            //5、把实参读取到数组中来
            String[] parameters = new String[invokeTime];
            for (int i = 1; i <= invokeTime; i++) {
                parameters[i-1] = prop.getProperty("parameter"+i);
            }
            invokeMethod(method,parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据方法名和参数来调用方法
     * @param method        方法名
     * @param parameters    参数
     */
    private void invokeMethod(String method, String[] parameters) {
        for(String param:parameters){
            String[] split = param.split(",");
            int []arr = new int[split.length];
            for(int i=0; i<arr.length; i++){
                arr[i] = Integer.parseInt(split[i]);
            }
            int wallSide = MapWall.getWallSide();
            int type = arr[0];
            int startX = arr[1]*wallSide+MAP_X;
            int startY = arr[2]*wallSide+MAP_Y;
            int rows = arr[3];
            int interval_dist =arr[4]*wallSide;
            switch (method){
                case "addRow":
                    addRow(type,startX,startY,rows,interval_dist);
                    break;
                case "addCol":
                    addCol(type,startX,startY,rows,interval_dist);
                    break;
                case "addRect":
                    int cols = arr[4];
                    interval_dist = arr[5]*wallSide;
                    addRect(type,startX,startY,rows,cols,interval_dist);
                    break;
            }
        }
    }

    private void addHouse(){
        walls.addAll(house.getHomeWalls());
    }

    public void drawMap(Graphics g){
        //绘制除了遮挡物以外的所有块
        drawBk(g);
        //初始化大本营
        house.draw(g);
    }

    /**
     * 只对没有遮挡物效果的块进行绘制
     * @param g
     */
    public void drawBk(Graphics g){
        for (MapWall mw : walls){
            if(mw.getType() != MapWall.TYPE_COVER) {
                mw.draw(g);
            }
        }
    }

    /**
     * 只对有遮挡物效果的块进行绘制
     * @param g
     */
    public void drawCover(Graphics g){
        for (MapWall mw : walls){
            if(mw.getType() == MapWall.TYPE_COVER) {
                mw.draw(g);
            }
        }
    }
    /**
     * 将所有不可见的地图块移除
     */
    public void clearDestroyedWalls(){
        for (int i = 0; i < walls.size(); i++) {
            if(!walls.get(i).isVisible()){
                walls.remove(i);
            }
        }
    }

    /**
     * 在指定范围内随机画砖块
     * @param count         砖块的数量
     * @param count         砖块的类型
     * @param startX        范围的起始x坐标
     * @param startY        范围的起始y坐标
     * @param mapWidth      范围的宽度
     * @param mapHeight     范围的高度
     */
    public void addWallRandom(int count,int type,int startX,int startY,int mapWidth,int mapHeight){
        for (int i = 0; i < count; i++) {
            //在地图范围内随机添加count个地图块
            //int x = MyUtil.getRandomNumber(MAP_X,MAP_X+MAP_WIDTH-MapWall.getWallSide());
            //int y = MyUtil.getRandomNumber(MAP_Y,MAP_Y+MAP_HEIGHT-MapWall.getWallSide());
            int x = MyUtil.getRandomNumber(startX,startX+mapWidth-MapWall.getWallSide());
            int y = MyUtil.getRandomNumber(startY,startY+mapHeight-MapWall.getWallSide());
            if(isConflict(walls,x,y)){
                i--;
                continue;
            }
            MapWall mw = MapWallPool.get();
            mw.setX(x);
            mw.setY(y);
            mw.setType(type);
            //加入到walls集合中
            walls.add(mw);
        }
    }
    /**
     * 新出生的砖块是否与walls集合中的所有砖块发生冲突（有重叠部分）
     * @param walls
     * @param x
     * @param y
     * @return  有重叠，返回true，否则返回false
     */
    private boolean isConflict(List<MapWall> walls,int x,int y){
        for(MapWall mw : walls){
            int mw_x = mw.getX();
            int mw_y = mw.getY();
            if(Math.abs(mw_x - x) < MapWall.getWallSide() && Math.abs(mw_y - y) < MapWall.getWallSide()){
                return true;
            }
        }
        return false;
    }

    /**
     * 往地图元素块中添加一列元素
     * @param type              地图块的类型
     * @param startX            添加地图块的起始X坐标
     * @param startY            添加地图块的起始Y坐标
     * @param wallnum          代表这一列的地图块的个数
     * @param interval_dist      间隔的距离
     */
    public void addCol(int type,int startX,int startY,int wallnum,int interval_dist){
        //得到地图块的边长
        int sideLength = MapWall.getWallSide();
        //添加地图块
        for (int i = 0; i < wallnum; i++) {
            MapWall mw = MapWallPool.get();
            mw.setType(type);
            mw.setX(startX);
            mw.setY(startY+(sideLength+interval_dist)*i);
            walls.add(mw);
        }
    }
    /**
     * 往地图块添加一行指定类型的地图块
     * @param type              地图块的类型
     * @param startX            添加地图块的起始X坐标
     * @param startY            添加地图块的起始Y坐标
     * @param wallnum          代表这一行的地图块的个数
     * @param interval_dist      间隔的距离
     */
    public void addRow(int type,int startX,int startY,int wallnum,int interval_dist){
        int sideLength = MapWall.getWallSide();
        //添加地图块
        for (int i = 0; i < wallnum; i++) {
            MapWall mw = MapWallPool.get();
            mw.setType(type);
            mw.setX(startX + (sideLength+interval_dist)*i);
            mw.setY(startY);
            walls.add(mw);
        }
    }

    /**
     * 对指定的矩形区域添加等间距元素块
     * @param type                地图块的类型
     * @param startX              左上角横坐标
     * @param startY              左上角纵坐标
     * @param rowNum              每行元素块的个数
     * @param colNum              每列元素块的个数
     * @param interval_dist       间距
     */
    public void addRect(int type,int startX,int startY,int rowNum,int colNum,int interval_dist){
        for (int i = 0; i < colNum; i++) {
            int y = startY+i*(interval_dist+MapWall.getWallSide());
            addRow(type,startX,y,rowNum,interval_dist);
        }
    }
    public List<MapWall> getWalls() {
        return walls;
    }

    public void setWalls(List<MapWall> walls) {
        this.walls = walls;
    }
}
