# Minecraft Bot
## Rundown:
This bot retrieves in-game data using the F3 data screen, and is limited to 
non-diagonal movement. To run this bot, you must first have Minecraft already
open and set to the specifications found in the [In-game Settings](#in-game-settings-).
As this is a for-fun, home-brewed program, only one screen resolution is currently
accepted, as it produces non-blurry screenshots on my supposed 1080p screen. Not sure
why that is.

The program is not yet user-ready, so for the time being, you must add commands for
your bot in "src/main/java/org.mcbot/**App.java**". Some examples can be found there.
Presently, you are given 4 seconds to re-open Minecraft, or it will crash. Get used to it,
change it, or get wrecked I guess.

## Rant:
Obviously, the limitations applied to this program prevent a lot of things, but hey,
those things aren't that important. Things like looking like a real player, multi-block
scanning, and combat? We don't need these things. We just need a robot that can
accomplish simple tasks and reach simple-to-reach places. Also yes, I also noticed that
movement is jittery. This is a result of a program that does not directly interact with
the program it works in.

Will I one day create a bot that does have fluid movement and does directly interact
with Minecraft's API and can fight effectively against both mobs and other players?
Probably not, unless someone pays me ;)

## In-game Settings:
- Resolution: 1600x900@60 (24 bit)
- GUI Scale: 3
- Set to Fullscreen
- Render Distance: 2 (For RAM freeing)
- Mouse Sensitivity: 30
- Auto-jump: OFF
- Default controls
- DO NOT USE OPTIFINE (you only see the inventory in all screenshots)

All other settings should be Minecraft's default settings.
## Computer Settings:
- Make sure Fn Lock is on. You can test this by pressing the volume button.
It should not change the volume. You can turn on Fn Lock by pressing Fn + esc
- Make sure it is not on battery saver mode, or the GPU will not be used by
Minecraft.
## Ideas to be implemented:
1. Mining
2. Path-finding
3. Long-jumping
4. Text Item Database
4. Swimming
5. Attacking
6. Interactable Class
7. Decision-making Class
8. Directions Class
9. Updated Item Class
- action affirmation
- retrieving items from known locations
- building
- crafting
- attacking (different from combat)
- build schematics
- Image Pre-processing
## Ideas that are out of scope:
- Use of vehicles
- Picking the best available tools
- Diagonal Paths
- Reading slanted words (such as renamed items)