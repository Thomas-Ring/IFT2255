package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    private String id;          // Code du cours (ex: IFT2255)
    private String name;        // Nom du cours
    private String description;
    private int credits;

    // Nouveaux attributs pour l'énoncé
    private List<String> prerequisites;  // Prérequis
    private List<String> corequisites;   // Co-requis
    private String cycle;                // 1er cycle, 2e cycle, etc.
    private String schedule;             // Horaire

    // Résultats académiques
    private double moyenne;
    private int inscrits;
    private int echecs;

    // Pour la charge de travail (comparaison)
    private int workload;  // Heures par semaine estimées

    // Constructeurs
    public Course() {}

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(String id, String name, String desc, int credits) {
        this(id, name);
        this.description = desc;
        this.credits = credits;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public List<String> getPrerequisites() { return prerequisites; }
    public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }

    public List<String> getCorequisites() { return corequisites; }
    public void setCorequisites(List<String> corequisites) { this.corequisites = corequisites; }

    public String getCycle() { return cycle; }
    public void setCycle(String cycle) { this.cycle = cycle; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public double getMoyenne() { return moyenne; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }

    public int getInscrits() { return inscrits; }
    public void setInscrits(int inscrits) { this.inscrits = inscrits; }

    public int getEchecs() { return echecs; }
    public void setEchecs(int echecs) { this.echecs = echecs; }

    public int getWorkload() { return workload; }
    public void setWorkload(int workload) { this.workload = workload; }
}