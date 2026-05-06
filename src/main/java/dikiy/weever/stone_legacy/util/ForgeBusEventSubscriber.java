package dikiy.weever.stone_legacy.util;

import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.command.ZombieOwnerCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StoneLegacyAddon.MOD_ID)
public class ForgeBusEventSubscriber {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        ZombieOwnerCommand.register(event.getDispatcher());
    }
}
