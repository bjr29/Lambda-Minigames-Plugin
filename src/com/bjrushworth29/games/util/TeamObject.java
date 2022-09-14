package com.bjrushworth29.games.util;

import org.bukkit.scoreboard.Team;

public record TeamObject<T>(T object, Team team, boolean teamSpecific, boolean enabled) { }
