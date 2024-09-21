package dikiy.weever.stone_legacy.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("need to fix")
public class FruitItemRenderer extends ItemRenderer {

    public FruitItemRenderer(EntityRendererManager renderManager, Minecraft mc) {
        super(renderManager, mc.getItemRenderer());
    }

    @Override
    public void render(ItemEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        ItemStack stack = entity.getItem();
        byte stage = stack.getOrCreateTag().getByte("Stonefied");

        IBakedModel model;
        model = Minecraft.getInstance().getItemRenderer().getModel(stack, entity.level, null);

        matrixStack.pushPose();
        ItemCameraTransforms.TransformType transformType = ItemCameraTransforms.TransformType.GROUND;
        Minecraft.getInstance().getItemRenderer().render(stack, transformType, false, matrixStack, buffer, packedLight, 1, model);
        matrixStack.popPose();
    }
}