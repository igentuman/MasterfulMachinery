package io.ticticboom.mods.mm.recipe.output.simple;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.port.IPortIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import io.ticticboom.mods.mm.recipe.output.IRecipeOutputEntry;
import io.ticticboom.mods.mm.util.ChanceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class SimpleRecipeOutputEntry implements IRecipeOutputEntry {

    private final IPortIngredient ingredient;
    private final double chance;
    private final boolean perTick;

    private boolean shouldRun = true;

    public SimpleRecipeOutputEntry(IPortIngredient ingredient, double chance, boolean perTick) {

        this.ingredient = ingredient;
        this.chance = chance;
        this.perTick = perTick;
    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
        shouldRun = ChanceUtils.shouldProceed(chance);
        return ingredient.canOutput(level, storages, state);
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
        if (!perTick && shouldRun) {
            ingredient.output(level, storages, state);
        }
    }

    @Override
    public void processTick(Level level, RecipeStorages storages, RecipeStateModel state) {
        if (perTick && shouldRun) {
            ingredient.output(level, storages, state);
        }
        ingredient.outputTick(level, storages, state);
    }

    @Override
    public void ditchRecipe(Level level, RecipeStorages storages, RecipeStateModel state) {
        ingredient.ditchRecipe(level, storages, state);
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // Set up the ingredient layout
        ingredient.setupRecipeLayout(context);
        
        // Add tooltip information for chance and per-tick output
        if (chance < 1) {
            var fmtChance = String.format("%.2f", chance * 100) + "% Chance of Output";
            context.addTooltip(Component.literal(fmtChance).withStyle(ChatFormatting.DARK_AQUA));
        }
        if (perTick) {
            context.addTooltip(Component.literal("Output Per Tick").withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    @Override
    public JsonObject debugExpected(Level level, RecipeStorages storages, RecipeStateModel model, JsonObject json) {
        json.addProperty("chance", chance);
        json.addProperty("perTick", perTick);
        json.add("ingredient", ingredient.debugOutput(level, storages, new JsonObject()));
        return json;
    }
    
    @Override
    public EmiStack getEmiStack() {
        return ingredient.getEmiStack();
    }
}
