package com.lms;

import com.lms.view.*;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);

        new MainMenu().showMainMenu(sc);
    }
}
