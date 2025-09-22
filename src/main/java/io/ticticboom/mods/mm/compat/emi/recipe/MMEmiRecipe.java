package io.ticticboom.mods.mm.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.compat.emi.category.MMEmiRecipeCategory;
import io.ticticboom.mods.mm.compat.emi.util.EmiSlotGrid;
import io.ticticboom.mods.mm.compat.emi.util.EmiSlotGridEntry;
import io.ticticboom.mods.mm.recipe.RecipeModel;
import io.ticticboom.mods.mm.recipe.input.IRecipeIngredientEntry;
import io.ticticboom.mods.mm.recipe.output.IRecipeOutputEntry;
import io.ticticboom.mods.mm.structure.StructureManager;
import io.ticticboom.mods.mm.structure.StructureModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MMEmiRecipe implements EmiRecipe {
    
    private final RecipeModel recipe;
    private final EmiRecipeCategory category;
    private final List<EmiIngredient> inputs = new ArrayList<>();
    private final List<EmiStack> outputs = new ArrayList<>();
    
    public MMEmiRecipe(RecipeModel recipe, EmiRecipeCategory category) {
        this.recipe = recipe;
        this.category = category;
        
        // Convert inputs
        for (IRecipeIngredientEntry input : recipe.inputs().inputs()) {
            EmiIngredient ingredient = input.getEmiIngredient();
            if (ingredient != null) {
                inputs.add(ingredient);
            }
        }
        
        // Convert outputs
        for (IRecipeOutputEntry output : recipe.outputs().outputs()) {
            EmiStack stack = output.getEmiStack();
            if (stack != null) {
                outputs.add(stack);
            }
        }
    }
    
    @Override
    public EmiRecipeCategory getCategory() {
        return category;
    }
    
    @Override
    public ResourceLocation getId() {
        return recipe.id();
    }
    
    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }
    
    @Override
    public List<EmiStack> getOutputs() {
        return outputs;
    }
    
    @Override
    public int getDisplayWidth() {
        return 162;
    }
    
    @Override
    public int getDisplayHeight() {
        return 50;
    }
    
    @Override
    public void addWidgets(WidgetHolder widgets) {
        var inGrid = new EmiSlotGrid(20, 20, 3, 5, 0, 0);
        var outGrid = new EmiSlotGrid(20, 20, 3, 5, 100, 0);
        
        // Add input slots
        for (IRecipeIngredientEntry input : recipe.inputs().inputs()) {
            EmiSlotGridEntry slot = inGrid.next();
            EmiIngredient ingredient = input.getEmiIngredient();
            if (ingredient != null) {
                widgets.addSlot(ingredient, slot.getInnerX(), slot.getInnerY());
                slot.setUsed();
            }
        }
        
        // Add output slots
        for (IRecipeOutputEntry output : recipe.outputs().outputs()) {
            EmiSlotGridEntry slot = outGrid.next();
            EmiStack stack = output.getEmiStack();
            if (stack != null) {
                widgets.addSlot(stack, slot.getInnerX(), slot.getInnerY()).recipeContext(this);
                slot.setUsed();
            }
        }
        
        // Add progress bar
        widgets.addTexture(Ref.Textures.SLOT_PARTS, 70, 12, 24, 17, 26, 0);
        widgets.addAnimatedTexture(Ref.Textures.SLOT_PARTS, 70, 12, 24, 17, 26, 17, recipe.ticks() * 50, false, true, false);
        
        // Add processing time tooltip
        var seconds = (double) recipe.ticks() / 20;
        var timeText = String.format("%.2f", seconds) + "s";
        widgets.addTooltipText(List.of(Component.literal(timeText)), 70, 12, 24, 17);
        
        // Add structure info if not structure-specific category
        boolean showStructureInfo = false;
        if (category instanceof MMEmiRecipeCategory recipeCategory) {
            showStructureInfo = recipeCategory.getStructureModel() == null;
        } else {
            // For port categories, always show structure info
            showStructureInfo = true;
        }
        
        if (showStructureInfo) {
            widgets.addTexture(Ref.Textures.SLOT_PARTS, 75, 28, 7, 9, 19, 26);
            StructureModel structure = StructureManager.STRUCTURES.get(recipe.structureId());
            if (structure != null) {
                widgets.addTooltipText(List.of(Component.literal("Structure: " + structure.name())), 75, 28, 7, 9);
            }
        }
        
        // Add slot backgrounds
        for (EmiSlotGridEntry inputSlot : inGrid.getSlots()) {
            if (inputSlot.used()) {
                widgets.addTexture(Ref.Textures.SLOT_PARTS, inputSlot.x, inputSlot.y, 18, 18, 0, 26);
            }
        }
        
        for (EmiSlotGridEntry outputSlot : outGrid.getSlots()) {
            if (outputSlot.used()) {
                widgets.addTexture(Ref.Textures.SLOT_PARTS, outputSlot.x, outputSlot.y, 18, 18, 0, 26);
            }
        }
    }
}