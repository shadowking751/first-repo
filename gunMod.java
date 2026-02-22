package com.yourname.gunmod;

import com.yourname.gunmod.entity.BulletEntity;
import com.yourname.gunmod.network.ModMessages;
import com.yourname.gunmod.particles.ModParticles;
import com.yourname.gunmod.sound.ModSounds;
import com.yourname.gunmod.item.GunItem;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;

@Mod(GunMod.MOD_ID)
public class GunMod {

    public static final String MOD_ID = "gunmod";

    // -----------------------------
    // REGISTRIES
    // -----------------------------
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);

    // -----------------------------
    // ITEMS
    // -----------------------------
    public static final RegistryObject<Item> GUN =
            ITEMS.register("gun", () -> new GunItem(new Item.Properties()));

    public static final RegistryObject<Item> AMMO =
            ITEMS.register("ammo", () -> new Item(new Item.Properties()));

    // -----------------------------
    // ENTITIES
    // -----------------------------
    public static final RegistryObject<EntityType<BulletEntity>> BULLET =
            ENTITIES.register("bullet",
                () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .build("gunmod:bullet")
            );

    // -----------------------------
    // CONSTRUCTOR
    // -----------------------------
    public GunMod() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register items + entities
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);

        // Register sounds
        ModSounds.SOUNDS.register(modEventBus);

        // Register particles
        ModParticles.PARTICLES.register(modEventBus);

        // Register networking
        ModMessages.register();
    }
}
