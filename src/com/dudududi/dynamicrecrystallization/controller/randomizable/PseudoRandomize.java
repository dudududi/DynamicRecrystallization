package com.dudududi.dynamicrecrystallization.controller.randomizable;

import com.dudududi.dynamicrecrystallization.controller.Randomizable;
import com.dudududi.dynamicrecrystallization.model.Cell;
import com.dudududi.dynamicrecrystallization.model.CellAutomaton;

import java.util.Random;

/**
 * Created by dudek on 5/7/16.
 */
public class PseudoRandomize implements Randomizable {
    private Random random;

    public PseudoRandomize() {
        random = new Random();
    }

    @Override
    public void randomize(CellAutomaton automaton, int quantity, int radius) {
        Cell[][] cells = automaton.getCells();
        for (int i = 0; i < quantity; i++) {
            int x = random.nextInt(automaton.getWidth());
            int y = random.nextInt(automaton.getHeight());
            cells[x][y].setAliveWithRandomColor();
        }
    }

    @Override
    public String toString() {
        return "Losowe";
    }
}
