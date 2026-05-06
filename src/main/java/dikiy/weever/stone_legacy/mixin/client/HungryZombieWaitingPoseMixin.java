package dikiy.weever.stone_legacy.mixin.client;

import dikiy.weever.stone_legacy.mixin_helper.IWaitableEntity;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.renderer.model.ModelHelper.bobArms;

@Mixin(value = AbstractZombieModel.class)
public abstract class HungryZombieWaitingPoseMixin<T extends MonsterEntity> extends BipedModel<T> {

    public HungryZombieWaitingPoseMixin(float p_i1148_1_) { super(p_i1148_1_); }

    @Inject(method = "setupAnim(Lnet/minecraft/entity/monster/MonsterEntity;FFFFF)V", cancellable = true, at = @At(value = "INVOKE", shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/client/renderer/model/ModelHelper;animateZombieArms(Lnet/minecraft/client/renderer/model/ModelRenderer;Lnet/minecraft/client/renderer/model/ModelRenderer;ZFF)V"))
    public void setupAnim(T entity, float swing, float swing_amount, float bob, float head_yaw, float head_pitch, CallbackInfo ci) {
        if (entity instanceof IWaitableEntity && ((IWaitableEntity) entity).isInSittingPose()) {
            animateWaiting(this.leftArm, this.rightArm, this.attackTime, bob);
            ci.cancel();
        }
    }

    @Unique
    private static void animateWaiting(ModelRenderer left_arm, ModelRenderer right_arm, float attack_time, float bob) {
        float f = MathHelper.sin(attack_time * (float)Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - attack_time) * (1.0F - attack_time)) * (float)Math.PI);
        right_arm.zRot = 0.0F;
        left_arm.zRot = 0.0F;
        right_arm.yRot = -(0.1F - f * 0.6F);
        left_arm.yRot = 0.1F - f * 0.6F;
        float f2 = -(float)Math.PI / 3.375f;
        right_arm.xRot = f2;
        left_arm.xRot = f2;
        right_arm.xRot += f * 1.2F - f1 * 0.4F;
        left_arm.xRot += f * 1.2F - f1 * 0.4F;
        bobArms(right_arm, left_arm, bob);
    }
}
