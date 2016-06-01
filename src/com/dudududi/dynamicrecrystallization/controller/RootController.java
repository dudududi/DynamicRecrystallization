package com.dudududi.dynamicrecrystallization.controller;

import com.dudududi.dynamicrecrystallization.controller.randomizable.PseudoRandomize;
import com.dudududi.dynamicrecrystallization.controller.randomizable.RadiusRandomize;
import com.dudududi.dynamicrecrystallization.controller.randomizable.StaticRandomize;
import com.dudududi.dynamicrecrystallization.model.Cell;
import com.dudududi.dynamicrecrystallization.model.CellAutomaton;
import com.dudududi.dynamicrecrystallization.model.CellNeighbourhood;
import com.dudududi.dynamicrecrystallization.model.Rule;
import com.dudududi.dynamicrecrystallization.model.neighbourhoods.*;
import com.dudududi.dynamicrecrystallization.model.rules.DynamicRecrystallizationRule;
import com.dudududi.dynamicrecrystallization.model.rules.GrainExpansionRule;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RootController {
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_EMBRYOS_NUMBER = 25;
    private static final int DEFAULT_RADIUS = 50;
    private static final int MAX_THREAD_NUM = 8;

    private List<Node> disableNodes;
    private WritableImage image;

    @FXML
    private ImageView board;
    @FXML
    private ToggleButton startButton, recrystallizationButton;
    @FXML
    private TextField widthField, heightField, embryosField, radiusField, neighbourhoodSizeField;
    @FXML
    private Button clearButton, generateButton, randomizeButton;
    @FXML
    private ComboBox<CellNeighbourhood> selectNeighbourhood;
    @FXML
    private ComboBox<Randomizable> selectRandomizeMethod;
    @FXML
    private CheckBox periodicBC;
    @FXML
    private Label radiusLabel, neighbourhoodSizeLabel;

    private Timeline animation;
    private Rule automatonRule;
    private CellNeighbourhood neighbourhood;
    private CellAutomaton cellAutomaton;
    private Executor executor;


    @FXML
    private void initialize() {
        executor = Executors.newFixedThreadPool(MAX_THREAD_NUM, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        collectNodes();
        neighbourhood = new MooreNeighbourhood();
        generateAutomaton(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        widthField.setText(Integer.toString(DEFAULT_WIDTH));
        heightField.setText(Integer.toString(DEFAULT_HEIGHT));
        embryosField.setText(Integer.toString(DEFAULT_EMBRYOS_NUMBER));
        radiusField.setText(Integer.toString(DEFAULT_RADIUS));

        generateButton.setOnMouseClicked(event -> executor.execute(() -> {
            cellAutomaton.clear();
            int width = Integer.valueOf(widthField.getText());
            int height = Integer.valueOf(heightField.getText());
            generateAutomaton(width, height);
        }));

        periodicBC.setSelected(true);
        periodicBC.selectedProperty().addListener((observable, oldValue, newValue) -> {
            neighbourhood.setPeriodic(newValue);
            executor.execute(() -> cellAutomaton.init(neighbourhood));
        });

        clearButton.setOnMouseClicked(event -> executor.execute(() -> cellAutomaton.clear()));

        configureAnimation();
        generateNeighbourhoods();
        generateRandomizeMethods();
    }

    private void generateAutomaton(int width, int height) {
        image = new WritableImage(width, height);
        cellAutomaton = new CellAutomaton(width, height);
        Cell[][] cells = cellAutomaton.getCells();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = cells[i][j];
                int m = i, n = j;
                cell.getColorProperty().addListener((observable, oldValue, newValue) -> {
                    image.getPixelWriter().setColor(m, n, cell.getColor());
                });
                image.getPixelWriter().setColor(i, j, Color.WHITE);
            }
        }
        board.setImage(image);
        board.onMouseClickedProperty().setValue(event -> {
            int x = (int) event.getX(), y = (int) event.getY();
            Cell cell = cells[x][y];
            if (cell.isAlive()) {
                cell.setAlive(false);
            } else {
                cell.setAliveWithRandomColor();
            }
        });
        cellAutomaton.init(neighbourhood);
    }

    private void generateNeighbourhoods() {
        neighbourhoodSizeLabel.setVisible(false);
        neighbourhoodSizeField.setVisible(false);
        neighbourhoodSizeField.setText("10");

        RandomNeighbourhood randomNeighbourhood = new RandomNeighbourhood(periodicBC.selectedProperty().getValue(),
                Integer.valueOf(neighbourhoodSizeField.getText()));
        neighbourhoodSizeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*") && !newValue.equals("")) {
                randomNeighbourhood.setRadius(Integer.valueOf(newValue));
            }
        });

        ObservableList<CellNeighbourhood> neighbourhoods = FXCollections.observableArrayList(
                new MooreNeighbourhood(periodicBC.selectedProperty().getValue()),
                new VonNeumannNeighbourhood(periodicBC.selectedProperty().getValue()),
                new PentagonalNeighbourhood(periodicBC.selectedProperty().getValue()),
                new HexagonalNeighbourhood(periodicBC.selectedProperty().getValue(), HexagonalNeighbourhood.LEFT),
                new HexagonalNeighbourhood(periodicBC.selectedProperty().getValue(), HexagonalNeighbourhood.RIGHT),
                randomNeighbourhood);
        selectNeighbourhood.getItems().addAll(neighbourhoods);
        selectNeighbourhood.getSelectionModel().selectFirst();
        selectNeighbourhood.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue instanceof RandomNeighbourhood) {
                neighbourhoodSizeLabel.setVisible(true);
                neighbourhoodSizeField.setVisible(true);
            } else {
                neighbourhoodSizeLabel.setVisible(false);
                neighbourhoodSizeField.setVisible(false);
            }
            neighbourhood = newValue;
            neighbourhood.setPeriodic(periodicBC.isSelected());
            executor.execute(() -> cellAutomaton.init(neighbourhood));
        }));
    }

    private void generateRandomizeMethods() {
        radiusLabel.setVisible(false);
        radiusField.setVisible(false);

        ObservableList<Randomizable> randomizeMethods = FXCollections.observableArrayList(
                new PseudoRandomize(),
                new RadiusRandomize(),
                new StaticRandomize()
        );
        selectRandomizeMethod.getItems().addAll(randomizeMethods);
        selectRandomizeMethod.getSelectionModel().selectFirst();
        selectRandomizeMethod.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof RadiusRandomize) {
                radiusLabel.setVisible(true);
                radiusField.setVisible(true);
            } else {
                radiusLabel.setVisible(false);
                radiusField.setVisible(false);
            }
        });
        randomizeButton.setOnMouseClicked(event ->
                selectRandomizeMethod.getSelectionModel().getSelectedItem()
                        .randomize(cellAutomaton, Integer.parseInt(embryosField.getText()), Integer.parseInt(radiusField.getText())));
    }

    private void collectNodes() {
        disableNodes = new ArrayList<>();
        disableNodes.add(widthField);
        disableNodes.add(heightField);
        disableNodes.add(generateButton);
        disableNodes.add(selectNeighbourhood);
        disableNodes.add(periodicBC);
        disableNodes.add(selectRandomizeMethod);
        disableNodes.add(embryosField);
        disableNodes.add(radiusField);
        disableNodes.add(randomizeButton);
        disableNodes.add(clearButton);
    }

    private void configureAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(90), event -> executor.execute(() -> cellAutomaton.next(automatonRule))));
        animation.setCycleCount(Timeline.INDEFINITE);

        startButton.setToggleGroup(new ToggleGroup());
        startButton.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                animation.stop();
                disableNodes.forEach(node -> node.setDisable(false));
                startButton.setText("START");
                recrystallizationButton.setDisable(false);
            } else {
                automatonRule = new GrainExpansionRule();
                animation.play();
                disableNodes.forEach(node -> node.setDisable(true));
                startButton.setText("STOP");
                recrystallizationButton.setDisable(true);
            }
        });

        recrystallizationButton.setToggleGroup(new ToggleGroup());
        recrystallizationButton.getToggleGroup().selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == null) {
                animation.stop();
                disableNodes.forEach(node -> node.setDisable(false));
                startButton.setDisable(false);
            } else {
                automatonRule = new DynamicRecrystallizationRule(cellAutomaton.getWidth(), cellAutomaton.getHeight());
                animation.play();
                disableNodes.forEach(node -> node.setDisable(true));
                startButton.setDisable(true);
            }
        }));
    }
}
