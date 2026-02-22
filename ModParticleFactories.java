package com.yourname.gunmod.client;

import com.yourname.gunmod.GunMod;
import com.yourname.gunmod.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticleFactories {

    @SubscribeEvent
    public static void registerFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RED_LASER.get(), RedLaserFactory::new);
        event.registerSpriteSet(ModParticles.STONE_DUST.get(), SimpleDustFactory::new);
        event.registerSpriteSet(ModParticles.METAL_SPARKS.get(), SimpleDustFactory::new);
        event.registerSpriteSet(ModParticles.WOOD_SPLINTERS.get(), SimpleDustFactory::new);
        event.registerSpriteSet(ModParticles.BLOOD_HIT.get(), SimpleDustFactory::new);
    }

    // Thin, bright laser
    public static class RedLaserParticle extends TextureSheetParticle {
        protected RedLaserParticle(ParticleEngine engine, SpriteSet sprites, double x, double y, double z, double vx, double vy, double vz) {
            super(engine.level, x, y, z, vx, vy, vz);
            this.pickSprite(sprites);
            this.lifetime = 6;
            this.gravity = 0.0f;
            this.rCol = 1.0f;
            this.gCol = 0.1f;
            this.bCol = 0.1f;
            this.quadSize = 0.12f;
        }

        @Override
        public void tick() {
            super.tick();
            this.alpha = 1.0f - (float) this.age / (float) this.lifetime;
        }

        @Override
        public net.minecraft.client.particle.ParticleRenderType getRenderType() {
            return net.minecraft.client.particle.ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
    }

    public static class RedLaserFactory implements net.minecraft.client.particle.ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public RedLaserFactory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, net.minecraft.client.multiplayer.ClientLevel level,
                                                   double x, double y, double z, double vx, double vy, double vz) {
            return new RedLaserParticle(Minecraft.getInstance().particleEngine, sprites, x, y, z, vx, vy, vz);
        }
    }

    // Generic small debris/blood
    public static class SimpleDustParticle extends SimpleAnimatedParticle {
        protected SimpleDustParticle(net.minecraft.client.multiplayer.ClientLevel level, double x, double y, double z,
                                     double vx, double vy, double vz, SpriteSet sprites) {
            super(level, x, y, z, sprites, 0.0f);
            this.setSize(0.1f, 0.1f);
            this.lifetime = 10 + this.random.nextInt(6);
            this.gravity = 0.1f;
            this.xd = vx;
            this.yd = vy;
            this.zd = vz;
        }
    }

    public static class SimpleDustFactory implements net.minecraft.client.particle.ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public SimpleDustFactory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, net.minecraft.client.multiplayer.ClientLevel level,
                                                   double x, double y, double z, double vx, double vy, double vz) {
            return new SimpleDustParticle(level, x, y, z, vx, vy, vz, sprites);
        }
    }
}
