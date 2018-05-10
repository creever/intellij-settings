package hu.creever.robots.controllers;

import hu.creever.robots.helpers.Log;
import hu.creever.robots.models.FactoryConfig;
import hu.creever.robots.models.Product;
import hu.creever.robots.models.Robot;
import hu.creever.robots.models.products.Wheel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RobotController extends BaseController {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);
    private final Random rnd = new Random();

    private int minExecutionTime = 1000; // in miliseconds
    private int maxExecutionTime = 2000; // in miliseconds

    private FactoryConfig config;
    private List<ScheduledFuture> robotThreads = new ArrayList<>();

    private Runnable executionCheck = () -> {
        Log.info("Execution check...");
    };

    public RobotController initiate(FactoryConfig config) {
        this.config = config;
        return this;
    }

    public void start() {
        Log.info("Start production");
        scheduler.scheduleAtFixedRate (this.executionCheck, 0, this.getExecutionCheckPeriod(), TimeUnit.MILLISECONDS);
        this.robotThreads = this.config.getRobots().stream()
                .map((Robot robot) -> scheduler.scheduleAtFixedRate(robot.produce, 0, this.getExecutionCheckPeriod(), TimeUnit.MILLISECONDS))
                .collect(Collectors.toList());
    }

    public void stop() {

    }

    public void abort() {

    }

    private Supplier<Product> generateProduct = () -> {

        // TODO Check and generate product if necessary

        return new Wheel();
    };

    private long getExecutionCheckPeriod() {
        return this.rnd.nextInt(this.maxExecutionTime - this.minExecutionTime) + this.minExecutionTime;
    }
}
