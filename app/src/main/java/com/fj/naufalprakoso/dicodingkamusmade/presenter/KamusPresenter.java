package com.fj.naufalprakoso.dicodingkamusmade.presenter;

import android.content.Context;

import com.fj.naufalprakoso.dicodingkamusmade.database.KamusDataHelper;
import com.fj.naufalprakoso.dicodingkamusmade.model.Kamus;
import com.fj.naufalprakoso.dicodingkamusmade.view.KamusView;

import java.util.List;

/**
 * Created by NaufalPrakoso on 13/03/18.
 */

public class KamusPresenter {

    private KamusView view;
    private Context context;

    public KamusPresenter(KamusView view, Context context) {
        this.view = view;
        this.context = context;
    }

    public void loadData(boolean indo) {
        KamusDataHelper dataHelper = new KamusDataHelper(context);
        dataHelper.open();
        List<Kamus> results;
        if (indo) {
            results = dataHelper.getBahasaByNama();
        } else {
            results = dataHelper.getEnglishByNama();
        }

        view.onLoadDataSuccess(results);
        dataHelper.close();
    }
}
