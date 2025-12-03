package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Tests de la logique métier de ComparisonService
class ComparisonServiceTest {

    /**
     * FakeCourseService : version simplifiée de CourseService
     * utilisée seulement pour les tests, sans appeler Planifium.
     */
    static class FakeCourseService extends CourseService {

        public FakeCourseService() {
            // On passe null au parent car on ne l'utilise pas dans ce fake
            super(null);
        }

        @Override
        public Optional<Course> getCourseById(String courseId) {
            if ("IFT2255".equalsIgnoreCase(courseId)) {
                Course c = new Course();
                c.setId("IFT2255");
                c.setName("Génie logiciel");
                c.setDescription("Cours de GL");
                c.setCredits(3);
                return Optional.of(c);
            } else if ("IFT2015".equalsIgnoreCase(courseId)) {
                Course c = new Course();
                c.setId("IFT2015");
                c.setName("Structures de données");
                c.setDescription("Cours de SD");
                c.setCredits(3);
                return Optional.of(c);
            }
            // Simule: Planifium ne connaît pas ce cours
            return Optional.empty();
        }
    }

    @Test
    void compareCourses_twoValidCourses_returnsCorrectTotals() {
        // Arrange
        FakeCourseService fakeService = new FakeCourseService();
        ComparisonService comparisonService = new ComparisonService(fakeService);

        List<String> ids = Arrays.asList("IFT2255", "IFT2015");

        // Act
        ComparisonService.ComparisonResult result = comparisonService.compareCourses(ids);

        // Assert
        assertEquals(2, result.courses.size());
        assertEquals(6, result.totalCredits);
        assertEquals(18, result.estimatedWorkload); // 6 crédits * 3h
        assertTrue(result.notFound.isEmpty());
        assertTrue(result.recommendation.contains("6 crédits"));
    }

    @Test
    void compareCourses_withUnknownCourse_ignoresUnknownCourse() {
        // Arrange
        FakeCourseService fakeService = new FakeCourseService();
        ComparisonService comparisonService = new ComparisonService(fakeService);

        List<String> ids = Arrays.asList("IFT2255", "FAKE999");

        // Act
        ComparisonService.ComparisonResult result = comparisonService.compareCourses(ids);

        // Assert
        assertEquals(1, result.courses.size());
        assertEquals(3, result.totalCredits);
        assertEquals(9, result.estimatedWorkload); // 3 crédits * 3h
        assertEquals(1, result.notFound.size());
        assertTrue(result.notFound.contains("FAKE999"));
    }

    @Test
    void compareCourses_emptyList_returnsZero() {
        // Arrange
        FakeCourseService fakeService = new FakeCourseService();
        ComparisonService comparisonService = new ComparisonService(fakeService);

        // Act
        ComparisonService.ComparisonResult result =
                comparisonService.compareCourses(Collections.emptyList());

        // Assert
        assertTrue(result.courses.isEmpty());
        assertEquals(0, result.totalCredits);
        assertEquals(0, result.estimatedWorkload);
        assertTrue(result.notFound.isEmpty());
    }
}
