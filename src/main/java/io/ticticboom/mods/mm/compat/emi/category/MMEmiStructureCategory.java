package io.ticticboom.mods.mm.compat.emi.category;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.setup.MMRegisters;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MMEmiStructureCategory extends EmiRecipeCategory {
    
    public static final ResourceLocation ID = Ref.id("structure");
    public static final MMEmiStructureCategory INSTANCE = new MMEmiStructureCategory();
    
    public MMEmiStructureCategory() {
        super(ID, EmiStack.of(MMRegisters.BLUEPRINT.get()));
    }
    
    @Override
    public Component getName() {
        return Component.literal("MM Structure");
    }
}