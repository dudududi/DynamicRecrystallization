package com.dudududi.dynamicrecrystallization.model.neighbourhoods;

import com.dudududi.dynamicrecrystallization.model.CellNeighbourhood;
import com.dudududi.dynamicrecrystallization.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/28/16.
 */
public class HexagonalNeighbourhood extends CellNeighbourhood {
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private int whichSide;
    private List<CoordinatePair> indices;

    public HexagonalNeighbourhood(boolean isPeriodic, int whichSide) {
        super(isPeriodic);
        this.whichSide = whichSide;
        generateIndices();
    }

    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        return indices;
    }

    @Override
    public String toString() {
        String description = "Heksadecymalne ";
        description += whichSide == LEFT ? "lewe" : "prawe";
        return description;
    }

    private void generateIndices() {
        indices = new ArrayList<>();
        switch (whichSide) {
            case RIGHT:
                indices.add(new CoordinatePair(-1, 0));
                indices.add(new CoordinatePair(-1, 1));
                indices.add(new CoordinatePair(0, 1));
                indices.add(new CoordinatePair(1, 0));
                indices.add(new CoordinatePair(1, -1));
                indices.add(new CoordinatePair(0, -1));
                break;
            case LEFT:
                indices.add(new CoordinatePair(0, 1));
                indices.add(new CoordinatePair(1, 1));
                indices.add(new CoordinatePair(1, 0));
                indices.add(new CoordinatePair(0, -1));
                indices.add(new CoordinatePair(-1, -1));
                indices.add(new CoordinatePair(-1, 0));
                break;
        }
    }
}
