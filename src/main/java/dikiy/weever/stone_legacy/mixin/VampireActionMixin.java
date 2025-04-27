package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.action.non_stand.VampirismZombieSummon;
import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import com.github.standobyte.jojo.init.ModCustomStats;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import dikiy.weever.stone_legacy.mixin_helper.INonStandPowerMixinHelper;
import dikiy.weever.stone_legacy.network.AddonPackets;
import dikiy.weever.stone_legacy.network.server.TrResetDeathTimePacket;
import dikiy.weever.stone_legacy.network.server.TrSyncGivePowerDataPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(VampirismZombieSummon.class)
public class VampireActionMixin extends VampirismAction {
    public VampireActionMixin(Builder builder) {
        super(builder);
    }

    /**
     * @author Weever
     * @reason because zombie power!!!
     */
    @Overwrite
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget targets) {
        if (!world.isClientSide()) {
            List<LivingEntity> entitiesAround = MCUtil.entitiesAround(LivingEntity.class, user, 16, false, null);
            int canSpawn = world.getDifficulty().getId();
            AtomicInteger spawned = new AtomicInteger();
            entitiesAround.forEach(entity -> {
                if (spawned.get() < canSpawn) {
                    if (entity.deathTime >= 1) {
                        INonStandPower.getNonStandPowerOptional(entity).ifPresent(nonPower -> {
                            if (nonPower.getType() == ModPowers.HAMON.get() && nonPower.getEnergy() < 5) {
                                stoneLegacyAddon$giveZombie(nonPower, user.getUUID());
                                spawned.getAndIncrement();
                            } else {
                                stoneLegacyAddon$giveZombie(nonPower, user.getUUID());
                                spawned.getAndIncrement();
                            }
                        });
                    }
                }
            });
            if (spawned.get() < canSpawn) {
                for (int i = spawned.get(); i < canSpawn; ++i) {
                    HungryZombieEntity zombie = new HungryZombieEntity(world);
                    zombie.setSummonedFromAbility();
                    zombie.copyPosition(user);
                    zombie.setOwner(user);
                    world.addFreshEntity(zombie);
                }
                if (user instanceof ServerPlayerEntity) {
                    ((ServerPlayerEntity) user).awardStat(ModCustomStats.VAMPIRE_ZOMBIES_SUMMONED, canSpawn - spawned.get());
                }
            }
        }
    }

    @Unique
    private void stoneLegacyAddon$giveZombie(INonStandPower nonPower, UUID ownerUUID) {
        LivingEntity user = nonPower.getUser();
        user.getCapability(ZombieUtilProvider.CAPABILITY).ifPresent(cap -> cap.setOwnerUUID(ownerUUID));
        float previousEnergy = nonPower.getEnergy();
        float previousMaxEnergy = nonPower.getMaxEnergy();
        if (nonPower instanceof INonStandPowerMixinHelper) {
            ((INonStandPowerMixinHelper) nonPower).stoneLegacyAddon$givePower(ModPowers.ZOMBIE.get(), true);
            if (!user.level.isClientSide) {
                AddonPackets.sendToClientsTrackingAndSelf(new TrSyncGivePowerDataPacket(user.getId(), ModPowers.ZOMBIE.get().getRegistryName(), true), user);
            }
//            nonPower.clear()
//            nonPower.givePower(ModPowers.ZOMBIE.get())  ;
            nonPower.setEnergy((float) (Math.pow(1.6 * previousEnergy, 1.8) / Math.pow(previousMaxEnergy, 2)));
            stoneLegacyAddon$onEntityResurrect(user);
            user.setHealth(2f);
        }
    }

    @Unique
    private void stoneLegacyAddon$onEntityResurrect(LivingEntity entity) {
        if (entity.deathTime >= 1) {
            entity.deathTime = 0;
            AddonPackets.sendToClientsTrackingAndSelf(new TrResetDeathTimePacket(entity.getId()), entity);
        }
    }
}
