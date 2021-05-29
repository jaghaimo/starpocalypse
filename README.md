# Starpocalypse

This minimod changes markets and submarkets work.

1. Weapons and combat ships are scarce and highly regulated. As such, only Military Markets sell weapons, LPCs, and combat ships. Open Markets and Black Markets still sell civilian grade ships, albeit of inferior quality.
1. Factions are armed to the teeth. Any non-hidden market has at least an orbiting station, ground defences, and patrol HQ.
1. Raider bases (pirates and pathers alike) also get ground defences.

## Implementation details

1. Ignore player owned markets altogether (do nothing). This also means autonomous colonies from Nexerelin.
1. Add Ground Defenses to all non-player markets, raider bases included.
1. Addtionally, add Orbiting Station and Patrol HQ to all non-player, non-hidden markets that did not have them, or did not have any of their upgrades...
1. And make sure that the above two are met throughout your playthrough.
1. Remove all combat ships, weapons, and LPCs from open and black markets if it is not a pirate or luddic path market, and...
1. Add all removed  weapons and LPCs to Military Market, if there is one.

## Configuration

* `factionIndustries.csv` determines which faction will get industries that are missing.
* `factionShipDamager.csv` determines which markets will have pristine ships damaged.
* `factionShipRemover.csv` determines which markets will have combat ships removed or moved.
* `factionStations.csv` determines which station will be added to which market that is missing a station.
* `factionWeapons.csv` determines which markets will have weapons and LPCs removed or moved.
* `stationDatabase.csv` a list of all stations, needed to avoid double stations.
* `submarketChanger.csv` determines which submarkets can be modifed.
