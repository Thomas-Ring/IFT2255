package com.diro.ift2255.config;

import io.javalin.Javalin;
import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.service.ComparisonService;
import com.diro.ift2255.util.HttpClientApi;

public class Routes {
    public static void register(Javalin app) {
        // Initialisation des services
        HttpClientApi httpClient = new HttpClientApi();
        CourseService courseService = new CourseService(httpClient);
        ComparisonService comparisonService = new ComparisonService(courseService);
        CourseController courseController = new CourseController(courseService, comparisonService);

        // Routes
        app.get("/", ctx -> ctx.result("API de choix de cours - UdeM"));

        // CU09 - Recherche de cours
        app.get("/courses", courseController::getAllCourses);

        // CU10 - Voir les détails d'un cours
        app.get("/courses/{id}", courseController::getCourseById);

        // CU11 - Comparer des cours
        app.post("/courses/compare", courseController::compareCourses);
    }
}