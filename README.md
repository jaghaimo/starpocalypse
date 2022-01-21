# Starpocalypse

This mod makes the following changes to the campaign layer of Starsector:

1. Weapons and combat ships are scarce and highly regulated. As such, only Military Markets (and those pesky Black
   Markets) sell higher tier weapons, LPCs, modspecs, and combat ships. Open Markets still sell civilian grade ships
   and low tier combat ships and items.
1. Lawless factions (e.g. pirates and pathers) and independents do not submit to this rule of law. You can still find
   high tier weapons and combat ships at their bases (both core and raider).
1. There are no pristine ships any more, everything is d-modded. Including your starting fleet.
1. Contraband can happen, and when stability is low, some weapons and ships "disappear" from Military Market and
   "magically" show up in the Black Market instead.
1. Access to most Black Markets is impossible while legally docked at the station. Speaking of access, you will need to
   use a fence who will ask for a cut.
1. Factions are armed to the teeth. Any non-hidden market has at least an orbiting station, ground defences, and patrol
   HQ. Hidden bases (pirates and pathers raider bases) only get ground defences.
1. Your actions have consequences. When defeating a fleet, your reputation with seemingly unrelated factions changes as
   well. Enemies of your enemy start to like you a bit, while their friends, less.
1. Similarly, targetting a any colony item will be deemed as an act of war.

All changes are optional, and can be disabled via `starpocalypse.json`.

_Important!_ As of Starpocalypse 2.2.0, the mod is no longer save to disable. In order to remove Starpocalypse from
a save game, delete `starpocalypse/data` folder, load, and finally save the game.

## Implementation details

Every change can be disabled at will, see `starpocalypse.json`.
Additional configuration files can be found in `settings/` folder.
Mods can apply changes and merges to default values by shipping the same folder with their version of CSV files.

### Changes to markets

1. Ignore player owned markets altogether (do nothing). This also means autonomous colonies from Nexerelin.
1. Add Ground Defenses to all non-player markets, raider bases included.
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
1. Stealing a colony item instantly sets your reputation to -1 (hostile).

The blacklist file `reputationBlacklist.csv` controls which factions will NOT adjust their reputation of the player.
The list of raid-protected items (special item ids) is present in `raidProtectorItem.csv`.

### Submarket changes

1. Remove larger combat ships (>5 FP), and high tier (>0) weapons, LPCs, and modspecs from open markets if it is not a
   pirate or Luddic Path market.
1. When the stability is low, some of the initially illegal items and ships on Military Market will become legal.
1. Finally, damage all pristine ships by putting a random number of d-mods.

Decision which factions have their Open Market regulated regulated is made via `militaryRegulationFaction.csv` file.
Same submarkets and factions can additionally have contraband applied to their Military Market in
`militaryRegulationsStability.csv`.
Finally, exclusion lists can be applied to regulations - see `militaryRegulationsLegal.csv`.

Ship damager accepts faction and submarket, and is applied to all ships. It is controlled by `shipDamage*.csv`.

Both faction and submarket files work as whitelist and accept: faction or submarket (allow) id, negated faction or
submarket id (disallow), "all" keyword (allow all except negated).

#### Black Market

Black Market mechanics are slightly tweaked to make it less of a go-to market for everything.
Factions that regulate their open markets, and independents, will not allow you to trade on Black Market.
As such, you will have to illegally dock at those stations (transponder off).
On top of that, a fence fee (1/3rd of the market default tariff) will be required to pay for any transactions.
