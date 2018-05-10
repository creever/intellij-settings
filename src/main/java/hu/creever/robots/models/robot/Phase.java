package hu.creever.robots.models.robot;

import hu.creever.robots.models.Product;

import java.util.ArrayList;
import java.util.HashMap;

public enum Phase {

    PHASE_ONE(1000, 2000),
    PHASE_TWO(2000, 3000),
    PHASE_THREE(3000, 4000);

    private final int minExecutionTime; // in miliseconds
    private final int maxExecutionTime; // in miliseconds

    private String description = "";

    private HashMap<String, Integer> input = new HashMap<>();
    private HashMap<String, Integer> output = new HashMap<>();

    Phase(int minExecutionTime, int maxExecutionTime) {
        this.minExecutionTime = minExecutionTime;
        this.maxExecutionTime = maxExecutionTime;
    }

    public void setInput(HashMap<String, Integer> input) {
        this.input = input;
    }

    public void setOutput(HashMap<String, Integer> output) {
        this.output = output;
    }

    public HashMap<String, Integer> getInput() {
        return this.input;
    }

    public HashMap<String, Integer> getOutput() {
        return this.output;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
