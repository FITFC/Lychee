package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.LycheeRecipe;

public enum IsSneaking implements ContextualCondition {
	INSTANCE;

	@Override
	public ContextualConditionType<? extends ContextualCondition> getType() {
		return ContextualConditionTypes.IS_SNEAKING;
	}

	@Override
	public int test(LycheeRecipe<?> recipe, LycheeContext ctx, int times) {
		return ctx.getParam(LootContextParams.THIS_ENTITY).isCrouching() ? times : 0;
	}

	@Override
	public MutableComponent getDescription(boolean inverted) {
		return new TranslatableComponent(makeDescriptionId(inverted));
	}

	public static class Type extends ContextualConditionType<IsSneaking> {

		@Override
		public IsSneaking fromJson(JsonObject o) {
			return IsSneaking.INSTANCE;
		}

		@Override
		public IsSneaking fromNetwork(FriendlyByteBuf buf) {
			return IsSneaking.INSTANCE;
		}

		@Override
		public void toNetwork(IsSneaking condition, FriendlyByteBuf buf) {
		}

	}

}
