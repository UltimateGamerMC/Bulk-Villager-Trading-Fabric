# Bulk Villager Trading

Fabric mod for Minecraft 1.21.11. Automatically re-stocks the villager trade UI after you shift-click a trade.

## How to use

1. Install Fabric Loader and Fabric API for 1.21.11.
2. Put the mod jar in your `mods` folder.
3. Open a villager trading screen, pick a trade, then shift-click the result to buy. The same trade is selected again so you can shift-click repeatedly to buy in bulk.

## Building

- Java 21
- `./gradlew build` (omit the `org.gradle.toolchains.foojay-resolver-convention` plugin from `settings.gradle` if you hit a Gson/reflection error on Java 21)

Output: `build/libs/bulktrade-1.0.0.jar`
