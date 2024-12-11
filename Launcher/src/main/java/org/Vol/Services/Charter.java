package org.Vol.Services;

import org.Vol.Category.Category;
import org.Vol.Vol;

public class Charter extends Vol {

    public Charter(String depart, String arrivee, Double taux_proc, Category category) {
        super(depart, arrivee, taux_proc, category);
    }

    public String toString(String depart, String arrivee, Double taux_proc) {
        return "Charter " + this.cat.getCategoryName();
    }
}
