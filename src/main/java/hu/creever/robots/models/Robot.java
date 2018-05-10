package hu.creever.robots.models;

import hu.creever.robots.exceptions.InsufficientProductException;
import hu.creever.robots.exceptions.MissingProductException;
import hu.creever.robots.exceptions.WrongProductClassNameException;
import hu.creever.robots.helpers.Log;
import hu.creever.robots.models.products.Wheel;
import hu.creever.robots.models.robot.Phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Robot {

    /*
     * Robot's name
     */
    private String name;

    /*
     * Robot's serial number
     */
    private String serialNumber;

    /*
     * Available Product collection for the process
     */
    private HashMap<String, ArrayList<Product>> productStorage = new HashMap<String, ArrayList<Product>>();

    /*
     * Actual process phase of the robot
     */
    private Phase currentPhase = Phase.PHASE_ONE;

    public Robot(String name, String serialNumber) {
        this.name = name;
        this.serialNumber = serialNumber;
    }

    public Phase getCurrentPhase() {
        return this.currentPhase;
    }

    public int getAvailableProduct(String name) {
        if(!this.productStorage.containsKey(name))
            throw new MissingProductException(name, this.serialNumber, this.currentPhase.name());

        return this.productStorage.get(name).size();
    }

    /*
    * Only one output supported
    */
    private Supplier<Product> productSupplier = () -> {

        if(!canBuild()) {
            throw new InsufficientProductException();
        }

        // Remove products from the storage
        this.currentPhase.getInput().forEach((name, quantity) -> this.removeProductFromStorage.accept(name, quantity));

        Map.Entry<String,Integer> entry = this.currentPhase.getOutput().entrySet().iterator().next();

        try {
            Class productClass = Class.forName(Factory.getInstance().getProductFolder() + entry.getKey());
            Product product = (Product) productClass.newInstance();
            product.setName(entry.getKey());
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WrongProductClassNameException(entry.getKey());
        }
    };

    private BiConsumer<String, Integer> removeProductFromStorage = (productName, quantity) -> IntStream.range(0,quantity).forEach(i -> this.productStorage.get(productName).remove(0));

    private boolean canBuild() {
        try {
            return this.currentPhase.getInput()
                    .entrySet()
                    .stream()
                    .filter(e -> this.getAvailableProduct(e.getKey()) >= e.getValue())
                    .map(Map.Entry::getKey).count() != 0;
        }catch(MissingProductException e) {
            return false;
        }
    }

    public Runnable produce = () -> {
        //Log.info("Produce - " + this.toString());
        try {
            Product product = productSupplier.get();
            this.store(product);
            Log.info(product.getName() + " has been successfully Completed");
        } catch (InsufficientProductException e) {
            Log.error("Not enough product to build for " + this.name);
        } catch (WrongProductClassNameException e) {
            Log.error("Wrong product class name " + e.getMessage());
        }
    };

    public void store(Product product) {
        if(this.productStorage.containsKey(product.getName()))
            this.productStorage.get(product.getName()).add(product);
        else {
            ArrayList<Product> list = new ArrayList<>();
            list.add(product);
            this.productStorage.put(product.getName(), list);
        }
    }

    public String toString() {
        return "Name: " + this.name + ", SN: " + this.serialNumber + ", Actual Phase: " + this.currentPhase;
    }
}
