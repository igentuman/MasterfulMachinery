package io.ticticboom.mods.mm.compat.emi.ingredient;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class EnergyEmiStack extends EmiStack {
    
    private final int amount;
    private static final ResourceLocation ENERGY_TEXTURE = new ResourceLocation("mm", "textures/gui/energy.png");
    
    public EnergyEmiStack(int amount) {
        this.amount = amount;
    }
    
    @Override
    public EmiStack copy() {
        return new EnergyEmiStack(amount);
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
        // Render energy icon - for now just render a simple colored rectangle
        graphics.fill(x, y, x + 16, y + 16, 0xFFFFD700); // Gold color for energy
        
        // Render amount text if needed
        if ((flags & RENDER_AMOUNT) != 0 && amount > 1) {
            String amountText = String.valueOf(amount);
            var font = Minecraft.getInstance().font;
            graphics.drawString(font, amountText, x + 17 - font.width(amountText), y + 9, 0xFFFFFF, true);
        }
    }
    
    @Override
    public List<ClientTooltipComponent> getTooltip() {
        return List.of(
            EmiTooltipComponents.of(Component.literal("Energy")),
            EmiTooltipComponents.of(Component.literal(amount + " FE"))
        );
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mm", "energy");
    }
    
    @Override
    public Component getName() {
        return Component.literal("Energy");
    }
    
    @Override
    public boolean isEmpty() {
        return amount <= 0;
    }
    
    @Override
    public EmiStack setAmount(long amount) {
        return new EnergyEmiStack((int) amount);
    }

    @Override
    public long getAmount() {
        return amount;
    }
    
    @Override
    public boolean isEqual(EmiStack stack) {
        return stack instanceof EnergyEmiStack other && other.amount == this.amount;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(amount);
    }
}