package demo;

import java.awt.*;
import javax.swing.*;

public class bokeilx extends JFrame{
    private static Thread t; //声明线程
    private int coun=0;  //声明计算变量
    public bokeilx() {
        setTitle("使用线程让图片移动"); //设置窗体标题
        setBounds(390, 180, 800 , 300); //绝对定位窗体大小位置
        Container c=getContentPane(); //容器
        c.setLayout(null); //窗体不使用任何布局管理器
        JLabel m=new JLabel();
        ImageIcon icon=new ImageIcon("src/resource/message.png");		//获取图片
        //使图片适应标签大小
        icon.setImage(icon.getImage().getScaledInstance(100,60, Image.SCALE_DEFAULT));
        JLabel la=new JLabel();  //标签
        la.setIcon(icon);			//将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setBounds(10, 10,200,80);		//设置标签位置大小
        la.setOpaque(true);
        la.setText(String.valueOf(coun));


        t=new Thread(new Runnable() {
            @Override
            public void run() {
                while (coun<=500) {
                    la.setBounds(coun, 10,100, 60);		//将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠500毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    coun+=5;     //使横坐标每次加10
                    if(coun==500) {
                        //当图标到达最右边时，使其回到最左边
                        coun=10;
                    }
                }

            }
        });
        t.start(); //启动线程
        c.add(la);  //将标签添加到容器里面
        setVisible(true); //设置窗体可见
        setDefaultCloseOperation(EXIT_ON_CLOSE); //设置窗体退出样式
    }


    public static void main(String[] args) {
        new bokeilx();

    }

}
