<h1 align="center">SoupFFA</h1>
<p align="center"><em>A lightweight and customizable Soup PvP gamemode for Minecraft servers.</em></p>

---

## 🧾 About

**SoupFFA** is a fast-paced PvP gamemode originally created for my old Minecraft server **HG-Practice.de**.  
This new version offers improved performance and extensive customization options—perfect for any PvP-focused server.

---

## ✨ Features

- 🔧 **Customizable Player Kit**  
  Configure armor, inventory items, and refill items (like soups for instant healing)
- 📊 **Custom Scoreboard & Messages**  
  Fully editable scoreboard and all messages with placeholder support
- 💾 **MySQL Database Support**  
  Stores player data persistently in a MySQL database
- ⚔️ **Enhanced PvP Mechanics**  
  Tweak damage values for a smoother and more responsive PvP experience
- 🥣 **Instant-Healing Soups**  
  Automatic healing when consuming soup, optimized for Soup-PvP
- 🏠 **Safe Spawn Area**  
  Define a no-damage zone at the spawn to prevent unfair kills

---

## 📦 Requirements

- A **Spigot** server (or compatible fork)
- **Minecraft Version**: `1.8.8`
- [**GameAPI v1.0.1**](https://github.com/Creepercraft206/HGP-GameAPI/releases/tag/v.1.0.1) (required dependency)
- A **MySQL database** (for storing player stats)

---

## ⚙️ Installation

1. Download the latest release of both **SoupFFA** and the required **GameAPI**.
2. Place both `.jar` files in your server’s `plugins/` directory.
3. Start your server to generate the configuration files.
4. You’ll now find a new folder named `SoupFFA` with the following files:
    - `config.yml`: Main configuration file
    - `DatabaseSettings.yml`: Configure your database connection here
5. Edit `DatabaseSettings.yml` to match your MySQL credentials.

---

## 🛠️ Configuration

SoupFFA is highly configurable. In the `config.yml` file, you can adjust:

- 📋 **Scoreboard Lines**  
  Use placeholders like `%player%`, `%kills%`, `%deaths%`, `%kd%`, `%coins%`

- 💬 **All Plugin Messages**  
  (Default language: German — feel free to translate!)

- 🧰 **Player Kit**  
  Customize inventory items, armor, and refill contents like soups

---

## 💬 Support / Contact

Have questions or need help? Feel free to open an issue or contact me via GitHub.  
If you enjoyed this plugin, consider giving it a ⭐️!

---
