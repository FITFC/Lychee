package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.MinMaxBounds.Doubles;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BoundsHelper;
import snownee.lychee.core.def.DoubleBoundsHelper;
import snownee.lychee.core.recipe.LycheeRecipe;

public record EntityHealth(Doubles range) implements ContextualCondition {

	@Override
	public ContextualConditionType<? extends ContextualCondition> getType() {
		return ContextualConditionTypes.ENTITY_HEALTH;
	}

	@Override
	public int test(LycheeRecipe<?> recipe, LycheeContext ctx, int times) {
		Entity entity = ctx.getParam(LootContextParams.THIS_ENTITY);
		double d = 0;
		if (entity instanceof LivingEntity living) {
			d = living.getHealth();
		}
		return range.matches(d) ? times : 0;
	}

	@Override
	public MutableComponent getDescription(boolean inverted) {
		return new TranslatableComponent(makeDescriptionId(inverted), BoundsHelper.getDescription(range));
	}

	public static class Type extends ContextualConditionType<EntityHealth> {

		@Override
		public EntityHealth fromJson(JsonObject o) {
			return new EntityHealth(Doubles.fromJson(o.get("range")));
		}

		@Override
		public EntityHealth fromNetwork(FriendlyByteBuf buf) {
			return new EntityHealth(DoubleBoundsHelper.fromNetwork(buf));
		}

		@Override
		public void toNetwork(EntityHealth condition, FriendlyByteBuf buf) {
			DoubleBoundsHelper.toNetwork(condition.range, buf);
		}

	}

}
