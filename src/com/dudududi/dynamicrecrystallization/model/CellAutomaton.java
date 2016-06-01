package com.dudududi.dynamicrecrystallization.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/21/16.
 */
public class CellAutomaton {
    private Cell[][] cells;
    private int width, height;
    private CellNeighbourhood neighbourhood;

    public CellAutomaton(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public synchronized void init(CellNeighbourhood neighbourhood) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                List<Cell> adjacentCells = new ArrayList<>();
                for (CoordinatePair index : neighbourhood.getCellNeighbourhood()) {
                    int x = i + index.x;
                    int y = j + index.y;
                    if (neighbourhood.isPeriodic()) {
                        x = x >= 0 ? x % width : width + (x % width);
                        y = y >= 0 ? y % height : height + (y % height);
                        adjacentCells.add(cells[x][y]);
                    } else if (x >= 0 && y >= 0 && x < width && y < height) {
                        adjacentCells.add(cells[x][y]);
                    }
                }
                cells[i][j].setNeighbourhood(adjacentCells);
            }
        }
    }

    public synchronized void next(Rule rule) {
        Cell.State[][] nextStep = new Cell.State[width][height];
        if (rule.requiresAdditionalCalculations()) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    rule.calculate(cells[i][j]);
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nextStep[i][j] = rule.determineState(cells[i][j]);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (nextStep[i][j] != null) {
                    cells[i][j].setState(nextStep[i][j]);
                }
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public synchronized void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j].resetState();
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
