# Beautifier

Beautifier is a Clojure program that makes any code from any C-like language (e.g. Java, C#, C++) beautiful... at least, according to a Python programmer.

Run the test project by going to your command line, and typing `lein run` (provided you have Leiningen).

## Functions:

```clojure
(beautify input output)
```

`input` and `output` are paths to file names - `input` is the file to be converted, `output` is the file to convert to.

```clojure
(set-gap n)
```

This function sets the gap between the line with the maximum character count and the extra characters. `n` must be a positive integer.
<<<<<<< HEAD

## Example output:

=======

## Example output:

>>>>>>> c2784ceb31b86f04eb77a54006613313ee5a3b0a
```java
package com.qwerpderp.test                                     ;
import java.lang.Math.*                                        ;
public class ListStuff                                         {
    public static boolean isSorted(int[] arr)                  {
        for (int a = 0; a < arr.length - 1; a++)               {
            if (arr[a] > arr[a + 1])                           {
                return false                                   ;}}
        return true                                            ;}
    public static int[] bubbleSort(int[] arr)                  {
        while (!isSorted(arr))                                 {
            for (int a = 0; a < arr.length - 1; a++)           {
                if (arr[a] > arr[a + 1])                       {
                    int temp = arr[a]                          ;
                    arr[a] = arr[a + 1]                        ;
                    arr[a + 1] = temp                          ;}}}
        return arr                                             ;}}
<<<<<<< HEAD
```
=======
```
>>>>>>> c2784ceb31b86f04eb77a54006613313ee5a3b0a
