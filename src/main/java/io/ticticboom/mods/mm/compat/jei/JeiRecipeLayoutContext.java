package io.ticticboom.mods.mm.compat.jei;

import io.ticticboom.mods.mm.compat.jei.ingredient.MMJeiIngredients;
import io.ticticboom.mods.mm.compat.jei.ingredient.energy.EnergyStack;
import io.ticticboom.mods.mm.compat.jei.ingredient.mana.BotaniaManaStack;
import io.ticticboom.mods.mm.compat.jei.ingredient.pncr.PneumaticAirStack;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeModel;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.client.jei.MekanismJEI;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

/**
 * JEI-specific implementation of the recipe layout context.
 * This class bridges the abstraction layer with JEI's actual API.
 */
public class JeiRecipeLayoutContext implements IRecipeLayoutContext {
    private final IRecipeSlotBuilder recipeSlot;
    private final RecipeModel recipeModel;
    private final SlotGrid slotGrid;

    public JeiRecipeLayoutContext(IRecipeSlotBuilder recipeSlot, RecipeModel recipeModel, SlotGrid slotGrid) {
        this.recipeSlot = recipeSlot;
        this.recipeModel = recipeModel;
        this.slotGrid = slotGrid;
    }

    @Override
    public RecipeModel getRecipeModel() {
        return recipeModel;
    }

    @Override
    public void addIngredient(Object ingredientType, Object ingredient) {
        String type = (String) ingredientType;
        
        switch (type) {
            case "ITEM":
                if (ingredient instanceof ItemStack) {
                    recipeSlot.addIngredient(VanillaTypes.ITEM_STACK, (ItemStack) ingredient);
                }
                break;
                
            case "ITEM_STACKS":
                if (ingredient instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<ItemStack> stacks = (List<ItemStack>) ingredient;
                    recipeSlot.addItemStacks(stacks);
                }
                break;
                
            case "FLUID":
                if (ingredient instanceof FluidStack) {
                    FluidStack fluidStack = (FluidStack) ingredient;
                    recipeSlot.addIngredient(ForgeTypes.FLUID_STACK, fluidStack);
                    recipeSlot.addTooltipCallback((a, b) -> {
                        b.add(1, Component.literal(fluidStack.getAmount() + " mB"));
                    });
                }
                break;
                
            case "ENERGY":
                if (ingredient instanceof Integer) {
                    int amount = (Integer) ingredient;
                    recipeSlot.addIngredient(MMJeiIngredients.ENERGY, new EnergyStack(amount));
                }
                break;
                
            case "BOTANIA_MANA":
                if (ingredient instanceof Integer) {
                    int amount = (Integer) ingredient;
                    recipeSlot.addIngredient(MMJeiIngredients.BOTANIA_MANA, new BotaniaManaStack(amount));
                }
                break;
                
            case "PNEUMATIC_AIR":
                if (ingredient instanceof Object[]) {
                    Object[] params = (Object[]) ingredient;
                    if (params.length >= 2 && params[0] instanceof Integer && params[1] instanceof Float) {
                        int air = (Integer) params[0];
                        float bar = (Float) params[1];
                        recipeSlot.addIngredient(MMJeiIngredients.PNEUMATIC_AIR, new PneumaticAirStack(air, bar));
                    }
                }
                break;
                
            case "MEK_GAS":
                if (ingredient instanceof GasStack) {
                    GasStack gasStack = (GasStack) ingredient;
                    recipeSlot.addIngredient(MekanismJEI.TYPE_GAS, gasStack);
                    addMekanismTooltip(gasStack.getAmount());
                }
                break;
                
            case "MEK_INFUSION":
                if (ingredient instanceof InfusionStack) {
                    InfusionStack infusionStack = (InfusionStack) ingredient;
                    recipeSlot.addIngredient(MekanismJEI.TYPE_INFUSION, infusionStack);
                    addMekanismTooltip(infusionStack.getAmount());
                }
                break;
                
            case "MEK_PIGMENT":
                if (ingredient instanceof PigmentStack) {
                    PigmentStack pigmentStack = (PigmentStack) ingredient;
                    recipeSlot.addIngredient(MekanismJEI.TYPE_PIGMENT, pigmentStack);
                    addMekanismTooltip(pigmentStack.getAmount());
                }
                break;
                
            case "MEK_SLURRY":
                if (ingredient instanceof SlurryStack) {
                    SlurryStack slurryStack = (SlurryStack) ingredient;
                    recipeSlot.addIngredient(MekanismJEI.TYPE_SLURRY, slurryStack);
                    addMekanismTooltip(slurryStack.getAmount());
                }
                break;
        }
    }

    @Override
    public Object getNextSlotPosition() {
        return slotGrid.next();
    }
    
    @Override
    public void addTooltip(Component tooltip) {
        recipeSlot.addTooltipCallback((a, c) -> {
            c.add(tooltip);
        });
    }
    
    private void addMekanismTooltip(long amount) {
        recipeSlot.addTooltipCallback((a, c) -> {
            c.add(1, Component.literal(amount + " mB"));
        });
    }
}