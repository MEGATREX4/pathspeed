# PathSpeed Mod

PathSpeed is a Minecraft Fabric mod that allows server administrators and modpack creators to customize player movement speeds based on the type of blocks they are standing on. With this mod, you can configure different speed multipliers for various blocks, enhancing gameplay and adding new challenges to your Minecraft world.

## Features

- **Customizable Movement Speed:** Adjust the speed multipliers for various blocks.
- **Server Commands:** Reload the configuration without restarting the server.
- **Easy Configuration:** Use a JSON configuration file to set block speeds.

## Installation

1. **Download and Install Fabric:** Ensure you have Fabric installed for Minecraft 1.21+.
   - [Fabric Installer](https://fabricmc.net/use/)

2. **Download the Mod:** Get the latest version of PathSpeed from the Releases page.

3. **Place the Mod File:** Move the downloaded `.jar` file into the `mods` folder of your Minecraft directory.

4. **Run Minecraft:** Start Minecraft with the Fabric loader.

## Configuration

PathSpeed uses a JSON configuration file located at `config/movement_speed_config.json`. Here you can set different speed multipliers for blocks. The default configuration includes:

```json
{
  "minecraft:dirt_path": 2.0,
  "minecraft:sand": 1.0
}
```
Commands

    /reloadMovementConfig - Reloads the configuration file without restarting the server.

Usage

Once installed, PathSpeed will automatically adjust player movement speed based on the blocks they are standing on. Use the /reloadMovementConfig command to update the configuration without restarting the server.
Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

    Fork the repository
    Create a new branch (git checkout -b feature/your-feature)
    Commit your changes (git commit -am 'Add new feature')
    Push to the branch (git push origin feature/your-feature)
    Create a new Pull Request
