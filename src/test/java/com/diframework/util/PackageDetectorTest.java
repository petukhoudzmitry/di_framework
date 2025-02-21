package com.diframework.util;

import com.diframework.annotation.DIApplication;
import com.diframework.configuration.ClassScanner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

@DIApplication
public class PackageDetectorTest {

    @Test
    @DisplayName("Given nothing when find packages then return packages")
    public void givenNothing_whenFindPackages_thenReturnPackages() {
        // given
        ClassScanner.findClasses(PackageDetectorTest.class.getPackageName());
        Set<Class<?>> expectedPackages = ClassScanner.getClasses();
        int length = expectedPackages.size();
        // when
        Set<Class<?>> actualPackages = ClassScanner.getClasses();
        // then
        assertNotNull(actualPackages);
        assertEquals(length, actualPackages.size());
        assertIterableEquals(expectedPackages, actualPackages);
    }
}
