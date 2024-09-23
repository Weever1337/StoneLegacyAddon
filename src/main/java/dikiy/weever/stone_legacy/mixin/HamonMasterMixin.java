package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.entity.mob.HamonMasterEntity;
import com.github.standobyte.jojo.entity.mob.IMobPowerUser;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.INPC;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HamonMasterEntity.class)
public abstract class HamonMasterMixin extends MobEntity implements INPC, IMobPowerUser, IEntityAdditionalSpawnData {

    @Shadow public abstract INonStandPower getPower();

    @Shadow @Deprecated protected abstract void restoreHamon();

    protected HamonMasterMixin(EntityType<? extends MobEntity> p_i48576_1_, World p_i48576_2_) { super(p_i48576_1_, p_i48576_2_); }

    @Inject(method="restoreHamon", at = @At(value = "HEAD"), cancellable = true)
    public void zombiePowerCheck(CallbackInfo ci) {
        INonStandPower.getNonStandPowerOptional(this).ifPresent(power -> {
            if (power.getType() != ModPowers.HAMON.get()) ci.cancel();
        });
    }
}
