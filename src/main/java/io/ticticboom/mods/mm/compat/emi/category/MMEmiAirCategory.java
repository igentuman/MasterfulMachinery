package io.ticticboom.mods.mm.compat.emi.category;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.compat.emi.ingredient.AirEmiStack;

import java.util.Set;

public class MMEmiAirCategory extends MMEmiPortCategory {
    
    public static final MMEmiAirCategory INSTANCE = new MMEmiAirCategory();
    
    private MMEmiAirCategory() {
        super("Pneumatic Air", 
              Set.of(Ref.Ports.PNEUMATIC_AIR), 
              new AirEmiStack(1000, 5.0f));
    }
}