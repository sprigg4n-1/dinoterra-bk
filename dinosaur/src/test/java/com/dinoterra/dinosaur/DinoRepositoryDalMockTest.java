package com.dinoterra.dinosaur;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.dinoterra.dinosaur.dino.Dino;
import com.dinoterra.dinosaur.dino.DinoRepository;
import com.dinoterra.dinosaur.dino.enums.DinoDiet;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;
import com.dinoterra.dinosaur.dino.enums.DinoType;

public class DinoRepositoryDalMockTest {
    @Mock
    private DinoRepository dinoRepository;

    public DinoRepositoryDalMockTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("DinoRepository_FindById_ShouldReturnDino_WhenExists")
    void findById_ShouldReturnDino_WhenExists() {
        System.out.println("=================== Repository ===================");
        // Arrange
        Dino dino = new Dino();
        dino.setId(1L);
        dino.setName("Tyrannosaurus Rex");
        dino.setLatinName("Tyrannosaurus rex");
        dino.setDescription("Large carnivorous dinosaur");
        dino.setTypeOfDino(DinoType.Theropod);
        dino.setLength(12.0);
        dino.setWeight(12000);
        dino.setPeriod(DinoPeriod.Jurassic);
        dino.setPeriodDate("65-55 млн років тому");
        dino.setPeriodDescription("Good period");
        dino.setDiet(DinoDiet.Carnivores);
        dino.setDietDescription("Love meet");

        when(dinoRepository.findById(1L)).thenReturn(Optional.of(dino));

        // Act
        Optional<Dino> found = dinoRepository.findById(1L);

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Tyrannosaurus Rex");
        assertThat(found.get().getLatinName()).isEqualTo("Tyrannosaurus rex");

        // Виведення в консоль
        System.out.println("Found Dino: " + found.get().getName() + " with id " + found.get().getId());
    }

    @Test
    @DisplayName("DinoRepository_FindById_ShouldReturnEmpty_WhenNotExists")
    void findById_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(dinoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Dino> found = dinoRepository.findById(999L);

        // Assert
        assertThat(found).isNotPresent();

        if (found.isEmpty()) {
            System.out.println("Dino not found with id 999. No Dino to display.");
        } else {
            System.out.println("Found Dino: " + found.get());
        }
    }
}
