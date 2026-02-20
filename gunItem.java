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

public class GunItem extends Item {
    
    public GunItem(Item.Properties properties) {
        super(properties
            .durability(500)  // Gun breaks after 500 shots
            .fireResistant()  // Gun is fireproof
        );
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // Check if player is on cooldown
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(itemStack);
            }
            
            // Create and spawn bullet entity
            BulletEntity bullet = new BulletEntity(level, player);
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
            level.addFreshEntity(bullet);
            
            // Damage the gun (durability)
            itemStack.hurt(1, player.getRandom(), player);
            
            // Play gun sound
            level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.DISPENSER_FAIL,  // You can change this to any sound
                SoundSource.PLAYERS,
                1.0f,
                1.2f  // Slightly higher pitch
            );
            
            // Add knockback effect to player
            player.push(-Math.cos(Math.toRadians(player.getYRot())) * 0.1, 0, -Math.sin(Math.toRadians(player.getYRot())) * 0.1);
            
            // Add cooldown (10 ticks = 0.5 seconds for rapid fire)
            player.getCooldowns().addCooldown(this, 10);
        } else {
            // Client-side particle effects
            for (int i = 0; i < 3; i++) {
                double offsetX = (player.getRandom().nextDouble() - 0.5) * 0.5;
                double offsetY = (player.getRandom().nextDouble() - 0.5) * 0.5;
                double offsetZ = (player.getRandom().nextDouble() - 0.5) * 0.5;
                level.addParticle(
                    net.minecraft.core.particles.ParticleTypes.SMOKE,
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
        return true;  // Allows enchanting the gun
    }
    
    @Override
    public int getEnchantmentValue() {
        return 15;  // Makes it enchantable with most enchantments
    }
}
