package com.yourname.gunmod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafxmod.FXModLanguageProvider;
import net.minecraftforge.fml.loading.FXModLanguageProvider;
import net.minecraftforge.fml.javafxmod.FXModLanguageProvider;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;

@Mod("gunmod")
public class GunMod {
    public static final String MOD_ID = "gunmod";
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);
    
    public static final RegistryObject<Item> GUN = ITEMS.register("gun", () -> new GunItem(new Item.Properties()));
    
    public static final RegistryObject<EntityType<BulletEntity>> BULLET = ENTITIES.register("bullet",
        () -> EntityType.Builder.<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
            .sized(0.5f, 0.5f)
            .build("gunmod:bullet"));
    
    public GunMod() {
        IEventBus modEventBus = FXModLanguageProvider.get().getModEventBus();
        
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
    }
}
