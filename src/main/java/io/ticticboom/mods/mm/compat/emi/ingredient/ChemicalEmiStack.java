package io.ticticboom.mods.mm.compat.emi.ingredient;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ChemicalEmiStack extends EmiStack {
    
    private final ResourceLocation chemicalId;
    private final long amount;
    private final String chemicalType;
    private final int color;
    
    public ChemicalEmiStack(ResourceLocation chemicalId, long amount, String chemicalType, int color) {
        this.chemicalId = chemicalId;
        this.amount = amount;
        this.chemicalType = chemicalType;
        this.color = color;
    }
    
    @Override
    public EmiStack copy() {
        return new ChemicalEmiStack(chemicalId, amount, chemicalType, color);
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
        // Render chemical with its color
        graphics.fill(x, y, x + 16, y + 16, color | 0xFF000000); // Ensure alpha is set
        
        // Render amount text if needed
        if ((flags & RENDER_AMOUNT) != 0 && amount > 1) {
            String amountText = formatAmount(amount);
            var font = Minecraft.getInstance().font;
            graphics.drawString(font, amountText, x + 17 - font.width(amountText), y + 9, 0xFFFFFF, true);
        }
    }
    
    private String formatAmount(long amount) {
        if (amount >= 1000000) {
            return String.format("%.1fM", amount / 1000000.0);
        } else if (amount >= 1000) {
            return String.format("%.1fK", amount / 1000.0);
        } else {
            return String.valueOf(amount);
        }
    }
    
    @Override
    public List<ClientTooltipComponent> getTooltip() {
        String chemicalName = chemicalId.getPath().replace("_", " ");
        // Capitalize first letter of each word
        String[] words = chemicalName.split(" ");
        StringBuilder capitalizedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedName.append(Character.toUpperCase(word.charAt(0)))
                              .append(word.substring(1).toLowerCase())
                              .append(" ");
            }
        }
        
        return List.of(
            EmiTooltipComponents.of(Component.literal(capitalizedName.toString().trim())),
            EmiTooltipComponents.of(Component.literal(amount + " mB " + chemicalType))
        );
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mm", "chemical_" + chemicalType.toLowerCase() + "_" + chemicalId.getPath());
    }
    
    @Override
    public Component getName() {
        String chemicalName = chemicalId.getPath().replace("_", " ");
        // Capitalize first letter of each word
        String[] words = chemicalName.split(" ");
        StringBuilder capitalizedName = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedName.append(Character.toUpperCase(word.charAt(0)))
                              .append(word.substring(1).toLowerCase())
                              .append(" ");
            }
        }
        return Component.literal(capitalizedName.toString().trim());
    }
    
    @Override
    public boolean isEmpty() {
        return amount <= 0;
    }
    
    @Override
    public EmiStack setAmount(long amount) {
        return new ChemicalEmiStack(chemicalId, amount, chemicalType, color);
    }

    @Override
    public long getAmount() {
        return amount;
    }
    
    @Override
    public boolean isEqual(EmiStack stack) {
        return stack instanceof ChemicalEmiStack other && 
               other.chemicalId.equals(this.chemicalId) && 
               other.amount == this.amount &&
               other.chemicalType.equals(this.chemicalType);
    }

    @Override
    public int hashCode() {
        return chemicalId.hashCode() * 31 + Long.hashCode(amount) + chemicalType.hashCode();
    }
    
    public ResourceLocation getChemicalId() {
        return chemicalId;
    }
    
    public String getChemicalType() {
        return chemicalType;
    }
}