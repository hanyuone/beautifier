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

    public static int randint(int start, int end) {
        float rand_float = Math.random();
        int difference = end - start;

        return (int) (rand_float * difference) + start;
    }

    public static int[] delete(int[] arr, int index) {
        int[] new_arr = new int[arr.length - 1];

        for (int a = 0; a < index; a++) {
            new_arr[a] = arr[a];
        }

        for (int b = index + 1; b < arr.length; b++) {
            new_arr[b - 1] = arr[b];
        }

        return new_arr;
    }

    public static int[] shuffle(int[] arr) {
        int[] indices = new int[arr.length];

        for (int a = 0; a < arr.length; a++)
            indices[a] = a;
    }
}