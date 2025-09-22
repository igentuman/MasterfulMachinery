package io.ticticboom.mods.mm.port.fluid;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.port.IPortIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidPortIngredient implements IPortIngredient {

    private final ResourceLocation fluidId;
    private final int amount;
    private final Fluid fluid;

    public FluidPortIngredient(ResourceLocation fluidId, int amount) {
        this.fluidId = fluidId;
        this.amount = amount;
        fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if (fluid == null) {
            throw new RuntimeException(String.format("Could not find fluid [%s] which is required by an MM recipe", fluidId));
        }
    }

    @Override
    public boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state) {
        var fluidStorages = storages.getInputStorages(FluidPortStorage.class);
        int remaining = amount;
        for (FluidPortStorage storage : fluidStorages) {
             var drained = storage.getHandler().drain(new FluidStack(fluid, remaining), IFluidHandler.FluidAction.SIMULATE);
             remaining -= drained.getAmount();
        }
        return remaining <= 0;
    }

    @Override
    public void process(Level level, RecipeStorages storages, RecipeStateModel state) {
        var fluidStorages = storages.getInputStorages(FluidPortStorage.class);
        int remaining = amount;
        for (FluidPortStorage storage : fluidStorages) {
            var drained = storage.getHandler().drain(new FluidStack(fluid, remaining), IFluidHandler.FluidAction.EXECUTE);
            remaining -= drained.getAmount();
        }
    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
        var fluidStorages = storages.getOutputStorages(FluidPortStorage.class);
        int remaining = amount;
        for (FluidPortStorage storage : fluidStorages) {
            var filled = storage.getHandler().fill(new FluidStack(fluid, remaining), IFluidHandler.FluidAction.SIMULATE);
            remaining -= filled;
        }
        return remaining <= 0;
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
        var fluidStorages = storages.getOutputStorages(FluidPortStorage.class);
        int remaining = amount;
        for (FluidPortStorage storage : fluidStorages) {
            var filled = storage.getHandler().fill(new FluidStack(fluid, remaining), IFluidHandler.FluidAction.EXECUTE);
            remaining -= filled;
        }
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // Create a fluid stack with amount and tooltip information
        FluidStack fluidStack = new FluidStack(fluid, amount);
        context.addIngredient("FLUID", fluidStack);
        // Note: Tooltip handling will be done by the JEI integration layer
    }

    @Override
    public JsonObject debugInput(Level level, RecipeStorages storages, JsonObject json) {
        var fluidStorages = storages.getInputStorages(FluidPortStorage.class);
        var searchedStorages = new JsonArray();
        var searchIterations = new JsonArray();
        json.addProperty("ingredientType", Ref.Ports.FLUID.toString());
        json.addProperty("amountToDrain", amount);

        int remaining = amount;
        for (FluidPortStorage storage : fluidStorages) {
            var iterJson = new JsonObject();

            var drained = storage.getHandler().drain(new FluidStack(fluid, remaining), IFluidHandler.FluidAction.SIMULATE);
            remaining -= drained.getAmount();

            var drainedRes = JsonOps.INSTANCE.withEncoder(FluidStack.CODEC).apply(drained);

            if (drainedRes.result().isPresent()) {
                iterJson.add("drainedFluidStack", drainedRes.result().get());
            } else {
                iterJson.addProperty("failedToSerializeFluidStack", true);
            }
            iterJson.addProperty("drainedAmount", drained.getAmount());
            iterJson.addProperty("remainingToDrain", remaining);
            iterJson.addProperty("storageUid", storage.getStorageUid().toString());

            searchIterations.add(iterJson);
            searchedStorages.add(storage.getStorageUid().toString());
        }
        json.add("drainIterations", searchIterations);
        json.addProperty("canRun", remaining <= 0);
        json.add("searchedStorages", searchedStorages);
        return json;
    }

    @Override
    public JsonObject debugOutput(Level level, RecipeStorages storages, JsonObject json) {
        var fluidStorages = storages.getOutputStorages(FluidPortStorage.class);
        var searchedStorages = new JsonArray();
        var searchIterations = new JsonArray();
        json.addProperty("ingredientType", Ref.Ports.FLUID.toString());
        json.addProperty("amountToFill", amount);

        int remaining = amount;
        for (FluidPortStorage storage : fluidStorages) {
            var iterJson = new JsonObject();

            var filled = storage.getHandler().fill(new FluidStack(fluid, remaining), IFluidHandler.FluidAction.SIMULATE);
            remaining -= filled;

            iterJson.addProperty("filledAmount", filled);
            iterJson.addProperty("remainingToFill", remaining);
            iterJson.addProperty("storageUid", storage.getStorageUid().toString());

            searchIterations.add(iterJson);
            searchedStorages.add(storage.getStorageUid().toString());
        }
        json.add("fillIterations", searchIterations);
        json.addProperty("canRun", remaining <= 0);
        json.add("searchedStorages", searchedStorages);
        return json;
    }
    
    @Override
    public EmiIngredient getEmiIngredient() {
        return EmiStack.of(fluid, amount);
    }
    
    @Override
    public EmiStack getEmiStack() {
        return EmiStack.of(fluid, amount);
    }
}
