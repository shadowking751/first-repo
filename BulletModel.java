package com.yourname.gunmod.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.model.EntityModel;
import com.yourname.gunmod.entity.BulletEntity;

public class BulletModel extends EntityModel<BulletEntity> {

    private final ModelPart root;

    public BulletModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Main bullet body
        root.addOrReplaceChild("body",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-2, -2, -2, 4, 4, 4),
            PartPose.ZERO);

        // Tip
        root.addOrReplaceChild("tip",
            CubeListBuilder.create()
                .texOffs(0, 8)
                .addBox(-1, -1, -4, 2, 2, 2),
            PartPose.ZERO);

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void setupAnim(BulletEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

    @Override
    public void renderToBuffer(
        net.minecraft.client.renderer.MultiBufferSource buffer,
        net.minecraft.client.renderer.block.model.ItemTransforms.TransformType transformType,
        float partialTicks,
        int packedLight,
        int packedOverlay,
        float red, float green, float blue, float alpha
    ) {
        root.render(buffer.getBuffer(net.minecraft.client.renderer.RenderType.entityCutoutNoCull(getTextureLocation(null))), packedLight, packedOverlay);
    }
}
