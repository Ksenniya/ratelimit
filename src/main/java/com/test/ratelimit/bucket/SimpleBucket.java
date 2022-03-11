package com.test.ratelimit.bucket;

public class SimpleBucket {
    private final long capacity;
    private final long duration;
    private double available;
    private long lastTimeStamp;

    public SimpleBucket(int capacity, long duration) {
        this.capacity = capacity;
        this.duration = duration * 60000;
        available = capacity;
        lastTimeStamp = System.currentTimeMillis();
    }

    synchronized public ConsumptionProbe tryConsume() {
        long now = System.currentTimeMillis();
        if (now > lastTimeStamp + duration)
            available = capacity;
        if (available < 1)
            return ConsumptionProbe.rejected();
        else {
            available--;
            lastTimeStamp = now;
            return ConsumptionProbe.consumed();
        }
    }
}
