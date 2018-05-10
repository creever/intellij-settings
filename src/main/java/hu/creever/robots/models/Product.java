package hu.creever.robots.models;

public abstract class Product {

    protected String name = "";

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
