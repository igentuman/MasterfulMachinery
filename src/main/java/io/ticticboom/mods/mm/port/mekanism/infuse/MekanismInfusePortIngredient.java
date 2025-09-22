package io.ticticboom.mods.mm.port.mekanism.infuse;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortIngredient;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import net.minecraft.resources.ResourceLocation;

public class MekanismInfusePortIngredient extends MekanismChemicalPortIngredient<InfuseType, InfusionStack> {

    public MekanismInfusePortIngredient(ResourceLocation chemical, long amount) {
        super(chemical, amount);
    }

    @Override
    public InfusionStack createStack(InfuseType id, long amount) {
        return new InfusionStack(id, amount);
    }

    @Override
    public InfuseType findChemical(ResourceLocation id) {
        return MekanismAPI.infuseTypeRegistry().getValue(id);
    }

    @Override
    public Class<? extends MekanismChemicalPortStorage<InfuseType, InfusionStack>> getStorageClass() {
        return MekanismInfusePortStorage.class;
    }

    @Override
    public ResourceLocation getTypeId() {
        return Ref.Ports.MEK_INFUSE;
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        var s = createStack(chemical, amount);
        s.setAmount(1000);
        context.addIngredient("MEK_INFUSION", s);
    }
    
    @Override
    public String getChemicalTypeName() {
        return "Infusion";
    }
    
    @Override
    public int getChemicalColor() {
        // Try to get the color from the chemical, fallback to a default infusion color
        try {
            return chemical.getTint();
        } catch (Exception e) {
            return 0xFF8A2BE2; // Blue violet default for infusions
        }
    }
}
