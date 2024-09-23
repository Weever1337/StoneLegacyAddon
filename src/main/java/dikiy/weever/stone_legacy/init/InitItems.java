package dikiy.weever.stone_legacy.init;

import com.github.standobyte.jojo.JojoMod;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import dikiy.weever.stone_legacy.items.FruitItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StoneLegacyAddon.MOD_ID);

    public static final RegistryObject<FruitItem> FRUIT = ITEMS.register("fruit",
            () -> new FruitItem(new Item.Properties().stacksTo(1).tab(JojoMod.MAIN_TAB).rarity(Rarity.RARE)
                    .food(new Food.Builder().saturationMod(6).nutrition(4).alwaysEat().build())));
}
