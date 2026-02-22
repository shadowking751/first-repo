package com.yourname.gunmod.particles;

import com.yourname.gunmod.GunMod;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {

    public static final DeferredRegister<SimpleParticleType> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, GunMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> RED_LASER =
            PARTICLES.register("red_laser", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> STONE_DUST =
            PARTICLES.register("stone_dust", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> METAL_SPARKS =
            PARTICLES.register("metal_sparks", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> WOOD_SPLINTERS =
            PARTICLES.register("wood_splinters", () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> BLOOD_HIT =
            PARTICLES.register("blood_hit", () -> new SimpleParticleType(true));
}
