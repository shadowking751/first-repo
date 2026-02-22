package com.yourname.gunmod.item;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.entity.BulletEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GunItem extends Item {

    public GunItem(Item.Properties properties) {
        super(properties
            .durability(500)
            .fireResistant()
        );
    }

    // -----------------------------
    // AMMO CHECKING
    // -----------------------------
    private boolean hasAmmo(Player player) {
        return player.getInventory().contains(new ItemStack(GunMod.AMMO.get()));
    }

    private void consumeAmmo(Player player) {
        player.getInventory().removeItem(new ItemStack(GunMod.AMMO.get()));
    }

    // -----------------------------
    // MAIN USE LOGIC
    // -----------------------------
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Prevent firing during cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemStack);
        }

        // Check ammo
        if (!hasAmmo(player)) {
            if (!level.isClientSide) {
                level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.DISPENSER_FAIL,
                    SoundSource.PLAYERS,
                    0.5f,
                    0.5f
                );
            }
            return InteractionResultHolder.fail(itemStack);
        }

        // -----------------------------
        // SERVER-SIDE FIRING LOGIC
        // -----------------------------
        if (!level.isClientSide) {

            // Spawn bullet
            BulletEntity bullet = new BulletEntity(level, player);
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 1.0f);
            level.addFreshEntity(bullet);

            // Damage gun
            itemStack.hurt(1, player.getRandom(), player);

            // Recoil physics
            float recoilForce = 0.15f;
            double yawRad = Math.toRadians(player.getYRot());
            player.push(
                -Math.cos(yawRad) * recoilForce,
                0.05f,
                -Math.sin(yawRad) * recoilForce
            );

            // Consume ammo
            consumeAmmo(player);

            // Firing sound
            level.playSound(
                null,
                player.getX(),
                player.getEyeY(),
                player.getZ(),
                SoundEvents.CROSSBOW_SHOOT,
                SoundSource.PLAYERS,
                1.0f,
                1.2f
            );

            // Start using for animation
            player.startUsingItem(hand);

            // Cooldown
            player.getCooldowns().addCooldown(this, 10);
        }

        // -----------------------------
        // CLIENT-SIDE MUZZLE FLASH
        // -----------------------------
        if (level.isClientSide) {

            double yawRad = Math.toRadians(player.getYRot());
            double pitchRad = Math.toRadians(player.getXRot());

            // Barrel offsets
            double forward = 1.2;
            double right = 0.35;
            double up = -0.15;

            // Direction vectors
            double xDir = -Math.sin(yawRad);
            double zDir = Math.cos(yawRad);
            double yDir = -Math.sin(pitchRad);

            // Muzzle world position
            double muzzleX = player.getX() + xDir * forward + Math.cos(yawRad) * right;
            double muzzleY = player.getEyeY() + yDir * forward + up;
            double muzzleZ = player.getZ() + zDir * forward + Math.sin(yawRad) * right;

            // Flame particles
            for (int i = 0; i < 8; i++) {
                double ox = (player.getRandom().nextDouble() - 0.5) * 0.2;
                double oy = (player.getRandom().nextDouble() - 0.5) * 0.2;
                double oz = (player.getRandom().nextDouble() - 0.5) * 0.2;

                level.addParticle(
                    ParticleTypes.FLAME,
                    muzzleX + ox,
                    muzzleY + oy,
                    muzzleZ + oz,
                    0, 0, 0
                );
            }

            // Smoke
            for (int i = 0; i < 5; i++) {
                double ox = (player.getRandom().nextDouble() - 0.5) * 0.3;
                double oy = (player.getRandom().nextDouble() - 0.5) * 0.3;
                double oz = (player.getRandom().nextDouble() - 0.5) * 0.3;

                level.addParticle(
                    ParticleTypes.SMOKE,
                    muzzleX + ox,
                    muzzleY + oy,
                    muzzleZ + oz,
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
