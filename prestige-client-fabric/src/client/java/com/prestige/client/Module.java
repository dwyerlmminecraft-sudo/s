package com.prestige.client;

/** Prestige module catalogue. Non-HUD combat entries are UI placeholders and do not alter gameplay. */
public enum Module {
    AIMBOT("Aimbot", "Combat", "UI placeholder; no automatic aiming is implemented."),
    AUTO_CLICKER("Auto Clicker", "Combat", "UI placeholder; no automated attacks are implemented."),
    BRIDGE_ASSIST("Bridge Assist", "Combat", "UI placeholder; no automatic block placement is implemented."),
    HITBOXES("Hitboxes", "Combat", "Visual hitbox overlay placeholder."),
    NO_JUMP_DELAY("No Jump Delay", "Combat", "UI placeholder; gameplay timing is unchanged."),
    NO_MISS_DELAY("No Miss Delay", "Combat", "UI placeholder; gameplay timing is unchanged."),
    SWAP_HIT("Swap Hit", "Combat", "UI placeholder; automatic weapon switching is not implemented."),
    TRIGGERBOT("Triggerbot", "Combat", "UI placeholder; automatic attacks are not implemented."),
    WTAP("WTap", "Combat", "UI placeholder; sprint resets are not automated."),
    HITFLICK("Hitflick", "Combat", "UI placeholder; aim adjustments are not implemented."),
    SILENT_AIM("Silent Aim", "Combat", "UI placeholder; camera and aim are unchanged."),
    ANCHOR_MACRO("Anchor Macro", "Crystal PvP", "UI placeholder; no combat macro is implemented."),
    AUTO_CRYSTAL("Auto Crystal", "Crystal PvP", "UI placeholder; crystal placement and breaking are unchanged."),
    AUTO_HIT_CRYSTAL("Auto Hit Crystal", "Crystal PvP", "UI placeholder; no automatic targeting is implemented."),
    AUTO_TOTEM("Auto Totem", "Crystal PvP", "UI placeholder; equipment is not switched automatically."),
    BREACH_SWAP("Breach Swap", "Crystal PvP", "UI placeholder; automatic equipment switching is not implemented."),
    DOUBLE_ANCHOR("Double Anchor", "Crystal PvP", "UI placeholder; no anchor sequence is automated."),
    SAFE_ANCHOR("Safe Anchor", "Crystal PvP", "UI placeholder; placement calculations are not implemented."),
    CRYSTAL_OPTIMIZER("Crystal Optimizer", "Crystal PvP", "UI placeholder; no crystal calculations are changed."),
    AUTO_DRAIN("Auto Drain", "Crystal PvP", "UI placeholder; no combat behavior is implemented."),
    AUTO_MACE("Auto Mace", "Mace PvP", "UI placeholder; attacks are not automated."),
    AUTO_PEARL_CATCH("Auto Pearl Catch", "Mace PvP", "UI placeholder; pearl tracking is not implemented."),
    INSTANT_LUNGE("Instant Lunge", "Mace PvP", "UI placeholder; no enchantment automation is implemented."),
    EBOUNCE("Ebounce", "Mace PvP", "UI placeholder; movement is unchanged."),
    ELYTRA_WIND_CHARGE("Elytra Wind Charge", "Mace PvP", "UI placeholder; item use is not automated."),
    WIND_CHARGE_MACRO("Wind Charge Macro", "Mace PvP", "UI placeholder; no macro is implemented."),
    AUTO_CART("Auto Cart", "Mace PvP", "UI placeholder; minecart placement is not automated."),
    AUTO_JUMP_RESET("Auto Jump Reset", "Player Utilities", "UI placeholder; movement inputs are unchanged."),
    FAKE_LAG("Fake Lag", "Player Utilities", "UI placeholder; network traffic is unchanged."),
    FAST_XP("Fast XP", "Player Utilities", "UI placeholder; item use is not automated."),
    PEARL_MACRO("Pearl Macro", "Player Utilities", "UI placeholder; item switching is not automated."),
    POT_MACRO("Pot Macro", "Player Utilities", "UI placeholder; potion use is not automated."),
    AUTO_POT("Auto Pot", "Player Utilities", "UI placeholder; healing items are not used automatically."),
    AUTO_SHIELD_BREAK("Auto Shield Break", "Player Utilities", "UI placeholder; weapon switching is not automated."),
    AUTO_TOOL("Auto Tool", "Player Utilities", "UI placeholder; tool selection is unchanged."),
    FAST_PLACE("Fast Place", "Player Utilities", "UI placeholder; block placement timing is unchanged."),
    SPRINT("Sprint", "Player Utilities", "UI placeholder; sprinting is not automated."),
    ESP("ESP", "Visuals", "UI placeholder; no through-wall entity highlighting is implemented."),
    HIT_EFFECT("Hit Effect", "Visuals", "UI placeholder; custom hit particles are not implemented."),
    HUD("HUD", "Visuals", "Toggle the client information overlay."),
    THEMES("Themes", "Visuals", "Use the Themes tab to customize accent colors."),
    OPTIMIZATION("Optimization", "Visuals", "UI placeholder; rendering behavior is unchanged."),
    ANTI_BOT("Anti Bot", "Miscellaneous", "UI placeholder; player filtering is not implemented."),
    ANTI_ACTION("Anti Action", "Miscellaneous", "UI placeholder; input filtering is not implemented."),
    CONFIGS("Configs", "Miscellaneous", "Use the Configs tab to save and load setups."),
    SOCIALS("Socials", "Miscellaneous", "UI placeholder; social features are not implemented."),
    SELF_DESTRUCT("Self Destruct", "Miscellaneous", "UI placeholder; no files are deleted or hidden."),
    FPS("FPS Display", "HUD", "Shows your frames per second."),
    COORDINATES("Coordinates", "HUD", "Shows your current X, Y and Z position."),
    KEYSTROKES("Keystrokes", "HUD", "Shows WASD, jump and mouse button presses."),
    CPS("CPS Counter", "HUD", "Shows how many times per second you click."),
    MODULE_LIST("Module List", "HUD", "Lists enabled display modules in the overlay.");

    public final String title;
    public final String category;
    public final String description;

    Module(String title, String category, String description) {
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public static String[] categories() {
        return new String[] {"Combat", "Crystal PvP", "Mace PvP", "Player Utilities", "Visuals", "Miscellaneous", "HUD"};
    }
}
