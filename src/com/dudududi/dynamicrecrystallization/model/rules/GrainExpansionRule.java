package com.dudududi.dynamicrecrystallization.model.rules;

import com.dudududi.dynamicrecrystallization.model.Cell;
import com.dudududi.dynamicrecrystallization.model.Rule;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/28/16.
 */
public class GrainExpansionRule implements Rule {
    @Override
    public Cell.State determineState(final Cell cell) {
        if (cell.isAlive()) return null;
        Map<Color, Long> counts = cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive())
                .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()));
        Cell.State state = null;
        if (counts.size() != 0) {
            Color highestColor = counts.entrySet().stream()
                    .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                    .get()
                    .getKey();
            state = Cell.State.createAliveWithColor(highestColor);
        }
        return state;
    }

    @Override
    public boolean requiresAdditionalCalculations() {
        return false;
    }

    @Override
    public void calculate(Cell cell) {
    }
}
