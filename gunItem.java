package com.yourname.gunmod.item;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.entity.BulletEntity;
import com.yourname.gunmod.network.ModMessages;
import com.yourname.gunmod.network.ReloadPacket;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GunItem extends Item {

    private static final int MAGAZINE_SIZE = 8;
    private static final int RELOAD_TIME_TICKS = 30; // 1.5 seconds at 20 TPS

    public GunItem(Properties properties) {
        super(properties
            .durability(500)
            .fireResistant()
        );
    }

    // -----------------------------
    // MAGAZINE + RELOAD STATE
    // -----------------------------
    public static int getShotsLeft(ItemStack stack) {
        return stack.getOrCreateTag().getInt("ShotsLeft");
    }

    public static void setShotsLeft(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt("ShotsLeft", value);
    }

    public static boolean isReloading(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Reloading");
    }

    public static void setReloading(ItemStack stack, boolean value) {
        stack.getOrCreateTag().putBoolean("Reloading", value);
    }

    public static int getReloadTimer(ItemStack stack) {
        return stack.getOrCreateTag().getInt("ReloadTimer");
    }

    public static void setReloadTimer(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt("ReloadTimer", value);
    }

    // -----------------------------
    // MANUAL RELOAD (R KEY)
    // -----------------------------
    public static void tryManualReload(Player player, ItemStack stack) {
        if (stack.getItem() instanceof GunItem) {
            if (!isReloading(stack) && getShotsLeft(stack) < MAGAZINE_SIZE) {
                startReload(player, stack);
            }
        }
    }

    // -----------------------------
    // START RELOAD
    // -----------------------------
    private static void startReload(Player player, ItemStack stack) {
        if (!player.getInventory().contains(new ItemStack(GunMod.AMMO.get()))) {
            return;
        }

        setReloading(stack, true);
        setReloadTimer(stack, RELOAD_TIME_TICKS);

        // Sci-fi reload sound
        player.level().playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            GunMod.RELOAD_SOUND.get(),
            SoundSource.PLAYERS,
            1.0f,
            1.0f
        );
    }

    // -----------------------------
    // TICK RELOAD
    // -----------------------------
    @Override
    public void inventoryTick(ItemStack stack, Level level, Player player, int slot, boolean selected) {
        if (!selected) return;

        if (isReloading(stack)) {
            int timer = getReloadTimer(stack) - 1;
            setReloadTimer(stack, timer);

            // Glow effect during reload
            if (level.isClientSide) {
                player.level().addParticle(
                    ParticleTypes.END_ROD,
                    player.getX(),
                    player.getEyeY() + 0.2,
                    player.getZ(),
                    0, 0.02, 0
                );
            }

            if (timer <= 0) {
                finishReload(player, stack);
            }
        }
    }

    private static void finishReload(Player player, ItemStack stack) {
        setReloading(stack, false);
        setReloadTimer(stack, 0);

        // Consume 1 ammo item
        player.getInventory().removeItem(new ItemStack(GunMod.AMMO.get()));

        // Refill magazine
        setShotsLeft(stack, MAGAZINE_SIZE);
    }

    // -----------------------------
    // MAIN USE (FIRING)
    // -----------------------------
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Cannot fire during reload
        if (isReloading(stack)) {
            return InteractionResultHolder.fail(stack);
        }

        int shotsLeft = getShotsLeft(stack);

        // Auto-reload when empty
        if (shotsLeft <= 0) {
            startReload(player, stack);
            return InteractionResultHolder.fail(stack);
        }

        // FIRE BULLET
        if (!level.isClientSide) {
            BulletEntity bullet = new BulletEntity(level, player);
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 2.0f, 1.0f);
            level.addFreshEntity(bullet);

            // Damage gun
            stack.hurt(1, player.getRandom(), player);

            // Recoil
            float recoilForce = 0.15f;
            double yawRad = Math.toRadians(player.getYRot());
            player.push(
                -Math.cos(yawRad) * recoilForce,
                0.05f,
                -Math.sin(yawRad) * recoilForce
            );

            // Firing sound
            level.playSound(
                null,
                player.getX(),
                player.getEyeY(),
                player.getZ(),
                GunMod.FIRE_SOUND.get(),
                SoundSource.PLAYERS,
                1.0f,
                1.2f
            );
        }

        // Muzzle flash (client)
        if (level.isClientSide) {
            spawnMuzzleFlash(player, level);
        }

        // Reduce magazine count
        setShotsLeft(stack, shotsLeft - 1);

        return InteractionResultHolder.success(stack);
    }

    // -----------------------------
    // MUZZLE FLASH
    // -----------------------------
    private void spawnMuzzleFlash(Player player, Level level) {
        double yawRad = Math.toRadians(player.getYRot());
        double pitchRad = Math.toRadians(player.getXRot());

        double forward = 1.2;
        double right = 0.35;
        double up = -0.15;

        double xDir = -Math.sin(yawRad);
        double zDir = Math.cos(yawRad);
        double yDir = -Math.sin(pitchRad);

        double muzzleX = player.getX() + xDir * forward + Math.cos(yawRad) * right;
        double muzzleY = player.getEyeY() + yDir * forward + up;
        double muzzleZ = player.getZ() + zDir * forward + Math.sin(yawRad) * right;

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
    }
}
