package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bussinesLogic.serialization.QuizDataManager_serial;
import bussinesLogic.serialization.Theme;

public class MainThemePanelTest {

    private QuizDataManager_serial dataManager;

    @BeforeEach
    public void setup() {
        // Use instance or mock as needed
        dataManager = QuizDataManager_serial.getInstance();
        // Optionally clear or reset theme storage for isolation
    }
    
    @Test
    public void testAddNewThemeAndListUpdate() {
        // Arrange
        int initialSize = dataManager.getAllThemesTest().size();
        
        Theme newTheme = new Theme();
        int id = dataManager.createNewThemeIdTest();
        System.out.println(id);
        newTheme.setId(id);
        newTheme.setTitle("theme_" + id);
        newTheme.setThemeInfo("Testing theme saving");

        // Act
        dataManager.saveThemeTests(newTheme);
        List<Theme> themesAfterSave = dataManager.getAllThemesTest();

        // Assert
        assertTrue(themesAfterSave.stream().anyMatch(t -> ("theme_"+id).equals(t.getTitle())));
        assertEquals(initialSize + 1, themesAfterSave.size());
    }
}
