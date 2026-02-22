package com.yourname.gunmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yourname.gunmod.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class AmmoHudOverlay {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack held = mc.player.getMainHandItem();
        if (!(held.getItem() instanceof GunItem)) return;

        int shotsLeft = GunItem.getShotsLeft(held);
        boolean reloading = GunItem.isReloading(held);
        int reloadTimer = GunItem.getReloadTimer(held);
        int reloadMax = 30; // 1.5s reload

        GuiGraphics gui = event.getGuiGraphics();
        int screenW = mc.getWindow().getGuiScaledWidth();
        int screenH = mc.getWindow().getGuiScaledHeight();

        int x = screenW - 80;
        int y = screenH - 55;

        // -----------------------------
        // RELOADING TEXT
        // -----------------------------
        if (reloading) {
            gui.drawString(mc.font, "Reloading...", x, y, 0xFFAA00, false);

            // Progress bar
            float pct = 1f - (reloadTimer / (float) reloadMax);
            int barWidth = (int) (pct * 60);

            gui.fill(x, y + 12, x + barWidth, y + 20, 0xFFAA00FF);
            gui.fill(x + barWidth, y + 12, x + 60, y + 20, 0x55333333);

            return;
        }

        // -----------------------------
        // AMMO COUNTER
        // -----------------------------
        int color = 0xFFFFFF;

        if (shotsLeft <= 2) {
            color = 0xFF5555; // red when low
        } else if (shotsLeft <= 4) {
            color = 0xFFAA00; // gold when mid
        }

        String text = shotsLeft + " / 8";
        gui.drawString(mc.font, text, x, y, color, false);
    }
}
