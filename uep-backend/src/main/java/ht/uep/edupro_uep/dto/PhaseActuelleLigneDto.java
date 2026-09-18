package ht.uep.edupro_uep.dto;

/** § 30 de la FIOP : une phase cochée par l'opérateur (multi-choix), avec sa période. */
public class PhaseActuelleLigneDto {

    private String phase;
    private String periode;

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }
}
