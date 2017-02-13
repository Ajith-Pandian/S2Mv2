package com.wowconnect.ui.tickets.attachments;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wowconnect.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDocumentActivity extends AppCompatActivity {
    @BindView(R.id.list_docs)
    ListView listView;
    @BindView(R.id.layout)
    LinearLayout linearLayout;
    ArrayList<File> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        ButterKnife.bind(this);


        File root = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath());
        getfile(root);

        for (int i = 0; i < fileList.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(fileList.get(i).getName());
            textView.setPadding(5, 5, 5, 5);

            textView.setTextColor(Color.BLACK);
            System.out.println(fileList.get(i).getName());

            if (fileList.get(i).isDirectory()) {
                textView.setTextColor(Color.parseColor("#FF0000"));
            }
            linearLayout.addView(textView);
        }
        System.out.println("size:" + fileList.size());

    }

    public ArrayList<File> getfile(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    //fileList.add(listFile[i]);
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".docx")
                                    || listFile[i].getName().endsWith(".pdf"))

                    {
                        fileList.add(listFile[i]);
                    }
                }

            }
        }
        return fileList;
    }

}
