package io.ticticboom.mods.mm.port.mekanism.slurry;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortIngredient;
import io.ticticboom.mods.mm.port.mekanism.chemical.MekanismChemicalPortStorage;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MekanismSlurryPortIngredient extends MekanismChemicalPortIngredient<Slurry, SlurryStack> {

    public MekanismSlurryPortIngredient(ResourceLocation chemical, long amount) {
        super(chemical, amount);
    }

    @Override
    public SlurryStack createStack(Slurry id, long amount) {
        return new SlurryStack(id, amount);
    }

    @Override
    public Slurry findChemical(ResourceLocation id) {
        return MekanismAPI.slurryRegistry().getValue(id);
    }

    @Override
    public Class<? extends MekanismChemicalPortStorage<Slurry, SlurryStack>> getStorageClass() {
        return MekanismSlurryPortStorage.class;
    }

    @Override
    public ResourceLocation getTypeId() {
        return Ref.Ports.MEK_SLURRY;
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        var s = createStack(chemical, amount);
        s.setAmount(1000);
        context.addIngredient("MEK_SLURRY", s);
    }
    
    @Override
    public String getChemicalTypeName() {
        return "Slurry";
    }
    
    @Override
    public int getChemicalColor() {
        // Try to get the color from the chemical, fallback to a default slurry color
        try {
            return chemical.getTint();
        } catch (Exception e) {
            return 0xFF8B4513; // Saddle brown default for slurries
        }
    }
}
