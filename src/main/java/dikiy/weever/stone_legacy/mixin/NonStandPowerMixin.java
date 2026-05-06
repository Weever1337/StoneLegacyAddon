package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.PowerBaseImpl;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.NonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.TypeSpecificData;
import com.github.standobyte.jojo.power.impl.nonstand.type.NonStandPowerType;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import dikiy.weever.stone_legacy.mixin_helper.INonStandPowerMixinHelper;
import dikiy.weever.stone_legacy.mixin_helper.IZombieDataMixinHelper;
import dikiy.weever.stone_legacy.mixin_helper.IZombiesReminder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = NonStandPower.class, remap = false)
public abstract class NonStandPowerMixin extends PowerBaseImpl<INonStandPower, NonStandPowerType<?>> implements INonStandPower, INonStandPowerMixinHelper {
    @Shadow
    private TypeSpecificData typeSpecificData;
    @Shadow
    private float energy;

    public NonStandPowerMixin(LivingEntity user) {
        super(user);
    }

    @Shadow
    private void setType(NonStandPowerType<?> powerType) {
    }

    @Shadow
    public abstract void readNBT(CompoundNBT nbt);

    @Shadow
    private void setTypeSpecificData(TypeSpecificData data) {
    }

    @Shadow
    public abstract NonStandPowerType<?> getType();

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
                    this.energy = oldPower.getEnergy();
                    System.out.println(((IZombieDataMixinHelper) data).stoneLegacyAddon$getPreviousDataNbt().toString());
                    this.setTypeSpecificData(((IZombieDataMixinHelper) data).stoneLegacyAddon$getPreviousData());
                    ci.cancel();
                }
            });
        }
    }

    @Inject(method = "clear", at = @At("TAIL"))
    public void clear(CallbackInfoReturnable<Boolean> cir) {
        if (this.getType() == ModPowers.ZOMBIE.get()) {
            this.user.getCapability(ZombieUtilProvider.CAPABILITY).ifPresent(cap -> {
                LivingEntity owner = (LivingEntity) ((ServerWorld) this.user.level).getEntity(cap.getOwnerUUID());
                INonStandPower.getNonStandPowerOptional(owner).ifPresent(ownerPower -> {
                    if (ownerPower.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get() instanceof IZombiesReminder) {
                        ((IZombiesReminder) (ownerPower.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get())).removeZombie(this.user);
                    }
                });
            });
        }
    }
}
