package com.diframework.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class PackageDetectorTest {

    @Test
    @DisplayName("Given nothing when find packages then return packages")
    public void givenNothing_whenFindPackages_thenReturnPackages() {
        // given
        Set<Class<?>> expectedPackages = ClassScanner.findClasses();
        int length = expectedPackages.size();
        // when
        Set<Class<?>> actualPackages = ClassScanner.findClasses();
        // then
        assertNotNull(actualPackages);
        assertEquals(length, actualPackages.size());
        assertIterableEquals(expectedPackages, actualPackages);
    }
}
