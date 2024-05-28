package com.github.eqs;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.util.ArrayList;


class GameAudio {

    /** 音を保存する配列 */
    private  AudioClip[] sounds;

    public GameAudio() {
        /*
           ArrayList<AudioClip> list = new ArrayList<AudioClip>(0);

           list.add(Applet.newAudioClip(getClass().getResource("./wav/bom06.wav")));

           sounds = list.toArray(new AudioClip[]{});
           */

        sounds = new AudioClip[3];
        sounds[0] = Applet.newAudioClip(getClass().getResource("./wav/bom06.wav"));
        sounds[1] = Applet.newAudioClip(getClass().getResource("./wav/don07.wav"));
    }

    public void play(int n) {
        if (sounds[n] != null) {
            sounds[n].play();
        }
    }
}
