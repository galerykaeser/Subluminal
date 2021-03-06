# Game Rules

- Possibly 2-n players, for the moment we decided the maximum number of players will be 8.
- At the beginning of a game, the map is created randomly.
- You lose the game when your mother ship does not exist anymore.
- You win the game when your mother ship is the only remaining mother ship on the map.
- Starting positions of the players are assigned randomly, but in a way no player has a big advantage or disadvantage.
- Every player is able to see the whole map.
- The map consists of stars, which also are solar systems. At the beginning of a game every player's mother ship is randomly assigned to a star.
- Each star has its own attributes, which influence:  
	a) The frequency of production of new regular ships.
	b) The basic defense of a star against intruders.
	c) How much effort it takes to colonize the star.
- If a player colonizes a new star, the star automatically starts producing regular ships for the player, which are local to that star.
- The regular ships that are local to a star that is owned by a player can be sent out by the player.
- Every order to move regular ships from one star to another originate from the mother ship.
- Each star also has an attribute that regulates the maximum radius a ship can reach without having to fuel up on another star.
- The rule above implicates	that further destinations can only be reached by one's regular ships by "hopping" from star to star to be able to fuel up.
- "Hopping" can occur on both neutral and owned (colonized) stars.
- By hopping onto a star that is owned by an opponent, the hopping player gets punished by automatic tear-down of a certain percentage of his hopping ships.
- If a player targets a certain star he or she can influence the hopping path his or her ships or use the one that is automatically calculated by the system, which is the shortest.
- Hopping stars and colonizing stars are two separate processes. Accidental colonization while hopping a star can not occur.
- When regular ships reach the target they hopped to, two scenarios can happen:
	a) Nobody is there yet, the star is neutral. The player's regular ships start colonizing the star. The speed of the colonization progress depends from the number of regular ships the player sent, the basic defense of the star and how much effort the star takes to be colonized.
	b) The star is already under possession of an opponent. In this case the fleets of the opponents start tearing down themselves, until only regular ships of one of the fleets remain. If the remaining regular ships belong to the intruder, they immediately start neutralizing the star to then colonize it. If the remaining regular ships belong to the previous owner of the star, nothing else happens.
- Every move one's regular ships make originates from the mother ship. Thus, the order takes longer to get to the concerning regular ships, the further their base star is away from the mother ship.
- When a player orders a fleet to go colonize another star, the information about the number of available regular ships is always outdated (because of the distance). So the player has to options:
	a) He/She sends out the order that a certain percentage of the regular ships should leave their base star.
	b) He/She sends out the order that a certain number or regular ships should be the maximum number of sent out ships. If there are not that many regular ships left the moment the order arrives at the base, all regular ships are sent out.
- Fleets of regular ships that are hopping to their target send out information to their mother ship about their position in regular intervals.
- The mother ship can also be moved from her base, it's slower than the regular ships.
- The mother ship has an own basic defense value.
- If the base of the mother ship is intruded, the mother ship is the last one to be torn down.
- Every mother ship and every regular ship broadcast their position so that every player can see it.