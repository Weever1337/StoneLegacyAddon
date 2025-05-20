package dikiy.weever.stone_legacy.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StoneLegacyUtil
{
    public static boolean checkStoneFormationBehind(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return false;
        }
        return getStoneBlocksCounter(livingEntity) >= 6;
    }
    private static int getStoneBlocksCounter(LivingEntity livingEntity) {
        World world = livingEntity.level;
        BlockPos playerPos = new BlockPos(livingEntity.blockPosition());

        Direction horizontalFacing = Direction.fromYRot(livingEntity.yRot);
        Direction horizontalBehind = horizontalFacing.getOpposite();
        BlockPos anchorPos = playerPos.relative(horizontalBehind, 1);
        int stoneBlocksCounter = 0;
        for (int dy = 0; dy < 3; dy++) {
            for (int dSide = 0; dSide <= 1; dSide++) {
                BlockPos currentCheckPos;

                if (horizontalBehind.getAxis() == Direction.Axis.X) {
                    currentCheckPos = anchorPos.offset(0, dy, dSide - (livingEntity.getZ() - Math.round(livingEntity.getZ())));
                } else {
                    currentCheckPos = anchorPos.offset(dSide - (livingEntity.getX() - Math.round(livingEntity.getX())), dy, 0);
                }

                BlockState blockState = world.getBlockState(currentCheckPos);

                if (blockState.getMaterial() == Material.STONE) {
                    stoneBlocksCounter++;
                }
            }
        }
        return stoneBlocksCounter;
    }
    public static void replaceStoneWithSlumberingPillarman(LivingEntity living) {
        if (getStoneBlocksCounter(living) < 6) return;
        World world = living.level;
        BlockPos playerPos = new BlockPos(living.blockPosition());

        Direction horizontalFacing = Direction.fromYRot(living.yRot);
        Direction horizontalBehind = horizontalFacing.getOpposite();
        BlockPos anchorPos = playerPos.relative(horizontalBehind, 1);
        int stoneBlocksCounter = 0;
        for (int dy = 3; dy > 0; dy--) {
            for (int dSide = 1; dSide >= 0; dSide--) {
                BlockPos currentCheckPos;

                if (horizontalBehind.getAxis() == Direction.Axis.X) {
                    currentCheckPos = anchorPos.offset(0, dy, dSide - (living.getZ() - Math.round(living.getZ())));
                } else {
                    currentCheckPos = anchorPos.offset(dSide - (living.getX() - Math.round(living.getX())), dy, 0);
                }

                BlockState blockState = world.getBlockState(currentCheckPos);

                if (blockState.getMaterial() == Material.STONE) {
                    //place slumbering pillarman here with state from 0 to 5
                }
            }
        }
    }
}
