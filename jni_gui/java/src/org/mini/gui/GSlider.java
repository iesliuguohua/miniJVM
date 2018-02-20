/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mini.gui;

import static org.mini.glfw.utils.Nutil.NVG_HOLE;
import static org.mini.glfw.utils.Nutil.nvgBeginPath;
import static org.mini.glfw.utils.Nutil.nvgBoxGradient;
import static org.mini.glfw.utils.Nutil.nvgCircle;
import static org.mini.glfw.utils.Nutil.nvgFill;
import static org.mini.glfw.utils.Nutil.nvgFillColor;
import static org.mini.glfw.utils.Nutil.nvgFillPaint;
import static org.mini.glfw.utils.Nutil.nvgLinearGradient;
import static org.mini.glfw.utils.Nutil.nvgPathWinding;
import static org.mini.glfw.utils.Nutil.nvgRadialGradient;
import static org.mini.glfw.utils.Nutil.nvgRect;
import static org.mini.glfw.utils.Nutil.nvgRestore;
import static org.mini.glfw.utils.Nutil.nvgRoundedRect;
import static org.mini.glfw.utils.Nutil.nvgSave;
import static org.mini.glfw.utils.Nutil.nvgStroke;
import static org.mini.glfw.utils.Nutil.nvgStrokeColor;
import static org.mini.gui.GToolkit.nvgRGBA;

/**
 *
 * @author gust
 */
public class GSlider extends GObject {

    String text;
    float pos;
    boolean draged;
    public static final int HORIZONTAL = 0, VERTICAL = 1;
    int mode = HORIZONTAL;

    public GSlider(float pos, int mode, int left, int top, int width, int height) {
        this.pos = pos;
        this.mode = mode;
        boundle[LEFT] = left;
        boundle[TOP] = top;
        boundle[WIDTH] = width;
        boundle[HEIGHT] = height;
    }

    @Override
    public void mouseButtonEvent(int button, boolean pressed, int x, int y) {
        int rx = (int) (x - parent.getX());
        int ry = (int) (y - parent.getY());
        if (isInBoundle(boundle, rx, ry)) {
            if (pressed) {
                draged = true;
                parent.setFocus(this);
                pos = mode == HORIZONTAL ? (rx - boundle[LEFT]) / boundle[WIDTH] : (ry - boundle[TOP]) / boundle[HEIGHT];
            } else {
                draged = false;
                if (actionListener != null) {
                    actionListener.action();
                }
            }
        }
    }

    @Override
    public void cursorPosEvent(int x, int y) {
        int rx = (int) (x - parent.getX());
        int ry = (int) (y - parent.getY());
        if (isInBoundle(boundle, rx, ry)) {
            if (draged) {
                pos = mode == HORIZONTAL ? (rx - boundle[LEFT]) / boundle[WIDTH] : (ry - boundle[TOP]) / boundle[HEIGHT];
            }
        } else {
            //draged = false;
        }
    }

    /**
     *
     * @param vg
     * @return
     */
    public boolean update(long vg) {
        float x = getX();
        float y = getY();
        float w = getW();
        float h = getH();

        if (mode == HORIZONTAL) {
            drawSliderH(vg, pos, x, y, w, h);
        } else {
            drawSliderV(vg, pos, x, y, w, h);
        }
        return true;
    }

