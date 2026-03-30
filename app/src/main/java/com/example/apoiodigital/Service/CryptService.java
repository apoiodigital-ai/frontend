package com.example.apoiodigital.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CryptService {

    private char[] particularKey = "4>IÉÔôÚzN\"u;[BTc0Öb*YJçË\\qãèyöëúàjfÙ+omïÍ{Â^Hòvr=aÌwÊV`Ó8ù$tR6/)#]-!Û17%ó3(dAEÒX,k'S?Èh:Uì}5ei<sîÎÃ@~M2ÄÁäpFGí9POQgÜ|CD_ÀW&áxKnüÏZ.°êûÕLâõléÇ".toCharArray();
    private String allChars = "abcdefghijklmnopqrstuvwxyzçABCDEFGHIJKLMNOPQRSTUVWXYZÇáàâãäéèêëíìîïóòôõöúùûüÁÀÂÃÄÉÈÊËÍÌÎÏÓÒÔÕÖÚÙÛÜ!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~1234567890°";


    public String shuffleString(String text){
        var chars = text.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        Collections.shuffle(chars);

        StringBuilder auxString = new StringBuilder();
        for(char el : chars){
            auxString.append(el);
        }
        return auxString.toString();
    }

    public String getPublicKey(){
        return shuffleString(allChars);
    }

    private List<String> setGlossary(String publicKey){

        boolean change = true;

        var glossary = new ArrayList<String>();
        int count = 1;

        char[] publicKeyChar = publicKey.toCharArray();
        char[] charArray = allChars.toCharArray();
        for(char el : charArray){
            String encrity = "";

            if(change){

                encrity = ""+publicKeyChar[count-1]+particularKey[count-1]+publicKeyChar[publicKeyChar.length-count];
            }else{
                encrity = ""+publicKeyChar[count-1]+particularKey[count-1]+publicKeyChar[publicKeyChar.length-count+1];
            }

            glossary.add(encrity);
            count++;
            change=!change;
        }

        glossary.add(""+publicKeyChar[5]+particularKey[2]+publicKeyChar[publicKeyChar.length-1]); // espaco
//
//        for (String el : glossary){
//            System.out.println(el);
//        }

        return glossary;

    }

    public String encripty(String text, String publicKey){
        StringBuilder encriptyText = new StringBuilder();
        var glossary = setGlossary(publicKey);
        var auxChar = allChars + " ";
        char[] textArray = text.toCharArray();
        for (char letter : textArray){

            var index = auxChar.indexOf(letter);
//            Log.e("CryptServiceLOGE", "encripty: " + letter + index );
            var aux = "";
            if(index == -1){
                aux += "???";
            }else{
                aux += shuffleString(glossary.get(index));
            }
            encriptyText.append(aux);
        }

        return encriptyText.toString();
    }

    public String descrypt(String cryptedText, String publicKey){
        var _glossary = setGlossary(publicKey);
        char[] auxChars = (allChars+" ").toCharArray();
        int count = 0;
        StringBuilder descryptedText = new StringBuilder();

        for (int i = 0; i < cryptedText.length(); i++){
            if(count >= (cryptedText.length()-2)) break;

            var temp = "" + cryptedText.charAt(count) + cryptedText.charAt(count+1) + cryptedText.charAt(count+2);

            char[] codigo = (temp).toCharArray();
            if(temp.equals("???")) {
                descryptedText.append("");
                count+=3;
                continue;
            }
            int index = 0;
            Arrays.sort(codigo);

            for ( String el : _glossary){
                char[] auxEl = el.toCharArray();
                Arrays.sort(auxEl);
                if( Arrays.equals( auxEl, codigo ) ){

                    index = _glossary.indexOf(el);
                    break;

                }
            }
            descryptedText.append(auxChars[index]);
            count+=3;

        }

        return descryptedText.toString();

    }


}
