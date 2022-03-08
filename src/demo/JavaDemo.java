package demo;

import javax.swing.*;
import java.util.*;

public class JavaDemo {

    public static void main(String args[]){
        final Show show = new Show();
        new Thread(new Sender(show)).start();
    }
}
class Sender implements Runnable{


    JFrame frame;
    Show show;
    Data data;

    public Sender(Show show){
        this.show=show;
    }

    @Override
    public void run() {
        data = new Data();
        new Thread(new Reciever(data,show)).start();

        sender();


    }

    public void sender(){
        //滑动窗口协议

        Timer timer = new Timer(data,show);
        new ACKReceive(timer,data,show).start();
        timer.start();
        timer.suspend();

        while(true){
            try {
                Thread.sleep(1000);//原来是1000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //超时处理：启动计时器+重发
            if(data.timeOut){
                //重置计时器
                data.timeOut=false;
                timer.time=10;
                timer.resume();
                data.nextSeqnum=data.nextSeqnum-data.WINDOWS_SIZE;//这里修改了Base 会影响Timer的判断---------------------
                System.out.println("重发");
//                for(int i=0;i<data.nextSeqnum-data.Base;i++){
//                    //但不要太快，避免ACK确认跟不上
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    //重发要快，要赶在receiveAck修改Base之前
//                    System.out.print("重发");
//                    send(data.Base+i);
//
//
//                }

            }
            //正常发送
            if(data.nextSeqnum<data.Base+data.WINDOWS_SIZE){

                send(data.nextSeqnum);
                //发完第一个分组开始计时
                if(data.Base==data.nextSeqnum){
                    timer.time=10;
                    timer.resume();
                }
                data.nextSeqnum++;
            }
            else{
                try {
                    //暂无可发送的分组
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void send(int sendId){
        System.out.println("发送分组"+sendId);
        data.packet.offer(sendId);//
        System.out.print("packet队列：");
        for(int packet: data.packet){
            System.out.print(packet);
        }
        System.out.println();
        //显示发送动画
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                show.packetMove(sendId);
//            }
//        }).start();
//        //发慢一点
//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
class Timer extends Thread{
    //首先要取得ACK，进而得到Base
    Data data;
    Show show;
    public int time=10;
    public Timer(Data data,Show show){
        this.show=show;
        this.data=data;
    }

    public void run(){
        startTimer();
    }

    public void startTimer(){
        data.timeOut=false;
        System.out.println("-------计时器启动-------");
        show.addTimer(time);


        System.out.println("显示计时器");
        while(true){
            while(time-->0){

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while(true){
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            show.addTimer(time);
//
//
//                        }
//
//                    }
//                }).start();

                show.timer.setBounds(560,460,80,80);
                show.timer.setText(String.valueOf(time));



                try {
                    Thread.sleep(1000);//--------------------------
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                if(data.Base==data.nextSeqnum){
                    break;













                }
                System.out.println("time = " + time);
            }
            //超时
            if (time <=0) {
                data.timeOut=true;
                //Thread.currentThread().suspend();
                System.out.println("暂停了计时器");
            }



        }



    }

}

class Data{
   public int Base=1;
   public int nextSeqnum=1;
   public int ack=0;
   public final int WINDOWS_SIZE=5;
   public boolean timeOut=false;
   Queue<Integer> packet;
   Queue<Integer> acks;
   public Data(){
       packet = new LinkedList<>();
       acks= new ArrayDeque<>();
   }

}

class Reciever implements Runnable{

    Data data;
    Show show;
    int packetLoss=0;
    int posibility=0;
    Random random;

    public Reciever(Data data,Show show){
        this.show=show;
        this.data=data;
        random=new Random();
    }

    @Override
    public void run() {
        //收到packet，回复确认ACK
        while(true){
            try {
                Thread.sleep(20);//以前是800
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(data.packet.size()>0) {

                //模拟网络延迟
                Random random= new Random();
                int delay= random.nextInt(1);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //模拟掉包
                Random random1 = new Random();
                int probability = random1.nextInt(10);
                if(show.lossPacket||probability>10){
                    System.out.println("                       发生掉包");
                    //System.out.println("                       loosPacket="+show.lossPacket);
                    if(data.packet.size()>0){
                        packetLoss=data.packet.poll();
                        System.out.println("                    丢失了分组：" + packetLoss);
                        showPacketLoss(packetLoss);
                    }
                }
                //判断是否按序到达
                if(data.packet.size()>0&&show.lossPacket==false){
                    System.out.println("                    data.ack = " + data.ack);
                    System.out.println("                    data.packet.peek() = " + data.packet.peek());
                    //ACK丢失导致的重发，此时也要发送ACK
                    if(data.packet.peek()<=data.ack){
                        System.out.println("                    接收到重复分组："+data.packet.peek());
                        showPacketDiscard(data.packet.peek());
                        data.acks.offer(data.packet.poll());
                    }
                    else{
                        if(data.packet.peek()==data.ack+1){

                            while(show.lossPacket==false&&data.packet.size()>0) {
                                //寻找可用的ACK的最大序号
                                posibility=random.nextInt(10);
                                if(data.packet.peek()==data.ack+1){
                                    data.ack=data.packet.poll();
//                                    try {
//                                        Thread.sleep(500);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
                                    showPacket(data.ack);
                                    show.need.setText(String.valueOf(data.ack+1));
                                }
                                else{
                                    break;
                                }
                                if(posibility>10){
                                    break;
                                }
                            }
                            System.out.println("                    接收到分组"+data.ack);
                            show.need.setText(String.valueOf(data.ack+1));
                            show.setBounds(750,380,120,60);
                            data.acks.offer(data.ack);
                        }
                        else{
                            showPacketDiscard(data.packet.peek());
                            System.out.println("                    乱序到达，丢弃分组："+data.packet.poll());

                        }
                    }

                }







//
//                System.out.print(" packet队列：");
//                for(int packet:data.packet){
//
//                    System.out.print(packet+" ");
//                }
//                System.out.println();
            }

        }

    }

    private void showPacketLoss(int packetLoss) {

        //显示发送丢失动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                show.showPacketLoss(packetLoss);
            }
        }).start();
        //发慢一点
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    private void showPacketDiscard(int packetDiscard) {

        //显示丢弃乱序分组动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                show.showDiscard(packetDiscard);
            }
        }).start();
        //发慢一点
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void showPacket(int sendId){

        //显示发送动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                show.packetMove(sendId);
            }
        }).start();
        //发慢一点
        try {
            Thread.sleep(1500);//以前是1500
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}







class ACKReceive extends Thread{
    Timer timer;
    Data data;
    Show show;
    private boolean isWaiting=false;
    public ACKReceive(Timer timer, Data data,Show show){
        this.data=data;
        this.timer=timer;
        this.show=show;
    }
    public void run(){
        //接收ACK

        //避免进入死循环
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }
            //System.out.println("data.acks.size() = " + data.acks.size());

            //模拟ACK丢失，有可能导致重发，也有可能不重发
            //!!!注意连续5个ACK丢失的情况
            Random random = new Random();
            int probability = random.nextInt(10);
            if(data.acks.size()>0&&(probability>10||show.lossAck)){
                showAckLoss(data.acks.peek());

                System.out.println("丢失了ACK："+data.acks.poll());
                //System.out.println("time="+timer.time);
            }

            if(show.lossAck==false&&(data.acks.size()>0&&data.Base<data.acks.peek()+1)){
                //如果先收到序号较大的确认，再收到序号小的则不重置Base
                data.Base=data.acks.poll()+1;//-------------------------------------------
                System.out.println("收到确认分组："+(data.Base-1));

                //防止反复重发引起超时
                if(data.nextSeqnum<data.Base){
                    data.nextSeqnum=data.Base;
                }


                //调整时间
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                showAck(data.Base-1);

                if(data.Base==data.nextSeqnum){
                    //停止计时器
                    timer.suspend();
                    isWaiting=true;
                    System.out.println("暂停了计时器");//--------------------why？
                }
                //重置计时器
                else {

                    timer.time=10;
                    if(isWaiting){
                        timer.resume();
                        System.out.println("重启了计时器");
                        isWaiting=false;
                    }



                }


            }

            if(data.acks.size()>0&&data.Base>=data.acks.peek()+1){

                showAckDiscard(data.acks.poll());


            }


        }

    }

    private void showAckDiscard(int ackId) {
        //显示发送丢失动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                show.showAckDiscard(ackId);
            }
        }).start();
        //发慢一点
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showAckLoss(int ackLoss) {

        //显示发送丢失动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                show.showAckLoss(ackLoss);
            }
        }).start();
        //发慢一点
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }



    public void showAck(int ackId){

        //显示发送动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                show.ackMove(ackId);
            }
        }).start();

        //发慢一点
        try {
            Thread.sleep(1500);//原来是1500
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}