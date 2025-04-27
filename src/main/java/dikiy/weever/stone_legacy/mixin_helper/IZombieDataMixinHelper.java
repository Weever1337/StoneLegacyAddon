package dikiy.weever.stone_legacy.mixin_helper;

import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import net.minecraft.nbt.CompoundNBT;

public interface IZombieDataMixinHelper {
    TypeSpecificData stoneLegacyAddon$getPreviousData();

    CompoundNBT stoneLegacyAddon$getPreviousDataNbt();

    NonStandPowerType<?> stoneLegacyAddon$getPreviousPowerType();
}
