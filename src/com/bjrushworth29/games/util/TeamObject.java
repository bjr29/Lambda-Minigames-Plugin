package com.bjrushworth29.games.util;

import org.bukkit.scoreboard.Team;

public final class TeamObject<T> {
    private final T object;
    private final Team team;
    private final boolean teamSpecific;
    private final boolean enabled;

    public TeamObject(T object, Team team, boolean teamSpecific, boolean enabled) {
        this.object = object;
        this.team = team;
        this.teamSpecific = teamSpecific;
        this.enabled = enabled;
    }

    public T getObject() {
        return object;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isTeamSpecific() {
        return teamSpecific;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
