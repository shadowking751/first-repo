package com.yourname.gunmod.item;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.entity.BulletEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;

public class GunItem extends Item {
    
    public GunItem(Item.Properties properties) {
        super(properties
            .durability(500)
            .fireResistant()
        );
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(itemStack);
                player.startUsingItem(hand);
            }
            
            // Create bullet with velocity
            BulletEntity bullet = new BulletEntity(level, player);
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 1.0f);
            level.addFreshEntity(bullet);
            
            itemStack.hurt(1, player.getRandom(), player);
            
            // Smooth firing sound
            level.playSound(
                null,
                player.getX(),
                player.getY() + player.getEyeHeight(),
                player.getZ(),
                SoundEvents.DISPENSER_FAIL,
                SoundSource.PLAYERS,
                0.8f,
                1.3f
            );
            
            // Recoil with smooth animation
            float recoilForce = 0.15f;
            player.push(
                -Math.cos(Math.toRadians(player.getYRot())) * recoilForce,
                0.05f,
                -Math.sin(Math.toRadians(player.getYRot())) * recoilForce
            );
            
            // Muzzle flash effect
            for (int i = 0; i < 8; i++) {
                double offsetX = (player.getRandom().nextDouble() - 0.5) * 0.8;
                double offsetY = (player.getRandom().nextDouble() - 0.5) * 0.8;
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 0.8;
                level.addParticle(
                    ParticleTypes.FLAME,
                    player.getX() + offsetX,
                    player.getY() + 1.5 + offsetY,
                    player.getZ() + offsetZ,
                    offsetX * 0.15, offsetY * 0.15, offsetZ * 0.15
                );
            }
            
            player.getCooldowns().addCooldown(this, 10);
        } else {
            // Client-side muzzle smoke
            for (int i = 0; i < 5; i++) {
                double offsetX = (player.getRandom().nextDouble() - 0.5) * 0.6;
                double offsetY = (player.getRandom().nextDouble() - 0.5) * 0.6;
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 0.6;
                level.addParticle(
                    ParticleTypes.SMOKE,
                    player.getX() + offsetX,
                    player.getY() + 1.5 + offsetY,
                    player.getZ() + offsetZ,
                    0, 0, 0
                );
            }
        }
        
        return InteractionResultHolder.success(itemStack);
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
    @Override
    public int getEnchantmentValue() {
        return 15;
    }
}
