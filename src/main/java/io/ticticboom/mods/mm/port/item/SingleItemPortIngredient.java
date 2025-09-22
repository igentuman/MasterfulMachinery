package io.ticticboom.mods.mm.port.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Predicate;

public class SingleItemPortIngredient extends BaseItemPortIngredient {

    private final ResourceLocation itemId;
    private final Item item;
    private final ItemStack stack;

    public SingleItemPortIngredient(ResourceLocation itemId, int count) {
        super(count, createPredicate(itemId));
        this.itemId = itemId;
        item = ForgeRegistries.ITEMS.getValue(itemId);
        if (item == null) {
            throw new RuntimeException(String.format("Could not find item [%s] which is required by an MM recipe", itemId));
        }
        stack = new ItemStack(item, count);
    }

    private static Predicate<ItemStack> createPredicate(ResourceLocation id) {
        var item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null) {
            throw new RuntimeException(String.format("Could not find item [%s] which is required by an MM recipe", id));
        }
        return c -> c.is(item);
    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
        List<ItemPortStorage> itemStorages = storages.getOutputStorages(ItemPortStorage.class);
        int remainingToInsert = count;
        for (ItemPortStorage itemStorage : itemStorages) {
            remainingToInsert = itemStorage.canInsert(item, remainingToInsert);
        }
        return remainingToInsert <= 0;
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
        List<ItemPortStorage> itemStorages = storages.getOutputStorages(ItemPortStorage.class);
        int remainingToInsert = count;
        for (ItemPortStorage itemStorage : itemStorages) {
            remainingToInsert = itemStorage.insert(item, remainingToInsert);
        }
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // The ingredient type constant will be provided by the JEI integration
        context.addIngredient("ITEM", this.stack);
    }



    @Override
    public JsonObject debugOutput(Level level, RecipeStorages storages, JsonObject json) {
        List<ItemPortStorage> itemStorages = storages.getOutputStorages(ItemPortStorage.class);
        var searchedStorages = new JsonArray();
        var searchIterations = new JsonArray();
        json.addProperty("ingredientType", Ref.Ports.ITEM.toString());
        json.addProperty("amountToInsert", count);

        int remainingToInsert = count;
        for (ItemPortStorage storage : itemStorages) {
            var iterJson = new JsonObject();

            remainingToInsert = storage.canInsert(item, remainingToInsert);

            iterJson.addProperty("remaining", remainingToInsert);
            iterJson.addProperty("storageUid", storage.getStorageUid().toString());
            searchIterations.add(iterJson);
            searchedStorages.add(storage.getStorageUid().toString());
        }

        json.add("insertIterations", searchIterations);
        json.addProperty("canRun", remainingToInsert <= 0);
        json.add("searchedStorages", searchedStorages);
        return json;
    }
    
    @Override
    public EmiIngredient getEmiIngredient() {
        return EmiStack.of(stack);
    }
    
    @Override
    public EmiStack getEmiStack() {
        return EmiStack.of(stack);
    }
}
