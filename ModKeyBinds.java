package com.yourname.gunmod.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.yourname.gunmod.GunMod;

@Mod.EventBusSubscriber(modid = GunMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModKeybinds {

    public static KeyMapping RELOAD_KEY;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {

        RELOAD_KEY = new KeyMapping(
            "key.gunmod.reload",
            InputConstants.KEY_R,
            "key.categories.gameplay"
        );

        event.register(RELOAD_KEY);
    }
}
