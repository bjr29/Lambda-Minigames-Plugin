package com.bjrushworth29.utils;

import org.bukkit.scoreboard.Team;

public record TeamObject<T>(T object, Team team, boolean teamSpecific, boolean enabled) { }
