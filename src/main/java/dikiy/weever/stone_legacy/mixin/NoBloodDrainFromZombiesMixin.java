package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JojoModUtil.class)
public abstract class NoBloodDrainFromZombiesMixin {
    @Inject(method = "isUndeadOrVampiric", at = @At(value = "HEAD"), cancellable = true)
    private static void getPower(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        INonStandPower.getNonStandPowerOptional(entity).ifPresent(power -> {
            if (power.getType() == ModPowers.ZOMBIE.get()) {
                cir.setReturnValue(true);
            }
        });
    }
}
