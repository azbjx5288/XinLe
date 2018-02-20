package com.xinle.lottery.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sakura on 2016/9/15.
 */
public class NumbericUtils
{
    private static final String TAG = "NumbericUtils";
    
    public static boolean isNumeric(String str)
    {
        int length = str.length();
        for (int i = length; --i >= 0; )
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
    
    public static String formatPrize(double prize)
    {
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        return String.format("%.2f", Double.valueOf(decimalFormat.format(prize)));
    }
    
    public static String addZero(String number)
    {
        switch (number)
        {
            case "1":
                return "01";
            case "2":
                return "02";
            case "3":
                return "03";
            case "4":
                return "04";
            case "5":
                return "05";
            case "6":
                return "06";
            case "7":
                return "07";
            case "8":
                return "08";
            case "9":
                return "09";
        }
        return number;
    }
    
    public static boolean hasDupString(String[] strings)
    {
        Set<String> set = new HashSet<>();
        for (int i = 0, length = strings.length; i < length; i++)
        {
            set.add(strings[i]);
        }
        if (strings.length > set.size())
            return true;
        else
            return false;
    }
    
    public static boolean hasDupArray(ArrayList<String[]> arrayList)
    {
        for (int i = 0, size = arrayList.size(); i < size - 1; i++)
        {
            for (int j = i + 1; j < size; j++)
            {
                if (Arrays.equals(arrayList.get(i), arrayList.get(j)))
                    return true;
            }
        }
        return false;
    }
    
    public static boolean isDupStrCountUnique(String[] strings, int size)
    {
        HashMap<String, Integer> countMap = new HashMap<>();
        List<String> list = Arrays.asList(strings);
        for (String str : list)
        {
            if (countMap.containsKey(str))
                countMap.put(str, countMap.get(str) + 1);
            else
                countMap.put(str, 1);
        }
        int dupCount = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet())
        {
            if(entry.getValue()==size)
                dupCount++;
        }
        if (dupCount == 1)
            return true;
        return false;
    }
    
    public static ArrayList<String[]> delDup(ArrayList<String[]> arrayList)
    {
        ArrayList<String[]> newList = new ArrayList<>();
        newList.addAll(arrayList);
        for (int i = 0, size = arrayList.size(); i < size - 1; i++)
        {
            Arrays.sort(arrayList.get(i));
            for (int j = i + 1; j < size; j++)
            {
                Arrays.sort(arrayList.get(j));
                if (Arrays.equals(arrayList.get(i), arrayList.get(j)))
                    newList.remove(arrayList.get(j));
            }
        }
        //Collections.reverse(newList);
        return newList;
    }
    
    public static ArrayList<String[]> delDupWithOrder(ArrayList<String[]> arrayList)
    {
        ArrayList<String[]> newList = new ArrayList<>();
        newList.addAll(arrayList);
        for (int i = 0, size = arrayList.size(); i < size - 1; i++)
        {
            for (int j = i + 1; j < size; j++)
            {
                if (Arrays.equals(arrayList.get(i), arrayList.get(j)))
                    newList.remove(arrayList.get(j));
            }
        }
        return newList;
    }
    
    public static int[] stringToInt(String[] strings)
    {
        int[] ints = new int[strings.length];
        for (int i = 0; i < strings.length; i++)
        {
            ints[i] = Integer.parseInt(strings[i]);
        }
        return ints;
    }
    
    public static String replaceByMap(String string, HashMap<String, String> map, boolean hasSpace)
    {
        StringBuilder builder = new StringBuilder();
        String[] strings;
        if (hasSpace)
            strings = string.split(" ");
        else
            strings = string.split("");
        for (int i = 0, length = strings.length; i < length; i++)
        {
            String value = map.get(strings[i]);
            if (value != null)
                builder.append(value);
            else
                builder.append(strings[i]);
            if (i < length - 1 && hasSpace)
                builder.append(" ");
        }
        return builder.toString();
        /*for (Map.Entry<String, String> entry : map.entrySet())
        {
            string = string.replace(entry.getKey(), entry.getValue());
        }
        return string;*/
    }
}
