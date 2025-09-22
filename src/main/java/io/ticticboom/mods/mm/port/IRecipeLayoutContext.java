package io.ticticboom.mods.mm.port;

import io.ticticboom.mods.mm.recipe.RecipeModel;
import net.minecraft.network.chat.Component;

/**
 * Context interface that provides all necessary information for setting up recipe layouts.
 * This abstraction allows different recipe viewer implementations (like JEI) to provide
 * their specific context while keeping the port ingredients independent.
 */
public interface IRecipeLayoutContext {
    /**
     * Gets the recipe model being displayed
     */
    RecipeModel getRecipeModel();
    
    /**
     * Adds an ingredient to the recipe layout.
     * The implementation will handle the specific recipe viewer's requirements.
     * 
     * @param ingredientType The type of ingredient (implementation-specific)
     * @param ingredient The ingredient object to display
     */
    void addIngredient(Object ingredientType, Object ingredient);
    
    /**
     * Gets the next available slot position for layout
     * @return An object representing the slot position (implementation-specific)
     */
    Object getNextSlotPosition();
    
    /**
     * Adds a tooltip to the current ingredient slot
     * @param tooltip The tooltip component to add
     */
    void addTooltip(Component tooltip);
}