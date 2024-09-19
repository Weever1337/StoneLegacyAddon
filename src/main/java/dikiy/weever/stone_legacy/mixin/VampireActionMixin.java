package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.action.non_stand.VampirismZombieSummon;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(VampirismZombieSummon.class)
public class VampireActionMixin extends VampirismAction {
    public VampireActionMixin(Builder builder) {
        super(builder);
    }

    @Inject(method = "perform(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lcom/github/standobyte/jojo/power/impl/nonstand/INonStandPower;Lcom/github/standobyte/jojo/action/ActionTarget;)V", at = @At("HEAD"), cancellable = true)
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target, CallbackInfo ci) {
        if (!world.isClientSide()) {
            List<LivingEntity> entitiesAround = MCUtil.entitiesAround(LivingEntity.class, user, 16, false, null);
            entitiesAround.addAll(stoneLegacyAddon$playersAround(user, 16));
            AtomicInteger spawned = new AtomicInteger();
            int canSpawned = world.getDifficulty().getId();
            entitiesAround.forEach(entity -> {
                if (spawned.get() <= canSpawned) {
                    if (entity.deathTime >= 1) {
                        INonStandPower.getNonStandPowerOptional(entity).ifPresent(nonPower -> {
                            if (nonPower.getType() == ModPowers.HAMON.get()) {
                                if (nonPower.getEnergy() < 5) {
                                    stoneLegacyAddon$giveZombie(nonPower, user.getUUID());
                                    spawned.getAndIncrement();
                                }
                            } else {
                                stoneLegacyAddon$giveZombie(nonPower, user.getUUID());
                                spawned.getAndIncrement();
                            }
                        });
                    }
                }
            });
        }
    }

    @Unique
    private void stoneLegacyAddon$giveZombie(INonStandPower nonPower, UUID ownerUUID) {
        LivingEntity user = nonPower.getUser();
        if (user.isDeadOrDying()) {
            if (user instanceof ServerPlayerEntity) {
                System.out.println("x: " + user.getX() + " y: " + user.getY() + " z: " + user.getZ());
                user.revive();
                System.out.println("x: " + user.getX() + " y: " + user.getY() + " z: " + user.getZ());
            }
        }
        MCUtil.onEntityResurrect(user);
        user.setHealth(2f);
        nonPower.clear();
        nonPower.givePower(ModPowers.ZOMBIE.get());
        System.out.println("give zombie");
        user.getCapability(ZombieUtilProvider.CAPABILITY).ifPresent(cap -> {
            System.out.println("ownerUUID: " + ownerUUID.toString());
            cap.setOwnerUUID(ownerUUID);
            System.out.println("ownerUUID: " + cap.getOwnerUUID().toString());
        });
    }

    @Unique
    private List<ServerPlayerEntity> stoneLegacyAddon$playersAround(LivingEntity user, int range) {
        List<ServerPlayerEntity> players = new ArrayList<>();
        if (user.getServer() == null || user.getServer().getPlayerList().getPlayers().isEmpty()) return players;
        for (ServerPlayerEntity player : user.getServer().getPlayerList().getPlayers()) {
            if (player.distanceTo(user) <= range) {
                players.add(player);
            }
        }
        return players;
    }
}
