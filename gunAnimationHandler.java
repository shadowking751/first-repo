package com.yourname.gunmod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import com.yourname.gunmod.GunMod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GunMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GunAnimationHandler {
    
    public static void registerItemProperties() {
        // Register animation property for gun recoil
        ItemProperties.register(
            GunMod.GUN.get(),
            new ResourceLocation(GunMod.MOD_ID, "shooting"),
            (stack, level, entity, id) -> {
                if (entity == null) return 0.0f;
                if (!(entity instanceof Player player)) return 0.0f;
                
                // Check if player is actively using the gun
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                
                boolean isUsing = (mainHand.getItem() == GunMod.GUN.get() && player.isUsingItem()) ||
                                 (offHand.getItem() == GunMod.GUN.get() && player.isUsingItem());
                
                if (!isUsing && player.getCooldowns().isOnCooldown(GunMod.GUN.get())) {
                    // Recoil animation during cooldown
                    float cooldown = player.getCooldowns().getCooldownPercent(GunMod.GUN.get(), 0);
                    return 1.0f - cooldown; // or just cooldown, depending on how you want the curve
                }
                
                return 0.0f;
            }
        );
    }
}
