package io.ticticboom.mods.mm.compat.emi.category;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.recipe.RecipeModel;
import io.ticticboom.mods.mm.setup.MMRegisters;
import io.ticticboom.mods.mm.structure.StructureModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MMEmiRecipeCategory extends EmiRecipeCategory {
    
    public static final ResourceLocation ID = Ref.id("recipes");
    public static final MMEmiRecipeCategory INSTANCE = new MMEmiRecipeCategory();
    
    private final StructureModel structureModel;
    
    public MMEmiRecipeCategory() {
        this(null);
    }
    
    public MMEmiRecipeCategory(StructureModel structureModel) {
        super(structureModel != null ? 
            Ref.id(structureModel.id().getPath() + "_recipe") : ID, 
            EmiStack.of(MMRegisters.BLUEPRINT.get()));
        this.structureModel = structureModel;
    }
    
    @Override
    public Component getName() {
        if (structureModel != null) {
            return Component.literal(this.structureModel.name()).append(Component.literal(" (Recipes)"));
        } else {
            return Component.literal("MM Recipes");
        }
    }
    
    public StructureModel getStructureModel() {
        return structureModel;
    }
}