package com.dudududi.dynamicrecrystallization.model.rules;

import com.dudududi.dynamicrecrystallization.model.Cell;
import com.dudududi.dynamicrecrystallization.model.DislocationsPool;
import com.dudududi.dynamicrecrystallization.model.Rule;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by dudek on 5/29/16.
 */
public class DynamicRecrystallizationRule implements Rule {
    private static final double START_TIME = 0;
    private static final double DELTA_TIME = 0.01;

    private DislocationsPool dislocationsPool;
    private int automatonSize;
    private double averageDislocationsDensity;

    public DynamicRecrystallizationRule(int width, int height) {
        this.automatonSize = width * height;
        dislocationsPool = new DislocationsPool(START_TIME, DELTA_TIME);
    }

    @Override
    public Cell.State determineState(final Cell cell) {
        Cell.State state = null;
        if (cell.isRecrystallized()) return null;
        if (cell.getDislocations() > DislocationsPool.CRITICAL_SIGMA) {
            state = Cell.State.createAliveWithRandomColor();
            state.isRecrystallized = true;
            return state;
        }
        Cell recrystallized = cell.getNeighbourhood().stream()
                .filter(c -> c.isRecrystallized())
                .findAny()
                .orElse(null);
        if (recrystallized != null) {
            state = Cell.State.createFromCell(recrystallized);
        }
        return state;
    }

    @Override
    public boolean requiresAdditionalCalculations() {
        averageDislocationsDensity = dislocationsPool.getNextDelta() / automatonSize;
        return true;
    }

    @Override
    public void calculate(Cell cell) {
        if (cell.isRecrystallized()) return;
        boolean isOnEdge = cell.getNeighbourhood().stream()
                .filter(c -> !c.getColor().equals(cell.getColor()))
                .findAny()
                .isPresent();
        if (isOnEdge) {
            cell.addDislocations(ThreadLocalRandom.current().nextDouble(1.2, 1.8) * averageDislocationsDensity);
        } else {
            cell.addDislocations(ThreadLocalRandom.current().nextDouble(0., 0.3) * averageDislocationsDensity);
        }
    }

}
