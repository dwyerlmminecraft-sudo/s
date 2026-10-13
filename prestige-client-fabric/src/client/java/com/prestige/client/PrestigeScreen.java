package com.prestige.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/** The Prestige menu: sidebar, HUD modules, configs, themes and settings. */
public final class PrestigeScreen extends Screen {
    private record Hit(int x, int y, int w, int h, Runnable action) {
        boolean contains(double px, double py) {
            return px >= x && px < x + w && py >= y && py < y + h;
        }
    }

    private static final String[] TABS = {"Modules", "Configs", "Themes", "Settings"};
    private static final String[] SUBS = {"Toggle module catalogue", "Save and load setups", "Pick your accent", "Customize the client"};
    private static final String[] CROWN = {
            "X.....X.....X",
            "XX...XXX...XX",
            "XXX.XXXXX.XXX",
            "XXXXXXXXXXXXX",
            "XXXXXXXXXXXXX",
            ".............",
            "XXXXXXXXXXXXX",
            "XXXXXXXXXXXXX"};
    private static final int SIDE = 128;
    private static final int MUTE = 0xFF7D8BAB;
    private static final int WHITE = 0xFFFFFFFF;

    private final List<Hit> hits = new ArrayList<>();
    private int tab = 0;
    private int moduleCategory = 0;
    private int blue;
    private int ice;
    private int mx;
    private int my;

    public PrestigeScreen() {
        super(Component.literal("Prestige Client V2"));
    }

