package io.ticticboom.mods.mm.port.kinetic;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.compat.emi.ingredient.KineticEmiStack;
import io.ticticboom.mods.mm.port.IPortIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.world.level.Level;

public class CreateKineticPortIngredient implements IPortIngredient {

    private final float speed;

    public CreateKineticPortIngredient(float speed) {

        this.speed = speed;
    }

    @Override
    public boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state) {
        var inputs = storages.getInputStorages(CreateKineticPortStorage.class);
        for (CreateKineticPortStorage input : inputs) {
            if (Math.abs(input.getSpeed()) >= Math.abs(speed)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void process(Level level, RecipeStorages storages, RecipeStateModel state) {

    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputs = storages.getOutputStorages(CreateKineticPortStorage.class);
        return !outputs.isEmpty();
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputs = storages.getOutputStorages(CreateKineticPortStorage.class);
        for (CreateKineticPortStorage output : outputs) {
            output.updateSpeed(speed);
            output.stopNext();
        }
    }

    @Override
    public void outputTick(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputs = storages.getOutputStorages(CreateKineticPortStorage.class);
        for (CreateKineticPortStorage output : outputs) {
            output.updateSpeed(speed);
        }
    }

    @Override
    public void ditchRecipe(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputs = storages.getOutputStorages(CreateKineticPortStorage.class);
        for (CreateKineticPortStorage output : outputs) {
            output.updateSpeed(0);
        }
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // Kinetic ingredients don't display anything in JEI - they're just requirements
    }

    @Override
    public JsonObject debugInput(Level level, RecipeStorages storages, JsonObject json) {
        return null;
    }

    @Override
    public JsonObject debugOutput(Level level, RecipeStorages storages, JsonObject json) {
        return null;
    }
    
    @Override
    public EmiIngredient getEmiIngredient() {
        return new KineticEmiStack(speed);
    }
    
    @Override
    public EmiStack getEmiStack() {
        return new KineticEmiStack(speed);
    }
}
