package io.ticticboom.mods.mm.recipe.output;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.world.level.Level;

public interface IRecipeOutputEntry {
    boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state);
    void output(Level level, RecipeStorages storages, RecipeStateModel state);
    default void processTick(Level level, RecipeStorages storages, RecipeStateModel state) {}
    void ditchRecipe(Level level, RecipeStorages storages, RecipeStateModel state);
    void setupRecipeLayout(IRecipeLayoutContext context);

    JsonObject debugExpected(Level level, RecipeStorages storages, RecipeStateModel model, JsonObject jsonObject);
    
    // EMI support
    default EmiStack getEmiStack() {
        return null;
    }
}
