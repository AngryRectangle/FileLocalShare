package com.radioactive.gear.project.filelocalshare;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FileIconProvider {

    public static boolean hasIcon(String extension) {
        return _drawableDictionary.containsKey(extension);
    }

    public static Drawable getIcon(String extension) {
        return _drawableDictionary.get(extension);
    }

    public static Drawable getFolderIcon() {
        return _folderIcon;
    }

    public static Drawable getDefaultIcon() {
        return _defaultIcon;
    }

    private static Map<String, Drawable> _drawableDictionary = new HashMap<>();
    private static Drawable _defaultIcon;
    private static Drawable _folderIcon;

    static {
        Map<String, Integer> _iconDictionary = new HashMap<String, Integer>();
        _iconDictionary.put("avi", R.drawable.ic_file_icon_avi);
        _iconDictionary.put("css", R.drawable.ic_file_icon_css);
        _iconDictionary.put("csv", R.drawable.ic_file_icon_csv);
        _iconDictionary.put("doc", R.drawable.ic_file_icon_doc);
        _iconDictionary.put("docx", R.drawable.ic_file_icon_doc);
        _iconDictionary.put("exe", R.drawable.ic_file_icon_exe);
        _iconDictionary.put("html", R.drawable.ic_file_icon_html);
        _iconDictionary.put("htm", R.drawable.ic_file_icon_html);
        _iconDictionary.put("iso", R.drawable.ic_file_icon_iso);
        _iconDictionary.put("js", R.drawable.ic_file_icon_javascript);
        _iconDictionary.put("jpg", R.drawable.ic_file_icon_jpg);
        _iconDictionary.put("jpe", R.drawable.ic_file_icon_jpg);
        _iconDictionary.put("jpeg", R.drawable.ic_file_icon_jpg);
        _iconDictionary.put("json", R.drawable.ic_file_icon_json);
        _iconDictionary.put("mp3", R.drawable.ic_file_icon_mp3);
        _iconDictionary.put("mp4", R.drawable.ic_file_icon_mp4);
        _iconDictionary.put("pdf", R.drawable.ic_file_icon_pdf);
        _iconDictionary.put("png", R.drawable.ic_file_icon_png);
        _iconDictionary.put("ppt", R.drawable.ic_file_icon_ppt);
        _iconDictionary.put("pptx", R.drawable.ic_file_icon_ppt);
        _iconDictionary.put("psd", R.drawable.ic_file_icon_psd);
        _iconDictionary.put("svg", R.drawable.ic_file_icon_svg);
        _iconDictionary.put("txt", R.drawable.ic_file_icon_txt);
        _iconDictionary.put("rtf", R.drawable.ic_file_icon_rtf);
        _iconDictionary.put("xls", R.drawable.ic_file_icon_xls);
        _iconDictionary.put("xlsx", R.drawable.ic_file_icon_xls);
        _iconDictionary.put("xml", R.drawable.ic_file_icon_xml);
        _iconDictionary.put("zip", R.drawable.ic_file_icon_zip);
        _iconDictionary.put("7zip", R.drawable.ic_file_icon_archive);
        _iconDictionary.put("rar", R.drawable.ic_file_icon_archive);

        Set<String> keys = _iconDictionary.keySet();
        Iterator<String> keyIterator = keys.iterator();
        Resources resources = FileLocalShare.getActivityContext().getResources();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            _drawableDictionary.put(key, resources.getDrawable(_iconDictionary.get(key)));
        }
        _defaultIcon = resources.getDrawable(R.drawable.ic_file_icon_default);
        _folderIcon = resources.getDrawable(R.drawable.ic_file_icon_folder);

    }
}
