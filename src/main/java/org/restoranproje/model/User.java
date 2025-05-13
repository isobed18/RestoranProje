package org.restoranproje.model;



public abstract class User implements Observer {
    protected String name;

    public User(String name) {
        this.name = name;
    }

    public abstract void update(Order order);  // Observer interface'inden gelir

    public String getName() {
        return name;
    }
}

