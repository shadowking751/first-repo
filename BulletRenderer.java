package com.yourname.gunmod.client.renderer;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.client.model.BulletModel;
import com.yourname.gunmod.entity.BulletEntity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransforms;

import com.mojang.blaze3d.vertex.PoseStack;

public class BulletRenderer extends EntityRenderer<BulletEntity> {

    private static final ResourceLocation TEXTURE =
        new ResourceLocation(GunMod.MOD_ID, "textures/entity/bullet.png");

    private final BulletModel model;

    public BulletRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new BulletModel(ctx.bakeLayer(GunModModelLayers.BULLET));
    }

    @Override
    public void render(BulletEntity entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int light) {

        poseStack.pushPose();

        // Rotate bullet to match its velocity direction
        poseStack.mulPose(entity.getRotationVector().rotation());

        poseStack.scale(0.5f, 0.5f, 0.5f);

        model.renderToBuffer(
            poseStack,
            buffer.getBuffer(RenderType.entityCutout(TEXTURE)),
            light,
            net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
            1, 1, 1, 1
        );

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return TEXTURE;
    }
}
