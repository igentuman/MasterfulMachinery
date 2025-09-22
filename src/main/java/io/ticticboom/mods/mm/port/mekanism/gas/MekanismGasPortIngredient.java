package io.ticticboom.mods.mm.port.mekanism.gas;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortIngredient;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MekanismGasPortIngredient extends MekanismChemicalPortIngredient<Gas, GasStack> {

    public MekanismGasPortIngredient(ResourceLocation chemical, long amount) {
        super(chemical, amount);
    }

    @Override
    public GasStack createStack(Gas id, long amount) {
        return new GasStack(id, amount);
    }

    @Override
    public Gas findChemical(ResourceLocation id) {
        return MekanismAPI.gasRegistry().getValue(id);
    }

    @Override
    public Class<? extends MekanismChemicalPortStorage<Gas, GasStack>> getStorageClass() {
        return MekanismGasPortStorage.class;
    }

    @Override
    public ResourceLocation getTypeId() {
        return Ref.Ports.MEK_GAS;
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        GasStack s = createStack(chemical, amount);
        s.setAmount(1000);
        context.addIngredient("MEK_GAS", s);
    }
    
    @Override
    public String getChemicalTypeName() {
        return "Gas";
    }
    
    @Override
    public int getChemicalColor() {
        // Try to get the color from the chemical, fallback to a default gas color
        try {
            return chemical.getTint();
        } catch (Exception e) {
            return 0xFFCCCCCC; // Light gray default for gases
        }
    }
}
