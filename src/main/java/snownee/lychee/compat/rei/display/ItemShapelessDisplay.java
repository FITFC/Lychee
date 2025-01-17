package snownee.lychee.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import snownee.lychee.core.recipe.ItemShapelessRecipe;

public class ItemShapelessDisplay<T extends ItemShapelessRecipe<T>> extends BaseREIDisplay<T> {

	public ItemShapelessDisplay(T recipe) {
		super(recipe);
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return CategoryIdentifier.of(recipe.getType().id);
	}

}
