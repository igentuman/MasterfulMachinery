package io.ticticboom.mods.mm.port.energy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.compat.emi.ingredient.EnergyEmiStack;
import io.ticticboom.mods.mm.port.IPortIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.world.level.Level;

public class EnergyPortIngredient implements IPortIngredient {

    private final int amount;

    public EnergyPortIngredient(int amount) {

        this.amount = amount;
    }

    @Override
    public boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state) {
        var inputStorages = storages.getInputStorages(EnergyPortStorage.class);
        int remaining = amount;
        for (EnergyPortStorage storage : inputStorages) {
            var extracted = storage.internalExtract(remaining, true);
            remaining -= extracted;
        }
        return remaining <= 0;
    }

    @Override
    public void process(Level level, RecipeStorages storages, RecipeStateModel state) {
        var inputStorages = storages.getInputStorages(EnergyPortStorage.class);
        int remaining = amount;
        for (EnergyPortStorage storage : inputStorages) {
            var extracted = storage.internalExtract(remaining, false);
            remaining -= extracted;
        }
    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputStorages = storages.getOutputStorages(EnergyPortStorage.class);
        int remaining = amount;
        for (EnergyPortStorage storage : outputStorages) {
            var inserted = storage.internalInsert(remaining, true);
            remaining -= inserted;
        }
        return remaining <= 0;
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputStorages = storages.getOutputStorages(EnergyPortStorage.class);
        int remaining = amount;
        for (EnergyPortStorage storage : outputStorages) {
            var inserted = storage.internalInsert(remaining, false);
            remaining -= inserted;
        }
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // Pass the energy amount - the JEI integration will create the EnergyStack
        context.addIngredient("ENERGY", amount);
    }

    @Override
    public JsonObject debugInput(Level level, RecipeStorages storages, JsonObject json) {
        var inputStorages = storages.getInputStorages(EnergyPortStorage.class);
        var searchedStoragesJson = new JsonArray();
        var searchIterationsJson = new JsonArray();
        json.addProperty("ingredientType", Ref.Ports.ENERGY.toString());
        json.addProperty("amountToExtract", amount);

        int remaining = amount;
        for (EnergyPortStorage storage : inputStorages) {
            var iterJson = new JsonObject();

            var extracted = storage.internalExtract(remaining, true);
            remaining -= extracted;

            iterJson.addProperty("extracted", extracted);
            iterJson.addProperty("remainingToExtract", remaining);
            iterJson.addProperty("storageUid", storage.getStorageUid().toString());

            searchIterationsJson.add(iterJson);
            searchedStoragesJson.add(storage.getStorageUid().toString());
        }
        json.add("extractIterations", searchIterationsJson);
        json.addProperty("canRun", remaining <= 0);
        json.add("searchedStorages", searchedStoragesJson);
        return json;
    }

    @Override
    public JsonObject debugOutput(Level level, RecipeStorages storages, JsonObject json) {
        var outputStorages = storages.getOutputStorages(EnergyPortStorage.class);
        var searchedStoragesJson = new JsonArray();
        var searchIterationsJson = new JsonArray();
        json.addProperty("ingredientType", Ref.Ports.ENERGY.toString());
        json.addProperty("amountToInsert", amount);

        int remaining = amount;
        for (EnergyPortStorage storage : outputStorages) {
            var iterJson = new JsonObject();

            var inserted = storage.internalInsert(remaining, true);
            remaining -= inserted;

            iterJson.addProperty("inserted", inserted);
            iterJson.addProperty("remainingToInsert", remaining);

            searchIterationsJson.add(iterJson);
            searchedStoragesJson.add(storage.getStorageUid().toString());
        }
        json.add("insertIterations", searchIterationsJson);
        json.addProperty("canRun", remaining <= 0);
        json.add("searchedStorages", searchedStoragesJson);
        return json;
    }
    
    @Override
    public EmiIngredient getEmiIngredient() {
        return new EnergyEmiStack(amount);
    }
    
    @Override
    public EmiStack getEmiStack() {
        return new EnergyEmiStack(amount);
    }
}