    void drawSliderH(long vg, float pos, float x, float y, float w, float h) {
        byte[] bg, knob;
        float cy = y + (int) (h * 0.5f);
        float kr = 8;//(int) (h * 0.25f);

        nvgSave(vg);
//	nvgClearState(vg);

        // Slot
        bg = nvgBoxGradient(vg, x, cy - 2 + 1, w, 4, 2, 2, nvgRGBA(0, 0, 0, 32), nvgRGBA(0, 0, 0, 128));
        nvgBeginPath(vg);
        nvgRoundedRect(vg, x, cy - 2, w, 4, 2);
        nvgFillPaint(vg, bg);
        nvgFill(vg);

        // Knob Shadow
        bg = nvgRadialGradient(vg, x + (int) (pos * w), cy + 1, kr - 3, kr + 3, nvgRGBA(0, 0, 0, 64), nvgRGBA(0, 0, 0, 0));
        nvgBeginPath(vg);
        nvgRect(vg, x + (int) (pos * w) - kr - 5, cy - kr - 5, kr * 2 + 5 + 5, kr * 2 + 5 + 5 + 3);
        nvgCircle(vg, x + (int) (pos * w), cy, kr);
        nvgPathWinding(vg, NVG_HOLE);
        nvgFillPaint(vg, bg);
        nvgFill(vg);

        // Knob
        knob = nvgLinearGradient(vg, x, cy - kr, x, cy + kr, nvgRGBA(255, 255, 255, 16), nvgRGBA(0, 0, 0, 16));
        nvgBeginPath(vg);
        nvgCircle(vg, x + (int) (pos * w), cy, kr - 1);
        nvgFillColor(vg, nvgRGBA(40, 43, 48, 255));
        nvgFill(vg);
        nvgFillPaint(vg, knob);
        nvgFill(vg);

        nvgBeginPath(vg);
        nvgCircle(vg, x + (int) (pos * w), cy, kr - 0.5f);
        nvgStrokeColor(vg, nvgRGBA(0, 0, 0, 92));
        nvgStroke(vg);

        nvgRestore(vg);
    }

    void drawSliderV(long vg, float pos, float x, float y, float w, float h) {
        byte[] bg, knob;
//        float cy = y + (int) (h * 0.5f);
        float cx = x + (int) (w * 0.5f);
        float kr = 8;//(int) (w * 0.25f);

        nvgSave(vg);
//	nvgClearState(vg);

        // Slot
//        bg = nvgBoxGradient(vg, x, cx - 2 + 1, w, 4, 2, 2, nvgRGBA(0, 0, 0, 32), nvgRGBA(0, 0, 0, 128));
        bg = nvgBoxGradient(vg, cx - 2 + 1, y, 4, h, 2, 2, nvgRGBA(0, 0, 0, 32), nvgRGBA(0, 0, 0, 128));
        nvgBeginPath(vg);
        nvgRoundedRect(vg, cx - 2, y, 4, h, 2);
        nvgFillPaint(vg, bg);
        nvgFill(vg);

        // Knob Shadow
//        bg = nvgRadialGradient(vg, x + (int) (pos * w), cx + 1, kr - 3, kr + 3, nvgRGBA(0, 0, 0, 64), nvgRGBA(0, 0, 0, 0));
        bg = nvgRadialGradient(vg, cx + 1, y + (int) (pos * h), kr - 3, kr + 3, nvgRGBA(0, 0, 0, 64), nvgRGBA(0, 0, 0, 0));
        nvgBeginPath(vg);
//        nvgRect(vg, x + (int) (pos * w) - kr - 5, cx - kr - 5, kr * 2 + 5 + 5, kr * 2 + 5 + 5 + 3);
        nvgRect(vg, cx - kr - 5, y + (int) (pos * h) - kr - 5, kr * 2 + 5 + 5, kr * 2 + 5 + 5 + 3);
//        nvgCircle(vg, x + (int) (pos * w), cx, kr);
        nvgCircle(vg, cx, y + (int) (pos * h), kr);
        nvgPathWinding(vg, NVG_HOLE);
        nvgFillPaint(vg, bg);
        nvgFill(vg);

        // Knob
//        knob = nvgLinearGradient(vg, x, cx - kr, x, cx + kr, nvgRGBA(255, 255, 255, 16), nvgRGBA(0, 0, 0, 16));
        knob = nvgLinearGradient(vg, cx - kr, y, x, cx + kr, nvgRGBA(255, 255, 255, 16), nvgRGBA(0, 0, 0, 16));
        nvgBeginPath(vg);
//        nvgCircle(vg, x + (int) (pos * w), cx, kr - 1);
        nvgCircle(vg, cx, y + (int) (pos * h), kr - 1);
        nvgFillColor(vg, nvgRGBA(40, 43, 48, 255));
        nvgFill(vg);
        nvgFillPaint(vg, knob);
        nvgFill(vg);

        nvgBeginPath(vg);
//        nvgCircle(vg, x + (int) (pos * w), cx, kr - 0.5f);
        nvgCircle(vg, cx, y + (int) (pos * h), kr - 0.5f);
        nvgStrokeColor(vg, nvgRGBA(0, 0, 0, 92));
        nvgStroke(vg);

        nvgRestore(vg);
    }

}