package com.example.memorableplaces;

import java.util.*;
public class Crypto {

    public String Encode(ArrayList<String> arr) {
        String ans = "";
        for (int i = 0; i < arr.size(); i++) {
            ans += arr.get(i);
            if (i != arr.size() - 1) {
                ans += "~";
            }
        }
        return ans;
    }

    public String[] Decode(String s){
        return s.split("~");
    }

}
