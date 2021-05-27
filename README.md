# Starpocalypse

This minimod changes markets and submarkets work.

1. Weapons and combat ships are scarce and highly regulated. As such, only Military Markets sell weapons, LPCs, and combat ships. Open Markets and Black Markets still sell civilian grade ships, albeit of inferior quality.
1. Factions are armed to the teeth. Any non-hidden market has at least an orbiting station and patrol HQ.

## Implementation details

1. Ignore player owned markets altogether (do nothing).
1. Add Ground Defenses to all non-player markets.
1. Addtionally, add Orbiting Stations and Patrol HQ to all non-player, non-hidden markets that did not have them, or did not have any of their upgrades.
1. Remove all combat ships, weapons, and LPCs from open and black markets if it is not a pirate or luddic path market, and...
1. Add all removed (if any) weapons and LPCs to Military Market, if there is one.
