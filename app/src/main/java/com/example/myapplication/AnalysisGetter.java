package com.example.myapplication;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class AnalysisGetter {
    private String id;
    private String analysis;
    private String translation;

    public AnalysisGetter(String ID) {
        this.id = ID;
    }

    public String getAnalysis(char c) {
        try {
            ArrayList<String> p = new ArrayList<>();

            StringBuilder literary_analysis = new StringBuilder();
            StringBuilder translation = new StringBuilder();
            StringBuilder temp = new StringBuilder(this.id);
            String url = "https://so.gushiwen.org/shiwenv_" + temp.substring(7) + ".aspx";
            System.out.println(url);
            Document htmlDocument = Jsoup.connect(url).get();
            //Elements elems = htmlDocument.getElementsByClass("contyishang");
            Elements elems = htmlDocument.select("p");
            int j = 0;
            for (Element elem : elems) {
                String curr = elem.text();
                //System.out.println(j + " ::" + curr);


                if (curr.contains("注释 ")) {
                    translation.append(curr);
                    j++;
                    continue;
                }

                if ((j < 1) && (!curr.contentEquals("参考资料："))) {
                    translation.append(curr);
                } else {
                    literary_analysis.append(curr);
                }
            }

            //this.analysis = elems.text();
            //System.out.println(p);
            if (c == 't') {
                this.analysis = translation.toString();
            } else if (c == 'a'){
                this.analysis = literary_analysis.toString();
            }
            System.out.println(analysis);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return analysis;

    }

}
