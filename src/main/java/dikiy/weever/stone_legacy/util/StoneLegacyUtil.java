package dikiy.weever.stone_legacy.util;

import com.github.standobyte.jojo.block.PillarmanBossMultiBlock;
import com.github.standobyte.jojo.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StoneLegacyUtil {

    public static boolean checkStoneFormationBehind(LivingEntity livingEntity) {
        if (livingEntity == null) {
            return false;
        }
        return getStoneBlocksCounter(livingEntity) >= 6;
    }

    private static int getStoneBlocksCounter(LivingEntity livingEntity) {
        World world = livingEntity.level;
        Direction playerFacing = livingEntity.getDirection();
        Direction behind = playerFacing.getOpposite();
        Direction leftOfPlayer = playerFacing.getCounterClockWise();
        BlockPos bottomLeftPos = livingEntity.blockPosition().relative(behind).relative(leftOfPlayer);

        int stoneBlocksCounter = 0;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                BlockPos posToCheck = bottomLeftPos.above(j).relative(playerFacing.getClockWise(), i);
                if (world.getBlockState(posToCheck).getMaterial() == Material.STONE) {
                    stoneBlocksCounter++;
                }
            }
        }
        return stoneBlocksCounter;
    }

    public static void replaceStoneWithSlumberingPillarman(LivingEntity living) {
        if (!checkStoneFormationBehind(living)) return;

        World world = living.level;
        Direction playerFacing = living.getDirection();
        Direction statueFacing = playerFacing.getOpposite();
        Direction behind = playerFacing.getOpposite();
        Direction leftOfPlayer = playerFacing.getCounterClockWise();
        BlockPos bottomLeftPos = living.blockPosition().relative(behind).relative(leftOfPlayer);

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                BlockPos posToReplace = bottomLeftPos.above(j).relative(playerFacing.getClockWise(), i);

                int yIndexInStructure = 2 - j;
                int horizontalPartId = 1 - i;
                int partId = yIndexInStructure * 2 + horizontalPartId;

                BlockState pillarmanState = ModBlocks.SLUMBERING_PILLARMAN.get().defaultBlockState()
                        .setValue(PillarmanBossMultiBlock.FACING, statueFacing)
                        .setValue(PillarmanBossMultiBlock.PART, partId);

                if (world.getBlockState(posToReplace).getMaterial() == Material.STONE) {
                    world.setBlock(posToReplace, pillarmanState, 3);
                    world.getChunkSource().getLightEngine().checkBlock(posToReplace);
                }
            }
        }
    }
}