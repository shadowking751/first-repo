package com.yourname.gunmod.sound;

import com.yourname.gunmod.GunMod;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GunMod.MOD_ID);

    public static final RegistryObject<SoundEvent> FIRE_SOUND =
            SOUNDS.register("gun_fire",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GunMod.MOD_ID, "gun_fire")));

    public static final RegistryObject<SoundEvent> RELOAD_SOUND =
            SOUNDS.register("gun_reload",
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(GunMod.MOD_ID, "gun_reload")));
}
