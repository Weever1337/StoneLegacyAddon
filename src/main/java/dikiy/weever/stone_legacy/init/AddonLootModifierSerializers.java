package dikiy.weever.stone_legacy.init;

import com.github.standobyte.jojo.util.mc.loot.AdditionalSingleItemLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class AddonLootModifierSerializers {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS;
    public static final RegistryObject<AdditionalSingleItemLootModifier.Serializer> ADDITIONAL_SINGLE_ITEM;

    static {
        LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, "stone_legacy");
        ADDITIONAL_SINGLE_ITEM = LOOT_MODIFIER_SERIALIZERS.register("additional_single_item", AdditionalSingleItemLootModifier.Serializer::new);
    }

    public AddonLootModifierSerializers() {
    }
}
