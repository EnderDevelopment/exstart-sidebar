package com.enderdevelopment.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class Main extends JavaPlugin {
    private static final String OBJECTIVE_NAME = "exstart";
    private static final String CLAIMS_KEY = "claim-blocks";
    private final Map<UUID, Long> sessionStarts = new HashMap<>();

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        for (Player player : Bukkit.getOnlinePlayers()) {
            startSession(player);
        }
        Bukkit.getPluginManager().registerEvents(new ScoreboardListener(this), this);
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                updateScoreboard(player);
            }
        }, 1L, 20L);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayTime(player);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        saveConfig();
    }

    void startSession(Player player) {
        sessionStarts.putIfAbsent(player.getUniqueId(), System.currentTimeMillis());
        updateScoreboard(player);
    }

    void endSession(Player player) {
        savePlayTime(player);
        sessionStarts.remove(player.getUniqueId());
    }

    private void savePlayTime(Player player) {
        Long startedAt = sessionStarts.get(player.getUniqueId());
        if (startedAt == null) {
            return;
        }
        long elapsedSeconds = Math.max(0L, (System.currentTimeMillis() - startedAt) / 1000L);
        String path = "players." + player.getUniqueId() + ".play-seconds";
        getConfig().set(path, getConfig().getLong(path, 0L) + elapsedSeconds);
        sessionStarts.put(player.getUniqueId(), System.currentTimeMillis());
        saveConfig();
    }

    private long getSessionSeconds(Player player) {
        Long startedAt = sessionStarts.get(player.getUniqueId());
        return startedAt == null ? 0L : Math.max(0L, (System.currentTimeMillis() - startedAt) / 1000L);
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600L;
        long minutes = (seconds % 3600L) / 60L;
        return hours + "h " + minutes + "m";
    }

    private String entryName(String value) {
        return ChatColor.values()[Math.floorMod(value.hashCode(), ChatColor.values().length)].toString() + value;
    }

    void updateScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(OBJECTIVE_NAME, "dummy", ChatColor.GOLD + "Exstart");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        String[] lines = {
            ChatColor.GRAY + "Claim blocks: " + ChatColor.WHITE + getClaimBlocks(player.getUniqueId()),
            ChatColor.GRAY + "Total playtime: " + ChatColor.WHITE + formatDuration(getConfig().getLong("players." + player.getUniqueId() + ".play-seconds", 0L) + getSessionSeconds(player)),
            ChatColor.GRAY + "This session: " + ChatColor.WHITE + formatDuration(getSessionSeconds(player)),
            ChatColor.DARK_GRAY + " ",
            ChatColor.BLUE + "exstart.mcsh.gg"
        };

        int score = lines.length;
        for (String line : lines) {
            String entry = entryName("exstart-line-" + score);
            Team team = scoreboard.registerNewTeam("line" + score);
            team.addEntry(entry);
            team.setPrefix(line.length() > 64 ? line.substring(0, 64) : line);
            objective.getScore(entry).setScore(score--);
        }
        player.setScoreboard(scoreboard);
    }

    private int getClaimBlocks(UUID playerId) {
        return getConfig().getInt("players." + playerId + ".claim-blocks", 0);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("claimblocks")) {
            return false;
        }
        if (!sender.hasPermission("exstartscoreboard.admin")) {
            sender.sendMessage(ChatColor.RED + "Voce nao tem permissao para isso.");
            return true;
        }
        if (args.length != 2) {
            return false;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sender.sendMessage(ChatColor.RED + "Informe uma quantidade inteira valida.");
            return true;
        }
        if (amount < 0) {
            sender.sendMessage(ChatColor.RED + "A quantidade nao pode ser negativa.");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Esse jogador ainda nao entrou no servidor.");
            return true;
        }
        String path = "players." + target.getUniqueId() + ".claim-blocks";
        getConfig().set(path, amount);
        saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Blocos de claim de " + target.getName() + " definidos para " + amount + ".");
        if (target.getPlayer() != null) {
            updateScoreboard(target.getPlayer());
        }
        return true;
    }
}
