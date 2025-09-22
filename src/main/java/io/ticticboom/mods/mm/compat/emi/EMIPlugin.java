package io.ticticboom.mods.mm.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.compat.emi.category.MMEmiRecipeCategory;
import io.ticticboom.mods.mm.compat.emi.category.MMEmiStructureCategory;
import io.ticticboom.mods.mm.compat.emi.category.MMEmiCategoryManager;
import io.ticticboom.mods.mm.compat.emi.recipe.MMEmiRecipe;
import io.ticticboom.mods.mm.compat.emi.recipe.MMEmiStructureRecipe;
import io.ticticboom.mods.mm.config.MMConfig;
import io.ticticboom.mods.mm.controller.MMControllerRegistry;
import io.ticticboom.mods.mm.recipe.MachineRecipeManager;
import io.ticticboom.mods.mm.setup.MMRegisters;
import io.ticticboom.mods.mm.structure.StructureManager;
import io.ticticboom.mods.mm.structure.StructureModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class EMIPlugin implements EmiPlugin {
    
    public static final List<MMEmiRecipeCategory> recipeCategories = new ArrayList<>();
    
    @Override
    public void register(EmiRegistry registry) {
        // Register categories
        if (MMConfig.EMI_PORT_CATEGORIES) {
            // Use port-based categories
            MMEmiCategoryManager.registerPortCategories(registry);
        } else if (MMConfig.JEI_RECIPE_SPLIT) {
            for (StructureModel parentStructure : StructureManager.STRUCTURES.values()) {
                MMEmiRecipeCategory category = new MMEmiRecipeCategory(parentStructure);
                registry.addCategory(category);
                recipeCategories.add(category);
            }
        } else {
            registry.addCategory(MMEmiRecipeCategory.INSTANCE);
            recipeCategories.add(MMEmiRecipeCategory.INSTANCE);
        }
        
        registry.addCategory(MMEmiStructureCategory.INSTANCE);

        // Register recipes (skip if using port categories as they handle their own recipes)
        if (!MMConfig.EMI_PORT_CATEGORIES) {
            if (MMConfig.JEI_RECIPE_SPLIT) {
                for (var category : recipeCategories) {
                    var recipes = MachineRecipeManager.RECIPES.values().stream()
                            .filter(x -> x.structureId().equals(category.getStructureModel().id()))
                            .map(recipe -> new MMEmiRecipe(recipe, category))
                            .toList();
                    for (var recipe : recipes) {
                        registry.addRecipe(recipe);
                    }
                }
            } else {
                var recipes = MachineRecipeManager.RECIPES.values().stream()
                        .map(recipe -> new MMEmiRecipe(recipe, MMEmiRecipeCategory.INSTANCE))
                        .toList();
                for (var recipe : recipes) {
                    registry.addRecipe(recipe);
                }
            }
        }
        
        // Register structure recipes
        var structureRecipes = StructureManager.STRUCTURES.values().stream()
                .map(MMEmiStructureRecipe::new)
                .toList();
        for (var recipe : structureRecipes) {
            registry.addRecipe(recipe);
        }
        
        // Register recipe catalysts
        if (!MMConfig.EMI_PORT_CATEGORIES) {
            for (var category : recipeCategories) {
                if (category.getStructureModel() != null) {
                    ResourceLocation location = category.getStructureModel().controllerIds().getIds().get(0);
                    Item controller = MMControllerRegistry.getControllerItem(location);
                    if (controller != null) {
                        registry.addWorkstation(category, EmiStack.of(controller));
                    }
                }
            }
        } else {
            // For port categories, add all controllers as workstations
            for (var category : MMEmiCategoryManager.getRegisteredCategories()) {
                for (var structure : StructureManager.STRUCTURES.values()) {
                    ResourceLocation location = structure.controllerIds().getIds().get(0);
                    Item controller = MMControllerRegistry.getControllerItem(location);
                    if (controller != null) {
                        registry.addWorkstation(category, EmiStack.of(controller));
                    }
                }
            }
        }
        
        // Register blueprint as workstation for structure category
        registry.addWorkstation(MMEmiStructureCategory.INSTANCE, EmiStack.of(MMRegisters.BLUEPRINT.get()));
    }
}