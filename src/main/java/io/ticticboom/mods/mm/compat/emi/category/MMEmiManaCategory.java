package io.ticticboom.mods.mm.compat.emi.category;

import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.compat.emi.ingredient.ManaEmiStack;

import java.util.Set;

public class MMEmiManaCategory extends MMEmiPortCategory {
    
    public static final MMEmiManaCategory INSTANCE = new MMEmiManaCategory();
    
    private MMEmiManaCategory() {
        super("Mana", 
              Set.of(Ref.Ports.BOTANIA_MANA), 
              new ManaEmiStack(1000));
    }
}