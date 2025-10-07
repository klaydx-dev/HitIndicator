# 🎯 HitIndicator

A modern Minecraft plugin that displays stylish damage holograms when attacking mobs and players.


## ✨ Features

- 🎨 **Personal Holograms** - only the attacker sees the damage
- ⚔️ **All Attack Types** - melee, bow, crossbow, trident support
- 💥 **Critical Detection** - automatic recognition of critical hits
- 🎯 **Fully Customizable** - all texts and colors configurable
- ⚡ **No Delays** - instant hologram display
- 🔧 **Easy Setup** - intuitive config with color support

## 📋 Requirements

- **Minecraft**: 1.21.8
- **Server**: Paper / Purpur / Pufferfish
- **Dependencies**: [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/)
- **Java**: 21+

## 🚀 Installation

1. Download and install [DecentHolograms](https://www.spigotmc.org/resources/decentholograms.96927/)
2. Download `HitIndicator.jar` from [Releases](../../releases)
3. Place both plugins in the `plugins/` folder
4. Restart the server
5. Configure `plugins/HitIndicator/config.yml`

## ⚙️ Configuration

```yaml
# Hologram position settings
hologram:
  # Base height is Entity center height
  height-offset: 0.4

  # Delay in ticks
  remove-after-ticks: 40
  lines:
    - "%type%"
    - "%damage%"

messages:
  hit: "&6⚔ Hit"
  critical: "&c💥 CRIT"
  trident: "&b🔱 Trident"
  damage: "&4❤ %damage%"
```

### Placeholders (No PAPI Support)

- `%type%` - attack type (Hit/CRIT/TRIDENT)
- `%damage%` - damage dealt

### Color Codes

Use `&` for colors:
- `&e` - yellow
- `&c` - red
- `&a` - green
- `&b` - aqua
- `&d` - pink
- `&6` - gold

Full list: [Minecraft Color Codes](https://minecraft.fandom.com/wiki/Formatting_codes)

## 📝 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/hiti` | Plugin information | Everyone |
| `/hiti reload` | Reload config | `hitindicator.reload` |

**Aliases**: `/hitindicator`, `/hi`

## 🔑 Permissions

- `hitindicator.reload` - Allows reloading the config (default: OP)

## 📸 Examples

<img width="550" height="505" alt="image" src="https://github.com/user-attachments/assets/c49b9c9e-7684-43f9-b2e0-e64c2d4219bc" />

<img width="693" height="523" alt="image" src="https://github.com/user-attachments/assets/cd059357-8167-4766-91ae-be8795966ded" />

<img width="974" height="752" alt="image" src="https://github.com/user-attachments/assets/d82bf7c7-a389-4aee-a97b-a41e5d9b0b73" />



## 🎨 Customization

You can fully customize hologram appearance!

## 🐛 Found a Bug?

Create an [Issue](../../issues) with a description of the problem.

## 💡 Have an Idea?

Suggest a new feature via [Discussions](../../discussions).

## 📄 License

MIT License - do whatever you want with the plugin!

## 👤 Author

Created by [klaydx](https://github.com/klaydx-dev)

---

⭐ If you like the plugin - give it a star!
