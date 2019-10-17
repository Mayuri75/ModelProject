package simple.android.example.model;

import java.util.ArrayList;

public class MenuItems {
    private String Itemname;
    private int ItemDrawableId;
    public boolean ischecked = false;

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    private ArrayList<MenuLevel1> MenuLevel1List;

    public MenuItems(String itemname, ArrayList<MenuLevel1> menuLevel1List) {
        Itemname = itemname;
        MenuLevel1List = menuLevel1List;
    }

    public MenuItems() {
    }

    public int getItemDrawableId() {
        return ItemDrawableId;
    }

    public void setItemDrawableId(int itemDrawableId) {
        ItemDrawableId = itemDrawableId;
    }

    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String itemname) {
        Itemname = itemname;
    }

    public ArrayList<MenuLevel1> getMenuLevel1List() {
        return MenuLevel1List;
    }

    public void setMenuLevel1List(ArrayList<MenuLevel1> menuLevel1List) {
        MenuLevel1List = menuLevel1List;
    }



}

