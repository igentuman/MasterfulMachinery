package io.ticticboom.mods.mm.port.item;

import com.google.gson.JsonObject;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import io.ticticboom.mods.mm.Ref;
import io.ticticboom.mods.mm.port.IRecipeLayoutContext;
import io.ticticboom.mods.mm.recipe.RecipeStateModel;
import io.ticticboom.mods.mm.recipe.RecipeStorages;
import io.ticticboom.mods.mm.util.ConditionalLazy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Predicate;

public class TagItemPortIngredient extends BaseItemPortIngredient {

    private final TagKey<Item> tag;
    private final ConditionalLazy<List<ItemStack>> stacks;

    public TagItemPortIngredient(ResourceLocation tagId, int count) {
        super(count, createPredicate(tagId));
        this.tag = ItemTags.create(tagId);
        stacks = ConditionalLazy.create(() -> ForgeRegistries.ITEMS.tags().getTag(tag).stream().map(x -> new ItemStack(x, count)).toList(),
                () -> !ForgeRegistries.ITEMS.tags().getTag(tag).isEmpty(), List.of());
    }

    private static Predicate<ItemStack> createPredicate(ResourceLocation id) {
        var key = ItemTags.create(id);
        return  i -> i.is(key);
    }

    @Override
    public boolean canOutput(Level level, RecipeStorages storages, RecipeStateModel state) {
        Ref.LOG.warn("Item Tags Ingredients will NEVER produce output, REMOVE any recipe outputs using item tags.");
        return false;
    }

    @Override
    public void output(Level level, RecipeStorages storages, RecipeStateModel state) {
    }

    @Override
    public void setupRecipeLayout(IRecipeLayoutContext context) {
        // For tag ingredients, we need to pass the list of stacks
        context.addIngredient("ITEM_STACKS", stacks.get());
    }

    @Override
    public JsonObject debugOutput(Level level, RecipeStorages storages, JsonObject json) {
        json.addProperty("isTag", true);
        json.addProperty("WILL_NEVER_WORK", true);
        return json;
    }
    
    @Override
    public EmiIngredient getEmiIngredient() {
        List<EmiStack> emiStacks = stacks.get().stream()
                .map(EmiStack::of)
                .toList();
        return EmiIngredient.of(emiStacks);
    }
    
    @Override
    public EmiStack getEmiStack() {
        // Tags can't be output, so return null
        return null;
    }
}
