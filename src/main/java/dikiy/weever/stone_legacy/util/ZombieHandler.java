package dikiy.weever.stone_legacy.util;

import com.github.standobyte.jojo.power.impl.stand.StandUtil;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID)
public class ZombieHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onHurt(LivingAttackEvent event) {
        LivingEntity target = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        Entity damaging = damageSource.getEntity();
        if (target instanceof PlayerEntity && damaging instanceof LivingEntity) {
            PlayerEntity player = getPlayerFromDamageEntity(damaging);
            if (player == null || player == target) {
                return;
            }
            damaging.getCapability(ZombieUtilProvider.CAPABILITY).ifPresent(cap -> {
                if (target.getUUID().equals(cap.getOwnerUUID())) {
                    event.setCanceled(true);
                }});
        }
    }

    private static PlayerEntity getPlayerFromDamageEntity(Entity damageEntity) {
        if (damageEntity instanceof LivingEntity) {
            LivingEntity damageLiving = StandUtil.getStandUser((LivingEntity) damageEntity);
            if (damageLiving instanceof PlayerEntity) {
                return (PlayerEntity) damageLiving;
            }
        }
        return null;
    }
}
