package io.ticticboom.mods.mm.compat.emi.category;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.compat.emi.ingredient.KineticEmiStack;

import java.util.Set;

public class MMEmiKineticCategory extends MMEmiPortCategory {
    
    public static final MMEmiKineticCategory INSTANCE = new MMEmiKineticCategory();
    
    private MMEmiKineticCategory() {
        super("Kinetic", 
              Set.of(Ref.Ports.CREATE_KINETIC), 
              new KineticEmiStack(256.0f));
    }
}