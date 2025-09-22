package com.kaiho.gastromanager.infrastructure.submenu.output.jpa.adapter;

import com.kaiho.gastromanager.domain.submenu.domain.Submenu;
import com.kaiho.gastromanager.infrastructure.config.context.RestaurantContext;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.entity.SubmenuEntity;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.mapper.SubmenuEntityMapper;
import com.kaiho.gastromanager.infrastructure.submenu.output.jpa.repository.SubmenuEntityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmenuEntityAdapterTest {

    @Mock
    private SubmenuEntityRepository submenuEntityRepository;

    @Mock
    private SubmenuEntityMapper submenuEntityMapper;

    @InjectMocks
    private SubmenuEntityAdapter submenuEntityAdapter;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void submenuExistsByName_ShouldReturnTrue_WhenSubmenuExists() {
        // Arrange
        String name = "Postres";
        UUID restaurantId = UUID.randomUUID();
        try (MockedStatic<RestaurantContext> restaurantContext = mockStatic(RestaurantContext.class)) {
            restaurantContext.when(RestaurantContext::getCurrentRestaurant).thenReturn(restaurantId);
            when(submenuEntityRepository.existsByName(name, restaurantId)).thenReturn(true);

            // Act
            boolean result = submenuEntityAdapter.submenuExistsByName(name);

            // Assert
            assertTrue(result);
            verify(submenuEntityRepository).existsByName(name, restaurantId);
        }
    }

    @Test
    void submenuExistsByName_ShouldReturnFalse_WhenSubmenuDoesNotExist() {
        // Arrange
        String name = "MenuInexistente";
        UUID restaurantId = UUID.randomUUID();
        try (MockedStatic<RestaurantContext> restaurantContext = mockStatic(RestaurantContext.class)) {
            restaurantContext.when(RestaurantContext::getCurrentRestaurant).thenReturn(restaurantId);
            when(submenuEntityRepository.existsByName(name, restaurantId)).thenReturn(false);

            // Act
            boolean result = submenuEntityAdapter.submenuExistsByName(name);

            // Assert
            assertFalse(result);
            verify(submenuEntityRepository).existsByName(name, restaurantId);
        }
    }

    @Test
    void createSubmenu_ShouldReturnUUID_WhenSubmenuIsCreated() {
        // Arrange
        UUID expectedUuid = UUID.randomUUID();
        Submenu submenu = Submenu.builder()
                                 .name("Postres")
                                 .description("Menú de postres")
                                 .isEnabled(true)
                                 .build();
        SubmenuEntity entity = SubmenuEntity.builder()
                                            .uuid(expectedUuid)
                                            .name("Postres")
                                            .description("Menú de postres")
                                            .isEnabled(true)
                                            .build();

        when(submenuEntityMapper.toEntity(submenu)).thenReturn(entity);
        when(submenuEntityRepository.save(entity)).thenReturn(entity);

        // Act
        UUID result = submenuEntityAdapter.createSubmenu(submenu);

        // Assert
        assertEquals(expectedUuid, result);
        verify(submenuEntityMapper).toEntity(submenu);
        verify(submenuEntityRepository).save(entity);
    }
}
