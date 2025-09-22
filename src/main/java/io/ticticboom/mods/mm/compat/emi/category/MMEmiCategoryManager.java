package io.ticticboom.mods.mm.compat.emi.category;

import dev.emi.emi.api.EmiRegistry;
import io.ticticboom.mods.mm.recipe.MachineRecipeManager;
import io.ticticboom.mods.mm.compat.emi.recipe.MMEmiRecipe;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class MMEmiCategoryManager {
    
    private static final List<MMEmiPortCategory> PORT_CATEGORIES = new ArrayList<>();
    
    public static void registerPortCategories(EmiRegistry registry) {
        // Always register basic categories
        registerCategory(registry, MMEmiEnergyCategory.INSTANCE);
        registerCategory(registry, MMEmiFluidCategory.INSTANCE);
        
        // Conditionally register mod-specific categories
        if (ModList.get().isLoaded("create")) {
            registerCategory(registry, MMEmiKineticCategory.INSTANCE);
        }
        
        if (ModList.get().isLoaded("mekanism")) {
            registerCategory(registry, MMEmiChemicalCategory.INSTANCE);
        }
        
        if (ModList.get().isLoaded("botania")) {
            registerCategory(registry, MMEmiManaCategory.INSTANCE);
        }
        
        if (ModList.get().isLoaded("pneumaticcraft")) {
            registerCategory(registry, MMEmiAirCategory.INSTANCE);
        }
    }
    
    private static void registerCategory(EmiRegistry registry, MMEmiPortCategory category) {
        // Only register if there are recipes that use this port type
        boolean hasRecipes = MachineRecipeManager.RECIPES.values().stream()
                .anyMatch(category::matchesRecipe);
        
        if (hasRecipes) {
            registry.addCategory(category);
            PORT_CATEGORIES.add(category);
            
            // Register recipes for this category
            var recipes = MachineRecipeManager.RECIPES.values().stream()
                    .filter(category::matchesRecipe)
                    .map(recipe -> new MMEmiRecipe(recipe, category))
                    .toList();
            
            for (var recipe : recipes) {
                registry.addRecipe(recipe);
            }
        }
    }
    
    public static List<MMEmiPortCategory> getRegisteredCategories() {
        return new ArrayList<>(PORT_CATEGORIES);
    }
}