package io.ticticboom.mods.mm.port.mekanism.chemical;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.compat.emi.ingredient.ChemicalEmiStack;
import io.ticticboom.mods.mm.port.IPortIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import mekanism.api.Action;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public abstract class MekanismChemicalPortIngredient<CHEMICAL extends Chemical<CHEMICAL>, STACK extends ChemicalStack<CHEMICAL>> implements IPortIngredient {

    protected final ResourceLocation id;
    protected final long amount;

    protected final STACK stack;
    protected final CHEMICAL chemical;

    public abstract STACK createStack(CHEMICAL id, long amount);
    public abstract CHEMICAL findChemical(ResourceLocation id);
    public abstract Class<? extends MekanismChemicalPortStorage<CHEMICAL, STACK>> getStorageClass();
    public abstract ResourceLocation getTypeId();
    public abstract String getChemicalTypeName();
    public abstract int getChemicalColor();

    public MekanismChemicalPortIngredient(ResourceLocation chemical, long amount) {
        this.id = chemical;
        this.amount = amount;
        this.chemical = findChemical(id);
        stack = createStack(this.chemical, amount);
    }

    @Override
    public boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state) {
        var inputStorages = storages.getInputStorages(getStorageClass());
        long remaining = amount;
        for (MekanismChemicalPortStorage<CHEMICAL, STACK> storage : inputStorages) {
            var extracted = storage.extract(remaining, Action.SIMULATE);
            remaining -= extracted.getAmount();
        }
        return remaining <= 0;
    }

    @Override
    public void process(Level level, RecipeStorages storages, RecipeStateModel state) {
        var inputStorages = storages.getInputStorages(getStorageClass());
        long remaining = amount;
        for (MekanismChemicalPortStorage<CHEMICAL, STACK> storage : inputStorages) {
            var extracted = storage.extract(remaining, Action.EXECUTE);
            remaining -= extracted.getAmount();
        }
    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputStorages = storages.getOutputStorages(getStorageClass());
        long remaining = amount;
        for (MekanismChemicalPortStorage<CHEMICAL, STACK> storage : outputStorages) {
            var inserted = storage.insert(createStack(this.chemical, remaining), Action.SIMULATE);
            remaining -= inserted.getAmount();
        }
        return remaining <= 0;
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputStorages = storages.getOutputStorages(getStorageClass());
        long remaining = amount;
        for (MekanismChemicalPortStorage<CHEMICAL, STACK> storage : outputStorages) {
            var inserted = storage.insert(createStack(this.chemical, remaining), Action.EXECUTE);
            remaining -= inserted.getAmount();
        }
    }

    @Override
    public JsonObject debugInput(Level level, RecipeStorages storages, JsonObject json) {
        var inputStorages = storages.getInputStorages(getStorageClass());
        var searchedStoragesJson = new JsonArray();

        json.addProperty("ingredientType", getTypeId().toString());
        json.addProperty("amountToInsert", amount);

        long remaining = amount;
        for (MekanismChemicalPortStorage<CHEMICAL, STACK> storage : inputStorages) {
            var extracted = storage.extract(remaining, Action.SIMULATE);
            remaining -= extracted.getAmount();
            searchedStoragesJson.add(storage.getStorageUid().toString());
        }
        json.addProperty("canRun", remaining <= 0);
        json.add("searchedStorages", searchedStoragesJson);
        return json;
    }

    @Override
    public JsonObject debugOutput(Level level, RecipeStorages storages, JsonObject json) {
        var outputStorages = storages.getOutputStorages(getStorageClass());
        var searchedStoragesJson = new JsonArray();

        json.addProperty("ingredientType", getTypeId().toString());
        json.addProperty("amountToInsert", amount);

        long remaining = amount;
        for (MekanismChemicalPortStorage<CHEMICAL, STACK> storage : outputStorages) {
            var inserted = storage.insert(createStack(this.chemical, remaining), Action.SIMULATE);
            remaining -= inserted.getAmount();
            searchedStoragesJson.add(storage.getStorageUid().toString());
        }
        json.addProperty("canRun", remaining <= 0);
        json.add("searchedStorages", searchedStoragesJson);
        return json;
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // Base implementation - subclasses should override to provide specific chemical type
        // The tooltip will be handled by the JEI integration layer
    }
    
    @Override
    public EmiIngredient getEmiIngredient() {
        return new ChemicalEmiStack(id, amount, getChemicalTypeName(), getChemicalColor());
    }
    
    @Override
    public EmiStack getEmiStack() {
        return new ChemicalEmiStack(id, amount, getChemicalTypeName(), getChemicalColor());
    }
}
