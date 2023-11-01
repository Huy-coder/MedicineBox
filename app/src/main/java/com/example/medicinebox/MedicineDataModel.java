package com.example.medicinebox;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedicineDataModel {
    @SerializedName("number_medicine")
    private int[] numberMedicine;

    public int[] getNumberMedicine() {
        return numberMedicine;
    }
}
