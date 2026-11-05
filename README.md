# Exstart Sidebar Minecraft Plugin

Exstart Sidebar is a Minecraft scoreboard plugin for Paper servers that displays a sidebar with the orange Exstart title, claim block information, total playtime, current session time, and the blue server address exstart.mcsh.gg.

## Install

1. Build the plugin with Java 21 and Maven:
   ```bash
   mvn clean package
   ```
2. Copy the generated `target/plugin.jar` file to your server's `plugins` folder.
3. Start or restart a Paper Minecraft server running version 1.21.11 or a compatible version.
4. The plugin creates and manages its data in `plugins/ExstartScoreboard/config.yml`.

## Use

When a player joins, the sidebar is displayed automatically with:

- An orange `Exstart` title.
- The player's claim blocks.
- Total saved playtime.
- Current session playtime.
- The blue address `exstart.mcsh.gg`.

The scoreboard refreshes once per second.

To set a player's claim blocks, use:

```text
/claimblocks <player> <amount>
```

Example:

```text
/claimblocks Steve 2500
```

The command requires the `exstartscoreboard.admin` permission, which is granted to server operators by default. Claim block values and playtime are saved in the plugin configuration.

Requirements:

- Paper 1.21.11 or a compatible Paper server.
- Java 21 or newer.
- Maven and Java 21 if building from source.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/eebe31d1-1065-49b5-9bd9-0c75de4e47b5?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=eebe31d1-1065-49b5-9bd9-0c75de4e47b5
