Item Assumptions:
1. If duplicate items present in inventory, use the ones that appear first.
2. Bombs are placed on same position as player on the same layer.
3. Swords and Bows can be used in combination ie. if both are present in the inventory, character attacks twice with higher attack.
4. Duplicate weapons cannot be stacked. For example, if character has multiple swords, dmg bonus applies only once, and uses
   durability of one sword.
5. Using an item decreases its durability by 1.
6. Potions have a 'duration' of multiple ticks, and invincibility does not end after one battle.
7. There is a 10% chance to drop the one ring after a battle.
8. If the player who has an armour get another armour, the durability of his armour should increase to the initial durability.
9. There is a 30% chance to drop an armour after a fight with zombies or mercenaries.
10. Initial durability of an armour and a sword are 7.
11. Upon interacting with a mercenary/assassin, with both sceptre and bribe amount, prioritise using the sceptre.
12. Midnight armour has a durability of 12, and adds 12 attack damage. Its damage decrease is same as regular armour.
13. Anduril has twice the durability of a normal sword, and triple the character's damage instead of doubling it.
14. When bribing a mercenary, usage priority will be given to whatever item appears first in the inventory.
15. The zombie toast spawner can only be destroyed with a sword.
16. Bomb explodes the tick after the switch is activated/placed near active switch.

Dungeon Mania Controller Assumptions:
1. The id of dungeons is defined "dungeon" concatenated with current dungeon number.
2. The id of entities is defined as entities type concatenated with current entities number.
3. Function saveGame would throw IllegalArgumentException if name already exist.
4. saveGame will save the game in JSON format under directory "src/main/java/dungeonmania/save".

Battle Assumptions:
1. The character and enemy takes damage at the same time in battles.
2. Allies attack enemies after the character attacks during a battle.
3. If an ally kills an enemy with armour, the character receives the enemy's armour.
4. If an ally with armour is killed by an enemy, the character receives the ally's armour.
5. In peaceful game mode, the player also does not attack enemies so battles do not occur.
6. Mercenaries spawn at the same time as zombie_toasts from spawners(this includes tick decrease in hard mode).
7. Entry location is interpreted as player's spawn location.
8. Mercenaries will only stay 1 tile away from player, if they are an ally.
9. A hydra's health cannot exceed its initial health.