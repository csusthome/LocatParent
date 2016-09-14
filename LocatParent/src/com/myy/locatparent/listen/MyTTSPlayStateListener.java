package com.myy.locatparent.listen;

import com.baidu.navisdk.adapter.BaiduNaviManager.TTSPlayStateListener;

public class MyTTSPlayStateListener implements TTSPlayStateListener{
	 @Override
     public void playEnd() {
//         showToastMsg("TTSPlayStateListener : TTS play end");
     }
	 
     @Override
     public void playStart() {
//         showToastMsg("TTSPlayStateListener : TTS play start");
     }
}
