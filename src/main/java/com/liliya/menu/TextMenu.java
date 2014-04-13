package com.liliya.menu;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TextMenu {


    public static MenuActions display(String title, List<TextMenuItem> menu) {
        MenuActions input = null;
        System.out.println(title);
        System.out.println("------------------------");
        for (TextMenuItem item : menu) {
            System.out.println(menu.indexOf(item) + "." + item.getTitle());
        }
        System.out.print(">>");
        Scanner sc = new Scanner(System.in);
        input = menu.get(sc.nextInt()).getAction();
        return input;
    }

}
