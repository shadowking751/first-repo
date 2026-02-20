package com.yourname.gunmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import com.yourname.gunmod.GunMod;

public class BulletEntity extends AbstractArrow {
    
    private int ticksInAir = 0;
    
    public BulletEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }
    
    public BulletEntity(Level level, LivingEntity shooter) {
        super(GunMod.BULLET.get(), shooter, level);
        this.setBaseDamage(10.0D);
    }
    
    @Override
    protected void onHit(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) hitResult;
            if (entityHitResult.getEntity() != this.getOwner()) {
                this.explode();
            }
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.explode();
        }
        super.onHit(hitResult);
    }
    
    private void explode() {
        if (!this.level().isClientSide) {
            // Create explosion with power 2.0f (configurable)
            this.level().explode(
                this,
                this.getX(),
                this.getY(),
                this.getZ(),
                2.0f,  // Explosion radius/power
                true   // Set fire
            );
            this.discard();
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        ticksInAir++;
        
        // Bullet expires after 20 seconds
        if (ticksInAir > 400) {
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
