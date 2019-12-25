package com.example.ihelp.EmergencySMS.Model;

/**
 * Created by 朱继涛 2019/11/8
 * 设置紧急情况向外界发送的求助信息时候记录按电源键的次数和时间限制
 */

public class PressCounter {

    private static int T = 4;// 按下T次促发预警
    private static int D = 10 * 1000;//10s的时间限制

    private static PressCounter instance;

    private int pressedCount;
    private long[] queue;

    private PressCounter() {
        pressedCount = 0;
        queue = new long[T];
    }

    public static PressCounter getInstance() {
        if (instance == null)
            instance = new PressCounter();
        return instance;
    }

    /***
     * 重置参数
     * @param t
     * @param d
     */
    public void reset(int t, int d) {
        T = t;
        D = d * 1000;
        pressedCount = 0;
        queue = new long[T];
    }

    /***
     * 添加一个按下的时间
     * @param time
     */
    public void add(long time) {
        queue[pressedCount % T] = time;
        pressedCount++;
    }

    /***
     * 检验是否促发
     * @return
     */
    public boolean check() {
        long max = max();
        long min = min();
        if ((max - min) > D || min == 0)
            return false;
        setZero();
        return true;
    }

    private long min() {
        long min = queue[0];
        for (int i = 0; i < T; i++) {
            if (min > queue[i])
                min = queue[i];
        }
        return min;
    }

    private long max() {
        long max = queue[0];
        for (int i = 0; i < T; i++) {
            if (max < queue[i])
                max = queue[i];
        }
        return max;
    }

    /**
     * 促发后，将队列的时间点全部设置为0，以免下线按一次就促发
     */
    private void setZero() {
        for (int i = 0; i < T; i++){
            queue[i] = 0;
        }
        pressedCount = 0;
    }

}
