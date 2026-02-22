package com.yourname.gunmod.client;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.entity.BulletEntity;
import net.minecraft.client.renderer.entity.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityRenderers {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GunMod.BULLET.get(), LaserBulletRenderer::new);
    }
}
