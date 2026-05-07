package dikiy.weever.stone_legacy.action.non_stand.vampirism;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.entity.mob.HungryZombieEntity;
import com.github.standobyte.jojo.init.ModEntityTypes;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import dikiy.weever.stone_legacy.mixin_helper.IZombiesReminder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VampirismRecallZombies extends VampirismAction {

    public VampirismRecallZombies(Builder builder) { super(builder); }

    @Override
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target, @Nullable PacketBuffer extraInput) {
        if (!world.isClientSide()) {
            if (power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).isPresent() &&
                power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get()  instanceof IZombiesReminder) {
                List<UUID> missingEntities = ((IZombiesReminder) power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get()).getOwnedZombies();
                for (ServerWorld dim : world.getServer().getAllLevels()) {
                    List<UUID> tmp = new ArrayList<>(missingEntities);
                    missingEntities.forEach(e -> { if (dim.getEntity(e) != null || dim.getEntity(e) instanceof PlayerEntity) tmp.remove(e); });
                    missingEntities = tmp;
                }
                missingEntities.forEach(e -> ((IZombiesReminder) power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get()).removeZombie(e));
                int zombies_count = ((IZombiesReminder) power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get()).getOwnedZombies().size();
                for (int i = 0; i < zombies_count; i++) {
                    LivingEntity zombie = (LivingEntity) ((ServerWorld) world).getEntity(((IZombiesReminder) power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get()).getOwnedZombies().get(i));
                    if (zombie != null) {
                        // zombie.moveTo(user.position());
                        //float rotation = (float) Math.PI / 180 * user.getViewYRot(1.0f);
                        Vector3d position = user.position();
                        position = position.add(Math.sqrt(4.0 * zombies_count / (2.0 * Math.PI)) * Math.cos(2 * Math.PI / zombies_count * i),
                                0,
                                Math.sqrt(4.0 * zombies_count / (2.0 * Math.PI)) * Math.sin(2 * Math.PI / zombies_count * i));
//                        BlockPos blockPos = new BlockPos(position);
                        // tf this doesn't work
//                        while (!world.getBlockState(blockPos).isPathfindable(world, blockPos, PathType.LAND)) {
//                            blockPos = blockPos.above();
//                        }
//                        while (world.getBlockState(blockPos).isPathfindable(world, blockPos, PathType.LAND)) {
//                            blockPos = blockPos.below();
//                        }
//                        position.add(0, blockPos.getY() + world.getBlockState(blockPos).getCollisionShape(world, blockPos).bounds().maxY - position.y, 0);
                        zombie.moveTo(position);
                    }
                }
            }
        }
    }
}
