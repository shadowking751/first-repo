package com.yourname.gunmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import com.yourname.gunmod.GunMod;

public class BulletEntity extends AbstractArrow {
    
    private int ticksInAir = 0;
    private static final float EXPLOSION_POWER = 2.5f;
    private static final int MAX_TICKS = 400;
    
    // Effect durations (in ticks)
    private static final int SLOWNESS_DURATION = 100;  // 5 seconds
    private static final int SLOWNESS_LEVEL = 1;       // Slowness II
    private static final int BURNING_DURATION = 120;   // 6 seconds
    
    public BulletEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }
    
    public BulletEntity(Level level, LivingEntity shooter) {
        super(GunMod.BULLET.get(), shooter, level);
        this.setBaseDamage(8.0D);  // Bullet damage before explosion
        this.setNoGravity(false);  // Bullets fall with gravity for more realism
        this.setCritArrow(false);
    }
    
    @Override
    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            Entity entity = entityHitResult.getEntity();
            
            if (entity != this.getOwner()) {
                // Apply effects to living entities
                if (entity instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) entity;
                    
                    // Apply slowness effect
                    living.addEffect(new MobEffectInstance(
                        MobEffects.MOVEMENT_SLOWDOWN,
                        SLOWNESS_DURATION,
                        SLOWNESS_LEVEL,
                        false,  // ambient
                        true    // show particles
                    ));
                    
                    // Apply burning effect
                    living.setSecondsOnFire(BURNING_DURATION / 20);  // Convert ticks to seconds
                }
                
                this.explode();
            }
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.explode();
        }
        super.onHit(hitResult);
    }
    
    private void explode() {
        if (!this.level().isClientSide) {
            // Spawn explosion particles
            for (int i = 0; i < 20; i++) {
                double offsetX = (this.getRandom().nextDouble() - 0.5) * 2;
                double offsetY = (this.getRandom().nextDouble() - 0.5) * 2;
                double offsetZ = (this.getRandom().nextDouble() - 0.5) * 2;
                this.level().addParticle(
                    ParticleTypes.EXPLOSION,
                    this.getX() + offsetX,
                    this.getY() + offsetY,
                    this.getZ() + offsetZ,
                    offsetX * 0.1, offsetY * 0.1, offsetZ * 0.1
                );
            }
            
            // Play explosion sound
            this.level().playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.BLOCKS,
                1.0f,
                1.0f
            );
            
            // Create explosion with damage to entities
            this.level().explode(
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                EXPLOSION_POWER,
                true  // Set fire
            );
            this.discard();
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        ticksInAir++;
        
        // Spawn trail particles for visual effect
        if (ticksInAir % 2 == 0) {
            this.level().addParticle(
                ParticleTypes.SMOKE,
                this.getX(),
                this.getY(),
                this.getZ(),
                0, 0, 0
            );
        }
        
        // Bullet expires after 20 seconds
        if (ticksInAir > MAX_TICKS) {
            this.discard();
        }
    }
    
    @Override
    protected void tickDespawn() {
        // Override to prevent default despawn
    }
    
    @Override
    public void playerTouch(net.minecraft.world.entity.player.Player player) {
        // Prevent players from picking up the bullet
    }
}
