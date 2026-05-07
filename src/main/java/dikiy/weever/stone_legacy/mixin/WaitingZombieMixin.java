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
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
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

@Mixin(value = HungryZombieEntity.class)
public abstract class WaitingZombieMixin implements IWaitableEntity {
    @Final @Unique
    private static final DataParameter<Boolean> WAITING_DATA = EntityDataManager.defineId(HungryZombieEntity.class, DataSerializers.BOOLEAN);
    @Unique
    private boolean orderedToSit = false;
    @Unique
    private boolean isSitting = false;


    @Shadow
    @Nullable
    public abstract LivingEntity getOwner();


    @Inject(method = "registerGoals", at = @At("TAIL"))
    protected void registerMoreGoals(CallbackInfo ci) {
        ((HungryZombieEntity)((Object)this)).goalSelector.addGoal(1, new WaitGoal(this));
    }

    @NotNull
    public ActionResultType mobInteract(@NotNull PlayerEntity player, @NotNull Hand hand) {
        if (this.getOwner() == player) {
            if (player.level.isClientSide()) {
                return ActionResultType.SUCCESS;
            } else {
                if (player.getItemInHand(hand).getItem() instanceof ToolItem || player.getItemInHand(hand).getItem() instanceof SwordItem) {
                    ItemStack oldStack = ((HungryZombieEntity)((Object)this)).getItemInHand(Hand.MAIN_HAND).getStack();
                    ItemStack newStack = player.getItemInHand(hand).getStack();
                    ((HungryZombieEntity)((Object)this)).setItemInHand(hand, newStack);
                    if (!(oldStack.isEmpty() && player.abilities.instabuild))
                        player.setItemInHand(hand, oldStack);
                } else if (player.getItemInHand(hand).getItem() instanceof ArmorItem) {
                    ItemStack newStack = player.getItemInHand(hand).getStack();
                    EquipmentSlotType slot = ((ArmorItem)player.getItemInHand(hand).getItem()).getSlot();
                    ItemStack oldStack = ((HungryZombieEntity)((Object)this)).getItemBySlot(slot);
                    ((HungryZombieEntity)((Object)this)).setItemSlot(slot, newStack);
                    if (!(oldStack.isEmpty() && player.abilities.instabuild))
                        player.setItemInHand(hand, oldStack);
                } else if (player.getItemInHand(hand).isEmpty() && player.isShiftKeyDown()) {
                    ((HungryZombieEntity)((Object)this)).getAllSlots().forEach(itemStack -> {
                        ItemEntity item = new ItemEntity(((HungryZombieEntity)((Object)this)).level, ((HungryZombieEntity)((Object)this)).getX(), ((HungryZombieEntity)((Object)this)).getEyeHeight() + ((HungryZombieEntity)((Object)this)).getY(), ((HungryZombieEntity)((Object)this)).getZ());
                        item.setItem(itemStack);
                        ((ServerWorld)player.level).addFreshEntity(item);
                    });
                    ((HungryZombieEntity)((Object)this)).setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.AIR));
                    ((HungryZombieEntity)((Object)this)).setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));
                    ((HungryZombieEntity)((Object)this)).setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.AIR));
                    ((HungryZombieEntity)((Object)this)).setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.AIR));
                    ((HungryZombieEntity)((Object)this)).setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.AIR));
                    ((HungryZombieEntity)((Object)this)).setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.AIR));
                } else  {
                    INonStandPower.getNonStandPowerOptional(player).ifPresent(p -> {
                        p.getTypeSpecificData(ModPowers.VAMPIRISM.get()).ifPresent(data -> {
                            if (data instanceof IZombiesReminder && !((IZombiesReminder) data).getOwnedZombies().contains(((HungryZombieEntity)((Object)this)).getUUID()))
                                ((IZombiesReminder) data).addZombie(((HungryZombieEntity)((Object)this)));
                        });
                    });
                    setOrderedToSit(!isOrderedToSit());
                    ((HungryZombieEntity)((Object)this)).jumping = false;
                    ((HungryZombieEntity)((Object)this)).navigation.stop();
                    ((HungryZombieEntity)((Object)this)).setTarget((LivingEntity) null);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ((HungryZombieEntity)((Object)this)).mobInteract(player, hand);
    }

    public boolean hurt(DamageSource source, float damage) {
        if (!((HungryZombieEntity)((Object)this)).isInvulnerableTo(source) && source.getEntity() != null) {
            Entity entity = source.getEntity();
            for (HungryZombieEntity zombie : MCUtil.entitiesAround(HungryZombieEntity.class, ((HungryZombieEntity)((Object)this)),
                    ((HungryZombieEntity)((Object)this)).getAttributeValue(Attributes.FOLLOW_RANGE), true, e -> e.getOwner() == this.getOwner() && e instanceof IWaitableEntity)) {
                ((IWaitableEntity)zombie).setOrderedToSit(false);
            }
            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                damage = (damage + 1.0F) / 2.0F;
            }
        }
        return ((HungryZombieEntity)((Object)this)).hurt(source, damage);
    }

    public boolean removeWhenFarAway(double distanceFromPlayer) {
        return false;
    }

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
        return ((HungryZombieEntity)((Object)this)).entityData.get(WAITING_DATA);
    }

    @Override @Unique
    public void setInSittingPose(boolean isInPose) {
        ((HungryZombieEntity)((Object)this)).entityData.set(WAITING_DATA, isInPose);
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
        ((HungryZombieEntity)((Object)this)).entityData.define(WAITING_DATA, false);
    }
    @Shadow @Final static DataParameter<Optional<UUID>> OWNER_UUID;
    @Shadow
    protected abstract void setOwnerUUID(@Nullable UUID uuid);
    @Shadow
    public abstract void setOwner(LivingEntity owner);
}
