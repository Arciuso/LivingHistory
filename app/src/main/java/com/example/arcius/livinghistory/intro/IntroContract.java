package com.example.arcius.livinghistory.intro;

public interface IntroContract {

    interface View {

        interface Intro {
            void showIntroText();
            void hideIntroText();
            void showIntroSettings();
        }

        interface Settings {
            void setPresenter(IntroContract.Presenter.Settings presenter);
            void showDateText(String date);
            void showYear(String year);
            void showDays(String days);
            void showDaysText(String text);
            void showMain();
        }
    }


    interface Presenter {

        interface Settings {
            void start();
            void incYear();
            void decYear();
        }
    }
}
