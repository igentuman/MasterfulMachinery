package io.ticticboom.mods.mm.port.pneumaticcraft.air;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.compat.emi.ingredient.AirEmiStack;
import io.ticticboom.mods.mm.port.IPortIngredient;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.world.level.Level;

public class PneumaticAirPortIngredient implements IPortIngredient {
    private final float bar;
    private final int air;
    public PneumaticAirPortIngredient(float bar, int air) {
        this.bar = bar;
        this.air = air;
    }
    @Override
    public boolean canProcess(Level level, RecipeStorages storages, RecipeStateModel state) {
        var inputStorages = storages.getInputStorages(PneumaticAirPortStorage.class);
        for (PneumaticAirPortStorage storage : inputStorages) {
            if (storage.getPressure() < bar) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void process(Level level, RecipeStorages storages, RecipeStateModel state) {
        var inputStorages = storages.getInputStorages(PneumaticAirPortStorage.class);
        for (PneumaticAirPortStorage storage : inputStorages) {
            storage.addAir(-(int) air);
        }
    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
//        var outputStorages = storages.getOutputStorages(PneumaticAirPortStorage.class);
//        for (PneumaticAirPortStorage storage : outputStorages) {
//            if (storage.getPressure() <= bar) {
//                return true;
//            }
//        }
        return true;
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
        var outputStorages = storages.getOutputStorages(PneumaticAirPortStorage.class);
        for (PneumaticAirPortStorage storage : outputStorages) {
            storage.addAir(air);
        }
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // Pass both air amount and pressure - the JEI integration will create the PneumaticAirStack
        context.addIngredient("PNEUMATIC_AIR", new Object[]{air, bar});
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
        return new AirEmiStack(air, bar);
    }
    
    @Override
    public EmiStack getEmiStack() {
        return new AirEmiStack(air, bar);
    }
}
