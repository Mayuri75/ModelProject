package simple.android.example.model;

import java.util.ArrayList;

public class MenuLevel1 {
    private String Itemname;

    private ArrayList<MenuLevel2> MenuLevel2List;

    public MenuLevel1() {
    }

    public MenuLevel1(String itemname) {
        Itemname = itemname;
    }

    public MenuLevel1(String itemname, ArrayList<MenuLevel2> menuLevel2List) {
        Itemname = itemname;
        MenuLevel2List = menuLevel2List;
    }

    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String itemname) {
        Itemname = itemname;
    }

    public ArrayList<MenuLevel2> getMenuLevel2List() {
        return MenuLevel2List;
    }

    public void setMenuLevel2List(ArrayList<MenuLevel2> menuLevel2List) {
        MenuLevel2List = menuLevel2List;
    }
}
