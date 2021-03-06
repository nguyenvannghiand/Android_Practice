package com.nghianv.musiclibrary.common;

import com.nghianv.musiclibrary.model.Song;

import java.util.List;

public class Common {
	private static Common sInstance;

	public static Common getInstance() {
		if (sInstance == null) {
			sInstance = new Common();
		}
		return sInstance;
	}
	//Key of Mediaplayer
	public static int STATUS_IDLE = 1;
	public static int STATUS_PLAYING = 2;
	public static int STATUS_PAUSE = 3;
	public static int STATUS_STOP = 4;

	//Key
	public static final String KEY_ALBUM_ID = "KEY_ALBUM_ID";
	public static final String KEY_ALBUM_NAME = "KEY_ALBUM_NAME";
	public static final String KEY_ALBUM_ARTIST = "KEY_ALBUM_ARTIST";
	public static final String KEY_ID_ARTIST = "KEY_ID_ARTIST";
	public static final String KEY_NAME_ARTIST = "KEY_NAME_ARTIST";

	private List<Song> listSongDetail;

	public List<Song> getListSongDetail() {
		return listSongDetail;
	}

	public void setListSongDetail(List<Song> listSongDetail) {
		this.listSongDetail = listSongDetail;
	}
}
