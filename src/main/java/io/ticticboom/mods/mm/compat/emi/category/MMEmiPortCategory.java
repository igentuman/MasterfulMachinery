package io.ticticboom.mods.mm.compat.emi.category;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.setup.MMRegisters;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class MMEmiPortCategory extends EmiRecipeCategory {
    
    private final String portTypeName;
    private final Set<ResourceLocation> portTypes;
    private final EmiStack icon;
    
    public MMEmiPortCategory(String portTypeName, Set<ResourceLocation> portTypes, EmiStack icon) {
        super(Ref.id(portTypeName.toLowerCase() + "_recipes"), icon);
        this.portTypeName = portTypeName;
        this.portTypes = portTypes;
        this.icon = icon;
    }
    
    @Override
    public Component getName() {
        return Component.literal(portTypeName + " Recipes");
    }
    
    public Set<ResourceLocation> getPortTypes() {
        return portTypes;
    }
    
    public String getPortTypeName() {
        return portTypeName;
    }
    
    public boolean matchesRecipe(io.ticticboom.mods.mm.recipe.RecipeModel recipe) {
        // For now, accept all recipes - we'll filter by port type in the EMI plugin
        // TODO: Implement proper port type checking when recipe structure allows it
        return true;
    }
}