package com.yzh.loading;

import javax.swing.*;
import java.awt.*;

/**
 * @author Yzh
 * @create 2020-12-11 15:14
 */
public class LoadingDemo {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        InfiniteProgressPanel glasspane = new InfiniteProgressPanel("加载中,请稍后...", 9, 0.3f, 21, 5);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        glasspane.setBounds(0, 0, dimension.width, dimension.height);
        glasspane.start();//启动加载效果
        jFrame.add(glasspane);//此处也可以使用jFrame.setGlassPane(glasspane)但是不明应用场景可能出现动画异常效果.
//        new Thread() {//进程停顿几秒,模拟加载动画展示时间
//            public void run() {
//                for (int i = 0; i <= 30; i++) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                glasspane.stop();
//            }
//        }.start();
        jFrame.setVisible(true);
    }

}
