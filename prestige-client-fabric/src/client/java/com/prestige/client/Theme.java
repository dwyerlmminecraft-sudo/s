package com.prestige.client;

import java.util.List;

/** An accent theme: two RGB colors (no alpha). */
public record Theme(String name, int blue, int ice) {
    public static final List<Theme> ALL = List.of(
            new Theme("Royal", 0x3D8BFF, 0x6FD6FF),
            new Theme("Ice", 0x38BDF8, 0xA5F3FC),
            new Theme("Neon", 0x2F6BFF, 0x00E5FF),
            new Theme("Midnight", 0x5B6CFF, 0x9AA8FF),
            new Theme("Violet", 0x8B5CF6, 0xC4B5FD),
            new Theme("Emerald", 0x10B981, 0x6EE7B7));

    public static Theme byName(String name) {
        for (Theme t : ALL) {
            if (t.name().equals(name)) {
                return t;
            }
        }
        return ALL.get(0);
    }
}
