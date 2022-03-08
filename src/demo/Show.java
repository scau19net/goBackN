package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UIManager;

public class Show extends JPanel {
    JFrame frame;
    private Thread move;

    JLabel sender;
    JLabel receiver;
    JLabel PacketLoss;
    JLabel AckLoss;
    JLabel timer;
    JLabel discardAck;
    JLabel need;
    boolean lossPacket=false;
    boolean lossAck=false;

    public Show () {
        frame = new JFrame();
        frame.setSize(840, 670);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        addSender();
        addReceiver();
        addPacketLoss();
        addAckLoss();
        addDiscard();
        addPacketLossButton();
        addAckLossButton();
        addDiscardAck();
        addNeed(1);



        frame.repaint();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                packetMove(12);
//            }
//        }).start();


    }
    public void addSender(){
        sender = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/sender.png");
        icon.setImage(icon.getImage().getScaledInstance(80, 160, Image.SCALE_DEFAULT));
        sender.setIcon(icon);
        sender.setHorizontalAlignment(SwingConstants.LEFT);
        sender.setOpaque(true);
        sender.setBounds(0,220,80,160);
        frame.add(sender);

    }

    public void addReceiver(){
        receiver = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/receiver.png");
        icon.setImage(icon.getImage().getScaledInstance(80, 160, Image.SCALE_DEFAULT));
        receiver.setIcon(icon);
        receiver.setHorizontalAlignment(SwingConstants.LEFT);
        receiver.setOpaque(true);
        receiver.setBounds(750,220,80,160);
        frame.add(receiver);
    }

    public void addPacketLoss(){
        PacketLoss = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/loss2.png");
        icon.setImage(icon.getImage().getScaledInstance(180, 80, Image.SCALE_DEFAULT));
        PacketLoss.setIcon(icon);
        PacketLoss.setHorizontalAlignment(SwingConstants.LEFT);
        PacketLoss.setOpaque(true);
        PacketLoss.setBounds(280,0,180,80);
        frame.add(PacketLoss);
    }

    public void addAckLoss(){
        AckLoss = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/ackLoss.png");
        icon.setImage(icon.getImage().getScaledInstance(180, 80, Image.SCALE_DEFAULT));
        AckLoss.setIcon(icon);
        AckLoss.setHorizontalAlignment(SwingConstants.LEFT);
        AckLoss.setOpaque(true);
        AckLoss.setBounds(270,510,260,120);
        frame.add(AckLoss);


    }
    public void addDiscard(){
        AckLoss = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/discard.png");
        icon.setImage(icon.getImage().getScaledInstance(180, 35, Image.SCALE_DEFAULT));
        AckLoss.setIcon(icon);
        AckLoss.setHorizontalAlignment(SwingConstants.LEFT);
        AckLoss.setOpaque(true);
        AckLoss.setBounds(600,40,180,35);
        frame.add(AckLoss);


    }
    public void addDiscardAck(){
        discardAck = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/discardAck.png");
        icon.setImage(icon.getImage().getScaledInstance(180, 60, Image.SCALE_DEFAULT));
        discardAck.setIcon(icon);
        discardAck.setHorizontalAlignment(SwingConstants.LEFT);
        discardAck.setOpaque(true);
        discardAck.setBounds(0,560,180,60);
        frame.add(discardAck);


    }

