package dikiy.weever.stone_legacy;

import dikiy.weever.stone_legacy.capability.CapabilityHandler;
import dikiy.weever.stone_legacy.init.AddonLootModifierSerializers;
import dikiy.weever.stone_legacy.init.InitActions;
import dikiy.weever.stone_legacy.init.InitBlocks;
import dikiy.weever.stone_legacy.init.InitItems;
import dikiy.weever.stone_legacy.network.AddonPackets;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(StoneLegacyAddon.MOD_ID)
public class StoneLegacyAddon {
    public static final String MOD_ID = "stone_legacy";
    public static final Logger LOGGER = LogManager.getLogger();

    public StoneLegacyAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        initVanillaRegistries(modEventBus);

        modEventBus.addListener(this::preInit);
    }
    private void initVanillaRegistries(IEventBus modEventBus) {
        InitActions.ACTIONS.register(modEventBus);
        InitBlocks.BLOCKS.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);
        AddonLootModifierSerializers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
    }

    private void preInit(FMLCommonSetupEvent event) {
        AddonPackets.init();
        CapabilityHandler.registerCapabilities();
    }
}