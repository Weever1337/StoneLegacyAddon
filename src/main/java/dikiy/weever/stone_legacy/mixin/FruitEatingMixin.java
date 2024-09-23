package dikiy.weever.stone_legacy.mixin;

import com.github.standobyte.jojo.init.ModStatusEffects;
import dikiy.weever.stone_legacy.init.InitItems;
import dikiy.weever.stone_legacy.items.FruitItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class FruitEatingMixin extends Entity {
    @Shadow public abstract boolean isUsingItem();
    @Shadow public abstract void spawnItemParticles(ItemStack stack, int i);

    @Shadow public abstract boolean addEffect(EffectInstance p_195064_1_);

    @Shadow @Nullable public abstract EffectInstance getEffect(Effect p_70660_1_);

    public FruitEatingMixin(EntityType<?> p_i48580_1_, World p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    @Inject(method = "triggerItemUseEffects(Lnet/minecraft/item/ItemStack;I)V", at = @At(value = "HEAD", target = "Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/LivingEntity.isUsingItem()Z"), cancellable = true)
    public void getHurtSoundAndAnimation(ItemStack stack, int randomValue, CallbackInfo ci) {
        if (stack.getItem() == InitItems.FRUIT.get() && this.isUsingItem() && FruitItem.getStage(stack) == 630) {
            this.spawnItemParticles(stack, 1);
            if (!(this.getEffect(Effects.DAMAGE_RESISTANCE) != null || (this.getEntity() instanceof PlayerEntity && ((PlayerEntity) this.getEntity()).isCreative()))) {
                this.playSound(SoundEvents.PLAYER_HURT, 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                int amplifier = this.getEffect(ModStatusEffects.BLEEDING.get()) != null ? this.getEffect(ModStatusEffects.BLEEDING.get()).getAmplifier() + 1 : 0;
                this.addEffect(new EffectInstance(ModStatusEffects.BLEEDING.get(), 45, amplifier, false, false, true));
                ci.cancel();
            }
        }
    }

    @Inject(method="spawnItemParticles(Lnet/minecraft/item/ItemStack;I)V", at = @At(value = "HEAD"), cancellable = true)
    public void spawnTeethParticles(ItemStack stack, int particles, CallbackInfo ci) {
        if (stack.getItem() instanceof FruitItem && FruitItem.getStage(stack) == 630) {
            for(int i = 0; i < particles; ++i) {
                Vector3d vector3d = new Vector3d(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                vector3d = vector3d.xRot(-this.xRot * ((float)Math.PI / 180F));
                vector3d = vector3d.yRot(-this.yRot * ((float)Math.PI / 180F));
                double d0 = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
                Vector3d vector3d1 = new Vector3d(((double)this.random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
                vector3d1 = vector3d1.xRot(-this.xRot * ((float)Math.PI / 180F));
                vector3d1 = vector3d1.yRot(-this.yRot * ((float)Math.PI / 180F));
                vector3d1 = vector3d1.add(this.getX(), this.getEyeY(), this.getZ());
                if (this.level instanceof ServerWorld) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
                    ((ServerWorld)this.level).sendParticles(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.STONE)), vector3d1.x, vector3d1.y, vector3d1.z, 1, vector3d.x, vector3d.y + 0.05D, vector3d.z, 0.0D);
                else
                    this.level.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(Items.STONE)), vector3d1.x, vector3d1.y, vector3d1.z, vector3d.x, vector3d.y + 0.05D, vector3d.z);
            }
            ci.cancel();
        }
    }
}
