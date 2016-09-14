package com.myy.locatparent.listen;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.myy.locatparent.utils.LogUtils;

public class MyTTSPlayerCallback implements BNOuterTTSPlayerCallback{
	@Override
	public void stopTTS() {
		LogUtils.e("test_TTS", "stopTTS");
	}

	@Override
	public void resumeTTS() {
		LogUtils.e("test_TTS", "resumeTTS");
	}

	@Override
	public void releaseTTSPlayer() {
		LogUtils.e("test_TTS", "releaseTTSPlayer");
	}

	@Override
	public int playTTSText(String speech, int bPreempt) {
		LogUtils.e("test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);
		return 1;
	}

	@Override
	public void phoneHangUp() {
		LogUtils.e("test_TTS", "phoneHangUp");
	}

	@Override
	public void phoneCalling() {
		LogUtils.e("test_TTS", "phoneCalling");
	}
	
	@Override
	public void pauseTTS() {
		LogUtils.e("test_TTS", "pauseTTS");
	}

	@Override
	public void initTTSPlayer() {
		LogUtils.e("test_TTS", "initTTSPlayer");
	}

	@Override
	public int getTTSState() {
		LogUtils.e("test_TTS", "getTTSState");
		return 1;
	}
	
}
