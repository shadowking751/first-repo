package com.yourname.gunmod;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraft.world.item.CreativeModeTabs;

@Mod.EventBusSubscriber(modid = GunMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabEvents {

    @SubscribeEvent
    public static void buildContents(CreativeModeTabEvent.BuildContents event) {

        // Add the gun to the Weapons / Combat tab
        if (event.getTab() == CreativeModeTabs.COMBAT) {
            event.accept(GunMod.GUN.get());
        }

        // Add ammo to Ingredients tab (optional)
        if (event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(GunMod.AMMO.get());
        }
    }
}
