package com.dudududi.dynamicrecrystallization.model;

/**
 * Created by dudek on 5/19/16.
 */
public class DislocationsPool {
    public static final double CRITICAL_SIGMA = 80208201.5665831;
    private static final double A = 86710969050178.5;
    private static final double B = 9.41268203527779;
    private final double deltaTime;
    private double currentTime;
    private double currentDislocationDensity;
    private double previousDislocationDensity;

    public DislocationsPool(double startTime, double deltaTime) {
        this.deltaTime = deltaTime;
        this.currentTime = startTime;
        this.currentDislocationDensity = 0;
    }

    private void countCurrentDislocationDensity() {
        currentDislocationDensity = A / B + (1 - A / B) * Math.exp(-B * currentTime);
    }

    public double getNextDelta() {
        currentTime += deltaTime;
        countCurrentDislocationDensity();
        double delta = currentDislocationDensity - previousDislocationDensity;
        previousDislocationDensity = currentDislocationDensity;
        return delta;
    }

}
