package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import dikiy.weever.stone_legacy.mixin_helper.WaitGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = HungryZombieEntity.class, remap = false)
public abstract class WaitingZombieMixin extends ZombieEntity implements IWaitableEntity {
    @Final @Unique
    private static final DataParameter<Boolean> WAITING_DATA = EntityDataManager.defineId(WaitingZombieMixin.class, DataSerializers.BOOLEAN);
    @Unique
    private boolean orderedToSit;
    @Unique
    private boolean isSitting;


    @Shadow
    @Nullable
    public abstract LivingEntity getOwner();

    public WaitingZombieMixin(World level) { super(level); }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    protected void registerMoreGoals(CallbackInfo ci) {
        this.goalSelector.addGoal(1, new WaitGoal(this));
    }

    @NotNull
    public ActionResultType mobInteract(@NotNull PlayerEntity player, @NotNull Hand hand) {
        if (this.getOwner() == player) {
            if (player.level.isClientSide()) {
                return ActionResultType.SUCCESS;
            } else {
                setOrderedToSit(!isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget((LivingEntity) null);
                return ActionResultType.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!this.isInvulnerableTo(source)) {
            Entity entity = source.getEntity();
            this.setOrderedToSit(false);
            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                damage = (damage + 1.0F) / 2.0F;
            }
        }
        return super.hurt(source, damage);
    }

    @Override @Unique
    public boolean isOrderedToSit() {
        return orderedToSit;
    }

    @Override @Unique
    public void setOrderedToSit(boolean orderedToSit) {
        this.orderedToSit = orderedToSit;
    }

    @Override @Unique
    public boolean isInSittingPose() {
        return this.entityData.get(WAITING_DATA);
    }

    @Override @Unique
    public void setInSittingPose(boolean isInPose) {
        this.entityData.set(WAITING_DATA, isInPose);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addMoreData(CompoundNBT nbt, CallbackInfo ci) {
        nbt.putBoolean("Sitting", this.orderedToSit);
    }
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readMoreData(CompoundNBT nbt, CallbackInfo ci) {
        this.orderedToSit = nbt.getBoolean("Sitting");
        this.setInSittingPose(this.orderedToSit);
    }
    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    public void defineMoreSyncedData(CallbackInfo ci) {
        this.entityData.define(WAITING_DATA, false);
    }
}
