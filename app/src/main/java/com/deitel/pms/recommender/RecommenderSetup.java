package com.deitel.pms.recommender;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.internal.StorageReferenceUri;

public class RecommenderSetup {

    String universityCode;
    String courseCode;
    String dfEntry;
    StorageReference storageReferenceUrl;
    FirebaseStorage storage;

    public RecommenderSetup(String uniCode, String course, String dfEntry) {

        this.universityCode = uniCode;
        this.courseCode = course;
        this.dfEntry = dfEntry;
        this.storage = FirebaseStorage.getInstance();
        // you could have it so this is automatically done -
        // meaning when a user creates an account it already files it with the subject in
        // firestore so this never needs to be passed.
        this.storageReferenceUrl = this.storage.getReferenceFromUrl
                ("gs://pms-project-300122.appspot.com/Project Data/" + uniCode
                        + "/" + course);
    }

    public String getUniversityCode() {
        return universityCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getDfEntry() { return dfEntry; }

    public StorageReference getStorageReferenceUrl() {
        return storageReferenceUrl;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }
}
