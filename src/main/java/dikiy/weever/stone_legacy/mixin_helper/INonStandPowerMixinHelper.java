package dikiy.weever.stone_legacy.mixin_helper;

import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;

public interface INonStandPowerMixinHelper {
    boolean stoneLegacyAddon$givePower(NonStandPowerType<?> type, boolean force);
}
