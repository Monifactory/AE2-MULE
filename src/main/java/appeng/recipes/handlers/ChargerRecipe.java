package appeng.recipes.handlers;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import appeng.core.AppEng;
import appeng.init.InitRecipeTypes;

public class ChargerRecipe implements Recipe<Container> {
    public static final ResourceLocation TYPE_ID = AppEng.makeId("charger");

    public static final RecipeType<ChargerRecipe> TYPE = InitRecipeTypes.register(TYPE_ID.toString());

    private final ResourceLocation id;
    public final Ingredient ingredient;
    public final NonNullList<Ingredient> ingredients;
    public final Item result;

    public ChargerRecipe(ResourceLocation id, Ingredient ingredient, Item result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.ingredients = NonNullList.of(Ingredient.EMPTY, ingredient);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return getResultItem();
    }

    public ItemStack getResultItem() {
        return new ItemStack(result);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ChargerRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return TYPE;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }
}
