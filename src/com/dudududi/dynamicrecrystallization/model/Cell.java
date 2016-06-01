package com.dudududi.dynamicrecrystallization.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by dudek on 4/21/16.
 */
public class Cell {
    private boolean isAlive;
    private List<Cell> neighbourhood;
    private ObjectProperty<Color> color;
    private Random random;
    private double dislocations;
    private boolean isRecrystallized;

    public Cell() {
        random = new Random();
        isAlive = false;
        color = new SimpleObjectProperty<>(Color.WHITE);
        dislocations = 0;
        isRecrystallized = false;
    }

    public Color getColor() {
        return color.getValue();
    }

    public void setColor(Color color) {
        this.color.setValue(color);
    }

    public ObjectProperty<Color> getColorProperty() {
        return color;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setAliveWithRandomColor() {
        color.setValue(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        isAlive = true;
    }

    public double getDislocations() {
        return dislocations;
    }

    public void addDislocations(double dislocations) {
        this.dislocations += dislocations;
    }

    public boolean isRecrystallized() {
        return isRecrystallized;
    }

    public void setRecrystallized() {
        isRecrystallized = true;
        dislocations = 0;
    }

    public List<Cell> getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(List<Cell> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public void setState(State state) {
        if (state.color != null) this.color.setValue(state.color);
        this.isAlive = state.isAlive;
        this.isRecrystallized = state.isRecrystallized;
    }

    public void resetState(){
        isAlive = false;
        isRecrystallized = false;
        color.setValue(Color.WHITE);
        dislocations = 0;
    }

    public static class State {
        public Color color;
        public boolean isAlive;
        public boolean isRecrystallized;

        public static State createAliveWithColor(Color color) {
            State state = new State();
            state.color = color;
            state.isAlive = true;
            return state;
        }

        public static State createAliveWithRandomColor() {
            State state = new State();
            state.color = new Color(ThreadLocalRandom.current().nextDouble(),
                    ThreadLocalRandom.current().nextDouble(),
                    ThreadLocalRandom.current().nextDouble(), 1);
            state.isAlive = true;
            return state;
        }

        public static State createFromCell(Cell cell) {
            State state = new State();
            state.isAlive = cell.isAlive;
            state.isRecrystallized = cell.isRecrystallized;
            state.color = cell.color.getValue();
            return state;
        }
    }

}
