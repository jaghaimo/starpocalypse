# Starpocalypse

This minimod makes the following changes to the campaign layer of Starsector:

1. Weapons and combat ships are scarce and highly regulated. As such, only Military Markets sell weapons, LPCs, and combat ships. Open Markets and Black Markets still sell civilian grade ships, albeit of inferior quality.
1. Contraband can happen, and when stability is low, some weapons and ships will show up in the Black Market.
1. Lawless factions like pirates and pathers do not submit to this rule of law. You can still find weapons and combat ships at their bases (both core and raider).
1. Factions are armed to the teeth. Any non-hidden market has at least an orbiting station, ground defences, and patrol HQ. Hidden bases (pirates and pathers raider bases) only get ground defences.
1. Your actions have consequences. When defeating a fleet your reputation with seemingly unrelated factions changes as well. Enemies of your enemy start to like you a bit, while their friends, less.
1. There is no such thing as a free lunch. Nobody gets random Nanoforges or Synchrotrons around. Not even your best modded faction.

## Implementation details

**Procgen Module**

1. Controls special item (like Nanoforge or Synchrotron) removal during procedural generation (new game).
1. Does this at 3 stages - on new game, after economy load, and after time pass.

On a rare occasion it could miss markets from mods that add special items after time pass, which can happen if the mod loads after this mod.
There is one final removal pass at the end of the first month of the player game (31 March 206) provided the game has not been reloaded.

**Engagement Module**

1. Any non-blacklisted factions, and only player-won engagements are considered for reputation adjustment.
1. Reputation adjustment is based on relationship between faction being adjusted and owner of the fleet you have beaten.
1. Maximum reputation adjustment is 1 for factions that are vengeful (or -1 for factions that are cooperative) to the owner of the fleet you have beaten.
1. For commissioned faction the max adjustment is 3 (or -3).

**Industry Module**

1. Ignore player owned markets altogether (do nothing). This also means autonomous colonies from Nexerelin.
1. Add Ground Defences to all non-player markets, raider bases included.
1. Additionally, add Orbital Station and Patrol HQ to all non-player, non-hidden markets that did not have them, or did not have any of their upgrades...
1. And make sure that the above two are met throughout your playthrough (via a transient listener).

**Submarket Module**

1. Remove all combat ships, weapons, and LPCs from open and black markets if it is not a pirate or luddic path market, and...
1. Add all removed weapons and LPCs back to Military Market, if there is one.
1. But when stability is low, some of them leak back to Black Market.
1. Finally, damage all pristine ships by putting one random d-mod on them.

## Configuration

All files can be found in `data/starpocalypse` folder.
Mods can apply changes and merges to default values by shipping the same folder with their version of CSV files.

**Procgen Module**

The list of factions affected by item removal rules are found in `itemRemoverFactions.csv`.
The list of items removed can be found in `itemRemoverItems.csv`.

**Engagement Module**

The blacklist file `engagementBlacklist.csv` controls which factions will NOT adjust their reputation of the player.

**Industry Module**

No configuration file for industries exists yet.

Two files regulate station additions (`station*.csv`): faction map which points which station tech to use depending on faction, and database file that is needed to prevent stations being added multiple times.

When using mods that add new stations, it is generally a good idea to add them all to the database even if you do not plan to use them in the faction map.

**Submarket Module**

Decision which factions and submarkets of that faction are regulated is made via `militaryRegulation*.csv` files.
Same submarkets and factions can additionally have contraband applied - see `militaryContraband.csv` for details.

Ship damager accepts faction and submarket, and is applied to all ships; see `shipDamage*.csv`.

Both faction and submarket files work as whitelist and accept: faction or submarket (allow) id, negated faction or submarket id (disallow), "all" keyword (allow all except negated).
