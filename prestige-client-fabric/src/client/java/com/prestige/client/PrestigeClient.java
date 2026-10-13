package com.prestige.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.prestige.PrestigeMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class PrestigeClient implements ClientModInitializer {
    /** The menu key. Defaults to Right Shift and can be changed in Options > Controls > Key Binds. */
    public static KeyMapping openKey;

    @Override
    public void onInitializeClient() {
        PrestigeConfig.load();

        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(PrestigeMod.MOD_ID, "main"));
        openKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.prestige_client.menu",
                InputConstants.Type.KEYSYM,
                InputConstants.KEY_RSHIFT,
                category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.consumeClick()) {
                client.setScreen(new PrestigeScreen());
            }
        });

        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(PrestigeMod.MOD_ID, "hud"),
                Hud::render);
    }
}
