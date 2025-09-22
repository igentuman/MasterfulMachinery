package io.ticticboom.mods.mm.compat.emi.util;

import java.util.ArrayList;
import java.util.List;

public class EmiSlotGrid {
    private final int slotWidth;
    private final int slotHeight;
    private final int maxCols;
    private final int maxRows;
    private final int startX;
    private final int startY;
    private final List<EmiSlotGridEntry> slots = new ArrayList<>();
    
    private int currentCol = 0;
    private int currentRow = 0;
    
    public EmiSlotGrid(int slotWidth, int slotHeight, int maxCols, int maxRows, int startX, int startY) {
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
        this.maxCols = maxCols;
        this.maxRows = maxRows;
        this.startX = startX;
        this.startY = startY;
    }
    
    public EmiSlotGridEntry next() {
        if (currentCol >= maxCols) {
            currentCol = 0;
            currentRow++;
        }
        
        if (currentRow >= maxRows) {
            // Reset if we've exceeded the grid
            currentRow = 0;
            currentCol = 0;
        }
        
        int x = startX + (currentCol * slotWidth);
        int y = startY + (currentRow * slotHeight);
        
        EmiSlotGridEntry entry = new EmiSlotGridEntry(x, y);
        slots.add(entry);
        
        currentCol++;
        return entry;
    }
    
    public List<EmiSlotGridEntry> getSlots() {
        return slots;
    }
}