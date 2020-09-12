package com.radioactive.gear.project.filelocalshare;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FileExtensionsHandler {
    static Set<String> mediaExtensions = new HashSet<>(
            Arrays.asList(
                    "png",
                    "jpg",
                    "jpeg",
                    "jpe",
                    "gif",
                    "mp4",
                    "avi"
            )
    );

    public static boolean isMediaFile(String extension) {
        return mediaExtensions.contains(extension);
    }

    public static HashMap<String, Integer> fileIcons = new HashMap<String, Integer>();

    static {
        fileIcons.put("txt", R.drawable.ic_file_icon_txt);
        fileIcons.put("avi", R.drawable.ic_file_icon_avi);
        fileIcons.put("css", R.drawable.ic_file_icon_css);
        fileIcons.put("csv", R.drawable.ic_file_icon_csv);
        fileIcons.put("doc", R.drawable.ic_file_icon_doc);
        fileIcons.put("docx", R.drawable.ic_file_icon_doc);
        fileIcons.put("htm", R.drawable.ic_file_icon_html);
        fileIcons.put("html", R.drawable.ic_file_icon_html);
        fileIcons.put("js", R.drawable.ic_file_icon_javascript);
        fileIcons.put("jpg", R.drawable.ic_file_icon_jpg);
        fileIcons.put("jpe", R.drawable.ic_file_icon_jpg);
        fileIcons.put("jpeg", R.drawable.ic_file_icon_jpg);
        fileIcons.put("json", R.drawable.ic_file_icon_json);
        fileIcons.put("mp3", R.drawable.ic_file_icon_mp3);
        fileIcons.put("mp4", R.drawable.ic_file_icon_mp4);
        fileIcons.put("pdf", R.drawable.ic_file_icon_pdf);
        fileIcons.put("png", R.drawable.ic_file_icon_png);
        fileIcons.put("ppt", R.drawable.ic_file_icon_ppt);
        fileIcons.put("pptx", R.drawable.ic_file_icon_ppt);
        fileIcons.put("psd", R.drawable.ic_file_icon_psd);
        fileIcons.put("svg", R.drawable.ic_file_icon_svg);
        fileIcons.put("xls", R.drawable.ic_file_icon_xls);
        fileIcons.put("xml", R.drawable.ic_file_icon_xml);
        fileIcons.put("zip", R.drawable.ic_file_icon_zip);


        fileIcons.put("folder", R.drawable.ic_file_icon_folder);
        fileIcons.put("unknown", R.drawable.ic_file_icon_txt);
    }
}