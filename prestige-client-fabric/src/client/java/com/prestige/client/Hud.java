package com.prestige.client;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Draws the on-screen overlay. Read-only: it never sends input or changes game state. */
public final class Hud {
    private static int frames;
    private static int fps;
    private static long windowStart = System.nanoTime();
    private static boolean lastLeft;
    private static boolean lastRight;
    private static final ArrayDeque<Long> LEFT_CLICKS = new ArrayDeque<>();
    private static final ArrayDeque<Long> RIGHT_CLICKS = new ArrayDeque<>();

    private Hud() {
    }

    public static void render(GuiGraphicsExtractor g, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        sample(mc);

        PrestigeConfig c = PrestigeConfig.get();
        Theme theme = Theme.byName(c.theme);
        int blue = 0xFF000000 | theme.blue();
        int ice = 0xFF000000 | theme.ice();
        Font font = mc.font;

        int x = 6;
        int y = 6;
        if (c.watermark) {
            y = chip(g, font, "Prestige V2", x, y, ice, c.shadow);
        }
        if (c.isOn(Module.FPS)) {
            y = chip(g, font, fps + " FPS", x, y, blue, c.shadow);
        }
        if (c.isOn(Module.COORDINATES)) {
            String xyz = String.format(Locale.ROOT, "XYZ  %.1f  %.1f  %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
            y = chip(g, font, xyz, x, y, blue, c.shadow);
        }
        if (c.isOn(Module.CPS)) {
            y = chip(g, font, LEFT_CLICKS.size() + " | " + RIGHT_CLICKS.size() + " CPS", x, y, blue, c.shadow);
        }
        if (c.isOn(Module.KEYSTROKES)) {
            keystrokes(g, mc, font, blue, c.shadow);
        }
        if (c.isOn(Module.MODULE_LIST)) {
            moduleList(g, mc, font, c, ice);
        }
    }

    /** Counts frames for the FPS display and click presses for the CPS display. */
    private static void sample(Minecraft mc) {
        frames++;
        long now = System.nanoTime();
        if (now - windowStart >= 1_000_000_000L) {
            fps = frames;
            frames = 0;
            windowStart = now;
        }
        boolean left = mc.options.keyAttack.isDown();
        boolean right = mc.options.keyUse.isDown();
        long ms = System.currentTimeMillis();
        if (left && !lastLeft) LEFT_CLICKS.addLast(ms);
        if (right && !lastRight) RIGHT_CLICKS.addLast(ms);
        lastLeft = left;
        lastRight = right;
        while (!LEFT_CLICKS.isEmpty() && ms - LEFT_CLICKS.peekFirst() > 1000) LEFT_CLICKS.pollFirst();
        while (!RIGHT_CLICKS.isEmpty() && ms - RIGHT_CLICKS.peekFirst() > 1000) RIGHT_CLICKS.pollFirst();
    }

    private static int chip(GuiGraphicsExtractor g, Font font, String text, int x, int y, int accent, boolean shadow) {
        int w = font.width(text) + 8;
        int h = font.lineHeight + 4;
        g.fill(x, y, x + w, y + h, 0x90101828);
        g.fill(x, y, x + 2, y + h, accent);
        g.text(font, text, x + 5, y + 2, 0xFFFFFFFF, shadow);
        return y + h + 2;
    }

    private static void keystrokes(GuiGraphicsExtractor g, Minecraft mc, Font font, int accent, boolean shadow) {
        Options o = mc.options;
        int x = 6;
        int y = mc.getWindow().getGuiScaledHeight() - 76;
        key(g, font, x + 20, y, 18, 18, "W", o.keyUp.isDown(), accent, shadow);
        key(g, font, x, y + 20, 18, 18, "A", o.keyLeft.isDown(), accent, shadow);
        key(g, font, x + 20, y + 20, 18, 18, "S", o.keyDown.isDown(), accent, shadow);
        key(g, font, x + 40, y + 20, 18, 18, "D", o.keyRight.isDown(), accent, shadow);
        key(g, font, x, y + 40, 29, 16, "LMB", o.keyAttack.isDown(), accent, shadow);
        key(g, font, x + 31, y + 40, 29, 16, "RMB", o.keyUse.isDown(), accent, shadow);
        key(g, font, x, y + 58, 60, 12, "SPACE", o.keyJump.isDown(), accent, shadow);
    }

    private static void key(GuiGraphicsExtractor g, Font font, int x, int y, int w, int h, String label,
                            boolean down, int accent, boolean shadow) {
        g.fill(x, y, x + w, y + h, down ? ((0xB0 << 24) | (accent & 0xFFFFFF)) : 0x90101828);
        g.fill(x, y + h - 1, x + w, y + h, accent);
        g.text(font, label, x + (w - font.width(label)) / 2, y + (h - font.lineHeight) / 2 + 1, 0xFFFFFFFF, shadow);
    }

    private static void moduleList(GuiGraphicsExtractor g, Minecraft mc, Font font, PrestigeConfig c, int accent) {
        List<String> names = new ArrayList<>();
        for (Module m : Module.values()) {
            if (c.isOn(m)) names.add(m.title);
        }
        names.sort(Comparator.comparingInt((String s) -> font.width(s)).reversed());
        int screenW = mc.getWindow().getGuiScaledWidth();
        int y = 6;
        for (String n : names) {
            int w = font.width(n) + 8;
            int h = font.lineHeight + 4;
            int x = screenW - w - 6;
            g.fill(x, y, x + w, y + h, 0x90101828);
            g.fill(x + w - 2, y, x + w, y + h, accent);
            g.text(font, n, x + 3, y + 2, 0xFFFFFFFF, c.shadow);
            y += h + 2;
        }
    }
}
