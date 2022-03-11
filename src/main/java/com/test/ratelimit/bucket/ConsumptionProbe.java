package com.test.ratelimit.bucket;

public class ConsumptionProbe {

    private final boolean consumed;

    private ConsumptionProbe(boolean consumed) {
        this.consumed = consumed;

    }

    public static ConsumptionProbe consumed() {
        return new ConsumptionProbe(true);
    }

    public static ConsumptionProbe rejected() {
        return new ConsumptionProbe(false);
    }

    public boolean isConsumed() {
        return consumed;
    }
}
