# Starpocalypse

This minimod changes markets and submarkets work.

1. Weapons and combat ships are scarce and highly regulated. As such, only Military Markets sell weapons, LPCs, and combat ships. Open Markets and Black Markets still sell civilian grade ships, albeit of inferior quality.
1. Factions are armed to the teeth. Any non-hidden market has at least an orbiting station, ground defences, and patrol HQ.
1. Raider bases (pirates and pathers alike) also get ground defences.

## Implementation details

1. Ignore player owned markets altogether (do nothing). This also means autonomous colonies from Nexerelin.
1. Add Ground Defenses to all non-player markets, raider bases included.
1. Addtionally, add Orbiting Station and Patrol HQ to all non-player, non-hidden markets that did not have them, or did not have any of their upgrades...
1. And make sure that the above two are met throughout your playthrough (via a transient listener).
1. Remove all combat ships, weapons, and LPCs from open and black markets if it is not a pirate or luddic path market, and...
1. Add all removed weapons and LPCs back to Military Market, if there is one.
1. Damage all pristine ships by putting one random d-mod on them.

## Configuration

### Industries

No configuration file for industries exists yet.

### Stations

Two files regulate station additions: faction map which points which station tech to use depending on faction, and database file that is needed to prevent stations being added multiple times.

When using mods that add new stations it is generally a good idea to add them all to the database even if you do not plan to use them in the faction map.

### Submarkets

Configuration for weapon removal, ship removal and ship damager is split between two files: faction and submarket lists which decide which submarket gets modified.

Both faction and submarket files work as whitelist and accept: faction or submarket (allow) id, negated faction or submarket id (disallow), "all" keyword (allow except for negated entries).

#### Example

Damage ships in markets belonging to all factions, but only in open market:

```sh
# shipDamageFaction.csv
faction
all
!player

# shipDamageSubmarket.csv
submarket
open_market
```
