package org.medfordhistorical.overthemystic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.over_the_mystic_logo)
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect with us")
                .addEmail("overthemysticapp@gmail.com")
                .addWebsite("http://medium.com/over-the-mystic")
                .addPlayStore("org.medfordhistorical.overthemystic")
                .setDescription("A historical tour of the Medford, MA area. " +
                        "Made by Tufts Students: Monsurat Olaosebikan, Rebecca Alpert, and Anna Kasawaga")
                .create();

        setContentView(aboutPage);

    }
}
