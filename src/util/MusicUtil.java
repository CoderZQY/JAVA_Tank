package util;

import sun.applet.AppletAudioClip;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

public class MusicUtil {
    private static AudioClip start;
    private static AudioClip bomb;
    private static AudioClip fire;
    private static AudioClip hit;
    private static AudioClip add;
    private static AudioClip move;
    //装填音乐资源
    static {
        //引号里面的音乐文件是绝对路径
        try {
            start = Applet.newAudioClip(new File("music/start.wav").toURL());//加载开始音效
            bomb = Applet.newAudioClip(new File("music/blast.wav").toURL());//加载爆炸音效
            fire = Applet.newAudioClip(new File("music/fire.wav").toURL());//加载开火音效
            hit = Applet.newAudioClip(new File("music/hit.wav").toURL());//加载击中音效
            add = Applet.newAudioClip(new File("music/add.wav").toURL());//加载诞生音效
            move = Applet.newAudioClip(new File("music/move2.wav").toURL());//加载移动音效
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    //播放音频
    public static void playStart(){
        start.play();
    }
    public static void playBlast(){
        bomb.play();
    }

    public static void playFire() {
        fire.play();
    }
    public static void playHit() {
        hit.play();
    }
    public static void playAdd() {
        add.play();
    }
    public static void playMove() {
        move.play();
    }

}
