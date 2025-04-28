package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.PowerBaseImpl;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.NonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import com.github.standobyte.jojo.power.impl.nonstand.type.zombie.ZombieData;
import dikiy.weever.stone_legacy.mixin_helper.INonStandPowerMixinHelper;
import dikiy.weever.stone_legacy.mixin_helper.IZombieDataMixinHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NonStandPower.class, remap = false)
public abstract class NonStandPowerMixin extends PowerBaseImpl<INonStandPower, NonStandPowerType<?>> implements INonStandPower, INonStandPowerMixinHelper {
    @Shadow
    private TypeSpecificData typeSpecificData;

    public NonStandPowerMixin(LivingEntity user) {
        super(user);
    }

    @Shadow
    private void setType(NonStandPowerType<?> powerType) {
    }

    @Shadow private float energy;

    @Shadow public abstract void readNBT(CompoundNBT nbt);
    @Shadow private void setTypeSpecificData(TypeSpecificData data) {}

    @Override
    @Unique
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
    @Inject(method = "keepPower(Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;Z)V", at = @At("HEAD"), cancellable = true, remap = false)
    protected void restorePreviousPower(INonStandPower oldPower, boolean wasDeath, CallbackInfo ci) {
        if (oldPower.getType() == ModPowers.ZOMBIE.get() && wasDeath) {
            oldPower.getTypeSpecificData(ModPowers.ZOMBIE.get()).ifPresent(data -> {
                if (data instanceof IZombieDataMixinHelper) {
                    super.keepPower(oldPower, wasDeath);
                    setType(((IZombieDataMixinHelper) data).stoneLegacyAddon$getPreviousPowerType());
                    System.out.println(((IZombieDataMixinHelper) data).stoneLegacyAddon$getPreviousPowerType().toString());
                    this.energy = oldPower.getEnergy();
                    System.out.println(((IZombieDataMixinHelper) data).stoneLegacyAddon$getPreviousDataNbt().toString());
                    this.setTypeSpecificData(((IZombieDataMixinHelper) data).stoneLegacyAddon$getPreviousData());
                    ci.cancel();
                }
            });
        }
    }
}
