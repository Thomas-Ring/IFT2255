package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.service.ComparisonService;
import com.diro.ift2255.service.ComparisonService.ComparisonResult;
import com.diro.ift2255.util.ResponseUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CourseController {
    private final CourseService service;
    private final ComparisonService comparisonService;

    public CourseController(CourseService service, ComparisonService comparisonService) { // MODIF
        this.service = service;
        this.comparisonService = comparisonService;
    }

    /**
     * Récupère la liste de tous les cours.
     */
    public void getAllCourses(Context ctx) {
        Map<String, String> queryParams = extractQueryParams(ctx);
        List<Course> courses = service.getAllCourses(queryParams);

        // Si liste vide, ajouter un message informatif
        if (courses.isEmpty() && !queryParams.isEmpty()) {
            ctx.json(Map.of(
                    "courses", courses,
                    "message", "Aucun cours trouvé pour ces critères"
            ));
        } else {
            ctx.json(courses);
        }
    }

    /**
     * Récupère un cours spécifique par son ID.
     */
    public void getCourseById(Context ctx) {
        String id = ctx.pathParam("id");

        if (!validateCourseId(id)) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        Optional<Course> course = service.getCourseById(id);
        if (course.isPresent()) {
            ctx.json(course.get());
        } else {
            ctx.status(404).json(ResponseUtil.formatError("Aucun cours ne correspond à l'ID: " + id));
        }
    }

    /**
     * Compare plusieurs cours et retourne la charge totale
     */
    public void compareCourses(Context ctx) {
        CompareRequest req = ctx.bodyAsClass(CompareRequest.class);

        if (req == null || req.courseIds == null || req.courseIds.isEmpty()) {
            ctx.status(400).json(ResponseUtil.formatError("La liste des cours à comparer est vide ou invalide."));
            return;
        }

        ComparisonResult result = comparisonService.compareCourses(req.courseIds);
        ctx.json(result);
    }

    private boolean validateCourseId(String courseId) {
        return courseId != null && courseId.trim().length() >= 6;
    }

    private Map<String, String> extractQueryParams(Context ctx) {
        Map<String, String> queryParams = new HashMap<>();
        ctx.queryParamMap().forEach((key, values) -> {
            if (!values.isEmpty()) {
                queryParams.put(key, values.get(0));
            }
        });
        return queryParams;
    }

    public static class CompareRequest {
        public List<String> courseIds;
    }
}