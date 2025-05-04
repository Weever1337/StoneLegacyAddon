package dikiy.weever.stone_legacy.action.non_stand.pillarman;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.PillarmanStoneForm;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import dikiy.weever.stone_legacy.util.StoneLegacyUtil;
import net.minecraft.entity.LivingEntity;

public class PillarmanPetrify extends PillarmanStoneForm
{
    public PillarmanPetrify(Builder builder)
    {
        super(builder);
    }

    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, INonStandPower power, ActionTarget target)
    {
        System.out.println(StoneLegacyUtil.checkStoneFormationBehind(user));
        return ActionConditionResult.noMessage(StoneLegacyUtil.checkStoneFormationBehind(user));
    }
}
