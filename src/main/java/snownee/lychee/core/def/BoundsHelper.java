package snownee.lychee.core.def;

import java.text.DecimalFormat;
import java.util.Objects;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

public class BoundsHelper {
	public static DecimalFormat dfCommas = new DecimalFormat("##.##");

	public static MutableComponent getDescription(MinMaxBounds<?> bounds) {
		if (bounds.isAny()) {
			return new TextComponent("*");
		}
		if (bounds.getMin() == null) {
			return new TextComponent("<=" + dfCommas.format(bounds.getMax()));
		}
		if (bounds.getMax() == null) {
			return new TextComponent(">=" + dfCommas.format(bounds.getMin()));
		}
		if (Objects.equals(bounds.getMin(), bounds.getMax())) {
			return new TextComponent(dfCommas.format(bounds.getMin()));
		}
		return new TextComponent(dfCommas.format(bounds.getMin()) + "~" + dfCommas.format(bounds.getMax()));
	}

}
