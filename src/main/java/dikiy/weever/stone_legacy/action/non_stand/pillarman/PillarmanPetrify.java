package dikiy.weever.stone_legacy.action.non_stand.pillarman;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.PillarmanStoneForm;
import com.github.standobyte.jojo.power.IPower;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import dikiy.weever.stone_legacy.util.StoneLegacyUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PillarmanPetrify extends PillarmanStoneForm {
    public PillarmanPetrify(Builder builder) {
        super(builder);
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, INonStandPower power, ActionTarget target) {
        return ActionConditionResult.noMessage(StoneLegacyUtil.checkStoneFormationBehind(user));
    }

    @Override
    public void appendWarnings(List<ITextComponent> warnings, INonStandPower power, PlayerEntity clientPlayerUser) {
        warnings.add(new TranslationTextComponent("stone_legacy.pillarman_petrify.warning"));
    }

    @Override
    protected void perform(World world, LivingEntity user, INonStandPower power, ActionTarget target) {
        if (!user.level.isClientSide() && user instanceof ServerPlayerEntity)
        {
            ServerPlayerEntity player = (ServerPlayerEntity) user;
            Optional<Vector3d> pos = PlayerEntity.findRespawnPositionAndUseSpawnBlock((ServerWorld) player.level, Objects.requireNonNull(player.getRespawnPosition()), player.getRespawnAngle(), player.isRespawnForced(), true);
            if (pos.isPresent()) {
                float angle = player.getRespawnAngle();
                player.moveTo(pos.get().x, pos.get().y, pos.get().z, angle, 0);
//                player.setPlayerInput();
            } else {
                return;
            }
        }
        power.clear();
    }

    @Override
    public boolean greenSelection(INonStandPower power, ActionConditionResult conditionCheck) { return false; }
}
