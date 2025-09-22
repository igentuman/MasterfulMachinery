package io.ticticboom.mods.mm.compat.emi.category;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.compat.emi.ingredient.EnergyEmiStack;

import java.util.Set;

public class MMEmiEnergyCategory extends MMEmiPortCategory {
    
    public static final MMEmiEnergyCategory INSTANCE = new MMEmiEnergyCategory();
    
    private MMEmiEnergyCategory() {
        super("Energy", 
              Set.of(Ref.Ports.ENERGY), 
              new EnergyEmiStack(1000));
    }
}