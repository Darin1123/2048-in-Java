package model;


import util.random.RandomService;

import java.util.*;
import java.util.stream.Collectors;

public class GameBoard extends Subject<Cell[][]> {
    public GameBoard() {
        Cell[][] state = new Cell[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = new Cell();
            }
        }
        int[] indices = new int[] {0, 1, 2, 3};
        int x1 = RandomService.pick(indices);
        int x2 = RandomService.pick(indices);
        int v1 = RandomService.random2or4();
        int v2 = RandomService.random2or4();
        int y1 = -1, y2 = -1;

        try {
            if (x1 == x2) {
                y1 = RandomService.pick(indices);
                List<Integer> remaining = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
                remaining.remove(y1);
                y2 = remaining.get(RandomService.random(0, 3));
            } else {
                y1 = RandomService.pick(indices);
                y2 = RandomService.pick(indices);
            }
        } catch (UnsupportedOperationException e) {
            System.out.println(y1);
        }

        System.out.println(x1 + ", " + y1 + ", " + v1);
        System.out.println(x2 + ", " + y2 + ", " + v2);

        state[x1][y1].setValue(v1);
        state[x1][y1].check();

        state[x2][y2].setValue(v2);
        state[x2][y2].check();
        this.setState(state);
    }
}
