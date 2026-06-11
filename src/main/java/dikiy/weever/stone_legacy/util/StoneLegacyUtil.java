package dikiy.weever.stone_legacy.util;

import com.github.standobyte.jojo.block.PillarmanBossMultiBlock;
import com.github.standobyte.jojo.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

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

    public static void resetToRespawnPoint(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        if (server == null) return;

        RegistryKey<World> respawnDimension = player.getRespawnDimension();
        BlockPos respawnPos = player.getRespawnPosition();
        float respawnAngle = player.getRespawnAngle();
        boolean respawnForced = player.isRespawnForced();

        ServerWorld respawnWorld = server.getLevel(respawnDimension);
        Optional<Vector3d> respawnLocation = Optional.empty();
        if (respawnWorld != null && respawnPos != null) {
            respawnLocation = ServerPlayerEntity.findRespawnPositionAndUseSpawnBlock(respawnWorld, respawnPos, respawnAngle, respawnForced, true);
        }

        ServerWorld targetWorld = (respawnWorld != null && respawnLocation.isPresent()) ? respawnWorld : server.overworld();

        double x;
        double y;
        double z;
        float yaw = respawnAngle;
        if (respawnLocation.isPresent()) {
            Vector3d location = respawnLocation.get();
            x = location.x;
            y = location.y;
            z = location.z;
        }
        else {
            BlockPos sharedSpawn = targetWorld.getSharedSpawnPos();
            x = sharedSpawn.getX() + 0.5D;
            y = sharedSpawn.getY();
            z = sharedSpawn.getZ() + 0.5D;
            yaw = 0.0F;
        }

        player.teleportTo(targetWorld, x, y, z, yaw, 0.0F);
    }
}