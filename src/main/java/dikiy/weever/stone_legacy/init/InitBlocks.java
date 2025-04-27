package dikiy.weever.stone_legacy.init;

import com.github.standobyte.jojo.JojoMod;
import com.github.standobyte.jojo.block.StoneMaskBlock;
import dikiy.weever.stone_legacy.StoneLegacyAddon;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, StoneLegacyAddon.MOD_ID);

    public static final RegistryObject<StoneMaskBlock> AJA_STONE_MASK_PREDICTED_HEAT = BLOCKS.register("aja_stone_mask_predicted_heat",
            () -> new StoneMaskBlock(Block.Properties.copy(Blocks.STONE).harvestLevel(0).requiresCorrectToolForDrops().noCollission().isValidSpawn((state, reader, pos, entityType) -> false)));
    public static final RegistryObject<StoneMaskBlock> AJA_STONE_MASK_PREDICTED_LIGHT = BLOCKS.register("aja_stone_mask_predicted_light",
            () -> new StoneMaskBlock(Block.Properties.copy(Blocks.STONE).harvestLevel(0).requiresCorrectToolForDrops().noCollission().isValidSpawn((state, reader, pos, entityType) -> false)));
    public static final RegistryObject<StoneMaskBlock> AJA_STONE_MASK_PREDICTED_WIND = BLOCKS.register("aja_stone_mask_predicted_wind",
            () -> new StoneMaskBlock(Block.Properties.copy(Blocks.STONE).harvestLevel(0).requiresCorrectToolForDrops().noCollission().isValidSpawn((state, reader, pos, entityType) -> false)));
}
