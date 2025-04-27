package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.IPowerType;
import com.github.standobyte.jojo.power.impl.PowerBaseImpl;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.zombie.ZombiePowerType;
import dikiy.weever.stone_legacy.mixin_helper.IZombieDataMixinHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nonnull;

@Mixin(value = PowerBaseImpl.class, remap = false)
public abstract class PowerBaseImplMixin<P extends IPower<P, T>, T extends IPowerType<P, T>> implements IPower<P, T> {
    @Shadow @Final @Nonnull protected LivingEntity user;

    @Inject(method = "onClone", at = @At("TAIL"), remap = false)
    public void restorePreviousPower(P oldPower, boolean wasDeath, CallbackInfo ci) {
        if (oldPower.hasPower() && oldPower.getType() instanceof ZombiePowerType) {
            if (wasDeath)
                INonStandPower.getNonStandPowerOptional(user).ifPresent(p ->
                    p.getTypeSpecificData(ModPowers.ZOMBIE.get()).ifPresent(d -> {
                        if (d instanceof IZombieDataMixinHelper) {
                            IZombieDataMixinHelper data = (IZombieDataMixinHelper) d;
                            if (data.getPreviousPowerType() != null) {
                                if (data.getPreviousData() != null) {
                                    p.givePower(data.getPreviousPowerType());
                                    d.readNBT(data.getPreviousDataNbt());
                                } else p.clear();
                            }
                        }
                    }));
        }
    }
}
