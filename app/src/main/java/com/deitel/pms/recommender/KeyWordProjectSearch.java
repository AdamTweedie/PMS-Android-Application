package com.deitel.pms.recommender;

import java.util.ArrayList;
import java.util.List;

public class KeyWordProjectSearch {

    public static ArrayList<Integer> Search(ArrayList<ArrayList<String>> projectData, String keyWord) {

        ArrayList<Integer> indexList = new ArrayList<>();
        // split the project list twice to speed up computation
        // rounded to integer automatically by Java "/"
        List<ArrayList<String>> list1 = projectData.subList(0, projectData.size()/2);
        List<ArrayList<String>> list2 = projectData.subList(projectData.size()/2,
                projectData.size());
        Thread t1 = new Thread(() -> {
            for (ArrayList<String> project : list1) {
                String projectAsString = project.toString().toLowerCase();
                int index = list1.indexOf(project);
                if (projectAsString.contains(keyWord.toLowerCase())) {
                    indexList.add(index);
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (ArrayList<String> project : list2) {
                String projectAsString = project.toString().toLowerCase();
                // get original index of project list
                int index = list1.size() + list2.indexOf(project);
                if (projectAsString.contains(keyWord.toLowerCase())) {
                    indexList.add(index);
                }
            }
        });
        t1.start();
        t2.start();
        try {
            // wait for both threads to finish
            t2.join();
            return indexList;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
