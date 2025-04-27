package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.block.StoneMaskBlock;
import com.github.standobyte.jojo.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFaceBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = StoneMaskBlock.class, remap = false)
public abstract class StoneMaskBlockMixin extends HorizontalFaceBlock {
    public StoneMaskBlockMixin(Properties properties) { super(properties); }
    @Override
    public boolean hasTileEntity(BlockState state) {
        return (Object) this == ModBlocks.STONE_MASK.get();
    }
}
