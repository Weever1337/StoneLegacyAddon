package dikiy.weever.stone_legacy;

import dikiy.weever.stone_legacy.init.InitItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(StoneLegacyAddon.MOD_ID)
public class StoneLegacyAddon {
    public static final String MOD_ID = "stone_legacy";
    public static final Logger LOGGER = LogManager.getLogger();

    public StoneLegacyAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitItems.ITEMS.register(modEventBus);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}