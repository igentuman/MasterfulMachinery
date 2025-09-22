package io.ticticboom.mods.mm.port;

import io.ticticboom.mods.mm.recipe.RecipeModel;

/**
 * Abstraction interface for handling recipe layout in recipe viewers (like JEI).
 * This allows port ingredients to provide recipe display information without
 * directly depending on specific recipe viewer implementations.
 */
public interface IPortRecipeLayoutHandler {
    /**
     * Sets up the recipe layout for this port ingredient.
     * The actual implementation will be provided by the recipe viewer integration.
     * 
     * @param context The recipe layout context containing all necessary information
     */
    void setupRecipeLayout(IRecipeLayoutContext context);
}