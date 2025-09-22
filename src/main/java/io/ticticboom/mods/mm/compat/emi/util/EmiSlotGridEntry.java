package io.ticticboom.mods.mm.compat.emi.util;

public class EmiSlotGridEntry {
    public final int x;
    public final int y;
    private boolean used = false;
    
    public EmiSlotGridEntry(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean used() {
        return used;
    }
    
    public void setUsed() {
        this.used = true;
    }
    
    public int getInnerX() {
        return x + 1; // Add 1 pixel offset for inner slot position
    }
    
    public int getInnerY() {
        return y + 1; // Add 1 pixel offset for inner slot position
    }
}