package io.ticticboom.mods.mm.recipe.input.consume;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import io.ticticboom.mods.mm.port.IPortIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import io.ticticboom.mods.mm.recipe.input.IRecipeIngredientEntry;
import io.ticticboom.mods.mm.util.ChanceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class ConsumeRecipeIngredientEntry implements IRecipeIngredientEntry {

    private final IPortIngredient ingredient;
    private final double chance;
    private final boolean perTick;

    private boolean shouldRun = true;

    public ConsumeRecipeIngredientEntry(IPortIngredient ingredient, double chance, boolean perTick) {
        this.ingredient = ingredient;
        this.chance = chance;
        this.perTick = perTick;
    }

    @Override
    public boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state) {
        shouldRun = ChanceUtils.shouldProceed(chance);
        return ingredient.canProcess(level, storages, state);
    }

    @Override
    public void process(Level level, RecipeStorages storages, RecipeStateModel state) {
        if (!perTick && shouldRun) {
            ingredient.process(level, storages, state);
        }
    }

    @Override
    public void processTick(Level level, RecipeStorages storages, RecipeStateModel state) {
        if (perTick && shouldRun) {
            ingredient.process(level, storages, state);
        }
        ingredient.processTick(level, storages, state);
    }

    @Override
    public void ditchRecipe(Level level, RecipeStorages storages, RecipeStateModel state) {
        ingredient.ditchRecipe(level, storages, state);
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // Set up the ingredient layout
        ingredient.setupRecipeLayout(context);
        
        // Add tooltip information for chance and per-tick consumption
        // This will be handled by the JEI integration layer
        if (chance < 1) {
            var fmtChance = String.format("%.2f", chance * 100) + "% Chance of Consumption";
            context.addTooltip(Component.literal(fmtChance).withStyle(ChatFormatting.DARK_AQUA));
        }
        if (perTick) {
            context.addTooltip(Component.literal("Consumed Per Tick").withStyle(ChatFormatting.DARK_AQUA));
        }
    }

    @Override
    public JsonObject debugExpected(Level level, RecipeStorages storages, RecipeStateModel state, JsonObject json) {
        json.addProperty("chance", chance);
        json.addProperty("perTick", perTick);
        json.add("ingredient", ingredient.debugInput(level, storages, new JsonObject()));
        return json;
    }
    
    @Override
    public EmiIngredient getEmiIngredient() {
        return ingredient.getEmiIngredient();
    }
}
