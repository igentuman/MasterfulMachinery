package io.ticticboom.mods.mm.port;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.world.level.Level;

public interface IPortIngredient {
    boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state);
    void process(Level level, RecipeStorages storages, RecipeStateModel state);
    default void processTick(Level level, RecipeStorages storages, RecipeStateModel state) {}
    boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state);
    void output(Level level, RecipeStorages storages, RecipeStateModel state);
    default void outputTick(Level level, RecipeStorages storages, RecipeStateModel state) {}
    
    /**
     * Sets up the recipe layout for recipe viewers.
     * This method is called by recipe viewer integrations to display this ingredient.
     * 
     * @param context The recipe layout context containing all necessary information
     */
    default void setupRecipeLayout(IRecipeLayoutContext context) {
        // Default implementation does nothing - ingredients can override if they support recipe display
    }
    
    default void ditchRecipe(Level level, RecipeStorages storages, RecipeStateModel state) {
    }

    JsonObject debugInput(Level level, RecipeStorages storages, JsonObject json);
    JsonObject debugOutput(Level level, RecipeStorages storages, JsonObject json);

    // EMI support
    default EmiIngredient getEmiIngredient() {
        return null;
    }
    
    default EmiStack getEmiStack() {
        return null;
    }
}
