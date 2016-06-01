package com.dudududi.dynamicrecrystallization.model.neighbourhoods;

import com.dudududi.dynamicrecrystallization.model.CellNeighbourhood;
import com.dudududi.dynamicrecrystallization.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/28/16.
 */
public class VonNeumannNeighbourhood extends CellNeighbourhood {
    private List<CoordinatePair> indices;

    public VonNeumannNeighbourhood(boolean isPeriodic) {
        super(isPeriodic);
        generateIndices();
    }

    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        return indices;
    }

    @Override
    public String toString() {
        return "Von Neumann'a";
    }

    private void generateIndices() {
        indices = new ArrayList<>();
        indices.add(new CoordinatePair(-1, 0));
        indices.add(new CoordinatePair(1, 0));
        indices.add(new CoordinatePair(0, -1));
        indices.add(new CoordinatePair(0, 1));
    }
}
