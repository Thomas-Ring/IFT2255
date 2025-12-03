package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;
import java.util.*;

public class ComparisonService {
    private final CourseService courseService;

    public ComparisonService(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * CU11 - Comparer des cours
     * Compare plusieurs cours et calcule la charge totale
     */
    public ComparisonResult compareCourses(List<String> courseIds) {
        ComparisonResult result = new ComparisonResult();
        result.courses = new ArrayList<>();
        result.totalCredits = 0;
        result.notFound = new ArrayList<>();

        // Récupérer chaque cours et calculer les totaux
        for (String courseId : courseIds) {
            Optional<Course> courseOpt = courseService.getCourseById(courseId);

            if (courseOpt.isPresent()) {
                Course course = courseOpt.get();
                result.courses.add(course);
                result.totalCredits += course.getCredits();
            } else {
                result.notFound.add(courseId);
            }
        }


        // Générer une recommandation
        result.recommendation = generateRecommendation(result);

        return result;
    }

    private String generateRecommendation(ComparisonResult result) {
        if (result.totalCredits > 18) {
            return "Charge très élevée (" + result.totalCredits + " crédits). Considérez réduire le nombre de cours.";
        } else if (result.totalCredits > 15) {
            return "Charge élevée (" + result.totalCredits + " crédits). Session intensive mais gérable.";
        } else if (result.totalCredits >= 12) {
            return "Charge normale (" + result.totalCredits + " crédits). Bonne combinaison.";
        } else {
            return "Charge légère (" + result.totalCredits + " crédits). Vous pourriez ajouter un cours.";
        }
    }

    /**
     * Classe pour le résultat de comparaison
     */
    public static class ComparisonResult {
        public List<Course> courses;
        public int totalCredits;
        public int estimatedWorkload; // heures par semaine
        public List<String> notFound; // cours non trouvés
        public String recommendation;

        public ComparisonResult() {
            this.courses = new ArrayList<>();
            this.notFound = new ArrayList<>();
        }
    }
}