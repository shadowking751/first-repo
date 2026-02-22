package com.yourname.gunmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.entity.BulletEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LaserBulletRenderer extends EntityRenderer<BulletEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(GunMod.MOD_ID, "textures/entity/laser_bullet.png");

    public LaserBulletRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BulletEntity entity, float yaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        // Align with motion
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(0.2f, 0.2f, 0.8f);

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(TEXTURE));

        // Simple quad “laser capsule”
        float minU = 0.0f, maxU = 1.0f, minV = 0.0f, maxV = 1.0f;

        // Front face
        vc.vertex(poseStack.last().pose(), -0.1f, -0.1f, 0.4f).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, 1).endVertex();
        vc.vertex(poseStack.last().pose(),  0.1f, -0.1f, 0.4f).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, 1).endVertex();
        vc.vertex(poseStack.last().pose(),  0.1f,  0.1f, 0.4f).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, 1).endVertex();
        vc.vertex(poseStack.last().pose(), -0.1f,  0.1f, 0.4f).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, 1).endVertex();

        // Back face
        vc.vertex(poseStack.last().pose(), -0.1f,  0.1f, -0.4f).color(255, 255, 255, 255).uv(minU, minV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, -1).endVertex();
        vc.vertex(poseStack.last().pose(),  0.1f,  0.1f, -0.4f).color(255, 255, 255, 255).uv(maxU, minV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, -1).endVertex();
        vc.vertex(poseStack.last().pose(),  0.1f, -0.1f, -0.4f).color(255, 255, 255, 255).uv(maxU, maxV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, -1).endVertex();
        vc.vertex(poseStack.last().pose(), -0.1f, -0.1f, -0.4f).color(255, 255, 255, 255).uv(minU, maxV).overlayCoords(0, 10).uv2(packedLight).normal(0, 0, -1).endVertex();

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return TEXTURE;
    }
}
