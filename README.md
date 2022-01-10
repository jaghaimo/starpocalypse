# Starpocalypse

This mod makes the following changes to the campaign layer of Starsector:

1. Weapons and combat ships are scarce and highly regulated. As such, only Military Markets (and those pesky Black
   Markets) sell weapons, LPCs, modspecs, and combat ships. Open Markets still sell civilian grade ships.
1. Lawless factions (e.g. pirates and pathers) and independents do not submit to this rule of law. You can still find
   weapons and combat ships at their bases (both core and raider).
1. There are no pristine ships any more, everything is d-modded. Including starting fleet.
1. Contraband can happen, and when stability is low, some weapons and ships "disappear" from Military Market and
   "magically" show up in the Black Market instead.
1. Factions are armed to the teeth. Any non-hidden market has at least an orbiting station, ground defences, and patrol
   HQ. Hidden bases (pirates and pathers raider bases) only get ground defences.
1. Your actions have consequences. When defeating a fleet, your reputation with seemingly unrelated factions changes as
   well. Enemies of your enemy start to like you a bit, while their friends, less.
1. Similarly, targetting a any Nanoforge or Synchrotron Core will be deemed as an act of war.

All of these changes are optional, and can be disabled via `starpocalypse.json`.

## Implementation details

Every module can be disabled at will, see `starpocalypse.json`.
Additional configuration files can be found in `settings/` folder.
Mods can apply changes and merges to default values by shipping the same folder with their version of CSV files.

### Changes to markets

1. Ignore player owned markets altogether (do nothing). This also means autonomous colonies from Nexerelin.
1. Add Ground Defences to all non-player markets, raider bases included.
1. Additionally, add Orbital Station and Patrol HQ to all non-player, non-hidden markets that did not have them, or did
   not have any of their upgrades...
1. And make sure that the above two are met at all times (via a transient listener).

Two files regulate station additions (`station*.csv`): faction map which points which station tech to use depending on
faction, and database file that is needed to prevent stations being added multiple times.

When using mods that add new stations, it is generally a good idea to add them all to the database even if you do not
plan to use them in the faction map.

### Hostile action repercussions

1. Any non-blacklisted factions, and only player-won engagements are considered for reputation adjustment.
1. Reputation adjustment is based on relationship between faction being adjusted and owner of the fleet you have beaten.
1. Maximum reputation adjustment is 1 for factions that are vengeful (or -1 for factions that are cooperative) to the
   owner of the fleet you have beaten.
1. For commissioned faction the max adjustment is +/-3.
1. Stealing a Nanoforge or Synchrotron instantly sets your reputation to -1 (0).

The blacklist file `reputationBlacklist.csv` controls which factions will NOT adjust their reputation of the player.
The list of raid-protected items (special item ids) is present in `raidProtectorItem.csv`.

### Submarket changes

Main module:

1. Remove all combat ships, weapons, LPCs, and modspecs from open markets if it is not a pirate or Luddic Path market,
   and...
1. Add all removed items back to Military Market, if there is one, but...
1. When the stability is low, some of them will leak back to Black Market.
1. Finally, damage all pristine ships by putting a random number of d-mods.

Decision which factions and submarkets of that faction are regulated is made via `militaryRegulation*.csv` files.
Same submarkets and factions can additionally have contraband applied - see `militaryContrabandPool.csv` for details.
Finally, exclusion lists can be applied to both `regulations` and `contraband` behaviour - see `military*Blacklist.csv`.

Ship damager accepts faction and submarket, and is applied to all ships. It is controlled by `shipDamage*.csv`.

Both faction and submarket files work as whitelist and accept: faction or submarket (allow) id, negated faction or
submarket id (disallow), "all" keyword (allow all except negated).
