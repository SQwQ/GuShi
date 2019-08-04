package com.example.myapplication;

import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class AnalysisGetter {
    private String id;
    private String url;
    private String analysis;

    public AnalysisGetter(String ID) {
        this.id = ID;
    }

    public String getAnalysis(char c) {

        url = "https://baike.baidu.com/item/" + id;
        System.out.println(url);

        if (c == 't') {
            this.analysis = getText("译");
        } else if (c == 'a'){
            this.analysis = getText("赏");
        }
        System.out.println(analysis);

        return analysis;


         /*
            ArrayList<String> p = new ArrayList<>();

            StringBuilder literary_analysis = new StringBuilder();

            StringBuilder translation = new StringBuilder();


            StringBuilder temp = new StringBuilder(this.id);
            String url = "https://so.gushiwen.org/shiwenv_" + temp.substring(7) + ".aspx";
            System.out.println(url);
            */


            /*Elements elems = htmlDocument.getElementsByClass("contyishang");
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
        //System.out.println(p);*/

    }

    private String getText(String key) {
        StringBuilder result = new StringBuilder();
        try {

            Element current = null;
            Elements elems;

            Document htmlDocument = Jsoup.connect(url).get();

            Elements h2elems = htmlDocument.select("h2");
            Elements h3elems = htmlDocument.select("h3");


            if (isElementCorrectSet(h3elems, key)) {
                elems = h3elems;
            } else {
                elems = h2elems;
            }

            if (!elems.isEmpty()) {
                for (Element elem : elems) {
                    System.out.println("final set: " + elem.text());
                    if (elem.text().contains(key)) {

                        current = elem;

                        break;
                    }
                }
            }

            if (current != null) {

                current = current.parent().nextElementSibling();
                System.out.println(current.text());
                //System.out.println(current.className());
                while (!current.className().contains("chor")) {
                    //System.out.println(current.className());

                    result.append(current.text());
                    result.append("\n");
                    current = current.nextElementSibling();
                }
            } else {
                result.append("暂无资料");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private boolean isElementCorrectSet(Elements elems, String key) {
        for (Element elem : elems) {
            System.out.println(elem.text());
            if (elem.text().contains(key)) {
                return true;
            }
        }

        return false;
    }

}
