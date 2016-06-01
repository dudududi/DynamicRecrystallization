package com.dudududi.dynamicrecrystallization.controller;

import com.dudududi.dynamicrecrystallization.model.CellAutomaton;

/**
 * Created by dudek on 5/7/16.
 */
public interface Randomizable {
    void randomize(CellAutomaton automaton, int quantity, int radius);
}
