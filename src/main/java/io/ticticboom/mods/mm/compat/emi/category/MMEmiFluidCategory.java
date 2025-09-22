package io.ticticboom.mods.mm.compat.emi.category;

import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.Ref;
import net.minecraft.world.level.material.Fluids;

import java.util.Set;

public class MMEmiFluidCategory extends MMEmiPortCategory {
    
    public static final MMEmiFluidCategory INSTANCE = new MMEmiFluidCategory();
    
    private MMEmiFluidCategory() {
        super("Fluid", 
              Set.of(Ref.Ports.FLUID), 
              EmiStack.of(Fluids.WATER, 1000));
    }
}