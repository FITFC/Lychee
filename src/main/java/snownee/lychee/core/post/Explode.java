package snownee.lychee.core.post;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.PostActionTypes;
import snownee.lychee.client.gui.GuiGameElement;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.LycheeRecipe;
import snownee.lychee.util.LUtil;

public class Explode extends PostAction {

	public final BlockInteraction blockInteraction;
	public final BlockPos offset;
	public final boolean fire;
	public final float radius;
	public final float step;

	public Explode(BlockInteraction blockInteraction, BlockPos offset, boolean fire, float radius, float step) {
		this.blockInteraction = blockInteraction;
		this.offset = offset;
		this.fire = fire;
		this.radius = radius;
		this.step = step;
	}

	@Override
	public PostActionType<?> getType() {
		return PostActionTypes.EXPLODE;
	}

	@Override
	public boolean doApply(LycheeRecipe<?> recipe, LycheeContext ctx, int times) {
		apply(recipe, ctx, times);
		return true;
	}

	@Override
	protected void apply(LycheeRecipe<?> recipe, LycheeContext ctx, int times) {
		Vec3 pos = ctx.getParamOrNull(LootContextParams.ORIGIN);
		if (pos == null) {
			pos = Vec3.atCenterOf(ctx.getParam(LycheeLootContextParams.BLOCK_POS));
		}
		pos = pos.add(offset.getX(), offset.getY(), offset.getZ());
		float r = Math.min(radius + step * (Mth.sqrt(times) - 1), radius * 4);
		ctx.getLevel().explode(ctx.getParamOrNull(LootContextParams.THIS_ENTITY), null, null, pos.x, pos.y, pos.z, r, fire, blockInteraction);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void render(PoseStack poseStack, int x, int y) {
		GuiGameElement.of(Items.TNT).render(poseStack, x, y);
	}

	@Override
	public Component getDisplayName() {
		String s = switch (blockInteraction) {
		case NONE -> "none";
		case BREAK -> "break";
		case DESTROY -> "destroy";
		default -> throw new IllegalArgumentException("Unexpected value: " + blockInteraction);
		};
		return new TranslatableComponent(LUtil.makeDescriptionId("postAction", getType().getRegistryName()) + "." + s);
	}

	public static class Type extends PostActionType<Explode> {

		@Override
		public Explode fromJson(JsonObject o) {
			int x = GsonHelper.getAsInt(o, "offsetX", 0);
			int y = GsonHelper.getAsInt(o, "offsetY", 0);
			int z = GsonHelper.getAsInt(o, "offsetZ", 0);
			BlockPos offset = BlockPos.ZERO;
			if (x != 0 || y != 0 || z != 0) {
				offset = new BlockPos(x, y, z);
			}
			boolean fire = GsonHelper.getAsBoolean(o, "fire", false);
			String s = GsonHelper.getAsString(o, "block_interaction", "break");
			BlockInteraction blockInteraction = switch (s) {
			case "none" -> BlockInteraction.NONE;
			case "break" -> BlockInteraction.BREAK;
			case "destroy" -> BlockInteraction.DESTROY;
			default -> throw new IllegalArgumentException("Unexpected value: " + s);
			};
			float radius = GsonHelper.getAsFloat(o, "radius", 4);
			float radiusStep = GsonHelper.getAsFloat(o, "radius_step", 0.5F);
			return new Explode(blockInteraction, offset, fire, radius, radiusStep);
		}

		@Override
		public Explode fromNetwork(FriendlyByteBuf buf) {
			BlockInteraction blockInteraction = buf.readEnum(BlockInteraction.class);
			BlockPos offset = buf.readBlockPos();
			boolean fire = buf.readBoolean();
			float radius = buf.readFloat();
			float step = buf.readFloat();
			return new Explode(blockInteraction, offset, fire, radius, step);
		}

		@Override
		public void toNetwork(Explode action, FriendlyByteBuf buf) {
			buf.writeEnum(action.blockInteraction);
			buf.writeBlockPos(action.offset);
			buf.writeBoolean(action.fire);
			buf.writeFloat(action.radius);
			buf.writeFloat(action.step);
		}

	}

}
