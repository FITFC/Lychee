package snownee.lychee.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;

public class MixinPlugin implements IMixinConfigPlugin {
	private static boolean hasMod(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (mixinClassName.startsWith("snownee.lychee.mixin.fabric.")) {
			return !hasMod("quilted_fabric_api");
		}
		if ("snownee.lychee.mixin.ItemEntityHurtMixin".equals(mixinClassName)) {
			return !hasMod("itemphysic");
		}
		if ("snownee.lychee.mixin.itemphysic.ItemEntityHurtMixin".equals(mixinClassName)) {
			return hasMod("itemphysic");
		}
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

}