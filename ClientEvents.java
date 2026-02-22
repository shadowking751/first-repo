package com.yourname.gunmod.client;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.item.GunItem;
import com.yourname.gunmod.network.ModMessages;
import com.yourname.gunmod.network.ReloadPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {

        if (ModKeybinds.RELOAD_KEY.consumeClick()) {

            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null) return;

            ItemStack held = mc.player.getMainHandItem();

            if (held.getItem() instanceof GunItem) {

                // Send reload request to server
                ModMessages.sendToServer(new ReloadPacket());
            }
        }
    }
}
