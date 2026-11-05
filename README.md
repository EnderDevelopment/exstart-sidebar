# Exstart Sidebar Minecraft Plugin

Exstart Sidebar is a Minecraft plugin for Paper servers that provides a live scoreboard sidebar with player claim blocks, total playtime, current session time, an orange Exstart title, and the blue server address exstart.mcsh.gg.

## Install

1. Install Java 21 or newer.
2. Use a Paper Minecraft 1.21.11 server.
3. Build the plugin with Maven:
   ```text
   mvn package
   ```
4. Copy `target/plugin.jar` to the server's `plugins` folder.
5. Start or restart the server.
6. The plugin creates and uses its configuration file automatically.

To build from source, make sure Maven can access the PaperMC repository defined in `pom.xml`.

## Use

1. Join the server. The Exstart sidebar appears automatically.
2. The sidebar displays:
   - Claim blocks assigned to the player
   - Total saved playtime
   - Current session playtime
   - The server address `exstart.mcsh.gg`
3. Set a player's claim blocks with:
   ```text
   /claimblocks <player> <amount>
   ```
4. The command requires the `exstartscoreboard.admin` permission, which is granted to operators by default.
5. Claim block values must be whole numbers greater than or equal to zero.
6. Playtime is saved in the plugin configuration when a player leaves and when the server shuts down.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/eebe31d1-1065-49b5-9bd9-0c75de4e47b5?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=eebe31d1-1065-49b5-9bd9-0c75de4e47b5
