package org.Vol;

import org.Vol.Category.Category;

public class Vol {
    /**
     * Définira le type de vol
     */
    public enum VolEnum {
        CHARTER,
        SCHEDULED,
        CONVOYAGE,
        VAP,
        RAPATRIMENT,
        ESSAI;
    }

    /**
     * Définira le type de courrier
     */
    public enum CourrierEnum {
        LONG,
        MOYEN,
        COURT;
    }

    /**
     * Définira le type d'appereil
     */
    public enum AppareilEnum {
        HEAVY,
        MEDIUM,
        LIGHT;
    }

    protected String depart;
    protected String arrivee;
    protected Double taux_proc;
    protected Category cat;

    /**
     * Getter depart
     * @return
     */
    public String getDepart() {
        return depart;
    }

    /**
     * Setter depart
     * @param depart
     */
    public void setDepart(String depart) {
        this.depart = depart;
    }

    /**
     * Getter arrivee
     * @return
     */
    public String getArrivee() {
        return arrivee;
    }

    /**
     * Setter arrivee
     * @param arrivee
     */
    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }
    

    /**
     * Getter taux_proc
     * @return
     */
    public Double getTaux_proc() {
        return taux_proc;
    }

    /**
     * Setter taux_proc
     * @param taux_proc
     */
    public void setTaux_proc(Double taux_proc) {
        this.taux_proc = taux_proc;
    }

    public Vol(String depart, String arrivee, Double taux_proc, Category cat) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.taux_proc = taux_proc;
        this.cat = cat;
    }
}
