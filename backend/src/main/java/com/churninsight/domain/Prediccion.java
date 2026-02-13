package com.churninsight.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Prediccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String prevision;
    private double probabilidad;
    private String riesgo;
    private LocalDateTime fecha = LocalDateTime.now();

    protected Prediccion() {}

    public Prediccion(String prevision, double probabilidad, String riesgo) {
        this.prevision = prevision;
        this.probabilidad = probabilidad;
        this.riesgo = riesgo;
    }

    public Long getId() { return id; }
    public String getPrevision() { return prevision; }
    public double getProbabilidad() { return probabilidad; }
    public String getRiesgo() { return riesgo; }
}
