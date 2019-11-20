package com.example.homeactivity.Filter;

import com.example.homeactivity.R;

public class FilterTypeHelper {

    public static int FilterType2Color(FilterType filterType){
        switch (filterType) {
            case NONE:
                return R.color.filter_color_grey_light;
            case STYLE1:
                return R.color.filter_color_blue;
            case STYLE2:
                return R.color.filter_color_blue_dark;
            case STYLE3:
                return R.color.filter_color_blue_dark_dark;
            case STYLE4:
                return R.color.filter_color_red;
            case STYLE5:
                return R.color.filter_color_red_dark;
            case STYLE6:
                return R.color.filter_color_orange;
            case STYLE7:
                return R.color.filter_color_pink;
            case STYLE8:
                return R.color.filter_color_yellow_dark;
            case STYLE9:
                return R.color.filter_color_green_dark;
            case STYLE10:
                return R.color.filter_color_brown_dark;
            case STYLE11:
                return R.color.filter_color_brown;
            case STYLE12:
                return R.color.filter_color_brown_light;
            case STYLE13:
            case STYLE14:
            case STYLE15:
            default:
                return R.color.filter_color_grey_light;
        }
    }

    public static int FilterType2Thumb(FilterType filterType){
        switch (filterType) {
            case NONE:
                return R.drawable.filter_thumb_original;
            case STYLE1:
                return R.drawable.filter_thumb_style1;
            case STYLE2:
                return R.drawable.filter_thumb_style2;
            case STYLE3:
                return R.drawable.filter_thumb_style3;
            case STYLE4:
                return R.drawable.filter_thumb_style4;
            case STYLE5:
                return R.drawable.filter_thumb_style5;
            case STYLE6:
                return R.drawable.filter_thumb_style6;
            case STYLE7:
                return R.drawable.filter_thumb_style7;
            case STYLE8:
                return R.drawable.filter_thumb_style8;
            case STYLE9:
                return R.drawable.filter_thumb_style9;
            case STYLE10:
                return R.drawable.filter_thumb_style10;
            case STYLE11:
                return R.drawable.filter_thumb_style11;
            case STYLE12:
                return R.drawable.filter_thumb_style12;
            case STYLE13:
                return R.drawable.filter_thumb_style13;
            case STYLE14:
                return R.drawable.filter_thumb_style14;
//            case STYLE15:
//                return R.drawable.filter_thumb_style15;
            default:
                return R.drawable.filter_thumb_original;
        }
    }

    public static int FilterType2Name(FilterType filterType){
        switch (filterType) {
            case NONE:
                return R.string.filter_none;
            case STYLE1:
                return R.string.filter_style1;
            case STYLE2:
                return R.string.filter_style2;
            case STYLE3:
                return R.string.filter_style3;
            case STYLE4:
                return R.string.filter_style4;
            case STYLE5:
                return R.string.filter_style5;
            case STYLE6:
                return R.string.filter_style6;
            case STYLE7:
                return R.string.filter_style7;
            case STYLE8:
                return R.string.filter_style8;
            case STYLE9:
                return R.string.filter_style9;
            case STYLE10:
                return R.string.filter_style10;
            case STYLE11:
                return R.string.filter_style11;
            case STYLE12:
                return R.string.filter_style12;
            case STYLE13:
                return R.string.filter_style13;
            case STYLE14:
                return R.string.filter_style14;
            case STYLE15:
                return R.string.filter_style15;
            default:
                return R.string.filter_none;
        }
    }
}
