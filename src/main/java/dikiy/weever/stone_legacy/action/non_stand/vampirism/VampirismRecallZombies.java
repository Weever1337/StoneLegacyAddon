package dikiy.weever.stone_legacy.action.non_stand.vampirism;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismAction;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import dikiy.weever.stone_legacy.mixin_helper.IZombiesReminder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
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
                List<UUID> reachable_zombies = removeNullEntities(world, power);
                for (int i = 0; i < reachable_zombies.size(); i++) {
                    LivingEntity zombie = (LivingEntity) ((ServerWorld) world).getEntity((reachable_zombies.get(i)));
                    if (zombie != null) {
                        Vector3d position = user.position();
                        double offset_x = Math.sqrt(4.0 * reachable_zombies.size() / (2.0 * Math.PI)) * Math.cos(2 * Math.PI / reachable_zombies.size() * i);
                        double offset_z = Math.sqrt(4.0 * reachable_zombies.size() / (2.0 * Math.PI)) * Math.sin(2 * Math.PI / reachable_zombies.size() * i);
                        position = position.add(offset_x, 0, offset_z);
                        zombie.moveTo(position);
                    }
                }
            }
        }
    }

    private List<UUID> removeNullEntities(World world, INonStandPower power) {
        if (power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).map(d -> ((IZombiesReminder)d).getOwnedZombies()).isPresent()) {
            List<UUID> missingEntities = ((IZombiesReminder) power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).get()).getOwnedZombies();
            for (ServerWorld dim : world.getServer().getAllLevels()) {
                List<UUID> tmp = new ArrayList<>(missingEntities);
                missingEntities.forEach(e -> {
                    if (dim.getEntity(e) != null || dim.getEntity(e) instanceof PlayerEntity) tmp.remove(e);
                });
                missingEntities = tmp;
            }
            List<UUID> result = new ArrayList<>(power.getTypeSpecificData(ModPowers.VAMPIRISM.get()).map(d -> ((IZombiesReminder) d).getOwnedZombies()).get());
            missingEntities.forEach(result::remove);
            return result;
        }
        return new ArrayList<>();
    }
}
