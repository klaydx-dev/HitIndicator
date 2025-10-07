package org.klaydx;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("DecentHolograms") == null) {
            getLogger().severe("DecentHolograms не найден! Плагин отключается.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();

        getCommand("hiti").setExecutor(new HitIndicatorCommand(this));

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("HitIndicator включен!");
    }

    @Override
    public void onDisable() {
        getLogger().info("HitIndicator выключен!");
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Player attacker = null;
        boolean isCrit = false;
        boolean isTrident = false;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
            isCrit = attacker.getFallDistance() > 0.0F
                    && !attacker.isOnGround()
                    && !attacker.isInWater()
                    && !attacker.hasPotionEffect(org.bukkit.potion.PotionEffectType.BLINDNESS)
                    && attacker.getVehicle() == null;
        } else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.getShooter() instanceof Player) {
                attacker = (Player) arrow.getShooter();
                isCrit = arrow.isCritical();
            }
        } else if (event.getDamager() instanceof Trident) {
            Trident trident = (Trident) event.getDamager();
            if (trident.getShooter() instanceof Player) {
                attacker = (Player) trident.getShooter();
                isTrident = true;
                isCrit = attacker.getFallDistance() > 0.0F && !attacker.isOnGround();
            }
        }

        if (attacker == null) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        LivingEntity victim = (LivingEntity) event.getEntity();
        double damage = event.getFinalDamage();

        double heightOffset = getConfig().getDouble("hologram.height-offset", 0.4);

        Location victimLoc = victim.getLocation();
        Location hologramLoc = victimLoc.clone().add(0.8, victim.getHeight() + heightOffset, 0);

        String holoName = "hit_" + UUID.randomUUID().toString().substring(0, 8);

        String hitType;
        if (isTrident) {
            hitType = ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("messages.trident", "&eТРЕЗУБЕЦ"));
        } else if (isCrit) {
            hitType = ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("messages.critical", "&eКРИТ"));
        } else {
            hitType = ChatColor.translateAlternateColorCodes('&',
                    getConfig().getString("messages.hit", "&eУдар"));
        }

        String damageFormat = getConfig().getString("messages.damage", "&cУрон %damage% ❤");
        String damageText = ChatColor.translateAlternateColorCodes('&',
                damageFormat.replace("%damage%", String.format("%.1f", damage)));

        Hologram hologram = DHAPI.createHologram(holoName, hologramLoc);

        hologram.setDefaultVisibleState(false);

        for (String line : getConfig().getStringList("hologram.lines")) {
            String formattedLine = line
                    .replace("%type%", hitType)
                    .replace("%damage%", damageText);
            formattedLine = ChatColor.translateAlternateColorCodes('&', formattedLine);
            DHAPI.addHologramLine(hologram, formattedLine);
        }

        hologram.setShowPlayer(attacker);
        hologram.show(attacker, 0);

        long removeTime = getConfig().getLong("hologram.remove-after-ticks", 40L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (hologram != null) {
                    hologram.delete();
                }
            }
        }.runTaskLater(this, removeTime);
    }

    private class HitIndicatorCommand implements CommandExecutor {
        private final Main plugin;

        public HitIndicatorCommand(Main plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.YELLOW + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                sender.sendMessage(ChatColor.GOLD + "HitIndicator " + ChatColor.GRAY + "v" + plugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.GRAY + "Автор: " + ChatColor.WHITE + "klaydx");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Команды:");
                sender.sendMessage(ChatColor.GOLD + "/hiti reload " + ChatColor.GRAY + "- Перезагрузить конфиг");
                sender.sendMessage(ChatColor.YELLOW + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("hitindicator.reload")) {
                    sender.sendMessage(ChatColor.RED + "У вас нет прав!");
                    return true;
                }

                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "✓ Конфиг перезагружен!");
                return true;
            }

            sender.sendMessage(ChatColor.RED + "Неизвестная команда! Используйте: /hiti reload");
            return true;
        }
    }
}