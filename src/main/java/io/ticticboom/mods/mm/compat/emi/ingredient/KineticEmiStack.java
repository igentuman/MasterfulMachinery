package io.ticticboom.mods.mm.compat.emi.ingredient;

import dev.emi.emi.api.render.EmiTooltipComponents;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class KineticEmiStack extends EmiStack {
    
    private final float speed;
    private static final ResourceLocation KINETIC_TEXTURE = new ResourceLocation("mm", "textures/gui/kinetic.png");
    
    public KineticEmiStack(float speed) {
        this.speed = speed;
    }
    
    @Override
    public EmiStack copy() {
        return new KineticEmiStack(speed);
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y, float delta, int flags) {
        // Render kinetic icon - for now just render a simple colored rectangle
        graphics.fill(x, y, x + 16, y + 16, 0xFF8B4513); // Brown color for kinetic/mechanical
        
        // Render speed text if needed
        if ((flags & RENDER_AMOUNT) != 0 && speed != 0) {
            String speedText = String.format("%.1f", Math.abs(speed));
            var font = Minecraft.getInstance().font;
            graphics.drawString(font, speedText, x + 17 - font.width(speedText), y + 9, 0xFFFFFF, true);
        }
    }
    
    @Override
    public List<ClientTooltipComponent> getTooltip() {
        return List.of(
            EmiTooltipComponents.of(Component.literal("Kinetic Energy")),
            EmiTooltipComponents.of(Component.literal(String.format("%.1f RPM", speed)))
        );
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation("mm", "kinetic");
    }
    
    @Override
    public boolean isEmpty() {
        return speed == 0;
    }
    
    @Override
    public EmiStack setAmount(long amount) {
        return new KineticEmiStack((float) amount);
    }

    @Override
    public long getAmount() {
        return (long) Math.abs(speed);
    }
    
    @Override
    public boolean isEqual(EmiStack stack) {
        return stack instanceof KineticEmiStack other && Float.compare(other.speed, this.speed) == 0;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(speed);
    }
}