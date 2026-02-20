package com.yourname.gunmod.item;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.entity.BulletEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

public class GunItem extends Item {
    
    public GunItem(Item.Properties properties) {
        super(properties.tab(CreativeModeTab.TAB_COMBAT));
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
            
            // Add cooldown (20 ticks = 1 second)
            player.getCooldowns().addCooldown(this, 10);
        }
        
        return InteractionResultHolder.success(itemStack);
    }
}
