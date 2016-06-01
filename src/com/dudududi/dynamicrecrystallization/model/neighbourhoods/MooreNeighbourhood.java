package com.dudududi.dynamicrecrystallization.model.neighbourhoods;

import com.dudududi.dynamicrecrystallization.model.CellNeighbourhood;
import com.dudududi.dynamicrecrystallization.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/21/16.
 */
public class MooreNeighbourhood extends CellNeighbourhood {
    private List<CoordinatePair> indices;

    public MooreNeighbourhood() {
        super();
        generateIndices();
    }

    public MooreNeighbourhood(boolean isPeriodic) {
        super(isPeriodic);
        generateIndices();
    }

    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        return indices;
    }

    @Override
    public String toString() {
        return "Moore'a";
    }

    private void generateIndices() {
        indices = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) indices.add(new CoordinatePair(i, j));
            }
        }
    }
}
