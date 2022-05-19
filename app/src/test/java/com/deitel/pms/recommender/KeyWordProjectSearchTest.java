package com.deitel.pms.recommender;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class KeyWordProjectSearchTest {

    @Test
    public void arraySizeSearchTest() {
        ArrayList<ArrayList<String>> projects = getTestArray();

        ArrayList<Integer> indexArray = KeyWordProjectSearch.Search(projects, "mobile");
        assertEquals(1, indexArray.size());

        indexArray = KeyWordProjectSearch.Search(projects, "machine");
        assertEquals(2, indexArray.size());

    }

    @Test
    public void correctIndexSearchTest() {
        ArrayList<ArrayList<String>> projects = getTestArray();
        ArrayList<Integer> indexArray = KeyWordProjectSearch.Search(projects, "fun");

        int firstIndex = indexArray.get(0);
        int secondIndex = indexArray.get(1);
        assertEquals(0, firstIndex);
        assertEquals(2, secondIndex);
    }

    private ArrayList<ArrayList<String>> getTestArray() {
        ArrayList<ArrayList<String>> testProjectArray = new ArrayList<>();
        ArrayList<String> a1 = new ArrayList<>();
        a1.add("machine learning is fun");
        ArrayList<String> a2 = new ArrayList<>();
        a2.add("Web dev for the win");
        ArrayList<String> a3 = new ArrayList<>();
        a3.add("not as fun as making mobile apps though");
        ArrayList<String> a4 = new ArrayList<>();
        a4.add("Dont forget social media applications");
        ArrayList<String> a5 = new ArrayList<>();
        a5.add("data science and machine learning are the best");
        testProjectArray.add(a1);
        testProjectArray.add(a2);
        testProjectArray.add(a3);
        testProjectArray.add(a4);
        testProjectArray.add(a5);

        return testProjectArray;
    }
}