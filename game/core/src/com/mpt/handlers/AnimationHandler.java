package com.mpt.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Objects;

public class AnimationHandler {
    private final HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();
    private float timer = 0;
    private boolean looping = true;
    private boolean stopped = false;
    private String current;

    public void add(String name, Animation<TextureRegion> animation) {
        animations.put(name, animation);
    }

    public void setCurrent(String name) {
        if (Objects.equals(current, name)) return;
        assert (animations.containsKey(name)) : "No such animation " + name;
        current = name;
        timer = 0;
        looping = true;
        stopped = false;
    }

    public void setCurrent(String name, boolean looping) {
        setCurrent(name);
        this.looping = looping;
    }

    public boolean isCurrent(String name) {
        return current.equals(name);
    }

    public boolean isFinished() {
        return animations.get(current).isAnimationFinished(timer);
    }

    public boolean isAnimationOverHalf() {
        return timer >= animations.get(current).getAnimationDuration() / 2;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public TextureRegion getFrame() {
        if (!stopped) timer += Gdx.graphics.getDeltaTime();
        return animations.get(current).getKeyFrame(timer, looping);
    }

    @Override
    public String toString() {
        return "AnimationHandler{" + "timer=" + timer + ", looping=" + looping + ", current='" + current + '\'' + ", frame=" + animations.get(current).getKeyFrameIndex(timer) + '}';
    }

}