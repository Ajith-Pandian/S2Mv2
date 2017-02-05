package com.example.uilayer.manage;

/**
 * Created by Ajit on 05-02-2017.
 */

public interface TeacherOrSectionListener {

        void onAddOptionSelected(boolean isTeacher);

        void onEditOptionSelected(boolean isTeacher, int position);

        void onDeleteOptionSelected(boolean isTeacher, int position);

}
