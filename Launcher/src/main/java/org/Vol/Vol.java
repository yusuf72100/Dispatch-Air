package org.Vol;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Vol implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Définira le type de vol
     */
    public enum VolEnum {
        Charter,
        Scheduled,
        Convoyage,
        VAP,
        Rapatriment,
        Essai;
    }

    public enum CategoryEnum {
        Cargo,
        Airliner;
    }

    /**
     * Définira le type de courrier
     */
    public enum CourrierEnum {
        Long,
        Moyen,
        Court;
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
    protected String callSign;
    protected LocalDate dateDebut;
    protected LocalDate dateLimite;

    protected VolEnum typeVol;
    protected AppareilEnum appareil;
    protected CategoryEnum cat;
    protected CourrierEnum courrier;

    public VolEnum getTypeVol() {
        return typeVol;
    }

    public void setTypeVol(VolEnum typeVol) {
        this.typeVol = typeVol;
    }

    public AppareilEnum getAppareil() {
        return appareil;
    }

    public void setAppareil(AppareilEnum appareil) {
        this.appareil = appareil;
    }

    public CategoryEnum getCat() {
        return cat;
    }

    public void setCat(CategoryEnum cat) {
        this.cat = cat;
    }

    public CourrierEnum getCourrier() {
        return courrier;
    }

    public void setCourrier(CourrierEnum courrier) {
        this.courrier = courrier;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

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

    public Vol(String depart, String arrivee, Double taux_proc, VolEnum typeVol, AppareilEnum appareil, CategoryEnum cat, String callSign, String dateLimite) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.taux_proc = taux_proc;
        this.typeVol = typeVol;
        this.appareil = appareil;
        this.cat = cat;
        this.callSign = callSign;
        this.dateDebut = LocalDate.now();
        this.dateLimite = LocalDate.parse(dateLimite);
    }
}
