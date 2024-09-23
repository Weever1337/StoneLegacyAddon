package dikiy.weever.stone_legacy.util;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.capability.ZombieUtilCap;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID)
public class ZombieHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity hurtEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        Entity damageEntity = damageSource.getEntity();
        if (hurtEntity instanceof PlayerEntity && damageEntity instanceof LivingEntity) {
            PlayerEntity player = getPlayerFromDamageEntity(damageEntity);
            if (player != null && Objects.equals(player.getCapability(ZombieUtilProvider.CAPABILITY).map(ZombieUtilCap::getOwnerUUID).orElse(null), hurtEntity.getUUID()) && player != hurtEntity) {
                event.setAmount(0);
                event.setCanceled(true);
            }
        }
    }

    private static PlayerEntity getPlayerFromDamageEntity(Entity damageEntity) {
        return damageEntity instanceof StandEntity ? (PlayerEntity) ((StandEntity) damageEntity).getUser() : damageEntity instanceof PlayerEntity ? (PlayerEntity) damageEntity : null;
    }
}