    public void addTimer(int time){
        timer = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/timer.png");
        icon.setImage(icon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT));
        timer.setIcon(icon);
        timer.setHorizontalAlignment(SwingConstants.LEFT);
        timer.setOpaque(true);
        timer.setFont(new Font("黑体",0,23));
        timer.setText(String.valueOf(time));
        timer.setVerticalTextPosition(JLabel.CENTER);
        timer.setHorizontalTextPosition(JLabel.CENTER);
        frame.add(timer);



    }

    public void addNeed(int Base){
        need = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/need.png");
        icon.setImage(icon.getImage().getScaledInstance(120, 40, Image.SCALE_DEFAULT));
        need.setIcon(icon);
        need.setHorizontalAlignment(SwingConstants.LEFT);
        need.setOpaque(true);
        need.setFont(new Font("黑体",0,23));
        need.setText(String.valueOf(Base));
        need.setVerticalTextPosition(JLabel.CENTER);
        need.setHorizontalTextPosition(JLabel.CENTER);
        need.setBounds(750,380,120,40);
        frame.add(need);



    }





    public void addPacketLossButton(){

        Button suspend = new Button("暂停");
        suspend.setBounds(100,0,160,50);
        suspend.setVisible(true);
        suspend.setLabel("PacketLoss");
        suspend.setFont(new Font("黑体",0,23));
        suspend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lossPacket=!lossPacket;
                suspend.setLabel(String.valueOf("PacketLoss:"+lossPacket));
            }
        });


        frame.add(suspend);




    }
    public void addAckLossButton(){

        Button suspend = new Button("暂停");
        suspend.setBounds(100,70,150,50);
        suspend.setVisible(true);
        suspend.setLabel("AckLoss");
        suspend.setFont(new Font("黑体",0,23));
        suspend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lossAck=!lossAck;
                suspend.setLabel("ACKLoss:"+String.valueOf(lossAck));
            }
        });
        frame.add(suspend);
    }

    public void ackMove(int ackId){
        
        JLabel la = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/ack.png");        //获取图片
        //使图片适应标签大小
        icon.setImage(icon.getImage().getScaledInstance(60, 40, Image.SCALE_DEFAULT));
        la.setIcon(icon);            //将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setOpaque(true);
        la.setText(String.valueOf(ackId));
        la.setVerticalTextPosition(JLabel.CENTER);
        la.setHorizontalTextPosition(JLabel.CENTER);
        la.setFont(new Font("黑体",0,23));

        la.setBounds(700, 320, 60, 40);        //设置标签位置大小
        frame.add(la);



        move=new Thread(new Runnable() {
            int x=700;
            @Override
            public void run() {
                while (x >80) {
                    la.setBounds(x, 320, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠500毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    x -= 5;     //使横坐标每次加10
                    if (x <=80) {
                        //当图标到达最右边时，使其回到最左边
                        break;
                    }
                }
                frame.remove(la);

            }

        });

        move.start();


    }








    //显示发送的分组
    public void packetMove(int sendId){
        JLabel la = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/msg.png");        //获取图片
        //使图片适应标签大小
        icon.setImage(icon.getImage().getScaledInstance(60, 40, Image.SCALE_DEFAULT));
        la.setIcon(icon);            //将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setOpaque(true);
        la.setText(String.valueOf(sendId));
        la.setFont(new Font("黑体",0,23));

        la.setVerticalTextPosition(JLabel.CENTER);
        la.setHorizontalTextPosition(JLabel.CENTER);


        la.setBounds(0, 220, 60, 40);        //设置标签位置大小
        frame.add(la);



        move=new Thread(new Runnable() {
            int x=0;
            @Override
            public void run() {
                while (x <= 750) {
                    la.setBounds(x, 220, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    x += 5;     //使横坐标每次加5
                    if (x >= 700) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                frame.remove(la);

            }

        });

        move.start();


    }

    //显示分组丢失动画
    public void showPacketLoss(int sendId){
        JLabel la = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/msg.png");        //获取图片
        //使图片适应标签大小
        icon.setImage(icon.getImage().getScaledInstance(60, 40, Image.SCALE_DEFAULT));
        la.setIcon(icon);            //将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setOpaque(true);
        la.setText(String.valueOf(sendId));
        la.setFont(new Font("黑体",0,23));
        la.setVerticalTextPosition(JLabel.CENTER);
        la.setHorizontalTextPosition(JLabel.CENTER);
        la.setBounds(0, 220, 60, 40); //设置标签位置大小
        frame.add(la);



        move=new Thread(new Runnable() {
            int x=0;
            int y=220;
            @Override
            public void run() {
                while (x <= 380) {
                    la.setBounds(x, 220, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    x += 5;     //使横坐标每次加5
                    if (x >= 350) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                while (y>0) {
                    la.setBounds(x, y, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    y-=5;     //使横坐标每次加5
                    if (y<80) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                frame.remove(la);

            }

        });

        move.start();


    }

    //显示ACK丢失动画
    public void showAckLoss(int sendId){
        JLabel la = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/ack.png");        //获取图片
        //使图片适应标签大小
        icon.setImage(icon.getImage().getScaledInstance(60, 40, Image.SCALE_DEFAULT));
        la.setIcon(icon);            //将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setOpaque(true);
        la.setText(String.valueOf(sendId));
        la.setFont(new Font("黑体",0,23));
        la.setVerticalTextPosition(JLabel.CENTER);
        la.setHorizontalTextPosition(JLabel.CENTER);
        la.setBounds(700, 220, 60, 40); //设置标签位置大小
        frame.add(la);



        move=new Thread(new Runnable() {
            int x=700;
            int y=320;
            @Override
            public void run() {
                while (x >= 350) {
                    la.setBounds(x, 320, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    x -= 5;     //使横坐标每次加5
                    if (x <= 350) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                while (y<530) {
                    la.setBounds(x, y, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    y+=5;     //使横坐标每次加5
                    if (y>470) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                frame.remove(la);

            }

        });

        move.start();


    }


    //显示丢弃乱序的分组
    public void showDiscard(int sendId){
        JLabel la = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/msg.png");        //获取图片
        //使图片适应标签大小
        icon.setImage(icon.getImage().getScaledInstance(60, 40, Image.SCALE_DEFAULT));
        la.setIcon(icon);            //将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setOpaque(true);
        la.setText(String.valueOf(sendId));
        la.setFont(new Font("黑体",0,23));
        la.setVerticalTextPosition(JLabel.CENTER);
        la.setHorizontalTextPosition(JLabel.CENTER);
        la.setBounds(0, 220, 60, 40); //设置标签位置大小
        frame.add(la);



        move=new Thread(new Runnable() {
            int x=0;
            int y=220;
            @Override
            public void run() {
                while (x <= 700) {
                    la.setBounds(x, 220, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    x += 5;     //使横坐标每次加5
                    if (x >= 700) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                while (y>0) {
                    la.setBounds(x, y, 100, 60);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    y-=5;     //使横坐标每次加5
                    if (y<80) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                frame.remove(la);

            }

        });

        move.start();


    }

    //显示丢弃迟到的失效的ACK
    public void showAckDiscard(int sendId){
        JLabel la = new JLabel();
        ImageIcon icon = new ImageIcon("src/resource/ack.png");        //获取图片
        //使图片适应标签大小
        icon.setImage(icon.getImage().getScaledInstance(60, 40, Image.SCALE_DEFAULT));
        la.setIcon(icon);            //将图片放在标签中
        la.setHorizontalAlignment(SwingConstants.LEFT); //设置图片在标签的最左边
        la.setOpaque(true);
        la.setText(String.valueOf(sendId));
        la.setFont(new Font("黑体",0,23));
        la.setVerticalTextPosition(JLabel.CENTER);
        la.setHorizontalTextPosition(JLabel.CENTER);
        la.setBounds(0, 220, 60, 40); //设置标签位置大小
        frame.add(la);



        move=new Thread(new Runnable() {
            int x=700;
            int y=320;
            @Override
            public void run() {
                while (x > 80) {
                    la.setBounds(x, 320, 60, 40);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    x -= 5;     //使横坐标每次加5
                    if (x <= 80) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                while (y<630) {
                    la.setBounds(x, y, 60, 40);        //将标签的横坐标用变量显示
                    try {
                        Thread.sleep(10); //使线程睡眠10毫秒
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    y+=5;     //使横坐标每次加5
                    if (y>=630) {
                        //当图标到达最右边时，结束
                        break;
                    }
                }
                frame.remove(la);

            }

        });

        move.start();


    }




}

class Location {
    public int x=0;
    public int y=0;
    public int z=0;
}