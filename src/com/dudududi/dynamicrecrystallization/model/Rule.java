package com.dudududi.dynamicrecrystallization.model;

/**
 * Created by dudek on 4/21/16.
 */
public interface Rule {
    Cell.State determineState(final Cell cell);

    boolean requiresAdditionalCalculations();

    void calculate(Cell cell);
}
