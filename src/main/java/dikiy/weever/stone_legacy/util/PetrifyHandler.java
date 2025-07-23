package dikiy.weever.stone_legacy.util;

import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.google.common.collect.Maps;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID)
public class PetrifyHandler {
    private static final Map<UUID, Long> petrifyUsers = Maps.newHashMap();

    public static void addPetrifyUser(UUID uuid, long milliseconds){
        if (petrifyUsers.containsKey(uuid)) return;
        petrifyUsers.put(uuid, milliseconds);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (!player.level.isClientSide && petrifyUsers.containsKey(player.getUUID())) {
            if (System.currentTimeMillis() >= petrifyUsers.get(player.getUUID())) {
                INonStandPower.getNonStandPowerOptional(player).ifPresent(INonStandPower -> {
                    if (INonStandPower.getType() == ModPowers.PILLAR_MAN.get()) {
                        INonStandPower.clear();
                        petrifyUsers.remove(player.getUUID());
                    }
                });
            }
        }
    }
}
