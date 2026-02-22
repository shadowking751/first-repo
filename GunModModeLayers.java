package com.yourname.gunmod.client;

import com.yourname.gunmod.GunMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class GunModModelLayers {
    public static final ModelLayerLocation BULLET =
        new ModelLayerLocation(new ResourceLocation(GunMod.MOD_ID, "bullet"), "main");
}
