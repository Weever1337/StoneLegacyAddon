package dikiy.weever.stone_legacy.util;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.capability.ZombieUtilProvider;
import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import dikiy.weever.stone_legacy.mixin_helper.IZombiesReminder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID)
public class GameplayEventHandler {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void removeZombiesFromList(LivingDeathEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living instanceof IWaitableEntity) {
            LivingEntity owner = ((IWaitableEntity) living).getOwner();
            INonStandPower.getNonStandPowerOptional(owner).ifPresent(ownerPower -> {
                if (ownerPower.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get() instanceof IZombiesReminder) {
                    ((IZombiesReminder) ownerPower.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get()).removeZombie(living);
                }
            });
        }
    }
}
