package me.newblood.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnowEffect {
    private final List<Snowflake> snowflakes = new ArrayList<>();
    private final Random random = new Random();

    public SnowEffect(int count) {
        for (int i = 0; i < count; i++) {
            snowflakes.add(new Snowflake());
        }
    }

    public void draw(int width, int height) {
        for (Snowflake snowflake : snowflakes) {
            snowflake.update(width, height);
            // Drawing logic will be handled in the GUI screen using DrawContext
        }
    }

    public List<Snowflake> getSnowflakes() {
        return snowflakes;
    }

    public static class Snowflake {
        public float x, y, size, speed;
        private final Random random = new Random();

        public Snowflake() {
            reset(1920, 1080); // Default values, will be updated
        }

        public void reset(int width, int height) {
            x = random.nextFloat() * width;
            y = -random.nextFloat() * height;
            size = 1 + random.nextFloat() * 3;
            speed = 0.5f + random.nextFloat() * 1.5f;
        }

        public void update(int width, int height) {
            y += speed;
            if (y > height) {
                reset(width, height);
            }
        }
    }
}
