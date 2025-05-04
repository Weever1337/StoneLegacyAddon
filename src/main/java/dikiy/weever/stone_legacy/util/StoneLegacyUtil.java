package dikiy.weever.stone_legacy.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class StoneLegacyUtil
{
    public static boolean checkStoneFormationBehind(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return false;
        }

        World world = livingEntity.level;
        BlockPos playerPos = new BlockPos(livingEntity.blockPosition());

        Direction horizontalFacing = Direction.fromYRot(livingEntity.yRot);
        Direction horizontalBehind = horizontalFacing.getOpposite();

        BlockPos anchorPos = playerPos.relative(horizontalBehind, 1);
        int stoneBlocksCounter = 0;
        for (int dy = 0; dy < 3; dy++) {
            for (int dSide = -1; dSide <= 1; dSide++) {

                BlockPos currentCheckPos;

                if (horizontalBehind.getAxis() == Direction.Axis.X) {
                    currentCheckPos = anchorPos.offset(0, dy, dSide);
                } else {
                    currentCheckPos = anchorPos.offset(dSide, dy, 0);
                }

                BlockState blockState = world.getBlockState(currentCheckPos);

                if (blockState.is(Blocks.STONE)) {
                    stoneBlocksCounter++;
                }
            }
        }

        return stoneBlocksCounter >= 6;
    }
}