    // ---------- input ----------

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == 0) {
            for (int i = hits.size() - 1; i >= 0; i--) {
                Hit hit = hits.get(i);
                if (hit.contains(event.x(), event.y())) {
                    hit.action().run();
                    return true;
                }
            }
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (PrestigeClient.openKey != null && PrestigeClient.openKey.matches(event)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(event);
    }

    // ---------- drawing ----------

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
        super.extractRenderState(g, mouseX, mouseY, delta);
        hits.clear();
        mx = mouseX;
        my = mouseY;

        PrestigeConfig c = PrestigeConfig.get();
        Theme theme = Theme.byName(c.theme);
        blue = 0xFF000000 | theme.blue();
        ice = 0xFF000000 | theme.ice();

        int w = Math.min(540, this.width - 16);
        int h = Math.min(320, this.height - 16);
        int x0 = (this.width - w) / 2;
        int y0 = (this.height - h) / 2;

        // window
        g.fill(x0 - 1, y0 - 1, x0 + w + 1, y0 + h + 1, a(0x90, blue));
        g.fill(x0, y0, x0 + w, y0 + h, 0xF00A0F1E);
        g.fill(x0, y0, x0 + SIDE, y0 + h, 0xFF080C18);
        g.fill(x0 + SIDE, y0, x0 + SIDE + 1, y0 + h, 0xFF1B2540);

        // logo
        crown(g, x0 + 12, y0 + 12);
        text(g, "Prestige", x0 + 46, y0 + 13, WHITE);
        text(g, "Client V2", x0 + 46, y0 + 24, MUTE);

        // sidebar tabs
        for (int i = 0; i < TABS.length; i++) {
            int ty = y0 + 50 + i * 34;
            boolean active = i == tab;
            boolean hover = in(x0 + 8, ty, SIDE - 16, 30);
            g.fill(x0 + 8, ty, x0 + SIDE - 8, ty + 30, active ? a(0x40, blue) : hover ? 0x18FFFFFF : 0x00000000);
            if (active) {
                g.fill(x0 + 8, ty, x0 + 10, ty + 30, ice);
            }
            text(g, TABS[i], x0 + 18, ty + 6, active ? WHITE : 0xFFC4CFE6);
            text(g, SUBS[i], x0 + 18, ty + 17, MUTE);
            final int index = i;
            hits.add(new Hit(x0 + 8, ty, SIDE - 16, 30, () -> tab = index));
        }

        // sidebar status
        g.fill(x0 + 12, y0 + h - 22, x0 + 17, y0 + h - 17, 0xFF3DDC84);
        text(g, "Menu key closes", x0 + 23, y0 + h - 23, MUTE);

        // header
        int cx = x0 + SIDE + 14;
        int cw = w - SIDE - 28;
        text(g, TABS[tab], cx, y0 + 12, WHITE);
        text(g, SUBS[tab], cx, y0 + 24, MUTE);
        button(g, x0 + w - 60, y0 + 8, 50, 16, "Close", false, this::onClose);

        int cy = y0 + 44;
        if (tab == 0) {
            pageHud(g, c, cx, cy, cw);
        } else if (tab == 1) {
            pageConfigs(g, c, cx, cy, cw);
        } else if (tab == 2) {
            pageThemes(g, c, cx, cy, cw);
        } else {
            pageSettings(g, c, cx, cy, cw);
        }
    }

    private void pageHud(GuiGraphicsExtractor g, PrestigeConfig c, int cx, int cy, int cw) {
        String[] categories = Module.categories();
        int chipGap = 3;
        int chipW = (cw - chipGap * (categories.length - 1)) / categories.length;
        for (int i = 0; i < categories.length; i++) {
            final int index = i;
            int x = cx + i * (chipW + chipGap);
            button(g, x, cy, chipW, 18, categories[i], moduleCategory == i, () -> moduleCategory = index);
        }
        Module[] all = Module.values();
        List<Module> modules = new ArrayList<>();
        for (Module m : all) {
            if (m.category.equals(categories[moduleCategory])) modules.add(m);
        }
        int startY = cy + 25;
        int cardW = (cw - 8) / 2;
        int cardH = 42;
        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            int x = cx + (i % 2) * (cardW + 8);
            int y = startY + (i / 2) * (cardH + 5);
            if (y + cardH > this.height - 12) break;
            card(g, x, y, cardW, cardH, m.title, m.description, c.isOn(m), () -> c.toggle(m));
        }
        text(g, "Module toggles are a configurable catalogue; gameplay automation is not implemented.", cx, this.height - 22, MUTE);
    }

    private void pageConfigs(GuiGraphicsExtractor g, PrestigeConfig c, int cx, int cy, int cw) {
        button(g, cx, cy, 130, 18, "+ Save current setup", true, c::saveProfile);
        text(g, c.profiles.size() + "/" + PrestigeConfig.MAX_PROFILES + " saved", cx + 138, cy + 5, MUTE);
        if (c.profiles.isEmpty()) {
            text(g, "No saved configs yet. Save your setup to reuse it later.", cx, cy + 32, MUTE);
            return;
        }
        int row = 0;
        for (String name : new ArrayList<>(c.profiles.keySet())) {
            int y = cy + 28 + row * 26;
            row++;
            box(g, cx, y, cw, 22, 0x14FFFFFF, 0x20FFFFFF);
            text(g, name, cx + 8, y + 7, WHITE);
            button(g, cx + cw - 104, y + 2, 48, 18, "Apply", true, () -> c.applyProfile(name));
            button(g, cx + cw - 52, y + 2, 48, 18, "Delete", false, () -> c.deleteProfile(name));
        }
    }

    private void pageThemes(GuiGraphicsExtractor g, PrestigeConfig c, int cx, int cy, int cw) {
        int w = (cw - 16) / 3;
        int h = 40;
        for (int i = 0; i < Theme.ALL.size(); i++) {
            Theme t = Theme.ALL.get(i);
            int x = cx + (i % 3) * (w + 8);
            int y = cy + (i / 3) * (h + 8);
            boolean selected = t.name().equals(c.theme);
            boolean hover = in(x, y, w, h);
            box(g, x, y, w, h, selected ? a(0x40, blue) : hover ? a(0x28, blue) : 0x14FFFFFF,
                    selected ? a(0xA0, ice) : 0x20FFFFFF);
            g.fill(x + 8, y + 8, x + 32, y + 20, 0xFF000000 | t.blue());
            g.fill(x + 8, y + 20, x + 32, y + 32, 0xFF000000 | t.ice());
            text(g, t.name(), x + 40, y + 16, WHITE);
            hits.add(new Hit(x, y, w, h, () -> {
                c.theme = t.name();
                c.save();
            }));
        }
    }

    private void pageSettings(GuiGraphicsExtractor g, PrestigeConfig c, int cx, int cy, int cw) {
        card(g, cx, cy, cw, 46, "Text shadow", "Draw a drop shadow behind HUD text.", c.shadow, () -> {
            c.shadow = !c.shadow;
            c.save();
        });
        card(g, cx, cy + 54, cw, 46, "Watermark", "Show the Prestige V2 label at the top-left of the HUD.", c.watermark, () -> {
            c.watermark = !c.watermark;
            c.save();
        });
    }

    // ---------- widgets ----------

    private void card(GuiGraphicsExtractor g, int x, int y, int w, int h, String title, String desc,
                      boolean on, Runnable click) {
        boolean hover = in(x, y, w, h);
        box(g, x, y, w, h, on ? a(0x40, blue) : hover ? a(0x28, blue) : 0x14FFFFFF, on ? a(0xA0, ice) : 0x20FFFFFF);
        text(g, title, x + 8, y + 6, WHITE);
        int ty = y + 19;
        for (String line : wrap(desc, w - 52)) {
            if (ty > y + h - 10) {
                break;
            }
            text(g, line, x + 8, ty, MUTE);
            ty += 10;
        }
        int sx = x + w - 30;
        int sy = y + h / 2 - 5;
        g.fill(sx, sy, sx + 22, sy + 10, on ? blue : 0xFF1A2646);
        g.fill(on ? sx + 13 : sx + 2, sy + 2, on ? sx + 20 : sx + 9, sy + 8, on ? WHITE : MUTE);
        hits.add(new Hit(x, y, w, h, click));
    }

    private void button(GuiGraphicsExtractor g, int x, int y, int w, int h, String label, boolean primary, Runnable action) {
        boolean hover = in(x, y, w, h);
        box(g, x, y, w, h,
                primary ? a(hover ? 0xFF : 0xCC, blue) : hover ? a(0x40, ice) : 0x18FFFFFF,
                primary ? a(0xFF, ice) : 0x30FFFFFF);
        text(g, label, x + (w - this.font.width(label)) / 2, y + (h - this.font.lineHeight) / 2 + 1, WHITE);
        hits.add(new Hit(x, y, w, h, action));
    }

    private void box(GuiGraphicsExtractor g, int x, int y, int w, int h, int fill, int border) {
        g.fill(x, y, x + w, y + h, border);
        g.fill(x + 1, y + 1, x + w - 1, y + h - 1, fill);
    }

    private void crown(GuiGraphicsExtractor g, int x, int y) {
        for (int row = 0; row < CROWN.length; row++) {
            int color = lerp(ice, blue, row / (float) (CROWN.length - 1));
            for (int col = 0; col < CROWN[row].length(); col++) {
                if (CROWN[row].charAt(col) == 'X') {
                    g.fill(x + col * 2, y + row * 2, x + col * 2 + 2, y + row * 2 + 2, color);
                }
            }
        }
    }

    private void text(GuiGraphicsExtractor g, String s, int x, int y, int color) {
        g.text(this.font, s, x, y, color, false);
    }

    private List<String> wrap(String s, int maxWidth) {
        List<String> out = new ArrayList<>();
        String line = "";
        for (String word : s.split(" ")) {
            String test = line.isEmpty() ? word : line + " " + word;
            if (this.font.width(test) > maxWidth && !line.isEmpty()) {
                out.add(line);
                line = word;
            } else {
                line = test;
            }
        }
        out.add(line);
        return out;
    }

    private boolean in(int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static int a(int alpha, int rgb) {
        return (alpha << 24) | (rgb & 0xFFFFFF);
    }

    private static int lerp(int c1, int c2, float t) {
        int r = (int) (((c1 >> 16) & 255) * (1 - t) + ((c2 >> 16) & 255) * t);
        int gr = (int) (((c1 >> 8) & 255) * (1 - t) + ((c2 >> 8) & 255) * t);
        int b = (int) ((c1 & 255) * (1 - t) + (c2 & 255) * t);
        return 0xFF000000 | (r << 16) | (gr << 8) | b;
    }
}
