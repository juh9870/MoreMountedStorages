<p align="center"><img src="https://imgur.com/yrrR3Lf.gif" alt="Logo"></p>

### This mod is in a beta state, and some issues may arise while using it, so please report all issues you encounter.

**More Mounted Storages** mod allows for a greater variety of the modded item storages to be used as a part of Create moving contraptions, as well as providing an API for other mod makers to add their own types of mounted storages.

Currently, only item storages are supported, and fluid storages support won't be added until Create add some form of an API for them

## Currently supported mods:
- [Ender Storage](https://www.curseforge.com/minecraft/mc-mods/ender-storage-1-8): Ender Chest
- [Iron Chests](https://www.curseforge.com/minecraft/mc-mods/iron-chests): All chest types
- [Storage Drawers](https://www.curseforge.com/minecraft/mc-mods/storage-drawers): Both normal and compacting variants
- [Framed Compacting Drawers](https://www.curseforge.com/minecraft/mc-mods/framed-compacting-drawers): Same as above
- [Immersive Engineering](https://www.curseforge.com/minecraft/mc-mods/immersive-engineering): Crates
- [Industrial Foregoing](https://www.curseforge.com/minecraft/mc-mods/industrial-foregoing): Black Hole Unit and Black Hole Controller (items only)
- [PneumaticCraft: Repressurized](https://www.curseforge.com/minecraft/mc-mods/pneumaticcraft-repressurized): Smart chest fix, requires PnC v1.16.5-2.14.6-277 or above to function (mod will still load with older version of PnC installed, but fix won't be applied)
- [Expanded Storage](https://www.curseforge.com/minecraft/mc-mods/expanded-storage-forge): all chests and barrels types
- [Trash Cans](https://www.curseforge.com/minecraft/mc-mods/trash-cans): Trash Can and Ultimate Trash Can
- [EnderChests](https://www.curseforge.com/minecraft/mc-mods/enderchests): Ender Chest
- [DimStorage](https://www.curseforge.com/minecraft/mc-mods/dimstorage): Dimensional Chest

## Priority system:
Storages have different item insertion and retrieval priorities. By default, it goes in this order
1. Trash Can with whitelist filtering mode
2. Storage Drawers and Black Hole Units
3. Ender/Dimensional chests
4. All other chests/barrels/crates/etc
5. Trash Can with blacklist filtering mode

Priorities can be adjusted in mod configuration file
