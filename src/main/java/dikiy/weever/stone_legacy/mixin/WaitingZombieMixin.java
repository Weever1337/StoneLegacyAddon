package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import dikiy.weever.stone_legacy.mixin_helper.IZombiesReminder;
import dikiy.weever.stone_legacy.mixin_helper.WaitGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
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
import java.util.Optional;
import java.util.UUID;

@Mixin(value = HungryZombieEntity.class, remap = false)
public abstract class WaitingZombieMixin extends ZombieEntity implements IWaitableEntity {
    @Final @Unique
    private static final DataParameter<Boolean> WAITING_DATA = EntityDataManager.defineId(WaitingZombieMixin.class, DataSerializers.BOOLEAN);
    @Unique
    private boolean orderedToSit = false;
    @Unique
    private boolean isSitting = false;


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
                INonStandPower.getNonStandPowerOptional(player).ifPresent(p -> {
                    p.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> {
                        if (data instanceof IZombiesReminder && !((IZombiesReminder) data).getOwnedZombies().contains(this.getUUID()))
                            ((IZombiesReminder) data).addZombie(this);
                    });
                });
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
        if (!this.isInvulnerableTo(source) && source.getEntity() != null) {
            Entity entity = source.getEntity();
            for (HungryZombieEntity zombie : MCUtil.entitiesAround(HungryZombieEntity.class, this,
                    this.getAttributeValue(Attributes.FOLLOW_RANGE), true, e -> e.getOwner() == this.getOwner() && e instanceof IWaitableEntity)) {
                ((IWaitableEntity)zombie).setOrderedToSit(false);
            }
            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                damage = (damage + 1.0F) / 2.0F;
            }
        }
        return super.hurt(source, damage);
    }

    @Override
    public boolean removeWhenFarAway(double distanceFromPlayer) {
        return false;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
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
    @Shadow @Final static DataParameter<Optional<UUID>> OWNER_UUID;
    @Shadow
    private void setOwnerUUID(@Nullable UUID uuid) {
        entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }
    @Shadow
    public void setOwner(LivingEntity owner) {
        setOwnerUUID(owner != null ? owner.getUUID() : null);
    }
}
