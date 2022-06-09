package com.radioactive.gear.project.filelocalshare.ui.file;

import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.radioactive.gear.project.filelocalshare.R;

public class FileExplorerPopupMenuController {
    public FileExplorerPopupMenuController(PopupMenu menu, FileSortable fileSortable) {
        Button sortOrderButton = (Button) menu.getMenu().findItem(R.id.select_order_button);

        sortOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }
}
