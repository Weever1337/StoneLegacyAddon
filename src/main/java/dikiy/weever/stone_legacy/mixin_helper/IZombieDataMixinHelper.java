package dikiy.weever.stone_legacy.mixin_helper;

import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import net.minecraft.nbt.CompoundNBT;

public interface IZombieDataMixinHelper {
    public TypeSpecificData stoneLegacyAddon$getPreviousData();
    public CompoundNBT stoneLegacyAddon$getPreviousDataNbt();
    public NonStandPowerType<?> stoneLegacyAddon$getPreviousPowerType();
}
