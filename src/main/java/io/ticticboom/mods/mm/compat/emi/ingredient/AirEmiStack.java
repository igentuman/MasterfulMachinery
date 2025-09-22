package io.ticticboom.mods.mm.compat.emi.ingredient;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class AirEmiStack extends EmiStack {
    
    private final int air;
    private final float pressure;
    private static final ResourceLocation AIR_TEXTURE = new ResourceLocation("mm", "textures/gui/air.png");
    
    public AirEmiStack(int air, float pressure) {
        this.air = air;
        this.pressure = pressure;
    }
    
    @Override
    public EmiStack copy() {
        return new AirEmiStack(air, pressure);
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
        // Render air icon - for now just render a simple colored rectangle
        graphics.fill(x, y, x + 16, y + 16, 0xFFE6E6FA); // Lavender color for air
        
        // Render amount text if needed
        if ((flags & RENDER_AMOUNT) != 0 && air > 1) {
            String amountText = formatAmount(air);
            var font = Minecraft.getInstance().font;
            graphics.drawString(font, amountText, x + 17 - font.width(amountText), y + 9, 0x000000, true);
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
            EmiTooltipComponents.of(Component.literal("Compressed Air")),
            EmiTooltipComponents.of(Component.literal(air + " mL")),
            EmiTooltipComponents.of(Component.literal(String.format("%.1f bar", pressure)))
        );
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mm", "pneumatic_air");
    }
    
    @Override
    public Component getName() {
        return Component.literal("Compressed Air");
    }
    
    @Override
    public boolean isEmpty() {
        return air <= 0;
    }
    
    @Override
    public EmiStack setAmount(long amount) {
        return new AirEmiStack((int) amount, pressure);
    }

    @Override
    public long getAmount() {
        return air;
    }
    
    @Override
    public boolean isEqual(EmiStack stack) {
        return stack instanceof AirEmiStack other && 
               other.air == this.air && 
               Float.compare(other.pressure, this.pressure) == 0;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(air) * 31 + Float.hashCode(pressure);
    }
    
    public float getPressure() {
        return pressure;
    }
}