package uk.ac.nott.cs.g53dia.Playground;

import uk.ac.nott.cs.g53dia.library.Tanker;

public class SpiralPrint {
    static int[][] populate(int rows, int cols, int[][] arr) {
        int c = 1;

        for(int x = 0; x < rows; x++) {
            for(int y = 0; y < cols; y++) {
                arr[x][y] = c++;
            }
        }

        return arr;
    }

    static int[] spiral(int rows, int cols, int[][] arr) {
        int[] res = new int[rows * cols];

        int fr, lc, lr, fc, i;
        /*
        fr - First row
        lc - Last column
        lr - Last row
        fc - First column
        i - for loop iterator
         */
        int c = 0;
        fr = fc = 0;
        lc = cols - 1;
        lr = rows - 1;

        while(c < res.length) {
            for(i = fc; i <= lc; i++) // Top row values
                res[c++] = arr[fr][i];
            fr++;
            for(i = fr; i <= lr; i++) // Right column values
                res[c++] = arr[i][lc];
            lc--;
            if(fr < lr) { // Bottom row values
                for(i = lc; i >= fc; i--)
                    res[c++] = arr[lr][i];
                lr--;
            }
            if(fc < lc) { // Left column values
                for(i = lr; i >= fr; i--)
                    res[c++] = arr[i][fc];
                fc++;
            }
//            for(int j : res)
//                System.out.print(j + " ");
            System.out.println("fr: " + fr + " lc: " + lc + " lr: " + lr + " fc: " + fc);
        }

        return res;
    }


    public static void main(String[] args) {
        final int ROWS = Tanker.VIEW_RANGE * 2 + 1;
        final int COLUMNS = Tanker.VIEW_RANGE * 2 + 1;

        int[][] arr = new int[ROWS][COLUMNS];
        int[] res;

        arr = populate(ROWS, COLUMNS, arr);
        System.out.println("Input: ");
        for(int x = 0; x < ROWS; x++) {
            for(int y = 0; y < COLUMNS; y++) {
                System.out.print(arr[x][y] + " ");
            }
            System.out.println();
        }
        System.out.println();

        System.out.print("Output: ");
        res = spiral(ROWS, COLUMNS, arr);

        for (int i : res) {
            System.out.print(i + " ");
        }
    }
}
