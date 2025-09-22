package io.ticticboom.mods.mm.compat.emi.category;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.compat.emi.ingredient.ChemicalEmiStack;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class MMEmiChemicalCategory extends MMEmiPortCategory {
    
    public static final MMEmiChemicalCategory INSTANCE = new MMEmiChemicalCategory();
    
    private MMEmiChemicalCategory() {
        super("Chemical", 
              Set.of(Ref.Ports.MEK_GAS, Ref.Ports.MEK_INFUSE, Ref.Ports.MEK_SLURRY, Ref.Ports.MEK_PIGMENT), 
              new ChemicalEmiStack(new ResourceLocation("mekanism", "hydrogen"), 1000, "Gas", 0xFFCCCCCC));
    }
}