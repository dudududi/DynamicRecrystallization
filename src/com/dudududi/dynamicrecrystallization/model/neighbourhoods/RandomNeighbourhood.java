package com.dudududi.dynamicrecrystallization.model.neighbourhoods;

import com.dudududi.dynamicrecrystallization.model.CellNeighbourhood;
import com.dudududi.dynamicrecrystallization.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/28/16.
 */
public class RandomNeighbourhood extends CellNeighbourhood {

    private List<CoordinatePair> indices;
    private int radius;

    public RandomNeighbourhood(boolean isPeriodic, int radius) {
        super(isPeriodic);
        this.radius = radius;
        generateIndices();
    }

    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        return indices;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        generateIndices();
    }

    @Override
    public String toString() {
        return "Losowe";
    }


    private void generateIndices() {
        indices = new ArrayList<>();
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if ((!(i == 0 && j == 0)) && Math.pow(i, 2) + Math.pow(j, 2) < Math.pow(radius, 2)) {
                    indices.add(new CoordinatePair(i, j));
                }
            }
        }
    }
}
