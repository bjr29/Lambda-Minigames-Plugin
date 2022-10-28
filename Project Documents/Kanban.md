---

kanban-plugin: basic

---

## Back Burner

- [ ] Map no build/ break zones<br><br>#feature #priority-high
- [ ] Bed breaking system<br><br>#feature #priority-high
- [ ] Team system<br><br>#feature #priority-medium
- [ ] Bed fight game mode<br><br>- [ ] Basic map<br><br>#feature #game-mode #priority-medium
- [ ] Wait for all players to be fully loaded<br><br>#feature #priority-medium
- [ ] Bridge game mode<br><br>#feature #game-mode #building #priority-medium
- [ ] More sumo maps<br><br>#building #priority-low


## Todo

- [ ] Duels game mode<br><br>#feature #game-mode #priority-high


## In Progress

- [ ] Refine knockback values<br><br>#adjustment #priority-high
- [ ] Lobby map<br><br>#building #priority-medium


## Testing

- [ ] Rework world system<br>- [x] Copy map<br>- [x] Delete after used<br>- [x] Create map in correct place<br>- [ ] Create map in correct place with multiple games active<br>- [x] Correctly apply block state<br><br>#feature #priority-highest
- [ ] Fix knockback<br>- [ ] Applies at all<br>- [ ] Applies consistently<br><br>#bug #priority-highest
- [ ] Fix teleporting incorrectly at the end of a game<br><br>#bug #priority-highest
- [ ] Leaving the game before it starts<br>- [x] 2 players<br>- [ ] \>2 players<br><br>#feature #priority-high


## Beta 1

**Complete**
- [x] Spawn player on team side of map<br><br>#feature #priority-highest
- [x] Correct combat system<br><br>#bug #priority-highest
- [x] PVP<br>- [x] Within lobby<br>- [x] Inside game<br>- [x] Before/ after game<br>- [x] Inside game with PVP disabled<br><br>#feature #priority-highest
- [x] Prevent items from being dropped<br><br>#bug #priority-highest
- [x] Leave queue<br><br>#feature #priority-highest
- [x] Queuing System with multiple players<br>- [x] When players stay<br>- [x] When player a player leaves<br>- [x] When all players leave<br><br>#feature #priority-highest
- [x] Prevent glitchy movement while waiting to start game<br><br>#bug #priority-high
- [x] Sumo game mode<br>- [x] Basic map<br><br>#feature #game-mode #building #priority-high
- [x] Prevent game starting if the queue becomes empty<br><br>#bug #priority-medium
- [x] Spectator player constraint<br><br>#feature #priority-medium
- [x] Remove test game<br>- [x] World<br>- [x] Game type<br><br>#feature #priority-medium
- [x] Player lives system<br><br>#feature #priority-medium
- [x] Kill y level per map<br><br>#feature #priority-medium
- [x] Preventing the player joining a different game while in a game<br><br>#bug #priority-low
- [x] Enums for names in managers<br><br>#refactor #priority-low
- [x] Prevent being stuck after dying<br><br>#bug #priority-low




%% kanban:settings
```
{"kanban-plugin":"basic"}
```
%%