package io.ticticboom.mods.mm.port.mekanism.pigment;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortIngredient;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import net.minecraft.resources.ResourceLocation;

public class MekanismPigmentPortIngredient extends MekanismChemicalPortIngredient<Pigment, PigmentStack> {

    public MekanismPigmentPortIngredient(ResourceLocation chemical, long amount) {
        super(chemical, amount);
    }

    @Override
    public PigmentStack createStack(Pigment id, long amount) {
        return new PigmentStack(id, amount);
    }

    @Override
    public Pigment findChemical(ResourceLocation id) {
        return MekanismAPI.pigmentRegistry().getValue(id);
    }

    @Override
    public Class<? extends MekanismChemicalPortStorage<Pigment, PigmentStack>> getStorageClass() {
        return MekanismPigmentPortStorage.class;
    }

    @Override
    public ResourceLocation getTypeId() {
        return Ref.Ports.MEK_PIGMENT;
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        var s = createStack(chemical, amount);
        s.setAmount(1000);
        context.addIngredient("MEK_PIGMENT", s);
    }
    
    @Override
    public String getChemicalTypeName() {
        return "Pigment";
    }
    
    @Override
    public int getChemicalColor() {
        // Try to get the color from the chemical, fallback to a default pigment color
        try {
            return chemical.getTint();
        } catch (Exception e) {
            return 0xFFFF69B4; // Hot pink default for pigments
        }
    }
}
