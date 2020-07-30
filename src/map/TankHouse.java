package map;

import util.Constant;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TankHouse {
    //家的xy坐标
    public static final int HOUSE_X = (Constant.FRAME_WIDTH-3*MapWall.getWallSide())/2;
    public static final int HOUSE_Y = Constant.FRAME_HEIGHT-2*MapWall.getWallSide();
    //一共5块地图块
    private List<MapWall> HomeWalls = new ArrayList<>();
    public TankHouse(){
        int WallLen = MapWall.getWallSide();
        HomeWalls.add(new MapWall(HOUSE_X,HOUSE_Y));
        HomeWalls.add(new MapWall(HOUSE_X,HOUSE_Y+WallLen));
        HomeWalls.add(new MapWall(HOUSE_X+WallLen,HOUSE_Y));
        HomeWalls.add(new MapWall(HOUSE_X+WallLen*2,HOUSE_Y));
        HomeWalls.add(new MapWall(HOUSE_X+WallLen*2,HOUSE_Y+WallLen));
        //建老巢
        HomeWalls.add(new MapWall(HOUSE_X+WallLen,HOUSE_Y+WallLen));
        HomeWalls.get(HomeWalls.size()-1).setType(MapWall.TYPE_HOUSE);
    }
    public void draw(Graphics g){
        for (MapWall mw : HomeWalls
             ) {
            if(!mw.isVisible() && mw.isHouse()){
                mw.drawhomeDestroyed(g);
            }
            else{
                mw.draw(g);
            }
        }
    }

    public List<MapWall> getHomeWalls() {
        return HomeWalls;
    }

    public void setHomeWall(List<MapWall> homeWall) {
        HomeWalls = homeWall;
    }
}
