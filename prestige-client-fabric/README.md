# Prestige Client V2 (Fabric, Minecraft 26.1.2)

A blue, crown-themed in-game menu and HUD. Press **Right Shift** in a world to open the menu.

* **Modules tab**: categorized catalogue for Combat, Crystal PvP, Mace PvP, Player Utilities, Visuals, Miscellaneous and HUD, with persistent on/off toggles.
* **Configs tab**: save up to 5 setups and apply or delete them.
* **Themes tab**: six accent colors.
* **Settings tab**: text shadow and watermark toggles.

Settings are saved to `config/prestige_client.json`. The menu key can be changed in
Options > Controls > Key Binds.

## Build

You need **JDK 25** and Gradle 9.4+ (or the Gradle wrapper).

1. The wrapper jar is not included. Create it once with Gradle installed:
   `gradle wrapper --gradle-version 9.4.0`
   (or copy `gradlew`, `gradlew.bat` and `gradle/wrapper/gradle-wrapper.jar` from the
   [Fabric example mod](https://github.com/FabricMC/fabric-example-mod)).
2. Run `./gradlew build` (`gradlew.bat build` on Windows).
3. The mod is `build/libs/prestige-client-2.0.0.jar` (not the `-sources` jar).
4. Put it in `.minecraft/mods/` with Fabric Loader 0.19.2+ and Fabric API for 26.1.2.

If Gradle cannot resolve a version, check https://fabricmc.net/develop for the current
numbers for 26.1.2 and edit `gradle.properties` (`loader_version`, `loom_version`,
`fabric_api_version`).

## Notes

* This is a client-only mod. Every module only displays information; none of them changes
  gameplay or sends anything to a server, so it is safe on any server.
* Written against the 26.1.2 APIs (`GuiGraphicsExtractor`, `extractRenderState`,
  `KeyMappingHelper`, `HudElementRegistry`). It was written without access to the game
  jars, so if the build reports a changed method name, the API note in the Fabric docs for
  26.1.2 will show the new one.


## Build artifact note

This repository includes a GitHub Actions workflow (`.github/workflows/build.yml`) that builds the mod with Java 25 and uploads the JAR artifact. The JAR must be compiled from source; renaming a ZIP file to `.jar` is not sufficient.
