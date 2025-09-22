package io.ticticboom.mods.mm.compat.emi.ingredient;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ManaEmiStack extends EmiStack {
    
    private final int amount;
    private static final ResourceLocation MANA_TEXTURE = new ResourceLocation("mm", "textures/gui/mana.png");
    
    public ManaEmiStack(int amount) {
        this.amount = amount;
    }
    
    @Override
    public EmiStack copy() {
        return new ManaEmiStack(amount);
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
        // Render mana icon - for now just render a simple colored rectangle
        graphics.fill(x, y, x + 16, y + 16, 0xFF00BFFF); // Deep sky blue color for mana
        
        // Render amount text if needed
        if ((flags & RENDER_AMOUNT) != 0 && amount > 1) {
            String amountText = formatAmount(amount);
            var font = Minecraft.getInstance().font;
            graphics.drawString(font, amountText, x + 17 - font.width(amountText), y + 9, 0xFFFFFF, true);
        }
    }
    
    private String formatAmount(int amount) {
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
        return List.of(
            EmiTooltipComponents.of(Component.literal("Mana")),
            EmiTooltipComponents.of(Component.literal(amount + " Mana"))
        );
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mm", "mana");
    }
    
    @Override
    public Component getName() {
        return Component.literal("Mana");
    }
    
    @Override
    public boolean isEmpty() {
        return amount <= 0;
    }
    
    @Override
    public EmiStack setAmount(long amount) {
        return new ManaEmiStack((int) amount);
    }

    @Override
    public long getAmount() {
        return amount;
    }
    
    @Override
    public boolean isEqual(EmiStack stack) {
        return stack instanceof ManaEmiStack other && other.amount == this.amount;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(amount);
    }
}