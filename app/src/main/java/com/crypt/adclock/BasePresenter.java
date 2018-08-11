package com.crypt.adclock;

/**
 * Created by Ghito on 08-Mar-18.
 */

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();

}
