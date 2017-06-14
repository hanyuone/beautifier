package com.qwerpderp.test;

import java.lang.Math.*;

public class ListStuff {
    public static boolean isSorted(int[] arr) {
        for (int a = 0; a < arr.length - 1; a++) {
            if (arr[a] > arr[a + 1]) {
                return false;
            }
        }

        return true;
    }

    public static int[] bubbleSort(int[] arr) {
        while (!isSorted(arr)) {
            for (int a = 0; a < arr.length - 1; a++) {
                if (arr[a] > arr[a + 1]) {
                    int temp = arr[a];
                    arr[a] = arr[a + 1];
                    arr[a + 1] = temp;
                }
            }
        }

        return arr;
    }
}