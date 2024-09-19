package dikiy.weever.stone_legacy;

import com.github.standobyte.jojo.capability.entity.LivingUtilCap;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID)
public class ZombieHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity hurtedEntity = event.getEntityLiving();
        if (event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getEntity();
            attacker.getCapability(ZombieUtilProvider.CAPABILITY).ifPresent(cap -> {
                if (cap.getOwnerUUID() != null && cap.getOwnerUUID().equals(hurtedEntity.getUUID())) {
                    System.out.println("OWNER DETECTED");
                    event.setAmount(0);
                    event.setCanceled(true);
                }
            });
        }
    }
}
