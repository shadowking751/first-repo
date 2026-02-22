package com.yourname.gunmod.entity;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.particles.ModParticles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class BulletEntity extends AbstractArrow {

    public BulletEntity(EntityType<? extends BulletEntity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.setBaseDamage(6.0);
    }

    public BulletEntity(Level level, LivingEntity shooter) {
        super(GunMod.BULLET.get(), shooter, level);
        this.setNoGravity(true);
        this.setBaseDamage(6.0);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            spawnLaserTrail();
        }
    }

    // -----------------------------
    // RED LASER TRAIL
    // -----------------------------
    private void spawnLaserTrail() {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        // Red laser particle
        this.level().addParticle(
            ModParticles.RED_LASER.get(),
            x, y, z,
            0, 0, 0
        );
    }

    // -----------------------------
    // HIT LOGIC
    // -----------------------------
    @Override
    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            handleEntityHit((EntityHitResult) hitResult);
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            handleBlockHit((BlockHitResult) hitResult);
        }

        // Explosion (server-side)
        if (!this.level().isClientSide) {
            this.level().explode(
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                2.0f,
                Level.ExplosionInteraction.TNT
            );
        }

        this.discard();
    }

    // -----------------------------
    // ENTITY HIT EFFECTS
    // -----------------------------
    private void handleEntityHit(EntityHitResult hit) {
        Entity target = hit.getEntity();

        if (target instanceof LivingEntity living) {
            living.hurt(DamageSource.indirectMobAttack(this, this.getOwner()), 6.0f);

            // Blood particles (client-side)
            if (this.level().isClientSide) {
                for (int i = 0; i < 10; i++) {
                    this.level().addParticle(
                        ModParticles.BLOOD_HIT.get(),
                        living.getX(),
                        living.getEyeY(),
                        living.getZ(),
                        (this.random.nextDouble() - 0.5) * 0.3,
                        (this.random.nextDouble() - 0.5) * 0.3,
                        (this.random.nextDouble() - 0.5) * 0.3
                    );
                }
            }
        }
    }

    // -----------------------------
    // BLOCK HIT EFFECTS
    // -----------------------------
    private void handleBlockHit(BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        Block block = this.level().getBlockState(pos).getBlock();

        if (!this.level().isClientSide) return;

        SimpleParticleType particle = null;

        if (block == Blocks.STONE || block == Blocks.COBBLESTONE || block == Blocks.DEEPSLATE) {
            particle = ModParticles.STONE_DUST.get();
        } else if (block == Blocks.IRON_BLOCK || block == Blocks.ANVIL || block == Blocks.COPPER_BLOCK) {
            particle = ModParticles.METAL_SPARKS.get();
        } else if (block == Blocks.OAK_PLANKS || block == Blocks.SPRUCE_PLANKS || block == Blocks.BIRCH_PLANKS) {
            particle = ModParticles.WOOD_SPLINTERS.get();
        } else {
            particle = ParticleTypes.SMOKE;
        }

        for (int i = 0; i < 12; i++) {
            this.level().addParticle(
                particle,
                this.getX(),
                this.getY(),
                this.getZ(),
                (this.random.nextDouble() - 0.5) * 0.4,
                (this.random.nextDouble() - 0.5) * 0.4,
                (this.random.nextDouble() - 0.5) * 0.4
            );
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        // No arrow sticking
    }

    @Override
    protected boolean shouldFall() {
        return false;
    }
}
