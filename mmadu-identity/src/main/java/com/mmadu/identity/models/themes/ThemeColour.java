package com.mmadu.identity.models.themes;

import lombok.Data;

@Data
public class ThemeColour {
    private String primary = "lightcoral";
    private String secondary = "darkslateblue";
    private String info = "aliceblue";
    private String primaryDark = "#d07070";
    private String primaryDarkest = "#c06060";
    private String secondaryDark = "#39306f";
    private String secondaryDarkest = "#2b2453";
    private String danger = "darkred";
    private String dangerText = "white";
    private String success = "#2b8378";
    private String successText = "black";
}
