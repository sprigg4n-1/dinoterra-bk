package com.dinoterra.dinosaur;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dinoterra.dinosaur.dino.Dino;
import com.dinoterra.dinosaur.dino.DinoService;
import com.dinoterra.dinosaur.image.ImageRepository;
import com.dinoterra.dinosaur.location.FoundLocationRepository;
import com.dinoterra.dinosaur.dino.DinoRepository;
import com.dinoterra.dinosaur.dino.DinoResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class DinoServiceBllMockTest {
    @Mock
    private DinoRepository dinoRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private FoundLocationRepository foundLocationRepository;

    @InjectMocks
    private DinoService dinoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("DinoService_GetDinoById_ShouldReturnDino_WhenExists")
    void getDinoById_ShouldReturnDino_WhenExists() {
        System.out.println("=================== Service ===================");
        // Arrange
        Dino dino = new Dino();
        dino.setId(1L);
        dino.setName("Tyrannosaurus Rex");
        dino.setLatinName("Tyrannosaurus rex");
        dino.setDescription("Large carnivorous dinosaur");

        when(dinoRepository.findById(1L)).thenReturn(Optional.of(dino));
        when(imageRepository.findByDino(any(Dino.class))).thenReturn(new ArrayList<>());
        when(foundLocationRepository.findByDino(any(Dino.class))).thenReturn(new ArrayList<>());

        // Act
        DinoResponse foundDino = dinoService.getDino(1L); // Виклик методу сервісу

        // Assert
        assertThat(foundDino).isNotNull(); // Перевірка, що динозавр знайдений
        assertThat(foundDino.name()).isEqualTo("Tyrannosaurus Rex");

        // Виведення в консоль
        System.out.println("Found Dino in service: " + foundDino.name());
    }

    @Test
    @DisplayName("DinoService_GetDinoById_ShouldReturnEmpty_WhenNotExists")
    void getDinoById_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(dinoRepository.findById(999L)).thenReturn(Optional.empty()); // Mock repository to return empty for the
                                                                          // given ID

        // Act & Assert
        RuntimeException thrown = assertThrows(
                RuntimeException.class, // Expecting a RuntimeException
                () -> dinoService.getDino(999L) // Call the service method that should throw
        );

        // Verify the exception message
        assertThat(thrown.getMessage()).isEqualTo("Dino not found with ID 999");

        // Output for debugging
        System.out.println("Expected exception thrown: " + thrown.getMessage());
    }
}