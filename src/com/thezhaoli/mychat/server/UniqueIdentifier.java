package com.thezhaoli.mychat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueIdentifier {
    private static List<Integer> ids = new ArrayList<>();
    private static final int RANGE = 100000;
    private static int index = 0;

    static {
        for (int i = 0; i < RANGE; i++) {
            ids.add(i);
        }
        Collections.shuffle(ids);
    }

    public static int getIdentifier() {
        if (index >= RANGE) index = 0;
        return ids.get(index++);
    }
}
