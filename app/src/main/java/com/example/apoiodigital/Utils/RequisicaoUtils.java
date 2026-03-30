package com.example.apoiodigital.Utils;

import com.example.apoiodigital.Dto.ListRequisicaoRequestDTO;
import com.example.apoiodigital.Dto.RequisicaoDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class RequisicaoUtils {

    //Separação superficial de teste
    public Map<String, List<String>> separarDatas(List<String> datas) {
        Map<String, List<String>> result = new HashMap<>();
        result.put("hoje", new ArrayList<>());
        result.put("ontem", new ArrayList<>());
        result.put("semanaPassada", new ArrayList<>());

        for(int i = 0; i< datas.size(); ++i){
            if(i==0){
                result.get("hoje").add(datas.get(i));
            }
            else if(i>0 && i< 3){
                result.get("ontem").add(datas.get(i));
            }
            else {
                result.get("semanaPassada").add(datas.get(i));
            }
        }

        return result;
    }

    public static Map<String, List<RequisicaoDTO>> separarDatasAPI(ListRequisicaoRequestDTO datas) {
        Map<String, List<RequisicaoDTO>> result = new LinkedHashMap<>();
        result.put("hoje", new ArrayList<>());
        result.put("ontem", new ArrayList<>());
        result.put("estaSemana", new ArrayList<>());
        result.put("semanaPassada", new ArrayList<>());
        result.put("maisAntigo", new ArrayList<>());

        SimpleDateFormat sdfServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        sdfServer.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdfReq = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

        Calendar hoje = Calendar.getInstance();
        try {
            String serverTime = datas.getTimestamp();
            if (serverTime.contains(".")) {
                serverTime = serverTime.substring(0, 23) + "Z";
            }
            Date serverDate = sdfServer.parse(serverTime);
            hoje.setTime(serverDate);
        } catch (ParseException e) {
            e.printStackTrace();
            hoje = Calendar.getInstance();
        }
        zerarHora(hoje);

        Calendar ontem = (Calendar) hoje.clone();
        ontem.add(Calendar.DAY_OF_YEAR, -1);

        Calendar inicioSemanaAtual = (Calendar) hoje.clone();
        inicioSemanaAtual.setFirstDayOfWeek(Calendar.SUNDAY);
        inicioSemanaAtual.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        zerarHora(inicioSemanaAtual);

        Calendar fimSemanaAtual = (Calendar) inicioSemanaAtual.clone();
        fimSemanaAtual.add(Calendar.DAY_OF_YEAR, 6);
        zerarHora(fimSemanaAtual);

        Calendar inicioSemanaPassada = (Calendar) inicioSemanaAtual.clone();
        inicioSemanaPassada.add(Calendar.WEEK_OF_YEAR, -1);
        zerarHora(inicioSemanaPassada);

        Calendar fimSemanaPassada = (Calendar) inicioSemanaPassada.clone();
        fimSemanaPassada.add(Calendar.DAY_OF_YEAR, 6);
        zerarHora(fimSemanaPassada);

        for (RequisicaoDTO dto : datas.getRequisicoes()) {
            try {
                Date dataReq = sdfReq.parse(dto.getTimeStamp());
                Calendar calReqDia = Calendar.getInstance();
                calReqDia.setTime(dataReq);
                zerarHora(calReqDia);

                if (isSameDay(calReqDia, hoje)) {
                    result.get("hoje").add(dto);
                } else if (isSameDay(calReqDia, ontem)) {
                    result.get("ontem").add(dto);
                } else if (!calReqDia.before(inicioSemanaAtual) && !calReqDia.after(fimSemanaAtual)) {
                    result.get("estaSemana").add(dto);
                } else if (!calReqDia.before(inicioSemanaPassada) && !calReqDia.after(fimSemanaPassada)) {
                    result.get("semanaPassada").add(dto);
                } else {
                    result.get("maisAntigo").add(dto);
                }

            } catch (ParseException e) {
                e.printStackTrace();
                result.get("maisAntigo").add(dto);
            }
        }

        return result;
    }
    private static void zerarHora(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private static boolean isSameDay(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }
}
