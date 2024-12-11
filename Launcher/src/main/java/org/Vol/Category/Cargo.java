package org.Vol.Category;

public class Cargo extends Category {
    private Double MTOW;
    private Double MLW;
    private Double CG;


    public Cargo(Double MTOW, Double MLW, Double CG) {

    }

    @Override
    public String getCategoryName() {
        return "Cargo";
    }
}