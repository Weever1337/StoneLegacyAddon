package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.power.impl.PowerBaseImpl;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.NonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import dikiy.weever.stone_legacy.mixin_helper.INonStandPowerMixinHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = NonStandPower.class, remap = false)
public abstract class NonStandPowerMixin extends PowerBaseImpl<INonStandPower, NonStandPowerType<?>> implements INonStandPower, INonStandPowerMixinHelper {
    @Shadow private TypeSpecificData typeSpecificData;
    @Shadow private void setType(NonStandPowerType<?> powerType) {}
    public NonStandPowerMixin(LivingEntity user) { super(user); }
    @Override @Unique
    public boolean stoneLegacyAddon$givePower(NonStandPowerType<?> type, boolean force) {
        if (!canGetPower(type) && !(force && type != null)) {
            return false;
        }
        NonStandPowerType<?> oldType = this.getType();
        TypeSpecificData oldData = typeSpecificData;
        setType(type);
        onNewPowerGiven(type);
        typeSpecificData.onPowerGiven(oldType, oldData);
        clUpdateHud();
        addHadPowerBefore(type);
        return true;
    }
}
