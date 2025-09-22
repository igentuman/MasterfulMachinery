package io.ticticboom.mods.mm.recipe.input;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.world.level.Level;

public interface IRecipeIngredientEntry {
    boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state);
    void process(Level level, RecipeStorages storages, RecipeStateModel state);
    default void processTick(Level level, RecipeStorages storages, RecipeStateModel state) {}
    void ditchRecipe(Level level, RecipeStorages storages, RecipeStateModel state);
    void setupRecipeLayout(IRecipeLayoutContext context);
    JsonObject debugExpected(Level level, RecipeStorages storages, RecipeStateModel state, JsonObject json);
    
    // EMI support
    default EmiIngredient getEmiIngredient() {
        return null;
    }
}
