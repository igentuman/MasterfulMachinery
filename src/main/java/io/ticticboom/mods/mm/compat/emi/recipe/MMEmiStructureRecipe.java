package io.ticticboom.mods.mm.compat.emi.recipe;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.client.structure.GuiCountedItemStack;
import io.ticticboom.mods.mm.client.structure.GuiStructureRenderer;
import io.ticticboom.mods.mm.compat.emi.category.MMEmiStructureCategory;
import io.ticticboom.mods.mm.compat.emi.util.EmiSlotGrid;
import io.ticticboom.mods.mm.compat.emi.util.EmiSlotGridEntry;
import io.ticticboom.mods.mm.controller.MMControllerRegistry;
import io.ticticboom.mods.mm.setup.MMRegisters;
import io.ticticboom.mods.mm.structure.StructureModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class MMEmiStructureRecipe implements EmiRecipe {
    
    private final StructureModel structure;
    private final List<EmiIngredient> inputs = new ArrayList<>();
    private final List<EmiStack> outputs = new ArrayList<>();
    private final List<EmiIngredient> catalysts = new ArrayList<>();
    
    public MMEmiStructureRecipe(StructureModel structure) {
        this.structure = structure;
        
        // Add controller catalysts
        for (ResourceLocation id : structure.controllerIds().getIds()) {
            Item controller = MMControllerRegistry.getControllerItem(id);
            if (controller != null) {
                catalysts.add(EmiStack.of(controller));
            }
        }
        
        // Add structure blueprint as output
        outputs.add(EmiStack.of(MMRegisters.BLUEPRINT.get().getStructureInstance(structure.id())));
        
        // Add counted item stacks as inputs
        var countedItemStacks = structure.getCountedItemStacks();
        for (GuiCountedItemStack countedItemStack : countedItemStacks) {
            List<EmiStack> stacks = new ArrayList<>();
            for (var stack : countedItemStack.getStacks()) {
                stacks.add(EmiStack.of(stack));
            }
            if (!stacks.isEmpty()) {
                inputs.add(EmiIngredient.of(stacks));
            }
        }
    }
    
    @Override
    public EmiRecipeCategory getCategory() {
        return MMEmiStructureCategory.INSTANCE;
    }
    
    @Override
    public ResourceLocation getId() {
        return structure.id();
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
    public List<EmiIngredient> getCatalysts() {
        return catalysts;
    }
    
    @Override
    public int getDisplayWidth() {
        return 162;
    }
    
    @Override
    public int getDisplayHeight() {
        return 170;
    }
    
    @Override
    public void addWidgets(WidgetHolder widgets) {
        // Add background
        widgets.addTexture(Ref.Textures.GUI_LARGE_JEI, 0, 0, 162, 170, 0, 0);
        
        // Initialize structure renderer
        GuiStructureRenderer guiRenderer = structure.getGuiRenderer();
        guiRenderer.resetTransforms();
        guiRenderer.init();
        
        // Add structure name
        widgets.addText(Component.literal(structure.name()), 5, 5, 0xFFFFFF, false);
        
        // Add item slots
        var countedItemStacks = structure.getCountedItemStacks();
        var grid = new EmiSlotGrid(20, 20, 8, 3, 1, 130);
        
        for (GuiCountedItemStack countedItemStack : countedItemStacks) {
            EmiSlotGridEntry next = grid.next();
            if (next != null) {
                next.setUsed();
                
                List<EmiStack> stacks = new ArrayList<>();
                for (var stack : countedItemStack.getStacks()) {
                    stacks.add(EmiStack.of(stack));
                }
                
                if (!stacks.isEmpty()) {
                    var slot = widgets.addSlot(EmiIngredient.of(stacks), next.x, next.y);
                    slot.appendTooltip(countedItemStack.getDetail());
                }
                
                // Add slot background
                widgets.addTexture(Ref.Textures.SLOT_PARTS, next.x - 1, next.y - 1, 18, 18, 0, 26);
            }
        }
        
        // Add structure renderer widget (this would need custom implementation)
        // For now, we'll add a placeholder
        widgets.addText(Component.literal("3D Structure Preview"), 5, 25, 0xAAAAAA, false);
        widgets.addText(Component.literal("(View in JEI for 3D preview)"), 5, 35, 0x888888, false);
    }
}