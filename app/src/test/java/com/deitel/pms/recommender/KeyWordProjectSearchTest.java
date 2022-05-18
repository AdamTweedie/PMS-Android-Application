package com.deitel.pms.recommender;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class KeyWordProjectSearchTest {



    @Test
    public void search() {
        ArrayList<ArrayList<String>> projects = getTestArray();
        ArrayList<Integer> indexArray = KeyWordProjectSearch.Search(projects, "virtue");
        assertEquals(2, indexArray.size());


    }

    private ArrayList<ArrayList<String>> getTestArray() {
        ArrayList<ArrayList<String>> testProjectArray = new ArrayList<>();
        ArrayList<String> testProject = new ArrayList<>();
        String s1 = "The virtue of humility";
        testProject.add(s1);
        testProjectArray.add(testProject);
        testProject.clear();
        String s2 = "virtue within itself is to show a high degree of moral standards" ;
        testProject.add(s2);
        testProjectArray.add(testProject);
        testProject.clear();
        String s3 = "I believe the benefits across the scope of future leadership are far more prevalent.";
        testProject.add(s3);
        testProjectArray.add(testProject);
        testProject.clear();
        String s4 = "Humans in nature are imperfect and prone to mistakes, nor should they\n" +
                "hide from them.";
        testProject.add(s4);
        testProjectArray.add(testProject);
        testProject.clear();
        String s5 = "However, the traits a leader shows will have a paramount of\n" +
                "influence to a followers views of such errors.";
        testProject.add(s5);
        testProjectArray.add(testProject);
        testProject.clear();
        String s6 = "Accepting failure with prag-\n" +
                "matism and acknowledging ones limitations and weaknesses are key features\n" +
                "of a humble leader";
        testProject.add(s6);
        testProjectArray.add(testProject);

        return testProjectArray;
    }
}