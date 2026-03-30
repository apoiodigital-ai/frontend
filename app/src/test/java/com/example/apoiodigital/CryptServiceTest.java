package com.example.apoiodigital;


import static org.junit.Assert.assertEquals;
import com.example.apoiodigital.Service.CryptService;

import org.junit.Test;

public class CryptServiceTest {

    @Test
    public void testDecryptText(){

        CryptService service = new CryptService();
        String crypted = ":n(ÕÖ@gI5ÉN;04ob[Wb[Wã>Ío40%[^}MÔÕÖ@Y_QÖ@Õ04oBì%C5ÉùyÖ:(TNsXCÉ5Pü{AJesXNM}Ôçö-üP{94=XNsM}Ôç-öj/óùyÖ:T(ÎAY(bcÖÕ@ÍÏíÖ@Õ40o5CÉ5CÉNsX5gIXNs:(T%ìB0o4É;NÙZ?B%ìMôaT:(Ö@Õ_YQ@ÖÕQTïXsNbW[á*W(T:ÍíÏ0Ia(bcÖyùT:(^%[T:(ÈÃã\\ùPT(:Ia0H`dM!ãaI0géDÍÏíl5gg5lÏíÍaI0Ô}MìB%Wá*Öyù}ÔMÚit40oa0Id`HMã!0aIDégÏÍíégDéDg0aIMÔ}[%^I0a0o4*áWkt=aI0sêXiÕtaI0^%[sXNìB%AÎYWá*:T(b[WaI09ÓüIa0QTïNXsWb[á*WT:(aI0Óü9I0aÓüë}MÔ_QYaI0dH`ã!M0IaN.étÕiíÍÏlg5gDéa0IçÖÂ:T(ÖyùQ_Y0IadH`M!ãa0IgDéÏíÍlg5l5güP{aI09Óü0aIH`d!ãM0IaéDgíÏÍg5lgl5a0I`Hd!ãM0Ia.éNÕitÏíÍg5légD0Iaü9ÓIa0é°2tiÕü9ÓsXêÕitIa0[^%XNsìB%0aIöD-a0IaI0üÓ9Ia0jó/yùÖVè`*WáNsX[bWÕ@Ö%Ï^";
        String publicKey = "oâgC}aiïsréÉ^ì:b$y[áÎeöp\"ÁÈuY,ülÍ/qZFB&TãEÖ'dmwK9zÀ;íÚVSõPOkJjêà~6Ü+Iû#î@1äRn7<v]H=ò\\fèËúGcx4ÒÛ.`ÊçÕ>!Q)°U?*óÏÓ{Ì_Ã8LÄ-ëAÔWÙù|(h%ôN2XÇt3MÂ5D0";
        var result = service.descrypt(crypted, publicKey);

        System.out.println(result);
    }

    @Test
    public void testCryptText(){
        CryptService service = new CryptService();
        var publickey = service.getPublicKey();
        var text = "{\"addicionalInfo\":\"Zizinha ♥️\",\"bounds\":{\"bottom\":992,\"left\":0,\"right\":198,\"top\":805},\"className\":\"android.widget.FrameLayout\",\"viewID\":10}";
        System.out.println(publickey.length());
        var result = service.encripty(text, publickey);
        System.out.println(result);
        System.out.println(publickey);
    }

    @Test
    public void testGetKey(){
        CryptService service = new CryptService();

        var key = service.getPublicKey();
        System.out.println(key);
    }


}
